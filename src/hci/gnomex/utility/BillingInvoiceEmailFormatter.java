package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class BillingInvoiceEmailFormatter extends DetailObject{

  private BillingPeriod  billingPeriod;
  private Lab            lab;
  private BillingAccount billingAccount;
  private CoreFacility   coreFacility;
  private Invoice        invoice;
  private Map            billingItemMap; 
  private Map            requestMap;
  private Map            relatedBillingItemMap;
  private String         coreFacilityName;
  private String         coreFacilityContactName;
  private String         coreFacilityContactPhone;
  private String         invoiceNote1;
  private String         invoiceNote2;
  private Session        sess;


  private DictionaryHelper dictionaryHelper;

  protected boolean       includeMicroarrayCoreNotes = true;

  public BillingInvoiceEmailFormatter(Session sess, CoreFacility coreFacility, BillingPeriod billingPeriod, Lab lab, BillingAccount billingAccount, Invoice invoice, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
    this.sess           = sess;
    this.coreFacility   = coreFacility;
    this.billingPeriod  = billingPeriod;
    this.lab            = lab;
    this.billingAccount = billingAccount;
    this.invoice        = invoice;
    this.billingItemMap = billingItemMap;
    this.requestMap     = requestMap;
    this.relatedBillingItemMap = relatedBillingItemMap;
    this.coreFacilityName = coreFacility.getFacilityName();
    this.coreFacilityContactName = coreFacility.getContactName();
    this.coreFacilityContactPhone = coreFacility.getContactPhone();
    this.invoiceNote1 = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(coreFacility.getIdCoreFacility(), PropertyDictionary.INVOICE_NOTE_1);
    this.invoiceNote2 = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(coreFacility.getIdCoreFacility(), PropertyDictionary.INVOICE_NOTE_2);

    this.dictionaryHelper = DictionaryHelper.getInstance(sess);
  }

  public String getSubject() {
    String acctNum = billingAccount.getAccountNumber();
    if (acctNum == null || !acctNum.equals("")) {
      acctNum = billingAccount.getAccountName();
    }

    String subject = coreFacility.getFacilityName() + " Billing Summary for " + billingPeriod.getBillingPeriod() + " - " + acctNum;

    return subject;
  }

  public String getCCList(Session sess) {
    String ccList = "";
    if (billingAccount.getIsPO() != null && billingAccount.getIsPO().equals("Y")) {
      String queryString = "select distinct user from AppUser user join user.managingCoreFacilities cores where cores.idCoreFacility = :id";
      Query query = sess.createQuery(queryString);
      query.setParameter("id", coreFacility.getIdCoreFacility());
      List managers = query.list();
      for (Iterator j = managers.iterator(); j.hasNext();) {
        AppUser manager = (AppUser)j.next();
        Boolean send = false;
        if (manager.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND)) {
          if (manager.getEmail() != null && !manager.getEmail().equals("")) {
            send = true;    
          }
        }
        if (send) {
          if (ccList.length() != 0) {
            ccList += ", ";
          }
          ccList += manager.getEmail();
        }
      }
    }
    if (ccList.length() == 0) {
      ccList = null;
    }
    return ccList;
  }

  public String format() throws Exception {

    BillingInvoiceHTMLFormatter formatter = new BillingInvoiceHTMLFormatter(coreFacilityName, coreFacilityContactName, coreFacilityContactPhone,
        invoiceNote1, invoiceNote2, billingPeriod, lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap,
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(billingAccount.getIdCoreFacility(), PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(billingAccount.getIdCoreFacility(), PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY),
        PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(billingAccount.getIdCoreFacility(), PropertyDictionary.CORE_BILLING_OFFICE));

    Element root = new Element("HTML");
    Document doc = new Document(root);

    Element center1 = formatHeader(formatter, root);

    center1.addContent(formatter.makeDetail());

    XMLOutputter out = new org.jdom.output.XMLOutputter();
    String buf = out.outputString(doc);
    buf = buf.replaceAll("&amp;",    "&");
    buf = buf.replaceAll("�",        "&micro");
    buf = buf.replaceAll("&gt;",     ">");
    buf = buf.replaceAll("&lt;",     "<");

    return buf;
  }




  protected Element formatHeader(BillingInvoiceHTMLFormatter formatter, Element root) {


    Element head = new Element("HEAD");
    root.addContent(head);

    Element title = new Element("TITLE");
    title.addContent(getSubject());
    head.addContent(title);

    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getCascadingStyleSheet());
    head.addContent(style);

    Element body = new Element("BODY");
    root.addContent(body);


    body.addContent(formatter.makeIntroNote());
    body.addContent(new Element("HR"));    
    body.addContent(formatter.makeHeader(sess));


    return body;
  }

  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    BufferedReader input =  null;
    try {
      input = new BufferedReader(new FileReader(GNomExFrontController.getWebContextPath() + Constants.INVOICE_FORM_CSS));
    } catch (FileNotFoundException ex) {
      System.out.println(ex.toString());
    }
    if (input != null) {
      try {
        String line = null; 
        while (( line = input.readLine()) != null){
          buf.append(line);
          buf.append(System.getProperty("line.separator"));
        }
      }
      catch (IOException ex){
        ex.printStackTrace();
      }
      finally {
        try {
          input.close();          
        } catch (IOException e) {
        }
      }

    }
    return buf.toString();
  }

}
