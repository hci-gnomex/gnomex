// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.parsers.das.DASFeatureParser;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.net.URI;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.parsers.das.DASSymmetry;
import java.util.Collections;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import com.affymetrix.genometryImpl.util.QueryBuilder;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Set;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.util.SpeciesLookup;
import com.affymetrix.genometryImpl.util.VersionDiscoverer;
import java.util.List;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.PositionCalculator;
import org.apache.commons.lang3.mutable.MutableInt;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.general.GenericVersion;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.util.ServerTypeI;

public class DasServerType implements ServerTypeI
{
    private static final boolean DEBUG = true;
    private static final boolean exitOnError = false;
    private static final String dsn = "dsn.xml";
    private static final String name = "DAS";
    public static final int ordinal = 30;
    private static final GenometryModel gmodel;
    private static final SynonymLookup LOOKUP;
    private static final DasServerType instance;
    
    public static DasServerType getInstance() {
        return DasServerType.instance;
    }
    
    @Override
    public String getName() {
        return "DAS";
    }
    
    @Override
    public int compareTo(final ServerTypeI o) {
        return 30 - o.getOrdinal();
    }
    
    @Override
    public int getOrdinal() {
        return 30;
    }
    
    @Override
    public String toString() {
        return "DAS";
    }
    
    private String getPath(final String id, final URL server, final String file) {
        try {
            final URL server_path = new URL(server, id + "/" + file);
            return server_path.toExternalForm();
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private boolean getAllDasFiles(final String id, final URL server, final URL master, String local_path) {
        local_path = local_path + "/" + id;
        GeneralUtils.makeDir(local_path);
        final Map<String, String> DasFilePath = new HashMap<String, String>();
        final String entry_point = this.getPath(master.getPath(), master, "entry_points");
        final String types = this.getPath(id, server, "types");
        DasFilePath.put(entry_point, "entry_points.xml");
        DasFilePath.put(types, "types.xml");
        for (final Map.Entry<String, String> fileDet : DasFilePath.entrySet()) {
            final File file = GeneralUtils.getFile(fileDet.getKey(), false);
            if (file == null || !GeneralUtils.moveFileTo(file, fileDet.getValue(), local_path)) {}
        }
        return true;
    }
    
    @Override
    public boolean processServer(final GenericServer gServer, final String path) {
        final File file = GeneralUtils.getFile(gServer.URL, false);
        if (!GeneralUtils.moveFileTo(file, "dsn.xml", path)) {
            return false;
        }
        final DasServerInfo server = (DasServerInfo)gServer.serverObj;
        final Map<String, DasSource> sources = server.getDataSources();
        if (sources == null || sources.values() == null || sources.values().isEmpty()) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Couldn't find species for server: ", gServer);
            return false;
        }
        for (final DasSource source : sources.values()) {
            if (!this.getAllDasFiles(source.getID(), source.getServerURL(), source.getMasterURL(), path)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not find all files for {0} !!!", gServer.serverName);
                return false;
            }
            for (final String src : source.getSources()) {
                if (!this.getAllDasFiles(src, source.getServerURL(), source.getMasterURL(), path)) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not find all files for {0} !!!", gServer.serverName);
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String formatURL(String url) {
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
    
    @Override
    public Object getServerInfo(final String url, final String name) {
        return new DasServerInfo(url);
    }
    
    @Override
    public String adjustURL(final String url) {
        String tempURL = url;
        if (tempURL.endsWith("/dsn")) {
            tempURL = tempURL.substring(0, tempURL.length() - 4);
        }
        return tempURL;
    }
    
    @Override
    public boolean loadStrategyVisibleOnly() {
        return true;
    }
    
    @Override
    public void discoverFeatures(final GenericVersion gVersion, final boolean autoload) {
        final DasSource version = (DasSource)gVersion.versionSourceObj;
        final List<Map.Entry<String, String>> types = new ArrayList<Map.Entry<String, String>>(version.getTypes().entrySet());
        final MutableInt nameLoop = new MutableInt(0);
        final ProgressUpdater progressUpdater = new ProgressUpdater("DAS discover features", 0L, types.size(), new PositionCalculator() {
            @Override
            public long getCurrentPosition() {
                return nameLoop.intValue();
            }
        });
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(progressUpdater);
        while (nameLoop.intValue() < types.size()) {
            final Map.Entry<String, String> type = types.get(nameLoop.intValue());
            final String type_name = type.getKey();
            if (type_name == null || type_name.length() == 0) {
                System.out.println("WARNING: Found empty feature name in " + gVersion.versionName + ", " + gVersion.gServer.serverName);
            }
            else {
                gVersion.addFeature(new GenericFeature(type_name, null, gVersion, null, type.getValue(), autoload));
            }
            nameLoop.increment();
        }
    }
    
    @Override
    public void discoverChromosomes(final Object versionSourceObj) {
        final DasSource version = (DasSource)versionSourceObj;
        version.getGenome();
        version.getEntryPoints();
    }
    
    @Override
    public boolean hasFriendlyURL() {
        return false;
    }
    
    @Override
    public boolean canHandleFeature() {
        return true;
    }
    
    @Override
    public boolean getSpeciesAndVersions(final GenericServer gServer, GenericServer primaryServer, URL primaryURL, final VersionDiscoverer versionDiscoverer) {
        final DasServerInfo server = (DasServerInfo)gServer.serverObj;
        if (primaryURL == null) {
            try {
                primaryURL = new URL(gServer.URL);
                primaryServer = null;
            }
            catch (MalformedURLException x) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot load URL " + gServer.URL + " for DAS server " + gServer.serverName, x);
            }
        }
        final Map<String, DasSource> sources = server.getDataSources(primaryURL, primaryServer);
        if (sources == null || sources.values() == null || sources.values().isEmpty()) {
            System.out.println("WARNING: Couldn't find species for server: " + gServer);
            return false;
        }
        for (final DasSource source : sources.values()) {
            final String speciesName = SpeciesLookup.getSpeciesName(source.getID());
            final String versionName = DasServerType.LOOKUP.findMatchingSynonym(DasServerType.gmodel.getSeqGroupNames(), source.getID());
            final String versionID = source.getID();
            versionDiscoverer.discoverVersion(versionID, versionName, gServer, source, speciesName);
        }
        return true;
    }
    
    protected String getSegment(final SeqSpan span, final GenericFeature feature) {
        final BioSeq current_seq = span.getBioSeq();
        final Set<String> segments = ((DasSource)feature.gVersion.versionSourceObj).getEntryPoints();
        return SynonymLookup.getDefaultLookup().findMatchingSynonym(segments, current_seq.getID());
    }
    
    @Override
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan span, final GenericFeature feature) {
        final String segment = this.getSegment(span, feature);
        final QueryBuilder builder = new QueryBuilder(feature.typeObj.toString());
        builder.add("segment", segment);
        builder.add("segment", segment + ":" + (span.getMin() + 1) + "," + span.getMax());
        final ITrackStyleExtended style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(feature.typeObj.toString(), feature.featureName, "das1", feature.featureProps);
        style.setFeature(feature);
        final URI uri = builder.build();
        System.out.println("Loading DAS feature " + feature.featureName + " with uri " + uri);
        List<DASSymmetry> dassyms = this.parseData(uri);
        if (dassyms != null) {
            if (Thread.currentThread().isInterrupted()) {
                dassyms = null;
                return Collections.emptyList();
            }
            SymLoader.addAnnotations(dassyms, span, uri, feature);
            for (final DASSymmetry sym : dassyms) {
                feature.addMethod(sym.getType());
            }
        }
        return dassyms;
    }
    
    private List<DASSymmetry> parseData(final URI uri) {
        final Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
        InputStream stream = null;
        String content_type = "content/unknown";
        int content_length = -1;
        try {
            stream = LocalUrlCacher.getInputStream(uri.toURL(), true, null, respHeaders);
            List<String> list = respHeaders.get("Content-Type");
            if (list != null && !list.isEmpty()) {
                content_type = list.get(0);
            }
            list = respHeaders.get("Content-Length");
            if (list != null && !list.isEmpty()) {
                try {
                    content_length = Integer.parseInt(list.get(0));
                }
                catch (NumberFormatException ex3) {
                    content_length = -1;
                }
            }
            if (content_length == 0) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "{0} returned no data.", uri);
                return null;
            }
            if (content_type.startsWith("text/plain") || content_type.startsWith("text/html") || content_type.startsWith("text/xml")) {
                final AnnotatedSeqGroup group = GenometryModel.getGenometryModel().getSelectedSeqGroup();
                final DASFeatureParser das_parser = new DASFeatureParser();
                das_parser.setAnnotateSeq(false);
                BufferedInputStream bis = null;
                try {
                    bis = new BufferedInputStream(stream);
                    return das_parser.parse(bis, group);
                }
                catch (XMLStreamException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to parse DAS response", ex);
                }
                finally {
                    GeneralUtils.safeClose(bis);
                }
            }
            else {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Declared data type {0} cannot be processed", content_type);
            }
        }
        catch (Exception ex2) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception encountered: no data returned for url " + uri, ex2);
        }
        finally {
            GeneralUtils.safeClose(stream);
        }
        return null;
    }
    
    @Override
    public boolean isAuthOptional() {
        return false;
    }
    
    @Override
    public boolean getResidues(final GenericServer server, final List<GenericVersion> versions, final String genomeVersionName, final BioSeq aseq, final int min, final int max, final SeqSpan span) {
        final String seq_name = aseq.getID();
        for (final GenericVersion version : versions) {
            if (!server.equals(version.gServer)) {
                continue;
            }
            final String residues = DasLoader.getDasResidues(version, seq_name, min, max);
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
        LOOKUP = SynonymLookup.getDefaultLookup();
        instance = new DasServerType();
    }
}
