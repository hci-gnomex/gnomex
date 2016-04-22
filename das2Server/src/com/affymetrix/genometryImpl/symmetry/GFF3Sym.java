// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import java.util.LinkedHashMap;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import com.affymetrix.genometryImpl.Scored;

public final class GFF3Sym extends SimpleSymWithProps implements Scored, SupportsCdsSpan, Cloneable
{
    private String id;
    private static boolean multipleCdsWarning;
    public static final char UNKNOWN_FRAME = '.';
    public static final String UNKNOWN_SOURCE = ".";
    public static final String FEATURE_TYPE_GENE = "gene";
    public static final String FEATURE_TYPE_MRNA = "mrna";
    public static final String FEATURE_TYPE_EXON = "exon";
    public static final String FEATURE_TYPE_CDS = "cds";
    public static final String FEATURE_TYPE_CHROMOSOME = "chromosome";
    public static final String SOFA_GENE = "SO:0000704";
    public static final String SOFA_MRNA = "SO:0000234";
    public static final String SOFA_EXON = "SO:0000147";
    public static final String SOFA_CDS = "SO:0000316";
    private static final Pattern equalsP;
    private static final Pattern commaP;
    private static final String[] EMPTY_RESULT;
    private static final List<String> bad_prop_names;
    private String source;
    private String method;
    public String feature_type;
    private final float score;
    private final char frame;
    private final String attributes;
    
    public GFF3Sym(final String source, final String feature_type, final float score, final char frame, final String attributes) {
        if (!".".equals(source)) {
            this.source = source;
        }
        else {
            this.source = ".";
        }
        this.method = null;
        this.feature_type = feature_type;
        this.score = score;
        this.frame = frame;
        this.attributes = attributes;
        final String[] possible_names = getGFF3PropertyFromAttributes("Name", attributes);
        if (possible_names.length > 0) {
            this.id = possible_names[0];
        }
        else {
            this.id = null;
        }
    }
    
    @Override
    public String getID() {
        if (this.id == null) {
            return null;
        }
        return this.id.toString();
    }
    
    public String getSource() {
        return this.source;
    }
    
    public String getFeatureType() {
        return this.feature_type;
    }
    
    @Override
    public float getScore() {
        return this.score;
    }
    
    public char getFrame() {
        return this.frame;
    }
    
    public String getAttributes() {
        return this.attributes;
    }
    
    @Override
    public Object getProperty(final String name) {
        if (name.equals("source") && this.source != null) {
            return this.source;
        }
        if (name.equals("method")) {
            return this.method;
        }
        if (name.equals("feature_type") || name.equals("type")) {
            return this.feature_type;
        }
        if (name.equals("score") && this.score != Float.NEGATIVE_INFINITY) {
            return new Float(this.score);
        }
        if (name.equals("frame") && this.frame != '.') {
            return this.frame;
        }
        if (name.equals("id")) {
            return this.getID();
        }
        final String[] temp = getGFF3PropertyFromAttributes(name, this.attributes);
        if (temp.length == 0) {
            return null;
        }
        if (temp.length == 1) {
            return temp[0];
        }
        return temp;
    }
    
    @Override
    public boolean setProperty(final String name, final Object val) {
        final String lc_name = name.toLowerCase();
        if (name.equals("id")) {
            if (val instanceof String) {
                this.id = (String)val;
                return true;
            }
            return false;
        }
        else if (name.equals("source")) {
            if (val instanceof String) {
                this.source = (String)val;
                return true;
            }
            return false;
        }
        else if (name.equals("method")) {
            if (val instanceof String) {
                this.method = (String)val;
                return true;
            }
            return false;
        }
        else {
            if (GFF3Sym.bad_prop_names.contains(lc_name)) {
                throw new IllegalArgumentException("Currently can't modify property '" + name + "' via setProperty");
            }
            return super.setProperty(name, val);
        }
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.cloneProperties();
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        Map<String, Object> tprops = super.cloneProperties();
        if (tprops == null) {
            tprops = new HashMap<String, Object>();
        }
        if (this.getID() != null) {
            tprops.put("id", this.getID());
        }
        if (this.source != null) {
            tprops.put("source", this.source);
        }
        if (this.method != null) {
            tprops.put("method", this.method);
        }
        if (this.feature_type != null) {
            tprops.put("feature_type", this.feature_type);
            tprops.put("type", this.feature_type);
        }
        if (this.score != Float.NEGATIVE_INFINITY) {
            tprops.put("score", new Float(this.getScore()));
        }
        if (this.frame != '.') {
            tprops.put("frame", this.frame);
        }
        addAllAttributesFromGFF3(tprops, this.attributes);
        return tprops;
    }
    
    public static String getIdFromGFF3Attributes(final String attributes) {
        final String[] possible_ids = getGFF3PropertyFromAttributes("ID", attributes);
        if (possible_ids.length == 0) {
            return null;
        }
        return possible_ids[0];
    }
    
    private static void addAllAttributesFromGFF3(final Map<String, Object> m, final String attributes) {
        if (attributes == null) {
            return;
        }
        final String[] tag_vals = attributes.split(";");
        for (int i = 0; i < tag_vals.length; ++i) {
            if (!"".equals(tag_vals[i])) {
                final String[] tag_and_vals = GFF3Sym.equalsP.split(tag_vals[i], 2);
                if (tag_and_vals.length == 2) {
                    final String[] vals = GFF3Sym.commaP.split(tag_and_vals[1]);
                    for (int j = 0; j < vals.length; ++j) {
                        vals[j] = GeneralUtils.URLDecode(vals[j]);
                    }
                    if (vals.length == 1) {
                        m.put(tag_and_vals[0], vals[0]);
                    }
                    else {
                        m.put(tag_and_vals[0], vals);
                    }
                }
            }
        }
    }
    
