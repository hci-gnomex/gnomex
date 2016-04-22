// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.awt.Color;
import com.affymetrix.genometryImpl.style.GraphState;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import java.util.Collections;
import java.io.IOException;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Iterator;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.HashMap;
import java.util.Collection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.TrackLineParser;
import java.util.regex.Pattern;

public final class WiggleParser implements GraphParser
{
    private static final Pattern field_regex;
    private static final boolean ensure_unique_id = true;
    private final TrackLineParser track_line_parser;
    
    public WiggleParser() {
        this.track_line_parser = new TrackLineParser();
    }
    
    public List<GraphSym> parse(final InputStream istr, final AnnotatedSeqGroup seq_group, final boolean annotate_seq, final String stream_name) throws IOException {
        WigFormat current_format = WigFormat.BED4;
        final List<GraphSym> grafs = new ArrayList<GraphSym>();
        WiggleData current_data = null;
        Map<String, WiggleData> current_datamap = null;
        boolean previous_track_line = false;
        final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        String current_seq_id = null;
        int current_start = 0;
        int current_step = 0;
        int current_span = 0;
        String line;
        while ((line = br.readLine()) != null && !Thread.currentThread().isInterrupted()) {
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#' || line.charAt(0) == '%') {
                continue;
            }
            if (line.startsWith("browser")) {
                continue;
            }
            if (line.startsWith("track")) {
                if (previous_track_line) {
                    grafs.addAll(createGraphSyms(this.track_line_parser.getCurrentTrackHash(), seq_group, current_datamap, stream_name));
                }
                this.track_line_parser.parseTrackLine(line);
                previous_track_line = true;
                current_format = WigFormat.BED4;
                current_data = null;
                current_datamap = new HashMap<String, WiggleData>();
            }
            else if (line.startsWith("variableStep")) {
                if (!previous_track_line) {
                    throw new IllegalArgumentException("Wiggle format error: 'variableStep' line does not have a previous 'track' line");
                }
                current_format = WigFormat.VARSTEP;
                current_seq_id = parseFormatLine(line, "chrom", "unknown");
                current_span = Integer.parseInt(parseFormatLine(line, "span", "1"));
            }
            else if (line.startsWith("fixedStep")) {
                if (!previous_track_line) {
                    throw new IllegalArgumentException("Wiggle format error: 'fixedStep' line does not have a previous 'track' line");
                }
                current_format = WigFormat.FIXEDSTEP;
                current_seq_id = parseFormatLine(line, "chrom", "unknown");
                current_start = Integer.parseInt(parseFormatLine(line, "start", "1"));
                if (current_start < 1) {
                    throw new IllegalArgumentException("'fixedStep' format with start of " + current_start + ".");
                }
                current_step = Integer.parseInt(parseFormatLine(line, "step", "1"));
                current_span = Integer.parseInt(parseFormatLine(line, "span", "1"));
            }
            else {
                current_start = parseData(previous_track_line, line, current_format, seq_group, current_data, current_datamap, current_seq_id, current_span, current_start, current_step);
            }
        }
        grafs.addAll(createGraphSyms(this.track_line_parser.getCurrentTrackHash(), seq_group, current_datamap, stream_name));
        if (annotate_seq) {
            for (final GraphSym graf : grafs) {
                final BioSeq seq = graf.getGraphSeq();
                seq.addAnnotation(graf);
            }
        }
        return grafs;
    }
    
    private static int parseData(final boolean previous_track_line, final String line, final WigFormat current_format, final AnnotatedSeqGroup seq_group, final WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span, int current_start, final int current_step) throws IllegalArgumentException {
        if (!previous_track_line) {
            throw new IllegalArgumentException("Wiggle format error: File does not have a previous 'track' line");
        }
        final String[] fields = WiggleParser.field_regex.split(line.trim());
        switch (current_format) {
            case BED4: {
                if (fields.length < 4) {
                    throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                }
                parseDataLine(fields, current_data, current_datamap);
                break;
            }
            case VARSTEP: {
                if (fields.length < 2) {
                    throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                }
                parseDataLine(fields, current_data, current_datamap, current_seq_id, current_span);
                break;
            }
            case FIXEDSTEP: {
                if (fields.length < 1) {
                    throw new IllegalArgumentException("Wiggle format error: Improper " + current_format + " line: " + line);
                }
                parseDataLine(fields, current_data, current_datamap, current_seq_id, current_span, current_start);
                current_start += current_step;
                break;
            }
        }
        return current_start;
    }
    
    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap) {
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
        current_data.add(x1, Float.parseFloat(fields[3]), width);
    }
    
    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span) {
        current_data = current_datamap.get(current_seq_id);
        if (current_data == null) {
            current_data = new WiggleData(current_seq_id);
            current_datamap.put(current_seq_id, current_data);
        }
        int current_start = Integer.parseInt(fields[0]);
        if (current_start < 1) {
            throw new IllegalArgumentException("'variableStep' format with start of " + current_start + ".");
        }
        --current_start;
        current_data.add(current_start, Float.parseFloat(fields[1]), current_span);
    }
    
    private static void parseDataLine(final String[] fields, WiggleData current_data, final Map<String, WiggleData> current_datamap, final String current_seq_id, final int current_span, int current_start) {
        current_data = current_datamap.get(current_seq_id);
        if (current_data == null) {
            current_data = new WiggleData(current_seq_id);
            current_datamap.put(current_seq_id, current_data);
        }
        --current_start;
        current_data.add(current_start, Float.parseFloat(fields[0]), current_span);
    }
    
    private static String parseFormatLine(final String line, final String name, final String default_val) {
        final String[] fields = WiggleParser.field_regex.split(line);
        final String fieldName = name + "=";
        for (final String field : fields) {
            if (field.startsWith(fieldName)) {
                return field.substring(name.length() + 1);
            }
        }
        return default_val;
    }
    
    private static List<GraphSym> createGraphSyms(final Map<String, String> track_hash, final AnnotatedSeqGroup seq_group, final Map<String, WiggleData> current_datamap, final String stream_name) {
        if (current_datamap == null) {
            return Collections.emptyList();
        }
        final List<GraphSym> grafs = new ArrayList<GraphSym>(current_datamap.size());
        String graph_id = track_hash.get("name");
        if (graph_id == null) {
            graph_id = stream_name;
        }
        final String graph_name = new String(graph_id);
        graph_id = AnnotatedSeqGroup.getUniqueGraphID(graph_id, seq_group);
        track_hash.put("name", graph_id);
        TrackLineParser.createGraphStyle(track_hash, graph_id, graph_name, "wig");
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
    
    public static void writeBedFormat(final GraphIntervalSym graf, final String genome_version, final OutputStream outstream) throws IOException {
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
            bw.write("track type=wiggle_0 name=\"" + gname + "\"");
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
    
    private static void writeGraphPoints(final GraphIntervalSym graf, final BufferedWriter bw, final String seq_id) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            final int x2 = graf.getGraphXCoord(i) + graf.getGraphWidthCoord(i);
            bw.write(seq_id + ' ' + graf.getGraphXCoord(i) + ' ' + x2 + ' ' + graf.getGraphYCoord(i) + '\n');
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        throw new IllegalStateException("wiggle should not be processed here");
    }
    
    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        final InputStream newstr = GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        return this.parse(newstr, seq_group, false, stream_name);
    }
    
    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        if (gsym instanceof GraphIntervalSym) {
            BufferedOutputStream bos = null;
            try {
                final GraphIntervalSym gisym = (GraphIntervalSym)gsym;
                String genome_name = null;
                if (seq_group != null) {
                    genome_name = seq_group.getID();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file_name));
                writeBedFormat(gisym, genome_name, bos);
            }
            finally {
                GeneralUtils.safeClose(bos);
            }
            return;
        }
        throw new IOException("Not the correct graph type for the '.wig' format.");
    }
    
    static {
        field_regex = Pattern.compile("\\s+");
    }
    
    private enum WigFormat
    {
        BED4, 
        VARSTEP, 
        FIXEDSTEP;
    }
}
