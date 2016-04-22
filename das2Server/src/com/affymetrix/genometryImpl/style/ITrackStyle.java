// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.util.Map;
import java.awt.Color;

public interface ITrackStyle
{
    Color getForeground();
    
    void setForeground(final Color p0);
    
    boolean getShow();
    
    void setShow(final boolean p0);
    
    String getUniqueName();
    
    String getTrackName();
    
    void setTrackName(final String p0);
    
    String getMethodName();
    
    Color getBackground();
    
    void setBackground(final Color p0);
    
    boolean getCollapsed();
    
    void setCollapsed(final boolean p0);
    
    int getMaxDepth();
    
    void setMaxDepth(final int p0);
    
    void setHeight(final double p0);
    
    double getHeight();
    
    void setY(final double p0);
    
    double getY();
    
    boolean getExpandable();
    
    void setExpandable(final boolean p0);
    
    boolean isGraphTier();
    
    void setGraphTier(final boolean p0);
    
    Map<String, Object> getTransientPropertyMap();
    
    void copyPropertiesFrom(final ITrackStyle p0);
}
