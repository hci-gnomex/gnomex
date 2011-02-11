package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisLaneParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idSequenceLaneList = new ArrayList();
  protected HashMap     idRequestMap = new HashMap();
  
  public AnalysisLaneParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("SequenceLane").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
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

  
  public List getIdSequenceLanes() {
    return idSequenceLaneList;
  }
  
  public Integer getIdRequest(Integer idSequenceLane) {
    return (Integer)idRequestMap.get(idSequenceLane);
  }
  
}
