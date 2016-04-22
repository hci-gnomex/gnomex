// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.parsers.GFFParser;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.Scored;

public final class UcscGffSym extends SingletonSymWithProps implements Scored
{
    public static final char UNKNOWN_FRAME = '.';
    public static final Pattern gff1_regex;
    String source;
    String method;
    String feature_type;
    float score;
    char frame;
    String group;
    boolean is_gff1;
    
    public UcscGffSym(final BioSeq seq, final String source, final String feature_type, final int a, final int b, final float score, final char strand, final char frame, final String group_field, final boolean convert_base) {
        super(0, 0, seq);
        final int max = Math.max(a, b);
        int min = Math.min(a, b);
        if (convert_base) {
            --min;
        }
        if (strand == '-') {
            this.setCoords(max, min);
        }
        else {
            this.setCoords(min, max);
        }
        this.source = source;
        this.feature_type = feature_type;
        this.score = score;
        this.frame = frame;
        if (group_field == null || group_field.startsWith("#")) {
            this.group = null;
            this.is_gff1 = true;
        }
        else {
            final Matcher gff1_matcher = UcscGffSym.gff1_regex.matcher(group_field);
            if (gff1_matcher.matches()) {
                this.group = new String(gff1_matcher.group(1));
                this.is_gff1 = true;
            }
            else {
                this.group = group_field;
                this.is_gff1 = false;
            }
        }
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
    
    public String getGroup() {
        if (this.is_gff1) {
            return this.group;
        }
        return null;
    }
    
    public boolean isGFF1() {
        return this.is_gff1;
    }
    
    @Override
    public Object getProperty(final String name) {
        if (name.equals("method")) {
            return this.method;
        }
        if (name.equals("source")) {
            return this.source;
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
        if (name.equals("group")) {
            return this.getGroup();
        }
        if (name.equals("id")) {
            return this.getID();
        }
        if (this.is_gff1) {
            return super.getProperty(name);
        }
        final Map<String, Object> m = this.cloneProperties();
        return m.get(name);
    }
    
    @Override
    public boolean setProperty(final String name, final Object val) {
        if (name.equals("id")) {
            if (val instanceof String) {
                this.id = (String)val;
                return true;
            }
            return false;
        }
        else if (name.equals("feature_type") || name.equals("type")) {
            if (val instanceof String) {
                this.feature_type = (String)val;
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
            if (name.equals("group")) {
                throw new IllegalArgumentException("Currently can't modify 'group' via setProperty");
            }
            if (name.equals("score") || name.equals("frame")) {
                throw new IllegalArgumentException("Currently can't modify 'score' or 'frame' via setProperty");
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
        }
        if (this.feature_type != null) {
            tprops.put("type", this.feature_type);
        }
        if (this.score != Float.NEGATIVE_INFINITY) {
            tprops.put("score", new Float(this.getScore()));
        }
        if (this.frame != '.') {
            tprops.put("frame", this.frame);
        }
        if (this.is_gff1) {
            if (this.group != null) {
                tprops.put("group", this.group);
            }
        }
        else if (this.group != null) {
            GFFParser.processAttributes(tprops, this.group);
        }
        return tprops;
    }
    
    static {
        gff1_regex = Pattern.compile("^(\\S+)\\s*($|#.*)");
    }
}
