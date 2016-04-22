// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import java.util.Map;

public final class SharedProbesetInfo
{
    BioSeq seq;
    int probe_length;
    String id_prefix;
    Map<String, Object> props;
    
    public SharedProbesetInfo(final BioSeq seq, final int probe_length, final String id_prefix, final Map<String, Object> props) {
        this.seq = seq;
        this.probe_length = probe_length;
        this.id_prefix = id_prefix;
        this.props = props;
    }
    
    public BioSeq getBioSeq() {
        return this.seq;
    }
    
    public int getProbeLength() {
        return this.probe_length;
    }
    
    public String getIDPrefix() {
        return this.id_prefix;
    }
    
    public Map<String, Object> getProps() {
        return this.props;
    }
}
