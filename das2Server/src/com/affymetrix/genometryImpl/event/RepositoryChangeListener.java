// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

public interface RepositoryChangeListener
{
    boolean repositoryAdded(final String p0);
    
    void repositoryRemoved(final String p0);
}
