// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2;

import java.util.Stack;
import java.util.HashMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.net.MalformedURLException;
import com.affymetrix.genometryImpl.util.LoadUtils;
import org.w3c.dom.Document;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.util.XMLUtils;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collection;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URISyntaxException;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.util.ServerTypeI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI;
import com.affymetrix.genometryImpl.general.GenericServer;

public final class Das2ServerInfo
{
    private static boolean DEBUG_SOURCES_QUERY;
    private GenericServer primaryServer;
    private final URI server_uri;
    private URI primary_uri;
    private final String name;
    private final Map<String, Das2Source> sources;
    private boolean initialized;
    private String sessionId;
    private static String URID;
    private static String ID;
    private static String TITLE;
    private static String NAME;
    private static String TYPE;
    private static String QUERY_URI;
    private static String QUERY_ID;
    private static String XML;
    
    public Das2ServerInfo(final String uri, final String name, final boolean init) throws URISyntaxException {
        this.primaryServer = null;
        this.primary_uri = null;
        this.sources = new LinkedHashMap<String, Das2Source>();
        this.initialized = false;
        this.sessionId = null;
        final String root_string = ServerUtils.formatURL(uri, ServerTypeI.DAS2);
        this.server_uri = new URI(root_string);
        this.name = name;
        if (init) {
            this.initialize();
        }
    }
    
    public URI getURI() {
        return this.server_uri;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public synchronized Map<String, Das2Source> getSources(final URL primary_url, final GenericServer primaryServer) {
        if (this.primary_uri == null) {
            this.setPrimaryURL(primary_url);
            this.primaryServer = primaryServer;
        }
        if (!this.initialized) {
            this.initialize();
        }
        return this.sources;
    }
    
    private void setPrimaryURL(final URL primary_url) {
        if (primary_url != null) {
            try {
                this.primary_uri = new URI(ServerUtils.formatURL(primary_url.toExternalForm(), ServerTypeI.QuickLoad));
            }
            catch (URISyntaxException ex) {
                Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            this.primary_uri = null;
        }
    }
    
    public synchronized Map<String, Das2Source> getSources() {
        return this.getSources(null, null);
    }
    
    private synchronized void addDataSource(final Das2Source ds) {
        this.sources.put(ds.getID(), ds);
    }
    
    public Das2VersionedSource getVersionedSource(final AnnotatedSeqGroup group) {
        final Collection<Das2VersionedSource> vsources = this.getVersionedSources(group);
        if (vsources.isEmpty()) {
            return null;
        }
        return vsources.iterator().next();
    }
    
    private Collection<Das2VersionedSource> getVersionedSources(final AnnotatedSeqGroup group) {
        final Set<Das2VersionedSource> results = new LinkedHashSet<Das2VersionedSource>();
        for (final Das2Source source : this.getSources().values()) {
            for (final Das2VersionedSource version : source.getVersions().values()) {
                final AnnotatedSeqGroup version_group = version.getGenome();
                if (version_group == group) {
                    results.add(version);
                }
            }
        }
        return results;
    }
    
    private synchronized boolean initialize() {
        if (this.server_uri == null) {
            return false;
        }
        InputStream response = null;
        String das_query = this.server_uri.toString();
        try {
            if (!this.login()) {
                System.out.println("WARNING: Could not find Das2 server " + this.server_uri);
                return false;
            }
            if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                System.out.println("Das2 Request: " + this.server_uri);
            }
            final Map<String, String> headers = new LinkedHashMap<String, String>();
            response = this.getInputStream(headers);
            if (response == null) {
                System.out.println("WARNING: Could not find Das2 server " + this.server_uri);
                return false;
            }
            if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                final String content_type = headers.get("content-type");
                System.out.println("Das2 Response content type: " + content_type);
            }
            if (!das_query.endsWith("/")) {
                das_query += "/";
            }
            System.out.println("Initializing " + this.server_uri);
            final Document doc = XMLUtils.getDocument(response);
            this.parseSources(doc.getElementsByTagName("SOURCE"), das_query);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            LocalUrlCacher.invalidateCacheFile(das_query);
            return false;
        }
        finally {
            GeneralUtils.safeClose(response);
        }
        return this.initialized = true;
    }
    
