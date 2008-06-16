package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.WorkItem;

import java.io.Serializable;
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
  private Map        flowCellMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemSolexaRunParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idFlowCellString   = workItemNode.getAttributeValue("idFlowCell");
      String idWorkItemString = workItemNode.getAttributeValue("idWorkItem");
      
      FlowCell flowCell = (FlowCell)sess.load(FlowCell.class, new Integer(idFlowCellString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      this.initializeFlowCell(sess, workItemNode, flowCell);
      
      flowCellMap.put(workItem.getIdWorkItem(), flowCell);
      workItems.add(workItem);
    }
    
   
  }
  


  
  public FlowCell getFlowCell(Integer idWorkItem) {
    return (FlowCell)flowCellMap.get(idWorkItem);
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

  
  protected void initializeFlowCell(Session sess, Element n, FlowCell flowCell) throws Exception {
    if (n.getAttributeValue("firstCycleStatus") != null && !n.getAttributeValue("firstCycleStatus").equals("")) {
      String status = n.getAttributeValue("firstCycleStatus");
      if (status.equals(Constants.STATUS_IN_PROGRESS)) {
        flowCell.setStartDate(new java.sql.Date(System.currentTimeMillis()));      
      } if (status.equals(Constants.STATUS_COMPLETED)) {
        flowCell.setFirstCycleDate(new java.sql.Date(System.currentTimeMillis()));      
        flowCell.setFirstCycleFailed("N");        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        flowCell.setFirstCycleDate(null);      
        flowCell.setFirstCycleFailed("Y");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        flowCell.setFirstCycleDate(null);      
        flowCell.setFirstCycleFailed("N");
      }
    } else {
      flowCell.setFirstCycleDate(null);      
      flowCell.setFirstCycleFailed("N");
    }
    if (n.getAttributeValue("lastCycleStatus") != null && !n.getAttributeValue("lastCycleStatus").equals("")) {
      String status = n.getAttributeValue("lastCycleStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        flowCell.setLastCycleDate(new java.sql.Date(System.currentTimeMillis()));      
        flowCell.setLastCycleFailed("N");        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        flowCell.setLastCycleDate(null);      
        flowCell.setLastCycleFailed("Y");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        flowCell.setLastCycleDate(null);      
        flowCell.setLastCycleFailed("N");
      }
    } else {
      flowCell.setLastCycleDate(null);      
      flowCell.setLastCycleFailed("N");
    }   
  }


}
