//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.style.GraphType;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import java.util.Arrays;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.ArrayList;
import java.util.HashMap;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.style.GraphState;
import java.util.List;
import java.util.Map;

public class ScoredContainerSym extends RootSeqSymmetry
{
    private final Map<String, Object> name2scores;
    private final List<Object> scorevals;
    private final List<String> scorenames;
    private static Map<String, GraphState> id2gstate;
    private static Map<String, ITrackStyleExtended> id2combo_style_plus;
    private static Map<String, ITrackStyleExtended> id2combo_style_minus;
    private static Map<String, ITrackStyleExtended> id2combo_style_neutral;

    public ScoredContainerSym() {
        this.name2scores = new HashMap<String, Object>();
        this.scorevals = new ArrayList<Object>();
        this.scorenames = new ArrayList<String>();
    }

    public void addScores(final String name, final float[] scores) {
        this.name2scores.put(name, scores);
        this.scorevals.add(scores);
        this.scorenames.add(name);
    }

    public int getScoreCount() {
        return this.scorevals.size();
    }

    public float[] getScores(final String name) {
        return (float[]) this.name2scores.get(name);
    }

    public float[] getScores(final int index) {
        return (float[]) this.scorevals.get(index);
    }

    public String getScoreName(final int index) {
        return this.scorenames.get(index);
    }

    public float[] getChildScores(final IndexedSym child, final List<?> scorelist) {
        float[] result = null;
        if (child.getParent() == this) {
            final int score_index = child.getIndex();
            final int scores_count = scorelist.size();
            result = new float[scores_count];
            for (int i = 0; i < scores_count; ++i) {
                final float[] scores = (float[])((Object)scorelist.get(i));
                result[i] = scores[score_index];
            }
        }
        return result;
    }

    public float[] getChildScores(final IndexedSym child) {
        return this.getChildScores(child, this.scorevals);
    }

    @Override
    public void addChild(final SeqSymmetry sym) {
        if (sym instanceof IndexedSym) {
            final IndexedSym isym = (IndexedSym)sym;
            final int current_index = super.getChildCount();
            isym.setIndex(current_index);
            isym.setParent(this);
            super.addChild(isym);
        }
        else {
            System.err.println("ERROR: cannot add a child to ScoredContainerSym unless it is an IndexedSym");
        }
    }

