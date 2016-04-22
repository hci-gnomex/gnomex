// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.io.Serializable;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Comparator;

public final class SeqSymIdComparator implements Comparator<SeqSymmetry>, Serializable
{
    public static final long serialVersionUID = 1L;
    
    @Override
    public int compare(final SeqSymmetry sym1, final SeqSymmetry sym2) {
        return compareStrings(sym1.getID(), sym2.getID());
    }
    
    private static int compareStrings(final String id1, final String id2) {
        if (id1 == null || id2 == null) {
            return compareNullIDs(id1, id2);
        }
        return id1.compareTo(id2);
    }
    
    public static int compareNullIDs(final String id1, final String id2) {
        if (id1 != null) {
            return -1;
        }
        if (id2 == null) {
            return 0;
        }
        return 1;
    }
}
