// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI;

public final class Das2Source
{
    private final URI source_uri;
    private final String name;
    private final Map<String, Das2VersionedSource> versions;
    private final Das2ServerInfo server;
    
    public Das2Source(final Das2ServerInfo source_server, final URI src_uri, final String source_name) {
        this.versions = new LinkedHashMap<String, Das2VersionedSource>();
        this.source_uri = src_uri;
        this.server = source_server;
        this.name = source_name;
    }
    
    public String getID() {
        return this.source_uri.toString();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    Das2ServerInfo getServerInfo() {
        return this.server;
    }
    
    public synchronized Map<String, Das2VersionedSource> getVersions() {
        return this.versions;
    }
    
    synchronized void addVersion(final Das2VersionedSource version) {
        this.versions.put(version.getID(), version);
    }
}
