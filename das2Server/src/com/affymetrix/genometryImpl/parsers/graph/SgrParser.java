// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Iterator;
import java.util.Arrays;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Map;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class SgrParser implements GraphParser
{
    private static final boolean DEBUG = false;
    private static final Pattern line_regex;
    
    public static List<GraphSym> parse(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final boolean annotate_seq) throws IOException {
        return parse(istr, stream_name, seq_group, annotate_seq, true);
    }
    
    public static List<GraphSym> parse(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final boolean annotate_seq, final boolean ensure_unique_id) throws IOException {
        final List<GraphSym> results = new ArrayList<GraphSym>();
        try {
            final InputStreamReader isr = new InputStreamReader(istr);
            final BufferedReader br = new BufferedReader(isr);
            final Map<String, IntArrayList> xhash = new HashMap<String, IntArrayList>();
            final Map<String, FloatArrayList> yhash = new HashMap<String, FloatArrayList>();
            String gid = stream_name;
            if (ensure_unique_id) {
                gid = AnnotatedSeqGroup.getUniqueGraphID(gid, seq_group);
            }
            parseLines(br, xhash, yhash);
            createResults(xhash, seq_group, yhash, gid, results);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (!(e instanceof IOException)) {
                final IOException ioe = new IOException("Trouble reading SGR file: " + stream_name);
                ioe.initCause(e);
                throw ioe;
            }
        }
        return results;
    }
    
    private static void parseLines(final BufferedReader br, final Map<String, IntArrayList> xhash, final Map<String, FloatArrayList> yhash) throws IOException, NumberFormatException {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.charAt(0) == '#') {
                continue;
            }
            if (line.charAt(0) == '%') {
                continue;
            }
            final String[] fields = SgrParser.line_regex.split(line);
            final String seqid = fields[0];
            IntArrayList xlist = xhash.get(seqid);
            if (xlist == null) {
                xlist = new IntArrayList();
                xhash.put(seqid, xlist);
            }
            FloatArrayList ylist = yhash.get(seqid);
            if (ylist == null) {
                ylist = new FloatArrayList();
                yhash.put(seqid, ylist);
            }
            final int x = Integer.parseInt(fields[1]);
            final float y = Float.parseFloat(fields[2]);
            xlist.add(x);
            ylist.add(y);
        }
    }
    
    public static boolean writeSgrFormat(final GraphSym graf, final OutputStream ostr) throws IOException {
        final BioSeq seq = graf.getGraphSeq();
        if (seq == null) {
            throw new IOException("You cannot use the '.sgr' format when the sequence is unknown. Use '.gr' instead.");
        }
        final String seq_id = seq.getID();
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            writeGraphPoints(graf, dos, seq_id);
        }
        finally {
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(dos);
        }
        return true;
    }
    
    private static void writeGraphPoints(final GraphSym graf, final DataOutputStream dos, final String seq_id) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            dos.writeBytes(seq_id + "\t" + graf.getGraphXCoord(i) + "\t" + graf.getGraphYCoordString(i) + "\n");
        }
    }
    
    private static void createResults(final Map<String, IntArrayList> xhash, final AnnotatedSeqGroup seq_group, final Map<String, FloatArrayList> yhash, final String gid, final List<GraphSym> results) {
        for (final Map.Entry<String, IntArrayList> keyval : xhash.entrySet()) {
            final String seqid = keyval.getKey();
            BioSeq aseq = seq_group.getSeq(seqid);
            IntArrayList xlist = keyval.getValue();
            FloatArrayList ylist = yhash.get(seqid);
            if (aseq == null) {
                aseq = seq_group.addSeq(seqid, xlist.get(xlist.size() - 1));
            }
            final int[] xcoords = Arrays.copyOf(xlist.elements(), xlist.size());
            xlist = null;
            final float[] ycoords = Arrays.copyOf(ylist.elements(), ylist.size());
            ylist = null;
            final int xcount = xcoords.length;
            boolean sorted = true;
            int prevx = Integer.MIN_VALUE;
            for (final int x : xcoords) {
                if (x < prevx) {
                    sorted = false;
                    break;
                }
                prevx = x;
            }
            if (!sorted) {
                GrParser.sortXYDataOnX(xcoords, ycoords);
            }
            final GraphSym graf = new GraphSym(xcoords, ycoords, gid, aseq);
            results.add(graf);
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        throw new IllegalStateException("sgr should not be processed here");
    }
    
    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        final InputStream newstr = GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        return parse(newstr, stream_name, seq_group, false);
    }
    
    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file_name));
            writeSgrFormat(gsym, bos);
        }
        finally {
            GeneralUtils.safeClose(bos);
        }
    }
    
    static {
        line_regex = Pattern.compile("\\s+");
    }
}
