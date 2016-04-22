//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.Iterator;
import java.util.List;
import com.affymetrix.genometryImpl.GenometryConstants;

public class SumOperator extends AbstractGraphOperator implements Operator, Operator.Order
{
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    protected String getSymbol() {
        return null;
    }

    public float operate(final List<Float> operands) {
        float total = 0.0f;
        for (final Float f : operands) {
            total += f;
        }
        return total;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
