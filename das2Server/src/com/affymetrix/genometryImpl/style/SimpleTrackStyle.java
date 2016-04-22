// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.general.GenericFeature;
import java.awt.Color;

public final class SimpleTrackStyle extends DefaultTrackStyle implements ITrackStyleExtended
{
    private double reverseHeight;
    private float track_name_size;
    private Color label_foreGround;
    private Color label_backGround;
    private int directionType;
    private Color forwardColor;
    private Color reverseColor;
    private int reverseMaxDepth;
    String url;
    boolean colorByScore;
    int depth;
    boolean separate;
    boolean separable;
    String labelField;
    private String summary_view_mode;
    private String detail_view_mode;
    
    @Override
    public void setReverseHeight(final double theNewHeight) {
        this.reverseHeight = theNewHeight;
    }
    
    @Override
    public double getReverseHeight() {
        return this.reverseHeight;
    }
    
    @Override
    public void setForwardHeight(final double theNewHeight) {
        super.setHeight(theNewHeight);
    }
    
    @Override
    public double getForwardHeight() {
        return super.getHeight();
    }
    
    @Override
    public void setReverseMaxDepth(final int theNewDepth) {
        this.reverseMaxDepth = theNewDepth;
    }
    
    @Override
    public int getReverseMaxDepth() {
        return this.reverseMaxDepth;
    }
    
    @Override
    public void setForwardMaxDepth(final int theNewDepth) {
        final int rd = this.getMaxDepth();
        this.setMaxDepth(theNewDepth);
        this.reverseMaxDepth = rd;
    }
    
    @Override
    public int getForwardMaxDepth() {
        return this.getMaxDepth();
    }
    
    @Override
    public void setHeight(final double theNewHeight) {
        super.setHeight(theNewHeight);
        this.reverseHeight = super.getHeight();
    }
    
    public SimpleTrackStyle(final String name, final boolean is_graph) {
        super(name, is_graph);
        this.label_foreGround = null;
        this.label_backGround = null;
        this.reverseMaxDepth = 0;
        this.colorByScore = false;
        this.depth = 2;
        this.separate = true;
        this.separable = true;
        this.labelField = "id";
        this.summary_view_mode = null;
        this.detail_view_mode = null;
        this.reverseHeight = super.getHeight();
    }
    
    @Override
    public void setUrl(final String url) {
        this.url = url;
    }
    
    @Override
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public void setColorByScore(final boolean b) {
        this.colorByScore = b;
    }
    
    @Override
    public boolean getColorByScore() {
        return this.colorByScore;
    }
    
    @Override
    public Color getScoreColor(final float f) {
        return this.getForeground();
    }
    
    @Override
    public void setGlyphDepth(final int i) {
        this.depth = i;
    }
    
    @Override
    public int getGlyphDepth() {
        return this.depth;
    }
    
    @Override
    public void setSeparate(final boolean b) {
        this.separate = b;
    }
    
    @Override
    public boolean getSeparate() {
        return this.separate;
    }
    
    @Override
    public void setSeparable(final boolean b) {
        this.separable = b;
    }
    
    @Override
    public boolean getSeparable() {
        return this.separable;
    }
    
    @Override
    public void setLabelField(final String s) {
        this.labelField = s;
    }
    
    @Override
    public String getLabelField() {
        return this.labelField;
    }
    
    @Override
    public void copyPropertiesFrom(final ITrackStyle g) {
        super.copyPropertiesFrom(g);
        if (g instanceof ITrackStyleExtended) {
            final ITrackStyleExtended as = (ITrackStyleExtended)g;
            this.setUrl(as.getUrl());
            this.setColorByScore(as.getColorByScore());
            this.setGlyphDepth(as.getGlyphDepth());
            this.setSeparate(as.getSeparate());
            this.setLabelField(as.getLabelField());
            this.setSummaryThreshold(as.getSummaryThreshold());
        }
    }
    
    @Override
    public boolean drawCollapseControl() {
        return this.getExpandable();
    }
    
    @Override
    public GenericFeature getFeature() {
        return null;
    }
    
    @Override
    public String getSummaryViewMode() {
        return this.summary_view_mode;
    }
    
    @Override
    public void setSummaryViewMode(final String summary_view_mode) {
        this.summary_view_mode = summary_view_mode;
    }
    
    @Override
    public String getDetailViewMode() {
        return this.detail_view_mode;
    }
    
    @Override
    public void setDetailViewMode(final String detail_view_mode) {
        this.detail_view_mode = detail_view_mode;
    }
    
    @Override
    public void setFeature(final GenericFeature f) {
    }
    
    @Override
    public String getFileType() {
        return "";
    }
    
    @Override
    public FileTypeCategory getFileTypeCategory() {
        return null;
    }
    
    @Override
    public int getDirectionType() {
        return this.directionType;
    }
    
    @Override
    public void setDirectionType(final int ordinal) {
        this.directionType = ordinal;
    }
    
    @Override
    public void setForwardColor(final Color c) {
        this.forwardColor = c;
    }
    
    @Override
    public Color getForwardColor() {
        return this.forwardColor;
    }
    
    @Override
    public void setReverseColor(final Color c) {
        this.reverseColor = c;
    }
    
    @Override
    public Color getReverseColor() {
        return this.reverseColor;
    }
    
    @Override
    public final boolean getFloatTier() {
        return false;
    }
    
    @Override
    public int getSummaryThreshold() {
        return 0;
    }
    
    @Override
    public final void setFloatTier(final boolean b) {
    }
    
    @Override
    public void setTrackNameSize(final float font_size) {
        this.track_name_size = font_size;
    }
    
    @Override
    public float getTrackNameSize() {
        return this.track_name_size;
    }
    
    @Override
    public Color getLabelForeground() {
        if (this.label_foreGround == null) {
            return this.getForeground();
        }
        return this.label_foreGround;
    }
    
    @Override
    public Color getLabelBackground() {
        if (this.label_backGround == null) {
            return this.getBackground();
        }
        return this.label_backGround;
    }
    
    @Override
    public void setLabelForeground(final Color c) {
        this.label_foreGround = c;
    }
    
    @Override
    public void setLabelBackground(final Color c) {
        this.label_backGround = c;
    }
    
    @Override
    public void setSummaryThreshold(final int level) {
    }
}
