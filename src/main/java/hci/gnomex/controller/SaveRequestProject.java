package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.*;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;



public class SaveRequestProject extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveRequest.class);
  

  
  private Request    request;
  private Integer    idProject;
  private Integer    idRequest;
  private Integer    idAppUser;
  private Integer    idBillingAccount;
  private String     isExternal;
  private Boolean    forceNoChangeBilling;

  
  
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
    
    if (request.getParameter("forceNoChangeBilling") != null && request.getParameter("forceNoChangeBilling").equalsIgnoreCase("true")) {
        forceNoChangeBilling = true;
    } else {
        forceNoChangeBilling = false;
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
        if (forceNoChangeBilling || this.isExternal.equals("Y") || request.getIdLab().equals(project.getIdLab())) {
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
        
        boolean approvedBillingItemsPresent = false;
        if (changeBilling) {
            // Ensure there are no approved billing items
            Set<BillingItem> oldBillingItems = request.getBillingItemList(sess);
            for (BillingItem billingItem : oldBillingItems) {
                if (!billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING) && !billingItem.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
                    approvedBillingItemsPresent = true;
                    billingAccountMessage = "Billing was not reassigned because some billing items are already approved.";
                    break;
                }
            }
        }
        
        if (changeBilling && !approvedBillingItemsPresent) {

          request.setIdBillingAccount(idBillingAccount);
          BillingTemplate billingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
          if (billingTemplate == null) {
            billingTemplate = new BillingTemplate(request);
            sess.save(billingTemplate);
          }
          
          Map<Integer, List<Object>> infoForRecreatingBillingItems = BillingTemplate.retrieveInfoForRecreatingBillingItems(billingTemplate.getAcceptingBalanceItem(), billingTemplate.getBillingItems(sess));
          
          // Delete old billing template items if any
          Set<BillingTemplateItem> oldBtiSet = new TreeSet<BillingTemplateItem>();
          oldBtiSet.addAll( billingTemplate.getItems() );
          for (BillingTemplateItem billingTemplateItemToDelete : oldBtiSet) {
            BillingTemplateItem persistentBTI = sess.load( BillingTemplateItem.class, billingTemplateItemToDelete.getIdBillingTemplateItem() );
            sess.delete(persistentBTI);
          }
          sess.flush();
          billingTemplate.getItems().clear();

          BillingTemplateItem item = new BillingTemplateItem(billingTemplate);
          item.setIdBillingAccount(idBillingAccount);
          item.setPercentSplit(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
          item.setSortOrder(1);
          billingTemplate.getItems().add(item);
          sess.save(item);

          sess.flush();
          
          // Delete existing billing items
          Set<BillingItem> oldBillingItems = billingTemplate.getBillingItems(sess);
          for (BillingItem billingItemToDelete : oldBillingItems) {
              sess.delete(billingItemToDelete);
          }
          sess.flush();

          // Save new billing items
          Set<BillingItem> newBillingItems = billingTemplate.recreateBillingItems(sess, infoForRecreatingBillingItems);
          for (BillingItem newlyCreatedBillingItem : newBillingItems) {
              sess.save(newlyCreatedBillingItem);
          }
          sess.flush();
          
          billingAccountMessage = "Billing was successfully reassigned.";
        }
        
        sess.save(request);
        sess.flush();

        this.xmlResult = "<SUCCESS idRequest=\"" + request.getIdRequest() 
        + "\" billingAccountMessage = \"" + billingAccountMessage + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);        
      } 
    }catch (Exception e){
      LOG.error("An exception has occurred in SaveRequest ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        //closeHibernateSession;        
      } catch(Exception e){
        LOG.error("Error", e);
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