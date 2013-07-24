package hci.gnomex.utility;

import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class SamplePrimersParser implements Serializable {
  private Document          primersDoc;
  private HashMap<Integer, Integer>  primerMap;

  public SamplePrimersParser(Document primersDoc) {
    this.primersDoc = primersDoc;
  }

  public void init() {
    primerMap = new HashMap<Integer, Integer>();
  }
  
  public void parse(Session sess) throws Exception{
    init();
    
    Element primersNode = this.primersDoc.getRootElement();
    
    for(Iterator i = primersNode.getChildren("Primer").iterator(); i.hasNext();) {
      Element primerNode = (Element)i.next();
      Integer id = Integer.parseInt(primerNode.getAttributeValue("id"));
      Integer number = Integer.parseInt(primerNode.getAttributeValue("number"));
      primerMap.put(number, id);
    }
  }
  
  public Integer getID(Integer number) {
    return primerMap.get(number);
  }
}
