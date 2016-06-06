package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

/**
 * Created by u0395021 on 6/2/2016.
 */
public class LibraryPrepQCProtocol extends DictionaryEntry implements Serializable {
    private Integer idLibPrepQCProtocol;
    private String protocolDisplay;
    private String codeRequestCategory;

    public String getDisplay() {
        if(this.protocolDisplay == null){
            return "";
        } else{
            return this.protocolDisplay;
        }
    }

    @Override
    public String getValue() {
        return idLibPrepQCProtocol.toString();
    }

    public Integer getIdLibPrepQCProtocol() {
        return idLibPrepQCProtocol;
    }

    public void setIdLibPrepQCProtocol(Integer idLibPrepQCProtocol) {
        this.idLibPrepQCProtocol = idLibPrepQCProtocol;
    }

    public String getProtocolDisplay() {
        return protocolDisplay;
    }

    public void setProtocolDisplay(String protocolDisplay) {
        this.protocolDisplay = protocolDisplay;
    }

    public String getCodeRequestCategory() {
        return codeRequestCategory;
    }

    public void setCodeRequestCategory(String codeRequestCategory) {
        this.codeRequestCategory = codeRequestCategory;
    }
}
