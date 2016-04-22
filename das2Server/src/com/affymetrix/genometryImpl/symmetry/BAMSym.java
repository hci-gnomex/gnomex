// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.Iterator;
import net.sf.samtools.CigarOperator;
import net.sf.samtools.CigarElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import com.affymetrix.genometryImpl.BioSeq;
import net.sf.samtools.Cigar;
import com.affymetrix.genometryImpl.util.SearchableCharIterator;

public class BAMSym extends UcscBedSym implements SymWithResidues, SearchableCharIterator
{
    private final int[] iblockMins;
    private final int[] iblockMaxs;
    private final Cigar cigar;
    private final int min;
    private final String residues;
    private String insResidues;
    
    public BAMSym(final String type, final BioSeq seq, final int txMin, final int txMax, final String name, final float score, final boolean forward, final int cdsMin, final int cdsMax, final int[] blockMins, final int[] blockMaxs, final int[] iblockMins, final int[] iblockMaxs, final Cigar cigar, final String residues) {
        super(type, seq, txMin, txMax, name, score, forward, cdsMin, cdsMax, blockMins, blockMaxs);
        this.iblockMins = iblockMins;
        this.iblockMaxs = iblockMaxs;
        this.cigar = cigar;
        this.residues = residues;
        this.min = Math.min(txMin, txMax);
    }
    
    public int getInsChildCount() {
        if (this.iblockMins == null) {
            return 0;
        }
        return this.iblockMins.length;
    }
    
    public SeqSymmetry getInsChild(final int index) {
        if (this.iblockMins == null || this.iblockMins.length <= index) {
            return null;
        }
        if (this.forward) {
            return new BamInsChildSingletonSeqSym(this.iblockMins[index], this.iblockMaxs[index], index, this.seq);
        }
        return new BamInsChildSingletonSeqSym(this.iblockMaxs[index], this.iblockMins[index], index, this.seq);
    }
    
    @Override
    public SeqSymmetry getChild(final int index) {
        if (this.blockMins == null || this.blockMins.length <= index) {
            return null;
        }
        if (this.forward) {
            return new BamChildSingletonSeqSym(this.blockMins[index], this.blockMaxs[index], this.seq);
        }
        return new BamChildSingletonSeqSym(this.blockMaxs[index], this.blockMins[index], this.seq);
    }
    
    @Override
    public String substring(final int start, final int end) {
        return this.getResidues(start, end);
    }
    
