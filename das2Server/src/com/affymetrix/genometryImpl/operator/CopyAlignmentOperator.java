//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.util.Map;
import java.util.HashMap;
import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class CopyAlignmentOperator implements Operator, ICopy
{
    @Override
    public String getName() {
        return "copyalignment";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() != 1 || !(symList.get(0) instanceof TypeContainerAnnot)) {
            return null;
        }
        TypeContainerAnnot result = null;
        final TypeContainerAnnot t = (TypeContainerAnnot)symList.get(0);
        result = new TypeContainerAnnot(t.getType());
        for (int i = 0; i < t.getChildCount(); ++i) {
            result.addChild(t.getChild(i));
        }
        for (int i = 0; i < t.getSpanCount(); ++i) {
            result.addSpan(t.getSpan(i));
        }
        result.setProperties(new HashMap<String, Object>(t.getProperties()));
        return result;
    }

    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Alignment) ? 1 : 0;
    }

    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Alignment) ? 1 : 0;
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
        return FileTypeCategory.Alignment;
    }
}
