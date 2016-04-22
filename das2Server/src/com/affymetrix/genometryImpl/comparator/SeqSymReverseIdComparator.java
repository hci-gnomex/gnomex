// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.io.Serializable;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Comparator;

public final class SeqSymReverseIdComparator implements Comparator<SeqSymmetry>, Serializable
{
    public static final long serialVersionUID = 1L;
    
    @Override
    public int compare(final SeqSymmetry sym1, final SeqSymmetry sym2) {
        return compareReverseStrings(sym1.getID(), sym2.getID());
    }
    
    private static int compareReverseStrings(final String id1, final String id2) {
        if (id1 == null || id2 == null) {
            return SeqSymIdComparator.compareNullIDs(id1, id2);
        }
        StringBuffer IDbuffer = new StringBuffer(id1);
        IDbuffer = IDbuffer.reverse();
        final String tempID1 = IDbuffer.toString();
        IDbuffer = new StringBuffer(id2);
        IDbuffer = IDbuffer.reverse();
        final String tempID2 = IDbuffer.toString();
        return tempID1.compareTo(tempID2);
    }
}
