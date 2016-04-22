// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.ArrayList;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.ResiduesChars;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;

public final class SeqSymSummarizer
{
    public static MisMatchGraphSym getMismatchGraph(final List<SeqSymmetry> syms, final BioSeq seq, final boolean binary_depth, final String id, final int start, final int end, final boolean pileup) {
        if (syms.isEmpty()) {
            return null;
        }
        final int range = end - start;
        final int[] y = new int[range];
        final int[][] yR = new int[5][range];
        final SeqSymmetry sym = syms.get(0);
        final byte[] seq_residues = seq.getResidues(start, end).toLowerCase().getBytes();
        final byte intron = "-".getBytes()[0];
        final int y_offset = 0;
        for (int i = 0; i < sym.getChildCount(); ++i) {
            final SeqSymmetry childSeqSym = sym.getChild(i);
            if (childSeqSym instanceof SymWithResidues) {
                final SeqSpan span = childSeqSym.getSpan(seq);
                final int offset = (span.getMin() > start) ? (span.getMin() - start) : 0;
                final int cur_start = Math.max(start, span.getMin());
                final int cur_end = Math.min(end, span.getMax());
                final int length = cur_end - cur_start;
                final byte[] cur_residues = ((SymWithResidues)childSeqSym).getResidues(cur_start, cur_end).toLowerCase().getBytes();
                for (int j = 0; j < length; ++j) {
                    final byte ch = cur_residues[j];
                    if (seq_residues[offset + j] != ch && ch != intron) {
                        final int[] array = y;
                        final int n = offset - y_offset + j;
                        ++array[n];
                    }
                    final int k = ResiduesChars.getValue((char)ch);
                    if (k > -1) {
                        final int[] array2 = yR[k];
                        final int n2 = offset - y_offset + j;
                        ++array2[n2];
                    }
                }
            }
        }
        final MisMatchGraphSym summary = createMisMatchGraph(range, yR, start, y, id, seq, pileup);
        System.gc();
        return summary;
    }
    
    private static MisMatchGraphSym createMisMatchGraph(final int range, final int[][] yR, final int start, final int[] y, final String id, final BioSeq seq, final boolean pileup) {
        final IntArrayList _x = new IntArrayList(range);
        final FloatArrayList _y = new FloatArrayList(range);
        final IntArrayList _w = new IntArrayList(range);
        final IntArrayList _yA = new IntArrayList(range);
        final IntArrayList _yT = new IntArrayList(range);
        final IntArrayList _yG = new IntArrayList(range);
        final IntArrayList _yC = new IntArrayList(range);
        final IntArrayList _yN = new IntArrayList(range);
        for (int i = 0; i < range; ++i) {
            if (yR[0][i] > 0 || yR[1][i] > 0 || yR[2][i] > 0 || yR[3][i] > 0 || yR[4][i] > 0) {
                _x.add(start + i);
                _y.add((float)y[i]);
                _w.add(1);
                _yA.add(yR[0][i]);
                _yT.add(yR[1][i]);
                _yG.add(yR[2][i]);
                _yC.add(yR[3][i]);
                _yN.add(yR[4][i]);
            }
        }
        _x.trimToSize();
        _y.trimToSize();
        _w.trimToSize();
        _yA.trimToSize();
        _yT.trimToSize();
        _yG.trimToSize();
        _yC.trimToSize();
        _yN.trimToSize();
        final MisMatchGraphSym summary = pileup ? new MisMatchPileupGraphSym(_x.elements(), _w.elements(), _y.elements(), _yA.elements(), _yT.elements(), _yG.elements(), _yC.elements(), _yN.elements(), id, seq) : new MisMatchGraphSym(_x.elements(), _w.elements(), _y.elements(), _yA.elements(), _yT.elements(), _yG.elements(), _yC.elements(), _yN.elements(), id, seq);
        return summary;
    }
    
    public static GraphIntervalSym getSymmetrySummary(final List<SeqSymmetry> syms, final BioSeq seq, final boolean binary_depth, final String id, final Boolean isForward) {
        final int symcount = syms.size();
        final List<SeqSpan> leaf_spans = new ArrayList<SeqSpan>(symcount);
        for (final SeqSymmetry sym : syms) {
            SeqUtils.collectLeafSpans(sym, seq, isForward, leaf_spans);
        }
        if (leaf_spans.isEmpty()) {
            return null;
        }
        return getSpanSummary(leaf_spans, binary_depth, id);
    }
    
