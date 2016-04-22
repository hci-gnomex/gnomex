// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

public interface DerivedSeqSymmetry extends MutableSeqSymmetry
{
    SeqSymmetry getOriginalSymmetry();
    
    void setOriginalSymmetry(final SeqSymmetry p0);
}
