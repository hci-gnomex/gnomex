// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.general;

import com.affymetrix.genometryImpl.comparator.StringVersionDateComparator;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;

public final class GenericVersion implements Comparable<GenericVersion>
{
    public final AnnotatedSeqGroup group;
    public final String versionName;
    public final String versionID;
    public final GenericServer gServer;
    public final Object versionSourceObj;
    private final Set<GenericFeature> features;
    private boolean isInitialized;
    
    public GenericVersion(final AnnotatedSeqGroup group, final String versionID, final String versionName, final GenericServer gServer, final Object versionSourceObj) {
        this.features = new CopyOnWriteArraySet<GenericFeature>();
        this.isInitialized = false;
        this.group = group;
        this.versionID = versionID;
        this.versionName = versionName;
        this.gServer = gServer;
        this.versionSourceObj = versionSourceObj;
    }
    
    public void addFeature(final GenericFeature f) {
        this.features.add(f);
    }
    
    public boolean removeFeature(final GenericFeature f) {
        this.features.remove(f);
        return this.group.removeSeqsForUri(f.symL.uri.toString());
    }
    
    public void setInitialized() {
        this.isInitialized = true;
    }
    
    public boolean isInitialized() {
        return this.isInitialized;
    }
    
    public Set<GenericFeature> getFeatures() {
        return Collections.unmodifiableSet((Set<? extends GenericFeature>)this.features);
    }
    
    @Override
    public String toString() {
        return this.versionName;
    }
    
    @Override
    public int compareTo(final GenericVersion other) {
        return new StringVersionDateComparator().compare(this.versionName, other.versionName);
    }
    
    public void clear() {
        this.features.clear();
    }
}
