// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ArchiveInfo
{
    public static final String ARCHIVE_VERSION_KEY = "useqArchiveVersion";
    public static final String ARCHIVE_VERSION_VALUE_ONE = "1.0";
    public static final String ARCHIVE_README_NAME = "archiveReadMe.txt";
    public static final String VERSIONED_GENOME_KEY = "versionedGenome";
    public static final Pattern DAS2_VERSIONED_GENOME_FORM;
    public static final String DATA_TYPE_KEY = "dataType";
    public static final String DATA_TYPE_VALUE_GRAPH = "graph";
    public static final String DATA_TYPE_VALUE_REGION = "region";
    public static final String ORIGINATING_DATA_SOURCE_KEY = "originatingDataSource";
    public static final String GRAPH_STYLE_KEY = "initialGraphStyle";
    public static final String GRAPH_STYLE_VALUE_BAR = "Bar";
    public static final String GRAPH_STYLE_VALUE_DOT = "Dot";
    public static final String GRAPH_STYLE_VALUE_LINE = "Line";
    public static final String GRAPH_STYLE_VALUE_MINMAXAVE = "Min_Max_Ave";
    public static final String GRAPH_STYLE_VALUE_STAIRSTEP = "Stairstep";
    public static final String GRAPH_STYLE_VALUE_HEATMAP = "HeatMap";
    public static final String COLOR_KEY = "initialColor";
    public static final String BACKGROUND_COLOR_KEY = "initialBackground";
    public static final Pattern COLOR_HEX_FORM;
    public static final String MIN_Y_KEY = "initialMinY";
    public static final String MAX_Y_KEY = "initialMaxY";
    public static final String DESCRIPTION_KEY = "description";
    public static final String UNIT_KEY = "units";
    public static final String ARCHIVE_CREATION_DATE = "archiveCreationDate";
    private String[] commentLines;
    private LinkedHashMap<String, String> keyValues;
    public static final Pattern KEY_VALUE_SPLITTER;
    
    public ArchiveInfo(final String versionedGenome, final String dataType) {
        this.commentLines = null;
        this.keyValues = null;
        (this.keyValues = new LinkedHashMap<String, String>()).put("useqArchiveVersion", "1.0");
        this.keyValues.put("archiveCreationDate", new Date().toString());
        this.keyValues.put("dataType", dataType);
        this.keyValues.put("versionedGenome", versionedGenome);
        if (!ArchiveInfo.DAS2_VERSIONED_GENOME_FORM.matcher(versionedGenome).matches()) {
            System.err.println("WARNING: Versioned genome does not follow recommended form (e.g. H_sapiens_Mar_2006) correct -> " + versionedGenome);
        }
    }
    
    public ArchiveInfo(final File readMeTxtFile) throws IOException {
        this.commentLines = null;
        this.keyValues = null;
        this.loadTextArchiveReadMeFile(readMeTxtFile);
        if (!this.keyValues.containsKey("useqArchiveVersion") || !this.keyValues.containsKey("versionedGenome") || !this.keyValues.containsKey("dataType")) {
            throw new IOException("Error: text archiveReadMe.txt file does not contain required keys.  Add 'useqArchiveVersion' and or 'versionedGenome' and or 'dataType' to " + readMeTxtFile);
        }
        if (!this.keyValues.get("useqArchiveVersion").equals("1.0")) {
            throw new IOException("Error: this ArchiveInfo parser only supports useqArchiveVersion = 1.0");
        }
    }
    
    public ArchiveInfo(final InputStream is, final boolean closeStreams) {
        this.commentLines = null;
        this.keyValues = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            this.loadTextArchiveReadMeFile(br);
        }
        catch (Exception e) {
            e.printStackTrace();
            USeqUtilities.safeClose(br);
            USeqUtilities.safeClose(isr);
        }
        finally {
            if (closeStreams) {
                USeqUtilities.safeClose(br);
                USeqUtilities.safeClose(isr);
            }
        }
    }
    
    public static ArchiveInfo fetchArchiveInfo(final File useqArchive, final boolean closeStream) {
        InputStream is = null;
        ArchiveInfo ai = null;
        try {
            if (!USeqUtilities.USEQ_ARCHIVE.matcher(useqArchive.getName()).matches()) {
                return null;
            }
            final ZipFile zf = new ZipFile(useqArchive);
            final Enumeration<? extends ZipEntry> e = zf.entries();
            final ZipEntry ze = (ZipEntry)e.nextElement();
            is = zf.getInputStream(ze);
            ai = new ArchiveInfo(is, closeStream);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        finally {
            if (closeStream) {
                USeqUtilities.safeClose(is);
            }
        }
        return ai;
    }
    
    public File writeReadMeFile(final File saveDirectory) {
        this.keyValues.put("archiveCreationDate", new Date().toString());
        PrintWriter out = null;
        try {
            final File readme = new File(saveDirectory, "archiveReadMe.txt");
            out = new PrintWriter(new FileWriter(readme));
            if (this.commentLines != null) {
                for (int i = 0; i < this.commentLines.length; ++i) {
                    out.println(this.commentLines[i]);
                }
                out.println();
            }
            for (final String key : this.keyValues.keySet()) {
                final String value = this.keyValues.get(key);
                out.println(key + " = " + value);
            }
            return readme;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            USeqUtilities.safeClose(out);
        }
    }
    
    public void appendCommentedKeyValues(final PrintWriter out) {
        if (this.commentLines != null) {
            for (int i = 0; i < this.commentLines.length; ++i) {
                out.println(this.commentLines[i]);
            }
            out.println();
        }
        for (final String key : this.keyValues.keySet()) {
            final String value = this.keyValues.get(key);
            out.println("# " + key + " = " + value);
        }
    }
    
    public void loadTextArchiveReadMeFile(final BufferedReader in) {
        try {
            this.keyValues = new LinkedHashMap<String, String>();
            final ArrayList<String> comments = new ArrayList<String>();
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("#")) {
                    comments.add(line);
                }
                else {
                    final Matcher mat = ArchiveInfo.KEY_VALUE_SPLITTER.matcher(line);
                    if (!mat.matches()) {
                        throw new IOException("Error in parsing archiveReadMe.txt file. Found a non comment and non key = value line. Bad line -> '" + line);
                    }
                    this.keyValues.put(mat.group(1), mat.group(2));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(in);
        }
    }
    
    public void loadTextArchiveReadMeFile(final File readMeTxt) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(readMeTxt);
            br = new BufferedReader(fr);
            this.loadTextArchiveReadMeFile(br);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(fr);
            USeqUtilities.safeClose(br);
        }
    }
    
    public void setArchiveVersion(final String archiveVersion) {
        this.keyValues.put("useqArchiveVersion", archiveVersion);
    }
    
    public void setOriginatingDataSource(final String originatingDataSource) {
        this.keyValues.put("originatingDataSource", originatingDataSource);
    }
    
    public void setDataType(final String dataType) {
        this.keyValues.put("dataType", dataType);
    }
    
    public void setInitialGraphStyle(final String initialGraphStyle) {
        this.keyValues.put("initialGraphStyle", initialGraphStyle);
    }
    
    public void setInitialColor(final String initialColor) throws IOException {
        if (!ArchiveInfo.COLOR_HEX_FORM.matcher(initialColor).matches()) {
            throw new IOException("Error: initial color does not follow hex form (e.g. #B2B300)! " + initialColor);
        }
        this.keyValues.put("initialColor", initialColor);
    }
    
    public void setInitialBackgroundColor(final String initialBackgroundColor) throws IOException {
        if (!ArchiveInfo.COLOR_HEX_FORM.matcher(initialBackgroundColor).matches()) {
            throw new IOException("Error: initial background color does not follow hex form (e.g. #B2B300)! " + initialBackgroundColor);
        }
        this.keyValues.put("initialBackground", initialBackgroundColor);
    }
    
    public void setInitialMinY(final String initialMinY) {
        this.keyValues.put("initialMinY", initialMinY);
    }
    
    public void setInitialMaxY(final String initialMaxY) {
        this.keyValues.put("initialMaxY", initialMaxY);
    }
    
    public void setDescription(final String description) {
        this.keyValues.put("description", description);
    }
    
    public void setUnits(final String units) {
        this.keyValues.put("units", units);
    }
    
    public String[] getCommentLines() {
        return this.commentLines;
    }
    
    public void setCommentLines(final String[] commentLines) {
        this.commentLines = commentLines;
    }
    
    public String getValue(final String key) {
        return this.keyValues.get(key);
    }
    
    public void setKeyValue(final String key, final String value) {
        this.keyValues.put(key, value);
    }
    
    public LinkedHashMap<String, String> getKeyValues() {
        return this.keyValues;
    }
    
    public void setKeyValues(final LinkedHashMap<String, String> keyValues) throws IOException {
        if (!keyValues.containsKey("useqArchiveVersion") || !keyValues.containsKey("versionedGenome") || !keyValues.containsKey("dataType")) {
            throw new IOException("Error: keyValues do not contain required keys.  Add 'useqArchiveVersion' and or 'versionedGenome' and or 'dataType'");
        }
        this.keyValues = keyValues;
    }
    
    public String getVersionedGenome() {
        return this.keyValues.get("versionedGenome");
    }
    
    public String getArchiveVersion() {
        return this.keyValues.get("useqArchiveVersion");
    }
    
    public String getDataType() {
        return this.keyValues.get("dataType");
    }
    
    public boolean isGraphData() {
        return this.keyValues.get("dataType").equals("graph");
    }
    
    public boolean isRegionData() {
        return this.keyValues.get("dataType").equals("region");
    }
    
    static {
        DAS2_VERSIONED_GENOME_FORM = Pattern.compile("^\\w_\\w+_\\w+_\\d+$");
        COLOR_HEX_FORM = Pattern.compile("#\\w{6}");
        KEY_VALUE_SPLITTER = Pattern.compile("\\s*([^=\\s]+)\\s*=\\s*(.+)\\s*");
    }
}
