//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.NoSuchElementException;
import com.affymetrix.genometryImpl.SeqSpan;
import java.io.InputStream;
import net.sf.picard.sam.BuildBamIndex;
import net.sf.samtools.SAMFileWriter;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.OutputStream;
import java.io.IOException;
import net.sf.samtools.SAMFileWriterFactory;
import java.io.DataOutputStream;
import java.util.Iterator;
import net.sf.samtools.util.CloseableIterator;
import net.sf.samtools.SAMException;
import net.sf.samtools.SAMRecord;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.PositionCalculator;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.lang.reflect.Field;
import com.affymetrix.genometryImpl.util.BlockCompressedStreamPosition;
import net.sf.samtools.SAMFormatException;
//import com.affymetrix.genometryImpl.util.SeekableFTPStream;
import java.util.logging.Logger;

//import net.sf.samtools.util.SeekableStream;
//import net.sf.samtools.util.SeekableBufferedStream;
//import net.sf.samtools.util.SeekableHTTPStream;

import net.sf.samtools.seekablestream.SeekableStream;
import net.sf.samtools.seekablestream.SeekableBufferedStream;
import net.sf.samtools.seekablestream.SeekableHTTPStream;
import net.sf.samtools.seekablestream.SeekableFTPStream;

import java.net.URL;
import java.util.logging.Level;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.io.File;
import net.sf.samtools.SAMFileReader;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import net.sf.samtools.SAMFileHeader;
import java.util.regex.Pattern;
import java.util.List;

public final class BAM extends XAM
{
    public static final List<String> pref_list;
    private static final Pattern CLEAN;
    protected SAMFileHeader header;

    public BAM(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
        this.strategyList.add(LoadUtils.LoadStrategy.AUTOLOAD);
    }

