// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.filter;

import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;

public class SymmetryFilterIntersecting implements SymmetryFilterI
{
    private Object param;
    private SeqSymmetry original_sym;
    private final MutableSeqSymmetry dummySym;
    
    public SymmetryFilterIntersecting() {
        this.dummySym = new SimpleMutableSeqSymmetry();
    }
    
    @Override
    public String getName() {
        return "existing";
    }
    
    @Override
    public boolean setParam(final Object param) {
        this.param = param;
        if (!(param instanceof SeqSymmetry)) {
            return false;
        }
        this.original_sym = (SeqSymmetry)param;
        return true;
    }
    
    @Override
    public Object getParam() {
        return this.param;
    }
    
    @Override
    public boolean filterSymmetry(final BioSeq seq, final SeqSymmetry sym) {
        if (sym instanceof GraphSym) {
            return true;
        }
        this.dummySym.clear();
        return !SeqUtils.intersection(sym, this.original_sym, this.dummySym, seq);
    }
}
