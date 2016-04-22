// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import com.affymetrix.genometryImpl.util.PreferenceUtils;
import java.util.Map;
import java.awt.Color;

public class GraphState
{
    public static final int THRESHOLD_DIRECTION_GREATER = 1;
    public static final int THRESHOLD_DIRECTION_BETWEEN = 0;
    public static final int THRESHOLD_DIRECTION_LESS_EQUAL = -1;
    public static final double DEFAULT_YPOS = 30.0;
    public static final double DEFAULT_YHEIGHT = 60.0;
    public static final Color DEFAULT_COL;
    public static final boolean DEFAULT_FLOAT = false;
    public static final boolean DEFAULT_SHOW_LABEL = true;
    public static final boolean DEFAULT_SHOW_AXIS = false;
    public static final double DEFAULT_MINVIS = Double.NEGATIVE_INFINITY;
    public static final double DEFAULT_MAXVIS = Double.POSITIVE_INFINITY;
    public static final double DEFAULT_SCORE_THRESH = 0.0;
    public static final int DEFAULT_MINRUN_THRESH = 30;
    public static final int DEFAULT_MAXGAP_THRESH = 100;
    public static final boolean DEFAULT_SHOW_THRESH = false;
    private GraphType graph_style;
    private float min_score_threshold;
    private float max_score_threshold;
    private int threshold_direction;
    private int max_gap_threshold;
    private int min_run_threshold;
    private double span_start_shift;
    private double span_end_shift;
    private int position;
    private float graph_visible_min;
    private float graph_visible_max;
    private boolean show_threshold;
    private boolean show_axis;
    private boolean show_graph;
    private boolean show_bounds;
    private boolean show_label;
    private boolean default_show_threshold;
    private boolean default_show_axis;
    private boolean default_show_label;
    private HeatMap heat_map;
    private final ITrackStyleExtended tier_style;
    private ITrackStyleExtended combo_tier_style;
    private boolean lockGraphStyle;
    public static final float default_graph_height = 100.0f;
    float[] gridLineYValues;
    
    public static GraphType getStyleNumber(final String style_name) {
        return GraphType.fromString(style_name);
    }
    
    public GraphState(final String id, final String human_name, final String extension, final Map<String, String> props) {
        this(DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(id, human_name, extension, props));
        this.tier_style.setHeight(100.0);
        this.tier_style.setGraphTier(true);
    }
    
    public GraphState(final ITrackStyleExtended tierStyle) {
        this.graph_style = GraphType.MINMAXAVG;
        this.min_score_threshold = Float.NEGATIVE_INFINITY;
        this.max_score_threshold = Float.POSITIVE_INFINITY;
        this.threshold_direction = 1;
        this.max_gap_threshold = 100;
        this.min_run_threshold = 30;
        this.span_start_shift = 0.0;
        this.span_end_shift = 1.0;
        this.position = 0;
        this.graph_visible_min = Float.NEGATIVE_INFINITY;
        this.graph_visible_max = Float.POSITIVE_INFINITY;
        this.show_threshold = false;
        this.show_axis = true;
        this.show_graph = true;
        this.show_bounds = false;
        this.show_label = false;
        this.default_show_threshold = false;
        this.default_show_axis = false;
        this.default_show_label = false;
        this.combo_tier_style = null;
        this.lockGraphStyle = false;
        this.gridLineYValues = null;
        this.tier_style = tierStyle;
    }
    
    public void restoreToDefault() {
        this.show_threshold = this.default_show_threshold;
        this.show_axis = this.default_show_axis;
        this.show_label = this.default_show_label;
    }
    
    public void copyProperties(final GraphState ostate) {
        this.setGraphStyle(ostate.getGraphStyle());
        this.setVisibleMinY(ostate.getVisibleMinY());
        this.setVisibleMaxY(ostate.getVisibleMaxY());
        this.setShowThreshold(ostate.getShowThreshold());
        this.setShowAxis(ostate.getShowAxis());
        this.setShowGraph(ostate.getShowGraph());
        this.setShowBounds(ostate.getShowBounds());
        this.setShowLabel(ostate.getShowLabel());
        this.setThresholdDirection(ostate.getThresholdDirection());
        this.setMinScoreThreshold(ostate.getMinScoreThreshold());
        this.setMaxScoreThreshold(ostate.getMaxScoreThreshold());
        this.setMaxGapThreshold(ostate.getMaxGapThreshold());
        this.setMinRunThreshold(ostate.getMinRunThreshold());
        this.setThreshStartShift(ostate.getThreshStartShift());
        this.setThreshEndShift(ostate.getThreshEndShift());
        this.setHeatMap(ostate.getHeatMap());
        this.getTierStyle().copyPropertiesFrom(ostate.getTierStyle());
    }
    
