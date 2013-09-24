package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class PriceCriteria extends DictionaryEntry implements Serializable {
  private Integer  idPriceCriteria;
  private String   filter1;
  private String   filter2;
  private Integer  idPrice;
  
  public String getDisplay() {
    String display = this.getNonNullString(getIdPriceCriteria());
    

    return display;
  }

  public String getValue() {
    return getIdPriceCriteria().toString();
  }

  
  public Integer getIdPriceCriteria() {
    return idPriceCriteria;
  }

  
  public void setIdPriceCriteria(Integer idPriceCriteria) {
    this.idPriceCriteria = idPriceCriteria;
  }

  
  public String getFilter1() {
    return filter1;
  }

  
  public void setFilter1(String filter1) {
    this.filter1 = filter1;
  }

  
  public String getFilter2() {
    return filter2;
  }

  
  public void setFilter2(String filter2) {
    this.filter2 = filter2;
  }

  
  public Integer getIdPrice() {
    return idPrice;
  }

  
  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }


 

}