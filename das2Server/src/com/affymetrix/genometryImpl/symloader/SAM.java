//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.io.IOException;
import net.sf.samtools.util.BufferedLineReader;
import net.sf.samtools.SAMTextReader;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.SeqSpan;
import net.sf.samtools.SAMException;
import java.util.ArrayList;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.util.CloseableIterator;
import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.logging.Logger;
import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFormatException;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import java.util.logging.Level;
import java.io.InputStream;
import net.sf.samtools.SAMFileReader;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;

public class SAM extends XAM implements LineProcessor
{
    public SAM(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
    }

    public void init() throws Exception {
        try {
            (this.reader = new SAMFileReader((InputStream)LocalUrlCacher.convertURIToBufferedStream(this.uri))).setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
            if (this.isInitialized) {
                return;
            }
            if (this.initTheSeqs()) {
                super.init();
            }
        }
        catch (SAMFormatException ex) {
            ErrorHandler.errorPanel("SAM exception", "A SAMFormatException has been thrown by the Picard tools.\nPlease validate your BAM files (see http://picard.sourceforge.net/command-line-overview.shtml#ValidateSamFile). See console for the details of the exception.\n", Level.SEVERE);
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean initTheSeqs() {
        final boolean ret = super.initTheSeqs();
        if (ret && this.header.getSortOrder() != SAMFileHeader.SortOrder.coordinate) {
            Logger.getLogger(SAM.class.getName()).log(Level.SEVERE, "Sam file must be sorted by coordinate.");
            return false;
        }
        return ret;
    }

    @Override
    public List<SeqSymmetry> parse(final BioSeq seq, final int min, final int max, final boolean containerSym, final boolean contained) throws Exception {
        this.init();
        if (this.reader != null) {
            final CloseableIterator<SAMRecord> iter = (CloseableIterator<SAMRecord>)this.reader.iterator();
            if (iter != null && iter.hasNext()) {
                return this.parse(iter, seq, min, max, containerSym, contained, true);
            }
        }
        return Collections.emptyList();
    }

    public List<SeqSymmetry> parse(final CloseableIterator<SAMRecord> iter, final BioSeq seq, final int min, final int max, final boolean containerSym, final boolean contained, final boolean check) throws Exception {
        final List<SeqSymmetry> symList = new ArrayList<SeqSymmetry>(1000);
        final List<Throwable> errList = new ArrayList<Throwable>(10);
        final String seqId = this.seqs.get(seq);
        SAMRecord sr = null;
        try {
            while (iter.hasNext() && !Thread.currentThread().isInterrupted()) {
                try {
                    sr = (SAMRecord)iter.next();
                    final int maximum = sr.getAlignmentEnd();
                    if (check) {
                        if (!seqId.equals(sr.getReferenceName())) {
                            continue;
                        }
                        if (!checkRange(sr.getAlignmentStart(), maximum, min, max)) {
                            if (maximum > max) {
                                break;
                            }
                            continue;
                        }
                    }
                    if (this.skipUnmapped && sr.getReadUnmappedFlag()) {
                        continue;
                    }
                    symList.add(XAM.convertSAMRecordToSymWithProps(sr, seq, this.uri.toString()));
                }
                catch (SAMException e) {
                    errList.add((Throwable)e);
                }
            }
            return symList;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            if (iter != null) {
                iter.close();
            }
            if (!errList.isEmpty()) {
                ErrorHandler.errorPanel("SAM exception", "Ignoring " + errList.size() + " records", errList, Level.WARNING);
            }
        }
    }

    private static boolean checkRange(final int start, final int end, final int min, final int max) {
        return end >= min && start <= max;
    }

    @Override
    public List<SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        this.init();
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("SAM SymLoaderProgressUpdater getRegion for " + this.uri + " - " + span, span);
        if (CThreadHolder.getInstance().getCurrentCThreadWorker() != null) {
            CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        }
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax(), true, false);
    }

    @Override
    public List<? extends SeqSymmetry> processLines(final BioSeq seq, final LineReader lineReader, final LineTrackerI lineTracker) throws Exception {
        final SeqSpan span = new SimpleSeqSpan(seq.getMin(), seq.getMax(), seq);
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("SAM SymLoaderProgressUpdater getRegion for " + this.uri + " - " + span, span);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        final SAMTextReader str = new SAMTextReader((BufferedLineReader)new AsciiTabixLineReader(lineReader), this.header, SAMFileReader.ValidationStringency.SILENT);
        return this.parse((CloseableIterator<SAMRecord>)str.queryUnmapped(), seq, seq.getMin(), seq.getMax(), true, false, false);
    }

    @Override
    public void init(final URI uri) {
        (this.reader = new SAMFileReader((InputStream)LocalUrlCacher.convertURIToBufferedStream(uri))).setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        this.header = this.reader.getFileHeader();
    }

    @Override
    public SeqSpan getSpan(final String line) {
        return null;
    }

    @Override
    public boolean processInfoLine(final String line, final List<String> infoLines) {
        return false;
    }

    private class AsciiTabixLineReader extends BufferedLineReader
    {
        private final LineReader readerImpl;
        private int lineNumber;

        AsciiTabixLineReader(final LineReader readerImpl) {
            super((InputStream)null);
            this.readerImpl = readerImpl;
            this.lineNumber = 0;
        }

        public String readLine() {
            try {
                return this.readerImpl.readLine();
            }
            catch (IOException ex) {
                Logger.getLogger(SAM.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                ++this.lineNumber;
            }
            return null;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public void close() {
            this.readerImpl.close();
        }

        public int peek() {
            return -1;
        }
    }
}
