package hci.gnomex.utility.parsers;

import hci.gnomex.model.Request;
import hci.gnomex.model.Visibility;
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
      
      if (secAdvisor.canUpdate(request, SecurityAdvisor.PROFILE_OBJECT_VISIBILITY) && (request.getRequestCategory().getIsOwnerOnly() == null || request.getRequestCategory().getIsOwnerOnly().equals("N"))) {
        if (codeVisibility == null || codeVisibility.equals("")) {
          throw new Exception("Visibility is required for experiment " + request.getNumber());
        }
        if(idInstitution != null && !idInstitution.equals("")){
          request.setIdInstitution(Integer.valueOf(idInstitution));
        }
        
        request.setCodeVisibility(codeVisibility);
        requests.add(request);
      } else if ((request.getRequestCategory().getIsOwnerOnly() != null && request.getRequestCategory().getIsOwnerOnly().equals("Y"))) {
        request.setCodeVisibility(Visibility.VISIBLE_TO_OWNER);
        log.warn("Bypassing update of visibility on request " + request.getNumber() + 
            ".  Request category allows owner visibility only.");
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
