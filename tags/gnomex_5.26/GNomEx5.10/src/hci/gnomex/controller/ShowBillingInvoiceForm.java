package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.security.SecurityAdvisor;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.BillingInvoiceHTMLFormatter;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class ShowBillingInvoiceForm extends GNomExCommand implements Serializable {

  private static final String     ACTION_SHOW  = "show";
  private static final String     ACTION_EMAIL = "email";
  public static final String      DISK_USAGE_NUMBER_PREFIX = "ZZZZDiskUsage"; // Leading Z's to make it sort last.

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowBillingInvoiceForm.class);  

  public String SUCCESS_JSP = "/getHTML.jsp";

  private String           serverName;

  private Integer          idLab;
  private Integer          idBillingAccount;
  private Integer          idBillingPeriod;
  private Integer          idCoreFacility;
  private String           action = "show";
  private String           emailAddress = null;
  private Boolean          respondInHTML = false;
  private String           idLabs;
  private String           idBillingAccounts;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idLabs") != null && !request.getParameter("idLabs").equals("")) {
      idLabs = request.getParameter("idLabs");

      if(request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")){
        idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
      } else {
        this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
      }
      
      if(request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")){
        idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
      } else {
        this.addInvalidField("idCoreFacility", "idCoreFacility is required");
      }
      
      if(request.getParameter("idBillingAccounts") != null && !request.getParameter("idBillingAccounts").equals("")){
        idBillingAccounts = request.getParameter("idBillingAccounts");
      }

      if (request.getParameter("action") != null && !request.getParameter("action").equals("")) {
        action = request.getParameter("action");
      }
    }

    if(idLabs == null){
      if (request.getParameter("idLab") != null) {
        idLab = new Integer(request.getParameter("idLab"));
      } else {
        this.addInvalidField("idLab", "idLab is required");
      }
      if (request.getParameter("idBillingAccount") != null) {
        idBillingAccount = new Integer(request.getParameter("idBillingAccount"));
      } else {
        this.addInvalidField("idBillingAccount", "idBillingAccount is required");
      }
      if (request.getParameter("idBillingPeriod") != null) {
        idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
      } else {
        this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
      }
      if (request.getParameter("idCoreFacility") != null) {
        idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
      } else {
        this.addInvalidField("idCoreFacility", "idCoreFacility is required");
      }
      if (request.getParameter("action") != null && !request.getParameter("action").equals("")) {
        action = request.getParameter("action");
      }
      if (request.getParameter("emailAddress") != null && !request.getParameter("emailAddress").equals("")) {
        emailAddress = request.getParameter("emailAddress");
      }
      if (request.getParameter("respondInHTML") != null && request.getParameter("respondInHTML").equals("Y")) {
        respondInHTML = true;
      }
    }
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {

    try {


      Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());


      DictionaryHelper dh = DictionaryHelper.getInstance(sess);



      if (this.isValid()) {
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 

          if(idLabs != null){
            String[] labs = idLabs.split(",");
            String[] billingAccounts = idBillingAccounts.split(",");
              if (action.equals(ACTION_SHOW)) {
                this.makeInvoiceReports(sess, labs, billingAccounts);
              }
            }

          else{
            BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);
            Lab lab = (Lab)sess.get(Lab.class, idLab);
            BillingAccount billingAccount = (BillingAccount)sess.get(BillingAccount.class, idBillingAccount);
            CoreFacility coreFacility = (CoreFacility)sess.get(CoreFacility.class, idCoreFacility);
            String queryString = "from Invoice where idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount and idCoreFacility=:idCoreFacility";
            Query query = sess.createQuery(queryString);
            query.setParameter("idBillingPeriod", billingPeriod.getIdBillingPeriod());
            query.setParameter("idBillingAccount", billingAccount.getIdBillingAccount());
            query.setParameter("idCoreFacility", coreFacility.getIdCoreFacility());
            Invoice invoice = (Invoice)query.uniqueResult();

            TreeMap requestMap = new TreeMap();
            TreeMap billingItemMap = new TreeMap();
            TreeMap relatedBillingItemMap = new TreeMap();
            cacheBillingItemMap(sess, this.getSecAdvisor(), idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);


            if (action.equals(ACTION_SHOW)) {
              this.makeInvoiceReport(sess, billingPeriod, lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap);
            } else if (action.equals(ACTION_EMAIL)) {
              String contactEmail = this.emailAddress;
              if (contactEmail == null) {
                contactEmail = lab.getBillingNotificationEmail();
              }
              this.sendInvoiceEmail(sess, contactEmail, coreFacility, billingPeriod, lab, billingAccount, billingItemMap, relatedBillingItemMap, requestMap);
            }

            else {
              this.addInvalidField("Insufficient permissions", "Insufficient permission to show flow cell report.");
            }
          }

          if (isValid()) {
            setResponsePage(this.SUCCESS_JSP);
          } else {
            setResponsePage(this.ERROR_JSP);
          }
        }
      }


    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowBillingInvoiceForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (NamingException e){
      log.error("An exception has occurred in ShowBillingInvoiceForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in ShowBillingInvoiceForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } catch (Exception e) {
      log.error("An exception has occurred in ShowBillingInvoiceForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeHibernateSession();    
      } catch(Exception e) {

      }
    }

    return this;
  }


  public static void cacheBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap)
  throws Exception {
    cacheRequestBillingItemMap(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
    cacheDiskUsageBillingItemMap(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
  }

  public static void cacheRequestBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap)
  throws Exception {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT req, bi ");
    buf.append("FROM   Request req ");
    buf.append("JOIN   req.billingItems bi ");
    buf.append("WHERE  bi.idLab = " + idLab + " ");
    buf.append("AND    bi.idBillingAccount = " + idBillingAccount + " ");
    buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
    buf.append("AND    bi.idCoreFacility = " + idCoreFacility + " ");
    buf.append("AND    bi.codeBillingStatus in ('" + BillingStatus.COMPLETED + "', '" + BillingStatus.APPROVED + "', '" + BillingStatus.APPROVED_PO + "')");

    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }

    buf.append("ORDER BY req.number, bi.idBillingItem ");

    List results = sess.createQuery(buf.toString()).list();


    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Request req    =  (Request)row[0];
      BillingItem bi =  (BillingItem)row[1];

      // Exclude any requests that are have
      // pending billing items.
      boolean hasPendingItems = false;
      for(Iterator i1 = req.getBillingItems().iterator(); i1.hasNext();) {
        BillingItem item = (BillingItem)i1.next();

        if (item.getIdBillingPeriod().equals(idBillingPeriod) &&
            item.getIdBillingAccount().equals(idBillingAccount)) {
          if (item.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
            hasPendingItems = true;
            break;
          }          
        }
      }
      if (hasPendingItems) {
        continue;
      }


      requestMap.put(req.getNumber(), req);

      List billingItems = (List)billingItemMap.get(req.getNumber());
      if (billingItems == null) {
        billingItems = new ArrayList();
        billingItemMap.put(req.getNumber(), billingItems);
      }
      billingItems.add(bi);
    }    

    buf = new StringBuffer();
    buf.append("SELECT req, bi ");
    buf.append("FROM   Request req ");
    buf.append("JOIN   req.billingItems bi ");
    buf.append("WHERE  bi.idBillingAccount != " + idBillingAccount + " ");
    buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");

    if(requestMap.keySet().iterator().hasNext()){
      buf.append("AND    bi.idRequest in (");
      Boolean first = true;
      for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
        String requestNumber = (String)i.next();      
        Request request = (Request)requestMap.get(requestNumber);
        if (!first) {
          buf.append(", ");
        }
        first = false;
        buf.append(request.getIdRequest().toString());
      }
      buf.append(")");
    }

    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "req");
      buf.append(" ");
    }

    buf.append(" ORDER BY req.number, bi.idBillingAccount, bi.idBillingItem ");

    results = sess.createQuery(buf.toString()).list();

    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Request req    =  (Request)row[0];
      BillingItem bi =  (BillingItem)row[1];

      List billingItems = (List)relatedBillingItemMap.get(req.getNumber());
      if (billingItems == null) {
        billingItems = new ArrayList();
        relatedBillingItemMap.put(req.getNumber(), billingItems);
      }
      billingItems.add(bi);
    }    
  }

  public static void cacheDiskUsageBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap)
  throws Exception {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT dsk, bi ");
    buf.append("FROM   DiskUsageByMonth dsk ");
    buf.append("JOIN   dsk.billingItems bi ");
    buf.append("WHERE  bi.idLab = " + idLab + " ");
    buf.append("AND    bi.idBillingAccount = " + idBillingAccount + " ");
    buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
    buf.append("AND    bi.idCoreFacility = " + idCoreFacility + " ");
    buf.append("AND    bi.codeBillingStatus in ('" + BillingStatus.COMPLETED + "', '" + BillingStatus.APPROVED + "', '" + BillingStatus.APPROVED_PO + "')");

    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }

    buf.append("ORDER BY dsk.idDiskUsageByMonth, bi.idBillingItem ");

    List results = sess.createQuery(buf.toString()).list();


    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      DiskUsageByMonth dsk    =  (DiskUsageByMonth)row[0];
      BillingItem bi          =  (BillingItem)row[1];

      // Exclude any disk usage that have
      // pending billing items.  (shouldn't be any)
      boolean hasPendingItems = false;
      for(Iterator i1 = dsk.getBillingItems().iterator(); i1.hasNext();) {
        BillingItem item = (BillingItem)i1.next();

        if (item.getIdBillingPeriod().equals(idBillingPeriod) &&
            item.getIdBillingAccount().equals(idBillingAccount)) {
          if (item.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
            hasPendingItems = true;
            break;
          }          
        }
      }
      if (hasPendingItems) {
        continue;
      }

      String diskUsageNumber = DISK_USAGE_NUMBER_PREFIX + dsk.getIdDiskUsageByMonth().toString();
      requestMap.put(diskUsageNumber, dsk);

      List billingItems = (List)billingItemMap.get(diskUsageNumber);
      if (billingItems == null) {
        billingItems = new ArrayList();
        billingItemMap.put(diskUsageNumber, billingItems);
      }
      billingItems.add(bi);
    }    
  }

  private void makeInvoiceReport(Session sess, BillingPeriod billingPeriod, 
      Lab lab, BillingAccount billingAccount, Invoice invoice,
      Map billingItemMap, Map relatedBillingItemMap, Map requestMap) throws Exception {

    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    BillingInvoiceHTMLFormatter formatter = new BillingInvoiceHTMLFormatter(
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CORE_FACILITY_NAME),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_NAME_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_PHONE_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.INVOICE_NOTE_1),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.INVOICE_NOTE_2),
        billingPeriod, 
        lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap,
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY));

    Element root = new Element("HTML");
    Document doc = new Document(root);

    Element head = new Element("HEAD");
    root.addContent(head);

    Element link = new Element("link");
    link.setAttribute("rel", "stylesheet");
    link.setAttribute("type", "text/css");
    link.setAttribute("href", Constants.INVOICE_FORM_CSS);
    head.addContent(link);

    Element title = new Element("TITLE");
    title.addContent("Billing Invoice - " + lab.getName() + 
        " " + billingAccount.getAccountName());
    head.addContent(title);

    Element body = new Element("BODY");
    root.addContent(body);


    Element center1 = new Element("CENTER");
    body.addContent(center1);

    center1.addContent(formatter.makeHeader());

    body.addContent(new Element("BR"));

    Element center2 = new Element("CENTER");
    body.addContent(center2);

    if (!billingItemMap.isEmpty()) {
      center2.addContent(formatter.makeDetail());          
    }

    if(billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue() && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")){
      body.addContent(new Element("BR"));
      Element hr = new Element("HR");
      hr.setAttribute("style", "border-style:dashed");
      hr.setAttribute("align", "center");
      hr.setAttribute("width", "50%");
      body.addContent(hr);
      Element p = new Element("P");
      p.setAttribute("align", "center");
      p.addContent("To ensure proper credit, please return this portion with your payment to University of Utah");
      body.addContent(p);
      body.addContent(new Element("BR"));

      Element wrapDiv = new Element("DIV");
      wrapDiv.setAttribute("class", "wrap");

      Element remitAddressDiv = new Element("DIV");
      remitAddressDiv.setAttribute("class", "remitAddress");
      Element h3 = new Element("H3");
      Element u = new Element("U");
      u.addContent("REMITTANCE ADVICE");
      h3.addContent(u);
      remitAddressDiv.addContent(h3);
      Element h5 = new Element("H5");
      h5.addContent("Your payment is due upon receipt");
      remitAddressDiv.addContent(h5);
      remitAddressDiv.addContent(formatter.makeRemittanceAddress());

      wrapDiv.addContent(remitAddressDiv);

      Element labAddressDiv = new Element("DIV");
      labAddressDiv.setAttribute("class", "labAddress");
      Element p1 = new Element("P");
      Element b = new Element("B");
      b.addContent("Invoice Number: " + invoice.getInvoiceNumber());
      p1.addContent(b);
      labAddressDiv.addContent(p1);
      Element p2 = new Element("P");
      Element b1 = new Element("B");
      b1.addContent("Amount Due: " + formatter.getGrandTotal());
      p2.addContent(b1);
      labAddressDiv.addContent(p2);
      labAddressDiv.addContent(formatter.makeLabAddress());

      wrapDiv.addContent(labAddressDiv);
      body.addContent(wrapDiv);
    }



    XMLOutputter out = new org.jdom.output.XMLOutputter();
    out.setOmitEncoding(true);
    this.xmlResult = out.outputString(doc);
    this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
    this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

  }

  private void makeInvoiceReports(Session sess, String[] labs, String[] billingAccounts) throws Exception {
    Element root = new Element("HTML");
    Document doc = new Document(root);

    Element head = new Element("HEAD");
    root.addContent(head);

    Element link = new Element("link");
    link.setAttribute("rel", "stylesheet");
    link.setAttribute("type", "text/css");
    link.setAttribute("href", Constants.INVOICE_FORM_CSS);
    head.addContent(link);

    Element title = new Element("TITLE");
    title.addContent("Billing Invoices");
    head.addContent(title);

    Element body = new Element("BODY");
    root.addContent(body);
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);

    for(int i = 0; i < labs.length; i++){
      idLab = new Integer(labs[i]);
      BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);
      Lab lab = (Lab)sess.get(Lab.class, idLab);
      BillingAccount billingAccount = (BillingAccount) sess.get(BillingAccount.class, new Integer(billingAccounts[i]));
      idBillingAccount = new Integer(billingAccounts[i]);
      CoreFacility coreFacility = (CoreFacility)sess.get(CoreFacility.class, new Integer(idCoreFacility));
      String queryString = "from Invoice where idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount and idCoreFacility=:idCoreFacility";
      Query query = sess.createQuery(queryString);
      query.setParameter("idBillingPeriod", billingPeriod.getIdBillingPeriod());
      query.setParameter("idBillingAccount", billingAccount.getIdBillingAccount());
      query.setParameter("idCoreFacility", coreFacility.getIdCoreFacility());
      Invoice invoice = (Invoice)query.uniqueResult();

      TreeMap requestMap = new TreeMap();
      TreeMap billingItemMap = new TreeMap();
      TreeMap relatedBillingItemMap = new TreeMap();
      cacheBillingItemMap(sess, this.getSecAdvisor(), idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);

    BillingInvoiceHTMLFormatter formatter = new BillingInvoiceHTMLFormatter(
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CORE_FACILITY_NAME),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_NAME_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_PHONE_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.INVOICE_NOTE_1),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.INVOICE_NOTE_2),
        billingPeriod, 
        lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap,
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY));


    Element center1 = new Element("CENTER");
    body.addContent(center1);

    center1.addContent(formatter.makeHeader());

    body.addContent(new Element("BR"));

    Element center2 = new Element("CENTER");
    body.addContent(center2);

    if (!billingItemMap.isEmpty()) {
      center2.addContent(formatter.makeDetail());          
    }

    if(billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue() && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")){
      body.addContent(new Element("BR"));
      Element hr = new Element("HR");
      hr.setAttribute("style", "border-style:dashed");
      hr.setAttribute("align", "center");
      hr.setAttribute("width", "50%");
      body.addContent(hr);
      Element p = new Element("P");
      p.setAttribute("align", "center");
      p.addContent("To ensure proper credit, please return this portion with your payment to University of Utah");
      body.addContent(p);
      body.addContent(new Element("BR"));

      Element wrapDiv = new Element("DIV");
      wrapDiv.setAttribute("class", "wrap");

      Element remitAddressDiv = new Element("DIV");
      remitAddressDiv.setAttribute("class", "remitAddress");
      Element h3 = new Element("H3");
      Element u = new Element("U");
      u.addContent("REMITTANCE ADVICE");
      h3.addContent(u);
      remitAddressDiv.addContent(h3);
      Element h5 = new Element("H5");
      h5.addContent("Your payment is due upon receipt");
      remitAddressDiv.addContent(h5);
      remitAddressDiv.addContent(formatter.makeRemittanceAddress());

      wrapDiv.addContent(remitAddressDiv);

      Element labAddressDiv = new Element("DIV");
      labAddressDiv.setAttribute("class", "labAddress");
      Element p1 = new Element("P");
      Element b = new Element("B");
      b.addContent("Invoice Number: " + invoice.getInvoiceNumber());
      p1.addContent(b);
      labAddressDiv.addContent(p1);
      Element p2 = new Element("P");
      Element b1 = new Element("B");
      b1.addContent("Amount Due: " + formatter.getGrandTotal());
      p2.addContent(b1);
      labAddressDiv.addContent(p2);
      labAddressDiv.addContent(formatter.makeLabAddress());

      wrapDiv.addContent(labAddressDiv);
      body.addContent(wrapDiv);
    }
    
    if(i != labs.length - 1){
      Element p3 = new Element("P");
      p3.setAttribute("style", "page-break-after:always");
      body.addContent(p3);
    }

    }
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    out.setOmitEncoding(true);
    this.xmlResult = out.outputString(doc);
    this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
    this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

  }

  private void sendInvoiceEmail(Session sess, String contactEmail, CoreFacility coreFacility,
      BillingPeriod billingPeriod, Lab lab,
      BillingAccount billingAccount, Map billingItemMap, Map relatedBillingItemMap,
      Map requestMap) throws Exception {

    DictionaryHelper dh = DictionaryHelper.getInstance(sess);

    String queryString="from Invoice where idCoreFacility=:idCoreFacility and idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount";
    Query query = sess.createQuery(queryString);
    query.setParameter("idCoreFacility", idCoreFacility);
    query.setParameter("idBillingPeriod", idBillingPeriod);
    query.setParameter("idBillingAccount", idBillingAccount);
    Invoice invoice = (Invoice)query.uniqueResult();
    BillingInvoiceEmailFormatter emailFormatter = new BillingInvoiceEmailFormatter(sess, coreFacility,
        billingPeriod, lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap);
    String subject = emailFormatter.getSubject();
    String body = emailFormatter.format();

    String note = "";
    boolean send = false;
    if (contactEmail != null && !contactEmail.equals("")) {
      if (dh.isProductionServer(serverName)) {
        send = true;
      } else {
        if (contactEmail.equals(dh.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
          send = true;
          subject = "(TEST) " + subject;
        } else {
          note = "Bypassing send on test system.";
        }
      }     
    } else {
      note = "Unable to email billing invoice. Billing contact email is blank for " + lab.getName();
    }

    if (send) {
      try {
        MailUtil.send(contactEmail, 
            emailFormatter.getCCList(sess, serverName),
            PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY),
            subject, 
            body,
            true);

        note = "Billing invoice emailed to " + contactEmail + ".";

        // Set last email date
        if (invoice != null) {
          invoice.setLastEmailDate(new java.sql.Date(System.currentTimeMillis()));
          sess.save(invoice);
          sess.flush();
        }
      } catch( Exception e) {
        log.error("Unable to send invoice email to " + contactEmail, e);
        note = "Unable to email invoice to " + contactEmail + " due to the following error: " + e.toString();        
      } 
    }
    Document doc = null;
    if (respondInHTML) {
      if (note.length() == 0) {
        note = "&nbsp";
      }
      Element root = new Element("HTML");
      doc = new Document(root);

      Element head = new Element("HEAD");
      root.addContent(head);

      Element link = new Element("link");
      link.setAttribute("rel", "stylesheet");
      link.setAttribute("type", "text/css");
      link.setAttribute("href", "invoiceForm.css");
      head.addContent(link);

      Element title = new Element("TITLE");
      title.addContent("Email Billing Invoice - " + lab.getName() + 
          " " + billingAccount.getAccountName());
      head.addContent(title);

      Element responseBody = new Element("BODY");
      root.addContent(responseBody);

      Element h = new Element("H3");
      h.addContent(note);   
      responseBody.addContent(h);
    } else {
      Element root = new Element("BillingInvoiceEmail");
      doc = new Document(root);
      root.setAttribute("note", note);
      root.setAttribute("title", "Email Billing Invoice - " + lab.getName() + " " + billingAccount.getAccountName());
    }

    XMLOutputter out = new org.jdom.output.XMLOutputter();
    out.setOmitEncoding(true);
    this.xmlResult = out.outputString(doc);
  }  



  /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    return response;
  } 



}