// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Map;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.List;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;

public final class Psl3Sym extends UcscPslSym
{
    static int OTHER_INDEX;
    BioSeq otherseq;
    boolean same_other_orientation;
    int omin;
    int omax;
    int[] omins;
    boolean overlapping_other_coords;
    
    public Psl3Sym(final String type, final int matches, final int mismatches, final int repmatches, final int ncount, final int qNumInsert, final int qBaseInsert, final int tNumInsert, final int tBaseInsert, final boolean same_target_orientation, final boolean same_other_orientation, final BioSeq queryseq, final int qmin, final int qmax, final BioSeq targetseq, final int tmin, final int tmax, final BioSeq otherseq, final int omin, final int omax, final int blockcount, final int[] blockSizes, final int[] qmins, final int[] tmins, final int[] omins) {
        super(type, matches, mismatches, repmatches, ncount, qNumInsert, qBaseInsert, tNumInsert, tBaseInsert, same_target_orientation, queryseq, qmin, qmax, targetseq, tmin, tmax, blockcount, blockSizes, qmins, tmins);
        this.overlapping_other_coords = false;
        this.otherseq = otherseq;
        this.omin = omin;
        this.omax = omax;
        this.same_other_orientation = same_other_orientation;
        this.omins = omins;
    }
    
    @Override
    public int getSpanCount() {
        return 3;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq bs) {
        if (bs.equals(this.otherseq)) {
            SeqSpan span = null;
            if (this.same_other_orientation) {
                span = new SimpleSeqSpan(this.omin, this.omax, this.otherseq);
            }
            else {
                span = new SimpleSeqSpan(this.omax, this.omin, this.otherseq);
            }
            return span;
        }
        return super.getSpan(bs);
    }
    
    @Override
    public boolean getSpan(final BioSeq bs, final MutableSeqSpan span) {
        if (bs.equals(this.otherseq)) {
            if (this.same_other_orientation) {
                span.set(this.omin, this.omax, this.otherseq);
            }
            else {
                span.set(this.omax, this.omin, this.otherseq);
            }
            return true;
        }
        return super.getSpan(bs, span);
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == Psl3Sym.OTHER_INDEX) {
            if (this.same_other_orientation) {
                span.set(this.omin, this.omax, this.otherseq);
            }
            else {
                span.set(this.omax, this.omin, this.otherseq);
            }
            return true;
        }
        return super.getSpan(index, span);
    }
    
    @Override
    public SeqSpan getSpan(final int index) {
        if (index == Psl3Sym.OTHER_INDEX) {
            SeqSpan span = null;
            if (this.same_other_orientation) {
                span = new SimpleSeqSpan(this.omin, this.omax, this.otherseq);
            }
            else {
                span = new SimpleSeqSpan(this.omax, this.omin, this.otherseq);
            }
            return span;
        }
        return super.getSpan(index);
    }
    
    @Override
    public BioSeq getSpanSeq(final int index) {
        if (index == Psl3Sym.OTHER_INDEX) {
            return this.otherseq;
        }
        return super.getSpanSeq(index);
    }
    
    @Override
    public SeqSymmetry getChild(final int i) {
        int t1;
        int t2;
        if (this.same_orientation) {
            t1 = this.tmins[i];
            t2 = this.tmins[i] + this.blockSizes[i];
        }
        else {
            t1 = this.tmins[i] + this.blockSizes[i];
            t2 = this.tmins[i];
        }
        int o1;
        int o2;
        if (this.same_other_orientation) {
            o1 = this.omins[i];
            o2 = this.omins[i] + this.blockSizes[i];
        }
        else {
            o1 = this.omins[i] + this.blockSizes[i];
            o2 = this.omins[i];
        }
        return new LeafTrioSeqSymmetry(this.qmins[i], this.qmins[i] + this.blockSizes[i], this.queryseq, t1, t2, this.targetseq, o1, o2, this.otherseq);
    }
    
    @Override
    public List<SeqSymmetry> getOverlappingChildren(final SeqSpan input_span) {
        if (input_span.getBioSeq() == this.otherseq) {
            return SeqUtils.getOverlappingChildren(this, input_span);
        }
        return super.getOverlappingChildren(input_span);
    }
    
    public BioSeq getOtherSeq() {
        return this.otherseq;
    }
    
    public int getOtherMin() {
        return this.omin;
    }
    
    public int getOtherMax() {
        return this.omax;
    }
    
    public boolean getSameOtherOrientation() {
        return this.same_other_orientation;
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final Map<String, Object> tprops = super.cloneProperties();
        tprops.put("other seq", this.getOtherSeq().getID());
        tprops.put("same other orientation", this.getSameOtherOrientation());
        return tprops;
    }
    
    public void outputPsl3Format(final DataOutputStream out) throws IOException {
        this.outputStandardPsl(out, false);
        if (this.same_other_orientation) {
            out.write(43);
        }
        else {
            out.write(45);
        }
        out.write(9);
        out.write(this.otherseq.getID().getBytes());
        out.write(9);
        out.write(Integer.toString(this.otherseq.getLength()).getBytes());
        out.write(9);
        out.write(Integer.toString(this.omin).getBytes());
        out.write(9);
        out.write(Integer.toString(this.omax).getBytes());
        out.write(9);
        for (int blockcount = this.getChildCount(), i = 0; i < blockcount; ++i) {
            out.write(Integer.toString(this.omins[i]).getBytes());
            out.write(44);
        }
        out.write(9);
        this.outputPropTagVals(out);
        out.write(10);
    }
    
    static {
        Psl3Sym.OTHER_INDEX = 2;
    }
}
