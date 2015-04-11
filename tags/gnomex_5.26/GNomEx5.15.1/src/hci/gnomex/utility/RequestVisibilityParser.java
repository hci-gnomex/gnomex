package hci.gnomex.utility;

import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class RequestVisibilityParser implements Serializable {
  
  protected Document   doc;
  protected List       requests = new ArrayList();
  
  public RequestVisibilityParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess, SecurityAdvisor secAdvisor, Logger log) throws Exception{
    
    Element rootNode = this.doc.getRootElement();
    
    
    for(Iterator i = rootNode.getChildren("Request").iterator(); i.hasNext();) {
      Element requestNode = (Element)i.next();
      
      String idRequest           = requestNode.getAttributeValue("idRequest");
      String codeVisibility      = requestNode.getAttributeValue("codeVisibility");
      String idInstitution       = requestNode.getAttributeValue("idInstitution");
      
      Request request = (Request)sess.load(Request.class, new Integer(idRequest));
      
      if (secAdvisor.canUpdate(request, SecurityAdvisor.PROFILE_OBJECT_VISIBILITY)) {
        if (codeVisibility == null || codeVisibility.equals("")) {
          throw new Exception("Visibility is required for experiment " + request.getNumber());
        }
        if(idInstitution == null || idInstitution.equals("")){
          throw new Exception("Please specify Institution for experiment " + request.getNumber());
        }
        
        request.setCodeVisibility(codeVisibility);
        request.setIdInstitution(Integer.valueOf(idInstitution));
        requests.add(request);
      } else {
        // Skip saving requests that user does not have permission to save
        log.warn("Bypassing update of visibility on request " + request.getNumber() + 
            ".  User " + secAdvisor.getUserLastName() + ", " + secAdvisor.getUserFirstName() + 
            " does not have permission to update visibility.");
      }
      
      
    }
    
   
  }
  
   
  public List getRequests() {
    return requests;
  }
  
  
  public void resetIsDirty() {
    Element rootNode = this.doc.getRootElement();
    
    for(Iterator i = rootNode.getChildren("Request").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      workItemNode.setAttribute("isDirty", "N");
    }
  }


  


}
