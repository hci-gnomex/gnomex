// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das;

import java.io.IOException;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import org.w3c.dom.Element;
import com.affymetrix.genometryImpl.util.XMLUtils;
import java.util.List;
import java.util.HashMap;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.util.ServerTypeI;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedHashMap;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.util.Map;
import java.net.URL;

public final class DasServerInfo
{
    private static final boolean REPORT_SOURCES = false;
    private static final boolean REPORT_CAPS = true;
    private URL serverURL;
    private URL primaryURL;
    private final Map<String, DasSource> sources;
    private boolean initialized;
    private GenericServer primaryServer;
    
    public DasServerInfo(final String url) {
        this.primaryURL = null;
        this.sources = new LinkedHashMap<String, DasSource>();
        this.initialized = false;
        this.primaryServer = null;
        try {
            this.serverURL = new URL(url);
        }
        catch (MalformedURLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to convert URL '" + url + "' to URI", e);
        }
    }
    
    public Map<String, DasSource> getDataSources(final URL primaryURL, final GenericServer primaryServer) {
        if (this.primaryURL == null) {
            this.setPrimaryURL(primaryURL);
            this.primaryServer = primaryServer;
        }
        if (!this.initialized) {
            this.initialize();
        }
        return this.sources;
    }
    
    private void setPrimaryURL(final URL primaryURL) {
        if (primaryURL != null) {
            try {
                this.primaryURL = new URL(ServerUtils.formatURL(primaryURL.toExternalForm(), ServerTypeI.QuickLoad));
            }
            catch (MalformedURLException ex) {
                Logger.getLogger(DasServerInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            this.primaryURL = null;
        }
    }
    
    public Map<String, DasSource> getDataSources() {
        return this.getDataSources(null, null);
    }
    
    private boolean initialize() {
        InputStream stream = null;
        try {
            final Map<String, List<String>> headers = new HashMap<String, List<String>>();
            stream = this.getInputStream(headers, "Das Request");
            if (stream == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not find URL {0}", this.serverURL);
                return false;
            }
            String das_version = "";
            String das_status = "";
            String das_capabilities = "";
            List<String> list = headers.get("X-DAS-Version");
            if (list != null) {
                das_version = list.toString();
            }
            list = headers.get("X-DAS-Status");
            if (list != null) {
                das_status = list.toString();
            }
            list = headers.get("X-DAS-Capabilities");
            if (list != null) {
                das_capabilities = list.toString();
            }
            System.out.println("DAS server version: " + das_version + ", status: " + das_status);
            System.out.println("DAS capabilities: " + das_capabilities);
            final Document doc = XMLUtils.getDocument(stream);
            final NodeList dsns = doc.getElementsByTagName("DSN");
            final int dsnLength = dsns.getLength();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "dsn count: {0}", dsnLength);
            for (int i = 0; i < dsnLength; ++i) {
                final Element dsn = (Element)dsns.item(i);
                try {
                    this.parseDSNElement(dsn);
                }
                catch (Exception ex) {
                    System.out.println("Error initializing DAS server info for\n" + this.serverURL);
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex2) {
            System.out.println("Error initializing DAS server info for\n" + this.serverURL);
            ex2.printStackTrace();
            return false;
        }
        finally {
            GeneralUtils.safeClose(stream);
        }
        return this.initialized = true;
    }
    
    private void parseDSNElement(final Element dsn) throws DOMException {
        final NodeList sourcelist = dsn.getElementsByTagName("SOURCE");
        final Element source = (Element)sourcelist.item(0);
        if (source == null) {
            System.out.println("Missing SOURCE element.  Ignoring.");
            return;
        }
        final String sourceid = source.getAttribute("id");
        final NodeList masterlist = dsn.getElementsByTagName("MAPMASTER");
        final Element master = (Element)masterlist.item(0);
        if (master == null) {
            System.out.println("Missing MAPMASTER element.  Ignoring " + sourceid);
            return;
        }
        final Text mastertext = (Text)master.getFirstChild();
        String master_url = null;
        if (mastertext != null) {
            master_url = mastertext.getData();
        }
        if (master_url == null || "null".equals(master_url)) {
            return;
        }
        try {
            final URL masterURL = new URL(master_url);
            if (DasSource.getID(masterURL).isEmpty()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Skipping {0} as MAPMASTER could not be parsed", sourceid);
                return;
            }
            DasSource das_source = this.sources.get(DasSource.getID(masterURL));
            synchronized (this) {
                if (das_source == null) {
                    das_source = new DasSource(this.serverURL, masterURL, this.primaryURL, this.primaryServer);
                    this.sources.put(DasSource.getID(masterURL), das_source);
                }
                das_source.add(sourceid);
            }
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "MalformedURLException in DasServerInfo.parseDSNElement() " + ex.getMessage());
        }
    }
    
    public InputStream getInputStream(final Map<String, List<String>> headers, final String log_string) throws MalformedURLException, IOException {
        URL load_url = this.getLoadURL();
        InputStream istr = LocalUrlCacher.getInputStream(load_url, true, null, headers);
        if (istr == null && this.isLoadingFromPrimary()) {
            Logger.getLogger(DasServerInfo.class.getName()).log(Level.WARNING, "Primary Server :{0} is not responding. So disabling it for this session.", this.primaryServer.serverName);
            this.primaryServer.setServerStatus(LoadUtils.ServerStatus.NotResponding);
            load_url = this.getLoadURL();
            istr = LocalUrlCacher.getInputStream(load_url, true, null, headers);
        }
        Logger.getLogger(DasServerInfo.class.getName()).log(Level.INFO, "{0} : {1}", new Object[] { log_string, load_url });
        return istr;
    }
    
    private boolean isLoadingFromPrimary() {
        return this.primaryURL != null && this.primaryServer != null && !this.primaryServer.getServerStatus().equals(LoadUtils.ServerStatus.NotResponding);
    }
    
    private URL getLoadURL() throws MalformedURLException {
        if (!this.isLoadingFromPrimary()) {
            return this.serverURL;
        }
        return new URL(this.primaryURL.toExternalForm() + "/dsn.xml");
    }
}
