// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;

public abstract class AbstractAnnotationOperator implements Operator
{
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Annotation) ? 2 : 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Annotation) ? 2 : 0;
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
        return FileTypeCategory.Annotation;
    }
    
    protected static List<SeqSymmetry> findChildSyms(final SeqSymmetry sym) {
        final List<SeqSymmetry> childSyms = new ArrayList<SeqSymmetry>();
        for (int i = 0; i < sym.getChildCount(); ++i) {
            childSyms.add(sym.getChild(i));
        }
        return childSyms;
    }
}
