// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class PositionScoreText extends PositionScore
{
    protected String text;
    
    public PositionScoreText(final int position, final float score, final String text) {
        super(position, score);
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.position + "\t" + this.score + "\t" + this.text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
