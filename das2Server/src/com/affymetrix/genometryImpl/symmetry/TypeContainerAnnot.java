// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;

public final class TypeContainerAnnot extends RootSeqSymmetry implements TypedSym
{
    private static final FileTypeCategory DEFAULT_CATEGORY;
    private final String ext;
    String type;
    
    public TypeContainerAnnot(final String type) {
        this(type, "");
    }
    
    public TypeContainerAnnot(final String type, final String ext) {
        this.type = type;
        this.ext = ext;
        this.setProperty("method", type);
        this.setProperty("container sym", Boolean.TRUE);
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public FileTypeCategory getCategory() {
        FileTypeCategory category = null;
        final FileTypeHandler handler = FileTypeHolder.getInstance().getFileTypeHandler(this.ext);
        if (handler != null) {
            category = handler.getFileTypeCategory();
        }
        if (category == null) {
            category = TypeContainerAnnot.DEFAULT_CATEGORY;
        }
        return category;
    }
    
    static {
        DEFAULT_CATEGORY = FileTypeCategory.Annotation;
    }
}
