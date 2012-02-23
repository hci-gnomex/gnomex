
package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class FlowCellChannelParser extends DetailObject implements Serializable
{

  private Document doc;
  private Map      channelMap = new HashMap();

  public FlowCellChannelParser(Document doc) {
    this.doc = doc;
  }

  public void init() {
    channelMap = new HashMap();
  }

  public void parse(Session sess) throws Exception {
    FlowCellChannel channel = new FlowCellChannel();
    Element root = this.doc.getRootElement();

    for (Iterator i = root.getChildren("FlowCellChannel").iterator(); i.hasNext();) {
      Boolean isNewChannel = false;
      Map sequenceLaneMap = new HashMap();
      Element node = (Element) i.next();

      String idFlowCellChannelString = node.getAttributeValue("idFlowCellChannel");

      if (idFlowCellChannelString.startsWith("FlowCellChannel")
          || idFlowCellChannelString.equals("")) {
        
        isNewChannel = true;
        channel = new FlowCellChannel();
        channel.setSequenceLanes(new TreeSet(new LaneComparator()));
        
      } else {
        isNewChannel = false;
        channel = (FlowCellChannel) sess.get(FlowCellChannel.class,
            Integer.parseInt(idFlowCellChannelString));
      }

      this.initializeFlowCellChannel(sess, node, channel);

      if (node.getChild("sequenceLanes") != null
          && !node.getChild("sequenceLanes").getChildren("SequenceLane").isEmpty()) {
        
        for (Iterator i1 = node.getChild("sequenceLanes").getChildren(
            "SequenceLane").iterator(); i1.hasNext();) {
          Boolean isNewLane = false;
          SequenceLane sl = new SequenceLane();

          Element sequenceLaneNode = (Element) i1.next();
          String idSequenceLaneString = sequenceLaneNode.getAttributeValue("idSequenceLane");

          if (idSequenceLaneString.startsWith("SequenceLane")
              || idSequenceLaneString.equals("")) {
            
            isNewLane = true;
            sl = new SequenceLane();
          } else {
            isNewLane = false;
            sl = (SequenceLane) sess.get(SequenceLane.class,
                Integer.parseInt(idSequenceLaneString));
          }

          sl.setIdFlowCellChannel(channel.getIdFlowCellChannel());

          if (isNewLane) {
            sess.save(sl);
            idSequenceLaneString = sl.getIdSequenceLane().toString();
          }
          sequenceLaneMap.put(idSequenceLaneString, sl);
        }
      }

      //
      // Remove lanes
      //
      if (channel.getSequenceLanes() != null
          || !channel.getSequenceLanes().isEmpty()) {

        TreeSet lanesToDelete = new TreeSet(new LaneComparator());
        for (Iterator i2 = channel.getSequenceLanes().iterator(); i2.hasNext();) {
          SequenceLane existingLane = (SequenceLane) i2.next();
          if (!sequenceLaneMap.containsKey(existingLane.getIdSequenceLane().toString())) {
            lanesToDelete.add(existingLane);
          }
        }
        for (Iterator i2 = lanesToDelete.iterator(); i2.hasNext();) {
          SequenceLane laneToDelete = (SequenceLane) i2.next();
          channel.getSequenceLanes().remove(laneToDelete);
          laneToDelete.setIdFlowCellChannel(null);
        }
      }

      //
      // Save lanes
      //
      for (Iterator i2 = sequenceLaneMap.keySet().iterator(); i2.hasNext();) {
        String idLaneString = (String) i2.next();
        SequenceLane sl = (SequenceLane) sequenceLaneMap.get(idLaneString);

        boolean exists = false;
        if (!isNewChannel) {
          if (channel.getSequenceLanes() != null
              || !channel.getSequenceLanes().isEmpty()) {
            
            for (Iterator i3 = channel.getSequenceLanes().iterator(); i3.hasNext();) {
              SequenceLane existingLane = (SequenceLane) i3.next();
              
              if (existingLane.getIdSequenceLane().equals(
                  sl.getIdSequenceLane())) {
                
                exists = true;
              }
            }
          }
        }
        // New sequence lane -- add it to the list
        if (!exists) {
          channel.getSequenceLanes().add(sl);
        }
      }

      sess.flush();

      if (isNewChannel) {
        sess.save(channel);
        idFlowCellChannelString = channel.getIdFlowCellChannel().toString();
      }
      channelMap.put(idFlowCellChannelString, channel);
    }
  }

  protected void initializeFlowCellChannel(Session sess, Element n,
      FlowCellChannel channel) throws Exception {
    
    if (n.getAttributeValue("number") != null
        && !n.getAttributeValue("number").equals("")) {
      
      channel.setNumber(new Integer(n.getAttributeValue("number")));
    } else {
      channel.setNumber(null);
    }
    if (n.getAttributeValue("idFlowCell") != null
        && !n.getAttributeValue("idFlowCell").equals("")) {
      
      channel.setIdFlowCell(new Integer(n.getAttributeValue("idFlowCell")));
    }
    if (n.getAttributeValue("idSequencingControl") != null
        && !n.getAttributeValue("idSequencingControl").equals("")) {
      
      channel.setIdSequencingControl(new Integer(
          n.getAttributeValue("idSequencingControl")));
    } else{
      channel.setIdSequencingControl(null);
    }
    if (n.getAttributeValue("startDate") != null
        && !n.getAttributeValue("startDate").equals("")){
      
      channel.setStartDate(this.parseDate(n.getAttributeValue("startDate")));
    } else {
      channel.setStartDate(null);
    }
    if (n.getAttributeValue("firstCycleDate") != null
        && !n.getAttributeValue("firstCycleDate").equals("")){
      
      channel.setFirstCycleDate(this.parseDate(n.getAttributeValue("firstCycleDate")));
    } else {
      channel.setFirstCycleDate(null);
    }
    if (n.getAttributeValue("firstCycleDate") != null
        && !n.getAttributeValue("firstCycleDate").equals("")){
      
      channel.setFirstCycleDate(this.parseDate(n.getAttributeValue("firstCycleDate")));
    } else {
      channel.setFirstCycleDate(null);
    }
    if (n.getAttributeValue("firstCycleFailed") != null
        && !n.getAttributeValue("firstCycleFailed").equals("")){
      
      channel.setFirstCycleFailed(n.getAttributeValue("firstCycleFailed"));
    }
    if (n.getAttributeValue("lastCycleDate") != null
        && !n.getAttributeValue("lastCycleDate").equals("")){
      
      channel.setLastCycleDate(this.parseDate(n.getAttributeValue("lastCycleDate")));
    } else {
      channel.setLastCycleDate(null);
    }
    if (n.getAttributeValue("lastCycleFailed") != null
        && !n.getAttributeValue("lastCycleFailed").equals("")){
      
      channel.setLastCycleFailed(n.getAttributeValue("lastCycleFailed"));
    }
    if (n.getAttributeValue("clustersPerTile") != null
        && !n.getAttributeValue("clustersPerTile").equals("")){
      
      channel.setClustersPerTile(new Integer(
          n.getAttributeValue("clustersPerTile")));
    } else {
      channel.setClustersPerTile(null);
    }
    if (n.getAttributeValue("fileName") != null
        && !n.getAttributeValue("fileName").equals("")){
      
      channel.setFileName(n.getAttributeValue("fileName"));
    }
    if (n.getAttributeValue("sampleConcentrationpM") != null
        && !n.getAttributeValue("sampleConcentrationpM").equals("")){
      
      channel.setSampleConcentrationpM(new BigDecimal(
          n.getAttributeValue("sampleConcentrationpM")));
    } else {
      channel.setSampleConcentrationpM(null);
    }
    if (n.getAttributeValue("numberSequencingCyclesActual") != null
        && !n.getAttributeValue("numberSequencingCyclesActual").equals("")){
      
      channel.setNumberSequencingCyclesActual(new Integer(
          n.getAttributeValue("numberSequencingCyclesActual")));
    } else {
      channel.setNumberSequencingCyclesActual(null);
    }
    if (n.getAttributeValue("pipelineDate") != null
        && !n.getAttributeValue("pipelineDate").equals("")){
      
      channel.setPipelineDate(this.parseDate(n.getAttributeValue("pipelineDate")));
    } else {
      channel.setPipelineDate(null);
    }
    if (n.getAttributeValue("pipelineFailed") != null
        && !n.getAttributeValue("pipelineFailed").equals("")){
      
      channel.setPipelineFailed(n.getAttributeValue("pipelineFailed"));
    }
    if (n.getAttributeValue("isControl") != null
        && !n.getAttributeValue("isControl").equals("")){
      
      channel.setIsControl(n.getAttributeValue("isControl"));
    }
    if (n.getAttributeValue("phiXErrorRate") != null
        && !n.getAttributeValue("phiXErrorRate").equals("")){
      
      channel.setPhiXErrorRate(new BigDecimal(
          n.getAttributeValue("phiXErrorRate")));
    } else {
      channel.setPhiXErrorRate(null);
    }
    if (n.getAttributeValue("read1ClustersPassedFilterM") != null
        && !n.getAttributeValue("read1ClustersPassedFilterM").equals("")){
      
      channel.setRead1ClustersPassedFilterM(new Integer(
          n.getAttributeValue("read1ClustersPassedFilterM")));
    } else {
      channel.setRead1ClustersPassedFilterM(null);
    }
    if (n.getAttributeValue("q30PercentForDisplay") != null
        && !n.getAttributeValue("q30PercentForDisplay").equals("")){
      
      channel.setQ30Percent(new BigDecimal(n.getAttributeValue("q30PercentForDisplay")).movePointLeft(2));
    } else {
      channel.setQ30Percent(null);
    }
  }

  public Map getChannelMap() {
    return channelMap;
  }

  public void setChannelMap(Map channelMap) {
    this.channelMap = channelMap;
  }

  private class LaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      SequenceLane u1 = (SequenceLane) o1;
      SequenceLane u2 = (SequenceLane) o2;
      return u1.getIdSequenceLane().compareTo(u2.getIdSequenceLane());
    }
  }
}
