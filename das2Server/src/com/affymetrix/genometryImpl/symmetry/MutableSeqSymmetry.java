// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.SeqSpan;

public interface MutableSeqSymmetry extends SeqSymmetry
{
    void addSpan(final SeqSpan p0);
    
    void removeSpan(final SeqSpan p0);
    
    void addChild(final SeqSymmetry p0);
    
    void removeChild(final SeqSymmetry p0);
    
    void removeChildren();
    
    void removeSpans();
    
    void clear();
}
