// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class PropertyOption
{
    private Integer idPropertyOption;
    private String name;
    private String isActive;
    private Integer sortOrder;
    private Integer idProperty;
    
    public Integer getIdPropertyOption() {
        return this.idPropertyOption;
    }
    
    public void setIdPropertyOption(final Integer idPropertyOption) {
        this.idPropertyOption = idPropertyOption;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(final String isActive) {
        this.isActive = isActive;
    }
    
    public Integer getSortOrder() {
        return this.sortOrder;
    }
    
    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getIdProperty() {
        return this.idProperty;
    }
    
    public void setIdProperty(final Integer idProperty) {
        this.idProperty = idProperty;
    }
}
