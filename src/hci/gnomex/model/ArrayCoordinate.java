package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ArrayCoordinate extends DictionaryEntry implements Serializable {
  private Integer  idArrayCoordinate;
  private String   name;
  private Integer  x;
  private Integer  y;
  private Integer  idSlideDesign;

  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdArrayCoordinate().toString();
  }

  
  public Integer getIdArrayCoordinate() {
    return idArrayCoordinate;
  }

  
  public void setIdArrayCoordinate(Integer idArrayCoordinate) {
    this.idArrayCoordinate = idArrayCoordinate;
  }

  
  public Integer getIdSlideDesign() {
    return idSlideDesign;
  }

  
  public void setIdSlideDesign(Integer idSlideDesign) {
    this.idSlideDesign = idSlideDesign;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }



  
  public Integer getX() {
    return x;
  }

  
  public void setX(Integer x) {
    this.x = x;
  }
  
  
  public Integer getY() {
    return y;
  }

  
  public void setY(Integer y) {
    this.y = y;
  }

  

}