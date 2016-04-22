// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.util.Collections;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;
import java.util.EventObject;

public final class SeqSelectionEvent extends EventObject
{
    private final List<BioSeq> selected_seqs;
    private BioSeq primary_selection;
    private static final long serialVersionUID = 1L;
    
    public SeqSelectionEvent(final Object src, final List<BioSeq> seqs) {
        super(src);
        this.primary_selection = null;
        if (seqs == null) {
            this.selected_seqs = Collections.emptyList();
        }
        else {
            this.selected_seqs = seqs;
            if (!this.selected_seqs.isEmpty()) {
                this.primary_selection = this.selected_seqs.get(0);
            }
        }
    }
    
    public BioSeq getSelectedSeq() {
        return this.primary_selection;
    }
    
    @Override
    public String toString() {
        return "SeqSelectionEvent: seq count: " + this.selected_seqs.size() + " first seq: '" + ((this.primary_selection == null) ? "null" : this.primary_selection.getID()) + "' source: " + this.getSource();
    }
}
