// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Hashtable;
import java.util.HashMap;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.Map;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import com.affymetrix.genometryImpl.SeqSpan;

public final class GenbankSym implements SeqSpan, SupportsCdsSpan, SymWithProps
{
    private final BioSeq seq;
    private final int txMin;
    private final int txMax;
    private String ID;
    boolean forward;
    int cdsMin;
    int cdsMax;
    IntArrayList blockMins;
    IntArrayList blockMaxs;
    private String type;
    Map<String, Object> props;
    
    public GenbankSym(final String type, final BioSeq seq, final int min, final int max, final String ID) {
        this.cdsMin = Integer.MIN_VALUE;
        this.cdsMax = Integer.MIN_VALUE;
        this.type = type;
        this.seq = seq;
        this.ID = ID;
        this.forward = (min < max);
        if (this.forward) {
            this.txMin = min - 1;
            this.txMax = max;
        }
        else {
            this.txMax = min;
            this.txMin = max - 1;
        }
    }
    
    @Override
    public boolean hasCdsSpan() {
        return this.cdsMin != Integer.MIN_VALUE;
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
        return this.ID;
    }
    
    public void setID(final String ID) {
        this.ID = ID;
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
        if (this.blockMins == null) {
            return 0;
        }
        return this.blockMins.size();
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (this.blockMins == null || this.blockMins.size() <= index) {
            return null;
        }
        if (this.forward) {
            return new GenbankChildSingletonSeqSym(this.blockMins.get(index) - 1, this.blockMaxs.get(index), this.seq);
        }
        return new GenbankChildSingletonSeqSym(this.blockMaxs.get(index) - 1, this.blockMins.get(index), this.seq);
    }
    
    public void addBlock(final int start, final int end) {
        if (this.blockMins == null) {
            this.blockMins = new IntArrayList();
            this.blockMaxs = new IntArrayList();
        }
        this.blockMins.add(start);
        this.blockMaxs.add(end);
    }
    
    public void addCDSBlock(final int start, final int end) {
        if (this.forward) {
            if (this.cdsMin == Integer.MIN_VALUE) {
                this.cdsMin = start - 1;
                this.cdsMax = end;
            }
            this.cdsMin = Math.min(this.cdsMin, start - 1);
            this.cdsMax = Math.max(this.cdsMax, end);
        }
        else {
            if (this.cdsMin == Integer.MIN_VALUE) {
                this.cdsMin = end - 1;
                this.cdsMax = start;
            }
            this.cdsMin = Math.min(this.cdsMin, end - 1);
            this.cdsMax = Math.max(this.cdsMax, start);
        }
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
        tprops.put("id", this.ID);
        tprops.put("type", this.type);
        if (this.props != null) {
            tprops.putAll(this.props);
        }
        return tprops;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("id")) {
            return this.ID;
        }
        if (key.equals("type")) {
            return this.type;
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
    
    class GenbankChildSingletonSeqSym extends SingletonSeqSymmetry implements SymWithProps
    {
        public GenbankChildSingletonSeqSym(final int start, final int end, final BioSeq seq) {
            super(start, end, seq);
        }
        
        @Override
        public String getID() {
            return GenbankSym.this.getID();
        }
        
        @Override
        public Map<String, Object> getProperties() {
            return GenbankSym.this.getProperties();
        }
        
        @Override
        public Map<String, Object> cloneProperties() {
            return GenbankSym.this.cloneProperties();
        }
        
        @Override
        public Object getProperty(final String key) {
            return GenbankSym.this.getProperty(key);
        }
        
        @Override
        public boolean setProperty(final String key, final Object val) {
            return GenbankSym.this.setProperty(key, val);
        }
    }
}
