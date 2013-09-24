package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SubmissionInstruction extends DictionaryEntry implements Serializable {
  

  private Integer idSubmissionInstruction;
  private String  description;
  private String  url;
  private String  codeRequestCategory;
  private String  codeApplication;
  private String  codeBioanalyzerChipType;
  private Integer  idBillingSlideServiceClass;
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
    return display;
  }

  public String getValue() {
    return getIdSubmissionInstruction().toString();
  }

  
  public String getCodeApplication() {
    return codeApplication;
  }

  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  
  public Integer getIdSubmissionInstruction() {
    return idSubmissionInstruction;
  }

  
  public void setIdSubmissionInstruction(Integer idSubmissionInstruction) {
    this.idSubmissionInstruction = idSubmissionInstruction;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public String getUrl() {
    return url;
  }

  
  public void setUrl(String url) {
    this.url = url;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public String getCodeBioanalyzerChipType() {
    return codeBioanalyzerChipType;
  }

  
  public void setCodeBioanalyzerChipType(String codeBioanalyzerChipType) {
    this.codeBioanalyzerChipType = codeBioanalyzerChipType;
  }

  
  public Integer getIdBillingSlideServiceClass() {
    return idBillingSlideServiceClass;
  }

  
  public void setIdBillingSlideServiceClass(Integer idBillingSlideServiceClass) {
    this.idBillingSlideServiceClass = idBillingSlideServiceClass;
  }


}