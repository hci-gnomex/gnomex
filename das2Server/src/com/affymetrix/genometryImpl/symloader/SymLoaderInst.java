// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.BioSeqComparator;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;

public class SymLoaderInst extends SymLoader
{
    private final List<BioSeq> chromosomeList;
    
    public SymLoaderInst(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.chromosomeList = new ArrayList<BioSeq>();
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        super.init();
        for (final BioSeq seq : SymLoader.getChromosomes(this.uri, this.featureName, this.group.getID())) {
            this.chromosomeList.add(this.group.addSeq(seq.getID(), seq.getLength(), this.uri.toString()));
        }
        Collections.sort(this.chromosomeList, new BioSeqComparator());
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return this.chromosomeList;
    }
    
    @Override
    public List<? extends SeqSymmetry> getGenome() throws Exception {
        this.init();
        return super.getGenome();
    }
    
    @Override
    public List<? extends SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return super.getChromosome(seq);
    }
    
    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        this.init();
        return super.getRegion(overlapSpan);
    }
}
