package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.WorkItemLabelingParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveWorkItemLabeling extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemLabeling.class);
  
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemLabelingParser       parser;
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";

      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemLabelingParser(workItemDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    if (workItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          for(Iterator i = parser.getWorkItems().iterator(); i.hasNext();) {
            WorkItem workItem = (WorkItem)i.next();
            LabeledSample labeledSample = (LabeledSample)parser.getLabeledSample(workItem.getIdWorkItem());
            
            // No further processing required for On Hold or In Progress work items
            if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_ON_HOLD)) {
              continue;
            } else if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
              continue;
            }
            
            // If labeling is done (or bypassed) for this labeled sample, create work items for HYB.
            if (labeledSample.getLabelingDate() != null || 
                (labeledSample.getLabelingBypassed() != null && labeledSample.getLabelingBypassed().equals("Y"))) {
              
              StringBuffer buf = new StringBuffer();
              buf.append("SELECT  h ");
              buf.append(" FROM   Request r");
              buf.append(" JOIN   r.hybridizations as  h ");
              buf.append(" WHERE  r.idRequest = " + workItem.getIdRequest());
              buf.append(" AND    (h.idLabeledSampleChannel1 =  " + labeledSample.getIdLabeledSample());
              buf.append("         OR     ");
              buf.append("         h.idLabeledSampleChannel2 =  " + labeledSample.getIdLabeledSample() + ")" );
              
              
              List hybs = sess.createQuery(buf.toString()).list();
              for(Iterator i1 = hybs.iterator(); i1.hasNext();) {
                Hybridization hyb = (Hybridization)i1.next();
                
                // If the labeleing is complete for both channels of the hyb and 
                // the work item has not already been created for the hyb, create a 
                // new work item.
                if (hyb.getLabeledSampleChannel1().getLabelingDate() != null || 
                    (hyb.getLabeledSampleChannel1().getLabelingBypassed() != null && hyb.getLabeledSampleChannel1().getLabelingBypassed().equals("Y"))) {
                  if (hyb.getLabeledSampleChannel2() == null ||
                      hyb.getLabeledSampleChannel2().getLabelingDate() != null ||
                      (hyb.getLabeledSampleChannel2().getLabelingBypassed() != null && hyb.getLabeledSampleChannel2().getLabelingBypassed().equals("Y"))) {
                    
                    StringBuffer queryBuf = new StringBuffer();
                    queryBuf.append("SELECT  wi.idWorkItem from WorkItem wi ");
                    queryBuf.append(" JOIN   wi.hybridization as hyb");
                    queryBuf.append(" WHERE  wi.idRequest = " + workItem.getIdRequest());
                    queryBuf.append(" AND    hyb.idHybridization = " + hyb.getIdHybridization());
                    List existingWorkItems = sess.createQuery(queryBuf.toString()).list();
                    if (existingWorkItems.size() == 0) {
                      WorkItem wi = new WorkItem();
                      wi.setIdRequest(workItem.getIdRequest());
                      wi.setCodeStepNext(Step.HYB_STEP);
                      wi.setHybridization(hyb);
                      wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                     
                      sess.save(wi);
                    }
                  }                
                }
              }
            }
            
            // If labeling is done or failed for this labeled sample, delete the work item
            if (labeledSample.getLabelingDate() != null || 
              (labeledSample.getLabelingFailed() != null && labeledSample.getLabelingFailed().equalsIgnoreCase("Y")) ||
              (labeledSample.getLabelingBypassed() != null && labeledSample.getLabelingBypassed().equalsIgnoreCase("Y"))) {
            
              // Delete labeling work item
              sess.delete(workItem);
            }
          }
          
          sess.flush();
          
          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(workItemDoc);
          
          setResponsePage(this.SUCCESS_JSP);
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }

        
      }catch (Exception e){
        log.error("An exception has occurred in SaveWorkflowLabeling ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
          
      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {
          
        }
      }
      
    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }
    
    return this;
  }
  

  
  
  

}