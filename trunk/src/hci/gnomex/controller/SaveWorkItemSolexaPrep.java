package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MolarityCalculator;
import hci.gnomex.utility.WorkItemQualityControlParser;
import hci.gnomex.utility.WorkItemSolexaPrepParser;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveWorkItemSolexaPrep extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemSolexaPrep.class);
  
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemSolexaPrepParser     parser;
  
  private String                       appURL;
  
  private String                       serverName;
  
  private Map                          confirmedRequestMap = new HashMap();
  
  private DictionaryHelper             dictionaryHelper = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
      
      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemSolexaPrepParser(workItemDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }
    
    try {
      appURL = this.getLaunchAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveWorkItemSolexaPrep", e);
    }
    
    serverName = request.getServerName();
    
  }

  public Command execute() throws RollBackCommandException {
    
    if (workItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          for(Iterator i = parser.getWorkItems().iterator(); i.hasNext();) {
            WorkItem workItem = (WorkItem)i.next();
            Sample sample = (Sample)parser.getSample(workItem.getIdWorkItem());
            
            // No further processing required for On Hold or In Progress work items
            if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_ON_HOLD)) {
              continue;
            } else if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
              continue;
            }
            
            // If Solexa sample prep is done or bypassed for this sample, create work items for Solexa stock prep
            // for the sample
            if (sample.getSeqPrepDate() != null || 
                (sample.getSeqPrepBypassed() != null && sample.getSeqPrepBypassed().equalsIgnoreCase("Y"))) {
                
                Request request = (Request)sess.load(Request.class, workItem.getIdRequest());
                
                // Create a cluster gen work item for every unprocessed seq lane of the sample.
                for(Iterator i1 = request.getSequenceLanes().iterator(); i1.hasNext();) {
                  SequenceLane lane = (SequenceLane)i1.next();
                  
                  if (lane.getIdSample().equals(sample.getIdSample()) && lane.getIdFlowCellChannel() == null) {
                    
                    // Make sure this lane isn't already queued up on the cluster gen workflow
                    List otherWorkItems = (List)sess.createQuery("SELECT wi from WorkItem wi where wi.codeStepNext = '" + Step.SEQ_CLUSTER_GEN + "' and wi.idSequenceLane = " + lane.getIdSequenceLane());
                    if (otherWorkItems.size() == 0) {
                      WorkItem wi = new WorkItem();
                      wi.setIdRequest(sample.getIdRequest());
                      wi.setCodeStepNext(Step.SEQ_CLUSTER_GEN);
                      wi.setSequenceLane(lane);
                      wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                      sess.save(wi);                      
                    }
                    
                  }
                }
                
                
            }
            
            
            // If Solexa sample prep is done or failed for this sample, delete the work item
            if (sample.getSeqPrepDate() != null || 
              (sample.getSeqPrepFailed() != null && sample.getSeqPrepFailed().equalsIgnoreCase("Y")) ||
              (sample.getSeqPrepBypassed() != null && sample.getSeqPrepBypassed().equalsIgnoreCase("Y"))) {
            
              // Delete  work item
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
        log.error("An exception has occurred in SaveWorkItemSolexaPrep ", e);
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