package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FlowCellChannelComparator;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.WorkItemSolexaAssembleParser;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
  private String                       flowCellDateStr;
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemSolexaAssembleParser parser;
  
  private String                       appURL;
  
  private String                       serverName;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("flowCellBarcode") != null && !request.getParameter("flowCellBarcode").equals("")) {
      flowCellBarcode = request.getParameter("flowCellBarcode");
    }
    
    if (request.getParameter("flowCellDate") != null && !request.getParameter("flowCellDate").equals("")) {
      flowCellDateStr = request.getParameter("flowCellDate");
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
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);
       
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          // Create a flow cell
          FlowCell flowCell = new FlowCell();
          flowCell.setBarcode(flowCellBarcode);
          sess.save(flowCell);
          sess.flush();
          
          flowCell.setNumber("FC" + flowCell.getIdFlowCell());
          
          
          java.sql.Date flowCellDate = null;
          if (flowCellDateStr != null) {
            flowCellDate = this.parseDate(flowCellDateStr);
          } else {
            flowCellDate = new java.sql.Date(System.currentTimeMillis());
          }
          flowCell.setCreateDate(flowCellDate);
          
          TreeSet channels = new TreeSet(new FlowCellChannelComparator());
          int laneNumber = 1;
          HashMap requestNumbers = new HashMap();
          HashMap idOrganisms = new HashMap();
          int maxCycles = 0;
          Integer idNumberSequencingCycles = null;
          for(Iterator i = parser.getChannelNumbers().iterator(); i.hasNext();) {
            String channelNumber = (String)i.next();
            
            
            FlowCellChannel channel = new FlowCellChannel();
            channel.setNumber(new Integer(laneNumber));
            channel.setIdFlowCell(flowCell.getIdFlowCell());
            sess.save(channel);
            sess.flush();

            channel.setSampleConcentrationpM(parser.getSampleConcentrationpm(channelNumber));
            channel.setIsControl(parser.getIsControl(channelNumber));
            
            List channelContents = parser.getChannelContents(channelNumber);
            for (Iterator i1 = channelContents.iterator(); i1.hasNext();) {
              WorkItemSolexaAssembleParser.ChannelContent content = (WorkItemSolexaAssembleParser.ChannelContent)i1.next();
              
              if (content.getSequenceLane() != null) {
                SequenceLane lane = content.getSequenceLane();

                lane.setIdFlowCellChannel(channel.getIdFlowCellChannel());                
                flowCell.setIdSeqRunType(lane.getIdSeqRunType());
                
                Integer seqCycles = new Integer(dh.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()));
                if (idNumberSequencingCycles == null ||
                    seqCycles.intValue() > maxCycles ) {
                  idNumberSequencingCycles = lane.getIdNumberSequencingCycles();
                  maxCycles = seqCycles.intValue();
                }

                WorkItem workItem = content.getWorkItem();

                // Keep track of request numbers, organisms on flow cells
                requestNumbers.put(workItem.getRequest().getNumber(), null);
                idOrganisms.put(lane.getSample().getIdOrganism(), null);
                
                
                // Delete  work item
                sess.delete(workItem);
              } else if (content.getSequenceControl() != null) {
                channel.setIdSequencingControl(content.getSequenceControl().getIdSequencingControl());              
              }
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
          
          this.createFlowCellDirectory(flowCell, dh.getFlowCellDirectory(serverName));
          
          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = "<SUCCESS flowCellNumber='" + flowCell.getNumber() + "'/>";
          
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
  
  private void createFlowCellDirectory(FlowCell flowCell, String flowCellDir) {

    
    String createYear = this.formatDate(flowCell.getCreateDate(), this.DATE_OUTPUT_ALTIO).substring(0,4);
    String rootDir = flowCellDir + createYear;
    
    boolean success = false;
    if (!new File(rootDir).exists()) {
      success = (new File(rootDir)).mkdir();
      if (!success) {
        log.error("Unable to create directory " + rootDir);      
      }      
    }
    
    String directoryName = rootDir +  "/" + flowCell.getNumber();
    
    success = (new File(directoryName)).mkdir();
    if (!success) {
      log.error("Unable to create directory " + directoryName);      
    }
    
   
  }

  

}