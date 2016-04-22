//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import com.affymetrix.genometryImpl.symmetry.UcscBedSym;
import com.affymetrix.genometryImpl.filter.UniqueLocationFilter;
import com.affymetrix.genometryImpl.filter.ChildThresholdFilter;
import com.affymetrix.genometryImpl.filter.NoIntronFilter;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.filter.SymmetryFilterI;

public class FindJunctionOperator implements Operator
{
    public static final String THRESHOLD = "threshold";
    public static final String TWOTRACKS = "twoTracks";
    public static final String UNIQUENESS = "uniqueness";
    public static final int default_threshold = 5;
    public static final boolean default_twoTracks = true;
    public static final boolean default_uniqueness = true;
    private static final SymmetryFilterI noIntronFilter;
    private static final SymmetryFilterI childThresholdFilter;
    private static final SymmetryFilterI uniqueLocationFilter;
    private int threshold;
    private boolean twoTracks;
    private boolean uniqueness;

    public FindJunctionOperator() {
        this.threshold = 5;
        this.twoTracks = true;
        this.uniqueness = true;
    }

    @Override
    public String getName() {
        return "findjunctions";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public SeqSymmetry operate(final BioSeq bioseq, final List<SeqSymmetry> list) {
        final SimpleSymWithProps container = new SimpleSymWithProps();
        if (list.isEmpty()) {
            return container;
        }
        final SeqSymmetry topSym = list.get(0);
        final List<SeqSymmetry> symList = new ArrayList<SeqSymmetry>();
        for (int i = 0; i < topSym.getChildCount(); ++i) {
            symList.add(topSym.getChild(i));
        }
        final HashMap<String, SeqSymmetry> map = new HashMap<String, SeqSymmetry>();
        this.subOperate(bioseq, symList, map);
        for (final SeqSymmetry sym : map.values()) {
            container.addChild(sym);
        }
        map.clear();
        symList.clear();
        return container;
    }

    public void subOperate(final BioSeq bioseq, final List<SeqSymmetry> list, final HashMap<String, SeqSymmetry> map) {
        for (final SeqSymmetry sym : list) {
            if (FindJunctionOperator.noIntronFilter.filterSymmetry(bioseq, sym) && (!this.uniqueness || (this.uniqueness && FindJunctionOperator.uniqueLocationFilter.filterSymmetry(bioseq, sym)))) {
                updateIntronHashMap(sym, bioseq, map, this.threshold, this.twoTracks);
            }
        }
    }

    @Override
    public int getOperandCountMin(final FileTypeCategory ftc) {
        return (ftc == FileTypeCategory.Alignment) ? 1 : 0;
    }

    @Override
    public int getOperandCountMax(final FileTypeCategory ftc) {
        return (ftc == FileTypeCategory.Alignment) ? 1 : 0;
    }

    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }

    @Override
    public boolean setParameters(final Map<String, Object> map) {
        if (map.size() <= 0) {
            return false;
        }
        for (final String s : map.keySet()) {
            if (s.equalsIgnoreCase("threshold")) {
                this.threshold = (Integer) map.get(s);
            }
            else if (s.equalsIgnoreCase("twoTracks")) {
                this.twoTracks = (Boolean)map.get(s);
            }
            else {
                if (!s.equalsIgnoreCase("uniqueness")) {
                    continue;
                }
                this.uniqueness = (Boolean)map.get(s);
            }
        }
        return true;
    }

