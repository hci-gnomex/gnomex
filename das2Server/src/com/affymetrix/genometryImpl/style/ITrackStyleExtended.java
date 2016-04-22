// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.general.GenericFeature;
import java.awt.Color;

public interface ITrackStyleExtended extends ITrackStyle
{
    public static final int NO_THRESHOLD = -1;
    
    void setUrl(final String p0);
    
    String getUrl();
    
    void setColorByScore(final boolean p0);
    
    boolean getColorByScore();
    
    Color getScoreColor(final float p0);
    
    void setGlyphDepth(final int p0);
    
    int getGlyphDepth();
    
    void setSeparate(final boolean p0);
    
    boolean getSeparate();
    
    void setSeparable(final boolean p0);
    
    boolean getSeparable();
    
    void setLabelField(final String p0);
    
    String getLabelField();
    
    void setFeature(final GenericFeature p0);
    
    GenericFeature getFeature();
    
    boolean drawCollapseControl();
    
    void setForwardColor(final Color p0);
    
    Color getForwardColor();
    
    void setReverseColor(final Color p0);
    
    Color getReverseColor();
    
    void setSummaryViewMode(final String p0);
    
    String getSummaryViewMode();
    
    void setDetailViewMode(final String p0);
    
    String getDetailViewMode();
    
    int getDirectionType();
    
    void setDirectionType(final int p0);
    
    String getFileType();
    
    FileTypeCategory getFileTypeCategory();
    
    void setForwardHeight(final double p0);
    
    double getForwardHeight();
    
    void setReverseHeight(final double p0);
    
    double getReverseHeight();
    
    void setForwardMaxDepth(final int p0);
    
    int getForwardMaxDepth();
    
    void setReverseMaxDepth(final int p0);
    
    int getReverseMaxDepth();
    
    boolean getFloatTier();
    
    void setFloatTier(final boolean p0);
    
    void setTrackNameSize(final float p0);
    
    float getTrackNameSize();
    
    Color getLabelForeground();
    
    Color getLabelBackground();
    
    void setLabelForeground(final Color p0);
    
    void setLabelBackground(final Color p0);
    
    int getSummaryThreshold();
    
    void setSummaryThreshold(final int p0);
}
