// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.span;

import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;

public class SimpleSeqSpan implements SeqSpan, Cloneable
{
    protected int start;
    protected int end;
    protected BioSeq seq;
    
    public SimpleSeqSpan(final int start, final int end, final BioSeq seq) {
        this.start = start;
        this.end = end;
        this.seq = seq;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public int getStart() {
        return this.start;
    }
    
    @Override
    public int getEnd() {
        return this.end;
    }
    
    @Override
    public int getLength() {
        return (this.end > this.start) ? (this.end - this.start) : (this.start - this.end);
    }
    
    @Override
    public BioSeq getBioSeq() {
        return this.seq;
    }
    
    @Override
    public int getMin() {
        return (this.start < this.end) ? this.start : this.end;
    }
    
    @Override
    public int getMax() {
        return (this.end > this.start) ? this.end : this.start;
    }
    
    @Override
    public boolean isForward() {
        return this.end >= this.start;
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.end;
        result = 31 * result + ((this.seq == null) ? 0 : this.seq.hashCode());
        result = 31 * result + this.start;
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
        final SimpleSeqSpan other = (SimpleSeqSpan)obj;
        if (this.end != other.end) {
            return false;
        }
        if (this.seq == null) {
            if (other.seq != null) {
                return false;
            }
        }
        else if (!this.seq.equals(other.seq)) {
            return false;
        }
        return this.start == other.start;
    }
    
    @Override
    public String toString() {
        return this.seq.toString() + ":" + this.start + "-" + this.end;
    }
}