    @Override
    public int indexOf(final String searchstring, final int offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getResidues() {
        if (this.residues != null) {
            return this.residues;
        }
        return getEmptyString(this.txMax - this.txMin);
    }
    
    @Override
    public String getResidues(final int start, final int end) {
        if (this.residues != null) {
            return this.interpretCigar(start, end, false);
        }
        return getEmptyString(end - start);
    }
    
    private static String getEmptyString(final int length) {
        final char[] tempArr = new char[length];
        Arrays.fill(tempArr, '-');
        return new String(tempArr);
    }
    
    public void setInsResidues(final String residues) {
        this.insResidues = residues;
    }
    
    public String getInsResidue(final int childNo) {
        if (childNo > this.iblockMins.length) {
            return "";
        }
        int start = 0;
        for (int i = 0; i < childNo; ++i) {
            start += this.iblockMaxs[i] - this.iblockMins[i];
        }
        final int end = start + (this.iblockMaxs[childNo] - this.iblockMins[childNo]);
        return this.insResidues.substring(start, end);
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        if (this.props == null) {
            this.props = new HashMap<String, Object>();
        }
        this.props.put("residues", this.getResidues().replaceAll("-", ""));
        return super.cloneProperties();
    }
    
    @Override
    public Object getProperty(final String key) {
        if ("residues".equalsIgnoreCase(key)) {
            return this.getResidues();
        }
        return super.getProperty(key);
    }
    
    public Cigar getCigar() {
        return this.cigar;
    }
    
    private String interpretCigar(int start, int end, final boolean isIns) {
        if (this.cigar == null || this.cigar.numCigarElements() == 0 || this.residues == null) {
            return "";
        }
        start = Math.max(start, this.txMin);
        end = Math.min(this.txMax, end);
        start -= this.min;
        end -= this.min;
        if (start > end) {
            return "";
        }
        final char[] sb = new char[end - start];
        int stringPtr = 0;
        int currentPos = 0;
        int offset = 0;
        for (final CigarElement cel : this.cigar.getCigarElements()) {
            try {
                if (offset >= sb.length) {
                    return String.valueOf(sb);
                }
                final int celLength = cel.getLength();
                char[] tempArr = new char[celLength];
                if (cel.getOperator() == CigarOperator.INSERTION) {
                    if (isIns && currentPos == start) {
                        return this.residues.substring(stringPtr, stringPtr + celLength);
                    }
                    stringPtr += celLength;
                }
                else if (cel.getOperator() == CigarOperator.SOFT_CLIP) {
                    stringPtr += celLength;
                }
                else {
                    if (cel.getOperator() == CigarOperator.HARD_CLIP) {
                        continue;
                    }
                    if (cel.getOperator() == CigarOperator.DELETION) {
                        Arrays.fill(tempArr, '_');
                        currentPos += celLength;
                    }
                    else if (cel.getOperator() == CigarOperator.M) {
                        tempArr = this.residues.substring(stringPtr, stringPtr + celLength).toCharArray();
                        stringPtr += celLength;
                        currentPos += celLength;
                    }
                    else if (cel.getOperator() == CigarOperator.N) {
                        Arrays.fill(tempArr, '-');
                        currentPos += celLength;
                    }
                    else if (cel.getOperator() == CigarOperator.PADDING) {
                        Arrays.fill(tempArr, '*');
                        stringPtr += celLength;
                        currentPos += celLength;
                    }
                    if (currentPos <= start) {
                        continue;
                    }
                    final int tempOffset = Math.max(tempArr.length - (currentPos - start), 0);
                    final int len = Math.min(tempArr.length - tempOffset, sb.length - offset);
                    System.arraycopy(tempArr, tempOffset, sb, offset, len);
                    offset += len;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                if (end - start - stringPtr <= 0) {
                    continue;
                }
                final char[] tempArr = new char[end - start - stringPtr];
                Arrays.fill(tempArr, '.');
                System.arraycopy(tempArr, 0, sb, 0, tempArr.length);
            }
        }
        return String.valueOf(sb);
    }
    
    class BamChildSingletonSeqSym extends BedChildSingletonSeqSym implements SymWithResidues
    {
        public BamChildSingletonSeqSym(final int start, final int end, final BioSeq seq) {
            super(start, end, seq);
        }
        
        @Override
        public String getResidues() {
            return BAMSym.this.interpretCigar(this.getMin(), this.getMax(), false);
        }
        
        @Override
        public String getResidues(final int start, final int end) {
            return BAMSym.this.interpretCigar(start, end, false);
        }
        
        @Override
        public Map<String, Object> cloneProperties() {
            final HashMap<String, Object> tprops = new HashMap<String, Object>();
            tprops.putAll(super.cloneProperties());
            tprops.put("id", BAMSym.this.name);
            tprops.put("residues", this.getResidues());
            tprops.put("forward", this.isForward());
            return tprops;
        }
    }
    
    class BamInsChildSingletonSeqSym extends BedChildSingletonSeqSym implements SymWithResidues
    {
        final int index;
        
        public BamInsChildSingletonSeqSym(final int start, final int end, final int index, final BioSeq seq) {
            super(start, end, seq);
            this.index = index;
        }
        
        @Override
        public String getResidues(final int start, final int end) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public String getResidues() {
            return BAMSym.this.interpretCigar(this.getMin(), this.getMax(), true);
        }
        
        @Override
        public Map<String, Object> getProperties() {
            return this.cloneProperties();
        }
        
        @Override
        public Map<String, Object> cloneProperties() {
            final HashMap<String, Object> tprops = new HashMap<String, Object>();
            tprops.putAll(super.cloneProperties());
            tprops.put("id", BAMSym.this.name);
            tprops.put("residues", this.getResidues());
            tprops.put("forward", this.isForward());
            return tprops;
        }
    }
}
