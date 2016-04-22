// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

public class Position implements Comparable<Position>
{
    protected int position;
    
    public Position(final int position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.position);
    }
    
    @Override
    public int compareTo(final Position other) {
        if (this.position < other.position) {
            return -1;
        }
        if (this.position > other.position) {
            return 1;
        }
        return 0;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public boolean isContainedBy(final int beginningBP, final int endingBP) {
        return this.position >= beginningBP && this.position < endingBP;
    }
}
