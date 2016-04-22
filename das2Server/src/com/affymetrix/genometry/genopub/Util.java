//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.text.ParseException;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import java.nio.MappedByteBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Set;

public class Util
{
    private static SimpleDateFormat dateFormat;
    private static final double KB;
    public static final Pattern toStripURL;

    public static String stripBadURLChars(final String name, final String replaceWith) {
        return Util.toStripURL.matcher(name).replaceAll(replaceWith);
    }

    public static boolean copy(final File source, final File dest) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            final long size = in.size();
            final MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0L, size);
            out.write(buf);
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteNonIndexFiles(final File directory, final long days) {
        long cutoff = days * 24L * 60L * 1000L * 60L;
        final long current = System.currentTimeMillis();
        cutoff = current - cutoff;
        final File[] arr$;
        final File[] files = arr$ = directory.listFiles();
        for (final File f : arr$) {
            if (!f.getName().equals("index.html")) {
                if (f.lastModified() < cutoff) {
                    deleteDirectory(f);
                }
            }
        }
    }

    public static void deleteDirectory(final File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            final File[] children = dir.listFiles();
            for (int i = 0; i < children.length; ++i) {
                deleteDirectory(children[i]);
            }
            dir.delete();
        }
        dir.delete();
    }

    public static boolean makeSoftLinkViaUNIXCommandLine(final File realFile, final File link) {
        try {
            final String[] cmd = { "ln", "-s", realFile.getCanonicalPath(), link.toString() };
            Runtime.getRuntime().exec(cmd);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer getIntegerParameter(final HttpServletRequest req, final String parameterName) {
        if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
            return new Integer(req.getParameter(parameterName));
        }
        return null;
    }

    public static Date getDateParameter(final HttpServletRequest req, final String parameterName) {
        if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
            try {
                return parseDate(req.getParameter(parameterName));
            }
            catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    public static String getFlagParameter(final HttpServletRequest req, final String parameterName) {
        if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
            return req.getParameter(parameterName);
        }
        return "Y";
    }

    public static boolean fileHasSegmentName(final String fileName, final GenomeVersion genomeVersion) {
        if (genomeVersion.getSegments() == null || genomeVersion.getSegments().size() == 0) {
            return true;
        }
        boolean isValid = false;
        for (final Segment segment : (Set<Segment>) genomeVersion.getSegments()) {
            final String[] fileParts = fileName.split("\\.");
            if (fileParts.length == 2 && fileParts[0].equalsIgnoreCase(segment.getName())) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    public static HashSet<String> getChromosmeSegmentNames(final GenomeVersion genomeVersion) {
        final HashSet<String> chroms = new HashSet<String>();
        for (final Segment segment : (Set<Segment>)genomeVersion.getSegments()) {
            chroms.add(segment.getName());
        }
        return chroms;
    }

    public static boolean isValidAnnotationFileType(final String fileName) {
        boolean isValid = false;
        for (int x = 0; x < Constants.ANNOTATION_FILE_EXTENSIONS.length; ++x) {
            if (fileName.toUpperCase().endsWith(Constants.ANNOTATION_FILE_EXTENSIONS[x].toUpperCase())) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    public static boolean isValidSequenceFileType(final String fileName) {
        boolean isValid = false;
        for (int x = 0; x < Constants.SEQUENCE_FILE_EXTENSIONS.length; ++x) {
            if (fileName.toUpperCase().endsWith(Constants.SEQUENCE_FILE_EXTENSIONS[x].toUpperCase())) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    public static String formatDate(final Date date) {
        return Util.dateFormat.format(date);
    }

    public static Date parseDate(final String date) throws ParseException {
        return new Date(Util.dateFormat.parse(date).getTime());
    }

    public static long getKilobytes(final long bytes) {
        long kb = Math.round(bytes / Util.KB);
        if (kb == 0L) {
            kb = 1L;
        }
        return kb;
    }

    public static String removeHTMLTags(String buf) {
        if (buf != null) {
            buf = buf.replaceAll("<(.|\n)+?>", " ");
            buf = escapeHTML(buf);
        }
        return buf;
    }

    public static String escapeHTML(String buf) {
        if (buf != null) {
            buf = buf.replaceAll("&", "&amp;");
            buf = buf.replaceAll("<", "&lt;");
            buf = buf.replaceAll(">", "&gt;");
            buf = buf.replaceAll("\"", "'");
        }
        return buf;
    }

    public static boolean tooManyLines(final File file) throws IOException {
        final String lcName = file.getName().toLowerCase();
        for (int i = 0; i < Constants.FILE_EXTENSIONS_TO_CHECK_SIZE_BEFORE_UPLOADING.length; ++i) {
            if (lcName.endsWith(Constants.FILE_EXTENSIONS_TO_CHECK_SIZE_BEFORE_UPLOADING[i])) {
                int counter = 0;
                final BufferedReader in = new BufferedReader(new FileReader(file));
                while (in.readLine() != null) {
                    if (counter > 10000) {
                        in.close();
                        return true;
                    }
                    ++counter;
                }
                in.close();
                return false;
            }
        }
        return false;
    }

    static {
        Util.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        KB = Math.pow(2.0, 10.0);
        toStripURL = Pattern.compile("[^a-zA-Z_0-9\\./]");
    }
}
