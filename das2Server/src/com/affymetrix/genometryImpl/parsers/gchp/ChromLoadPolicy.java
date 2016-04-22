// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class ChromLoadPolicy
{
    static final ChromLoadPolicy LOAD_ALL;
    static final ChromLoadPolicy LOAD_NOTHING;
    
    public abstract boolean shouldLoadChrom(final String p0);
    
    public static ChromLoadPolicy getLoadAllPolicy() {
        return ChromLoadPolicy.LOAD_ALL;
    }
    
    public static ChromLoadPolicy getLoadNothingPolicy() {
        return ChromLoadPolicy.LOAD_NOTHING;
    }
    
    public static ChromLoadPolicy getLoadListedChromosomesPolicy(final List<String> list) {
        final List<String> chromList = new ArrayList<String>(list);
        return new ChromLoadPolicy() {
            @Override
            public boolean shouldLoadChrom(final String chromName) {
                return chromList.contains(chromName);
            }
        };
    }
    
    static {
        LOAD_ALL = new ChromLoadPolicy() {
            @Override
            public boolean shouldLoadChrom(final String chromName) {
                return true;
            }
        };
        LOAD_NOTHING = new ChromLoadPolicy() {
            @Override
            public boolean shouldLoadChrom(final String chromName) {
                return false;
            }
        };
    }
}
