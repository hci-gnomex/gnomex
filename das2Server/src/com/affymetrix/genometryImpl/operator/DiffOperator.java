//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.List;
import com.affymetrix.genometryImpl.GenometryConstants;

public class DiffOperator extends AbstractGraphOperator implements Operator, Operator.Order
{
    @Override
    public String getName() {
        return "diff";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    protected float operate(final List<Float> operands) {
        return operands.get(0) - operands.get(1);
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
