// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.text.DecimalFormat;

public final class PowerTransformer extends AbstractFloatTransformer implements Operator
{
    private static final DecimalFormat DF;
    double exponent;
    final String paramPrompt;
    final String name;
    final boolean parameterized;
    
    public PowerTransformer() {
        this.paramPrompt = "Exponent";
        this.name = "Power";
        this.parameterized = true;
    }
    
    public PowerTransformer(final Double exponent) {
        this.exponent = exponent;
        this.paramPrompt = null;
        this.name = this.getBaseName();
        this.parameterized = false;
    }
    
    private String getBaseName() {
        if (this.exponent == 0.5) {
            return "Sqrt";
        }
        return "Power" + PowerTransformer.DF.format(this.exponent);
    }
    
    @Override
    public String getParamPrompt() {
        return this.paramPrompt;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getDisplay() {
        return this.parameterized ? this.getBaseName() : this.name;
    }
    
    @Override
    public float transform(final float x) {
        return (float)Math.pow(x, this.exponent);
    }
    
    public boolean setParameter(final String s) {
        if (this.parameterized) {
            if ("sqrt".equals(s.trim().toLowerCase())) {
                this.exponent = 0.5;
            }
            else {
                try {
                    this.exponent = Double.parseDouble(s);
                }
                catch (Exception x) {
                    return false;
                }
            }
        }
        return true;
    }
    
    static {
        DF = new DecimalFormat("#,##0.##");
    }
}
