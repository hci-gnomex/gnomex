// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.PrintStream;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.DataInputStream;

public final class AffyChpParameter
{
    String name;
    byte[] valueBytes;
    AffyDataType type;
    
    static AffyChpParameter parse(final DataInputStream dis) throws IOException {
        final AffyChpParameter param = new AffyChpParameter();
        param.name = AffyGenericChpFile.parseWString(dis);
        param.valueBytes = parseValueString(dis);
        param.type = AffyDataType.getType(AffyGenericChpFile.parseWString(dis));
        return param;
    }
    
    private static byte[] parseValueString(final DataInputStream istr) throws IOException {
        final int len = istr.readInt();
        final byte[] bytes = new byte[len];
        istr.readFully(bytes);
        return bytes;
    }
    
    @Override
    public String toString() {
        return "Parameter:  Name: " + this.name + " type: " + this.type.affyMimeType + " value: " + this.getValue();
    }
    
    Object getValue() {
        Object result = this.valueBytes;
        final ByteBuffer bb = ByteBuffer.wrap(this.valueBytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        switch (this.type) {
            case INT8: {
                result = (char)this.valueBytes[0];
                break;
            }
            case UINT8: {
                result = (char)this.valueBytes[0];
                break;
            }
            case INT16: {
                result = bb.getShort();
                break;
            }
            case UINT16: {
                result = (char)bb.getShort();
                break;
            }
            case INT32: {
                result = bb.getInt(0);
                break;
            }
            case UINT32: {
                throw new RuntimeException("Can't do type UINT32");
            }
            case FLOAT: {
                result = new Float(bb.getFloat(0));
                break;
            }
            case TEXT_ASCII: {
                result = AffyGenericChpFile.makeString(this.valueBytes, AffyDataType.UTF8);
                break;
            }
            case TEXT_UTF16BE: {
                result = AffyGenericChpFile.makeString(this.valueBytes, AffyDataType.UTF16);
                break;
            }
            default: {
                result = this.valueBytes;
                break;
            }
        }
        return result;
    }
    
    void dump(final PrintStream str) {
        str.println(this.toString() + "<" + this.getValue().getClass().getName() + ">");
    }
}
