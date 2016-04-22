//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.io.BufferedWriter;
import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.Scored;
import java.io.DataOutputStream;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.awt.Color;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.UcscBedSym;
import com.affymetrix.genometryImpl.symmetry.UcscBedDetailSym;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
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
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.BioSeqComparator;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.parsers.TrackLineParser;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.regex.Pattern;
import java.util.List;

public class BED extends SymLoader implements LineProcessor
{
    private static final List<String> pref_list;
    private static final boolean DEBUG = false;
    private static final Pattern line_regex;
    private static final Pattern tab_regex;
    private static final Pattern comma_regex;
    private final List<SeqSymmetry> symlist;
    private final Map<BioSeq, Map<String, SeqSymmetry>> seq2types;
    private boolean annotate_seq;
    private boolean create_container_annot;
    private String default_type;
    private final TrackLineParser track_line_parser;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    private int BED_DETAIL_LINE_CHECK_COUNT;
    private static final int BED_DETAIL_LINE_CHECK_LIMIT = 10;
    private static final int BED_DETAIL_FIELD_COUNT = 14;
    private boolean bedDetail;

    public BED(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.symlist = new ArrayList<SeqSymmetry>();
        this.seq2types = new HashMap<BioSeq, Map<String, SeqSymmetry>>();
        this.annotate_seq = true;
        this.create_container_annot = false;
        this.default_type = null;
        this.track_line_parser = new TrackLineParser();
        this.BED_DETAIL_LINE_CHECK_COUNT = 0;
        this.bedDetail = false;
        this.default_type = uri.toString();
        this.annotate_seq = false;
        this.create_container_annot = false;
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return BED.strategyList;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        if (this.buildIndex()) {
            this.sortCreatedFiles();
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
    public List<SeqSymmetry> getGenome() throws Exception {
        this.init();
        final List<BioSeq> allSeq = this.getChromosomeList();
        final List<SeqSymmetry> retList = new ArrayList<SeqSymmetry>();
        for (final BioSeq seq : allSeq) {
            retList.addAll(this.getChromosome(seq));
        }
        return retList;
    }

    @Override
    public List<SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.parse(seq, seq.getMin(), seq.getMax());
    }

    @Override
    public List<SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        this.init();
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater(this, "BED parse lines " + this.uri);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax());
    }

    private List<SeqSymmetry> parse(final BioSeq seq, final int min, final int max) throws Exception {
        InputStream istr = null;
        try {
            final File file = this.chrList.get(seq);
            final boolean isSorted = this.chrSort.get(seq);
            if (file == null) {
                Logger.getLogger(BED.class.getName()).log(Level.FINE, "Could not find chromosome {0}", seq.getID());
                return Collections.emptyList();
            }
            istr = new FileInputStream(file);
            return this.parse(istr, isSorted, min, max);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }

    private List<SeqSymmetry> parse(final InputStream istr, final boolean isSorted, final int min, final int max) throws IOException {
        BufferedInputStream bis;
        if (istr instanceof BufferedInputStream) {
            bis = (BufferedInputStream)istr;
        }
        else {
            bis = new BufferedInputStream(istr);
        }
        final DataInputStream dis = new DataInputStream(bis);
        this.parse(dis, isSorted, min, max);
        return this.symlist;
    }

    private void parse(final DataInputStream dis, final boolean isSorted, final int min, final int max) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
        final Iterator<String> it = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                String line = null;
                try {
                    line = reader.readLine();
                }
                catch (IOException x) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error reading bed file", x);
                    line = null;
                }
                return line;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        this.parse(it, isSorted, min, max, this);
    }

    private void parse(final Iterator<String> it, final boolean isSorted, final int min, final int max, final LineTrackerI lineTracker) {
        this.seq2types.clear();
        this.symlist.clear();
        String type = this.default_type;
        String bedType = null;
        boolean use_item_rgb = false;
        final GenometryModel gmodel = GenometryModel.getGenometryModel();
        final Thread thread = Thread.currentThread();
        try {
            this.lastSleepTime = System.nanoTime();
            String line;
            while ((line = it.next()) != null && !thread.isInterrupted()) {
                this.checkSleep();
                if (lineTracker != null) {
                    lineTracker.notifyReadLine(line.length());
                }
                if (line.length() == 0) {
                    continue;
                }
                final char firstChar = line.charAt(0);
                if (firstChar == '#') {
                    continue;
                }
                if (firstChar == 't' && line.startsWith("track")) {
                    this.track_line_parser.parseTrackLine(line);
                    TrackLineParser.createTrackStyle(this.track_line_parser.getCurrentTrackHash(), this.default_type, this.extension);
                    type = this.track_line_parser.getCurrentTrackHash().get("name");
                    final String item_rgb_string = this.track_line_parser.getCurrentTrackHash().get("itemrgb");
                    use_item_rgb = "on".equalsIgnoreCase(item_rgb_string);
                    bedType = this.track_line_parser.getCurrentTrackHash().get("type");
                    this.bedDetail = "bedDetail".equals(bedType);
                }
                else {
                    if (firstChar == 'b' && line.startsWith("browser")) {
                        continue;
                    }
                    if (!this.parseLine(line, gmodel, type, use_item_rgb, min, max) && isSorted) {
                        break;
                    }
                    continue;
                }
            }
        }
        catch (InterruptedException ex) {}
    }

    @Override
    public List<? extends SeqSymmetry> processLines(final BioSeq seq, final LineReader lineReader, final LineTrackerI lineTracker) {
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
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error reading bed file", x);
                    line = null;
                }
                return line;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        this.parse(it, true, 0, Integer.MAX_VALUE, lineTracker);
        return this.symlist;
    }

    private boolean parseLine(final String line, final GenometryModel gmodel, final String type, final boolean use_item_rgb, final int minimum, final int maximum) {
        String[] fields = BED.tab_regex.split(line);
        if (fields.length == 1) {
            fields = BED.line_regex.split(line);
        }
        String detailSymbol = null;
        String detailDescription = null;
        int field_count = fields.length;
        if (!this.bedDetail && field_count == 14 && this.BED_DETAIL_LINE_CHECK_COUNT++ < 10) {
            this.bedDetail = true;
        }
        if (this.bedDetail) {
            detailSymbol = fields[field_count - 2];
            detailDescription = fields[field_count - 1];
            field_count -= 2;
        }
        if (field_count < 3) {
            return true;
        }
        String seq_name = null;
        String annot_name = null;
        String itemRgb = "";
        int thick_min = Integer.MIN_VALUE;
        int thick_max = Integer.MIN_VALUE;
        float score = Float.NEGATIVE_INFINITY;
        int[] blockSizes = null;
        int[] blockStarts = null;
        int[] blockMins = null;
        int[] blockMaxs = null;
        final boolean includes_bin_field = field_count > 6 && (fields[6].startsWith("+") || fields[6].startsWith("-") || fields[6].startsWith("."));
        int findex = 0;
        if (includes_bin_field) {
            ++findex;
        }
        seq_name = fields[findex++];
        final int beg = Integer.parseInt(fields[findex++]);
        final int end = Integer.parseInt(fields[findex++]);
        if (field_count >= 4) {
            annot_name = parseName(fields[findex++]);
            if (annot_name == null || annot_name.length() == 0) {
                annot_name = this.group.getID();
            }
        }
        if (field_count >= 5) {
            score = parseScore(fields[findex++]);
        }
        boolean forward;
        if (field_count >= 6) {
            forward = !fields[findex++].equals("-");
        }
        else {
            forward = (beg <= end);
        }
        final int min = Math.min(beg, end);
        final int max = Math.max(beg, end);
        if (!checkRange(min, max, minimum, maximum)) {
            return max <= maximum;
        }
        BioSeq seq = this.group.getSeq(seq_name);
        if (seq == null && seq_name.indexOf(59) > -1) {
            String seqid = seq_name.substring(0, seq_name.indexOf(59));
            String version = seq_name.substring(seq_name.indexOf(59) + 1);
            if (gmodel.getSeqGroup(version) == this.group || this.group.getID().equals(version)) {
                seq = this.group.getSeq(seqid);
                if (seq != null) {
                    seq_name = seqid;
                }
            }
            else if (gmodel.getSeqGroup(seqid) == this.group || this.group.getID().equals(seqid)) {
                final String temp = seqid;
                seqid = version;
                version = temp;
                seq = this.group.getSeq(seqid);
                if (seq != null) {
                    seq_name = seqid;
                }
            }
        }
        if (seq == null) {
            seq = this.group.addSeq(seq_name, 0, this.uri.toString());
        }
        if (field_count >= 8) {
            thick_min = Integer.parseInt(fields[findex++]);
            thick_max = Integer.parseInt(fields[findex++]);
        }
        if (field_count >= 9) {
            itemRgb = fields[findex++];
        }
        else {
            ++findex;
        }
        if (field_count >= 12) {
            final int blockCount = Integer.parseInt(fields[findex++]);
            blockSizes = parseIntArray(fields[findex++]);
            if (blockCount != blockSizes.length) {
                System.out.println("WARNING: block count does not agree with block sizes.  Ignoring " + annot_name + " on " + seq_name);
                return true;
            }
            blockStarts = parseIntArray(fields[findex++]);
            if (blockCount != blockStarts.length) {
                System.out.println("WARNING: block size does not agree with block starts.  Ignoring " + annot_name + " on " + seq_name);
                return true;
            }
            blockMins = makeBlockMins(min, blockStarts);
            blockMaxs = makeBlockMaxs(blockSizes, blockMins);
        }
        else {
            blockMins = new int[] { min };
            blockMaxs = new int[] { max };
        }
        if (max > seq.getLength()) {
            seq.setLength(max);
        }
        SymWithProps bedline_sym = null;
        bedline_sym = (this.bedDetail ? new UcscBedDetailSym(type, seq, min, max, annot_name, score, forward, thick_min, thick_max, blockMins, blockMaxs, detailSymbol, detailDescription) : new UcscBedSym(type, seq, min, max, annot_name, score, forward, thick_min, thick_max, blockMins, blockMaxs));
        if (use_item_rgb && itemRgb != null) {
            Color c = null;
            try {
                c = TrackLineParser.reformatColor(itemRgb);
            }
            catch (Exception e) {
                Logger.getLogger(BED.class.getName()).log(Level.SEVERE, "Could not parse a color from String '" + itemRgb + "'");
            }
            if (c != null) {
                bedline_sym.setProperty("itemrgb", c);
            }
        }
        this.symlist.add(bedline_sym);
        this.notifyAddSymmetry(bedline_sym);
        if (this.annotate_seq) {
            this.annotationParsed(bedline_sym);
        }
        if (annot_name != null) {
            this.group.addToIndex(annot_name, bedline_sym);
        }
        return true;
    }

    private static float parseScore(final String s) {
        return Float.parseFloat(s);
    }

    private static String parseName(final String s) {
        final String annot_name = new String(s);
        return annot_name;
    }

    private void annotationParsed(final SeqSymmetry bedline_sym) {
        final BioSeq seq = bedline_sym.getSpan(0).getBioSeq();
        if (this.create_container_annot) {
            String type = this.track_line_parser.getCurrentTrackHash().get("name");
            if (type == null) {
                type = this.default_type;
            }
            Map<String, SeqSymmetry> type2csym = this.seq2types.get(seq);
            if (type2csym == null) {
                type2csym = new HashMap<String, SeqSymmetry>();
                this.seq2types.put(seq, type2csym);
            }
            SimpleSymWithProps parent_sym = (SimpleSymWithProps)type2csym.get(type);
            if (parent_sym == null) {
                parent_sym = new SimpleSymWithProps();
                parent_sym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
                parent_sym.setProperty("method", type);
                parent_sym.setProperty("preferred_formats", BED.pref_list);
                parent_sym.setProperty("container sym", Boolean.TRUE);
                seq.addAnnotation(parent_sym);
                type2csym.put(type, parent_sym);
            }
            parent_sym.addChild(bedline_sym);
        }
        else {
            seq.addAnnotation(bedline_sym);
        }
    }

    public static int[] parseIntArray(final String int_array) {
        final String[] intstrings = BED.comma_regex.split(int_array);
        final int count = intstrings.length;
        final int[] results = new int[count];
        for (int i = 0; i < count; ++i) {
            final int val = Integer.parseInt(intstrings[i]);
            results[i] = val;
        }
        return results;
    }

    public static int[] makeBlockMins(final int min, final int[] blockStarts) {
        final int count = blockStarts.length;
        final int[] blockMins = new int[count];
        for (int i = 0; i < count; ++i) {
            blockMins[i] = blockStarts[i] + min;
        }
        return blockMins;
    }

    public static int[] makeBlockMaxs(final int[] blockMins, final int[] blockSizes) {
        final int count = blockMins.length;
        final int[] blockMaxs = new int[count];
        for (int i = 0; i < count; ++i) {
            blockMaxs[i] = blockMins[i] + blockSizes[i];
        }
        return blockMaxs;
    }

    public static void writeBedFormat(final DataOutputStream out, final List<SeqSymmetry> syms, final BioSeq seq) throws IOException {
        for (final SeqSymmetry sym : syms) {
            writeSymmetry(out, sym, seq);
        }
    }

    public static void writeSymmetry(final DataOutputStream out, final SeqSymmetry sym, final BioSeq seq) throws IOException {
        final SeqSpan span = sym.getSpan(seq);
        if (span == null) {
            return;
        }
        if (sym instanceof UcscBedSym) {
            final UcscBedSym bedsym = (UcscBedSym)sym;
            if (seq == bedsym.getBioSeq()) {
                bedsym.outputBedFormat(out);
            }
            return;
        }
        SymWithProps propsym = null;
        if (sym instanceof SymWithProps) {
            propsym = (SymWithProps)sym;
        }
        writeOutFile(out, seq, span, sym, propsym);
    }

    private static void writeOutFile(final DataOutputStream out, final BioSeq seq, final SeqSpan span, final SeqSymmetry sym, final SymWithProps propsym) throws IOException {
        out.write(seq.getID().getBytes());
        out.write(9);
        final int min = span.getMin();
        final int max = span.getMax();
        out.write(Integer.toString(min).getBytes());
        out.write(9);
        out.write(Integer.toString(max).getBytes());
        final int childcount = sym.getChildCount();
        if (!span.isForward() || childcount > 0 || propsym != null) {
            out.write(9);
            if (propsym != null) {
                if (propsym.getProperty("name") != null) {
                    out.write(((String)propsym.getProperty("name")).getBytes());
                }
                else if (propsym.getProperty("id") != null) {
                    out.write(((String)propsym.getProperty("id")).getBytes());
                }
            }
            out.write(9);
            if (propsym != null && propsym.getProperty("score") != null) {
                out.write(propsym.getProperty("score").toString().getBytes());
            }
            else if (sym instanceof Scored) {
                out.write(Float.toString(((Scored)sym).getScore()).getBytes());
            }
            else {
                out.write(48);
            }
            out.write(9);
            if (span.isForward()) {
                out.write(43);
            }
            else {
                out.write(45);
            }
            if (childcount > 0) {
                writeOutChildren(out, propsym, min, max, childcount, sym, seq);
            }
        }
        out.write(10);
    }

    private static void writeOutChildren(final DataOutputStream out, final SymWithProps propsym, final int min, final int max, final int childcount, final SeqSymmetry sym, final BioSeq seq) throws IOException {
        out.write(9);
        if (propsym != null && propsym.getProperty("cds min") != null) {
            out.write(propsym.getProperty("cds min").toString().getBytes());
        }
        else {
            out.write(Integer.toString(min).getBytes());
        }
        out.write(9);
        if (propsym != null && propsym.getProperty("cds max") != null) {
            out.write(propsym.getProperty("cds max").toString().getBytes());
        }
        else {
            out.write(Integer.toString(max).getBytes());
        }
        out.write(9);
        out.write(48);
        out.write(9);
        out.write(Integer.toString(childcount).getBytes());
        out.write(9);
        final int[] blockSizes = new int[childcount];
        final int[] blockStarts = new int[childcount];
        for (int i = 0; i < childcount; ++i) {
            final SeqSymmetry csym = sym.getChild(i);
            final SeqSpan cspan = csym.getSpan(seq);
            blockSizes[i] = cspan.getLength();
            blockStarts[i] = cspan.getMin() - min;
        }
        for (int i = 0; i < childcount; ++i) {
            out.write(Integer.toString(blockSizes[i]).getBytes());
            out.write(44);
        }
        out.write(9);
        for (int i = 0; i < childcount; ++i) {
            out.write(Integer.toString(blockStarts[i]).getBytes());
            out.write(44);
        }
    }

    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (final SeqSymmetry sym : syms) {
                writeSymmetry(dos, sym, seq);
            }
            dos.flush();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void writeSymmetry(final SeqSymmetry sym, final BioSeq seq, final OutputStream os) throws IOException {
        DataOutputStream dos = null;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream)os;
        }
        else {
            dos = new DataOutputStream(os);
        }
        writeSymmetry(dos, sym, seq);
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

    @Override
    public List<String> getFormatPrefList() {
        return BED.pref_list;
    }

    public String getMimeType() {
        return "text/bed";
    }

    private static boolean checkRange(final int start, final int end, final int min, final int max) {
        return end >= min && start <= max;
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater(this, "BED parse lines " + this.uri);
        if (CThreadHolder.getInstance().getCurrentCThreadWorker() != null) {
            CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        Map<String, Boolean> chrTrack = new HashMap<String, Boolean>();
        final Map<String, BufferedWriter> chrs = new HashMap<String, BufferedWriter>();
        String trackLine = null;
        String seq_name = null;
        int lineCounter = 0;
        try {
            final Thread thread = Thread.currentThread();
            br = new BufferedReader(new InputStreamReader(istr));
            this.lastSleepTime = System.nanoTime();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                ++lineCounter;
                this.checkSleep();
                this.notifyReadLine(line.length());
                if (line.length() == 0) {
                    continue;
                }
                char firstChar = line.charAt(0);
                if (firstChar == '#') {
                    continue;
                }
                if (firstChar == 't' && line.startsWith("track")) {
                    chrTrack = new HashMap<String, Boolean>();
                    trackLine = line;
                }
                else {
                    if (firstChar == 'b' && line.startsWith("browser")) {
                        continue;
                    }
                    String[] fields = BED.tab_regex.split(line);
                    if (fields.length == 1) {
                        fields = BED.line_regex.split(line);
                    }
                    if (fields.length < 3) {
                        Logger.getLogger(BED.class.getName()).log(Level.WARNING, "Invalid line at {0} in BED file", lineCounter);
                    }
                    else {
                        boolean includes_bin_field = false;
                        if (fields.length > 6) {
                            firstChar = fields[6].charAt(0);
                            includes_bin_field = (firstChar == '+' || firstChar == '-' || firstChar == '.');
                        }
                        int findex = 0;
                        if (includes_bin_field) {
                            ++findex;
                        }
                        seq_name = fields[findex++];
                        final int beg = Integer.parseInt(fields[findex++]);
                        final int end = Integer.parseInt(fields[findex++]);
                        final int max = Math.max(beg, end);
                        if (!chrs.containsKey(seq_name)) {
                            this.addToLists(chrs, seq_name, chrFiles, chrLength, ".bed");
                        }
                        bw = chrs.get(seq_name);
                        if (!chrTrack.containsKey(seq_name)) {
                            chrTrack.put(seq_name, true);
                            if (trackLine != null) {
                                bw.write(trackLine + "\n");
                            }
                        }
                        bw.write(line + "\n");
                        if (max <= chrLength.get(seq_name)) {
                            continue;
                        }
                        chrLength.put(seq_name, max);
                    }
                }
            }
            return !thread.isInterrupted();
        }
        catch (InterruptedException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw ex2;
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
        final GenometryModel gmodel = GenometryModel.getGenometryModel();
        for (final Map.Entry<String, Integer> bioseq : chrLength.entrySet()) {
            String seq_name = bioseq.getKey();
            BioSeq seq = this.group.getSeq(seq_name);
            if (seq == null && seq_name.indexOf(59) > -1) {
                String seqid = seq_name.substring(0, seq_name.indexOf(59));
                String version = seq_name.substring(seq_name.indexOf(59) + 1);
                if (gmodel.getSeqGroup(version) == this.group || this.group.getID().equals(version)) {
                    seq = this.group.getSeq(seqid);
                    if (seq != null) {
                        seq_name = seqid;
                    }
                }
                else if (gmodel.getSeqGroup(seqid) == this.group || this.group.getID().equals(seqid)) {
                    final String temp = seqid;
                    seqid = version;
                    version = temp;
                    seq = this.group.getSeq(seqid);
                    if (seq != null) {
                        seq_name = seqid;
                    }
                }
            }
            if (seq == null) {
                seq = this.group.addSeq(seq_name, bioseq.getValue(), this.uri.toString());
            }
            this.chrList.put(seq, chrFiles.get(seq_name));
        }
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

    static {
        (pref_list = new ArrayList<String>()).add("bed");
        line_regex = Pattern.compile("\\s+");
        tab_regex = Pattern.compile("\\t");
        comma_regex = Pattern.compile(",");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        BED.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        BED.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
