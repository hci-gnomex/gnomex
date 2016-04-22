// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.HashMap;
import java.util.Map;

public class GraphMultiplexer implements Operator
{
    private static int serialNumber;
    private static final int MINIMUMINPUTGRAPHS = 2;
    private static final int MAXIMUMINPUTGRAPHS = 9;
    private Map<String, Class<?>> properties;
    private String name;
    
    public GraphMultiplexer() {
        this.properties = new HashMap<String, Class<?>>();
        this.name = this.getClass().getName();
        if (0 < GraphMultiplexer.serialNumber) {
            this.name += Integer.toString(GraphMultiplexer.serialNumber);
        }
        ++GraphMultiplexer.serialNumber;
        this.name = "multiplexer";
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getDisplay() {
        return "Multiplex";
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() < 2) {
            throw new IllegalArgumentException("Must give me at least 2 graphs.");
        }
        if (9 < symList.size()) {
            throw new IllegalArgumentException("Must give no more than 9 graphs.");
        }
        final SeqSymmetry firstOne = symList.get(0);
        if (!(firstOne instanceof GraphSym)) {
            throw new IllegalArgumentException("Can only deal with GraphSyms.");
        }
        final GraphSym paradigm = (GraphSym)firstOne;
        final int[] x = new int[paradigm.getGraphXCoords().length];
        System.arraycopy(paradigm.getGraphXCoords(), 0, x, 0, x.length);
        final float[] y = new float[paradigm.getGraphYCoords().length];
        System.arraycopy(paradigm.getGraphYCoords(), 0, y, 0, y.length);
        final GraphSym newParent = new GraphSym(x, y, "newguy", aseq);
        newParent.setProperties(paradigm.cloneProperties());
        for (final SeqSymmetry s : symList) {
            newParent.addChild(s);
        }
        return newParent;
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? 2 : 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? 9 : 0;
    }
    
    @Override
    public Map<String, Class<?>> getParameters() {
        return this.properties;
    }
    
    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        return false;
    }
    
    @Override
    public boolean supportsTwoTrack() {
        return false;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Graph;
    }
    
    public static void main(final String[] argv) {
        System.out.println("Hi from the multiplexer.");
        final Operator o = new GraphMultiplexer();
        System.out.println(o.getName());
        System.out.println(o.getDisplay());
    }
    
    static {
        GraphMultiplexer.serialNumber = 0;
    }
}
