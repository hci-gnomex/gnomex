// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GenbankFeature
{
    private static final int key_offset = 5;
    private String type;
    private Map<String, List<String>> tagValues;
    private StringBuffer location;
    private String active_tag;
    private List<int[]> locs;
    private boolean initialized;
    private boolean extend3prime;
    private boolean extend5prime;
    
    protected GenbankFeature() {
        this.tagValues = new HashMap<String, List<String>>(3);
        this.active_tag = null;
        this.locs = null;
        this.initialized = false;
        this.extend3prime = false;
        this.extend5prime = false;
    }
    
    private void init() {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        this.initSynonyms();
        this.initLocations();
    }
    
    protected String getFeatureType(final String current_line) {
        final String str = current_line.substring(5);
        final int index = str.indexOf(32);
        if (index > 0) {
            this.location = new StringBuffer();
            this.type = str.substring(0, index);
            this.location.append(str.substring(index).trim());
        }
        return this.type;
    }
    
    protected boolean addToFeature(final String current_line) {
        if (forSameFeature(current_line)) {
            final String str = current_line.substring(5).trim();
            if (str.charAt(0) != '/' && this.active_tag == null) {
                this.location.append(str);
            }
            else {
                this.setTagValue(str);
            }
            return true;
        }
        return false;
    }
    
    private static boolean forSameFeature(final String current_line) {
        return current_line.charAt(5) == ' ';
    }
    
    private List<String> getValues(final String tag) {
        this.init();
        return this.tagValues.get(tag);
    }
    
    protected Map<String, List<String>> getTagValues() {
        return this.tagValues;
    }
    
    protected String getValue(final String tag) {
        final StringBuilder val = new StringBuilder();
        final List<String> all_vals = this.getValues(tag);
        if (all_vals != null) {
            for (int val_count = all_vals.size(), i = 0; i < val_count; ++i) {
                val.append(all_vals.get(i));
                if (i < val_count - 1) {
                    val.append(" ");
                }
            }
        }
        return val.toString();
    }
    
    private void initSynonyms() {
        List<String> syns = null;
        final String note = this.getValue("note");
        int index = (note != null) ? note.indexOf("synonyms:") : -1;
        if (index >= 0) {
            syns = new ArrayList<String>(1);
            String syns_str = note.substring(index + "synonyms:".length()).trim();
            int end = syns_str.indexOf(59);
            if (end > 0) {
                syns_str = syns_str.substring(0, end);
                index = note.indexOf(59, index) + 1;
            }
            while (syns_str.length() > 0) {
                end = syns_str.indexOf(44);
                String syn;
                if (end > 0) {
                    syn = syns_str.substring(0, end);
                    syns_str = ((++end < syns_str.length()) ? syns_str.substring(end) : "");
                }
                else {
                    syn = syns_str;
                    syns_str = "";
                }
                syns.add(syn);
            }
        }
        if (syns != null) {
            this.tagValues.put("synonyms", syns);
        }
    }
    
    private void setTagValue(final String content) {
        String tag;
        String value;
        if (content.charAt(0) == '/') {
            int index = content.indexOf("=");
            if (index >= 0) {
                tag = content.substring(1, index);
                value = content.substring(index + "=".length());
                value = stripQuotes(value);
                index = value.indexOf(58);
                if (index > 0 && !tag.equals("note") && !value.substring(0, index).contains(" ") && !tag.equals("gene") && !tag.equals("method") && !tag.equals("date") && !tag.endsWith("synonym") && !tag.equals("product") && !tag.equals("prot_desc") && !tag.equals("db_xref")) {
                    tag = value.substring(0, index);
                    final String tmp = value = value.substring(index + ":".length());
                }
                this.active_tag = tag;
            }
            else if (content.charAt(0) == '/') {
                tag = content.substring(1);
                value = "true";
            }
            else {
                tag = this.active_tag;
                value = content;
            }
        }
        else {
            tag = this.active_tag;
            value = stripQuotes(content);
        }
        if (!value.equalsIgnoreCase("unknown")) {
            List<String> current_vec = this.tagValues.get(tag);
            if (current_vec == null) {
                current_vec = new ArrayList<String>();
                this.tagValues.put(tag, current_vec);
            }
            if (!value.equals("") && !value.equals(".")) {
                current_vec.add(value);
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.type).append(" at ").append(this.location.toString()).append("\n");
        for (final String tag : this.tagValues.keySet()) {
            buf.append("\t").append(tag).append(" = ").append(this.getValue(tag)).append("\n");
        }
        return buf.toString();
    }
    
    static String stripQuotes(String value) {
        if (value.length() == 0) {
            return value;
        }
        if (value.charAt(0) == '\"') {
            value = value.substring(1);
        }
        if (value.length() >= 1 && value.charAt(value.length() - 1) == '\"') {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
    
    protected List<int[]> getLocation() {
        this.init();
        return this.locs;
    }
    
    protected List<int[]> initLocations() {
        this.locs = new ArrayList<int[]>();
        this.parseLocations(this.location.toString(), this.locs);
        return this.locs;
    }
    
    private void parseLocations(final String location_str, final List<int[]> locs) {
        if (location_str != null && !location_str.equals("")) {
            String operation_str = null;
            int index_start = 0;
            int index_end = 0;
            if (location_str.startsWith("complement(")) {
                final List<int[]> comp_vect = new ArrayList<int[]>();
                index_start = "complement(".length();
                index_end = indexOfClosingParen(location_str, index_start);
                operation_str = substringLocation(location_str, index_start, index_end);
                this.parseLocations(operation_str, comp_vect);
                final int cmp_count = comp_vect.size();
                for (int i = cmp_count - 1; i >= 0; --i) {
                    final int[] span = comp_vect.get(i);
                    comp_vect.remove(span);
                    final int tmp = span[0];
                    span[0] = span[1];
                    span[1] = tmp;
                    locs.add(span);
                }
                if (this.extend3prime) {
                    this.extend5prime = true;
                    this.extend3prime = false;
                }
                else if (this.extend5prime) {
                    this.extend5prime = false;
                    this.extend3prime = true;
                }
            }
            else if (location_str.startsWith("join(")) {
                index_start = "join(".length();
                index_end = indexOfClosingParen(location_str, index_start);
                operation_str = substringLocation(location_str, index_start, index_end);
                this.parseLocations(operation_str, locs);
            }
            else if (location_str.startsWith("order(")) {
                index_start = "order(".length();
                index_end = indexOfClosingParen(location_str, index_start);
                operation_str = substringLocation(location_str, index_start, index_end);
                this.parseLocations(operation_str, locs);
            }
            else if (!Character.isDigit(location_str.charAt(0))) {
                if (location_str.charAt(0) == '<') {
                    this.extend5prime = true;
                    this.parseLocations(location_str.substring(1), locs);
                }
                else if (location_str.indexOf(58) > 0) {
                    this.parseLocations(location_str.substring(location_str.indexOf(58) + 1), locs);
                }
                else {
                    this.parseLocations(location_str.substring(1), locs);
                }
            }
            else {
                index_start = 0;
                index_end = indexOfNextNonDigit(location_str, index_start);
                String pos_str = "";
                try {
                    pos_str = substringLocation(location_str, index_start, index_end);
                    int low = Integer.parseInt(pos_str);
                    final boolean no_high = (index_end < location_str.length() && location_str.charAt(index_end) == ',') || index_end >= location_str.length();
                    int high;
                    if (no_high) {
                        high = low;
                    }
                    else {
                        index_start = ++index_end;
                        if (location_str.charAt(index_start) == '>') {
                            this.extend3prime = true;
                            ++index_start;
                        }
                        if (Character.isDigit(location_str.charAt(index_start))) {
                            index_end = indexOfNextNonDigit(location_str, index_start);
                            pos_str = substringLocation(location_str, index_start, index_end);
                            high = Integer.parseInt(pos_str);
                            if (high != low) {
                                low += (high - low + 1) / 2;
                                high = low + 1;
                            }
                            else {
                                ++high;
                            }
                        }
                        else {
                            ++index_start;
                            if (location_str.charAt(index_start) == '>') {
                                this.extend3prime = true;
                                ++index_start;
                            }
                            index_end = indexOfNextNonDigit(location_str, index_start);
                            pos_str = substringLocation(location_str, index_start, index_end);
                            high = Integer.parseInt(pos_str);
                        }
                    }
                    final int[] pos = { low, high };
                    locs.add(pos);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            final int index_comma = (index_end < location_str.length()) ? location_str.indexOf(",", index_end) : -1;
            final int next_locs = (index_comma >= 0) ? (index_comma + 1) : 0;
            if (next_locs > 0 && next_locs < location_str.length()) {
                this.parseLocations(location_str.substring(next_locs), locs);
            }
        }
    }
    
    private static int indexOfNextNonDigit(final String location_str, final int index_start) {
        int index_end;
        for (index_end = index_start; index_end < location_str.length() && Character.isDigit(location_str.charAt(index_end)); ++index_end) {}
        return index_end;
    }
    
    private static String substringLocation(final String location_str, final int index_start, final int index_end) {
        try {
            return (index_end < location_str.length()) ? location_str.substring(index_start, index_end) : ((index_start < location_str.length()) ? location_str.substring(index_start) : location_str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static int indexOfClosingParen(final String location_str, final int index_start) {
        int index_end = index_start;
        for (int open_paren_count = 1; index_end < location_str.length() && open_paren_count != 0; ++index_end) {
            if (location_str.charAt(index_end) == ')') {
                --open_paren_count;
            }
            else if (location_str.charAt(index_end) == '(') {
                ++open_paren_count;
            }
            if (open_paren_count != 0) {}
        }
        return index_end;
    }
}
