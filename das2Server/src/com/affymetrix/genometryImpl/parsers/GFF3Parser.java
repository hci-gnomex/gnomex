// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.Collections;
import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symloader.LineTrackerI;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.GFF3Sym;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class GFF3Parser implements Parser
{
    private static final boolean DEBUG = false;
    public static final int GFF3 = 3;
    public static final String GFF3_ID = "ID";
    public static final String GFF3_NAME = "Name";
    public static final String GFF3_ALIAS = "Alias";
    public static final String GFF3_PARENT = "Parent";
    public static final String GFF3_TARGET = "Target";
    public static final String GFF3_GAP = "Gap";
    public static final String GFF3_DERIVES_FROM = "Derives_from";
    public static final String GFF3_NOTE = "Note";
    public static final String GFF3_DBXREF = "Dbxref";
    public static final String GFF3_ONTOLOGY_TERM = "Ontology_term";
    private static final Pattern line_regex;
    private static final Pattern directive_version;
    private static final boolean use_track_lines = true;
    private final TrackLineParser track_line_parser;
    private static final Set<String> IGNORABLE_TYPES;
    private static final Set<String> seenTypes;
    private final Map<String, GFF3Sym> id2sym;
    public final List<GFF3Sym> symlist;
    boolean useDefaultSource;
    private final Set<String> bad_parents;
    
    public GFF3Parser() {
        this.track_line_parser = new TrackLineParser();
        this.id2sym = new HashMap<String, GFF3Sym>();
        this.symlist = new ArrayList<GFF3Sym>();
        this.useDefaultSource = true;
        this.bad_parents = new HashSet<String>();
    }
    
    public List<? extends SeqSymmetry> parse(final InputStream istr, final String default_source, final AnnotatedSeqGroup seq_group, final boolean annot_seq) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        this.parse(br, default_source, seq_group, annot_seq);
        return this.symlist;
    }
    
    public void parse(final BufferedReader br, final String default_source, final AnnotatedSeqGroup seq_group, final boolean annot_seq) throws IOException {
        final Iterator<String> it = new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public String next() {
                String line = null;
                try {
                    line = br.readLine();
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
        this.parse(it, default_source, seq_group, annot_seq, null);
    }
    
    public void parse(final Iterator<String> it, final String default_source, final AnnotatedSeqGroup seq_group, final boolean annot_seq, final LineTrackerI lineTracker) throws IOException {
        this.symlist.clear();
        int line_count = 0;
        String line = null;
        final List<GFF3Sym> all_syms = new ArrayList<GFF3Sym>();
        String track_name = null;
        final Thread thread = Thread.currentThread();
        while ((line = it.next()) != null && !thread.isInterrupted()) {
            if (lineTracker != null) {
                lineTracker.notifyReadLine(line.length());
            }
            if ("###".equals(line)) {
                continue;
            }
            if ("##FASTA".equals(line)) {
                break;
            }
            if (line.startsWith("##track")) {
                this.track_line_parser.parseTrackLine(line);
                TrackLineParser.createTrackStyle(this.track_line_parser.getCurrentTrackHash(), default_source, "gff3");
                track_name = this.track_line_parser.getCurrentTrackHash().get("name");
            }
            else if (line.startsWith("##")) {
                processDirective(line);
            }
            else {
                if (line.startsWith("#")) {
                    continue;
                }
                final String[] fields = GFF3Parser.line_regex.split(line);
                if (fields == null) {
                    continue;
                }
                if (fields.length < 8) {
                    continue;
                }
                ++line_count;
                final String seq_name = fields[0].intern();
                String source = fields[1].intern();
                if (this.useDefaultSource || ".".equals(source)) {
                    source = default_source;
                }
                final String feature_type = GFF3Sym.normalizeFeatureType(fields[2]);
                final int coord_a = Integer.parseInt(fields[3]);
                final int coord_b = Integer.parseInt(fields[4]);
                final String score_str = fields[5];
                final char strand_char = fields[6].charAt(0);
                final char frame_char = fields[7].charAt(0);
                String attributes_field = null;
                if (fields.length >= 9) {
                    attributes_field = new String(fields[8]);
                }
                float score = Float.NEGATIVE_INFINITY;
                if (!".".equals(score_str)) {
                    score = Float.parseFloat(score_str);
                }
                if ("chromosome".equals(feature_type)) {
                    seq_group.addSeq(seq_name, coord_b);
                }
                else if (GFF3Parser.IGNORABLE_TYPES.contains(feature_type.toLowerCase())) {
                    final String[] ids = GFF3Sym.getGFF3PropertyFromAttributes("ID", attributes_field);
                    if (ids.length > 0) {
                        this.bad_parents.add(ids[0]);
                    }
                    synchronized (GFF3Parser.seenTypes) {
                        if (!GFF3Parser.seenTypes.add(feature_type.toLowerCase())) {
                            continue;
                        }
                        System.out.println("Ignoring GFF3 type '" + feature_type + "'");
                    }
                }
                else {
                    BioSeq seq = seq_group.getSeq(seq_name);
                    if (seq == null) {
                        seq = seq_group.addSeq(seq_name, 0);
                    }
                    final int min = Math.min(coord_a, coord_b) - 1;
                    final int max = Math.max(coord_a, coord_b);
                    if (max > seq.getLength()) {
                        seq.setLength(max);
                    }
                    final SimpleSeqSpan span = new SimpleSeqSpan((strand_char != '-') ? min : max, (strand_char != '-') ? max : min, seq);
                    final String the_id = GFF3Sym.getIdFromGFF3Attributes(attributes_field);
                    final GFF3Sym old_sym = this.id2sym.get(the_id);
                    if (the_id == null || the_id.equals("null") || "-".equals(the_id)) {
                        final GFF3Sym sym = createSym(source, feature_type, score, frame_char, attributes_field, span, track_name);
                        all_syms.add(sym);
                    }
                    else if (old_sym == null) {
                        final GFF3Sym sym = createSym(source, feature_type, score, frame_char, attributes_field, span, track_name);
                        all_syms.add(sym);
                        this.id2sym.put(the_id, sym);
                    }
                    else {
                        old_sym.addSpan(span);
                    }
                }
            }
        }
        this.addToParent(all_syms, seq_group, this.symlist, annot_seq, this.id2sym);
        System.out.print("Finished parsing GFF3.");
        System.out.print("  line count: " + line_count);
        System.out.println("  result count: " + this.symlist.size());
    }
    
    private void addToParent(final List<GFF3Sym> all_syms, final AnnotatedSeqGroup seq_group, final List<GFF3Sym> results, final boolean annot_seq, final Map<String, GFF3Sym> id2sym) throws IOException {
        final Set<GFF3Sym> moreCdsSpans = new HashSet<GFF3Sym>();
        for (final GFF3Sym sym : all_syms) {
            final String[] parent_ids = GFF3Sym.getGFF3PropertyFromAttributes("Parent", sym.getAttributes());
            String id = sym.getID();
            if (id != null && !"-".equals(id) && id.length() != 0) {
                seq_group.addToIndex(id, sym);
            }
            else {
                id = seq_group.getUniqueID();
                sym.setID(id);
                seq_group.addToIndex(id, sym);
            }
            if (parent_ids.length == 0 || sym.getFeatureType().equals("TF_binding_site")) {
                results.add(sym);
            }
            else {
                for (int i = 0; i < parent_ids.length; ++i) {
                    final String parent_id = parent_ids[i];
                    if ("-".equals(parent_id)) {
                        throw new IOException("Parent ID cannot be '-'");
                    }
                    final GFF3Sym parent_sym = id2sym.get(parent_id);
                    if (this.bad_parents.contains(parent_id)) {
                        final String[] ids = GFF3Sym.getGFF3PropertyFromAttributes("ID", sym.getAttributes());
                        if (ids.length > 0) {
                            this.bad_parents.add(ids[0]);
                        }
                    }
                    else if (parent_sym == null) {
                        if (!this.bad_parents.contains(parent_id)) {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "No parent found with ID: {0}", parent_id);
                            this.bad_parents.add(parent_id);
                        }
                        this.addBadParent(sym);
                    }
                    else if (parent_sym == sym) {
                        if (!this.bad_parents.contains(parent_id)) {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Parent and child are the same for ID: {0}", parent_id);
                            this.bad_parents.add(parent_id);
                        }
                        this.addBadParent(sym);
                    }
                    else {
                        parent_sym.addChild(sym);
                        if (parent_sym.getCdsSpans().size() > 1) {
                            moreCdsSpans.add(parent_sym);
                        }
                    }
                }
            }
        }
        this.handleMultipleCDS(moreCdsSpans, results, annot_seq, id2sym);
    }
    
    private void handleMultipleCDS(final Set<GFF3Sym> moreCdsSpans, final List<GFF3Sym> results, final boolean annot_seq, final Map<String, GFF3Sym> id2sym) {
        for (final GFF3Sym parent_sym : moreCdsSpans) {
            final String[] top_parent_ids = GFF3Sym.getGFF3PropertyFromAttributes("Parent", parent_sym.getAttributes());
            if (top_parent_ids.length == 0) {
                final Map<String, List<SeqSymmetry>> cdsSpans = parent_sym.getCdsSpans();
                parent_sym.removeCdsSpans();
                for (final Map.Entry<String, List<SeqSymmetry>> entry : cdsSpans.entrySet()) {
                    final GFF3Sym clone = (GFF3Sym)parent_sym.clone();
                    for (final SeqSymmetry seqsym : entry.getValue()) {
                        clone.addChild(seqsym);
                    }
                    results.add(clone);
                    if (annot_seq) {
                        for (int i = 0; i < clone.getSpanCount(); ++i) {
                            final BioSeq seq = clone.getSpanSeq(i);
                            seq.addAnnotation(clone);
                        }
                    }
                }
            }
            else {
                for (int k = 0; k < top_parent_ids.length; ++k) {
                    final String top_parent_id = top_parent_ids[k];
                    final GFF3Sym top_parent_sym = id2sym.get(top_parent_id);
                    top_parent_sym.removeChild(parent_sym);
                    final Map<String, List<SeqSymmetry>> cdsSpans2 = parent_sym.getCdsSpans();
                    parent_sym.removeCdsSpans();
                    for (final Map.Entry<String, List<SeqSymmetry>> entry2 : cdsSpans2.entrySet()) {
                        final GFF3Sym clone2 = (GFF3Sym)parent_sym.clone();
                        for (final SeqSymmetry seqsym2 : entry2.getValue()) {
                            clone2.addChild(seqsym2);
                        }
                        top_parent_sym.addChild(clone2);
                    }
                }
            }
        }
    }
    
    private void addBadParent(final GFF3Sym sym) {
        final String[] ids = GFF3Sym.getGFF3PropertyFromAttributes("ID", sym.getAttributes());
        if (ids.length > 0) {
            this.bad_parents.add(ids[0]);
        }
    }
    
    static void processDirective(final String line) throws IOException {
        final Matcher m = GFF3Parser.directive_version.matcher(line);
        if (!m.matches()) {
            Logger.getLogger(GFF3Parser.class.getName()).log(Level.WARNING, "Didn''t recognize directive: {0}", line);
            return;
        }
        final String vstr = m.group(1).trim();
        if (!"3".equals(vstr)) {
            throw new IOException("The specified GFF version can not be processed by this parser: version = '" + vstr + "'");
        }
    }
    
    private static GFF3Sym createSym(final String source, final String feature_type, final float score, final char frame_char, final String attributes_field, final SimpleSeqSpan span, final String track_name) {
        final GFF3Sym sym = new GFF3Sym(source, feature_type, score, frame_char, attributes_field);
        sym.addSpan(span);
        if (track_name != null) {
            sym.setProperty("method", track_name);
        }
        else {
            sym.setProperty("method", source);
        }
        return sym;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.parse(is, uri, group, true);
        return null;
    }
    
    public void clear() {
        this.id2sym.clear();
        GFF3Parser.seenTypes.clear();
        this.symlist.clear();
        GFF3Parser.IGNORABLE_TYPES.clear();
    }
    
    static {
        line_regex = Pattern.compile("\\t");
        directive_version = Pattern.compile("##gff-version\\s+(.*)");
        seenTypes = Collections.synchronizedSet(new HashSet<String>());
        final Set<String> types = new HashSet<String>();
        types.add("protein");
        IGNORABLE_TYPES = Collections.unmodifiableSet((Set<? extends String>)types);
    }
}