    public final GraphType getGraphStyle() {
        return this.graph_style;
    }
    
    public HeatMap getHeatMap() {
        if (this.heat_map == null) {
            final String preferredHeatMap = PreferenceUtils.getTopNode().get("Default Heatmap", HeatMap.def_heatmap_name.name());
            this.heat_map = HeatMap.getStandardHeatMap(preferredHeatMap);
        }
        return this.heat_map;
    }
    
    public final float getVisibleMinY() {
        return this.graph_visible_min;
    }
    
    public final float getVisibleMaxY() {
        return this.graph_visible_max;
    }
    
    public final boolean getShowThreshold() {
        return this.show_threshold;
    }
    
    public final boolean getShowAxis() {
        return this.show_axis;
    }
    
    public final boolean getShowGraph() {
        return this.show_graph;
    }
    
    public final boolean getShowBounds() {
        return this.show_bounds;
    }
    
    public final boolean getShowLabel() {
        return this.show_label;
    }
    
    public float getMinScoreThreshold() {
        return this.min_score_threshold;
    }
    
    public float getMaxScoreThreshold() {
        return this.max_score_threshold;
    }
    
    public int getMaxGapThreshold() {
        return this.max_gap_threshold;
    }
    
    public int getMinRunThreshold() {
        return this.min_run_threshold;
    }
    
    public double getThreshStartShift() {
        return this.span_start_shift;
    }
    
    public double getThreshEndShift() {
        return this.span_end_shift;
    }
    
    public boolean getGraphStyleLocked() {
        return this.lockGraphStyle;
    }
    
    public int getThresholdDirection() {
        return this.threshold_direction;
    }
    
    public final void setGraphStyle(final GraphType style) {
        this.graph_style = style;
    }
    
    public final void setHeatMap(final HeatMap hmap) {
        this.heat_map = hmap;
    }
    
    public final void setVisibleMinY(final float vminy) {
        this.graph_visible_min = vminy;
    }
    
    public final void setVisibleMaxY(final float vmaxy) {
        this.graph_visible_max = vmaxy;
    }
    
    public final void setShowThreshold(final boolean b) {
        this.show_threshold = b;
    }
    
    public final void setShowAxis(final boolean b) {
        this.show_axis = b;
    }
    
    public final void setShowGraph(final boolean b) {
        this.show_graph = b;
    }
    
    public final void setShowBounds(final boolean b) {
        this.show_bounds = b;
    }
    
    public final void setShowLabel(final boolean b) {
        this.show_label = b;
    }
    
    public void setMinScoreThreshold(final float thresh) {
        this.min_score_threshold = thresh;
    }
    
    public void setMaxScoreThreshold(final float thresh) {
        this.max_score_threshold = thresh;
    }
    
    public void setMaxGapThreshold(final int thresh) {
        this.max_gap_threshold = thresh;
    }
    
    public void setMinRunThreshold(final int thresh) {
        this.min_run_threshold = thresh;
    }
    
    public void setThreshStartShift(final double d) {
        this.span_start_shift = d;
    }
    
    public void setThreshEndShift(final double d) {
        this.span_end_shift = d;
    }
    
    public void lockGraphStyle() {
        this.lockGraphStyle = true;
    }
    
    public void setThresholdDirection(final int d) {
        if (d != 1 && d != -1 && d != 0) {
            throw new IllegalArgumentException();
        }
        this.threshold_direction = d;
    }
    
    public ITrackStyleExtended getTierStyle() {
        return this.tier_style;
    }
    
    public ITrackStyleExtended getComboStyle() {
        return this.combo_tier_style;
    }
    
    public void setComboStyle(final ITrackStyleExtended s, final int pos) {
        this.combo_tier_style = s;
        this.position = pos;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public void setGridLinesYValues(final float[] f) {
        this.gridLineYValues = f;
    }
    
    public float[] getGridLinesYValues() {
        return this.gridLineYValues;
    }
    
    public static boolean isHeatMapStyle(final GraphType graph_style) {
        return graph_style == GraphType.HEAT_MAP;
    }
    
    static {
        DEFAULT_COL = Color.lightGray;
    }
}
