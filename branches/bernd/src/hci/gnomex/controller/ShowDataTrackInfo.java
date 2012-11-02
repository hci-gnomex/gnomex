package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyType;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;



public class ShowDataTrackInfo extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";

  private Integer idDataTrack = null;
  private String baseURL = "";
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idDataTrack") != null) {
      idDataTrack = new Integer(request.getParameter("org.dom4j.io"));
    } else {
      this.addInvalidField("org.dom4j.io", "org.dom4j.io is required");
    }
    
    baseURL = "";
    StringBuffer fullPath = request.getRequestURL();
    String extraPath = request.getServletPath() + request.getPathInfo();
    int pos = fullPath.lastIndexOf(extraPath);
    if (pos > 0) {
      baseURL = fullPath.substring(0, pos);
    }
  }

  public Command execute() throws RollBackCommandException {
    
    Session sess = null;
    try {
      
   
      sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
     
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

      if (!this.getSecAdvisor().canRead(dataTrack)) {
        throw new Exception("Insufficient permissions to access information on this dataTrack.");        
      }


      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("HTML");

      Element head = root.addElement("HEAD");
      Element link = head.addElement("link");
      link.addAttribute("rel", "stylesheet");
      link.addAttribute("type", "text/css");



      link.addAttribute("href", baseURL + "/info.css");

      Element body = root.addElement("BODY");


      Element center = body.addElement("CENTER");
      Element h1   = center.addElement("H1");
      h1.addText("DAS2 DataTrack");

      Element h2   = body.addElement("H2");
      h2.addText(dataTrack.getName());

      Element table = body.addElement("TABLE");

      Element row   = table.addElement("TR");
      row.addElement("TD").addText("Summary").addAttribute("CLASS", "label");
      row.addElement("TD").addCDATA(dataTrack.getSummary() != null && !dataTrack.getSummary().equals("") ? dataTrack.getSummary() : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("Description").addAttribute("CLASS", "label");
      if (dataTrack.getDescription() == null || dataTrack.getDescription().equals("")) {
        row.addElement("TD").addCDATA("&nbsp;");
      } else {
        String description = dataTrack.getDescription().replaceAll("\\n", "<br>");
        description = dataTrack.getDescription().replaceAll("\\r", "<br>");
        row.addElement("TD").addCDATA(description);       
      }

      row   = table.addElement("TR");     
      row.addElement("TD").addText("Owner").addAttribute("CLASS", "label");
      row.addElement("TD").addCDATA(dataTrack.getIdAppUser() != null ? dh.getAppUserObject(dataTrack.getIdAppUser()).getDisplayName() : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("Owner email").addAttribute("CLASS", "label");
      String userEmail = dataTrack.getIdAppUser() != null ?  dh.getAppUserObject(dataTrack.getIdAppUser()).getEmail() : "";
      row.addElement("TD").addCDATA(userEmail != null ? userEmail : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("Owner institute").addAttribute("CLASS", "label");
      String userInstitute =  dataTrack.getIdAppUser() != null ?  dh.getAppUserObject(dataTrack.getIdAppUser()).getInstitute() : "";
      row.addElement("TD").addCDATA(userInstitute != null ? userInstitute : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("User Group").addAttribute("CLASS", "label");
      row.addElement("TD").addCDATA(dataTrack.getLab() != null ? dataTrack.getLab().getName() : "&nbsp;");

      row   = table.addElement("TR");      
      row.addElement("TD").addText("User Group contact").addAttribute("CLASS", "label");
      String groupContact = dataTrack.getLab() != null ? dataTrack.getLab().getContactName() : "&nbsp;";
      row.addElement("TD").addCDATA(groupContact != null ? groupContact : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("User Group email").addAttribute("CLASS", "label");
      String groupEmail =  dataTrack.getLab() != null ? dataTrack.getLab().getContactEmail() : "&nbsp;";
      row.addElement("TD").addCDATA(groupEmail != null ? groupEmail : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("User Group institute").addAttribute("CLASS", "label");
      String instituteName = dataTrack.getIdInstitution() != null ? DictionaryManager.getDisplay("hci.gnomex.model.Institution", dataTrack.getIdInstitution().toString()) : "&nbsp;";
      row.addElement("TD").addCDATA(instituteName != null && !instituteName.equals("" )? instituteName : "&nbsp;");

      row   = table.addElement("TR");     
      row.addElement("TD").addText("Visibility").addAttribute("CLASS", "label");
      row.addElement("TD").addCDATA(dataTrack.getCodeVisibility() != null && !dataTrack.getCodeVisibility().equals("") ? DictionaryManager.getDisplay("hci.gnomex.model.Visibility", dataTrack.getCodeVisibility().toString()) : "&nbsp;");

      for(PropertyEntry ap : (Set<PropertyEntry>)dataTrack.getPropertyEntries()) {
        row   = table.addElement("TR");     
        row.addElement("TD").addText(ap.getProperty().getName()).addAttribute("CLASS", "label");
        if (ap.getProperty().getCodePropertyType().equals(PropertyType.URL)) {
          StringBuffer value = new StringBuffer();
          for(PropertyEntryValue av : (Set<PropertyEntryValue>)ap.getValues()) {
            if (value.length() > 0) {
              value.append(", ");
            }
            value.append(av.getValue());
          }
          row.addElement("TD").addCDATA(value.length() > 0 ? value.toString() : "&nbsp;");

        } else {
          row.addElement("TD").addCDATA(ap.getValue() != null && !ap.getValue().equals("") ? ap.getValue() : "&nbsp;");

        }

      }

      String publishedBy = "&nbsp;";
      if (dataTrack.getCreatedBy() != null && !dataTrack.getCreatedBy().equals("")) {
        publishedBy = dataTrack.getCreatedBy();

        if (dataTrack.getCreateDate() != null) {
          publishedBy += " " + this.formatDate(dataTrack.getCreateDate());
        }
      } else {
        if (dataTrack.getCreateDate() != null) {
          publishedBy = " " + this.formatDate(dataTrack.getCreateDate());
        }
      }
      row   = table.addElement("TR");     
      row.addElement("TD").addText("Published by").addAttribute("CLASS", "label");
      row.addElement("TD").addCDATA(publishedBy);


      this.xmlResult = doc.asXML();

    } catch (Exception e) {

      e.printStackTrace();
      Document doc = DocumentHelper.createDocument();

      
      Element root = doc.addElement("HTML");

      Element head = root.addElement("HEAD");
      Element link = head.addElement("link");
      link.addAttribute("rel", "stylesheet");
      link.addAttribute("type", "text/css");
      Element body = root.addElement("BODY");
      body.addText(e.toString());

      this.xmlResult = doc.asXML();

    } finally {
      

      if (sess != null) {
        sess.close();
      }
    }
    
    return this;
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