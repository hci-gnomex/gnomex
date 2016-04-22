// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Comparator;

public final class SeqSymStartComparator implements Comparator<SeqSymmetry>
{
    private final boolean ascending;
    private final BioSeq seq;
    
    public SeqSymStartComparator(final BioSeq s, final boolean b) {
        this.seq = s;
        this.ascending = b;
    }
    
    @Override
    public int compare(final SeqSymmetry sym1, final SeqSymmetry sym2) {
        final SeqSpan span1 = sym1.getSpan(this.seq);
        final SeqSpan span2 = sym2.getSpan(this.seq);
        if (this.ascending) {
            return Integer.valueOf(span1.getStart()).compareTo(Integer.valueOf(span2.getStart()));
        }
        return Integer.valueOf(span2.getStart()).compareTo(Integer.valueOf(span1.getStart()));
    }
}
