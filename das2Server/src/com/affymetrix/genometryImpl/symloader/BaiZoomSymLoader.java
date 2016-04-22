//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.samtools.StubBAMFileIndex;
import java.io.InputStream;
import net.sf.samtools.SAMFileReader;
import java.util.Map;
import java.util.Iterator;
import net.sf.samtools.seekablestream.SeekableFileStream;
import net.sf.samtools.seekablestream.SeekableStream;
import java.io.File;
import net.sf.samtools.seekablestream.SeekableHTTPStream;
import java.net.URL;
//import net.sf.samtools.util.SeekableStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.List;
import net.sf.samtools.SAMSequenceRecord;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import net.sf.samtools.SAMSequenceDictionary;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;

public class BaiZoomSymLoader extends IndexZoomSymLoader
{
    public BaiZoomSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
    }

    @Override
    protected SymLoader getDataFileSymLoader() throws Exception {
        return FileTypeHolder.getInstance().getFileTypeHandler("bam").createSymLoader(this.getBamURI(this.uri), this.featureName, this.group);
    }

    private int getRefNo(final String igbSeq, final SAMSequenceDictionary ssd) {
        final List<SAMSequenceRecord> sList = (List<SAMSequenceRecord>)ssd.getSequences();
        for (int i = 0; i < sList.size(); ++i) {
            final String bamSeq = SynonymLookup.getChromosomeLookup().getPreferredName(sList.get(i).getSequenceName());
            if (igbSeq.equals(bamSeq)) {
                return i;
            }
        }
        return -1;
    }

    private URI getBamURI(final URI baiUri) throws Exception {
        String bamUriString = baiUri.toString().substring(0, baiUri.toString().length() - ".bai".length());
        if (!bamUriString.startsWith("file:") && !bamUriString.startsWith("http:") && !bamUriString.startsWith("https:") && !bamUriString.startsWith("ftp:")) {
            bamUriString = GeneralUtils.getFileScheme() + bamUriString;
        }
        return new URI(bamUriString);
    }

    protected SeekableStream getSeekableStream(final URI uri) throws Exception {
        if (uri.toString().startsWith("http:") || uri.toString().startsWith("https:")) {
            return (SeekableStream)new SeekableHTTPStream(new URL(uri.toString()));
        }
        return (SeekableStream)new SeekableFileStream(new File(GeneralUtils.fixFileName(uri.toString())));
    }

    @Override
    protected Iterator<Map<Integer, List<List<Long>>>> getBinIter(final String seq) {
        try {
            final SeekableStream ssData = this.getSeekableStream(this.getBamURI(this.uri));
            final SeekableStream ssIndex = this.getSeekableStream(this.uri);
            final SAMFileReader sfr = new SAMFileReader((InputStream)ssData, false);
            final SAMSequenceDictionary ssd = sfr.getFileHeader().getSequenceDictionary();
            final int refno = this.getRefNo(seq.toString(), ssd);
            if (refno == -1) {
                return null;
            }
            final StubBAMFileIndex sbfi = new StubBAMFileIndex(ssIndex, this.uri, ssd);
            return (Iterator<Map<Integer, List<List<Long>>>>)sbfi.getBinIter(refno);
        }
        catch (Exception x) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot read BAI file " + this.uri, x);
            return null;
        }
    }
}
