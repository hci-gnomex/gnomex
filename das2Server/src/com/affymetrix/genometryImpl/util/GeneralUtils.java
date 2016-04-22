// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.ByteArrayOutputStream;
import java.util.prefs.Preferences;
import com.affymetrix.genometryImpl.general.GenericVersion;
import java.util.Set;
import org.broad.tribble.util.SeekableStream;
import org.broad.tribble.util.SeekableStreamFactory;
import java.net.HttpURLConnection;
import net.sf.samtools.util.BlockCompressedInputStream;
import java.util.zip.ZipException;
import java.util.zip.GZIPInputStream;
import com.affymetrix.genometryImpl.general.GenericFeature;
import java.util.Collection;
import java.net.URISyntaxException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.net.URLConnection;
import java.awt.image.BufferedImage;
import java.awt.Image;
import net.sf.image4j.codec.ico.ICOImage;
import net.sf.image4j.codec.ico.ICODecoder;
import java.net.URL;
import javax.swing.ImageIcon;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.Closeable;
import java.util.regex.Pattern;

public final class GeneralUtils
{
    public static final String UTF8 = "UTF-8";
    private static final Pattern CLEAN;
    public static final String[] compression_endings;
    private static final double COMPRESSION_RATIO = 3.5;
    private static final SynonymLookup LOOKUP;
    
