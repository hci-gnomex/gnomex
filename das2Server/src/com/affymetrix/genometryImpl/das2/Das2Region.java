// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.BioSeq;
import java.net.URI;

public final class Das2Region
{
    private final URI region_uri;
    private BioSeq aseq;
    private final Das2VersionedSource versioned_source;
    
    public Das2Region(final Das2VersionedSource source, final URI reg_uri, final String name, final String info, final int len) {
        this.region_uri = reg_uri;
        this.versioned_source = source;
        final AnnotatedSeqGroup genome = this.versioned_source.getGenome();
        if (!(genome instanceof Das2SeqGroup)) {
            this.aseq = genome.getSeq(name);
            if (this.aseq == null) {
                this.aseq = genome.getSeq(this.getID());
            }
        }
        if (this.aseq == null) {
            this.aseq = genome.addSeq(name, len);
        }
    }
    
    public String getID() {
        return this.region_uri.toString();
    }
    
    public Das2VersionedSource getVersionedSource() {
        return this.versioned_source;
    }
    
    public BioSeq getAnnotatedSeq() {
        return this.aseq;
    }
}
