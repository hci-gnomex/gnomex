// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.File;

public class UCSCLinkFiles
{
    private File[] filesToLink;
    private boolean converting;
    private boolean stranded;
    
    public UCSCLinkFiles(final File[] filesToLink, final boolean converting, final boolean stranded) {
        this.filesToLink = filesToLink;
        this.converting = converting;
        this.setStranded(stranded);
    }
    
    public File[] getFilesToLink() {
        return this.filesToLink;
    }
    
    public void setFilesToLink(final File[] filesToLink) {
        this.filesToLink = filesToLink;
    }
    
    public boolean isConverting() {
        return this.converting;
    }
    
    public void setConverting(final boolean converting) {
        this.converting = converting;
    }
    
    public void setStranded(final boolean stranded) {
        this.stranded = stranded;
    }
    
    public boolean isStranded() {
        return this.stranded;
    }
}
