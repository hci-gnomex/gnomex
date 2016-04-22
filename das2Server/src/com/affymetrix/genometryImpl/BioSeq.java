// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.Arrays;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.DNAUtils;
import com.affymetrix.genometryImpl.symmetry.TypedSym;
import com.affymetrix.genometryImpl.symmetry.ScoredContainerSym;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.HashMap;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Set;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.util.IndexingUtils;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.Map;
import com.affymetrix.genometryImpl.util.SearchableCharIterator;

public final class BioSeq implements SearchableCharIterator
{
    private Map<String, SymWithProps> type_id2sym;
    private Map<String, IndexingUtils.IndexedSyms> type_id2indexedsym;
    private Map<String, SymLoader> type_id2symloader;
    private AnnotatedSeqGroup seq_group;
    private List<SeqSymmetry> annots;
    private String version;
    private SearchableCharIterator residues_provider;
    private String residues;
    private int start;
    private int end;
    private SeqSymmetry compose;
    private double length;
    private final String id;
    
    public BioSeq(final String seqid, final String seqversion, final int length) {
        this.type_id2sym = null;
        this.type_id2indexedsym = null;
        this.type_id2symloader = null;
        this.length = 0.0;
        this.id = seqid;
        this.length = length;
        this.start = 0;
        this.end = length;
        this.version = seqversion;
    }
    
    public String getID() {
        return this.id;
    }
    
    public AnnotatedSeqGroup getSeqGroup() {
        return this.seq_group;
    }
    
    public void setSeqGroup(final AnnotatedSeqGroup group) {
        this.seq_group = group;
    }
    
    public SeqSymmetry getComposition() {
        return this.compose;
    }
    
    public void setComposition(final SeqSymmetry compose) {
        this.compose = compose;
    }
    
    public Set<String> getTypeList() {
        if (this.type_id2sym == null) {
            return Collections.emptySet();
        }
        return this.type_id2sym.keySet();
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String str) {
        this.version = str;
    }
    
    public double getLengthDouble() {
        return this.length;
    }
    
    @Override
    public int getLength() {
        if (this.length > 2.147483647E9) {
            return 2147483646;
        }
        return (int)this.length;
    }
    
    public void setLength(final int length) {
        this.setBounds(0, length);
        if (this.residues != null && this.residues.length() != length) {
            System.out.println("*** WARNING!!! lengths disagree: residues = " + this.residues.length() + ", seq = " + this.length);
        }
    }
    
    public void setBoundsDouble(final double min, final double max) {
        this.length = max - min;
        if (min < -2.147483648E9) {
            this.start = -2147483647;
        }
        else {
            this.start = (int)min;
        }
        if (max > 2.147483647E9) {
            this.end = 2147483646;
        }
        else {
            this.end = (int)max;
        }
    }
    
    public void setBounds(final int min, final int max) {
        this.start = min;
        this.end = max;
        this.length = this.end - this.start;
    }
    
    public int getMin() {
        return this.start;
    }
    
    public int getMax() {
        return this.end;
    }
    
    public int getAnnotationCount() {
        if (null != this.annots) {
            return this.annots.size();
        }
        return 0;
    }
    
    public SeqSymmetry getAnnotation(final int index) {
        if (null != this.annots && index < this.annots.size()) {
            return this.annots.get(index);
        }
        return null;
    }
    
    public SymWithProps getAnnotation(final String type) {
        if (this.type_id2sym == null) {
            return null;
        }
        return this.type_id2sym.get(type);
    }
    
    public List<SymWithProps> getAnnotations(final Pattern regex) {
        final List<SymWithProps> results = new ArrayList<SymWithProps>();
        if (this.type_id2sym != null) {
            final Matcher match = regex.matcher("");
            for (final Map.Entry<String, SymWithProps> entry : this.type_id2sym.entrySet()) {
                final String type = entry.getKey();
                if (match.reset(type).matches()) {
                    results.add(entry.getValue());
                }
            }
        }
        return results;
    }
    
    public synchronized void addAnnotation(final SeqSymmetry sym) {
        this.addAnnotation(sym, "");
    }
    
