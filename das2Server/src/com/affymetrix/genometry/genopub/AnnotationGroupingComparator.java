// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class AnnotationGroupingComparator implements Comparator<AnnotationGrouping>, Serializable
{
    @Override
    public int compare(final AnnotationGrouping ag1, final AnnotationGrouping ag2) {
        return ag1.getIdAnnotationGrouping().compareTo(ag2.getIdAnnotationGrouping());
    }
}
