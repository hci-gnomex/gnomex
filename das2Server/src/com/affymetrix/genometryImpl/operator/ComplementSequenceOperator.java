// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import org.apache.commons.lang3.ArrayUtils;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithResidues;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class ComplementSequenceOperator implements Operator
{
    private static final char[] COMPLEMENT;
    
    @Override
    public String getName() {
        return "complement";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        final SimpleSymWithResidues residueSym = (SimpleSymWithResidues)symList.get(0).getChild(0);
        final String residues = residueSym.getResidues();
        final StringBuffer complementResidues = new StringBuffer(residues.length());
        for (int i = 0; i < residues.length(); ++i) {
            complementResidues.append(ComplementSequenceOperator.COMPLEMENT[residues.charAt(i)]);
        }
        return new SimpleSymWithResidues("complement:" + residueSym.getType(), residueSym.getBioSeq(), residueSym.getMin(), residueSym.getMax(), residueSym.getName(), residueSym.getScore(), residueSym.isForward(), residueSym.hasCdsSpan() ? residueSym.getCdsSpan().getMin() : Integer.MIN_VALUE, residueSym.hasCdsSpan() ? residueSym.getCdsSpan().getMax() : Integer.MAX_VALUE, ArrayUtils.clone(residueSym.getBlockMins()), ArrayUtils.clone(residueSym.getBlockMaxs()), complementResidues.toString());
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Sequence) ? 1 : 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Sequence) ? 1 : 0;
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
        return false;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Sequence;
    }
    
    static {
        (COMPLEMENT = new char[256])[65] = 'T';
        ComplementSequenceOperator.COMPLEMENT[84] = 'A';
        ComplementSequenceOperator.COMPLEMENT[67] = 'G';
        ComplementSequenceOperator.COMPLEMENT[71] = 'C';
        ComplementSequenceOperator.COMPLEMENT[78] = 'N';
        ComplementSequenceOperator.COMPLEMENT[97] = 't';
        ComplementSequenceOperator.COMPLEMENT[116] = 'a';
        ComplementSequenceOperator.COMPLEMENT[99] = 'g';
        ComplementSequenceOperator.COMPLEMENT[103] = 'c';
        ComplementSequenceOperator.COMPLEMENT[110] = 'n';
    }
}
