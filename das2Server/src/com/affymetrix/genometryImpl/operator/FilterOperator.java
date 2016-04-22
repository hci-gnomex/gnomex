// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.filter.SymmetryFilterI;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;

public class FilterOperator implements Operator
{
    private final FileTypeCategory category;
    private final SymmetryFilterI filter;
    
    public FilterOperator(final FileTypeCategory category, final SymmetryFilterI filter) {
        this.category = category;
        this.filter = filter;
    }
    
    @Override
    public String getName() {
        return "filter_operator_" + this.category.toString() + "_" + this.filter.getName();
    }
    
    @Override
    public String getDisplay() {
        return "filter " + this.category.toString() + " by " + this.filter.getName();
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() != 1) {
            return null;
        }
        final SimpleSymWithProps sym = new SimpleSymWithProps();
        for (int i = 0; i < symList.get(0).getChildCount(); ++i) {
            final SeqSymmetry child = symList.get(0).getChild(i);
            if (this.filter.filterSymmetry(aseq, child)) {
                sym.addChild(child);
            }
        }
        return sym;
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return 0;
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == this.category) ? Integer.MAX_VALUE : 0;
    }
    
    @Override
    public Map<String, Class<?>> getParameters() {
        final Map<String, Class<?>> parameters = new HashMap<String, Class<?>>();
        parameters.put(this.filter.getName(), String.class);
        return parameters;
    }
    
    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        if (parms.size() == 1 && parms.get(this.filter.getName()) instanceof String) {
            this.filter.setParam(parms.get(this.filter.getName()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean supportsTwoTrack() {
        return false;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return this.category;
    }
}
