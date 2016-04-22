// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class RegionText extends Region
{
    protected String text;
    private static final long serialVersionUID = 1L;
    
    public RegionText(final int start, final int stop, final String text) {
        super(start, stop);
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.start + "\t" + this.stop + "\t" + this.text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
