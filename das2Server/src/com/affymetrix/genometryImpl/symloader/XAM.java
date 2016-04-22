// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.Arrays;
import net.sf.samtools.SAMSequenceDictionary;
import net.sf.samtools.SAMProgramRecord;
import net.sf.samtools.SAMReadGroupRecord;
import net.sf.samtools.CigarOperator;
import net.sf.samtools.CigarElement;
import net.sf.samtools.Cigar;
import com.affymetrix.genometryImpl.symmetry.BAMSym;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import net.sf.samtools.SAMRecord;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;
import java.util.Iterator;
import net.sf.samtools.SAMSequenceRecord;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;

public abstract class XAM extends SymLoader
{
    protected final List<LoadUtils.LoadStrategy> strategyList;
    protected static final boolean DEBUG = false;
    protected boolean skipUnmapped;
    protected SAMFileReader reader;
    protected SAMFileHeader header;
    protected final Map<BioSeq, String> seqs;
    public static final String CIGARPROP = "cigar";
    public static final String RESIDUESPROP = "residues";
    public static final String BASEQUALITYPROP = "baseQuality";
    public static final String SHOWMASK = "showMask";
    public static final String INSRESIDUESPROP = "insResidues";
    
    public XAM(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
        this.strategyList = new ArrayList<LoadUtils.LoadStrategy>();
        this.skipUnmapped = true;
        this.seqs = new HashMap<BioSeq, String>();
        this.strategyList.add(LoadUtils.LoadStrategy.NO_LOAD);
        this.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return this.strategyList;
    }
    
