// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class UserGroupComparator implements Comparator<UserGroup>, Serializable
{
    @Override
    public int compare(final UserGroup g1, final UserGroup g2) {
        return g1.getIdUserGroup().compareTo(g2.getIdUserGroup());
    }
}
