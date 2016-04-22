//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.servlets;

import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import com.affymetrix.genometryImpl.symloader.BAM;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.Set;
import java.net.URI;
import com.affymetrix.genometryImpl.util.SearchUtils;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collections;
import java.util.Collection;
import javax.xml.transform.Result;
import com.affymetrix.genometryImpl.das2.SimpleDas2Type;
import java.io.Closeable;
import javax.xml.transform.TransformerException;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import com.affymetrix.genometryImpl.SeqSpan;
import hci.gnomex.security.SecurityAdvisor;
import com.affymetrix.genometryImpl.AnnotSecurity;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.FileFilter;
import com.affymetrix.genometryImpl.util.HiddenFileFilter;
import com.affymetrix.genometryImpl.util.DirectoryFilter;
import java.io.IOException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.QualifiedDataTrack;
import hci.gnomex.model.UnloadDataTrack;
import hci.gnomex.utility.DataTrackQuery;
import hci.gnomex.utility.DictionaryHelper;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometry.genopub.GenomeVersion;
import java.util.Iterator;
import org.hibernate.Transaction;
import com.affymetrix.genometryImpl.util.Optimize;
import com.affymetrix.genometry.genopub.QualifiedAnnotation;
import com.affymetrix.genometry.genopub.Segment;
import java.util.ArrayList;
import com.affymetrix.genometry.genopub.UnloadAnnotation;
import com.affymetrix.genometry.genopub.Organism;
import com.affymetrix.genometry.genopub.AnnotationQuery;
import com.affymetrix.genometryImpl.parsers.CytobandParser;
import com.affymetrix.genometryImpl.parsers.ExonArrayDesignParser;
import com.affymetrix.genometryImpl.parsers.Bprobe1Parser;
import com.affymetrix.genometryImpl.parsers.graph.BarParser;
import com.affymetrix.genometryImpl.parsers.Das2FeatureSaxParser;
import com.affymetrix.genometryImpl.parsers.GFFParser;
import com.affymetrix.genometryImpl.parsers.BrsParser;
import com.affymetrix.genometryImpl.parsers.BgnParser;
import com.affymetrix.genometryImpl.parsers.SimpleBedParser;
import com.affymetrix.genometryImpl.parsers.BedParser;
import com.affymetrix.genometryImpl.parsers.PSLParser;
import com.affymetrix.genometryImpl.parsers.BpsParser;
import com.affymetrix.genometryImpl.parsers.ProbeSetDisplayPlugin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.servlet.ServletContext;
import hci.gnomex.utility.PropertyDictionaryHelper;
import com.affymetrix.genometry.gnomex.HibernateUtil;
import javax.xml.transform.Source;
import java.util.Date;
import com.affymetrix.genometry.genopub.GenoPubSecurity;
import com.affymetrix.genometry.gnomex.GNomExSecurity;
import com.affymetrix.genometryImpl.util.ServerUtils;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.servlet.ServletException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedHashMap;
import java.io.File;
import javax.xml.transform.Transformer;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import java.util.HashMap;
import com.affymetrix.genometryImpl.parsers.AnnotsXmlParser;
import java.text.SimpleDateFormat;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.List;
import com.affymetrix.genometryImpl.GenometryModel;
import java.util.regex.Pattern;
import java.util.Map;
import javax.servlet.http.HttpServlet;

public final class GenometryDas2Servlet extends HttpServlet
{
    static final boolean DEBUG = true;
    private static final String RELEASE_VERSION = "2.6";
    private static final boolean USE_CREATED_ATT = true;
    private static final String SERVER_SYNTAX_EXPLANATION = "See http://netaffxdas.affymetrix.com/das2 for proper query syntax.";
    private static final String LIMITED_FEATURE_QUERIES_EXPLANATION = "See http://netaffxdas.affymetrix.com/das2 for supported feature queries.";
    private static final Map<String, Das2Coords> genomeid2coord;
    private static final String DAS2_NAMESPACE = "http://biodas.org/documents/das2";
    private static final String SOURCES_CONTENT_TYPE = "application/x-das-sources+xml";
    private static final String SEGMENTS_CONTENT_TYPE = "application/x-das-segments+xml";
    private static final String TYPES_CONTENT_TYPE = "application/x-das-types+xml";
    private static final String LOGIN_CONTENT_TYPE = "application/x-das-login+xml";
    private static final String REFRESH_CONTENT_TYPE = "application/x-das-refresh+xml";
    private static final String URID = "uri";
    private static final String NAME = "title";
    private static String sources_query_with_slash;
    private static String sources_query_no_slash;
    private static final String segments_query = "segments";
    private static final String types_query = "types";
    private static final String features_query = "features";
    private static final String query_att = "query_uri";
    private static final String login_query = "login";
    private static final String refresh_query = "refresh";
    private static final String default_feature_format = "das2feature";
    private static String genometry_mode;
    private static final String MAINTAINER_EMAIL = "maintainer_email";
    private static final String XML_BASE = "xml_base";
    private static String genometry_server_dir;
    private static String gnomex_analysis_root_dir;
    private static String maintainer_email;
    private static String xml_base;
    private static String data_root;
    private static String types_xslt_file;
    private static final Pattern query_splitter;
    private static final Pattern tagval_splitter;
    private static GenometryModel gmodel;
    private static Map<String, List<AnnotatedSeqGroup>> organisms;
    private final Map<String, Class<? extends AnnotationWriter>> output_registry;
    private final SimpleDateFormat date_formatter;
    private long date_initialized;
    private String date_init_string;
    private static Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map;
    private final Map<AnnotatedSeqGroup, Map<String, String>> genome2graphfiles;
    private final Map<AnnotatedSeqGroup, Map<String, String>> genome2graphdirs;
    private final HashMap<String, USeqArchive> file2USeqArchive;
    private Transformer types_transformer;
    private static final boolean DEFAULT_USE_TYPES_XSLT = true;
    private boolean use_types_xslt;
    private static File synonym_file;
    private static File chr_synonym_file;
    private static String org_order_filename;

    public GenometryDas2Servlet() {
        this.output_registry = new HashMap<String, Class<? extends AnnotationWriter>>();
        this.date_formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        this.date_initialized = 0L;
        this.date_init_string = null;
        this.genome2graphfiles = new LinkedHashMap<AnnotatedSeqGroup, Map<String, String>>();
        this.genome2graphdirs = new LinkedHashMap<AnnotatedSeqGroup, Map<String, String>>();
        this.file2USeqArchive = new HashMap<String, USeqArchive>();
    }

