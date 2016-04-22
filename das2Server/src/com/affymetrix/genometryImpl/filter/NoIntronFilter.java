// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.filter;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;

public class NoIntronFilter implements SymmetryFilterI
{
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public boolean setParam(final Object o) {
        return false;
    }
    
    @Override
    public Object getParam() {
        return null;
    }
    
    @Override
    public boolean filterSymmetry(final BioSeq bioseq, final SeqSymmetry ss) {
        return ss.getChildCount() > 1;
    }
}