    @Override
    public boolean supportsTwoTrack() {
        return true;
    }

    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Annotation;
    }

    private static void updateIntronHashMap(final SeqSymmetry sym, final BioSeq bioseq, final HashMap<String, SeqSymmetry> map, final int threshold, final boolean twoTracks) {
        final List<Integer> childIntronIndices = new ArrayList<Integer>();
        final int childCount = sym.getChildCount();
        FindJunctionOperator.childThresholdFilter.setParam(threshold);
        for (int i = 0; i < childCount - 1; ++i) {
            if (FindJunctionOperator.childThresholdFilter.filterSymmetry(bioseq, sym.getChild(i)) && FindJunctionOperator.childThresholdFilter.filterSymmetry(bioseq, sym.getChild(i + 1))) {
                childIntronIndices.add(i);
            }
        }
        if (childIntronIndices.size() > 0) {
            final SeqSymmetry intronSym = SeqUtils.getIntronSym(sym, bioseq);
            for (final Integer j : childIntronIndices) {
                final SeqSymmetry intronChild = intronSym.getChild(j);
                if (intronChild != null) {
                    final SeqSpan span = intronSym.getSpan(bioseq);
                    addToMap(span, map, bioseq, threshold, twoTracks);
                }
            }
        }
    }

    private static void addToMap(final SeqSpan span, final HashMap<String, SeqSymmetry> map, final BioSeq bioseq, final int threshold, final boolean twoTracks) {
        boolean currentForward = false;
        final String name = "J:" + bioseq.getID() + ":" + span.getMin() + "-" + span.getMax() + ":";
        if (map.containsKey(name)) {
            final JunctionUcscBedSym sym = (JunctionUcscBedSym) map.get(name);
            if (!twoTracks) {
                currentForward = (sym.isCanonical() ? sym.isForward() : (sym.isRare() ? span.isForward() : sym.isForward()));
            }
            else {
                currentForward = span.isForward();
            }
            sym.updateScore(currentForward);
        }
        else {
            boolean canonical = true;
            boolean rare = false;
            if (!twoTracks) {
                final String leftResidues = bioseq.getResidues(span.getMin(), span.getMin() + 2);
                final String rightResidues = bioseq.getResidues(span.getMax() - 2, span.getMax());
                if (leftResidues.equalsIgnoreCase("GT") && rightResidues.equalsIgnoreCase("AG")) {
                    canonical = true;
                    currentForward = true;
                }
                else if (leftResidues.equalsIgnoreCase("CT") && rightResidues.equalsIgnoreCase("AC")) {
                    canonical = true;
                    currentForward = false;
                }
                else if ((leftResidues.equalsIgnoreCase("AT") && rightResidues.equalsIgnoreCase("AC")) || (leftResidues.equalsIgnoreCase("GC") && rightResidues.equalsIgnoreCase("AG"))) {
                    canonical = false;
                    currentForward = true;
                }
                else if ((leftResidues.equalsIgnoreCase("GT") && rightResidues.equalsIgnoreCase("AT")) || (leftResidues.equalsIgnoreCase("CT") && rightResidues.equalsIgnoreCase("GC"))) {
                    canonical = false;
                    currentForward = false;
                }
                else {
                    canonical = false;
                    currentForward = span.isForward();
                    rare = true;
                }
            }
            else {
                currentForward = span.isForward();
            }
            final int[] blockMins = { span.getMin() - threshold, span.getMax() };
            final int[] blockMaxs = { span.getMin(), span.getMax() + threshold };
            final JunctionUcscBedSym tempSym = new JunctionUcscBedSym(bioseq, name, currentForward, blockMins, blockMaxs, canonical, rare);
            map.put(name, tempSym);
        }
    }

    static {
        noIntronFilter = new NoIntronFilter();
        childThresholdFilter = new ChildThresholdFilter();
        uniqueLocationFilter = new UniqueLocationFilter();
    }

    private static class JunctionUcscBedSym extends UcscBedSym
    {
        int positiveScore;
        int negativeScore;
        int localScore;
        boolean canonical;
        boolean rare;

        private JunctionUcscBedSym(final BioSeq seq, final String name, final boolean forward, final int[] blockMins, final int[] blockMaxs, final boolean canonical, final boolean rare) {
            super(name, seq, blockMins[0], blockMaxs[1], name, 1.0f, forward, 0, 0, blockMins, blockMaxs);
            this.localScore = 1;
            this.positiveScore = (forward ? 1 : 0);
            this.negativeScore = (forward ? 0 : 1);
            this.canonical = canonical;
            this.rare = rare;
        }

        private void updateScore(final boolean isForward) {
            ++this.localScore;
            if (!this.canonical) {
                if (isForward) {
                    ++this.positiveScore;
                }
                else {
                    ++this.negativeScore;
                }
            }
        }

        @Override
        public float getScore() {
            return this.localScore;
        }

        @Override
        public Map<String, Object> cloneProperties() {
            final Map<String, Object> tprops = super.cloneProperties();
            tprops.put("score", this.localScore);
            if (!this.canonical) {
                tprops.put("canonical", this.canonical);
                tprops.put("positive_score", this.positiveScore);
                tprops.put("negative_score", this.negativeScore);
            }
            return tprops;
        }

        @Override
        public Object getProperty(final String key) {
            if (key.equals("score")) {
                return this.localScore;
            }
            return super.getProperty(key);
        }

        @Override
        public String getName() {
            return this.getID();
        }

        @Override
        public String getID() {
            return super.getID() + (this.isForward() ? "+" : "-");
        }

        @Override
        public boolean isForward() {
            return this.canonical ? super.isForward() : (this.positiveScore > this.negativeScore);
        }

        public boolean isCanonical() {
            return this.canonical;
        }

        public boolean isRare() {
            return this.rare;
        }
    }
}
