//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl;

import java.util.HashSet;
import com.affymetrix.genometryImpl.event.SymSelectionEvent;
import com.affymetrix.genometryImpl.symmetry.RootSeqSymmetry;
import com.affymetrix.genometryImpl.event.SeqSelectionEvent;
import com.affymetrix.genometryImpl.event.GroupSelectionEvent;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.affymetrix.genometryImpl.event.SymSelectionListener;
import com.affymetrix.genometryImpl.event.GroupSelectionListener;
import com.affymetrix.genometryImpl.event.SeqSelectionListener;
import java.util.Set;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.Map;

public final class GenometryModel
{
    private static GenometryModel smodel;
    private static final boolean DEBUG = false;
    private final Map<String, AnnotatedSeqGroup> seq_groups;
    private final Map<BioSeq, List<SeqSymmetry>> seq2selectedGraphSymsHash;
    private final Set<SeqSelectionListener> seq_selection_listeners;
    private final Set<GroupSelectionListener> group_selection_listeners;
    private final Set<SymSelectionListener> sym_selection_listeners;
    private AnnotatedSeqGroup selected_group;
    private BioSeq selected_seq;

    private GenometryModel() {
        this.seq_groups = new LinkedHashMap<String, AnnotatedSeqGroup>();
        this.seq2selectedGraphSymsHash = new HashMap<BioSeq, List<SeqSymmetry>>();
        this.seq_selection_listeners = new CopyOnWriteArraySet<SeqSelectionListener>();
        this.group_selection_listeners = new CopyOnWriteArraySet<GroupSelectionListener>();
        this.sym_selection_listeners = new CopyOnWriteArraySet<SymSelectionListener>();
        this.selected_group = null;
        this.selected_seq = null;
    }

    public static GenometryModel getGenometryModel() {
        return GenometryModel.smodel;
    }

    public void resetGenometryModel() {
        this.seq_groups.clear();
        this.seq2selectedGraphSymsHash.clear();
        this.seq_selection_listeners.clear();
        this.group_selection_listeners.clear();
        this.sym_selection_listeners.clear();
        this.selected_group = null;
        this.selected_seq = null;
    }

