package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class OligoBarcodeSchemeAllowed extends DictionaryEntry implements Serializable {
  
  private Integer            idOligoBarcodeSchemeAllowed;
  private Integer            idOligoBarcodeScheme;
  private Integer            idSeqLibProtocol;
  private OligoBarcodeScheme oligoBarcodeScheme;
  private SeqLibProtocol     seqLibProtocol;
 
  
  public String getDisplay() {
    String display =  (getSeqLibProtocol() != null ? getSeqLibProtocol().getDisplay() : "?") + 
    " - " + 
    (getOligoBarcodeScheme() != null ? getOligoBarcodeScheme().getOligoBarcodeScheme() : "?");

    return display;
  }

  public String getValue() {
    return getIdOligoBarcodeSchemeAllowed().toString();
  }
  
  
  public Integer getIdOligoBarcodeScheme() {
    return idOligoBarcodeScheme;
  }

  
  public void setIdOligoBarcodeScheme(Integer idOligoBarcodeScheme) {
    this.idOligoBarcodeScheme = idOligoBarcodeScheme;
  }

  
  private OligoBarcodeScheme getOligoBarcodeScheme() {
    return oligoBarcodeScheme;
  }

  
  public void setOligoBarcodeScheme(OligoBarcodeScheme oligoBarcodeScheme) {
    this.oligoBarcodeScheme = oligoBarcodeScheme;
  }

    
  public Integer getIdSeqLibProtocol() {
    return idSeqLibProtocol;
  }

  
  public void setIdSeqLibProtocol( Integer idSeqLibProtocol ) {
    this.idSeqLibProtocol = idSeqLibProtocol;
  }

  
  private SeqLibProtocol getSeqLibProtocol() {
    return seqLibProtocol;
  }

  
  public void setSeqLibProtocol( SeqLibProtocol seqLibProtocol ) {
    this.seqLibProtocol = seqLibProtocol;
  }

  public Integer getIdOligoBarcodeSchemeAllowed() {
    return idOligoBarcodeSchemeAllowed;
  }

  
  public void setIdOligoBarcodeSchemeAllowed(Integer idOligoBarcodeSchemeAllowed) {
    this.idOligoBarcodeSchemeAllowed = idOligoBarcodeSchemeAllowed;
  }


  

}