    public final GraphIntervalSym makeGraphSym(final String name, final AnnotatedSeqGroup seq_group) {
        final float[] scores = this.getScores(name);
        final SeqSpan pspan = this.getSpan(0);
        if (scores == null) {
            System.err.println("ScoreContainerSym.makeGraphSym() called, but no scores found for: " + name);
            return null;
        }
        if (pspan == null) {
            System.err.println("ScoreContainerSym.makeGraphSym() called, but has no span yet");
            return null;
        }
        final BioSeq aseq = pspan.getBioSeq();
        final int score_count = scores.length;
        final int[] xcoords = new int[score_count];
        final int[] wcoords = new int[score_count];
        final float[] ycoords = new float[score_count];
        for (int i = 0; i < score_count; ++i) {
            final IndexedSym isym = (IndexedSym)this.getChild(i);
            if (isym.getIndex() != i) {
                System.err.println("problem in ScoredContainerSym.makeGraphSym(), child.getIndex() not same as child's index in parent child list: " + isym.getIndex() + ", " + i);
            }
            final SeqSpan cspan = isym.getSpan(aseq);
            xcoords[i] = cspan.getMin();
            wcoords[i] = cspan.getLength();
            ycoords[i] = scores[i];
        }
        if (xcoords.length == 0) {
            return null;
        }
        final String id = this.getGraphID(seq_group, name, '.');
        final GraphIntervalSym gsym = new GraphIntervalSym(xcoords, wcoords, ycoords, id, aseq);
        gsym.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_BOTH);
        return gsym;
    }

    public final GraphIntervalSym makeGraphSym(final String name, final boolean orientation, final AnnotatedSeqGroup seq_group) {
        final float[] scores = this.getScores(name);
        final SeqSpan pspan = this.getSpan(0);
        if (scores == null) {
            System.err.println("ScoreContainerSym.makeGraphSym() called, but no scores found for: " + name);
            return null;
        }
        if (pspan == null) {
            System.err.println("ScoreContainerSym.makeGraphSym() called, but has no span yet");
            return null;
        }
        final BioSeq aseq = pspan.getBioSeq();
        final int score_count = scores.length;
        IntArrayList xlist = new IntArrayList(score_count);
        IntArrayList wlist = new IntArrayList(score_count);
        FloatArrayList ylist = new FloatArrayList(score_count);
        for (int i = 0; i < score_count; ++i) {
            final IndexedSym isym = (IndexedSym)this.getChild(i);
            if (isym.getIndex() != i) {
                System.err.println("problem in ScoredContainerSym.makeGraphSym(), child.getIndex() not same as child's index in parent child list: " + isym.getIndex() + ", " + i);
            }
            final SeqSpan cspan = isym.getSpan(aseq);
            if (cspan.isForward() == orientation) {
                xlist.add(cspan.getMin());
                wlist.add(cspan.getLength());
                ylist.add(scores[i]);
            }
        }
        if (xlist.size() == 0) {
            return null;
        }
        String id;
        if (orientation) {
            id = this.getGraphID(seq_group, name, '+');
        }
        else {
            id = this.getGraphID(seq_group, name, '-');
        }
        final int[] xcoords = Arrays.copyOf(xlist.elements(), xlist.size());
        xlist = null;
        final int[] wcoords = Arrays.copyOf(wlist.elements(), wlist.size());
        wlist = null;
        final float[] ycoords = Arrays.copyOf(ylist.elements(), ylist.size());
        ylist = null;
        final GraphIntervalSym gsym = new GraphIntervalSym(xcoords, wcoords, ycoords, id, aseq);
        if (orientation) {
            gsym.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_PLUS);
        }
        else {
            gsym.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_MINUS);
        }
        return gsym;
    }

    public final String getGraphID(final AnnotatedSeqGroup seq_group, final String score_name, final char strand) {
        final String id = this.getID() + ":" + strand + ":" + score_name;
        if (ScoredContainerSym.id2gstate.get(id) == null) {
            final GraphState gs = this.initializeGraphState(id, score_name, strand);
            ScoredContainerSym.id2gstate.put(id, gs);
        }
        return id;
    }

    private GraphState initializeGraphState(final String id, final String score_name, final char strand) {
        final GraphState gs = DefaultStateProvider.getGlobalStateProvider().getGraphState(id);
        gs.setGraphStyle(GraphType.HEAT_MAP);
        gs.getTierStyle().setTrackName(score_name);
        gs.setComboStyle(this.getContainerStyle(strand), 0);
        return gs;
    }

    private ITrackStyleExtended getContainerStyle(final char strand) {
        String name = (String)this.getProperty("method");
        if (name == null) {
            name = "Scores";
        }
        else {
            name = "Scores " + name;
        }
        ITrackStyleExtended style;
        if (strand == '+') {
            style = ScoredContainerSym.id2combo_style_plus.get(this.getID());
            if (style == null) {
                style = newComboStyle(name);
                ScoredContainerSym.id2combo_style_plus.put(this.getID(), style);
            }
        }
        else if (strand == '-') {
            style = ScoredContainerSym.id2combo_style_minus.get(this.getID());
            if (style == null) {
                style = newComboStyle(name);
                ScoredContainerSym.id2combo_style_minus.put(this.getID(), style);
            }
        }
        else {
            style = ScoredContainerSym.id2combo_style_neutral.get(this.getID());
            if (style == null) {
                style = newComboStyle(name);
                ScoredContainerSym.id2combo_style_neutral.put(this.getID(), style);
            }
        }
        return style;
    }

    private static ITrackStyleExtended newComboStyle(final String name) {
        final ITrackStyleExtended style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(name);
        style.setGraphTier(true);
        style.setExpandable(true);
        style.setCollapsed(false);
        return style;
    }

    @Override
    public FileTypeCategory getCategory() {
        return FileTypeCategory.ScoredContainer;
    }

    static {
        ScoredContainerSym.id2gstate = new HashMap<String, GraphState>();
        ScoredContainerSym.id2combo_style_plus = new HashMap<String, ITrackStyleExtended>();
        ScoredContainerSym.id2combo_style_minus = new HashMap<String, ITrackStyleExtended>();
        ScoredContainerSym.id2combo_style_neutral = new HashMap<String, ITrackStyleExtended>();
    }
}
