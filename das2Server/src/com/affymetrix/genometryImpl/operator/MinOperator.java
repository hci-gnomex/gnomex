// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.Iterator;
import java.util.List;

public class MinOperator extends AbstractGraphOperator implements Operator
{
    @Override
    protected String getSymbol() {
        return null;
    }
    
    @Override
    protected float operate(final List<Float> operands) {
        float min = Float.MAX_VALUE;
        for (final Float f : operands) {
            min = Math.min(min, f);
        }
        return min;
    }
    
    @Override
    public String getName() {
        return "min";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
}
