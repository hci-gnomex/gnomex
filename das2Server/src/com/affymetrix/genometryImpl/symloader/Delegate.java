//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import com.affymetrix.genometryImpl.util.GraphSymUtils;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;
import com.affymetrix.genometryImpl.general.GenericFeature;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.general.GenericVersion;
import java.net.URI;
import com.affymetrix.genometryImpl.operator.Operator;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import com.affymetrix.genometryImpl.quickload.QuickLoadSymLoader;

public class Delegate extends QuickLoadSymLoader
{
    public static final String EXT = "igbtrack";
    private static final List<LoadUtils.LoadStrategy> defaultStrategyList;
    private Operator operator;
    private List<DelegateParent> dps;
    private final List<LoadUtils.LoadStrategy> strategyList;

    public Delegate(final URI uri, final String featureName, final GenericVersion version, final Operator operator, final List<DelegateParent> dps) {
        super(uri, featureName, version, null);
        this.operator = operator;
        this.dps = dps;
        this.strategyList = new ArrayList<LoadUtils.LoadStrategy>();
        if (dps != null) {
            this.extension = "igbtrack";
            this.strategyList.addAll(Delegate.defaultStrategyList);
        }
        else {
            this.extension = "";
            this.strategyList.add(LoadUtils.LoadStrategy.NO_LOAD);
        }
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return this.strategyList;
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        return this.version.group.getSeqList();
    }

    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        final List<SeqSymmetry> result = new ArrayList<SeqSymmetry>();
        final List<SeqSymmetry> syms = new ArrayList<SeqSymmetry>();
        if (this.dps.isEmpty()) {
            return result;
        }
        for (final DelegateParent dp : this.dps) {
            if (overlapSpan.getBioSeq().getAnnotation(dp.name) == null) {
                return result;
            }
            syms.add(dp.getSeqSymmetry(overlapSpan.getBioSeq()));
        }
        final SymWithProps sym = (SymWithProps)this.operator.operate(overlapSpan.getBioSeq(), syms);
        if (sym == null) {
            return result;
        }
        sym.setProperty("method", this.uri.toString());
        sym.setProperty("meth", this.uri.toString());
        sym.setProperty("id", this.uri.toString());
        result.add(sym);
        return result;
    }

    @Override
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan overlapSpan, final GenericFeature feature) throws OutOfMemoryError, IOException {
        boolean notUpdatable = false;
        if (this.dps == null || this.dps.isEmpty()) {
            return Collections.emptyList();
        }
        for (final DelegateParent dp : this.dps) {
            if (!dp.feature.isVisible()) {
                notUpdatable = true;
                Thread.currentThread().interrupt();
                break;
            }
            while (!dp.feature.isLoaded(overlapSpan)) {
                try {
                    Thread.sleep(500L);
                    continue;
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(Delegate.class.getName()).log(Level.SEVERE, null, ex);
                    return Collections.emptyList();
                }
//                break;
            }
        }
        if (notUpdatable) {
            for (final DelegateParent dp : this.dps) {
                dp.clear();
            }
            this.dps = null;
            this.operator = null;
            this.strategyList.remove(LoadUtils.LoadStrategy.VISIBLE);
            feature.setLoadStrategy(LoadUtils.LoadStrategy.NO_LOAD);
            return Collections.emptyList();
        }
        return super.loadFeatures(overlapSpan, feature);
    }

    @Override
    protected List<? extends SeqSymmetry> addSymmtries(final SeqSpan span, final List<? extends SeqSymmetry> results, final GenericFeature feature) {
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        if (results.get(0) instanceof GraphSym) {
            final GraphSym graphSym = (GraphSym)results.get(0);
            if (results.size() == 1 && graphSym.isSpecialGraph()) {
                final BioSeq seq = graphSym.getGraphSeq();
                seq.addAnnotation(graphSym);
                feature.addMethod(this.uri.toString());
                graphSym.getGraphName();
            }
            else {
                for (final SeqSymmetry feat : results) {
                    if (feat instanceof GraphSym) {
                        GraphSymUtils.addChildGraph((GraphSym)feat, this.uri.toString(), ((GraphSym)feat).getGraphName(), this.uri.toString(), span);
                        feature.addMethod(this.uri.toString());
                    }
                }
            }
            return results;
        }
        final BioSeq seq2 = span.getBioSeq();
        if (seq2.getAnnotation(this.uri.toString()) != null) {
            final TypeContainerAnnot sym = (TypeContainerAnnot)seq2.getAnnotation(this.uri.toString());
            sym.clear();
        }
        for (final SeqSymmetry feat : results) {
            seq2.addAnnotation(feat);
        }
        feature.addMethod(this.uri.toString());
        return results;
    }

    static {
        (defaultStrategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Delegate.defaultStrategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }

    public static class DelegateParent
    {
        String name;
        Boolean direction;
        GenericFeature feature;

        public DelegateParent(final String name, final Boolean direction, final GenericFeature feature) {
            this.name = name;
            this.direction = direction;
            this.feature = feature;
        }

        SeqSymmetry getSeqSymmetry(final BioSeq seq) {
            if (this.direction == null) {
                return seq.getAnnotation(this.name);
            }
            return this.getChilds(seq, this.name, this.direction);
        }

        private SeqSymmetry getChilds(final BioSeq seq, final String name, final boolean isForward) {
            final SeqSymmetry parentSym = seq.getAnnotation(name);
            final TypeContainerAnnot tca = new TypeContainerAnnot(name);
            for (int i = 0; i < parentSym.getChildCount(); ++i) {
                final SeqSymmetry sym = parentSym.getChild(i);
                final SeqSpan span = sym.getSpan(seq);
                if (span != null) {
                    if (span.getLength() != 0) {
                        if (span.isForward() == isForward) {
                            tca.addChild(sym);
                        }
                    }
                }
            }
            return tca;
        }

        void clear() {
            this.name = null;
            this.feature = null;
        }
    }
}
