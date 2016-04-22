// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

public final class SimpleDerivedSeqSymmetry extends SimpleMutableSeqSymmetry implements DerivedSeqSymmetry
{
    SeqSymmetry original_sym;
    
    @Override
    public SeqSymmetry getOriginalSymmetry() {
        return this.original_sym;
    }
    
    @Override
    public void setOriginalSymmetry(final SeqSymmetry sym) {
        this.original_sym = sym;
    }
}
