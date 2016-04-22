// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.regionfinder;

import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;

public class DefaultRegionFinder implements RegionFinder
{
    @Override
    public SeqSpan findInterestingRegion(final BioSeq aseq, final List<SeqSymmetry> syms) {
        List<SeqSymmetry> less_syms = new ArrayList<SeqSymmetry>();
        if (syms.size() > 100) {
            for (int size = syms.size(), inc = size / 100, i = 0; i < size; i += inc) {
                less_syms.add(syms.get(i));
            }
        }
        else {
            less_syms = syms;
        }
        final MutableSeqSymmetry resultSym = new SimpleMutableSeqSymmetry();
        SeqUtils.union(less_syms, resultSym, aseq);
        return resultSym.getSpan(aseq);
    }
}
