// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Iterator;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryBuilder
{
    private final Map<String, String> parameters;
    private final String u;
    
    public QueryBuilder(final String u) {
        this.parameters = new LinkedHashMap<String, String>();
        this.u = u;
    }
    
    public void add(final String key, final String value) {
        this.parameters.put(GeneralUtils.URLEncode(key), GeneralUtils.URLEncode(value));
    }
    
    public URI build() {
        final StringBuilder query = new StringBuilder(this.u);
        for (final Map.Entry<String, String> parameter : this.parameters.entrySet()) {
            query.append(parameter.getKey());
            query.append("=");
            query.append(parameter.getValue());
            query.append(";");
        }
        return URI.create(query.toString());
    }
}
