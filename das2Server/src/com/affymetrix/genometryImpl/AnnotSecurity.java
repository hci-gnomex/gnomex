// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import java.util.Map;

public interface AnnotSecurity
{
    boolean isGuestRole();
    
    boolean isAuthorized(final String p0, final String p1, final Object p2);
    
    Map<String, Object> getProperties(final String p0, final String p1, final Object p2);
    
    boolean isBarGraphData(final String p0, final String p1, final String p2, final Object p3);
    
    boolean isUseqGraphData(final String p0, final String p1, final String p2, final Object p3);
    
    boolean isBamData(final String p0, final String p1, final String p2, final Object p3);
    
    String getSequenceDirectory(final String p0, final AnnotatedSeqGroup p1) throws Exception;
}
