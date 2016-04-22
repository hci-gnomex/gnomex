// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Iterator;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.List;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;

public class UcscPslSym implements TypedSym, SearchableSeqSymmetry, SymWithProps
{
    public static final int QUERY_INDEX = 0;
    public static final int TARGET_INDEX = 1;
    private String type;
    private int matches;
    private int mismatches;
    private int repmatches;
    private int ncount;
    private int qNumInsert;
    private int qBaseInsert;
    private int tNumInsert;
    private int tBaseInsert;
    protected boolean same_orientation;
    private boolean overlapping_query_coords;
    protected BioSeq queryseq;
    private int qmin;
    private int qmax;
    protected BioSeq targetseq;
    private int tmin;
    private int tmax;
    private String[] target_res_arr;
    protected int[] blockSizes;
    protected int[] qmins;
    protected int[] tmins;
    private Map<String, Object> props;
    
    public UcscPslSym(final String type, final int matches, final int mismatches, final int repmatches, final int ncount, final int qNumInsert, final int qBaseInsert, final int tNumInsert, final int tBaseInsert, final boolean same_orientation, final BioSeq queryseq, final int qmin, final int qmax, final BioSeq targetseq, final int tmin, final int tmax, final int blockcount, final int[] blockSizes, final int[] qmins, final int[] tmins) {
        this(type, matches, mismatches, repmatches, ncount, qNumInsert, qBaseInsert, tNumInsert, tBaseInsert, same_orientation, queryseq, qmin, qmax, targetseq, tmin, tmax, null, blockcount, blockSizes, qmins, tmins);
    }
    
    public UcscPslSym(final String type, final int matches, final int mismatches, final int repmatches, final int ncount, final int qNumInsert, final int qBaseInsert, final int tNumInsert, final int tBaseInsert, final boolean same_orientation, final BioSeq queryseq, final int qmin, final int qmax, final BioSeq targetseq, final int tmin, final int tmax, final String[] target_res_arr, final int blockcount, final int[] blockSizes, final int[] qmins, final int[] tmins) {
        this.overlapping_query_coords = false;
        this.type = type;
        this.matches = matches;
        this.mismatches = mismatches;
        this.repmatches = repmatches;
        this.ncount = ncount;
        this.qNumInsert = qNumInsert;
        this.qBaseInsert = qBaseInsert;
        this.tNumInsert = tNumInsert;
        this.tBaseInsert = tBaseInsert;
        this.same_orientation = same_orientation;
        this.queryseq = queryseq;
        this.qmin = qmin;
        this.qmax = qmax;
        this.targetseq = targetseq;
        this.tmin = tmin;
        this.tmax = tmax;
        this.target_res_arr = target_res_arr;
        this.blockSizes = blockSizes;
        this.qmins = qmins;
        this.tmins = tmins;
        final int count = qmins.length - 1;
        int prevmin = 0;
        for (int i = 0; i < count; ++i) {
            if (qmins[i] < prevmin || qmins[i] + blockSizes[i] > qmins[i + 1]) {
                this.overlapping_query_coords = true;
                break;
            }
            prevmin = qmins[i];
        }
    }
    
