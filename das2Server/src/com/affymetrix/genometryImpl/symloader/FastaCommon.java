// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;

public abstract class FastaCommon extends SymLoader
{
    private static final List<String> pref_list;
    protected final List<BioSeq> chrSet;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    
    public FastaCommon(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, "", group);
        this.chrSet = new ArrayList<BioSeq>();
        this.isResidueLoader = true;
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        if (this.initChromosomes()) {
            super.init();
        }
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return FastaCommon.strategyList;
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return this.chrSet;
    }
    
    protected abstract boolean initChromosomes() throws Exception;
    
    @Override
    public List<String> getFormatPrefList() {
        return FastaCommon.pref_list;
    }
    
    static {
        (pref_list = new ArrayList<String>()).add("fa");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        FastaCommon.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }
}
