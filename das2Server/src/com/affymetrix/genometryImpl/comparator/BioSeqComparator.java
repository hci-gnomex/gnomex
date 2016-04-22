// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import com.affymetrix.genometryImpl.BioSeq;
import java.util.Comparator;

public final class BioSeqComparator implements Comparator<BioSeq>
{
    @Override
    public int compare(final BioSeq o1, final BioSeq o2) {
        return compareStrings(o1.getID(), o2.getID());
    }
    
    private static int compareStrings(final String id1, final String id2) {
        if (id1 == null || id2 == null) {
            return compareNullIDs(id1, id2);
        }
        return id1.compareTo(id2);
    }
    
    static int compareNullIDs(final String id1, final String id2) {
        if (id1 != null) {
            return -1;
        }
        if (id2 == null) {
            return 0;
        }
        return 1;
    }
}