    public static GraphIntervalSym getSymmetrySummary(final List<SeqSymmetry> syms, final BioSeq seq, final boolean binary_depth, final String id) {
        final int symcount = syms.size();
        final List<SeqSpan> leaf_spans = new ArrayList<SeqSpan>(symcount);
        for (final SeqSymmetry sym : syms) {
            SeqUtils.collectLeafSpans(sym, seq, leaf_spans);
        }
        if (leaf_spans.isEmpty()) {
            return null;
        }
        return getSpanSummary(leaf_spans, binary_depth, id);
    }
    
    private static GraphIntervalSym getSpanSummary(final List<SeqSpan> spans, final boolean binary_depth, final String gid) {
        final BioSeq seq = spans.get(0).getBioSeq();
        final int span_num = spans.size();
        final int[] starts = new int[span_num];
        final int[] stops = new int[span_num];
        for (int i = 0; i < span_num; ++i) {
            final SeqSpan span = spans.get(i);
            starts[i] = span.getMin();
            stops[i] = span.getMax();
        }
        Arrays.sort(starts);
        Arrays.sort(stops);
        int starts_index = 0;
        int stops_index = 0;
        int depth = 0;
        int max_depth = 0;
        final IntArrayList transition_xpos = new IntArrayList(span_num * 2);
        final FloatArrayList transition_ypos = new FloatArrayList(span_num * 2);
        int prev_depth = 0;
        while (starts_index < span_num && stops_index < span_num) {
            final int next_start = starts[starts_index];
            final int next_stop = stops[stops_index];
            final int next_transition = Math.min(next_start, next_stop);
            if (next_start <= next_stop) {
                while (starts_index < span_num && starts[starts_index] == next_start) {
                    ++depth;
                    ++starts_index;
                }
            }
            if (next_start >= next_stop) {
                while (stops_index < span_num && stops[stops_index] == next_stop) {
                    --depth;
                    ++stops_index;
                }
            }
            if (binary_depth) {
                if (prev_depth <= 0 && depth > 0) {
                    transition_xpos.add(next_transition);
                    transition_ypos.add(1.0f);
                    prev_depth = 1;
                }
                else {
                    if (prev_depth <= 0 || depth > 0) {
                        continue;
                    }
                    transition_xpos.add(next_transition);
                    transition_ypos.add(0.0f);
                    prev_depth = 0;
                }
            }
            else {
                transition_xpos.add(next_transition);
                transition_ypos.add((float)depth);
                max_depth = Math.max(depth, max_depth);
            }
        }
        while (stops_index < span_num) {
            int next_transition2;
            for (int next_stop2 = next_transition2 = stops[stops_index]; stops_index < span_num && stops[stops_index] == next_stop2; ++stops_index) {
                --depth;
            }
            if (binary_depth) {
                if (prev_depth <= 0 && depth > 0) {
                    transition_xpos.add(next_transition2);
                    transition_ypos.add(1.0f);
                    prev_depth = 1;
                }
                else {
                    if (prev_depth <= 0 || depth > 0) {
                        continue;
                    }
                    transition_xpos.add(next_transition2);
                    transition_ypos.add(0.0f);
                    prev_depth = 0;
                }
            }
            else {
                transition_xpos.add(next_transition2);
                transition_ypos.add((float)depth);
                max_depth = Math.max(depth, max_depth);
            }
        }
        transition_xpos.trimToSize();
        transition_ypos.trimToSize();
        final int[] x_positions = transition_xpos.elements();
        final int[] widths = new int[x_positions.length];
        for (int j = 0; j < widths.length - 1; ++j) {
            widths[j] = x_positions[j + 1] - x_positions[j];
        }
        widths[widths.length - 1] = 1;
        final String uid = AnnotatedSeqGroup.getUniqueGraphID(gid, seq);
        final GraphIntervalSym gsym = new GraphIntervalSym(x_positions, widths, transition_ypos.elements(), uid, seq);
        return gsym;
    }
    
    public static List<SeqSpan> getMergedSpans(final List<SeqSpan> spans) {
        final GraphSym landscape = getSpanSummary(spans, true, "");
        return projectLandscapeSpans(landscape);
    }
    
