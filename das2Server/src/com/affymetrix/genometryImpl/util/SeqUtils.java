// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.text.DecimalFormat;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.comparator.SeqSymStartComparator;
import com.affymetrix.genometryImpl.span.MutableDoubleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleDerivedSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.DerivedSeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.MutableSingletonSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.SeqSpanComparator;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;

public abstract class SeqUtils
{
    public static int getDepth(final SeqSymmetry sym) {
        return getDepth(sym, 1);
    }
    
    private static int getDepth(final SeqSymmetry sym, final int current_depth) {
        final int child_count = sym.getChildCount();
        if (child_count == 0) {
            return current_depth;
        }
        int max_child_depth = current_depth;
        final int next_depth = current_depth + 1;
        for (int i = child_count - 1; i >= 0; --i) {
            final int current_child_depth = getDepth(sym.getChild(i), next_depth);
            max_child_depth = Math.max(max_child_depth, current_child_depth);
        }
        return max_child_depth;
    }
    
    public static boolean spansEqual(final SeqSpan spanA, final SeqSpan spanB) {
        return spanA != null && spanB != null && spanA.getStartDouble() == spanB.getStartDouble() && spanA.getEndDouble() == spanB.getEndDouble() && spanA.getBioSeq() == spanB.getBioSeq();
    }
    
    public static List<SeqSpan> getLeafSpans(final SeqSymmetry sym, final BioSeq seq) {
        final List<SeqSpan> leafSpans = new ArrayList<SeqSpan>();
        collectLeafSpans(sym, seq, leafSpans);
        return leafSpans;
    }
    
