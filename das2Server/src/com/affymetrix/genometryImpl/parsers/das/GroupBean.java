// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

class GroupBean
{
    private String id;
    private String label;
    private String type;
    private List<String> notes;
    private List<LinkBean> links;
    private List<TargetBean> targets;
    
    GroupBean() {
        this.notes = new ArrayList<String>(2);
        this.links = new ArrayList<LinkBean>(2);
        this.targets = new ArrayList<TargetBean>(2);
        this.clear();
    }
    
    void setID(final String id) {
        this.id = id.intern();
    }
    
    String getID() {
        return this.id;
    }
    
    void setLabel(final String label) {
        this.label = label.intern();
    }
    
    String getLabel() {
        return this.label;
    }
    
    void setType(final String type) {
        this.type = type.intern();
    }
    
    String getType() {
        return this.type;
    }
    
    void addNote(final String note) {
        this.notes.add(note.intern());
    }
    
    List<String> getNotes() {
        return Collections.unmodifiableList((List<? extends String>)this.notes);
    }
    
    void addLink(final LinkBean link) {
        this.links.add(link);
    }
    
    List<LinkBean> getLinks() {
        return Collections.unmodifiableList((List<? extends LinkBean>)this.links);
    }
    
    void addTarget(final TargetBean target) {
        this.targets.add(target);
    }
    
    List<TargetBean> getTargets() {
        return Collections.unmodifiableList((List<? extends TargetBean>)this.targets);
    }
    
    void clear() {
        this.id = "";
        this.label = "";
        this.type = "";
        this.notes.clear();
        this.links.clear();
        this.targets.clear();
    }
}
