package hci.gnomex.model;


import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class SeqLibProtocolApplication extends DictionaryEntry implements Serializable {
  

  private String             codeApplication;
  private Integer            idSeqLibProtocol;
  private SeqLibProtocol     seqLibProtocol;
  private Application        application;
 
  
  public String getDisplay() {
    String display =  (getSeqLibProtocol() != null ? getSeqLibProtocol().getSeqLibProtocol() : "?") + 
                      " - " + 
                      (getApplication() != null ? getApplication().getApplication() : "?");
    return display;
  }

  public String getValue() {
    return getIdSeqLibProtocol() + " " + getCodeApplication();
  }

 
  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getCodeApplication() == null || this.getIdSeqLibProtocol() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof SeqLibProtocolApplication) {
        if (this.getIdSeqLibProtocol().equals(((SeqLibProtocolApplication) obj).getIdSeqLibProtocol())
            && this.getCodeApplication().equals(((SeqLibProtocolApplication) obj).getCodeApplication())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getCodeApplication() == null || this.getIdSeqLibProtocol() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getCodeApplication().hashCode() ^ this.getIdSeqLibProtocol().hashCode();
    }
  }

  
 
  
  private Application getApplication() {
    return application;
  }

  
  private void setApplication(Application application) {
    this.application = application;
  }

  
  public String getCodeApplication() {
    return codeApplication;
  }

  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  
  public Integer getIdSeqLibProtocol() {
    return idSeqLibProtocol;
  }

  
  public void setIdSeqLibProtocol(Integer idSeqLibProtocol) {
    this.idSeqLibProtocol = idSeqLibProtocol;
  }

  
  private SeqLibProtocol getSeqLibProtocol() {
    return seqLibProtocol;
  }

  
  private void setSeqLibProtocol(SeqLibProtocol seqLibProtocol) {
    this.seqLibProtocol = seqLibProtocol;
  }

  
}