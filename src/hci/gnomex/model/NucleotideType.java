package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

public class NucleotideType extends DictionaryEntry implements Serializable {
  private String codeNucleotideType;
  
  public String getDisplay() {
    return codeNucleotideType;
  }

  public String getValue() {
    return codeNucleotideType;
  }

  public String getCodeNucleotideType() {
    return codeNucleotideType;
  }
  
  public void setCodeNucleotideType(String code) {
    this.codeNucleotideType = code;
  }
}
