package hci.gnomex.utility;

import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.Application;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class ApplicationParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected Map         codeApplicationMap = new HashMap();
  
  public ApplicationParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("Application").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String code    = node.getAttributeValue("codeApplication");
      Application mc = (Application)sess.load(Application.class, code);
      
      codeApplicationMap.put(code, mc);

    }
  }

  
  public Map getCodeApplicationMap() {
    return codeApplicationMap;
  }
}
