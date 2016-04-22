// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import java.util.Set;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.regex.Matcher;
import java.util.Map;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.IOException;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.IndexedSingletonSym;
import java.util.ArrayList;
import java.util.HashMap;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.LinkedHashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.symmetry.ScoredContainerSym;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class ScoredIntervalParser implements GraphParser
{
    private static Pattern line_regex;
    private static Pattern tagval_regex;
    private static Pattern strand_regex;
    
    public List<ScoredContainerSym> parse(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final boolean annotate_seq) throws IOException {
        final String unique_container_name = AnnotatedSeqGroup.getUniqueGraphID(stream_name, seq_group);
        BufferedReader br = null;
        int line_count = 0;
        int score_count = 0;
        int hit_count = 0;
        int mod_hit_count = 0;
        int total_mod_hit_count = 0;
        int miss_count = 0;
        boolean all_sin3 = true;
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            String line = null;
            final Map<BioSeq, List<SinEntry>> seq2sinentries = new LinkedHashMap<BioSeq, List<SinEntry>>();
            final Map<Integer, String> index2id = new HashMap<Integer, String>();
            List<String> score_names = null;
            final Map<String, Object> props = new HashMap<String, Object>();
            line = parseHeader(br, line, index2id, props);
            final Matcher strand_matcher = ScoredIntervalParser.strand_regex.matcher("");
            final List<IndexedSingletonSym> isyms = new ArrayList<IndexedSingletonSym>();
            while (line != null) {
                isyms.clear();
                Label_0917: {
                    if (line.charAt(0) != '#') {
                        final String[] fields = ScoredIntervalParser.line_regex.split(line);
                        String annot_id = null;
                        String strand = null;
                        int score_offset;
                        if (fields.length > 3 && strand_matcher.reset(fields[3]).matches()) {
                            all_sin3 = false;
                            score_offset = 4;
                            annot_id = null;
                            final String seqid = fields[0];
                            final int min = Integer.parseInt(fields[1]);
                            final int max = Integer.parseInt(fields[2]);
                            strand = fields[3];
                            BioSeq aseq = seq_group.getSeq(seqid);
                            if (aseq == null) {
                                aseq = seq_group.addSeq(seqid, 0, stream_name);
                            }
                            IndexedSingletonSym child;
                            if (strand.equals("-")) {
                                child = new IndexedSingletonSym(max, min, aseq);
                            }
                            else {
                                child = new IndexedSingletonSym(min, max, aseq);
                            }
                            isyms.add(child);
                            if (max > aseq.getLength()) {
                                aseq.setLength(max);
                            }
                        }
                        else if (fields.length > 4 && strand_matcher.reset(fields[4]).matches()) {
                            all_sin3 = false;
                            score_offset = 5;
                            annot_id = fields[0];
                            final String seqid = fields[1];
                            final int min = Integer.parseInt(fields[2]);
                            final int max = Integer.parseInt(fields[3]);
                            strand = fields[4];
                            BioSeq aseq = seq_group.getSeq(seqid);
                            if (aseq == null) {
                                aseq = seq_group.addSeq(seqid, 0);
                            }
                            if (max > aseq.getLength()) {
                                aseq.setLength(max);
                            }
                            IndexedSingletonSym child;
                            if (strand.equals("-")) {
                                child = new IndexedSingletonSym(max, min, aseq);
                            }
                            else {
                                child = new IndexedSingletonSym(min, max, aseq);
                            }
                            child.setID(annot_id);
                            isyms.add(child);
                            seq_group.addToIndex(annot_id, child);
                        }
                        else {
                            score_offset = 1;
                            annot_id = fields[0];
                            final SeqSymmetry original_sym = findSym(seq_group, annot_id);
                            if (original_sym == null) {
                                SeqSymmetry mod_sym = findSym(seq_group, annot_id + ".0");
                                if (mod_sym == null) {
                                    ++miss_count;
                                    break Label_0917;
                                }
                                ++mod_hit_count;
                                for (int ext = 0; mod_sym != null; mod_sym = findSym(seq_group, annot_id + "." + ext)) {
                                    final SeqSpan span = mod_sym.getSpan(0);
                                    final IndexedSingletonSym child2 = new IndexedSingletonSym(span.getStart(), span.getEnd(), span.getBioSeq());
                                    child2.setID(mod_sym.getID());
                                    isyms.add(child2);
                                    ++total_mod_hit_count;
                                    ++ext;
                                }
                            }
                            else {
                                final SeqSpan span2 = original_sym.getSpan(0);
                                final IndexedSingletonSym child3 = new IndexedSingletonSym(span2.getStart(), span2.getEnd(), span2.getBioSeq());
                                child3.setID(original_sym.getID());
                                isyms.add(child3);
                                ++hit_count;
                            }
                        }
                        if (score_names == null) {
                            score_count = fields.length - score_offset;
                            score_names = initScoreNames(score_count, index2id, stream_name);
                        }
                        score_count = fields.length - score_offset;
                        final float[] entry_floats = new float[score_count];
                        int findex = 0;
                        for (int field_index = score_offset; field_index < fields.length; ++field_index) {
                            final float score = Float.parseFloat(fields[field_index]);
                            entry_floats[findex] = score;
                            ++findex;
                        }
                        for (final IndexedSingletonSym child4 : isyms) {
                            final BioSeq aseq2 = child4.getSpan(0).getBioSeq();
                            List<SinEntry> sin_entries = seq2sinentries.get(aseq2);
                            if (sin_entries == null) {
                                sin_entries = new ArrayList<SinEntry>();
                                seq2sinentries.put(aseq2, sin_entries);
                            }
                            final SinEntry sentry = new SinEntry(child4, entry_floats);
                            sin_entries.add(sentry);
                        }
                        ++line_count;
                    }
                }
                line = br.readLine();
            }
            final SinEntryComparator comp = new SinEntryComparator();
            for (final List<SinEntry> entry_list : seq2sinentries.values()) {
                Collections.sort(entry_list, comp);
            }
            System.out.println("number of scores per line: " + score_count);
            System.out.println("data lines in .sin file: " + line_count);
            if (hit_count + miss_count > 0) {
                System.out.println("sin3 miss count: " + miss_count);
                System.out.println("sin3 exact id hit count: " + hit_count);
            }
            if (mod_hit_count > 0) {
                System.out.println("sin3 extended id hit count: " + mod_hit_count);
            }
            if (total_mod_hit_count > 0) {
                System.out.println("sin3 total extended id hit count: " + total_mod_hit_count);
            }
            if (all_sin3 && hit_count == 0 && mod_hit_count == 0 && miss_count > 0) {
                throw new IOException("No data loaded. The ID's in the file did not match any ID's from data that has already been loaded.");
            }
            return createContainerSyms(seq2sinentries, props, unique_container_name, score_count, score_names, annotate_seq);
        }
        catch (Exception ex) {
            final IOException ioe = new IOException("Error while reading '.egr' or '.sin' file from: '" + stream_name + "'");
            ioe.initCause(ex);
            throw ioe;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
    }
    
    private static String parseHeader(final BufferedReader br, String line, final Map<Integer, String> index2id, final Map<String, Object> props) throws IOException {
        while ((line = br.readLine()) != null && (line.charAt(0) == '#' || line.charAt(0) == ' ' || line.charAt(0) == '\t')) {
            if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                System.out.println("skipping line starting with whitespace: " + line);
            }
            else {
                final Matcher match = ScoredIntervalParser.tagval_regex.matcher(line);
                if (!match.matches()) {
                    continue;
                }
                final String tag = match.group(1);
                final String val = match.group(2);
                if (tag.startsWith("score")) {
                    try {
                        final int score_index = Integer.parseInt(tag.substring(tag.indexOf("score") + 5));
                        index2id.put(score_index, val);
                        continue;
                    }
                    catch (NumberFormatException nfe) {
                        throw new IOException("Tag '" + tag + "' is not in the format score# where # = 0,1,2,....");
                    }
                }
                props.put(tag, val);
            }
        }
        return line;
    }
    
    private static List<ScoredContainerSym> createContainerSyms(final Map<BioSeq, List<SinEntry>> seq2sinentries, final Map<String, Object> props, final String unique_container_name, final int score_count, final List<String> score_names, final boolean annotate_seq) {
        final List<ScoredContainerSym> containerSyms = new ArrayList<ScoredContainerSym>(seq2sinentries.keySet().size());
        for (final BioSeq aseq : seq2sinentries.keySet()) {
            final ScoredContainerSym container = new ScoredContainerSym();
            container.addSpan(new SimpleSeqSpan(0, aseq.getLength(), aseq));
            for (final Map.Entry<String, Object> entry : props.entrySet()) {
                container.setProperty(entry.getKey(), entry.getValue());
            }
            container.setProperty("method", unique_container_name);
            container.setProperty("container sym", Boolean.TRUE);
            final ITrackStyleExtended style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(unique_container_name);
            style.setGlyphDepth(1);
            final List<SinEntry> entry_list = seq2sinentries.get(aseq);
            final int entry_count = entry_list.size();
            for (final SinEntry entry2 : entry_list) {
                container.addChild(entry2.sym);
            }
            for (int i = 0; i < score_count; ++i) {
                final String score_name = score_names.get(i);
                final float[] score_column = new float[entry_count];
                for (int k = 0; k < entry_count; ++k) {
                    final SinEntry sentry = entry_list.get(k);
                    score_column[k] = sentry.scores[i];
                }
                container.addScores(score_name, score_column);
            }
            container.setID(unique_container_name);
            containerSyms.add(container);
            if (annotate_seq) {
                aseq.addAnnotation(container);
            }
        }
        return containerSyms;
    }
    
    private static SeqSymmetry findSym(final AnnotatedSeqGroup seq_group, final String id) {
        final Set<SeqSymmetry> sym_list = seq_group.findSyms(id);
        if (sym_list.isEmpty()) {
            return null;
        }
        return sym_list.iterator().next();
    }
    
    private static List<String> initScoreNames(final int score_count, final Map<Integer, String> index2id, final String stream_name) {
        final List<String> names = new ArrayList<String>();
        for (int i = 0; i < score_count; ++i) {
            final Integer index = i;
            String id = index2id.get(index);
            if (id == null) {
                if (stream_name == null) {
                    id = "score" + i;
                }
                else if (score_count > 1) {
                    id = stream_name + ": score" + i;
                }
                else {
                    id = stream_name;
                }
            }
            names.add(id);
        }
        return names;
    }
    
    public static boolean writeEgrFormat(final GraphIntervalSym graf, final String genome_version, final OutputStream ostr) throws IOException {
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            final BioSeq seq = graf.getGraphSeq();
            final String seq_id = (seq == null) ? "." : seq.getID();
            final String human_name = graf.getGraphState().getTierStyle().getTrackName();
            if (genome_version != null) {
                dos.writeBytes("# genome_version = " + genome_version + '\n');
            }
            dos.writeBytes("# score0 = " + human_name + '\n');
            final Object strand_property = graf.getProperty("Graph Strand");
            char strand_char = '.';
            if (GraphSym.GRAPH_STRAND_PLUS.equals(strand_property)) {
                strand_char = '+';
            }
            else if (GraphSym.GRAPH_STRAND_MINUS.equals(strand_property)) {
                strand_char = '-';
            }
            else if (GraphSym.GRAPH_STRAND_BOTH.equals(strand_property)) {
                strand_char = '.';
            }
            writeGraphPoints(graf, dos, seq_id, strand_char);
            dos.flush();
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return true;
    }
    
    private static void writeGraphPoints(final GraphIntervalSym graf, final DataOutputStream dos, final String seq_id, final char strand_char) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            final int x2 = graf.getGraphXCoord(i) + graf.getGraphWidthCoord(i);
            dos.writeBytes(seq_id + '\t' + graf.getGraphXCoord(i) + '\t' + x2 + '\t' + strand_char + '\t' + graf.getGraphYCoordString(i) + '\n');
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, uri, group, annotate_seq);
    }
    
    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        return null;
    }
    
    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        if (gsym instanceof GraphIntervalSym) {
            BufferedOutputStream bos = null;
            try {
                String genome_name = null;
                if (seq_group != null) {
                    genome_name = seq_group.getID();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file_name));
                writeEgrFormat((GraphIntervalSym)gsym, genome_name, bos);
            }
            finally {
                GeneralUtils.safeClose(bos);
            }
            return;
        }
        throw new IOException("Not the correct graph type for the '.egr' format.");
    }
    
    static {
        ScoredIntervalParser.line_regex = Pattern.compile("\t");
        ScoredIntervalParser.tagval_regex = Pattern.compile("#\\s*([\\w]+)\\s*=\\s*(.*)$");
        ScoredIntervalParser.strand_regex = Pattern.compile("[\\+\\-\\.]");
    }
    
    private static final class SinEntry
    {
        SeqSymmetry sym;
        float[] scores;
        
        public SinEntry(final SeqSymmetry sym, final float[] scores) {
            this.sym = sym;
            this.scores = scores;
        }
    }
    
    private static final class SinEntryComparator implements Comparator<SinEntry>
    {
        @Override
        public int compare(final SinEntry objA, final SinEntry objB) {
            final SeqSpan symA = objA.sym.getSpan(0);
            final SeqSpan symB = objB.sym.getSpan(0);
            final int minA = symA.getMin();
            final int minB = symB.getMin();
            if (minA < minB) {
                return -1;
            }
            if (minA > minB) {
                return 1;
            }
            final int maxA = symA.getMax();
            final int maxB = symB.getMax();
            if (maxA < maxB) {
                return -1;
            }
            if (maxA > maxB) {
                return 1;
            }
            return 0;
        }
    }
}
