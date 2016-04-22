// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.HashSet;
import com.affymetrix.genometryImpl.parsers.FastaParser;
import com.affymetrix.genometryImpl.parsers.NibbleResiduesParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.symloader.BAM;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import java.net.HttpURLConnection;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URI;
import com.affymetrix.genometryImpl.parsers.Das2FeatureSaxParser;
import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.util.SpeciesLookup;
import com.affymetrix.genometryImpl.util.VersionDiscoverer;
import java.net.URL;
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
import java.net.URISyntaxException;
import com.affymetrix.genometryImpl.util.ServerUtils;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.util.Set;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.util.ServerTypeI;

public class Das2ServerType implements ServerTypeI
{
    private static final String name = "DAS2";
    public static final int ordinal = 10;
    private static final GenometryModel gmodel;
    private static final SynonymLookup LOOKUP;
    private static final Set<String> das2Files;
    private static final Das2ServerType instance;
    private static final String default_format = "das2feature";
    
    public static Das2ServerType getInstance() {
        return Das2ServerType.instance;
    }
    
    @Override
    public String getName() {
        return "DAS2";
    }
    
    @Override
    public int compareTo(final ServerTypeI o) {
        return 10 - o.getOrdinal();
    }
    
    @Override
    public int getOrdinal() {
        return 10;
    }
    
    @Override
    public String toString() {
        return "DAS2";
    }
    
    private boolean getFileAvailability(final String fileName) {
        return fileName.equals("annots.txt") || fileName.equals("annots.xml") || fileName.equals("liftAll.lft");
    }
    
