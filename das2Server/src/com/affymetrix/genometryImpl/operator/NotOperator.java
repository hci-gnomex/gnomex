// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SingletonSeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymSummarizer;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class NotOperator implements Operator
{
    @Override
    public String getName() {
        return "not";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq seq, final List<SeqSymmetry> symList) {
        return getNot(symList, seq, true);
    }
    
    private static SeqSymmetry getNot(final List<SeqSymmetry> syms, final BioSeq seq, final boolean include_ends) {
        final SeqSymmetry union = SeqSymSummarizer.getUnion(syms, seq);
        if (union == null) {
            return null;
        }
        final int spanCount = union.getChildCount();
        if (!include_ends && spanCount <= 1) {
            return null;
        }
        final MutableSeqSymmetry invertedSym = new SimpleSymWithProps();
        if (include_ends) {
            if (spanCount < 1) {
                invertedSym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
                return invertedSym;
            }
            final SeqSpan firstSpan = union.getChild(0).getSpan(seq);
            if (firstSpan.getMin() > 0) {
                final SeqSymmetry beforeSym = new SingletonSeqSymmetry(0, firstSpan.getMin(), seq);
                invertedSym.addChild(beforeSym);
            }
        }
        for (int i = 0; i < spanCount - 1; ++i) {
            final SeqSpan preSpan = union.getChild(i).getSpan(seq);
            final SeqSpan postSpan = union.getChild(i + 1).getSpan(seq);
            final SeqSymmetry gapSym = new SingletonSeqSymmetry(preSpan.getMax(), postSpan.getMin(), seq);
            invertedSym.addChild(gapSym);
        }
        if (include_ends) {
            final SeqSpan lastSpan = union.getChild(spanCount - 1).getSpan(seq);
            if (lastSpan.getMax() < seq.getLength()) {
                final SeqSymmetry afterSym = new SingletonSeqSymmetry(lastSpan.getMax(), seq.getLength(), seq);
                invertedSym.addChild(afterSym);
            }
        }
        if (include_ends) {
            invertedSym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
        }
        else {
            final int min = union.getChild(0).getSpan(seq).getMax();
            final int max = union.getChild(spanCount - 1).getSpan(seq).getMin();
            invertedSym.addSpan(new SimpleSeqSpan(min, max, seq));
        }
        return invertedSym;
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Annotation) ? 1 : 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Annotation) ? 1 : 0;
    }
    
    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }
    
    @Override
    public boolean setParameters(final Map<String, Object> obj) {
        return false;
    }
    
    @Override
    public boolean supportsTwoTrack() {
        return false;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Annotation;
    }
}
