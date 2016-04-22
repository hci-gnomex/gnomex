// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.io.Serializable;

public class Institute implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer idInstitute;
    private String name;
    private String description;
    private String isActive;
    
    public Integer getIdInstitute() {
        return this.idInstitute;
    }
    
    public void setIdInstitute(final Integer idInstitute) {
        this.idInstitute = idInstitute;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(final String isActive) {
        this.isActive = isActive;
    }
    
    public Document getXML(final GenoPubSecurity genoPubSecurity) {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Institute");
        root.addAttribute("label", (this.getName() != null) ? this.getName() : "");
        root.addAttribute("idInstitute", this.getIdInstitute().toString());
        root.addAttribute("name", (this.getName() != null) ? this.getName() : "");
        root.addAttribute("isActive", (this.getIsActive() != null) ? this.getIsActive() : "");
        root.addAttribute("canWrite", genoPubSecurity.canWrite(this) ? "Y" : "N");
        return doc;
    }
}
