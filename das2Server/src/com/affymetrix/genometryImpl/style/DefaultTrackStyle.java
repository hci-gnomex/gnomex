// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

public class DefaultTrackStyle implements ITrackStyle
{
    Color fg;
    Color bg;
    boolean show;
    boolean collapsed;
    boolean expandable;
    int max_depth;
    protected String unique_name;
    String human_name;
    double height;
    double y;
    boolean is_graph;
    Map<String, Object> transient_properties;
    
    public DefaultTrackStyle() {
        this.fg = Color.WHITE;
        this.bg = Color.BLACK;
        this.show = true;
        this.collapsed = false;
        this.expandable = true;
        this.max_depth = 0;
        this.unique_name = "";
        this.human_name = "";
        this.height = 60.0;
        this.y = 0.0;
        this.is_graph = false;
        this.transient_properties = null;
        this.unique_name = Integer.toHexString(this.hashCode());
    }
    
    public DefaultTrackStyle(final String name, final boolean graph) {
        this();
        this.unique_name = name.toLowerCase();
        this.human_name = name;
        this.setGraphTier(graph);
    }
    
    @Override
    public boolean isGraphTier() {
        return this.is_graph;
    }
    
    @Override
    public void setGraphTier(final boolean b) {
        this.is_graph = b;
    }
    
    @Override
    public Color getForeground() {
        return this.fg;
    }
    
    @Override
    public void setForeground(final Color c) {
        this.fg = c;
    }
    
    @Override
    public boolean getShow() {
        return this.show;
    }
    
    @Override
    public void setShow(final boolean b) {
        this.show = b;
    }
    
    @Override
    public String getUniqueName() {
        return this.unique_name;
    }
    
    @Override
    public String getTrackName() {
        return this.human_name;
    }
    
    @Override
    public void setTrackName(final String s) {
        this.human_name = s;
    }
    
    @Override
    public Color getBackground() {
        return this.bg;
    }
    
    @Override
    public void setBackground(final Color c) {
        this.bg = c;
    }
    
    @Override
    public boolean getCollapsed() {
        return this.collapsed;
    }
    
    @Override
    public void setCollapsed(final boolean b) {
        this.collapsed = b;
    }
    
    @Override
    public int getMaxDepth() {
        return this.max_depth;
    }
    
    @Override
    public void setMaxDepth(final int m) {
        this.max_depth = m;
    }
    
    @Override
    public void setHeight(final double h) {
        this.height = h;
    }
    
    @Override
    public double getHeight() {
        return this.height;
    }
    
    @Override
    public void setY(final double yval) {
        this.y = yval;
    }
    
    @Override
    public double getY() {
        return this.y;
    }
    
    @Override
    public void setExpandable(final boolean b) {
        this.expandable = b;
    }
    
    @Override
    public boolean getExpandable() {
        return this.expandable;
    }
    
    @Override
    public String getMethodName() {
        return null;
    }
    
    @Override
    public Map<String, Object> getTransientPropertyMap() {
        if (this.transient_properties == null) {
            this.transient_properties = new HashMap<String, Object>();
        }
        return this.transient_properties;
    }
    
    @Override
    public void copyPropertiesFrom(final ITrackStyle g) {
        this.setGraphTier(g.isGraphTier());
        this.setForeground(g.getForeground());
        this.setShow(g.getShow());
        this.setTrackName(g.getTrackName());
        this.setBackground(g.getBackground());
        this.setCollapsed(g.getCollapsed());
        this.setMaxDepth(g.getMaxDepth());
        this.setHeight(g.getHeight());
        this.setY(g.getY());
        this.setExpandable(g.getExpandable());
        this.getTransientPropertyMap().putAll(g.getTransientPropertyMap());
    }
}
