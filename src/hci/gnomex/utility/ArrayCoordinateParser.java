package hci.gnomex.utility;

import hci.gnomex.model.ArrayCoordinate;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class ArrayCoordinateParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected Map         arrayCoordinateMap = new HashMap();
  
  public ArrayCoordinateParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("ArrayCoordinate").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String x     = node.getAttributeValue("x");
      String y     = node.getAttributeValue("y");
      String name  = node.getAttributeValue("name");
      String idArrayCoordinateString = node.getAttributeValue("idArrayCoordinate");

      ArrayCoordinate ac = null;
      if (idArrayCoordinateString.startsWith("ArrayCoordinate")) {
        ac = new ArrayCoordinate();
      } else {
        ac = (ArrayCoordinate)sess.load(ArrayCoordinate.class, new Integer(idArrayCoordinateString));
      }

      ac.setX(new Integer(x));
      ac.setY(new Integer(y));
      ac.setName(name);        
      
      
      arrayCoordinateMap.put(idArrayCoordinateString, ac);
    }
  }

  
  public Map getArrayCoordinateMap() {
    return arrayCoordinateMap;
  }
}
