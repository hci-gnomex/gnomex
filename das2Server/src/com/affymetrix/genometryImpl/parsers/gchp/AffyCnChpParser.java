// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import com.affymetrix.genometryImpl.BioSeq;
import java.util.Iterator;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.Parser;

public final class AffyCnChpParser implements Parser
{
    public List<SeqSymmetry> parse(final File file, final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final boolean annotateSeq) throws IOException {
        Logger.getLogger(AffyCnChpParser.class.getName()).log(Level.FINE, "Parsing with {0}: {1}", new Object[] { this.getClass().getName(), stream_name });
        final ChromLoadPolicy loadPolicy = ChromLoadPolicy.getLoadAllPolicy();
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        try {
            final AffyGenericChpFile chpFile = AffyGenericChpFile.parse(file, loadPolicy, istr, false);
            final AffyDataGroup group = chpFile.groups.get(0);
            final AffyDataSet dataSet = group.getDataSets().get(0);
            for (final String seq_name : dataSet.getChromosomeNames()) {
                this.getSeq(seq_group, seq_name);
            }
            for (final AffySingleChromData data : dataSet.getSingleChromData()) {
                final BioSeq seq = this.getSeq(seq_group, data.displayName);
                final List<SeqSymmetry> syms = data.makeGraphs(seq);
                if (annotateSeq) {
                    for (final SeqSymmetry sym : syms) {
                        seq.addAnnotation(sym);
                    }
                }
                results.addAll(syms);
            }
        }
        catch (Exception e) {
            if (!(e instanceof IOException)) {
                final IOException ioe = new IOException("IOException for file: " + stream_name);
                e.printStackTrace();
                ioe.initCause(e);
                throw ioe;
            }
        }
        return results;
    }
    
    private BioSeq getSeq(final AnnotatedSeqGroup seq_group, final String seqid) {
        BioSeq aseq = seq_group.getSeq(seqid);
        if (aseq == null) {
            aseq = seq_group.addSeq(seqid, 1);
        }
        return aseq;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(null, is, annotate_seq ? uri : nameType, group, annotate_seq);
    }
}