    public static String[] getGFF3PropertyFromAttributes(final String prop_name, final String attributes) {
        if (attributes == null) {
            return GFF3Sym.EMPTY_RESULT;
        }
        final String[] tag_vals = attributes.split(";");
        final String prop_with_equals = prop_name + "=";
        String val = null;
        for (int i = 0; i < tag_vals.length; ++i) {
            if (tag_vals[i].startsWith(prop_with_equals)) {
                val = tag_vals[i].substring(prop_with_equals.length());
                break;
            }
        }
        if (val == null) {
            return GFF3Sym.EMPTY_RESULT;
        }
        final String[] results = val.split(",");
        for (int j = 0; j < results.length; ++j) {
            results[j] = GeneralUtils.URLDecode(results[j]);
        }
        return results;
    }
    
    public static String normalizeFeatureType(final String s) {
        if ("gene".equalsIgnoreCase(s)) {
            return "gene";
        }
        if ("exon".equalsIgnoreCase(s)) {
            return "exon";
        }
        if ("mrna".equalsIgnoreCase(s)) {
            return "mrna";
        }
        if ("cds".equalsIgnoreCase(s)) {
            return "cds";
        }
        if ("chromosome".equalsIgnoreCase(s)) {
            return "chromosome";
        }
        if ("SO:0000704".equalsIgnoreCase(s)) {
            return "gene";
        }
        if ("SO:0000147".equalsIgnoreCase(s)) {
            return "exon";
        }
        if ("SO:0000234".equalsIgnoreCase(s)) {
            return "mrna";
        }
        if ("SO:0000316".equalsIgnoreCase(s)) {
            return "cds";
        }
        return s.intern();
    }
    
    @Override
    public String toString() {
        return "GFF3Sym: ID = '" + this.getProperty("ID") + "'  type=" + this.feature_type + " children=" + this.getChildCount();
    }
    
    @Override
    public boolean hasCdsSpan() {
        for (final SeqSymmetry child : this.children) {
            if (isCdsSym(child)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isCdsSym(final SeqSymmetry sym) {
        return sym instanceof GFF3Sym && ((GFF3Sym)sym).getFeatureType().equals("cds");
    }
    
    @Override
    public SeqSpan getCdsSpan() {
        final Map<String, MutableSeqSpan> cdsSpans = new LinkedHashMap<String, MutableSeqSpan>();
        MutableSeqSpan span = null;
        for (final SeqSymmetry child : this.children) {
            if (isCdsSym(child)) {
                final String gff3ID = getIdFromGFF3Attributes(((GFF3Sym)child).getAttributes());
                for (int i = 0; i < child.getSpanCount(); ++i) {
                    span = cdsSpans.get(gff3ID);
                    if (span == null) {
                        span = new SimpleMutableSeqSpan(child.getSpan(i));
                        cdsSpans.put(gff3ID, span);
                    }
                    else {
                        SeqUtils.encompass(child.getSpan(i), span, span);
                    }
                }
            }
        }
        if (cdsSpans.isEmpty()) {
            throw new IllegalArgumentException("This Symmetry does not have a CDS");
        }
        if (cdsSpans.size() > 1) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Multiple CDS spans detected.  Skipping remaining CDS spans.  (found {0} spans for {1})", new Object[] { cdsSpans.size(), getIdFromGFF3Attributes(this.attributes) });
            if (!GFF3Sym.multipleCdsWarning) {
                GFF3Sym.multipleCdsWarning = !GFF3Sym.multipleCdsWarning;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Multiple CDS regions for a shared parent have been\ndetected in a GFF3 file.  Only the first CDS region\nencountered will be displayed.  This is a known\nlimitation of the GFF3 parser.", "Multiple CDS Regions Detected", 2);
                    }
                });
            }
        }
        return cdsSpans.entrySet().iterator().next().getValue();
    }
    
    public Map<String, List<SeqSymmetry>> getCdsSpans() {
        final Map<String, List<SeqSymmetry>> cdsSpans = new LinkedHashMap<String, List<SeqSymmetry>>();
        for (final SeqSymmetry child : this.children) {
            if (isCdsSym(child)) {
                final String gff3ID = getIdFromGFF3Attributes(((GFF3Sym)child).getAttributes());
                for (int i = 0; i < child.getSpanCount(); ++i) {
                    List<SeqSymmetry> list = cdsSpans.get(gff3ID);
                    if (list == null) {
                        list = new ArrayList<SeqSymmetry>();
                        cdsSpans.put(gff3ID, list);
                        list.add(child);
                    }
                }
            }
        }
        return cdsSpans;
    }
    
    public void removeCdsSpans() {
        final List<SeqSymmetry> remove_list = new ArrayList<SeqSymmetry>();
        for (final SeqSymmetry child : this.children) {
            if (isCdsSym(child)) {
                remove_list.add(child);
            }
        }
        this.children.removeAll(remove_list);
    }
    
    public Object clone() {
        final GFF3Sym dup = new GFF3Sym(this.source, this.feature_type, this.score, this.frame, this.attributes);
        if (this.children != null) {
            for (final SeqSymmetry child : this.children) {
                dup.addChild(child);
            }
        }
        if (this.spans != null) {
            for (final SeqSpan span : this.spans) {
                dup.addSpan(span);
            }
        }
        dup.props = this.cloneProperties();
        dup.method = this.method;
        return dup;
    }
    
    static {
        GFF3Sym.multipleCdsWarning = false;
        equalsP = Pattern.compile("=");
        commaP = Pattern.compile(",");
        EMPTY_RESULT = new String[0];
        bad_prop_names = Arrays.asList("feature_type", "type", "score", "frame");
    }
}
