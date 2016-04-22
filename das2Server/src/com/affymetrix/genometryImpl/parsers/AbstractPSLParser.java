//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.util.Comparator;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetryConverter;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.Psl3Sym;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.List;
import com.affymetrix.genometryImpl.comparator.UcscPslComparator;

public abstract class AbstractPSLParser implements AnnotationWriter, IndexWriter, Parser
{
    private static final UcscPslComparator comp;
    static List<String> psl_pref_list;
    static List<String> link_psl_pref_list;
    static List<String> psl3_pref_list;
    boolean look_for_targets_in_query_group;
    boolean create_container_annot;
    boolean is_link_psl;
    public boolean DEBUG;
    static Pattern line_regex;
    static Pattern comma_regex;
    static Pattern tagval_regex;
    static Pattern non_digit;
    TrackLineParser track_line_parser;
    String track_name_prefix;
    private static String newLine;

    public AbstractPSLParser() {
        this.look_for_targets_in_query_group = false;
        this.create_container_annot = false;
        this.is_link_psl = false;
        this.DEBUG = false;
        this.track_line_parser = new TrackLineParser();
        this.track_name_prefix = null;
    }

    public void enableSharedQueryTarget(final boolean b) {
        this.look_for_targets_in_query_group = b;
    }

    public void setCreateContainerAnnot(final boolean b) {
        this.create_container_annot = b;
    }

    public void setIsLinkPsl(final boolean b) {
        this.is_link_psl = b;
    }

