// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.LinkedHashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.TypedSym;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;

public final class SimpleDas2Feature extends SimpleSymWithProps implements TypedSym
{
    String id;
    String type;
    String name;
    String created;
    String modified;
    String doc_href;
    String parent_id;
    
    public SimpleDas2Feature(final String feat_id, final String feat_type, final String feat_name, final String feat_parent_id, final String feat_created, final String feat_modified, final String feat_doc_href, final Map<String, Object> feat_props) {
        this.id = feat_id;
        this.type = feat_type;
        this.name = feat_name;
        this.parent_id = feat_parent_id;
        this.created = feat_created;
        this.modified = feat_modified;
        this.doc_href = feat_doc_href;
        this.setProperties(feat_props);
    }
    
    @Override
    public String getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public Object getProperty(final String prop) {
        if (prop.equals("id")) {
            return this.id;
        }
        if (prop.equals("name")) {
            return this.name;
        }
        if (prop.equals("type")) {
            return this.type;
        }
        if (prop.equals("link")) {
            return this.doc_href;
        }
        if (prop.equals("created")) {
            return this.created;
        }
        if (prop.equals("modified")) {
            return this.modified;
        }
        return super.getProperty(prop);
    }
    
    @Override
    public boolean setProperty(final String tag, final Object val) {
        return tag != null && super.setProperty(tag, val);
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        Map<String, Object> cprops = super.cloneProperties();
        if (cprops == null) {
            cprops = new LinkedHashMap<String, Object>();
        }
        cprops.put("id", this.id);
        if (this.name != null) {
            cprops.put("name", this.name);
        }
        if (this.type != null) {
            cprops.put("type", this.type);
        }
        if (this.doc_href != null) {
            cprops.put("link", this.doc_href);
        }
        if (this.created != null) {
            cprops.put("created", this.created);
        }
        if (this.modified != null) {
            cprops.put("modified", this.modified);
        }
        return cprops;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.cloneProperties();
    }
}
