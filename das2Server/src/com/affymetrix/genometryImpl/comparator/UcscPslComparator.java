// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.io.Serializable;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import java.util.Comparator;

public final class UcscPslComparator implements Comparator<UcscPslSym>, Serializable
{
    public static final long serialVersionUID = 1L;
    
    @Override
    public int compare(final UcscPslSym sym1, final UcscPslSym sym2) {
        final int min1 = sym1.getTargetMin();
        final int min2 = sym2.getTargetMin();
        if (min1 != min2) {
            return Integer.valueOf(min1).compareTo(Integer.valueOf(min2));
        }
        return Integer.valueOf(sym1.getTargetMax()).compareTo(Integer.valueOf(sym2.getTargetMax()));
    }
}
