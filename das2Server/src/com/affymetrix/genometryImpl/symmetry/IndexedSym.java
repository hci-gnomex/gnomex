// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

public interface IndexedSym extends SeqSymmetry
{
    void setParent(final ScoredContainerSym p0);
    
    ScoredContainerSym getParent();
    
    void setIndex(final int p0);
    
    int getIndex();
}