    public static void collectLeafSpans(final SeqSymmetry sym, final BioSeq seq, final Boolean isForward, final Collection<SeqSpan> leafs) {
        final int childCount = sym.getChildCount();
        if (childCount == 0) {
            final SeqSpan span = sym.getSpan(seq);
            if (span != null && span.isForward() == isForward) {
                leafs.add(span);
            }
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                collectLeafSpans(sym.getChild(i), seq, isForward, leafs);
            }
        }
    }
    
    public static void collectLeafSpans(final SeqSymmetry sym, final BioSeq seq, final Collection<SeqSpan> leafs) {
        final int childCount = sym.getChildCount();
        if (childCount == 0) {
            final SeqSpan span = sym.getSpan(seq);
            if (span != null) {
                leafs.add(span);
            }
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                collectLeafSpans(sym.getChild(i), seq, leafs);
            }
        }
    }
    
    public static List<SeqSymmetry> getLeafSyms(final SeqSymmetry sym) {
        final List<SeqSymmetry> leafSyms = new ArrayList<SeqSymmetry>();
        collectLeafSyms(sym, leafSyms);
        return leafSyms;
    }
    
    private static void collectLeafSyms(final SeqSymmetry sym, final Collection<SeqSymmetry> leafs) {
        final int childCount = sym.getChildCount();
        if (childCount == 0) {
            leafs.add(sym);
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                collectLeafSyms(sym.getChild(i), leafs);
            }
        }
    }
    
    public static SeqSymmetry getIntronSym(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        if (span == null) {
            return null;
        }
        final SimpleMutableSeqSymmetry psym = new SimpleMutableSeqSymmetry();
        psym.addSpan(span);
        return exclusive(psym, sym, seq);
    }
    
    public static SeqSymmetry exclusive(final SeqSymmetry symA, final SeqSymmetry symB, final BioSeq seq) {
        final SeqSymmetry xorSym = xor(symA, symB, seq);
        return intersection(symA, xorSym, seq);
    }
    
    private static SeqSymmetry xor(final SeqSymmetry symA, final SeqSymmetry symB, final BioSeq seq) {
        final SeqSymmetry unionAB = union(symA, symB, seq);
        final SeqSymmetry interAB = intersection(symA, symB, seq);
        final SeqSymmetry inverseInterAB = inverse(interAB, seq);
        return intersection(unionAB, inverseInterAB, seq);
    }
    
    public static SeqSymmetry inverse(final SeqSymmetry symA, final BioSeq seq) {
        final List<SeqSpan> spans = getLeafSpans(symA, seq);
        final MutableSeqSymmetry mergedSym = spanMerger(spans);
        final List<SeqSpan> mergedSpans = getLeafSpans(mergedSym, seq);
        Collections.sort(mergedSpans, new SeqSpanComparator());
        final MutableSeqSymmetry invertedSym = new SimpleMutableSeqSymmetry();
        final int spanCount = mergedSpans.size();
        if (spanCount > 0) {
            addInvertChildren(mergedSpans, seq, invertedSym, spanCount);
        }
        invertedSym.addSpan(new SimpleSeqSpan(0, seq.getLength(), seq));
        return invertedSym;
    }
    
    private static void addInvertChildren(final List<SeqSpan> mergedSpans, final BioSeq seq, final MutableSeqSymmetry invertedSym, final int spanCount) {
        final SeqSpan firstSpan = mergedSpans.get(0);
        if (firstSpan.getMin() > 0) {
            final SeqSymmetry beforeSym = new MutableSingletonSeqSymmetry(0, firstSpan.getMin(), seq);
            invertedSym.addChild(beforeSym);
        }
        for (int i = 0; i < spanCount - 1; ++i) {
            final SeqSpan preSpan = mergedSpans.get(i);
            final SeqSpan postSpan = mergedSpans.get(i + 1);
            final SeqSymmetry gapSym = new MutableSingletonSeqSymmetry(preSpan.getMax(), postSpan.getMin(), seq);
            invertedSym.addChild(gapSym);
        }
        final SeqSpan lastSpan = mergedSpans.get(spanCount - 1);
        if (lastSpan.getMax() < seq.getLength()) {
            final SeqSymmetry afterSym = new MutableSingletonSeqSymmetry(lastSpan.getMax(), seq.getLength(), seq);
            invertedSym.addChild(afterSym);
        }
    }
    
    public static MutableSeqSymmetry union(final SeqSymmetry symA, final SeqSymmetry symB, final BioSeq seq) {
        final MutableSeqSymmetry resultSym = new SimpleMutableSeqSymmetry();
        union(symA, symB, resultSym, seq);
        return resultSym;
    }
    
    public static void union(final List<SeqSymmetry> syms, final MutableSeqSymmetry resultSym, final BioSeq seq) {
        resultSym.clear();
        final List<SeqSymmetry> leaves = new ArrayList<SeqSymmetry>();
        for (final SeqSymmetry sym : syms) {
            collectLeafSyms(sym, leaves);
        }
        final int leafCount = leaves.size();
        final List<SeqSpan> spans = new ArrayList<SeqSpan>(leafCount);
        for (final SeqSymmetry sym2 : leaves) {
            spans.add(sym2.getSpan(seq));
        }
        spanMerger(spans, resultSym);
    }
    
    private static void union(final SeqSymmetry symA, final SeqSymmetry symB, final MutableSeqSymmetry resultSym, final BioSeq seq) {
        resultSym.clear();
        final List<SeqSpan> spans = new ArrayList<SeqSpan>();
        collectLeafSpans(symA, seq, spans);
        collectLeafSpans(symB, seq, spans);
        spanMerger(spans, resultSym);
    }
    
    public static MutableSeqSymmetry intersection(final SeqSymmetry symA, final SeqSymmetry symB, final BioSeq seq) {
        final MutableSeqSymmetry resultSym = new SimpleMutableSeqSymmetry();
        intersection(symA, symB, resultSym, seq);
        return resultSym;
    }
    
    public static boolean intersection(final SeqSymmetry symA, final SeqSymmetry symB, final MutableSeqSymmetry resultSym, final BioSeq seq) {
        final List<SeqSpan> tempA = getLeafSpans(symA, seq);
        final List<SeqSpan> tempB = getLeafSpans(symB, seq);
        final MutableSeqSymmetry mergesymA = spanMerger(tempA);
        final MutableSeqSymmetry mergesymB = spanMerger(tempB);
        final List<SeqSpan> leavesA = getLeafSpans(mergesymA, seq);
        final List<SeqSpan> leavesB = getLeafSpans(mergesymB, seq);
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (final SeqSpan spanA : leavesA) {
            if (spanA == null) {
                continue;
            }
            for (final SeqSpan spanB : leavesB) {
                if (spanB == null) {
                    continue;
                }
                if (!strictOverlap(spanA, spanB)) {
                    continue;
                }
                final SeqSpan spanI = intersection(spanA, spanB);
                min = Math.min(spanI.getMin(), min);
                max = Math.max(spanI.getMax(), max);
                final MutableSeqSymmetry symI = new SimpleMutableSeqSymmetry();
                symI.addSpan(spanI);
                resultSym.addChild(symI);
            }
        }
        if (resultSym.getChildCount() == 0) {
            return false;
        }
        final SeqSpan resultSpan = new SimpleSeqSpan(min, max, seq);
        resultSym.addSpan(resultSpan);
        return true;
    }
    
    private static MutableSeqSymmetry spanMerger(final List<SeqSpan> spans) {
        final MutableSeqSymmetry resultSym = new SimpleMutableSeqSymmetry();
        spanMerger(spans, resultSym);
        return resultSym;
    }
    
    private static void spanMerger(final List<SeqSpan> spans, final MutableSeqSymmetry resultSym) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        final List<SeqSpan> merged_spans = new ArrayList<SeqSpan>(spans.size());
        SeqSpan span;
        while ((span = getFirstNonNullSpan(spans)) != null) {
            final MutableSeqSpan mergeSpan = mergeHelp(spans, span);
            merged_spans.add(mergeSpan);
        }
        Collections.sort(merged_spans, new SeqSpanComparator());
        for (final SeqSpan mergedSpan : merged_spans) {
            final MutableSingletonSeqSymmetry childSym = new MutableSingletonSeqSymmetry(mergedSpan.getStart(), mergedSpan.getEnd(), mergedSpan.getBioSeq());
            min = Math.min(mergedSpan.getMin(), min);
            max = Math.max(mergedSpan.getMax(), max);
            resultSym.addChild(childSym);
        }
        final BioSeq seq = merged_spans.isEmpty() ? null : merged_spans.get(0).getBioSeq();
        final SeqSpan resultSpan = new SimpleSeqSpan(min, max, seq);
        resultSym.addSpan(resultSpan);
    }
    
    private static SeqSpan getFirstNonNullSpan(final List<SeqSpan> spans) {
        for (final SeqSpan span : spans) {
            if (span != null) {
                return span;
            }
        }
        return null;
    }
    
    private static MutableSeqSpan mergeHelp(final List<SeqSpan> spans, final SeqSpan curSpan) {
        final MutableSeqSpan result = new SimpleMutableSeqSpan(curSpan);
        while (mergeHelp(spans, result)) {}
        return result;
    }
    
    private static boolean mergeHelp(final List<SeqSpan> spans, final MutableSeqSpan result) {
        boolean changed = false;
        for (int spanCount = spans.size(), i = 0; i < spanCount; ++i) {
            final SeqSpan curSpan = spans.get(i);
            if (curSpan != null) {
                if (union(result, curSpan, result, false)) {
                    changed = true;
                    spans.set(i, null);
                }
            }
        }
        return changed;
    }
    
    public static SeqSpan getOtherSpan(final SeqSymmetry sym, final SeqSpan span) {
        for (int spanCount = sym.getSpanCount(), i = 0; i < spanCount; ++i) {
            if (!spansEqual(span, sym.getSpan(i))) {
                return sym.getSpan(i);
            }
        }
        return null;
    }
    
    public static BioSeq getOtherSeq(final SeqSymmetry sym, final BioSeq seq) {
        for (int spanCount = sym.getSpanCount(), i = 0; i < spanCount; ++i) {
            final BioSeq testseq = sym.getSpan(i).getBioSeq();
            if (testseq != seq) {
                return testseq;
            }
        }
        return null;
    }
    
    public static boolean transformSymmetry(final MutableSeqSymmetry resultSet, final SeqSymmetry[] symPath) {
        for (final SeqSymmetry sym : symPath) {
            if (!transformSymmetry(resultSet, sym, true)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean transformSymmetry(final MutableSeqSymmetry resultSym, final SeqSymmetry mapSym, final boolean src2dst_recurse) {
        final int resChildCount = resultSym.getChildCount();
        if (resChildCount > 0) {
            for (int child_index = 0; child_index < resChildCount; ++child_index) {
                final MutableSeqSymmetry childResSym = (MutableSeqSymmetry)resultSym.getChild(child_index);
                transformSymmetry(childResSym, mapSym, src2dst_recurse);
            }
            addParentSpans(resultSym, mapSym);
            return true;
        }
        if (src2dst_recurse && mapSym.getChildCount() > 0) {
            loopThruMapSymChildren(mapSym, resultSym);
            return true;
        }
        final int spanCount = mapSym.getSpanCount();
        SeqSpan linkSpan = null;
        SeqSpan mapSpan = null;
        for (int spandex = 0; spandex < spanCount; ++spandex) {
            mapSpan = mapSym.getSpan(spandex);
            final BioSeq seq = mapSpan.getBioSeq();
            final SeqSpan respan = resultSym.getSpan(seq);
            if (respan != null) {
                linkSpan = respan;
                break;
            }
        }
        return linkSpan != null && transformLeafSymmetry(spanCount, mapSym, resultSym, linkSpan, mapSpan);
    }
    
    private static void loopThruMapSymChildren(final SeqSymmetry mapSym, final MutableSeqSymmetry resultSym) {
        for (int map_childcount = mapSym.getChildCount(), index = 0; index < map_childcount; ++index) {
            final SeqSymmetry map_child_sym = mapSym.getChild(index);
            final MutableSeqSymmetry childResult = addIntersectionsToChildResultSyms(map_child_sym, resultSym, mapSym);
            if (childResult != null) {
                transformSymmetry(childResult, resultSym, false);
                transformSymmetry(childResult, map_child_sym, true);
                resultSym.addChild(childResult);
            }
        }
        addParentSpans(resultSym, mapSym);
    }
    
    private static MutableSeqSymmetry addIntersectionsToChildResultSyms(final SeqSymmetry map_child_sym, final MutableSeqSymmetry resultSym, final SeqSymmetry mapSym) {
        MutableSeqSymmetry childResult = null;
        for (int spanCount = (map_child_sym == null) ? 0 : map_child_sym.getSpanCount(), spandex = 0; spandex < spanCount; ++spandex) {
            final SeqSpan mapspan = map_child_sym.getSpan(spandex);
            final BioSeq seq = mapspan.getBioSeq();
            final SeqSpan respan = resultSym.getSpan(seq);
            if (respan != null) {
                final MutableSeqSpan interSpan = (MutableSeqSpan)intersection(respan, mapspan);
                if (interSpan != null) {
                    if (respan.isForward()) {
                        interSpan.setDouble(interSpan.getMinDouble(), interSpan.getMaxDouble(), interSpan.getBioSeq());
                    }
                    else {
                        interSpan.setDouble(interSpan.getMaxDouble(), interSpan.getMinDouble(), interSpan.getBioSeq());
                    }
                    if (childResult == null) {
                        if (mapSym instanceof DerivedSeqSymmetry) {
                            childResult = new SimpleDerivedSeqSymmetry();
                            ((DerivedSeqSymmetry)childResult).setOriginalSymmetry(resultSym);
                        }
                        else {
                            childResult = new SimpleMutableSeqSymmetry();
                        }
                    }
                    childResult.addSpan(interSpan);
                }
            }
        }
        return childResult;
    }
    
    private static boolean transformLeafSymmetry(final int spanCount, final SeqSymmetry mapSym, final MutableSeqSymmetry resultSym, final SeqSpan linkSpan, final SeqSpan mapSpan) {
        MutableSeqSpan interSpan = null;
        for (int spandex = 0; spandex < spanCount; ++spandex) {
            if (interSpan == null) {
                interSpan = (MutableSeqSpan)intersection(linkSpan, mapSpan);
                if (interSpan == null) {
                    return false;
                }
                if (linkSpan.isForward()) {
                    interSpan.setDouble(interSpan.getMinDouble(), interSpan.getMaxDouble(), interSpan.getBioSeq());
                }
                else {
                    interSpan.setDouble(interSpan.getMaxDouble(), interSpan.getMinDouble(), interSpan.getBioSeq());
                }
            }
            final SeqSpan newspan = mapSym.getSpan(spandex);
            final BioSeq seq = newspan.getBioSeq();
            final SeqSpan respan = resultSym.getSpan(seq);
            final MutableSeqSpan newResSpan = (respan == null) ? new MutableDoubleSeqSpan() : ((MutableSeqSpan)respan);
            if (!transformSpan(interSpan, newResSpan, seq, mapSym)) {
                return false;
            }
            if (respan == null) {
                resultSym.addSpan(newResSpan);
            }
        }
        return true;
    }
    
    public static List<SeqSymmetry> getOverlappingChildren(final SeqSymmetry sym, final SeqSpan ospan) {
        final int childcount = sym.getChildCount();
        if (childcount == 0) {
            return null;
        }
        List<SeqSymmetry> results = null;
        final BioSeq oseq = ospan.getBioSeq();
        for (int i = 0; i < childcount; ++i) {
            final SeqSymmetry child = sym.getChild(i);
            final SeqSpan cspan = child.getSpan(oseq);
            if (strictOverlap(ospan, cspan)) {
                if (results == null) {
                    results = new ArrayList<SeqSymmetry>();
                }
                results.add(child);
            }
        }
        return results;
    }
    
    private static void addParentSpans(final MutableSeqSymmetry resultSym, final SeqSymmetry mapSym) {
        final int resultChildCount = resultSym.getChildCount();
        if (resultChildCount == 0) {
            return;
        }
        for (int mapSpanCount = mapSym.getSpanCount(), spandex = 0; spandex < mapSpanCount; ++spandex) {
            final SeqSpan mapSpan = mapSym.getSpan(spandex);
            final BioSeq mapSeq = mapSpan.getBioSeq();
            final SeqSpan resSpan = resultSym.getSpan(mapSeq);
            if (resSpan == null) {
                int forCount = 0;
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                boolean bounds_set = false;
                for (int childIndex = 0; childIndex < resultChildCount; ++childIndex) {
                    final SeqSymmetry childResSym = resultSym.getChild(childIndex);
                    final SeqSpan childResSpan = childResSym.getSpan(mapSeq);
                    if (childResSpan != null) {
                        min = Math.min(childResSpan.getMinDouble(), min);
                        max = Math.max(childResSpan.getMaxDouble(), max);
                        bounds_set = true;
                        if (childResSpan.isForward()) {
                            ++forCount;
                        }
                        else {
                            --forCount;
                        }
                    }
                }
                if (bounds_set) {
                    addParentSpan(mapSeq, forCount, min, max, resultSym);
                }
            }
        }
    }
    
    private static void addParentSpan(final BioSeq mapSeq, final int forCount, final double min, final double max, final MutableSeqSymmetry resultSym) {
        final MutableSeqSpan newResSpan = new MutableDoubleSeqSpan();
        newResSpan.setBioSeq(mapSeq);
        if (forCount >= 0) {
            newResSpan.setStartDouble(min);
            newResSpan.setEndDouble(max);
        }
        else {
            newResSpan.setStartDouble(max);
            newResSpan.setEndDouble(min);
        }
        resultSym.addSpan(newResSpan);
    }
    
    public static boolean transformSpan(final SeqSpan srcSpan, final MutableSeqSpan dstSpan, final BioSeq dstSeq, final SeqSymmetry sym) {
        final SeqSpan span1 = sym.getSpan(srcSpan.getBioSeq());
        final SeqSpan span2 = sym.getSpan(dstSeq);
        if (span1 == null || span2 == null) {
            return false;
        }
        if (!strictOverlap(srcSpan, span1)) {
            return false;
        }
        dstSpan.setBioSeq(dstSeq);
        final boolean opposite_spans = span1.isForward() ^ span2.isForward();
        final boolean resultForward = opposite_spans ^ srcSpan.isForward();
        final double scale = span2.getLengthDouble() / span1.getLengthDouble();
        double vstart;
        double vend;
        if (opposite_spans) {
            vstart = scale * (span1.getStartDouble() - srcSpan.getStartDouble()) + span2.getStartDouble();
            vend = scale * (span1.getEndDouble() - srcSpan.getEndDouble()) + span2.getEndDouble();
        }
        else {
            vstart = scale * (srcSpan.getStartDouble() - span1.getStartDouble()) + span2.getStartDouble();
            vend = scale * (srcSpan.getEndDouble() - span1.getEndDouble()) + span2.getEndDouble();
        }
        if (resultForward) {
            dstSpan.setStartDouble(Math.min(vstart, vend));
            dstSpan.setEndDouble(Math.max(vstart, vend));
        }
        else {
            dstSpan.setStartDouble(Math.max(vstart, vend));
            dstSpan.setEndDouble(Math.min(vstart, vend));
        }
        return true;
    }
    
    public static boolean overlap(final SeqSpan spanA, final SeqSpan spanB) {
        return strictOverlap(spanA, spanB);
    }
    
    public static boolean looseOverlap(final SeqSpan spanA, final SeqSpan spanB) {
        final double AMin = spanA.getMinDouble();
        final double BMin = spanB.getMinDouble();
        return (AMin >= BMin) ? (AMin <= spanB.getMaxDouble()) : (BMin <= spanA.getMaxDouble());
    }
    
    private static boolean looseOverlap(final double AMin, final double AMax, final double BMin, final double BMax) {
        return (AMin >= BMin) ? (AMin <= BMax) : (BMin <= AMax);
    }
    
    private static boolean strictOverlap(final SeqSpan spanA, final SeqSpan spanB) {
        final double AMin = spanA.getMinDouble();
        final double BMin = spanB.getMinDouble();
        return (AMin >= BMin) ? (AMin < spanB.getMaxDouble()) : (BMin < spanA.getMaxDouble());
    }
    
    private static boolean strictOverlap(final double AMin, final double AMax, final double BMin, final double BMax) {
        return (AMin >= BMin) ? (AMin < BMax) : (BMin < AMax);
    }
    
    public static boolean contains(final SeqSpan spanA, final SeqSpan spanB) {
        return spanA.getMinDouble() <= spanB.getMinDouble() && spanA.getMaxDouble() >= spanB.getMaxDouble();
    }
    
    private static SeqSpan intersection(final SeqSpan spanA, final SeqSpan spanB) {
        if (!strictOverlap(spanA, spanB)) {
            return null;
        }
        final MutableSeqSpan dstSpan = new MutableDoubleSeqSpan();
        if (intersection(spanA, spanB, dstSpan)) {
            return dstSpan;
        }
        return null;
    }
    
    public static boolean intersection(final SeqSpan spanA, final SeqSpan spanB, final MutableSeqSpan dstSpan) {
        if (null == spanA || null == spanB) {
            return false;
        }
        if (spanA.getBioSeq() != spanB.getBioSeq()) {
            return false;
        }
        if (!strictOverlap(spanA, spanB)) {
            return false;
        }
        final boolean AForward = spanA.isForward();
        final boolean BForward = spanB.isForward();
        double start;
        double end;
        if (AForward && BForward) {
            start = Math.max(spanA.getStartDouble(), spanB.getStartDouble());
            end = Math.min(spanA.getEndDouble(), spanB.getEndDouble());
        }
        else if (!AForward && !BForward) {
            start = Math.min(spanA.getStartDouble(), spanB.getStartDouble());
            end = Math.max(spanA.getEndDouble(), spanB.getEndDouble());
        }
        else if (AForward) {
            start = Math.max(spanA.getStartDouble(), spanB.getEndDouble());
            end = Math.min(spanA.getEndDouble(), spanB.getStartDouble());
        }
        else {
            start = Math.min(spanA.getStartDouble(), spanB.getEndDouble());
            end = Math.max(spanA.getEndDouble(), spanB.getStartDouble());
        }
        dstSpan.setStartDouble(start);
        dstSpan.setEndDouble(end);
        dstSpan.setBioSeq(spanA.getBioSeq());
        return true;
    }
    
    public static boolean union(final SeqSpan spanA, final SeqSpan spanB, final MutableSeqSpan dstSpan, final boolean use_strict_overlap) {
        if (spanA.getBioSeq() != spanB.getBioSeq()) {
            return false;
        }
        final boolean AForward = spanA.isForward();
        final boolean BForward = spanB.isForward();
        double AMin;
        double AMax;
        if (AForward) {
            AMin = spanA.getStartDouble();
            AMax = spanA.getEndDouble();
        }
        else {
            AMin = spanA.getEndDouble();
            AMax = spanA.getStartDouble();
        }
        double BMin;
        double BMax;
        if (BForward) {
            BMin = spanB.getStartDouble();
            BMax = spanB.getEndDouble();
        }
        else {
            BMin = spanB.getEndDouble();
            BMax = spanB.getStartDouble();
        }
        if (use_strict_overlap) {
            if (!strictOverlap(AMin, AMax, BMin, BMax)) {
                return false;
            }
        }
        else if (!looseOverlap(AMin, AMax, BMin, BMax)) {
            return false;
        }
        encompass(AForward, AMin, AMax, BMin, BMax, spanA.getBioSeq(), dstSpan);
        return true;
    }
    
    public static boolean encompass(final SeqSpan spanA, final SeqSpan spanB, final MutableSeqSpan dstSpan) {
        if (spanA.getBioSeq() != spanB.getBioSeq()) {
            return false;
        }
        final boolean AForward = spanA.isForward();
        final boolean BForward = spanB.isForward();
        double start;
        double end;
        if (AForward && BForward) {
            start = Math.min(spanA.getStartDouble(), spanB.getStartDouble());
            end = Math.max(spanA.getEndDouble(), spanB.getEndDouble());
        }
        else if (!AForward && !BForward) {
            start = Math.max(spanA.getStartDouble(), spanB.getStartDouble());
            end = Math.min(spanA.getEndDouble(), spanB.getEndDouble());
        }
        else if (AForward) {
            start = Math.min(spanA.getStartDouble(), spanB.getEndDouble());
            end = Math.max(spanA.getEndDouble(), spanB.getStartDouble());
        }
        else {
            start = Math.max(spanA.getStartDouble(), spanB.getEndDouble());
            end = Math.min(spanA.getEndDouble(), spanB.getStartDouble());
        }
        dstSpan.setStartDouble(start);
        dstSpan.setEndDouble(end);
        dstSpan.setBioSeq(spanA.getBioSeq());
        return true;
    }
    
    private static void encompass(final boolean AForward, final double AMin, final double AMax, final double BMin, final double BMax, final BioSeq seq, final MutableSeqSpan dstSpan) {
        double start;
        double end;
        if (AForward) {
            start = Math.min(AMin, BMin);
            end = Math.max(AMax, BMax);
        }
        else {
            start = Math.max(AMax, BMax);
            end = Math.min(AMin, BMin);
        }
        dstSpan.setStartDouble(start);
        dstSpan.setEndDouble(end);
        dstSpan.setBioSeq(seq);
    }
    
    public static DerivedSeqSymmetry copyToDerived(final SeqSymmetry sym) {
        final DerivedSeqSymmetry der = new SimpleDerivedSeqSymmetry();
        copyToDerived(sym, der);
        return der;
    }
    
    private static void copyToDerived(final SeqSymmetry sym, final DerivedSeqSymmetry der) {
        der.clear();
        if (sym instanceof DerivedSeqSymmetry) {
            der.setOriginalSymmetry(((DerivedSeqSymmetry)sym).getOriginalSymmetry());
        }
        else {
            der.setOriginalSymmetry(sym);
        }
        for (int spanCount = sym.getSpanCount(), i = 0; i < spanCount; ++i) {
            final SeqSpan span = sym.getSpan(i);
            final SeqSpan newspan = new SimpleMutableSeqSpan(span);
            der.addSpan(newspan);
        }
        for (int childCount = sym.getChildCount(), j = 0; j < childCount; ++j) {
            final SeqSymmetry child = sym.getChild(j);
            final DerivedSeqSymmetry newchild = new SimpleDerivedSeqSymmetry();
            copyToDerived(child, newchild);
            der.addChild(newchild);
        }
    }
    
    public static SeqSpan getChildBounds(final SeqSymmetry parent, final BioSeq seq) {
        int rev_count = 0;
        int for_count = 0;
        SeqSpan cbSpan = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        final int childCount = parent.getChildCount();
        boolean bounds_set = false;
        for (int i = 0; i < childCount; ++i) {
            final SeqSymmetry childSym = parent.getChild(i);
            final SeqSpan childSpan = childSym.getSpan(seq);
            if (childSpan != null) {
                min = Math.min(min, childSpan.getMin());
                max = Math.max(max, childSpan.getMax());
                if (childSpan.isForward()) {
                    ++for_count;
                }
                else {
                    ++rev_count;
                }
                bounds_set = true;
            }
        }
        if (bounds_set) {
            if (for_count >= rev_count) {
                cbSpan = new SimpleSeqSpan(min, max, seq);
            }
            else {
                cbSpan = new SimpleSeqSpan(max, min, seq);
            }
        }
        return cbSpan;
    }
    
    public static String getResidues(final SeqSymmetry sym, final BioSeq seq) {
        String result = null;
        final int childcount = sym.getChildCount();
        if (childcount > 0) {
            result = "";
            for (int i = 0; i < childcount; ++i) {
                final SeqSymmetry child = sym.getChild(i);
                final String child_result = getResidues(child, seq);
                if (child_result == null) {
                    result = null;
                    break;
                }
                result += child_result;
            }
        }
        else {
            final SeqSpan span = sym.getSpan(seq);
            if (span != null) {
                result = seq.getResidues(span.getStart(), span.getEnd());
            }
        }
        return result;
    }
    
    public static String determineSelectedResidues(SeqSymmetry residues_sym, final BioSeq seq) {
        final int child_count = residues_sym.getChildCount();
        if (child_count > 0) {
            final List<SeqSymmetry> sorted_children = new ArrayList<SeqSymmetry>(child_count);
            for (int i = 0; i < child_count; ++i) {
                sorted_children.add(residues_sym.getChild(i));
            }
            final Comparator<SeqSymmetry> symcompare = new SeqSymStartComparator(seq, residues_sym.getSpan(seq).isForward());
            Collections.sort(sorted_children, symcompare);
            final MutableSeqSymmetry sorted_sym = new SimpleMutableSeqSymmetry();
            for (int j = 0; j < child_count; ++j) {
                sorted_sym.addChild(sorted_children.get(j));
            }
            residues_sym = sorted_sym;
        }
        return getResidues(residues_sym, seq);
    }
    
    public static String selectedAllResidues(final SeqSymmetry residues_sym, final BioSeq seq) {
        final SeqSpan span = residues_sym.getSpan(seq);
        if (span != null) {
            return seq.getResidues(span.getStart(), span.getEnd());
        }
        return null;
    }
    
    public static boolean areResiduesComplete(final String residues) {
        for (int rescount = residues.length(), i = 0; i < rescount; ++i) {
            final char res = residues.charAt(i);
            if (res == '-' || res == ' ' || res == '.') {
                return false;
            }
        }
        return true;
    }
    
    public static SeqSpan getAnnotationBounds(final BioSeq aseq) {
        if (aseq == null) {
            return null;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        synchronized (aseq) {
            for (int annotCount = aseq.getAnnotationCount(), i = 0; i < annotCount; ++i) {
                final SeqSymmetry annotSym = aseq.getAnnotation(i);
                if (!(annotSym instanceof GraphSym)) {
                    if (annotSym instanceof TypeContainerAnnot) {
                        final TypeContainerAnnot tca = (TypeContainerAnnot)annotSym;
                        final int[] sub_bounds = getAnnotationBounds(aseq, tca, min, max);
                        min = sub_bounds[0];
                        max = sub_bounds[1];
                    }
                    else {
                        final SeqSpan span = annotSym.getSpan(aseq);
                        if (span != null) {
                            min = Math.min(span.getMin(), min);
                            max = Math.max(span.getMax(), max);
                        }
                    }
                }
            }
            if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE) {
                min = Math.max(0, min - 100);
                max = Math.min(aseq.getLength(), max + 100);
                return new SimpleSeqSpan(min, max, aseq);
            }
            return null;
        }
    }
    
    private static int[] getAnnotationBounds(final BioSeq seq, final TypeContainerAnnot tca, final int min, final int max) {
        final int[] min_max = { min, max };
        for (int child_count = tca.getChildCount(), j = 0; j < child_count; ++j) {
            final SeqSymmetry next_sym = tca.getChild(j);
            for (int annotCount = next_sym.getChildCount(), i = 0; i < annotCount; ++i) {
                final SeqSymmetry annotSym = next_sym.getChild(i);
                if (!(annotSym instanceof GraphSym)) {
                    final SeqSpan span = annotSym.getSpan(seq);
                    if (span != null) {
                        min_max[0] = Math.min(span.getMin(), min_max[0]);
                        min_max[1] = Math.max(span.getMax(), min_max[1]);
                    }
                }
            }
        }
        return min_max;
    }
    
    public static boolean hasSpan(final SeqSymmetry sym) {
        if (sym.getSpanCount() > 0) {
            return true;
        }
        for (int childCount = sym.getChildCount(), i = 0; i < childCount; ++i) {
            if (hasSpan(sym.getChild(i))) {
                return true;
            }
        }
        return false;
    }
    
    public static void printSymmetry(final SeqSymmetry sym) {
        printSymmetry("", sym, "  ");
    }
    
    private static void printSymmetry(final String indent, final SeqSymmetry sym, final String spacer) {
        System.out.println(indent + symToString(sym));
        if (sym instanceof SymWithProps) {
            final SymWithProps pp = (SymWithProps)sym;
            final Map<String, Object> props = pp.getProperties();
            if (props != null) {
                for (final Map.Entry<String, Object> entry : props.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    System.out.println(indent + spacer + key + " --> " + value);
                }
            }
            else {
                System.out.println(indent + spacer + " no properties");
            }
        }
        for (int i = 0; i < sym.getSpanCount(); ++i) {
            final SeqSpan span = sym.getSpan(i);
            System.out.println(indent + spacer + spanToString(span));
        }
        for (int j = 0; j < sym.getChildCount(); ++j) {
            final SeqSymmetry child_sym = sym.getChild(j);
            printSymmetry(indent + spacer, child_sym, spacer);
        }
    }
    
    public static String spanToString(final SeqSpan span) {
        if (span == null) {
            return "Span: null";
        }
        final BioSeq seq = span.getBioSeq();
        final DecimalFormat span_format = new DecimalFormat("#,###.###");
        return ((seq == null) ? "nullseq" : seq.getID()) + ": [" + span_format.format(span.getMin()) + " - " + span_format.format(span.getMax()) + "] (" + (span.isForward() ? "+" : "-") + span_format.format(span.getLength()) + ")";
    }
    
    public static String symToString(final SeqSymmetry sym) {
        if (sym == null) {
            return "SeqSymmetry == null";
        }
        return "sym.getID() is not implemented.";
    }
}
