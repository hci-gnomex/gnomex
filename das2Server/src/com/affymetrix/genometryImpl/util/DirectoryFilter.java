// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.File;
import java.io.FileFilter;

public final class DirectoryFilter implements FileFilter
{
    private final FileFilter filter;
    
    public DirectoryFilter() {
        this.filter = null;
    }
    
    public DirectoryFilter(final FileFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean accept(final File pathname) {
        return pathname.isDirectory() && (this.filter == null || this.filter.accept(pathname));
    }
}
