// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;

public final class Das2SeqGroup extends AnnotatedSeqGroup
{
    private final Das2VersionedSource original_version;
    
    public Das2SeqGroup(final Das2VersionedSource version, final String gid) {
        super(gid);
        this.original_version = version;
    }
    
    private void ensureSeqsLoaded() {
        this.original_version.getSegments();
    }
    
    @Override
    public List<BioSeq> getSeqList() {
        this.ensureSeqsLoaded();
        return super.getSeqList();
    }
    
    @Override
    public BioSeq getSeq(final int index) {
        this.ensureSeqsLoaded();
        return super.getSeq(index);
    }
    
    @Override
    public int getSeqCount() {
        this.ensureSeqsLoaded();
        return super.getSeqCount();
    }
    
    @Override
    public BioSeq getSeq(final String synonym) {
        this.ensureSeqsLoaded();
        return super.getSeq(synonym);
    }
    
    @Override
    public BioSeq getSeq(final SeqSymmetry sym) {
        this.ensureSeqsLoaded();
        return super.getSeq(sym);
    }
}
