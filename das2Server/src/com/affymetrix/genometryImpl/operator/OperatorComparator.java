// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.Comparator;

public class OperatorComparator implements Comparator<Operator>
{
    @Override
    public int compare(final Operator o1, final Operator o2) {
        if (o1 instanceof Operator.Order && o2 instanceof Operator.Order) {
            if (((Operator.Order)o1).getOrder() == ((Operator.Order)o2).getOrder()) {
                return 0;
            }
            if (((Operator.Order)o1).getOrder() > ((Operator.Order)o2).getOrder()) {
                return 1;
            }
            return -1;
        }
        else {
            if (o1 instanceof Operator.Order && !(o2 instanceof Operator.Order)) {
                return -1;
            }
            if (!(o1 instanceof Operator.Order) && o2 instanceof Operator.Order) {
                return 1;
            }
            int c = o1.getDisplay().compareTo(o2.getDisplay());
            if (c == 0) {
                c = o1.getName().compareTo(o2.getName());
            }
            return c;
        }
    }
}
