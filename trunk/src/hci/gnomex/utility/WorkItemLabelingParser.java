package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.LabeledSample;
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


public class WorkItemLabelingParser implements Serializable {
  
  private Document   doc;
  private Map        labeledSampleMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemLabelingParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws InvalidValueException{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idLabeledSampleString = workItemNode.getAttributeValue("idLabeledSample");
      String idWorkItemString      = workItemNode.getAttributeValue("idWorkItem");
      
      LabeledSample labeledSample = (LabeledSample)sess.load(LabeledSample.class, new Integer(idLabeledSampleString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      this.initializeLabeledSample(workItemNode, labeledSample);
      
      labeledSampleMap.put(workItem.getIdWorkItem(), labeledSample);
      workItems.add(workItem);
    }
    
   
  }
  
  private void initializeLabeledSample(Element n, LabeledSample labeledSample) throws InvalidValueException {
    if (n.getAttributeValue("labelingStatus") != null && !n.getAttributeValue("labelingStatus").equals("")) {
      String status = n.getAttributeValue("labelingStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        labeledSample.setLabelingDate(new java.sql.Date(System.currentTimeMillis()));      
        labeledSample.setLabelingFailed("N");
        labeledSample.setLabelingBypassed("N");
        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        labeledSample.setLabelingDate(null);      
        labeledSample.setLabelingFailed("Y");
        labeledSample.setLabelingBypassed("N");
        
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        labeledSample.setLabelingDate(null);      
        labeledSample.setLabelingFailed("N");
        labeledSample.setLabelingBypassed("Y");
      }
    } else {
      labeledSample.setLabelingDate(null);      
      labeledSample.setLabelingFailed("N");
      labeledSample.setLabelingBypassed("N");
    }
    
    if (n.getAttributeValue("labelingYield") != null && !n.getAttributeValue("labelingYield").equals("")) {
      labeledSample.setLabelingYield(new BigDecimal(n.getAttributeValue("labelingYield")));
    } else {
      labeledSample.setLabelingYield(null);
    }
    
    if (n.getAttributeValue("idLabelingProtocol") != null && !n.getAttributeValue("idLabelingProtocol").equals("")) {
      labeledSample.setIdLabelingProtocol(new Integer(n.getAttributeValue("idLabelingProtocol")));
    } else {
      labeledSample.setIdLabelingProtocol(null);
    }
    
    if (n.getAttributeValue("codeLabelingReactionSize") != null && !n.getAttributeValue("codeLabelingReactionSize").equals("")) {
      labeledSample.setCodeLabelingReactionSize(n.getAttributeValue("codeLabelingReactionSize"));
    } else {
      labeledSample.setCodeLabelingReactionSize(null);
    }
 
    if (n.getAttributeValue("numberOfReactions") != null && !n.getAttributeValue("numberOfReactions").equals("")) {
      labeledSample.setNumberOfReactions(new Integer(n.getAttributeValue("numberOfReactions")));
    } else {
      labeledSample.setNumberOfReactions(null);
    }
    
  }

  public LabeledSample getLabeledSample(Integer idWorkItem) {
    return (LabeledSample)labeledSampleMap.get(idWorkItem);
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
