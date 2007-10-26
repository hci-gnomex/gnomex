package hci.gnomex.utility;

import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.MicroarrayCategory;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class MicroarrayCategoryParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected Map         codeMicroarrayCategoryMap = new HashMap();
  
  public MicroarrayCategoryParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("MicroarrayCategory").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String code    = node.getAttributeValue("codeMicroarrayCategory");
      MicroarrayCategory mc = (MicroarrayCategory)sess.load(MicroarrayCategory.class, code);
      
      codeMicroarrayCategoryMap.put(code, mc);

    }
  }

  
  public Map getCodeMicroarrayCategoryMap() {
    return codeMicroarrayCategoryMap;
  }
}
