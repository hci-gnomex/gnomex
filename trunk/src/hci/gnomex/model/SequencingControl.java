package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class SequencingControl extends DictionaryEntry implements Serializable, DictionaryEntryUserOwned {
  private Integer  idSequencingControl;
  private String   analysisType;
  private String   isActive;
  private Integer  idAppUser;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSequencingControl());
    return display;
  }

  public String getValue() {
    return getIdSequencingControl().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdSequencingControl() {
    return idSequencingControl;
  }

  
  public void setIdSequencingControl(Integer idSequencingControl) {
    this.idSequencingControl = idSequencingControl;
  }

  
  public String getSequencingControl() {
    return analysisType;
  }

  
  public void setSequencingControl(String analysisType) {
    this.analysisType = analysisType;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }


  public int compare(Object one, Object two) {
    if (one instanceof SequencingControl && two instanceof SequencingControl) {
      
      SequencingControl at1 = (SequencingControl)one;
      SequencingControl at2 = (SequencingControl)two;
      
      return at1.getSequencingControl().compareTo(at2.getSequencingControl());
       
    } else if (one instanceof NullDictionaryEntry && two instanceof SequencingControl) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && one instanceof SequencingControl) {
      return 1;
    } else {
      return 0;
    }
  }
  
  public String getLaneNumber() {
    return getDisplay();
  }
  
}