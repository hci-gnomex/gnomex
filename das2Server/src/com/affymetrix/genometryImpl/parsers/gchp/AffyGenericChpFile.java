// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.LinkedHashMap;
import java.io.File;
import java.util.List;
import java.util.Map;

public final class AffyGenericChpFile
{
    private int magic;
    private int version;
    private int num_groups;
    private int group_0_pos;
    private Map<String, AffyChpParameter> parameterMap;
    private AffyGenericDataHeader header;
    List<AffyDataGroup> groups;
    private final File file;
    private final ChromLoadPolicy loadPolicy;
    
    private AffyGenericChpFile(final File file, final ChromLoadPolicy loadPolicy) {
        this.parameterMap = new LinkedHashMap<String, AffyChpParameter>();
        this.file = file;
        this.loadPolicy = loadPolicy;
    }
    
    static String parseWString(final DataInputStream istr) throws IOException {
        final int len = istr.readInt();
        final byte[] bytes = new byte[len * 2];
        istr.readFully(bytes);
        return makeString(bytes, AffyDataType.UTF16);
    }
    
    static CharSequence parseString(final DataInputStream istr) throws IOException {
        final int len = istr.readInt();
        final byte[] bytes = new byte[len];
        istr.readFully(bytes);
        return makeString(bytes, AffyDataType.UTF8);
    }
    
    public static AffyGenericChpFile parse(final File file, final ChromLoadPolicy loadPolicy, final InputStream istr, final boolean headerOnly) throws IOException {
        final AffyGenericChpFile chpFile = new AffyGenericChpFile(file, loadPolicy);
        if (file != null) {
            if (headerOnly) {
                Logger.getLogger(AffyGenericChpFile.class.getName()).log(Level.INFO, "Parsing header of file: {0}", file.getName());
            }
            else {
                Logger.getLogger(AffyGenericChpFile.class.getName()).log(Level.INFO, "Parsing file: {0}", file.getName());
            }
        }
        final DataInputStream dis = new DataInputStream(istr);
        chpFile.magic = dis.readUnsignedByte();
        chpFile.version = dis.readUnsignedByte();
        if (chpFile.magic != 59) {
            throw new IOException("Error in chp file format: wrong magic number: " + chpFile.magic);
        }
        chpFile.num_groups = dis.readInt();
        chpFile.group_0_pos = dis.readInt();
        chpFile.parameterMap = new LinkedHashMap<String, AffyChpParameter>();
        chpFile.header = AffyGenericDataHeader.readHeader(dis);
        chpFile.groups = new ArrayList<AffyDataGroup>(chpFile.num_groups);
        if (!headerOnly) {
            for (int i = 0; i < chpFile.num_groups; ++i) {
                final AffyDataGroup group = AffyDataGroup.parse(chpFile, dis);
                chpFile.groups.add(group);
            }
        }
        return chpFile;
    }
    
    static String makeString(final byte[] bytes, final Charset charset) {
        String s = null;
        try {
            s = new String(bytes, charset.name());
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return "String could not be parsed: charset " + charset.name() + " not known";
        }
        final int index = s.indexOf(0);
        if (index >= 0) {
            s = new String(s.substring(0, index));
        }
        return s;
    }
    
    ChromLoadPolicy getLoadPolicy() {
        return this.loadPolicy;
    }
}
