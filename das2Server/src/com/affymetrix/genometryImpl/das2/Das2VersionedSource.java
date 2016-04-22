// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.io.IOException;
import java.net.MalformedURLException;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLConnection;
import com.affymetrix.genometryImpl.parsers.BedParser;
import java.io.DataInputStream;
import java.net.URL;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.parsers.Das2FeatureSaxParser;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.XMLUtils;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.util.ServerUtils;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Map;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.net.URI;
import com.affymetrix.genometryImpl.GenometryModel;

public final class Das2VersionedSource
{
    private static final boolean URL_ENCODE_QUERY = true;
    public static final String SEGMENTS_CAP_QUERY = "segments";
    public static final String TYPES_CAP_QUERY = "types";
    public static final String FEATURES_CAP_QUERY = "features";
    private static final String XML = ".xml";
    static String ID;
    static String URID;
    static String SEGMENT;
    static String NAME;
    static String TITLE;
    static GenometryModel gmodel;
    private final URI version_uri;
    private final URI coords_uri;
    private final URI primary_uri;
    private final GenericServer primaryServer;
    private final Das2Source source;
    private final String name;
    private final Map<String, Das2Capability> capabilities;
    private final Map<String, Das2Region> regions;
    private AnnotatedSeqGroup genome;
    private final Map<String, Das2Type> types;
    private final Map<String, List<Das2Type>> residue2types;
    private boolean regions_initialized;
    private boolean types_initialized;
    private String types_filter;
    
    public Das2VersionedSource(final Das2Source das_source, final URI vers_uri, final URI coords_uri, final String name, final String href, final String description, final boolean init, final URI pri_uri, final GenericServer primaryServer) {
        this.capabilities = new HashMap<String, Das2Capability>();
        this.regions = new LinkedHashMap<String, Das2Region>();
        this.genome = null;
        this.types = new LinkedHashMap<String, Das2Type>();
        this.residue2types = new LinkedHashMap<String, List<Das2Type>>();
        this.regions_initialized = false;
        this.types_initialized = false;
        this.types_filter = null;
        this.name = name;
        this.coords_uri = coords_uri;
        this.version_uri = vers_uri;
        this.primaryServer = primaryServer;
        this.primary_uri = ((pri_uri == null) ? null : URI.create(pri_uri.toString() + name + "/"));
        this.source = das_source;
        if (init) {
            this.initSegments();
            this.initTypes(null);
        }
    }
    
