// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.Scored;

public final class ScoredSingletonSym extends SingletonSeqSymmetry implements Scored
{
    float score;
    
    public ScoredSingletonSym(final int start, final int end, final BioSeq seq, final float score) {
        super(start, end, seq);
        this.score = score;
    }
    
    @Override
    public float getScore() {
        return this.score;
    }
}
