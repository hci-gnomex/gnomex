package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;



public class DeleteWorkItem extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteWorkItem.class);
  
  
  private List<Integer>      workItemIds = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("workItemIds") != null && !request.getParameter("workItemIds").equals("")) {
     String idWorkItems = request.getParameter("workItemIds");
     String[] idStrings = idWorkItems.split(",");
     workItemIds = new ArrayList<Integer>();
     for (String idString : idStrings) {
       workItemIds.add(new Integer(idString));
     }
   } else {
     this.addInvalidField("idWorkItems", "idWorkItems is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
    
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        
        for(Integer idWorkItem: workItemIds) {
          WorkItem wi = (WorkItem)sess.load(WorkItem.class, idWorkItem);
          sess.delete(wi);
        }
        sess.flush();

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete work items.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      LOG.error("An exception has occurred in DeleteWorkItem ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        LOG.error("An exception has occurred in DeleteWorkItem ", e);
      }
    }
    
    return this;
  }
  
 
  
  
  

}