// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.BioSeq;

public class SimpleSymWithResidues extends UcscBedSym implements SymWithResidues
{
    private final String residues;
    
    public SimpleSymWithResidues(final String type, final BioSeq seq, final int txMin, final int txMax, final String name, final float score, final boolean forward, final int cdsMin, final int cdsMax, final int[] blockMins, final int[] blockMaxs, final String residues) {
        super(type, seq, txMin, txMax, name, score, forward, cdsMin, cdsMax, blockMins, blockMaxs);
        this.residues = residues;
    }
    
    @Override
    public String getResidues() {
        return this.residues;
    }
    
    @Override
    public String getResidues(final int start, final int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
