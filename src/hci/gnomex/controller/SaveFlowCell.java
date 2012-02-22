package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
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
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SaveFlowCell extends GNomExCommand implements Serializable {


	// the static field for logging in Log4J
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveFlowCell.class);

	private String						channelsXMLString;
	private Document					channelsDoc;
	private FlowCellChannelParser		channelParser;
	private FlowCell					fc;
	private boolean						isNewFlowCell = false;
	private String						serverName;
	private String						launchAppURL;
	private String            lastCycleDateStr;
	
	public void validate() {}

  public void loadCommand(HttpServletRequest request, HttpSession session)
  {
    if (request.getParameter("lastCycleDate") != null && !request.getParameter("lastCycleDate").equals("")) {
      lastCycleDateStr = request.getParameter("lastCycleDate");
    }

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
      log.error("Cannot parse channelsXMLString", je);
      this.addInvalidField("channelsXMLString", "Invalid channelsXMLString");
    }
  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      if (this.getSecurityAdvisor().canUpdate(fc)) {
        channelParser.parse(sess);
        FlowCell flowCell = null;

        if (isNewFlowCell) {
          flowCell = fc;
          sess.save(flowCell);
        } else {
          flowCell = (FlowCell) sess.get(FlowCell.class, fc.getIdFlowCell());
          initializeFlowCell(flowCell);
        }

        //
        // Remove channels
        //
        TreeSet channelsToDelete = new TreeSet(new ChannelComparator());
        for(Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext();) {
          FlowCellChannel existingChannel = (FlowCellChannel)i.next();
          if (!channelParser.getChannelMap().containsKey(existingChannel.getIdFlowCellChannel().toString())) {
            channelsToDelete.add(existingChannel);
            
            // Delete Work Items
            List workItems = sess.createQuery("SELECT x from WorkItem x where idFlowCellChannel = " + 
                existingChannel.getIdFlowCellChannel()).list();
            for (Iterator i1 = workItems.iterator(); i1.hasNext();) {
              WorkItem x = (WorkItem)i1.next();
              sess.delete(x);
            }
            
            // Dissociate Sequence Lanes from channel 
            for (Iterator i2 = existingChannel.getSequenceLanes().iterator(); i2.hasNext();) {
              SequenceLane lane = (SequenceLane) i2.next();
              lane.setIdFlowCellChannel(null);
            }
          }
        }
        for (Iterator i = channelsToDelete.iterator(); i.hasNext();) {
          FlowCellChannel channelToDelete = (FlowCellChannel)i.next();
          flowCell.getFlowCellChannels().remove(channelToDelete);
        }

        //
        // Build the run folder name for the channels.
        //
        String runFolder = flowCell.getRunFolderName(dh);
        java.sql.Date lastCycleDate = null;
        if (lastCycleDateStr != null) {
          lastCycleDate = this.parseDate(lastCycleDateStr);
        }

        //
        // Save channels
        //
        for (Iterator i = channelParser.getChannelMap().keySet().iterator(); i.hasNext();) {
          String idFlowCellChannelString = (String) i.next();
          FlowCellChannel fcc = 
            (FlowCellChannel) channelParser.getChannelMap().get(idFlowCellChannelString);
          fcc.setFileName(runFolder);
          fcc.setLastCycleDate(lastCycleDate);
          
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
          }
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
      log.error("An exception has occurred in SaveFlowCell ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }
    finally {
      try {
        HibernateSession.closeSession();
      } 
      catch (Exception e) {}
    }
    return this;
  }

  private void initializeFlowCell(FlowCell flowCell) {
    flowCell.setNumber(fc.getNumber());
    flowCell.setCreateDate(fc.getCreateDate());
    flowCell.setNotes(fc.getNotes());
    flowCell.setIdSeqRunType(fc.getIdSeqRunType());
    flowCell.setIdNumberSequencingCycles(fc.getIdNumberSequencingCycles());
    flowCell.setBarcode(fc.getBarcode());
    flowCell.setCodeSequencingPlatform(fc.getCodeSequencingPlatform());
    flowCell.setRunNumber(fc.getRunNumber());
    flowCell.setIdInstrument(fc.getIdInstrument());
    flowCell.setNumberSequencingCyclesActual(fc.getNumberSequencingCyclesActual());
    flowCell.setSide(fc.getSide());
  }

  private class ChannelComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      FlowCellChannel u1 = (FlowCellChannel) o1;
      FlowCellChannel u2 = (FlowCellChannel) o2;
      
      return u1.getIdFlowCellChannel().compareTo(u2.getIdFlowCellChannel());
    }
  }
}