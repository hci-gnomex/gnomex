package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.billing.IScanChipPlugin;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductType;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequisitionFormUtil;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SaveProductOrder extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProductOrder.class);

  private String    productListXMLString;
  private Integer   idBillingAccount;
  private Integer   idAppUser;
  private Integer   idLab;
  private BillingPeriod   billingPeriod;
  private Integer   idCoreFacility;
  private Document  productDoc;
  private String    codeProductOrderStatus;

  private IScanChipPlugin iscanPlugin = new IScanChipPlugin();

  private String    appURL;
  private String    serverName;



  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    try {
      appURL = this.getAppURL(request);
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveProductOrder", e);
    }

    serverName = request.getServerName();
    if(request.getParameter("idBillingAccount") != null && !request.getParameter("idBillingAccount").equals("")) {
      idBillingAccount = Integer.parseInt(request.getParameter("idBillingAccount"));
    } else {
      this.addInvalidField("idBillingAccount", "Missing idBillingAccount");
    }

    if(request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
      idAppUser = Integer.parseInt(request.getParameter("idAppUser"));
    } else {
      this.addInvalidField("idAppUser", "Missing idAppUser");
    }

    if(request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
      idCoreFacility = Integer.parseInt(request.getParameter("idCoreFacility"));
    } else {
      this.addInvalidField("idCoreFacility", "Missing idCoreFacility");
    }

    if(request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.parseInt(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "Missing idLab");
    }

    if(request.getParameter("codeProductOrderStatus") != null && !request.getParameter("codeProductOrderStatus").equals("")) {
      codeProductOrderStatus = request.getParameter("codeProductOrderStatus");
    } else {
      this.addInvalidField("codeProductOrderStatus", "Missing codeProductOrderStatus");
    }


    if(request.getParameter("productListXMLString") != null && !request.getParameter("productListXMLString").equals("")) {
      productListXMLString = request.getParameter("productListXMLString");
    } else {
      this.addInvalidField("productListXMLString", "Missing productListXMLString");
    }

    StringReader reader = new StringReader(productListXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      productDoc = sax.build(reader);
    } catch (JDOMException je ) {
      log.error( "Cannot parse producListXMLString", je );
      this.addInvalidField( "productListXMLString", "Invalid producList xml");
    }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    try {
      if(this.isValid()) {
        sess = HibernateSession.currentSession(this.getUsername());
        billingPeriod = DictionaryHelper.getInstance(sess).getCurrentBillingPeriod();
        Lab lab = DictionaryHelper.getInstance(sess).getLabObject(idLab);
        HashMap<String, ArrayList<Element>> productTypes = new HashMap<String, ArrayList<Element>>();

        for (Iterator i = productDoc.getRootElement().getChildren().iterator(); i.hasNext(); ) {
          Element n = (Element) i.next();
          if(n.getAttribute("quantity") == null || n.getAttribute("quantity").equals("") || n.getAttribute("quantity").equals("0") ) {
            continue;
          }
          if(!productTypes.containsKey(n.getAttributeValue("codeProductType"))) {
            ArrayList<Element> products = new ArrayList<Element>();
            products.add(n);
            productTypes.put(n.getAttributeValue("codeProductType"), products);
          } else {
            ArrayList<Element> existingList = productTypes.get(n.getAttributeValue("codeProductType"));
            existingList.add(n);
            productTypes.put(n.getAttributeValue("codeProductType"), existingList);
          }
        }

        for(Iterator i = productTypes.keySet().iterator(); i.hasNext();) {
          String codeProductTypeKey = (String)i.next();
          ProductType productType = (ProductType)sess.load(ProductType.class, codeProductTypeKey);
          PriceCategory priceCategory = (PriceCategory)sess.load(PriceCategory.class, productType.getIdPriceCategory());
          ArrayList<Element> products = productTypes.get(codeProductTypeKey);
          Set productLineItems = new TreeSet();
          ProductOrder po = new ProductOrder();
          
          if(products.size() > 0) {
            initializeProductOrder(po, codeProductTypeKey);
            sess.save(po);
            po.setProductOrderNumber( getNextPONumber(po, sess) );
            
            for(Element n : products) {
              if(n.getAttributeValue("isSelected").equals("Y") && n.getAttributeValue("quantity") != null && !n.getAttributeValue("quantity").equals("") && !n.getAttributeValue("quantity").equals("0")) {
                ProductLineItem pi = new ProductLineItem();
                Price p = (Price)sess.load(Price.class, Integer.parseInt(n.getAttributeValue("idPrice")));

                initializeProductLineItem(pi, po.getIdProductOrder(), n, p.getEffectiveUnitPrice(lab));
                productLineItems.add(pi);
                sess.save(pi);
              }
            }

            sess.save(po);

            List billingItems = iscanPlugin.constructBillingItems(sess, billingPeriod, priceCategory, po, productLineItems);
            for(Iterator j = billingItems.iterator(); j.hasNext();) {
              BillingItem bi = (BillingItem)j.next();
              sess.save(bi);
            }

            boolean isHCI = lab.getContactEmail().indexOf( "@hci.utah.edu" ) > 0;
            if(po.getCodeProductType().equals(ProductType.TYPE_ISCAN_CHIP) && !isHCI && !lab.isExternalLab()) {
              // REQUISITION FORM
              try {
                // Download and fill out requisition form
                File reqFile = RequisitionFormUtil.saveReqFileFromURL(po, sess, serverName);
                reqFile = RequisitionFormUtil.populateRequisitionForm( po, reqFile, sess );
                if ( reqFile == null ) {
                  String msg = "Unable to download requisition form for product order " + po.getIdProductOrder() + "." ;
                  System.out.println(msg);
                } else {
                  sendIlluminaEmail(sess, po);
                }

              } catch (Exception e) {
                String msg = "Unable to download requisition form OR unable to send Illumina email for Request "
                  + po.getIdProductOrder()
                  + ".  " + e.toString();
                System.out.println(msg);
                e.printStackTrace();
              }
            }
          }
        }

        sess.flush();
        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions",
        "Insufficient permission to create product orders.");
        setResponsePage(this.ERROR_JSP);
      }


    }  catch (Exception e){
      log.error("An exception has occurred while emailing in SaveRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.toString());
    } finally {
      try {
        HibernateSession.closeTomcatSession();
        HibernateSession.closeSession();
      } catch(Exception e) {

      }
    }

    return this;
  }

  public static String getNextPONumber(ProductOrder po, Session sess) throws SQLException {
    String poNumber = "";
    String procedure = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(
        po.getIdCoreFacility(),
        PropertyDictionary.GET_PO_NUMBER_PROCEDURE);
    if (procedure != null && procedure.length() > 0) {
      Connection con = sess.connection();
      String queryString = "";
      if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
        queryString = "exec " + procedure;
      } else {
        queryString = "call " + procedure;
      }
      SQLQuery query = sess.createSQLQuery(queryString);
      List l = query.list();
      if (l.size() != 0) {
        Object o = l.get(0);
        if (o.getClass().equals(String.class)) {
          poNumber = (String)o;
          poNumber = poNumber.toUpperCase();
        }
      }
    }
    if (poNumber.length() == 0) {
      poNumber = po.getIdProductOrder().toString();
    }

    return poNumber;
  }

  private void initializeProductOrder(ProductOrder po, String codeProductType) {
    po.setSubmitDate(new Date(System.currentTimeMillis()));
    po.setCodeProductType(codeProductType);
    po.setQuoteNumber("");
    po.setUuid(UUID.randomUUID().toString());
    po.setIdAppUser(idAppUser);
    po.setIdCoreFacility(idCoreFacility);
    po.setIdBillingAccount(idBillingAccount);
    po.setIdLab(idLab);

  }

  private void initializeProductLineItem(ProductLineItem pi, Integer idProductOrder, Element n, BigDecimal unitPrice) {
    pi.setIdProductOrder(idProductOrder);
    pi.setIdProduct(Integer.parseInt(n.getAttributeValue("idProduct")));
    pi.setQty(Integer.parseInt(n.getAttributeValue("quantity")));
    pi.setUnitPrice(unitPrice);
    pi.setCodeProductOrderStatus(codeProductOrderStatus);
  }

  private boolean sendIlluminaEmail(Session sess, ProductOrder po) throws NamingException, MessagingException {

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

    CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, idCoreFacility);

    StringBuffer emailBody = new StringBuffer();

    String uuidStr = po.getUuid();
    String uploadQuoteURL = appURL + "/" + Constants.UPLOAD_QUOTE_JSP + "?orderUuid=" + uuidStr ;

    emailBody.append("A request for iScan chips has been submitted from the " +
        cf.getFacilityName() +
    " core.");

    emailBody.append("<br><br><table border='0' width = '600'>");
    for(Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem pi = (ProductLineItem)i.next();
      Product p = (Product)sess.load(Product.class, pi.getIdProduct());

      emailBody.append("<tr><td>Chip Type:</td><td>" + p.getName() + "</td></tr>" );
      emailBody.append("<tr><td>Catalog Number:</td><td>" + p.getCatalogNumber() + "</td></tr>");

      if(i.hasNext()) {
        emailBody.append("<br><br>");
      }

    }

    emailBody.append("</td></tr></table><br><br>To enter a quote number and upload a file, click <a href=\"" + uploadQuoteURL + "\">" + Constants.APP_NAME + " - Upload Quote Info</a>.");

    String subject = "Request for Quote Number for iScan Chips";

    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    String contactEmailIlluminaRep  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_ILLUMINA_REP);

    String senderEmail = contactEmailCoreFacility;

    String contactEmail = contactEmailIlluminaRep;
    String ccEmail = null;


    String emailInfo = "";
    boolean send = false;

    if(contactEmail.contains(",")){
      for(String e: contactEmail.split(",")){
        if(!MailUtil.isValidEmail(e.trim())){
          log.error("Invalid email address: " + e);
        }
      }
    } else{
      if(!MailUtil.isValidEmail(contactEmail)){
        log.error("Invalid email address: " + contactEmail);
      }
    }
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      send = true;
      subject = subject + " (TEST)";
      contactEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmailIlluminaRep + "]<br><br>";
      ccEmail = null;
    }

    if (send) {
      if(!MailUtil.isValidEmail(senderEmail)){
        senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      try {
        MailUtil.send(contactEmail,
            ccEmail,
            senderEmail,
            subject,
            emailInfo + emailBody.toString(),
            true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return send;
  }

  public void validate() {
  }

}
