package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class RequestEmailBodyFormatter extends DetailObject{
  
  private SecurityAdvisor  secAdvisor;
  private Request          request;
  private Set              samples;
  private Set              hybs;
  private Set              lanes;
  private String           introNote;
  private String           appURL;
  
  private String           amendState;
  
  private AppUser          appUser;
  private BillingAccount   billingAccount;
  private DictionaryHelper dictionaryHelper;
  
  private String           captionStyle = "font-size: 10pt; font-weight: bold; color: #8B7765; caption-side: top; text-align: left; margin-left: 0; margin-top: 25; margin-bottom: 0; padding-top: 25; padding-bottom: 0;";
  
  protected boolean       includeMicroarrayCoreNotes = true;

  public RequestEmailBodyFormatter(Session sess, SecurityAdvisor secAdvisor, String appURL, DictionaryHelper dictionaryHelper, Request request, String amendState, Set samples, Set hybs, Set lanes, String introNote) {
    this.secAdvisor = secAdvisor;
    this.request = request;
    this.samples = samples;
    this.hybs = hybs;
    this.lanes = lanes;
    this.introNote = introNote;
    this.appURL = appURL;
    this.amendState = amendState;
    
    
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

    
    RequestHTMLFormatter formatter = new RequestHTMLFormatter(this.secAdvisor, request, appUser, billingAccount, dictionaryHelper);
    formatter.setIncludeMicroarrayCoreNotes(includeMicroarrayCoreNotes);
    
    Element root = new Element("HTML");
    Document doc = new Document(root);
    
    Element center1 = formatHeader(formatter, root);
    
    
    center1.addContent(formatter.makeRequestTable());

    formatter.addSampleTable(center1, samples, captionStyle);
    
    center1.addContent(new Element("BR"));

    if (!hybs.isEmpty()) {
      center1.addContent(formatter.makeHybTable(hybs, captionStyle));          
    }
    
    if (!lanes.isEmpty()) {
      formatter.addSequenceLaneTable(center1, lanes, amendState, captionStyle);          
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
    RequestHTMLFormatter formatter = new RequestHTMLFormatter(this.secAdvisor, request, appUser, billingAccount, dictionaryHelper);
    
    Element root = new Element("HTML");
    Document doc = new Document(root);
    
    Element center1 = formatHeader(formatter, root);
    
    
    center1.addContent(formatter.makeRequestTable());

    center1.addContent(formatter.makeSampleQualityTable(samples, captionStyle));

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
    
    
    Element title = new Element("TITLE");
    title.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + (request.getIsExternal() != null && request.getIsExternal().equals("Y") ? "" :  " Request") + request.getNumber());
    head.addContent(title);

    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getCascadingStyleSheet());
    head.addContent(style);

    
    Element body = new Element("BODY");
    root.addContent(body);

    if (introNote != null) {
      
    }
    Element centera = new Element("CENTER");

    
    centera.addContent(formatter.makeIntroNote(introNote.toString()));
    Element hLine = new Element("HR");
    hLine.setAttribute("WIDTH", "640");
    centera.addContent(hLine);
    centera.addContent(new Element("BR"));
    body.addContent(centera);

    
    Element center = new Element("CENTER");
    body.addContent(center);
    
    Element h2 = new Element("H2");
    h2.addContent(formatter.makeRequestCategoryImage(appURL));
    h2.addContent("&nbsp;&nbsp;&nbsp;" + request.getNumber() + "&nbsp;&nbsp;&nbsp;");
    h2.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + (request.getIsExternal() != null && request.getIsExternal().equals("Y") ? "" :  " Request"));
    center.addContent(h2);
    
    if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
      Element he = new Element("H4");
      he.addContent("External Experiment");
      center.addContent(he);              
    }

    
    Element center1 = new Element("CENTER");
    body.addContent(center1);
    
    if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
      Element hApp = new Element("H4");
      hApp.addContent(dictionaryHelper.getApplication(request.getCodeApplication()));
      center1.addContent(hApp);              
    }
    
    String seqLibTreatments = ""; 
    if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
      int count = 0;
      for(Iterator i = request.getSeqLibTreatments().iterator(); i.hasNext();) {
        SeqLibTreatment t = (SeqLibTreatment)i.next();
        Element hTreatment = new Element("H4");
        hTreatment.addContent(t.getSeqLibTreatment());
        center1.addContent(hTreatment);                  
      }
    }
    
    return center1;
  }
  
  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    BufferedReader input =  null;
    try {
      input = new BufferedReader(new FileReader(GNomExFrontController.getWebContextPath() + Constants.REQUEST_FORM_EMAIL_CSS));
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
