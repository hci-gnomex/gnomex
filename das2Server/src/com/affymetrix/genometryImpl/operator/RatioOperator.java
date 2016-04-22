//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.List;

public class RatioOperator extends AbstractGraphOperator implements Operator, Operator.Order
{
    @Override
    protected String getSymbol() {
        return "/";
    }

    @Override
    protected float operate(final List<Float> operands) {
        if (operands.get(1) == 0.0) {
            return 0.0f;
        }
        return operands.get(0) / operands.get(1);
    }

    @Override
    public String getName() {
        return "ratio";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
