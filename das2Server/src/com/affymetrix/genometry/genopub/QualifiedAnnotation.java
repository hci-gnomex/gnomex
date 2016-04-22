// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;

public class QualifiedAnnotation implements Serializable
{
    private Annotation annotation;
    private String typePrefix;
    private String resourceName;
    
    public QualifiedAnnotation(final Annotation annotation, final String typePrefix, final String resourceName) {
        this.annotation = annotation;
        this.typePrefix = typePrefix;
        this.resourceName = resourceName;
    }
    
    public Annotation getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotation(final Annotation annotation) {
        this.annotation = annotation;
    }
    
    public String getTypePrefix() {
        return this.typePrefix;
    }
    
    public void setTypePrefix(final String typePrefix) {
        this.typePrefix = typePrefix;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
}
