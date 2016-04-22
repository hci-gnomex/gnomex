// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;

public final class EfficientPairSeqSymmetry implements SeqSymmetry, SymWithResidues
{
    private static final int count = 2;
    private final int startA;
    private final int startB;
    private final int endA;
    private final int endB;
    private final BioSeq seqA;
    private final BioSeq seqB;
    private final String residues;
    
    public EfficientPairSeqSymmetry(final int startA, final int endA, final BioSeq seqA, final int startB, final int endB, final BioSeq seqB, final String residues) {
        this.startA = startA;
        this.startB = startB;
        this.endA = endA;
        this.endB = endB;
        this.seqA = seqA;
        this.seqB = seqB;
        this.residues = residues;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq seq) {
        if (this.seqA == seq) {
            return new SimpleSeqSpan(this.startA, this.endA, this.seqA);
        }
        if (this.seqB == seq) {
            return new SimpleSeqSpan(this.startB, this.endB, this.seqB);
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        return 2;
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        if (i == 0) {
            return new SimpleSeqSpan(this.startA, this.endA, this.seqA);
        }
        if (i == 1) {
            return new SimpleSeqSpan(this.startB, this.endB, this.seqB);
        }
        return null;
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        if (i == 0) {
            return this.seqA;
        }
        if (i == 1) {
            return this.seqB;
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq seq, final MutableSeqSpan span) {
        if (this.seqA == seq) {
            span.setStart(this.startA);
            span.setEnd(this.endA);
            span.setBioSeq(this.seqA);
            return true;
        }
        if (this.seqB == seq) {
            span.setStart(this.startB);
            span.setEnd(this.endB);
            span.setBioSeq(this.seqB);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.setStart(this.startA);
            span.setEnd(this.endA);
            span.setBioSeq(this.seqA);
            return true;
        }
        if (index == 1) {
            span.setStart(this.startB);
            span.setEnd(this.endB);
            span.setBioSeq(this.seqB);
            return true;
        }
        return false;
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        return null;
    }
    
    @Override
    public String getID() {
        return null;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.cloneProperties();
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final Map<String, Object> props = new HashMap<String, Object>();
        props.put("residues", this.residues);
        return props;
    }
    
    @Override
    public Object getProperty(final String key) {
        if ("residues".equalsIgnoreCase(key)) {
            return this.residues;
        }
        return null;
    }
    
    @Override
    public String getResidues() {
        return this.residues;
    }
    
    @Override
    public String getResidues(final int start, final int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean setProperty(final String key, final Object val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
