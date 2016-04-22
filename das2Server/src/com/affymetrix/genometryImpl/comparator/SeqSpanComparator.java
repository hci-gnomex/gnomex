// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.io.Serializable;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Comparator;

public final class SeqSpanComparator implements Comparator<SeqSpan>, Serializable
{
    public static final long serialVersionUID = 1L;
    
    @Override
    public int compare(final SeqSpan span1, final SeqSpan span2) {
        return compareSpans(span1, span2);
    }
    
    static int compareSpans(final SeqSpan span1, final SeqSpan span2) {
        final int min1 = span1.getMin();
        final int min2 = span2.getMin();
        if (min1 != min2) {
            return Integer.valueOf(min1).compareTo(Integer.valueOf(min2));
        }
        return Integer.valueOf(span1.getMax()).compareTo(Integer.valueOf(span2.getMax()));
    }
}
