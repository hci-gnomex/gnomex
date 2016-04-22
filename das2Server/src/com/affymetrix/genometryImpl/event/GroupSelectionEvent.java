// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.util.Collections;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.List;
import java.util.EventObject;

public final class GroupSelectionEvent extends EventObject
{
    private List<AnnotatedSeqGroup> selected_groups;
    private AnnotatedSeqGroup primary_selection;
    private static final long serialVersionUID = 1L;
    
    public GroupSelectionEvent(final Object src, final List<AnnotatedSeqGroup> groups) {
        super(src);
        this.primary_selection = null;
        this.selected_groups = groups;
        this.primary_selection = null;
        if (this.selected_groups == null) {
            this.selected_groups = Collections.emptyList();
        }
        else if (!this.selected_groups.isEmpty()) {
            this.primary_selection = groups.get(0);
        }
    }
    
    public AnnotatedSeqGroup getSelectedGroup() {
        return this.primary_selection;
    }
    
    @Override
    public String toString() {
        return "GroupSelectionEvent: group count: " + this.selected_groups.size() + " first group: '" + ((this.primary_selection == null) ? "null" : this.primary_selection.getID()) + "' source: " + this.getSource();
    }
}
