// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;

public interface Operator
{
    String getName();
    
    String getDisplay();
    
    SeqSymmetry operate(final BioSeq p0, final List<SeqSymmetry> p1);
    
    int getOperandCountMin(final FileTypeCategory p0);
    
    int getOperandCountMax(final FileTypeCategory p0);
    
    Map<String, Class<?>> getParameters();
    
    boolean setParameters(final Map<String, Object> p0);
    
    boolean supportsTwoTrack();
    
    FileTypeCategory getOutputCategory();
    
    public interface Order
    {
        int getOrder();
    }
}
