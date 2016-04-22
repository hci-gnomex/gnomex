// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import affymetrix.calvin.data.ProbeSetQuantificationData;
import java.util.Comparator;

public final class QuantByIntIdComparator implements Comparator<ProbeSetQuantificationData>
{
    @Override
    public int compare(final ProbeSetQuantificationData dataA, final ProbeSetQuantificationData dataB) {
        final int idA = dataA.getId();
        final int idB = dataB.getId();
        return Integer.valueOf(idA).compareTo(Integer.valueOf(idB));
    }
}
