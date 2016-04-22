// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import java.io.Closeable;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.UUID;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.BufferedReader;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class USeqUtilities
{
    public static final String BYTE = "b";
    public static final String SHORT = "s";
    public static final String INT = "i";
    public static final String FLOAT = "f";
    public static final String DOUBLE = "d";
    public static final String TEXT = "t";
    public static final String BOOLEAN = "o";
    public static final String USEQ_EXTENSION_NO_PERIOD = "useq";
    public static final String USEQ_EXTENSION_WITH_PERIOD = ".useq";
    public static final Pattern POSITION;
    public static final Pattern POSITION_INT;
    public static final Pattern POSITION_SHORT;
    public static final Pattern POSITION_SCORE;
    public static final Pattern POSITION_SCORE_INT_FLOAT;
    public static final Pattern POSITION_SCORE_SHORT_FLOAT;
    public static final Pattern POSITION_TEXT;
    public static final Pattern POSITION_TEXT_INT_TEXT;
    public static final Pattern POSITION_TEXT_SHORT_TEXT;
    public static final Pattern POSITION_SCORE_TEXT;
    public static final Pattern POSITION_SCORE_TEXT_INT_FLOAT_TEXT;
    public static final Pattern POSITION_SCORE_TEXT_SHORT_FLOAT_TEXT;
    public static final Pattern REGION;
    public static final Pattern REGION_INT_INT;
    public static final Pattern REGION_INT_SHORT;
    public static final Pattern REGION_SHORT_INT;
    public static final Pattern REGION_SHORT_SHORT;
    public static final Pattern REGION_SCORE;
    public static final Pattern REGION_SCORE_INT_INT_FLOAT;
    public static final Pattern REGION_SCORE_INT_SHORT_FLOAT;
    public static final Pattern REGION_SCORE_SHORT_INT_FLOAT;
    public static final Pattern REGION_SCORE_SHORT_SHORT_FLOAT;
    public static final Pattern REGION_TEXT;
    public static final Pattern REGION_TEXT_INT_INT_TEXT;
    public static final Pattern REGION_TEXT_INT_SHORT_TEXT;
    public static final Pattern REGION_TEXT_SHORT_INT_TEXT;
    public static final Pattern REGION_TEXT_SHORT_SHORT_TEXT;
    public static final Pattern REGION_SCORE_TEXT;
    public static final Pattern REGION_SCORE_TEXT_INT_INT_FLOAT_TEXT;
    public static final Pattern REGION_SCORE_TEXT_INT_SHORT_FLOAT_TEXT;
    public static final Pattern REGION_SCORE_TEXT_SHORT_INT_FLOAT_TEXT;
    public static final Pattern REGION_SCORE_TEXT_SHORT_SHORT_FLOAT_TEXT;
    public static final Pattern USEQ_ARCHIVE;
    public static final List<String> USEQ_FORMATS;
    
    public static int fixBedScore(final float value) {
        int score = Math.round(value);
        if (score < 0) {
            score = 0;
        }
        else if (score > 1000) {
            score = 1000;
        }
        return score;
    }
    
    public static void printErrAndExit(final String message) {
        System.err.println(message);
        System.exit(0);
    }
    
    public static String removeExtension(final String txt) {
        final int index = txt.lastIndexOf(".");
        if (index != -1) {
            return txt.substring(0, index);
        }
        return txt;
    }
    
    public static String stringArrayToString(final String[] s, final String separator) {
        if (s == null) {
            return "";
        }
        final int len = s.length;
        if (len == 1) {
            return s[0];
        }
        if (len == 0) {
            return "";
        }
        final StringBuffer sb = new StringBuffer(s[0]);
        for (int i = 1; i < len; ++i) {
            sb.append(separator);
            sb.append(s[i]);
        }
        return sb.toString();
    }
    
    public static BufferedReader fetchBufferedReader(final File txtFile) {
        BufferedReader in = null;
        try {
            final String name = txtFile.getName().toLowerCase();
            if (name.endsWith(".zip")) {
                final ZipFile zf = new ZipFile(txtFile);
                final ZipEntry ze = (ZipEntry)zf.entries().nextElement();
                in = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
            }
            else if (name.endsWith(".gz")) {
                in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(txtFile))));
            }
            else {
                in = new BufferedReader(new FileReader(txtFile));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            safeClose(in);
        }
        return in;
    }
    
    public static void printExit(final String message) {
        System.out.println(message);
        System.exit(0);
    }
    
    public static int[] stringArrayToInts(final String s, final String delimiter) throws NumberFormatException {
        final String[] tokens = s.split(delimiter);
        final int[] num = new int[tokens.length];
        for (int i = 0; i < tokens.length; ++i) {
            num[i] = Integer.parseInt(tokens[i]);
        }
        return num;
    }
    
    public static File[] extractFiles(final File directory) {
        File[] files = null;
        if (directory.isDirectory()) {
            final String[] fileNames = directory.list();
            final int num = fileNames.length;
            final ArrayList<File> al = new ArrayList<File>();
            try {
                final String path = directory.getCanonicalPath();
                final Pattern pat = Pattern.compile("^\\w+.*");
                for (int i = 0; i < num; ++i) {
                    final Matcher mat = pat.matcher(fileNames[i]);
                    if (mat.matches()) {
                        al.add(new File(path, fileNames[i]));
                    }
                }
                if (al.size() != 0) {
                    files = new File[al.size()];
                    al.toArray(files);
                }
            }
            catch (IOException e) {
                System.out.println("Problem extractFiles() " + directory);
                e.printStackTrace();
                return null;
            }
        }
        if (files == null) {
            files = new File[] { directory };
        }
        Arrays.sort(files);
        return files;
    }
    
    public static File[] fetchFilesRecursively(final File directory, final String extension) {
        if (!directory.isDirectory()) {
            return extractFiles(directory, extension);
        }
        final ArrayList<File> al = fetchAllFilesRecursively(directory, extension);
        final File[] files = new File[al.size()];
        al.toArray(files);
        return files;
    }
    
    public static ArrayList<File> fetchAllFilesRecursively(final File directory, final String extension) {
        final ArrayList<File> files = new ArrayList<File>();
        final File[] list = directory.listFiles();
        for (int i = 0; i < list.length; ++i) {
            if (list[i].isDirectory()) {
                final ArrayList<File> al = fetchAllFilesRecursively(list[i], extension);
                files.addAll(al);
            }
            else if (list[i].getName().endsWith(extension)) {
                files.add(list[i]);
            }
        }
        return files;
    }
    
    public static File[] extractFiles(final File dirOrFile, final String extension) {
        if (dirOrFile == null) {
            return null;
        }
        File[] files = null;
        final Pattern p = Pattern.compile(".*" + extension + "$", 2);
        if (dirOrFile.isDirectory()) {
            files = dirOrFile.listFiles();
            final int num = files.length;
            final ArrayList<File> chromFiles = new ArrayList<File>();
            for (int i = 0; i < num; ++i) {
                final Matcher m = p.matcher(files[i].getName());
                if (m.matches()) {
                    chromFiles.add(files[i]);
                }
            }
            files = new File[chromFiles.size()];
            chromFiles.toArray(files);
        }
        else {
            final Matcher m = p.matcher(dirOrFile.getName());
            if (m.matches()) {
                files = new File[] { dirOrFile };
            }
        }
        if (files != null) {
            Arrays.sort(files);
        }
        return files;
    }
    
    public static File makeDirectory(final File file, final String extension) {
        String name = file.getName();
        name = capitalizeFirstLetter(name);
        name = name.replace(".gz", "");
        name = name.replace(".zip", "");
        name = removeExtension(name);
        final File dir = new File(file.getParentFile(), name + extension);
        dir.mkdir();
        return dir;
    }
    
    public static String capitalizeFirstLetter(final String s) {
        final char[] first = s.toCharArray();
        if (Character.isLetter(first[0])) {
            first[0] = Character.toUpperCase(first[0]);
            return new String(first);
        }
        return s;
    }
    
    public static boolean deleteDirectory(final File directory) {
        if (directory == null) {
            return false;
        }
        final File[] files = directory.listFiles();
        for (int num = files.length, i = 0; i < num; ++i) {
            if (files[i].isDirectory()) {
                if (!deleteDirectory(files[i])) {
                    return false;
                }
            }
            else if (!files[i].delete()) {
                return false;
            }
        }
        return directory.delete();
    }
    
    public static boolean zip(final File[] filesToZip, final File zipFile) {
        final byte[] buf = new byte[2048];
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < filesToZip.length; ++i) {
                in = new FileInputStream(filesToZip[i]);
                out.putNextEntry(new ZipEntry(filesToZip[i].getName()));
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        }
        catch (IOException e) {
            System.err.println("Can't zip()");
            e.printStackTrace();
            safeClose(out);
            safeClose(in);
            return false;
        }
        return true;
    }
    
    public static File[] collapseFileArray(final File[][] f) {
        final ArrayList<File> al = new ArrayList<File>();
        for (int i = 0; i < f.length; ++i) {
            if (f[i] != null) {
                for (int j = 0; j < f[i].length; ++j) {
                    al.add(f[i][j]);
                }
            }
        }
        final File[] files = new File[al.size()];
        al.toArray(files);
        return files;
    }
    
    public static int calculateMiddleIntergenicCoordinates(final int start, final int end) {
        if (start == end) {
            return start;
        }
        final double length = end - start;
        final double halfLength = length / 2.0;
        return (int)Math.round(halfLength) + start;
    }
    
    public static String convertHexadecimal2RGB(final String hexColor, final String divider) {
        final String hex = hexColor.replaceFirst("#", "");
        if (hex.length() != 6) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        String toCon = hex.substring(0, 2);
        int color = Integer.parseInt(toCon, 16);
        sb.append(color);
        for (int i = 2; i < 6; i += 2) {
            toCon = hex.substring(i, i + 2);
            color = Integer.parseInt(toCon, 16);
            sb.append(divider);
            sb.append(color);
        }
        return sb.toString();
    }
    
    public static String[] executeCommandLineReturnAll(final String[] command) {
        final ArrayList<String> al = new ArrayList<String>();
        try {
            final Runtime rt = Runtime.getRuntime();
            rt.traceInstructions(true);
            rt.traceMethodCalls(true);
            final Process p = rt.exec(command);
            final BufferedReader data = new BufferedReader(new InputStreamReader(p.getInputStream()));
            final BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = data.readLine()) != null) {
                al.add(line);
            }
            while ((line = error.readLine()) != null) {
                al.add(line);
            }
            data.close();
            error.close();
        }
        catch (Exception e) {
            System.out.println("Problem executing -> " + stringArrayToString(command, " ") + " " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
        final String[] res = new String[al.size()];
        al.toArray(res);
        return res;
    }
    
    public static String[] executeShellScript(final String shellScript, final File tempDirectory) {
        final File shellFile = new File(tempDirectory, "tempFile_" + UUID.randomUUID() + ".sh");
        final String fullPath = getFullPathName(shellFile);
        writeString(shellScript, shellFile);
        String[] cmd = { "chmod", "777", fullPath };
        String[] res = executeCommandLineReturnAll(cmd);
        if (res == null || res.length != 0) {
            shellFile.delete();
            return null;
        }
        cmd = new String[] { fullPath };
        res = executeCommandLineReturnAll(cmd);
        shellFile.delete();
        return res;
    }
    
    public static boolean writeString(final String data, final File file) {
        try {
            final PrintWriter out = new PrintWriter(new FileWriter(file));
            out.print(data);
            out.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("Problem writing String to disk!");
            e.printStackTrace();
            return false;
        }
    }
    
    public static String getFullPathName(final File fileDirectory) {
        String full = null;
        try {
            full = fileDirectory.getCanonicalPath();
        }
        catch (IOException e) {
            System.out.println("Problem with getFullPathtName(), " + fileDirectory);
            e.printStackTrace();
        }
        return full;
    }
    
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
    
    static {
        POSITION = Pattern.compile("[is]");
        POSITION_INT = Pattern.compile("i");
        POSITION_SHORT = Pattern.compile("s");
        POSITION_SCORE = Pattern.compile("[is]f");
        POSITION_SCORE_INT_FLOAT = Pattern.compile("if");
        POSITION_SCORE_SHORT_FLOAT = Pattern.compile("sf");
        POSITION_TEXT = Pattern.compile("[is]t");
        POSITION_TEXT_INT_TEXT = Pattern.compile("it");
        POSITION_TEXT_SHORT_TEXT = Pattern.compile("st");
        POSITION_SCORE_TEXT = Pattern.compile("[is]ft");
        POSITION_SCORE_TEXT_INT_FLOAT_TEXT = Pattern.compile("ift");
        POSITION_SCORE_TEXT_SHORT_FLOAT_TEXT = Pattern.compile("sft");
        REGION = Pattern.compile("[is]{2}");
        REGION_INT_INT = Pattern.compile("ii");
        REGION_INT_SHORT = Pattern.compile("is");
        REGION_SHORT_INT = Pattern.compile("si");
        REGION_SHORT_SHORT = Pattern.compile("ss");
        REGION_SCORE = Pattern.compile("[is]{2}f");
        REGION_SCORE_INT_INT_FLOAT = Pattern.compile("iif");
        REGION_SCORE_INT_SHORT_FLOAT = Pattern.compile("isf");
        REGION_SCORE_SHORT_INT_FLOAT = Pattern.compile("sif");
        REGION_SCORE_SHORT_SHORT_FLOAT = Pattern.compile("ssf");
        REGION_TEXT = Pattern.compile("[is]{2}t");
        REGION_TEXT_INT_INT_TEXT = Pattern.compile("iit");
        REGION_TEXT_INT_SHORT_TEXT = Pattern.compile("ist");
        REGION_TEXT_SHORT_INT_TEXT = Pattern.compile("sit");
        REGION_TEXT_SHORT_SHORT_TEXT = Pattern.compile("sst");
        REGION_SCORE_TEXT = Pattern.compile("[is]{2}ft");
        REGION_SCORE_TEXT_INT_INT_FLOAT_TEXT = Pattern.compile("iift");
        REGION_SCORE_TEXT_INT_SHORT_FLOAT_TEXT = Pattern.compile("isft");
        REGION_SCORE_TEXT_SHORT_INT_FLOAT_TEXT = Pattern.compile("sift");
        REGION_SCORE_TEXT_SHORT_SHORT_FLOAT_TEXT = Pattern.compile("ssft");
        USEQ_ARCHIVE = Pattern.compile("(.+)\\.(useq)$");
        (USEQ_FORMATS = new ArrayList<String>()).add("useq");
    }
}
