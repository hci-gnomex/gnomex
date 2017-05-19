package hci.gnomex.controller;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Notification;
import hci.gnomex.model.NumberSequencingCyclesAllowed;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.WorkItem;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FlowCellChannelParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;

public class SaveFlowCell extends GNomExCommand implements Serializable {


  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveFlowCell.class);

  private String						channelsXMLString;
  private Document					channelsDoc;
  private FlowCellChannelParser		channelParser;
  private FlowCell					fc;
  private boolean						isNewFlowCell = false;
  private String						serverName;
  private String						launchAppURL;
  private String            lastCycleDateStr;
  private String            numberSequencingCyclesActualStr;
  private String						runFolder = null;

  public void validate() {}

  public void loadCommand(HttpServletRequest request, HttpSession session)
  {
    if (request.getParameter("lastCycleDate") != null && !request.getParameter("lastCycleDate").equals("")) {
      lastCycleDateStr = request.getParameter("lastCycleDate");
    }
    if (request.getParameter("numberSequencingCyclesActual") != null && !request.getParameter("numberSequencingCyclesActual").equals("")) {
      numberSequencingCyclesActualStr = request.getParameter("numberSequencingCyclesActual");
    }
    if (request.getParameter("runFolder") != null && !request.getParameter("runFolder").equals("")) {
      runFolder = request.getParameter("runFolder");
    }
    // flow cell as it exists in the request
    fc = new FlowCell();
    HashMap errors = this.loadDetailObject(request, fc);
    this.addInvalidFields(errors);

    if (fc.getIdFlowCell() == null || fc.getIdFlowCell().intValue() == 0) {
      isNewFlowCell = true;
    }

    if (request.getParameter("channelsXMLString") != null
        && !request.getParameter("channelsXMLString").equals("")) {
      channelsXMLString = request.getParameter("channelsXMLString");
    }

    StringReader reader = new StringReader(channelsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      channelsDoc = sax.build(reader);
      channelParser = new FlowCellChannelParser(channelsDoc);
    }
    catch (JDOMException je) {
      LOG.error("Cannot parse channelsXMLString", je);
      this.addInvalidField("channelsXMLString", "Invalid channelsXMLString");
    }
  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      if (this.getSecurityAdvisor().canUpdate(fc)) {
        channelParser.parse(sess); // updates changes to Channels and Sequence Lanes in database
        FlowCell flowCell = null;

        if (isNewFlowCell) {
          flowCell = fc;
          sess.save(flowCell);
        } else {
          flowCell = (FlowCell) sess.get(FlowCell.class, fc.getIdFlowCell()); // load flow cell from database
          initializeFlowCell(flowCell); // copy flow cell info from request into flow cell loaded from database
        }
        // Save updated sequence lanes
        for(Object key : channelParser.getChannelMap().keySet()) {
          FlowCellChannel fcc = (FlowCellChannel) channelParser.getChannelMap().get(key);
          Set seqLanes = fcc.getSequenceLanes();
          for(Object temp : seqLanes) {
            SequenceLane sl = (SequenceLane)temp; 
            if(sl.getIdNumberSequencingCyclesAllowed() != null && flowCell.getIdNumberSequencingCyclesAllowed() != null && 
                !sl.getIdNumberSequencingCyclesAllowed().equals(flowCell.getIdNumberSequencingCyclesAllowed())) {
              // Update Sequencing Protocol for Sequence Lanes if they were forced into a Flow Cell with a different protocol
              sl.setIdNumberSequencingCyclesAllowed(flowCell.getIdNumberSequencingCyclesAllowed());
              sl.setIdSeqRunType(flowCell.getIdSeqRunType());
              sl.setIdNumberSequencingCycles(flowCell.getIdNumberSequencingCycles());
              sess.save(sl);
            }


          }
        }

        //
        // Remove channels that belong to this FlowCell but were not sent in the request (meaning the user removed them)
        //
        TreeSet channelsToDelete = new TreeSet(new ChannelComparator());
        List requestNumbersToDelete = new ArrayList();

        for(Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext();) {
          FlowCellChannel existingChannel = (FlowCellChannel)i.next();
          if (!channelParser.getChannelMap().containsKey(existingChannel.getIdFlowCellChannel().toString())) {
            channelsToDelete.add(existingChannel);

            // Delete Work Items
            List workItems = sess.createQuery("SELECT x from WorkItem x where idFlowCellChannel = " + 
                existingChannel.getIdFlowCellChannel()).list();
            for (Iterator i1 = workItems.iterator(); i1.hasNext();) {
              WorkItem x = (WorkItem)i1.next();
              if(x.getCodeStepNext().equals("HSEQFINFC") || x.getCodeStepNext().equals("HSEQPIPE") || x.getCodeStepNext().equals("MISEQFINFC") || x.getCodeStepNext().equals("MISEQPIPE")) {
                for(Iterator ii = existingChannel.getSequenceLanes().iterator(); ii.hasNext();) {
                  SequenceLane sl = (SequenceLane)ii.next();
                  WorkItem wi = new WorkItem();
                  wi.setIdRequest(sl.getIdRequest());
                  wi.setSequenceLane(sl);
                  if(x.getCodeStepNext().equals("HSEQFINFC") || x.getCodeStepNext().equals("HSEQPIPE")) {
                    wi.setCodeStepNext("HSEQASSEM");  
                  } else if (x.getCodeStepNext().equals("MISEQFINFC") || x.getCodeStepNext().equals("MISEQPIPE")) {
                    wi.setCodeStepNext("MISEQASSEM");            		  
                  }
                  sess.save(wi);
                }

              }
              sess.delete(x);
            }

            // Dissociate Sequence Lanes from channel and grab the request numbers so that we can update the flowcell notes
            for (Iterator i2 = existingChannel.getSequenceLanes().iterator(); i2.hasNext();) {
              SequenceLane lane = (SequenceLane) i2.next();
              requestNumbersToDelete.add(lane.getSample().getRequest().getNumber());
              lane.setIdFlowCellChannel(null);
            }
          }
        }
        for (Iterator i = channelsToDelete.iterator(); i.hasNext();) {
          FlowCellChannel channelToDelete = (FlowCellChannel)i.next();
          flowCell.getFlowCellChannels().remove(channelToDelete);
        }        

        //        //Now compare the request numbers of the updated flowcell to the request numbers that we may need to delete FROM THE FlowCell NOTES
        //        //If there are no instances of the request number we need to delete in the existing request numbers list then
        //        //remove that request number from the flow cell notes.
        //        for(Iterator i = requestNumbersToDelete.iterator(); i.hasNext();){
        //          String requestNumDeleted = (String)i.next();
        //          int index = flowCell.getNotes().indexOf(requestNumDeleted);
        //          if(!existingRequestNums.contains(requestNumDeleted) && index != -1){
        //            requestNumDeleted += ",";
        //            flowCell.setNotes(flowCell.getNotes().replace(requestNumDeleted, ""));
        //            flowCell.setNotes(flowCell.getNotes().replace(requestNumDeleted.replace(",", ""), ""));
        //            flowCell.setNotes(flowCell.getNotes().trim());
        //          }
        //        }

        //
        // Build the run folder name for the channels.
        // Note this will be null if any of the parts for building
        // are not filled in.  That is a flag to NOT update the
        // folder name.
        //
        // runFolder is provided if SaveFlowCell called from FinalizeFlowCell screen
        // if called from EditFlowCell then it will need to be recreated here.
        if(runFolder == null || runFolder.equals("")) {
          runFolder = flowCell.getRunFolderName(dh);} 
        java.sql.Date lastCycleDate = null;
        if (lastCycleDateStr != null) {
          lastCycleDate = this.parseDate(lastCycleDateStr);
        }
        Integer numberSequencingCyclesActual = null;
        if (numberSequencingCyclesActualStr != null && numberSequencingCyclesActualStr.length() > 0) {
          numberSequencingCyclesActual = new Integer(numberSequencingCyclesActualStr);
        }

        //
        // Save channels
        // channelParser contains flow cell channels from the request, flowCell has channels from the database
        List newChannelRequestNums = new ArrayList();
        for (Iterator i = channelParser.getChannelMap().keySet().iterator(); i.hasNext();) {
          String idFlowCellChannelString = (String) i.next();
          FlowCellChannel fcc = 
            (FlowCellChannel) channelParser.getChannelMap().get(idFlowCellChannelString);
          if (runFolder != null) {
            fcc.setFileName(runFolder);
          }
          fcc.setLastCycleDate(lastCycleDate);
          fcc.setNumberSequencingCyclesActual(numberSequencingCyclesActual);

          boolean exists = false;
          for(Iterator i1 = flowCell.getFlowCellChannels().iterator(); i1.hasNext();) {
            FlowCellChannel existingChannel = (FlowCellChannel)i1.next();
            if (existingChannel.getIdFlowCellChannel().equals(fcc.getIdFlowCellChannel())) {
              exists = true;
            }
          }

          // New flow cell channel -- add it to the list
          if (!exists) {
            flowCell.getFlowCellChannels().add(fcc);
            for(Iterator i2 = fcc.getSequenceLanes().iterator(); i2.hasNext();) {
              SequenceLane sl = (SequenceLane)i2.next();
              newChannelRequestNums.add(sl.getSample().getRequest().getNumber());
            }
          }          
        }

        // Rebuild the Flow Cell's notes: request numbers followed by organism names
        //Grab all of the request numbers and oganism ids associated with the flowCell's channels
        TreeMap<String, Object> requestNums = new TreeMap<String,Object>();
        TreeMap<Integer, Object> idOrganisms = new TreeMap<Integer,Object>();
        for(Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext();){
          FlowCellChannel fc = (FlowCellChannel)i.next();
          for(Iterator i2 = fc.getSequenceLanes().iterator(); i2.hasNext();){
            SequenceLane lane = (SequenceLane)i2.next();
            requestNums.put(lane.getSample().getRequest().getNumber(),null);
            if(lane.getSample().getIdOrganism() != null) {
              idOrganisms.put(lane.getSample().getIdOrganism(),null);
            }
          }
        }        
        String notes = "";
        for(Iterator<String> i1 = requestNums.keySet().iterator(); i1.hasNext();) {
          String r = i1.next();
          notes += r;
          if(i1.hasNext()) {
            notes += ", ";
          } else {
            notes += " ";
          }
        }
        Boolean isFirst = true;
        for(Iterator<Integer> i2 = idOrganisms.keySet().iterator(); i2.hasNext();) {
          if(isFirst) {
            notes += "(";
            isFirst = false;
          }
          Integer idOrganism = i2.next();
          String organism = dh.getOrganism(idOrganism);
          notes += organism;
          if(i2.hasNext()) {
            notes += Constants.FILE_SEPARATOR;
          }        	
        }
        if(!isFirst) {
          notes += ")";
        }
        flowCell.setNotes(notes);


        //
        // Update work items
        // 
        //        ArrayList<String> slIDs = new ArrayList<String>();
        //        for (Iterator i1 = channelParser.getChannelMap().keySet().iterator(); i1.hasNext();) {
        //        	String idFlowCellChannelString = (String) i1.next();
        //            FlowCellChannel fcc = 
        //              (FlowCellChannel) channelParser.getChannelMap().get(idFlowCellChannelString);
        //            for(Iterator i2 = fcc.getSequenceLanes().iterator(); i2.hasNext();) {
        //            	SequenceLane sl = (SequenceLane)i2.next();
        //            	slIDs.add(sl.getIdSequenceLane().toString());
        //            }
        //        }

        // When a flow cell is finalized the work items attached to the flow cell's channels
        // need to have their codeStepNext updated from finalize to the next step.
        if(!channelParser.getChannelMap().isEmpty()){
          StringBuilder sb = new StringBuilder();
          sb.append("SELECT x from WorkItem x where idFlowCellChannel IN ");
          String slids = channelParser.getChannelMap().keySet().toString();
          slids = slids.replace('[', '(');
          slids = slids.replace(']',')');
          slids = slids.trim();
          sb.append(slids);
          List workItems = sess.createQuery(sb.toString()).list();
          for(Iterator i = workItems.iterator(); i.hasNext();) {
            WorkItem wi = (WorkItem)i.next();
            if(wi.getCodeStepNext().equals("HSEQFINFC")) {
              wi.setCodeStepNext("HSEQPIPE");
              sess.save(wi);
            } else if (wi.getCodeStepNext().equals("MISEQFINFC")) {
              wi.setCodeStepNext("MISEQPIPE");
              sess.save(wi);
            }
          }
        }

        if(isNewFlowCell){
          sendNotification(flowCell, sess, Notification.NEW_STATE, Notification.SOURCE_TYPE_USER, Notification.TYPE_FLOWCELL);
          sendNotification(flowCell, sess, Notification.NEW_STATE, Notification.SOURCE_TYPE_ADMIN, Notification.TYPE_FLOWCELL);
        } else{
          sendNotification(flowCell, sess, Notification.EXISTING_STATE, Notification.SOURCE_TYPE_USER, Notification.TYPE_FLOWCELL);
          sendNotification(flowCell, sess, Notification.EXISTING_STATE, Notification.SOURCE_TYPE_ADMIN, Notification.TYPE_FLOWCELL);
        }
        sess.flush();

        this.xmlResult = "<SUCCESS idFlowCell=\"" + flowCell.getIdFlowCell()
        + "\"/>";

        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions",
        "Insufficient permission to save flowCell.");
        setResponsePage(this.ERROR_JSP);
      }

    }
    catch (Exception e) {
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in SaveFlowCell ", e);
      throw new RollBackCommandException(e.getMessage());
    }

    return this;
  }
  // flowCell is loaded from database. fc is from the HttpRequest. Here we update flowCell with any new values from fc.
  private void initializeFlowCell(FlowCell flowCell) {
    flowCell.setNumber(fc.getNumber());
    flowCell.setCreateDate(fc.getCreateDate());
    flowCell.setNotes(fc.getNotes());
    //flowCell.setIdSeqRunType(fc.getIdSeqRunType());
    //flowCell.setIdNumberSequencingCycles(fc.getIdNumberSequencingCycles());
    flowCell.setBarcode(fc.getBarcode());
    flowCell.setCodeSequencingPlatform(fc.getCodeSequencingPlatform());
    flowCell.setRunNumber(fc.getRunNumber());
    flowCell.setIdInstrument(fc.getIdInstrument());
    flowCell.setSide(fc.getSide());
    flowCell.setIdCoreFacility(fc.getIdCoreFacility());
    flowCell.setIdNumberSequencingCyclesAllowed(fc.getIdNumberSequencingCyclesAllowed());

    for(Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.NumberSequencingCyclesAllowed").iterator(); i.hasNext();) {
      DictionaryEntry de = (DictionaryEntry)i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      NumberSequencingCyclesAllowed nsca = (NumberSequencingCyclesAllowed)de;
      if (nsca.getIdNumberSequencingCyclesAllowed().equals(fc.getIdNumberSequencingCyclesAllowed())) {
        flowCell.setIdSeqRunType(nsca.getIdSeqRunType());
        flowCell.setIdNumberSequencingCycles(nsca.getIdNumberSequencingCycles());
        break;
      }
    }
  }

  private class ChannelComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      FlowCellChannel u1 = (FlowCellChannel) o1;
      FlowCellChannel u2 = (FlowCellChannel) o2;

      return u1.getIdFlowCellChannel().compareTo(u2.getIdFlowCellChannel());
    }
  }
}
