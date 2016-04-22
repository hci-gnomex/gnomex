// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collections;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;

public class SymLoaderInstNC extends SymLoaderInst
{
    private static final List<LoadUtils.LoadStrategy> strategyList;
    
    public SymLoaderInstNC(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return SymLoaderInstNC.strategyList;
    }
    
    @Override
    public List<BioSeq> getChromosomeList() {
        return Collections.emptyList();
    }
    
    @Override
    public List<? extends SeqSymmetry> getGenome() throws Exception {
        return super.getGenome();
    }
    
    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        SymLoaderInstNC.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
