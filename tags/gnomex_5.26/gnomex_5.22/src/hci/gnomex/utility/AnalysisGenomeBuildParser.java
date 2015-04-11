package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisGenomeBuildParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idGenomeBuildList = new ArrayList();
  
  public AnalysisGenomeBuildParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("GenomeBuild").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idGenomeBuildString = node.getAttributeValue("idGenomeBuild");
      Integer idGenomeBuild = Integer.valueOf(idGenomeBuildString);
          
      idGenomeBuildList.add(idGenomeBuild);
    }
  }

  
  public List getIdGenomeBuildList() {
    return idGenomeBuildList;
  }

}
