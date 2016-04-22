//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.das;

import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.net.MalformedURLException;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import com.affymetrix.genometryImpl.util.XMLUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.GenometryModel;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.HashSet;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Map;
import java.util.Set;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.net.URL;

public final class DasSource
{
    public static final String ENTRY_POINTS = "entry_points";
    public static final String TYPES = "types";
    private final URL server;
    private final URL master;
    private final URL primary;
    private final GenericServer primaryServer;
    private final String id;
    private final Set<String> sources;
    private final Set<String> entry_points;
    private final Map<String, String> types;
    private boolean entries_initialized;
    private boolean types_initialized;
    private AnnotatedSeqGroup genome;

    public DasSource(final URL server, final URL master, final URL primary, final GenericServer primaryServer) {
        this.sources = new HashSet<String>();
        this.entry_points = new LinkedHashSet<String>();
        this.types = new LinkedHashMap<String, String>();
        this.entries_initialized = false;
        this.types_initialized = false;
        this.genome = null;
        this.server = server;
        this.master = master;
        this.primary = primary;
        this.id = getID(master);
        this.primaryServer = primaryServer;
    }

    static String getID(final URL master) {
        String path = master.getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path.substring(1 + path.lastIndexOf(47), path.length());
    }

    public String getID() {
        return this.id;
    }

    synchronized void add(final String source) {
        this.sources.add(source);
    }

    public AnnotatedSeqGroup getGenome() {
        if (this.genome == null) {
            this.genome = GenometryModel.getGenometryModel().addSeqGroup(this.getID());
        }
        return this.genome;
    }

    public Set<String> getEntryPoints() {
        if (!this.entries_initialized) {
            this.initEntryPoints();
        }
        return this.entry_points;
    }

    public Map<String, String> getTypes() {
        if (!this.types_initialized) {
            this.initTypes();
        }
        return this.types;
    }

    private boolean initEntryPoints() {
        InputStream stream = null;
        try {
            final String entry_point = this.master.getPath() + "/" + "entry_points";
            final String pri_entry_point = this.id + "/" + "entry_points";
            stream = this.getInputStream(this.master, entry_point, pri_entry_point, "Das Entry Request");
            if (stream == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not contact server at {0}", this.master);
                return false;
            }
            final Document doc = XMLUtils.getDocument(stream);
            final NodeList segments = doc.getElementsByTagName("SEGMENT");
            this.addSegments(segments);
            return true;
        }
        catch (MalformedURLException ex) {
            ErrorHandler.errorPanel("Error initializing DAS entry points for\n" + this.getID() + " on " + this.server, ex, Level.SEVERE);
        }
        catch (ParserConfigurationException ex2) {
            ErrorHandler.errorPanel("Error initializing DAS entry points for\n" + this.getID() + " on " + this.server, ex2, Level.SEVERE);
        }
        catch (SAXException ex3) {
            ErrorHandler.errorPanel("Error initializing DAS entry points for\n" + this.getID() + " on " + this.server, ex3, Level.SEVERE);
        }
        catch (IOException ex4) {
            ErrorHandler.errorPanel("Error initializing DAS entry points for\n" + this.getID() + " on " + this.server, ex4, Level.SEVERE);
        }
        finally {
            GeneralUtils.safeClose(stream);
            synchronized (this) {
                this.entries_initialized = true;
            }
        }
        return true;
    }

