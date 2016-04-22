// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;

final class BarSeqHeader
{
    BioSeq aseq;
    int data_point_count;
    Map<String, String> tagvals;
    
    BarSeqHeader(final BioSeq seq, final int data_points, final Map<String, String> tagvals) {
        this.aseq = seq;
        this.data_point_count = data_points;
        this.tagvals = tagvals;
    }
}
