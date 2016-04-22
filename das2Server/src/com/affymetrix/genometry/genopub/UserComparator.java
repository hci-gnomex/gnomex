// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class UserComparator implements Comparator<User>, Serializable
{
    @Override
    public int compare(final User u1, final User u2) {
        return u1.getIdUser().compareTo(u2.getIdUser());
    }
}
