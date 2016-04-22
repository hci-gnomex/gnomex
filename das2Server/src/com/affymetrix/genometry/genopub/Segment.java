// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class Segment
{
    private Integer idSegment;
    private Integer length;
    private String name;
    private Integer idGenomeVersion;
    private Integer sortOrder;
    
    public Integer getIdSegment() {
        return this.idSegment;
    }
    
    public void setIdSegment(final Integer idSegment) {
        this.idSegment = idSegment;
    }
    
    public Integer getLength() {
        return this.length;
    }
    
    public void setLength(final Integer length) {
        this.length = length;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Integer getSortOrder() {
        return this.sortOrder;
    }
    
    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getIdGenomeVersion() {
        return this.idGenomeVersion;
    }
    
    public void setIdGenomeVersion(final Integer idGenomeVersion) {
        this.idGenomeVersion = idGenomeVersion;
    }
}
