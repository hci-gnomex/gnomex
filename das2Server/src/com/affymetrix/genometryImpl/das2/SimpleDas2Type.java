// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.Map;
import java.util.List;

public final class SimpleDas2Type
{
    private String name;
    private List<String> formats;
    private Map<String, Object> props;
    
    public SimpleDas2Type(final String name, final List<String> formats, final Map<String, Object> props) {
        this.name = name;
        this.formats = formats;
        this.props = props;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<String> getFormats() {
        return this.formats;
    }
    
    public void setFormats(final List<String> formats) {
        this.formats = formats;
    }
    
    public Map<String, Object> getProps() {
        return this.props;
    }
    
    public void setProps(final Map<String, Object> props) {
        this.props = props;
    }
}
