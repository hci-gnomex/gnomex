// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class PropertyType
{
    public static final String TEXT = "TEXT";
    public static final String URL = "URL";
    public static final String CHECKBOX = "CHECK";
    public static final String OPTION = "OPTION";
    public static final String MULTI_OPTION = "MOPTION";
    private String codePropertyType;
    private String name;
    
    public String getCodePropertyType() {
        return this.codePropertyType;
    }
    
    public void setCodePropertyType(final String codePropertyType) {
        this.codePropertyType = codePropertyType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
