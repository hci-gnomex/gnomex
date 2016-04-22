// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Map;
import com.affymetrix.genometryImpl.BioSeq;

public class UcscBedDetailSym extends UcscBedSym
{
    private final String geneName;
    private String description;
    
    public UcscBedDetailSym(final String type, final BioSeq seq, final int txMin, final int txMax, final String name, final float score, final boolean forward, final int cdsMin, final int cdsMax, final int[] blockMins, final int[] blockMaxs, final String geneName, final String description) {
        super(type, seq, txMin, txMax, name, score, forward, cdsMin, cdsMax, blockMins, blockMaxs);
        this.geneName = geneName;
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getGeneName() {
        return this.geneName;
    }
    
    @Override
    public Map<String, Object> cloneProperties() {
        final Map<String, Object> tprops = super.cloneProperties();
        tprops.put("gene name", this.geneName);
        tprops.put("description", this.description);
        return tprops;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("gene name")) {
            return this.geneName;
        }
        if (key.equals("description")) {
            return this.description;
        }
        return super.getProperty(key);
    }
    
    @Override
    protected void outputAdditional(final DataOutputStream out) throws IOException {
        out.write(9);
        out.write(this.geneName.getBytes());
        out.write(9);
        out.write(this.description.getBytes());
    }
}