    public void init() throws ServletException {
        try {
            Thread.sleep(5000L);
        }
        catch (InterruptedException ex) {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!this.loadAndSetFields()) {
            throw new ServletException("FAILED to init() GenometryDas2Servlet, aborting!");
        }
        try {
            super.init();
            this.use_types_xslt = new File(GenometryDas2Servlet.types_xslt_file).exists();
            if (this.use_types_xslt) {
                final Source type_xslt = new StreamSource(GenometryDas2Servlet.types_xslt_file);
                final TransformerFactory transFact = TransformerFactory.newInstance();
                this.types_transformer = transFact.newTransformer(type_xslt);
            }
            if (!new File(GenometryDas2Servlet.data_root).isDirectory()) {
                throw new ServletException("Aborting: Specified directory does not exist: '" + GenometryDas2Servlet.data_root + "'");
            }
            initFormats(this.output_registry);
            ServerUtils.loadSynonyms(GenometryDas2Servlet.synonym_file, SynonymLookup.getDefaultLookup());
            ServerUtils.loadSynonyms(GenometryDas2Servlet.chr_synonym_file, SynonymLookup.getChromosomeLookup());
            if (GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
                Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading genomes from gnomex database....");
                this.loadGenomesFromGNomEx(null, false);
            }
            else if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
                Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading genomes from relational database....");
                this.loadGenomesFromGenoPub(null, false);
            }
            else {
                Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading genomes from file system....");
                this.loadGenomesFromFileSystem(GenometryDas2Servlet.data_root, GenometryDas2Servlet.organisms, GenometryDas2Servlet.org_order_filename);
            }
            ServerUtils.printGenomes((Map)GenometryDas2Servlet.organisms);
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServletException((Throwable)ex2);
        }
        this.date_initialized = System.currentTimeMillis();
        this.date_init_string = this.date_formatter.format(new Date(this.date_initialized));
        System.out.println("GenometryDas2Servlet 2.6, dir: '" + GenometryDas2Servlet.data_root + "', url: '" + GenometryDas2Servlet.xml_base + "'");
    }

    private boolean loadAndSetFields() {
        final ServletContext context = this.getServletContext();
        if (context.getInitParameter("genometry_mode") != null) {
            GenometryDas2Servlet.genometry_mode = context.getInitParameter("genometry_mode");
        }
        if (GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
            Session sess = null;
            try {
                final String gnomex_server_name = context.getInitParameter("gnomex_server_name");
                sess = (Session)HibernateUtil.getSessionFactory().openSession();
                GenometryDas2Servlet.genometry_server_dir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(gnomex_server_name);
                GenometryDas2Servlet.gnomex_analysis_root_dir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(gnomex_server_name);
            }
            catch (Exception e) {
                System.out.println("\nERROR: Cannot open hibernate session to obtain gnomex property " + e.toString());
            }
            finally {
				if (sess != null) {
                sess.close();
				}
            }
        }
        else if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
            GenometryDas2Servlet.genometry_server_dir = context.getInitParameter("genometry_server_dir_genopub");
        }
        else {
            GenometryDas2Servlet.genometry_server_dir = context.getInitParameter("genometry_server_dir");
        }
        GenometryDas2Servlet.maintainer_email = context.getInitParameter("maintainer_email");
        GenometryDas2Servlet.xml_base = context.getInitParameter("xml_base");
        if (GenometryDas2Servlet.genometry_server_dir == null || GenometryDas2Servlet.maintainer_email == null || GenometryDas2Servlet.xml_base == null) {
            if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
                GenometryDas2Servlet.genometry_server_dir = System.getProperty("das2_genometry_server_dir_genopub");
            }
            else {
                GenometryDas2Servlet.genometry_server_dir = System.getProperty("das2_genometry_server_dir");
            }
            GenometryDas2Servlet.maintainer_email = System.getProperty("das2_maintainer_email");
            GenometryDas2Servlet.xml_base = System.getProperty("das2_xml_base");
        }
        if (GenometryDas2Servlet.genometry_server_dir == null || GenometryDas2Servlet.maintainer_email == null || GenometryDas2Servlet.xml_base == null) {
            File p = new File("genometryDas2ServletParameters.txt");
            if (!p.exists()) {
                System.out.println("\tLooking for but couldn't find " + p);
                File dir = new File(System.getProperty("user.dir"));
                p = new File(dir, "genometryDas2ServletParameters.txt");
                if (!p.exists()) {
                    System.out.println("\tLooking for but couldn't find " + p);
                    dir = new File(System.getProperty("user.home"));
                    p = new File(dir, "genometryDas2ServletParameters.txt");
                    if (!p.exists()) {
                        System.out.println("\tLooking for but couldn't find " + p);
                        return false;
                    }
                }
            }
            System.out.println("\tFound and loading " + p);
            final HashMap<String, String> prop = (HashMap<String, String>)ServerUtils.loadFileIntoHashMap(p);
            if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
                if (GenometryDas2Servlet.genometry_server_dir == null && prop.containsKey("genometry_server_dir_genopub")) {
                    GenometryDas2Servlet.genometry_server_dir = prop.get("genometry_server_dir_genopub");
                }
            }
            else if (GenometryDas2Servlet.genometry_mode.equals("classic") && GenometryDas2Servlet.genometry_server_dir == null && prop.containsKey("genometry_server_dir")) {
                GenometryDas2Servlet.genometry_server_dir = prop.get("genometry_server_dir");
            }
            if (GenometryDas2Servlet.maintainer_email == null && prop.containsKey("maintainer_email")) {
                GenometryDas2Servlet.maintainer_email = prop.get("maintainer_email");
            }
            if (GenometryDas2Servlet.xml_base == null && prop.containsKey("xml_base")) {
                GenometryDas2Servlet.xml_base = prop.get("xml_base");
            }
            if (GenometryDas2Servlet.genometry_server_dir == null || GenometryDas2Servlet.xml_base == null) {
                System.out.println("\tERROR: could not set the following:\n\t\tgenometry_server_dir\t" + GenometryDas2Servlet.genometry_server_dir + "\n\t\txml_base\t" + GenometryDas2Servlet.xml_base);
                return false;
            }
        }
        if (GenometryDas2Servlet.genometry_server_dir != null && !GenometryDas2Servlet.genometry_server_dir.endsWith("/")) {
            GenometryDas2Servlet.genometry_server_dir += "/";
        }
        GenometryDas2Servlet.data_root = GenometryDas2Servlet.genometry_server_dir + "/";
        GenometryDas2Servlet.synonym_file = new File(GenometryDas2Servlet.data_root + "synonyms.txt");
        GenometryDas2Servlet.chr_synonym_file = new File(GenometryDas2Servlet.data_root + "synonyms.txt");
        GenometryDas2Servlet.types_xslt_file = GenometryDas2Servlet.data_root + "types.xslt";
        GenometryDas2Servlet.org_order_filename = GenometryDas2Servlet.data_root + "organism_order.txt";
        setXmlBase(GenometryDas2Servlet.xml_base);
        return true;
    }

    private static void initFormats(final Map<String, Class<? extends AnnotationWriter>> output_registry) {
        output_registry.put("link.psl", (Class<? extends AnnotationWriter>)ProbeSetDisplayPlugin.class);
        output_registry.put("bps", (Class<? extends AnnotationWriter>)BpsParser.class);
        output_registry.put("psl", (Class<? extends AnnotationWriter>)PSLParser.class);
        output_registry.put("bed", (Class<? extends AnnotationWriter>)BedParser.class);
        output_registry.put("simplebed", (Class<? extends AnnotationWriter>)SimpleBedParser.class);
        output_registry.put("bgn", (Class<? extends AnnotationWriter>)BgnParser.class);
        output_registry.put("brs", (Class<? extends AnnotationWriter>)BrsParser.class);
        output_registry.put("gff", (Class<? extends AnnotationWriter>)GFFParser.class);
        output_registry.put("das2feature", (Class<? extends AnnotationWriter>)Das2FeatureSaxParser.class);
        output_registry.put("das2xml", (Class<? extends AnnotationWriter>)Das2FeatureSaxParser.class);
        output_registry.put("bar", (Class<? extends AnnotationWriter>)BarParser.class);
        output_registry.put("application/x-das-features+xml", (Class<? extends AnnotationWriter>)Das2FeatureSaxParser.class);
        output_registry.put("x-das-features+xml", (Class<? extends AnnotationWriter>)Das2FeatureSaxParser.class);
        output_registry.put("bp2", (Class<? extends AnnotationWriter>)Bprobe1Parser.class);
        output_registry.put("ead", (Class<? extends AnnotationWriter>)ExonArrayDesignParser.class);
        output_registry.put("cyt", (Class<? extends AnnotationWriter>)CytobandParser.class);
    }

    private boolean loadGenomesFromGenoPub(final GenoPubSecurity genoPubSecurity, final boolean isServerRefreshMode) {
        Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading Genomes from DB");
        Session sess = null;
        Transaction tx = null;
        File file = null;
        try {
            sess = (Session)com.affymetrix.genometry.genopub.HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final AnnotationQuery annotationQuery = new AnnotationQuery();
            annotationQuery.runAnnotationQuery(sess, genoPubSecurity, isServerRefreshMode);
            for (final Organism organism : annotationQuery.getOrganisms()) {
                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Organism = {0}", organism.getName());
                for (final String genomeVersionName : annotationQuery.getVersionNames(organism)) {
                    final GenomeVersion gv = annotationQuery.getGenomeVersion(genomeVersionName);
                    for (final UnloadAnnotation unloadAnnotation : AnnotationQuery.getUnloadedAnnotations(sess, genoPubSecurity, gv)) {
                        if (isServerRefreshMode) {
                            final AnnotatedSeqGroup genomeVersion = GenometryDas2Servlet.gmodel.getSeqGroup(genomeVersionName);
                            if (genomeVersion != null) {
                                ServerUtils.unloadGenoPubAnnot(unloadAnnotation.getTypeName(), genomeVersion, (Map)this.genome2graphdirs.get(genomeVersion));
                            }
                        }
                        sess.delete((Object)unloadAnnotation);
                    }
                    final List<QualifiedAnnotation> qualifiedAnnotations = annotationQuery.getQualifiedAnnotations(organism, genomeVersionName);
                    final List<Segment> segments = annotationQuery.getSegments(organism, genomeVersionName);
                    if (!gv.hasSequence(GenometryDas2Servlet.data_root) && (qualifiedAnnotations == null || qualifiedAnnotations.isEmpty())) {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Bypassing Genome version = {0}. No annotations nor sequence exists.", genomeVersionName);
                    }
                    else if (segments == null || segments.isEmpty()) {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing annotations/sequence for Genome version {0}.  No segments have been defined.", genomeVersionName);
                    }
                    else {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Genome version = {0}", genomeVersionName);
                        final AnnotatedSeqGroup genomeVersion = GenometryDas2Servlet.gmodel.addSeqGroup(genomeVersionName);
                        genomeVersion.setOrganism(organism.getName());
                        List<AnnotatedSeqGroup> versions = GenometryDas2Servlet.organisms.get(organism.getName());
                        if (versions == null) {
                            versions = new ArrayList<AnnotatedSeqGroup>();
                            GenometryDas2Servlet.organisms.put(organism.getName(), versions);
                        }
                        versions.add(genomeVersion);
                        if (segments != null) {
                            for (final Segment segment : segments) {
                                final BioSeq chrom = genomeVersion.addSeq(segment.getName(), (int)segment.getLength());
                                chrom.setVersion(genomeVersionName);
                            }
                        }
                        Map<String, String> graph_name2dir = this.genome2graphdirs.get(genomeVersion);
                        if (graph_name2dir == null) {
                            graph_name2dir = new LinkedHashMap<String, String>();
                            this.genome2graphdirs.put(genomeVersion, graph_name2dir);
                        }
                        Map<String, String> graph_name2file = this.genome2graphfiles.get(genomeVersion);
                        if (graph_name2file == null) {
                            graph_name2file = new LinkedHashMap<String, String>();
                            this.genome2graphfiles.put(genomeVersion, graph_name2file);
                        }
                        for (final QualifiedAnnotation qa : qualifiedAnnotations) {
                            final String fileName = qa.getAnnotation().getQualifiedFileName(GenometryDas2Servlet.genometry_server_dir);
                            final String typePrefix = qa.getTypePrefix();
                            file = new File(fileName);
                            if (file.exists()) {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Annotation type = {0}\t{1}", new Object[] { (typePrefix != null) ? typePrefix : "", (fileName != null) ? fileName : "" });
                                if (file.isDirectory()) {
                                    if (isMultiFileDataTrackType(file)) {
                                        ServerUtils.loadGenoPubAnnotFromDir(typePrefix, file.getPath(), genomeVersion, file, qa.getAnnotation().getIdAnnotation(), (Map)graph_name2dir);
                                    }
                                    else if (!file.exists() || file.list() == null || file.list().length == 0) {
                                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing annotation {0}.  No files associated with this annotation.", typePrefix);
                                    }
                                    else {
                                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing annotation {0} for file {1}. Only the bar format permits multiple annotation files.", new Object[] { typePrefix, fileName });
                                    }
                                }
                                else if (file.getName().toLowerCase().endsWith(".bai")) {
                                    Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing annotation {0}.  No associated bam alignment file for " + file);
                                }
                                else {
                                    ServerUtils.loadGenoPubAnnotsFromFile(GenometryDas2Servlet.genometry_server_dir, file, genomeVersion, (Map)GenometryDas2Servlet.annots_map, typePrefix, qa.getAnnotation().getIdAnnotation(), (Map)graph_name2file);
                                }
                                if (qa.getAnnotation().getIsLoaded() == null || !qa.getAnnotation().getIsLoaded().equals("N")) {
                                    continue;
                                }
                                qa.getAnnotation().setIsLoaded("Y");
                            }
                            else {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Annotation not loaded. File does not exist or is not supported: {0}\t{1}", new Object[] { (typePrefix != null) ? typePrefix : "", (fileName != null) ? fileName : "" });
                            }
                        }
                        Optimize.genome(genomeVersion);
                    }
                }
            }
            sess.flush();
            tx.commit();
        }
        catch (Exception e) {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.SEVERE, "Problems reading annotations from file '" + file + "' in database {0}", e.toString());
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
			if (sess != null) {
            sess.close();
			}
        }
        return true;
    }

    private boolean loadGenomesFromGNomEx(final GNomExSecurity gnomexSecurity, final boolean isServerRefreshMode) {
        Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading Genomes from GNomEx DB");
        Session sess = null;
        Transaction tx = null;
        File file = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            if (isServerRefreshMode) {
                DictionaryHelper.reloadLimited(sess);
            }
            final DataTrackQuery dataTrackQuery = new DataTrackQuery();
            dataTrackQuery.runDataTrackQuery(sess, (gnomexSecurity != null) ? gnomexSecurity.getSecAdvisor() : null, isServerRefreshMode);
            DataTrackQuery dataTrackQueryAll = null;
            if (isServerRefreshMode) {
                dataTrackQueryAll = new DataTrackQuery();
                dataTrackQueryAll.runDataTrackQuery(sess, (gnomexSecurity != null) ? gnomexSecurity.getSecAdvisor() : null, false);
            }
            else {
                dataTrackQueryAll = dataTrackQuery;
            }
            if (isServerRefreshMode) {
                for (final Map.Entry<String, List<AnnotatedSeqGroup>> oentry : GenometryDas2Servlet.organisms.entrySet()) {
                    final String org = oentry.getKey();
                    final List<AnnotatedSeqGroup> versions = oentry.getValue();
                    final ArrayList<AnnotatedSeqGroup> staleVersions = new ArrayList<AnnotatedSeqGroup>();
                    for (final AnnotatedSeqGroup version : versions) {
                        final GenomeBuild gb = dataTrackQueryAll.getGenomeBuild(version.getID());
                        if (gb == null) {
                            staleVersions.add(version);
                        }
                    }
                    for (final AnnotatedSeqGroup staleVersion : staleVersions) {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Removing stale genome version " + staleVersion.getID());
                        versions.remove(staleVersion);
                    }
                }
            }
            for (final hci.gnomex.model.Organism organism : dataTrackQueryAll.getOrganisms()) {
                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Organism = {0}", organism.getDas2Name());
                for (final String genomeBuildName : dataTrackQueryAll.getGenomeBuildNames(organism)) {
                    final GenomeBuild gb2 = dataTrackQueryAll.getGenomeBuild(genomeBuildName);
                    for (final UnloadDataTrack unloadDataTrack : DataTrackQuery.getUnloadedDataTracks(sess, (gnomexSecurity != null) ? gnomexSecurity.getSecAdvisor() : null, gb2)) {
                        if (isServerRefreshMode) {
                            final AnnotatedSeqGroup genomeVersion = GenometryDas2Servlet.gmodel.getSeqGroup(genomeBuildName);
                            if (genomeVersion != null) {
                                ServerUtils.unloadGenoPubAnnot(unloadDataTrack.getTypeName(), genomeVersion, (Map)null);
                            }
                        }
                        sess.delete((Object)unloadDataTrack);
                    }
                    final List<QualifiedDataTrack> qualifiedDataTracks = (List<QualifiedDataTrack>)dataTrackQuery.getQualifiedDataTracks(organism, genomeBuildName);
                    final List<hci.gnomex.model.Segment> segments = (List<hci.gnomex.model.Segment>)dataTrackQuery.getSegments(organism, genomeBuildName);
                    List<QualifiedDataTrack> qualifiedDataTracksAll = null;
                    qualifiedDataTracksAll = (List<QualifiedDataTrack>)dataTrackQueryAll.getQualifiedDataTracks(organism, genomeBuildName);
                    System.out.println(genomeBuildName + " qualifiedDataTracksAll.size=" + ((qualifiedDataTracksAll != null) ? qualifiedDataTracksAll.size() : "") + " segments=" + ((segments != null) ? segments.size() : ""));
                    if (!gb2.hasSequence(GenometryDas2Servlet.data_root) && (qualifiedDataTracksAll == null || qualifiedDataTracksAll.isEmpty())) {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Bypassing Genome version = {0}. No data tracks nor sequence exists.", genomeBuildName);
                        if (!isServerRefreshMode) {
                            continue;
                        }
                        final List<AnnotatedSeqGroup> versions2 = GenometryDas2Servlet.organisms.get(organism.getDas2Name());
                        if (versions2 == null) {
                            continue;
                        }
                        for (final AnnotatedSeqGroup gv : versions2) {
                            if (gv.getID().equals(genomeBuildName)) {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Remove invalid genome version " + genomeBuildName);
                                versions2.remove(gv);
                                break;
                            }
                        }
                    }
                    else if (segments == null || segments.isEmpty()) {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing data tracks/sequence for Genome version {0}.  No segments have been defined.", genomeBuildName);
                        if (!isServerRefreshMode) {
                            continue;
                        }
                        final List<AnnotatedSeqGroup> versions2 = GenometryDas2Servlet.organisms.get(organism.getDas2Name());
                        if (versions2 == null) {
                            continue;
                        }
                        for (final AnnotatedSeqGroup gv : versions2) {
                            if (gv.getID().equals(genomeBuildName)) {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Remove invalid genome version " + genomeBuildName);
                                versions2.remove(gv);
                                break;
                            }
                        }
                    }
                    else {
                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Genome version = {0}", genomeBuildName);
                        final AnnotatedSeqGroup genomeVersion2 = GenometryDas2Servlet.gmodel.addSeqGroup(genomeBuildName);
                        genomeVersion2.setOrganism(organism.getDas2Name());
                        List<AnnotatedSeqGroup> versions3 = GenometryDas2Servlet.organisms.get(organism.getDas2Name());
                        if (versions3 == null) {
                            versions3 = new ArrayList<AnnotatedSeqGroup>();
                            GenometryDas2Servlet.organisms.put(organism.getDas2Name(), versions3);
                        }
                        boolean found = false;
                        if (isServerRefreshMode) {
                            for (final AnnotatedSeqGroup gv2 : versions3) {
                                if (gv2.getID().equals(genomeBuildName)) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            versions3.add(genomeVersion2);
                        }
                        if (segments != null) {
                            for (final hci.gnomex.model.Segment segment : segments) {
                                final BioSeq chrom = genomeVersion2.addSeq(segment.getName(), (int)segment.getLength());
                                chrom.setVersion(genomeBuildName);
                            }
                        }
                        Map<String, String> graph_name2dir = this.genome2graphdirs.get(genomeVersion2);
                        if (graph_name2dir == null) {
                            graph_name2dir = new LinkedHashMap<String, String>();
                            this.genome2graphdirs.put(genomeVersion2, graph_name2dir);
                        }
                        Map<String, String> graph_name2file = this.genome2graphfiles.get(genomeVersion2);
                        if (graph_name2file == null) {
                            graph_name2file = new LinkedHashMap<String, String>();
                            this.genome2graphfiles.put(genomeVersion2, graph_name2file);
                        }
                        for (final QualifiedDataTrack qdt : qualifiedDataTracks) {
                            final String fileName = qdt.getDataTrack().getQualifiedFileName(GenometryDas2Servlet.genometry_server_dir, GenometryDas2Servlet.gnomex_analysis_root_dir);
                            final String typePrefix = qdt.getTypePrefix();
                            file = new File(fileName);
                            if (file.exists()) {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.FINE, "Data track type = {0}\t{1}", new Object[] { (typePrefix != null) ? typePrefix : "", (fileName != null) ? fileName : "" });
                                if (file.isDirectory()) {
                                    if (isMultiFileDataTrackType(file)) {
                                        ServerUtils.loadGenoPubAnnotFromDir(typePrefix, file.getPath(), genomeVersion2, file, qdt.getDataTrack().getIdDataTrack(), (Map)graph_name2dir);
                                    }
                                    else if (!file.exists() || file.list() == null || file.list().length == 0) {
                                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing data track {0}.  No files associated with this data track.", typePrefix);
                                    }
                                    else {
                                        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing data track {0} for file {1}. Only the bar format permits multiple data track files.", new Object[] { typePrefix, fileName });
                                    }
                                }
                                else if (file.getName().toLowerCase().endsWith(".bai")) {
                                    Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Bypassing data track {0}.  No associated bam alignment file for " + file);
                                }
                                else {
                                    ServerUtils.loadGenoPubAnnotsFromFile(GenometryDas2Servlet.genometry_server_dir, file, genomeVersion2, (Map)GenometryDas2Servlet.annots_map, typePrefix, qdt.getDataTrack().getIdDataTrack(), (Map)graph_name2file);
                                }
                                if (qdt.getDataTrack().getIsLoaded() == null || !qdt.getDataTrack().getIsLoaded().equals("N")) {
                                    continue;
                                }
                                qdt.getDataTrack().setIsLoaded("Y");
                            }
                            else {
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Data track not loaded. File does not exist or is not supported: {0}\t{1}", new Object[] { (typePrefix != null) ? typePrefix : "", (fileName != null) ? fileName : "" });
                                Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.WARNING, "Toggling off loaded flag for {0}\t{1}", new Object[] { (typePrefix != null) ? typePrefix : "", (fileName != null) ? fileName : "" });
                                qdt.getDataTrack().setIsLoaded("Y");
                            }
                        }
                        Optimize.genome(genomeVersion2);
                    }
                }
            }
            sess.flush();
            tx.commit();
        }
        catch (Exception e) {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.SEVERE, "Problems reading data tracks from file '" + file + "' in gnomex database {0}", e.toString());
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
			if (sess != null) {
            sess.close();
			}
        }
        return true;
    }

    private static boolean isMultiFileDataTrackType(final File dir) {
        if (dir.exists() && dir.isDirectory()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    if (childFileNames[x].endsWith("bar")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void loadGenomesFromFileSystem(final String dataRoot, final Map<String, List<AnnotatedSeqGroup>> organisms, final String org_order_filename) throws IOException {
        final File top_level = new File(dataRoot);
        if (!top_level.exists() && !top_level.isDirectory()) {
            throw new IOException("'" + top_level + "' does not exist or is not a directory");
        }
        final FileFilter filter = (FileFilter)new HiddenFileFilter((FileFilter)new DirectoryFilter());
        for (final File org : top_level.listFiles(filter)) {
            for (final File version : org.listFiles(filter)) {
                this.loadGenome(version, org.getName(), dataRoot);
            }
        }
        ServerUtils.sortGenomes((Map)organisms, org_order_filename);
    }

    private void loadGenome(final File genome_directory, final String organism, final String dataRoot) throws IOException {
        final String genome_version = genome_directory.getName();
        ServerUtils.parseChromosomeData(genome_directory, genome_version);
        final AnnotatedSeqGroup genome = GenometryDas2Servlet.gmodel.getSeqGroup(genome_version);
        if (genome == null) {
            return;
        }
        this.genome2graphdirs.put(genome, new LinkedHashMap<String, String>());
        this.genome2graphfiles.put(genome, new LinkedHashMap<String, String>());
        genome.setOrganism(organism);
        List<AnnotatedSeqGroup> versions = GenometryDas2Servlet.organisms.get(organism);
        if (versions == null) {
            versions = new ArrayList<AnnotatedSeqGroup>();
            GenometryDas2Servlet.organisms.put(organism, versions);
        }
        versions.add(genome);
        final Map<String, String> graph_name2dir = this.genome2graphdirs.get(genome);
        final Map<String, String> graph_name2file = this.genome2graphfiles.get(genome);
        ServerUtils.loadAnnots(genome_directory, genome, (Map)GenometryDas2Servlet.annots_map, (Map)graph_name2dir, (Map)graph_name2file, dataRoot);
        Optimize.genome(genome);
        System.gc();
    }

    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String path_info = request.getPathInfo();
        final String query = request.getQueryString();
        System.out.println("GenometryDas2Servlet received POST request: ");
        System.out.println("   path: " + path_info);
        System.out.println("   query: " + query);
    }

    public void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GenometryDas2Servlet received PUT request: ");
    }

    public long getLastModified(final HttpServletRequest request) {
        return this.date_initialized;
    }

    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.handleDas2Request(response, request);
    }

    private void handleDas2Request(final HttpServletResponse response, final HttpServletRequest request) throws IOException {
        final String path_info = request.getPathInfo();
        final String query = request.getQueryString();
        System.out.println("GenometryDas2Servlet received GET request: ");
        System.out.println("   path: " + path_info);
        System.out.println("   query: " + query);
        if (query != null) {
            System.out.println("   decoded: " + GeneralUtils.URLDecode(query));
        }
        if (path_info == null || path_info.trim().length() == 0 || path_info.endsWith(GenometryDas2Servlet.sources_query_no_slash) || path_info.endsWith(GenometryDas2Servlet.sources_query_with_slash)) {
            handleSourcesRequest(request, response, this.date_init_string);
        }
        else if (path_info.endsWith("login")) {
            this.handleLoginRequest(request, response);
        }
        else if (path_info.endsWith("refresh")) {
            this.handleRefreshRequest(request, response);
        }
        else {
            final AnnotatedSeqGroup genome = getGenome(path_info);
            if (genome == null) {
                response.sendError(400, "Query was not recognized, possibly the genome name is incorrect or missing from path? See http://netaffxdas.affymetrix.com/das2 for proper query syntax.");
                return;
            }
            final String das_command = path_info.substring(path_info.lastIndexOf("/") + 1);
            if (das_command.equals("segments")) {
                handleSegmentsRequest(genome, request, response);
            }
            else if (das_command.equals("types")) {
                this.handleTypesRequest(genome, request, response);
            }
            else if (das_command.equals("features")) {
                this.handleFeaturesRequest(genome, request, response);
            }
            else if (genome.getSeq(das_command) != null) {
                this.handleSequenceRequest(genome, request, response);
            }
            else {
                System.out.println("DAS2 request " + path_info + " not recognized, setting HTTP status header to 400, BAD_REQUEST");
                response.sendError(400, "Query was not recognized. See http://netaffxdas.affymetrix.com/das2 for proper query syntax.");
            }
        }
    }

    private AnnotSecurity getAnnotSecurity(final HttpServletRequest request) {
        AnnotSecurity annotSecurity = null;
        if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
            annotSecurity = (AnnotSecurity)this.getGenoPubSecurity(request);
        }
        else if (GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
            annotSecurity = (AnnotSecurity)this.getGNomExSecurity(request);
        }
        return annotSecurity;
    }

    private GenoPubSecurity getGenoPubSecurity(final HttpServletRequest request) {
        if (!GenometryDas2Servlet.genometry_mode.equals("genopub")) {
            return null;
        }
        GenoPubSecurity genoPubSecurity = null;
        Session sess = null;
        try {
            genoPubSecurity = GenoPubSecurity.class.cast(request.getSession().getAttribute(this.getClass().getName() + "GenoPubSecurity"));
            if (genoPubSecurity == null) {
                sess = (Session)com.affymetrix.genometry.genopub.HibernateUtil.getSessionFactory().openSession();
                genoPubSecurity = new GenoPubSecurity(sess, (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : null, true, request.getUserPrincipal() != null && request.isUserInRole("admin"), request.getUserPrincipal() == null || request.isUserInRole("guest"), false);
                genoPubSecurity.setBaseURL(request.getRequestURL().toString(), request.getServletPath(), request.getPathInfo());
                request.getSession().setAttribute(this.getClass().getName() + "GenoPubSecurity", (Object)genoPubSecurity);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        finally {
			if (sess != null) {
            sess.close();
			}
        }
        return genoPubSecurity;
    }

    private GNomExSecurity getGNomExSecurity(final HttpServletRequest request) {
        if (!GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
            return null;
        }
        GNomExSecurity gnomexSecurity = null;
        Session sess = null;
        try {
            gnomexSecurity = GNomExSecurity.class.cast(request.getSession().getAttribute(this.getClass().getName() + "GNomExSecurity"));
            if (gnomexSecurity == null) {
                sess = (Session)HibernateUtil.getSessionFactory().openSession();
                final SecurityAdvisor secAdvisor = SecurityAdvisor.create(sess, request.getUserPrincipal().getName());
                gnomexSecurity = new GNomExSecurity(sess, request.getServerName(), secAdvisor, true);
                final ServletContext context = this.getServletContext();
                final String gnomex_server_name = context.getInitParameter("gnomex_server_name");
                final String gnomex_port = context.getInitParameter("gnomex_server_port");
                gnomexSecurity.setDataTrackInfoURL(gnomex_server_name, gnomex_port);
                request.getSession().setAttribute(this.getClass().getName() + "GNomExSecurity", (Object)gnomexSecurity);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        finally {
			if (sess != null) {
            	sess.close();
			}
        }
        return gnomexSecurity;
    }

    private static AnnotatedSeqGroup getGenome(final String path_info) {
        final int last_slash = path_info.lastIndexOf(47);
        final int prev_slash = path_info.lastIndexOf(47, last_slash - 1);
        final String genome_name = path_info.substring(prev_slash + 1, last_slash);
        final AnnotatedSeqGroup genome = GenometryDas2Servlet.gmodel.getSeqGroup(genome_name);
        if (genome == null) {
            System.out.println("unknown genome version: '" + genome_name + "' with request: " + path_info);
        }
        return genome;
    }

    private void handleSequenceRequest(final AnnotatedSeqGroup genome, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String queryString = request.getQueryString();
        if (queryString == null) {
            System.out.println("No query string, aborting");
            return;
        }
        final List<String> formats = new ArrayList<String>();
        final List<String> ranges = new ArrayList<String>();
        splitSequenceQuery(GeneralUtils.URLDecode(request.getQueryString()), formats, ranges);
        if (ranges.size() > 1) {
            System.out.println("too many range params, aborting");
            return;
        }
        if (formats.size() > 1) {
            System.out.println("too many format params, aborting");
            return;
        }
        final String path_info = request.getPathInfo();
        final String seqname = path_info.substring(path_info.lastIndexOf("/") + 1);
        SeqSpan span = null;
        if (ranges.size() == 1) {
            span = ServerUtils.getLocationSpan(seqname, (String)ranges.get(0), genome);
        }
        String format = "";
        if (formats.size() == 1) {
            format = formats.get(0);
        }
        String sequence_directory = GenometryDas2Servlet.data_root + genome.getOrganism() + "/" + genome.getID() + "/dna/";
        Label_0289: {
            if (!GenometryDas2Servlet.genometry_mode.equals("genopub")) {
                if (!GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
                    break Label_0289;
                }
            }
            try {
                final AnnotSecurity annotSecurity = this.getAnnotSecurity(request);
                sequence_directory = annotSecurity.getSequenceDirectory(GenometryDas2Servlet.data_root, genome);
            }
            catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
        if (format.equals("raw")) {
            ServletUtils.retrieveRAW(ranges, span, sequence_directory, seqname, response, request);
            return;
        }
        if (format.equals("bnib")) {
            ServletUtils.retrieveBNIB(sequence_directory, seqname, response, request);
            return;
        }
        if (format.equals("fasta")) {
            ServletUtils.retrieveFASTA(ranges, span, sequence_directory, genome.getOrganism(), seqname, response, request);
            return;
        }
        final PrintWriter pw = response.getWriter();
        pw.println("This DAS/2 server cannot currently handle request:    ");
        pw.println(request.getRequestURL().toString());
    }

    private static void handleSourcesRequest(final HttpServletRequest request, final HttpServletResponse response, final String date_init_string) throws IOException {
        response.setContentType("application/x-das-sources+xml");
        final PrintWriter pw = response.getWriter();
        final String xbase = getXmlBase(request);
        printXmlDeclaration(pw);
        pw.println("<SOURCES");
        pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
        pw.println("    xml:base=\"" + xbase + "\" >");
        if (GenometryDas2Servlet.maintainer_email != null && GenometryDas2Servlet.maintainer_email.length() > 0) {
            pw.println("  <MAINTAINER email=\"" + GenometryDas2Servlet.maintainer_email + "\" />");
        }
        for (final Map.Entry<String, List<AnnotatedSeqGroup>> oentry : GenometryDas2Servlet.organisms.entrySet()) {
            final String org = oentry.getKey();
            final List<AnnotatedSeqGroup> versions = oentry.getValue();
            pw.println("  <SOURCE uri=\"" + org + "\" title=\"" + org + "\" >");
            for (final AnnotatedSeqGroup genome : versions) {
                final Das2Coords coords = GenometryDas2Servlet.genomeid2coord.get(genome.getID());
                pw.println("      <VERSION uri=\"" + genome.getID() + "\" title=\"" + genome.getID() + "\" created=\"" + date_init_string + "\" >");
                if (coords != null) {
                    pw.println("           <COORDINATES uri=\"" + coords.getURI() + "\" authority=\"" + coords.getAuthority() + "\" taxid=\"" + coords.getTaxid() + "\" version=\"" + coords.getVersion() + "\" source=\"" + coords.getSource() + "\" />");
                }
                pw.println("           <CAPABILITY type=\"segments\" query_uri=\"" + genome.getID() + "/" + "segments" + "\" />");
                pw.println("           <CAPABILITY type=\"types\" query_uri=\"" + genome.getID() + "/" + "types" + "\" />");
                pw.println("           <CAPABILITY type=\"features\" query_uri=\"" + genome.getID() + "/" + "features" + "\" />");
                pw.println("      </VERSION>");
            }
            pw.println("  </SOURCE>");
        }
        pw.println("</SOURCES>");
    }

    private static void handleSegmentsRequest(final AnnotatedSeqGroup genome, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Das2Coords coords = GenometryDas2Servlet.genomeid2coord.get(genome.getID());
        response.setContentType("application/x-das-segments+xml");
        final PrintWriter pw = response.getWriter();
        printXmlDeclaration(pw);
        final String xbase = getXmlBase(request) + genome.getID() + "/";
        final String segments_uri = xbase + "segments";
        pw.println("<SEGMENTS ");
        pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
        pw.println("    xml:base=\"" + xbase + "\" ");
        pw.println("    uri=\"" + segments_uri + "\" >");
        for (final BioSeq aseq : genome.getSeqList()) {
            String refatt = "";
            if (coords != null) {
                final String ref = coords.getURI() + "dna/" + aseq.getID();
                refatt = "reference=\"" + ref + "\"";
            }
            pw.println("   <SEGMENT uri=\"" + aseq.getID() + "\" " + "title" + "=\"" + aseq.getID() + "\"" + " length=\"" + aseq.getLength() + "\" " + refatt + " />");
        }
        pw.println("</SEGMENTS>");
    }

    private void handleTypesRequest(final AnnotatedSeqGroup genome, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("application/x-das-types+xml");
        final Map<String, SimpleDas2Type> types_hash = (Map<String, SimpleDas2Type>)ServerUtils.getAnnotationTypes(GenometryDas2Servlet.data_root, genome, this.getAnnotSecurity(request));
        ServerUtils.getSymloaderTypes(genome, this.getAnnotSecurity(request), (Map)types_hash);
        ServerUtils.getGraphTypes(GenometryDas2Servlet.data_root, genome, this.getAnnotSecurity(request), (Map)types_hash);
        ByteArrayOutputStream buf = null;
        ByteArrayInputStream bais = null;
        PrintWriter pw = null;
        try {
            if (this.use_types_xslt) {
                buf = new ByteArrayOutputStream(types_hash.size() * 1000);
                pw = new PrintWriter(buf);
            }
            else {
                pw = response.getWriter();
            }
            final String xbase = getXmlBase(request) + genome.getID() + "/";
            final List<AnnotsXmlParser.AnnotMapElt> annotList = GenometryDas2Servlet.annots_map.get(genome);
            writeTypesXML(pw, xbase, types_hash, annotList);
            if (this.use_types_xslt) {
                pw.flush();
                final byte[] buf_array = buf.toByteArray();
                bais = new ByteArrayInputStream(buf_array);
                final Source types_doc = new StreamSource(bais);
                final Result result = new StreamResult(response.getWriter());
                try {
                    this.types_transformer.transform(types_doc, result);
                }
                catch (TransformerException ex) {
                    ex.printStackTrace();
                }
            }
        }
        finally {
            GeneralUtils.safeClose((Closeable)bais);
            GeneralUtils.safeClose((Closeable)buf);
        }
    }

    private static void writeTypesXML(final PrintWriter pw, final String xbase, final Map<String, SimpleDas2Type> types_hash, final List<AnnotsXmlParser.AnnotMapElt> annotList) {
        printXmlDeclaration(pw);
        pw.println("<TYPES ");
        pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
        pw.println("    xml:base=\"" + xbase + "\" >");
        final List<String> sorted_types_list = new ArrayList<String>(types_hash.keySet());
        Collections.sort(sorted_types_list);
        for (final String feat_type : sorted_types_list) {
            final SimpleDas2Type das2Type = types_hash.get(feat_type);
            final List<String> formats = (List<String>)das2Type.getFormats();
            Map<String, Object> props = (Map<String, Object>)das2Type.getProps();
            String feat_type_encoded = GeneralUtils.URLEncode(feat_type);
            feat_type_encoded = feat_type_encoded.replaceAll("%2F", "/");
            final String title = feat_type;
            pw.println("   <TYPE uri=\"" + feat_type_encoded + "\" " + "title" + "=\"" + title + "\" >");
            if (formats != null) {
                for (final String format : formats) {
                    pw.println("       <FORMAT name=\"" + format + "\" />");
                }
            }
            if (props == null && annotList != null) {
                final AnnotsXmlParser.AnnotMapElt ame = AnnotsXmlParser.AnnotMapElt.findTitleElt(title, (List)annotList);
                if (ame != null && ame.props != null) {
                    props = new HashMap<String, Object>();
                    for (final Map.Entry<String, String> propEntry : ame.props.entrySet()) {
                        if (propEntry.getValue().length() > 0) {
                            props.put(propEntry.getKey(), propEntry.getValue());
                        }
                    }
                }
            }
            if (props != null) {
                for (final Map.Entry<String, Object> entry : props.entrySet()) {
                    final Object value = entry.getValue();
                    if (value != null && !value.equals("")) {
                        pw.println("       <PROP key=\"" + entry.getKey() + "\" value=\"" + value + "\" />");
                    }
                }
            }
            pw.println("   </TYPE>");
        }
        pw.println("</TYPES>");
    }

    private void handleLoginRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final boolean authorized = true;
        response.setContentType("application/x-das-login+xml");
        final PrintWriter pw = response.getWriter();
        final String xbase = getXmlBase(request);
        printXmlDeclaration(pw);
        pw.println("<LOGIN");
        pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
        pw.println("    xml:base=\"" + xbase + "\" >");
        if (GenometryDas2Servlet.maintainer_email != null && GenometryDas2Servlet.maintainer_email.length() > 0) {
            pw.println("  <MAINTAINER email=\"" + GenometryDas2Servlet.maintainer_email + "\" />");
        }
        pw.println("\t<AUTHORIZED>" + authorized + "</AUTHORIZED>");
        if (request.getUserPrincipal() != null) {
            pw.println("\t<USERNAME>" + request.getUserPrincipal().getName() + "</USERNAME>");
        }
        pw.println("</LOGIN>");
    }

    private void handleRefreshRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (GenometryDas2Servlet.genometry_mode.equals("classic")) {
            final PrintWriter pw = response.getWriter();
            pw.println("DAS/2 refresh is not supported in classic mode");
            return;
        }
        if (this.getAnnotSecurity(request).isGuestRole()) {
            final PrintWriter pw = response.getWriter();
            pw.println("DAS/2 refresh cannot by performed by guest users.");
            return;
        }
        Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.INFO, "Refreshing DAS2 server.  User: {0}", request.getUserPrincipal().getName());

		Session sess = null;
        try {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Loading genomes from relational database....");
            Logger.getLogger(GenometryDas2Servlet.class.getName()).info("Refreshing authorized resources....");

            if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
                sess = (Session)com.affymetrix.genometry.genopub.HibernateUtil.getSessionFactory().openSession();
                this.loadGenomesFromGenoPub(this.getGenoPubSecurity(request), true);
                this.getGenoPubSecurity(request).loadAuthorizedResources(sess);
            }
            else if (GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
                sess = (Session)HibernateUtil.getSessionFactory().openSession();
                this.loadGenomesFromGNomEx(this.getGNomExSecurity(request), true);
                this.getGNomExSecurity(request).loadAuthorizedResources(sess);
            }
        }
        catch (Exception e) {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.SEVERE, "ERROR - problems refreshing annotations {0}", e.toString());
            e.printStackTrace();
        }
        finally {
            if (GenometryDas2Servlet.genometry_mode.equals("genopub")) {
				if (sess != null) {
                sess.close();
				}
            }
            else if (GenometryDas2Servlet.genometry_mode.equals("gnomex")) {
				if (sess != null) {
                sess.close();
				}
            }
        }
        response.setContentType("application/x-das-login+xml");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
        response.setHeader("Cache-Control", "max-age=0, no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        final PrintWriter pw = response.getWriter();
        final String xbase = getXmlBase(request);
        printXmlDeclaration(pw);
        pw.println("<REFRESH");
        pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
        pw.println("    xml:base=\"" + xbase + "\" >");
        if (GenometryDas2Servlet.maintainer_email != null && GenometryDas2Servlet.maintainer_email.length() > 0) {
            pw.println("  <MAINTAINER email=\"" + GenometryDas2Servlet.maintainer_email + "\" />");
        }
        pw.println("</REFRESH>");
        this.date_initialized = System.currentTimeMillis();
        this.date_init_string = this.date_formatter.format(new Date(this.date_initialized));
    }

    private void handleFeaturesRequest(final AnnotatedSeqGroup genome, final HttpServletRequest request, final HttpServletResponse response) {
        final String query = request.getQueryString();
        final String xbase = getXmlBase(request);
        String output_format = "das2feature";
        String query_type = null;
        SeqSpan overlap_span = null;
        SeqSpan inside_span = null;
        List<SeqSymmetry> result = null;
        BioSeq outseq = null;
        Class<? extends AnnotationWriter> writerclass = null;
        if (query != null) {
            if (query.length() != 0) {
                final List<String> formats = new ArrayList<String>();
                final List<String> types = new ArrayList<String>();
                final List<String> segments = new ArrayList<String>();
                final List<String> overlaps = new ArrayList<String>();
                final List<String> insides = new ArrayList<String>();
                final List<String> excludes = new ArrayList<String>();
                final List<String> names = new ArrayList<String>();
                final List<String> coordinates = new ArrayList<String>();
                final List<String> links = new ArrayList<String>();
                final List<String> notes = new ArrayList<String>();
                final Map<String, ArrayList<String>> props = new HashMap<String, ArrayList<String>>();
                final boolean known_query = splitFeaturesQuery(GeneralUtils.URLDecode(query), formats, types, segments, overlaps, insides, excludes, names, coordinates, links, notes, props);
                if (formats.size() == 1) {
                    output_format = formats.get(0);
                }
                writerclass = this.output_registry.get(output_format);
                if (!known_query) {
                    result = null;
                }
                else if (formats.size() > 1) {
                    result = null;
                }
                else if (links.size() > 0 || notes.size() > 0 || props.size() > 0) {
                    result = new ArrayList<SeqSymmetry>();
                }
                else if (names != null && names.size() == 1) {
                    BioSeq seq = null;
                    if (segments.size() == 1) {
                        String seqid = segments.get(0);
                        System.out.println("seqid is " + seqid);
                        final int sindex = seqid.lastIndexOf("/");
                        if (sindex >= 0) {
                            seqid = seqid.substring(sindex + 1);
                        }
                        seq = genome.getSeq(seqid);
                        System.out.println(("Seq is " + seq == null) ? null : seq.getID());
                    }
                    handleNameQuery(names, genome, seq, writerclass, response, xbase);
                    return;
                }
                if (types.size() == 1 && segments.size() == 1 && overlaps.size() <= 1 && insides.size() <= 1 && excludes.isEmpty() && names.isEmpty()) {
                    String seqid2 = segments.get(0);
                    final int sindex2 = seqid2.lastIndexOf("/");
                    if (sindex2 >= 0) {
                        seqid2 = seqid2.substring(sindex2 + 1);
                    }
                    final String type_full_uri = types.get(0);
                    query_type = getInternalType(type_full_uri, genome);
                    String overlap = null;
                    if (overlaps.size() == 1) {
                        overlap = overlaps.get(0);
                    }
                    overlap_span = ServerUtils.getLocationSpan(seqid2, overlap, genome);
                    if (overlap_span != null) {
                        final Map<String, String> graph_name2dir = this.genome2graphdirs.get(genome);
                        final Map<String, String> graph_name2file = this.genome2graphfiles.get(genome);
                        if (formats.contains("useq")) {
                            if (graph_name2file.containsKey(query_type)) {
                                this.handleUSeqRequest(output_format, response, new File(graph_name2file.get(query_type)), overlap_span);
                            }
                            else {
                                result = null;
                                System.out.println("  ***** Call for a useq file that doesn't exist? Aborting. *****  ");
                            }
                            return;
                        }
                        if (insides.size() == 1) {
                            final String inside = insides.get(0);
                            inside_span = ServerUtils.getLocationSpan(seqid2, inside, genome);
                        }
                        outseq = overlap_span.getBioSeq();
                        if (formats.contains("bam")) {
                            this.handleBamRequest(query_type, outseq, overlap_span, inside_span, response);
                            return;
                        }
                        if (graph_name2dir.get(query_type) != null || graph_name2file.get(query_type) != null || query_type.endsWith(".bar")) {
                            this.handleGraphRequest(this.output_registry, xbase, response, query_type, overlap_span);
                            return;
                        }
                        result = (List<SeqSymmetry>)ServerUtils.getIntersectedSymmetries(overlap_span, query_type, inside_span);
                    }
                }
                else {
                    result = null;
                    System.out.println("  ***** query combination not supported, throwing an error");
                }
            }
        }
        OutputTheDataTracks(writerclass, output_format, response, result, outseq, query_type, xbase);
    }

    private void handleUSeqRequest(final String outputFormat, final HttpServletResponse response, final File useqArchiveFile, final SeqSpan overlapSpan) {
        OutputStream outputStream = null;
        try {
            final String chromosome = overlapSpan.getBioSeq().getID();
            final int start = overlapSpan.getStart();
            final int end = overlapSpan.getEnd();
            USeqArchive useqArchive = this.file2USeqArchive.get(useqArchiveFile.toString());
            if (useqArchive == null) {
                useqArchive = new USeqArchive(useqArchiveFile);
                this.file2USeqArchive.put(useqArchiveFile.toString(), useqArchive);
            }
            response.setContentType("binary/useq");
            outputStream = (OutputStream)response.getOutputStream();
            final boolean wrote = useqArchive.writeSlicesToStream(outputStream, chromosome, start, end, true);
            if (!wrote) {
                response.setStatus(404);
                final PrintWriter pw = response.getWriter();
                pw.println("DAS/2 server could not find useq data for " + chromosome + ":" + start + "-" + end + " from " + useqArchiveFile.getName());
                pw.close();
            }
            else {
                System.out.println("Wrote useq data to stream for " + chromosome + ":" + start + "-" + end + " from " + useqArchiveFile.getName());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(503);
            try {
                final PrintWriter pw2 = response.getWriter();
                pw2.println("The DAS/2 server encountered an error while attempting to fetch useq data from " + useqArchiveFile.getName());
                pw2.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        GeneralUtils.safeClose((Closeable)outputStream);
    }

    private static void splitSequenceQuery(final String query, final List<String> formats, final List<String> ranges) {
        for (final String tagval : GenometryDas2Servlet.query_splitter.split(query)) {
            final String[] tagval_array = GenometryDas2Servlet.tagval_splitter.split(tagval);
            final String tag = tagval_array[0];
            final String val = tagval_array[1];
            if (tag.equals("format")) {
                formats.add(val);
            }
            else if (tag.equals("range")) {
                ranges.add(val);
            }
        }
    }

    private static boolean splitFeaturesQuery(final String query, final List<String> formats, final List<String> types, final List<String> segments, final List<String> overlaps, final List<String> insides, final List<String> excludes, final List<String> names, final List<String> coordinates, final List<String> links, final List<String> notes, final Map<String, ArrayList<String>> props) {
        boolean known_query = true;
        for (final String tagval : GenometryDas2Servlet.query_splitter.split(query)) {
            final String[] tagval_array = GenometryDas2Servlet.tagval_splitter.split(tagval);
            final String tag = tagval_array[0];
            final String val = tagval_array[1];
            if (tag.equals("format")) {
                formats.add(val);
            }
            else if (tag.equals("type")) {
                types.add(val);
            }
            else if (tag.equals("segment")) {
                segments.add(val);
            }
            else if (tag.equals("overlaps")) {
                overlaps.add(val);
            }
            else if (tag.equals("inside")) {
                insides.add(val);
            }
            else if (tag.equals("excludes")) {
                excludes.add(val);
            }
            else if (tag.equals("name")) {
                names.add(val);
            }
            else if (tag.equals("coordinates")) {
                coordinates.add(val);
            }
            else if (tag.equals("link")) {
                links.add(val);
            }
            else if (tag.equals("note")) {
                notes.add(val);
            }
            else if (tag.startsWith("prop-")) {
                final String pkey = tag.substring(5);
                ArrayList<String> vlist = props.get(pkey);
                if (vlist == null) {
                    vlist = new ArrayList<String>();
                    props.put(pkey, vlist);
                }
                vlist.add(val);
            }
            else {
                known_query = false;
            }
        }
        return known_query;
    }

    private static void handleNameQuery(final List<String> names, final AnnotatedSeqGroup genome, final BioSeq seq, final Class<? extends AnnotationWriter> writerclass, final HttpServletResponse response, final String xbase) {
        final String name = names.get(0);
        final Set<SeqSymmetry> result = (Set<SeqSymmetry>)SearchUtils.findNameInGenome(name, genome);
        OutputStream outstream = null;
        try {
            final AnnotationWriter writer = (AnnotationWriter)writerclass.newInstance();
            final String mime_type = writer.getMimeType();
            if (writer instanceof Das2FeatureSaxParser) {
                ((Das2FeatureSaxParser)writer).setBaseURI(new URI(xbase));
            }
            response.setContentType(mime_type);
            outstream = (OutputStream)response.getOutputStream();
            if (seq != null) {
                writer.writeAnnotations((Collection)result, seq, (String)null, outstream);
            }
            else {
                for (final BioSeq tempSeq : genome.getSeqList()) {
                    writer.writeAnnotations((Collection)result, tempSeq, (String)null, outstream);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose((Closeable)outstream);
        }
    }

    private void handleGraphRequest(final Map<String, Class<? extends AnnotationWriter>> output_registry, final String xbase, final HttpServletResponse response, final String type, final SeqSpan span) {
        final BioSeq seq = span.getBioSeq();
        final String seqid = seq.getID();
        final AnnotatedSeqGroup genome = seq.getSeqGroup();
        final Map<String, String> graph_name2dir = this.genome2graphdirs.get(genome);
        final Map<String, String> graph_name2file = this.genome2graphfiles.get(genome);
        final String file_path = DetermineFilePath(graph_name2dir, graph_name2file, type, seqid);
        OutputGraphSlice(output_registry.get("bar"), file_path, span, type, xbase, response);
    }

    private static String DetermineFilePath(final Map<String, String> graph_name2dir, final Map<String, String> graph_name2file, final String graph_name, final String seqid) {
        String file_path = graph_name2dir.get(graph_name);
        if (file_path != null) {
            file_path = file_path + "/" + seqid + ".bar";
        }
        else {
            file_path = graph_name2file.get(graph_name);
            if (file_path == null) {
                file_path = graph_name;
            }
        }
        if (file_path.startsWith("file:")) {
            file_path = file_path.substring(5);
        }
        return file_path;
    }

    private static void OutputGraphSlice(final Class<? extends AnnotationWriter> writerclass, final String file_path, final SeqSpan span, final String type, final String xbase, final HttpServletResponse response) {
        GraphSym graf = null;
        try {
            graf = BarParser.getRegion(file_path, span);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (graf != null) {
            final List<SeqSymmetry> gsyms = new ArrayList<SeqSymmetry>();
            gsyms.add((SeqSymmetry)graf);
            System.out.println("#### returning graph slice in bar format");
            outputDataTracks(gsyms, span.getBioSeq(), type, xbase, response, writerclass, "bar");
        }
        else {
            System.out.println("####### problem with retrieving graph slice ########");
            response.setStatus(404);
            try {
                final PrintWriter pw = response.getWriter();
                pw.println("DAS/2 server could not find graph to return for type: " + type);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("set status to 404 not found");
        }
    }

    private static void OutputTheDataTracks(final Class<? extends AnnotationWriter> writerclass, final String output_format, final HttpServletResponse response, final List<SeqSymmetry> result, final BioSeq outseq, final String query_type, final String xbase) {
        try {
            System.out.println("overlapping annotations found: " + (Object)((result == null) ? null : result.size()));
            if (result == null) {
                response.sendError(413, "Query could not be handled. See http://netaffxdas.affymetrix.com/das2 for supported feature queries.");
            }
            else {
                outputDataTracks(result, outseq, query_type, xbase, response, writerclass, output_format);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getInternalType(final String full_type_uri, final AnnotatedSeqGroup genome) {
        String query_type = GeneralUtils.URLDecode(full_type_uri);
        if (!query_type.endsWith(".bar")) {
            final String gid = genome.getID();
            final int gindex = query_type.indexOf(gid);
            if (gindex >= 0) {
                query_type = query_type.substring(gindex + gid.length() + 1);
            }
        }
        return query_type;
    }

    private static boolean outputDataTracks(final List<SeqSymmetry> syms, final BioSeq seq, final String annot_type, final String xbase, final HttpServletResponse response, final Class<? extends AnnotationWriter> writerclass, final String format) {
        try {
            if (writerclass == null) {
                System.out.println("no AnnotationWriter found for format: " + format);
                response.setStatus(400);
                return false;
            }
            final AnnotationWriter writer = (AnnotationWriter)writerclass.newInstance();
            final String mime_type = writer.getMimeType();
            if (writer instanceof Das2FeatureSaxParser) {
                ((Das2FeatureSaxParser)writer).setBaseURI(new URI(xbase));
            }
            response.setContentType(mime_type);
            final OutputStream outstream = (OutputStream)response.getOutputStream();
            try {
                return writer.writeAnnotations((Collection)syms, seq, annot_type, outstream);
            }
            finally {
                GeneralUtils.safeClose((Closeable)outstream);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void printXmlDeclaration(final PrintWriter pw) {
        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    static void setXmlBase(final String xbase) {
        GenometryDas2Servlet.xml_base = xbase;
        String trimmed_xml_base;
        if (GenometryDas2Servlet.xml_base.endsWith("/")) {
            trimmed_xml_base = GenometryDas2Servlet.xml_base.substring(0, GenometryDas2Servlet.xml_base.length() - 1);
        }
        else {
            trimmed_xml_base = GenometryDas2Servlet.xml_base;
            GenometryDas2Servlet.xml_base += "/";
        }
        GenometryDas2Servlet.sources_query_no_slash = trimmed_xml_base.substring(trimmed_xml_base.lastIndexOf("/"));
        GenometryDas2Servlet.sources_query_with_slash = GenometryDas2Servlet.sources_query_no_slash + "/";
    }

    private static String getXmlBase(final HttpServletRequest request) {
        if (GenometryDas2Servlet.xml_base != null) {
            return GenometryDas2Servlet.xml_base;
        }
        return request.getRequestURL().toString();
    }

    private void handleBamRequest(final String query_type, final BioSeq seq, final SeqSpan overlap_span, final SeqSpan inside_span, final HttpServletResponse response) {
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            final BAM bamfile = (BAM)seq.getSymLoader(query_type);
            response.setContentType(bamfile.getMimeType());
            bos = new BufferedOutputStream((OutputStream)response.getOutputStream());
            dos = new DataOutputStream(bos);
            bamfile.writeAnnotations(seq, overlap_span.getMin(), overlap_span.getMax(), dos, true);
        }
        catch (Exception ex) {
            Logger.getLogger(GenometryDas2Servlet.class.getName()).log(Level.SEVERE, "Unable to load bam file", ex);
        }
        finally {
            GeneralUtils.safeClose((Closeable)bos);
            GeneralUtils.safeClose((Closeable)dos);
        }
    }

    static {
        (genomeid2coord = new HashMap<String, Das2Coords>()).put("H_sapiens_Mar_2006", new Das2Coords("http://www.ncbi.nlm.nih.gov/genome/H_sapiens/B36.1/", "NCBI", "9606", "36", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("Human_Mar_2006", new Das2Coords("http://www.ncbi.nlm.nih.gov/genome/H_sapiens/B36.1/", "NCBI", "9606", "36", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("H_sapiens_May_2004", new Das2Coords("http://www.ncbi.nlm.nih.gov/genome/H_sapiens/B35.1/", "NCBI", "9606", "35", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("Human_May_2004", new Das2Coords("http://www.ncbi.nlm.nih.gov/genome/H_sapiens/B35.1/", "NCBI", "9606", "35", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("D_melanogaster_Apr_2004", new Das2Coords("http://www.flybase.org/genome/D_melanogaster/R3.1/", "BDGP", "7227", "4", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("Drosophila_Apr_2004", new Das2Coords("http://www.flybase.org/genome/D_melanogaster/R3.1/", "BDGP", "7227", "4", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("D_rerio_Jul_2007", new Das2Coords("http://zfin.org/genome/D_rerio/Zv7/", "ZFISH_7", "7955", "Zv7", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("C_elegans_Jan_2007", new Das2Coords("http://www.wormbase.org/genome/C_elegans/WS180/", "WS", "6239", "180", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("S_pombe_Apr_2007", new Das2Coords("http://www.sanger.ac.uk/Projects/S_pombe/Apr_2007", "Sanger", "4896", "Apr_2007", "Chromosome", null));
        GenometryDas2Servlet.genomeid2coord.put("S_glossinidius_Jan_2006", new Das2Coords("ftp://ftp.ncbi.nih.gov/genomes/Bacteria/Sodalis_glossinidius_morsitans/Jan_2006", "NCBI", "343509", "Jan_2006", "Chromosome", null));
        GenometryDas2Servlet.sources_query_with_slash = "";
        GenometryDas2Servlet.sources_query_no_slash = "";
        GenometryDas2Servlet.genometry_mode = "genometry_mode";
        query_splitter = Pattern.compile("[;\\&]");
        tagval_splitter = Pattern.compile("=");
        GenometryDas2Servlet.gmodel = GenometryModel.getGenometryModel();
        GenometryDas2Servlet.organisms = new LinkedHashMap<String, List<AnnotatedSeqGroup>>();
        GenometryDas2Servlet.annots_map = new HashMap<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>>();
    }
}
