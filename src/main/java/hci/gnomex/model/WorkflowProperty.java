package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

/**
 * Created by u6008750 on 2/13/2017.
 */
public class WorkflowProperty extends DictionaryEntry implements Serializable {
    private Integer  idWorkflowProperty;
    private String   workflowPropertyName;
    private String   workflowPropertyValue;
    private String   codeRequestCategory;

    public String getValue() {
        return getWorkflowPropertyValue().toString();
    }

    public String getDisplay() {
        String display = this.getNonNullString(getWorkflowPropertyName());
        return display;
    }

    public String getWorkflowPropertyName() {
        return workflowPropertyName;
    }

    public void setWorkflowPropertyName(String workflowPropertyName) {
        this.workflowPropertyName = workflowPropertyName;
    }

    public Integer getIdWorkflowProperty() {
        return idWorkflowProperty;
    }

    public void setIdWorkflowProperty(Integer idWorkflowProperty) {
        this.idWorkflowProperty = idWorkflowProperty;
    }

    public String getWorkflowPropertyValue() {
        return workflowPropertyValue;
    }

    public void setWorkflowPropertyValue(String workflowPropertyValue) {
        this.workflowPropertyValue = workflowPropertyValue;
    }

    public String getCodeRequestCategory() {
        return codeRequestCategory;
    }

    public void setCodeRequestCategory(String codeRequestCategory) {
        this.codeRequestCategory = codeRequestCategory;
    }

}
