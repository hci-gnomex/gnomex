// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.List;

public abstract class SimpleSeqSymmetry implements SeqSymmetry
{
    protected List<SeqSpan> spans;
    protected List<SeqSymmetry> children;
    
    public SimpleSeqSymmetry() {
        this.children = null;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq seq) {
        if (this.spans == null) {
            return null;
        }
        for (final SeqSpan span : this.spans) {
            if (span.getBioSeq() == seq) {
                return span;
            }
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        if (this.spans == null) {
            return 0;
        }
        return this.spans.size();
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        return this.spans.get(i);
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        final SeqSpan sp = this.getSpan(i);
        if (null != sp) {
            return sp.getBioSeq();
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        final SeqSpan vspan = this.spans.get(index);
        span.set(vspan.getStart(), vspan.getEnd(), vspan.getBioSeq());
        return true;
    }
    
    @Override
    public boolean getSpan(final BioSeq seq, final MutableSeqSpan span) {
        if (this.spans == null) {
            return false;
        }
        for (final SeqSpan vspan : this.spans) {
            if (vspan.getBioSeq() == seq) {
                span.set(vspan.getStart(), vspan.getEnd(), vspan.getBioSeq());
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getChildCount() {
        if (null != this.children) {
            return this.children.size();
        }
        return 0;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (null != this.children) {
            return this.children.get(index);
        }
        return null;
    }
    
    @Override
    public String getID() {
        return null;
    }
    
    protected List<SeqSymmetry> getChildren() {
        return this.children;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.children == null) ? 0 : new CopyOnWriteArrayList(this.children).hashCode());
        result = 31 * result + ((this.spans == null) ? 0 : new CopyOnWriteArrayList(this.spans).hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SimpleSeqSymmetry other = (SimpleSeqSymmetry)obj;
        if (this.children == null) {
            if (other.children != null) {
                return false;
            }
        }
        else {
            if (other.children == null) {
                return false;
            }
            if (!new CopyOnWriteArrayList(this.children).equals(new CopyOnWriteArrayList(other.children))) {
                return false;
            }
        }
        if (this.spans == null) {
            if (other.spans != null) {
                return false;
            }
        }
        else {
            if (other.spans == null) {
                return false;
            }
            if (!new CopyOnWriteArrayList(this.spans).equals(new CopyOnWriteArrayList(other.spans))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "children:" + ((this.children == null) ? "null" : Arrays.toString(this.children.toArray())) + ";spans:" + ((this.spans == null) ? "null" : Arrays.toString(this.spans.toArray()));
    }
}
