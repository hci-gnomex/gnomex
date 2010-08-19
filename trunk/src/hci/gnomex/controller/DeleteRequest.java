package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.WorkItem;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;




public class DeleteRequest extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteRequest.class);
  
  
  private Integer      idRequest = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
     idRequest = new Integer(request.getParameter("idRequest"));
   } else {
     this.addInvalidField("idRequest", "idRequest is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
    
      Request req = (Request)sess.load(Request.class, idRequest);
    
      if (this.getSecAdvisor().canDelete(req)) {
        
        // Remove the work items
        for(Iterator i = req.getWorkItems().iterator(); i.hasNext();) {
          WorkItem wi = (WorkItem)i.next();
          sess.delete(wi);
        }
        sess.flush();
        
        // Remove references to labeled samples on hyb
        for(Iterator i = req.getHybridizations().iterator(); i.hasNext();) {
          Hybridization h = (Hybridization)i.next();
          h.setIdLabeledSampleChannel1(null);
          h.setIdLabeledSampleChannel2(null);
        }
        sess.flush();


        // Delete the labeled samples next
        for(Iterator i = req.getLabeledSamples().iterator(); i.hasNext();) {
          LabeledSample ls  = (LabeledSample)i.next();
          sess.delete(ls);
        }
        sess.flush();
        
        // Delete sequence lanes
        for(Iterator i = req.getSequenceLanes().iterator(); i.hasNext();) {
          SequenceLane lane = (SequenceLane)i.next();
          sess.delete(lane);
        }
        sess.flush();
        
        // Delete billing items
        for(Iterator i = req.getBillingItems().iterator(); i.hasNext();) {
          BillingItem bi = (BillingItem)i.next();
          sess.delete(bi);
        }
        sess.flush(); 
       
        
        //
        // Delete Request
        //
        Hibernate.initialize(req.getSeqLibTreatments());
        req.setSeqLibTreatments(new TreeSet());
        sess.flush();
        
        sess.delete(req);
        
        sess.flush();
        
       

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete this request.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      log.error("An exception has occurred in DeleteRequest ", e);
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