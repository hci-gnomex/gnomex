// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import com.affymetrix.genometryImpl.symmetry.UcscGeneSym;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Collection;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SpeciesLookup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import java.util.HashMap;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import com.affymetrix.genometryImpl.general.GenericVersion;
import java.util.Set;

public class AnnotatedSeqGroup
{
    private final String UNKNOWN_ID = "UNKNOWN_SYM_";
    private int unknown_id_no;
    private final String id;
    private String organism;
    private String description;
    private final Set<GenericVersion> gVersions;
    private boolean use_synonyms;
    private final Map<String, BioSeq> id2seq;
    private List<BioSeq> seqlist;
    private boolean id2seq_dirty_bit;
    private final TreeMap<String, Set<SeqSymmetry>> id2sym_hash;
    private final TreeMap<String, Set<String>> symid2id_hash;
    private HashMap<String, Integer> type_id2annot_id;
    private HashMap<String, Set<String>> uri2Seqs;
    private static final SynonymLookup chrLookup;
    private static final SynonymLookup groupLookup;
    
    public AnnotatedSeqGroup(final String gid) {
        this.unknown_id_no = 1;
        this.gVersions = new CopyOnWriteArraySet<GenericVersion>();
        this.type_id2annot_id = new HashMap<String, Integer>();
        this.uri2Seqs = new HashMap<String, Set<String>>();
        this.id = gid;
        this.use_synonyms = true;
        this.id2seq = Collections.synchronizedMap(new LinkedHashMap<String, BioSeq>());
        this.id2seq_dirty_bit = false;
        this.seqlist = new ArrayList<BioSeq>();
        this.id2sym_hash = new TreeMap<String, Set<SeqSymmetry>>();
        this.symid2id_hash = new TreeMap<String, Set<String>>();
    }
    
    public final String getID() {
        return this.id;
    }
    
    public final void setDescription(final String str) {
        this.description = str;
    }
    
    public final String getDescription() {
        return this.description;
    }
    
    public final void setOrganism(final String org) {
        this.organism = org;
    }
    
    public final String getOrganism() {
        if (this.organism != null && !"".equals(this.organism)) {
            return this.organism;
        }
        final String org = SpeciesLookup.getSpeciesName(this.id);
        if (org != null && !"".equals(org)) {
            this.organism = org;
        }
        return this.organism;
    }
    
    public final void addVersion(final GenericVersion gVersion) {
        this.gVersions.add(gVersion);
    }
    
    public final Set<GenericVersion> getEnabledVersions() {
        final Set<GenericVersion> versions = new CopyOnWriteArraySet<GenericVersion>();
        for (final GenericVersion v : this.gVersions) {
            if (v.gServer.isEnabled()) {
                versions.add(v);
            }
        }
        return versions;
    }
    
    public final GenericVersion getVersionOfServer(final GenericServer gServer) {
        for (final GenericVersion v : this.gVersions) {
            if (v.gServer.equals(gServer)) {
                return v;
            }
        }
        return null;
    }
    
    public final Set<GenericVersion> getAllVersions() {
        return Collections.unmodifiableSet((Set<? extends GenericVersion>)this.gVersions);
    }
    
    public final void addType(final String type, final Integer annot_id) {
        this.type_id2annot_id.put(type, annot_id);
    }
    
    public final void removeType(final String type) {
        this.type_id2annot_id.remove(type);
    }
    
    public final Set<String> getTypeList() {
        return this.type_id2annot_id.keySet();
    }
    
    public final Integer getAnnotationId(final String type) {
        return this.type_id2annot_id.get(type);
    }
    
    public List<BioSeq> getSeqList() {
        if (this.id2seq_dirty_bit) {
            this.seqlist = new ArrayList<BioSeq>(this.id2seq.values());
            this.id2seq_dirty_bit = false;
        }
        return Collections.unmodifiableList((List<? extends BioSeq>)this.seqlist);
    }
    
    public BioSeq getSeq(final int index) {
        final List<BioSeq> seq_list = this.getSeqList();
        if (index < seq_list.size()) {
            return seq_list.get(index);
        }
        return null;
    }
    
    public int getSeqCount() {
        return this.id2seq.size();
    }
    
    public final void setUseSynonyms(final boolean b) {
        this.use_synonyms = b;
    }
    
    public BioSeq getSeq(final String synonym) {
        BioSeq aseq = this.id2seq.get(synonym.toLowerCase());
        if (this.use_synonyms && aseq == null) {
            for (final String syn : AnnotatedSeqGroup.chrLookup.getSynonyms(synonym, false)) {
                aseq = this.id2seq.get(syn.toLowerCase());
                if (aseq != null) {
                    return aseq;
                }
            }
        }
        return aseq;
    }
    
