// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.SeqSpan;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;

public class MutableSingletonSeqSymmetry extends SingletonSeqSymmetry implements MutableSeqSymmetry
{
    protected CharSequence id;
    
    public MutableSingletonSeqSymmetry(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
    }
    
    public MutableSingletonSeqSymmetry(final CharSequence id, final int start, final int end, final BioSeq seq) {
        this(start, end, seq);
        this.id = id;
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
    public void removeChildren() {
        this.children = null;
    }
    
    @Override
    public void removeSpans() {
        throw new RuntimeException("can't removeSpans(), MutableSingletonSeqSymmetry is not mutable itself, only its children");
    }
    
    @Override
    public void clear() {
        throw new RuntimeException("can't clear(), MutableSingletonSeqSymmetry is not mutable itself, only its children");
    }
    
    @Override
    public void addSpan(final SeqSpan span) {
        throw new RuntimeException("Operation Not Allowed. Can't add a span to a SingletonSeqSymmetry.");
    }
    
    @Override
    public void removeSpan(final SeqSpan span) {
        throw new RuntimeException("Operation Not Allowed. Can't remove a span froma a SingletonSeqSymmetry.");
    }
    
    @Override
    public String getID() {
        return (this.id != null) ? this.id.toString() : null;
    }
}
