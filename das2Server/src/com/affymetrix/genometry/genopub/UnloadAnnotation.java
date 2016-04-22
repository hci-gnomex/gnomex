// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;

public class UnloadAnnotation implements Serializable
{
    private Integer idUnloadAnnotation;
    private String typeName;
    private Integer idUser;
    private Integer idGenomeVersion;
    
    public Integer getIdUnloadAnnotation() {
        return this.idUnloadAnnotation;
    }
    
    public void setIdUnloadAnnotation(final Integer idUnloadAnnotation) {
        this.idUnloadAnnotation = idUnloadAnnotation;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }
    
    public Integer getIdUser() {
        return this.idUser;
    }
    
    public void setIdUser(final Integer idUser) {
        this.idUser = idUser;
    }
    
    public Integer getIdGenomeVersion() {
        return this.idGenomeVersion;
    }
    
    public void setIdGenomeVersion(final Integer idGenomeVersion) {
        this.idGenomeVersion = idGenomeVersion;
    }
}
