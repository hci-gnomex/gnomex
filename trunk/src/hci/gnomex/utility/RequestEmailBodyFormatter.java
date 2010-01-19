package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Request;
import hci.framework.model.DetailObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class RequestEmailBodyFormatter extends DetailObject{
  
  private Request          request;
  private Set              samples;
  private Set              hybs;
  private Set              lanes;
  private String           introNote;
  
  private AppUser          appUser;
  private BillingAccount   billingAccount;
  private DictionaryHelper dictionaryHelper;
  
  protected boolean       includeMicroarrayCoreNotes = true;

  public RequestEmailBodyFormatter(Session sess, DictionaryHelper dictionaryHelper, Request request, Set samples, Set hybs, Set lanes, String introNote) {
    this.request = request;
    this.samples = samples;
    this.hybs = hybs;
    this.lanes = lanes;
    this.introNote = introNote;
    
    
    this.dictionaryHelper = dictionaryHelper;
    
    appUser = null;
    if (request.getIdAppUser() != null) {
      appUser = (AppUser)sess.get(AppUser.class, request.getIdAppUser());        
    }
    billingAccount = null;
    if (request.getIdBillingAccount() != null) {
      billingAccount = (BillingAccount)sess.get(BillingAccount.class, request.getIdBillingAccount());
    }

  }
 
  public String format() {

    
    RequestHTMLFormatter formatter = new RequestHTMLFormatter(request, appUser, billingAccount, dictionaryHelper);
    formatter.setIncludeMicroarrayCoreNotes(includeMicroarrayCoreNotes);
    
    Element root = new Element("HTML");
    Document doc = new Document(root);
    
    Element center1 = formatHeader(formatter, root);
    
    
    center1.addContent(formatter.makeRequestTable());

    center1.addContent(formatter.makeSampleTable(samples));
    
    center1.addContent(new Element("BR"));

    if (!hybs.isEmpty()) {
      center1.addContent(formatter.makeHybTable(hybs));          
    }
    
    if (!lanes.isEmpty()) {
      center1.addContent(formatter.makeSequenceLaneTable(lanes));          
    }

    XMLOutputter out = new org.jdom.output.XMLOutputter();
    String buf = out.outputString(doc);
    buf = buf.replaceAll("&amp;",    "&");
    buf = buf.replaceAll("�",        "&micro");
    buf = buf.replaceAll("&gt;",     ">");
    buf = buf.replaceAll("&lt;",     "<");
    
    return buf;
  }
  
  
  public String formatQualityControl() {
    RequestHTMLFormatter formatter = new RequestHTMLFormatter(request, appUser, billingAccount, dictionaryHelper);
    
    Element root = new Element("HTML");
    Document doc = new Document(root);
    
    Element center1 = formatHeader(formatter, root);
    
    
    center1.addContent(formatter.makeRequestTable());

    center1.addContent(formatter.makeSampleQualityTable(samples));

    XMLOutputter out = new org.jdom.output.XMLOutputter();
    String buf = out.outputString(doc);
    buf = buf.replaceAll("&amp;",    "&");
    buf = buf.replaceAll("�",        "&micro");
    buf = buf.replaceAll("&gt;",     ">");
    buf = buf.replaceAll("&lt;",     "<");
    
    return buf;
  }
  
  protected Element formatHeader(RequestHTMLFormatter formatter, Element root) {

    
    Element head = new Element("HEAD");
    root.addContent(head);
    
    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getCascadingStyleSheet());
    head.addContent(style);
    
    Element title = new Element("TITLE");
    title.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber());
    head.addContent(title);
    
    Element body = new Element("BODY");
    root.addContent(body);

    if (introNote != null) {
      
    }
    Element centera = new Element("CENTER");

    
    centera.addContent(formatter.makeIntroNote(introNote.toString()));
    Element hLine = new Element("HR");
    hLine.setAttribute("WIDTH", "640");
    centera.addContent(hLine);
    body.addContent(centera);

    
    Element center = new Element("CENTER");
    body.addContent(center);
    
    Element h3 = new Element("H2");
    h3.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request");
    center.addContent(h3);
    
    Element center1 = new Element("CENTER");
    body.addContent(center1);
    
    if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
      Element hApp = new Element("H4");
      hApp.addContent(dictionaryHelper.getApplication(request.getCodeApplication()));
      center1.addContent(hApp);              
    }
    
    Element h2 = new Element("H4");
    h2.addContent(formatDate(request.getCreateDate()));
    center1.addContent(h2);
    
    return center1;
  }
  
  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    BufferedReader input =  null;
    try {
      input = new BufferedReader(new FileReader(Constants.WEBCONTEXT_DIR + Constants.REQUEST_FORM_CSS));
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

    
 public boolean isIncludeMicroarrayCoreNotes() {
    return includeMicroarrayCoreNotes;
  }

  
  public void setIncludeMicroarrayCoreNotes(boolean includeMicroarrayCoreNotes) {
    this.includeMicroarrayCoreNotes = includeMicroarrayCoreNotes;
  }

}
