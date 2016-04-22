// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.GenometryConstants;

public class IdentityTransform extends AbstractFloatTransformer implements Operator
{
    @Override
    public String getName() {
        return "copy";
    }
    
    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }
    
    @Override
    public float transform(final float x) {
        return x;
    }
}