    private InputStream getInputStream(final Map<String, String> headers) throws MalformedURLException, IOException {
        String load_url = this.getLoadURL();
        InputStream istr = LocalUrlCacher.getInputStream(load_url, true, headers);
        if (istr == null && this.isLoadingFromPrimary()) {
            LocalUrlCacher.invalidateCacheFile(load_url);
            Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.WARNING, "Primary Server :{0} is not responding. So disabling it for this session.", this.primaryServer.serverName);
            this.primaryServer.setServerStatus(LoadUtils.ServerStatus.NotResponding);
            load_url = this.getLoadURL();
            istr = LocalUrlCacher.getInputStream(load_url, true, headers);
        }
        Logger.getLogger(Das2ServerInfo.class.getName()).log(Level.INFO, "Loading from server : {0}", load_url);
        return istr;
    }
    
    private boolean isLoadingFromPrimary() {
        return this.primary_uri != null && this.primaryServer != null && !this.primaryServer.getServerStatus().equals(LoadUtils.ServerStatus.NotResponding);
    }
    
    private String getLoadURL() {
        if (!this.isLoadingFromPrimary()) {
            return this.server_uri.toString();
        }
        return this.primary_uri.toString() + "/" + "genome" + Das2ServerInfo.XML;
    }
    
    private synchronized boolean login() {
        try {
            final String das_query = this.server_uri + "/login";
            final Map<String, String> headers = new LinkedHashMap<String, String>();
            final InputStream response = LocalUrlCacher.getInputStream(das_query, 100, false, headers);
            if (response == null) {
                return false;
            }
            final String cookie = headers.get("set-cookie");
            if (cookie != null) {
                this.sessionId = cookie.substring(0, cookie.indexOf(";"));
            }
        }
        catch (IOException ex) {
            System.out.println("Failed server login test:");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void parseSources(final NodeList sources, final String das_query) {
        for (int i = 0; i < sources.getLength(); ++i) {
            final Element source = (Element)sources.item(i);
            String source_id = source.getAttribute(Das2ServerInfo.URID);
            if (source_id.length() == 0) {
                source_id = source.getAttribute(Das2ServerInfo.ID);
            }
            String source_name = source.getAttribute(Das2ServerInfo.TITLE);
            if (source_name.length() == 0) {
                source_name = source.getAttribute(Das2ServerInfo.NAME);
            }
            if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                System.out.println("title: " + source_name + ",  length: " + source_name.length());
            }
            if (source_name == null || source_name.length() == 0) {
                source_name = source_id;
            }
            if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                System.out.println("source_name: " + source_name);
            }
            final URI source_uri = getBaseURI(das_query, source).resolve(source_id);
            final Das2Source dasSource = new Das2Source(this, source_uri, source_name);
            this.addDataSource(dasSource);
            final NodeList slist = source.getChildNodes();
            parseSourceChildren(slist, das_query, dasSource, this.primary_uri, this.primaryServer);
        }
    }
    
    private static void parseSourceChildren(final NodeList slist, final String das_query, final Das2Source dasSource, final URI primary_uri, final GenericServer primaryServer) {
        for (int k = 0; k < slist.getLength(); ++k) {
            if (slist.item(k).getNodeName().equals("VERSION")) {
                final Element version = (Element)slist.item(k);
                String version_id = version.getAttribute(Das2ServerInfo.URID);
                if (version_id.length() == 0) {
                    version_id = version.getAttribute(Das2ServerInfo.ID);
                }
                String version_name = version.getAttribute(Das2ServerInfo.TITLE);
                if (version_name.length() == 0) {
                    version_name = version.getAttribute(Das2ServerInfo.NAME);
                }
                if (version_name.length() == 0) {
                    version_name = version_id;
                }
                if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                    System.out.println("version_name: " + version_name);
                }
                final String version_desc = version.getAttribute("description");
                final String version_info_url = version.getAttribute("doc_href");
                final URI version_uri = getBaseURI(das_query, version).resolve(version_id);
                if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                    System.out.println("base URI for version element: " + getBaseURI(das_query, version));
                    System.out.println("versioned source, name: " + version_name + ", URI: " + version_uri.toString());
                }
                final NodeList vlist = version.getChildNodes();
                final HashMap<String, Das2Capability> caps = new HashMap<String, Das2Capability>();
                URI coords_uri = null;
                for (int j = 0; j < vlist.getLength(); ++j) {
                    final String nodename = vlist.item(j).getNodeName();
                    if (nodename.equals("CAPABILITY") || nodename.equals("CATEGORY")) {
                        final Element capel = (Element)vlist.item(j);
                        final String captype = capel.getAttribute(Das2ServerInfo.TYPE);
                        String query_id = capel.getAttribute(Das2ServerInfo.QUERY_URI);
                        if (query_id.length() == 0) {
                            query_id = capel.getAttribute(Das2ServerInfo.QUERY_ID);
                        }
                        final URI base_uri = getBaseURI(das_query, capel);
                        final URI cap_root = base_uri.resolve(query_id);
                        if (Das2ServerInfo.DEBUG_SOURCES_QUERY) {
                            System.out.println("Capability: " + captype + ", URI: " + cap_root);
                        }
                        final Das2Capability cap = new Das2Capability(captype, cap_root);
                        caps.put(captype, cap);
                    }
                    else if (nodename.equals("COORDINATES")) {
                        final Element coordel = (Element)vlist.item(j);
                        final String uri_att = coordel.getAttribute("uri");
                        final URI base_uri2 = getBaseURI(das_query, coordel);
                        coords_uri = base_uri2.resolve(uri_att);
                    }
                }
                final Das2VersionedSource vsource = new Das2VersionedSource(dasSource, version_uri, coords_uri, version_name, version_desc, version_info_url, false, primary_uri, primaryServer);
                for (final Das2Capability cap2 : caps.values()) {
                    vsource.addCapability(cap2);
                }
                dasSource.addVersion(vsource);
            }
        }
    }
    
    public static URI getBaseURI(final String doc_uri, final Node cnode) {
        final Stack<String> xml_bases = new Stack<String>();
        for (Node pnode = cnode; pnode != null; pnode = pnode.getParentNode()) {
            if (pnode instanceof Element) {
                final Element el = (Element)pnode;
                final String xbase = el.getAttribute("xml:base");
                if (xbase != null && !xbase.equals("")) {
                    xml_bases.push(xbase);
                }
            }
        }
        URI base_uri;
        try {
            base_uri = new URI(doc_uri);
            while (!xml_bases.empty()) {
                final String xbase = xml_bases.pop();
                base_uri = base_uri.resolve(xbase);
            }
        }
        catch (Exception ex) {
            System.out.println("*** problem figuring out base URI, setting to null");
            base_uri = null;
        }
        return base_uri;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    static {
        Das2ServerInfo.DEBUG_SOURCES_QUERY = false;
        Das2ServerInfo.URID = "uri";
        Das2ServerInfo.ID = "id";
        Das2ServerInfo.TITLE = "title";
        Das2ServerInfo.NAME = "name";
        Das2ServerInfo.TYPE = "type";
        Das2ServerInfo.QUERY_URI = "query_uri";
        Das2ServerInfo.QUERY_ID = "query_id";
        Das2ServerInfo.XML = ".xml";
    }
}
