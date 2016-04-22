//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import org.broad.tribble.readers.LineReader;
import java.io.BufferedReader;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.BioSeqComparator;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;

public abstract class UnindexedSymLoader extends SymLoader
{
    private static final List<LoadUtils.LoadStrategy> strategyList;
    private LineProcessor lineProcessor;

    public UnindexedSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.lineProcessor = this.createLineProcessor(featureName);
    }

    protected abstract LineProcessor createLineProcessor(final String p0);

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return UnindexedSymLoader.strategyList;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        this.lineProcessor.init(this.uri);
        if (this.buildIndex()) {
            super.init();
        }
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        final List<BioSeq> chromosomeList = new ArrayList<BioSeq>(this.chrList.keySet());
        Collections.sort(chromosomeList, new BioSeqComparator());
        return chromosomeList;
    }

    @Override
    public List<? extends SeqSymmetry> getGenome() throws Exception {
        this.init();
        final List<BioSeq> allSeq = this.getChromosomeList();
        final List<SeqSymmetry> retList = new ArrayList<SeqSymmetry>();
        for (final BioSeq seq : allSeq) {
            retList.addAll(this.getChromosome(seq));
        }
        return retList;
    }

    @Override
    public List<? extends SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.parse(seq, seq.getMin(), seq.getMax());
    }

    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        this.init();
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax());
    }

    protected LineReader getLineReader(final BufferedReader br, final int min, final int max) {
        return (LineReader)new LineReader() {
            public String readLine() throws IOException {
                String line = br.readLine();
                if (line == null || UnindexedSymLoader.this.lineProcessor.getSpan(line).getMin() >= max) {
                    return null;
                }
                while (line != null && UnindexedSymLoader.this.lineProcessor.getSpan(line).getMax() <= min) {
                    line = br.readLine();
                }
                return line;
            }

            public void close() {
            }
        };
    }

    private List<? extends SeqSymmetry> parse(final BioSeq seq, final int min, final int max) throws Exception {
        InputStream istr = null;
        try {
            final File file = this.chrList.get(seq);
            if (file == null) {
                Logger.getLogger(UnindexedSymLoader.class.getName()).log(Level.FINE, "Could not find chromosome {0}", seq.getID());
                return Collections.emptyList();
            }
            istr = new FileInputStream(file);
            final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
            final LineReader lineReader = this.getLineReader(br, min, max);
            this.parseLinesProgressUpdater = new ParseLinesProgressUpdater("Unindexed process lines " + this.uri, 0L, file.length());
            CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
            return this.lineProcessor.processLines(seq, lineReader, this);
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }

    public Comparator<SeqSymmetry> getComparator(final BioSeq seq) {
        return new SeqSymMinComparator(seq);
    }

    public int getMin(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMin();
    }

    public int getMax(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMax();
    }

    public LineProcessor getLineProcessor() {
        return this.lineProcessor;
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater(this, "Unindexed parse lines " + this.uri);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        BufferedReader br = null;
        BufferedWriter bw = null;
        final Map<String, Boolean> chrTrack = new HashMap<String, Boolean>();
        final Map<String, BufferedWriter> chrs = new HashMap<String, BufferedWriter>();
        final List<String> infoLines = new ArrayList<String>();
        String seq_name = null;
        String trackLine = null;
        try {
            final Thread thread = Thread.currentThread();
            br = new BufferedReader(new InputStreamReader(istr));
            this.lastSleepTime = System.nanoTime();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                this.checkSleep();
                this.notifyReadLine(line.length());
                final char ch = line.charAt(0);
                if (ch == 't' && line.startsWith("track")) {
                    trackLine = line;
                    chrTrack.clear();
                }
                else {
                    if (this.lineProcessor.processInfoLine(line, infoLines)) {
                        continue;
                    }
                    final SeqSpan span = this.lineProcessor.getSpan(line);
                    seq_name = span.getBioSeq().getID();
                    final int end = span.getMax();
                    bw = chrs.get(seq_name);
                    if (bw == null) {
                        this.addToLists(chrs, seq_name, chrFiles, chrLength, "." + this.getExtension());
                        bw = chrs.get(seq_name);
                        for (final String infoLine : infoLines) {
                            bw.write(infoLine + "\n");
                        }
                    }
                    if (!chrTrack.containsKey(seq_name)) {
                        chrTrack.put(seq_name, true);
                        if (trackLine != null) {
                            bw.write(trackLine + "\n");
                        }
                    }
                    bw.write(line + "\n");
                    if (end <= chrLength.get(seq_name)) {
                        continue;
                    }
                    chrLength.put(seq_name, end);
                }
            }
            return !thread.isInterrupted();
        }
        finally {
            for (final BufferedWriter b : chrs.values()) {
                GeneralUtils.safeClose(b);
            }
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bw);
        }
    }

    @Override
    protected void createResults(final Map<String, Integer> chrLength, final Map<String, File> chrFiles) {
        for (final Map.Entry<String, Integer> bioseq : chrLength.entrySet()) {
            final String seq_name = bioseq.getKey();
            BioSeq seq = this.group.getSeq(seq_name);
            if (seq == null) {
                seq = this.group.addSeq(seq_name, bioseq.getValue(), this.uri.toString());
            }
            this.chrList.put(seq, chrFiles.get(seq_name));
        }
    }

    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        UnindexedSymLoader.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        UnindexedSymLoader.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
