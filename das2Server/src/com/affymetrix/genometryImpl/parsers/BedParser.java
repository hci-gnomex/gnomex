//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.Scored;
import java.util.Iterator;
import java.io.DataOutputStream;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.awt.Color;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.UcscBedSym;
import com.affymetrix.genometryImpl.symmetry.UcscBedDetailSym;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.regex.Pattern;
import java.util.List;

public final class BedParser implements AnnotationWriter, IndexWriter, Parser
{
    private static List<String> pref_list;
    private static final boolean DEBUG = false;
    private static final Pattern tab_regex;
    private static final Pattern line_regex;
    private static final Pattern comma_regex;
    private List<SeqSymmetry> symlist;
    private Map<BioSeq, Map<String, SeqSymmetry>> seq2types;
    private boolean annotate_seq;
    private boolean create_container_annot;
    private String default_type;
    private final TrackLineParser track_line_parser;

    public BedParser() {
        this.symlist = new ArrayList<SeqSymmetry>();
        this.seq2types = new HashMap<BioSeq, Map<String, SeqSymmetry>>();
        this.annotate_seq = true;
        this.create_container_annot = false;
        this.default_type = null;
        this.track_line_parser = new TrackLineParser();
    }

    public List<SeqSymmetry> parse(final InputStream istr, final GenometryModel gmodel, final AnnotatedSeqGroup group, final boolean annot_seq, final String stream_name, final boolean create_container) throws IOException {
        this.seq2types = new HashMap<BioSeq, Map<String, SeqSymmetry>>();
        this.symlist = new ArrayList<SeqSymmetry>();
        this.annotate_seq = annot_seq;
        this.create_container_annot = create_container;
        this.default_type = stream_name;
        if (stream_name.endsWith(".bed")) {
            this.default_type = stream_name.substring(0, stream_name.lastIndexOf(".bed"));
        }
        BufferedInputStream bis;
        if (istr instanceof BufferedInputStream) {
            bis = (BufferedInputStream)istr;
        }
        else {
            bis = new BufferedInputStream(istr);
        }
        final DataInputStream dis = new DataInputStream(bis);
        this.parse(dis, gmodel, group, this.default_type);
        return this.symlist;
    }

