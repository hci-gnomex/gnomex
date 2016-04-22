// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.HashMap;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.Scored;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import com.affymetrix.genometryImpl.SeqSpan;

public class UcscBedSym implements SeqSpan, SupportsCdsSpan, SymSpanWithCds, TypedSym, SymWithProps, Scored
{
    BioSeq seq;
    int txMin;
    int txMax;
    String name;
    float score;
    boolean forward;
    int cdsMin;
    int cdsMax;
    int[] blockMins;
    int[] blockMaxs;
    String type;
    Map<String, Object> props;
    boolean hasCdsSpan;
    
    public UcscBedSym(final String type, final BioSeq seq, final int txMin, final int txMax, final String name, final float score, final boolean forward, final int cdsMin, final int cdsMax, final int[] blockMins, final int[] blockMaxs) {
        this.cdsMin = Integer.MIN_VALUE;
        this.cdsMax = Integer.MIN_VALUE;
        this.hasCdsSpan = false;
        this.type = type;
        this.seq = seq;
        this.txMin = txMin;
        this.txMax = txMax;
        this.name = name;
        this.score = score;
        this.forward = forward;
        this.cdsMin = cdsMin;
        this.cdsMax = cdsMax;
        this.hasCdsSpan = (cdsMin != Integer.MIN_VALUE && cdsMax != Integer.MIN_VALUE && cdsMin != cdsMax);
        this.blockMins = blockMins;
        this.blockMaxs = blockMaxs;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public boolean hasCdsSpan() {
        return this.hasCdsSpan;
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
        if (this.blockMins == null) {
            return 0;
        }
        return this.blockMins.length;
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (this.blockMins == null || this.blockMins.length <= index) {
            return null;
        }
        if (this.forward) {
            return new BedChildSingletonSeqSym(this.blockMins[index], this.blockMaxs[index], this.seq);
        }
        return new BedChildSingletonSeqSym(this.blockMaxs[index], this.blockMins[index], this.seq);
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
    public float getScore() {
        return this.score;
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
        tprops.put("name", this.name);
        tprops.put("seq id", this.seq.getID());
        tprops.put("forward", this.forward);
        if (this.hasCdsSpan) {
            tprops.put("cds min", this.cdsMin);
            tprops.put("cds max", this.cdsMax);
        }
        if (this.score != Float.NEGATIVE_INFINITY) {
            tprops.put("score", new Float(this.score));
        }
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
        if (key.equals("method")) {
            return this.getType();
        }
        if (key.equals("name")) {
            return this.name;
        }
        if (key.equals("seq id")) {
            return this.seq.getID();
        }
        if (key.equals("forward")) {
            return this.forward;
        }
        if (this.hasCdsSpan && key.equals("cds min")) {
            return this.cdsMin;
        }
        if (this.hasCdsSpan && key.equals("cds max")) {
            return this.cdsMax;
        }
        if (key.equals("score") && this.score != Float.NEGATIVE_INFINITY) {
            return new Float(this.score);
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
    
    public void outputBedFormat(final DataOutputStream out) throws IOException {
        out.write(this.seq.getID().getBytes());
        out.write(9);
        out.write(Integer.toString(this.txMin).getBytes());
        out.write(9);
        out.write(Integer.toString(this.txMax).getBytes());
        if (this.name != null) {
            out.write(9);
            out.write(this.getName().getBytes());
            if (this.getScore() > Float.NEGATIVE_INFINITY) {
                out.write(9);
                if (this.getScore() == 0.0f) {
                    out.write(48);
                }
                else {
                    out.write(Float.toString(this.getScore()).getBytes());
                }
                out.write(9);
                if (this.isForward()) {
                    out.write(43);
                }
                else {
                    out.write(45);
                }
                if (this.cdsMin > Integer.MIN_VALUE && this.cdsMax > Integer.MIN_VALUE) {
                    out.write(9);
                    out.write(Integer.toString(this.cdsMin).getBytes());
                    out.write(9);
                    out.write(Integer.toString(this.cdsMax).getBytes());
                    final int child_count = this.getChildCount();
                    if (child_count > 0) {
                        out.write(9);
                        out.write(48);
                        out.write(9);
                        out.write(Integer.toString(child_count).getBytes());
                        out.write(9);
                        for (int i = 0; i < child_count; ++i) {
                            out.write(Integer.toString(this.blockMaxs[i] - this.blockMins[i]).getBytes());
                            out.write(44);
                        }
                        out.write(9);
                        for (int i = 0; i < child_count; ++i) {
                            out.write(Integer.toString(this.blockMins[i] - this.txMin).getBytes());
                            out.write(44);
                        }
                        this.outputAdditional(out);
                    }
                }
            }
        }
        out.write(10);
    }
    
    public int[] getBlockMins() {
        return this.blockMins;
    }
    
    public int[] getBlockMaxs() {
        return this.blockMaxs;
    }
    
    protected void outputAdditional(final DataOutputStream out) throws IOException {
    }
    
    @Override
    public String toString() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            this.outputBedFormat(new DataOutputStream(baos));
        }
        catch (IOException x) {
            return x.getMessage();
        }
        return baos.toString();
    }
    
    protected class BedChildSingletonSeqSym extends SingletonSeqSymmetry implements SymWithProps, Scored
    {
        public BedChildSingletonSeqSym(final int start, final int end, final BioSeq seq) {
            super(start, end, seq);
        }
        
        @Override
        public String getID() {
            return UcscBedSym.this.getID();
        }
        
        @Override
        public Map<String, Object> getProperties() {
            return UcscBedSym.this.getProperties();
        }
        
        @Override
        public Map<String, Object> cloneProperties() {
            return UcscBedSym.this.cloneProperties();
        }
        
        @Override
        public Object getProperty(final String key) {
            return UcscBedSym.this.getProperty(key);
        }
        
        @Override
        public boolean setProperty(final String key, final Object val) {
            return UcscBedSym.this.setProperty(key, val);
        }
        
        @Override
        public float getScore() {
            return UcscBedSym.this.getScore();
        }
    }
}
