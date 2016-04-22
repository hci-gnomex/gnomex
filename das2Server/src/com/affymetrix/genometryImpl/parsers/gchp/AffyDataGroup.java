// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

public final class AffyDataGroup
{
    long file_pos;
    long file_first_dataset_pos;
    int num_datasets;
    String name;
    List<AffyDataSet> dataSets;
    AffyGenericChpFile chpFile;
    
    protected AffyDataGroup(final AffyGenericChpFile chpFile) {
        this.dataSets = new ArrayList<AffyDataSet>();
        this.chpFile = chpFile;
    }
    
    public AffyDataGroup(final int pos, final int data_pos, final int sets, final String name) {
        this.dataSets = new ArrayList<AffyDataSet>();
        this.file_first_dataset_pos = data_pos;
        this.file_pos = pos;
        this.num_datasets = sets;
        this.name = name;
    }
    
    public static AffyDataGroup parse(final AffyGenericChpFile chpFile, final DataInputStream dis) throws IOException {
        final AffyDataGroup group = new AffyDataGroup(chpFile);
        group.file_pos = dis.readInt();
        group.file_first_dataset_pos = dis.readInt();
        group.num_datasets = dis.readInt();
        group.name = AffyGenericChpFile.parseWString(dis);
        Logger.getLogger(AffyDataGroup.class.getName()).log(Level.FINE, "Parsing group: pos={0}, name={1}, datasets={2}", new Object[] { group.file_pos, group.name, group.num_datasets });
        if (group.num_datasets > 1) {}
        for (int i = 0; i < 1 && i < group.num_datasets; ++i) {
            final AffyDataSet data = new AffyDataSet(chpFile);
            data.parse(chpFile, dis);
            group.dataSets.add(data);
        }
        return group;
    }
    
    public List<AffyDataSet> getDataSets() {
        return this.dataSets;
    }
    
    @Override
    public String toString() {
        return "AffyDataGroup: pos: " + this.file_pos + ", first_dataset_pos: " + this.file_first_dataset_pos + ", datasets: " + this.num_datasets + ", name: " + this.name;
    }
}
