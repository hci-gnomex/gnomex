package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class VerifyLabUsersEmailFormatter extends DetailObject{
  
  private String           labName;
  private Map              managers; 
  private Map              members;
  private Map              collaborators;

  private StringBuffer     introNote = new StringBuffer();

  private DictionaryHelper dictionaryHelper;
  

  public VerifyLabUsersEmailFormatter(Session sess, String labName, Map managers, Map members, Map collaborators) { 
    this.labName = labName;
    this.managers = managers;
    this.members = members;
    this.collaborators = collaborators;
 
    this.dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    introNote.append("The following list shows active accounts for the GNomEx microarray and next generation sequencing database.<br>");
    introNote.append("<b>After review of this list, please inform us of individuals that no longer work in your lab group so that<br>their accounts can be inactivated.</b><br>");
  }
  

  public String format() throws Exception {


    Element root = new Element("HTML");
    Document doc = new Document(root);
    
    Element body = formatHeader(root);
    
    formatBody(body);
    
    formatFooter(body);
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    String buf = out.outputString(doc);
    buf = buf.replaceAll("&amp;",    "&");
    buf = buf.replaceAll("�",        "&micro");
    buf = buf.replaceAll("&gt;",     ">");
    buf = buf.replaceAll("&lt;",     "<");
    
    return buf;
  }
  
 
  

  protected Element formatHeader(Element root) {

    
    Element head = new Element("HEAD");
    root.addContent(head);
    
    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getCascadingStyleSheet());
    head.addContent(style);
    
    Element title = new Element("TITLE");
    title.addContent("GNomEx User Accounts for " + labName + " Lab");
    head.addContent(title);
    
    Element body = new Element("BODY");
    root.addContent(body);


    body.addContent(introNote.toString());
    
    return body;
  }
  
  protected void formatBody(Element body) {
    
    StringBuffer emailBody = new StringBuffer();
    
    if (!managers.isEmpty()) {
      emailBody.append("<br><u>" + "Lab Manager Accounts" + "</u><br>");;
      for (String manager : (Set<String>)managers.keySet()) {
        emailBody.append(" " + manager + "<br>");
      }
    }

    if (!members.isEmpty()) {
      emailBody.append("<br><u>" +  "Lab Member Accounts" + "</u><br>");;
      for (String member : (Set<String>)members.keySet()) {
        emailBody.append(" " + member + "<br>");            
      }
    }

    if (!collaborators.isEmpty()) {
      emailBody.append("<br><u>" + "Lab Collaborators Accounts" + "</u><br>");;
      for (String collab : (Set<String>)collaborators.keySet()) {
        emailBody.append(" " + collab + "<br>");            
      }
      
    }

    body.addContent(emailBody.toString());
    
  }
  
  private void formatFooter(Element body) {
    StringBuffer emailFooter = new StringBuffer();
    emailFooter.append("<br>");
    emailFooter.append("Thanks,");
    emailFooter.append("<br>");
    emailFooter.append(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_NAME_CORE_FACILITY));
    emailFooter.append("<br>");
    emailFooter.append(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME));
     
    body.addContent(emailFooter.toString());
  }
  
  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    BufferedReader input =  null;
    try {
      input = new BufferedReader(new FileReader(GNomExFrontController.getWebContextPath() + Constants.EMAIL_NOTIFY_CSS));
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
