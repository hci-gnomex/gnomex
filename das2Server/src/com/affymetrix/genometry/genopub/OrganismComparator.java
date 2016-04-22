// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class OrganismComparator implements Comparator<Organism>, Serializable
{
    @Override
    public int compare(final Organism org1, final Organism org2) {
        return org1.getBinomialName().compareTo(org2.getBinomialName());
    }
}
