// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.parsers.TwoBitParser;
import java.util.HashMap;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.util.SearchableCharIterator;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.util.List;

public class TwoBit extends SymLoader
{
    private static final List<String> pref_list;
    private final Map<BioSeq, SearchableCharIterator> chrMap;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    
    public TwoBit(final URI uri, final AnnotatedSeqGroup group, final String seqName) {
        super(uri, "", group);
        this.chrMap = new HashMap<BioSeq, SearchableCharIterator>();
        this.isResidueLoader = true;
        try {
            final BioSeq retseq = TwoBitParser.parse(uri, group, seqName);
            if (retseq != null) {
                this.chrMap.put(retseq, retseq.getResiduesProvider());
                retseq.removeResidueProvider();
            }
            super.init();
        }
        catch (Exception ex) {
            Logger.getLogger(TwoBit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TwoBit(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, "", group);
        this.chrMap = new HashMap<BioSeq, SearchableCharIterator>();
        this.isResidueLoader = true;
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        final List<BioSeq> seqs = TwoBitParser.parse(this.uri, this.group);
        if (seqs != null) {
            for (final BioSeq seq : seqs) {
                this.chrMap.put(seq, seq.getResiduesProvider());
                seq.removeResidueProvider();
            }
        }
        super.init();
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return TwoBit.strategyList;
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return new ArrayList<BioSeq>(this.chrMap.keySet());
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        this.init();
        final BioSeq seq = span.getBioSeq();
        if (this.chrMap.containsKey(seq)) {
            return this.chrMap.get(seq).substring(span.getMin(), span.getMax());
        }
        Logger.getLogger(TwoBit.class.getName()).log(Level.WARNING, "Seq {0} not present {1}", new Object[] { seq.getID(), this.uri.toString() });
        return "";
    }
    
    @Override
    public List<String> getFormatPrefList() {
        return TwoBit.pref_list;
    }
    
    static {
        (pref_list = new ArrayList<String>()).add("raw");
        TwoBit.pref_list.add("2bit");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        TwoBit.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }
}
