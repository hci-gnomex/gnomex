// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SingletonSeqSymmetry;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymSummarizer;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class IntersectionOperator extends AbstractAnnotationOperator implements Operator
{
    @Override
    public String getName() {
        return "intersection";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        final SeqSymmetry unionA = SeqSymSummarizer.getUnion(AbstractAnnotationOperator.findChildSyms(symList.get(0)), aseq);
        final SeqSymmetry unionB = SeqSymSummarizer.getUnion(AbstractAnnotationOperator.findChildSyms(symList.get(1)), aseq);
        return intersect(aseq, unionA, unionB);
    }
    
    private static SeqSymmetry intersect(final BioSeq seq, final SeqSymmetry unionA, final SeqSymmetry unionB) {
        MutableSeqSymmetry psym = new SimpleSymWithProps();
        final List<SeqSymmetry> symsAB = new ArrayList<SeqSymmetry>();
        symsAB.add(unionA);
        symsAB.add(unionB);
        final GraphSym combo_graph = SeqSymSummarizer.getSymmetrySummary(symsAB, seq, false, "");
        final int num_points = combo_graph.getPointCount();
        int current_region_start = 0;
        int current_region_end = 0;
        boolean in_region = false;
        for (int i = 0; i < num_points; ++i) {
            final int xpos = combo_graph.getGraphXCoord(i);
            final float ypos = combo_graph.getGraphYCoord(i);
            if (in_region) {
                if (ypos < 2.0f) {
                    in_region = false;
                    current_region_end = xpos;
                    final SeqSymmetry newsym = new SingletonSeqSymmetry(current_region_start, current_region_end, seq);
                    psym.addChild(newsym);
                }
            }
            else if (ypos >= 2.0f) {
                in_region = true;
                current_region_start = xpos;
            }
        }
        if (in_region) {
            System.err.println("still in a covered region at end of getUnion() loop!");
        }
        if (psym.getChildCount() <= 0) {
            psym = null;
        }
        else {
            final int pmin = psym.getChild(0).getSpan(seq).getMin();
            final int pmax = psym.getChild(psym.getChildCount() - 1).getSpan(seq).getMax();
            final SeqSpan pspan = new SimpleSeqSpan(pmin, pmax, seq);
            psym.addSpan(pspan);
        }
        return psym;
    }
}
