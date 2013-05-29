package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ArrayDesign extends DictionaryEntry implements Serializable {
  private Integer  idArrayDesign;
  private String   name;
  private Integer  accessionNumberUArrayExpress;
  private Integer  idArrayCoordinate;
  

  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdArrayDesign().toString();
  }

  
  public Integer getAccessionNumberUArrayExpress() {
    return accessionNumberUArrayExpress;
  }

  
  public void setAccessionNumberUArrayExpress(Integer accessionNumberUArrayExpress) {
    this.accessionNumberUArrayExpress = accessionNumberUArrayExpress;
  }

  
  public Integer getIdArrayCoordinate() {
    return idArrayCoordinate;
  }

  
  public void setIdArrayCoordinate(Integer idArrayCoordinate) {
    this.idArrayCoordinate = idArrayCoordinate;
  }

  
  public Integer getIdArrayDesign() {
    return idArrayDesign;
  }

  
  public void setIdArrayDesign(Integer idArrayDesign) {
    this.idArrayDesign = idArrayDesign;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  

}