    public Map<String, AnnotatedSeqGroup> getSeqGroups() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends AnnotatedSeqGroup>)this.seq_groups);
    }

    public synchronized List<String> getSeqGroupNames() {
        final List<String> list = new ArrayList<String>(this.seq_groups.keySet());
        Collections.sort(list);
        return Collections.unmodifiableList((List<? extends String>)list);
    }

    public AnnotatedSeqGroup getSeqGroup(final String group_syn) {
        if (group_syn == null) {
            return null;
        }
        final AnnotatedSeqGroup group = this.seq_groups.get(group_syn);
        if (group == null) {
            for (final AnnotatedSeqGroup curgroup : this.seq_groups.values()) {
                if (curgroup.isSynonymous(group_syn)) {
                    return curgroup;
                }
            }
        }
        return group;
    }

    public synchronized AnnotatedSeqGroup addSeqGroup(final String group_id) {
        AnnotatedSeqGroup group = this.getSeqGroup(group_id);
        if (group == null) {
            group = this.createSeqGroup(group_id);
            this.seq_groups.put(group.getID(), group);
        }
        return group;
    }

    protected AnnotatedSeqGroup createSeqGroup(final String group_id) {
        return new AnnotatedSeqGroup(group_id);
    }

    public void removeSeqGroup(final String group_id) {
        this.seq_groups.remove(group_id);
    }

    public synchronized void addSeqGroup(final AnnotatedSeqGroup group) {
        this.seq_groups.put(group.getID(), group);
    }

    public AnnotatedSeqGroup getSelectedSeqGroup() {
        return this.selected_group;
    }

    public void setSelectedSeqGroup(final AnnotatedSeqGroup group) {
        this.selected_group = group;
        this.selected_seq = null;
        final List<AnnotatedSeqGroup> glist = new ArrayList<AnnotatedSeqGroup>();
        glist.add(this.selected_group);
        this.fireGroupSelectionEvent(this, glist);
    }

    public void addGroupSelectionListener(final GroupSelectionListener listener) {
        this.group_selection_listeners.add(listener);
    }

    public void removeGroupSelectionListener(final GroupSelectionListener listener) {
        this.group_selection_listeners.remove(listener);
    }

    private void fireGroupSelectionEvent(final Object src, final List<AnnotatedSeqGroup> glist) {
        final GroupSelectionEvent evt = new GroupSelectionEvent(src, glist);
        for (final GroupSelectionListener listener : this.group_selection_listeners) {
            listener.groupSelectionChanged(evt);
        }
    }

    public BioSeq getSelectedSeq() {
        return this.selected_seq;
    }

    public void setSelectedSeq(final BioSeq seq) {
        this.setSelectedSeq(seq, this);
    }

    public void setSelectedSeq(final BioSeq seq, final Object src) {
        this.selected_seq = seq;
        final ArrayList<BioSeq> slist = new ArrayList<BioSeq>();
        slist.add(this.selected_seq);
        this.fireSeqSelectionEvent(src, slist);
    }

    public void addSeqSelectionListener(final SeqSelectionListener listener) {
        this.seq_selection_listeners.add(listener);
    }

    public void removeSeqSelectionListener(final SeqSelectionListener listener) {
        this.seq_selection_listeners.remove(listener);
    }

    void fireSeqSelectionEvent(final Object src, final List<BioSeq> slist) {
        final SeqSelectionEvent evt = new SeqSelectionEvent(src, slist);
        for (final SeqSelectionListener listener : this.seq_selection_listeners) {
            listener.seqSelectionChanged(evt);
        }
    }

    public void addSymSelectionListener(final SymSelectionListener listener) {
        this.sym_selection_listeners.add(listener);
    }

    public void removeSymSelectionListener(final SymSelectionListener listener) {
        this.sym_selection_listeners.remove(listener);
    }

    private void fireSymSelectionEvent(final Object src, final List<RootSeqSymmetry> all_syms, final List<SeqSymmetry> graph_syms) {
        final SymSelectionEvent sevt = new SymSelectionEvent(src, all_syms, graph_syms);
        for (final SymSelectionListener listener : this.sym_selection_listeners) {
            listener.symSelectionChanged(sevt);
        }
    }

    public void setSelectedSymmetries(final List<RootSeqSymmetry> all_syms, final List<SeqSymmetry> graph_syms, final Object src) {
        this.setSelectedSymmetries(graph_syms);
        this.fireSymSelectionEvent(src, all_syms, graph_syms);
    }

    public void setSelectedSymmetriesAndSeq(final List<SeqSymmetry> graph_syms, final Object src) {
        final List<BioSeq> seqs_with_selections = this.setSelectedSymmetries(graph_syms);
        if (!seqs_with_selections.contains(this.getSelectedSeq()) && this.getSelectedSymmetries(this.getSelectedSeq()).isEmpty()) {
            BioSeq seq = null;
            if (!seqs_with_selections.isEmpty()) {
                seq = seqs_with_selections.get(0);
            }
            this.setSelectedSeq(seq, src);
        }
        final List<RootSeqSymmetry> all_syms = Collections.emptyList();
        this.fireSymSelectionEvent(src, all_syms, graph_syms);
    }

    private List<BioSeq> setSelectedSymmetries(final List<SeqSymmetry> syms) {
        final HashMap<BioSeq, List<SeqSymmetry>> seq2GraphSymsHash = new HashMap<BioSeq, List<SeqSymmetry>>();
        final HashSet<BioSeq> all_seqs = new HashSet<BioSeq>();
        for (final SeqSymmetry sym : syms) {
            if (sym == null) {
                continue;
            }
            BioSeq seq = null;
            if (this.getSelectedSeqGroup() != null) {
                seq = this.getSelectedSeqGroup().getSeq(sym);
            }
            if (seq == null) {
                continue;
            }
            List<SeqSymmetry> symlist = seq2GraphSymsHash.get(seq);
            if (symlist == null) {
                symlist = new ArrayList<SeqSymmetry>();
                seq2GraphSymsHash.put(seq, symlist);
            }
            symlist.add(sym);
            all_seqs.add(seq);
        }
        this.clearSelectedSymmetries();
        for (final Map.Entry<BioSeq, List<SeqSymmetry>> entry : seq2GraphSymsHash.entrySet()) {
            this.setSelectedSymmetries(entry.getValue(), entry.getKey());
        }
        return new ArrayList<BioSeq>(all_seqs);
    }

    private void setSelectedSymmetries(final List<SeqSymmetry> syms, final BioSeq seq) {
        if (seq == null) {
            return;
        }
        if (syms != null && !syms.isEmpty()) {
            this.seq2selectedGraphSymsHash.put(seq, syms);
        }
        else {
            this.seq2selectedGraphSymsHash.remove(seq);
        }
    }

    public final List<SeqSymmetry> getSelectedSymmetries(final BioSeq seq) {
        List<SeqSymmetry> selections = this.seq2selectedGraphSymsHash.get(seq);
        if (selections == null) {
            selections = new ArrayList<SeqSymmetry>();
        }
        return selections;
    }

    public void clearSelectedSymmetries(final Object src) {
        this.clearSelectedSymmetries();
//        this.fireSymSelectionEvent(src, Collections.emptyList(), Collections.emptyList());
        fireSymSelectionEvent(src, Collections.<RootSeqSymmetry>emptyList(), Collections.<SeqSymmetry>emptyList());
    }

    private void clearSelectedSymmetries() {
        for (final List<SeqSymmetry> list : this.seq2selectedGraphSymsHash.values()) {
            list.clear();
        }
        this.seq2selectedGraphSymsHash.clear();
    }

    static {
        GenometryModel.smodel = new GenometryModel();
    }
}
