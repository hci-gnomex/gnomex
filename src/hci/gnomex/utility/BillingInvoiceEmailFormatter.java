package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Property;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class BillingInvoiceEmailFormatter extends DetailObject{
  
  private BillingPeriod  billingPeriod;
  private Lab            lab;
  private BillingAccount billingAccount;
  private Map            billingItemMap; 
  private Map            requestMap;


  private DictionaryHelper dictionaryHelper;
  
  protected boolean       includeMicroarrayCoreNotes = true;

  public BillingInvoiceEmailFormatter(Session sess, BillingPeriod billingPeriod, Lab lab, BillingAccount billingAccount, Map billingItemMap, Map requestMap) { 
    this.billingPeriod = billingPeriod;
    this.lab = lab;
    this.billingAccount = billingAccount;
    this.billingItemMap = billingItemMap;
    this.requestMap = requestMap;
    
    
 
    this.dictionaryHelper = DictionaryHelper.getInstance(sess);
  }
  
  public String getSubject() {
    String acctNum = billingAccount.getAccountNumber();
    if (acctNum == null || !acctNum.equals("")) {
      acctNum = billingAccount.getAccountName();
    }
    String subject = billingPeriod.getBillingPeriod() + " Microarray Chargeback" +
                     " - " + acctNum;
    return subject;
  }
 
  public String format() throws Exception {

    BillingInvoiceHTMLFormatter formatter = new BillingInvoiceHTMLFormatter(this.dictionaryHelper.getProperty(Property.CORE_FACILITY_NAME), 
        this.dictionaryHelper.getProperty(Property.CONTACT_NAME_CORE_FACILITY),
        this.dictionaryHelper.getProperty(Property.CONTACT_PHONE_CORE_FACILITY),
        billingPeriod, lab, billingAccount, billingItemMap, requestMap);
    
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
    body.addContent(formatter.makeHeader());
    
    
    return body;
  }
  
  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    BufferedReader input =  null;
    try {
      input = new BufferedReader(new FileReader(Constants.WEBCONTEXT_DIR + Constants.INVOICE_FORM_CSS));
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