    private void parse(final DataInputStream dis, final GenometryModel gmodel, final AnnotatedSeqGroup seq_group, final String default_type) throws IOException {
        String type = default_type;
        String bedType = null;
        boolean use_item_rgb = false;
        final Thread thread = Thread.currentThread();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
        String line;
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            if (!line.startsWith("#")) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("track")) {
                    this.track_line_parser.parseTrackLine(line);
                    TrackLineParser.createTrackStyle(this.track_line_parser.getCurrentTrackHash(), default_type, "bed");
                    type = this.track_line_parser.getCurrentTrackHash().get("name");
                    final String item_rgb_string = this.track_line_parser.getCurrentTrackHash().get("itemrgb");
                    use_item_rgb = "on".equalsIgnoreCase(item_rgb_string);
                    bedType = this.track_line_parser.getCurrentTrackHash().get("type");
                }
                else {
                    if (line.startsWith("browser")) {
                        continue;
                    }
                    this.parseLine(line, seq_group, gmodel, type, use_item_rgb, bedType);
                }
            }
        }
    }

    private void parseLine(final String line, final AnnotatedSeqGroup seq_group, final GenometryModel gmodel, final String type, final boolean use_item_rgb, final String bedType) throws NumberFormatException, IOException {
        final boolean bedDetail = "bedDetail".equals(bedType);
        String detailId = null;
        String detailDescription = null;
        String[] fields = BedParser.tab_regex.split(line);
        int field_count = fields.length;
        if (field_count == 1) {
            fields = BedParser.line_regex.split(line);
        }
        if (bedDetail) {
            detailId = fields[field_count - 2];
            detailDescription = fields[field_count - 1];
            field_count -= 2;
        }
        if (field_count < 3) {
            return;
        }
        String seq_name = null;
        String annot_name = null;
        String itemRgb = "";
        int thick_min = Integer.MIN_VALUE;
        int thick_max = Integer.MIN_VALUE;
        float score = Float.NEGATIVE_INFINITY;
        int[] blockSizes = null;
        int[] blockStarts = null;
        int[] blockMins = null;
        int[] blockMaxs = null;
        final boolean includes_bin_field = field_count > 6 && (fields[6].startsWith("+") || fields[6].startsWith("-") || fields[6].startsWith("."));
        int findex = 0;
        if (includes_bin_field) {
            ++findex;
        }
        seq_name = fields[findex++];
        BioSeq seq = seq_group.getSeq(seq_name);
        if (seq == null && seq_name.indexOf(59) > -1) {
            String seqid = seq_name.substring(0, seq_name.indexOf(59));
            String version = seq_name.substring(seq_name.indexOf(59) + 1);
            if (gmodel.getSeqGroup(version) == seq_group || seq_group.getID().equals(version)) {
                seq = seq_group.getSeq(seqid);
                if (seq != null) {
                    seq_name = seqid;
                }
            }
            else if (gmodel.getSeqGroup(seqid) == seq_group || seq_group.getID().equals(seqid)) {
                final String temp = seqid;
                seqid = version;
                version = temp;
                seq = seq_group.getSeq(seqid);
                if (seq != null) {
                    seq_name = seqid;
                }
            }
        }
        if (seq == null) {
            seq = seq_group.addSeq(seq_name, 0);
        }
        final int beg = Integer.parseInt(fields[findex++]);
        final int end = Integer.parseInt(fields[findex++]);
        if (field_count >= 4) {
            annot_name = parseName(fields[findex++]);
            if (annot_name == null || annot_name.length() == 0) {
                annot_name = seq_group.getUniqueID();
            }
        }
        if (field_count >= 5) {
            score = parseScore(fields[findex++]);
        }
        boolean forward;
        if (field_count >= 6) {
            forward = !fields[findex++].equals("-");
        }
        else {
            forward = (beg <= end);
        }
        final int min = Math.min(beg, end);
        final int max = Math.max(beg, end);
        if (field_count >= 8) {
            thick_min = Integer.parseInt(fields[findex++]);
            thick_max = Integer.parseInt(fields[findex++]);
        }
        if (field_count >= 9) {
            itemRgb = fields[findex++];
        }
        else {
            ++findex;
        }
        if (field_count >= 12) {
            final int blockCount = Integer.parseInt(fields[findex++]);
            blockSizes = parseIntArray(fields[findex++]);
            if (blockCount != blockSizes.length) {
                System.out.println("WARNING: block count does not agree with block sizes.  Ignoring " + annot_name + " on " + seq_name);
                return;
            }
            blockStarts = parseIntArray(fields[findex++]);
            if (blockCount != blockStarts.length) {
                System.out.println("WARNING: block size does not agree with block starts.  Ignoring " + annot_name + " on " + seq_name);
                return;
            }
            blockMins = makeBlockMins(min, blockStarts);
            blockMaxs = makeBlockMaxs(blockSizes, blockMins);
        }
        else {
            blockMins = new int[] { min };
            blockMaxs = new int[] { max };
        }
        if (max > seq.getLength()) {
            seq.setLength(max);
        }
        SymWithProps bedline_sym = null;
        bedline_sym = (bedDetail ? new UcscBedDetailSym(type, seq, min, max, annot_name, score, forward, thick_min, thick_max, blockMins, blockMaxs, detailId, detailDescription) : new UcscBedSym(type, seq, min, max, annot_name, score, forward, thick_min, thick_max, blockMins, blockMaxs));
        if (use_item_rgb && itemRgb != null) {
            Color c = null;
            try {
                c = TrackLineParser.reformatColor(itemRgb);
            }
            catch (Exception e) {
                throw new IOException("Could not parse a color from String '" + itemRgb + "'");
            }
            if (c != null) {
                bedline_sym.setProperty("itemrgb", c);
            }
        }
        this.symlist.add(bedline_sym);
        if (this.annotate_seq) {
            this.annotationParsed(bedline_sym);
        }
        if (annot_name != null) {
            seq_group.addToIndex(annot_name, bedline_sym);
        }
    }

    private static float parseScore(final String s) {
        return Float.parseFloat(s);
    }

    private static String parseName(final String s) {
        final String annot_name = new String(s);
        return annot_name;
    }

    private void annotationParsed(final SeqSymmetry bedline_sym) {
        final BioSeq seq = bedline_sym.getSpan(0).getBioSeq();
        if (this.create_container_annot) {
            String type = this.track_line_parser.getCurrentTrackHash().get("name");
            if (type == null) {
                type = this.default_type;
            }
            Map<String, SeqSymmetry> type2csym = this.seq2types.get(seq);
            if (type2csym == null) {
                type2csym = new HashMap<String, SeqSymmetry>();
                this.seq2types.put(seq, type2csym);
            }
            SimpleSymWithProps parent_sym = (SimpleSymWithProps) type2csym.get(type);
            if (parent_sym == null) {
                parent_sym = new SimpleSymWithProps();
                parent_sym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
                parent_sym.setProperty("method", type);
                parent_sym.setProperty("preferred_formats", BedParser.pref_list);
                parent_sym.setProperty("container sym", Boolean.TRUE);
                seq.addAnnotation(parent_sym);
                type2csym.put(type, parent_sym);
            }
            parent_sym.addChild(bedline_sym);
        }
        else {
            seq.addAnnotation(bedline_sym);
        }
    }

    public static int[] parseIntArray(final String int_array) {
        final String[] intstrings = BedParser.comma_regex.split(int_array);
        final int count = intstrings.length;
        final int[] results = new int[count];
        for (int i = 0; i < count; ++i) {
            final int val = Integer.parseInt(intstrings[i]);
            results[i] = val;
        }
        return results;
    }

    public static int[] makeBlockMins(final int min, final int[] blockStarts) {
        final int count = blockStarts.length;
        final int[] blockMins = new int[count];
        for (int i = 0; i < count; ++i) {
            blockMins[i] = blockStarts[i] + min;
        }
        return blockMins;
    }

    public static int[] makeBlockMaxs(final int[] blockMins, final int[] blockSizes) {
        final int count = blockMins.length;
        final int[] blockMaxs = new int[count];
        for (int i = 0; i < count; ++i) {
            blockMaxs[i] = blockMins[i] + blockSizes[i];
        }
        return blockMaxs;
    }

    public static void writeBedFormat(final DataOutputStream out, final List<SeqSymmetry> syms, final BioSeq seq) throws IOException {
        for (final SeqSymmetry sym : syms) {
            writeSymmetry(out, sym, seq);
        }
    }

    public static void writeSymmetry(final DataOutputStream out, final SeqSymmetry sym, final BioSeq seq) throws IOException {
        final SeqSpan span = sym.getSpan(seq);
        if (span == null) {
            return;
        }
        if (sym instanceof UcscBedSym) {
            final UcscBedSym bedsym = (UcscBedSym)sym;
            if (seq == bedsym.getBioSeq()) {
                bedsym.outputBedFormat(out);
            }
            return;
        }
        SymWithProps propsym = null;
        if (sym instanceof SymWithProps) {
            propsym = (SymWithProps)sym;
        }
        writeOutFile(out, seq, span, sym, propsym);
    }

    private static void writeOutFile(final DataOutputStream out, final BioSeq seq, final SeqSpan span, final SeqSymmetry sym, final SymWithProps propsym) throws IOException {
        out.write(seq.getID().getBytes());
        out.write(9);
        final int min = span.getMin();
        final int max = span.getMax();
        out.write(Integer.toString(min).getBytes());
        out.write(9);
        out.write(Integer.toString(max).getBytes());
        final int childcount = sym.getChildCount();
        if (!span.isForward() || childcount > 0 || propsym != null) {
            out.write(9);
            if (propsym != null) {
                if (propsym.getProperty("name") != null) {
                    out.write(((String)propsym.getProperty("name")).getBytes());
                }
                else if (propsym.getProperty("id") != null) {
                    out.write(((String)propsym.getProperty("id")).getBytes());
                }
            }
            out.write(9);
            if (propsym != null && propsym.getProperty("score") != null) {
                out.write(propsym.getProperty("score").toString().getBytes());
            }
            else if (sym instanceof Scored) {
                out.write(Float.toString(((Scored)sym).getScore()).getBytes());
            }
            else {
                out.write(48);
            }
            out.write(9);
            if (span.isForward()) {
                out.write(43);
            }
            else {
                out.write(45);
            }
            if (childcount > 0) {
                writeOutChildren(out, propsym, min, max, childcount, sym, seq);
            }
        }
        out.write(10);
    }

    private static void writeOutChildren(final DataOutputStream out, final SymWithProps propsym, final int min, final int max, final int childcount, final SeqSymmetry sym, final BioSeq seq) throws IOException {
        out.write(9);
        if (propsym != null && propsym.getProperty("cds min") != null) {
            out.write(propsym.getProperty("cds min").toString().getBytes());
        }
        else {
            out.write(Integer.toString(min).getBytes());
        }
        out.write(9);
        if (propsym != null && propsym.getProperty("cds max") != null) {
            out.write(propsym.getProperty("cds max").toString().getBytes());
        }
        else {
            out.write(Integer.toString(max).getBytes());
        }
        out.write(9);
        out.write(48);
        out.write(9);
        out.write(Integer.toString(childcount).getBytes());
        out.write(9);
        final int[] blockSizes = new int[childcount];
        final int[] blockStarts = new int[childcount];
        for (int i = 0; i < childcount; ++i) {
            final SeqSymmetry csym = sym.getChild(i);
            final SeqSpan cspan = csym.getSpan(seq);
            blockSizes[i] = cspan.getLength();
            blockStarts[i] = cspan.getMin() - min;
        }
        for (int i = 0; i < childcount; ++i) {
            out.write(Integer.toString(blockSizes[i]).getBytes());
            out.write(44);
        }
        out.write(9);
        for (int i = 0; i < childcount; ++i) {
            out.write(Integer.toString(blockStarts[i]).getBytes());
            out.write(44);
        }
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (final SeqSymmetry sym : syms) {
                writeSymmetry(dos, sym, seq);
            }
            dos.flush();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
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
        writeSymmetry(dos, sym, seq);
    }

    @Override
    public List<SeqSymmetry> parse(final DataInputStream dis, final String annot_type, final AnnotatedSeqGroup group) {
        try {
            return this.parse(dis, GenometryModel.getGenometryModel(), group, false, annot_type, false);
        }
        catch (IOException ex) {
            Logger.getLogger(BedParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Comparator<SeqSymmetry> getComparator(final BioSeq seq) {
        return new SeqSymMinComparator(seq);
    }

    @Override
    public int getMin(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMin();
    }

    @Override
    public int getMax(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMax();
    }

    @Override
    public List<String> getFormatPrefList() {
        return BedParser.pref_list;
    }

    @Override
    public String getMimeType() {
        return "text/bed";
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, GenometryModel.getGenometryModel(), group, annotate_seq, uri, false);
    }

    static {
        (BedParser.pref_list = new ArrayList<String>()).add("bed");
        tab_regex = Pattern.compile("\\t");
        line_regex = Pattern.compile("\\s+");
        comma_regex = Pattern.compile(",");
    }
}
