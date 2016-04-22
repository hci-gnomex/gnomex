// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class SegmentComparator implements Comparator<Segment>, Serializable
{
    @Override
    public int compare(final Segment s1, final Segment s2) {
        return s1.getIdSegment().compareTo(s2.getIdSegment());
    }
}
