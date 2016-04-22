//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.SeqSymStartComparator;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.symmetry.UcscGffSym;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Set;

public final class GFFParser implements AnnotationWriter, Parser
{
    public static final int VERSION_UNKNOWN = 0;
    public static final int GFF1 = 1;
    public static final int GFF2 = 2;
    public static final int GFF3 = 3;
    public static final int GTF = 201;
    public static final String GFF3_ID = "ID";
    public static final String GFF3_PARENT = "Parent";
    static List pref_list;
    int gff_version;
    private static final boolean DEBUG = false;
    boolean DEBUG_GROUPING;
    boolean USE_FILTER;
    boolean USE_GROUPING;
    boolean useDefaultSource;
    boolean use_standard_filters;
    boolean gff_base1;
    static final Pattern line_regex;
    static final Pattern att_regex;
    static final Pattern tag_regex;
    static final Pattern value_regex;
    static final Pattern gff3_tagval_splitter;
    static final Pattern gff3_multival_splitter;
    Map<String, String> fail_filter_hash;
    Map<String, String> pass_filter_hash;
    Map<String, Object> gff3_id_hash;
    String group_tag;
    String group_id_field_name;
    String id_tag;
    TrackLineParser track_line_parser;
    boolean GROUP_ID_TO_LOWER_CASE;
    boolean use_first_one_as_group;
    boolean use_track_lines;
    static final Pattern directive_version;
    static final Pattern directive_filter;
    static final Pattern directive_hierarchy;
    static final Pattern directive_group_by;
    static final Pattern directive_group_from_first;
    static final Pattern directive_index_field;
    boolean use_hierarchy;
    Map<String, Integer> hierarchy_levels;
    Map<String, String> hierarchy_id_fields;
    static final Integer TWO;
    int number_of_duplicate_warnings;

    public GFFParser() {
        this(true);
    }

    public GFFParser(final boolean coords_are_base1) {
        this.gff_version = 0;
        this.DEBUG_GROUPING = false;
        this.USE_FILTER = true;
        this.USE_GROUPING = true;
        this.useDefaultSource = true;
        this.use_standard_filters = false;
        this.gff_base1 = true;
        this.fail_filter_hash = null;
        this.pass_filter_hash = null;
        this.gff3_id_hash = new HashMap<String, Object>();
        this.group_tag = null;
        this.group_id_field_name = null;
        this.id_tag = null;
        this.track_line_parser = new TrackLineParser();
        this.GROUP_ID_TO_LOWER_CASE = false;
        this.use_first_one_as_group = false;
        this.use_track_lines = true;
        this.use_hierarchy = false;
        this.hierarchy_levels = new HashMap<String, Integer>();
        this.hierarchy_id_fields = new HashMap<String, String>();
        this.number_of_duplicate_warnings = 0;
        this.gff_base1 = coords_are_base1;
    }

    public void addFeatureFilter(final String feature_type) {
        this.addFeatureFilter(feature_type, false);
    }

    public void addFeatureFilter(final String feature_type, final boolean pass_filter) {
        if (pass_filter) {
            if (this.pass_filter_hash == null) {
                this.pass_filter_hash = new HashMap<String, String>();
            }
            this.pass_filter_hash.put(feature_type, feature_type);
        }
        else {
            if (this.fail_filter_hash == null) {
                this.fail_filter_hash = new HashMap<String, String>();
            }
            this.fail_filter_hash.put(feature_type, feature_type);
        }
    }

    public void removeFeatureFilter(final String feature_type) {
        this.removeFeatureFilter(feature_type, false);
    }

    public void removeFeatureFilter(final String feature_type, final boolean pass_filter) {
        if (pass_filter) {
            if (this.pass_filter_hash != null) {
                this.pass_filter_hash.remove(feature_type);
                if (this.pass_filter_hash.size() == 0) {
                    this.pass_filter_hash = null;
                }
            }
        }
        else if (this.fail_filter_hash != null) {
            this.fail_filter_hash.remove(feature_type);
            if (this.fail_filter_hash.size() == 0) {
                this.fail_filter_hash = null;
            }
        }
    }