    public BioSeq getSeq(final SeqSymmetry sym) {
        for (int spancount = sym.getSpanCount(), i = 0; i < spancount; ++i) {
            final SeqSpan span = sym.getSpan(i);
            final BioSeq seq1 = span.getBioSeq();
            final String seqid = seq1.getID();
            final BioSeq seq2 = this.id2seq.get(seqid.toLowerCase());
            if (seq2 != null && seq1 == seq2) {
                return seq2;
            }
        }
        return null;
    }
    
    public final boolean isSynonymous(final String synonym) {
        return this.id.equals(synonym) || AnnotatedSeqGroup.groupLookup.isSynonym(this.id, synonym);
    }
    
    private boolean findSeqid(final String searchSeqid) {
        if (searchSeqid == null) {
            return false;
        }
        boolean found = false;
        for (final Set<String> seqids : this.uri2Seqs.values()) {
            for (final String seqid : seqids) {
                if (searchSeqid.toLowerCase().equals(seqid.toLowerCase())) {
                    found = true;
                }
            }
        }
        return found;
    }
    
    public boolean removeSeqsForUri(final String uri) {
        final Set<String> seqids = this.uri2Seqs.get(uri);
        this.uri2Seqs.remove(uri);
        boolean removed = false;
        if (seqids != null) {
            for (final String seqid : seqids) {
                if (!this.findSeqid(seqid)) {
                    this.id2seq.remove(seqid.toLowerCase());
                    this.id2seq_dirty_bit = true;
                    removed = true;
                }
            }
        }
        if (removed && this.getSeqCount() == 1) {
            final BioSeq seq = this.getSeq(0);
            if (seq.getID().equals("genome")) {
                this.seqlist.remove(seq);
            }
        }
        return removed;
    }
    
    private void addUri2Seqs(final String uri, final String seqid) {
        Set<String> seqids = this.uri2Seqs.get(uri);
        if (seqids == null) {
            seqids = new HashSet<String>();
            this.uri2Seqs.put(uri, seqids);
        }
        seqids.add(seqid);
    }
    
    public final BioSeq addSeq(final String seqid, final int length) {
        return this.addSeq(seqid, length, "");
    }
    
    public final BioSeq addSeq(final String seqid, final int length, final String uri) {
        this.addUri2Seqs(uri, seqid);
        if (seqid == null) {
            throw new NullPointerException();
        }
        BioSeq aseq = this.getSeq(seqid);
        if (aseq != null) {
            if (aseq.getLength() < length) {
                aseq.setLength(length);
            }
        }
        else {
            aseq = new BioSeq(seqid, this.getID(), length);
            this.addSeq(aseq);
        }
        return aseq;
    }
    
    public final void addSeq(final BioSeq seq) {
        final String seqID = seq.getID();
        final BioSeq oldseq = this.id2seq.get(seqID.toLowerCase());
        if (oldseq == null) {
            synchronized (this) {
                this.id2seq_dirty_bit = true;
                this.id2seq.put(seqID.toLowerCase(), seq);
                seq.setSeqGroup(this);
            }
            return;
        }
        throw new IllegalStateException("ERROR! tried to add seq: " + seqID + " to AnnotatedSeqGroup: " + this.getID() + ", but seq with same id is already in group");
    }
    
    public final Set<SeqSymmetry> findAllSyms() {
        final Set<SeqSymmetry> symset = new HashSet<SeqSymmetry>();
        final Thread current_thread = Thread.currentThread();
        for (final Map.Entry<String, Set<SeqSymmetry>> ent : this.id2sym_hash.entrySet()) {
            if (current_thread.isInterrupted()) {
                break;
            }
            for (final SeqSymmetry seq : ent.getValue()) {
                if (current_thread.isInterrupted()) {
                    break;
                }
                symset.add(seq);
            }
        }
        return symset;
    }
    
    public final Set<SeqSymmetry> findInSymProp(final Pattern regex) {
        final Set<SeqSymmetry> symset = new HashSet<SeqSymmetry>();
        final Matcher matcher = regex.matcher("");
        final Thread current_thread = Thread.currentThread();
        for (final Map.Entry<String, Set<SeqSymmetry>> ent : this.id2sym_hash.entrySet()) {
            if (current_thread.isInterrupted()) {
                break;
            }
            for (final SeqSymmetry seq : ent.getValue()) {
                if (current_thread.isInterrupted()) {
                    break;
                }
                if (!(seq instanceof SymWithProps)) {
                    continue;
                }
                final SymWithProps swp = (SymWithProps)seq;
                for (final Map.Entry<String, Object> prop : swp.getProperties().entrySet()) {
                    if (current_thread.isInterrupted()) {
                        break;
                    }
                    if (prop.getValue() == null) {
                        continue;
                    }
                    final String match = prop.getValue().toString();
                    matcher.reset(match);
                    if (!matcher.matches()) {
                        continue;
                    }
                    symset.add(seq);
                }
            }
        }
        return symset;
    }
    
