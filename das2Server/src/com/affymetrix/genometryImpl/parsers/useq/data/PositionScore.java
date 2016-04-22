// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class PositionScore extends Position
{
    protected float score;
    
    public PositionScore(final int position, final float score) {
        super(position);
        this.score = score;
    }
    
    @Override
    public String toString() {
        return this.position + "\t" + this.score;
    }
    
    public float getScore() {
        return this.score;
    }
    
    public void setScore(final float score) {
        this.score = score;
    }
}