    public String getID() {
        return this.version_uri.toString();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    public Das2Source getSource() {
        return this.source;
    }
    
    void addCapability(final Das2Capability cap) {
        this.capabilities.put(cap.getType(), cap);
        Das2Capability.getCapabilityMap().put(cap.getRootURI().toString(), this);
    }
    
    public Das2Capability getCapability(final String type) {
        return this.capabilities.get(type);
    }
    
    public AnnotatedSeqGroup getGenome() {
        if (this.genome != null) {
            return this.genome;
        }
        String groupid = this.getName();
        if (groupid == null) {
            groupid = this.getID();
        }
        this.genome = Das2VersionedSource.gmodel.getSeqGroup(groupid);
        if (this.genome == null && this.coords_uri != null) {
            this.genome = Das2VersionedSource.gmodel.getSeqGroup(this.coords_uri.toString());
        }
        if (this.genome == null) {
            if (this.coords_uri == null) {
                this.genome = new Das2SeqGroup(this, groupid);
            }
            else if (this.getName() == null) {
                this.genome = new Das2SeqGroup(this, this.coords_uri.toString());
            }
            else {
                this.genome = new Das2SeqGroup(this, groupid);
            }
            Das2VersionedSource.gmodel.addSeqGroup(this.genome);
        }
        return this.genome;
    }
    
    public synchronized Map<String, Das2Region> getSegments() {
        if (!this.regions_initialized) {
            this.initSegments();
        }
        return this.regions;
    }
    
    public Das2Region getSegment(final BioSeq seq) {
        for (final Das2Region region : this.getSegments().values()) {
            final BioSeq region_seq = region.getAnnotatedSeq();
            if (region_seq == seq) {
                return region;
            }
        }
        return null;
    }
    
    private synchronized void addType(final Das2Type type) {
        boolean isResidueFormat = false;
        for (final String format : type.getFormats().keySet()) {
            if (ServerUtils.isResidueFile(format)) {
                isResidueFormat = true;
                break;
            }
        }
        if (isResidueFormat) {
            final String tname = type.getName();
            List<Das2Type> prevlist = this.residue2types.get(tname);
            if (prevlist == null) {
                prevlist = new ArrayList<Das2Type>();
                this.residue2types.put(tname, prevlist);
            }
            prevlist.add(type);
        }
        else {
            this.types.put(type.getURI().toString(), type);
        }
    }
    
    public synchronized Map<String, Das2Type> getTypes() {
        if (!this.types_initialized || this.types_filter != null) {
            this.initTypes(null);
        }
        return this.types;
    }
    
    public synchronized Set<String> getResidueFormat(final String name) {
        final List<Das2Type> Localtypes = this.residue2types.get(name);
        if (Localtypes == null || Localtypes.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<String> formats = new HashSet<String>();
        for (final Das2Type type : Localtypes) {
            for (final String format : type.getFormats().keySet()) {
                formats.add(format.toLowerCase());
            }
        }
        return formats;
    }
    
    private synchronized void initSegments() {
        this.regions_initialized = true;
        final Das2Capability segcap = this.getCapability("segments");
        final String region_request = segcap.getRootURI().toString();
        try {
            Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.FINE, "Das2 Segments Request: {0}", region_request);
            final InputStream response = this.getInputStream("segments", LocalUrlCacher.getPreferredCacheUsage(), false, null, "Das2 Segments Request");
            final Document doc = XMLUtils.getDocument(response);
            final NodeList regionlist = doc.getElementsByTagName("SEGMENT");
            this.getRegionList(regionlist, region_request);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            LocalUrlCacher.invalidateCacheFile(region_request);
        }
    }
    
    private void getRegionList(final NodeList regionlist, final String region_request) throws NumberFormatException {
        Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.FINE, "segments: {0}", regionlist.getLength());
        for (int regionLength = regionlist.getLength(), i = 0; i < regionLength; ++i) {
            final Element reg = (Element)regionlist.item(i);
            String region_id = reg.getAttribute(Das2VersionedSource.URID);
            if (region_id.length() == 0) {
                region_id = reg.getAttribute(Das2VersionedSource.ID);
            }
            if (region_id.indexOf("|") >= 0 || region_id.charAt(region_id.length() - 1) == '.') {
                Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.WARNING, "@@@@@@@@@@@@@ caught bad seq id: {0}", region_id);
            }
            else {
                final URI region_uri = Das2ServerInfo.getBaseURI(region_request, reg).resolve(region_id);
                region_id = Das2FeatureSaxParser.doSeqIdHack(region_id);
                final String lengthstr = reg.getAttribute("length");
                String region_name = reg.getAttribute(Das2VersionedSource.NAME);
                if (region_name.length() == 0) {
                    region_name = reg.getAttribute(Das2VersionedSource.TITLE);
                }
                final String region_info_url = reg.getAttribute("doc_href");
                final int length = Integer.parseInt(lengthstr);
                final Das2Region region = new Das2Region(this, region_uri, region_name, region_info_url, length);
                this.regions.put(region.getID(), region);
            }
        }
    }
    
    private synchronized void initTypes(final String filter) {
        this.types_filter = filter;
        this.types.clear();
        final Das2Capability segcap = this.getCapability("types");
        final String types_request = segcap.getRootURI().toString();
        InputStream response = null;
        try {
            final Map<String, String> headers = new LinkedHashMap<String, String>();
            final String sessionId = this.source.getServerInfo().getSessionId();
            if (sessionId != null) {
                headers.put("sessionId", sessionId);
                response = this.getInputStream("types", 100, false, headers, "Das2 Types Request");
            }
            else {
                response = this.getInputStream("types", LocalUrlCacher.getPreferredCacheUsage(), true, headers, "Das2 Types Request");
            }
            if (response == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Types request {0} was not reachable.", types_request);
                return;
            }
            final Document doc = XMLUtils.getDocument(response);
            final NodeList typelist = doc.getElementsByTagName("TYPE");
            this.getTypeList(typelist, types_request);
        }
        catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            LocalUrlCacher.invalidateCacheFile(types_request);
        }
        finally {
            GeneralUtils.safeClose(response);
        }
        this.types_initialized = true;
    }
    
    private void getTypeList(final NodeList typelist, final String types_request) {
        for (int i = 0; i < typelist.getLength(); ++i) {
            final Element typenode = (Element)typelist.item(i);
            String typeid = typenode.getAttribute(Das2VersionedSource.URID);
            if (typeid.length() == 0) {
                typeid = typenode.getAttribute(Das2VersionedSource.ID);
            }
            String type_name = typenode.getAttribute(Das2VersionedSource.NAME);
            if (type_name.length() == 0) {
                type_name = typenode.getAttribute(Das2VersionedSource.TITLE);
            }
            final NodeList flist = typenode.getElementsByTagName("FORMAT");
            final LinkedHashMap<String, String> formats = new LinkedHashMap<String, String>();
            final HashMap<String, String> props = new HashMap<String, String>();
            for (int k = 0; k < flist.getLength(); ++k) {
                final Element fnode = (Element)flist.item(k);
                String formatid = fnode.getAttribute(Das2VersionedSource.NAME);
                if (formatid == null) {
                    formatid = fnode.getAttribute(Das2VersionedSource.ID);
                }
                String mimetype = fnode.getAttribute("mimetype");
                if (mimetype == null || mimetype.equals("")) {
                    mimetype = "unknown";
                }
                formats.put(formatid, mimetype);
            }
            final NodeList plist = typenode.getElementsByTagName("PROP");
            for (int j = 0; j < plist.getLength(); ++j) {
                final Element pnode = (Element)plist.item(j);
                final String key = pnode.getAttribute("key");
                final String val = pnode.getAttribute("value");
                props.put(key, val);
            }
            URI type_uri = null;
            try {
                type_uri = Das2ServerInfo.getBaseURI(types_request, typenode).resolve(typeid);
            }
            catch (Exception e) {
                Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.WARNING, "Error in typeid, skipping: {0}\nUsually caused by an improper character in the URI.", typeid);
            }
            if (type_uri != null) {
                final Das2Type type = new Das2Type(this, type_uri, type_name, formats, props);
                this.addType(type);
            }
        }
    }
    
    public synchronized List<SeqSymmetry> getFeaturesByName(final String name, final AnnotatedSeqGroup group, final BioSeq chrFilter) {
        InputStream istr = null;
        DataInputStream dis = null;
        try {
            final String feature_query = determineFeatureQuery(this.getCapability("features"), name, chrFilter);
            final URL query_url = new URL(feature_query);
            final URLConnection query_con = query_url.openConnection();
            query_con.setConnectTimeout(20000);
            query_con.setReadTimeout(60000);
            istr = query_con.getInputStream();
            dis = new DataInputStream(istr);
            final AnnotatedSeqGroup tempGroup = AnnotatedSeqGroup.tempGenome(group);
            final BedParser parser = new BedParser();
            final List<SeqSymmetry> feats = parser.parse(dis, feature_query, tempGroup);
            Logger.getLogger(Das2VersionedSource.class.getName()).log(Level.FINE, "parsed query results, annot count = {0}", feats.size());
            return feats;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(istr);
        }
        return null;
    }
    
    private static String determineFeatureQuery(final Das2Capability featcap, final String name, final BioSeq chrFilter) throws UnsupportedEncodingException {
        final String request_root = featcap.getRootURI().toString();
        final String nameglob = URLEncoder.encode(name, "UTF-8");
        final String chrFilterStr = (chrFilter == null) ? "?" : ("?segment=" + URLEncoder.encode(chrFilter.getID(), "UTF-8") + ";");
        final String feature_query = request_root + chrFilterStr + "name=" + nameglob + ";format=bed";
        Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.FINE, "feature query: {0}", feature_query);
        return feature_query;
    }
    
    private InputStream getInputStream(final String query_type, final int cache_opt, final boolean write_to_cache, final Map<String, String> headers, final String log_string) throws MalformedURLException, IOException {
        String load_url = this.getRegionString(query_type);
        InputStream istr = LocalUrlCacher.getInputStream(load_url, cache_opt, write_to_cache, headers);
        if (istr == null && this.isLoadingFromPrimary()) {
            LocalUrlCacher.invalidateCacheFile(load_url);
            Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.WARNING, "Primary Server :{0} is not responding. So disabling it for this session.", this.primaryServer.serverName);
            this.primaryServer.setServerStatus(LoadUtils.ServerStatus.NotResponding);
            load_url = this.getRegionString(query_type);
            istr = LocalUrlCacher.getInputStream(load_url, cache_opt, write_to_cache, headers);
        }
        Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.INFO, "{0} : {1}", new Object[] { log_string, load_url });
        return istr;
    }
    
    private boolean isLoadingFromPrimary() {
        return this.primary_uri != null && this.primaryServer != null && !this.primaryServer.getServerStatus().equals(LoadUtils.ServerStatus.NotResponding);
    }
    
    private String getRegionString(final String type) {
        if (!this.isLoadingFromPrimary()) {
            final Das2Capability segcap = this.getCapability(type);
            return segcap.getRootURI().toString();
        }
        return this.primary_uri.toString() + type + ".xml";
    }
    
    static {
        Das2VersionedSource.ID = "id";
        Das2VersionedSource.URID = "uri";
        Das2VersionedSource.SEGMENT = "segment";
        Das2VersionedSource.NAME = "name";
        Das2VersionedSource.TITLE = "title";
        Das2VersionedSource.gmodel = GenometryModel.getGenometryModel();
    }
}
