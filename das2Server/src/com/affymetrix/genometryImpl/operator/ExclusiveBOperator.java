// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class ExclusiveBOperator extends ExclusiveOperator implements Operator
{
    @Override
    public String getName() {
        return "b_not_a";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq seq, final List<SeqSymmetry> symList) {
        return this.operate(seq, symList.get(1), symList.get(0));
    }
}
