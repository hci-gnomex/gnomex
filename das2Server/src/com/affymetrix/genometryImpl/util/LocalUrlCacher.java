// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.net.URISyntaxException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import net.sf.samtools.util.SeekableHTTPStream;
import net.sf.samtools.util.SeekableFileStream;
import java.io.File;
import net.sf.samtools.util.SeekableStream;
import java.net.URI;

public final class LocalUrlCacher
{
    private static final String cache_content_root;
    private static final String cache_header_root;
    private static final String HTTP_STATUS_HEADER = "HTTP_STATUS";
    private static final String HTTP_LOCATION_HEADER = "Location";
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static boolean DEBUG_CONNECTION;
    private static boolean CACHE_FILE_URLS;
    public static final int IGNORE_CACHE = 100;
    public static final int ONLY_CACHE = 101;
    public static final int NORMAL_CACHE = 102;
    public static final String PREF_CACHE_USAGE = "quickload_cache_usage";
    public static final int CACHE_USAGE_DEFAULT = 102;
    public static final String URL_NOT_REACHABLE = "URL_NOT_REACHABLE";
    public static final int CONNECT_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 60000;
    private static boolean offline;
    
    public static void setOffLine(final boolean b) {
        LocalUrlCacher.offline = b;
    }
    
    public static boolean getOffLine() {
        return LocalUrlCacher.offline;
    }
    
    private static boolean isFile(final String url) {
        return url != null && url.length() >= 5 && url.substring(0, 5).compareToIgnoreCase("file:") == 0;
    }
    
    public static boolean isFile(final URI uri) {
        return uri.getScheme() == null || uri.getScheme().length() == 0 || uri.getScheme().equalsIgnoreCase("file");
    }
    
    public static SeekableStream getSeekableStream(final URI uri) throws FileNotFoundException, MalformedURLException {
        if (isFile(uri)) {
            final File f = new File(uri.getPath());
            return (SeekableStream)new SeekableFileStream(f);
        }
        return (SeekableStream)new SeekableHTTPStream(uri.toURL());
    }
    
    public static InputStream getInputStream(final URL url) throws IOException {
        return getInputStream(url.toString(), getPreferredCacheUsage(), true, null, null, false);
    }
    
    public static InputStream getInputStream(final URL url, final boolean write_to_cache, final Map<String, String> rqstHeaders, final Map<String, List<String>> respHeaders) throws IOException {
        return getInputStream(url.toString(), getPreferredCacheUsage(), write_to_cache, rqstHeaders, respHeaders, false);
    }
    
    public static InputStream getInputStream(final String url) throws IOException {
        return getInputStream(url, getPreferredCacheUsage(), true, null, null, false);
    }
    
    public static InputStream getInputStream(final String url, final boolean write_to_cache, final Map<String, String> rqstHeaders) throws IOException {
        return getInputStream(url, getPreferredCacheUsage(), write_to_cache, rqstHeaders, null, false);
    }
    
    public static InputStream getInputStream(final String url, final boolean write_to_cache, final Map<String, String> rqstHeaders, final boolean fileMayNotExist) throws IOException {
        return getInputStream(url, getPreferredCacheUsage(), write_to_cache, rqstHeaders, null, fileMayNotExist);
    }
    
    public static InputStream getInputStream(final String url, final boolean write_to_cache) throws IOException {
        return getInputStream(url, getPreferredCacheUsage(), write_to_cache, null, null, false);
    }
    
    public static InputStream getInputStream(final String url, final int cache_option, final boolean write_to_cache, final Map<String, String> rqstHeaders) throws IOException {
        return getInputStream(url, cache_option, write_to_cache, rqstHeaders, null, false);
    }
    
