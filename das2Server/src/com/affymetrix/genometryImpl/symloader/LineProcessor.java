// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.SeqSpan;
import java.net.URI;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.BioSeq;

public interface LineProcessor
{
    List<? extends SeqSymmetry> processLines(final BioSeq p0, final LineReader p1, final LineTrackerI p2) throws Exception;
    
    void init(final URI p0) throws Exception;
    
    List<String> getFormatPrefList();
    
    SeqSpan getSpan(final String p0);
    
    boolean processInfoLine(final String p0, final List<String> p1);
}
