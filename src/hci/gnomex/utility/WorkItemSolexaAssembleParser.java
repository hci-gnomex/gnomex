package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;
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


public class WorkItemSolexaAssembleParser implements Serializable {
  
  private Document   doc;
  private Map        laneMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemSolexaAssembleParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idSequenceLaneString   = workItemNode.getAttributeValue("idSequenceLane");
      String idWorkItemString = workItemNode.getAttributeValue("idWorkItem");
      
      SequenceLane lane = (SequenceLane)sess.load(SequenceLane.class, new Integer(idSequenceLaneString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      
      laneMap.put(workItem.getIdWorkItem(), lane);
      workItems.add(workItem);
    }
    
   
  }
  


  
  public SequenceLane getSequenceLane(Integer idWorkItem) {
    return (SequenceLane)laneMap.get(idWorkItem);
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


  


}
