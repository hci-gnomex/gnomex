// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;

public final class EfficientSnpSym implements SeqSymmetry, SeqSpan
{
    SeqSymmetry parent;
    int base_coord;
    int numeric_id;
    
    public EfficientSnpSym(final SeqSymmetry sym_parent, final int coord) {
        this.parent = sym_parent;
        this.base_coord = coord;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq bs) {
        if (this.getBioSeq() == bs) {
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
            return this.getBioSeq();
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq bs, final MutableSeqSpan span) {
        if (this.getBioSeq() == bs) {
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
        return Integer.toString(this.numeric_id);
    }
    
    @Override
    public int getStart() {
        return this.base_coord;
    }
    
    @Override
    public int getEnd() {
        return this.base_coord + 1;
    }
    
    @Override
    public int getMin() {
        return this.base_coord;
    }
    
    @Override
    public int getMax() {
        return this.base_coord + 1;
    }
    
    @Override
    public int getLength() {
        return 1;
    }
    
    @Override
    public boolean isForward() {
        return true;
    }
    
    @Override
    public BioSeq getBioSeq() {
        return this.parent.getSpanSeq(0);
    }
    
    @Override
    public double getStartDouble() {
        return this.getStart();
    }
    
    @Override
    public double getEndDouble() {
        return this.getEnd();
    }
    
    @Override
    public double getMinDouble() {
        return this.getMin();
    }
    
    @Override
    public double getMaxDouble() {
        return this.getMax();
    }
    
    @Override
    public double getLengthDouble() {
        return this.getLength();
    }
    
    @Override
    public boolean isIntegral() {
        return true;
    }
}
