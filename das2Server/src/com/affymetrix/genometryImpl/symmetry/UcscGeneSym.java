// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Hashtable;
import java.util.HashMap;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import com.affymetrix.genometryImpl.SeqSpan;

public final class UcscGeneSym implements SeqSpan, SupportsCdsSpan, SymSpanWithCds, SymWithProps
{
    String geneName;
    String name;
    int txMin;
    int txMax;
    int cdsMin;
    int cdsMax;
    boolean forward;
    int[] emins;
    int[] emaxs;
    BioSeq seq;
    String type;
    Map<String, Object> props;
    
    public UcscGeneSym(final String type, final String geneName, final String name, final BioSeq seq, final boolean forward, final int txMin, final int txMax, final int cdsMin, final int cdsMax, final int[] emins, final int[] emaxs) {
        this.type = type;
        this.geneName = geneName;
        this.name = name;
        this.seq = seq;
        this.forward = forward;
        this.txMin = txMin;
        this.txMax = txMax;
        this.cdsMin = cdsMin;
        this.cdsMax = cdsMax;
        this.emins = emins;
        this.emaxs = emaxs;
    }
    
    public String getGeneName() {
        return this.geneName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public boolean hasCdsSpan() {
        return this.cdsMin >= 0 && this.cdsMax >= 0;
    }
    
    @Override
    public SeqSpan getCdsSpan() {
        if (!this.hasCdsSpan()) {
            return null;
        }
        if (this.forward) {
            return new SimpleSeqSpan(this.cdsMin, this.cdsMax, this.seq);
        }
        return new SimpleSeqSpan(this.cdsMax, this.cdsMin, this.seq);
    }
    
    @Override
    public String getID() {
        return this.name;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq bs) {
        if (bs.equals(this.seq)) {
            return this;
        }
        return null;
    }
    
    @Override
    public SeqSpan getSpan(final int index) {
        if (index == 0) {
            return this;
        }
        return null;
    }
    
    @Override
    public boolean getSpan(final BioSeq bs, final MutableSeqSpan span) {
        if (bs.equals(this.seq)) {
            if (this.forward) {
                span.set(this.txMin, this.txMax, this.seq);
            }
            else {
                span.set(this.txMax, this.txMin, this.seq);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            if (this.forward) {
                span.set(this.txMin, this.txMax, this.seq);
            }
            else {
                span.set(this.txMax, this.txMin, this.seq);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int getSpanCount() {
        return 1;
    }
    
    @Override
    public BioSeq getSpanSeq(final int index) {
        if (index == 0) {
            return this.seq;
        }
        return null;
    }
    
    @Override
    public int getChildCount() {
        return this.emins.length;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (this.forward) {
            return new SingletonSeqSymmetry(this.emins[index], this.emaxs[index], this.seq);
        }
        return new SingletonSeqSymmetry(this.emaxs[index], this.emins[index], this.seq);
    }
    
    @Override
    public int getStart() {
        return this.forward ? this.txMin : this.txMax;
    }
    
    @Override
    public int getEnd() {
        return this.forward ? this.txMax : this.txMin;
    }
    
    @Override
    public int getMin() {
        return this.txMin;
    }
    
    @Override
    public int getMax() {
        return this.txMax;
    }
    
    @Override
    public int getLength() {
        return this.txMax - this.txMin;
    }
    
    @Override
    public boolean isForward() {
        return this.forward;
    }
    
    @Override
    public BioSeq getBioSeq() {
        return this.seq;
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
    public double getMaxDouble() {
        return this.getMax();
    }
    
    @Override
    public double getMinDouble() {
        return this.getMin();
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
        return this.cloneProperties();
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final HashMap<String, Object> tprops = new HashMap<String, Object>();
        tprops.put("id", this.name);
        tprops.put("type", this.type);
        tprops.put("gene name", this.geneName);
        tprops.put("seq id", this.seq.getID());
        tprops.put("forward", this.forward);
        tprops.put("cds min", this.cdsMin);
        tprops.put("cds max", this.cdsMax);
        if (this.props != null) {
            tprops.putAll(this.props);
        }
        return tprops;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("id")) {
            return this.name;
        }
        if (key.equals("type")) {
            return this.getType();
        }
        if (key.equals("gene name") || key.equals("gene_name")) {
            return this.geneName;
        }
        if (key.equals("seq id")) {
            return this.seq.getID();
        }
        if (key.equals("forward")) {
            return this.forward;
        }
        if (key.equals("cds min")) {
            return this.cdsMin;
        }
        if (key.equals("cds max")) {
            return this.cdsMax;
        }
        if (this.props != null) {
            return this.props.get(key);
        }
        return null;
    }
    
    @Override
    public boolean setProperty(final String name, final Object val) {
        if (this.props == null) {
            this.props = new Hashtable<String, Object>();
        }
        this.props.put(name, val);
        return true;
    }
}
