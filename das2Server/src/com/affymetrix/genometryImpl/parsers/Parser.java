// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;

public interface Parser
{
    List<? extends SeqSymmetry> parse(final InputStream p0, final AnnotatedSeqGroup p1, final String p2, final String p3, final boolean p4) throws Exception;
}
