// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.IntId;

public final class SingletonSymWithIntId extends MutableSingletonSeqSymmetry implements IntId
{
    int nid;
    String id_prefix;
    
    public SingletonSymWithIntId(final int start, final int end, final BioSeq seq, final String id_prefix, final int nid) {
        super(start, end, seq);
        this.id_prefix = id_prefix;
        this.nid = nid;
    }
    
    @Override
    public String getID() {
        final String rootid = Integer.toString(this.getIntID());
        if (this.id_prefix == null) {
            return rootid;
        }
        return this.id_prefix + rootid;
    }
    
    @Override
    public int getIntID() {
        return this.nid;
    }
}