    private boolean getAllFiles(final GenericServer gServer, final String genome_name, String local_path) {
        final Set<String> files = Das2ServerType.das2Files;
        final String server_path = gServer.URL + "/" + genome_name;
        local_path = local_path + "/" + genome_name;
        GeneralUtils.makeDir(local_path);
        for (String fileName : files) {
            final boolean fileMayNotExist = this.getFileAvailability(fileName);
            final File file = GeneralUtils.getFile(server_path + "/" + fileName, fileMayNotExist);
            fileName += ".xml";
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
        final File file = GeneralUtils.getFile(gServer.URL, false);
        if (!GeneralUtils.moveFileTo(file, "genome.xml", path)) {
            return false;
        }
        final Das2ServerInfo serverInfo = (Das2ServerInfo)gServer.serverObj;
        final Map<String, Das2Source> sources = serverInfo.getSources();
        if (sources == null || sources.values() == null || sources.values().isEmpty()) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Couldn't find species for server: ", gServer);
            return false;
        }
        for (final Das2Source source : sources.values()) {
            for (final Das2VersionedSource versionSource : source.getVersions().values()) {
                if (!this.getAllFiles(gServer, versionSource.getName(), path)) {
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
        Object info = null;
        try {
            info = new Das2ServerInfo(url, name, false);
        }
        catch (URISyntaxException e) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Could not initialize {0} server with address: {1}", new Object[] { name, url });
            e.printStackTrace(System.out);
        }
        return info;
    }
    
    @Override
    public String adjustURL(final String url) {
        String tempURL = url;
        if (tempURL.endsWith("/genome")) {
            tempURL = tempURL.substring(0, tempURL.length() - 7);
        }
        return tempURL;
    }
    
    @Override
    public boolean loadStrategyVisibleOnly() {
        return true;
    }
    
    @Override
    public void discoverFeatures(final GenericVersion gVersion, final boolean autoload) {
        final Das2VersionedSource version = (Das2VersionedSource)gVersion.versionSourceObj;
        final List<Das2Type> types = new ArrayList<Das2Type>(version.getTypes().values());
        final MutableInt nameLoop = new MutableInt(0);
        final ProgressUpdater progressUpdater = new ProgressUpdater("DAS2 discover features", 0L, types.size(), new PositionCalculator() {
            @Override
            public long getCurrentPosition() {
                return nameLoop.intValue();
            }
        });
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(progressUpdater);
        while (nameLoop.intValue() < types.size()) {
            final Das2Type type = types.get(nameLoop.intValue());
            final String type_name = type.getName();
            if (type_name == null || type_name.length() == 0) {
                System.out.println("WARNING: Found empty feature name in " + gVersion.versionName + ", " + gVersion.gServer.serverName);
            }
            else {
                final Map<String, String> type_props = type.getProps();
                gVersion.addFeature(new GenericFeature(type_name, type_props, gVersion, null, type, autoload));
            }
            nameLoop.increment();
        }
    }
    
    @Override
    public void discoverChromosomes(final Object versionSourceObj) {
        final Das2VersionedSource version = (Das2VersionedSource)versionSourceObj;
        version.getGenome();
        version.getSegments();
    }
    
    @Override
    public boolean hasFriendlyURL() {
        return true;
    }
    
    @Override
    public boolean canHandleFeature() {
        return true;
    }
    
    @Override
    public boolean getSpeciesAndVersions(final GenericServer gServer, final GenericServer primaryServer, final URL primaryURL, final VersionDiscoverer versionDiscoverer) {
        final Das2ServerInfo server = (Das2ServerInfo)gServer.serverObj;
        final Map<String, Das2Source> sources = server.getSources(primaryURL, primaryServer);
        if (sources == null || sources.values() == null || sources.values().isEmpty()) {
            System.out.println("WARNING: Couldn't find species for server: " + gServer);
            return false;
        }
        for (final Das2Source source : sources.values()) {
            final String speciesName = SpeciesLookup.getSpeciesName(source.getName());
            for (final Das2VersionedSource versionSource : source.getVersions().values()) {
                final String versionName = Das2ServerType.LOOKUP.findMatchingSynonym(Das2ServerType.gmodel.getSeqGroupNames(), versionSource.getName());
                final String versionID = versionSource.getName();
                versionDiscoverer.discoverVersion(versionID, versionName, gServer, versionSource, speciesName);
            }
        }
        return true;
    }
    
    @Override
    public List<? extends SeqSymmetry> loadFeatures(final SeqSpan span, final GenericFeature feature) {
        final Das2Type dtype = (Das2Type)feature.typeObj;
        final Das2Region region = ((Das2VersionedSource)feature.gVersion.versionSourceObj).getSegment(span.getBioSeq());
        if (dtype == null || region == null) {
            return Collections.emptyList();
        }
        return this.loadSpan(feature, span, region, dtype);
    }
    
    private List<? extends SeqSymmetry> loadSpan(final GenericFeature feature, final SeqSpan span, final Das2Region region, final Das2Type type) {
        final String overlap_filter = Das2FeatureSaxParser.getRangeString(span, false);
        String format = FormatPriorities.getFormat(type);
        if (format == null) {
            format = "das2feature";
        }
        final Das2VersionedSource versioned_source = region.getVersionedSource();
        final Das2Capability featcap = versioned_source.getCapability("features");
        final String request_root = featcap.getRootURI().toString();
        try {
            final String query_part = this.DetermineQueryPart(region, overlap_filter, type.getURI(), format);
            final String feature_query = request_root + "?" + query_part;
            return this.LoadFeaturesFromQuery(feature, span, feature_query, format, type.getURI(), type.getName());
        }
        catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }
    
    private String DetermineQueryPart(final Das2Region region, final String overlap_filter, final URI typeURI, final String format) throws UnsupportedEncodingException {
        final StringBuilder buf = new StringBuilder(200);
        buf.append("segment=");
        buf.append(URLEncoder.encode(region.getID(), "UTF-8"));
        buf.append(";");
        buf.append("overlaps=");
        buf.append(URLEncoder.encode(overlap_filter, "UTF-8"));
        buf.append(";");
        buf.append("type=");
        buf.append(URLEncoder.encode(typeURI.toString(), "UTF-8"));
        if (format != null) {
            buf.append(";");
            buf.append("format=");
            buf.append(URLEncoder.encode(format, "UTF-8"));
        }
        return buf.toString();
    }
    
    public Map<String, List<SeqSymmetry>> splitResultsByTracks(final List<? extends SeqSymmetry> results) {
        final Map<String, List<SeqSymmetry>> track2Results = new HashMap<String, List<SeqSymmetry>>();
        List<SeqSymmetry> resultList = null;
        String method = null;
        for (final SeqSymmetry result : results) {
            method = BioSeq.determineMethod(result);
            if (track2Results.containsKey(method)) {
                resultList = track2Results.get(method);
            }
            else {
                resultList = new ArrayList<SeqSymmetry>();
                track2Results.put(method, resultList);
            }
            resultList.add(result);
        }
        return track2Results;
    }
    
    private List<? extends SeqSymmetry> LoadFeaturesFromQuery(final GenericFeature feature, final SeqSpan span, final String feature_query, final String format, final URI typeURI, final String typeName) {
        final BufferedInputStream bis = null;
        InputStream istr = null;
        String content_subtype = null;
        final Thread thread = Thread.currentThread();
        if (thread.isInterrupted()) {
            return Collections.emptyList();
        }
        try {
            final BioSeq aseq = span.getBioSeq();
            if (span.getMin() == 0 && span.getMax() == aseq.getLength()) {
                istr = LocalUrlCacher.getInputStream(feature_query);
                if (istr == null) {
                    System.out.println("Server couldn't be accessed with query " + feature_query);
                    return Collections.emptyList();
                }
                content_subtype = format;
            }
            else {
                final URL query_url = new URL(feature_query);
                final HttpURLConnection query_con = (HttpURLConnection)query_url.openConnection();
                query_con.setConnectTimeout(20000);
                query_con.setReadTimeout(60000);
                final int response_code = query_con.getResponseCode();
                final String response_message = query_con.getResponseMessage();
                if (response_code != 200) {
                    System.out.println("WARNING, HTTP response code not 200/OK: " + response_code + ", " + response_message);
                }
                if (response_code >= 400 && response_code < 600) {
                    System.out.println("Server returned error code, aborting response parsing!");
                    return Collections.emptyList();
                }
                final String content_type = query_con.getContentType();
                istr = query_con.getInputStream();
                content_subtype = content_type.substring(content_type.indexOf("/") + 1);
                final int sindex = content_subtype.indexOf(59);
                if (sindex >= 0) {
                    content_subtype = content_subtype.substring(0, sindex);
                    content_subtype = content_subtype.trim();
                }
                if (content_subtype == null || content_type.equals("unknown") || content_subtype.equals("unknown") || content_subtype.equals("xml") || content_subtype.equals("plain")) {
                    content_subtype = format;
                }
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Parsing {0} format for DAS2 feature response", content_subtype.toUpperCase());
            List<? extends SeqSymmetry> feats = null;
            final FileTypeHandler fileTypeHandler = FileTypeHolder.getInstance().getFileTypeHandler(content_subtype.toLowerCase());
            if (fileTypeHandler == null) {
                Logger.getLogger(SymLoader.class.getName()).log(Level.WARNING, "ABORTING FEATURE LOADING, FORMAT NOT RECOGNIZED: {0}", content_subtype);
                return Collections.emptyList();
            }
            final ITrackStyleExtended ts = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(typeURI.toString(), typeName, format, feature.featureProps);
            ts.setFeature(feature);
            final SymLoader symL = fileTypeHandler.createSymLoader(typeURI, typeName, aseq.getSeqGroup());
            symL.setExtension(content_subtype);
            if (symL instanceof BAM) {
                final File bamfile = GeneralUtils.convertStreamToFile(istr, typeName);
                bamfile.deleteOnExit();
                final BAM bam = new BAM(bamfile.toURI(), typeName, aseq.getSeqGroup());
                if (typeURI.getScheme().equals("http")) {
                    feats = bam.parseAll(span.getBioSeq(), typeURI.toString());
                }
                else {
                    feats = bam.getRegion(span);
                }
            }
            else {
                feats = symL.parse(istr, false);
            }
            if (thread.isInterrupted()) {
                feats = null;
                return Collections.emptyList();
            }
            return SymLoader.splitFilterAndAddAnnotation(span, feats, feature);
        }
        catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(istr);
        }
    }
    
    @Override
    public boolean isAuthOptional() {
        return true;
    }
    
    private boolean LoadResiduesFromDAS2(final BioSeq aseq, final AnnotatedSeqGroup seq_group, final String uri) {
        InputStream istr = null;
        BufferedReader buff = null;
        final Map<String, String> headers = new HashMap<String, String>();
        try {
            istr = LocalUrlCacher.getInputStream(uri, true, headers);
            final String content_type = headers.get("content-type");
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "    response content-type: {0}", content_type);
            if (istr == null || content_type == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.FINE, "  Improper response from DAS/2; aborting DAS/2 residues loading.");
                return false;
            }
            if (content_type.equals("text/raw")) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "   response is in raw format, parsing...");
                buff = new BufferedReader(new InputStreamReader(istr));
                aseq.setResidues(buff.readLine());
                return true;
            }
            if (content_type.equals(NibbleResiduesParser.getMimeType())) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "   response is in bnib format, parsing...");
                NibbleResiduesParser.parse(istr, seq_group);
                return true;
            }
            if (content_type.equals(FastaParser.getMimeType())) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "   response is in fasta format, parsing...");
                FastaParser.parseSingle(istr, seq_group);
                return true;
            }
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "   response is not in accepted format, aborting DAS/2 residues loading");
            return false;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(buff);
            GeneralUtils.safeClose(istr);
        }
        return false;
    }
    
    private String GetPartialFASTADas2Residues(final String uri) {
        InputStream istr = null;
        BufferedReader buff = null;
        final Map<String, String> headers = new HashMap<String, String>();
        try {
            istr = LocalUrlCacher.getInputStream(uri, true, headers);
            final String content_type = headers.get("content-type");
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "    response content-type: {0}", content_type);
            if (istr == null || content_type == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.FINE, "  Didn't get a proper response from DAS/2; aborting DAS/2 residues loading.");
                return null;
            }
            if (content_type.equals("text/raw")) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "   response is in raw format, parsing...");
                buff = new BufferedReader(new InputStreamReader(istr));
                return buff.readLine();
            }
            if (content_type.equals(FastaParser.getMimeType())) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "   response is in fasta format, parsing...");
                return FastaParser.parseResidues(istr);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "   response is not in accepted format, aborting DAS/2 residues loading");
            return null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(buff);
            GeneralUtils.safeClose(istr);
        }
        return null;
    }
    
    private boolean loadDAS2Residues(final BioSeq aseq, final String uri, final SeqSpan span, final boolean partial_load) {
        final AnnotatedSeqGroup seq_group = aseq.getSeqGroup();
        if (partial_load) {
            final String residues = this.GetPartialFASTADas2Residues(uri);
            if (residues != null) {
                BioSeq.addResiduesToComposition(aseq, residues, span);
                return true;
            }
        }
        else if (this.LoadResiduesFromDAS2(aseq, seq_group, uri)) {
            BioSeq.addResiduesToComposition(aseq);
            return true;
        }
        return false;
    }
    
    private String generateDas2URI(final String URL, final String genomeVersionName, final String segmentName, final int min, final int max, final FORMAT Format) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "trying to load residues via DAS/2");
        String uri = URL + "/" + genomeVersionName + "/" + segmentName + "?format=";
        switch (Format) {
            case RAW: {
                uri += "raw";
                break;
            }
            case BNIB: {
                uri += "bnib";
                break;
            }
            case FASTA: {
                uri += "fasta";
                break;
            }
        }
        if (max > -1) {
            uri = uri + "&range=" + min + ":" + max;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "   request URI: {0}", uri);
        return uri;
    }
    
    @Override
    public boolean getResidues(final GenericServer server, final List<GenericVersion> versions, final String genomeVersionName, final BioSeq aseq, final int min, final int max, final SeqSpan span) {
        final String seq_name = aseq.getID();
        final boolean partial_load = min > 0 || max < aseq.getLength() - 1;
        for (final GenericVersion version : versions) {
            if (!server.equals(version.gServer)) {
                continue;
            }
            final Das2VersionedSource das2version = (Das2VersionedSource)version.versionSourceObj;
            final Set<String> format = das2version.getResidueFormat(seq_name);
            FORMAT[] formats = null;
            if (format != null && !format.isEmpty()) {
                if (format.contains("bnib")) {
                    formats = new FORMAT[] { FORMAT.BNIB };
                }
                else if (format.contains("raw")) {
                    formats = new FORMAT[] { FORMAT.RAW };
                }
                else if (format.contains("fasta") || format.contains("fa")) {
                    formats = new FORMAT[] { FORMAT.FASTA };
                }
            }
            if (formats == null) {
                FORMAT[] array2;
                if (partial_load) {
                    final FORMAT[] array = array2 = new FORMAT[2];
                    array[0] = FORMAT.RAW;
                    array[1] = FORMAT.FASTA;
                }
                else {
                    final FORMAT[] array3 = array2 = new FORMAT[3];
                    array3[0] = FORMAT.BNIB;
                    array3[1] = FORMAT.RAW;
                    array3[2] = FORMAT.FASTA;
                }
                formats = array2;
            }
            for (final FORMAT formatLoop : formats) {
                final String uri = this.generateDas2URI(server.URL, genomeVersionName, seq_name, min, max, formatLoop);
                if (this.loadDAS2Residues(aseq, uri, span, partial_load)) {
                    return true;
                }
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
        (das2Files = new HashSet<String>()).add("types");
        Das2ServerType.das2Files.add("segments");
        instance = new Das2ServerType();
    }
    
    enum FORMAT
    {
        BNIB, 
        RAW, 
        FASTA;
    }
}
