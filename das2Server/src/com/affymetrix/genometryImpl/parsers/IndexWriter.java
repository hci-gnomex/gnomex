// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.Comparator;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;

public interface IndexWriter
{
    void writeSymmetry(final SeqSymmetry p0, final BioSeq p1, final OutputStream p2) throws IOException;
    
    List<? extends SeqSymmetry> parse(final DataInputStream p0, final String p1, final AnnotatedSeqGroup p2);
    
    Comparator<? extends SeqSymmetry> getComparator(final BioSeq p0);
    
    int getMin(final SeqSymmetry p0, final BioSeq p1);
    
    int getMax(final SeqSymmetry p0, final BioSeq p1);
    
    List<String> getFormatPrefList();
}
