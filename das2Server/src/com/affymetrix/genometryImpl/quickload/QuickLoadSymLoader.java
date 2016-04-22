// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.quickload;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import java.util.Iterator;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.io.IOException;
import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.util.ParserController;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.general.GenericVersion;
import com.affymetrix.genometryImpl.symloader.SymLoader;

public class QuickLoadSymLoader extends SymLoader
{
    protected final GenericVersion version;
    protected final SymLoader symL;
    protected GenometryModel gmodel;
    
    public QuickLoadSymLoader(final URI uri, final String featureName, final GenericVersion version, final SymLoader symL) {
        super(uri, featureName, null);
        this.gmodel = GenometryModel.getGenometryModel();
        this.version = version;
        this.symL = symL;
        this.isResidueLoader = (this.symL != null && this.symL.isResidueLoader());
    }
    
    @Override
    protected void init() {
        this.isInitialized = true;
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        if (this.symL != null) {
            return this.symL.getLoadChoices();
        }
        Logger.getLogger(QuickLoadSymLoader.class.getName()).log(Level.SEVERE, "No symloader found.");
        return super.getLoadChoices();
    }
    
    @Override
    public boolean isResidueLoader() {
        if (this.symL != null) {
            return this.symL.isResidueLoader();
        }
        return this.isResidueLoader;
    }
    
    public static String detemineFriendlyName(final URI uri) {
        final String uriString = uri.toASCIIString().toLowerCase();
        final String unzippedStreamName = GeneralUtils.stripEndings(uriString);
        final String ext = ParserController.getExtension(unzippedStreamName);
        final String unzippedName = GeneralUtils.getUnzippedName(uri.toString());
        final String strippedName = unzippedName.substring(unzippedName.lastIndexOf("/") + 1);
        final String friendlyName = strippedName.substring(0, strippedName.toLowerCase().indexOf(ext));
        return friendlyName;
    }
    
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan overlapSpan, final GenericFeature feature) throws OutOfMemoryError, IOException {
        try {
            if (this.symL != null && this.symL.isResidueLoader()) {
                this.loadResiduesThread(feature, overlapSpan);
                return Collections.emptyList();
            }
            return this.loadSymmetriesThread(feature, overlapSpan);
        }
        catch (Exception ex) {
            this.logException(ex);
            return Collections.emptyList();
        }
    }
    
    protected List<? extends SeqSymmetry> loadSymmetriesThread(final GenericFeature feature, final SeqSpan overlapSpan) throws OutOfMemoryError, Exception {
        if ("genome".equals(overlapSpan.getBioSeq().getID())) {
            return Collections.emptyList();
        }
        return this.loadAndAddSymmetries(feature, overlapSpan);
    }
    
    private List<? extends SeqSymmetry> loadAndAddSymmetries(final GenericFeature feature, final SeqSpan span) throws Exception, OutOfMemoryError {
        if (this.symL != null && !this.symL.getChromosomeList().contains(span.getBioSeq())) {
            return Collections.emptyList();
        }
        this.setStyle(feature);
        if (!this.isInitialized) {
            this.init();
        }
        List<? extends SeqSymmetry> results = this.getRegion(span);
        if (Thread.currentThread().isInterrupted()) {
            results = null;
            return Collections.emptyList();
        }
        if (results != null) {
            return this.addSymmtries(span, results, feature);
        }
        return Collections.emptyList();
    }
    
    public void loadAndAddAllSymmetries(final GenericFeature feature) throws OutOfMemoryError {
        this.setStyle(feature);
        if (!this.isInitialized) {
            this.init();
        }
        List<? extends SeqSymmetry> results = this.getGenome();
        if (Thread.currentThread().isInterrupted() || results == null) {
            feature.setLoadStrategy(LoadUtils.LoadStrategy.NO_LOAD);
            results = null;
            return;
        }
        this.addAllSymmetries(feature, results);
    }
    
    protected void addAllSymmetries(final GenericFeature feature, final List<? extends SeqSymmetry> results) throws OutOfMemoryError {
        final Map<BioSeq, List<SeqSymmetry>> seq_syms = SymLoader.splitResultsBySeqs(results);
        SeqSpan span = null;
        BioSeq seq = null;
        for (final Map.Entry<BioSeq, List<SeqSymmetry>> seq_sym : seq_syms.entrySet()) {
            seq = seq_sym.getKey();
            span = new SimpleSeqSpan(seq.getMin(), seq.getMax() - 1, seq);
            this.addSymmtries(span, seq_sym.getValue(), feature);
            feature.addLoadedSpanRequest(span);
        }
    }
    
    private void setStyle(final GenericFeature feature) {
        final ITrackStyleExtended style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(this.uri.toString(), this.featureName, this.extension, feature.featureProps);
        style.setFeature(feature);
    }
    
    protected List<? extends SeqSymmetry> addSymmtries(final SeqSpan span, List<? extends SeqSymmetry> results, final GenericFeature feature) {
        results = ServerUtils.filterForOverlappingSymmetries(span, results);
        return SymLoader.splitFilterAndAddAnnotation(span, results, feature);
    }
    
    private void loadResiduesThread(final GenericFeature feature, final SeqSpan span) throws Exception {
        final String results = this.getRegionResidues(span);
        if (results != null && !results.isEmpty()) {
            BioSeq.addResiduesToComposition(span.getBioSeq(), results, span);
        }
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        if (this.symL != null) {
            return this.symL.getChromosomeList();
        }
        return super.getChromosomeList();
    }
    
    @Override
    public List<? extends SeqSymmetry> getGenome() {
        try {
            BufferedInputStream bis = null;
            try {
                bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
                return this.symL.parse(bis, false);
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(QuickLoadSymLoader.class.getName()).log(Level.SEVERE, null, ex);
                GeneralUtils.safeClose(bis);
                return null;
            }
        }
        catch (Exception ex2) {
            this.logException(ex2);
            return null;
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        if (this.symL != null) {
            return this.symL.getRegion(span);
        }
        return super.getRegion(span);
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        if (this.symL != null && this.symL.isResidueLoader()) {
            return this.symL.getRegionResidues(span);
        }
        Logger.getLogger(QuickLoadSymLoader.class.getName()).log(Level.SEVERE, "Residue loading was called with a non-residue format.");
        return "";
    }
    
    public SymLoader getSymLoader() {
        return this.symL;
    }
    
    public GenericVersion getVersion() {
        return this.version;
    }
    
    public void logException(final Exception ex) {
        String loggerName = QuickLoadSymLoader.class.getName();
        Level level = Level.SEVERE;
        if (this.symL != null) {
            loggerName = this.symL.getClass().getName();
        }
        if (ex instanceof RuntimeException) {
            level = Level.WARNING;
        }
        Logger.getLogger(loggerName).log(level, ex.getMessage(), ex);
    }
}
