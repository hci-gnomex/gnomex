// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.span;

import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.MutableSeqSpan;

public final class MutableDoubleSeqSpan implements MutableSeqSpan, Cloneable
{
    private double start;
    private double end;
    private BioSeq seq;
    
    public MutableDoubleSeqSpan(final double start, final double end, final BioSeq seq) {
        this.start = start;
        this.end = end;
        this.seq = seq;
    }
    
    public MutableDoubleSeqSpan() {
        this(0.0, 0.0, null);
    }
    
    @Override
    public void set(final int start, final int end, final BioSeq seq) {
        this.start = start;
        this.end = end;
        this.seq = seq;
    }
    
    @Override
    public void setCoords(final int start, final int end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    public void setStart(final int start) {
        this.start = start;
    }
    
    @Override
    public void setEnd(final int end) {
        this.end = end;
    }
    
    @Override
    public void setBioSeq(final BioSeq seq) {
        this.seq = seq;
    }
    
    @Override
    public void setDouble(final double start, final double end, final BioSeq seq) {
        this.start = start;
        this.end = end;
        this.seq = seq;
    }
    
    @Override
    public void setStartDouble(final double start) {
        this.start = start;
    }
    
    @Override
    public void setEndDouble(final double end) {
        this.end = end;
    }
    
    @Override
    public boolean isIntegral() {
        return false;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public int getStart() {
        return (int)this.start;
    }
    
    @Override
    public int getEnd() {
        return (int)this.end;
    }
    
    @Override
    public int getMin() {
        return (int)this.getMinDouble();
    }
    
    @Override
    public int getMax() {
        return (int)this.getMaxDouble();
    }
    
    @Override
    public int getLength() {
        final double dl = this.getLengthDouble();
        if (dl > 2.147483647E9) {
            return 2147483646;
        }
        return (int)dl;
    }
    
    @Override
    public BioSeq getBioSeq() {
        return this.seq;
    }
    
    @Override
    public boolean isForward() {
        return this.end >= this.start;
    }
    
    @Override
    public double getMinDouble() {
        return (this.start < this.end) ? this.start : this.end;
    }
    
    @Override
    public double getMaxDouble() {
        return (this.end > this.start) ? this.end : this.start;
    }
    
    @Override
    public double getStartDouble() {
        return this.start;
    }
    
    @Override
    public double getEndDouble() {
        return this.end;
    }
    
    @Override
    public double getLengthDouble() {
        return (this.end > this.start) ? (this.end - this.start) : (this.start - this.end);
    }
    
    @Override
    public String toString() {
        return ((this.seq == null) ? "null" : this.seq.toString()) + ":" + this.start + "-" + this.end;
    }
}
