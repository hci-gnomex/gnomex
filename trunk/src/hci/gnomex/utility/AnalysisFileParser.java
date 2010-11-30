package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AnalysisFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisFileParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected Map         analysisFileMap = new HashMap();
  
  public AnalysisFileParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("AnalysisFileDescriptor").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idAnalysisFileString = node.getAttributeValue("idAnalysisFileString");

      AnalysisFile af = null;
      if (!idAnalysisFileString.startsWith("AnalysisFile")) {
        af = (AnalysisFile)sess.load(AnalysisFile.class, new Integer(idAnalysisFileString));
      } else {
        af = new AnalysisFile();
        af.setFileName(node.getAttributeValue("displayName"));
        af.setIdAnalysis(Integer.valueOf(node.getAttributeValue("idAnalysis")));
      }
      af.setComments(node.getAttributeValue("comments"));
      
      
      analysisFileMap.put(idAnalysisFileString, af);
    }
  }

  
  public Map getAnalysisFileMap() {
    return analysisFileMap;
  }
}
