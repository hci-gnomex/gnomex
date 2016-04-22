// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.IOException;
import java.io.DataInputStream;
import java.util.Iterator;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AffyGenericDataHeader
{
    public CharSequence data_type_guid;
    public CharSequence unique_id;
    public String date_time;
    public String locale;
    public Map<String, AffyChpParameter> paramMap;
    public List<AffyGenericDataHeader> children;
    
    protected AffyGenericDataHeader() {
        this.paramMap = new LinkedHashMap<String, AffyChpParameter>();
        this.children = new ArrayList<AffyGenericDataHeader>();
    }
    
    public void dump(final PrintStream str) {
        str.println(this.getClass().getName());
        str.println("guid: " + (Object)this.data_type_guid);
        str.println("unique: " + (Object)this.unique_id);
        str.println("date: " + this.date_time);
        str.println("locale: " + this.locale);
        for (final AffyChpParameter param : this.paramMap.values()) {
            str.println(param.toString());
        }
        for (final AffyGenericDataHeader header : this.children) {
            str.println("----- child header ------");
            header.dump(str);
        }
    }
    
    public static AffyGenericDataHeader readHeader(final DataInputStream dis) throws IOException {
        final AffyGenericDataHeader header = new AffyGenericDataHeader();
        header.data_type_guid = AffyGenericChpFile.parseString(dis);
        header.unique_id = AffyGenericChpFile.parseString(dis);
        header.date_time = AffyGenericChpFile.parseWString(dis);
        header.locale = AffyGenericChpFile.parseWString(dis);
        for (int param_count = dis.readInt(), i = 0; i < param_count; ++i) {
            final AffyChpParameter p = AffyChpParameter.parse(dis);
            header.paramMap.put(p.name, p);
        }
        for (int moreParamMaps = dis.readInt(), j = 0; j < moreParamMaps && j < 1; ++j) {
            final AffyGenericDataHeader childHeader = readHeader(dis);
            header.children.add(childHeader);
        }
        return header;
    }
}
