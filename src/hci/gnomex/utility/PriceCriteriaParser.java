package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.PriceCriteria;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class PriceCriteriaParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        priceCriteriaMap = new HashMap();
  
  public PriceCriteriaParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("PriceCriteria").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idPriceCriteriaString = node.getAttributeValue("idPriceCriteria");
      PriceCriteria priceCriteria = null;
      if (idPriceCriteriaString.startsWith("PriceCriteria")) {
        priceCriteria = new PriceCriteria();
      } else {
        priceCriteria = (PriceCriteria)sess.load(PriceCriteria.class, new Integer(idPriceCriteriaString));
      }
      
      
      
      this.initializePriceCriteria(sess, node, priceCriteria);
      
      priceCriteriaMap.put(idPriceCriteriaString, priceCriteria);
    }
    
   
  }
  
  protected void initializePriceCriteria(Session sess, Element n, PriceCriteria priceCriteria) throws Exception {

    if (n.getAttributeValue("filter1") != null && !n.getAttributeValue("filter1").equals("")) {
      priceCriteria.setFilter1(n.getAttributeValue("filter1"));
    } 
    if (n.getAttributeValue("filter2") != null && !n.getAttributeValue("filter2").equals("")) {
      priceCriteria.setFilter2(n.getAttributeValue("filter2"));
    } 
    
  }

  
  public Map getPriceCriteriaMap() {
    return priceCriteriaMap;
  }

  
  public void setPriceCriteriaMap(Map priceCriteriaMap) {
    this.priceCriteriaMap = priceCriteriaMap;
  }
  


}