    public void resetFilters() {
        this.pass_filter_hash = null;
        this.fail_filter_hash = null;
    }

    public void setGroupTag(final String tag) {
        this.group_tag = tag;
    }

    public void setIdTag(final String tag) {
        this.id_tag = tag;
    }

    public List<? extends SeqSymmetry> parse(final InputStream istr, final AnnotatedSeqGroup seq_group, final boolean create_container_annot) throws IOException {
        return this.parse(istr, ".", seq_group, create_container_annot);
    }

    public List<? extends SeqSymmetry> parse(final InputStream istr, final String default_source, final AnnotatedSeqGroup seq_group, final boolean create_container_annot) throws IOException {
        return this.parse(istr, default_source, seq_group, create_container_annot, true);
    }

    public List<? extends SeqSymmetry> parse(final InputStream istr, final String default_source, final AnnotatedSeqGroup seq_group, final boolean create_container_annot, final boolean annotate_seq) throws IOException {
        int line_count = 0;
        int sym_count = 0;
        int group_count = 0;
        this.number_of_duplicate_warnings = 0;
        final Map<BioSeq, Map<String, SimpleSymWithProps>> seq2meths = new HashMap<BioSeq, Map<String, SimpleSymWithProps>>();
        final Map<String, SingletonSymWithProps> group_hash = new HashMap<String, SingletonSymWithProps>();
        this.gff3_id_hash = new HashMap<String, Object>();
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        this.use_hierarchy = false;
        this.hierarchy_levels.clear();
        final int current_h_level = 0;
        final UcscGffSym[] hier_parents = null;
        final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        String line = null;
        String track_name = null;
        try {
            final Thread thread = Thread.currentThread();
            while (!thread.isInterrupted() && (line = br.readLine()) != null) {
                if (line == null) {
                    continue;
                }
                if (line.startsWith("##")) {
                    this.processDirective(line);
                    if (this.gff_version != 3) {
                        continue;
                    }
                    if (line_count > 0) {
                        throw new IOException("You can only use the '##gff-version' parameter at the beginning of the file");
                    }
                    final GFF3Parser gff3_parser = new GFF3Parser();
                    gff3_parser.parse(br, default_source, seq_group, annotate_seq);
                    return gff3_parser.symlist;
                }
                else {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.startsWith("track")) {
                        this.track_line_parser.parseTrackLine(line);
                        TrackLineParser.createTrackStyle(this.track_line_parser.getCurrentTrackHash(), default_source, "gff");
                        track_name = this.track_line_parser.getCurrentTrackHash().get("name");
                    }
                    else {
                        final String[] fields = GFFParser.line_regex.split(line);
                        if (fields == null || fields.length < 8) {
                            continue;
                        }
                        ++line_count;
                        final String feature_type = fields[2].intern();
                        if (this.USE_FILTER && this.fail_filter_hash != null && this.fail_filter_hash.get(feature_type) != null) {
                            continue;
                        }
                        if (this.USE_FILTER && this.pass_filter_hash != null && this.pass_filter_hash.get(feature_type) == null) {
                            continue;
                        }
                        final String seq_name = fields[0].intern();
                        String source = fields[1].intern();
                        if (this.useDefaultSource || ".".equals(source)) {
                            source = default_source;
                        }
                        final int coord_a = Integer.parseInt(fields[3]);
                        final int coord_b = Integer.parseInt(fields[4]);
                        final String score_str = fields[5];
                        final String strand_str = fields[6].intern();
                        final String frame_str = fields[7].intern();
                        String last_field = null;
                        if (fields.length >= 9) {
                            last_field = new String(fields[8]);
                        }
                        float score = Float.NEGATIVE_INFINITY;
                        if (!score_str.equals(".")) {
                            score = Float.parseFloat(score_str);
                        }
                        BioSeq seq = seq_group.getSeq(seq_name);
                        if (seq == null) {
                            seq = seq_group.addSeq(seq_name, 0, default_source);
                        }
                        if (this.gff_version == 3) {
                            last_field = this.hackGff3GroupId(last_field);
                        }
                        final UcscGffSym sym = new UcscGffSym(seq, source, feature_type, coord_a, coord_b, score, strand_str.charAt(0), frame_str.charAt(0), last_field, this.gff_base1);
                        if (this.use_track_lines && track_name != null) {
                            sym.setProperty("method", track_name);
                        }
                        else {
                            sym.setProperty("method", source);
                        }
                        final int max = sym.getMax();
                        if (max > seq.getLength()) {
                            seq.setLength(max);
                        }
                        if (this.use_hierarchy) {
                            this.useHierarchy(hier_parents, feature_type, current_h_level, line, sym, results);
                        }
                        else if (this.USE_GROUPING) {
                            group_count = this.useGrouping(sym, results, group_hash, source, track_name, group_count, seq_group);
                        }
                        else {
                            results.add(sym);
                        }
                        ++sym_count;
                    }
                }
            }
        }
        finally {
            GeneralUtils.safeClose(br);
        }
        this.hierarchy_levels.clear();
        this.addSymstoSeq(results, create_container_annot, seq2meths, annotate_seq);
        System.out.println("lines: " + line_count + " syms:" + sym_count + " groups:" + group_count + " results:" + results.size());
        return results;
    }

    private void useHierarchy(UcscGffSym[] hier_parents, final String feature_type, int current_h_level, final String line, final UcscGffSym sym, final List<SeqSymmetry> results) throws RuntimeException {
        if (hier_parents == null) {
            hier_parents = new UcscGffSym[this.hierarchy_levels.size()];
        }
        final Integer new_h_level_int = this.hierarchy_levels.get(feature_type);
        if (new_h_level_int == null) {
            throw new RuntimeException("Hierarchy exception: unknown feature type: " + feature_type);
        }
        final int new_h_level = new_h_level_int;
        if (new_h_level - current_h_level > 1) {
            throw new RuntimeException("Hierarchy exception: skipped a level: " + current_h_level + " -> " + new_h_level + ":\n" + line + "\n");
        }
        final String id_field = this.hierarchy_id_fields.get(feature_type);
        if (id_field != null) {
            final String group_id = this.determineGroupId(sym, id_field);
            if (group_id != null) {
                sym.setProperty("id", group_id);
            }
        }
        hier_parents[new_h_level] = sym;
        if (new_h_level == 0) {
            results.add(sym);
        }
        else {
            final UcscGffSym the_parent = hier_parents[new_h_level - 1];
            if (the_parent == null) {
                throw new RuntimeException("Hierarchy exception: no parent");
            }
            the_parent.addChild(sym);
        }
        current_h_level = new_h_level;
    }

    private int useGrouping(final UcscGffSym sym, final List<SeqSymmetry> results, final Map<String, SingletonSymWithProps> group_hash, final String source, final String track_name, int group_count, final AnnotatedSeqGroup seq_group) {
        String group_id = null;
        if (sym.isGFF1()) {
            group_id = sym.getGroup();
        }
        else if (this.group_tag != null) {
            group_id = this.determineGroupId(sym, this.group_tag);
        }
        if (group_id == null) {
            results.add(sym);
        }
        else {
            if (this.DEBUG_GROUPING) {
                System.out.println(group_id);
            }
            SingletonSymWithProps groupsym = group_hash.get(group_id);
            if (groupsym == null) {
                if (this.use_first_one_as_group) {
                    groupsym = sym;
                }
                else {
                    groupsym = new SingletonSymWithProps(sym.getStart(), sym.getEnd(), sym.getBioSeq());
                    groupsym.addChild(sym);
                    groupsym.setProperty("group", group_id);
                    groupsym.setProperty("source", source);
                    if (this.use_track_lines && track_name != null) {
                        groupsym.setProperty("method", track_name);
                    }
                    else {
                        groupsym.setProperty("method", source);
                    }
                }
                ++group_count;
                String index_id = null;
                if (this.group_id_field_name != null) {
                    index_id = (String)sym.getProperty(this.group_id_field_name);
                }
                if (index_id != null) {
                    groupsym.setProperty("id", index_id);
                    if (seq_group != null) {
                        seq_group.addToIndex(index_id, groupsym);
                    }
                }
                else {
                    groupsym.setProperty("id", group_id);
                    if (seq_group != null) {
                        seq_group.addToIndex(group_id, groupsym);
                    }
                }
                group_hash.put(group_id, groupsym);
                results.add(groupsym);
            }
            else {
                groupsym.addChild(sym);
            }
        }
        return group_count;
    }

	private void addSymstoSeq(List<SeqSymmetry> results, boolean create_container_annot, Map<BioSeq, Map<String, SimpleSymWithProps>> seq2meths, boolean annotate_seq) {
		// Loop through the results List and add all Sym's to the BioSeq
		Iterator iter = results.iterator();
		while (iter.hasNext()) {
			SingletonSymWithProps sym = (SingletonSymWithProps) iter.next();
			BioSeq seq = sym.getBioSeq();
			if (USE_GROUPING && sym.getChildCount() > 0) {
				// stretch sym to bounds of all children
				SeqSpan pspan = SeqUtils.getChildBounds(sym, seq);
				// SeqSpan pspan = SeqUtils.getLeafBounds(sym, seq);  // alternative that does full recursion...
				sym.setCoords(pspan.getStart(), pspan.getEnd());
				resortChildren( sym, seq);
			}
			if (create_container_annot) {
				String meth = (String) sym.getProperty("method");
				SimpleSymWithProps parent_sym = getContainer(seq2meths, seq, meth, annotate_seq);
				parent_sym.addChild(sym);
			} else {
				if (annotate_seq) {
					seq.addAnnotation(sym);
				}
			}
		}
	}

