package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

public class PipelineProtocol extends DictionaryEntry implements Serializable {
    private Integer idPipelineProtocol;
    private String protocol;
    private String description;
    private Integer idCoreFacility;
    private String isDefault;

    @Override
    public String getDisplay() {
        return this.protocol;
    }

    @Override
    public String getValue() { return idPipelineProtocol.toString(); }

    public Integer getIdPipelineProtocol() {
        return idPipelineProtocol;
    }

    public void setIdPipelineProtocol(Integer idPipelineProtocol) {
        this.idPipelineProtocol = idPipelineProtocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdCoreFacility() {
        return idCoreFacility;
    }

    public void setIdCoreFacility(Integer idCoreFacility) {
        this.idCoreFacility = idCoreFacility;
    }

    public String getIsDefault() { return isDefault; }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

}
