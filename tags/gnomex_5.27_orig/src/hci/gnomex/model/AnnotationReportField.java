package hci.gnomex.model;

import java.io.Serializable;

import hci.dictionary.model.DictionaryEntry;

public class AnnotationReportField extends DictionaryEntry implements Serializable   {
  
  private Integer idAnnotationReportField;
  private String  source;
  private String  fieldName;
  private String  display;
  private String  isCustom;
  private String  sortOrder;
  private String  dictionaryLookUpTable;
  
  public static final String  SOURCE_TYPE_SAMPLE = "SAMPLE";
  public static final String  SOURCE_TYPE_REQUEST = "REQUEST";

  @Override
  public String getDisplay() {
    if(display != null) {
      return display;
    } else {
      return "";
    }
  }

  @Override
  public String getValue() {
    return this.idAnnotationReportField.toString();
  }

  public Integer getIdAnnotationReportField() {
    return idAnnotationReportField;
  }

  public void setIdAnnotationReportField(Integer idAnnotationReportField) {
    this.idAnnotationReportField = idAnnotationReportField;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getIsCustom() {
    return isCustom;
  }

  public void setIsCustom(String isCustom) {
    this.isCustom = isCustom;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getDictionaryLookUpTable() {
    return dictionaryLookUpTable;
  }

  public void setDictionaryLookUpTable(String dictionaryLookUpTable) {
    this.dictionaryLookUpTable = dictionaryLookUpTable;
  }

}
