// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.awt.Color;

public class HeatMap
{
    public static final String FOREGROUND_BACKGROUND = "FG/BG";
    public static final String PREF_HEATMAP_NAME = "Default Heatmap";
    public static final StandardHeatMap def_heatmap_name;
    private final String name;
    protected final Color[] colors;
    
    public HeatMap(final String name, final Color[] colors) {
        this.name = name;
        this.colors = colors;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Color[] getColors() {
        return this.colors;
    }
    
    public Color getColor(final int heatmap_index) {
        if (heatmap_index < 0) {
            return this.colors[0];
        }
        if (heatmap_index > 255) {
            return this.colors[255];
        }
        return this.colors[heatmap_index];
    }
    
    public static HeatMap getStandardHeatMap(final String name) {
        try {
            return StandardHeatMap.valueOf(name).getHeatMap();
        }
        catch (IllegalArgumentException e) {
            for (final StandardHeatMap s : StandardHeatMap.values()) {
                if (s.toString().equals(name)) {
                    return s.getHeatMap();
                }
            }
            throw e;
        }
    }
    
    public static HeatMap makeLinearHeatmap(final String name, final Color low, final Color high) {
        final Color[] colors = new Color[256];
        final HeatMap heat_map = new HeatMap(name, colors);
        for (int i = 0; i < 256; ++i) {
            final float x = i * 1.0f / 255.0f;
            colors[i] = interpolateColor(low, high, x);
        }
        return heat_map;
    }
    
    public static Color interpolateColor(final Color c1, final Color c2, final float x) {
        if (x <= 0.0f) {
            return c1;
        }
        if (x >= 1.0f) {
            return c2;
        }
        final int r = (int)((1.0f - x) * c1.getRed() + x * c2.getRed());
        final int g = (int)((1.0f - x) * c1.getGreen() + x * c2.getGreen());
        final int b = (int)((1.0f - x) * c1.getBlue() + x * c2.getBlue());
        final int a = (int)((1.0f - x) * c1.getAlpha() + x * c2.getAlpha());
        return new Color(r, g, b, a);
    }
    
    public static String[] getStandardNames() {
        final int length = StandardHeatMap.values().length;
        final String[] names = new String[length + 1];
        final StandardHeatMap[] shm = StandardHeatMap.values();
        names[0] = "FG/BG";
        for (int i = 1; i < length; ++i) {
            names[i] = shm[i].toString();
        }
        return names;
    }
    
    static {
        def_heatmap_name = StandardHeatMap.BLUE_YELLOW;
    }
    
    public enum StandardHeatMap
    {
        BLACK_WHITE("Black/White", Color.BLACK, Color.WHITE), 
        VIOLET("Violet", Color.BLACK, new Color(255, 0, 255)), 
        BLUE_YELLOW("Blue/Yellow", Color.BLUE, Color.YELLOW), 
        BLUE_YELLOW_2("Blue/Yellow 2", new Color(0, 0, 128), new Color(255, 255, 0)), 
        RED_BLACK_GREEN("Red/Black/Green", (Color)null, (Color)null) {
            @Override
            protected HeatMap create(final String name, final Color c1, final Color c2) {
                final Color[] colors = new Color[256];
                for (int bin = 0; bin < 256; ++bin) {
                    colors[bin] = new Color(Math.max(255 - 2 * bin, 0), Math.min(Math.max(2 * (bin - 128), 0), 255), 0);
                }
                return new HeatMap(name, colors);
            }
        }, 
        RAINBOW("Rainbow", (Color)null, (Color)null) {
            @Override
            protected HeatMap create(final String name, final Color c1, final Color c2) {
                final Color[] colors = new Color[256];
                for (int bin = 0; bin < 256; ++bin) {
                    colors[bin] = new Color(Color.HSBtoRGB(0.66f * (1.0f * bin) / 256.0f, 0.8f, 1.0f));
                }
                return new HeatMap(name, colors);
            }
        }, 
        RED_GRAY_BLUE("Red/Gray/Blue", (Color)null, (Color)null) {
            @Override
            protected HeatMap create(final String name, final Color c1, final Color c2) {
                final Color[] colors = new Color[256];
                for (int bin = 0; bin < 256; ++bin) {
                    final Color c3 = new Color(Color.HSBtoRGB(0.66f * (1.0f * bin) / 256.0f, 0.8f, 1.0f));
                    final int g = 192 * c3.getGreen() / 256;
                    colors[bin] = new Color(Math.max(c3.getRed(), g), g, Math.max(c3.getBlue(), g));
                }
                return new HeatMap(name, colors);
            }
        }, 
        TRANSPARENT_BW("Transparent B/W", new Color(0, 0, 0, 128), new Color(255, 255, 255, 128)), 
        TRANSPARENT_RED("Transparent Red", new Color(0, 0, 0, 128), new Color(255, 0, 0, 128)), 
        TRANSPARENT_GREEN("Transparent Green", new Color(0, 0, 0, 128), new Color(0, 255, 0, 128)), 
        TRANSPARENT_BLUE("Transparent Blue", new Color(0, 0, 0, 128), new Color(0, 0, 255, 128));
        
        private static final int bins = 256;
        private final HeatMap heatmap;
        
        private StandardHeatMap(final String name, final Color c1, final Color c2) {
            this.heatmap = this.create(name, c1, c2);
        }
        
        protected HeatMap create(final String name, final Color c1, final Color c2) {
            return HeatMap.makeLinearHeatmap(name, c1, c2);
        }
        
        public HeatMap getHeatMap() {
            return this.heatmap;
        }
        
        @Override
        public String toString() {
            return this.heatmap.getName();
        }
    }
}
