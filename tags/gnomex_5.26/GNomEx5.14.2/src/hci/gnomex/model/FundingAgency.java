package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class FundingAgency extends DictionaryEntry implements Serializable {
  private Integer  idFundingAgency;
  private String   fundingAgency;
  private String   isActive;
  private String   isPeerReviewedFunding;
  
  public String getDisplay() {
    String display = this.getNonNullString(getFundingAgency());
    return display;
  }

  public String getValue() {
    return getIdFundingAgency().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdFundingAgency() {
    return idFundingAgency;
  }

  
  public void setIdFundingAgency(Integer idFundingAgency) {
    this.idFundingAgency = idFundingAgency;
  }

  
  public String getFundingAgency() {
    return fundingAgency;
  }

  
  public void setFundingAgency(String fundingAgency) {
    this.fundingAgency = fundingAgency;
  }

  
  public String getIsPeerReviewedFunding() {
    return isPeerReviewedFunding;
  }

  
  public void setIsPeerReviewedFunding(String isPeerReviewedFunding) {
    this.isPeerReviewedFunding = isPeerReviewedFunding;
  }


}