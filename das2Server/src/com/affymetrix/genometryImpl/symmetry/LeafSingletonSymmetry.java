// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;

public class LeafSingletonSymmetry extends SimpleMutableSeqSpan implements SeqSymmetry
{
    private static final int count = 1;
    
    public LeafSingletonSymmetry(final SeqSpan span) {
        this.start = span.getStart();
        this.end = span.getEnd();
        this.seq = span.getBioSeq();
    }
    
    public LeafSingletonSymmetry(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq seq) {
        if (this.getBioSeq() == seq) {
            return this;
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        return 1;
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        if (i == 0) {
            return this;
        }
        return null;
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        if (i == 0) {
            return this.seq;
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq seq, final MutableSeqSpan span) {
        if (this.getBioSeq() == seq) {
            span.setStart(this.getStart());
            span.setEnd(this.getEnd());
            span.setBioSeq(this.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.setStart(this.getStart());
            span.setEnd(this.getEnd());
            span.setBioSeq(this.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        return null;
    }
    
    @Override
    public String getID() {
        return null;
    }
}
