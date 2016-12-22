package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.InternalAccountFieldsConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class InternalBillingAccountFieldsConfigurationParser extends DetailObject implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Document                                       doc;
  private List<InternalAccountFieldsConfiguration>       configurations = new ArrayList<InternalAccountFieldsConfiguration>();
  
  
  public InternalBillingAccountFieldsConfigurationParser(Document doc) {
    this.doc = doc;
  }
  
  public void parse(Session sess) throws Exception{
    
    Element confNode = this.doc.getRootElement();
    
    

    for(Iterator i = confNode.getChildren("InternalAccountFieldsConfiguration").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      String idString = node.getAttributeValue("idInternalAccountFieldsConfiguration");
      InternalAccountFieldsConfiguration conf = new InternalAccountFieldsConfiguration();
      if (idString != null && idString.length() > 0) {
        conf = (InternalAccountFieldsConfiguration)sess.load(InternalAccountFieldsConfiguration.class, new Integer(idString));
      }
      conf.setDisplayName(node.getAttributeValue("displayName"));
      conf.setFieldName(node.getAttributeValue("fieldName"));
      conf.setInclude(node.getAttributeValue("include"));
      conf.setIsNumber(node.getAttributeValue("isNumber"));
      conf.setIsRequired(node.getAttributeValue("isRequired"));
      conf.setMaxLength(new Integer(node.getAttributeValue("maxLength")));
      conf.setMinLength(new Integer(node.getAttributeValue("minLength")));
      conf.setSortOrder(new Integer(node.getAttributeValue("sortOrder")));
      
      configurations.add(conf);
    }
  }
 
  
  public List<InternalAccountFieldsConfiguration> getConfigurations() {
    return configurations;
  }
}
