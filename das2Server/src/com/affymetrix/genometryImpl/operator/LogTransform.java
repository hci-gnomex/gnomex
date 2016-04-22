// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

public final class LogTransform extends AbstractLogTransform implements Operator
{
    private static final String BASE_NAME = "log";
    double LN_BASE;
    float LOG_1;
    
    public LogTransform() {
    }
    
    public LogTransform(final Double base) {
        super(base);
        this.LN_BASE = Math.log(base);
        this.LOG_1 = (float)(Math.log(1.0) / this.LN_BASE);
    }
    
    @Override
    protected String getBaseName() {
        return "log";
    }
    
    @Override
    public float transform(final float x) {
        return (x <= 1.0f) ? this.LOG_1 : ((float)(Math.log(x) / this.LN_BASE));
    }
    
    @Override
    protected boolean setParameter(final String s) {
        if (this.parameterized && super.setParameter(s) && !"e".equals(s.trim().toLowerCase())) {
            this.LN_BASE = Math.log(this.base);
            this.LOG_1 = (float)(Math.log(1.0) / this.LN_BASE);
        }
        return true;
    }
}
