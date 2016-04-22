//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithResidues;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;

public class ResidueTrackSymLoader extends SymLoader
{
    private final SymLoader symL;

    public ResidueTrackSymLoader(final SymLoader loader) {
        super(loader.uri, loader.featureName, loader.group);
        this.symL = loader;
    }

    public void init() throws Exception {
        this.symL.init();
        this.isInitialized = true;
    }

    public void loadAsReferenceSequence(final boolean bool) {
        this.isResidueLoader = bool;
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return this.symL.getLoadChoices();
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return this.symL.getChromosomeList();
    }

    @Override
    public List<? extends SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.getResidueTrack(new SimpleSeqSpan(0, seq.getLength(), seq));
    }

    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        this.init();
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("Residue SymLoaderProgressUpdater getRegion for " + this.uri + " - " + overlapSpan, overlapSpan);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        return this.getResidueTrack(overlapSpan);
    }

    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        return this.symL.getRegionResidues(span);
    }

    private List<? extends SeqSymmetry> getResidueTrack(final SeqSpan span) throws Exception {
        final List<SeqSymmetry> list = new ArrayList<SeqSymmetry>();
        final SymWithProps sym = new SimpleSymWithResidues(this.uri.toString(), span.getBioSeq(), span.getStart(), span.getEnd(), "", 0.0f, span.isForward(), 0, 0, null, null, this.getRegionResidues(span));
        list.add(sym);
        return list;
    }
}
