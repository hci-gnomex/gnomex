// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Arrays;
import com.affymetrix.genometryImpl.SharedProbesetInfo;
import com.affymetrix.genometryImpl.IntId;
import com.affymetrix.genometryImpl.SeqSpan;

public final class EfficientProbesetSymA implements SeqSpan, SymWithProps, IntId
{
    SharedProbesetInfo info;
    boolean forward;
    int nid;
    int[] child_mins;
    
    public EfficientProbesetSymA(final SharedProbesetInfo info, final int[] cmins, final boolean forward, final int nid) {
        this.info = info;
        this.child_mins = cmins;
        this.forward = forward;
        this.nid = nid;
        Arrays.sort(this.child_mins);
    }
    
    public MutableSeqSpan getChildSpan(final int child_index, final BioSeq aseq, final MutableSeqSpan result_span) {
        if (child_index >= this.child_mins.length || aseq != this.getBioSeq() || result_span == null) {
            return null;
        }
        if (this.forward) {
            result_span.set(this.child_mins[child_index], this.child_mins[child_index] + this.getProbeLength(), aseq);
        }
        else {
            result_span.set(this.child_mins[child_index] + this.getProbeLength(), this.child_mins[child_index], aseq);
        }
        return result_span;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq bs) {
        if (this.getBioSeq() == bs) {
            return this;
        }
        return null;
    }
    
    @Override
    public int getSpanCount() {
        return 1;
    }
    
    @Override
    public SeqSpan getSpan(final int i) {
        if (i == 0) {
            return this;
        }
        return null;
    }
    
    @Override
    public BioSeq getSpanSeq(final int i) {
        if (i == 0) {
            return this.getBioSeq();
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq bs, final MutableSeqSpan span) {
        if (this.getBioSeq() == bs) {
            span.setStart(this.getStart());
            span.setEnd(this.getEnd());
            span.setBioSeq(this.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.setStart(this.getStart());
            span.setEnd(this.getEnd());
            span.setBioSeq(this.getBioSeq());
            return true;
        }
        return false;
    }
    
    @Override
    public int getChildCount() {
        return this.child_mins.length;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (index >= this.getChildCount()) {
            return null;
        }
        int start;
        int end;
        if (this.forward) {
            start = this.child_mins[index];
            end = start + this.getProbeLength();
        }
        else {
            end = this.child_mins[index];
            start = end + this.getProbeLength();
        }
        return new LeafSingletonSymmetry(start, end, this.getBioSeq());
    }
    
    @Override
    public int getIntID() {
        return this.nid;
    }
    
    public int getProbeLength() {
        return this.info.getProbeLength();
    }
    
    public String getIDPrefix() {
        return this.info.getIDPrefix();
    }
    
    @Override
    public String getID() {
        final String rootid = Integer.toString(this.getIntID());
        if (this.getIDPrefix() == null) {
            return rootid;
        }
        return this.getIDPrefix() + rootid;
    }
    
    @Override
    public int getStart() {
        if (this.forward) {
            return this.child_mins[0];
        }
        return this.child_mins[this.child_mins.length - 1] + this.getProbeLength();
    }
    
    @Override
    public int getEnd() {
        if (this.forward) {
            return this.child_mins[this.child_mins.length - 1] + this.getProbeLength();
        }
        return this.child_mins[0];
    }
    
    @Override
    public int getMin() {
        return this.child_mins[0];
    }
    
    @Override
    public int getMax() {
        return this.child_mins[this.child_mins.length - 1] + this.getProbeLength();
    }
    
    @Override
    public int getLength() {
        return this.getMax() - this.getMin();
    }
    
    @Override
    public boolean isForward() {
        return this.forward;
    }
    
    @Override
    public BioSeq getBioSeq() {
        return this.info.getBioSeq();
    }
    
    @Override
    public double getStartDouble() {
        return this.getStart();
    }
    
    @Override
    public double getEndDouble() {
        return this.getEnd();
    }
    
    @Override
    public double getMinDouble() {
        return this.getMin();
    }
    
    @Override
    public double getMaxDouble() {
        return this.getMax();
    }
    
    @Override
    public double getLengthDouble() {
        return this.getLength();
    }
    
    @Override
    public boolean isIntegral() {
        return true;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        final HashMap<String, Object> properties = new HashMap<String, Object>(1);
        final Map<String, Object> shared_props = this.info.getProps();
        if (shared_props != null && shared_props.get("method") != null) {
            properties.put("method", shared_props.get("method"));
        }
        properties.put("id", "" + this.getID());
        return properties;
    }
    
    @Override
    public boolean setProperty(final String key, final Object val) {
        return false;
    }
    
    @Override
    public Object getProperty(final String key) {
        final Map<String, Object> shared_props = this.info.getProps();
        if ("method".equals(key) && shared_props != null) {
            return shared_props.get("method");
        }
        if ("id".equals(key)) {
            return this.getID();
        }
        return null;
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        return this.getProperties();
    }
}
