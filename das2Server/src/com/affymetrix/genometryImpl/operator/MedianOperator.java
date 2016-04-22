//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class MedianOperator extends AbstractGraphOperator implements Operator, Operator.Order
{
    @Override
    protected String getSymbol() {
        return null;
    }

    @Override
    protected float operate(final List<Float> operands) {
        final List<Float> sortOperands = new ArrayList<Float>(operands);
        Collections.sort(sortOperands);
        final float median = (operands.size() % 2 == 0) ? ((float)((sortOperands.get(operands.size() / 2 - 1) + sortOperands.get(operands.size() / 2)) / 2.0)) : sortOperands.get((operands.size() - 1) / 2);
        return median;
    }

    @Override
    public String getName() {
        return "median";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public int getOrder() {
        return 6;
    }
}
