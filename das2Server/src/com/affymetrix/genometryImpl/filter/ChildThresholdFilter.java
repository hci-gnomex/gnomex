//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.filter;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;

public class ChildThresholdFilter implements SymmetryFilterI
{
    int threshold;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean setParam(final Object o) {
        this.threshold = (Integer)o;
        return true;
    }

    @Override
    public Object getParam() {
        return this.threshold;
    }

    @Override
    public boolean filterSymmetry(final BioSeq bioseq, final SeqSymmetry ss) {
        final SeqSpan span = ss.getSpan(bioseq);
        return span.getMax() - span.getMin() >= this.threshold;
    }
}
