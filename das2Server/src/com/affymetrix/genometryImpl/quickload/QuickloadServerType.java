// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.quickload;

import java.util.HashSet;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symloader.TwoBitNew;
import com.affymetrix.genometryImpl.symloader.BNIB;
import java.net.URISyntaxException;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Collection;
import java.io.IOException;
import com.affymetrix.genometryImpl.util.SpeciesLookup;
import com.affymetrix.genometryImpl.util.VersionDiscoverer;
import java.util.Map;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.PositionCalculator;
import org.apache.commons.lang3.mutable.MutableInt;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import java.text.MessageFormat;
import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.parsers.AnnotsXmlParser;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.general.GenericVersion;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.util.Set;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import java.util.List;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.util.ServerTypeI;

public class QuickloadServerType implements ServerTypeI
{
    private static final boolean DEBUG = false;
    private static final String name = "Quickload";
    public static final int ordinal = 20;
    private static final GenometryModel gmodel;
    private static final List<QuickLoadSymLoaderHook> quickLoadSymLoaderHooks;
    private static final SynonymLookup LOOKUP;
    private static final Set<String> quickloadFiles;
    private static final QuickloadServerType instance;
    
    public static QuickloadServerType getInstance() {
        return QuickloadServerType.instance;
    }
    
    @Override
    public String getName() {
        return "Quickload";
    }
    
    @Override
    public int compareTo(final ServerTypeI o) {
        return 20 - o.getOrdinal();
    }
    
    @Override
    public int getOrdinal() {
        return 20;
    }
    
    @Override
    public String toString() {
        return "Quickload";
    }
    
    private boolean getFileAvailability(final String fileName) {
        return fileName.equals("annots.txt") || fileName.equals("annots.xml") || fileName.equals("liftAll.lft");
    }
    
