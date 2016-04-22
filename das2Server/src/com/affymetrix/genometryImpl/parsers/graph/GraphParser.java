// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.Parser;

public interface GraphParser extends Parser
{
    List<GraphSym> readGraphs(final InputStream p0, final String p1, final AnnotatedSeqGroup p2, final BioSeq p3) throws IOException;
    
    void writeGraphFile(final GraphSym p0, final AnnotatedSeqGroup p1, final String p2) throws IOException;
}
