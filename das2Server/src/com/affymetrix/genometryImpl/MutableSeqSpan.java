// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

public interface MutableSeqSpan extends SeqSpan
{
    void set(final int p0, final int p1, final BioSeq p2);
    
    void setCoords(final int p0, final int p1);
    
    void setStart(final int p0);
    
    void setEnd(final int p0);
    
    void setBioSeq(final BioSeq p0);
    
    void setStartDouble(final double p0);
    
    void setEndDouble(final double p0);
    
    void setDouble(final double p0, final double p1, final BioSeq p2);
}
