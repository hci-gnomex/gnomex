// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.Scored;

public class SimpleScoredSymWithProps extends SimpleSymWithProps implements Scored
{
    static final String SCORE = "score";
    private final float score;
    
    public SimpleScoredSymWithProps(final float score) {
        this.score = score;
    }
    
    @Override
    public float getScore() {
        return this.score;
    }
    
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        properties.put("score", this.score);
        return properties;
    }
    
    @Override
    public Object getProperty(final String name) {
        if ("score".equalsIgnoreCase(name)) {
            return this.getScore();
        }
        return super.getProperty(name);
    }
}
