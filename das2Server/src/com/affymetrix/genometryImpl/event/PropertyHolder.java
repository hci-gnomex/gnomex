// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Map;
import java.util.List;

public interface PropertyHolder
{
    List<Map<String, Object>> getProperties();
    
    Map<String, Object> determineProps(final SeqSymmetry p0);
}
