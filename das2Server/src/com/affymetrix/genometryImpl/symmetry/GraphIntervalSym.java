// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.BioSeq;

public final class GraphIntervalSym extends GraphSym
{
    public GraphIntervalSym(final int[] x, final int[] width, final float[] y, final String id, final BioSeq seq) {
        super(x, width, y, id, seq);
    }
    
    @Override
    public int getChildCount() {
        return this.getPointCount();
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        return new ScoredSingletonSym(this.getGraphXCoord(index), this.getGraphXCoord(index) + this.getGraphWidthCoord(index), this.getGraphSeq(), this.getGraphYCoord(index));
    }
}
