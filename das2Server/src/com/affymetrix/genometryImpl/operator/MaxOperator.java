// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.Iterator;
import java.util.List;

public class MaxOperator extends AbstractGraphOperator implements Operator
{
    @Override
    protected String getSymbol() {
        return null;
    }
    
    @Override
    protected float operate(final List<Float> operands) {
        float max = Float.MIN_VALUE;
        for (final Float f : operands) {
            max = Math.max(max, f);
        }
        return max;
    }
    
    @Override
    public String getName() {
        return "max";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
}
