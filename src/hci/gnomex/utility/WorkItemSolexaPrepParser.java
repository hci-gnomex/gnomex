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


public class WorkItemSolexaPrepParser implements Serializable {
  
  private Document   doc;
  private Map        sampleMap = new HashMap();
  private List       workItems = new ArrayList();
  
  
  public WorkItemSolexaPrepParser(Document doc) {
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
      
      
      if (workItemNode.getAttributeValue("seqPrepStatus") != null && !workItemNode.getAttributeValue("seqPrepStatus").equals("")) {
        workItem.setStatus(workItemNode.getAttributeValue("seqPrepStatus"));
      } else {
        workItem.setStatus(null);
      }
      
      this.initializeSample(workItemNode, sample);
      
      sampleMap.put(workItem.getIdWorkItem(), sample);
      workItems.add(workItem);
    }
    
   
  }
  

  
  private void initializeSample(Element n, Sample sample) throws Exception {
    if (n.getAttributeValue("seqPrepStatus") != null && !n.getAttributeValue("seqPrepStatus").equals("")) {
      String status = n.getAttributeValue("seqPrepStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        sample.setSeqPrepDate(new java.sql.Date(System.currentTimeMillis()));      
        sample.setSeqPrepFailed("N");
        sample.setSeqPrepBypassed("N");
        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        sample.setSeqPrepDate(null);
        sample.setSeqPrepFailed("Y");
        sample.setSeqPrepBypassed("N");
        
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        sample.setSeqPrepDate(null);
        sample.setSeqPrepFailed("N");
        sample.setSeqPrepBypassed("Y");        
      }
    } else {
      sample.setSeqPrepDate(null);
      sample.setSeqPrepFailed("N");
      sample.setSeqPrepBypassed("N");
    }
    
    
    if (n.getAttributeValue("seqPrepLibConcentration") != null && !n.getAttributeValue("seqPrepLibConcentration").equals("")) {
      sample.setSeqPrepLibConcentration(new Integer(n.getAttributeValue("seqPrepLibConcentration")));
    } else {
      sample.setSeqPrepLibConcentration(null);
    }    
    
    if (n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType") != null && !n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType").equals("")) {
      sample.setSeqPrepQualCodeBioanalyzerChipType(n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType"));
    } else {
      sample.setSeqPrepQualCodeBioanalyzerChipType(null);
    }    
    
    if (n.getAttributeValue("seqPrepGelFragmentSizeFrom") != null && !n.getAttributeValue("seqPrepGelFragmentSizeFrom").equals("")) {
      sample.setSeqPrepGelFragmentSizeFrom(new Integer(n.getAttributeValue("seqPrepGelFragmentSizeFrom")));
    } else {
      sample.setSeqPrepGelFragmentSizeFrom(null);
    }    

    if (n.getAttributeValue("seqPrepGelFragmentSizeTo") != null && !n.getAttributeValue("seqPrepGelFragmentSizeTo").equals("")) {
      sample.setSeqPrepGelFragmentSizeTo(new Integer(n.getAttributeValue("seqPrepGelFragmentSizeTo")));
    } else {
      sample.setSeqPrepGelFragmentSizeTo(null);
    }    
    if (n.getAttributeValue("idSeqLibProtocol") != null && !n.getAttributeValue("idSeqLibProtocol").equals("")) {
      sample.setIdSeqLibProtocol(new Integer(n.getAttributeValue("idSeqLibProtocol")));
    } else {
      sample.setIdSeqLibProtocol(null);
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
