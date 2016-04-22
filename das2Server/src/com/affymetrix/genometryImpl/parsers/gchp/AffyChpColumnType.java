// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.PrintStream;

public final class AffyChpColumnType
{
    String name;
    AffyDataType type;
    int size;
    
    public AffyChpColumnType(final String name, final byte type, final int size) {
        this.name = name;
        this.type = AffyDataType.getType(type);
        this.size = size;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.name + ", " + this.type + ", " + this.size;
    }
    
    void dump(final PrintStream str) {
        str.println(this.toString());
    }
}
