// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class AnnotationPropertyValue
{
    private Integer idAnnotationPropertyValue;
    private String value;
    private Integer idAnnotationProperty;
    
    public Integer getIdAnnotationPropertyValue() {
        return this.idAnnotationPropertyValue;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public Integer getIdAnnotationProperty() {
        return this.idAnnotationProperty;
    }
    
    public void setIdAnnotationPropertyValue(final Integer idAnnotationPropertyValue) {
        this.idAnnotationPropertyValue = idAnnotationPropertyValue;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public void setIdAnnotationProperty(final Integer idAnnotationProperty) {
        this.idAnnotationProperty = idAnnotationProperty;
    }
}
