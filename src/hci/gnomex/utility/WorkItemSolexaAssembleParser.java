package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
import hci.gnomex.model.WorkItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class WorkItemSolexaAssembleParser implements Serializable {
  
  private Document   doc;
  private TreeMap    channelContentMap = new TreeMap();
  private TreeMap    channelConcentrationMap = new TreeMap();
  
  
  public WorkItemSolexaAssembleParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      String channelNumber        = node.getAttributeValue("channelNumber");
      ChannelContent cc = new ChannelContent();
      String sampleConcentration  = node.getAttributeValue("sampleConcentrationpM");
      
      if (node.getName().equals("WorkItem")) {
        String idSequenceLaneString = node.getAttributeValue("idSequenceLane");
        String idWorkItemString     = node.getAttributeValue("idWorkItem");
        
        SequenceLane lane = (SequenceLane)sess.load(SequenceLane.class, new Integer(idSequenceLaneString));
        WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
        
        cc.setSequenceLane(lane);
        cc.setWorkItem(workItem);
      } else {
        String idSequencingControlString = node.getAttributeValue("idSequencingControl");
        SequencingControl control = (SequencingControl)sess.load(SequencingControl.class, new Integer(idSequencingControlString));
        cc.setSequenceControl(control);
      }
      
      List channelContents = (List)channelContentMap.get(channelNumber);
      if (channelContents == null) {
        channelContents = new ArrayList();
      }
      channelContents.add(cc);
      channelContentMap.put(channelNumber, channelContents);
      
      if (sampleConcentration != null && !sampleConcentration.equals("")) {
          channelConcentrationMap.put(channelNumber, sampleConcentration);    	  
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

  
  public Set getChannelNumbers() {
    return channelContentMap.keySet();
  }
  
  public List getChannelContents(String channelNumber) {
    return (List)channelContentMap.get(channelNumber);
  }

  public List getWorkItems(String channelNumber) {
    List channelContents =  (List)channelContentMap.get(channelNumber);
    List workItems = new ArrayList();
    for(Iterator i = channelContents.iterator(); i.hasNext();) {
      ChannelContent cc = (ChannelContent)i.next();
      if (cc.getWorkItem() != null) {
        workItems.add(cc.getWorkItem());
      }
    }
    return workItems;
  }
  
  public BigDecimal getSampleConcentrationpm(String channelNumber) {
    String sc = (String)channelConcentrationMap.get(channelNumber);
    if (sc != null && !sc.equals("")) {
      return new BigDecimal(sc);
    } else {
      return null;
    }
  }
  
  
  public static class ChannelContent implements Serializable{
    private SequenceLane sequenceLane;
    private SequencingControl sequenceControl;
    private WorkItem workItem;

    
    public SequenceLane getSequenceLane() {
      return sequenceLane;
    }
    
    public void setSequenceLane(SequenceLane sequenceLane) {
      this.sequenceLane = sequenceLane;
    }
    
    public SequencingControl getSequenceControl() {
      return sequenceControl;
    }
    
    public void setSequenceControl(SequencingControl sequenceControl) {
      this.sequenceControl = sequenceControl;
    }
    
    public WorkItem getWorkItem() {
      return workItem;
    }
    
    public void setWorkItem(WorkItem workItem) {
      this.workItem = workItem;
    }
  }


}
