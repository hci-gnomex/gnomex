//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithProps;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;

public class TabDelimitedParser implements Parser
{
    private final int chromosome_col;
    private final int start_col;
    private final int end_col;
    private final int length_col;
    private final int strand_col;
    private final int group_col;
    private final int type_col;
    private int id_col;
    private boolean make_props;
    private boolean use_length;
    private boolean use_group;
    private boolean use_type;
    private boolean use_strand;
    private boolean has_header;
    private boolean has_id;
    private static final Pattern line_splitter;

    public TabDelimitedParser(final int type, final int chromosome, final int start, final int end, final int length, final int strand, final int group, final int id, final boolean props, final boolean header) {
        this.make_props = true;
        this.use_length = false;
        this.use_group = false;
        this.use_type = false;
        this.use_strand = false;
        this.has_header = false;
        this.has_id = false;
        if (chromosome < 0) {
            throw new IllegalArgumentException("Chromosome column number must be 0 or greater.");
        }
        this.chromosome_col = chromosome;
        this.start_col = start;
        this.end_col = end;
        this.length_col = length;
        this.group_col = group;
        this.type_col = type;
        this.strand_col = strand;
        this.id_col = id;
        this.has_header = header;
        this.use_length = (length >= 0);
        this.use_group = (group >= 0);
        this.use_type = (type >= 0);
        this.use_strand = (strand >= 0);
        this.has_id = (id >= 0);
        this.make_props = props;
    }

    public List<SeqSymmetry> parse(final InputStream istr, final String default_type, final AnnotatedSeqGroup seq_group, final boolean annotateSeq) {
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        final Map<String, SeqSymmetry> group_hash = new HashMap<String, SeqSymmetry>();
        final MutableSeqSpan union_span = new SimpleMutableSeqSpan();
        List<String> col_names = null;
        try {
            final InputStreamReader asr = new InputStreamReader(istr);
            final BufferedReader br = new BufferedReader(asr);
            if (this.has_header) {
                final String line = br.readLine();
                final String[] cols = TabDelimitedParser.line_splitter.split(line);
                col_names = new ArrayList<String>(cols.length);
                for (int i = 0; i < cols.length; ++i) {
                    col_names.add(cols[i]);
                }
            }
            String line;
            while ((line = br.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                final String[] cols = TabDelimitedParser.line_splitter.split(line);
                if (cols.length <= 0) {
                    continue;
                }
                final int start = Integer.parseInt(cols[this.start_col]);
                int end;
                if (this.use_length) {
                    final int length = Integer.parseInt(cols[this.length_col]);
                    if (this.use_strand) {
                        final String strand = cols[this.strand_col];
                        if (strand.equals("-")) {
                            end = start - length;
                        }
                        else {
                            end = start + length;
                        }
                    }
                    else {
                        end = start + length;
                    }
                }
                else {
                    end = Integer.parseInt(cols[this.end_col]);
                }
                String type = default_type;
                if (this.use_type) {
                    type = cols[this.type_col];
                }
                String id = null;
                if (this.has_id) {
                    id = cols[this.id_col];
                }
                final String chromName = cols[this.chromosome_col];
                BioSeq seq = seq_group.getSeq(chromName);
                if (seq == null) {
                    seq = seq_group.addSeq(chromName, 0);
                }
                if (seq.getLength() < end) {
                    seq.setLength(end);
                }
                if (seq.getLength() < start) {
                    seq.setLength(start);
                }
                final SingletonSymWithProps child = new SingletonSymWithProps(start, end, seq);
                child.setProperty("method", type);
                if (id == null) {
                    id = type + " " + seq.getID() + ":" + start + "-" + end;
                }
                child.setProperty("id", id);
                if (this.make_props) {
                    for (int j = 0; j < cols.length && j < col_names.size(); ++j) {
                        final String name = col_names.get(j);
                        final String val = cols[j];
                        child.setProperty(name, val);
                    }
                }
                if (this.use_group) {
                    final String group = cols[this.group_col];
                    SimpleSymWithProps parent = (SimpleSymWithProps)group_hash.get(group);
                    if (parent == null) {
                        parent = new SimpleSymWithProps();
                        final SimpleMutableSeqSpan span = new SimpleMutableSeqSpan(start, end, seq);
                        parent.addSpan(span);
                        parent.setProperty("method", type);
                        if (id == null) {
                            id = type + " " + span.getBioSeq().getID() + ":" + span.getStart() + "-" + span.getEnd();
                        }
                        parent.setProperty("id", id);
                        group_hash.put(group, parent);
                        results.add(parent);
                        if (annotateSeq) {
                            seq.addAnnotation(parent);
                            seq_group.addToIndex(parent.getID(), parent);
                        }
                    }
                    else {
                        final MutableSeqSpan pspan = (MutableSeqSpan)parent.getSpan(seq);
                        SeqUtils.encompass(pspan, child, union_span);
                        pspan.set(union_span.getStart(), union_span.getEnd(), seq);
                    }
                    parent.addChild(child);
                }
                else {
                    results.add(child);
                    if (!annotateSeq) {
                        continue;
                    }
                    seq.addAnnotation(child);
                    seq_group.addToIndex(child.getID(), child);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, uri, group, annotate_seq);
    }

    static {
        line_splitter = Pattern.compile("\t");
    }
}
