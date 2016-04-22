// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class PositionText extends Position
{
    protected String text;
    
    public PositionText(final int position, final String text) {
        super(position);
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.position + "\t" + this.text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
