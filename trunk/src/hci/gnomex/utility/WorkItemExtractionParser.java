package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Slide;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class WorkItemExtractionParser extends WorkItemHybParser implements Serializable {
  
 
  public WorkItemExtractionParser(Document doc) {
    super(doc);
 
  }
  
  
  protected void initializeHybridization(Session sess, Element n, Hybridization hyb) throws Exception {
    if (n.getAttributeValue("extractionStatus") != null && !n.getAttributeValue("extractionStatus").equals("")) {
      String status = n.getAttributeValue("extractionStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hyb.setExtractionDate(new java.sql.Date(System.currentTimeMillis()));      
        hyb.setExtractionFailed("N");
        hyb.setExtractionBypassed("N");
        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hyb.setExtractionDate(null);      
        hyb.setExtractionFailed("Y");
        hyb.setExtractionBypassed("N");
        
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hyb.setExtractionDate(null);      
        hyb.setExtractionFailed("N");
        hyb.setExtractionBypassed("Y");
      }
    } else {
      hyb.setExtractionDate(null);      
      hyb.setExtractionFailed("N");
      hyb.setExtractionBypassed("N");
    }
    
    
    if (n.getAttributeValue("idScanProtocol") != null && !n.getAttributeValue("idScanProtocol").equals("")) {
      hyb.setIdScanProtocol(new Integer(n.getAttributeValue("idScanProtocol")));
    } else {
      hyb.setIdScanProtocol(null);
    }
    
    if (n.getAttributeValue("idFeatureExtractionProtocol") != null && !n.getAttributeValue("idFeatureExtractionProtocol").equals("")) {
      hyb.setIdFeatureExtractionProtocol(new Integer(n.getAttributeValue("idFeatureExtractionProtocol")));
    } else {
      hyb.setIdFeatureExtractionProtocol(null);
    }
  }

}
