// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.List;
import com.affymetrix.genometryImpl.SeqSpan;

public interface SearchableSeqSymmetry
{
    List<SeqSymmetry> getOverlappingChildren(final SeqSpan p0);
}
