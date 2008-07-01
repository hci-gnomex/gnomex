package hci.gnomex.utility;

import hci.gnomex.model.Analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisVisibilityParser implements Serializable {
  
  protected Document   doc;
  protected List       analysisList = new ArrayList();
  
  public AnalysisVisibilityParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element rootNode = this.doc.getRootElement();
    
    
    for(Iterator i = rootNode.getChildren("Analysis").iterator(); i.hasNext();) {
      Element aNode = (Element)i.next();
      
      String idAnalysis           = aNode.getAttributeValue("idAnalysis");
      String codeVisibility       = aNode.getAttributeValue("codeVisibility");
      
      Analysis analysis = (Analysis)sess.load(Analysis.class, new Integer(idAnalysis));
      analysis.setCodeVisibility(codeVisibility);

      analysisList.add(analysis);
    }
    
   
  }
  
   
  public List getAnalysiss() {
    return analysisList;
  }
  
  
  public void resetIsDirty() {
    Element rootNode = this.doc.getRootElement();
    
    for(Iterator i = rootNode.getChildren("Analysis").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      workItemNode.setAttribute("isDirty", "N");
    }
  }


  


}
