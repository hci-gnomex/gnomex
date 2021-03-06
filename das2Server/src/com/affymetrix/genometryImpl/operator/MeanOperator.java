//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.Iterator;
import java.util.List;

public class MeanOperator extends AbstractGraphOperator implements Operator, Operator.Order
{
    @Override
    protected String getSymbol() {
        return null;
    }

    @Override
    protected float operate(final List<Float> operands) {
        float total = 0.0f;
        for (final Float f : operands) {
            total += f;
        }
        return total / operands.size();
    }

    @Override
    public String getName() {
        return "mean";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public int getOrder() {
        return 5;
    }
}
