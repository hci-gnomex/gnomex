package hci.gnomex.model;


import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class RequestCategoryMicroarrayCategory extends DictionaryEntry implements Serializable {
  

  private String codeMicroarrayCategory;
  private String codeRequestCategory;
 
  
  public String getDisplay() {
    String display = getCodeRequestCategory() + " " + getCodeMicroarrayCategory();
    return display;
  }

  public String getValue() {
    return getCodeRequestCategory() + " " + getCodeMicroarrayCategory();
  }

  
  public String getCodeMicroarrayCategory() {
    return codeMicroarrayCategory;
  }

  
  public void setCodeMicroarrayCategory(String codeMicroarrayCategory) {
    this.codeMicroarrayCategory = codeMicroarrayCategory;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getCodeMicroarrayCategory() == null || this.getCodeRequestCategory() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof RequestCategoryMicroarrayCategory) {
        if (this.getCodeRequestCategory().equals(((RequestCategoryMicroarrayCategory) obj).getCodeRequestCategory())
            && this.getCodeMicroarrayCategory().equals(((RequestCategoryMicroarrayCategory) obj).getCodeMicroarrayCategory())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getCodeMicroarrayCategory() == null || this.getCodeRequestCategory() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getCodeMicroarrayCategory().hashCode() ^ this.getCodeRequestCategory().hashCode();
    }
  }

  
}