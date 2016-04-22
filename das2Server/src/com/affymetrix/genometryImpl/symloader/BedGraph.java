// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public class BedGraph extends Wiggle implements AnnotationWriter
{
    private static final String TRACK_TYPE = "bedgraph";
    
    public BedGraph() {
        this(null, null, null);
    }
    
    public BedGraph(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
    }
    
    @Override
    protected String getTrackType() {
        return "bedgraph";
    }
    
    @Override
    public String getMimeType() {
        return "text/bedgraph";
    }
}
