// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.regionfinder;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;

public interface RegionFinder
{
    SeqSpan findInterestingRegion(final BioSeq p0, final List<SeqSymmetry> p1);
}
