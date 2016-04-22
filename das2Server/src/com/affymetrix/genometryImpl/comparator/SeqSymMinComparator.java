// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Comparator;

public final class SeqSymMinComparator implements Comparator<SeqSymmetry>
{
    private final BioSeq seq;
    
    public SeqSymMinComparator(final BioSeq s) {
        this.seq = s;
    }
    
    @Override
    public int compare(final SeqSymmetry sym1, final SeqSymmetry sym2) {
        final SeqSpan span1 = sym1.getSpan(this.seq);
        final SeqSpan span2 = sym2.getSpan(this.seq);
        return SeqSpanComparator.compareSpans(span1, span2);
    }
}
