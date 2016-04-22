// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SingletonSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SeqSymSummarizer;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class UnionOperator extends AbstractAnnotationOperator implements Operator
{
    @Override
    public String getName() {
        return "union";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        final List<SeqSymmetry> allSyms = new ArrayList<SeqSymmetry>();
        for (final SeqSymmetry syms : symList) {
            allSyms.addAll(AbstractAnnotationOperator.findChildSyms(syms));
        }
        final GraphSym landscape = SeqSymSummarizer.getSymmetrySummary(allSyms, aseq, true, "");
        if (landscape != null) {
            return projectLandscape(landscape);
        }
        return null;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Annotation) ? Integer.MAX_VALUE : 0;
    }
    
    private static SymWithProps projectLandscape(final GraphSym landscape) {
        final BioSeq seq = landscape.getGraphSeq();
        SimpleSymWithProps psym = new SimpleSymWithProps();
        final int num_points = landscape.getPointCount();
        int current_region_start = 0;
        int current_region_end = 0;
        boolean in_region = false;
        for (int i = 0; i < num_points; ++i) {
            final int xpos = landscape.getGraphXCoord(i);
            final float ypos = landscape.getGraphYCoord(i);
            if (in_region) {
                if (ypos <= 0.0f) {
                    in_region = false;
                    current_region_end = xpos;
                    final SeqSymmetry newsym = new SingletonSeqSymmetry(current_region_start, current_region_end, seq);
                    psym.addChild(newsym);
                }
            }
            else if (ypos > 0.0f) {
                in_region = true;
                current_region_start = xpos;
            }
        }
        if (in_region) {
            System.err.println("still in a covered region at end of projectLandscape() loop!");
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
