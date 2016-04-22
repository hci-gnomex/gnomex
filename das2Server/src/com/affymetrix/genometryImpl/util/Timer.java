// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public final class Timer
{
    private long start_time;
    private long read_time;
    
    public void start() {
        this.start_time = System.currentTimeMillis();
    }
    
    public long read() {
        this.read_time = System.currentTimeMillis();
        return this.read_time - this.start_time;
    }
    
    public void print() {
        System.out.println("Elapsed time since start: " + this.read() / 1000.0);
    }
}