    private static List<SeqSpan> projectLandscapeSpans(final GraphSym landscape) {
        final List<SeqSpan> spanlist = new ArrayList<SeqSpan>();
        final BioSeq seq = landscape.getGraphSeq();
        final int num_points = landscape.getPointCount();
        int current_region_start = 0;
        int current_region_end = 0;
        boolean in_region = false;
        for (int i = 0; i < num_points; ++i) {
            final int xpos = landscape.getGraphXCoord(i);
            final float ypos = landscape.getGraphYCoord(i);
            if (in_region) {
                if (ypos <= 0.0f) {
                    in_region = false;
                    current_region_end = xpos;
                    final SeqSpan newspan = new SimpleSeqSpan(current_region_start, current_region_end, seq);
                    spanlist.add(newspan);
                }
            }
            else if (ypos > 0.0f) {
                in_region = true;
                current_region_start = xpos;
            }
        }
        if (in_region) {
            System.err.println("still in a covered region at end of projectLandscapeSpans() loop!");
        }
        return spanlist;
    }
    
    private static SymWithProps projectLandscape(final GraphSym landscape) {
        final BioSeq seq = landscape.getGraphSeq();
        SimpleSymWithProps psym = new SimpleSymWithProps();
        final int num_points = landscape.getPointCount();
        int current_region_start = 0;
        int current_region_end = 0;
        boolean in_region = false;
        for (int i = 0; i < num_points; ++i) {
            final int xpos = landscape.getGraphXCoord(i);
            final float ypos = landscape.getGraphYCoord(i);
            if (in_region) {
                if (ypos <= 0.0f) {
                    in_region = false;
                    current_region_end = xpos;
                    final SeqSymmetry newsym = new SingletonSeqSymmetry(current_region_start, current_region_end, seq);
                    psym.addChild(newsym);
                }
            }
            else if (ypos > 0.0f) {
                in_region = true;
                current_region_start = xpos;
            }
        }
        if (in_region) {
            System.err.println("still in a covered region at end of projectLandscape() loop!");
        }
        if (psym.getChildCount() <= 0) {
            psym = null;
        }
        else {
            final int pmin = psym.getChild(0).getSpan(seq).getMin();
            final int pmax = psym.getChild(psym.getChildCount() - 1).getSpan(seq).getMax();
            final SeqSpan pspan = new SimpleSeqSpan(pmin, pmax, seq);
            psym.addSpan(pspan);
        }
        return psym;
    }
    
    public static SeqSymmetry getUnion(final List<SeqSymmetry> syms, final BioSeq seq) {
        final GraphSym landscape = getSymmetrySummary(syms, seq, true, "");
        if (landscape != null) {
            return projectLandscape(landscape);
        }
        return null;
    }
    
    public static SeqSymmetry getIntersection(final List<SeqSymmetry> symsA, final List<SeqSymmetry> symsB, final BioSeq seq) {
        MutableSeqSymmetry psym = new SimpleSymWithProps();
        final SeqSymmetry unionA = getUnion(symsA, seq);
        final SeqSymmetry unionB = getUnion(symsB, seq);
        final List<SeqSymmetry> symsAB = new ArrayList<SeqSymmetry>();
        symsAB.add(unionA);
        symsAB.add(unionB);
        final GraphSym combo_graph = getSymmetrySummary(symsAB, seq, false, "");
        final int num_points = combo_graph.getPointCount();
        int current_region_start = 0;
        int current_region_end = 0;
        boolean in_region = false;
        for (int i = 0; i < num_points; ++i) {
            final int xpos = combo_graph.getGraphXCoord(i);
            final float ypos = combo_graph.getGraphYCoord(i);
            if (in_region) {
                if (ypos < 2.0f) {
                    in_region = false;
                    current_region_end = xpos;
                    final SeqSymmetry newsym = new SingletonSeqSymmetry(current_region_start, current_region_end, seq);
                    psym.addChild(newsym);
                }
            }
            else if (ypos >= 2.0f) {
                in_region = true;
                current_region_start = xpos;
            }
        }
        if (in_region) {
            System.err.println("still in a covered region at end of getUnion() loop!");
        }
        if (psym.getChildCount() <= 0) {
            psym = null;
        }
        else {
            final int pmin = psym.getChild(0).getSpan(seq).getMin();
            final int pmax = psym.getChild(psym.getChildCount() - 1).getSpan(seq).getMax();
            final SeqSpan pspan = new SimpleSeqSpan(pmin, pmax, seq);
            psym.addSpan(pspan);
        }
        return psym;
    }
}
