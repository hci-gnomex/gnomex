// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Set;
import java.io.Serializable;

public class AnnotationProperty implements Serializable
{
    private Integer idAnnotationProperty;
    private String name;
    private String value;
    private Integer idAnnotation;
    private Integer idProperty;
    private Property property;
    private Set options;
    private Set values;
    
    public Integer getIdAnnotationProperty() {
        return this.idAnnotationProperty;
    }
    
    public void setIdAnnotationProperty(final Integer idAnnotationProperty) {
        this.idAnnotationProperty = idAnnotationProperty;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public Set getOptions() {
        return this.options;
    }
    
    public void setOptions(final Set options) {
        this.options = options;
    }
    
    public Integer getIdAnnotation() {
        return this.idAnnotation;
    }
    
    public void setIdAnnotation(final Integer idAnnotation) {
        this.idAnnotation = idAnnotation;
    }
    
    public Integer getIdProperty() {
        return this.idProperty;
    }
    
    public void setIdProperty(final Integer idProperty) {
        this.idProperty = idProperty;
    }
    
    public Property getProperty() {
        return this.property;
    }
    
    public void setProperty(final Property property) {
        this.property = property;
    }
    
    public Set getValues() {
        return this.values;
    }
    
    public void setValues(final Set values) {
        this.values = values;
    }
}
