// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.filter;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;

public interface SymmetryFilterI
{
    String getName();
    
    boolean setParam(final Object p0);
    
    Object getParam();
    
    boolean filterSymmetry(final BioSeq p0, final SeqSymmetry p1);
}