    public synchronized void addAnnotation(final SeqSymmetry sym, final String ext) {
        if (!needsContainer(sym)) {
            if (!(sym instanceof SymWithProps)) {
                throw new RuntimeException("sym must be a SymWithProps");
            }
            final String symID = sym.getID();
            if (symID == null) {
                throw new RuntimeException("sym.getID() == null && (! needsContainer(sym)), this should never happen!");
            }
            if (this.type_id2sym == null) {
                this.type_id2sym = new LinkedHashMap<String, SymWithProps>();
            }
            else if (this.type_id2sym.containsKey(symID) && sym.equals(this.type_id2sym.get(this.id))) {
                return;
            }
            this.type_id2sym.put(symID, (SymWithProps)sym);
            if (this.annots == null) {
                this.annots = new ArrayList<SeqSymmetry>();
            }
            this.annots.add(sym);
        }
        else {
            final String type = determineMethod(sym);
            if (type != null) {
                this.addAnnotation(sym, type, ext);
                return;
            }
            throw new RuntimeException("BioSeq.addAnnotation(sym) will only accept  SeqSymmetries that are also SymWithProps and  have a _method_ property");
        }
    }
    
    private synchronized void addAnnotation(final SeqSymmetry sym, final String type, final String ext) {
        if (this.type_id2sym == null) {
            this.type_id2sym = new LinkedHashMap<String, SymWithProps>();
        }
        MutableSeqSymmetry container = (MutableSeqSymmetry)this.type_id2sym.get(type);
        if (container == null) {
            container = new TypeContainerAnnot(type, ext);
            ((TypeContainerAnnot)container).setProperty("method", type);
            final SeqSpan span = new SimpleSeqSpan(0, this.getLength(), this);
            container.addSpan(span);
            this.type_id2sym.put(type, (TypeContainerAnnot)container);
            if (this.annots == null) {
                this.annots = new ArrayList<SeqSymmetry>();
            }
            this.annots.add(container);
        }
        container.addChild(sym);
    }
    
    public synchronized void unloadAnnotation(final SeqSymmetry annot) {
        this.removeAnnotation(annot, true);
    }
    
    public synchronized void removeAnnotation(final SeqSymmetry annot) {
        this.removeAnnotation(annot, false);
    }
    
    private synchronized void removeAnnotation(final SeqSymmetry annot, final boolean clearContainer) {
        if (annot != null) {
            this.getSeqGroup().removeSymmetry(annot);
        }
        if (null != this.annots) {
            this.annots.remove(annot);
        }
        final String type = determineMethod(annot);
        if (type == null) {
            return;
        }
        final SymWithProps sym = this.getAnnotation(type);
        if (sym != null && sym instanceof MutableSeqSymmetry) {
            final MutableSeqSymmetry container = (MutableSeqSymmetry)sym;
            if (container == annot) {
                this.type_id2sym.remove(type);
            }
            else {
                container.removeChild(annot);
            }
            if (clearContainer) {
                container.clear();
            }
        }
    }
    
    public final void addIndexedSyms(final String type, final IndexingUtils.IndexedSyms value) {
        if (this.type_id2indexedsym == null) {
            this.type_id2indexedsym = new HashMap<String, IndexingUtils.IndexedSyms>();
        }
        this.type_id2indexedsym.put(type, value);
    }
    
    public final Set<String> getIndexedTypeList() {
        if (this.type_id2indexedsym == null) {
            return Collections.emptySet();
        }
        return this.type_id2indexedsym.keySet();
    }
    
    public final IndexingUtils.IndexedSyms getIndexedSym(final String type) {
        if (this.type_id2indexedsym == null) {
            return null;
        }
        return this.type_id2indexedsym.get(type);
    }
    
    public boolean removeIndexedSym(final String type) {
        if (this.type_id2indexedsym == null || !this.type_id2indexedsym.containsKey(type)) {
            return false;
        }
        this.type_id2indexedsym.remove(type);
        return true;
    }
    
    public final void addSymLoader(final String type, final SymLoader value) {
        if (this.type_id2symloader == null) {
            this.type_id2symloader = new HashMap<String, SymLoader>();
        }
        this.type_id2symloader.put(type, value);
    }
    
    public final Set<String> getSymloaderList() {
        if (this.type_id2symloader == null) {
            return Collections.emptySet();
        }
        return this.type_id2symloader.keySet();
    }
    
    public final SymLoader getSymLoader(final String type) {
        if (this.type_id2symloader == null) {
            return null;
        }
        return this.type_id2symloader.get(type);
    }
    
    public boolean removeSymLoader(final String type) {
        if (this.type_id2symloader == null || !this.type_id2symloader.containsKey(type)) {
            return false;
        }
        this.type_id2symloader.remove(type);
        return true;
    }
    
    public static boolean needsContainer(final SeqSymmetry sym) {
        return !(sym instanceof GraphSym) && !(sym instanceof ScoredContainerSym) && !(sym instanceof TypeContainerAnnot);
    }
    
