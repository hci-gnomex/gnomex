package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.Step;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class PriceCategoryStepParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        stepMap = new HashMap();
  
  public PriceCategoryStepParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("Step").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String codeStep = node.getAttributeValue("codeStep");
      Step step = (Step)sess.get(Step.class, codeStep);
      
      stepMap.put(step.getCodeStep(), step);
    }
  }

  
  public Map getStepMap() {
    return stepMap;
  }
}
