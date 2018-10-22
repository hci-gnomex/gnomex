package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class AnalysisLaneParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idSequenceLaneList = new ArrayList();
  protected List        idSampleList = new ArrayList();
  protected HashMap     idRequestMap = new HashMap();
  
  public AnalysisLaneParser(Document doc) {
    this.doc = doc;
 
  }
  
  
  public void parse(Session sess, boolean isBatchMode, boolean isLinkBySample) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("SequenceLane").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      if (isBatchMode) {
        String seqLaneNumber = node.getAttributeValue("number");
        SequenceLane seqLane = (SequenceLane)sess.createQuery("SELECT l from SequenceLane l where number = '" + seqLaneNumber + "'").uniqueResult();
        if (seqLane == null) {
          throw new RuntimeException("Cannot find sequence lane " + seqLaneNumber);
        }
        idSequenceLaneList.add(seqLane.getIdSequenceLane());
        idRequestMap.put(seqLane.getIdSequenceLane(), seqLane.getIdRequest());
      } else {
        String idSequenceLaneString = node.getAttributeValue("idSequenceLane");
        Integer idSequenceLane = new Integer(idSequenceLaneString);
        idSequenceLaneList.add(idSequenceLane);

        String idRequestString = node.getAttributeValue("idRequest");
        if (idRequestString == null || idRequestString.equals("")) {
          // idRequest wasn't provided on the XML element, so look up the
          // idSequenceLane to get to the request.
          SequenceLane lane = (SequenceLane)sess.load(SequenceLane.class, Integer.valueOf(idSequenceLane));
          idRequestMap.put(idSequenceLane, lane.getIdRequest());
          
        } else {
          // The idRequest was provided on the XML element, so just use it to save
          // the extra read
          idRequestMap.put(idSequenceLane, new Integer(idRequestString));

        }
        
      }


    }
    
    for(Iterator i = root.getChildren("Experiment").iterator(); i.hasNext();) {
      Element node = (Element)i.next();

      if (isBatchMode) {
        String experimentNumber = node.getAttributeValue("number");
        if (!isLinkBySample){
          List<Object[]> rows = (List<Object[]>) sess.createQuery("SELECT r.id, l.id from Request r join r.sequenceLanes l where r.number = '" + experimentNumber + "'").list();
          if (rows == null || rows.size() == 0) {
            throw new RuntimeException("Cannot find experiment when joining sequence lanes " + experimentNumber);
          }
          for (Object[] row : rows) {
            Integer idRequest = (Integer) row[0];
            Integer idSequenceLane = (Integer) row[1];
            idSequenceLaneList.add(idSequenceLane);
            idRequestMap.put(idSequenceLane, idRequest);
          }
        }else{
          List<Object[]> rows = (List<Object[]>) sess.createQuery("SELECT r.id, s.id from Request r join r.samples s where r.number = '" + experimentNumber + "'").list();
          if(rows == null || rows.size() == 0){
            throw new RuntimeException("Cannot find experiment when joining sample " + experimentNumber);
          }
          for (Object[] row : rows) {
            Integer idRequest = (Integer) row[0];
            Integer idSample = (Integer) row[1];
            idSampleList.add(idSample);
            idRequestMap.put(idSample, idRequest);
          }

        }

      }
    }
    
    for(Iterator i = root.getChildren("Sample").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      if (isBatchMode) {
        String sampleNumber = node.getAttributeValue("number");
        //List<Object[]> rows = (List<Object[]>)sess.createQuery("SELECT r.idRequest, l.idSequenceLane from Request r join r.sequenceLanes l join l.sample s where s.number = '" + sampleNumber + "'").list();
        List<Object[]> rows = (List<Object[]>)sess.createQuery("SELECT r.idRequest, l.idSample from Request r join r.sequenceLanes l join l.sample s where s.number = '" + sampleNumber + "'").list();

        if (rows == null || rows.size() == 0) {
          throw new RuntimeException("Cannot find sample  " + sampleNumber);
        }
        for (Object[] row : rows) {
          Integer idRequest = (Integer)row[0];
          Integer idSequenceLane = (Integer)row[1];
          idSequenceLaneList.add(idSequenceLane);
          idRequestMap.put(idSequenceLane, idRequest);
        }
      }
    }

  }

  
  public List getIdSequenceLanes() {
    return idSequenceLaneList;
  }
  public List getIdSamples() {
    return idSampleList;
  }
  
  public Integer getIdRequest(Integer idSequenceLane) {
    return (Integer)idRequestMap.get(idSequenceLane);
  }
  
}
