// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import com.affymetrix.genometryImpl.general.GenericFeature;
import java.util.Map;
import java.net.URI;

public final class Das2Type
{
    private final Das2VersionedSource versioned_source;
    private final URI type_uri;
    private final String name;
    private final Map<String, String> props;
    private final Map<String, String> formats;
    private GenericFeature feature;
    
    public Das2Type(final Das2VersionedSource version, final URI type_uri, final String name, final Map<String, String> formats, final Map<String, String> props) {
        this.versioned_source = version;
        this.type_uri = type_uri;
        this.formats = formats;
        this.props = props;
        this.name = name;
    }
    
    public Das2VersionedSource getVersionedSource() {
        return this.versioned_source;
    }
    
    public URI getURI() {
        return this.type_uri;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        if (this.getName() == null) {
            return this.getURI().toString();
        }
        return this.getName();
    }
    
    public Map<String, String> getProps() {
        return this.props;
    }
    
    public Map<String, String> getFormats() {
        return this.formats;
    }
    
    public void setFeature(final GenericFeature f) {
        this.feature = f;
    }
    
    public GenericFeature getFeature() {
        return this.feature;
    }
}
