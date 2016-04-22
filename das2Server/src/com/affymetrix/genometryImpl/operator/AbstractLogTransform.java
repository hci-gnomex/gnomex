//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.GenometryConstants;
import java.text.DecimalFormat;

public abstract class AbstractLogTransform extends AbstractFloatTransformer implements Operator
{
    protected static final DecimalFormat DF;
    double base;
    protected String paramPrompt;
    protected String name;
    protected boolean parameterized;

    public AbstractLogTransform() {
        this.paramPrompt = "Base";
        this.name = this.getBaseName();
        this.parameterized = true;
    }

    public AbstractLogTransform(final Double base) {
        this.base = base;
        this.paramPrompt = null;
        this.name = this.getBaseName() + "_" + base;
        this.parameterized = false;
    }

    protected abstract String getBaseName();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplay() {
        if (this.base == 2.718281828459045) {
            return GenometryConstants.BUNDLE.getString("operator_" + this.getBaseName() + "_ln");
        }
        return GenometryConstants.BUNDLE.getString("operator_" + this.getBaseName()) + ((this.base == 0.0) ? "" : (" " + AbstractLogTransform.DF.format(this.base)));
    }

    @Override
    public Map<String, Class<?>> getParameters() {
        if (this.paramPrompt == null) {
            return null;
        }
        final Map<String, Class<?>> parameters = new HashMap<String, Class<?>>();
        parameters.put(this.paramPrompt, String.class);
        return parameters;
    }

    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        if (this.paramPrompt != null && parms.size() == 1 && parms.get(this.paramPrompt) instanceof String) {
            this.setParameter((String)parms.get(this.paramPrompt));
            return true;
        }
        return false;
    }

    protected boolean setParameter(final String s) {
        if (this.parameterized) {
            if ("e".equals(s.trim().toLowerCase())) {
                this.base = 2.718281828459045;
            }
            else {
                try {
                    this.base = Double.parseDouble(s);
                    if (this.base <= 0.0) {
                        return false;
                    }
                }
                catch (Exception x) {
                    return false;
                }
            }
        }
        return true;
    }

    static {
        DF = new DecimalFormat("#,##0.##");
    }
}
