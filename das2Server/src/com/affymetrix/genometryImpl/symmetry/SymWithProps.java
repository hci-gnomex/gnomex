// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Map;

public interface SymWithProps extends SeqSymmetry
{
    Map<String, Object> getProperties();
    
    Map<String, Object> cloneProperties();
    
    Object getProperty(final String p0);
    
    boolean setProperty(final String p0, final Object p1);
}
