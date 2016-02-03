package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.billing.IScanChipPlugin;
import hci.gnomex.constants.Constants;
//import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderStatus;
import hci.gnomex.model.ProductType;
import hci.gnomex.model.PropertyDictionary;
//import hci.gnomex.model.RequestCategory;
//import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
//import hci.gnomex.utility.HibernateUtil;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
//import hci.gnomex.utility.RequisitionFormUtil;

//import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

//import hci.framework.control.Command;
//import hci.framework.control.RollBackCommandException;
//import hci.gnomex.billing.IScanChipPlugin;
//import hci.gnomex.constants.Constants;
//import hci.gnomex.model.BillingItem;
//import hci.gnomex.model.BillingPeriod;
//import hci.gnomex.model.CoreFacility;
//import hci.gnomex.model.Lab;
//import hci.gnomex.model.Price;
//import hci.gnomex.model.PriceCategory;
//import hci.gnomex.model.Product;
//import hci.gnomex.model.ProductLineItem;
//import hci.gnomex.model.ProductOrder;
//import hci.gnomex.model.ProductType;
//import hci.gnomex.model.PropertyDictionary;
//import hci.gnomex.utility.DictionaryHelper;
//import hci.gnomex.utility.HibernateSession;
//import hci.gnomex.utility.HibernateUtil;
//import hci.gnomex.utility.MailUtil;
//import hci.gnomex.utility.MailUtilHelper;
//import hci.gnomex.utility.PropertyDictionaryHelper;

