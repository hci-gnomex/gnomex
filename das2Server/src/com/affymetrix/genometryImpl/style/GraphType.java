// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.util.HashMap;
import java.util.Map;

public enum GraphType
{
    LINE_GRAPH("Line"), 
    BAR_GRAPH("Bar"), 
    DOT_GRAPH("Dot"), 
    MINMAXAVG("Min_Max_Avg"), 
    STAIRSTEP_GRAPH("Stairstep"), 
    HEAT_MAP("HeatMap"), 
    FILL_BAR_GRAPH("Fill Bar");
    
    private String humanReadable;
    private static final Map<String, GraphType> humanReadable2number;
    
    private GraphType(final String humanReadable) {
        this.humanReadable = humanReadable;
    }
    
    public static GraphType fromString(final String humanReadable) {
        final GraphType nr = GraphType.humanReadable2number.get(humanReadable);
        if (nr != null) {
            return nr;
        }
        return GraphType.LINE_GRAPH;
    }
    
    @Override
    public String toString() {
        return this.humanReadable;
    }
    
    static {
        humanReadable2number = new HashMap<String, GraphType>();
        for (final GraphType type : values()) {
            GraphType.humanReadable2number.put(type.humanReadable, type);
        }
    }
}
