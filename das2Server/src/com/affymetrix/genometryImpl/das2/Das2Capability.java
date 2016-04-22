// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI;

public final class Das2Capability
{
    private final String type;
    private final URI root_uri;
    private static final Map<String, Das2VersionedSource> cap2version;
    
    Das2Capability(final String cap_type, final URI cap_root) {
        this.type = cap_type;
        this.root_uri = cap_root;
    }
    
    public static Map<String, Das2VersionedSource> getCapabilityMap() {
        return Das2Capability.cap2version;
    }
    
    String getType() {
        return this.type;
    }
    
    public URI getRootURI() {
        return this.root_uri;
    }
    
    static {
        cap2version = new LinkedHashMap<String, Das2VersionedSource>();
    }
}
