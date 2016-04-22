// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.util.Collection;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AffyDataSet
{
    private long pos_first_data_element;
    private long pos_next_data_element;
    private String name;
    private int param_count;
    private Map<String, AffyChpParameter> params;
    private int num_columns;
    private List<AffyChpColumnType> columnTypes;
    private long num_rows;
    private final Map<Integer, AffySingleChromData> num2chromData;
    private final List<String> chromosomeNames;
    
    protected AffyDataSet(final AffyGenericChpFile chpFile) {
        this.num2chromData = new LinkedHashMap<Integer, AffySingleChromData>();
        this.chromosomeNames = new ArrayList<String>();
    }
    
    public void parse(final AffyGenericChpFile chpFile, final DataInputStream dis) throws IOException {
        this.pos_first_data_element = dis.readInt();
        this.pos_next_data_element = dis.readInt();
        this.name = AffyGenericChpFile.parseWString(dis);
        this.param_count = dis.readInt();
        Logger.getLogger(AffyDataSet.class.getName()).fine("Parsing data set: name=" + this.name);
        this.params = new LinkedHashMap<String, AffyChpParameter>(this.param_count);
        for (int i = 0; i < this.param_count; ++i) {
            final AffyChpParameter param = AffyChpParameter.parse(dis);
            this.params.put(param.name, param);
        }
        this.num_columns = dis.readInt();
        this.columnTypes = new ArrayList<AffyChpColumnType>(this.num_columns);
        for (int i = 0; i < this.num_columns; ++i) {
            final AffyChpColumnType col = new AffyChpColumnType(AffyGenericChpFile.parseWString(dis), dis.readByte(), dis.readInt());
            this.columnTypes.add(col);
        }
        this.num_rows = dis.readInt();
        for (int chromNum = 0; chromNum < 100; ++chromNum) {
            if (this.params.containsKey(chromNum + ":start")) {
                final Integer start = (Integer)this.params.get(chromNum + ":start").getValue();
                final Integer count = (Integer)this.params.get(chromNum + ":count").getValue();
                final String chromName = (String)this.params.get(chromNum + ":display").getValue();
                this.chromosomeNames.add(chromName);
                final List<AffyChpColumnData> chromDataColumns = new ArrayList<AffyChpColumnData>(this.columnTypes.size());
                for (final AffyChpColumnType setColumn : this.columnTypes) {
                    chromDataColumns.add(new AffyChpColumnData(null, setColumn.name, setColumn.type, setColumn.size));
                }
                final AffySingleChromData chromData = new AffySingleChromData(chpFile, this, chromNum, chromName, start, count, chromDataColumns);
                Logger.getLogger(AffyDataSet.class.getName()).fine("Made chrom: " + chromData.toString());
                this.num2chromData.put(chromNum, chromData);
            }
        }
        Logger.getLogger(AffyDataSet.class.getName()).fine("Chromosome Numbers: " + this.num2chromData.keySet());
        for (int chromNum = 0; chromNum < 100; ++chromNum) {
            if (this.num2chromData.containsKey(chromNum)) {
                final AffySingleChromData chromData2 = this.num2chromData.get(chromNum);
                chromData2.parseOrSkip(dis);
            }
        }
    }
    
    @Override
    public String toString() {
        return "AffyDataSet: first_element: " + this.pos_first_data_element + " next_element: " + this.pos_next_data_element + " name: " + this.name + "\n params: " + this.params.size() + "\n num_columns: " + this.num_columns + "\n num_rows: " + this.num_rows;
    }
    
    public void dump(final PrintStream str) {
        str.println(this.getClass().getName());
        str.println("  first_element: " + this.pos_first_data_element);
        str.println("  next_element: " + this.pos_next_data_element);
        str.println("  name: " + this.name);
        str.println("  params: " + this.params.size());
        str.println("  num_columns: " + this.num_columns);
        str.println("  num_rows: " + this.num_rows);
        str.println("  Parameters:  ");
        for (final AffyChpParameter param : this.params.values()) {
            param.dump(str);
        }
        str.println("  Column descriptions:  ");
        for (int i = 0; i < this.num_columns; ++i) {
            final AffyChpColumnType col = this.columnTypes.get(i);
            col.dump(str);
        }
    }
    
    List<String> getChromosomeNames() {
        return new ArrayList<String>(this.chromosomeNames);
    }
    
    List<AffySingleChromData> getSingleChromData() {
        return new ArrayList<AffySingleChromData>(this.num2chromData.values());
    }
}
