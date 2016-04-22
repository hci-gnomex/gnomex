// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.io.Serializable;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Comparator;

public final class GenomeVersionDateComparator implements Comparator<AnnotatedSeqGroup>, Serializable
{
    public static final long serialVersionUID = 1L;
    private static final Comparator<String> stringComp;
    
    @Override
    public int compare(final AnnotatedSeqGroup group1, final AnnotatedSeqGroup group2) {
        return GenomeVersionDateComparator.stringComp.compare(group1.getID(), group2.getID());
    }
    
    static {
        stringComp = new StringVersionDateComparator();
    }
}
