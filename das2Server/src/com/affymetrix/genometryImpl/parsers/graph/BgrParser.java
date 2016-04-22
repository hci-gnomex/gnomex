//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.Timer;
import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.GraphSym;

public final class BgrParser implements GraphParser
{
    public static boolean writeBgrFormat(final GraphSym graf, final OutputStream ostr) throws IOException {
        System.out.println("writing graph: " + graf);
        final BufferedOutputStream bos = new BufferedOutputStream(ostr);
        final DataOutputStream dos = new DataOutputStream(bos);
        Map<String, Object> headers = graf.getProperties();
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        if (headers.get("seq_name") == null) {
            if (graf.getGraphSeq() == null) {
                dos.writeUTF("null");
            }
            else {
                dos.writeUTF(graf.getGraphSeq().getID());
            }
        }
        else {
            dos.writeUTF((String)headers.get("seq_name"));
        }
        if (headers.get("release_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("release_name"));
        }
        if (headers.get("analysis_group_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("analysis_group_name"));
        }
        if (headers.get("map_analysis_group_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("map_analysis_group_name"));
        }
        if (headers.get("method_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("method_name"));
        }
        if (headers.get("parameter_set_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("parameter_set_name"));
        }
        if (headers.get("value_type_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("value_type_name"));
        }
        if (headers.get("control_group_name") == null) {
            dos.writeUTF("null");
        }
        else {
            dos.writeUTF((String)headers.get("control_group_name"));
        }
        writeGraphPoints(graf, dos);
        dos.close();
        return true;
    }

    public static List<GraphSym> parse(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group) throws IOException {
        final List<GraphSym> results = new ArrayList<GraphSym>();
        results.add(parse(istr, stream_name, seq_group, true));
        return results;
    }

    public static GraphSym parse(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final boolean ensure_unique_id) throws IOException {
        final Timer tim = new Timer();
        tim.start();
        int count = 0;
        final BufferedInputStream bis = new BufferedInputStream(istr);
        final DataInputStream dis = new DataInputStream(bis);
        final HashMap<String, Object> props = new HashMap<String, Object>();
        final String seq_name = dis.readUTF();
        final String release_name = dis.readUTF();
        final String analysis_group_name = dis.readUTF();
        System.out.println(seq_name + ", " + release_name + ", " + analysis_group_name);
        final String map_analysis_group_name = dis.readUTF();
        final String method_name = dis.readUTF();
        final String parameter_set_name = dis.readUTF();
        final String value_type_name = dis.readUTF();
        final String control_group_name = dis.readUTF();
        props.put("seq_name", seq_name);
        props.put("release_name", release_name);
        props.put("analysis_group_name", analysis_group_name);
        props.put("map_analysis_group_name", map_analysis_group_name);
        props.put("method_name", method_name);
        props.put("parameter_set_name", parameter_set_name);
        props.put("value_type_name", value_type_name);
        props.put("control_group_name", control_group_name);
        final int total_points = dis.readInt();
        System.out.println("loading graph from binary file, name = " + seq_name + ", release = " + release_name + ", total_points = " + total_points);
        final int[] xcoords = new int[total_points];
        final float[] ycoords = new float[total_points];
        int largest_x = 0;
        final Thread thread = Thread.currentThread();
        for (int i = 0; i < total_points && !thread.isInterrupted(); ++i) {
            final int[] array = xcoords;
            final int n = i;
            final int int1 = dis.readInt();
            array[n] = int1;
            largest_x = int1;
            ycoords[i] = dis.readFloat();
            ++count;
        }
        BioSeq seq = seq_group.getSeq(seq_name);
        if (seq == null) {
            seq = seq_group.addSeq(seq_name, largest_x, stream_name);
        }
        final StringBuffer sb = new StringBuffer();
        append(sb, analysis_group_name);
        append(sb, value_type_name);
        append(sb, parameter_set_name);
        String graph_name;
        if (sb.length() == 0) {
            graph_name = stream_name;
        }
        else {
            graph_name = sb.toString();
        }
        if (ensure_unique_id) {
            graph_name = AnnotatedSeqGroup.getUniqueGraphID(graph_name, seq);
        }
        final GraphSym graf = new GraphSym(xcoords, ycoords, graph_name, seq);
        graf.setProperties(props);
        final double load_time = tim.read() / 1000.0f;
        System.out.println("loaded graf, total points = " + count);
        System.out.println("time to load graf from binary: " + load_time);
        return graf;
    }

    static void append(final StringBuffer sb, final String s) {
        if (s != null && !"null".equals(s) && s.trim().length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(s);
        }
    }

    private static void writeGraphPoints(final GraphSym graf, final DataOutputStream dos) throws IOException {
        final int total_points = graf.getPointCount();
        dos.writeInt(total_points);
        for (int i = 0; i < total_points; ++i) {
            dos.writeInt(graf.getGraphXCoord(i));
            dos.writeFloat(graf.getGraphYCoord(i));
        }
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return parse(is, uri, group);
    }

    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        final InputStream newstr = GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        return GraphParserUtil.getInstance().wrapInList(parse(newstr, stream_name, seq_group, true));
    }

    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file_name));
            writeBgrFormat(gsym, bos);
        }
        finally {
            GeneralUtils.safeClose(bos);
        }
    }
}