    public static <S extends Closeable> void safeClose(final S s) {
        if (s == null) {
            return;
        }
        try {
            s.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String stripEndings(final String name) {
        for (int i = 0; i < GeneralUtils.compression_endings.length; ++i) {
            final String ending = GeneralUtils.compression_endings[i].toLowerCase();
            if (name.toLowerCase().endsWith(ending)) {
                final String stripped_name = name.substring(0, name.lastIndexOf(46));
                return stripEndings(stripped_name);
            }
        }
        return name;
    }
    
    public static InputStream getInputStream(final File f, final StringBuffer sb) throws FileNotFoundException, IOException {
        final String infile_name = "file:" + f.getAbsolutePath();
        final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
        final InputStream isr = unzipStream(bis, infile_name, sb);
        return isr;
    }
    
    public static InputStream unzipStream(final InputStream istr, final String stream_name, final StringBuffer stripped_name) throws IOException {
        final String lc_stream_name = stream_name.toLowerCase();
        if (lc_stream_name.endsWith(".gz") || lc_stream_name.endsWith(".gzip") || lc_stream_name.endsWith(".z")) {
            final InputStream gzstr = getGZipInputStream(stream_name, istr);
            final String new_name = stream_name.substring(0, stream_name.lastIndexOf(46));
            return unzipStream(gzstr, new_name, stripped_name);
        }
        if (stream_name.endsWith(".zip")) {
            final ZipInputStream zstr = new ZipInputStream(istr);
            zstr.getNextEntry();
            final String new_name = stream_name.substring(0, stream_name.lastIndexOf(46));
            return unzipStream(zstr, new_name, stripped_name);
        }
        stripped_name.append(stream_name);
        return istr;
    }
    
    public static String getUnzippedName(final String stream_name) {
        final String lc_stream_name = stream_name.toLowerCase();
        if (lc_stream_name.endsWith(".gz") || lc_stream_name.endsWith(".gzip") || lc_stream_name.endsWith(".z")) {
            return stream_name.substring(0, stream_name.lastIndexOf(46));
        }
        if (stream_name.endsWith(".zip")) {
            return stream_name.substring(0, stream_name.lastIndexOf(46));
        }
        return stream_name;
    }
    
    public static String convertStreamNameToValidURLName(String streamName) {
        final int httpIndex = streamName.indexOf("http:");
        if (httpIndex > -1) {
            streamName = streamName.substring(httpIndex + 5);
        }
        for (int streamNameLen = streamName.length(), i = 0; i < streamNameLen; ++i) {
            if (streamName.startsWith("/")) {
                streamName = streamName.substring(1);
            }
        }
        if (streamName.endsWith("/")) {
            streamName = streamName.substring(0, streamName.length() - 1);
        }
        return "http://" + streamName;
    }
    
    public static File convertStreamToFile(final InputStream istr, final String streamName) {
        OutputStream out = null;
        final FileInputStream fis = null;
        try {
            final String unzippedStreamName = stripEndings(streamName);
            final String extension = ParserController.getExtension(unzippedStreamName);
            final File f = File.createTempFile(GeneralUtils.CLEAN.matcher(unzippedStreamName).replaceAll("_"), extension);
            f.deleteOnExit();
            out = new FileOutputStream(f);
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = istr.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            return f;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
            safeClose(out);
            safeClose(fis);
        }
    }
    
    public static ImageIcon determineFriendlyIcon(final String iconString) {
        URL iconURL = null;
        try {
            iconURL = new URL(iconString);
        }
        catch (Exception ex2) {}
        if (iconURL == null) {
            return null;
        }
        BufferedImage icon = null;
        URLConnection conn = null;
        List<ICOImage> icoImages = null;
        try {
            conn = iconURL.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            if (conn.getInputStream() == null) {
                return null;
            }
            icoImages = (List<ICOImage>)ICODecoder.readExt(conn.getInputStream());
        }
        catch (Exception ex) {
            return null;
        }
        if (icoImages == null) {
            return null;
        }
        int maxColorDepth = 0;
        for (final ICOImage icoImage : icoImages) {
            final int colorDepth = icoImage.getColourDepth();
            final int width = icoImage.getWidth();
            if (width == 16 && maxColorDepth < colorDepth) {
                icon = icoImage.getImage();
                maxColorDepth = colorDepth;
            }
        }
        if (icon == null && !icoImages.isEmpty()) {
            icon = icoImages.get(0).getImage();
        }
        ImageIcon friendlyIcon = null;
        try {
            if (icon != null) {
                friendlyIcon = new ImageIcon(icon);
            }
        }
        catch (Exception ex3) {}
        return friendlyIcon;
    }
    
    public static String URLEncode(final String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
    }
    
    public static String URLDecode(final String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
    }
    
    public static void browse(final String s) {
        try {
            final URI u = new URI(s);
            if ("file".equalsIgnoreCase(u.getScheme())) {
                Desktop.getDesktop().open(new File(u));
                return;
            }
            Desktop.getDesktop().browse(u);
        }
        catch (IOException ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (URISyntaxException ex2) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex2);
        }
    }
    
    public static void writeFileToStream(final File f, final OutputStream dos) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            final byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            safeClose(is);
        }
    }
    
    public static GenericFeature findFeatureWithURI(final Collection<GenericFeature> features, final URI uri) {
        if (uri == null || features.isEmpty()) {
            return null;
        }
        for (final GenericFeature feature : features) {
            if (uri.equals(feature.getURI())) {
                return feature;
            }
        }
        return null;
    }
    
    public static boolean moveFileTo(final File file, final String fileName, final String path) {
        final File newLocation = new File(path + "/" + fileName);
        final boolean sucess = file.renameTo(newLocation);
        if (!sucess) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, "Could not find move file {0} to {1} !!!", new Object[] { fileName, path });
        }
        return sucess;
    }
    
    public static boolean copyFileTo(final File file, final String fileName, final String path) {
        try {
            final File newLocation = new File(path + "/" + fileName);
            if (!newLocation.createNewFile()) {
                Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, "Could not find copy file from {0} to {1} !!!", new Object[] { fileName, path });
                return false;
            }
            unzipFile(file, newLocation);
            return true;
        }
        catch (IOException ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static File makeDir(final String path) {
        final File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
    
    public static File getFile(final String path, final boolean fileMayNotExist) {
        File file = null;
        try {
            file = LocalUrlCacher.convertURIToFile(URI.create(path), fileMayNotExist);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (file == null && !fileMayNotExist) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, "Invalid path : {0} !!!", path);
        }
        return file;
    }
    
    public static InputStream getGZipInputStream(final String url, final InputStream istr) throws IOException {
        InputStream gzstr = null;
        boolean blockedGZip = false;
        GZIPInputStream gis = null;
        try {
            final URLConnection conn = LocalUrlCacher.connectToUrl(url, null, -1L);
            gis = new GZIPInputStream(conn.getInputStream());
            gis.read();
        }
        catch (ZipException x) {
            blockedGZip = true;
        }
        catch (Exception x2) {
            blockedGZip = true;
        }
        finally {
            try {
                gis.close();
            }
            catch (Exception ex) {}
        }
        if (blockedGZip) {
            gzstr = (InputStream)new BlockCompressedInputStream(istr);
        }
        else {
            gzstr = new GZIPInputStream(istr);
        }
        return gzstr;
    }
    
    public static void unzipFile(final File f, final File f2) throws IOException {
        InputStream is = null;
        OutputStream out = null;
        try {
            is = getInputStream(f, new StringBuffer());
            out = new FileOutputStream(f2);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
        finally {
            safeClose(is);
            safeClose(out);
        }
    }
    
    private static boolean httpExists(final String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            final HttpURLConnection con = (HttpURLConnection)new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return con.getResponseCode() == 200;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String getFileScheme() {
        final String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("windows")) {
            return "file:/";
        }
        return "file:";
    }
    
    public static String fixFileName(final String fileName) {
        if (fileName.startsWith("http:/") || fileName.startsWith("https:/")) {
            return fileName;
        }
        String fixedFileName = fileName;
        if (fileName.startsWith("file:/")) {
            fixedFileName = fileName.substring(getFileScheme().length());
        }
        else if (fileName.startsWith("file:")) {
            fixedFileName = fileName.substring("file:".length());
        }
        return URLDecode(fixedFileName);
    }
    
    public static long getUriLength(final URI uri) {
        long uriLength = -1L;
        try {
            final SeekableStream seekableStream = SeekableStreamFactory.getStreamFor(fixFileName(uri.toString()));
            uriLength = seekableStream.length();
            seekableStream.close();
            if (uri.toString().toLowerCase().endsWith(".gz") || uri.toString().toLowerCase().endsWith(".zip")) {
                uriLength *= (long)3.5;
            }
        }
        catch (IOException x) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, "can't get length of uri " + uri);
        }
        return uriLength;
    }
    
    public static boolean urlExists(final String url) {
        if (url == null) {
            return false;
        }
        if (url.startsWith("http:")) {
            return httpExists(url);
        }
        final File f = new File(fixFileName(url));
        return f.exists();
    }
    
    public static String getPreferredVersionName(final Set<GenericVersion> gVersions) {
        return GeneralUtils.LOOKUP.getPreferredName(gVersions.iterator().next().versionName);
    }
    
    public static String preferencesDisplay(final Preferences prefs) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            prefs.exportSubtree(baos);
        }
        catch (Exception x) {
            return x.getClass().getSimpleName() + " " + x.getMessage();
        }
        return baos.toString();
    }
    
    static {
        CLEAN = Pattern.compile("[/\\s+]");
        compression_endings = new String[] { ".z", ".gzip", ".gz", ".zip" };
        LOOKUP = SynonymLookup.getDefaultLookup();
    }
}
