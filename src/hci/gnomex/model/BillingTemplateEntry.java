package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class BillingTemplateEntry extends DictionaryEntry implements Serializable {
  private Integer  idBillingTemplate;
  private Integer  idBillingCategory;
  private Integer  sortOrder;
  private BillingCategory billingCategory;
  private BillingTemplate billingTemplate;
  
  
  
  public String getDisplay() {
    String display =  (getBillingCategory() != null ? getBillingCategory().getDescription() : "?") + 
                      " - " + 
                      (getBillingTemplate() != null ? getBillingTemplate().getDescription() : "?");
    return display;
  }

  public String getValue() {
    return getIdBillingTemplate().toString() + getIdBillingCategory().toString();
  }

  
  public Integer getIdBillingTemplate() {
    return idBillingTemplate;
  }

  
  public void setIdBillingTemplate(Integer idBillingTemplate) {
    this.idBillingTemplate = idBillingTemplate;
  }

  
  public Integer getIdBillingCategory() {
    return idBillingCategory;
  }

  
  public void setIdBillingCategory(Integer idBillingCategory) {
    this.idBillingCategory = idBillingCategory;
  }

  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getIdBillingTemplate() == null || this.getIdBillingCategory() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof BillingTemplateEntry) {
        if (this.getIdBillingTemplate().equals(((BillingTemplateEntry) obj).getIdBillingTemplate())
            && this.getIdBillingCategory().equals(((BillingTemplateEntry) obj).getIdBillingCategory())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getIdBillingTemplate() == null || this.getIdBillingCategory() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getIdBillingTemplate().hashCode() ^ this.getIdBillingCategory().hashCode();
    }
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
  private BillingCategory getBillingCategory() {
    return billingCategory;
  }

  
  private void setBillingCategory(BillingCategory billingCategory) {
    this.billingCategory = billingCategory;
  }

  
  private BillingTemplate getBillingTemplate() {
    return billingTemplate;
  }

  
  private void setBillingTemplate(BillingTemplate billingTemplate) {
    this.billingTemplate = billingTemplate;
  } 


  

}