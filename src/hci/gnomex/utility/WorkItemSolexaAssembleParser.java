package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
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
  private List       flowCellChannelContents = new ArrayList();
  private Map        sampleConcentrationMap = new HashMap();
  private Map        sampleConcentrationForControlMap = new HashMap();
  private Map        workItemMap = new HashMap();
  
  
  public WorkItemSolexaAssembleParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      if (node.getName().equals("WorkItem")) {
        String idSequenceLaneString = node.getAttributeValue("idSequenceLane");
        String idWorkItemString     = node.getAttributeValue("idWorkItem");
        
        SequenceLane lane = (SequenceLane)sess.load(SequenceLane.class, new Integer(idSequenceLaneString));
        WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
        flowCellChannelContents.add(lane);
        workItemMap.put(lane.getIdSequenceLane(), workItem);
        sampleConcentrationMap.put(lane.getIdSequenceLane(), node.getAttributeValue("sampleConcentrationpM"));
        
      } else {
        String idSequencingControlString = node.getAttributeValue("idSequencingControl");
        SequencingControl control = (SequencingControl)sess.load(SequencingControl.class, new Integer(idSequencingControlString));
        flowCellChannelContents.add(control);
        sampleConcentrationForControlMap.put(control.getIdSequencingControl(), node.getAttributeValue("sampleConcentrationpM"));
      }
      
      
    }
    
   
  }
  


  
  
  public void resetIsDirty() {
    Element workItemListNode = this.doc.getRootElement();
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      workItemNode.setAttribute("isDirty", "N");
    }
  }

  
  public List getFlowCellChannelContents() {
    return flowCellChannelContents;
  }

  public WorkItem getWorkItem(Integer idSequenceLane) {
    return (WorkItem)workItemMap.get(idSequenceLane);
  }
  
  public Integer getSampleConcentrationpm(Integer idSequenceLane) {
    String sc = (String)sampleConcentrationMap.get(idSequenceLane);
    if (sc != null && !sc.equals("")) {
      return new Integer(sc);
    } else {
      return null;
    }
  }
  
  public Integer getSampleConcentrationpmForControl(Integer idSequencingControl) {
    String sc = (String)sampleConcentrationForControlMap.get(idSequencingControl);
    if (sc != null && !sc.equals("")) {
      return new Integer(sc);
    } else {
      return null;
    }
  }
  


}
