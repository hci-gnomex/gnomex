package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FlowCellChannelComparator;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.WorkItemSolexaAssembleParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveWorkItemSolexaAssemble extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemSolexaAssemble.class);
  
  private String                       flowCellBarcode;
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemSolexaAssembleParser parser;
  
  private String                       appURL;
  
  private String                       serverName;
  
  private DictionaryHelper             dictionaryHelper = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("flowCellBarcode") != null && !request.getParameter("flowCellBarcode").equals("")) {
      flowCellBarcode = request.getParameter("flowCellBarcode");
    }
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
      
      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemSolexaAssembleParser(workItemDoc);
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
        DictionaryHelper dh = new DictionaryHelper();
        dh.getDictionaries(sess);
       
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          // Create a flow cell
          FlowCell flowCell = new FlowCell();
          flowCell.setBarcode(flowCellBarcode);
          sess.save(flowCell);
          sess.flush();
          
          flowCell.setNumber("FC" + flowCell.getIdFlowCell());
          flowCell.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
          
          TreeSet channels = new TreeSet(new FlowCellChannelComparator());
          int laneNumber = 1;
          HashMap requestNumbers = new HashMap();
          HashMap idOrganisms = new HashMap();
          int maxCycles = 0;
          Integer idNumberSequencingCycles = null;
          for(Iterator i = parser.getFlowCellChannelContents().iterator(); i.hasNext();) {
            Object content = i.next();

            FlowCellChannel channel = new FlowCellChannel();
            channel.setNumber(new Integer(laneNumber));
            channel.setIdFlowCell(flowCell.getIdFlowCell());
            sess.save(channel);
            sess.flush();

            if (content instanceof SequenceLane) {
              SequenceLane lane = (SequenceLane)content;
              channel.setIdSequenceLane(lane.getIdSequenceLane());              

              lane.setIdFlowCellChannel(channel.getIdFlowCellChannel());
              
              flowCell.setIdFlowCellType(lane.getIdFlowCellType());
              
              Integer seqCycles = new Integer(dh.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()));
            	if (idNumberSequencingCycles == null ||
                	seqCycles.intValue() > maxCycles ) {
              	idNumberSequencingCycles = lane.getIdNumberSequencingCycles();
              	maxCycles = seqCycles.intValue();
            	}
              
              
              // Delete  work item
              WorkItem workItem = parser.getWorkItem(lane.getIdSequenceLane());
              sess.delete(workItem);
              
              
              
              
            } else if (content instanceof SequencingControl) {
              channel.setIdSequencingControl(((SequencingControl)content).getIdSequencingControl());              
            }

            // Create a work item for each channel
            WorkItem wi = new WorkItem();
            wi.setFlowCellChannel(channel);
            wi.setCodeStepNext(Step.SEQ_RUN);
            wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
            sess.save(wi);

            sess.flush();
            
            
            channels.add(channel);

            
            laneNumber++;
                        
          }
          flowCell.setIdNumberSequencingCycles(idNumberSequencingCycles);
          flowCell.setFlowCellChannels(channels);
          
          String notes = "";
          for(Iterator i = requestNumbers.keySet().iterator(); i.hasNext();) {
            notes += i.next();
            if (i.hasNext()) {
              notes += ", ";
            } else {
              notes += " ";
            }
          }
          if (idOrganisms.size() > 0) {
            notes += "(";
            for(Iterator i = idOrganisms.keySet().iterator(); i.hasNext();) {
              notes += dh.getOrganism((Integer)i.next());
              if (i.hasNext()) {
                notes += "/";
              }
            }           
            notes += ")";
          }
          if (!notes.equals("")) {
            flowCell.setNotes(notes);
          }
          
          sess.save(flowCell);
          
          
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
        log.error("An exception has occurred in SaveWorkflowQualityControl ", e);
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