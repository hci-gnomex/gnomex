//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import java.util.Iterator;
import java.util.HashMap;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.ByteArrayInputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Arrays;
import com.affymetrix.genometryImpl.util.Timer;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Map;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public final class BarParser implements AnnotationWriter, GraphParser
{
    private static final boolean DEBUG = false;
    private static final int BYTE4_FLOAT = 1;
    private static final int BYTE4_SIGNED_INT = 2;
    static final int[] bytes_per_val;
    private static final String[] valstrings;
    private static final int points_per_chunk = 1024;
    private static Map<String, Object> coordset2seqs;

    public static GraphSym getRegion(final String file_name, final SeqSpan span) throws IOException {
        final GenometryModel gmodel = GenometryModel.getGenometryModel();
        final Timer tim = new Timer();
        tim.start();
        final BioSeq aseq = span.getBioSeq();
        final int min_base = span.getMin();
        final int max_base = span.getMax();
        int[] chunk_mins = (int[])BarParser.coordset2seqs.get(file_name);
        final AnnotatedSeqGroup seq_group = aseq.getSeqGroup();
        if (chunk_mins == null) {
            buildIndex(file_name, file_name, gmodel, seq_group);
            chunk_mins = (int[]) BarParser.coordset2seqs.get(file_name);
        }
        int min_index = 0;
        int max_index = 0;
        boolean readToEnd = false;
        if (chunk_mins != null) {
            min_index = Arrays.binarySearch(chunk_mins, min_base);
            max_index = Arrays.binarySearch(chunk_mins, max_base);
            if (min_index < 0) {
                min_index = -min_index - 1 - 1;
                if (min_index < 0) {
                    min_index = 0;
                }
            }
            if (max_index < 0) {
                readToEnd = true;
            }
        }
        return constructGraf(file_name, gmodel, seq_group, min_index, readToEnd, max_index, min_base, max_base, aseq, span, tim);
    }

    private static GraphSym constructGraf(final String file_name, final GenometryModel gmodel, final AnnotatedSeqGroup seq_group, final int min_index, final boolean readToEnd, final int max_index, final int min_base, final int max_base, final BioSeq aseq, final SeqSpan span, final Timer tim) throws IOException {
        GraphSym graf = null;
        DataInputStream dis = null;
        DataInputStream bufstr = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
            final BarFileHeader bar_header = parseBarHeader(dis);
            final BarSeqHeader seq_header = parseSeqHeader(file_name, dis, gmodel, seq_group, bar_header);
            final int bytes_per_point = bar_header.bytes_per_point;
            final int points_per_index = 1024;
            final int points_to_skip = min_index * points_per_index;
            final int bytes_to_skip = points_to_skip * bytes_per_point;
            int points_to_read = 0;
            if (readToEnd) {
                points_to_read = seq_header.data_point_count - points_to_skip;
            }
            else {
                points_to_read = (max_index - min_index) * points_per_index;
            }
            if (points_to_read == 0) {
                points_to_read = seq_header.data_point_count;
            }
            final int bytes_to_read = points_to_read * bytes_per_point;
            skipBytes(bytes_to_skip, dis);
            final byte[] buf = new byte[bytes_to_read];
            dis.readFully(buf);
            GeneralUtils.safeClose(dis);
            bufstr = new DataInputStream(new ByteArrayInputStream(buf));
            final int[] xcoord = new int[points_to_read];
            final float[] ycoord = new float[points_to_read];
            int start_index = 0;
            int end_index;
            final int max_end_index = end_index = points_to_read - 1;
            for (int i = 0; i < points_to_read; ++i) {
                xcoord[i] = bufstr.readInt();
                ycoord[i] = bufstr.readFloat();
                if (start_index == 0 && xcoord[i] >= min_base) {
                    start_index = i;
                }
                if (end_index == max_end_index && xcoord[i] > max_base) {
                    end_index = i - 1;
                }
            }
            final int graph_point_count = end_index - start_index + 1;
            final int[] graph_xcoords = new int[graph_point_count];
            final float[] graph_ycoords = new float[graph_point_count];
            System.arraycopy(xcoord, start_index, graph_xcoords, 0, graph_point_count);
            System.arraycopy(ycoord, start_index, graph_ycoords, 0, graph_point_count);
            checkSeqLength(aseq, graph_xcoords);
            graf = new GraphSym(graph_xcoords, graph_ycoords, "slice", aseq);
            graf.removeSpan(graf.getSpan(aseq));
            graf.addSpan(span);
            final long t1 = tim.read();
            setTagValues(seq_header, graf);
        }
        finally {
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(bufstr);
        }
        return graf;
    }

    private static void buildIndex(final String file_name, final String coord_set_id, final GenometryModel gmodel, final AnnotatedSeqGroup seq_group) throws IOException {
        final Timer tim = new Timer();
        tim.start();
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
            final BarFileHeader file_header = parseBarHeader(dis);
            final int bytes_per_point = file_header.bytes_per_point;
            final BarSeqHeader seq_header = parseSeqHeader(file_name, dis, gmodel, seq_group, file_header);
            final int total_points = seq_header.data_point_count;
            int point_count = 0;
            int chunk_count = 0;
            final int total_chunks = total_points / 1024 + 1;
            final int[] chunk_mins = new int[total_chunks];
            final int skip_offset = 1024 * bytes_per_point - 4;
            while (point_count < total_points) {
                final int base_pos = dis.readInt();
                chunk_mins[chunk_count] = base_pos;
                if (skipBytes(skip_offset, dis)) {
                    break;
                }
                point_count += 1024;
                ++chunk_count;
            }
            if (chunk_mins[total_chunks - 1] == 0) {
                chunk_mins[total_chunks - 1] = seq_header.aseq.getLength();
            }
            BarParser.coordset2seqs.put(coord_set_id, chunk_mins);
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
    }

    private static void setTagValues(final BarSeqHeader seq_header, final GraphSym graf) {
        final Map<String, String> seq_tagvals = seq_header.tagvals;
        if (seq_tagvals != null && seq_tagvals.size() > 0) {
            copyProps(graf, seq_tagvals);
            setStrandProp(seq_tagvals, graf);
        }
    }

    private static void setStrandProp(final Map<String, String> seq_tagvals, final GraphSym graf) {
        if (seq_tagvals.containsKey("strand")) {
            final String strand = seq_tagvals.get("strand");
            if (strand.equals("+")) {
                graf.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_PLUS);
            }
            if (strand.equals("-")) {
                graf.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_MINUS);
            }
        }
    }

    private static boolean skipBytes(int bytes_to_skip, final DataInputStream dis) throws IOException {
        while (bytes_to_skip > 0) {
            final int skipped = (int)dis.skip(bytes_to_skip);
            if (skipped < 0) {
                return true;
            }
            bytes_to_skip -= skipped;
        }
        return false;
    }

    public static List<GraphSym> parse(final String uri, final InputStream istr, final GenometryModel gmodel, final AnnotatedSeqGroup default_seq_group, final BioSeq chrFilter, final int min, final int max, final String stream_name, final boolean ensure_unique_id) throws IOException {
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        final List<GraphSym> graphs = new ArrayList<GraphSym>();
        final Timer tim = new Timer();
        tim.start();
        try {
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final BarFileHeader bar_header = parseBarHeader(dis);
            final boolean bar2 = bar_header.version >= 2.0f;
            final int total_seqs = bar_header.seq_count;
            final int[] val_types = bar_header.val_types;
            final int vals_per_point = bar_header.vals_per_point;
            final Map<String, String> file_tagvals = bar_header.tagvals;
            String graph_id = "unknown";
            if (stream_name != null) {
                graph_id = stream_name;
            }
            if (file_tagvals.get("file_type") != null) {
                graph_id = graph_id + ":" + file_tagvals.get("file_type");
            }
            for (int k = 0; k < total_seqs; ++k) {
                final BarSeqHeader seq_header = parseSeqHeader(uri, dis, gmodel, default_seq_group, bar_header);
                final int total_points = seq_header.data_point_count;
                final Map<String, String> seq_tagvals = seq_header.tagvals;
                final BioSeq seq = seq_header.aseq;
                if (vals_per_point == 1) {
                    throw new IOException("PARSING FOR BAR FILES WITH 1 VALUE PER POINT NOT YET IMPLEMENTED");
                }
                if (chrFilter != null && chrFilter != seq) {
                    skipBytes(total_points * vals_per_point, dis);
                }
                else if (vals_per_point == 2) {
                    if (val_types[0] != 2 || val_types[1] != 1) {
                        throw new IOException("Error in BAR file: Currently, first val must be int4, others must be float4.");
                    }
                    handle2ValPerPoint(total_points, dis, seq, min, max, graph_id, ensure_unique_id, file_tagvals, bar2, seq_tagvals, graphs);
                }
                else if (vals_per_point == 3) {
                    if (val_types[0] != 2 || val_types[1] != 1 || val_types[2] != 1) {
                        throw new IOException("Error in BAR file: Currently, first val must be int4, others must be float4.");
                    }
                    handle3ValPerPoint(total_points, dis, seq, min, max, graph_id, ensure_unique_id, file_tagvals, bar2, seq_tagvals, graphs);
                }
            }
            final long t1 = tim.read();
            Logger.getLogger(BarParser.class.getName()).log(Level.FINE, "bar load time: {0}", t1 / 1000.0f);
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(dis);
        }
        return graphs;
    }

    public static List<AnnotatedSeqGroup> getSeqGroups(final String uri, final InputStream istr, final AnnotatedSeqGroup default_seq_group, final GenometryModel gmodel) throws IOException {
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        final List<AnnotatedSeqGroup> groups = new ArrayList<AnnotatedSeqGroup>(4);
        try {
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final BarFileHeader bar_header = parseBarHeader(dis);
            final int total_seqs = bar_header.seq_count;
            final int vals_per_point = bar_header.vals_per_point;
            for (int k = 0; k < total_seqs; ++k) {
                final BarSeqHeader seq_header = parseSeqHeader(uri, dis, gmodel, default_seq_group, bar_header);
                final AnnotatedSeqGroup group = seq_header.aseq.getSeqGroup();
                if (!groups.contains(group)) {
                    groups.add(group);
                }
                skipRest(seq_header, vals_per_point, dis);
            }
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(dis);
        }
        return groups;
    }

    private static void skipRest(final BarSeqHeader seq_header, final int vals_per_point, final DataInputStream dis) throws IOException {
        final int total_points = seq_header.data_point_count;
        if (vals_per_point == 1) {
            throw new IOException("PARSING FOR BAR FILES WITH 1 VALUE PER POINT NOT YET IMPLEMENTED");
        }
        skipBytes(total_points * vals_per_point * 4, dis);
    }

    private static void handle2ValPerPoint(final int total_points, final DataInputStream dis, final BioSeq seq, final int min, final int max, String graph_id, final boolean ensure_unique_id, final Map<String, String> file_tagvals, final boolean bar2, final Map<String, String> seq_tagvals, final List<GraphSym> graphs) throws IOException {
        final IntArrayList xcoords = new IntArrayList();
        final FloatArrayList ycoords = new FloatArrayList();
        float prev_max_xcoord = -1.0f;
        boolean sort_reported = false;
        for (int i = 0; i < total_points; ++i) {
            final int col0 = dis.readInt();
            final float col2 = dis.readFloat();
            if (col0 < prev_max_xcoord && !sort_reported) {
                sort_reported = true;
            }
            prev_max_xcoord = col0;
            if (col0 >= min && col0 < max) {
                xcoords.add(col0);
                ycoords.add(col2);
            }
        }
        if (ensure_unique_id) {
            graph_id = AnnotatedSeqGroup.getUniqueGraphID(graph_id, seq);
        }
        xcoords.trimToSize();
        ycoords.trimToSize();
        final int[] xArr = xcoords.elements();
        final float[] yArr = ycoords.elements();
        checkSeqLength(seq, xArr);
        final GraphSym graf = new GraphSym(xArr, yArr, graph_id, seq);
        copyProps(graf, file_tagvals);
        if (bar2) {
            copyProps(graf, seq_tagvals);
        }
        setStrandProp(seq_tagvals, graf);
        graphs.add(graf);
    }

    private static void handle3ValPerPoint(final int total_points, final DataInputStream dis, final BioSeq seq, final int min, final int max, final String graph_id, final boolean ensure_unique_id, final Map<String, String> file_tagvals, final boolean bar2, final Map<String, String> seq_tagvals, final List<GraphSym> graphs) throws IOException {
        final IntArrayList xcoords = new IntArrayList();
        final FloatArrayList ycoords = new FloatArrayList();
        final FloatArrayList zcoords = new FloatArrayList();
        for (int i = 0; i < total_points; ++i) {
            final int col0 = dis.readInt();
            final float col2 = dis.readFloat();
            final float col3 = dis.readFloat();
            if (col0 >= min && col0 < max) {
                xcoords.add(col0);
                ycoords.add(col2);
                zcoords.add(col3);
            }
        }
        String pm_name = graph_id + " : pm";
        String mm_name = graph_id + " : mm";
        if (ensure_unique_id) {
            pm_name = AnnotatedSeqGroup.getUniqueGraphID(pm_name, seq);
            mm_name = AnnotatedSeqGroup.getUniqueGraphID(mm_name, seq);
        }
        xcoords.trimToSize();
        ycoords.trimToSize();
        zcoords.trimToSize();
        final int[] xArr = xcoords.elements();
        final float[] yArr = ycoords.elements();
        final float[] zArr = zcoords.elements();
        checkSeqLength(seq, xArr);
        final GraphSym pm_graf = new GraphSym(xArr, yArr, pm_name, seq);
        final GraphSym mm_graf = new GraphSym(xArr, zArr, mm_name, seq);
        copyProps(pm_graf, file_tagvals);
        copyProps(mm_graf, file_tagvals);
        if (bar2) {
            copyProps(pm_graf, seq_tagvals);
            copyProps(mm_graf, seq_tagvals);
        }
        pm_graf.setProperty("probetype", "PM (perfect match)");
        mm_graf.setProperty("probetype", "MM (mismatch)");
        graphs.add(pm_graf);
        graphs.add(mm_graf);
    }

    private static HashMap<String, String> readTagValPairs(final DataInput dis, final int pair_count) throws IOException {
        final HashMap<String, String> tvpairs = new HashMap<String, String>(pair_count);
        for (int i = 0; i < pair_count; ++i) {
            final int taglength = dis.readInt();
            byte[] barray = new byte[taglength];
            dis.readFully(barray);
            final String tag = new String(barray);
            final int vallength = dis.readInt();
            barray = new byte[vallength];
            dis.readFully(barray);
            final String val = new String(barray);
            tvpairs.put(tag, val);
        }
        return tvpairs;
    }

    private static void copyProps(final GraphSym graf, final Map<String, String> tagvals) {
        if (tagvals == null) {
            return;
        }
        for (final Map.Entry<String, String> tagval : tagvals.entrySet()) {
            graf.setProperty(tagval.getKey(), tagval.getValue());
        }
    }

    static BarFileHeader parseBarHeader(final DataInput dis) throws IOException {
        try {
            final byte[] headbytes = new byte[8];
            dis.readFully(headbytes);
            final float version = dis.readFloat();
            final int total_seqs = dis.readInt();
            final int vals_per_point = dis.readInt();
            final int[] val_types = new int[vals_per_point];
            for (int i = 0; i < vals_per_point; ++i) {
                val_types[i] = dis.readInt();
            }
            final int tvcount = dis.readInt();
            final HashMap<String, String> file_tagvals = readTagValPairs(dis, tvcount);
            final BarFileHeader header = new BarFileHeader(version, total_seqs, val_types, file_tagvals);
            return header;
        }
        catch (Throwable t) {
            final IOException ioe = new IOException("Could not parse bar-file header.");
            ioe.initCause(t);
            throw ioe;
        }
    }

    private static BarSeqHeader parseSeqHeader(final String uri, final DataInput dis, final GenometryModel gmodel, final AnnotatedSeqGroup default_seq_group, final BarFileHeader file_header) throws IOException {
        final int namelength = dis.readInt();
        byte[] barray = new byte[namelength];
        dis.readFully(barray);
        String seqname = new String(barray);
        String groupname = null;
        final boolean bar2 = file_header.version >= 2.0f;
        if (bar2) {
            final int grouplength = dis.readInt();
            barray = new byte[grouplength];
            dis.readFully(barray);
            groupname = new String(barray);
        }
        final int verslength = dis.readInt();
        barray = new byte[verslength];
        dis.readFully(barray);
        String seqversion = new String(barray);
        final int sc_pos = seqname.lastIndexOf(59);
        final String orig_seqname = seqname;
        if (sc_pos >= 0) {
            seqversion = seqname.substring(0, sc_pos);
            seqname = seqname.substring(sc_pos + 1);
        }
        HashMap<String, String> seq_tagvals = null;
        if (bar2) {
            final int seq_tagval_count = dis.readInt();
            seq_tagvals = readTagValPairs(dis, seq_tagval_count);
        }
        final int total_points = dis.readInt();
        final AnnotatedSeqGroup seq_group = getSeqGroup(groupname, seqversion, gmodel, default_seq_group);
        final BioSeq seq = determineSeq(uri, seq_group, seqname, orig_seqname, seqversion, groupname, bar2);
        return new BarSeqHeader(seq, total_points, seq_tagvals);
    }

    private static BioSeq determineSeq(final String uri, final AnnotatedSeqGroup seq_group, final String seqname, final String orig_seqname, final String seqversion, final String groupname, final boolean bar2) {
        BioSeq seq = seq_group.getSeq(seqname);
        if (seq == null) {
            seq = seq_group.getSeq(orig_seqname);
        }
        if (seq == null) {
            final SynonymLookup lookup = SynonymLookup.getDefaultLookup();
            for (final BioSeq testseq : seq_group.getSeqList()) {
                if (lookup.isSynonym(testseq.getID(), seqname)) {
                    if ((seqversion == null && groupname == null) || ((seqversion == null || seqversion.equals("")) && (groupname == null || groupname.equals("")))) {
                        seq = testseq;
                        break;
                    }
                    final String test_version = testseq.getVersion();
                    if (lookup.isSynonym(test_version, seqversion) || lookup.isSynonym(test_version, groupname) || lookup.isSynonym(test_version, groupname + ":" + seqversion)) {
                        seq = testseq;
                        break;
                    }
                    continue;
                }
            }
        }
        if (seq == null) {
            seq = seq_group.addSeq(seqname, 1, uri);
        }
        return seq;
    }

    private static AnnotatedSeqGroup getSeqGroup(final String groupname, final String version, final GenometryModel gmodel, final AnnotatedSeqGroup default_seq_group) {
        AnnotatedSeqGroup group = null;
        if ((version == null || version.trim().length() == 0) && (groupname == null || groupname.trim().length() == 0)) {
            return default_seq_group;
        }
        if (groupname != null && version != null) {
            group = gmodel.getSeqGroup(groupname + ":" + version);
        }
        if (group == null && groupname != null) {
            group = gmodel.getSeqGroup(groupname);
        }
        if (group == null && version != null) {
            group = gmodel.getSeqGroup(version);
        }
        if (group == null) {
            Logger.getLogger(BarParser.class.getName()).log(Level.WARNING, "Did not find group {0}.  Adding to default group {1}", new Object[] { version, default_seq_group.getID() });
            return default_seq_group;
        }
        if (group == default_seq_group) {
            return group;
        }
        Logger.getLogger(BarParser.class.getName()).log(Level.WARNING, "Switching to group {0}", group.getID());
        return group;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream ostr) {
        try {
            final BufferedOutputStream bos = new BufferedOutputStream(ostr);
            final DataOutputStream dos = new DataOutputStream(bos);
            writeHeaderInfo(dos, syms.size());
            for (final GraphSym graf : (Set<GraphSym>) syms) {
                writeSeqInfo(graf.getGraphSeq(), dos);
                writeTagValuePairs(dos, graf.getProperties());
                writeGraphPoints(graf, dos);
            }
            dos.close();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void writeHeaderInfo(final DataOutputStream dos, final int size) throws IOException {
        dos.writeBytes("barr\r\n\u001a\n");
        dos.writeFloat(2.0f);
        dos.writeInt(size);
        dos.writeInt(2);
        dos.writeInt(2);
        dos.writeInt(1);
        writeTagValues(dos);
    }

    private static void writeTagValues(final DataOutputStream dos) throws IOException {
        dos.writeInt(0);
    }

    private static void writeSeqInfo(final BioSeq seq, final DataOutputStream dos) throws IOException {
        final AnnotatedSeqGroup group = seq.getSeqGroup();
        final String groupid = group.getID();
        final String seqid = seq.getID();
        dos.writeInt(seqid.length());
        dos.writeBytes(seqid);
        dos.writeInt(groupid.length());
        dos.writeBytes(groupid);
        dos.writeInt(groupid.length());
        dos.writeBytes(groupid);
    }

    private static void writeTagValuePairs(final DataOutputStream dos, final Map<String, Object> tagValuePairs) throws IOException {
        if (tagValuePairs == null || tagValuePairs.isEmpty()) {
            dos.writeInt(0);
            return;
        }
        dos.writeInt(tagValuePairs.size());
        for (final Map.Entry<String, Object> entry : tagValuePairs.entrySet()) {
            final String tag = entry.getKey();
            dos.writeInt(tag.length());
            dos.writeBytes(tag);
            final String value = entry.getValue().toString();
            dos.writeInt(value.length());
            dos.writeBytes(value);
        }
    }

    private static void checkSeqLength(final BioSeq seq, final int[] xcoords) {
        if (seq != null) {
            final int xcount = xcoords.length;
            if (xcount > 0 && xcoords[xcount - 1] > seq.getLength()) {
                seq.setLength(xcoords[xcount - 1]);
            }
        }
    }

    private static void writeGraphPoints(final GraphSym graf, final DataOutputStream dos) throws IOException {
        final int total_points = graf.getPointCount();
        dos.writeInt(calculateTotalPoints(graf));
        for (int i = 0; i < total_points; ++i) {
            final int w = graf.getGraphWidthCoord(i);
            if (w == 0) {
                dos.writeInt(graf.getGraphXCoord(i));
                dos.writeFloat(graf.getGraphYCoord(i));
            }
            else {
                for (int j = 0; j < w + 1; ++j) {
                    dos.writeInt(j + graf.getGraphXCoord(i));
                    dos.writeFloat(graf.getGraphYCoord(i));
                }
            }
        }
    }

    private static int calculateTotalPoints(final GraphSym graf) {
        int return_points;
        for (int total_points = return_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            final int w = graf.getGraphWidthCoord(i);
            return_points += w;
        }
        return return_points;
    }

    @Override
    public String getMimeType() {
        return "binary/bar";
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return parse(uri, is, GenometryModel.getGenometryModel(), group, null, 0, Integer.MAX_VALUE, uri, false);
    }

    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        final InputStream newstr = GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        return parse(stream_name, newstr, GenometryModel.getGenometryModel(), seq_group, null, 0, Integer.MAX_VALUE, stream_name, true);
    }

    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
    }

    static {
        bytes_per_val = new int[] { 8, 4, 4, 2, 1, 4, 2, 1 };
        valstrings = new String[] { "BYTE8_FLOAT", "BYTE4_FLOAT", "BYTE4_SIGNED_INT", "BYTE2_SIGNED_INT", "BYTE1_SIGNED_INT", "BYTE4_UNSIGNED_INT", "BYTE2_UNSIGNED_INT", "BYTE1_UNSIGNED_INT" };
        BarParser.coordset2seqs = new HashMap<String, Object>();
    }
}
