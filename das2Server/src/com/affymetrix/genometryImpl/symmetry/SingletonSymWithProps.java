//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symmetry;

import java.util.Hashtable;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;

public class SingletonSymWithProps extends MutableSingletonSeqSymmetry implements SymWithProps
{
    Map<String, Object> props;

    public SingletonSymWithProps(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
    }

    public SingletonSymWithProps(final CharSequence id, final int start, final int end, final BioSeq seq) {
        super(id, start, end, seq);
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.props;
    }

    @Override
    public String getID() {
        if (this.id != null) {
            return this.id.toString();
        }
        if (this.props != null) {
            return (String) this.props.get("id");
        }
        return null;
    }

    @Override
    public Map<String, Object> cloneProperties() {
        if (this.props == null) {
            return null;
        }
        try {
            final Map<String, Object> newprops = (Map<String, Object>)this.props.getClass().newInstance();
            newprops.putAll(this.props);
            return newprops;
        }
        catch (Exception ex) {
            System.out.println("problem trying to clone SingletonSymWithProps properties, returning null instead");
            return null;
        }
    }

    @Override
    public boolean setProperty(final String name, final Object val) {
        if (this.props == null) {
            this.props = new Hashtable<String, Object>();
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
}
