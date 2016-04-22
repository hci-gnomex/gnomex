//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.filter;

import com.affymetrix.genometryImpl.symmetry.BAMSym;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;

public class UniqueLocationFilter implements SymmetryFilterI
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
        if (!(ss instanceof BAMSym)) {
            return false;
        }
        if (((BAMSym)ss).getProperty("NH") == null) {
            return false;
        }
        final int currentNH = (Integer)((BAMSym)ss).getProperty("NH");
        return currentNH == 1;
    }
}
