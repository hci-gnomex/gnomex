// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SimpleScoredSymWithProps;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;

public class SummaryOperator implements Operator
{
    private final FileTypeCategory fileTypeCategory;
    
    public SummaryOperator(final FileTypeCategory fileTypeCategory) {
        this.fileTypeCategory = fileTypeCategory;
    }
    
    @Override
    public String getName() {
        return this.fileTypeCategory.toString().toLowerCase() + "_summary";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.isEmpty()) {
            return new SimpleSymWithProps();
        }
        final SeqSymmetry topSym = symList.get(0);
        final List<SeqSymmetry> syms = new ArrayList<SeqSymmetry>();
        for (int i = 0; i < topSym.getChildCount(); ++i) {
            syms.add(topSym.getChild(i));
        }
        Collections.sort(syms, new SeqSymMinComparator(aseq));
        final SimpleSymWithProps result = new SimpleScoredSymWithProps(0.0f);
        final List<SeqSymmetry> temp = new ArrayList<SeqSymmetry>();
        double lastMax = syms.get(0).getSpan(aseq).getMax();
        for (final SeqSymmetry sym : syms) {
            final SeqSpan currentSpan = sym.getSpan(aseq);
            if (currentSpan.getMin() > lastMax) {
                final MutableSeqSymmetry resultSym = new SimpleScoredSymWithProps((float)temp.size());
                SeqUtils.union(temp, resultSym, aseq);
                result.addChild(resultSym);
                lastMax = -2.147483648E9;
                temp.clear();
            }
            temp.add(sym);
            lastMax = Math.max(lastMax, currentSpan.getMax());
        }
        final MutableSeqSymmetry resultSym2 = new SimpleScoredSymWithProps((float)temp.size());
        SeqUtils.union(temp, resultSym2, aseq);
        result.addChild(resultSym2);
        temp.clear();
        syms.clear();
        return result;
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == this.fileTypeCategory) ? 1 : 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == this.fileTypeCategory) ? 1 : 0;
    }
    
    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }
    
    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        return false;
    }
    
    @Override
    public boolean supportsTwoTrack() {
        return true;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Annotation;
    }
}
