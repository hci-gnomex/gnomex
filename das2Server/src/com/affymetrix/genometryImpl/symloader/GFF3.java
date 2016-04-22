//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.regex.Matcher;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.broad.tribble.readers.LineReader;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
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
import com.affymetrix.genometryImpl.parsers.GFF3Parser;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import java.util.regex.Pattern;

public class GFF3 extends SymLoader implements LineProcessor
{
    private static final boolean DEBUG = false;
    private static final Pattern line_regex;
    private static final Pattern directive_version;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    private GFF3Parser parser;

    public GFF3(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.parser = new GFF3Parser();
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return GFF3.strategyList;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
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
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("GFF3 SymLoaderProgressUpdater getRegion for " + this.uri + " - " + span, span);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax());
    }

    private List<? extends SeqSymmetry> parse(final BioSeq seq, final int min, final int max) throws Exception {
        InputStream istr = null;
        try {
            final File file = this.chrList.get(seq);
            if (file == null) {
                Logger.getLogger(GFF3.class.getName()).log(Level.FINE, "Could not find chromosome {0}", seq.getID());
                return Collections.emptyList();
            }
            istr = new FileInputStream(file);
            return this.parser.parse(istr, this.uri.toString(), this.group, true);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }

    @Override
    public List<? extends SeqSymmetry> processLines(final BioSeq seq, final LineReader lineReader, final LineTrackerI lineTracker) throws Exception {
        final Iterator<String> it = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                String line = null;
                try {
                    line = lineReader.readLine();
                }
                catch (IOException x) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error reading gff file", x);
                    line = null;
                }
                return line;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        this.parser.parse(it, this.uri.toString(), this.group, true, lineTracker);
        return this.parser.symlist;
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater(this, "GFF3 parse lines " + this.uri);
        if (CThreadHolder.getInstance().getCurrentCThreadWorker() != null) {
            CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        Map<String, Boolean> chrTrack = new HashMap<String, Boolean>();
        final Map<String, BufferedWriter> chrs = new HashMap<String, BufferedWriter>();
        String trackLine = null;
        String seq_name = null;
        int counter = 0;
        try {
            final Thread thread = Thread.currentThread();
            br = new BufferedReader(new InputStreamReader(istr));
            this.lastSleepTime = System.nanoTime();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                ++counter;
                this.checkSleep();
                this.notifyReadLine(line.length());
                if (line.length() == 0) {
                    continue;
                }
                final char firstchar = line.charAt(0);
                if (firstchar == '#') {
                    if (!line.startsWith("##track name")) {
                        continue;
                    }
                    chrTrack = new HashMap<String, Boolean>();
                    trackLine = line;
                }
                else {
                    final String[] fields = GFF3.line_regex.split(line);
                    if (fields.length < 5) {
                        Logger.getLogger(GFF3.class.getName()).log(Level.WARNING, "Invalid line at {0} in GFF3 file", counter);
                    }
                    else {
                        seq_name = fields[0];
                        final int end = Integer.parseInt(fields[4]);
                        if (!chrs.containsKey(seq_name)) {
                            this.addToLists(chrs, seq_name, chrFiles, chrLength, ".gff3");
                        }
                        bw = chrs.get(seq_name);
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
            }
            return !thread.isInterrupted();
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            for (final BufferedWriter b : chrs.values()) {
                GeneralUtils.safeClose(b);
            }
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bw);
        }
    }

    public static boolean isGFF3(final URI uri) {
        BufferedInputStream bis = null;
        BufferedReader br = null;
        try {
            final Thread thread = Thread.currentThread();
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(uri);
            br = new BufferedReader(new InputStreamReader(bis));
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                if (!line.startsWith("#")) {
                    return false;
                }
                if (processDirective(line)) {
                    return true;
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(GFF3.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bis);
        }
        return false;
    }

    private static boolean processDirective(final String line) throws IOException {
        final Matcher m = GFF3.directive_version.matcher(line);
        if (m.matches()) {
            final String vstr = m.group(1).trim();
            if ("3".equals(vstr)) {
                return true;
            }
        }
        else {
            Logger.getLogger(GFF3.class.getName()).log(Level.WARNING, "Didn''t recognize directive: {0}", line);
        }
        return false;
    }

    @Override
    public void init(final URI uri) {
    }

    @Override
    public SeqSpan getSpan(final String line) {
        return null;
    }

    @Override
    public boolean processInfoLine(final String line, final List<String> infoLines) {
        return false;
    }

    @Override
    public void clear() {
        this.parser.clear();
    }

    static {
        line_regex = Pattern.compile("\\s+");
        directive_version = Pattern.compile("##gff-version\\s+(.*)");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        GFF3.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        GFF3.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