    public final Set<SeqSymmetry> findSyms(final Pattern regex) {
        final Set<SeqSymmetry> symset = new HashSet<SeqSymmetry>();
        final Matcher matcher = regex.matcher("");
        final Thread current_thread = Thread.currentThread();
        for (final Map.Entry<String, Set<SeqSymmetry>> ent : this.id2sym_hash.entrySet()) {
            if (current_thread.isInterrupted()) {
                break;
            }
            final String seid = ent.getKey();
            final Set<SeqSymmetry> val = ent.getValue();
            if (seid == null || val == null) {
                continue;
            }
            matcher.reset(seid);
            if (!matcher.matches()) {
                continue;
            }
            symset.addAll(val);
        }
        return symset;
    }
    
    public final Set<SeqSymmetry> findSyms(final String id) {
        if (id == null) {
            return Collections.emptySet();
        }
        final Set<SeqSymmetry> sym_list = this.id2sym_hash.get(id.toLowerCase());
        if (sym_list == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet((Set<? extends SeqSymmetry>)sym_list);
    }
    
    public String getUniqueID() {
        return "UNKNOWN_SYM_" + this.unknown_id_no++;
    }
    
    public final void addToIndex(final String id, final SeqSymmetry sym) {
        if (id == null || sym == null) {
            throw new NullPointerException();
        }
        this.putSeqInList(id.toLowerCase(), sym);
    }
    
    public final Set<String> getSymmetryIDs(final String symID) {
        return this.symid2id_hash.get(symID);
    }
    
    public static String getUniqueGraphTrackID(final String id, String trackName) {
        if (trackName == null || trackName.length() == 0) {
            trackName = "_EMPTY";
        }
        return id + "_TRACK_" + trackName;
    }
    
    public static String getUniqueGraphID(final String id, final AnnotatedSeqGroup seq_group) {
        String result = id;
        for (final BioSeq seq : seq_group.getSeqList()) {
            result = getUniqueGraphID(result, seq);
        }
        return result;
    }
    
    public static String getUniqueGraphID(final String id, final BioSeq seq) {
        if (id == null) {
            return null;
        }
        if (seq == null) {
            return id;
        }
        int prevcount;
        String newid;
        for (prevcount = 0, newid = id; seq.getAnnotation(newid) != null; newid = id + "." + prevcount) {
            ++prevcount;
        }
        return newid;
    }
    
    private void putSeqInList(final String id, final SeqSymmetry sym) {
        Set<SeqSymmetry> seq_list = this.id2sym_hash.get(id);
        if (seq_list == null) {
            seq_list = new LinkedHashSet<SeqSymmetry>();
            this.id2sym_hash.put(id, seq_list);
        }
        seq_list.add(sym);
        final String lcSymID = sym.getID().toLowerCase();
        if (id.equals(lcSymID)) {
            return;
        }
        Set<String> id_list = this.symid2id_hash.get(lcSymID);
        if (id_list == null) {
            id_list = new HashSet<String>();
            this.symid2id_hash.put(lcSymID, id_list);
        }
        id_list.add(id);
    }
    
    public final void removeSymmetry(final SeqSymmetry sym) {
        if (sym == null) {
            return;
        }
        for (int i = 0; i < sym.getChildCount(); ++i) {
            this.removeSymmetry(sym.getChild(i));
        }
        if (sym.getID() == null) {
            return;
        }
        String lcSymID = sym.getID().toLowerCase();
        this.removeSymmetry(lcSymID, sym);
        if (sym instanceof UcscGeneSym) {
            lcSymID = ((UcscGeneSym)sym).getGeneName();
            if (lcSymID != null) {
                this.removeSymmetry(lcSymID.toLowerCase(), sym);
            }
        }
    }
    
    private void removeSymmetry(final String lcSymID, final SeqSymmetry sym) {
        final Set<SeqSymmetry> symList = this.id2sym_hash.get(lcSymID);
        if (symList != null && symList.contains(sym)) {
            symList.remove(sym);
            if (symList.isEmpty()) {
                this.id2sym_hash.remove(lcSymID);
            }
        }
        this.symid2id_hash.remove(lcSymID);
    }
    
    public static AnnotatedSeqGroup tempGenome(final AnnotatedSeqGroup oldGenome) {
        final AnnotatedSeqGroup tempGenome = new AnnotatedSeqGroup(oldGenome.getID());
        tempGenome.setOrganism(oldGenome.getOrganism());
        for (final BioSeq seq : oldGenome.getSeqList()) {
            tempGenome.addSeq(seq.getID(), seq.getLength());
        }
        return tempGenome;
    }
    
    static {
        chrLookup = SynonymLookup.getChromosomeLookup();
        groupLookup = SynonymLookup.getDefaultLookup();
    }
}
