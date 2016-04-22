// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

class TargetBean
{
    private String id;
    private String name;
    private int start;
    private int stop;
    
    TargetBean() {
        this.clear();
    }
    
    void setID(final String id) {
        this.id = id.intern();
    }
    
    String getID() {
        return this.id;
    }
    
    void setName(final String name) {
        this.name = name.intern();
    }
    
    String getName() {
        return this.name;
    }
    
    void setStart(final String start) {
        this.start = Integer.parseInt(start) - 1;
    }
    
    int getStart() {
        return this.start;
    }
    
    void setStop(final String stop) {
        this.stop = Integer.parseInt(stop);
    }
    
    int getStop() {
        return this.stop;
    }
    
    void clear() {
        this.id = "";
        this.name = "";
        this.start = 0;
        this.stop = 0;
    }
}
