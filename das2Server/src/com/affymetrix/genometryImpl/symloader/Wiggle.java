//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.text.MessageFormat;
import com.affymetrix.genometryImpl.parsers.graph.BarParser;
import java.awt.Color;
import com.affymetrix.genometryImpl.style.GraphState;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.util.Map;
import com.affymetrix.genometryImpl.parsers.graph.WiggleData;
import java.util.HashMap;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
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
import java.util.Set;
import com.affymetrix.genometryImpl.parsers.TrackLineParser;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public class Wiggle extends SymLoader implements AnnotationWriter, LineProcessor
{
    private static final String TRACK = "track type={0}_{1} name=\"{2}_{3}\"";
    private static final String TRACK_TYPE = "wiggle_0";
    private int TRACK_COUNTER;
    private static final Pattern field_regex;
    private static final boolean ensure_unique_id = true;
    private final TrackLineParser track_line_parser;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public Wiggle(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
        this.TRACK_COUNTER = 0;
        this.track_line_parser = new TrackLineParser();
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return Wiggle.strategyList;
    }

    @Override
    public void init(final URI uri) {
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
    public List<GraphSym> getGenome() throws Exception {
        this.init();
        final List<BioSeq> allSeq = this.getChromosomeList();
        final List<GraphSym> retList = new ArrayList<GraphSym>();
        for (final BioSeq seq : allSeq) {
            retList.addAll(this.getChromosome(seq));
        }
        return retList;
    }

    @Override
    public List<GraphSym> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.parse(seq, seq.getMin(), seq.getMax());
    }

    @Override
    public List<GraphSym> getRegion(final SeqSpan span) throws Exception {
        this.init();
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax());
    }

    private List<GraphSym> parse(final BioSeq seq, final int min, final int max) throws Exception {
        final FileInputStream fis = null;
        final InputStream istr = null;
        try {
            final File file = this.chrList.get(seq);
            if (file == null) {
                Logger.getLogger(Wiggle.class.getName()).log(Level.FINE, "Could not find chromosome {0}", seq.getID());
                return Collections.emptyList();
            }
            final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            final Iterator<String> it = new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public String next() {
                    try {
                        return br.readLine();
                    }
                    catch (IOException x) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error reading wig file", x);
                        return null;
                    }
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
            return this.parse(it, seq, min, max);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(istr);
            GeneralUtils.safeClose(fis);
        }
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
        return this.parse(it, seq, seq.getMin(), seq.getMax());
    }

    private List<GraphSym> parse(final Iterator<String> it, final BioSeq seq, final int min, final int max) {
        WigFormat current_format = WigFormat.BED4;
        final List<GraphSym> grafs = new ArrayList<GraphSym>();
        WiggleData current_data = null;
        Map<String, WiggleData> current_datamap = new HashMap<String, WiggleData>();
        boolean previous_track_line = false;
        String current_seq_id = null;
        int current_start = 0;
        int current_step = 0;
        int current_span = 0;
        String line;
        while ((line = it.next()) != null && !Thread.currentThread().isInterrupted()) {
            if (line.length() == 0) {
                continue;
            }
            final char firstChar = line.charAt(0);
            if (firstChar == '#' || firstChar == '%') {
                continue;
            }
            if (firstChar == 'b' && line.startsWith("browser")) {
                continue;
            }
            if (firstChar == 't' && line.startsWith("track")) {
                if (previous_track_line) {
                    grafs.addAll(createGraphSyms(this.track_line_parser.getCurrentTrackHash(), this.group, current_datamap, this.uri.toString(), this.extension));
                }
                this.track_line_parser.parseTrackLine(line);
                previous_track_line = true;
                current_format = WigFormat.BED4;
                current_data = null;
                current_datamap = new HashMap<String, WiggleData>();
            }
            else if (firstChar == 'v' && line.startsWith("variableStep")) {
                final String[] fields = Wiggle.field_regex.split(line);
                current_format = WigFormat.VARSTEP;
                current_seq_id = parseFormatFields(fields, "chrom", "unknown");
                current_span = Integer.parseInt(parseFormatFields(fields, "span", "1"));
            }
            else if (firstChar == 'f' && line.startsWith("fixedStep")) {
                final String[] fields = Wiggle.field_regex.split(line);
                current_format = WigFormat.FIXEDSTEP;
                current_seq_id = parseFormatFields(fields, "chrom", "unknown");
                current_start = Integer.parseInt(parseFormatFields(fields, "start", "1"));
                if (current_start < 1) {
                    throw new IllegalArgumentException("'fixedStep' format with start of " + current_start + ".");
                }
                current_step = Integer.parseInt(parseFormatFields(fields, "step", "1"));
                current_span = Integer.parseInt(parseFormatFields(fields, "span", "1"));
            }
            else {
                current_start = parseData(previous_track_line, line, current_format, current_data, current_datamap, current_seq_id, current_span, current_start, current_step, seq, min, max);
            }
        }
        grafs.addAll(createGraphSyms(this.track_line_parser.getCurrentTrackHash(), this.group, current_datamap, this.uri.toString(), this.extension));
        return grafs;
    }

    private static int parseData(final boolean previous_track_line, final String line, final WigFormat current_format, final WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span, int current_start, final int current_step, final BioSeq reqSeq, final int min, final int max) throws IllegalArgumentException {
        final String[] fields = Wiggle.field_regex.split(line.trim());
        switch (current_format) {
            case BED4: {
                parseDataLine(fields, current_data, current_datamap, min, max);
                break;
            }
            case VARSTEP: {
                parseDataLine(fields, current_data, current_datamap, current_seq_id, current_span, min, max);
                break;
            }
            case FIXEDSTEP: {
                parseDataLine(fields, current_data, current_datamap, current_seq_id, current_span, current_start, min, max);
                current_start += current_step;
                break;
            }
        }
        return current_start;
    }

    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap, final int min, final int max) {
        final String seq_id = fields[0];
        current_data = current_datamap.get(seq_id);
        if (current_data == null) {
            current_data = new WiggleData(seq_id);
            current_datamap.put(seq_id, current_data);
        }
        final int x1 = Integer.parseInt(fields[1]);
        final int x2 = Integer.parseInt(fields[2]);
        final int start = Math.min(x1, x2);
        final int width = Math.max(x1, x2) - start;
        if (!checkRange(x1, width, min, max)) {
            return;
        }
        current_data.add(x1, Float.parseFloat(fields[3]), width);
    }

    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span, final int min, final int max) {
        current_data = current_datamap.get(current_seq_id);
        if (current_data == null) {
            current_data = new WiggleData(current_seq_id);
            current_datamap.put(current_seq_id, current_data);
        }
        int current_start = Integer.parseInt(fields[0]);
        if (current_start < 1) {
            throw new IllegalArgumentException("'variableStep' format with start of " + current_start + ".");
        }
        if (!checkRange(--current_start, current_span, min, max)) {
            return;
        }
        current_data.add(current_start, Float.parseFloat(fields[1]), current_span);
    }

    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span, int current_start, final int min, final int max) {
        current_data = current_datamap.get(current_seq_id);
        if (current_data == null) {
            current_data = new WiggleData(current_seq_id);
            current_datamap.put(current_seq_id, current_data);
        }
        if (!checkRange(--current_start, current_span, min, max)) {
            return;
        }
        current_data.add(current_start, Float.parseFloat(fields[0]), current_span);
    }

    private static boolean checkRange(final int start, final int width, final int min, final int max) {
        return start + width >= min && start <= max;
    }

    private static String parseFormatFields(final String[] fields, final String name, final String default_val) {
        final String fieldName = name + "=";
        for (final String field : fields) {
            if (field.startsWith(fieldName)) {
                return field.substring(name.length() + 1);
            }
        }
        return default_val;
    }

    private static List<GraphSym> createGraphSyms(final Map<String, String> track_hash, final AnnotatedSeqGroup seq_group, final Map<String, WiggleData> current_datamap, final String stream_name, final String extension) {
        if (current_datamap == null) {
            return Collections.emptyList();
        }
        final List<GraphSym> grafs = new ArrayList<GraphSym>(current_datamap.size());
        String graph_id = track_hash.get("name");
        if (graph_id == null) {
            graph_id = stream_name;
        }
        final String graph_name = new String(graph_id);
        if (!stream_name.equals(graph_id)) {
            graph_id = AnnotatedSeqGroup.getUniqueGraphTrackID(stream_name, graph_id);
        }
        track_hash.put("name", graph_id);
        TrackLineParser.createGraphStyle(track_hash, graph_id, graph_name, extension);
        final Iterator<WiggleData> wiggleDataIterator = current_datamap.values().iterator();
        while (wiggleDataIterator.hasNext()) {
            final GraphSym gsym = wiggleDataIterator.next().createGraph(seq_group, graph_id, stream_name);
            if (gsym != null) {
                grafs.add(gsym);
            }
            wiggleDataIterator.remove();
        }
        return grafs;
    }

    private static void writeGraphPoints(final GraphSym graf, final BufferedWriter bw, final String seq_id) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            final int x2 = graf.getGraphXCoord(i) + graf.getGraphWidthCoord(i);
            bw.write(seq_id + ' ' + graf.getGraphXCoord(i) + ' ' + x2 + ' ' + graf.getGraphYCoord(i) + '\n');
        }
    }

    protected String getTrackType() {
        return "wiggle_0";
    }

    public void writeBedFormat(final GraphSym graf, final String genome_version, final OutputStream outstream) throws IOException {
        final BioSeq seq = graf.getGraphSeq();
        final String seq_id = (seq == null) ? "." : seq.getID();
        final String human_name = graf.getGraphState().getTierStyle().getTrackName();
        final String gname = graf.getGraphName();
        final GraphState state = graf.getGraphState();
        final Color color = state.getTierStyle().getForeground();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(outstream));
            if (genome_version != null && genome_version.length() > 0) {
                bw.write("# genome_version = " + genome_version + '\n');
            }
            bw.write("track type=" + this.getTrackType() + " name=\"" + gname + "\"");
            bw.write(" description=\"" + human_name + "\"");
            bw.write(" visibility=full");
            bw.write(" color=" + color.getRed() + "," + color.getGreen() + "," + color.getBlue());
            bw.write(" viewLimits=" + Float.toString(state.getVisibleMinY()) + ":" + Float.toString(state.getVisibleMaxY()));
            bw.write("");
            bw.write(10);
            writeGraphPoints(graf, bw, seq_id);
            bw.flush();
        }
        finally {
            GeneralUtils.safeClose(bw);
        }
    }

    public static boolean writeBarFormat(final Collection<? extends SeqSymmetry> syms, final String type, final OutputStream ostr) {
        final BarParser instance = new BarParser();
        return instance.writeAnnotations(syms, null, type, ostr);
    }

    @Override
    public String getMimeType() {
        return "text/wig";
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        BufferedReader br = null;
        BufferedWriter bw = null;
        WigFormat current_format = WigFormat.BED4;
        boolean previous_track_line = false;
        String current_seq_id = null;
        String trackLine = null;
        int current_start = 0;
        int current_step = 0;
        int current_span = 0;
        int length = 0;
        Map<String, Boolean> chrTrack = new HashMap<String, Boolean>();
        final Map<String, BufferedWriter> chrs = new HashMap<String, BufferedWriter>();
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            final Thread thread = Thread.currentThread();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                if (line.length() == 0) {
                    continue;
                }
                final char firstChar = line.charAt(0);
                if (firstChar == '#' || firstChar == '%') {
                    continue;
                }
                if (firstChar == 'b' && line.startsWith("browser")) {
                    continue;
                }
                if (firstChar == 't' && line.startsWith("track")) {
                    chrTrack = new HashMap<String, Boolean>();
                    trackLine = line;
                    previous_track_line = true;
                    current_format = WigFormat.BED4;
                }
                else if ((firstChar == 'v' && line.startsWith("variableStep")) || (firstChar == 'f' && line.startsWith("fixedStep"))) {
                    if (!previous_track_line) {
                        trackLine = MessageFormat.format("track type={0}_{1} name=\"{2}_{3}\"", this.uri.toString(), this.TRACK_COUNTER, this.featureName, this.TRACK_COUNTER++);
                        Logger.getLogger(Wiggle.class.getName()).log(Level.WARNING, "Wiggle format error: line does not have a previous ''track'' line. Creating a dummy track line. {0}", trackLine);
                    }
                    final String[] fields = Wiggle.field_regex.split(line);
                    if (firstChar == 'v') {
                        current_format = WigFormat.VARSTEP;
                        current_seq_id = parseFormatFields(fields, "chrom", "unknown");
                        current_span = Integer.parseInt(parseFormatFields(fields, "span", "1"));
                    }
                    else {
                        current_format = WigFormat.FIXEDSTEP;
                        current_seq_id = parseFormatFields(fields, "chrom", "unknown");
                        current_start = Integer.parseInt(parseFormatFields(fields, "start", "1"));
                        if (current_start < 1) {
                            throw new IllegalArgumentException("'fixedStep' format with start of " + current_start + ".");
                        }
                        current_step = Integer.parseInt(parseFormatFields(fields, "step", "1"));
                        current_span = Integer.parseInt(parseFormatFields(fields, "span", "1"));
                    }
                    if (!chrs.containsKey(current_seq_id)) {
                        this.addToLists(chrs, current_seq_id, chrFiles, chrLength, ".wig");
                    }
                    bw = chrs.get(current_seq_id);
                    if (!chrTrack.containsKey(current_seq_id) && trackLine != null) {
                        chrTrack.put(current_seq_id, true);
                        bw.write(trackLine + "\n");
                    }
                    bw.write(line + "\n");
                }
                else {
                    final String[] fields = Wiggle.field_regex.split(line.trim());
                    switch (current_format) {
                        case BED4: {
                            if (fields.length < 4) {
                                throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                            }
                            current_seq_id = fields[0];
                            length = Integer.parseInt(fields[2]);
                            if (!chrs.containsKey(current_seq_id)) {
                                this.addToLists(chrs, current_seq_id, chrFiles, chrLength, ".wig");
                            }
                            bw = chrs.get(current_seq_id);
                            if (!chrTrack.containsKey(current_seq_id) && trackLine != null) {
                                chrTrack.put(current_seq_id, true);
                                bw.write(trackLine + "\n");
                                break;
                            }
                            break;
                        }
                        case VARSTEP: {
                            if (fields.length < 2) {
                                throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                            }
                            current_start = Integer.parseInt(fields[0]) - 1;
                            length = current_start + current_span;
                            break;
                        }
                        case FIXEDSTEP: {
                            if (fields.length < 1) {
                                throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                            }
                            length = current_start + current_span - 1;
                            current_start += current_step;
                            break;
                        }
                    }
                    bw.write(line + "\n");
                    if (length <= chrLength.get(current_seq_id)) {
                        continue;
                    }
                    chrLength.put(current_seq_id, length);
                }
            }
            return !thread.isInterrupted();
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            for (final BufferedWriter b : chrs.values()) {
                try {
                    b.flush();
                }
                catch (IOException ex2) {
                    Logger.getLogger(Wiggle.class.getName()).log(Level.SEVERE, null, ex2);
                }
                GeneralUtils.safeClose(b);
            }
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bw);
        }
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) throws IOException {
        return this.writeAnnotations(syms, seq, outstream);
    }

    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final OutputStream ostr) throws IOException {
        try {
            for (final GraphSym graf : (Set<GraphSym>)syms) {
                this.writeBedFormat(graf, graf.getGraphSeq().getID(), ostr);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        field_regex = Pattern.compile("\\s+");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Wiggle.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        Wiggle.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }

    private enum WigFormat
    {
        BED4,
        VARSTEP,
        FIXEDSTEP;
    }
}
