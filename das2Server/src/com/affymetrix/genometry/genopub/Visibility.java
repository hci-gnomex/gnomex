// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class Visibility
{
    public static final String OWNER = "OWNER";
    public static final String MEMBERS = "MEM";
    public static final String MEMBERS_AND_COLLABORATORS = "MEMCOL";
    public static final String INSTITUTE = "INST";
    public static final String PUBLIC = "PUBLIC";
    private String codeVisibility;
    private String name;
    
    public String getCodeVisibility() {
        return this.codeVisibility;
    }
    
    public void setCodeVisibility(final String codeVisibility) {
        this.codeVisibility = codeVisibility;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public static String getDisplay(final String codeVisibility) {
        if (codeVisibility.equals("MEM")) {
            return "Members";
        }
        if (codeVisibility.equals("MEMCOL")) {
            return "Members and Collaborators";
        }
        if (codeVisibility.equals("PUBLIC")) {
            return "Public";
        }
        return "";
    }
}
