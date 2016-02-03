package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveRequestProject extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveRequest.class);
  

  
  private Request    request;
  private Integer    idProject;
  private Integer    idRequest;
  private Integer    idAppUser;
  private Integer    idBillingAccount;
  private String     isExternal;

  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    
    if (request.getParameter("idProject") != null && !request.getParameter("idProject").equals("")) {
      idProject = new Integer(request.getParameter("idProject"));
    } else {
      this.addInvalidField("idProject", "idProject is required");
    }
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    
    if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
      idAppUser = new Integer(request.getParameter("idAppUser"));
    } else {
      this.addInvalidField("idAppUser", "idAppUser is required");
    }

    if (request.getParameter("isExternal") != null && !request.getParameter("isExternal").equals("")) {
      isExternal = request.getParameter("isExternal");
    } else {
      isExternal = "N";
    }

    if (request.getParameter("idBillingAccount") != null && !request.getParameter("idBillingAccount").equals("")) {
      idBillingAccount = new Integer(request.getParameter("idBillingAccount"));
    }

    
  }

  public Command execute() throws RollBackCommandException {
    String billingAccountMessage = "";
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      request = (Request)sess.load(Request.class, idRequest);
      
      Project project = (Project)sess.get(Project.class, idProject);
      
      if (!this.getSecAdvisor().canUpdate(project)) {
        this.addInvalidField("projectperm", "You do not have update permissions on project " + project.getName() + ".");
        setResponsePage(this.ERROR_JSP);
      }
      
      Boolean changeBilling = true;
      if (this.isValid()) {
        if (this.isExternal.equals("Y") || request.getIdLab().equals(project.getIdLab())) {
          changeBilling = false;
        } else {
          if (idBillingAccount == null) {
            this.addInvalidField("idBillingAccount", "idBillingAccount is required");
            setResponsePage(this.ERROR_JSP);
          }
        }
      }
      
      if (this.isValid() && 
          !this.getSecAdvisor().canUpdate(request)) {
        this.addInvalidField("Insufficient permissions", "You do not have update permissions on experiment " + request.getNumber() + ".");
        setResponsePage(this.ERROR_JSP);
      }
      
      String reqExternal = "N";
      if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
        reqExternal = "Y";
      }

      if (this.isValid() && !this.isExternal.equals(reqExternal)) {
        this.addInvalidField("IsExternal", "Is external flag incorrect on request");
        setResponsePage(this.ERROR_JSP);
      }
      
      if (this.isValid()) {
        reassignLabForTransferLog(sess, project, request);
        
        request.setIdProject(idProject);       
        request.setIdLab(project.getIdLab());
        request.setIdAppUser(idAppUser);
        if (changeBilling) {
          request.setIdBillingAccount(idBillingAccount);
            
          // Change the account on the billing items.
          int reassignCount =  0;
          int unassignedCount = 0;
          for(Iterator ib = request.getBillingItems(sess).iterator(); ib.hasNext();) {
            BillingItem bi = (BillingItem)ib.next();
            if (bi.getCodeBillingStatus().equals(BillingStatus.PENDING) || bi.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
              bi.setIdBillingAccount(request.getIdBillingAccount());   
              bi.resetInvoiceForBillingItem(sess);
              reassignCount++;
            } else  {
              unassignedCount++;
            }
          }
          if (unassignedCount > 0) {
            billingAccountMessage = "WARNING: The billing account could not be reassigned for " + unassignedCount + " approved billing items.  Please reassign in the Billing screen.";
          } 
          if (billingAccountMessage.length() > 0) {
            billingAccountMessage += "\n\n(The billing account has been reassigned for  " + reassignCount + " billing item(s).)";
          } else {
            billingAccountMessage = "The billing account has been reassigned for " + reassignCount + " billing item(s).";
          }
          if (reassignCount > 0) {
            sess.flush();
          }
        }
        
        sess.save(request);
        sess.flush();

        this.xmlResult = "<SUCCESS idRequest=\"" + request.getIdRequest() 
        + "\" billingAccountMessage = \"" + billingAccountMessage + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);        
      } 
    }catch (Exception e){
      log.error("An exception has occurred in SaveRequest ", e);
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
  
  private void reassignLabForTransferLog(Session sess, Project project, Request request) {
    if (!request.getIdLab().equals(project.getIdLab())) {
      // If an existing request has been assigned to a different lab, change
      // the idLab on the TransferLogs.
      String buf = "SELECT tl from TransferLog tl where idRequest = " + request.getIdRequest();
      List transferLogs = sess.createQuery(buf).list();
      for (Iterator i = transferLogs.iterator(); i.hasNext();) {
        TransferLog tl = (TransferLog)i.next();
        tl.setIdLab(project.getIdLab());
      }
    }
  }
  

}