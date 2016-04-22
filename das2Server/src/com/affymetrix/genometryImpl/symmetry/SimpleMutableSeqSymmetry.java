// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.ArrayList;
import com.affymetrix.genometryImpl.SeqSpan;

public class SimpleMutableSeqSymmetry extends SimpleSeqSymmetry implements MutableSeqSymmetry
{
    @Override
    public void addSpan(final SeqSpan span) {
        if (this.spans == null) {
            this.spans = new ArrayList<SeqSpan>();
        }
        this.spans.add(span);
    }
    
    @Override
    public void removeSpan(final SeqSpan span) {
        if (this.spans != null) {
            this.spans.remove(span);
        }
    }
    
    @Override
    public void addChild(final SeqSymmetry sym) {
        if (this.children == null) {
            this.children = new ArrayList<SeqSymmetry>();
        }
        this.children.add(sym);
    }
    
    @Override
    public void removeChild(final SeqSymmetry sym) {
        this.children.remove(sym);
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (this.children == null || index >= this.children.size()) {
            return null;
        }
        return this.children.get(index);
    }
    
    @Override
    public int getChildCount() {
        if (this.children == null) {
            return 0;
        }
        return this.children.size();
    }
    
    @Override
    public void removeChildren() {
        this.children = null;
    }
    
    @Override
    public void removeSpans() {
        this.spans = null;
    }
    
    @Override
    public void clear() {
        this.removeChildren();
        this.removeSpans();
    }
}
