package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Request;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.output.XMLOutputter;




public class ChangeRequestStatus extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeRequestStatus.class);
  
  private String           codeRequestStatus = null;
  private Integer          idRequest = 0;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("codeRequestStatus") != null && !request.getParameter("codeRequestStatus").equals("")) {
      codeRequestStatus = request.getParameter("codeRequestStatus");
    }
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    }
   
  }

  public Command execute() throws RollBackCommandException {
    
    Session sess = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
            
      if ( codeRequestStatus.equals(null) || idRequest.equals("0") ) {
        this.addInvalidField( "Missing information", "id and code request status needed" );
      }
      
      if (this.isValid()) {
        
        
        Request req = (Request) sess.get( Request.class,idRequest );
        req.setCodeRequestStatus( codeRequestStatus );
        sess.flush();
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        
        this.xmlResult = "<SUCCESS idRequest=\"" + idRequest + 
             "\" codeRequestStatus=\"" + codeRequestStatus  +
             "\"/>";
      
      }
        
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
      
    } catch (Exception e){
      log.error("An exception has occurred while emailing in ChangeRequestStatus ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.toString());      
    } finally {
      try {
       
        if (sess != null) {
          HibernateSession.closeSession();
        }
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
    
  
  
  

}