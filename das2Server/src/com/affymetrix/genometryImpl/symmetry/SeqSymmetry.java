// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;

public interface SeqSymmetry
{
    String getID();
    
    int getSpanCount();
    
    SeqSpan getSpan(final BioSeq p0);
    
    SeqSpan getSpan(final int p0);
    
    boolean getSpan(final BioSeq p0, final MutableSeqSpan p1);
    
    boolean getSpan(final int p0, final MutableSeqSpan p1);
    
    BioSeq getSpanSeq(final int p0);
    
    int getChildCount();
    
    SeqSymmetry getChild(final int p0);
}
