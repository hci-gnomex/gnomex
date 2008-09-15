package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Sample;
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


public class WorkItemSolexaStockParser implements Serializable {
  
  private Document   doc;
  private Map        sampleMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemSolexaStockParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idSampleString   = workItemNode.getAttributeValue("idSample");
      String idWorkItemString = workItemNode.getAttributeValue("idWorkItem");
      
      Sample sample = (Sample)sess.load(Sample.class, new Integer(idSampleString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      this.initializeSample(workItemNode, sample);
      
      sampleMap.put(workItem.getIdWorkItem(), sample);
      workItems.add(workItem);
    }
    
   
  }
  

  
  private void initializeSample(Element n, Sample sample) throws Exception {
    if (n.getAttributeValue("seqPrepStockStatus") != null && !n.getAttributeValue("seqPrepStockStatus").equals("")) {
      String status = n.getAttributeValue("seqPrepStockStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        sample.setSeqPrepStockDate(new java.sql.Date(System.currentTimeMillis()));      
        sample.setSeqPrepStockFailed("N");
        sample.setSeqPrepStockBypassed("N");
        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        sample.setSeqPrepStockDate(null);
        sample.setSeqPrepStockFailed("Y");
        sample.setSeqPrepStockBypassed("N");
        
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        sample.setSeqPrepStockDate(null);
        sample.setSeqPrepStockFailed("N");
        sample.setSeqPrepStockBypassed("Y");        
      }
    } else {
      sample.setSeqPrepStockDate(null);
      sample.setSeqPrepStockFailed("N");
      sample.setSeqPrepStockBypassed("N");
    }
  }

  
  public Sample getSample(Integer idWorkItem) {
    return (Sample)sampleMap.get(idWorkItem);
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
