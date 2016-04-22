// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;

public interface FileTypeHandler
{
    String getName();
    
    String[] getExtensions();
    
    SymLoader createSymLoader(final URI p0, final String p1, final AnnotatedSeqGroup p2);
    
    Parser getParser();
    
    IndexWriter getIndexWriter(final String p0);
    
    FileTypeCategory getFileTypeCategory();
}
