// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Date;
import java.io.Serializable;
import java.util.Comparator;

public class GenomeVersionComparator implements Comparator<GenomeVersion>, Serializable
{
    @Override
    public int compare(final GenomeVersion v1, final GenomeVersion v2) {
        if (v1.getBuildDate() != null && v2.getBuildDate() != null) {
            return v2.getBuildDate().compareTo((Date)v1.getBuildDate());
        }
        if (v1.getBuildDate() != null) {
            return 1;
        }
        if (v2.getBuildDate() != null) {
            return 2;
        }
        return v1.getName().compareTo(v2.getName());
    }
}
