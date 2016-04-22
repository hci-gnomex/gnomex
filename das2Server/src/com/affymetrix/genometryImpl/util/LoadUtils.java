// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public final class LoadUtils
{
    public static String stripFilenameExtensions(final String name) {
        if (name.endsWith(".gz")) {
            return stripFilenameExtensions(name.substring(0, name.length() - 3));
        }
        if (name.endsWith(".zip")) {
            return stripFilenameExtensions(name.substring(0, name.length() - 4));
        }
        if (name.indexOf(46) > 0) {
            return name.substring(0, name.lastIndexOf(46));
        }
        return name;
    }
    
    public enum LoadStrategy
    {
        NO_LOAD("Don't Load"), 
        AUTOLOAD("Auto"), 
        VISIBLE("Manual"), 
        GENOME("Genome");
        
        private String name;
        
        private LoadStrategy(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public enum LoadStatus
    {
        UNLOADED("Unloaded"), 
        LOADING("Loading"), 
        LOADED("Loaded");
        
        private String name;
        
        private LoadStatus(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public enum ServerStatus
    {
        NotInitialized("Not initialized"), 
        Initialized("Initialized"), 
        NotResponding("Not responding");
        
        private String name;
        
        private ServerStatus(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public enum RefreshStatus
    {
        NOT_REFRESHED("Feature not refeshed yet."), 
        NO_DATA_LOADED("No data found in visible region."), 
        NO_SEQ_PRESENT("Current sequence is not present on feature."), 
        NO_NEW_DATA_LOADED("All data in visible region is already loaded."), 
        DATA_LOADED("Data Loaded");
        
        private String message;
        
        private RefreshStatus(final String message) {
            this.message = message;
        }
        
        @Override
        public String toString() {
            return this.message;
        }
    }
}
