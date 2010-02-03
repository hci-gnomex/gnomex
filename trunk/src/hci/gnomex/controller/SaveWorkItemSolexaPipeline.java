package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.WorkItemSolexaPipelineParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveWorkItemSolexaPipeline extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemSolexaPipeline.class);
  
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemSolexaPipelineParser parser;
  
  private DictionaryHelper             dictionaryHelper;
  
  private String                       appURL;
  
  private String                       serverName;
  
  private HashMap                      requestNotifyMap = new HashMap();
  private HashMap                      requestNotifyLaneMap = new HashMap();
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
      
      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemSolexaPipelineParser(workItemDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }
    
    try {
      appURL = this.getLaunchAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveRequest", e);
    }
    
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    if (workItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          for (Iterator i = parser.getWorkItems().iterator(); i.hasNext();) {
            WorkItem workItem = (WorkItem) i.next();
            FlowCellChannel channel = (FlowCellChannel) parser.getFlowCellChannel(workItem.getIdWorkItem());

            // If pipeline is done or has failed, delete the work item.
            if (channel.getPipelineDate() != null ||
                (channel.getPipelineFailed() != null && channel.getPipelineFailed().equals("Y"))) {

              // Delete work item
              sess.delete(workItem);
              
              
             
            }
            
            // Check to see if all of the sequence lanes for each request have been completed.
            if (channel.getSequenceLanes() != null) {
              for(Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext();) {
                SequenceLane lane = (SequenceLane)i1.next();
                
                Request request = (Request)sess.load(Request.class, lane.getIdRequest());
                
                // Keep track of requests to send out a confirmation email (just once per request)
                // (Send email if at least one sequence lane has been completed pipeline.)
                if (channel.getPipelineDate() != null) {  
                  requestNotifyMap.put(request.getNumber(), request);

                  Collection lanes = (Collection)requestNotifyLaneMap.get(request.getNumber());
                  if (lanes == null) {
                    lanes = new ArrayList();
                  }
                  lanes.add(lane);
                  requestNotifyLaneMap.put(request.getNumber(), lanes);
                }

                // Set the completed date on the request
                if (request.isConsideredFinished() && request.getCompletedDate() == null) {
                  request.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
                }
                
              }
            }
            
          }
          
          sess.flush();
          
          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(workItemDoc);

          // Now send out confirmation emails
          for(Iterator i = requestNotifyMap.keySet().iterator(); i.hasNext();) {
            String requestNumber = (String)i.next();
            Request request = (Request)requestNotifyMap.get(requestNumber);
            try {
              this.sendConfirmationEmail(sess, request, (Collection)requestNotifyLaneMap.get(request.getNumber()));
            } catch (Exception e) {
              log.error("Unable to send confirmation email notifying submitter that request "
                  + request.getNumber()
                  + " has sequence lanes that have completed the pipeline.  " + e.toString());
            }
          }
          
          setResponsePage(this.SUCCESS_JSP);  
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }
        
        
      }catch (Exception e){
        log.error("An exception has occurred in SaveWorkflowSolexaPipeline ", e);
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
  
  

  
  private void sendConfirmationEmail(Session sess, Request request, Collection lanes) throws NamingException, MessagingException {
    
    dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    String downloadRequestURL = appURL + "?requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_FETCH_RESULTS;

    String finishedLaneText = "";
    int finishedLaneCount = 0;
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();
      if (lane.getFlowCellChannel() != null && lane.getFlowCellChannel().getPipelineDate() != null) {
        finishedLaneText += lane.getNumber();
        finishedLaneCount++;
        if (i.hasNext()) {
          finishedLaneText += ", ";
        }
      }
    }
    String laneText = finishedLaneCount > 1 ? "Lanes" : "Lane";
    String haveText = finishedLaneCount > 1 ? "have"  : "has";
    
    if (finishedLaneCount == 0) {
      return;
    }
    
    
    
    StringBuffer introNote = new StringBuffer();
    introNote.append("Sequence " + laneText + " " + finishedLaneText + " for ");
    introNote.append("Request " + request.getNumber() + " " + haveText + " been completed by the " + dictionaryHelper.getProperty(Property.CORE_FACILITY_NAME) + ".");
    introNote.append("<br><br>To fetch the results, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_FETCH_RESULTS + "</a>.");
    
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, dictionaryHelper, request, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(), introNote.toString());
    emailFormatter.setIncludeMicroarrayCoreNotes(false);
        
    String subject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber() + " completed";
    
    boolean send = false;
    if (serverName.equals(dictionaryHelper.getProperty(Property.PRODUCTION_SERVER))) {
      send = true;
    } else {
      if (request.getAppUser() != null  &&
          request.getAppUser().getEmail() != null && 
          !request.getAppUser().getEmail().equals("") &&
          request.getAppUser().getEmail().equals(dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "TEST - " + subject;
      }
    }
    
    if (send) {
      MailUtil.send(request.getAppUser().getEmail(), 
          null,
          dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
          subject, 
          emailFormatter.format(),
          true);
      
    }
    
  }

  

}