// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;

public final class SimplePairSeqSymmetry implements SeqSymmetry
{
    private static int count;
    private SeqSpan spanA;
    private SeqSpan spanB;
    
    public SimplePairSeqSymmetry(final SeqSpan spanA, final SeqSpan spanB) {
        this.spanA = spanA;
        this.spanB = spanB;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq seq) {
        if (this.spanA.getBioSeq() == seq) {
            return this.spanA;
        }
        if (this.spanB.getBioSeq() == seq) {
            return this.spanB;
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        return SimplePairSeqSymmetry.count;
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        if (i == 0) {
            return this.spanA;
        }
        if (i == 1) {
            return this.spanB;
        }
        return null;
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        if (i == 0) {
            return this.spanA.getBioSeq();
        }
        if (i == 1) {
            return this.spanB.getBioSeq();
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq seq, final MutableSeqSpan span) {
        if (seq == this.spanA.getBioSeq()) {
            span.setStart(this.spanA.getStart());
            span.setEnd(this.spanA.getEnd());
            span.setBioSeq(this.spanA.getBioSeq());
            return true;
        }
        if (seq == this.spanB.getBioSeq()) {
            span.setStart(this.spanB.getStart());
            span.setEnd(this.spanB.getEnd());
            span.setBioSeq(this.spanB.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.setStart(this.spanA.getStart());
            span.setEnd(this.spanA.getEnd());
            span.setBioSeq(this.spanA.getBioSeq());
            return true;
        }
        if (index == 1) {
            span.setStart(this.spanB.getStart());
            span.setEnd(this.spanB.getEnd());
            span.setBioSeq(this.spanB.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        return null;
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public String getID() {
        return null;
    }
    
    static {
        SimplePairSeqSymmetry.count = 2;
    }
}
