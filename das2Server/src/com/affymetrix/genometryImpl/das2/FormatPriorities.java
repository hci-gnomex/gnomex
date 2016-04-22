// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.Map;

public final class FormatPriorities
{
    private static final String[] ordered_formats;
    
    public static String getFormat(final Das2Type type) {
        if (type.getURI().toString().endsWith(".bar")) {
            return "bar";
        }
        if (type.getURI().toString().endsWith("useq")) {
            return "useq";
        }
        if (type.getURI().toString().endsWith(".bed")) {
            return "bed";
        }
        final Map<String, String> type_formats = type.getFormats();
        if (type_formats != null) {
            for (final String format : FormatPriorities.ordered_formats) {
                if (type_formats.get(format) != null) {
                    return format;
                }
            }
        }
        return null;
    }
    
    static {
        ordered_formats = new String[] { "link.psl", "ead", "bp2", "brs", "bgn", "bps", "cyt", "useq", "bed", "psl", "gff", "bar", "bam" };
    }
}
