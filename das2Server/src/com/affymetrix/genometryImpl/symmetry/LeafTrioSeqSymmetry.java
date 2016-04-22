// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;

public final class LeafTrioSeqSymmetry implements SeqSymmetry
{
    private int startA;
    private int startB;
    private int endA;
    private int endB;
    private int startC;
    private int endC;
    private BioSeq seqA;
    private BioSeq seqB;
    private BioSeq seqC;
    
    public LeafTrioSeqSymmetry(final int startA, final int endA, final BioSeq seqA, final int startB, final int endB, final BioSeq seqB, final int startC, final int endC, final BioSeq seqC) {
        this.startA = startA;
        this.startB = startB;
        this.startC = startC;
        this.endA = endA;
        this.endB = endB;
        this.endC = endC;
        this.seqA = seqA;
        this.seqB = seqB;
        this.seqC = seqC;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq seq) {
        if (this.seqA == seq) {
            return new SimpleSeqSpan(this.startA, this.endA, this.seqA);
        }
        if (this.seqB == seq) {
            return new SimpleSeqSpan(this.startB, this.endB, this.seqB);
        }
        if (this.seqC == seq) {
            return new SimpleSeqSpan(this.startC, this.endC, this.seqC);
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        return 3;
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        if (i == 0) {
            return new SimpleSeqSpan(this.startA, this.endA, this.seqA);
        }
        if (i == 1) {
            return new SimpleSeqSpan(this.startB, this.endB, this.seqB);
        }
        if (i == 2) {
            return new SimpleSeqSpan(this.startC, this.endC, this.seqC);
        }
        return null;
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        if (i == 0) {
            return this.seqA;
        }
        if (i == 1) {
            return this.seqB;
        }
        if (i == 2) {
            return this.seqC;
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq seq, final MutableSeqSpan span) {
        if (this.seqA == seq) {
            span.set(this.startA, this.endA, this.seqA);
        }
        else if (this.seqB == seq) {
            span.set(this.startB, this.endB, this.seqB);
        }
        else {
            if (this.seqC != seq) {
                return false;
            }
            span.set(this.startC, this.endC, this.seqC);
        }
        return true;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.set(this.startA, this.endA, this.seqA);
        }
        else if (index == 1) {
            span.set(this.startB, this.endB, this.seqB);
        }
        else {
            if (index != 2) {
                return false;
            }
            span.set(this.startC, this.endC, this.seqC);
        }
        return true;
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
