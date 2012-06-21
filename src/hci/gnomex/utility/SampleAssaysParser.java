package hci.gnomex.utility;

import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class SampleAssaysParser implements Serializable {
  private Document          assaysDoc;
  private HashMap<String, Integer>  assayMap;

  public SampleAssaysParser(Document assaysDoc) {
    this.assaysDoc = assaysDoc;
  }

  public void init() {
    assayMap = new HashMap<String, Integer>();
  }
  
  public void parse(Session sess) throws Exception{
    init();
    
    Element assaysNode = this.assaysDoc.getRootElement();
    
    for(Iterator i = assaysNode.getChildren("Assay").iterator(); i.hasNext();) {
      Element assayNode = (Element)i.next();
      Integer id = Integer.parseInt(assayNode.getAttributeValue("id"));
      Integer number = Integer.parseInt(assayNode.getAttributeValue("number"));
      String name = assayNode.getAttributeValue("name");
      assayMap.put(name, id);
    }
  }
  
  public Integer getID(String name) {
    return assayMap.get(name);
  }
  
  public Integer getNumAssays() {
    return assayMap.keySet().size();
  }
}
