// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.BioSeq;
import java.io.IOException;
import com.affymetrix.genometryImpl.quickload.QuickLoadSymLoader;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.SeqSpan;
import java.net.URL;
import com.affymetrix.genometryImpl.general.GenericVersion;
import com.affymetrix.genometryImpl.general.GenericServer;

public class LocalFilesServerType implements ServerTypeI
{
    private static final String name = "Local Files";
    public static final int ordinal = 40;
    private static final LocalFilesServerType instance;
    
    public static LocalFilesServerType getInstance() {
        return LocalFilesServerType.instance;
    }
    
    @Override
    public String getName() {
        return "Local Files";
    }
    
    @Override
    public int compareTo(final ServerTypeI o) {
        return 40 - o.getOrdinal();
    }
    
    @Override
    public int getOrdinal() {
        return 40;
    }
    
    @Override
    public String toString() {
        return "Local Files";
    }
    
    @Override
    public boolean processServer(final GenericServer gServer, final String path) {
        return false;
    }
    
    @Override
    public String formatURL(final String url) {
        return url;
    }
    
    @Override
    public Object getServerInfo(final String url, final String name) {
        return null;
    }
    
    @Override
    public String adjustURL(final String url) {
        return url;
    }
    
    @Override
    public boolean loadStrategyVisibleOnly() {
        return false;
    }
    
    @Override
    public void discoverFeatures(final GenericVersion gVersion, final boolean autoload) {
    }
    
    @Override
    public void discoverChromosomes(final Object versionSourceObj) {
    }
    
    @Override
    public boolean hasFriendlyURL() {
        return true;
    }
    
    @Override
    public boolean canHandleFeature() {
        return false;
    }
    
    @Override
    public boolean getSpeciesAndVersions(final GenericServer gServer, final GenericServer primaryServer, final URL primaryURL, final VersionDiscoverer versionDiscoverer) {
        return false;
    }
    
    @Override
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan span, final GenericFeature feature) throws IOException {
        return ((QuickLoadSymLoader)feature.symL).loadFeatures(span, feature);
    }
    
    @Override
    public boolean isAuthOptional() {
        return false;
    }
    
    @Override
    public boolean getResidues(final GenericServer server, final List<GenericVersion> versions, final String genomeVersionName, final BioSeq aseq, final int min, final int max, final SeqSpan span) {
        return false;
    }
    
    @Override
    public boolean isSaveServersInPrefs() {
        return false;
    }
    
    static {
        instance = new LocalFilesServerType();
    }
}
