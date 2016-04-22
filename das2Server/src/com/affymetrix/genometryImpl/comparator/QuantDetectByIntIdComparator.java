// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import affymetrix.calvin.data.ProbeSetQuantificationDetectionData;
import java.util.Comparator;

public final class QuantDetectByIntIdComparator implements Comparator<ProbeSetQuantificationDetectionData>
{
    @Override
    public int compare(final ProbeSetQuantificationDetectionData dataA, final ProbeSetQuantificationDetectionData dataB) {
        final int idA = dataA.getId();
        final int idB = dataB.getId();
        return Integer.valueOf(idA).compareTo(Integer.valueOf(idB));
    }
}
