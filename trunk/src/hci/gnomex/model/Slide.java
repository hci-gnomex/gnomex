package hci.gnomex.model;


import hci.hibernate3utils.HibernateDetailObject;



public class Slide extends HibernateDetailObject {
  
  private Integer     idSlide;
  private String      barcode;
  private Integer     idSlideDesign;
  
  public String getBarcode() {
    return barcode;
  }
  
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }
  
  public Integer getIdSlide() {
    return idSlide;
  }
  
  public void setIdSlide(Integer idSlide) {
    this.idSlide = idSlide;
  }
  
  public Integer getIdSlideDesign() {
    return idSlideDesign;
  }
  
  public void setIdSlideDesign(Integer idSlideDesign) {
    this.idSlideDesign = idSlideDesign;
  }
  

}