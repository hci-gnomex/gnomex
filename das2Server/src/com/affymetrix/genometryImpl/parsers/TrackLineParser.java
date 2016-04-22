// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.style.GraphState;
import com.affymetrix.genometryImpl.style.GraphType;
import java.util.Iterator;
import java.util.List;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import java.util.Arrays;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import com.affymetrix.genometryImpl.style.ITrackStyle;
import java.util.regex.Matcher;
import java.awt.Color;
import java.util.TreeMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class TrackLineParser
{
    private static final boolean DEBUG = false;
    private static final Pattern comma_regex;
    public static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String DESCRIPTION = "description";
    private static final String VISIBILITY = "visibility";
    private static final String URL = "url";
    private static final String USE_SCORE = "usescore";
    public static final String ITEM_RGB = "itemrgb";
    private static final Pattern track_line_parser;
    private final Map<String, String> track_hash;
    
    public TrackLineParser() {
        this.track_hash = new TreeMap<String, String>();
    }
    
    public Map<String, String> getCurrentTrackHash() {
        return this.track_hash;
    }
    
    public static Color reformatColor(final String color_string) {
        final String[] rgb = TrackLineParser.comma_regex.split(color_string);
        if (rgb.length == 3) {
            final int red = Integer.parseInt(rgb[0]);
            final int green = Integer.parseInt(rgb[1]);
            final int blue = Integer.parseInt(rgb[2]);
            return new Color(red, green, blue);
        }
        return null;
    }
    
    private static String unquote(final String str) {
        final int length = str.length();
        if (length > 1 && str.charAt(0) == '\"' && str.charAt(length - 1) == '\"') {
            return str.substring(1, length - 1);
        }
        return str;
    }
    
    public Map<String, String> parseTrackLine(final String track_line) {
        return this.parseTrackLine(track_line, null);
    }
    
    public Map<String, String> parseTrackLine(final String track_line, final String track_name_prefix) {
        this.track_hash.clear();
        final Matcher matcher = TrackLineParser.track_line_parser.matcher(track_line);
        while (matcher.find() && !Thread.currentThread().isInterrupted()) {
            if (matcher.groupCount() == 2) {
                final String tag = unquote(matcher.group(1).toLowerCase().trim());
                final String val = unquote(matcher.group(2));
                this.track_hash.put(unquote(tag), unquote(val));
            }
            else {
                System.out.println("Couldn't parse this part of the track line: " + matcher.group(0));
            }
        }
        final String track_name = this.track_hash.get("name");
        if (track_name != null && track_name_prefix != null) {
            final String new_track_name = track_name_prefix + track_name;
            this.track_hash.put("name", new_track_name);
        }
        return this.track_hash;
    }
    
    public static ITrackStyle createTrackStyle(final Map<String, String> track_hash, final String default_track_name, final String file_type) {
        final String human_name = appendTrackName(track_hash, default_track_name);
        final String name = track_hash.get("name");
        final ITrackStyle style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(name, getHumanName(track_hash, name, human_name), file_type, null);
        applyTrackProperties(track_hash, style);
        return style;
    }
    
    private static String appendTrackName(final Map<String, String> track_hash, final String default_track_name) {
        String human_name = new String(track_hash.get("name"));
        String name = track_hash.get("name");
        if (name != null && (default_track_name.indexOf("/") > -1 || default_track_name.indexOf("\\\\") > -1) && !name.equals(default_track_name)) {
            String separator = "";
            if (default_track_name.indexOf("/") > -1) {
                separator = "/";
            }
            else {
                separator = "\\\\";
            }
            final String[] s = default_track_name.split(separator);
            if (s[s.length - 1].equals(name)) {
                name = default_track_name;
            }
            else {
                final StringBuilder newTrackName = new StringBuilder();
                for (int i = 0; i < s.length - 1; ++i) {
                    newTrackName.append(s[i]).append(separator);
                }
                newTrackName.append(name);
                name = newTrackName.toString();
            }
            track_hash.put("name", name);
        }
        if (name == null) {
            track_hash.put("name", default_track_name);
            human_name = default_track_name;
        }
        return human_name;
    }
    
    private static String getHumanName(final Map<String, String> track_hash, final String id, final String default_name) {
        final String description = track_hash.get("description");
        if (description != null && !description.equals(id)) {
            return description;
        }
        final String name = track_hash.get("name");
        if (name != null && !name.equals(id)) {
            return name;
        }
        return default_name;
    }
    
    private static void applyTrackProperties(final Map<String, String> track_hash, final ITrackStyle style) {
        final String visibility = track_hash.get("visibility");
        final String color_string = track_hash.get("color");
        if (color_string != null) {
            final Color color = reformatColor(color_string);
            if (color != null) {
                style.setForeground(color);
            }
        }
        final List<String> collapsed_modes = Arrays.asList("1", "dense");
        final List<String> expanded_modes = Arrays.asList("2", "full", "3", "pack", "4", "squish");
        if (visibility != null) {
            if (collapsed_modes.contains(visibility)) {
                style.setCollapsed(true);
            }
            else if (expanded_modes.contains(visibility)) {
                style.setCollapsed(false);
            }
        }
        if (style instanceof ITrackStyleExtended) {
            final ITrackStyleExtended annot_style = (ITrackStyleExtended)style;
            final String url = track_hash.get("url");
            if (url != null) {
                annot_style.setUrl(url);
            }
            if ("1".equals(track_hash.get("usescore"))) {
                annot_style.setColorByScore(true);
            }
            else if (track_hash.get("usescore") != null) {
                annot_style.setColorByScore(false);
            }
        }
        for (final String key : track_hash.keySet()) {
            final Object value = track_hash.get(key);
            style.getTransientPropertyMap().put(key, value);
        }
    }
    
    public static void createGraphStyle(final Map<String, String> track_hash, final String graph_id, final String graph_name, final String extension) {
        final GraphState gstate = DefaultStateProvider.getGlobalStateProvider().getGraphState(graph_id, getHumanName(track_hash, graph_id, graph_name), extension, null);
        applyTrackProperties(track_hash, gstate.getTierStyle());
        final String view_limits = track_hash.get("viewlimits");
        if (view_limits != null) {
            final String[] limits = view_limits.split(":");
            if (limits.length == 2) {
                final float min = Float.parseFloat(limits[0]);
                final float max = Float.parseFloat(limits[1]);
                gstate.setVisibleMinY(min);
                gstate.setVisibleMaxY(max);
            }
        }
        final String graph_type = track_hash.get("graphtype");
        if ("points".equalsIgnoreCase(graph_type)) {
            gstate.setGraphStyle(GraphType.DOT_GRAPH);
        }
        else if ("bar".equalsIgnoreCase(graph_type)) {
            gstate.setGraphStyle(GraphType.BAR_GRAPH);
        }
    }
    
    static {
        comma_regex = Pattern.compile(",");
        track_line_parser = Pattern.compile("([\\S&&[^=]]+)=((?:\"[^\"]*\")|(?:\\S+))");
    }
}
