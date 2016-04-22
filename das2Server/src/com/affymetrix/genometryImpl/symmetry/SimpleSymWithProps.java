// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Map;

public class SimpleSymWithProps extends SimpleMutableSeqSymmetry implements SymWithProps
{
    public static final String CONTAINER_PROP = "container sym";
    protected Map<String, Object> props;
    
    public SimpleSymWithProps() {
    }
    
    public SimpleSymWithProps(final int estimated_child_count) {
        this();
        this.children = new ArrayList<SeqSymmetry>(estimated_child_count);
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.props;
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        if (this.props == null) {
            return null;
        }
        if (this.props instanceof Hashtable) {
            return (Map<String, Object>)((Hashtable)this.props).clone();
        }
        if (this.props instanceof HashMap) {
            return (Map<String, Object>)((HashMap)this.props).clone();
        }
        if (this.props instanceof TreeMap) {
            return (Map<String, Object>)((TreeMap)this.props).clone();
        }
        try {
            final Map<String, Object> newprops = (Map<String, Object>)this.props.getClass().newInstance();
            newprops.putAll(this.props);
            return newprops;
        }
        catch (Exception ex) {
            System.out.println("problem trying to clone SymWithProps properties, returning null instead");
            return null;
        }
    }
    
    public boolean setProperties(final Map<String, Object> propmap) {
        this.props = propmap;
        return true;
    }
    
    @Override
    public String getID() {
        return (String)this.getProperty("id");
    }
    
    public void setID(final String id) {
        this.setProperty("id", id);
    }
    
    @Override
    public boolean setProperty(final String name, final Object val) {
        if (name == null) {
            return false;
        }
        if (this.props == null) {
            this.props = new HashMap<String, Object>();
        }
        this.props.put(name, val);
        return true;
    }
    
    @Override
    public Object getProperty(final String name) {
        if (this.props == null) {
            return null;
        }
        return this.props.get(name);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.props == null) ? 0 : this.props.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SimpleSymWithProps other = (SimpleSymWithProps)obj;
        if (this.props == null) {
            if (other.props != null) {
                return false;
            }
        }
        else if (!this.props.equals(other.props)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return super.toString() + ";props:" + ((this.props == null) ? "null" : this.props.toString());
    }
}
