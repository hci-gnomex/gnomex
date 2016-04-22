// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.RootSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.EventObject;

public final class SymSelectionEvent extends EventObject
{
    private final List<SeqSymmetry> selected_graph_syms;
    private final List<RootSeqSymmetry> all_selected_syms;
    private static final long serialVersionUID = 1L;
    
    public SymSelectionEvent(final Object src, final List<RootSeqSymmetry> all_syms, final List<SeqSymmetry> graph_syms) {
        super(src);
        if (all_syms == null) {
            this.all_selected_syms = Collections.emptyList();
        }
        else {
            this.all_selected_syms = all_syms;
        }
        if (graph_syms == null) {
            this.selected_graph_syms = Collections.emptyList();
        }
        else {
            this.selected_graph_syms = graph_syms;
        }
    }
    
    public List<RootSeqSymmetry> getAllSelectedSyms() {
        return this.all_selected_syms;
    }
    
    public List<SeqSymmetry> getSelectedGraphSyms() {
        return this.selected_graph_syms;
    }
}
