// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

class FeatureBean
{
    private String id;
    private String label;
    private String typeID;
    private String typeCategory;
    private String typeLabel;
    private boolean typeReference;
    private String methodID;
    private String methodLabel;
    private int start;
    private int end;
    private float score;
    private DASFeatureParser.Orientation orientation;
    private char phase;
    private final List<String> notes;
    private final List<LinkBean> links;
    private final List<TargetBean> targets;
    private final List<GroupBean> groups;
    
    FeatureBean() {
        this.notes = new ArrayList<String>(2);
        this.links = new ArrayList<LinkBean>(2);
        this.targets = new ArrayList<TargetBean>(2);
        this.groups = new ArrayList<GroupBean>(2);
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
    
    void setTypeID(final String typeID) {
        this.typeID = typeID.intern();
    }
    
    String getTypeID() {
        return this.typeID;
    }
    
    void setTypeCategory(final String typeCategory) {
        this.typeCategory = typeCategory.intern();
    }
    
    String getTypeCategory() {
        return this.typeCategory;
    }
    
    void setTypeLabel(final String typeLabel) {
        this.typeLabel = typeLabel.intern();
    }
    
    String getTypeLabel() {
        return this.typeLabel;
    }
    
    void setTypeReference(final String typeReference) {
        this.typeReference = typeReference.equals("yes");
    }
    
    boolean isTypeReference() {
        return this.typeReference;
    }
    
    void setMethodID(final String methodID) {
        this.methodID = methodID.intern();
    }
    
    String getMethodID() {
        return this.methodID;
    }
    
    void setMethodLabel(final String methodLabel) {
        this.methodLabel = methodLabel.intern();
    }
    
    String getMethodLabel() {
        return this.methodLabel;
    }
    
    void setStart(final String start) {
        this.start = Integer.parseInt(start) - 1;
    }
    
    int getStart() {
        return this.start;
    }
    
    public void setEnd(final String end) {
        this.end = Integer.parseInt(end);
    }
    
    int getEnd() {
        return this.end;
    }
    
    void setScore(final String score) {
        this.score = (score.equals("-") ? Float.NEGATIVE_INFINITY : Float.parseFloat(score));
    }
    
    float getScore() {
        return this.score;
    }
    
    void setOrientation(final String orientation) {
        if (orientation.equals("+")) {
            this.orientation = DASFeatureParser.Orientation.FORWARD;
        }
        else if (orientation.equals("-")) {
            this.orientation = DASFeatureParser.Orientation.REVERSE;
        }
        else {
            this.orientation = DASFeatureParser.Orientation.UNKNOWN;
        }
    }
    
    DASFeatureParser.Orientation getOrientation() {
        return this.orientation;
    }
    
    void setPhase(final String phase) {
        this.phase = (phase.isEmpty() ? '-' : phase.charAt(0));
    }
    
    char getPhase() {
        return this.phase;
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
    
    void addGroup(final GroupBean group) {
        this.groups.add(group);
    }
    
    List<GroupBean> getGroups() {
        return Collections.unmodifiableList((List<? extends GroupBean>)this.groups);
    }
    
    void clear() {
        this.id = "";
        this.label = "";
        this.typeID = "";
        this.typeCategory = "";
        this.typeLabel = "";
        this.typeReference = false;
        this.methodID = "";
        this.methodLabel = "";
        this.start = 0;
        this.end = 0;
        this.score = Float.NEGATIVE_INFINITY;
        this.orientation = DASFeatureParser.Orientation.UNKNOWN;
        this.phase = '-';
        this.notes.clear();
        this.links.clear();
        this.targets.clear();
        this.groups.clear();
    }
}
