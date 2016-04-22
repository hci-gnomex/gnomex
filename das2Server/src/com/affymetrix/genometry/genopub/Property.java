// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Set;

public class Property implements Owned
{
    private Integer idProperty;
    private String name;
    private String isActive;
    private Integer idUser;
    private String codePropertyType;
    private Integer sortOrder;
    private Set options;
    
    public Integer getIdProperty() {
        return this.idProperty;
    }
    
    public void setIdProperty(final Integer idProperty) {
        this.idProperty = idProperty;
    }
    
    public String getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(final String isActive) {
        this.isActive = isActive;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Integer getIdUser() {
        return this.idUser;
    }
    
    public void setIdUser(final Integer idUser) {
        this.idUser = idUser;
    }
    
    @Override
    public boolean isOwner(final Integer idUser) {
        return this.getIdUser() != null && this.getIdUser().equals(idUser);
    }
    
    @Override
    public boolean isUserGroup(final Integer idUserGroup) {
        return false;
    }
    
    public String getCodePropertyType() {
        return this.codePropertyType;
    }
    
    public void setCodePropertyType(final String codePropertyType) {
        this.codePropertyType = codePropertyType;
    }
    
    public Set getOptions() {
        return this.options;
    }
    
    public void setOptions(final Set options) {
        this.options = options;
    }
    
    public Integer getSortOrder() {
        return this.sortOrder;
    }
    
    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
