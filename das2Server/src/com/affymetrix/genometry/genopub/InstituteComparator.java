// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class InstituteComparator implements Comparator<Institute>, Serializable
{
    @Override
    public int compare(final Institute i1, final Institute i2) {
        return i1.getIdInstitute().compareTo(i2.getIdInstitute());
    }
}
