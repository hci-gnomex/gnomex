// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public final class ImprovedStringCharIter implements SearchableCharIterator
{
    private final String src;
    
    public ImprovedStringCharIter(final String src) {
        this.src = src;
    }
    
    @Override
    public String substring(final int start, final int end) {
        return this.src.substring(start, end);
    }
    
    @Override
    public int indexOf(final String searchString, final int offset) {
        return this.src.indexOf(searchString, offset);
    }
    
    @Override
    public int getLength() {
        return this.src.length();
    }
}
