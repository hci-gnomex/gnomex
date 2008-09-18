package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.WorkItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class WorkItemSolexaRunParser implements Serializable {
  
  private Document   doc;
  private Map        flowCellChannelMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemSolexaRunParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idFlowCellChannelString   = workItemNode.getAttributeValue("idFlowCellChannel");
      String idWorkItemString = workItemNode.getAttributeValue("idWorkItem");
      
      FlowCellChannel channel = (FlowCellChannel)sess.load(FlowCellChannel.class, new Integer(idFlowCellChannelString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      this.initializeFlowCellChannel(sess, workItemNode, channel);
      
      flowCellChannelMap.put(workItem.getIdWorkItem(), channel);
      workItems.add(workItem);
    }
    
   
  }
  


  
  public FlowCellChannel getFlowCellChannel(Integer idWorkItem) {
    return (FlowCellChannel)flowCellChannelMap.get(idWorkItem);
  }
  
  public List getWorkItems() {
    return workItems;
  }
  
  
  public void resetIsDirty() {
    Element workItemListNode = this.doc.getRootElement();
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      workItemNode.setAttribute("isDirty", "N");
    }
  }

  
  protected void initializeFlowCellChannel(Session sess, Element n, FlowCellChannel channel) throws Exception {
    if (n.getAttributeValue("firstCycleStatus") != null && !n.getAttributeValue("firstCycleStatus").equals("")) {
      String status = n.getAttributeValue("firstCycleStatus");
      if (status.equals(Constants.STATUS_IN_PROGRESS)) {
        channel.setStartDate(new java.sql.Date(System.currentTimeMillis()));      
      } if (status.equals(Constants.STATUS_COMPLETED)) {
        channel.setFirstCycleDate(new java.sql.Date(System.currentTimeMillis()));      
        channel.setFirstCycleFailed("N");        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        channel.setFirstCycleDate(null);      
        channel.setFirstCycleFailed("Y");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        channel.setFirstCycleDate(null);      
        channel.setFirstCycleFailed("N");
      }
    } else {
      channel.setFirstCycleDate(null);      
      channel.setFirstCycleFailed("N");
    }
    if (n.getAttributeValue("lastCycleStatus") != null && !n.getAttributeValue("lastCycleStatus").equals("")) {
      String status = n.getAttributeValue("lastCycleStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        channel.setLastCycleDate(new java.sql.Date(System.currentTimeMillis()));      
        channel.setLastCycleFailed("N");        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        channel.setLastCycleDate(null);      
        channel.setLastCycleFailed("Y");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        channel.setLastCycleDate(null);      
        channel.setLastCycleFailed("N");
      }
    } else {
      channel.setLastCycleDate(null);      
      channel.setLastCycleFailed("N");
    }   
    
    
    if (n.getAttributeValue("numberSequencingCyclesActual") != null && !n.getAttributeValue("numberSequencingCyclesActual").equals("")) {
      channel.setNumberSequencingCyclesActual(new Integer(n.getAttributeValue("numberSequencingCyclesActual")));
    } else {
      channel.setNumberSequencingCyclesActual(null);
    }
    
    if (n.getAttributeValue("clustersPerTile") != null && !n.getAttributeValue("clustersPerTile").equals("")) {
      channel.setClustersPerTile(new Integer(n.getAttributeValue("clustersPerTile").replaceAll(",", "")));
    } else {
      channel.setClustersPerTile(null);
    }

    if (n.getAttributeValue("fileName") != null && !n.getAttributeValue("fileName").equals("")) {
      channel.setFileName(n.getAttributeValue("fileName"));
    } else {
      channel.setFileName(null);
    }

  }


}
