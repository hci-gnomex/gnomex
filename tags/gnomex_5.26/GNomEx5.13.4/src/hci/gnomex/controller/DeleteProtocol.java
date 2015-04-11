package hci.gnomex.controller;

import hci.gnomex.model.HybProtocol;
import hci.gnomex.model.LabelingProtocol;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.dictionary.model.DictionaryEntry;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class DeleteProtocol extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteProtocol.class);
  

  private Integer   idProtocol;
  private String    protocolClassName;

  
  private Integer   idProtocolDeleted;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idProtocol") != null && !request.getParameter("idProtocol").equals("")) {
      idProtocol = new Integer(request.getParameter("idProtocol"));
    } 
    if (request.getParameter("protocolClassName") != null && !request.getParameter("protocolClassName").equals("")) {
      protocolClassName = request.getParameter("protocolClassName");
    }
    if (protocolClassName == null) {
      this.addInvalidField("protocolClassName", "protocolClassName is required");
    }
    if (idProtocol == null) {
      this.addInvalidField("idProtocol", "idProtocol is required");      
    }

   
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_DICTIONARIES)) {
        
        DictionaryEntry protocol = null;
        Class theClass = Class.forName(protocolClassName);
        protocol = (DictionaryEntry)sess.load(theClass, idProtocol);

        sess.delete(protocol);
        sess.flush();
        

        idProtocolDeleted = new Integer(protocol.getValue());
        
        
        this.xmlResult = "<SUCCESS idProtocol=\"" + idProtocolDeleted + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to edit dictionareis.");
        setResponsePage(this.ERROR_JSP);
      }
      
      
    }catch (Exception e){
      log.error("An exception has occurred in DeleteProtocol ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  

}