// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.TypedSym;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import com.affymetrix.genometryImpl.Scored;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;

public class DASSymmetry extends SimpleSymWithProps implements Scored, SupportsCdsSpan, TypedSym
{
    private final float score;
    private final String type;
    
    DASSymmetry(final GroupBean group, final FeatureBean feature, final BioSeq sequence) {
        this.score = Float.NEGATIVE_INFINITY;
        if (!group.getType().isEmpty()) {
            this.type = group.getType();
        }
        else if (!feature.getTypeLabel().isEmpty()) {
            this.type = feature.getTypeLabel();
        }
        else {
            this.type = feature.getTypeID();
        }
        this.addSpan(new SimpleMutableSeqSpan(new SimpleMutableSeqSpan((feature.getOrientation() == DASFeatureParser.Orientation.REVERSE) ? feature.getEnd() : feature.getStart(), (feature.getOrientation() == DASFeatureParser.Orientation.REVERSE) ? feature.getStart() : feature.getEnd(), sequence)));
        this.setID(group.getID());
        this.addLinks(group.getLinks());
        this.setProperty("label", group.getLabel().isEmpty() ? group.getID() : group.getLabel());
    }
    
    DASSymmetry(final FeatureBean feature, final BioSeq sequence) {
        this.score = feature.getScore();
        this.type = (feature.getTypeLabel().isEmpty() ? feature.getTypeID() : feature.getTypeLabel());
        this.addSpan(new SimpleMutableSeqSpan((feature.getOrientation() == DASFeatureParser.Orientation.REVERSE) ? feature.getEnd() : feature.getStart(), (feature.getOrientation() == DASFeatureParser.Orientation.REVERSE) ? feature.getStart() : feature.getEnd(), sequence));
        this.setID(feature.getID());
        this.addLinks(feature.getLinks());
        this.setProperty("label", feature.getLabel().isEmpty() ? feature.getID() : feature.getLabel());
    }
    
    @Override
    public void addChild(final SeqSymmetry child) {
        super.addChild(child);
        if (child.getSpanCount() > 0 && this.getSpanCount() > 0 && this.getSpan(0) instanceof MutableSeqSpan) {
            SeqUtils.encompass(child.getSpan(0), this.getSpan(0), (MutableSeqSpan)this.getSpan(0));
        }
    }
    
    @Override
    public float getScore() {
        return this.score;
    }
    
    @Override
    public boolean hasCdsSpan() {
        for (final SeqSymmetry child : this.children) {
            if (this.isCdsSym(child)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public SeqSpan getCdsSpan() {
        MutableSeqSpan span = null;
        for (final SeqSymmetry child : this.children) {
            if (this.isCdsSym(child)) {
                for (int i = 0; i < child.getSpanCount(); ++i) {
                    if (span == null) {
                        span = new SimpleMutableSeqSpan(child.getSpan(i));
                    }
                    else {
                        SeqUtils.encompass(child.getSpan(i), span, span);
                    }
                }
            }
        }
        if (span == null) {
            throw new IllegalArgumentException("This Symmetry does not have a CDS");
        }
        return span;
    }
    
    private boolean isCdsSym(final SeqSymmetry sym) {
        return sym instanceof TypedSym && ((TypedSym)sym).getType().startsWith("exon:coding");
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    private void addLinks(final List<LinkBean> links) {
        if (links.size() == 1) {
            final String url = links.get(0).getURL();
            String title = links.get(0).getTitle();
            title = (title.isEmpty() ? url : title);
            this.setProperty("link", url);
            this.setProperty("link_name", (title == null) ? url : title);
        }
        else if (links.size() > 1) {
            final Map<String, String> linkMap = new HashMap<String, String>();
            for (final LinkBean linkBean : links) {
                final String url = linkBean.getURL();
                String title = linkBean.getTitle();
                title = (title.isEmpty() ? url : title);
                if (url != null && !url.isEmpty()) {
                    linkMap.put(title, url);
                }
            }
            this.setProperty("link", linkMap);
        }
    }
}