    private static InputStream getInputStream(final String url, int cache_option, final boolean write_to_cache, final Map<String, String> rqstHeaders, final Map<String, List<String>> respHeaders, final boolean fileMayNotExist) throws IOException {
        String sessionId = null;
        if (rqstHeaders != null) {
            if (rqstHeaders.containsKey("sessionId")) {
                sessionId = rqstHeaders.get("sessionId");
            }
            rqstHeaders.clear();
        }
        if (!LocalUrlCacher.CACHE_FILE_URLS && isFile(url)) {
            return getUncachedFileStream(url, sessionId, fileMayNotExist);
        }
        InputStream result_stream = null;
        final File cache_file = getCacheFile(LocalUrlCacher.cache_content_root, url);
        final File header_cache_file = getCacheFile(LocalUrlCacher.cache_header_root, url);
        long local_timestamp = -1L;
        if (cache_file.exists() && (!header_cache_file.exists() || header_cache_file.length() == 0L)) {
            cache_file.delete();
        }
        else if ((!cache_file.exists() || cache_file.length() == 0L) && header_cache_file.exists()) {
            header_cache_file.delete();
        }
        if ((LocalUrlCacher.offline || cache_option != 100) && cache_file.exists() && header_cache_file.exists()) {
            local_timestamp = cache_file.lastModified();
        }
        URLConnection conn = null;
        long remote_timestamp = 0L;
        boolean url_reachable = false;
        int http_status = -1;
        if (LocalUrlCacher.offline) {
            cache_option = 101;
        }
        if (cache_option != 101) {
            try {
                conn = connectToUrl(url, sessionId, local_timestamp);
                if (LocalUrlCacher.DEBUG_CONNECTION) {
                    reportHeaders(conn);
                }
                if (respHeaders != null) {
                    respHeaders.putAll(conn.getHeaderFields());
                }
                remote_timestamp = conn.getLastModified();
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection hcon = (HttpURLConnection)conn;
                    http_status = hcon.getResponseCode();
                    if (http_status == 307) {
                        conn = handleTemporaryRedirect(conn, url, sessionId, local_timestamp);
                        hcon = (HttpURLConnection)conn;
                        http_status = hcon.getResponseCode();
                    }
                    url_reachable = (http_status >= 200 && http_status < 400);
                }
                else {
                    url_reachable = true;
                    remote_timestamp = conn.getIfModifiedSince();
                }
            }
            catch (IOException ioe) {
                url_reachable = false;
            }
            catch (Exception e) {
                e.printStackTrace();
                url_reachable = false;
            }
            if (!url_reachable) {
                if (!fileMayNotExist) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "URL not reachable, status code = {0}: {1}", new Object[] { http_status, url });
                }
                if (rqstHeaders != null) {
                    rqstHeaders.put("LocalUrlCacher", "URL_NOT_REACHABLE");
                }
                if (!cache_file.exists()) {
                    if (!fileMayNotExist) {
                        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "URL {0} is not reachable, and is not cached!", url);
                    }
                    return null;
                }
            }
        }
        if (local_timestamp != -1L) {
            result_stream = TryToRetrieveFromCache(url_reachable, http_status, cache_file, remote_timestamp, local_timestamp, url, cache_option);
            if (rqstHeaders != null) {
                retrieveHeadersFromCache(rqstHeaders, header_cache_file);
            }
        }
        if (result_stream == null && url_reachable && cache_option != 101) {
            result_stream = RetrieveFromURL(conn, rqstHeaders, write_to_cache, cache_file, header_cache_file);
        }
        if (rqstHeaders != null && LocalUrlCacher.DEBUG_CONNECTION) {
            reportHeaders(url, rqstHeaders);
        }
        if (result_stream == null) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "couldn''t get content for: {0}", url);
        }
        return result_stream;
    }
    
    private static URLConnection handleTemporaryRedirect(URLConnection conn, final String url, final String sessionId, final long local_timestamp) throws MalformedURLException, IOException {
        final String redirect_url = conn.getHeaderField("Location");
        if (redirect_url == null) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "Url {0} moved temporarily. But no redirect url provided", url);
            return conn;
        }
        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "Url {0} moved temporarily. \n Using redirect \nurl {1}", new Object[] { url, redirect_url });
        conn = connectToUrl(redirect_url, sessionId, local_timestamp);
        return conn;
    }
    
    public static URLConnection connectToUrl(final String url, final String sessionId, final long local_timestamp) throws MalformedURLException, IOException {
        final URL theurl = new URL(url);
        final URLConnection conn = theurl.openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(60000);
        conn.setRequestProperty("Accept-Encoding", "gzip");
        if (sessionId != null) {
            conn.setRequestProperty("Cookie", sessionId);
        }
        if (local_timestamp != -1L) {
            conn.setIfModifiedSince(local_timestamp);
        }
        conn.connect();
        return conn;
    }
    
    private static InputStream getUncachedFileStream(final String url, final String sessionId, final boolean fileMayNotExist) throws MalformedURLException, IOException {
        final URL furl = new URL(url);
        final URLConnection huc = furl.openConnection();
        huc.setConnectTimeout(20000);
        huc.setReadTimeout(60000);
        if (sessionId != null) {
            huc.setRequestProperty("Cookie", sessionId);
        }
        InputStream fstr = null;
        try {
            fstr = huc.getInputStream();
        }
        catch (FileNotFoundException ex) {
            if (fileMayNotExist) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Couldn''t find file {0}, but it''s optional.", url);
                return null;
            }
        }
        return fstr;
    }
    
    private static File getCacheFile(final String root, final String url) {
        final File fil = new File(root);
        if (!fil.exists()) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Creating new cache directory: {0}", fil.getAbsolutePath());
            if (!fil.mkdirs()) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, "Could not create directory: {0}", fil.toString());
            }
        }
        final String encoded_url = UrlToFileName.encode(url);
        String cache_file_name = root + encoded_url;
        if (cache_file_name.length() > 255) {
            cache_file_name = root + UrlToFileName.toMd5(encoded_url);
        }
        final File cache_file = new File(cache_file_name);
        return cache_file;
    }
    
    public static void invalidateCacheFile(final String url) {
        final File cache_file = getCacheFile(LocalUrlCacher.cache_content_root, url);
        if (cache_file.exists() && !cache_file.delete()) {
            cache_file.deleteOnExit();
        }
        final File header_cache_file = getCacheFile(LocalUrlCacher.cache_header_root, url);
        if (header_cache_file.exists() && !header_cache_file.delete()) {
            header_cache_file.deleteOnExit();
        }
    }
    
    private static InputStream TryToRetrieveFromCache(final boolean url_reachable, final int http_status, final File cache_file, final long remote_timestamp, final long local_timestamp, final String url, final int cache_option) throws IOException, FileNotFoundException {
        if (url_reachable) {
            if (http_status == 304) {
                if (LocalUrlCacher.DEBUG_CONNECTION) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.FINE, "Received HTTP_NOT_MODIFIED status for URL, using cache: {0}", cache_file);
                }
            }
            else {
                if (remote_timestamp <= 0L || remote_timestamp > local_timestamp) {
                    if (LocalUrlCacher.DEBUG_CONNECTION) {
                        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.FINE, "cached file exists, but URL is more recent, so reloading cache: {0}", url);
                    }
                    return null;
                }
                if (LocalUrlCacher.DEBUG_CONNECTION) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.FINE, "Cache exists and is more recent, using cache: {0}", cache_file);
                }
            }
        }
        else {
            if (cache_option != 101 && LocalUrlCacher.DEBUG_CONNECTION) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "Remote URL not reachable: {0}", url);
            }
            if (LocalUrlCacher.DEBUG_CONNECTION) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Loading cached file for URL: {0}", url);
            }
        }
        return new BufferedInputStream(new FileInputStream(cache_file));
    }
    
    private static void retrieveHeadersFromCache(final Map<String, String> rqstHeaders, final File header_cache_file) throws IOException {
        BufferedInputStream hbis = null;
        try {
            hbis = new BufferedInputStream(new FileInputStream(header_cache_file));
            final Properties headerprops = new Properties();
            headerprops.load(hbis);
            for (final String propKey : headerprops.stringPropertyNames()) {
                rqstHeaders.put(propKey, headerprops.getProperty(propKey));
            }
        }
        finally {
            GeneralUtils.safeClose(hbis);
        }
    }
    
    private static InputStream RetrieveFromURL(final URLConnection conn, final Map<String, String> headers, final boolean write_to_cache, final File cache_file, final File header_cache_file) throws IOException {
        final String contentEncoding = conn.getHeaderField("Content-Encoding");
        final boolean isGZipped = contentEncoding != null && "gzip".equalsIgnoreCase(contentEncoding);
        InputStream connstr;
        if (isGZipped) {
            connstr = GeneralUtils.getGZipInputStream(conn.getURL().toString(), conn.getInputStream());
            if (LocalUrlCacher.DEBUG_CONNECTION) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.FINE, "gzipped stream, so ignoring reported content length of {0}", conn.getContentLength());
            }
        }
        else {
            connstr = conn.getInputStream();
        }
        if (write_to_cache) {
            writeHeadersToCache(header_cache_file, populateHeaderProperties(conn, headers));
            return new CachingInputStream(connstr, cache_file, conn.getURL().toExternalForm());
        }
        return connstr;
    }
    
    private static Properties populateHeaderProperties(final URLConnection conn, final Map<String, String> headers) {
        final Map<String, List<String>> headermap = conn.getHeaderFields();
        final Properties headerprops = new Properties();
        for (final Map.Entry<String, List<String>> ent : headermap.entrySet()) {
            String key = ent.getKey();
            final List<String> vals = ent.getValue();
            if (vals.isEmpty()) {
                continue;
            }
            final String val = vals.get(0);
            if (key == null) {
                key = "HTTP_STATUS";
            }
            key = key.toLowerCase();
            headerprops.setProperty(key, val);
            if (headers == null) {
                continue;
            }
            headers.put(key, val);
        }
        return headerprops;
    }
    
    private static void reportHeaders(final String url, final Map<String, String> headers) {
        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "   HEADERS for URL: {0}", url);
        for (final Map.Entry<String, String> ent : headers.entrySet()) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "   key: {0}, val: {1}", new Object[] { ent.getKey(), ent.getValue() });
        }
    }
    
    public static void clearCache() {
        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Clearing cache");
        DeleteFilesInDirectory(LocalUrlCacher.cache_header_root);
        DeleteFilesInDirectory(LocalUrlCacher.cache_content_root);
    }
    
    private static void DeleteFilesInDirectory(final String filename) {
        final File dir = new File(filename);
        if (dir.exists()) {
            for (final File fil : dir.listFiles()) {
                fil.delete();
            }
        }
    }
    
    public static String getCacheRoot() {
        return LocalUrlCacher.cache_content_root;
    }
    
    public static int getPreferredCacheUsage() {
        return PreferenceUtils.getIntParam("quickload_cache_usage", 102);
    }
    
    public static void setPreferredCacheUsage(final int usage) {
        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Setting Caching mode to {0}", getCacheUsage(usage));
        PreferenceUtils.saveIntParam("quickload_cache_usage", usage);
    }
    
    public static void reportHeaders(final URLConnection query_con) {
        try {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "URL: {0}", query_con.getURL().toString());
            int hindex = 0;
            while (true) {
                final String val = query_con.getHeaderField(hindex);
                final String key = query_con.getHeaderFieldKey(hindex);
                if (val == null && key == null) {
                    break;
                }
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "   header:   key = {0}, val = {1}", new Object[] { key, val });
                ++hindex;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void loadSynonyms(final SynonymLookup lookup, final String synonym_loc) {
        InputStream syn_stream = null;
        try {
            syn_stream = getInputStream(synonym_loc, getPreferredCacheUsage(), false, null, null, true);
            if (syn_stream == null) {
                return;
            }
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "Synonyms found at: {0}", synonym_loc);
            lookup.loadSynonyms(syn_stream);
        }
        catch (IOException ioe) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "Unable to load synonyms from '" + synonym_loc + "'", ioe);
        }
        finally {
            GeneralUtils.safeClose(syn_stream);
        }
    }
    
    private static void writeHeadersToCache(final File header_cache_file, final Properties headerprops) throws IOException {
        if (LocalUrlCacher.DEBUG_CONNECTION) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.INFO, "writing headers to cache: {0}", header_cache_file.getPath());
        }
        BufferedOutputStream hbos = null;
        try {
            hbos = new BufferedOutputStream(new FileOutputStream(header_cache_file));
            headerprops.store(hbos, null);
        }
        finally {
            GeneralUtils.safeClose(hbos);
        }
    }
    
    public static CacheUsage getCacheUsage(final int usage) {
        for (final CacheUsage u : CacheUsage.values()) {
            if (u.usage == usage) {
                return u;
            }
        }
        return null;
    }
    
    public static File convertURIToFile(final URI uri) {
        return convertURIToFile(uri, false);
    }
    
    public static File convertURIToFile(final URI uri, final boolean fileMayNotExist) {
        if (uri.getScheme() == null) {}
        if (isFile(uri)) {
            final File f = new File(uri);
            if (!GeneralUtils.getUnzippedName(f.getName()).equalsIgnoreCase(f.getName())) {
                try {
                    final File f2 = File.createTempFile(f.getName(), null);
                    f2.deleteOnExit();
                    GeneralUtils.unzipFile(f, f2);
                    return f2;
                }
                catch (IOException ex) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
            return f;
        }
        final String scheme = uri.getScheme().toLowerCase();
        if (scheme.startsWith("http") || scheme.startsWith("ftp")) {
            InputStream istr = null;
            try {
                final String uriStr = uri.toString();
                istr = getInputStream(uriStr, false, null, fileMayNotExist);
                if (istr == null) {
                    return null;
                }
                final StringBuffer stripped_name = new StringBuffer();
                InputStream str = GeneralUtils.unzipStream(istr, uriStr, stripped_name);
                final String stream_name = stripped_name.toString();
                if (!(str instanceof BufferedInputStream)) {
                    str = new BufferedInputStream(str);
                }
                return GeneralUtils.convertStreamToFile(str, stream_name.substring(stream_name.lastIndexOf("/")));
            }
            catch (IOException ex2) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, null, ex2);
            }
            finally {
                GeneralUtils.safeClose(istr);
            }
        }
        Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, "URL scheme: {0} not recognized", scheme);
        return null;
    }
    
    public static BufferedInputStream convertURIToBufferedUnzippedStream(final URI uri) {
        final String scheme = uri.getScheme().toLowerCase();
        InputStream is = null;
        try {
            if (scheme.length() == 0 || scheme.equals("file")) {
                is = new FileInputStream(new File(uri));
            }
            else {
                if (!scheme.startsWith("http") && !scheme.startsWith("ftp")) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, "URL scheme: {0} not recognized", scheme);
                    return null;
                }
                is = getInputStream(uri.toString());
            }
            final StringBuffer stripped_name = new StringBuffer();
            final InputStream str = GeneralUtils.unzipStream(is, uri.toString(), stripped_name);
            if (str instanceof BufferedInputStream) {
                return (BufferedInputStream)str;
            }
            return new BufferedInputStream(str);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static BufferedInputStream convertURIToBufferedStream(final URI uri) {
        final String scheme = uri.getScheme().toLowerCase();
        InputStream is = null;
        try {
            if (scheme.length() == 0 || scheme.equals("file")) {
                is = new FileInputStream(new File(uri));
            }
            else {
                if (!scheme.startsWith("http") && !scheme.startsWith("ftp")) {
                    Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.SEVERE, "URL scheme: {0} not recognized", scheme);
                    return null;
                }
                is = getInputStream(uri.toString());
            }
            if (is instanceof BufferedInputStream) {
                return (BufferedInputStream)is;
            }
            return new BufferedInputStream(is);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static boolean isValidURL(final String url) {
        URI uri = null;
        try {
            uri = new URI(url);
            return isValidURI(uri);
        }
        catch (URISyntaxException ex) {
            Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, null, "Invalid url :" + url);
            return false;
        }
    }
    
    public static boolean isValidURI(final URI uri) {
        final String scheme = uri.getScheme().toLowerCase();
        if (scheme.length() == 0 || scheme.equals("file")) {
            final File f = new File(uri);
            if (f != null && f.exists()) {
                return true;
            }
        }
        if (scheme.startsWith("http") || scheme.startsWith("ftp")) {
            InputStream istr = null;
            URLConnection conn = null;
            try {
                conn = uri.toURL().openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(60000);
                istr = conn.getInputStream();
                if (istr == null) {
                    return false;
                }
                final int bytesRead = istr.read(new byte[1]);
                return bytesRead != -1;
            }
            catch (MalformedURLException ex) {
                Logger.getLogger(LocalUrlCacher.class.getName()).log(Level.WARNING, "Malformed Invalid uri :{0}", uri.toString());
            }
            catch (IOException ex2) {}
            finally {
                GeneralUtils.safeClose(istr);
            }
        }
        return scheme.startsWith("igb");
    }
    
    public static String getReachableUrl(final String urlString) {
        if (urlString.startsWith("file")) {
            final File f = new File(urlString);
            if (f != null && f.exists()) {
                return urlString;
            }
        }
        if (urlString.startsWith("http") || urlString.startsWith("ftp")) {
            final InputStream istr = null;
            URLConnection conn = null;
            try {
                conn = connectToUrl(urlString, null, -1L);
                if (!(conn instanceof HttpURLConnection)) {
                    return urlString;
                }
                String reachable_url = urlString;
                HttpURLConnection hcon = (HttpURLConnection)conn;
                int http_status = hcon.getResponseCode();
                if (http_status == 307) {
                    reachable_url = conn.getHeaderField("Location");
                    conn = handleTemporaryRedirect(conn, urlString, null, -1L);
                    hcon = (HttpURLConnection)conn;
                    http_status = hcon.getResponseCode();
                }
                final boolean url_reachable = http_status >= 200 && http_status < 400;
                if (url_reachable) {
                    return reachable_url;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                GeneralUtils.safeClose(istr);
            }
        }
        return null;
    }
    
    static {
        cache_content_root = PreferenceUtils.getAppDataDirectory() + "cache/";
        cache_header_root = LocalUrlCacher.cache_content_root + "headers/";
        LocalUrlCacher.DEBUG_CONNECTION = false;
        LocalUrlCacher.CACHE_FILE_URLS = false;
        LocalUrlCacher.offline = false;
    }
    
    public enum CacheUsage
    {
        Normal(102), 
        Disabled(100), 
        Offline(101);
        
        public final int usage;
        
        private CacheUsage(final int usage) {
            this.usage = usage;
        }
    }
    
    public enum CacheOption
    {
        IGNORE, 
        ONLY, 
        NORMAL;
    }
}
