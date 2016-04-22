// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.operator;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ComboChainOperator implements Operator
{
    private static final String BASE_NAME = "chain";
    private final List<Operator> operators;
    
    public ComboChainOperator(final Operator... operators_) {
        this.operators = ((operators_ == null) ? new ArrayList<Operator>() : Arrays.asList(operators_));
        this.checkAllCompatible();
    }
    
    private boolean checkAllCompatible() {
        boolean isCompatible = true;
        for (int i = 1; i < this.operators.size(); ++i) {
            isCompatible &= this.checkCompatible(this.operators.get(i - 1), this.operators.get(i - 1));
        }
        return isCompatible;
    }
    
    private boolean checkCompatible(final Operator before, final Operator after) {
        final FileTypeCategory category = before.getOutputCategory();
        boolean isCompatible = true;
        for (final FileTypeCategory checkCategory : FileTypeCategory.values()) {
            final int categoryCount = (checkCategory == category) ? 1 : 0;
            final boolean ok = after.getOperandCountMin(checkCategory) <= categoryCount;
            if (!ok) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "incompatible operands, " + before.getDisplay() + " cannot pass output to " + after.getDisplay());
            }
            isCompatible &= ok;
        }
        return isCompatible;
    }
    
    @Override
    public String getName() {
        final StringBuffer name = new StringBuffer("chain");
        for (final Operator operator : this.operators) {
            name.append("_");
            name.append(operator.getName());
        }
        return name.toString();
    }
    
    @Override
    public String getDisplay() {
        final StringBuffer name = new StringBuffer(GenometryConstants.BUNDLE.getString("operator_chain"));
        for (final Operator operator : this.operators) {
            if ("chain".equals(name.toString())) {
                name.append(" ");
            }
            else {
                name.append(",");
            }
            name.append(GenometryConstants.BUNDLE.getString("operator_" + operator.getName()));
        }
        return name.toString();
    }
    
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (!this.checkAllCompatible()) {
            return null;
        }
        SeqSymmetry resultSym = null;
        for (final Operator operator : this.operators) {
            if (resultSym == null) {
                resultSym = operator.operate(aseq, symList);
            }
            else {
                final List<SeqSymmetry> inputSymList = new ArrayList<SeqSymmetry>();
                inputSymList.add(resultSym);
                resultSym = operator.operate(aseq, inputSymList);
            }
        }
        return resultSym;
    }
    
    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (this.operators.size() == 0) ? 0 : this.operators.get(0).getOperandCountMin(category);
    }
    
    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (this.operators.size() == 0) ? 0 : this.operators.get(0).getOperandCountMax(category);
    }
    
    @Override
    public Map<String, Class<?>> getParameters() {
        final Map<String, Class<?>> parameters = new HashMap<String, Class<?>>();
        for (final Operator operator : this.operators) {
            parameters.putAll(operator.getParameters());
        }
        return parameters;
    }
    
    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        for (final Operator operator : this.operators) {
            operator.setParameters(parms);
        }
        return true;
    }
    
    @Override
    public FileTypeCategory getOutputCategory() {
        return (this.operators.size() == 0) ? null : this.operators.get(this.operators.size() - 1).getOutputCategory();
    }
    
    @Override
    public boolean supportsTwoTrack() {
        boolean support = true;
        for (final Operator operator : this.operators) {
            support &= operator.supportsTwoTrack();
        }
        return support;
    }
}
