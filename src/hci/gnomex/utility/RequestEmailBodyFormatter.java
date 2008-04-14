package hci.gnomex.utility;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Request;
import hci.framework.model.DetailObject;

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
      center1.addContent(formatter.makeLaneTable(lanes));          
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
    style.addContent(this.getInternalCSS());
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
    
    Element h3 = new Element("H3");
    h3.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request");
    center.addContent(h3);
    
    Element center1 = new Element("CENTER");
    body.addContent(center1);
    
    
    Element h2 = new Element("H2");
    h2.addContent(formatDate(request.getCreateDate()));
    center1.addContent(h2);
    
    return center1;
  }
  
  
  private String getInternalCSS() {
    StringBuffer buf = new StringBuffer();
    

    buf.append("h3 {");
    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 13pt;");
    buf.append("font-weight: bold;");
    buf.append("color: black;");
    buf.append("line-height: 0;");
    buf.append("padding-top: 5;");
    buf.append("padding-bottom: 0;");
    buf.append("}");

    buf.append("h2 {");
    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 11pt;");
    buf.append("font-weight: bold;");
    buf.append("color: black;");
    buf.append("line-height: 0;");
    buf.append("padding-top: 0;");
    buf.append("padding-bottom: 5;");
    buf.append("}   ");

    buf.append("table {");
    buf.append("width: 660;");
    buf.append("}    ");

    buf.append("table.grid {");
    buf.append("border: thin solid;");
    buf.append("border-color: #CDCDC1;");
    buf.append("width: 660;");
    buf.append("}");

    buf.append("caption{");

    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 10pt;");
    buf.append("color: black;");
    buf.append("font-weight: bold;");
    buf.append("padding-top: 5;");
    buf.append("padding-bottom: 5;");
    buf.append("}");

    buf.append("td.value {");

    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 10pt;");
    buf.append("padding-top: 0;");
    buf.append("padding-bottom: 0;");

    buf.append("}");

    buf.append("td.label {");

    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 10pt;");
    buf.append("font-weight: bold;");
    buf.append("color: #8B7765;");
    buf.append("padding-top: 0;");
    buf.append("padding-right: 10;");
    buf.append("padding-bottom: 0;  ");
    buf.append("}");

    buf.append("td.grid {");

    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 9pt;");
    buf.append("border-top: thin solid;");
    buf.append("border-left: thin solid;");
    buf.append("border-color: #CDCDC1;");
    buf.append("padding-top: 4;");
    buf.append("padding-bottom: 4;");
    buf.append("padding-right: 4;");
    buf.append("padding-left: 4;");
    buf.append("}");

    buf.append("td.gridleft {");

    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 9pt;");
    buf.append("border-top: thin solid;");
    buf.append("border-color: #CDCDC1;");
    buf.append("padding-top: 4;");
    buf.append("padding-bottom: 4;");
    buf.append("padding-right: 4;");
    buf.append("padding-left: 4;");
    buf.append("}");

    buf.append("td.gridempty {");
                                                                                                                                                                                                                                                    
                                                                                                                                                                                                                                                    
      
    buf.append("border-top: thin solid;");
    buf.append("border-left: thin solid;");
    buf.append("border-color: #CDCDC1;");
    buf.append("width: 150;");
    buf.append("}  ");

    buf.append("td.gridemptysmall {");

    buf.append("border-top: thin solid;");
    buf.append("border-left: thin solid;");
    buf.append("border-color: #CDCDC1;");
    buf.append("width: 70;");
    buf.append("} ");

    
    buf.append("td.gridreverse { ");

    buf.append("  font-family: Trebuchet MS; ");
    buf.append("  font-size: 9pt; ");
    buf.append("border-top: thin solid; ");
    buf.append("border-left: thin solid; ");
    buf.append("border-color: #CDCDC1; ");
    buf.append("padding-top: 4; ");
    buf.append(" padding-bottom: 4; ");
    buf.append(" padding-right: 4; ");
    buf.append(" padding-left: 4; ");
    buf.append(" color: white; ");
    buf.append(" font-weight: bold; ");
    buf.append("  background-color: gray; ");
    buf.append(" } ");
    
    buf.append("th {");
    buf.append("background-color: #6CA6CD;");
    buf.append("font-family: Trebuchet MS;");
    buf.append("font-size: 9pt;");
    buf.append("font-weight: bold;");
    buf.append("color: white;");

    buf.append(" padding-top: 0;");
    buf.append(" padding-bottom: 4;");
    buf.append(" padding-right: 4;");
    buf.append(" padding-left: 4;");

    buf.append("}");

    buf.append("th.normal {");
    buf.append("   border-left: thin solid white; ");
    buf.append(" }");

    buf.append("th.left {");
    buf.append("}");

    buf.append("th.colgroup {");
    buf.append("   border-left: thin solid white;");
    buf.append("    border-bottom: thin solid white;");
    buf.append(" }");
      
      

    return buf.toString();
    
  }

  
 public boolean isIncludeMicroarrayCoreNotes() {
    return includeMicroarrayCoreNotes;
  }

  
  public void setIncludeMicroarrayCoreNotes(boolean includeMicroarrayCoreNotes) {
    this.includeMicroarrayCoreNotes = includeMicroarrayCoreNotes;
  }

}
