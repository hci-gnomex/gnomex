package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisLaneParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idSequenceLaneList = new ArrayList();
  
  public AnalysisLaneParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("SequenceLane").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idSequenceLaneString = node.getAttributeValue("idSequenceLane");
      idSequenceLaneList.add(new Integer(idSequenceLaneString));
    }
  }

  
  public List getIdSequenceLanes() {
    return idSequenceLaneList;
  }
}