    private static String getResidue(final String[] target_res_arr) {
        if (target_res_arr == null) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(2000);
        for (int i = 0; i < target_res_arr.length; ++i) {
            builder.append(target_res_arr[i]);
        }
        return builder.toString();
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public String getID() {
        return this.queryseq.getID();
    }
    
    @Override
    public int getSpanCount() {
        return 2;
    }
    
    @Override
    public SeqSpan getSpan(final BioSeq bs) {
        SeqSpan span = null;
        if (bs.equals(this.targetseq)) {
            if (this.same_orientation) {
                span = new SimpleSeqSpan(this.tmin, this.tmax, this.targetseq);
            }
            else {
                span = new SimpleSeqSpan(this.tmax, this.tmin, this.targetseq);
            }
        }
        else if (bs.equals(this.queryseq)) {
            span = new SimpleSeqSpan(this.qmin, this.qmax, this.queryseq);
        }
        return span;
    }
    
    @Override
    public boolean getSpan(final BioSeq bs, final MutableSeqSpan span) {
        if (bs.equals(this.targetseq)) {
            if (this.same_orientation) {
                span.set(this.tmin, this.tmax, this.targetseq);
            }
            else {
                span.set(this.tmax, this.tmin, this.targetseq);
            }
            return true;
        }
        if (bs.equals(this.queryseq)) {
            span.set(this.qmin, this.qmax, this.queryseq);
        }
        return false;
    }
    
    @Override
    public boolean getSpan(final int index, final MutableSeqSpan span) {
        if (index == 0) {
            span.set(this.qmin, this.qmax, this.queryseq);
        }
        else if (index == 1) {
            if (this.same_orientation) {
                span.set(this.tmin, this.tmax, this.targetseq);
            }
            else {
                span.set(this.tmax, this.tmin, this.targetseq);
            }
        }
        return false;
    }
    
    @Override
    public SeqSpan getSpan(final int index) {
        SeqSpan span = null;
        if (index == 0) {
            span = new SimpleSeqSpan(this.qmin, this.qmax, this.queryseq);
        }
        else if (index == 1) {
            if (this.same_orientation) {
                span = new SimpleSeqSpan(this.tmin, this.tmax, this.targetseq);
            }
            else {
                span = new SimpleSeqSpan(this.tmax, this.tmin, this.targetseq);
            }
        }
        return span;
    }
    
    @Override
    public BioSeq getSpanSeq(final int index) {
        if (index == 0) {
            return this.queryseq;
        }
        if (index == 1) {
            return this.targetseq;
        }
        return null;
    }
    
    @Override
    public int getChildCount() {
        return this.blockSizes.length;
    }
    
    @Override
    public SeqSymmetry getChild(final int i) {
        if (this.same_orientation) {
            return new EfficientPairSeqSymmetry(this.qmins[i], this.qmins[i] + this.blockSizes[i], this.queryseq, this.tmins[i], this.tmins[i] + this.blockSizes[i], this.targetseq, this.getChildResidue(i));
        }
        return new EfficientPairSeqSymmetry(this.qmins[i], this.qmins[i] + this.blockSizes[i], this.queryseq, this.tmins[i] + this.blockSizes[i], this.tmins[i], this.targetseq, this.getChildResidue(i));
    }
    
    private String getChildResidue(final int i) {
        if (this.target_res_arr != null && this.target_res_arr.length > i) {
            return this.target_res_arr[i];
        }
        return "";
    }
    
    @Override
    public List<SeqSymmetry> getOverlappingChildren(final SeqSpan input_span) {
        final boolean debug = false;
        List<SeqSymmetry> results = null;
        if (input_span.getBioSeq() != this.getQuerySeq()) {
            results = SeqUtils.getOverlappingChildren(this, input_span);
        }
        else if (this.overlapping_query_coords) {
            results = SeqUtils.getOverlappingChildren(this, input_span);
        }
        else {
            final int input_min = input_span.getMin();
            final int input_max = input_span.getMax();
            int beg_index = Arrays.binarySearch(this.qmins, input_min);
            if (beg_index < 0) {
                beg_index = -beg_index - 1;
            }
            else {
                while (beg_index > 0 && this.qmins[beg_index - 1] == this.qmins[beg_index]) {
                    --beg_index;
                }
            }
            while (beg_index > 0 && this.qmins[beg_index - 1] + this.blockSizes[beg_index - 1] > input_min) {
                --beg_index;
            }
            if (beg_index < this.qmins.length && this.qmins[beg_index] < input_max) {
                results = new ArrayList<SeqSymmetry>();
                for (int index = beg_index; index < this.qmins.length && this.qmins[index] < input_max; ++index) {
                    results.add(this.getChild(index));
                }
            }
        }
        return results;
    }
    
    public int getMatches() {
        return this.matches;
    }
    
    public void setMatches(final int count) {
        this.matches = count;
    }
    
    public int getMisMatches() {
        return this.mismatches;
    }
    
    public int getRepMatches() {
        return this.repmatches;
    }
    
    public int getNCount() {
        return this.ncount;
    }
    
    public int getQueryNumInserts() {
        return this.qNumInsert;
    }
    
    public int getQueryBaseInserts() {
        return this.qBaseInsert;
    }
    
    public int getTargetNumInserts() {
        return this.tNumInsert;
    }
    
    public int getTargetBaseInserts() {
        return this.tBaseInsert;
    }
    
    public boolean getSameOrientation() {
        return this.same_orientation;
    }
    
    public BioSeq getQuerySeq() {
        return this.queryseq;
    }
    
    public int getQueryMin() {
        return this.qmin;
    }
    
    public int getQueryMax() {
        return this.qmax;
    }
    
    public BioSeq getTargetSeq() {
        return this.targetseq;
    }
    
    public int getTargetMin() {
        return this.tmin;
    }
    
    public int getTargetMax() {
        return this.tmax;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.cloneProperties();
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final HashMap<String, Object> tprops = new HashMap<String, Object>();
        tprops.put("id", this.getQuerySeq().getID());
        tprops.put("type", "Pairwise Alignment");
        tprops.put("same orientation", this.getSameOrientation());
        tprops.put("# matches", this.getMatches());
        tprops.put("query length", this.queryseq.getLength());
        tprops.put("# query inserts", this.getQueryNumInserts());
        tprops.put("# query bases inserted", this.getQueryBaseInserts());
        tprops.put("# target inserts", this.getTargetNumInserts());
        tprops.put("# target bases inserted", this.getTargetBaseInserts());
        if (this.props != null) {
            tprops.putAll(this.props);
        }
        if (this.target_res_arr != null) {
            tprops.put("residues", getResidue(this.target_res_arr));
        }
        return tprops;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("id")) {
            return this.getQuerySeq().getID();
        }
        if (key.equals("method")) {
            return this.getType();
        }
        if (key.equals("type")) {
            return "Pairwise Alignment";
        }
        if (key.equals("same orientation")) {
            return this.getSameOrientation() ? "true" : "false";
        }
        if (key.equals("# matches")) {
            return Integer.toString(this.getMatches());
        }
        if (key.equals("query length")) {
            return Integer.toString(this.queryseq.getLength());
        }
        if (key.equals("# query inserts")) {
            return Integer.toString(this.getQueryNumInserts());
        }
        if (key.equals("# query bases inserted")) {
            return Integer.toString(this.getQueryBaseInserts());
        }
        if (key.equals("# target inserts")) {
            return Integer.toString(this.getTargetNumInserts());
        }
        if (key.equals("# target bases inserted")) {
            return Integer.toString(this.getTargetBaseInserts());
        }
        if (key.equals("residues") && this.target_res_arr != null) {
            return getResidue(this.target_res_arr);
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
    
    public final void outputPslFormat(final DataOutputStream out) throws IOException {
        this.outputStandardPsl(out, false);
        this.outputPropTagVals(out);
        out.write(10);
    }
    
    protected void outputStandardPsl(final DataOutputStream out, final boolean include_newline) throws IOException {
        out.write(Integer.toString(this.matches).getBytes());
        out.write(9);
        out.write(Integer.toString(this.mismatches).getBytes());
        out.write(9);
        out.write(Integer.toString(this.repmatches).getBytes());
        out.write(9);
        out.write(Integer.toString(this.ncount).getBytes());
        out.write(9);
        out.write(Integer.toString(this.qNumInsert).getBytes());
        out.write(9);
        out.write(Integer.toString(this.qBaseInsert).getBytes());
        out.write(9);
        out.write(Integer.toString(this.tNumInsert).getBytes());
        out.write(9);
        out.write(Integer.toString(this.tBaseInsert).getBytes());
        out.write(9);
        if (this.same_orientation) {
            out.write(43);
        }
        else {
            out.write(45);
        }
        out.write(9);
        out.write(this.queryseq.getID().getBytes());
        out.write(9);
        out.write(Integer.toString(this.queryseq.getLength()).getBytes());
        out.write(9);
        out.write(Integer.toString(this.qmin).getBytes());
        out.write(9);
        out.write(Integer.toString(this.qmax).getBytes());
        out.write(9);
        out.write(this.targetseq.getID().getBytes());
        out.write(9);
        out.write(Integer.toString(this.targetseq.getLength()).getBytes());
        out.write(9);
        out.write(Integer.toString(this.tmin).getBytes());
        out.write(9);
        out.write(Integer.toString(this.tmax).getBytes());
        out.write(9);
        final int blockcount = this.getChildCount();
        out.write(Integer.toString(blockcount).getBytes());
        out.write(9);
        for (int i = 0; i < blockcount; ++i) {
            out.write(Integer.toString(this.blockSizes[i]).getBytes());
            out.write(44);
        }
        out.write(9);
        for (int i = 0; i < blockcount; ++i) {
            if (this.same_orientation) {
                out.write(Integer.toString(this.qmins[i]).getBytes());
            }
            else {
                final int mod_qmin = this.queryseq.getLength() - this.qmins[i] - this.blockSizes[i];
                out.write(Integer.toString(mod_qmin).getBytes());
            }
            out.write(44);
        }
        out.write(9);
        for (int i = 0; i < blockcount; ++i) {
            out.write(Integer.toString(this.tmins[i]).getBytes());
            out.write(44);
        }
        if (include_newline) {
            out.write(10);
        }
    }
    
    protected void outputPropTagVals(final DataOutputStream out) throws IOException {
        if (this.props != null) {
            for (final Map.Entry<String, Object> entry : this.props.entrySet()) {
                out.write(entry.getKey().getBytes());
                out.write(61);
                out.write(entry.getValue().toString().getBytes());
                out.write(9);
            }
        }
    }
    
    public final void outputBpsFormat(final DataOutputStream dos) throws IOException {
        dos.writeInt(this.matches);
        dos.writeInt(this.mismatches);
        dos.writeInt(this.repmatches);
        dos.writeInt(this.ncount);
        dos.writeInt(this.qNumInsert);
        dos.writeInt(this.qBaseInsert);
        dos.writeInt(this.tNumInsert);
        dos.writeInt(this.tBaseInsert);
        dos.writeBoolean(this.same_orientation);
        dos.writeUTF(this.queryseq.getID());
        dos.writeInt(this.queryseq.getLength());
        dos.writeInt(this.qmin);
        dos.writeInt(this.qmax);
        dos.writeUTF(this.targetseq.getID());
        dos.writeInt(this.targetseq.getLength());
        dos.writeInt(this.tmin);
        dos.writeInt(this.tmax);
        final int blockcount = this.getChildCount();
        dos.writeInt(blockcount);
        for (int i = 0; i < blockcount; ++i) {
            dos.writeInt(this.blockSizes[i]);
        }
        for (int i = 0; i < blockcount; ++i) {
            dos.writeInt(this.qmins[i]);
        }
        for (int i = 0; i < blockcount; ++i) {
            dos.writeInt(this.tmins[i]);
        }
    }
}