    public static String determineMethod(final SeqSymmetry sym) {
        String meth = null;
        if (sym instanceof SymWithProps) {
            final SymWithProps psym = (SymWithProps)sym;
            meth = (String)psym.getProperty("method");
            if (meth == null) {
                meth = (String)psym.getProperty("meth");
            }
            if (meth == null) {
                meth = (String)psym.getProperty("type");
            }
        }
        if (meth == null && sym instanceof TypedSym) {
            meth = ((TypedSym)sym).getType();
        }
        return meth;
    }
    
    public SearchableCharIterator getResiduesProvider() {
        return this.residues_provider;
    }
    
    public <S extends SearchableCharIterator> void setResiduesProvider(final S chariter) {
        if (chariter.getLength() != this.getLength()) {
            System.out.println("WARNING -- in setResidueProvider, lengths don't match");
        }
        this.residues_provider = chariter;
    }
    
    public void removeResidueProvider() {
        this.residues_provider = null;
    }
    
    public String getResidues() {
        return this.getResidues(this.start, this.end);
    }
    
    public String getResidues(final int start, final int end) {
        return this.getResidues(start, end, ' ');
    }
    
    private String getResidues(final int start, final int end, final char fillchar) {
        if (this.residues_provider == null) {
            return this.getResiduesNoProvider(start, end, '-');
        }
        if (start <= end) {
            return this.residues_provider.substring(start, end);
        }
        return DNAUtils.reverseComplement(this.residues_provider.substring(end, start));
    }
    
    private String getResiduesNoProvider(int start, int end, final char fillchar) {
        if (this.getLengthDouble() > 2.147483647E9) {
            Logger.getLogger(BioSeq.class.getName()).fine("Length exceeds integer size");
            return "";
        }
        final int residue_length = this.getLength();
        if (start < 0 || residue_length <= 0) {
            Logger.getLogger(BioSeq.class.getName()).fine("Invalid arguments: " + start + "," + end + "," + residue_length);
            return "";
        }
        start = Math.min(start, residue_length);
        end = Math.min(end, residue_length);
        if (start <= end) {
            end = Math.min(end, start + residue_length);
        }
        else {
            start = Math.min(start, end + residue_length);
        }
        if (this.residues == null) {
            return this.getResiduesFromComposition(start, end, fillchar);
        }
        if (start <= end) {
            return this.residues.substring(start, end);
        }
        return DNAUtils.reverseComplement(this.residues.substring(end, start));
    }
    
    private String getResiduesFromComposition(final int res_start, final int res_end, final char fillchar) {
        final SeqSpan residue_span = new SimpleSeqSpan(res_start, res_end, this);
        final int reslength = Math.abs(res_end - res_start);
        final char[] char_array = new char[reslength];
        Arrays.fill(char_array, fillchar);
        final SeqSymmetry rootsym = this.getComposition();
        if (rootsym != null) {
            this.getResiduesFromComposition(residue_span, rootsym, char_array);
        }
        return new String(char_array);
    }
    
    private void getResiduesFromComposition(final SeqSpan this_residue_span, final SeqSymmetry sym, final char[] residues) {
        final int symCount = sym.getChildCount();
        if (symCount == 0) {
            final SeqSpan this_comp_span = sym.getSpan(this);
            if (this_comp_span == null || !SeqUtils.overlap(this_comp_span, this_residue_span)) {
                return;
            }
            final BioSeq other_seq = SeqUtils.getOtherSeq(sym, this);
            final SeqSpan other_comp_span = sym.getSpan(other_seq);
            final MutableSeqSpan ispan = new SimpleMutableSeqSpan();
            SeqUtils.intersection(this_comp_span, this_residue_span, ispan);
            final MutableSeqSpan other_residue_span = new SimpleMutableSeqSpan();
            SeqUtils.transformSpan(ispan, other_residue_span, other_seq, sym);
            final boolean opposite_strands = this_comp_span.isForward() ^ other_comp_span.isForward();
            final boolean resultForward = opposite_strands ^ this_residue_span.isForward();
            String spanResidues;
            if (resultForward) {
                spanResidues = other_seq.getResidues(other_residue_span.getMin(), other_residue_span.getMax());
            }
            else {
                spanResidues = other_seq.getResidues(other_residue_span.getMax(), other_residue_span.getMin());
            }
            if (spanResidues != null) {
                final int offset = ispan.getMin() - this_residue_span.getMin();
                System.arraycopy(spanResidues.toCharArray(), 0, residues, offset, spanResidues.length());
            }
        }
        else {
            for (int i = 0; i < symCount; ++i) {
                final SeqSymmetry childSym = sym.getChild(i);
                this.getResiduesFromComposition(this_residue_span, childSym, residues);
            }
        }
    }
    
