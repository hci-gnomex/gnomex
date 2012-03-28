
package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Sample;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class PlateWellParser extends DetailObject implements Serializable
{

  private Document doc;
  private Map      wellMap = new HashMap();

  public PlateWellParser(Document doc) {
    this.doc = doc;
  }

  public void init() {
    wellMap = new HashMap();
  }

  public void parse(Session sess) throws Exception {
    PlateWell well = new PlateWell();
    Element root = this.doc.getRootElement();

    for (Iterator i = root.getChildren("PlateWell").iterator(); i.hasNext();) {
      Boolean isNewWell = false;
      Element node = (Element) i.next();

      String idPlateWellString = node.getAttributeValue("idPlateWell");

      if (idPlateWellString.equals(null) || idPlateWellString.equals("0")) {
        
        isNewWell = true;
        well = new PlateWell();
        
      } else {
        isNewWell = false;
        well = (PlateWell) sess.get(PlateWell.class,
            Integer.parseInt(idPlateWellString));
      }

      this.initializePlateWell(sess, node, well);
      sess.flush();

      if (isNewWell) {
        sess.save(well);
        idPlateWellString = well.getIdPlateWell().toString();
      }
      wellMap.put(idPlateWellString, well);
    }
  }

  protected void initializePlateWell(Session sess, Element n,
      PlateWell well) throws Exception {
    
    // Can't do this unless we use samples from the DB - foreign key!
    if (n.getAttributeValue("idSample") != null
        && !n.getAttributeValue("idSample").equals("")) {
      
      Sample samp = (Sample) sess.get(Sample.class,
          new Integer(n.getAttributeValue("idSample")));
      
      if (samp!=null) {
        well.setIdSample(new Integer(n.getAttributeValue("idSample")));
        
        well.setSample(samp);
      }
      
    } 
    
    if (n.getAttributeValue("idRequest") != null
        && !n.getAttributeValue("idRequest").equals("----")) {
      try {
        well.setIdRequest(new Integer(n.getAttributeValue("idRequest"))); 
      } catch (NumberFormatException e) {
        well.setIdRequest(null);
      }
    } else {
      well.setIdRequest(null);
    }
    
    if (n.getAttributeValue("row") != null
        && !n.getAttributeValue("row").equals("")) {
      well.setRow(n.getAttributeValue("row"));
    } else {
      well.setIdRequest(null);
    }
    
    if (n.getAttributeValue("col") != null
        && !n.getAttributeValue("col").equals("")) {
      well.setCol(new Integer(n.getAttributeValue("col")));
    } else {
      well.setCol(null);
    }
    
    if (n.getAttributeValue("index") != null
        && !n.getAttributeValue("index").equals("")) {
      well.setIndex(new Integer(n.getAttributeValue("index")));
    } else {
      well.setIndex(null);
    }
    
  }

  public Map getWellMap() {
    return wellMap;
  }

  public void setWellMap(Map wellMap) {
    this.wellMap = wellMap;
  }

}
