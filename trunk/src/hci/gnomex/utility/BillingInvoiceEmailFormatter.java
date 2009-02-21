package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Request;

import java.util.Map;
import java.util.Set;

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

    
    BillingInvoiceHTMLFormatter formatter = new BillingInvoiceHTMLFormatter(billingPeriod, lab, billingAccount, billingItemMap, requestMap);
    
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
    
    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getInternalCSS());
    head.addContent(style);
    
    Element title = new Element("TITLE");
    title.addContent(getSubject());
    head.addContent(title);
    
    Element body = new Element("BODY");
    root.addContent(body);

    Element centera = new Element("CENTER");
    body.addContent(centera);

    
    centera.addContent(formatter.makeHeader());
    
    
    return centera;
  }
  
  
  private String getInternalCSS() {
    StringBuffer buf = new StringBuffer();
    
    buf.append("table {");
    buf.append("  width: 960;");

    buf.append(" }");    

    buf.append(" table.grid {");
    buf.append("  border: none;");
    buf.append("  width: 960;");

    buf.append(" }");

    buf.append("  caption{");

    buf.append("       font-family: Trebuchet MS;");
    buf.append("       font-size: 10pt;");
    buf.append("       color: black;");
    buf.append("       font-weight: bold;");
    buf.append("       padding-top: 15;");
    buf.append("       padding-bottom: 5;");
    buf.append("       text-align: left;");
    buf.append("   }");



    buf.append("   td.value {");

    buf.append("       font-family: Trebuchet MS;");
    buf.append("       font-size: 10pt;");
    buf.append("       padding-top: 0;");
    buf.append("       padding-bottom: 0;");
          
    buf.append("   }");

    buf.append("   td.label {");

    buf.append("       font-family: Trebuchet MS;");
    buf.append("       font-size: 9pt;");
    buf.append("       font-weight: bold;");
    buf.append("       padding-top: 0;");
    buf.append("       padding-right: 8;");
    buf.append("       padding-bottom: 0;");     
    buf.append("   }");

    buf.append("   td.grid {");

    buf.append("      font-family: Trebuchet MS;");
    buf.append("       font-size: 9pt;");
          
    buf.append("      padding-top: 4;");
    buf.append("       padding-bottom: 0;");
    buf.append("       padding-right: 8;");
    buf.append("       padding-left: 4;");

    buf.append("      border-color: #CDCDC1;");
    buf.append("       border-bottom: thin  solid;");
    buf.append("       border-width: 1;");
    buf.append("   }");
      


    buf.append("  td.gridright {");

    buf.append("      font-family: Trebuchet MS;");
    buf.append("       font-size: 9pt;");
    buf.append("       text-align: RIGHT;");

    buf.append("      padding-top: 4;");
    buf.append("       padding-bottom: 0;");
    buf.append("       padding-right: 8;");
    buf.append("       padding-left: 4;");

    buf.append("       border-color: #CDCDC1;");
    buf.append("       border-bottom: thin  solid;");
    buf.append("       border-width: 1;");

    buf.append("   }");
      

    buf.append("   td.gridcenter {");

    buf.append("       font-family: Trebuchet MS;");
    buf.append("      font-size: 9pt;");
    buf.append("       text-align: CENTER;");

    buf.append("padding-top: 4;");
    buf.append("    padding-bottom: 0;");
    buf.append("    padding-right: 8;");
    buf.append("    padding-left: 4;");

    buf.append("    border-color: #CDCDC1;");
    buf.append("    border-bottom: thin  solid;");
    buf.append("    border-width: 1;");

    buf.append("}");

    buf.append("td.gridtotal {");

    buf.append("    font-family: Trebuchet MS;");
    buf.append("    font-size: 9pt;");
    buf.append("    font-weight: bold;");
    buf.append("    text-align: RIGHT;");

    buf.append("    padding-top: 4;");
    buf.append("    padding-bottom: 0;");
    buf.append("    padding-right: 8;");
    buf.append("    padding-left: 4;");

    buf.append("  border: none;");

    buf.append("}");

    buf.append("td.gridempty {");
        


    buf.append("    width: 150;");
    buf.append("}  ");
    
    buf.append("td.gridemptysmall {");

    buf.append("    border-color: #CDCDC1;");
    buf.append("    width: 50;");
    buf.append("} ");

    buf.append("th {");
         
    buf.append("     font-family: Trebuchet MS;");
    buf.append("     font-size: 9pt;");
    buf.append("     font-weight: bold;");
    buf.append("     text-decoration: underline;");
    buf.append("     padding-top: 8;");
    buf.append("     padding-bottom: 0;");
    buf.append("      padding-right: 8;");
    buf.append("     padding-left: 4;");
    buf.append("     text-align: left;");
    buf.append("     border: none;");
   

        
    buf.append(" }");
     
     
    buf.append(" a {");
    buf.append("     font-family: Trebuchet MS;");
    buf.append("     font-size: 9pt;");
    buf.append("     text-align: left;");
    buf.append(" }");

    return buf.toString();
    
  }



}
