// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.das2.Das2ServerType;
import com.affymetrix.genometryImpl.das.DasServerType;
import com.affymetrix.genometryImpl.quickload.QuickloadServerType;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.SeqSpan;
import java.net.URL;
import com.affymetrix.genometryImpl.general.GenericServer;
import com.affymetrix.genometryImpl.general.GenericVersion;

public interface ServerTypeI extends Comparable<ServerTypeI>
{
    public static final ServerTypeI QuickLoad = QuickloadServerType.getInstance();
    public static final ServerTypeI DAS = DasServerType.getInstance();
    public static final ServerTypeI DAS2 = Das2ServerType.getInstance();
    public static final ServerTypeI LocalFiles = LocalFilesServerType.getInstance();
    public static final ServerTypeI DEFAULT = ServerTypeI.LocalFiles;
    
    String getName();
    
    int getOrdinal();
    
    String formatURL(final String p0);
    
    Object getServerInfo(final String p0, final String p1);
    
    String adjustURL(final String p0);
    
    boolean loadStrategyVisibleOnly();
    
    void discoverFeatures(final GenericVersion p0, final boolean p1);
    
    void discoverChromosomes(final Object p0);
    
    boolean hasFriendlyURL();
    
    boolean canHandleFeature();
    
    boolean getSpeciesAndVersions(final GenericServer p0, final GenericServer p1, final URL p2, final VersionDiscoverer p3);
    
    List<? extends SeqSymmetry> loadFeatures(final SeqSpan p0, final GenericFeature p1) throws IOException;
    
    boolean isAuthOptional();
    
    boolean getResidues(final GenericServer p0, final List<GenericVersion> p1, final String p2, final BioSeq p3, final int p4, final int p5, final SeqSpan p6);
    
    boolean processServer(final GenericServer p0, final String p1);
    
    boolean isSaveServersInPrefs();
}
