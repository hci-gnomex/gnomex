// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.io.IOException;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.HashMap;
import cern.colt.list.IntArrayList;
import cern.colt.list.FloatArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.parsers.Parser;

public final class CntParser implements Parser
{
    private static final Pattern tag_val;
    private static final Pattern line_regex;
    private static final Pattern section_regex;
    private static final String SECTION_HEADER = "[Header]";
    private static final String SECTION_COL_NAME = "[ColumnName]";
    private static final int FIRST_DATA_COLUMN = 3;
    private final Map<String, FloatArrayList[]> seq2Floats;
    private final Map<String, IntArrayList> seqToIntList;
    private final Map<String, String> unique_gids;
    
    public CntParser() {
        this.seq2Floats = new HashMap<String, FloatArrayList[]>();
        this.seqToIntList = new HashMap<String, IntArrayList>();
        this.unique_gids = new HashMap<String, String>();
    }
    
    public List<GraphSym> parse(final InputStream dis, final AnnotatedSeqGroup seq_group, final boolean annotateSeq) throws IOException {
        final Thread thread = Thread.currentThread();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
        final Matcher section_regex_matcher = CntParser.section_regex.matcher("");
        final Matcher tag_val_matcher = CntParser.tag_val.matcher("");
        String current_section = "";
        final Map<String, Object> headerData = new HashMap<String, Object>();
        final List<GraphSym> results = new ArrayList<GraphSym>();
        String line;
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            section_regex_matcher.reset(line);
            if (section_regex_matcher.matches()) {
                current_section = line;
                if ("[Header]".equals(current_section)) {
                    continue;
                }
                break;
            }
            else {
                if (!"[Header]".equals(current_section)) {
                    break;
                }
                tag_val_matcher.reset(line);
                if (!tag_val_matcher.matches()) {
                    continue;
                }
                final String tag = tag_val_matcher.group(1);
                final String val = tag_val_matcher.group(2);
                headerData.put(tag, val);
            }
        }
        String[] column_names = null;
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            section_regex_matcher.reset(line);
            if (section_regex_matcher.matches()) {
                current_section = line;
                if ("[ColumnName]".equals(current_section)) {
                    continue;
                }
                break;
            }
            else {
                if (!"[ColumnName]".equals(current_section)) {
                    break;
                }
                column_names = CntParser.line_regex.split(line);
            }
        }
        if (column_names == null) {
            throw new IOException("Column names were missing or malformed");
        }
        final int numScores = column_names.length - 3;
        if (numScores < 1) {
            throw new IOException("No score columns in file");
        }
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            final String[] fields = CntParser.line_regex.split(line);
            final int field_count = fields.length;
            if (field_count != column_names.length) {
                throw new IOException("Line has wrong number of data columns.");
            }
            final String seqid = fields[1];
            final int x = Integer.parseInt(fields[2]);
            BioSeq aseq = seq_group.getSeq(seqid);
            if (aseq == null) {
                aseq = seq_group.addSeq(seqid, x);
            }
            if (x > aseq.getLength()) {
                aseq.setLength(x);
            }
            final IntArrayList xVals = this.getXCoordsForSeq(aseq);
            xVals.add(x);
            final FloatArrayList[] floats = this.getFloatsForSeq(aseq, numScores);
            for (int j = 0; j < numScores; ++j) {
                final FloatArrayList floatList = floats[j];
                final float floatVal = parseFloat(fields[3 + j]);
                floatList.add(floatVal);
            }
        }
        for (final Map.Entry<String, IntArrayList> entry : this.seqToIntList.entrySet()) {
            final String seqid = entry.getKey();
            final IntArrayList x2 = entry.getValue();
            x2.trimToSize();
            final FloatArrayList[] ys = this.seq2Floats.get(seqid);
            final BioSeq seq = seq_group.getSeq(seqid);
            for (int i = 0; i < ys.length; ++i) {
                final FloatArrayList y = ys[i];
                y.trimToSize();
                String id = column_names[i + 3];
                if (!"ChipNum".equals(id)) {
                    id = this.getGraphIdForColumn(id, seq_group);
                    final GraphSym graf = new GraphSym(x2.elements(), y.elements(), id, seq);
                    if (annotateSeq) {
                        seq.addAnnotation(graf);
                    }
                    results.add(graf);
                }
            }
        }
        return results;
    }
    
    String getGraphIdForColumn(final String column_id, final AnnotatedSeqGroup seq_group) {
        String gid = this.unique_gids.get(column_id);
        if (gid == null) {
            gid = AnnotatedSeqGroup.getUniqueGraphID(column_id, seq_group);
            this.unique_gids.put(column_id, gid);
        }
        return gid;
    }
    
    public static float parseFloat(final String s) {
        float val = 0.0f;
        try {
            val = Float.parseFloat(s);
        }
        catch (NumberFormatException nfe) {
            val = 0.0f;
        }
        return val;
    }
    
    FloatArrayList[] getFloatsForSeq(final BioSeq seq, final int numScores) {
        FloatArrayList[] floats = this.seq2Floats.get(seq.getID());
        if (floats == null) {
            floats = new FloatArrayList[numScores];
            for (int i = 0; i < numScores; ++i) {
                floats[i] = new FloatArrayList();
            }
            this.seq2Floats.put(seq.getID(), floats);
        }
        return floats;
    }
    
    IntArrayList getXCoordsForSeq(final BioSeq seq) {
        IntArrayList xcoords = this.seqToIntList.get(seq.getID());
        if (xcoords == null) {
            xcoords = new IntArrayList();
            this.seqToIntList.put(seq.getID(), xcoords);
        }
        return xcoords;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, group, annotate_seq);
    }
    
    static {
        tag_val = Pattern.compile("(.*)=(.*)");
        line_regex = Pattern.compile("\\t");
        section_regex = Pattern.compile("\\[.*\\]");
    }
}
