// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.SeqSpan;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;

public final class IntervalSearchSym extends SimpleSymWithProps implements SearchableSeqSymmetry
{
    private static final boolean DEBUG = false;
    private boolean ready_for_searching;
    private BioSeq search_seq;
    private List<SeqSymmetry> max_sym_sofar;
    private SeqSymMinComparator comp;
    
    public IntervalSearchSym(final BioSeq seq) {
        this.ready_for_searching = false;
        this.max_sym_sofar = null;
        this.comp = null;
        this.search_seq = seq;
    }
    
    public IntervalSearchSym(final BioSeq seq, final SeqSymmetry sym_to_copy) {
        this(seq);
        if (sym_to_copy instanceof SymWithProps) {
            this.setProperties(((SymWithProps)sym_to_copy).getProperties());
        }
        for (int child_count = sym_to_copy.getChildCount(), i = 0; i < child_count; ++i) {
            this.addChild(sym_to_copy.getChild(i));
        }
    }
    
    public boolean getOptimizedForSearch() {
        return this.ready_for_searching;
    }
    
    @Override
    public void addChild(final SeqSymmetry child) {
        super.addChild(child);
        this.ready_for_searching = false;
    }
    
    public void initForSearching(final BioSeq seq) {
        this.search_seq = seq;
        this.comp = new SeqSymMinComparator(this.search_seq);
        final int child_count = this.getChildCount();
        boolean sorted = true;
        int prev_min = Integer.MIN_VALUE;
        for (int i = 0; i < child_count; ++i) {
            final SeqSymmetry child = this.getChild(i);
            if (child != null) {
                if (child.getSpan(this.search_seq) != null) {
                    final int min = child.getSpan(this.search_seq).getMin();
                    if (prev_min > min) {
                        sorted = false;
                        break;
                    }
                    prev_min = min;
                }
            }
        }
        if (!sorted) {
            Collections.sort(this.getChildren(), this.comp);
            sorted = true;
        }
        this.determineMaxSymList(child_count);
        this.ready_for_searching = true;
    }
    
    private void determineMaxSymList(final int child_count) {
        this.max_sym_sofar = new ArrayList<SeqSymmetry>(child_count);
        SeqSymmetry curMaxSym = this.getChild(0);
        for (int i = 0; i < child_count; ++i) {
            final SeqSymmetry child = this.getChild(i);
            if (child != null) {
                if (child.getSpan(this.search_seq) != null) {
                    final int max = child.getSpan(this.search_seq).getMax();
                    if (max > curMaxSym.getSpan(this.search_seq).getMax()) {
                        curMaxSym = child;
                    }
                    this.max_sym_sofar.add(curMaxSym);
                }
            }
        }
    }
    
    @Override
    public List<SeqSymmetry> getOverlappingChildren(final SeqSpan qinterval) {
        final int child_count = this.getChildCount();
        if (child_count <= 0) {
            return null;
        }
        if (qinterval.getBioSeq() != this.search_seq) {
            this.ready_for_searching = false;
        }
        this.search_seq = qinterval.getBioSeq();
        final int search_min = qinterval.getMin();
        final int search_max = qinterval.getMax();
        if (!this.ready_for_searching) {
            this.initForSearching(this.search_seq);
        }
        final SeqSymmetry query_sym = new SingletonSeqSymmetry(search_min, search_max, this.search_seq);
        int beg_index = Collections.binarySearch(this.children, query_sym, this.comp);
        if (beg_index < 0) {
            beg_index = -beg_index - 1;
        }
        if (beg_index >= child_count) {
            beg_index = child_count - 1;
        }
        final int cur_min = this.getChild(beg_index).getSpan(this.search_seq).getMin();
        beg_index = this.minBackTrack(beg_index, cur_min);
        final int backtrack_max_index = this.maxBacktrack(beg_index, search_min);
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(1000);
        this.checkBackwards(backtrack_max_index, beg_index, search_min, search_max, results);
        this.checkForwards(beg_index, child_count, search_max, results);
        return results;
    }
    
    private int minBackTrack(int beg_index, final int cur_min) {
        while (beg_index > 0) {
            final int back_min = this.getChild(beg_index - 1).getSpan(this.search_seq).getMin();
            if (back_min != cur_min) {
                break;
            }
            --beg_index;
        }
        return beg_index;
    }
    
    private int maxBacktrack(final int beg_index, final int search_min) {
        int backtrack_max_index = beg_index;
        while (backtrack_max_index > 0) {
            --backtrack_max_index;
            final SeqSymmetry back_sym = this.max_sym_sofar.get(backtrack_max_index);
            if (back_sym.getSpan(this.search_seq).getMax() < search_min) {
                ++backtrack_max_index;
                break;
            }
        }
        return backtrack_max_index;
    }
    
    private void checkBackwards(final int backtrack_max_index, final int beg_index, final int search_min, final int search_max, final List<SeqSymmetry> results) {
        for (int i = backtrack_max_index; i <= beg_index; ++i) {
            final SeqSymmetry sym = this.children.get(i);
            final SeqSpan span = sym.getSpan(this.search_seq);
            if (span.getMax() > search_min && span.getMin() < search_max) {
                results.add(sym);
            }
        }
    }
    
    private void checkForwards(final int beg_index, final int child_count, final int search_max, final List<SeqSymmetry> results) {
        for (int cur_index = beg_index + 1; cur_index < child_count; ++cur_index) {
            final SeqSymmetry sym = this.children.get(cur_index);
            final SeqSpan span = sym.getSpan(this.search_seq);
            if (span.getMin() >= search_max) {
                break;
            }
            results.add(sym);
        }
    }
}