/*
    private void addSymstoSeq(final List<SeqSymmetry> results, final boolean create_container_annot, final Map<BioSeq, Map<String, SimpleSymWithProps>> seq2meths, final boolean annotate_seq) {
        for (final SingletonSymWithProps sym : (Set<SingletonSymWithProps>) results) {
            final BioSeq seq = sym.getBioSeq();
            if (this.USE_GROUPING && sym.getChildCount() > 0) {
                final SeqSpan pspan = SeqUtils.getChildBounds(sym, seq);
                sym.setCoords(pspan.getStart(), pspan.getEnd());
                resortChildren(sym, seq);
            }
            if (create_container_annot) {
                final String meth = (String)sym.getProperty("method");
                final SimpleSymWithProps parent_sym = getContainer(seq2meths, seq, meth, annotate_seq);
                parent_sym.addChild(sym);
            }
            else {
                if (!annotate_seq) {
                    continue;
                }
                seq.addAnnotation(sym);
            }
        }
    }
*/
    static SimpleSymWithProps getContainer(final Map<BioSeq, Map<String, SimpleSymWithProps>> seq2meths, final BioSeq seq, final String meth, final boolean annotate_seq) {
        Map<String, SimpleSymWithProps> meth2csym = seq2meths.get(seq);
        if (meth2csym == null) {
            meth2csym = new HashMap<String, SimpleSymWithProps>();
            seq2meths.put(seq, meth2csym);
        }
        SimpleSymWithProps parent_sym = meth2csym.get(meth);
        if (parent_sym == null) {
            parent_sym = new SimpleSymWithProps();
            parent_sym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
            parent_sym.setProperty("method", meth);
            parent_sym.setProperty("preferred_formats", GFFParser.pref_list);
            parent_sym.setProperty("container sym", Boolean.TRUE);
            if (annotate_seq) {
                seq.addAnnotation(parent_sym);
            }
            meth2csym.put(meth, parent_sym);
        }
        return parent_sym;
    }

    public static void resortChildren(final MutableSeqSymmetry psym, final BioSeq sortseq) {
        final SeqSpan pspan = psym.getSpan(sortseq);
        final boolean ascending = pspan.isForward();
        final int child_count = psym.getChildCount();
        if (child_count > 0) {
            final List<SeqSymmetry> child_list = new ArrayList<SeqSymmetry>(child_count);
            for (int i = 0; i < child_count; ++i) {
                final SeqSymmetry csym = psym.getChild(i);
                if (csym.getSpan(sortseq) != null) {
                    child_list.add(psym.getChild(i));
                }
            }
            psym.removeChildren();
            final Comparator<SeqSymmetry> comp = new SeqSymStartComparator(sortseq, ascending);
            Collections.sort(child_list, comp);
            for (final SeqSymmetry child : child_list) {
                psym.addChild(child);
            }
        }
    }

    void processDirective(final String line) throws IOException {
        Matcher m = GFFParser.directive_version.matcher(line);
        if (m.matches()) {
            final String vstr = m.group(1).trim();
            try {
                final int vers = (int)Float.parseFloat(vstr);
                this.setGffVersion(vers);
            }
            catch (Exception ex) {
                System.err.println("could not parse \"##gff-version\" pragma line: " + line);
                ex.printStackTrace();
            }
            return;
        }
        m = GFFParser.directive_filter.matcher(line);
        if (m.matches()) {
            this.resetFilters();
            final String[] feature_types = m.group(2).split(" ");
            for (int i = 0; i < feature_types.length; ++i) {
                final String feature_type = feature_types[i].trim();
                if (feature_type.length() > 0) {
                    this.addFeatureFilter(feature_type, "include ".equals(m.group(1)));
                }
            }
            return;
        }
        m = GFFParser.directive_group_by.matcher(line);
        if (m.matches()) {
            final String group = m.group(1).trim();
            if (group.length() > 0) {
                this.setGroupTag(group);
            }
            else {
                this.setGroupTag(null);
            }
            return;
        }
        m = GFFParser.directive_group_from_first.matcher(line);
        if (m.matches()) {
            final String true_false = m.group(1).trim();
            this.use_first_one_as_group = "true".equals(true_false);
            return;
        }
        m = GFFParser.directive_index_field.matcher(line);
        if (m.matches()) {
            this.group_id_field_name = m.group(1).trim();
            return;
        }
        m = GFFParser.directive_hierarchy.matcher(line);
        if (!m.matches()) {
            if (line.startsWith("##IGB")) {
                System.out.println("WARNING: GFF/GTF processing directive not understood: '" + line + "'");
            }
            return;
        }
        if (!this.use_hierarchy) {
            this.resetFilters();
        }
        final String hierarchy_string = m.group(1).trim();
        final Pattern p = Pattern.compile("\\s*([0-9]+)\\s*(\\S*)(\\s*<(\\S*)>)?");
        final Matcher mm = p.matcher(hierarchy_string);
        while (mm.find()) {
            final String level_string = mm.group(1);
            final String feature_type2 = mm.group(2);
            final Integer level = Integer.valueOf(level_string);
            this.hierarchy_levels.put(feature_type2, level);
            this.addFeatureFilter(feature_type2, true);
            final String id_field = mm.group(4);
            if (id_field != null) {
                this.hierarchy_id_fields.put(feature_type2, id_field);
            }
        }
        if (this.hierarchy_levels.isEmpty()) {
            throw new IOException("The '##IGB-filter-hierarchy' directive could not be parsed");
        }
        this.use_hierarchy = true;
    }

    public static void processAttributes(final Map<String, Object> m, final String attributes) {
        List<String> vals = new ArrayList<String>();
        final String[] attarray = GFFParser.att_regex.split(attributes);
        for (int i = 0; i < attarray.length; ++i) {
            final String att = attarray[i];
            final Matcher tag_matcher = GFFParser.tag_regex.matcher(att);
            if (tag_matcher.find()) {
                final String tag = tag_matcher.group(1);
                final int index = tag_matcher.end(1);
                final Matcher value_matcher = GFFParser.value_regex.matcher(att);
                for (boolean matches = value_matcher.find(index); matches; matches = value_matcher.find()) {
                    final String group1 = value_matcher.group(1);
                    final String group2 = value_matcher.group(2);
                    if (group1 != null) {
                        vals.add(group1);
                    }
                    else {
                        vals.add(group2);
                    }
                }
                if (vals.size() == 1) {
                    final Object the_object = vals.get(0);
                    m.put(tag, the_object);
                    vals.clear();
                }
                else if (vals.size() == 0) {
                    m.put(tag, tag);
                    vals.clear();
                }
                else {
                    m.put(tag, vals);
                    vals = new ArrayList<String>();
                }
            }
        }
    }

    public void setUseStandardFilters(final boolean b) {
        this.addFeatureFilter("intron");
        this.addFeatureFilter("splice3");
        this.addFeatureFilter("splice5");
        this.addFeatureFilter("splice_donor");
        this.addFeatureFilter("splice_acceptor");
        this.addFeatureFilter("prim_trans");
        this.addFeatureFilter("transcript");
        this.addFeatureFilter("gene");
        this.addFeatureFilter("cluster");
        this.addFeatureFilter("psr");
        this.addFeatureFilter("link");
        this.addFeatureFilter("chromosome");
        this.setGroupTag("transcript_id");
    }

    public void setGffVersion(final int version) {
        this.gff_version = version;
        if (this.gff_version != 3) {
            this.setUseStandardFilters(this.use_standard_filters);
        }
    }

    public String hackGff3GroupId(final String atts) {
        String groupid = null;
        String featid = null;
        final String[] tagvals = GFFParser.att_regex.split(atts);
        for (int i = 0; i < tagvals.length; ++i) {
            final String tagval = tagvals[i];
            final String[] tv = GFFParser.gff3_tagval_splitter.split(tagval);
            final String tag = tv[0];
            final String val = tv[1];
            if (tag.equals("Parent")) {
                groupid = val;
            }
            else if (tag.equals("ID")) {
                featid = val;
                final Object obj = this.gff3_id_hash.get(featid);
                if (obj == null) {
                    this.gff3_id_hash.put(featid, featid);
                }
                else {
                    if (obj instanceof String) {
                        this.gff3_id_hash.put(featid, GFFParser.TWO);
                        featid += "_1";
                    }
                    else if (obj instanceof Integer) {
                        final Integer iobj = (Integer)obj;
                        final int fcount = iobj;
                        this.gff3_id_hash.put(featid, fcount + 1);
                        featid = featid + "_" + iobj.toString();
                    }
                    if (this.number_of_duplicate_warnings++ <= 10) {
                        System.out.println("duplicate feature id, new id: " + featid);
                        if (this.number_of_duplicate_warnings == 10) {
                            System.out.println("(Suppressing further warnings about duplicate ids");
                        }
                    }
                }
            }
        }
        if (groupid == null) {
            return featid;
        }
        return groupid;
    }

    public String determineGroupId(final SymWithProps sym, final String group_tag) {
        String group_id = null;
        if (group_tag != null) {
            if (this.gff_version == 3) {
                System.out.println("shouldn't get here, GFF3 should have been transformed to look like GFF1");
            }
            else {
                final Object value = sym.getProperty(group_tag);
                if (value != null) {
                    if (value instanceof String) {
                        group_id = (String)value;
                        if (this.GROUP_ID_TO_LOWER_CASE) {
                            group_id = group_id.toLowerCase();
                        }
                    }
                    else if (value instanceof Number) {
                        group_id = "" + value;
                    }
                    else if (value instanceof Character) {
                        group_id = "" + value;
                    }
                    else if (value instanceof List) {
                        final List valist = (List)value;
                        if (valist.size() > 0 && valist.get(0) instanceof String) {
                            group_id = (String) valist.get(0);
                            if (this.GROUP_ID_TO_LOWER_CASE) {
                                group_id = group_id.toLowerCase();
                            }
                        }
                    }
                }
            }
        }
        return group_id;
    }

    public static void outputGffFormat(final SymWithProps psym, final BioSeq seq, final Writer wr) throws IOException {
        final int childcount = psym.getChildCount();
        String meth = (String)psym.getProperty("source");
        if (meth == null) {
            meth = (String)psym.getProperty("type");
        }
        String group = (String)psym.getProperty("group");
        if (group == null) {
            group = psym.getID();
        }
        if (group == null) {
            group = (String)psym.getProperty("id");
        }
        for (int i = 0; i < childcount; ++i) {
            final SeqSymmetry csym = psym.getChild(i);
            final SeqSpan span = csym.getSpan(seq);
            wr.write(seq.getID());
            wr.write(9);
            if (meth != null) {
                wr.write(meth);
            }
            else {
                wr.write("unknown_source");
            }
            wr.write(9);
            String child_type = null;
            SymWithProps cwp = null;
            if (csym instanceof SymWithProps) {
                cwp = (SymWithProps)csym;
                child_type = (String)cwp.getProperty("type");
            }
            if (child_type != null) {
                wr.write(child_type);
            }
            else {
                wr.write("unknown_feature_type");
            }
            wr.write(9);
            wr.write(Integer.toString(span.getMin() + 1));
            wr.write(9);
            wr.write(Integer.toString(span.getMax()));
            wr.write(9);
            Object score = null;
            if (cwp != null) {
                score = cwp.getProperty("score");
            }
            if (score != null) {
                wr.write(score.toString());
            }
            else {
                wr.write(".");
            }
            wr.write(9);
            if (span.isForward()) {
                wr.write("+");
            }
            else {
                wr.write("-");
            }
            wr.write(9);
            Object frame = null;
            if (cwp != null) {
                frame = cwp.getProperty("frame");
            }
            if (frame != null) {
                wr.write(frame.toString());
            }
            else {
                wr.write(46);
            }
            wr.write(9);
            if (group != null) {
                wr.write(group);
            }
            wr.write(10);
        }
    }

    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final String type, final OutputStream outstream) {
        boolean success = true;
        int count = 0;
        try {
            final Writer bw = new BufferedWriter(new OutputStreamWriter(outstream));
            final Iterator iterator = syms.iterator();
            while (iterator.hasNext()) {
                ++count;
                final SeqSymmetry sym = (SeqSymmetry) iterator.next();
                final SeqSpan span = (SeqSpan) sym.getSpan(0);
                final BioSeq seq = (BioSeq) span.getBioSeq();
                if (sym instanceof SymWithProps) {
                    outputGffFormat((SymWithProps)sym, seq, bw);
                }
                else {
                    System.err.println("sym is not instance of SymWithProps");
                }
            }
            bw.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        System.out.println("total line count: " + count);
        return success;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        boolean success = true;
        try {
            final Writer bw = new BufferedWriter(new OutputStreamWriter(outstream));
            for (final SeqSymmetry sym : syms) {
                if (sym instanceof SymWithProps) {
                    outputGffFormat((SymWithProps)sym, seq, bw);
                }
                else {
                    System.err.println("sym is not instance of SymWithProps");
                }
            }
            bw.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    public void setUseDefaultSource(final boolean useDefaultSource) {
        this.useDefaultSource = useDefaultSource;
    }

    public void setUseTrackLines(final boolean use_track_lines) {
        this.use_track_lines = use_track_lines;
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        if (annotate_seq) {
            this.setUseStandardFilters(true);
        }
        return this.parse(is, uri, group, false, annotate_seq);
    }

    static {
        GFFParser.pref_list = Arrays.asList("gff");
        line_regex = Pattern.compile("\\t+");
        att_regex = Pattern.compile(";");
        tag_regex = Pattern.compile("^\\s*([\\w]+)\\s*");
        value_regex = Pattern.compile("\\s*\"([^\"]*)\"|\\s*([^\"\\s]\\S*)");
        gff3_tagval_splitter = Pattern.compile("=");
        gff3_multival_splitter = Pattern.compile(",");
        directive_version = Pattern.compile("##gff-version\\s+(.*)");
        directive_filter = Pattern.compile("##IGB-filter-(include |exclude |clear)(.*)");
        directive_hierarchy = Pattern.compile("##IGB-filter-hierarchy (.*)");
        directive_group_by = Pattern.compile("##IGB-group-by (.*)");
        directive_group_from_first = Pattern.compile("##IGB-group-properties-from-first-member (true|false)");
        directive_index_field = Pattern.compile("##IGB-group-id-field (.*)");
        TWO = 2;
    }
}
