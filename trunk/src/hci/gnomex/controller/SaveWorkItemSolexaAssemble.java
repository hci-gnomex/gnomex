package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingPlatform;
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
  
  private String                       codeStepNext;
  private String                       flowCellBarcode;
  private String                       flowCellDateStr;
  private String                       flowCellRunNumberStr;
  private String                       flowCellNumCyclesStr;
  private String                       flowCellSide;
  private String                       flowCellIdSeqRunTypeStr;
  private String                       flowCellIdInstrumentStr;
  private String                       lastCycleDateStr;
  private String                       workItemXMLString = null;
  private Document                     workItemDoc;
  private String                       dirtyWorkItemXMLString = null;
  private Document                     dirtyWorkItemDoc;
  private WorkItemSolexaAssembleParser parser;

  
  private String                       appURL;
  
  private String                       serverName;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("codeStepNext") != null && !request.getParameter("codeStepNext").equals("")) {
      codeStepNext = request.getParameter("codeStepNext");
    } else {
      this.addInvalidField("codeStepNext", "codeStepNext is required");
    }
    
    if (request.getParameter("flowCellBarcode") != null && !request.getParameter("flowCellBarcode").equals("")) {
      flowCellBarcode = request.getParameter("flowCellBarcode");
    }
    
    if (request.getParameter("flowCellDate") != null && !request.getParameter("flowCellDate").equals("")) {
      flowCellDateStr = request.getParameter("flowCellDate");
    }
    
    if (request.getParameter("runNumber") != null && !request.getParameter("runNumber").equals("")) {
      flowCellRunNumberStr = request.getParameter("runNumber");
    }
    
    if (request.getParameter("numberSequencingCyclesActual") != null && !request.getParameter("numberSequencingCyclesActual").equals("")) {
      flowCellNumCyclesStr = request.getParameter("numberSequencingCyclesActual");
    }
    
    if (request.getParameter("side") != null && !request.getParameter("side").equals("")) {
      flowCellSide = request.getParameter("side");
    }
    
    if (request.getParameter("idSeqRunType") != null && !request.getParameter("idSeqRunType").equals("")) {
      flowCellIdSeqRunTypeStr = request.getParameter("idSeqRunType");
    }
    
    if (request.getParameter("idInstrument") != null && !request.getParameter("idInstrument").equals("")) {
      flowCellIdInstrumentStr = request.getParameter("idInstrument");
    }
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
      
      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
      } catch (JDOMException je ) {
        log.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }
    
    
    if (request.getParameter("dirtyWorkItemXMLString") != null && !request.getParameter("dirtyWorkItemXMLString").equals("")) {
      dirtyWorkItemXMLString = "<WorkItemList>" + request.getParameter("dirtyWorkItemXMLString") + "</WorkItemList>";
      StringReader dirtyReader = new StringReader(dirtyWorkItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        dirtyWorkItemDoc = sax.build(dirtyReader);
      } catch (JDOMException je ) {
        log.error( "Cannot parse dirtyWorkItemXMLString", je );
        this.addInvalidField( "DirtyWorkItemXMLString", "Invalid work item xml");
      }

    }
    
    try {
      parser = new WorkItemSolexaAssembleParser(workItemDoc, dirtyWorkItemDoc);      
    } catch (Exception e) {
      log.error( "Error occurred in WorkItemSolexaAssemberParser", e );
      this.addInvalidField( "ParserError", "Error occurred in WorkItemSolexaAssemberParser");      
    }
    
    try {
      appURL = this.getLaunchAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveRequest", e);
    }
    
    serverName = request.getServerName();
    
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DictionaryHelper dh;
    FlowCell flowCell = null;
    
    if (workItemXMLString != null || this.dirtyWorkItemXMLString != null) {
      try {
        sess = HibernateSession.currentSession(this.getUsername());
        dh = DictionaryHelper.getInstance(sess);
        parser.parse(sess);
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          
          // Create flow cell
          if (this.workItemXMLString != null) {
            
            // Create a flow cell
            flowCell = new FlowCell();
            flowCell.setBarcode(flowCellBarcode);
            sess.save(flowCell);
            sess.flush();
            
            flowCell.setNumber("FC" + flowCell.getIdFlowCell());
            flowCell.setCodeSequencingPlatform(codeStepNext.equals(Step.SEQ_RUN) ? SequencingPlatform.ILLUMINA_GAIIX_SEQUENCING_PLATFORM : SequencingPlatform.ILLUMINA_HISEQ_2000_SEQUENCING_PLATFORM);
            
            
            java.sql.Date flowCellDate = null;
            if (flowCellDateStr != null) {
              flowCellDate = this.parseDate(flowCellDateStr);
            } else {
              flowCellDate = new java.sql.Date(System.currentTimeMillis());
            }
            flowCell.setCreateDate(flowCellDate);

            if (flowCellRunNumberStr != null && !flowCellRunNumberStr.equals("")) {
              flowCell.setRunNumber(new Integer(flowCellRunNumberStr));
            }
            if (flowCellNumCyclesStr != null && !flowCellNumCyclesStr.equals("")) {
              flowCell.setNumberSequencingCyclesActual(new Integer(flowCellNumCyclesStr));
            }
            if (flowCellSide != null){
              flowCell.setSide(flowCellSide);
            }
            if (flowCellIdSeqRunTypeStr != null && !flowCellIdSeqRunTypeStr.equals("")) {
              flowCell.setIdSeqRunType(new Integer(flowCellIdSeqRunTypeStr));
            }
            if (flowCellIdInstrumentStr != null && !flowCellIdInstrumentStr.equals("")) {
              flowCell.setIdInstrument(new Integer(flowCellIdInstrumentStr));
            }

            String runFolder = flowCell.getRunFolderName(dh);
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

              channel.setFileName(runFolder);
              channel.setSampleConcentrationpM(parser.getSampleConcentrationpm(channelNumber));
              channel.setIsControl(parser.getIsControl(channelNumber));
              
              List channelContents = parser.getChannelContents(channelNumber);
              for (Iterator i1 = channelContents.iterator(); i1.hasNext();) {
                WorkItemSolexaAssembleParser.ChannelContent content = (WorkItemSolexaAssembleParser.ChannelContent)i1.next();
                
                if (content.getSequenceLane() != null) {
                  SequenceLane lane = content.getSequenceLane();

                  lane.setIdFlowCellChannel(channel.getIdFlowCellChannel());
                  if (flowCell.getIdSeqRunType() == null) {
                    flowCell.setIdSeqRunType(lane.getIdSeqRunType());
                  }
                  
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
              wi.setCodeStepNext(codeStepNext);
              wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
              sess.save(wi);

              sess.flush();
              
              
              channels.add(channel);

              
              laneNumber++;
                          
            }
            flowCell.setIdNumberSequencingCycles(idNumberSequencingCycles);
            flowCell.setFlowCellChannels(channels);
            if (flowCell.getNumberSequencingCyclesActual() == null) {
              flowCell.setNumberSequencingCyclesActual(maxCycles);
            }
            
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
            
            
          }
          
          if (this.dirtyWorkItemXMLString != null) {
            for(Iterator i = this.parser.getDirtyWorkItemList().iterator(); i.hasNext();) {
              WorkItem wi = (WorkItem)i.next();
              sess.save(wi);
            }
            sess.flush();
          }

          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          if (flowCell != null) {
            this.xmlResult = "<SUCCESS flowCellNumber='" + flowCell.getNumber() + "'/>";            
          } else {
            this.xmlResult = "<SUCCESS/>";            
          }
          
          setResponsePage(this.SUCCESS_JSP);          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        log.error("An exception has occurred in SaveWorkflowSolexaAssemble ", e);
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
    String rootDir = flowCellDir + "/" + createYear;
    
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