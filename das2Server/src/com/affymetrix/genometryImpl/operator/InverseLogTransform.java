// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

public class InverseLogTransform extends AbstractLogTransform implements Operator
{
    private static final String BASE_NAME = "inverse_log";
    
    public InverseLogTransform() {
    }
    
    public InverseLogTransform(final Double base) {
        super(base);
    }
    
    @Override
    protected String getBaseName() {
        return "inverse_log";
    }
    
    @Override
    public float transform(final float x) {
        return (float)Math.pow(this.base, x);
    }
}
