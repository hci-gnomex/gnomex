package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AnalysisFile;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisFileParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected Document    filesToDeleteDoc;
  protected Map         analysisFileMap = new HashMap();
  protected Map         analysisFileToDeleteMap = new HashMap();
  
  public AnalysisFileParser(Document doc, Document filesToDeleteDoc) {
    this.doc = doc;
    this.filesToDeleteDoc = filesToDeleteDoc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    recurseDirectories(root, null, sess);
    
    Element deleteRoot = this.filesToDeleteDoc.getRootElement();
    recurseDeleteDirectories(deleteRoot, null, sess);
    
  }
  
  private void recurseDirectories(Element folderNode, String parentDir, Session sess) {
    
    for(Iterator i1 = folderNode.getChildren("AnalysisFileDescriptor").iterator(); i1.hasNext();) {
      Element fileNode = (Element)i1.next();
      
      String idAnalysisFileString = fileNode.getAttributeValue("idAnalysisFileString");
      AnalysisFile af = initializeFile(fileNode, sess);
      
      if ( !fileNode.getAttributeValue("type").equals("dir") ) {
        analysisFileMap.put(idAnalysisFileString, af);
      }
      recurseDirectories(fileNode, null, sess);
    }
    
  }
  
  private void recurseDeleteDirectories(Element folderNode, String parentDir, Session sess) {
    
    for(Iterator i1 = folderNode.getChildren("AnalysisFileDescriptor").iterator(); i1.hasNext();) {
      Element fileNode = (Element)i1.next();

      String idAnalysisFileString = fileNode.getAttributeValue("idAnalysisFileString");
      AnalysisFile af = initializeFile(fileNode, sess);

      analysisFileToDeleteMap.put(idAnalysisFileString, af);
      recurseDeleteDirectories(fileNode, null, sess);
    }
    
  }
  
  protected AnalysisFile initializeFile(Element n, Session sess){
    String idAnalysisFileString = n.getAttributeValue("idAnalysisFileString");

    AnalysisFile af = null;
    if (!idAnalysisFileString.startsWith("AnalysisFile") && !idAnalysisFileString.equals("")) {
      af = (AnalysisFile)sess.load(AnalysisFile.class, new Integer(idAnalysisFileString));
    } else {
      af = new AnalysisFile();
    }
    af.setBaseFilePath(n.getAttributeValue("baseFilePath"));
    af.setQualifiedFilePath(n.getAttributeValue("qualifiedFilePath"));
    af.setFileName(n.getAttributeValue("displayName"));
    af.setComments(n.getAttributeValue("comments"));
    af.setIdAnalysis(Integer.valueOf(n.getAttributeValue("idAnalysis")));
    af.setFileSize(n.getAttributeValue("fileSize")!=null? new BigDecimal(Integer.valueOf(n.getAttributeValue("fileSize"))): new BigDecimal(0));
    return af;

  }
  
  public Map getAnalysisFileMap() {
    return analysisFileMap;
  }
  
  public Map getAnalysisFileToDeleteMap() {
    return analysisFileToDeleteMap;
  }

}