    protected boolean initTheSeqs() {
        this.header = this.reader.getFileHeader();
        if (this.header == null || this.header.getSequenceDictionary() == null || this.header.getSequenceDictionary().getSequences() == null || this.header.getSequenceDictionary().getSequences().isEmpty()) {
            Logger.getLogger(BAM.class.getName()).log(Level.WARNING, "Couldn't find sequences in file");
            return false;
        }
        final Thread thread = Thread.currentThread();
        for (final SAMSequenceRecord ssr : this.header.getSequenceDictionary().getSequences()) {
            try {
                if (thread.isInterrupted()) {
                    break;
                }
                final String seqID = ssr.getSequenceName();
                final int seqLength = ssr.getSequenceLength();
                final BioSeq seq = this.group.addSeq(seqID, seqLength, this.uri.toString());
                if (seq.getVersion() != null) {
                    seq.setVersion(this.group.getID());
                }
                this.seqs.put(seq, seqID);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return !thread.isInterrupted();
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return new ArrayList<BioSeq>(this.seqs.keySet());
    }
    
    @Override
    public List<SeqSymmetry> getGenome() throws Exception {
        this.init();
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        for (final BioSeq seq : this.group.getSeqList()) {
            results.addAll(this.getChromosome(seq));
        }
        return results;
    }
    
    @Override
    public List<SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.parse(seq, seq.getMin(), seq.getMax(), true, false);
    }
    
    @Override
    public List<SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        this.init();
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax(), true, false);
    }
    
    public abstract List<SeqSymmetry> parse(final BioSeq p0, final int p1, final int p2, final boolean p3, final boolean p4) throws Exception;
    
    protected static SymWithProps convertSAMRecordToSymWithProps(final SAMRecord sr, final BioSeq seq, final String meth) {
        final SymWithProps sym = convertSAMRecordToSymWithProps(sr, seq, meth, true, false);
        addAllSAMRecordProperties(sym, sr);
        return sym;
    }
    
    protected static SymWithProps convertSAMRecordToSymWithProps(final SAMRecord sr, final BioSeq seq, final String meth, final boolean includeResidues, final boolean includeNH) {
        final int start = sr.getAlignmentStart() - 1;
        final int end = sr.getAlignmentEnd();
        SimpleSeqSpan span;
        if (!sr.getReadNegativeStrandFlag()) {
            span = new SimpleSeqSpan(start, end, seq);
        }
        else {
            span = new SimpleSeqSpan(end, start, seq);
        }
        final List<SimpleSymWithProps> insertChilds = new ArrayList<SimpleSymWithProps>();
        final List<SimpleSymWithProps> childs = getChildren(seq, sr.getCigar(), sr.getReadNegativeStrandFlag(), insertChilds);
        int[] blockMins = new int[childs.size()];
        int[] blockMaxs = new int[childs.size()];
        for (int i = 0; i < childs.size(); ++i) {
            final SymWithProps child = childs.get(i);
            blockMins[i] = child.getSpan(0).getMin() + span.getMin();
            blockMaxs[i] = blockMins[i] + child.getSpan(0).getLength();
        }
        final int[] iblockMins = new int[insertChilds.size()];
        final int[] iblockMaxs = new int[insertChilds.size()];
        for (int j = 0; j < insertChilds.size(); ++j) {
            final SymWithProps child2 = insertChilds.get(j);
            iblockMins[j] = child2.getSpan(0).getMin() + span.getMin();
            iblockMaxs[j] = iblockMins[j] + child2.getSpan(0).getLength();
        }
        if (childs.isEmpty()) {
            blockMins = new int[] { span.getStart() };
            blockMaxs = new int[] { span.getEnd() };
        }
        final SymWithProps sym = new BAMSym(meth, seq, start, end, sr.getReadName(), 0.0f, span.isForward(), 0, 0, blockMins, blockMaxs, iblockMins, iblockMaxs, sr.getCigar(), includeResidues ? sr.getReadString() : null);
        sym.setProperty("id", sr.getReadName());
        sym.setProperty("method", meth);
        if (includeNH && sr.getAttribute("NH") != null) {
            sym.setProperty("NH", sr.getAttribute("NH"));
        }
        return sym;
    }
    
    private static void addAllSAMRecordProperties(final SymWithProps sym, final SAMRecord sr) {
        sym.setProperty("baseQuality", sr.getBaseQualityString());
        for (final SAMRecord.SAMTagAndValue tv : sr.getAttributes()) {
            sym.setProperty(tv.tag, tv.value);
        }
        sym.setProperty("cigar", sr.getCigar());
        sym.setProperty("showMask", true);
        getFileHeaderProperties(sr.getHeader(), sym);
    }
    
    private static List<SimpleSymWithProps> getChildren(final BioSeq seq, final Cigar cigar, final boolean isNegative, final List<SimpleSymWithProps> insertChilds) {
        final List<SimpleSymWithProps> results = new ArrayList<SimpleSymWithProps>();
        if (cigar == null || cigar.numCigarElements() == 0) {
            return results;
        }
        int currentChildStart = 0;
        int currentChildEnd = 0;
        int celLength = 0;
        for (final CigarElement cel : cigar.getCigarElements()) {
            try {
                celLength = cel.getLength();
                if (cel.getOperator() == CigarOperator.DELETION) {
                    currentChildStart = currentChildEnd;
                    currentChildEnd = currentChildStart + celLength;
                }
                else if (cel.getOperator() == CigarOperator.INSERTION) {
                    final SimpleSymWithProps ss = new SimpleSymWithProps();
                    if (!isNegative) {
                        ss.addSpan(new SimpleSeqSpan(currentChildEnd, currentChildEnd + celLength, seq));
                    }
                    else {
                        ss.addSpan(new SimpleSeqSpan(currentChildEnd + celLength, currentChildEnd, seq));
                    }
                    insertChilds.add(ss);
                }
                else if (cel.getOperator() == CigarOperator.M) {
                    currentChildEnd += celLength;
                    final SimpleSymWithProps ss = new SimpleSymWithProps();
                    if (!isNegative) {
                        ss.addSpan(new SimpleSeqSpan(currentChildStart, currentChildEnd, seq));
                    }
                    else {
                        ss.addSpan(new SimpleSeqSpan(currentChildEnd, currentChildStart, seq));
                    }
                    results.add(ss);
                }
                else if (cel.getOperator() == CigarOperator.N) {
                    currentChildStart = (currentChildEnd += celLength);
                }
                else if (cel.getOperator() == CigarOperator.PADDING) {
                    currentChildEnd += celLength;
                }
                else {
                    if (cel.getOperator() == CigarOperator.SOFT_CLIP) {
                        continue;
                    }
                    if (cel.getOperator() == CigarOperator.HARD_CLIP) {}
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return results;
    }
    
    private static void getFileHeaderProperties(final SAMFileHeader hr, final SymWithProps sym) {
        if (hr == null) {
            return;
        }
        final SAMSequenceDictionary ssd = hr.getSequenceDictionary();
        for (final SAMSequenceRecord ssr : ssd.getSequences()) {
            if (ssr.getAssembly() != null) {
                sym.setProperty("genomeAssembly", ssr.getAssembly());
            }
            if (ssr.getSpecies() != null) {
                sym.setProperty("species  ", ssr.getSpecies());
            }
        }
        for (final SAMReadGroupRecord srgr : hr.getReadGroups()) {
            for (final Map.Entry<String, String> en : srgr.getAttributes()) {
                sym.setProperty(en.getKey(), en.getValue());
            }
        }
        for (final SAMProgramRecord spr : hr.getProgramRecords()) {
            for (final Map.Entry<String, String> en : spr.getAttributes()) {
                sym.setProperty(en.getKey(), en.getValue());
            }
        }
    }
    
    private static String interpretCigar(final Cigar cigar, final String residues, final int spanLength, final StringBuffer insResidues) {
        if (cigar == null || cigar.numCigarElements() == 0) {
            return residues;
        }
        final char[] sb = new char[spanLength];
        int currentPos = 0;
        int currentEnd = 0;
        for (final CigarElement cel : cigar.getCigarElements()) {
            try {
                final int celLength = cel.getLength();
                if (cel.getOperator() == CigarOperator.DELETION) {
                    final char[] tempArr = new char[celLength];
                    Arrays.fill(tempArr, '_');
                    System.arraycopy(tempArr, 0, sb, currentEnd, tempArr.length);
                }
                else {
                    if (cel.getOperator() == CigarOperator.INSERTION) {
                        insResidues.append(residues.substring(currentPos, currentPos + celLength));
                        currentPos += celLength;
                        continue;
                    }
                    if (cel.getOperator() == CigarOperator.M) {
                        final char[] tempArr = residues.substring(currentPos, currentPos + celLength).toCharArray();
                        System.arraycopy(tempArr, 0, sb, currentEnd, tempArr.length);
                        currentPos += celLength;
                    }
                    else if (cel.getOperator() == CigarOperator.N) {
                        final char[] tempArr = new char[celLength];
                        Arrays.fill(tempArr, '-');
                        System.arraycopy(tempArr, 0, sb, currentEnd, tempArr.length);
                    }
                    else if (cel.getOperator() == CigarOperator.PADDING) {
                        final char[] tempArr = new char[celLength];
                        Arrays.fill(tempArr, '*');
                        System.arraycopy(tempArr, 0, sb, currentEnd, tempArr.length);
                        currentPos += celLength;
                    }
                    else {
                        if (cel.getOperator() == CigarOperator.SOFT_CLIP) {
                            currentPos += celLength;
                            continue;
                        }
                        if (cel.getOperator() == CigarOperator.HARD_CLIP) {
                            continue;
                        }
                    }
                }
                currentEnd += celLength;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                if (spanLength - currentPos <= 0) {
                    continue;
                }
                final char[] tempArr = new char[spanLength - currentPos];
                Arrays.fill(tempArr, '.');
                System.arraycopy(tempArr, 0, sb, 0, tempArr.length);
            }
        }
        return String.valueOf(sb);
    }
}