    private SAMFileReader getSAMFileReader() throws Exception {
        File indexFile = null;
        final String scheme = this.uri.getScheme().toLowerCase();
        SAMFileReader samFileReader;
        if (scheme.length() == 0 || scheme.equals("file")) {
            final File f = new File(this.uri);
            indexFile = findIndexFile(f);
            samFileReader = new SAMFileReader(f, indexFile, false);
            samFileReader.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        }
        else if (scheme.startsWith("http")) {
            final String reachable_url = LocalUrlCacher.getReachableUrl(this.uri.toASCIIString());
            if (reachable_url == null) {
                ErrorHandler.errorPanel("Url cannot be reached");
                this.isInitialized = false;
                return null;
            }
            final String baiUriStr = findIndexFile(this.uri.toString());
            if (baiUriStr == null) {
                ErrorHandler.errorPanel("No BAM index file", "Could not find URL of BAM index at " + this.uri.toString() + ". Please be sure this is in the same directory as the BAM file.", Level.SEVERE);
                this.isInitialized = false;
                return null;
            }
            indexFile = LocalUrlCacher.convertURIToFile(URI.create(baiUriStr));
//            samFileReader = new SAMFileReader((SeekableStream)new SeekableBufferedStream((SeekableStream)new SeekableHTTPStream(new URL(reachable_url))), indexFile, false);

/*
  public More ...SAMFileReader(final SeekableStream strm, final File indexFile, final boolean eagerDecode) {
195        init(strm, indexFile, eagerDecode, defaultValidationStringency);
196    }
197
198    public More ...SAMFileReader(final SeekableStream strm, final SeekableStream indexStream, final boolean eagerDecode) {
199        init(strm, indexStream, eagerDecode, defaultValidationStringency);
200    }
201
*/


            samFileReader = new SAMFileReader((net.sf.samtools.seekablestream.SeekableStream) new SeekableBufferedStream(new SeekableHTTPStream(new URL(reachable_url))), indexFile, false);

            samFileReader.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        }
        else {
            if (!scheme.startsWith("ftp")) {
                Logger.getLogger(BAM.class.getName()).log(Level.SEVERE, "URL scheme: {0} not recognized", scheme);
                return null;
            }
            final String baiUriStr2 = findIndexFile(this.uri.toString());
            if (baiUriStr2 == null) {
                ErrorHandler.errorPanel("No BAM index file", "Could not find URL of BAM index at " + this.uri.toString() + ". Please be sure this is in the same directory as the BAM file.", Level.SEVERE);
                this.isInitialized = false;
                return null;
            }
            indexFile = LocalUrlCacher.convertURIToFile(URI.create(baiUriStr2));
            samFileReader = new SAMFileReader((net.sf.samtools.seekablestream.SeekableStream)new SeekableBufferedStream(new SeekableFTPStream(this.uri.toURL())), indexFile, false);
            samFileReader.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        }
        return samFileReader;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        try {
            this.reader = this.getSAMFileReader();
            this.header = this.reader.getFileHeader();
            if (this.initTheSeqs()) {
                super.init();
            }
        }
        catch (SAMFormatException ex) {
            ErrorHandler.errorPanel("SAM exception", "A SAMFormatException has been thrown by the Picard tools.\nPlease validate your BAM files (see http://picard.sourceforge.net/command-line-overview.shtml#ValidateSamFile). See console for the details of the exception.\n", Level.SEVERE);
            ex.printStackTrace();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    @Override
    public List<String> getFormatPrefList() {
        return BAM.pref_list;
    }

    private BlockCompressedStreamPosition getCompressedInputStreamPosition(final SAMFileReader sfr) throws Exception {
        final Field privateReaderField = sfr.getClass().getDeclaredField("mReader");
        privateReaderField.setAccessible(true);
        final Object mReaderValue = privateReaderField.get(sfr);
        final Field privateCompressedInputStreamField = mReaderValue.getClass().getDeclaredField("mCompressedInputStream");
        privateCompressedInputStreamField.setAccessible(true);
        final Object compressedInputStreamValue = privateCompressedInputStreamField.get(mReaderValue);
        final Field privateBlockAddressField = compressedInputStreamValue.getClass().getDeclaredField("mBlockAddress");
        privateBlockAddressField.setAccessible(true);
        final long blockAddressValue = (Long)privateBlockAddressField.get(compressedInputStreamValue);
        final Field privateCurrentOffsetField = compressedInputStreamValue.getClass().getDeclaredField("mCurrentOffset");
        privateCurrentOffsetField.setAccessible(true);
        final int currentOffsetValue = (Integer)privateCurrentOffsetField.get(compressedInputStreamValue);
        return new BlockCompressedStreamPosition(blockAddressValue, currentOffsetValue);
    }

    @Override
    public synchronized List<SeqSymmetry> parse(final BioSeq seq, final int min, final int max, final boolean containerSym, final boolean contained) throws Exception {
        this.init();
        final List<SeqSymmetry> symList = new ArrayList<SeqSymmetry>(1000);
        final List<Throwable> errList = new ArrayList<Throwable>(10);
        CloseableIterator<SAMRecord> iter = null;
        try {
            if (this.reader != null) {
                iter = (CloseableIterator<SAMRecord>)this.reader.query((String)this.seqs.get(seq), max - 1, max, contained);
                while (iter.hasNext()) {
                    iter.next();
                }
                final long endPosition = this.getCompressedInputStreamPosition(this.reader).getApproximatePosition();
                iter.close();
                iter = (CloseableIterator<SAMRecord>)this.reader.query((String)this.seqs.get(seq), min, max, contained);
                final long startPosition = this.getCompressedInputStreamPosition(this.reader).getApproximatePosition();
                final ProgressUpdater progressUpdater = new ProgressUpdater("BAM parse " + this.uri + " - " + seq + ":" + min + "-" + max, startPosition, endPosition, new PositionCalculator() {
                    @Override
                    public long getCurrentPosition() {
                        long currentPosition = startPosition;
                        try {
                            currentPosition = BAM.this.getCompressedInputStreamPosition(BAM.this.reader).getApproximatePosition();
                        }
                        catch (Exception ex) {}
                        return currentPosition;
                    }
                });
                if (CThreadHolder.getInstance().getCurrentCThreadWorker() != null) {
                    CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(progressUpdater);
                }
                if (iter != null && iter.hasNext()) {
                    SAMRecord sr = null;
                    this.lastSleepTime = System.nanoTime();
                    while (iter.hasNext() && !Thread.currentThread().isInterrupted()) {
                        try {
                            sr = (SAMRecord)iter.next();
                            this.checkSleep();
                            if (this.skipUnmapped && sr.getReadUnmappedFlag()) {
                                continue;
                            }
                            symList.add(XAM.convertSAMRecordToSymWithProps(sr, seq, this.uri.toString()));
                        }
                        catch (InterruptedException e2) {}
                        catch (SAMException e) {
                            errList.add((Throwable)e);
                        }
                    }
                }
            }
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
        return symList;
    }

    public List<SeqSymmetry> parseAll(final BioSeq seq, final String method) {
        (this.reader = new SAMFileReader(new File(this.uri))).setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        final List<SeqSymmetry> symList = new ArrayList<SeqSymmetry>(1000);
        if (this.reader != null) {
            for (final SAMRecord sr : this.reader) {
                if (this.skipUnmapped && sr.getReadUnmappedFlag()) {
                    continue;
                }
                symList.add(XAM.convertSAMRecordToSymWithProps(sr, seq, method));
            }
        }
        return symList;
    }

    public SeqSymmetryIterator getIterator(final BioSeq seq, final int min, final int max, final boolean contained) throws Exception {
        this.init();
        if (this.reader != null) {
            CloseableIterator<SAMRecord> iter = (CloseableIterator<SAMRecord>)this.reader.query((String)this.seqs.get(seq), max - 1, max, contained);
            while (iter.hasNext()) {
                iter.next();
            }
            iter.close();
            final long endPosition = this.getCompressedInputStreamPosition(this.reader).getApproximatePosition();
            iter = (CloseableIterator<SAMRecord>)this.reader.query((String)this.seqs.get(seq), min, max, contained);
            final long startPosition = this.getCompressedInputStreamPosition(this.reader).getApproximatePosition();
            return new SeqSymmetryIterator(seq, iter, startPosition, endPosition);
        }
        return null;
    }

    public void writeAnnotations(final BioSeq seq, final int min, final int max, final DataOutputStream dos, final boolean BAMWriter) throws Exception {
        this.init();
        if (this.reader == null) {
            return;
        }
        CloseableIterator<SAMRecord> iter = null;
        SAMFileWriter sfw = null;
        File tempBAMFile = null;
        try {
            iter = (CloseableIterator<SAMRecord>)this.reader.query(seq.getID(), min, max, false);
            if (!iter.hasNext()) {
                Logger.getLogger(BAM.class.getName()).log(Level.INFO, "No overlapping bam alignments.", "Min-Max: " + min + "-" + max);
                return;
            }
            this.reader.getFileHeader().setSortOrder(SAMFileHeader.SortOrder.coordinate);
            if (iter != null) {
                final SAMFileWriterFactory sfwf = new SAMFileWriterFactory();
                if (BAMWriter) {
                    try {
                        tempBAMFile = File.createTempFile(BAM.CLEAN.matcher(this.featureName).replaceAll("_"), ".bam");
                        tempBAMFile.deleteOnExit();
                    }
                    catch (IOException ex) {
                        Logger.getLogger(BAM.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("Cannot create temporary BAM file! \n" + ex.getStackTrace());
                        return;
                    }
                    sfw = sfwf.makeBAMWriter(this.header, true, tempBAMFile);
                }
                else {
                    sfw = sfwf.makeSAMWriter(this.header, true, (OutputStream)dos);
                }
                SAMRecord sr = (SAMRecord)iter.next();
                while (iter.hasNext() && !Thread.currentThread().isInterrupted()) {
                    sfw.addAlignment(sr);
                    sr = (SAMRecord)iter.next();
                }
            }
        }
        catch (Exception ex2) {
            Logger.getLogger(BAM.class.getName()).log(Level.SEVERE, "SAM exception A SAMFormatException has been thrown by the Picard tools.\nPlease validate your BAM files and contact the Picard project at http://picard.sourceforge.net.See console and the tomcat catalina.out for the details of the exception.\n", ex2);
        }
        finally {
            if (iter != null) {
                try {
                    iter.close();
                }
                catch (Exception ex3) {
                    ex3.printStackTrace();
                }
            }
            if (sfw != null) {
                try {
                    sfw.close();
                }
                catch (Exception ex3) {
                    ex3.printStackTrace();
                }
            }
            if (tempBAMFile != null && tempBAMFile.exists()) {
                GeneralUtils.writeFileToStream(tempBAMFile, dos);
                if (!tempBAMFile.delete()) {
                    Logger.getLogger(BAM.class.getName()).log(Level.WARNING, "Couldn''t delete file {0}", tempBAMFile.getName());
                }
            }
        }
    }

    public static File findIndexFile(final File bamfile) throws IOException {
        String path = bamfile.getPath();
        File f = new File(path + ".bai");
        if (f.exists()) {
            return f;
        }
        path = path.substring(0, path.length() - 3) + "bai";
        f = new File(path);
        if (f.exists()) {
            return f;
        }
        return null;
    }

    public static String findIndexFile(final String bamfile) {
        String baiUriStr = bamfile + ".bai";
        if (LocalUrlCacher.isValidURL(baiUriStr)) {
            return baiUriStr;
        }
        baiUriStr = bamfile.substring(0, bamfile.length() - 3) + "bai";
        if (LocalUrlCacher.isValidURL(baiUriStr)) {
            return baiUriStr;
        }
        return null;
    }

    public static boolean hasIndex(final URI uri) throws IOException {
        final String scheme = uri.getScheme().toLowerCase();
        if (scheme.length() == 0 || scheme.equals("file")) {
            final File f = findIndexFile(new File(uri));
            return f != null;
        }
        if (scheme.startsWith("http") || scheme.startsWith("ftp")) {
            final String uriStr = findIndexFile(uri.toString());
            return uriStr != null;
        }
        return false;
    }

    private static File createIndexFile(final File bamfile) throws IOException {
        final File indexfile = File.createTempFile(bamfile.getName(), ".bai");
        if (!indexfile.exists()) {
            ErrorHandler.errorPanel("Unable to create file.");
            return null;
        }
        final String input = "INPUT=" + bamfile.getAbsolutePath();
        final String output = "OUTPUT=" + indexfile.getAbsolutePath();
        final String quiet = "QUIET=true";
        final BuildBamIndex buildIndex = new BuildBamIndex();
        buildIndex.instanceMain(new String[] { input, output, quiet });
        return indexfile;
    }

    public String getMimeType() {
        return "binary/BAM";
    }

    public static List<? extends SeqSymmetry> parse(final URI uri, final InputStream istr, final AnnotatedSeqGroup group, final String featureName, final SeqSpan overlap_span) throws Exception {
        final File bamfile = GeneralUtils.convertStreamToFile(istr, featureName);
        bamfile.deleteOnExit();
        final BAM bam = new BAM(bamfile.toURI(), featureName, group);
        if (uri.getScheme().equals("http")) {
            return bam.parseAll(overlap_span.getBioSeq(), uri.toString());
        }
        return bam.getRegion(overlap_span);
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final boolean annotate_seq) throws Exception {
        throw new IllegalStateException();
    }

    static {
        (pref_list = new ArrayList<String>()).add("bam");
        CLEAN = Pattern.compile("[/\\s+]");
    }

    public class SeqSymmetryIterator implements CloseableIterator<SeqSymmetry>
    {
        final BioSeq seq;
        final CloseableIterator<SAMRecord> iter;
        private SeqSymmetry next;
        final ProgressUpdater progressUpdater;

        SeqSymmetryIterator(final BioSeq seq, final CloseableIterator<SAMRecord> iter, final long startPosition, final long endPosition) {
            this.next = null;
            this.seq = seq;
            this.iter = iter;
            this.progressUpdater = this.getProgressUpdater(startPosition, endPosition);
            this.next = this.getNext();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final SeqSymmetry next() {
            final SeqSymmetry sym = this.next;
            this.next = this.getNext();
            return sym;
        }

        public final void remove() {
            this.iter.remove();
        }

        public final void close() {
            this.iter.close();
        }

        private SeqSymmetry getNext() {
            while (this.iter.hasNext()) {
                try {
                    final SAMRecord sr = (SAMRecord)this.iter.next();
                    if (BAM.this.skipUnmapped && sr.getReadUnmappedFlag()) {
                        continue;
                    }
                    return XAM.convertSAMRecordToSymWithProps(sr, this.seq, BAM.this.uri.toString(), false, true);
                }
                catch (SAMException ex) {
                    System.err.print("!!! SAM Record Error:" + ex.getMessage());
                    continue;
                }
                catch (NoSuchElementException ex3) {
                    continue;
                }
                catch (Exception ex2) {
                    System.err.print("!!! Error:" + ex2.getMessage());
                    continue;
                }
//                break;
            }
            return null;
        }

        public double getProgress() {
            return this.progressUpdater.getProgress();
        }

        private ProgressUpdater getProgressUpdater(final long startPosition, final long endPosition) {
            return new ProgressUpdater("BAM parse " + BAM.this.uri + " - " + this.seq, startPosition, endPosition, new PositionCalculator() {
                @Override
                public long getCurrentPosition() {
                    long currentPosition = startPosition;
                    try {
                        currentPosition = BAM.this.getCompressedInputStreamPosition(BAM.this.reader).getApproximatePosition();
                    }
                    catch (Exception ex) {}
                    return currentPosition;
                }
            });
        }
    }
}
