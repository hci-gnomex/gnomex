// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.span;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.MutableSeqSpan;

public class SimpleMutableSeqSpan extends SimpleSeqSpan implements MutableSeqSpan
{
    public SimpleMutableSeqSpan(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
    }
    
    public SimpleMutableSeqSpan(final SeqSpan span) {
        this(span.getStart(), span.getEnd(), span.getBioSeq());
    }
    
    public SimpleMutableSeqSpan() {
        this(0, 0, null);
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
        this.start = (int)start;
        this.end = (int)end;
        this.seq = seq;
    }
    
    @Override
    public void setStartDouble(final double start) {
        this.start = (int)start;
    }
    
    @Override
    public void setEndDouble(final double end) {
        this.end = (int)end;
    }
}
