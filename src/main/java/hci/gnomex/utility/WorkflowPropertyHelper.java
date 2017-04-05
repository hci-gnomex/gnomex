package hci.gnomex.utility;

import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.WorkflowProperty;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by u6008750 on 2/14/2017.
 */
public class WorkflowPropertyHelper {

    private static WorkflowPropertyHelper theInstance;

    private Map propertyMap = new HashMap();

    public WorkflowPropertyHelper() {
    }

    public static synchronized WorkflowPropertyHelper reload(Session sess) {
        theInstance = new WorkflowPropertyHelper();
        theInstance.loadProperties(sess);
        return theInstance;

    }

    public static synchronized WorkflowPropertyHelper getInstance(Session sess) {
        if (theInstance == null) {
            theInstance = new WorkflowPropertyHelper();
            theInstance.loadProperties(sess);
        }
        return theInstance;

    }


    private void loadProperties(Session sess) {
        Query propQuery = sess.createQuery("select wp from WorkflowProperty as wp");
        List properties = propQuery.list();
        for (Iterator i = properties.iterator(); i.hasNext();) {
            WorkflowProperty prop = (WorkflowProperty) i.next();
            String name = prop.getWorkflowPropertyName();
            if (prop.getCodeRequestCategory() != null && !prop.getCodeRequestCategory().equals("") ) {
                name = prop.getCodeRequestCategory() + "\t" + prop.getWorkflowPropertyName();
            }
            propertyMap.put(name, prop.getWorkflowPropertyValue());
        }
    }

    public String getRequestCategoryProperty(String name, String codeRequestCategory) {
        String propertyValue = "";
        if (name != null && !name.equals("")) {
            propertyValue = null;
            if (codeRequestCategory != null && codeRequestCategory.length() > 0) {
                String qualName = codeRequestCategory + "\t" + name;
                propertyValue = (String) propertyMap.get(qualName);
            }
            if (propertyValue == null) {
                propertyValue = (String) propertyMap.get(name);
            }
        }

        return propertyValue;
    }

}