public class SaveProductOrder extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProductOrder.class);

  private String productListXMLString;
  private Integer idBillingAccount;
  private Integer idAppUser;
  private Integer idLab;
  private BillingPeriod billingPeriod;
  private Integer idCoreFacility;
  private Document productDoc;
  private String codeProductOrderStatus;

  private IScanChipPlugin iscanPlugin = new IScanChipPlugin();

  private String appURL;
  private String serverName;

  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    try {
      appURL = this.getAppURL(request);
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveProductOrder", e);
    }

    serverName = request.getServerName();
    if (request.getParameter("idBillingAccount") != null && !request.getParameter("idBillingAccount").equals("")) {
      idBillingAccount = Integer.parseInt(request.getParameter("idBillingAccount"));
    } else {
      this.addInvalidField("idBillingAccount", "Missing idBillingAccount");
    }

    if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
      idAppUser = Integer.parseInt(request.getParameter("idAppUser"));
    } else {
      this.addInvalidField("idAppUser", "Missing idAppUser");
    }

    if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
      idCoreFacility = Integer.parseInt(request.getParameter("idCoreFacility"));
    } else {
      this.addInvalidField("idCoreFacility", "Missing idCoreFacility");
    }

    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.parseInt(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "Missing idLab");
    }

    if (request.getParameter("codeProductOrderStatus") != null && !request.getParameter("codeProductOrderStatus").equals("")) {
      codeProductOrderStatus = request.getParameter("codeProductOrderStatus");
    } else {
      this.addInvalidField("codeProductOrderStatus", "Missing codeProductOrderStatus");
    }

    if (request.getParameter("productListXMLString") != null && !request.getParameter("productListXMLString").equals("")) {
      productListXMLString = request.getParameter("productListXMLString");
    } else {
      this.addInvalidField("productListXMLString", "Missing productListXMLString");
    }

    StringReader reader = new StringReader(productListXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      productDoc = sax.build(reader);
    } catch (JDOMException je) {
      log.error("Cannot parse producListXMLString", je);
      this.addInvalidField("productListXMLString", "Invalid producList xml");
    }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    try {
      if (this.isValid()) {
        sess = HibernateSession.currentSession(this.getUsername());
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        Element root = new Element("SUCCESS");
        Document outputDoc = new Document(root);

        billingPeriod = DictionaryHelper.getInstance(sess).getCurrentBillingPeriod();
        Lab lab = DictionaryHelper.getInstance(sess).getLabObject(idLab);
        HashMap<Integer, ArrayList<Element>> productTypes = new HashMap<Integer, ArrayList<Element>>();

        for (Iterator i = productDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
          Element n = (Element) i.next();
          if (n.getAttribute("quantity") == null || n.getAttributeValue("quantity").equals("") || n.getAttributeValue("quantity").equals("0")) {
            continue;
          }
          if (!productTypes.containsKey(Integer.parseInt(n.getAttributeValue("idProductType")))) {
            ArrayList<Element> products = new ArrayList<Element>();
            products.add(n);
            productTypes.put(Integer.parseInt(n.getAttributeValue("idProductType")), products);
          } else {
            ArrayList<Element> existingList = productTypes.get(Integer.parseInt(n.getAttributeValue("idProductType")));
            existingList.add(n);
            productTypes.put(Integer.parseInt(n.getAttributeValue("idProductType")), existingList);
          }
        }

        for (Iterator i = productTypes.keySet().iterator(); i.hasNext();) {
          Integer idProductTypeKey = (Integer) i.next();
          ProductType productType = sess.load(ProductType.class, idProductTypeKey);
          PriceCategory priceCategory = sess.load(PriceCategory.class, productType.getIdPriceCategory());
          ArrayList<Element> products = productTypes.get(idProductTypeKey);
          Set<ProductLineItem> productLineItems = new TreeSet<ProductLineItem>(new ProductLineItemComparator());

          ProductOrder po = new ProductOrder();

          if (products.size() > 0) {
            initializeProductOrder(po, idProductTypeKey);
            sess.save(po);
            po.setProductOrderNumber(getNextPONumber(po, sess));

            for (Element n : products) {
              if (n.getAttributeValue("isSelected").equals("Y") && n.getAttributeValue("quantity") != null && !n.getAttributeValue("quantity").equals("") && !n.getAttributeValue("quantity").equals("0")) {
                ProductLineItem pi = new ProductLineItem();
                Price p = sess.load(Price.class, Integer.parseInt(n.getAttributeValue("idPrice")));

                initializeProductLineItem(pi, po.getIdProductOrder(), n, p.getEffectiveUnitPrice(lab));
                productLineItems.add(pi);
                sess.save(pi);
              }
            }
            po.setProductLineItems(productLineItems);

            sess.save(po);
            sess.flush();
            sess.refresh(po);

            sendConfirmationEmail(sess, po, ProductOrderStatus.NEW, serverName);

            Element poNode = new Element("ProductOrder");

            String submitter = dh.getAppUserObject(po.getIdAppUser()).getDisplayName();
            String orderStatus = po.getStatus();

            poNode.setAttribute("display", po.getDisplay());
            poNode.setAttribute("submitter", submitter);
            poNode.setAttribute("submitDate", po.getSubmitDate().toString());
            poNode.setAttribute("status", orderStatus);
            poNode.setAttribute("idLab", idLab == null ? "" : idLab.toString());
            poNode.setAttribute("idProductOrder", po.getIdProductOrder() != null ? po.getIdProductOrder().toString() : "");
            outputDoc.getRootElement().addContent(poNode);

            List<BillingItem> billingItems = iscanPlugin.constructBillingItems(sess, billingPeriod, priceCategory, po, productLineItems);
            for (Iterator<BillingItem> j = billingItems.iterator(); j.hasNext();) {
              BillingItem bi = j.next();
              sess.save(bi);
            }

            boolean isHCI = lab.getContactEmail().indexOf("@hci.utah.edu") > 0;
            // TODO : Make this a property on Product Type to Link to Purchasing System
            // if (po.getIdProductType().equals(ProductType.TYPE_ISCAN_CHIP) && !isHCI && !lab.isExternalLab()) {
            // // REQUISITION FORM
            // try {
            // // Download and fill out requisition form
            // File reqFile = RequisitionFormUtil.saveReqFileFromURL(po, sess, serverName);
            // reqFile = RequisitionFormUtil.populateRequisitionForm(po, reqFile, sess);
            // if (reqFile == null) {
            // String msg = "Unable to download requisition form for product order " + po.getIdProductOrder() + ".";
            // System.out.println(msg);
            // } else {
            // sendIlluminaEmail(sess, po);
            // }
            //
            // } catch (Exception e) {
            // String msg = "Unable to download requisition form OR unable to send Illumina email for Request " + po.getIdProductOrder() + ".  " + e.toString();
            // System.out.println(msg);
            // e.printStackTrace();
            // }
            // }
          }
        }

        sess.flush();

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        out.setOmitEncoding(true);
        this.xmlResult = out.outputString(outputDoc);
        this.setResponsePage(SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to create product orders.");
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e) {
      log.error("An exception has occurred while emailing in SaveRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.toString());
    } finally {
      try {
        HibernateSession.closeTomcatSession();
        HibernateSession.closeSession();
      } catch (Exception e) {

      }
    }

    return this;
  }

  public static void sendConfirmationEmail(Session sess, ProductOrder po, String orderStatus, String serverName) throws NamingException, MessagingException, IOException {

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
    CoreFacility cf = sess.load(CoreFacility.class, po.getIdCoreFacility());

    // we have to load these from the id's b/c the po is currently loaded in this session and
    // therefore a sess.get() or sess.load() won't return a new po with these fields initialized,
    // it will just return the current po object that is recorded in this session which is just a bare bones version
    // since we haven't gone back to the DB yet.
    // AppUser au = sess.load(AppUser.class, po.getIdAppUser());
    // Lab l = sess.load(Lab.class, po.getIdLab());

    String subject = "";
    if (orderStatus.equals(ProductOrderStatus.NEW)) {
      subject = "Product Order " + po.getProductOrderNumber() + " has been submitted.";
    } else if (orderStatus.equals(ProductOrderStatus.COMPLETED)) {
      subject = "Product Order " + po.getProductOrderNumber() + " has been completed.";
    }
    String contactEmailCoreFacility = cf.getContactEmail() != null ? cf.getContactEmail() : "";
    String contactEmailAppUser = po.getSubmitter().getEmail() != null ? po.getSubmitter().getEmail() : "";
    String fromAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    String noAppUserEmailMsg = "";

    String toAddress = contactEmailCoreFacility + "," + contactEmailAppUser;

    BillingAccount ba = sess.load(BillingAccount.class, po.getIdBillingAccount());
    ProductType pt = sess.load(ProductType.class, po.getIdProductType());

    StringBuffer products = new StringBuffer();
    for (Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem pli = (ProductLineItem) i.next();
      Product p = sess.load(Product.class, pli.getIdProduct());
      products.append(p.getDisplay() + "(Qty: " + pli.getQty() + "), ");
    }
    products.replace(products.lastIndexOf(","), products.lastIndexOf(",") + 1, "");

    if (!MailUtil.isValidEmail(contactEmailAppUser)) {
      noAppUserEmailMsg = "The user who submitted this product order did not receive a copy of this confirmation because they do not have a valid email on file.\n";
    }

    // If no valid to address then send to gnomex support team
    if (!MailUtil.isValidEmail(toAddress)) {
      toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }

    StringBuffer body = new StringBuffer();
    if (orderStatus.equals(ProductOrderStatus.NEW)) {
      body.append("Product Order " + po.getProductOrderNumber() + " has been submitted to the " + cf.getFacilityName() + ".\n\n");
    } else if (orderStatus.equals(ProductOrderStatus.COMPLETED)) {
      body.append("Product Order " + po.getProductOrderNumber() + " has been completed and the products are ready for your use.\n\n");
    }
    body.append("Product Order #: \t\t" + po.getProductOrderNumber() + "\n");
    body.append("Products Ordered: \t\t" + products.toString() + "\n");
    body.append("Product Type: \t\t" + pt.getDisplay() + "\n");
    body.append("Submit Date: \t\t\t" + po.getSubmitDate() + "\n");
    body.append("Submitted By: \t\t" + po.getSubmitter().getDisplayName() + "\n");
    body.append("Lab: \t\t\t\t" + po.getLab().getName(false, true) + "\n");
    body.append("Billing Acct: \t\t" + ba.getAccountNameAndNumber() + "\n");
    body.append(noAppUserEmailMsg);

    MailUtilHelper mailHelper = new MailUtilHelper(toAddress, fromAddress, subject, body.toString(), null, false, dictionaryHelper, serverName);

    MailUtil.validateAndSendEmail(mailHelper);

  }

  public static String getNextPONumber(ProductOrder po, Session sess) throws SQLException {
    String poNumber = "";
    String procedure = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(po.getIdCoreFacility(), PropertyDictionary.GET_PO_NUMBER_PROCEDURE);
    if (procedure != null && procedure.length() > 0) {
      SessionImpl sessionImpl = (SessionImpl) sess;   	  
      Connection con =  sessionImpl.connection();   
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
          poNumber = (String) o;
          poNumber = poNumber.toUpperCase();
        }
      }
    }
    if (poNumber.length() == 0) {
      poNumber = po.getIdProductOrder().toString();
    }

    return poNumber;
  }

  private void initializeProductOrder(ProductOrder po, Integer idProductType) {
    po.setSubmitDate(new Date(System.currentTimeMillis()));
    po.setIdProductType(idProductType);
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

    CoreFacility cf = sess.load(CoreFacility.class, idCoreFacility);

    StringBuffer emailBody = new StringBuffer();

    String uuidStr = po.getUuid();
    String uploadQuoteURL = appURL + "/" + Constants.UPLOAD_QUOTE_JSP + "?orderUuid=" + uuidStr;

    emailBody.append("A request for iScan chips has been submitted from the " + cf.getFacilityName() + " core.");

    emailBody.append("<br><br><table border='0' width = '600'>");
    for (Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem pi = (ProductLineItem) i.next();
      Product p = sess.load(Product.class, pi.getIdProduct());

      emailBody.append("<tr><td>Chip Type:</td><td>" + p.getName() + "</td></tr>");
      emailBody.append("<tr><td>Catalog Number:</td><td>" + p.getCatalogNumber() + "</td></tr>");

      if (i.hasNext()) {
        emailBody.append("<br><br>");
      }

    }

    emailBody.append("</td></tr></table><br><br>To enter a quote number and upload a file, click <a href=\"" + uploadQuoteURL + "\">" + Constants.APP_NAME + " - Upload Quote Info</a>.");

    String subject = "Request for Quote Number for iScan Chips";

    String contactEmailCoreFacility = cf.getContactEmail();
    String contactEmailIlluminaRep = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_ILLUMINA_REP);

    String senderEmail = contactEmailCoreFacility;

    String contactEmail = contactEmailIlluminaRep;
    String ccEmail = null;

    if (contactEmail.contains(",")) {
      for (String e : contactEmail.split(",")) {
        if (!MailUtil.isValidEmail(e.trim())) {
          log.error("Invalid email address: " + e);
        }
      }
    } else {
      if (!MailUtil.isValidEmail(contactEmail)) {
        log.error("Invalid email address: " + contactEmail);
      }
    }

    if (!MailUtil.isValidEmail(senderEmail)) {
      senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }

    boolean sent = false;
    try {
      MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBody.toString(), null, true, dictionaryHelper, serverName);
      sent = MailUtil.validateAndSendEmail(helper);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return sent;
  }

  public class ProductLineItemComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      ProductLineItem li1 = (ProductLineItem) o1;
      ProductLineItem li2 = (ProductLineItem) o2;
      return li1.getIdProduct().compareTo(li2.getIdProduct());

    }
  }

  public void validate() {
  }

}
