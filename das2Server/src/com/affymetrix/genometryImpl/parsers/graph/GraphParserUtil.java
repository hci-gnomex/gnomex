// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.GraphSym;

public class GraphParserUtil
{
    private static final GraphParserUtil instance;
    
    public static GraphParserUtil getInstance() {
        return GraphParserUtil.instance;
    }
    
    public List<GraphSym> wrapInList(final GraphSym gsym) {
        List<GraphSym> grafs = null;
        if (gsym != null) {
            grafs = new ArrayList<GraphSym>();
            grafs.add(gsym);
        }
        return grafs;
    }
    
    static {
        instance = new GraphParserUtil();
    }
}
