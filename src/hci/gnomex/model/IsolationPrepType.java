package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

import org.hibernate.Session;



public class IsolationPrepType extends DictionaryEntry implements Serializable {
  public static final String   TYPE_FFPE            = "FFPE Scrolls";
  public static final String   TYPE_MICRO           = "Micro-Dissection";
  
  public static final String   TYPE_RNA             = "RNA";
  public static final String   TYPE_DNA             = "DNA";
  public static final String   TYPE_BOTH            = "BOTH";
  public static final String   CODE_PREP_PREFIX     = "PREPTYPE";
  
  private String     codeIsolationPrepType;
  private String     isolationPrepType;
  private String     type;
  private String     isActive;
  private String     codeRequestCategory;
  private Integer    idPrice;
  
  public String getDisplay() {
    return isolationPrepType;
  }

  public String getValue() {
    return codeIsolationPrepType;
  }

  
  public String getCodeIsolationPrepType() {
    return codeIsolationPrepType;
  }
  
  public void setCodeIsolationPrepType(String codeIsolationPrepType){
    this.codeIsolationPrepType = codeIsolationPrepType;
  }


  public String getIsolationPrepType() {
    return isolationPrepType;
  }

  public void setIsolationPrepType(String isolationPrepType) {
    this.isolationPrepType = isolationPrepType;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }
  
  
  public Integer getIdPrice() {
    return idPrice;
  }

  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }

  public static Price getIsolationPrepTypePrice(Session sess, IsolationPrepType ipt) {
    
    if ( ipt == null || ipt.getIdPrice() == null ) {
      return null;
    }
    
    String priceQuery; 
    
     priceQuery = "SELECT p from Price as p where p.idPrice=" + ipt.getIdPrice();
     Price price = (Price) sess.createQuery( priceQuery ).uniqueResult();
     
     return price;
   }

}