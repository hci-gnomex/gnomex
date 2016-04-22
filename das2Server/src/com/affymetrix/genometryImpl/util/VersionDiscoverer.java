// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.general.GenericVersion;
import com.affymetrix.genometryImpl.general.GenericServer;

public interface VersionDiscoverer
{
    GenericVersion discoverVersion(final String p0, final String p1, final GenericServer p2, final Object p3, final String p4);
    
    String versionName2Species(final String p0);
}
