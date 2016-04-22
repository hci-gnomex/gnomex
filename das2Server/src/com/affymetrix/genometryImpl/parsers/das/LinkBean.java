// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

class LinkBean
{
    private String url;
    private String title;
    
    LinkBean() {
        this.clear();
    }
    
    void setURL(final String url) {
        this.url = url.intern();
    }
    
    String getURL() {
        return this.url;
    }
    
    void setTitle(final String title) {
        this.title = title.intern();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    void clear() {
        this.url = "";
        this.title = "";
    }
}
