package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisHybParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idHybridizationList = new ArrayList();
  protected HashMap     idRequestMap = new HashMap();
  
  public AnalysisHybParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("Hybridization").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idHybridizationString = node.getAttributeValue("idHybridization");
      Integer idHybridization = new Integer(idHybridizationString);
      
      String idRequestString = node.getAttributeValue("idRequest");
      
      idHybridizationList.add(idHybridization);
      idRequestMap.put(idHybridization, new Integer(idRequestString));
    }
  }

  
  public List getIdHybridizations() {
    return idHybridizationList;
  }
  
  public Integer getIdRequest(Integer idHybridization) {
    return (Integer)idRequestMap.get(idHybridization);
  }
}
