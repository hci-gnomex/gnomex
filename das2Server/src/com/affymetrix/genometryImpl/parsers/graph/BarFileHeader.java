// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.util.Map;

final class BarFileHeader
{
    float version;
    int seq_count;
    int vals_per_point;
    int[] val_types;
    int bytes_per_point;
    Map<String, String> tagvals;
    
    BarFileHeader(final float version, final int seq_count, final int[] val_types, final Map<String, String> tagvals) {
        this.bytes_per_point = 0;
        this.version = version;
        this.seq_count = seq_count;
        this.val_types = val_types;
        this.vals_per_point = val_types.length;
        this.tagvals = tagvals;
        for (int i = 0; i < val_types.length; ++i) {
            final int valtype = val_types[i];
            this.bytes_per_point += BarParser.bytes_per_val[valtype];
        }
    }
}