    private boolean getAllFiles(final GenericServer gServer, final String genome_name, String local_path) {
        final Set<String> files = QuickloadServerType.quickloadFiles;
        final String server_path = gServer.URL + "/" + genome_name;
        local_path = local_path + "/" + genome_name;
        GeneralUtils.makeDir(local_path);
        for (final String fileName : files) {
            final boolean fileMayNotExist = this.getFileAvailability(fileName);
            final File file = GeneralUtils.getFile(server_path + "/" + fileName, fileMayNotExist);
            if (file == null && !fileMayNotExist) {
                return false;
            }
            if (!GeneralUtils.moveFileTo(file, fileName, local_path)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean processServer(final GenericServer gServer, final String path) {
        final File file = GeneralUtils.getFile(gServer.URL + "contents.txt", false);
        String quickloadStr = null;
        quickloadStr = (String)gServer.serverObj;
        final QuickLoadServerModel quickloadServer = new QuickLoadServerModel(quickloadStr);
        final List<String> genome_names = quickloadServer.getGenomeNames();
        if (!GeneralUtils.moveFileTo(file, "contents.txt", path)) {
            return false;
        }
        for (final String genome_name : genome_names) {
            if (!this.getAllFiles(gServer, genome_name, path)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not find all files for {0} !!!", gServer.serverName);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String formatURL(final String url) {
        return url.endsWith("/") ? url : (url + "/");
    }
    
    @Override
    public Object getServerInfo(final String url, final String name) {
        return this.formatURL(url);
    }
    
    @Override
    public String adjustURL(final String url) {
        return url;
    }
    
    @Override
    public boolean loadStrategyVisibleOnly() {
        return false;
    }
    
    public static void addQuickLoadSymLoaderHook(final QuickLoadSymLoaderHook quickLoadSymLoaderHook) {
        QuickloadServerType.quickLoadSymLoaderHooks.add(quickLoadSymLoaderHook);
    }
    
    private QuickLoadSymLoader getQuickLoad(final GenericVersion version, final String featureName) {
        final URI uri = determineURI(version, featureName);
        final String extension = SymLoader.getExtension(uri);
        final SymLoader symL = ServerUtils.determineLoader(extension, uri, featureName, version.group);
        QuickLoadSymLoader quickLoadSymLoader = new QuickLoadSymLoader(uri, featureName, version, symL);
        for (final QuickLoadSymLoaderHook quickLoadSymLoaderHook : QuickloadServerType.quickLoadSymLoaderHooks) {
            quickLoadSymLoader = quickLoadSymLoaderHook.processQuickLoadSymLoader(quickLoadSymLoader);
        }
        return quickLoadSymLoader;
    }
    
    private static URI determineURI(final GenericVersion version, String featureName) {
        URI uri = null;
        if (version.gServer.URL == null || version.gServer.URL.length() == 0) {
            final int httpIndex = featureName.toLowerCase().indexOf("http:");
            if (httpIndex > -1) {
                featureName = GeneralUtils.convertStreamNameToValidURLName(featureName);
                uri = URI.create(featureName);
            }
            else {
                uri = new File(featureName).toURI();
            }
        }
        else {
            final String fileName = determineFileName(version, featureName);
            final int httpIndex2 = fileName.toLowerCase().indexOf("http:");
            final int httpsIndex = fileName.toLowerCase().indexOf("https:");
            final int ftpIndex = fileName.toLowerCase().indexOf("ftp:");
            if (httpIndex2 > -1 || httpsIndex > -1 || ftpIndex > -1) {
                uri = URI.create(fileName);
            }
            else {
                uri = URI.create(version.gServer.URL + version.versionID + "/" + determineFileName(version, featureName));
            }
        }
        return uri;
    }
    
    private static String determineFileName(final GenericVersion version, final String featureName) {
        URL quickloadURL;
        try {
            quickloadURL = new URL((String)version.gServer.serverObj);
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
            return "";
        }
        final QuickLoadServerModel quickloadServer = QuickLoadServerModel.getQLModelForURL(quickloadURL);
        final List<AnnotsXmlParser.AnnotMapElt> annotsList = quickloadServer.getAnnotsMap(version.versionID);
        for (final AnnotsXmlParser.AnnotMapElt annotMapElt : annotsList) {
            if (annotMapElt.title.equals(featureName)) {
                return annotMapElt.fileName;
            }
        }
        return "";
    }
    
    @Override
    public void discoverFeatures(final GenericVersion gVersion, final boolean autoload) {
        ProgressUpdater progressUpdater = null;
        try {
            final URL quickloadURL = new URL((String)gVersion.gServer.serverObj);
            final QuickLoadServerModel quickloadServer = QuickLoadServerModel.getQLModelForURL(quickloadURL);
            final List<String> typeNames = quickloadServer.getTypes(gVersion.versionName);
            if (typeNames == null) {
                final String errorText = MessageFormat.format(GenometryConstants.BUNDLE.getString("quickloadGenomeError"), gVersion.gServer.serverName, gVersion.group.getOrganism(), gVersion.versionName);
                ErrorHandler.errorPanelWithReportBug(gVersion.gServer.serverName, errorText, Level.SEVERE);
                return;
            }
            final MutableInt nameLoop = new MutableInt(0);
            progressUpdater = new ProgressUpdater("Quickload discover features", 0L, typeNames.size(), new PositionCalculator() {
                @Override
                public long getCurrentPosition() {
                    return nameLoop.intValue();
                }
            });
            CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(progressUpdater);
            while (nameLoop.intValue() < typeNames.size()) {
                final String type_name = typeNames.get(nameLoop.intValue());
                if (type_name == null || type_name.length() == 0) {
                    System.out.println("WARNING: Found empty feature name in " + gVersion.versionName + ", " + gVersion.gServer.serverName);
                }
                else {
                    final Map<String, String> type_props = quickloadServer.getProps(gVersion.versionName, type_name);
                    gVersion.addFeature(new GenericFeature(type_name, type_props, gVersion, this.getQuickLoad(gVersion, type_name), null, autoload));
                }
                nameLoop.increment();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
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
        URL quickloadURL = null;
        try {
            quickloadURL = new URL((String)gServer.serverObj);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        final QuickLoadServerModel quickloadServer = QuickLoadServerModel.getQLModelForURL(quickloadURL, primaryURL, primaryServer);
        if (quickloadServer == null) {
            System.out.println("ERROR: No quickload server model found for server: " + gServer);
            return false;
        }
        final List<String> genomeList = quickloadServer.getGenomeNames();
        if (genomeList == null || genomeList.isEmpty()) {
            System.out.println("WARNING: No species found in server: " + gServer);
            return false;
        }
        if (quickloadServer.hasSpeciesTxt()) {
            try {
                SpeciesLookup.load(quickloadServer.getSpeciesTxt());
            }
            catch (IOException ex2) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "No species.txt found at this quickload server.", ex2);
            }
        }
        for (final String genomeID : genomeList) {
            final String genomeName = QuickloadServerType.LOOKUP.findMatchingSynonym(QuickloadServerType.gmodel.getSeqGroupNames(), genomeID);
            final Set<GenericVersion> gVersions = QuickloadServerType.gmodel.addSeqGroup(genomeName).getEnabledVersions();
            String versionName;
            String speciesName;
            if (!gVersions.isEmpty()) {
                versionName = GeneralUtils.getPreferredVersionName(gVersions);
                speciesName = versionDiscoverer.versionName2Species(versionName);
            }
            else {
                versionName = genomeName;
                speciesName = SpeciesLookup.getSpeciesName(genomeName);
            }
            versionDiscoverer.discoverVersion(genomeID, versionName, gServer, quickloadServer, speciesName);
        }
        return true;
    }
    
    @Override
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan span, final GenericFeature feature) throws IOException {
        return ((QuickLoadSymLoader)feature.symL).loadFeatures(span, feature);
    }
    
    @Override
    public boolean isAuthOptional() {
        return false;
    }
    
    private String generateQuickLoadURI(String common_url, final String vPath, final QFORMAT Format) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "trying to load residues via Quickload");
        switch (Format) {
            case BNIB: {
                common_url += "bnib";
                break;
            }
            case FA: {
                common_url += "fa";
                break;
            }
            case VTWOBIT: {
                common_url = vPath;
                break;
            }
            case TWOBIT: {
                common_url += "2bit";
                break;
            }
        }
        return common_url;
    }
    
    private QFORMAT determineFormat(final String common_url, final String vPath) {
        for (final QFORMAT format : QFORMAT.values()) {
            final String url_path = this.generateQuickLoadURI(common_url, vPath, format);
            if (LocalUrlCacher.isValidURL(url_path)) {
                Logger.getLogger(this.getClass().getName()).log(Level.FINE, "  Quickload location of " + format + " file: {0}", url_path);
                return format;
            }
        }
        return null;
    }
    
    private SymLoader determineLoader(final String common_url, final String vPath, final AnnotatedSeqGroup seq_group, final String seq_name) {
        final QFORMAT format = this.determineFormat(common_url, vPath);
        if (format == null) {
            return null;
        }
        URI uri = null;
        try {
            uri = new URI(this.generateQuickLoadURI(common_url, vPath, format));
        }
        catch (URISyntaxException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        switch (format) {
            case BNIB: {
                return new BNIB(uri, "", seq_group);
            }
            case VTWOBIT: {
                return new TwoBitNew(uri, "", seq_group);
            }
            case TWOBIT: {
                return new TwoBitNew(uri, "", seq_group);
            }
            default: {
                return null;
            }
        }
    }
    
    private String GetQuickLoadResidues(final GenericServer server, final GenericVersion version, final AnnotatedSeqGroup seq_group, final String seq_name, final String root_url, final SeqSpan span, final BioSeq aseq) {
        String common_url = "";
        String path = "";
        try {
            final URL quickloadURL = new URL((String)server.serverObj);
            final QuickLoadServerModel quickloadServer = QuickLoadServerModel.getQLModelForURL(quickloadURL);
            path = quickloadServer.getPath(version.versionName, seq_name);
            common_url = root_url + path + ".";
            final String vPath = root_url + quickloadServer.getPath(version.versionName, version.versionName) + ".2bit";
            final SymLoader symloader = this.determineLoader(common_url, vPath, seq_group, seq_name);
            if (symloader != null) {
                return symloader.getRegionResidues(span);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public boolean getResidues(final GenericServer server, final List<GenericVersion> versions, final String genomeVersionName, final BioSeq aseq, final int min, final int max, final SeqSpan span) {
        final String seq_name = aseq.getID();
        final AnnotatedSeqGroup seq_group = aseq.getSeqGroup();
        for (final GenericVersion version : versions) {
            if (!server.equals(version.gServer)) {
                continue;
            }
            final String residues = this.GetQuickLoadResidues(server, version, seq_group, seq_name, server.URL, span, aseq);
            if (residues != null) {
                BioSeq.addResiduesToComposition(aseq, residues, span);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSaveServersInPrefs() {
        return true;
    }
    
    static {
        gmodel = GenometryModel.getGenometryModel();
        quickLoadSymLoaderHooks = new ArrayList<QuickLoadSymLoaderHook>();
        LOOKUP = SynonymLookup.getDefaultLookup();
        (quickloadFiles = new HashSet<String>()).add("annots.txt");
        QuickloadServerType.quickloadFiles.add("annots.xml");
        QuickloadServerType.quickloadFiles.add("mod_chromInfo.txt");
        QuickloadServerType.quickloadFiles.add("liftAll.lft");
        instance = new QuickloadServerType();
    }
    
    enum QFORMAT
    {
        BNIB, 
        VTWOBIT, 
        TWOBIT, 
        FA;
    }
}
