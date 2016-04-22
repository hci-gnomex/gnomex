// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class PropertyOptionComparator implements Comparator<PropertyOption>, Serializable
{
    @Override
    public int compare(final PropertyOption o1, final PropertyOption o2) {
        return o1.getIdPropertyOption().compareTo(o2.getIdPropertyOption());
    }
}
