package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SlideDesign extends DictionaryEntry implements Serializable {
  private Integer  idSlideDesign;
  private String   name;
  private String   slideDesignProtocolName;
  private Integer  idSlideProduct;
  private String   isActive;
  private String   accessionNumberArrayExpress;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdSlideDesign().toString();
  }
  
  public Integer getIdSlideDesign() {
    return idSlideDesign;
  }

  
  public void setIdSlideDesign(Integer idSlideDesign) {
    this.idSlideDesign = idSlideDesign;
  }

 
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getSlideDesignProtocolName() {
    return slideDesignProtocolName;
  }

  
  public void setSlideDesignProtocolName(String slideDesignProtocolName) {
    this.slideDesignProtocolName = slideDesignProtocolName;
  }

  
  public Integer getIdSlideProduct() {
    return idSlideProduct;
  }

  
  public void setIdSlideProduct(Integer idSlideProduct) {
    this.idSlideProduct = idSlideProduct;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public String getAccessionNumberArrayExpress() {
    return accessionNumberArrayExpress;
  }

  
  public void setAccessionNumberArrayExpress(String accessionNumberArrayExpress) {
    this.accessionNumberArrayExpress = accessionNumberArrayExpress;
  }

}