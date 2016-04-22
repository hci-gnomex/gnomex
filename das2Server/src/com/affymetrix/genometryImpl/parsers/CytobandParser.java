//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.TypedSym;
import com.affymetrix.genometryImpl.Scored;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithProps;
import java.util.Arrays;
import java.awt.GradientPaint;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.io.IOException;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.List;
import java.awt.Paint;
import java.awt.Color;
import com.affymetrix.genometryImpl.style.HeatMap;
import java.util.regex.Pattern;

public final class CytobandParser implements AnnotationWriter, Parser
{
    static Pattern line_regex;
    static final HeatMap cyto_heat_map;
    static final Color cyto_acen_color;
    static final Color cyto_stalk_color;
    public static final Paint acen_paint;
    public static final Paint stalk_paint;
    public static final String CYTOBAND_TIER_NAME = "__cytobands";
    public static final String CYTOBAND = "cytoband";
    public static final float GNEG_SCORE = 100.0f;
    public static final float GVAR_SCORE = 100.0f;
    public static final float ACEN_SCORE = 600.0f;
    public static final float STALK_SCORE = 500.0f;
    static List<String> pref_list;
    public static final String BAND_STALK = "stalk";
    public static final String BAND_ACEN = "acen";

    public List<SeqSymmetry> parse(final InputStream dis, final AnnotatedSeqGroup seq_group, final boolean annotate_seq) throws IOException {
        int band_alternator = 1;
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(100);
        final Thread thread = Thread.currentThread();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
        final Map<BioSeq, SeqSymmetry> seq2csym = new HashMap<BioSeq, SeqSymmetry>();
        String line;
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            if (line.charAt(0) != '#') {
                if (line.length() == 0) {
                    continue;
                }
                final String[] fields = CytobandParser.line_regex.split(line);
                final int field_count = fields.length;
                if (field_count < 4) {
                    throw new IOException("Line has wrong number of data columns.");
                }
                final String seq_name = fields[0];
                BioSeq seq = seq_group.getSeq(seq_name);
                if (seq == null) {
                    seq = seq_group.addSeq(seq_name, 0);
                }
                final int beg = Integer.parseInt(fields[1]);
                final int end = Integer.parseInt(fields[2]);
                String annot_name = new String(fields[3]);
                String band = null;
                if (field_count >= 5) {
                    band = new String(fields[4]);
                }
                else {
                    if (band_alternator > 0) {
                        band = "gpos25";
                    }
                    else {
                        band = "gpos75";
                    }
                    band_alternator = -band_alternator;
                }
                if (beg > seq.getLength()) {
                    seq.setLength(beg);
                }
                if (end > seq.getLength()) {
                    seq.setLength(end);
                }
                if (annot_name == null || annot_name.length() == 0) {
                    annot_name = seq_group.getID();
                }
                final CytobandSym sym = new CytobandSym(beg, end, seq, annot_name, band);
                if (annotate_seq) {
                    SimpleSymWithProps parent_sym = (SimpleSymWithProps) seq2csym.get(seq);
                    if (parent_sym == null) {
                        parent_sym = new SimpleSymWithProps();
                        parent_sym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
                        parent_sym.setProperty("method", "__cytobands");
                        parent_sym.setProperty("preferred_formats", CytobandParser.pref_list);
                        parent_sym.setProperty("container sym", Boolean.TRUE);
                        seq2csym.put(seq, parent_sym);
                        seq.addAnnotation(parent_sym);
                    }
                    parent_sym.addChild(sym);
                    if (annot_name != null && annot_name.length() != 0) {
                        seq_group.addToIndex(annot_name, sym);
                    }
                }
                results.add(sym);
            }
        }
        return results;
    }

    private static float parseScore(final String s) {
        if ("gneg".equals(s)) {
            return 100.0f;
        }
        if ("gvar".equals(s)) {
            return 100.0f;
        }
        if ("acen".equals(s)) {
            return 600.0f;
        }
        if ("stalk".equals(s)) {
            return 500.0f;
        }
        if (s.startsWith("gpos")) {
            float pos = 1000.0f;
            try {
                pos = 10.0f * Integer.parseInt(s.substring(4));
            }
            catch (NumberFormatException nfe) {
                pos = 1000.0f;
            }
            return pos;
        }
        return 0.0f;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        System.out.println("in CytobandParser.writeAnnotations()");
        boolean success = true;
        try {
            final Writer bw = new BufferedWriter(new OutputStreamWriter(outstream));
            for (final SeqSymmetry sym : syms) {
                writeCytobandFormat(bw, sym, seq);
            }
            bw.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    public static void writeCytobandFormat(final Writer out, final SeqSymmetry sym, final BioSeq seq) throws IOException {
        if (sym instanceof CytobandSym) {
            final CytobandSym cytosym = (CytobandSym)sym;
            final SeqSpan span = cytosym.getSpan(seq);
            if (span != null) {
                out.write(seq.getID());
                out.write(9);
                final int min = span.getMin();
                final int max = span.getMax();
                out.write(Integer.toString(min));
                out.write(9);
                out.write(Integer.toString(max));
                out.write(9);
                out.write(cytosym.getID());
                out.write(9);
                out.write(cytosym.getBand());
                out.write(10);
            }
        }
    }

    @Override
    public String getMimeType() {
        return "txt/plain";
    }

    public static int determineCentromerePoint(final List<CytobandSym> bands) {
        for (int i = 0; i < bands.size() - 1; ++i) {
            if (bands.get(i).getArm() != bands.get(i + 1).getArm()) {
                return i;
            }
        }
        return -1;
    }

    public static List<CytobandSym> generateBands(final SeqSymmetry cyto_container) {
        final List<CytobandSym> bands = new ArrayList<CytobandSym>();
        for (int q = 0; q < cyto_container.getChildCount(); ++q) {
            final SeqSymmetry child = cyto_container.getChild(q);
            if (child instanceof CytobandSym) {
                bands.add((CytobandSym)child);
            }
            else if (child != null) {
                for (int subindex = 0; subindex < child.getChildCount(); ++subindex) {
                    final SeqSymmetry grandchild = child.getChild(subindex);
                    if (grandchild instanceof CytobandSym) {
                        bands.add((CytobandSym)grandchild);
                    }
                }
            }
        }
        return bands;
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, group, annotate_seq);
    }

    static {
        CytobandParser.line_regex = Pattern.compile("\\t");
        cyto_heat_map = HeatMap.makeLinearHeatmap("cytobands", Color.WHITE, Color.BLACK);
        cyto_acen_color = new Color(198, 96, 96);
        cyto_stalk_color = new Color(128, 128, 160);
        acen_paint = new GradientPaint(0.0f, 0.0f, Color.DARK_GRAY, 1.0f, 1.0f, Color.WHITE, true);
        stalk_paint = new GradientPaint(0.0f, 1.0f, Color.GRAY, 1.0f, 0.0f, Color.WHITE, true);
        CytobandParser.pref_list = Arrays.asList("cyt");
    }

    public enum Arm
    {
        SHORT,
        LONG,
        UNKNOWN;
    }

    public static final class CytobandSym extends SingletonSymWithProps implements Scored, TypedSym
    {
        String band;

        public CytobandSym(final int start, final int end, final BioSeq seq, final String name, final String band) {
            super(start, end, seq);
            this.band = band;
            this.id = name;
        }

        @Override
        public float getScore() {
            return parseScore(this.band);
        }

        public String getBand() {
            return this.band;
        }

        public Arm getArm() {
            if (this.id.charAt(0) == 'p') {
                return Arm.SHORT;
            }
            if (this.id.charAt(0) == 'q') {
                return Arm.LONG;
            }
            return Arm.UNKNOWN;
        }

        public Color getColor() {
            if ("acen".equals(this.band)) {
                return CytobandParser.cyto_acen_color;
            }
            if ("stalk".equals(this.band)) {
                return CytobandParser.cyto_stalk_color;
            }
            final float score = parseScore(this.band);
            return CytobandParser.cyto_heat_map.getColors()[(int)(0.255 * score)];
        }

        public Color getTextColor() {
            final Color col = this.getColor();
            final int intensity = col.getRed() + col.getGreen() + col.getBlue();
            if (intensity > 383) {
                return Color.BLACK;
            }
            return Color.WHITE;
        }

        @Override
        public boolean setProperty(final String name, final Object val) {
            if ("band".equals(name)) {
                this.band = name;
            }
            return super.setProperty(name, val);
        }

        @Override
        public Object getProperty(final String name) {
            if ("id".equals(name)) {
                return this.getID();
            }
            if ("method".equals(name)) {
                return "__cytobands";
            }
            if ("band".equals(name)) {
                return this.band;
            }
            return super.getProperty(name);
        }

        @Override
        public Map<String, Object> cloneProperties() {
            Map<String, Object> props = super.cloneProperties();
            if (props == null) {
                props = new HashMap<String, Object>(4);
            }
            if (this.id != null) {
                props.put("id", this.id);
            }
            props.put("band", this.band);
            return props;
        }

        @Override
        public String getType() {
            return "__cytobands";
        }
    }
}
