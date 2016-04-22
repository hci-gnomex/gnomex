// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

public final class InverseTransformer extends AbstractFloatTransformer implements Operator
{
    final String paramPrompt;
    final String name;
    
    public InverseTransformer() {
        this.paramPrompt = null;
        this.name = "Inverse";
    }
    
    @Override
    public String getParamPrompt() {
        return null;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getDisplay() {
        return this.name;
    }
    
    @Override
    public float transform(final float x) {
        return 1.0f / x;
    }
    
    public boolean setParameter(final String s) {
        return true;
    }
}
