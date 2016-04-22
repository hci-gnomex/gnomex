// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

public enum FileTypeCategory
{
    Annotation(true), 
    Alignment(true), 
    Graph(false), 
    Sequence(false), 
    Mismatch(false), 
    ProbeSet(true), 
    ScoredContainer(false);
    
    private final boolean container;
    
    private FileTypeCategory(final boolean container) {
        this.container = container;
    }
    
    public boolean isContainer() {
        return this.container;
    }
}
