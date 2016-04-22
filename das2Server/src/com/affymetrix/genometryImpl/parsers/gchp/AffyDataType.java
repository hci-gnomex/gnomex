// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.nio.charset.Charset;

public enum AffyDataType
{
    INT8("text/x-calvin-integer-8"), 
    UINT8("text/x-calvin-unsigned-integer-8"), 
    INT16("text/x-calvin-integer-16"), 
    UINT16("text/x-calvin-unsigned-integer-16"), 
    INT32("text/x-calvin-integer-32"), 
    UINT32("text/x-calvin-unsigned-integer-32"), 
    FLOAT("text/x-calvin-float"), 
    TEXT_ASCII("text/ascii"), 
    TEXT_UTF16BE("text/plain"), 
    DOUBLE("text/x-calvin-double");
    
    static final Charset UTF16;
    static final Charset UTF8;
    String affyMimeType;
    
    private AffyDataType(final String mimeType) {
        this.affyMimeType = mimeType;
    }
    
    public static AffyDataType getType(final String mimeType) {
        for (final AffyDataType type : values()) {
            if (type.affyMimeType.equals(mimeType)) {
                return type;
            }
        }
        throw new RuntimeException("Unknown type: '" + mimeType + "'");
    }
    
    public static AffyDataType getType(final int i) {
        return values()[i];
    }
    
    static {
        UTF16 = Charset.forName("utf-16be");
        UTF8 = Charset.forName("utf-8");
    }
}
