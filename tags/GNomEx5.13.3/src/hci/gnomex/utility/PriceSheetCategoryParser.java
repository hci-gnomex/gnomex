package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.RequestCategory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class PriceSheetCategoryParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        requestCategoryMap = new HashMap();
  
  public PriceSheetCategoryParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("RequestCategory").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String codeRequestCategory = node.getAttributeValue("codeRequestCategory");
      
      requestCategoryMap.put(codeRequestCategory, codeRequestCategory);
    }
    
   
  }
  
  public Map getRequestCategoryMap() {
    return requestCategoryMap;
  }


  


}
