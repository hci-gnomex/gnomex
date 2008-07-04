package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisHybParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idHybridizationList = new ArrayList();
  
  public AnalysisHybParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("Hybridization").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idHybridizationString = node.getAttributeValue("idHybridization");
      idHybridizationList.add(new Integer(idHybridizationString));
    }
  }

  
  public List getIdHybridizations() {
    return idHybridizationList;
  }
}
