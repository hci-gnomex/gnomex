// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;

public interface SearchListener
{
    void searchResults(final String p0, final List<SeqSymmetry> p1);
}
