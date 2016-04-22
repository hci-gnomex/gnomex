// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.filter;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.regex.Matcher;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.regex.Pattern;

public class SymmetryFilterProps implements SymmetryFilterI
{
    private Object param;
    private Pattern regex;
    private SymWithProps swp;
    private String match;
    
    @Override
    public String getName() {
        return "properties";
    }
    
    @Override
    public Object getParam() {
        return this.param;
    }
    
    @Override
    public boolean setParam(final Object param) {
        this.param = param;
        if (param.getClass() != String.class) {
            return false;
        }
        this.regex = this.getRegex((String)param);
        return this.regex != null;
    }
    
    @Override
    public boolean filterSymmetry(final BioSeq seq, final SeqSymmetry sym) {
        boolean passes = false;
        final Matcher matcher = this.regex.matcher("");
        this.match = sym.getID();
        if (this.match != null) {
            matcher.reset(this.match);
            if (matcher.matches()) {
                passes = true;
            }
        }
        if (sym instanceof SymWithProps && !passes) {
            this.swp = (SymWithProps)sym;
            for (final Map.Entry<String, Object> prop : this.swp.getProperties().entrySet()) {
                if (prop.getValue() != null) {
                    matcher.reset(this.match = ArrayUtils.toString(prop.getValue()));
                    if (matcher.matches()) {
                        passes = true;
                        break;
                    }
                    continue;
                }
            }
        }
        return passes;
    }
    
    private Pattern getRegex(String search_text) {
        if (search_text == null) {
            search_text = "";
        }
        String regexText = search_text;
        if (!regexText.contains("*") && !regexText.contains("^") && !regexText.contains("$")) {
            regexText = ".*" + regexText + ".*";
        }
        Pattern regex = null;
        try {
            regex = Pattern.compile(regexText, 2);
        }
        catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error with regular expression " + search_text, e);
            regex = null;
        }
        return regex;
    }
}
