// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

public final class FishClonesParser extends TabDelimitedParser
{
    public static final String FILE_EXT = "fsh";
    public static final String FISH_CLONES_METHOD = "fishClones";
    
    public FishClonesParser() {
        super(-1, 0, 1, 2, -1, -1, -1, 3, false, false);
    }
}
