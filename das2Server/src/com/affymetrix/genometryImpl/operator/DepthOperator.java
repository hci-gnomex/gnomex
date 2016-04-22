// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymSummarizer;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;

public class DepthOperator implements Operator
{
    private final FileTypeCategory fileTypeCategory;
    
    public DepthOperator(final FileTypeCategory fileTypeCategory) {
        this.fileTypeCategory = fileTypeCategory;
    }
    
    @Override
    public String getName() {
        return this.fileTypeCategory.toString().toLowerCase() + "_depth";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        return SeqSymSummarizer.getSymmetrySummary(symList, aseq, false, null);
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
    public boolean setParameters(final Map<String, Object> obj) {
        return false;
    }
    
    @Override
    public boolean supportsTwoTrack() {
        return false;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Graph;
    }
}
