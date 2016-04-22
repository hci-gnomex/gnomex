// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

final class Das2Coords
{
    private final String urid;
    private final String authority;
    private final String taxid;
    private final String version;
    private final String source;
    private final String test_range;
    
    Das2Coords(final String urid, final String authority, final String taxid, final String version, final String source, final String test_range) {
        this.urid = urid;
        this.authority = authority;
        this.taxid = taxid;
        this.version = version;
        this.source = source;
        this.test_range = test_range;
    }
    
    String getURI() {
        return this.urid;
    }
    
    String getAuthority() {
        return this.authority;
    }
    
    String getTaxid() {
        return this.taxid;
    }
    
    String getVersion() {
        return this.version;
    }
    
    String getSource() {
        return this.source;
    }
    
    String getTestRange() {
        return this.test_range;
    }
}
