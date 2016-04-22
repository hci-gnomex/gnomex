// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.io.IOException;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;

public interface AnnotationWriter
{
    boolean writeAnnotations(final Collection<? extends SeqSymmetry> p0, final BioSeq p1, final String p2, final OutputStream p3) throws IOException;
    
    String getMimeType();
}
