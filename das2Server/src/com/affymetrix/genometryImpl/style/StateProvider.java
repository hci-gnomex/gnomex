// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.util.Map;

public interface StateProvider
{
    ITrackStyleExtended getAnnotStyle(final String p0);
    
    ITrackStyleExtended getAnnotStyle(final String p0, final String p1, final String p2, final Map<String, String> p3);
    
    GraphState getGraphState(final String p0);
    
    GraphState getGraphState(final String p0, final String p1, final String p2, final Map<String, String> p3);
}
