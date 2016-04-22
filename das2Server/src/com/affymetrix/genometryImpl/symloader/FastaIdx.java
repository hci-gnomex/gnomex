// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import net.sf.picard.reference.ReferenceSequence;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Iterator;
import net.sf.samtools.SAMSequenceRecord;
import net.sf.picard.reference.ReferenceSequenceFile;
import net.sf.picard.reference.ReferenceSequenceFileFactory;
import java.io.FileNotFoundException;
import java.io.File;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import net.sf.samtools.SAMSequenceDictionary;
import net.sf.picard.reference.IndexedFastaSequenceFile;

public class FastaIdx extends FastaCommon
{
    final IndexedFastaSequenceFile fastaFile;
    final SAMSequenceDictionary sequenceDict;
    
    public FastaIdx(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, "", group);
        if (!uri.toString().startsWith("file:")) {
            this.fastaFile = null;
            this.sequenceDict = null;
            return;
        }
        IndexedFastaSequenceFile tempFile;
        try {
            tempFile = new IndexedFastaSequenceFile(new File(uri));
        }
        catch (FileNotFoundException x) {
            this.fastaFile = null;
            this.sequenceDict = null;
            return;
        }
        this.fastaFile = tempFile;
        String uriString = uri.toString();
        if (uriString.startsWith("file:")) {
            uriString = uri.getPath();
        }
        final ReferenceSequenceFile refSeq = ReferenceSequenceFileFactory.getReferenceSequenceFile(new File(uriString));
        this.sequenceDict = refSeq.getSequenceDictionary();
    }
    
    @Override
    protected boolean initChromosomes() throws Exception {
        for (final SAMSequenceRecord rec : this.sequenceDict.getSequences()) {
            final String seqid = rec.getSequenceName();
            BioSeq seq = this.group.getSeq(seqid);
            final int count = rec.getSequenceLength();
            if (seq == null) {
                seq = this.group.addSeq(seqid, count, this.uri.toString());
            }
            this.chrSet.add(seq);
        }
        return true;
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        final ReferenceSequence sequence = this.fastaFile.getSubsequenceAt(span.getBioSeq().getID(), (long)(span.getMin() + 1), (long)Math.min(span.getMax() + 1, span.getBioSeq().getLength()));
        return new String(sequence.getBases());
    }
    
    public boolean isValid() {
        return this.fastaFile != null && this.sequenceDict != null;
    }
}