    public List<UcscPslSym> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup query_group, final AnnotatedSeqGroup target_group, final boolean annotate_query, final boolean annotate_target) throws IOException {
        return this.parse(istr, annot_type, query_group, target_group, null, annotate_query, annotate_target, false);
    }

    public List<UcscPslSym> parse(final InputStream istr, final String annot_type, AnnotatedSeqGroup query_group, AnnotatedSeqGroup target_group, AnnotatedSeqGroup other_group, final boolean annotate_query, final boolean annotate_target, final boolean annotate_other) throws IOException {
        if (this.DEBUG) {
            System.out.println("in PSLParser.parse(), create_container_annot: " + this.create_container_annot);
        }
        final List<UcscPslSym> results = new ArrayList<UcscPslSym>();
        if (query_group == null) {
            query_group = new AnnotatedSeqGroup("Query");
            query_group.setUseSynonyms(false);
        }
        if (target_group == null) {
            target_group = new AnnotatedSeqGroup("Target");
            target_group.setUseSynonyms(false);
        }
        if (other_group == null) {
            other_group = new AnnotatedSeqGroup("Other");
            other_group.setUseSynonyms(false);
        }
        boolean in_bottom_of_link_psl = false;
        final Map<BioSeq, Map<String, SimpleSymWithProps>> target2types = new HashMap<BioSeq, Map<String, SimpleSymWithProps>>();
        final Map<BioSeq, Map<String, SimpleSymWithProps>> query2types = new HashMap<BioSeq, Map<String, SimpleSymWithProps>>();
        final Map<BioSeq, Map<String, SimpleSymWithProps>> other2types = new HashMap<BioSeq, Map<String, SimpleSymWithProps>>();
        int line_count = 0;
        final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        String line = null;
        int childcount = 0;
        int total_annot_count = 0;
        int total_child_count = 0;
        String[] block_size_array = null;
        final Thread thread = Thread.currentThread();
        try {
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                ++line_count;
                if (line.trim().length() != 0 && !line.startsWith("#") && !line.startsWith("match\t")) {
                    if (line.startsWith("-------")) {
                        continue;
                    }
                    if (line.startsWith("track")) {
                        if (this.is_link_psl) {
                            final Map<String, String> track_props = this.track_line_parser.parseTrackLine(line, this.track_name_prefix);
                            final String track_name = track_props.get("name");
                            if (track_name == null || !track_name.endsWith("probesets")) {
                                continue;
                            }
                            in_bottom_of_link_psl = true;
                        }
                        else {
                            this.track_line_parser.parseTrackLine(line, this.track_name_prefix);
                            TrackLineParser.createTrackStyle(this.track_line_parser.getCurrentTrackHash(), annot_type, this.is_link_psl ? "link.psl" : "psl");
                        }
                    }
                    else {
                        final String[] fields = AbstractPSLParser.line_regex.split(line);
                        final String field0 = fields[0];
                        final boolean non_digits_present = AbstractPSLParser.non_digit.matcher(field0).find(0);
                        if (non_digits_present) {
                            continue;
                        }
                        int findex = 0;
                        findex = skipExtraBinField(findex, fields);
                        final int match = Integer.parseInt(fields[findex++]);
                        final int mismatch = Integer.parseInt(fields[findex++]);
                        final int repmatch = Integer.parseInt(fields[findex++]);
                        final int n_count = Integer.parseInt(fields[findex++]);
                        final int q_gap_count = Integer.parseInt(fields[findex++]);
                        final int q_gap_bases = Integer.parseInt(fields[findex++]);
                        final int t_gap_count = Integer.parseInt(fields[findex++]);
                        final int t_gap_bases = Integer.parseInt(fields[findex++]);
                        final String strandstring = fields[findex++];
                        boolean same_orientation = true;
                        boolean qforward = true;
                        boolean tforward = true;
                        if (strandstring.length() == 1) {
                            same_orientation = strandstring.equals("+");
                            qforward = (strandstring.charAt(0) == '+');
                            tforward = true;
                        }
                        else if (strandstring.length() == 2) {
                            same_orientation = (strandstring.equals("++") || strandstring.equals("--"));
                            qforward = (strandstring.charAt(0) == '+');
                            tforward = (strandstring.charAt(1) == '+');
                        }
                        else {
                            System.err.println("strand field longer than two characters! ==> " + strandstring);
                        }
                        final String qname = fields[findex++];
                        final int qsize = Integer.parseInt(fields[findex++]);
                        final int qmin = Integer.parseInt(fields[findex++]);
                        final int qmax = Integer.parseInt(fields[findex++]);
                        final String tname = fields[findex++];
                        final int tsize = Integer.parseInt(fields[findex++]);
                        final int tmin = Integer.parseInt(fields[findex++]);
                        final int tmax = Integer.parseInt(fields[findex++]);
                        final int blockcount = Integer.parseInt(fields[findex++]);
                        block_size_array = AbstractPSLParser.comma_regex.split(fields[findex++]);
                        final String[] q_start_array = AbstractPSLParser.comma_regex.split(fields[findex++]);
                        final String[] t_start_array = AbstractPSLParser.comma_regex.split(fields[findex++]);
                        childcount = block_size_array.length;
                        if (block_size_array.length == 0 || block_size_array[0] == null || block_size_array[0].length() == 0) {
                            System.err.println("PSLParser found problem with blockSizes list, skipping this line: ");
                            System.err.println(line);
                        }
                        else if (blockcount != block_size_array.length) {
                            System.err.println("PSLParser found disagreement over number of blocks, skipping this line: ");
                            System.err.println(line);
                        }
                        else {
                            final UcscPslSym sym = this.determineSym(query_group, qname, qsize, target_group, tname, in_bottom_of_link_psl, tsize, qforward, tforward, block_size_array, q_start_array, t_start_array, annot_type, fields, findex, childcount, other_group, match, mismatch, repmatch, n_count, q_gap_count, q_gap_bases, t_gap_count, t_gap_bases, same_orientation, qmin, qmax, tmin, tmax, blockcount, annotate_other, other2types, annotate_query, query2types, annotate_target, target2types);
                            ++total_annot_count;
                            total_child_count += sym.getChildCount();
                            results.add(sym);
                            if (!this.DEBUG || total_annot_count % 5000 != 0) {
                                continue;
                            }
                            System.out.println("current annot count: " + total_annot_count);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            final StringBuffer sb = new StringBuffer();
            sb.append("Error parsing PSL file\n");
            sb.append("line count: " + line_count + "\n");
            sb.append("child count: " + childcount + "\n");
            if (block_size_array != null && block_size_array.length != 0) {
                sb.append("block_size first element: **" + block_size_array[0] + "**\n");
            }
            final IOException ioe = new IOException(sb.toString());
            ioe.initCause(e);
            throw ioe;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
        if (this.DEBUG) {
            System.out.println("finished parsing PSL file, annot count: " + total_annot_count + ", child count: " + total_child_count);
        }
        return results;
    }

    private static int skipExtraBinField(int findex, final String[] fields) {
        final boolean includes_bin_field = fields[9].startsWith("+") || fields[9].startsWith("-");
        if (includes_bin_field) {
            ++findex;
        }
        return findex;
    }

    private static BioSeq determineSeq(final AnnotatedSeqGroup query_group, final String qname, final int qsize) {
        BioSeq qseq = query_group.getSeq(qname);
        if (qseq == null) {
            qseq = query_group.addSeq(new String(qname), qsize);
        }
        if (qseq.getLength() < qsize) {
            qseq.setLength(qsize);
        }
        return qseq;
    }

    private UcscPslSym determineSym(final AnnotatedSeqGroup query_group, final String qname, final int qsize, final AnnotatedSeqGroup target_group, final String tname, final boolean in_bottom_of_link_psl, final int tsize, final boolean qforward, final boolean tforward, final String[] block_size_array, final String[] q_start_array, final String[] t_start_array, final String annot_type, final String[] fields, int findex, final int childcount, final AnnotatedSeqGroup other_group, final int match, final int mismatch, final int repmatch, final int n_count, final int q_gap_count, final int q_gap_bases, final int t_gap_count, final int t_gap_bases, final boolean same_orientation, final int qmin, final int qmax, final int tmin, final int tmax, final int blockcount, final boolean annotate_other, final Map<BioSeq, Map<String, SimpleSymWithProps>> other2types, final boolean annotate_query, final Map<BioSeq, Map<String, SimpleSymWithProps>> query2types, final boolean annotate_target, final Map<BioSeq, Map<String, SimpleSymWithProps>> target2types) throws NumberFormatException {
        final BioSeq qseq = determineSeq(query_group, qname, qsize);
        BioSeq tseq = target_group.getSeq(tname);
        boolean shared_query_target = false;
        if (tseq == null) {
            if (this.look_for_targets_in_query_group && query_group.getSeq(tname) != null) {
                tseq = query_group.getSeq(tname);
                shared_query_target = true;
            }
            else if (this.look_for_targets_in_query_group && this.is_link_psl) {
                if (in_bottom_of_link_psl) {
                    tseq = query_group.addSeq(new String(tname), qsize);
                }
                else {
                    tseq = target_group.addSeq(new String(tname), qsize);
                }
            }
            else {
                tseq = target_group.addSeq(new String(tname), qsize);
            }
        }
        if (tseq.getLength() < tsize) {
            tseq.setLength(tsize);
        }
        final List<Object> child_arrays = calcChildren(qseq, tseq, qforward, tforward, block_size_array, q_start_array, t_start_array);
        final int[] blocksizes = (int[])child_arrays.get(0);
        final int[] qmins = (int[])child_arrays.get(1);
        final int[] tmins = (int[])child_arrays.get(2);
        String type = this.track_line_parser.getCurrentTrackHash().get("name");
        if (type == null) {
            type = annot_type;
        }
        UcscPslSym sym = null;
        final boolean is_psl3 = fields.length > findex && (fields[findex].equals("+") || fields[findex].equals("-"));
        if (is_psl3) {
            final String otherstrand_string = fields[findex++];
            final boolean other_same_orientation = otherstrand_string.equals("+");
            final String oname = fields[findex++];
            final int osize = Integer.parseInt(fields[findex++]);
            final int omin = Integer.parseInt(fields[findex++]);
            final int omax = Integer.parseInt(fields[findex++]);
            final String[] o_min_array = AbstractPSLParser.comma_regex.split(fields[findex++]);
            final int[] omins = new int[childcount];
            for (int i = 0; i < childcount; ++i) {
                omins[i] = Integer.parseInt(o_min_array[i]);
            }
            final BioSeq oseq = determineSeq(other_group, oname, osize);
            sym = new Psl3Sym(type, match, mismatch, repmatch, n_count, q_gap_count, q_gap_bases, t_gap_count, t_gap_bases, same_orientation, other_same_orientation, qseq, qmin, qmax, tseq, tmin, tmax, oseq, omin, omax, blockcount, blocksizes, qmins, tmins, omins);
            annotate(annotate_other, this.create_container_annot, this.is_link_psl, other2types, oseq, type, sym, is_psl3, other_group);
        }
        else {
            sym = new UcscPslSym(type, match, mismatch, repmatch, n_count, q_gap_count, q_gap_bases, t_gap_count, t_gap_bases, same_orientation, qseq, qmin, qmax, tseq, tmin, tmax, blockcount, blocksizes, qmins, tmins);
        }
        findExtraTagValues(fields, findex, sym);
        annotate(annotate_query, this.create_container_annot, this.is_link_psl, query2types, qseq, type, sym, is_psl3, query_group);
        annotateTarget(annotate_target || (shared_query_target && this.is_link_psl), this.create_container_annot, this.is_link_psl, target2types, tseq, type, sym, is_psl3, in_bottom_of_link_psl, target_group);
        return sym;
    }

    private static void findExtraTagValues(final String[] fields, final int findex, final UcscPslSym sym) {
        if (fields.length > findex) {
            for (int i = findex; i < fields.length; ++i) {
                final String field = fields[i];
                final String[] tagval = AbstractPSLParser.tagval_regex.split(field);
                if (tagval.length >= 2) {
                    final String tag = tagval[0];
                    final String val = tagval[1];
                    sym.setProperty(tag, val);
                }
            }
        }
    }

    private static void annotate(final boolean annotate, final boolean create_container_annot, final boolean is_link_psl, final Map<BioSeq, Map<String, SimpleSymWithProps>> str2types, final BioSeq seq, final String type, final UcscPslSym sym, final boolean is_psl3, final AnnotatedSeqGroup annGroup) {
        if (annotate) {
            if (create_container_annot) {
                createContainerAnnot(str2types, seq, type, sym, is_psl3, is_link_psl);
            }
            else {
                seq.addAnnotation(sym);
            }
            annGroup.addToIndex(sym.getID(), sym);
        }
    }

    private static void annotateTarget(final boolean annotate, final boolean create_container_annot, final boolean is_link_psl, final Map<BioSeq, Map<String, SimpleSymWithProps>> str2types, final BioSeq seq, final String type, final UcscPslSym sym, final boolean is_psl3, final boolean in_bottom_of_link_psl, final AnnotatedSeqGroup annGroup) {
        if (annotate) {
            if (create_container_annot) {
                createContainerAnnot(str2types, seq, type, sym, is_psl3, is_link_psl);
            }
            else {
                seq.addAnnotation(sym);
            }
            if (!in_bottom_of_link_psl) {
                annGroup.addToIndex(sym.getID(), sym);
            }
        }
    }

    private static void createContainerAnnot(final Map<BioSeq, Map<String, SimpleSymWithProps>> seq2types, final BioSeq seq, final String type, final SeqSymmetry sym, final boolean is_psl3, final boolean is_link) {
        Map<String, SimpleSymWithProps> type2csym = seq2types.get(seq);
        if (type2csym == null) {
            type2csym = new HashMap<String, SimpleSymWithProps>();
            seq2types.put(seq, type2csym);
        }
        SimpleSymWithProps parent_sym = type2csym.get(type);
        if (parent_sym == null) {
            parent_sym = new SimpleSymWithProps();
            parent_sym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
            parent_sym.setProperty("method", type);
            if (is_link) {
                parent_sym.setProperty("preferred_formats", AbstractPSLParser.link_psl_pref_list);
            }
            else if (is_psl3) {
                parent_sym.setProperty("preferred_formats", AbstractPSLParser.psl3_pref_list);
            }
            else {
                parent_sym.setProperty("preferred_formats", AbstractPSLParser.psl_pref_list);
            }
            parent_sym.setProperty("container sym", Boolean.TRUE);
            seq.addAnnotation(parent_sym);
            type2csym.put(type, parent_sym);
        }
        parent_sym.addChild(sym);
    }

    private static List<Object> calcChildren(final BioSeq qseq, final BioSeq tseq, final boolean qforward, final boolean tforward, final String[] blocksize_strings, final String[] qstart_strings, final String[] tstart_strings) {
        final int childCount = blocksize_strings.length;
        if (qstart_strings.length != childCount || tstart_strings.length != childCount) {
            System.out.println("array counts for block sizes, q starts, and t starts don't agree, skipping children");
            return null;
        }
        final int[] blocksizes = new int[childCount];
        final int[] qmins = new int[childCount];
        final int[] tmins = new int[childCount];
        if (childCount > 0) {
            final int qseq_length = qseq.getLength();
            final int tseq_length = tseq.getLength();
            if (qforward && tforward) {
                for (int i = 0; i < childCount; ++i) {
                    final int match_length = Integer.parseInt(blocksize_strings[i]);
                    final int qstart = Integer.parseInt(qstart_strings[i]);
                    final int tstart = Integer.parseInt(tstart_strings[i]);
                    blocksizes[i] = match_length;
                    qmins[i] = qstart;
                    tmins[i] = tstart;
                }
            }
            else if (!qforward && tforward) {
                for (int i = 0; i < childCount; ++i) {
                    final int string_index = childCount - i - 1;
                    final int match_length2 = Integer.parseInt(blocksize_strings[string_index]);
                    final int qstart2 = qseq_length - Integer.parseInt(qstart_strings[string_index]);
                    final int tstart2 = Integer.parseInt(tstart_strings[string_index]);
                    final int qend = qstart2 - match_length2;
                    blocksizes[i] = match_length2;
                    qmins[i] = qend;
                    tmins[i] = tstart2;
                }
            }
            else if (qforward && !tforward) {
                for (int i = 0; i < childCount; ++i) {
                    final int match_length = Integer.parseInt(blocksize_strings[i]);
                    final int qstart = Integer.parseInt(qstart_strings[i]);
                    final int tstart = tseq_length - Integer.parseInt(tstart_strings[i]);
                    final int tend = tstart - match_length;
                    blocksizes[i] = match_length;
                    qmins[i] = qstart;
                    tmins[i] = tend;
                }
            }
            else {
                for (int i = 0; i < childCount; ++i) {
                    final int string_index = childCount - i - 1;
                    final int match_length2 = Integer.parseInt(blocksize_strings[string_index]);
                    final int qstart2 = qseq_length - Integer.parseInt(qstart_strings[string_index]);
                    final int tstart2 = tseq_length - Integer.parseInt(tstart_strings[string_index]);
                    final int qend = qstart2 - match_length2;
                    final int tend2 = tstart2 - match_length2;
                    blocksizes[i] = match_length2;
                    qmins[i] = qend;
                    tmins[i] = tend2;
                }
            }
        }
        final List<Object> results = new ArrayList<Object>(3);
        results.add(blocksizes);
        results.add(qmins);
        results.add(tmins);
        return results;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        return this.writeAnnotations(syms, seq, false, type, null, outstream);
    }

    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final boolean writeTrackLines, final String type, final String description, final OutputStream outstream) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            if (writeTrackLines) {
                dos.write(trackLine(type, description).getBytes());
            }
            for (SeqSymmetry sym : syms) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (!(sym instanceof UcscPslSym)) {
                    final int spancount = sym.getSpanCount();
                    if (spancount == 1) {
                        sym = SeqSymmetryConverter.convertToPslSym(sym, type, seq);
                    }
                    else {
                        final BioSeq seq2 = SeqUtils.getOtherSeq(sym, seq);
                        sym = SeqSymmetryConverter.convertToPslSym(sym, type, seq2, seq);
                    }
                }
                this.writeSymmetry(sym, seq, dos);
            }
            dos.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static String trackLine(final String type, final String description) {
        String trackLine = "track";
        if (type != null) {
            trackLine = trackLine + " name=\"" + type + "\"";
        }
        if (description != null) {
            trackLine = trackLine + " description=\"" + description + "\"";
        }
        trackLine += AbstractPSLParser.newLine;
        return trackLine;
    }

    @Override
    public Comparator<UcscPslSym> getComparator(final BioSeq seq) {
        return AbstractPSLParser.comp;
    }

    @Override
    public void writeSymmetry(final SeqSymmetry sym, final BioSeq seq, final OutputStream os) throws IOException {
        DataOutputStream dos = null;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream)os;
        }
        else {
            dos = new DataOutputStream(os);
        }
        ((UcscPslSym)sym).outputPslFormat(dos);
    }

    @Override
    public int getMin(final SeqSymmetry sym, final BioSeq seq) {
        return ((UcscPslSym)sym).getTargetMin();
    }

    @Override
    public int getMax(final SeqSymmetry sym, final BioSeq seq) {
        return ((UcscPslSym)sym).getTargetMax();
    }

    @Override
    public List<String> getFormatPrefList() {
        if (this.is_link_psl) {
            return AbstractPSLParser.link_psl_pref_list;
        }
        return AbstractPSLParser.psl_pref_list;
    }

    @Override
    public List<UcscPslSym> parse(final DataInputStream dis, final String annot_type, final AnnotatedSeqGroup group) {
        try {
            return this.parse(dis, annot_type, null, group, null, false, false, false);
        }
        catch (IOException ex) {
            Logger.getLogger(AbstractPSLParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void setTrackNamePrefix(final String prefix) {
        this.track_name_prefix = prefix;
    }

    public String getTrackNamePrefix() {
        return this.track_name_prefix;
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    static {
        comp = new UcscPslComparator();
        AbstractPSLParser.psl_pref_list = Arrays.asList("psl");
        AbstractPSLParser.link_psl_pref_list = Arrays.asList("link.psl", "bps", "psl");
        AbstractPSLParser.psl3_pref_list = Arrays.asList("psl3", "bps", "psl");
        AbstractPSLParser.line_regex = Pattern.compile("\t");
        AbstractPSLParser.comma_regex = Pattern.compile(",");
        AbstractPSLParser.tagval_regex = Pattern.compile("=");
        AbstractPSLParser.non_digit = Pattern.compile("[^0-9-]");
        AbstractPSLParser.newLine = System.getProperty("line.separator");
    }
}