    public void setResidues(final String residues) {
        if (residues.length() != this.length) {
            System.out.println("*** WARNING!!! lengths disagree: residues = " + residues.length() + ", seq = " + this.length + " ****");
        }
        this.residues = residues;
        this.length = residues.length();
    }
    
    public boolean isComplete() {
        return this.isComplete(this.start, this.end);
    }
    
    public boolean isComplete(final int start, final int end) {
        if (this.residues_provider != null || this.residues != null) {
            return true;
        }
        final SeqSymmetry rootsym = this.getComposition();
        if (rootsym == null) {
            return false;
        }
        final int comp_count = rootsym.getChildCount();
        if (comp_count == 0) {
            final BioSeq other_seq = SeqUtils.getOtherSeq(rootsym, this);
            return other_seq.isComplete(start, end);
        }
        for (int i = 0; i < comp_count; ++i) {
            final SeqSymmetry comp_sym = rootsym.getChild(i);
            final BioSeq other_seq2 = SeqUtils.getOtherSeq(comp_sym, this);
            if (!other_seq2.isComplete()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isAvailable() {
        return this.isAvailable(this.start, this.end);
    }
    
    public boolean isAvailable(final int start, final int end) {
        return this.isAvailable(new SimpleSeqSpan(start, end, this));
    }
    
    public boolean isAvailable(final SeqSpan span) {
        final SeqSymmetry rootsym = this.getComposition();
        if (rootsym == null || span == null) {
            return false;
        }
        final MutableSeqSymmetry query_sym = new SimpleMutableSeqSymmetry();
        query_sym.addSpan(span);
        final SeqSymmetry optimized_sym = SeqUtils.exclusive(query_sym, rootsym, this);
        return !SeqUtils.hasSpan(optimized_sym);
    }
    
    @Override
    public String substring(final int start, final int end) {
        if (this.residues_provider == null) {
            return this.getResidues(start, end);
        }
        return this.residues_provider.substring(start, end);
    }
    
    @Override
    public int indexOf(final String str, final int fromIndex) {
        if (this.residues_provider == null) {
            return this.getResidues().indexOf(str, fromIndex);
        }
        return this.residues_provider.indexOf(str, fromIndex);
    }
    
    @Override
    public String toString() {
        return this.getID();
    }
    
    public static void addResiduesToComposition(final BioSeq aseq) {
        if (aseq.getResiduesProvider() != null) {
            final SeqSpan span = new SimpleSeqSpan(0, aseq.getResiduesProvider().getLength(), aseq);
            final BioSeq subseq = new BioSeq(aseq.getID() + ":" + span.getMin() + "-" + span.getMax(), aseq.getVersion(), aseq.getResiduesProvider().getLength());
            subseq.setResiduesProvider(aseq.getResiduesProvider());
            addSubseqToComposition(aseq, span, subseq);
            return;
        }
        final String residues = aseq.getResidues();
        final SeqSpan span2 = new SimpleSeqSpan(0, residues.length(), aseq);
        addResiduesToComposition(aseq, residues, span2);
    }
    
    public static void addResiduesToComposition(final BioSeq aseq, final String residues, final SeqSpan span) {
        final BioSeq subseq = new BioSeq(aseq.getID() + ":" + span.getMin() + "-" + span.getMax(), aseq.getVersion(), residues.length());
        subseq.setResidues(residues);
        addSubseqToComposition(aseq, span, subseq);
    }
    
    private static void addSubseqToComposition(final BioSeq aseq, final SeqSpan span, final BioSeq subSeq) {
        final SeqSpan subSpan = new SimpleSeqSpan(0, span.getLength(), subSeq);
        final MutableSeqSymmetry subsym = new SimpleMutableSeqSymmetry();
        subsym.addSpan(subSpan);
        subsym.addSpan(span);
        MutableSeqSymmetry compsym = (MutableSeqSymmetry)aseq.getComposition();
        if (compsym == null) {
            compsym = new SimpleMutableSeqSymmetry();
            compsym.addChild(subsym);
            compsym.addSpan(new SimpleSeqSpan(span.getMin(), span.getMax(), aseq));
            aseq.setComposition(compsym);
        }
        else {
            compsym.addChild(subsym);
            final SeqSpan compspan = compsym.getSpan(aseq);
            final int compmin = Math.min(compspan.getMin(), span.getMin());
            final int compmax = Math.max(compspan.getMax(), span.getMax());
            final SeqSpan new_compspan = new SimpleSeqSpan(compmin, compmax, aseq);
            compsym.removeSpan(compspan);
            compsym.addSpan(new_compspan);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BioSeq other = (BioSeq)obj;
        if (this.end != other.end) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return this.start == other.start;
    }
}
