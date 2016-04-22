// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class RegionScore extends Region
{
    private static final long serialVersionUID = 1L;
    protected float score;
    
    public RegionScore(final int start, final int stop, final float score) {
        super(start, stop);
        this.score = score;
    }
    
    @Override
    public String toString() {
        return this.start + "\t" + this.stop + "\t" + this.score;
    }
    
    public float getScore() {
        return this.score;
    }
    
    public void setScore(final float score) {
        this.score = score;
    }
}
