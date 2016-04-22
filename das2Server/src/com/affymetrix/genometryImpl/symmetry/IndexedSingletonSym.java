// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;

public final class IndexedSingletonSym extends SingletonSeqSymmetry implements IndexedSym, SymWithProps
{
    private int index_in_parent;
    private ScoredContainerSym parent;
    private String id;
    
    public IndexedSingletonSym(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
        this.index_in_parent = -1;
        this.parent = null;
        this.id = null;
    }
    
    @Override
    public void setParent(final ScoredContainerSym par) {
        this.parent = par;
    }
    
    @Override
    public void setIndex(final int index) {
        this.index_in_parent = index;
    }
    
    @Override
    public ScoredContainerSym getParent() {
        return this.parent;
    }
    
    @Override
    public int getIndex() {
        return this.index_in_parent;
    }
    
    public void setID(final String symid) {
        this.id = symid;
    }
    
    @Override
    public String getID() {
        return this.id;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props;
        if (this.id != null) {
            props = this.cloneProperties();
        }
        else {
            props = this.parent.getProperties();
        }
        return props;
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final Map<String, Object> props = this.parent.cloneProperties();
        if (this.id != null) {
            props.put("id", this.id);
        }
        return props;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("id")) {
            return this.id;
        }
        return this.parent.getProperty(key);
    }
    
    @Override
    public boolean setProperty(final String key, final Object val) {
        if (key.equals("id")) {
            this.setID((String)val);
            return true;
        }
        System.err.println("IndexedSingletonSym does not support setting properties, except for id");
        return false;
    }
}
