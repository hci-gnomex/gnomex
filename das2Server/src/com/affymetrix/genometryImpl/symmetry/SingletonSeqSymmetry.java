// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.List;

public class SingletonSeqSymmetry extends LeafSingletonSymmetry implements SeqSymmetry
{
    protected List<SeqSymmetry> children;
    
    public SingletonSeqSymmetry(final SeqSpan span) {
        super(span);
    }
    
    public SingletonSeqSymmetry(final int start, final int end, final BioSeq seq) {
        super(start, end, seq);
    }
    
    @Override
    public int getChildCount() {
        if (null != this.children) {
            return this.children.size();
        }
        return 0;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (null != this.children) {
            return this.children.get(index);
        }
        return null;
    }
}
