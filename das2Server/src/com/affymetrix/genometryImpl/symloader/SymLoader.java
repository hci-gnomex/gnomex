//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.thread.PositionCalculator;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import java.text.MessageFormat;
import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.genometryImpl.util.GraphSymUtils;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.Collection;
import com.affymetrix.genometryImpl.filter.SymmetryFilterIntersecting;
import com.affymetrix.genometryImpl.general.GenericFeature;
import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.util.ParserController;
import java.util.logging.Level;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collections;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SortTabFile;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.logging.Logger;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.util.HashMap;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.File;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.net.URI;

public abstract class SymLoader implements LineTrackerI
{
    public static final int SLEEP_INTERVAL_TIME = 30;
    public static final long SLEEP_INTERVAL_TIME_NANO = 30000000000L;
    public static final int SLEEP_TIME = 1;
    protected long lastSleepTime;
    public static final String FILE_PREFIX = "file:";
    public static final int UNKNOWN_CHROMOSOME_LENGTH = 1;
    public String extension;
    public final URI uri;
    protected boolean isResidueLoader;
    protected volatile boolean isInitialized;
    protected final Map<BioSeq, File> chrList;
    protected final Map<BioSeq, Boolean> chrSort;
    protected final AnnotatedSeqGroup group;
    public final String featureName;
    protected SymLoaderProgressUpdater symLoaderProgressUpdater;
    protected ParseLinesProgressUpdater parseLinesProgressUpdater;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public SymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        this.isResidueLoader = false;
        this.isInitialized = false;
        this.chrList = new HashMap<BioSeq, File>();
        this.chrSort = new HashMap<BioSeq, Boolean>();
        this.uri = uri;
        this.featureName = featureName;
        this.group = group;
        this.extension = getExtension(uri);
    }

    protected void init() throws Exception {
        this.isInitialized = true;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }

    public String getFeatureName() {
        return this.featureName;
    }

    protected boolean buildIndex() throws Exception {
        BufferedInputStream bis = null;
        final Map<String, Integer> chrLength = new HashMap<String, Integer>();
        final Map<String, File> chrFiles = new HashMap<String, File>();
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            if (this.parseLines(bis, chrLength, chrFiles)) {
                this.createResults(chrLength, chrFiles);
                Logger.getLogger(SymLoader.class.getName()).fine("Indexing successful");
                return true;
            }
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
        return false;
    }

    protected void sortCreatedFiles() throws Exception {
        for (final Map.Entry<BioSeq, File> file : this.chrList.entrySet()) {
            this.chrSort.put(file.getKey(), SortTabFile.sort(file.getValue()));
        }
    }

    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return SymLoader.strategyList;
    }

    public List<BioSeq> getChromosomeList() throws Exception {
        return Collections.emptyList();
    }

    public List<? extends SeqSymmetry> getGenome() throws Exception {
        BufferedInputStream bis = null;
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            return this.parse(bis, false);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
    }

    public List<? extends SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Retrieving chromosome is not optimized");
        final List<? extends SeqSymmetry> genomeResults = this.getGenome();
        if (seq == null || genomeResults == null) {
            return genomeResults;
        }
        return filterResultsByChromosome(genomeResults, seq);
    }

    public List<String> getFormatPrefList() {
        return Collections.emptyList();
    }

    public String getExtension() {
        return this.extension;
    }

    public static String getExtension(final URI uri) {
        if (uri == null) {
            return null;
        }
        return getExtension(uri.toASCIIString().toLowerCase());
    }

    public static String getExtension(final String uriString) {
        final String unzippedStreamName = GeneralUtils.stripEndings(uriString);
        String extension = ParserController.getExtension(unzippedStreamName);
        extension = extension.substring(extension.indexOf(46) + 1);
        return extension;
    }

    public static List<SeqSymmetry> filterResultsByChromosome(final List<? extends SeqSymmetry> genomeResults, final BioSeq seq) {
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        for (final SeqSymmetry sym : genomeResults) {
            BioSeq seq2 = null;
            if (sym instanceof UcscPslSym) {
                seq2 = ((UcscPslSym)sym).getTargetSeq();
            }
            else {
                seq2 = sym.getSpanSeq(0);
            }
            if (seq.equals(seq2)) {
                results.add(sym);
            }
        }
        return results;
    }

    protected void notifyAddSymmetry(final SeqSymmetry sym) {
        if (this.symLoaderProgressUpdater != null) {
            this.symLoaderProgressUpdater.setLastSeqSymmetry(sym);
        }
    }

    @Override
    public void notifyReadLine(final int lineLength) {
        if (this.parseLinesProgressUpdater != null) {
            this.parseLinesProgressUpdater.lineRead(lineLength + 1);
        }
    }

    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("SymLoaderProgressUpdater getRegion for " + this.uri + " - " + overlapSpan, overlapSpan);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Retrieving region is not supported.  Returning entire chromosome.");
        return this.getChromosome(overlapSpan.getBioSeq());
    }

    public boolean isResidueLoader() {
        return this.isResidueLoader;
    }

    public String getRegionResidues(final SeqSpan span) throws Exception {
        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Not supported.  Returning empty string.");
        return "";
    }

    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "parseLines is not defined");
        return false;
    }

    protected void addToLists(final Map<String, BufferedWriter> chrs, final String current_seq_id, final Map<String, File> chrFiles, final Map<String, Integer> chrLength, String format) throws IOException {
        String fileName = current_seq_id;
        if (fileName.length() < 3) {
            fileName += "___";
        }
        format = (format.startsWith(".") ? format : ("." + format));
        final File tempFile = File.createTempFile(fileName, format);
        tempFile.deleteOnExit();
        chrs.put(current_seq_id, new BufferedWriter(new FileWriter(tempFile, true)));
        chrFiles.put(current_seq_id, tempFile);
        chrLength.put(current_seq_id, 0);
    }

    protected void createResults(final Map<String, Integer> chrLength, final Map<String, File> chrFiles) {
        for (final Map.Entry<String, Integer> bioseq : chrLength.entrySet()) {
            final String key = bioseq.getKey();
            this.chrList.put(this.group.addSeq(key, bioseq.getValue(), this.uri.toString()), chrFiles.get(key));
        }
    }

    public static Map<String, List<SeqSymmetry>> splitResultsByTracks(final List<? extends SeqSymmetry> results) {
        final Map<String, List<SeqSymmetry>> track2Results = new HashMap<String, List<SeqSymmetry>>();
        List<SeqSymmetry> resultList = null;
        String method = null;
        for (final SeqSymmetry result : results) {
            method = BioSeq.determineMethod(result);
            if (track2Results.containsKey(method)) {
                resultList = track2Results.get(method);
            }
            else {
                resultList = new ArrayList<SeqSymmetry>();
                track2Results.put(method, resultList);
            }
            resultList.add(result);
        }
        return track2Results;
    }

    public static Map<BioSeq, List<SeqSymmetry>> splitResultsBySeqs(final List<? extends SeqSymmetry> results) {
        final Map<BioSeq, List<SeqSymmetry>> seq2Results = new HashMap<BioSeq, List<SeqSymmetry>>();
        List<SeqSymmetry> resultList = null;
        BioSeq seq = null;
        for (final SeqSymmetry result : results) {
            for (int i = 0; i < result.getSpanCount(); ++i) {
                seq = result.getSpan(i).getBioSeq();
                if (seq2Results.containsKey(seq)) {
                    resultList = seq2Results.get(seq);
                }
                else {
                    resultList = new ArrayList<SeqSymmetry>();
                    seq2Results.put(seq, resultList);
                }
                resultList.add(result);
            }
        }
        return seq2Results;
    }

    public static List<? extends SeqSymmetry> splitFilterAndAddAnnotation(final SeqSpan span, final List<? extends SeqSymmetry> results, final GenericFeature feature) {
        final Map<String, List<SeqSymmetry>> entries = splitResultsByTracks(results);
        final List<SeqSymmetry> added = new ArrayList<SeqSymmetry>();
        final SymmetryFilterIntersecting filter = new SymmetryFilterIntersecting();
        filter.setParam(feature.getRequestSym());
        for (final Map.Entry<String, List<SeqSymmetry>> entry : entries.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            final List<? extends SeqSymmetry> filteredFeats = filterOutExistingSymmetries(span.getBioSeq(), entry.getValue(), filter);
            if (filteredFeats.isEmpty()) {
                continue;
            }
            added.addAll(filteredFeats);
            addAnnotations(filteredFeats, span, feature.getURI(), feature);
            if (entry.getKey() == null) {
                continue;
            }
            feature.addMethod(entry.getKey());
        }
        return added;
    }

    public static void addAnnotations(final List<? extends SeqSymmetry> filteredFeats, final SeqSpan span, final URI uri, final GenericFeature feature) {
        if (filteredFeats.size() > 0 && filteredFeats.get(0) instanceof GraphSym) {
            final GraphSym graphSym = (GraphSym)filteredFeats.get(0);
            if (filteredFeats.size() == 1 && graphSym.isSpecialGraph()) {
                final BioSeq seq = graphSym.getGraphSeq();
                seq.addAnnotation(graphSym);
            }
            else {
                for (final SeqSymmetry feat : filteredFeats) {
                    if (feat instanceof GraphSym) {
                        GraphSymUtils.addChildGraph((GraphSym)feat, ((GraphSym)feat).getID(), ((GraphSym)feat).getGraphName(), uri.toString(), span);
                    }
                }
            }
            return;
        }
        final BioSeq seq2 = span.getBioSeq();
        for (final SeqSymmetry feat : filteredFeats) {
            seq2.addAnnotation(feat, feature.getExtension());
        }
    }

    private static List<? extends SeqSymmetry> filterOutExistingSymmetries(final BioSeq seq, final List<? extends SeqSymmetry> syms, final SymmetryFilterIntersecting filter) {
        final List<SeqSymmetry> filteredFeats = new ArrayList<SeqSymmetry>(syms.size());
        for (final SeqSymmetry sym : syms) {
            if (filter.filterSymmetry(seq, sym)) {
                filteredFeats.add(sym);
            }
        }
        return filteredFeats;
    }

    public static List<BioSeq> getChromosomes(final URI uri, final String featureName, final String groupID) throws Exception {
        AnnotatedSeqGroup temp_group = new AnnotatedSeqGroup(groupID);
        SymLoader temp = new SymLoader(uri, featureName, temp_group) {};
        List<? extends SeqSymmetry> syms = temp.getGenome();
        final List<BioSeq> seqs = new ArrayList<BioSeq>();
        seqs.addAll(temp_group.getSeqList());
        syms.clear();
        syms = null;
        temp = null;
        temp_group = null;
        return seqs;
    }

    public List<? extends SeqSymmetry> parse(final InputStream is, final boolean annotate_seq) throws Exception {
        final FileTypeHandler fileTypeHandler = FileTypeHolder.getInstance().getFileTypeHandler(this.extension);
        if (fileTypeHandler == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, MessageFormat.format(GenometryConstants.BUNDLE.getString("noHandler"), this.extension));
            return null;
        }
        return fileTypeHandler.getParser().parse(new BufferedInputStream(is), this.group, this.featureName, this.uri.toString(), false);
    }

    protected void checkSleep() throws InterruptedException {
        final long currentTime = System.nanoTime();
        if (currentTime - this.lastSleepTime >= 30000000000L) {
            Thread.sleep(1L);
            this.lastSleepTime = currentTime;
        }
    }

    public void clear() {
    }

    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        SymLoader.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        SymLoader.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }

    public class SymLoaderProgressUpdater extends ProgressUpdater
    {
        private SeqSymmetry lastSeqSymmetry;

        public SymLoaderProgressUpdater(final String name, final SeqSpan span) {
            super(name, span.getMin(), span.getMax(), new PositionCalculator() {
                @Override
                public long getCurrentPosition() {
                    if (SymLoader.this.symLoaderProgressUpdater == null) {
                        return span.getMin();
                    }
                    final SeqSpan testSpan = SymLoader.this.symLoaderProgressUpdater.getLastSeqSymmetry().getSpan(span.getBioSeq());
                    return testSpan.getMin();
                }
            });
        }

        public SeqSymmetry getLastSeqSymmetry() {
            return this.lastSeqSymmetry;
        }

        public void setLastSeqSymmetry(final SeqSymmetry lastSeqSymmetry) {
            this.lastSeqSymmetry = lastSeqSymmetry;
        }
    }

    protected class ParseLinesProgressUpdater extends ProgressUpdater
    {
        private long filePosition;

        public ParseLinesProgressUpdater(final SymLoader symLoader, final String name) {
            this(symLoader, name, symLoader.uri);
        }

        public ParseLinesProgressUpdater(final SymLoader symLoader, final String name, final URI uri) {
            this(name, 0L, GeneralUtils.getUriLength(uri));
        }

        public ParseLinesProgressUpdater(final String name, final long startPosition, final long endPosition) {
            super(name, startPosition, endPosition, new PositionCalculator() {
                @Override
                public long getCurrentPosition() {
                    if (SymLoader.this.parseLinesProgressUpdater == null) {
                        return 0L;
                    }
                    return SymLoader.this.parseLinesProgressUpdater.getFilePosition();
                }
            });
        }

        public long getFilePosition() {
            return this.filePosition;
        }

        public void lineRead(final int lineLength) {
            this.filePosition += lineLength + 1;
        }
    }
}
