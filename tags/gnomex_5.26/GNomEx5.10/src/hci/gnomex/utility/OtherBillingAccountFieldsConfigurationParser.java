package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.OtherAccountFieldsConfiguration;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class OtherBillingAccountFieldsConfigurationParser extends DetailObject implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Document                                       doc;
  private List<OtherAccountFieldsConfiguration>          configurations = new ArrayList<OtherAccountFieldsConfiguration>();
  
  
  public OtherBillingAccountFieldsConfigurationParser(Document doc) {
    this.doc = doc;
  }
  
  public void parse(Session sess) throws Exception{
    
    Element confNode = this.doc.getRootElement();
    
    

    for(Iterator i = confNode.getChildren("OtherAccountFieldsConfiguration").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      String idString = node.getAttributeValue("idOtherAccountFieldsConfiguration");
      OtherAccountFieldsConfiguration conf = new OtherAccountFieldsConfiguration();
      if (idString != null && idString.length() > 0) {
        conf = (OtherAccountFieldsConfiguration)sess.load(OtherAccountFieldsConfiguration.class, new Integer(idString));
      }
      conf.setFieldName(node.getAttributeValue("fieldName"));
      conf.setInclude(node.getAttributeValue("include"));
      conf.setIsRequired(node.getAttributeValue("isRequired"));
      
      configurations.add(conf);
    }
  }
 
  
  public List<OtherAccountFieldsConfiguration> getConfigurations() {
    return configurations;
  }
}
