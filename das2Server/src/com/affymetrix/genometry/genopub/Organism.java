// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.Set;

public class Organism
{
    private Integer idOrganism;
    private String name;
    private String commonName;
    private String binomialName;
    private String NCBITaxID;
    private Integer sortOrder;
    private Set genomeVersions;
    
    public Integer getIdOrganism() {
        return this.idOrganism;
    }
    
    public void setIdOrganism(final Integer idOrganism) {
        this.idOrganism = idOrganism;
    }
    
    public Set getGenomeVersions() {
        return this.genomeVersions;
    }
    
    public void setGenomeVersions(final Set genomeVersions) {
        this.genomeVersions = genomeVersions;
    }
    
    public String getCommonName() {
        return this.commonName;
    }
    
    public void setCommonName(final String commonName) {
        this.commonName = commonName;
    }
    
    public Integer getSortOrder() {
        return this.sortOrder;
    }
    
    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getBinomialName() {
        return this.binomialName;
    }
    
    public void setBinomialName(final String binomialName) {
        this.binomialName = binomialName;
    }
    
    public String getNCBITaxID() {
        return this.NCBITaxID;
    }
    
    public void setNCBITaxID(final String taxID) {
        this.NCBITaxID = taxID;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Document getXML(final GenoPubSecurity genoPubSecurity) {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Organism");
        root.addAttribute("label", this.getBinomialName());
        root.addAttribute("idOrganism", this.getIdOrganism().toString());
        root.addAttribute("name", (this.getName() != null) ? this.getName() : "");
        root.addAttribute("commonName", (this.getCommonName() != null) ? this.getCommonName() : "");
        root.addAttribute("binomialName", (this.getBinomialName() != null) ? this.getBinomialName() : "");
        root.addAttribute("NCBITaxID", (this.getNCBITaxID() != null) ? this.getNCBITaxID() : "");
        root.addAttribute("canWrite", genoPubSecurity.canWrite(this) ? "Y" : "N");
        return doc;
    }
}