    private void addSegments(final NodeList segments) throws NumberFormatException {
        final int length = segments.getLength();
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "segments: {0}", length);
        for (int i = 0; i < length; ++i) {
            final Element seg = (Element)segments.item(i);
            final String segid = seg.getAttribute("id");
            final String stopstr = seg.getAttribute("stop");
            final String sizestr = seg.getAttribute("size");
            int stop = 1;
            if (stopstr != null && !stopstr.isEmpty()) {
                stop = Integer.parseInt(stopstr);
            }
            else if (sizestr != null) {
                stop = Integer.parseInt(sizestr);
            }
            synchronized (this) {
                this.getGenome().addSeq(segid, stop);
                this.entry_points.add(segid);
            }
        }
    }

    private void initTypes() {
        final Set<String> badSources = new HashSet<String>();
        for (final String source : this.sources) {
            if (!this.initType(source)) {
                badSources.add(source);
            }
        }
        synchronized (this) {
            for (final String source2 : badSources) {
                this.sources.remove(source2);
            }
            this.types_initialized = true;
        }
    }

    private boolean initType(final String source) {
        InputStream stream = null;
        try {
            final String type_req = source + "/" + "types";
            final URL typesURL = new URL(this.server, type_req);
            final URL testMasterURL = new URL(this.master, this.master.getPath() + "/" + "types");
            stream = this.getInputStream(this.server, type_req, type_req, "Das Types Request");
            if (stream == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Types request failed for {0}, skipping", typesURL);
                return false;
            }
            final Document doc = XMLUtils.getDocument(stream);
            final NodeList typelist = doc.getElementsByTagName("TYPE");
            final int typeLength = typelist.getLength();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "types: {0}", typeLength);
            for (int i = 0; i < typeLength; ++i) {
                final Element typenode = (Element)typelist.item(i);
                final String typeid = typenode.getAttribute("id");
                String name = URLEquals(typesURL, testMasterURL) ? null : (source + "/" + typeid);
                final URL url = new URL(this.server, source + "/features?type=" + typeid + ";");
                name = ((name == null) ? typeid : name);
                synchronized (this) {
                    this.types.put(name, url.toExternalForm());
                }
            }
            return true;
        }
        catch (MalformedURLException ex) {
            ErrorHandler.errorPanel("Error initializing DAS types for\n" + this.getID() + " on " + this.server, ex, Level.SEVERE);
            return false;
        }
        catch (ParserConfigurationException ex2) {
            ErrorHandler.errorPanel("Error initializing DAS types for\n" + this.getID() + " on " + this.server, ex2, Level.SEVERE);
            return false;
        }
        catch (SAXException ex3) {
            ErrorHandler.errorPanel("Error initializing DAS types for\n" + this.getID() + " on " + this.server, ex3, Level.SEVERE);
            return false;
        }
        catch (IOException ex4) {
            ErrorHandler.errorPanel("Error initializing DAS types for\n" + this.getID() + " on " + this.server, ex4, Level.SEVERE);
            return false;
        }
        finally {
            GeneralUtils.safeClose(stream);
            return true;
        }
//        return true;
    }

    private static boolean URLEquals(final URL url1, final URL url2) {
        final int port1 = (url1.getPort() == -1) ? url1.getDefaultPort() : url1.getPort();
        final int port2 = (url2.getPort() == -1) ? url2.getDefaultPort() : url2.getPort();
        final String ref1 = (url1.getRef() == null) ? "" : url1.getRef();
        final String ref2 = (url2.getRef() == null) ? "" : url2.getRef();
        return port1 == port2 && ref1.equals(ref2) && url1.getProtocol().equalsIgnoreCase(url2.getProtocol()) && url1.getHost().equalsIgnoreCase(url2.getHost()) && url1.getFile().equals(url2.getFile());
    }

    private InputStream getInputStream(final URL server, final String query, final String pri_default, final String log_string) throws MalformedURLException, IOException {
        URL load_url = this.getLoadURL(server, query, pri_default);
        InputStream istr = LocalUrlCacher.getInputStream(load_url);
        if (istr == null && this.isLoadingFromPrimary()) {
            Logger.getLogger(DasSource.class.getName()).log(Level.WARNING, "Primary Server :{0} is not responding. So disabling it for this session.", this.primaryServer.serverName);
            this.primaryServer.setServerStatus(LoadUtils.ServerStatus.NotResponding);
            load_url = this.getLoadURL(server, query, pri_default);
            istr = LocalUrlCacher.getInputStream(load_url);
        }
        Logger.getLogger(DasServerInfo.class.getName()).log(Level.INFO, "{0} : {1}", new Object[] { log_string, load_url });
        return istr;
    }

    private boolean isLoadingFromPrimary() {
        return this.primary != null && this.primaryServer != null && !this.primaryServer.getServerStatus().equals(LoadUtils.ServerStatus.NotResponding);
    }

    private URL getLoadURL(final URL server, final String query, final String pri_default) throws MalformedURLException {
        if (!this.isLoadingFromPrimary()) {
            return new URL(server, query);
        }
        return new URL(this.primary, pri_default + ".xml");
    }

    public Set<String> getSources() {
        return this.sources;
    }

    public URL getMasterURL() {
        return this.master;
    }

    public URL getServerURL() {
        return this.server;
    }
}
