// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.symmetry.SeqSymSummarizer;
import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;

public abstract class ExclusiveOperator extends XorOperator implements Operator
{
    protected SeqSymmetry operate(final BioSeq aseq, final SeqSymmetry symsA, final SeqSymmetry symB) {
        return exclusive(aseq, AbstractAnnotationOperator.findChildSyms(symsA), AbstractAnnotationOperator.findChildSyms(symB));
    }
    
    protected static SeqSymmetry exclusive(final BioSeq seq, final List<SeqSymmetry> symsA, final List<SeqSymmetry> symsB) {
        final SeqSymmetry xorSym = XorOperator.getXor(seq, symsA, symsB);
        if (xorSym == null) {
            return null;
        }
        final List<SeqSymmetry> xorList = new ArrayList<SeqSymmetry>();
        xorList.add(xorSym);
        final SeqSymmetry a_not_b = SeqSymSummarizer.getIntersection(symsA, xorList, seq);
        return a_not_b;
    }
}
