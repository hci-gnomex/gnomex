// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.quickload;

import java.io.IOException;
import com.affymetrix.genometryImpl.parsers.ChromInfoParser;
import com.affymetrix.genometryImpl.parsers.LiftParser;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import org.xml.sax.SAXParseException;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.util.ServerTypeI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.general.GenericServer;
import com.affymetrix.genometryImpl.parsers.AnnotsXmlParser;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.util.SynonymLookup;

public final class QuickLoadServerModel
{
    private static final SynonymLookup LOOKUP;
    private static final Pattern tab_regex;
    private final String root_url;
    private final List<String> genome_names;
    private final Set<String> initialized;
    private final Map<String, List<AnnotsXmlParser.AnnotMapElt>> genome2annotsMap;
    private static final Map<String, QuickLoadServerModel> url2quickload;
    private final String primary_url;
    private final GenericServer primaryServer;
    
    public QuickLoadServerModel(final String url) {
        this(url, null, null);
    }
    
    private QuickLoadServerModel(String url, String pri_url, final GenericServer priServer) {
        this.genome_names = new ArrayList<String>();
        this.initialized = new HashSet<String>();
        this.genome2annotsMap = new HashMap<String, List<AnnotsXmlParser.AnnotMapElt>>();
        url = ServerUtils.formatURL(url, ServerTypeI.QuickLoad);
        if (pri_url != null) {
            pri_url = ServerUtils.formatURL(pri_url, ServerTypeI.QuickLoad);
        }
        this.root_url = url;
        this.primary_url = pri_url;
        this.primaryServer = priServer;
        Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "( {0}, {1} )", new Object[] { this.root_url, this.primary_url });
        this.loadGenomeNames();
    }
    
    public static synchronized QuickLoadServerModel getQLModelForURL(final URL url, final URL primary_url, final GenericServer primaryServer) {
        final String ql_http_root = url.toExternalForm();
        String primary_root = null;
        if (primary_url != null) {
            primary_root = primary_url.toExternalForm();
        }
        QuickLoadServerModel ql_server = QuickLoadServerModel.url2quickload.get(ql_http_root);
        if (ql_server == null) {
            LocalUrlCacher.loadSynonyms(QuickLoadServerModel.LOOKUP, ql_http_root + "synonyms.txt");
            ql_server = new QuickLoadServerModel(ql_http_root, primary_root, primaryServer);
            QuickLoadServerModel.url2quickload.put(ql_http_root, ql_server);
        }
        return ql_server;
    }
    
    public static synchronized QuickLoadServerModel getQLModelForURL(final URL url) {
        return getQLModelForURL(url, null, null);
    }
    
    public static synchronized void removeQLModelForURL(final String url) {
        if (QuickLoadServerModel.url2quickload.get(url) != null) {
            QuickLoadServerModel.url2quickload.remove(url);
        }
    }
    
    private static boolean getCacheAnnots() {
        return true;
    }
    
    private String getRootUrl() {
        return this.root_url;
    }
    
    public List<String> getGenomeNames() {
        return this.genome_names;
    }
    
    private AnnotatedSeqGroup getSeqGroup(final String genome_name) {
        return GenometryModel.getGenometryModel().addSeqGroup(QuickLoadServerModel.LOOKUP.findMatchingSynonym(GenometryModel.getGenometryModel().getSeqGroupNames(), genome_name));
    }
    
    public List<AnnotsXmlParser.AnnotMapElt> getAnnotsMap(final String genomeName) {
        return this.genome2annotsMap.get(genomeName);
    }
    
    public List<String> getTypes(String genome_name) {
        genome_name = QuickLoadServerModel.LOOKUP.findMatchingSynonym(this.genome_names, genome_name);
        if (!this.initialized.contains(genome_name) && !this.initGenome(genome_name)) {
            return null;
        }
        if (this.getAnnotsMap(genome_name) == null) {
            return Collections.emptyList();
        }
        final List<String> typeNames = new ArrayList<String>();
        for (final AnnotsXmlParser.AnnotMapElt annotMapElt : this.getAnnotsMap(genome_name)) {
            typeNames.add(annotMapElt.title);
        }
        return typeNames;
    }
    
    public Map<String, String> getProps(final String genomeName, final String featureName) {
        final Map<String, String> props = null;
        final List<AnnotsXmlParser.AnnotMapElt> annotList = this.getAnnotsMap(genomeName);
        if (annotList != null) {
            final AnnotsXmlParser.AnnotMapElt annotElt = AnnotsXmlParser.AnnotMapElt.findTitleElt(featureName, annotList);
            if (annotElt != null) {
                return annotElt.props;
            }
        }
        return props;
    }
    
    private synchronized boolean initGenome(final String genome_name) {
        Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "initializing data for genome: {0}", genome_name);
        final boolean metaOK = this.loadSeqInfo(genome_name);
        if (metaOK && this.loadAnnotationNames(genome_name)) {
            this.initialized.add(genome_name);
            return true;
        }
        final List<AnnotsXmlParser.AnnotMapElt> annotList = this.getAnnotsMap(genome_name);
        if (annotList != null) {
            annotList.clear();
        }
        return metaOK;
    }
    
    private boolean loadAnnotationNames(String genome_name) {
        genome_name = QuickLoadServerModel.LOOKUP.findMatchingSynonym(this.genome_names, genome_name);
        Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "loading list of available annotations for genome: {0}", genome_name);
        final List<AnnotsXmlParser.AnnotMapElt> annotList = new ArrayList<AnnotsXmlParser.AnnotMapElt>();
        this.genome2annotsMap.put(genome_name, annotList);
        InputStream istr = null;
        String filename = null;
        try {
            filename = this.getPath(genome_name, "annots.xml");
            istr = this.getInputStream(filename, false, true);
            boolean annots_found = false;
            try {
                annots_found = processAnnotsXml(istr, annotList);
            }
            catch (SAXParseException x) {
                final String errorMessage = "QuickLoad Server {0} has an invalid annotations (annots.xml) file for {1}. Please contact the server administrators or the IGB development team to let us know about the problem.";
                final String errorText = MessageFormat.format(errorMessage, this.root_url, genome_name);
                final String title = "Invalid annots.xml file";
                ErrorHandler.errorPanelWithReportBug(title, errorText, Level.SEVERE);
                return false;
            }
            if (annots_found) {
                return true;
            }
            Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "Couldn''t found annots.xml for {0}. Looking for annots.txt now.", genome_name);
            filename = this.getPath(genome_name, "annots.txt");
            istr = this.getInputStream(filename, getCacheAnnots(), false);
            annots_found = processAnnotsTxt(istr, annotList);
            if (!annots_found) {
                ErrorHandler.errorPanelWithReportBug("Missing Required File", MessageFormat.format("QuickLoad Server {0} does not contain required annots.xml/annots.txt metadata file for requested genome version {1}. IGB may not be able to display this genome.", this.root_url, genome_name), Level.SEVERE);
            }
            return annots_found;
        }
        catch (Exception ex) {
            Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.SEVERE, "Couldn''t found either annots.xml or annots.txt for {0}", genome_name);
            ex.printStackTrace();
            return false;
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }
    
    private static boolean processAnnotsXml(final InputStream istr, final List<AnnotsXmlParser.AnnotMapElt> annotList) throws SAXParseException {
        if (istr == null) {
            return false;
        }
        AnnotsXmlParser.parseAnnotsXml(istr, annotList);
        return true;
    }
    
    private static boolean processAnnotsTxt(final InputStream istr, final List<AnnotsXmlParser.AnnotMapElt> annotList) {
        BufferedReader br = null;
        try {
            if (istr == null) {
                return false;
            }
            br = new BufferedReader(new InputStreamReader(istr));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = QuickLoadServerModel.tab_regex.split(line);
                if (fields.length >= 1) {
                    final String annot_file_name = fields[0];
                    if (annot_file_name == null) {
                        continue;
                    }
                    if (annot_file_name.length() == 0) {
                        continue;
                    }
                    final String friendlyName = LoadUtils.stripFilenameExtensions(annot_file_name);
                    final AnnotsXmlParser.AnnotMapElt annotMapElt = new AnnotsXmlParser.AnnotMapElt(annot_file_name, friendlyName);
                    annotList.add(annotMapElt);
                }
            }
            return true;
        }
        catch (Exception ex) {
            return false;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
    }
    
    private boolean loadSeqInfo(String genome_name) {
        final String liftAll = "liftAll.lft";
        final String modChromInfo = "mod_chromInfo.txt";
        final String genomeTxt = "genome.txt";
        genome_name = QuickLoadServerModel.LOOKUP.findMatchingSynonym(this.genome_names, genome_name);
        boolean success = false;
        InputStream lift_stream = null;
        InputStream cinfo_stream = null;
        InputStream ginfo_stream = null;
        try {
            final String lift_path = this.getPath(genome_name, liftAll);
            try {
                lift_stream = this.getInputStream(lift_path, getCacheAnnots(), true);
            }
            catch (Exception ex3) {
                Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "couldn''t find {0}, looking instead for {1}", new Object[] { liftAll, modChromInfo });
                lift_stream = null;
            }
            final String ginfo_path = this.getPath(genome_name, genomeTxt);
            if (lift_stream == null) {
                try {
                    ginfo_stream = this.getInputStream(ginfo_path, getCacheAnnots(), false);
                }
                catch (Exception ex4) {
                    Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.WARNING, "couldn''t find {0}, looking instead for {1}", new Object[] { liftAll, genomeTxt });
                    ginfo_stream = null;
                }
            }
            if (ginfo_stream == null) {
                final String cinfo_path = this.getPath(genome_name, modChromInfo);
                try {
                    cinfo_stream = this.getInputStream(cinfo_path, getCacheAnnots(), false);
                }
                catch (Exception ex) {
                    Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.WARNING, "ERROR: could find {0} or {1} or {2}", new Object[] { lift_path, ginfo_path, cinfo_path });
                    ex.printStackTrace();
                    cinfo_stream = null;
                }
            }
            final boolean annot_contigs = false;
            if (lift_stream != null) {
                LiftParser.parse(lift_stream, GenometryModel.getGenometryModel(), genome_name, annot_contigs);
                success = true;
            }
            else if (ginfo_stream != null) {
                ChromInfoParser.parse(ginfo_stream, GenometryModel.getGenometryModel(), genome_name);
                success = true;
            }
            else if (cinfo_stream != null) {
                ChromInfoParser.parse(cinfo_stream, GenometryModel.getGenometryModel(), genome_name);
                success = true;
            }
        }
        catch (Exception ex2) {
            ErrorHandler.errorPanel("Error loading data for genome '" + genome_name + "'", ex2, Level.SEVERE);
        }
        finally {
            GeneralUtils.safeClose(lift_stream);
            GeneralUtils.safeClose(ginfo_stream);
            GeneralUtils.safeClose(cinfo_stream);
        }
        return success;
    }
    
    private synchronized void loadGenomeNames() {
        final String contentsTxt = "contents.txt";
        InputStream istr = null;
        InputStreamReader ireader = null;
        BufferedReader br = null;
        try {
            try {
                istr = this.getInputStream(contentsTxt, getCacheAnnots(), false);
            }
            catch (Exception e) {
                Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.WARNING, "ERROR: Couldn''t open ''{0}{1}\n:  {2}", new Object[] { this.getLoadURL(), contentsTxt, e.toString() });
                istr = null;
            }
            if (istr == null) {
                Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.SEVERE, "Could not load QuickLoad contents from\n" + this.getLoadURL() + contentsTxt);
                return;
            }
            ireader = new InputStreamReader(istr);
            br = new BufferedReader(ireader);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() != 0 && !line.startsWith("#")) {
                    if (line.startsWith("<") && line.endsWith(">")) {
                        continue;
                    }
                    AnnotatedSeqGroup group = null;
                    final String[] fields = QuickLoadServerModel.tab_regex.split(line);
                    String genome_name = "";
                    if (fields.length >= 1) {
                        genome_name = fields[0];
                        genome_name = genome_name.trim();
                        if (genome_name.length() == 0) {
                            Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.INFO, "Found blank QuickLoad genome -- skipping");
                            continue;
                        }
                        group = this.getSeqGroup(genome_name);
                        this.genome_names.add(genome_name);
                    }
                    if (fields.length < 2 || group.getDescription() != null) {
                        continue;
                    }
                    group.setDescription(fields[1]);
                }
            }
        }
        catch (Exception ex) {
            ErrorHandler.errorPanel("Error loading genome names", ex, Level.SEVERE);
        }
        finally {
            GeneralUtils.safeClose(istr);
            GeneralUtils.safeClose(ireader);
            GeneralUtils.safeClose(br);
        }
    }
    
    public String getPath(final String genome_name, final String file) {
        final StringBuilder builder = new StringBuilder();
        builder.append(QuickLoadServerModel.LOOKUP.findMatchingSynonym(this.genome_names, genome_name));
        builder.append("/");
        if (file != null && !file.isEmpty()) {
            builder.append(file);
        }
        return builder.toString();
    }
    
    private InputStream getInputStream(final String append_url, final boolean write_to_cache, final boolean fileMayNotExist) throws IOException {
        String load_url = this.getLoadURL() + append_url;
        InputStream istr = LocalUrlCacher.getInputStream(load_url, write_to_cache, null, fileMayNotExist);
        if (istr == null && this.isLoadingFromPrimary() && !fileMayNotExist) {
            Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.WARNING, "Primary Server :{0} is not responding. So disabling it for this session.", this.primaryServer.serverName);
            this.primaryServer.setServerStatus(LoadUtils.ServerStatus.NotResponding);
            load_url = this.getLoadURL() + append_url;
            istr = LocalUrlCacher.getInputStream(load_url, write_to_cache, null, fileMayNotExist);
        }
        Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.FINE, "Load URL: {0}", load_url);
        return istr;
    }
    
    private boolean isLoadingFromPrimary() {
        return this.primary_url != null && this.primaryServer != null && !this.primaryServer.getServerStatus().equals(LoadUtils.ServerStatus.NotResponding);
    }
    
    public InputStream getSpeciesTxt() {
        InputStream stream = null;
        try {
            stream = this.getInputStream("species.txt", false, true);
        }
        catch (IOException ex) {
            Logger.getLogger(QuickLoadServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stream;
    }
    
    public boolean hasSpeciesTxt() {
        InputStream stream;
        try {
            stream = this.getInputStream("species.txt", false, true);
        }
        catch (IOException ex) {
            return false;
        }
        return stream != null;
    }
    
    private String getLoadURL() {
        if (!this.isLoadingFromPrimary()) {
            return this.root_url;
        }
        return this.primary_url;
    }
    
    @Override
    public String toString() {
        return "QuickLoadServerModel: url='" + this.getRootUrl() + "'";
    }
    
    static {
        LOOKUP = SynonymLookup.getDefaultLookup();
        tab_regex = Pattern.compile("\t");
        url2quickload = new HashMap<String, QuickLoadServerModel>();
    }
}
