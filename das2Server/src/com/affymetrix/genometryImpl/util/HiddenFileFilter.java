// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.File;
import java.io.FileFilter;

public final class HiddenFileFilter implements FileFilter
{
    private final FileFilter filter;
    
    public HiddenFileFilter() {
        this.filter = null;
    }
    
    public HiddenFileFilter(final FileFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean accept(final File pathname) {
        return !pathname.isHidden() && (this.filter == null || this.filter.accept(pathname));
    }
}
