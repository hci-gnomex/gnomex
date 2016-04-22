//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.parsers.useq.apps.USeq2UCSCBig;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.Closeable;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.FileWriter;
import javax.servlet.ServletOutputStream;
import java.util.Calendar;
import java.util.UUID;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.util.zip.ZipEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.util.zip.ZipOutputStream;
import java.math.BigDecimal;
import java.io.BufferedReader;
import java.io.FileReader;
import net.sf.samtools.SAMRecordIterator;
import net.sf.samtools.SAMSequenceRecord;
import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import java.text.SimpleDateFormat;
import java.io.Writer;
import org.dom4j.io.HTMLWriter;
import org.dom4j.DocumentHelper;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.MultipartParser;
import java.util.Iterator;
import java.util.logging.Level;
import org.dom4j.Element;
import java.io.Reader;
import org.dom4j.io.SAXReader;
import java.io.StringReader;
import java.util.Set;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import org.hibernate.Transaction;
import org.dom4j.Document;
import java.io.OutputStream;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import java.io.Serializable;
import org.hibernate.Session;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.io.File;
import javax.servlet.http.HttpServlet;
import net.sf.samtools.*;


public class GenoPubServlet extends HttpServlet
{
    public static final String GENOPUB_WEBAPP_NAME = "genopub";
    private static final String GENOPUB_HTML_WRAPPER = "GenoPub.html";
    private static final String REALM = "Das2";
    private static final int ERROR_CODE_OTHER = 901;
    private static final int ERROR_CODE_UNSUPPORTED_FILE_TYPE = 902;
    private static final int ERROR_CODE_INCORRECT_FILENAME = 903;
    private static final int ERROR_CODE_INSUFFICIENT_PERMISSIONS = 904;
    private static final int ERROR_CODE_FILE_TOO_BIG = 905;
    private static final int ERROR_CODE_MALFORMED_BAM_FILE = 906;
    private static final int ERROR_CODE_INVALID_NAME = 907;
    private static final int ERROR_CODE_BULK_FILE_UPLOAD = 908;
    private static final String SESSION_DOWNLOAD_KEYS = "genopubDownloadKeys";
    public static final String SECURITY_REQUEST = "security";
    public static final String DICTIONARIES_REQUEST = "dictionaries";
    public static final String ANNOTATIONS_REQUEST = "annotations";
    public static final String ANNOTATION_REQUEST = "annotation";
    public static final String ORGANISM_ADD_REQUEST = "organismAdd";
    public static final String ORGANISM_UPDATE_REQUEST = "organismUpdate";
    public static final String ORGANISM_DELETE_REQUEST = "organismDelete";
    public static final String GENOME_VERSION_REQUEST = "genomeVersion";
    public static final String GENOME_VERSION_ADD_REQUEST = "genomeVersionAdd";
    public static final String GENOME_VERSION_UPDATE_REQUEST = "genomeVersionUpdate";
    public static final String GENOME_VERSION_DELETE_REQUEST = "genomeVersionDelete";
    public static final String SEGMENT_IMPORT_REQUEST = "segmentImport";
    public static final String SEQUENCE_FORM_UPLOAD_URL_REQUEST = "sequenceUploadURL";
    public static final String SEQUENCE_UPLOAD_FILES_REQUEST = "sequenceUploadFiles";
    public static final String ANNOTATION_GROUPING_ADD_REQUEST = "annotationGroupingAdd";
    public static final String ANNOTATION_GROUPING_UPDATE_REQUEST = "annotationGroupingUpdate";
    public static final String ANNOTATION_GROUPING_MOVE_REQUEST = "annotationGroupingMove";
    public static final String ANNOTATION_GROUPING_DELETE_REQUEST = "annotationGroupingDelete";
    public static final String ANNOTATION_ADD_REQUEST = "annotationAdd";
    public static final String ANNOTATION_UPDATE_REQUEST = "annotationUpdate";
    public static final String ANNOTATION_DUPLICATE_REQUEST = "annotationDuplicate";
    public static final String ANNOTATION_DELETE_REQUEST = "annotationDelete";
    public static final String ANNOTATION_UNLINK_REQUEST = "annotationUnlink";
    public static final String ANNOTATION_MOVE_REQUEST = "annotationMove";
    public static final String ANNOTATION_INFO_REQUEST = "annotationInfo";
    public static final String ANNOTATION_FORM_UPLOAD_URL_REQUEST = "annotationUploadURL";
    public static final String ANNOTATION_UPLOAD_FILES_REQUEST = "annotationUploadFiles";
    public static final String ANNOTATION_ESTIMATE_DOWNLOAD_SIZE_REQUEST = "annotationEstimateDownloadSize";
    public static final String ANNOTATION_DOWNLOAD_FILES_REQUEST = "annotationDownloadFiles";
    public static final String ANNOTATION_FDT_DOWNLOAD_FILES_REQUEST = "annotationFDTDownloadFiles";
    public static final String ANNOTATION_FDT_UPLOAD_FILES_REQUEST = "annotationFDTUploadFiles";
    public static final String USERS_AND_GROUPS_REQUEST = "usersAndGroups";
    public static final String USER_ADD_REQUEST = "userAdd";
    public static final String USER_PASSWORD_REQUEST = "userPassword";
    public static final String USER_UPDATE_REQUEST = "userUpdate";
    public static final String USER_DELETE_REQUEST = "userDelete";
    public static final String GROUP_ADD_REQUEST = "groupAdd";
    public static final String GROUP_UPDATE_REQUEST = "groupUpdate";
    public static final String GROUP_DELETE_REQUEST = "groupDelete";
    public static final String DICTIONARY_ADD_REQUEST = "dictionaryAdd";
    public static final String DICTIONARY_UPDATE_REQUEST = "dictionaryUpdate";
    public static final String DICTIONARY_DELETE_REQUEST = "dictionaryDelete";
    public static final String INSTITUTES_REQUEST = "institutes";
    public static final String INSTITUTES_SAVE_REQUEST = "institutesSave";
    public static final String VERIFY_RELOAD_REQUEST = "verifyReload";
    public static final String MAKE_UCSC_LINKS_REQUEST = "makeUCSCLink";
    public static final String MAKE_URL_LINKS_REQUEST = "makeURLLinks";
    private GenoPubSecurity genoPubSecurity;
    private String genometry_genopub_dir;
    private String fdt_dir;
    private String fdt_dir_genopub;
    private String fdt_task_dir;
    private String fdt_client_codebase;
    private String fdt_server_name;
    private File genoPubWebAppDir;
    private HashSet<String> urlLinkFileExtensions;
    private static final Pattern HTML_BRACKETS;
    private static boolean autoConvertUSeqArchives;
    private File ucscWig2BigWigExe;
    private File ucscBed2BigBedExe;
    private static final Pattern BULK_UPLOAD_LINE_SPLITTER;

    public GenoPubServlet() {
        this.genoPubSecurity = null;
        this.urlLinkFileExtensions = null;
    }

    public void init() throws ServletException {
        if (!this.getGenoPubDir()) {
            Logger.getLogger(this.getClass().getName()).severe("FAILED to init() GenoPubServlet, aborting!");
            throw new ServletException("FAILED " + this.getClass().getName() + ".init(), aborting!");
        }
        if (GenoPubServlet.autoConvertUSeqArchives && !this.fetchUCSCExecutableFiles()) {
            GenoPubServlet.autoConvertUSeqArchives = false;
            Logger.getLogger(this.getClass().getName()).warning("FAILED to find the UCSC big file executables, turning off useq auto conversion.");
        }
    }

    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        this.handleRequest(req, res);
    }

    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        this.handleRequest(req, res);
    }

    private void handleRequest(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        try {
            this.genoPubSecurity = GenoPubSecurity.class.cast(req.getSession().getAttribute("GenoPubSecurity"));
            if (this.genoPubSecurity == null) {
                final Session sess = (Session)HibernateUtil.getSessionFactory().openSession();
                this.genoPubSecurity = new GenoPubSecurity(sess, req.getUserPrincipal().getName(), true, req.isUserInRole("admin"), req.isUserInRole("guest"), this.isFDTSupported());
                req.getSession().setAttribute("GenoPubSecurity", (Object)this.genoPubSecurity);
            }
            if (req.getPathInfo() == null) {
                this.handleFlexRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("security")) {
                this.handleSecurityRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("dictionaries")) {
                this.handleDictionaryRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotations")) {
                this.handleAnnotationsRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotation")) {
                this.handleAnnotationRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("organismAdd")) {
                this.handleOrganismAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("organismUpdate")) {
                this.handleOrganismUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("organismDelete")) {
                this.handleOrganismDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("genomeVersion")) {
                this.handleGenomeVersionRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("genomeVersionAdd")) {
                this.handleGenomeVersionAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("genomeVersionUpdate")) {
                this.handleGenomeVersionUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("genomeVersionDelete")) {
                this.handleGenomeVersionDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("segmentImport")) {
                this.handleSegmentImportRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("sequenceUploadURL")) {
                this.handleSequenceFormUploadURLRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("sequenceUploadFiles")) {
                this.handleSequenceUploadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationGroupingAdd")) {
                this.handleAnnotationGroupingAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationGroupingUpdate")) {
                this.handleAnnotationGroupingUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationGroupingMove")) {
                this.handleAnnotationGroupingMoveRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationGroupingDelete")) {
                this.handleAnnotationGroupingDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationAdd")) {
                this.handleAnnotationAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationUpdate")) {
                this.handleAnnotationUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationDuplicate")) {
                this.handleAnnotationDuplicateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationDelete")) {
                this.handleAnnotationDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationUnlink")) {
                this.handleAnnotationUnlinkRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationMove")) {
                this.handleAnnotationMoveRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationInfo")) {
                this.handleAnnotationInfoRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationUploadURL")) {
                this.handleAnnotationFormUploadURLRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationUploadFiles")) {
                this.handleAnnotationUploadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationFDTUploadFiles")) {
                this.handleAnnotationFDTUploadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationDownloadFiles")) {
                this.handleAnnotationDownloadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationFDTDownloadFiles")) {
                this.handleAnnotationFDTDownloadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("annotationEstimateDownloadSize")) {
                this.handleAnnotationEstimateDownloadSizeRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("usersAndGroups")) {
                this.handleUsersAndGroupsRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("userAdd")) {
                this.handleUserAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("userUpdate")) {
                this.handleUserUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("userPassword")) {
                this.handleUserPasswordRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("userDelete")) {
                this.handleUserDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("groupAdd")) {
                this.handleGroupAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("groupUpdate")) {
                this.handleGroupUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("groupDelete")) {
                this.handleGroupDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("dictionaryAdd")) {
                this.handleDictionaryAddRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("dictionaryUpdate")) {
                this.handleDictionaryUpdateRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("dictionaryDelete")) {
                this.handleDictionaryDeleteRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("verifyReload")) {
                this.handleVerifyReloadRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("makeUCSCLink")) {
                this.handleMakeUCSCLinkRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("makeURLLinks")) {
                this.handleMakeURLLinksRequest(req, res);
            }
            else if (req.getPathInfo().endsWith("institutes")) {
                this.handleInstitutesRequest(req, res);
            }
            else {
                if (!req.getPathInfo().endsWith("institutesSave")) {
                    throw new Exception("Unknown GenoPub request " + req.getPathInfo());
                }
                this.handleInstitutesSaveRequest(req, res);
            }
            res.setHeader("Cache-Control", "max-age=0, must-revalidate");
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
    }

    private void handleFlexRequest(final HttpServletRequest request, final HttpServletResponse res) throws IOException {
        Session sess = null;
        try {
            if (request.getParameter("idAnnotation") != null && !request.getParameter("idAnnotation").equals("")) {
                sess = (Session)HibernateUtil.getSessionFactory().openSession();
                final Integer idAnnotation = new Integer(request.getParameter("idAnnotation"));
                final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
                if (!this.genoPubSecurity.canRead(annotation)) {
                    throw new InsufficientPermissionException("Insufficient permission to access this annotation");
                }
            }
            res.setContentType("text/html");
            res.getOutputStream().println(this.getFlexHTMLWrapper(request));
            res.setHeader("Cache-Control", "max-age=0, must-revalidate");
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleSecurityRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
        writer.write(this.genoPubSecurity.getXML());
    }

    private void handleDictionaryRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final Document doc = DictionaryHelper.reload(sess).getXML(this.genoPubSecurity);
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationsRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Document doc = null;
        Session sess = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final AnnotationQuery annotationQuery = new AnnotationQuery(request);
            doc = annotationQuery.getAnnotationDocument(sess, this.genoPubSecurity);
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            if (request.getParameter("idAnnotation") == null || request.getParameter("idAnnotation").equals("")) {
                throw new Exception("idAnnotation request to get Annotation");
            }
            final Integer idAnnotation = new Integer(request.getParameter("idAnnotation"));
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
            if (!this.genoPubSecurity.canRead(annotation)) {
                throw new InsufficientPermissionException("Insufficient permission to access this annotation");
            }
            final Document doc = annotation.getXML(this.genoPubSecurity, DictionaryHelper.getInstance(sess), this.genometry_genopub_dir);
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleOrganismAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (!this.genoPubSecurity.isAdminRole()) {
                throw new InsufficientPermissionException("Insufficient permission to add organism.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new InvalidNameException("Please enter an organism DAS2 name.");
            }
            if (request.getParameter("binomialName") == null || request.getParameter("binomialName").equals("")) {
                throw new InvalidNameException("Please enter an organism binomial name.");
            }
            if (request.getParameter("commonName") == null || request.getParameter("commonName").equals("")) {
                throw new InvalidNameException("Please enter an organism common name.");
            }
            if (request.getParameter("name").indexOf(" ") >= 0) {
                throw new InvalidNameException("The organism DAS2 name cannot have spaces.");
            }
            final Pattern pattern = Pattern.compile("\\W");
            final Matcher matcher = pattern.matcher(request.getParameter("name"));
            if (matcher.find()) {
                throw new InvalidNameException("The organism DAS2 name cannot have special characters.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Organism organism = new Organism();
            organism.setName(request.getParameter("name"));
            organism.setCommonName(request.getParameter("commonName"));
            organism.setBinomialName(request.getParameter("binomialName"));
            sess.save((Object)organism);
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res, "idOrganism", organism.getIdOrganism());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleOrganismUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Organism organism = Organism.class.cast(sess.load((Class)Organism.class, (Serializable)Util.getIntegerParameter(request, "idOrganism")));
            if (!this.genoPubSecurity.canWrite(organism)) {
                throw new InsufficientPermissionException("Insufficient permission to update organism.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new InvalidNameException("Please enter an organism DAS2 name.");
            }
            if (request.getParameter("binomialName") == null || request.getParameter("binomialName").equals("")) {
                throw new InvalidNameException("Please enter an organism binomial name.");
            }
            if (request.getParameter("commonName") == null || request.getParameter("commonName").equals("")) {
                throw new InvalidNameException("Please enter an organism common name.");
            }
            if (request.getParameter("name").indexOf(" ") >= 0) {
                throw new InvalidNameException("The organism DAS2 name cannot have spaces.");
            }
            final Pattern pattern = Pattern.compile("\\W");
            final Matcher matcher = pattern.matcher(request.getParameter("name"));
            if (matcher.find()) {
                throw new InvalidNameException("The organism DAS2 name cannot have special characters.");
            }
            organism.setName(request.getParameter("name"));
            organism.setCommonName(request.getParameter("commonName"));
            organism.setBinomialName(request.getParameter("binomialName"));
            organism.setNCBITaxID(request.getParameter("NCBITaxID"));
            sess.flush();
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res, "idOrganism", organism.getIdOrganism());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleOrganismDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idOrganism = Util.getIntegerParameter(request, "idOrganism");
            final Organism organism = Organism.class.cast(sess.load((Class)Organism.class, (Serializable)idOrganism));
            if (!this.genoPubSecurity.canWrite(organism)) {
                throw new InsufficientPermissionException("Insufficient permission to update organism.");
            }
            sess.delete((Object)organism);
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGenomeVersionRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            if (request.getParameter("idGenomeVersion") == null || request.getParameter("idGenomeVersion").equals("")) {
                throw new Exception("idGenomeVersion request to get Genome Version");
            }
            final Integer idGenomeVersion = new Integer(request.getParameter("idGenomeVersion"));
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            final Document doc = gv.getXML(this.genoPubSecurity, this.genometry_genopub_dir);
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGenomeVersionAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (!this.genoPubSecurity.isAdminRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to add genome version.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new InvalidNameException("Please enter the genome version name.");
            }
            if (request.getParameter("name").indexOf(" ") >= 0) {
                throw new InvalidNameException("The genome version DAS2 name cannot have spaces.");
            }
            final Pattern pattern = Pattern.compile("\\W");
            final Matcher matcher = pattern.matcher(request.getParameter("name"));
            if (matcher.find()) {
                throw new InvalidNameException("The genome version DAS2 name cannot have special characters.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final GenomeVersion genomeVersion = new GenomeVersion();
            final Integer idOrganism = Util.getIntegerParameter(request, "idOrganism");
            genomeVersion.setIdOrganism(idOrganism);
            genomeVersion.setName(request.getParameter("name"));
            genomeVersion.setBuildDate(Util.getDateParameter(request, "buildDate"));
            genomeVersion.setDataPath(this.genometry_genopub_dir);
            sess.save((Object)genomeVersion);
            final AnnotationGrouping annotationGrouping = new AnnotationGrouping();
            annotationGrouping.setName(genomeVersion.getName());
            annotationGrouping.setIdGenomeVersion(genomeVersion.getIdGenomeVersion());
            annotationGrouping.setIdParentAnnotationGrouping(null);
            sess.save((Object)annotationGrouping);
            final Set<AnnotationGrouping> annotationGroupingsToKeep = new TreeSet<AnnotationGrouping>(new AnnotationGroupingComparator());
            annotationGroupingsToKeep.add(annotationGrouping);
            genomeVersion.setAnnotationGroupings(annotationGroupingsToKeep);
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res, "idGenomeVersion", genomeVersion.getIdGenomeVersion());
        }
        catch (InvalidNameException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InsufficientPermissionException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGenomeVersionUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final GenomeVersion genomeVersion = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)Util.getIntegerParameter(request, "idGenomeVersion")));
            if (!this.genoPubSecurity.canWrite(genomeVersion)) {
                throw new InsufficientPermissionException("Insufficient permision to write genome version.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new InvalidNameException("Please enter the genome version name.");
            }
            if (request.getParameter("name").indexOf(" ") >= 0) {
                throw new InvalidNameException("The genome version DAS2 name cannot have spaces.");
            }
            final Pattern pattern = Pattern.compile("\\W");
            final Matcher matcher = pattern.matcher(request.getParameter("name"));
            if (matcher.find()) {
                throw new InvalidNameException("The genome version DAS2 name cannot have special characters.");
            }
            if (!request.getParameter("name").equals(genomeVersion.getName())) {
                final AnnotationGrouping ag = genomeVersion.getRootAnnotationGrouping();
                ag.setName(request.getParameter("name"));
                ag.setDescription(request.getParameter("name"));
            }
            genomeVersion.setIdOrganism(Util.getIntegerParameter(request, "idOrganism"));
            genomeVersion.setName(request.getParameter("name"));
            genomeVersion.setBuildDate(Util.getDateParameter(request, "buildDate"));
            genomeVersion.setUcscName(request.getParameter("ucscName"));
            genomeVersion.setCoordURI(request.getParameter("coordURI"));
            genomeVersion.setCoordVersion(request.getParameter("coordVersion"));
            genomeVersion.setCoordSource(request.getParameter("coordSource"));
            genomeVersion.setCoordTestRange(request.getParameter("coordTestRange"));
            genomeVersion.setCoordAuthority(request.getParameter("coordAuthority"));
            StringReader reader = new StringReader(request.getParameter("segmentsXML"));
            SAXReader sax = new SAXReader();
            final Document segmentsDoc = sax.read((Reader)reader);
            Iterator<?> i = genomeVersion.getSegments().iterator();
            while (i.hasNext()) {
                final Segment segment = Segment.class.cast(i.next());
                boolean found = false;
                final Iterator<?> i2 = (Iterator<?>)segmentsDoc.getRootElement().elementIterator();
                while (i2.hasNext()) {
                    final Element segmentNode = (Element)i2.next();
                    final String idSegment = segmentNode.attributeValue("idSegment");
                    if (idSegment != null && !idSegment.equals("") && segment.getIdSegment().equals(new Integer(idSegment))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    sess.delete((Object)segment);
                }
            }
            sess.flush();
            i = (Iterator<?>)segmentsDoc.getRootElement().elementIterator();
            while (i.hasNext()) {
                final Element segmentNode2 = (Element)i.next();
                final String idSegment2 = segmentNode2.attributeValue("idSegment");
                String len = segmentNode2.attributeValue("length");
                len = len.replace(",", "");
                final String sortOrder = segmentNode2.attributeValue("sortOrder");
                Segment s = null;
                if (idSegment2 != null && !idSegment2.equals("")) {
                    s = Segment.class.cast(sess.load((Class)Segment.class, (Serializable)new Integer(idSegment2)));
                    s.setName(segmentNode2.attributeValue("name"));
                    s.setLength((len != null && !len.equals("")) ? new Integer(len) : null);
                    s.setSortOrder((sortOrder != null && !sortOrder.equals("")) ? new Integer(sortOrder) : null);
                    s.setIdGenomeVersion(genomeVersion.getIdGenomeVersion());
                }
                else {
                    s = new Segment();
                    s.setName(segmentNode2.attributeValue("name"));
                    s.setLength((len != null && !len.equals("")) ? new Integer(len) : null);
                    s.setSortOrder((sortOrder != null && !sortOrder.equals("")) ? new Integer(sortOrder) : null);
                    s.setIdGenomeVersion(genomeVersion.getIdGenomeVersion());
                    sess.save((Object)s);
                    sess.flush();
                }
            }
            sess.flush();
            reader = new StringReader(request.getParameter("sequenceFilesToRemoveXML"));
            sax = new SAXReader();
            final Document filesDoc = sax.read((Reader)reader);
            final Iterator<?> j = (Iterator<?>)filesDoc.getRootElement().elementIterator();
            while (j.hasNext()) {
                final Element fileNode = (Element)j.next();
                final File file = new File(fileNode.attributeValue("url"));
                if (!file.delete()) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete sequence file " + file.getName() + " for genome version " + genomeVersion.getName());
                }
            }
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res, "idGenomeVersion", genomeVersion.getIdGenomeVersion());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGenomeVersionDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            final GenomeVersion genomeVersion = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            if (!this.genoPubSecurity.canWrite(genomeVersion)) {
                throw new InsufficientPermissionException("Insufficient permision to delete genome version.");
            }
            final AnnotationGrouping ag = genomeVersion.getRootAnnotationGrouping();
            if (ag != null) {
                if (ag.getAnnotationGroupings().size() > 0 || ag.getAnnotations().size() > 0) {
                    throw new Exception("The annotations for" + genomeVersion.getName() + " must be deleted first.");
                }
                sess.delete((Object)ag);
            }
            Iterator<?> i = genomeVersion.getSegments().iterator();
            while (i.hasNext()) {
                final Segment segment = Segment.class.cast(i.next());
                sess.delete((Object)segment);
            }
            i = genomeVersion.getAliases().iterator();
            while (i.hasNext()) {
                final GenomeVersionAlias alias = GenomeVersionAlias.class.cast(i.next());
                sess.delete((Object)alias);
            }
            sess.flush();
            genomeVersion.removeSequenceFiles(this.genometry_genopub_dir);
            sess.refresh((Object)genomeVersion);
            sess.delete((Object)genomeVersion);
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleSegmentImportRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            String chromosomeInfo = request.getParameter("chromosomeInfo");
            int count = 1;
            if (chromosomeInfo != null && !chromosomeInfo.equals("")) {
                final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
                final GenomeVersion genomeVersion = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
                if (!this.genoPubSecurity.canWrite(genomeVersion)) {
                    throw new InsufficientPermissionException("Insufficient permision to update the genome version.");
                }
                final Pattern pat = Pattern.compile("(\\w+)\\s+(\\d+)");
                final Pattern ret = Pattern.compile("\\r");
                chromosomeInfo = ret.matcher(chromosomeInfo).replaceAll("");
                final Matcher mat = pat.matcher(chromosomeInfo);
                while (mat.find()) {
                    final Segment s = new Segment();
                    s.setName(mat.group(1));
                    s.setLength(new Integer(mat.group(2)));
                    s.setSortOrder(count);
                    s.setIdGenomeVersion(genomeVersion.getIdGenomeVersion());
                    sess.save((Object)s);
                    ++count;
                }
                sess.flush();
            }
            tx.commit();
            DictionaryHelper.reload(sess);
            this.reportSuccess(res, "idGenomeVersion", new Integer(request.getParameter("idGenomeVersion")));
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, "Segment info did not import. " + e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleSequenceFormUploadURLRequest(final HttpServletRequest req, final HttpServletResponse res) {
        try {
            final String baseURL = "http://" + req.getServerName() + ":" + req.getLocalPort() + req.getContextPath();
            String URL = baseURL + "/" + "genopub" + "/" + "sequenceUploadFiles";
            URL = URL + ";jsessionid=" + req.getRequestedSessionId();
            final StringBuffer fileExtensions = new StringBuffer();
            for (int x = 0; x < Constants.SEQUENCE_FILE_EXTENSIONS.length; ++x) {
                if (fileExtensions.length() > 0) {
                    fileExtensions.append(";");
                }
                fileExtensions.append("*" + Constants.SEQUENCE_FILE_EXTENSIONS[x]);
            }
            res.setContentType("application/xml");
            res.getOutputStream().println("<UploadURL url='" + URL + "'" + " fileExtensions='" + fileExtensions.toString() + "'" + "/>");
        }
        catch (Exception e) {
            System.out.println("An error has occured in GenoPubServlet - " + e.toString());
        }
    }

    private void handleSequenceUploadRequest(final HttpServletRequest req, final HttpServletResponse res) {
        Session sess = null;
        Integer idGenomeVersion = null;
        GenomeVersion genomeVersion = null;
        String fileName = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            res.setDateHeader("Expires", -1L);
            res.setDateHeader("Last-Modified", System.currentTimeMillis());
            res.setHeader("Pragma", "");
            res.setHeader("Cache-Control", "");
            res.setCharacterEncoding("UTF-8");
            final MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
            Part part;
            while ((part = mp.readNextPart()) != null) {
                final String name = part.getName();
                if (part.isParam()) {
                    final ParamPart paramPart = (ParamPart)part;
                    final String value = paramPart.getStringValue();
                    if (name.equals("idGenomeVersion")) {
                        idGenomeVersion = new Integer(String.class.cast(value));
                    }
                }
                if (idGenomeVersion != null) {
                    break;
                }
            }
            if (idGenomeVersion != null) {
                genomeVersion = (GenomeVersion)sess.get((Class)GenomeVersion.class, (Serializable)idGenomeVersion);
            }
            if (genomeVersion == null) {
                throw new Exception("No genome version provided for sequence files");
            }
            if (this.genoPubSecurity.canWrite(genomeVersion)) {
                if (!new File(this.genometry_genopub_dir).exists()) {
                    final boolean success = new File(this.genometry_genopub_dir).mkdir();
                    if (!success) {
                        throw new Exception("Unable to create directory " + this.genometry_genopub_dir);
                    }
                }
                final String sequenceDir = genomeVersion.getSequenceDirectory(this.genometry_genopub_dir);
                if (!new File(sequenceDir).exists()) {
                    final boolean success2 = new File(sequenceDir).mkdir();
                    if (!success2) {
                        throw new Exception("Unable to create directory " + sequenceDir);
                    }
                }
                while ((part = mp.readNextPart()) != null) {
                    if (part.isFile()) {
                        final FilePart filePart = (FilePart)part;
                        fileName = filePart.getFileName();
                        if (fileName == null) {
                            continue;
                        }
                        if (!Util.isValidSequenceFileType(fileName)) {
                            throw new UnsupportedFileTypeException("Bypassing upload of sequence files for  " + genomeVersion.getName() + " for file" + fileName + ". Unsupported file extension");
                        }
                        final long size = filePart.writeTo(new File(sequenceDir));
                    }
                }
                sess.flush();
                this.reportSuccess(res, "idGenomeVersion", genomeVersion.getIdGenomeVersion());
                return;
            }
            throw new InsufficientPermissionException("Bypassing upload of sequence files for  " + genomeVersion.getName() + " due to insufficient permissions.");
        }
        catch (InsufficientPermissionException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            this.reportError(res, e.getMessage(), 902);
        }
        catch (UnsupportedFileTypeException e2) {
            Logger.getLogger(this.getClass().getName()).warning(e2.getMessage());
            this.reportError(res, e2.getMessage(), 902);
        }
        catch (Exception e3) {
            Logger.getLogger(this.getClass().getName()).warning(e3.toString());
            e3.printStackTrace();
            this.reportError(res, e3.toString(), 901);
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationGroupingAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter the annotation folder name.");
            }
            if (this.genoPubSecurity.isGuestRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to add a folder.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final AnnotationGrouping annotationGrouping = new AnnotationGrouping();
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            Integer idParentAnnotationGrouping = Util.getIntegerParameter(request, "idParentAnnotationGrouping");
            final Integer idUserGroup = Util.getIntegerParameter(request, "idUserGroup");
            AnnotationGrouping parentAnnotationGrouping = null;
            if (idParentAnnotationGrouping == null) {
                final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
                parentAnnotationGrouping = gv.getRootAnnotationGrouping();
                if (parentAnnotationGrouping == null) {
                    throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                }
                idParentAnnotationGrouping = parentAnnotationGrouping.getIdAnnotationGrouping();
            }
            else {
                parentAnnotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idParentAnnotationGrouping));
            }
            if (parentAnnotationGrouping.getIdUserGroup() != null && (idUserGroup == null || !parentAnnotationGrouping.getIdUserGroup().equals(idUserGroup))) {
                throw new Exception("Folder '" + request.getParameter("name") + "' must belong to user group '" + DictionaryHelper.getInstance(sess).getUserGroupName(parentAnnotationGrouping.getIdUserGroup()) + "'");
            }
            final String name = request.getParameter("name");
            if (name.contains("/") || name.contains("&")) {
                throw new InvalidNameException("The folder name cannnot contain characters / or &.");
            }
            annotationGrouping.setName(name);
            annotationGrouping.setIdGenomeVersion(idGenomeVersion);
            annotationGrouping.setIdParentAnnotationGrouping(idParentAnnotationGrouping);
            annotationGrouping.setIdUserGroup(Util.getIntegerParameter(request, "idUserGroup"));
            annotationGrouping.setCreateDate(new Date(System.currentTimeMillis()));
            annotationGrouping.setCreatedBy(this.genoPubSecurity.getUserName());
            sess.save((Object)annotationGrouping);
            tx.commit();
            this.reportSuccess(res, "idAnnotationGrouping", annotationGrouping.getIdAnnotationGrouping());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationGroupingUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter the annotation folder name.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final AnnotationGrouping annotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)Util.getIntegerParameter(request, "idAnnotationGrouping")));
            if (!this.genoPubSecurity.canWrite(annotationGrouping)) {
                throw new InsufficientPermissionException("Insufficient permision to write annotation folder.");
            }
            final Integer idUserGroup = Util.getIntegerParameter(request, "idUserGroup");
            if (annotationGrouping.getParentAnnotationGrouping() != null && annotationGrouping.getParentAnnotationGrouping().getIdUserGroup() != null && (idUserGroup == null || !annotationGrouping.getParentAnnotationGrouping().getIdUserGroup().equals(idUserGroup))) {
                throw new Exception("Folder '" + request.getParameter("name") + "' must belong to user group '" + DictionaryHelper.getInstance(sess).getUserGroupName(annotationGrouping.getParentAnnotationGrouping().getIdUserGroup()) + "'");
            }
            final String name = request.getParameter("name");
            if (name.contains("/") || name.contains("&")) {
                throw new InvalidNameException("The folder name cannnot contain any characters / or &.");
            }
            annotationGrouping.setName(name);
            annotationGrouping.setDescription(request.getParameter("description"));
            annotationGrouping.setIdUserGroup(idUserGroup);
            sess.save((Object)annotationGrouping);
            tx.commit();
            this.reportSuccess(res, "idAnnotationGrouping", annotationGrouping.getIdAnnotationGrouping());
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationGroupingMoveRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idAnnotationGrouping = Util.getIntegerParameter(request, "idAnnotationGrouping");
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            final Integer idParentAnnotationGrouping = Util.getIntegerParameter(request, "idParentAnnotationGrouping");
            final String isMove = Util.getFlagParameter(request, "isMove");
            AnnotationGrouping annotationGrouping = null;
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            AnnotationGrouping parentAnnotationGrouping = null;
            if (idParentAnnotationGrouping == null) {
                parentAnnotationGrouping = gv.getRootAnnotationGrouping();
                if (parentAnnotationGrouping == null) {
                    throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                }
            }
            else {
                parentAnnotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idParentAnnotationGrouping));
            }
            if (isMove.equals("Y")) {
                annotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
                if (!this.genoPubSecurity.canWrite(annotationGrouping)) {
                    throw new InsufficientPermissionException("Insufficient permision to move this annotation folder.");
                }
            }
            else {
                final AnnotationGrouping ag = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
                annotationGrouping = new AnnotationGrouping();
                annotationGrouping.setName(ag.getName());
                annotationGrouping.setDescription(ag.getDescription());
                annotationGrouping.setIdGenomeVersion(ag.getIdGenomeVersion());
                annotationGrouping.setIdUserGroup(ag.getIdUserGroup());
                final Set<Annotation> annotationsToKeep = new TreeSet<Annotation>(new AnnotationComparator());
                final Iterator<?> i = ag.getAnnotations().iterator();
                while (i.hasNext()) {
                    final Annotation a = Annotation.class.cast(i.next());
                    annotationsToKeep.add(a);
                }
                annotationGrouping.setAnnotations(annotationsToKeep);
                sess.save((Object)annotationGrouping);
            }
            if (!parentAnnotationGrouping.getIdGenomeVersion().equals(annotationGrouping.getIdGenomeVersion())) {
                throw new Exception("Annotation folder '" + annotationGrouping.getName() + "' cannot be moved to a different genome version");
            }
            if (parentAnnotationGrouping.getIdAnnotationGrouping().equals(idAnnotationGrouping)) {
                throw new Exception("Move/copy operation to same annotation folder is not allowed.");
            }
            annotationGrouping.setIdParentAnnotationGrouping(parentAnnotationGrouping.getIdAnnotationGrouping());
            tx.commit();
            this.reportSuccess(res, "idAnnotationGrouping", annotationGrouping.getIdAnnotationGrouping());
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationGroupingDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idAnnotationGrouping = Util.getIntegerParameter(request, "idAnnotationGrouping");
            final AnnotationGrouping annotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
            final List<Object> descendents = new ArrayList<Object>();
            descendents.add(annotationGrouping);
            annotationGrouping.recurseGetChildren(descendents);
            for (final Object descendent : descendents) {
                if (!this.genoPubSecurity.canWrite(descendent)) {
                    if (descendent.equals(annotationGrouping)) {
                        throw new InsufficientPermissionException("Insufficient permision to delete this annotation folder.");
                    }
                    if (descendent instanceof AnnotationGrouping) {
                        final AnnotationGrouping ag = (AnnotationGrouping)descendent;
                        throw new InsufficientPermissionException("Insufficent permission to delete child folder '" + ag.getName() + "'.");
                    }
                    if (descendent instanceof Annotation) {
                        final Annotation a = (Annotation)descendent;
                        throw new InsufficientPermissionException("Insufficent permission to delete child annotation '" + a.getName() + "'.");
                    }
                    continue;
                }
            }
            for (final Object descendent : descendents) {
                if (descendent instanceof Annotation) {
                    final Annotation a = (Annotation)descendent;
                    if (a.getAnnotationGroupings().size() <= 1) {
                        continue;
                    }
                    for (final AnnotationGrouping ag2 : (Set<AnnotationGrouping>) a.getAnnotationGroupings()) {
                        boolean inDeleteList = false;
                        for (final Object d : descendents) {
                            if (d instanceof AnnotationGrouping) {
                                final AnnotationGrouping agToDelete = (AnnotationGrouping)d;
                                if (agToDelete.getIdAnnotationGrouping().equals(ag2.getIdAnnotationGrouping())) {
                                    inDeleteList = true;
                                    break;
                                }
                                continue;
                            }
                        }
                        if (!inDeleteList) {
                            throw new InsufficientPermissionException("Cannot remove contents of folder '" + annotationGrouping.getName() + "' because annotation '" + a.getName() + "' exists in folder '" + ag2.getName() + "'.  Please remove this annotation first.");
                        }
                    }
                }
            }
            for (int j = descendents.size() - 1; j >= 0; --j) {
                final Object descendent = descendents.get(j);
                if (descendent instanceof Annotation) {
                    final Annotation a = (Annotation)descendent;
                    a.removeFiles(this.genometry_genopub_dir);
                }
                sess.delete(descendent);
            }
            tx.commit();
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (this.genoPubSecurity.isGuestRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to add an annotation.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter an annotation name.");
            }
            if (request.getParameter("codeVisibility") == null || request.getParameter("codeVisibility").equals("")) {
                throw new Exception("Please select the visibility for this annotation.");
            }
            if (!request.getParameter("codeVisibility").equals("PUBLIC") && Util.getIntegerParameter(request, "idUserGroup") == null) {
                throw new Exception("For private annotations, the group must be specified.");
            }
            final String name = request.getParameter("name");
            if (name.contains("/") || name.contains("&")) {
                throw new InvalidNameException("The annotation name cannnot contain characters / or &.");
            }
            final String codeVisibility = request.getParameter("codeVisibility");
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            final Integer idAnnotationGrouping = Util.getIntegerParameter(request, "idAnnotationGrouping");
            final Integer idUserGroup = Util.getIntegerParameter(request, "idUserGroup");
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Annotation annotation = this.createNewAnnotation(sess, name, codeVisibility, idGenomeVersion, idAnnotationGrouping, idUserGroup);
            sess.flush();
            tx.commit();
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            root.addAttribute("idAnnotation", annotation.getIdAnnotation().toString());
            root.addAttribute("idGenomeVersion", idGenomeVersion.toString());
            root.addAttribute("idAnnotationGrouping", (idAnnotationGrouping != null) ? idAnnotationGrouping.toString() : "");
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private Annotation createNewAnnotation(final Session sess, final String name, final String codeVisibility, final Integer idGenomeVersion, final Integer idAnnotationGrouping, final Integer idUserGroup) throws Exception {
        final Annotation annotation = new Annotation();
        annotation.setName(name);
        annotation.setIdGenomeVersion(idGenomeVersion);
        annotation.setCodeVisibility(codeVisibility);
        annotation.setIdUserGroup(idUserGroup);
        annotation.setIsLoaded("N");
        annotation.setDataPath(this.genometry_genopub_dir);
        if (!this.genoPubSecurity.isAdminRole()) {
            annotation.setIdUser(this.genoPubSecurity.getIdUser());
        }
        annotation.setCreateDate(new Date(System.currentTimeMillis()));
        annotation.setCreatedBy(this.genoPubSecurity.getUserName());
        sess.save((Object)annotation);
        sess.flush();
        AnnotationGrouping ag = null;
        if (idAnnotationGrouping == null) {
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            ag = gv.getRootAnnotationGrouping();
            if (ag == null) {
                throw new Exception("Cannot find root annotation grouping for " + gv.getName());
            }
        }
        else {
            ag = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
        }
        final Set<Annotation> newAnnotations = new TreeSet<Annotation>(new AnnotationComparator());
        final Iterator<?> i = ag.getAnnotations().iterator();
        while (i.hasNext()) {
            final Annotation a = Annotation.class.cast(i.next());
            newAnnotations.add(a);
        }
        newAnnotations.add(annotation);
        ag.setAnnotations(newAnnotations);
        annotation.setFileName("A" + annotation.getIdAnnotation());
        return annotation;
    }

    private void handleAnnotationUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)Util.getIntegerParameter(request, "idAnnotation")));
            if (!this.genoPubSecurity.canWrite(annotation)) {
                throw new InsufficientPermissionException("Insufficient permision to write annotation.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter an annotation name.");
            }
            if (request.getParameter("codeVisibility") == null || request.getParameter("codeVisibility").equals("")) {
                throw new Exception("Please select the visibility for this annotation.");
            }
            if (!request.getParameter("codeVisibility").equals("PUBLIC") && Util.getIntegerParameter(request, "idUserGroup") == null) {
                throw new Exception("For private annotations, the group must be specified.");
            }
            final String name = request.getParameter("name");
            if (name.contains("/") || name.contains("&")) {
                throw new InvalidNameException("The annotation name cannnot contain characters / or &.");
            }
            annotation.setName(name);
            annotation.setDescription(request.getParameter("description"));
            annotation.setSummary(request.getParameter("summary"));
            annotation.setCodeVisibility(request.getParameter("codeVisibility"));
            if (annotation.getCodeVisibility() != null && annotation.getCodeVisibility().equals("INST")) {
                annotation.setIdInstitute(Util.getIntegerParameter(request, "idInstitute"));
            }
            else {
                annotation.setIdInstitute(null);
            }
            annotation.setIdUserGroup(Util.getIntegerParameter(request, "idUserGroup"));
            annotation.setIdUser(Util.getIntegerParameter(request, "idUser"));
            StringReader reader = new StringReader(request.getParameter("collaboratorsXML"));
            SAXReader sax = new SAXReader();
            final Document collaboratorsDoc = sax.read((Reader)reader);
            final TreeSet<User> collaborators = new TreeSet<User>(new UserComparator());
            final Iterator<?> i = (Iterator<?>)collaboratorsDoc.getRootElement().elementIterator();
            while (i.hasNext()) {
                final Element userNode = (Element)i.next();
                final Integer idUser = Integer.parseInt(userNode.attributeValue("idUser"));
                final User user = User.class.cast(sess.load((Class)User.class, (Serializable)idUser));
                collaborators.add(user);
            }
            annotation.setCollaborators(collaborators);
            sess.flush();
            reader = new StringReader(request.getParameter("filesToRemoveXML"));
            sax = new SAXReader();
            final Document filesDoc = sax.read((Reader)reader);
            final Iterator<?> j = (Iterator<?>)filesDoc.getRootElement().elementIterator();
            while (j.hasNext()) {
                final Element fileNode = (Element)j.next();
                final File file = new File(fileNode.attributeValue("url"));
                if (!file.delete()) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable remove annotation file " + file.getName() + " for annotation " + annotation.getName());
                }
            }
            reader = new StringReader(request.getParameter("propertiesXML"));
            sax = new SAXReader();
            final Document propsDoc = sax.read((Reader)reader);
            Iterator<?> k = annotation.getAnnotationProperties().iterator();
            while (k.hasNext()) {
                final AnnotationProperty ap = AnnotationProperty.class.cast(k.next());
                boolean found = false;
                Iterator<?> i2 = (Iterator<?>)propsDoc.getRootElement().elementIterator();
                while (i2.hasNext()) {
                    final Element propNode = (Element)i2.next();
                    final String idAnnotationProperty = propNode.attributeValue("idAnnotationProperty");
                    if (idAnnotationProperty != null && !idAnnotationProperty.equals("") && ap.getIdAnnotationProperty().equals(new Integer(idAnnotationProperty))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    i2 = ap.getValues().iterator();
                    while (i2.hasNext()) {
                        final AnnotationPropertyValue av = AnnotationPropertyValue.class.cast(i2.next());
                        sess.delete((Object)av);
                    }
                    sess.flush();
                    sess.delete((Object)ap);
                }
            }
            sess.flush();
            k = (Iterator<?>)propsDoc.getRootElement().elementIterator();
            while (k.hasNext()) {
                final Element node = (Element)k.next();
                final String idAnnotationProperty2 = node.attributeValue("idAnnotationProperty");
                AnnotationProperty ap2 = null;
                if (idAnnotationProperty2 == null || idAnnotationProperty2.equals("")) {
                    ap2 = new AnnotationProperty();
                    ap2.setIdProperty(Integer.valueOf(node.attributeValue("idProperty")));
                }
                else {
                    ap2 = AnnotationProperty.class.cast(sess.get((Class)AnnotationProperty.class, (Serializable)Integer.valueOf(idAnnotationProperty2)));
                }
                ap2.setName(node.attributeValue("name"));
                ap2.setValue(node.attributeValue("value"));
                ap2.setIdAnnotation(annotation.getIdAnnotation());
                if (idAnnotationProperty2 == null || idAnnotationProperty2.equals("")) {
                    sess.save((Object)ap2);
                    sess.flush();
                }
                if (ap2.getValues() != null) {
                    final Iterator<?> i3 = ap2.getValues().iterator();
                    while (i3.hasNext()) {
                        final AnnotationPropertyValue av2 = AnnotationPropertyValue.class.cast(i3.next());
                        boolean found2 = false;
                        final Iterator<?> i4 = (Iterator<?>)node.elementIterator();
                        while (i4.hasNext()) {
                            final Element n = (Element)i4.next();
                            if (n.getName().equals("AnnotationPropertyValue")) {
                                final String idAnnotationPropertyValue = n.attributeValue("idAnnotationPropertyValue");
                                if (idAnnotationPropertyValue != null && !idAnnotationPropertyValue.equals("") && av2.getIdAnnotationPropertyValue().equals(new Integer(idAnnotationPropertyValue))) {
                                    found2 = true;
                                    break;
                                }
                                continue;
                            }
                        }
                        if (!found2) {
                            sess.delete((Object)av2);
                        }
                    }
                    sess.flush();
                }
                final Iterator<?> i3 = (Iterator<?>)node.elementIterator();
                while (i3.hasNext()) {
                    final Element n2 = (Element)i3.next();
                    if (n2.getName().equals("AnnotationPropertyValue")) {
                        final String idAnnotationPropertyValue2 = n2.attributeValue("idAnnotationPropertyValue");
                        final String value = n2.attributeValue("value");
                        AnnotationPropertyValue av3 = null;
                        if (value != null && value.equals("Enter URL here...")) {
                            continue;
                        }
                        if (idAnnotationPropertyValue2 == null || idAnnotationPropertyValue2.equals("")) {
                            av3 = new AnnotationPropertyValue();
                            av3.setIdAnnotationProperty(ap2.getIdAnnotationProperty());
                        }
                        else {
                            av3 = AnnotationPropertyValue.class.cast(sess.load((Class)AnnotationPropertyValue.class, (Serializable)Integer.valueOf(idAnnotationPropertyValue2)));
                        }
                        av3.setValue(n2.attributeValue("value"));
                        if (idAnnotationPropertyValue2 != null && !idAnnotationPropertyValue2.equals("")) {
                            continue;
                        }
                        sess.save((Object)av3);
                    }
                }
                sess.flush();
                String optionValue = "";
                final TreeSet<PropertyOption> options = new TreeSet<PropertyOption>(new PropertyOptionComparator());
                final Iterator<?> i5 = (Iterator<?>)node.elementIterator();
                while (i5.hasNext()) {
                    final Element n3 = (Element)i5.next();
                    if (n3.getName().equals("PropertyOption")) {
                        final Integer idPropertyOption = Integer.parseInt(n3.attributeValue("idPropertyOption"));
                        final String selected = n3.attributeValue("selected");
                        if (selected == null || !selected.equals("Y")) {
                            continue;
                        }
                        final PropertyOption option = PropertyOption.class.cast(sess.load((Class)PropertyOption.class, (Serializable)idPropertyOption));
                        options.add(option);
                        if (optionValue.length() > 0) {
                            optionValue += ",";
                        }
                        optionValue += option.getName();
                    }
                }
                ap2.setOptions(options);
                if (options.size() > 0) {
                    ap2.setValue(optionValue);
                }
                sess.flush();
            }
            tx.commit();
            this.reportSuccess(res, "idAnnotation", annotation.getIdAnnotation());
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (InvalidNameException e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationDuplicateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            if (request.getParameter("idAnnotation") == null || request.getParameter("idAnnotation").equals("")) {
                throw new Exception("idAnnotation required.");
            }
            final Annotation sourceAnnot = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)Util.getIntegerParameter(request, "idAnnotation")));
            if (!this.genoPubSecurity.canWrite(sourceAnnot)) {
                throw new InsufficientPermissionException("Insufficient permision to write annotation.");
            }
            final Annotation dup = new Annotation();
            dup.setName(sourceAnnot.getName() + "_copy");
            dup.setDescription(sourceAnnot.getDescription());
            dup.setSummary(sourceAnnot.getSummary());
            dup.setCodeVisibility(sourceAnnot.getCodeVisibility());
            dup.setIdUserGroup(sourceAnnot.getIdUserGroup());
            dup.setIdUser(sourceAnnot.getIdUser());
            dup.setIdGenomeVersion(sourceAnnot.getIdGenomeVersion());
            dup.setIsLoaded("N");
            dup.setCreateDate(new Date(System.currentTimeMillis()));
            dup.setCreatedBy(this.genoPubSecurity.getUserName());
            sess.save((Object)dup);
            final Set<AnnotationProperty> clonedAPSet = new HashSet<AnnotationProperty>();
            for (final AnnotationProperty sourceAP : (Set<AnnotationProperty>) sourceAnnot.getAnnotationProperties()) {
                final AnnotationProperty clonedAP = new AnnotationProperty();
                clonedAP.setIdProperty(sourceAP.getIdProperty());
                clonedAP.setName(sourceAP.getName());
                clonedAP.setValue(sourceAP.getValue());
                clonedAP.setIdAnnotation(dup.getIdAnnotation());
                sess.save((Object)clonedAP);
                sess.flush();
                clonedAPSet.add(clonedAP);
                final Set<AnnotationPropertyValue> clonedAPV = new HashSet<AnnotationPropertyValue>();
                for (final AnnotationPropertyValue sourceAV : (Set<AnnotationPropertyValue>) sourceAP.getValues()) {
                    final AnnotationPropertyValue clonedAV = new AnnotationPropertyValue();
                    clonedAV.setIdAnnotationProperty(clonedAP.getIdAnnotationProperty());
                    clonedAV.setValue(sourceAV.getValue());
                    sess.save((Object)clonedAV);
                    clonedAPV.add(clonedAV);
                }
                clonedAP.setValues(clonedAPV);
                final TreeSet<PropertyOption> clonedOptions = new TreeSet<PropertyOption>(new PropertyOptionComparator());
                for (final PropertyOption sourceOption : (Set<PropertyOption>) sourceAP.getOptions()) {
                    clonedOptions.add(sourceOption);
                }
                clonedAP.setOptions(clonedOptions);
            }
            dup.setAnnotationProperties(clonedAPSet);
            final TreeSet<User> collaborators = new TreeSet<User>(new UserComparator());
            final Iterator<?> cIt = sourceAnnot.getCollaborators().iterator();
            while (cIt.hasNext()) {
                collaborators.add((User)cIt.next());
            }
            dup.setCollaborators(collaborators);
            final Set<AnnotationGrouping> annotationGroupings = new TreeSet<AnnotationGrouping>(new AnnotationGroupingComparator());
            final Iterator<?> aIt = sourceAnnot.getAnnotationGroupings().iterator();
            while (aIt.hasNext()) {
                annotationGroupings.add((AnnotationGrouping)aIt.next());
            }
            dup.setAnnotationGroupings(annotationGroupings);
            sess.save((Object)dup);
            sess.flush();
            AnnotationGrouping ag = null;
            if (Util.getIntegerParameter(request, "idAnnotationGrouping") == null) {
                final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)sourceAnnot.getIdGenomeVersion()));
                ag = gv.getRootAnnotationGrouping();
                if (ag == null) {
                    throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                }
            }
            else {
                ag = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)Util.getIntegerParameter(request, "idAnnotationGrouping")));
            }
            final Set<Annotation> newAnnotations = new TreeSet<Annotation>(new AnnotationComparator());
            final Iterator<?> j = ag.getAnnotations().iterator();
            while (j.hasNext()) {
                final Annotation a = Annotation.class.cast(j.next());
                newAnnotations.add(a);
            }
            newAnnotations.add(dup);
            ag.setAnnotations(newAnnotations);
            dup.setFileName("A" + dup.getIdAnnotation());
            tx.commit();
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            root.addAttribute("idAnnotation", dup.getIdAnnotation().toString());
            if (Util.getIntegerParameter(request, "idAnnotationGrouping") != null) {
                root.addAttribute("idAnnotationGrouping", Util.getIntegerParameter(request, "idAnnotationGrouping").toString());
            }
            else {
                root.addAttribute("idAnnotationGrouping", "");
            }
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idAnnotation = Util.getIntegerParameter(request, "idAnnotation");
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
            if (!this.genoPubSecurity.canWrite(annotation)) {
                throw new InsufficientPermissionException("Insufficient permision to delete annotation.");
            }
            for (final AnnotationGrouping ag : (Set<AnnotationGrouping>)annotation.getAnnotationGroupings()) {
                String path = ag.getQualifiedTypeName();
                if (path.length() > 0) {
                    path += "/";
                }
                final String typeName = path + annotation.getName();
                final UnloadAnnotation unload = new UnloadAnnotation();
                unload.setTypeName(typeName);
                unload.setIdUser(this.genoPubSecurity.getIdUser());
                unload.setIdGenomeVersion(annotation.getIdGenomeVersion());
                sess.save((Object)unload);
            }
            annotation.removeFiles(this.genometry_genopub_dir);
            Iterator<?> i = annotation.getAnnotationProperties().iterator();
            while (i.hasNext()) {
                final AnnotationProperty ap = AnnotationProperty.class.cast(i.next());
                final Iterator<?> i2 = ap.getValues().iterator();
                while (i2.hasNext()) {
                    final AnnotationPropertyValue av = AnnotationPropertyValue.class.cast(i2.next());
                    sess.delete((Object)av);
                }
            }
            sess.flush();
            i = annotation.getAnnotationProperties().iterator();
            while (i.hasNext()) {
                final AnnotationProperty ap = AnnotationProperty.class.cast(i.next());
                sess.delete((Object)ap);
            }
            sess.flush();
            sess.delete((Object)annotation);
            sess.flush();
            tx.commit();
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationUnlinkRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idAnnotation = Util.getIntegerParameter(request, "idAnnotation");
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            final Integer idAnnotationGrouping = Util.getIntegerParameter(request, "idAnnotationGrouping");
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            if (!this.genoPubSecurity.canWrite(annotation)) {
                throw new InsufficientPermissionException("Insufficient permision to unlink annotation.");
            }
            AnnotationGrouping annotationGrouping = null;
            if (idAnnotationGrouping == null) {
                annotationGrouping = gv.getRootAnnotationGrouping();
                if (annotationGrouping == null) {
                    throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                }
            }
            else {
                annotationGrouping = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
            }
            final String typeName = annotationGrouping.getQualifiedTypeName() + "/" + annotation.getName();
            final UnloadAnnotation unload = new UnloadAnnotation();
            unload.setTypeName(typeName);
            unload.setIdUser(this.genoPubSecurity.getIdUser());
            unload.setIdGenomeVersion(annotation.getIdGenomeVersion());
            sess.save((Object)unload);
            final Set<Annotation> annotationsToKeep = new TreeSet<Annotation>(new AnnotationComparator());
            final Iterator<?> i = annotationGrouping.getAnnotations().iterator();
            while (i.hasNext()) {
                final Annotation a = Annotation.class.cast(i.next());
                if (a.getIdAnnotation().equals(annotation.getIdAnnotation())) {
                    continue;
                }
                annotationsToKeep.add(a);
            }
            annotationGrouping.setAnnotations(annotationsToKeep);
            tx.commit();
            sess.refresh((Object)annotation);
            final StringBuffer remainingAnnotationGroupings = new StringBuffer();
            int agCount = 0;
            final Iterator<?> i2 = annotation.getAnnotationGroupings().iterator();
            while (i2.hasNext()) {
                final AnnotationGrouping ag = AnnotationGrouping.class.cast(i2.next());
                if (remainingAnnotationGroupings.length() > 0) {
                    remainingAnnotationGroupings.append(",\n");
                }
                remainingAnnotationGroupings.append("    '" + ag.getName() + "'");
                ++agCount;
            }
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            root.addAttribute("idAnnotation", annotation.getIdAnnotation().toString());
            root.addAttribute("name", annotation.getName());
            root.addAttribute("numberRemainingAnnotationGroupings", Integer.valueOf(agCount).toString());
            root.addAttribute("remainingAnnotationGroupings", remainingAnnotationGroupings.toString());
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (InsufficientPermissionException e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationMoveRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Integer idAnnotation = Util.getIntegerParameter(request, "idAnnotation");
            final Integer idGenomeVersion = Util.getIntegerParameter(request, "idGenomeVersion");
            final Integer idAnnotationGrouping = Util.getIntegerParameter(request, "idAnnotationGrouping");
            final Integer idAnnotationGroupingOld = Util.getIntegerParameter(request, "idAnnotationGroupingOld");
            final String isMove = Util.getFlagParameter(request, "isMove");
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)idGenomeVersion));
            if (isMove.equals("Y") && !this.genoPubSecurity.canWrite(annotation)) {
                throw new InsufficientPermissionException("Insufficient permision to unlink annotation.");
            }
            AnnotationGrouping annotationGroupingNew = null;
            if (idAnnotationGrouping == null) {
                annotationGroupingNew = gv.getRootAnnotationGrouping();
                if (annotationGroupingNew == null) {
                    throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                }
            }
            else {
                annotationGroupingNew = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
            }
            if (!annotationGroupingNew.getIdGenomeVersion().equals(annotation.getIdGenomeVersion())) {
                throw new Exception("Annotation '" + annotation.getName() + "' cannot be moved to a different genome version");
            }
            if (idAnnotationGroupingOld != null) {
                if (annotationGroupingNew.getIdAnnotationGrouping().equals(idAnnotationGroupingOld)) {
                    throw new Exception("Move/copy operation to same annotation folder is not allowed.");
                }
            }
            else if (idAnnotationGrouping == null) {
                throw new Exception("Move/copy operation to same folder is not allowed.");
            }
            final Set<Annotation> newAnnotations = new TreeSet<Annotation>(new AnnotationComparator());
            final Iterator<?> i = annotationGroupingNew.getAnnotations().iterator();
            while (i.hasNext()) {
                final Annotation a = Annotation.class.cast(i.next());
                newAnnotations.add(a);
            }
            newAnnotations.add(annotation);
            annotationGroupingNew.setAnnotations(newAnnotations);
            if (isMove.equals("Y")) {
                AnnotationGrouping annotationGroupingOld = null;
                if (idAnnotationGroupingOld == null) {
                    annotationGroupingOld = gv.getRootAnnotationGrouping();
                    if (annotationGroupingOld == null) {
                        throw new Exception("Cannot find root annotation grouping for " + gv.getName());
                    }
                }
                else {
                    annotationGroupingOld = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGroupingOld));
                }
                final Set<Annotation> annotationsToKeep = new TreeSet<Annotation>(new AnnotationComparator());
                final Iterator<?> i2 = annotationGroupingOld.getAnnotations().iterator();
                while (i2.hasNext()) {
                    final Annotation a2 = Annotation.class.cast(i2.next());
                    if (a2.getIdAnnotation().equals(annotation.getIdAnnotation())) {
                        continue;
                    }
                    annotationsToKeep.add(a2);
                }
                annotationGroupingOld.setAnnotations(annotationsToKeep);
            }
            tx.commit();
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            root.addAttribute("idAnnotation", annotation.getIdAnnotation().toString());
            root.addAttribute("idGenomeVersion", idGenomeVersion.toString());
            root.addAttribute("idAnnotationGrouping", (idAnnotationGrouping != null) ? idAnnotationGrouping.toString() : "");
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationInfoRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        final OutputFormat format = OutputFormat.createPrettyPrint();
        HTMLWriter writer = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final Integer idAnnotation = Util.getIntegerParameter(request, "idAnnotation");
            final DictionaryHelper dh = DictionaryHelper.getInstance(sess);
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
            if (!this.genoPubSecurity.canRead(annotation)) {
                throw new Exception("Insufficient permissions to access information on this annotation.");
            }
            res.setContentType("text/html");
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("HTML");
            final Element head = root.addElement("HEAD");
            final Element link = head.addElement("link");
            link.addAttribute("rel", "stylesheet");
            link.addAttribute("type", "text/css");
            String baseURL = "";
            final StringBuffer fullPath = request.getRequestURL();
            final String extraPath = request.getServletPath() + request.getPathInfo();
            final int pos = fullPath.lastIndexOf(extraPath);
            if (pos > 0) {
                baseURL = fullPath.substring(0, pos);
            }
            link.addAttribute("href", baseURL + "/info.css");
            final Element body = root.addElement("BODY");
            final Element center = body.addElement("CENTER");
            final Element h1 = center.addElement("H1");
            h1.addText("DAS2 Annotation");
            final Element h2 = body.addElement("H2");
            h2.addText(annotation.getName());
            final Element table = body.addElement("TABLE");
            Element row = table.addElement("TR");
            row.addElement("TD").addText("Summary").addAttribute("CLASS", "label");
            row.addElement("TD").addCDATA((annotation.getSummary() != null && !annotation.getSummary().equals("")) ? annotation.getSummary() : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("Description").addAttribute("CLASS", "label");
            if (annotation.getDescription() == null || annotation.getDescription().equals("")) {
                row.addElement("TD").addCDATA("&nbsp;");
            }
            else {
                String description = annotation.getDescription().replaceAll("\\n", "<br>");
                description = annotation.getDescription().replaceAll("\\r", "<br>");
                row.addElement("TD").addCDATA(description);
            }
            row = table.addElement("TR");
            row.addElement("TD").addText("Owner").addAttribute("CLASS", "label");
            row.addElement("TD").addCDATA((annotation.getIdUser() != null) ? dh.getUserFullName(annotation.getIdUser()) : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("Owner email").addAttribute("CLASS", "label");
            final String userEmail = dh.getUserEmail(annotation.getIdUser());
            row.addElement("TD").addCDATA((userEmail != null) ? userEmail : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("Owner institute").addAttribute("CLASS", "label");
            final String userInstitute = dh.getUserInstitute(annotation.getIdUser());
            row.addElement("TD").addCDATA((userInstitute != null) ? userInstitute : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("User Group").addAttribute("CLASS", "label");
            row.addElement("TD").addCDATA((annotation.getIdUserGroup() != null) ? dh.getUserGroupName(annotation.getIdUserGroup()) : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("User Group contact").addAttribute("CLASS", "label");
            final String groupContact = dh.getUserGroupContact(annotation.getIdUserGroup());
            row.addElement("TD").addCDATA((groupContact != null) ? groupContact : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("User Group email").addAttribute("CLASS", "label");
            final String groupEmail = dh.getUserGroupEmail(annotation.getIdUserGroup());
            row.addElement("TD").addCDATA((groupEmail != null) ? groupEmail : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("User Group institute").addAttribute("CLASS", "label");
            final String instituteName = dh.getInstituteName(annotation.getIdInstitute());
            row.addElement("TD").addCDATA((instituteName != null && !instituteName.equals("")) ? instituteName : "&nbsp;");
            row = table.addElement("TR");
            row.addElement("TD").addText("Visibility").addAttribute("CLASS", "label");
            row.addElement("TD").addCDATA((annotation.getCodeVisibility() != null && !annotation.getCodeVisibility().equals("")) ? Visibility.getDisplay(annotation.getCodeVisibility()) : "&nbsp;");
            for (final AnnotationProperty ap : (Set<AnnotationProperty>) annotation.getAnnotationProperties()) {
                row = table.addElement("TR");
                row.addElement("TD").addText(ap.getName()).addAttribute("CLASS", "label");
                if (ap.getProperty().getCodePropertyType().equals("URL")) {
                    final StringBuffer value = new StringBuffer();
                    for (final AnnotationPropertyValue av : (Set<AnnotationPropertyValue>) ap.getValues()) {
                        if (value.length() > 0) {
                            value.append(", ");
                        }
                        value.append(av.getValue());
                    }
                    row.addElement("TD").addCDATA((value.length() > 0) ? value.toString() : "&nbsp;");
                }
                else {
                    row.addElement("TD").addCDATA((ap.getValue() != null && !ap.getValue().equals("")) ? ap.getValue() : "&nbsp;");
                }
            }
            String publishedBy = "&nbsp;";
            if (annotation.getCreatedBy() != null && !annotation.getCreatedBy().equals("")) {
                publishedBy = annotation.getCreatedBy();
                if (annotation.getCreateDate() != null) {
                    publishedBy = publishedBy + " " + Util.formatDate(annotation.getCreateDate());
                }
            }
            else if (annotation.getCreateDate() != null) {
                publishedBy = " " + Util.formatDate(annotation.getCreateDate());
            }
            row = table.addElement("TR");
            row.addElement("TD").addText("Published by").addAttribute("CLASS", "label");
            row.addElement("TD").addCDATA(publishedBy);
            writer = new HTMLWriter((Writer)res.getWriter(), format);
            writer.write(doc);
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            if (writer != null) {
                writer.close();
            }
            e.printStackTrace();
            final Document doc2 = DocumentHelper.createDocument();
            res.setContentType("text/html");
            final Element root2 = doc2.addElement("HTML");
            final Element head2 = root2.addElement("HEAD");
            final Element link2 = head2.addElement("link");
            link2.addAttribute("rel", "stylesheet");
            link2.addAttribute("type", "text/css");
            final Element body2 = root2.addElement("BODY");
            body2.addText(e.toString());
            final XMLWriter w = (XMLWriter)new HTMLWriter((Writer)res.getWriter(), format);
            w.write(doc2);
            w.close();
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationFormUploadURLRequest(final HttpServletRequest req, final HttpServletResponse res) {
        try {
            final String baseURL = "http://" + req.getServerName() + ":" + req.getLocalPort() + req.getContextPath();
            String URL = baseURL + "/" + "genopub" + "/" + "annotationUploadFiles";
            URL = URL + ";jsessionid=" + req.getRequestedSessionId();
            final StringBuffer fileExtensions = new StringBuffer();
            for (int x = 0; x < Constants.ANNOTATION_FILE_EXTENSIONS.length; ++x) {
                if (fileExtensions.length() > 0) {
                    fileExtensions.append(";");
                }
                fileExtensions.append("*" + Constants.ANNOTATION_FILE_EXTENSIONS[x]);
            }
            res.setContentType("application/xml");
            res.getOutputStream().println("<UploadURL url='" + URL + "'" + " fileExtensions='" + fileExtensions.toString() + "'" + "/>");
        }
        catch (Exception e) {
            System.out.println("An error has occured in GenoPubServlet handleAnnotationFormUploadURLRequest - " + e.toString());
        }
    }

    private void handleAnnotationUploadRequest(final HttpServletRequest req, final HttpServletResponse res) {
        Session sess = null;
        Integer idAnnotation = null;
        Annotation annotation = null;
        String annotationName = null;
        String codeVisibility = null;
        Integer idGenomeVersion = null;
        Integer idAnnotationGrouping = null;
        Integer idUserGroup = null;
        String fileName = null;
        Transaction tx = null;
        final StringBuffer bypassedFiles = new StringBuffer();
        File tempBulkUploadFile = null;
        try {
            if (this.genoPubSecurity.isGuestRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to upload data.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final DictionaryHelper dh = DictionaryHelper.getInstance(sess);
            res.setDateHeader("Expires", -1L);
            res.setDateHeader("Last-Modified", System.currentTimeMillis());
            res.setHeader("Pragma", "");
            res.setHeader("Cache-Control", "");
            res.setCharacterEncoding("UTF-8");
            final MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
            Part part;
            while ((part = mp.readNextPart()) != null) {
                final String name = part.getName();
                if (part.isParam()) {
                    final ParamPart paramPart = (ParamPart)part;
                    final String value = paramPart.getStringValue();
                    if (name.equals("idAnnotation")) {
                        idAnnotation = new Integer(String.class.cast(value));
                    }
                    else if (name.equals("name")) {
                        annotationName = value;
                    }
                    else if (name.equals("codeVisibility")) {
                        codeVisibility = value;
                    }
                    else if (name.equals("idGenomeVersion")) {
                        idGenomeVersion = new Integer(value);
                    }
                    else if (name.equals("idAnnotationGrouping")) {
                        if (value != null && !value.equals("")) {
                            idAnnotationGrouping = new Integer(value);
                        }
                    }
                    else if (name.equals("idUserGroup") && value != null && !value.equals("")) {
                        idUserGroup = new Integer(value);
                    }
                }
                if (idAnnotation != null) {
                    break;
                }
                if (annotationName != null && codeVisibility != null && idGenomeVersion != null && idAnnotationGrouping != null && idUserGroup != null) {
                    break;
                }
            }
            if (idAnnotation != null) {
                annotation = (Annotation)sess.get((Class)Annotation.class, (Serializable)idAnnotation);
            }
            else {
                if (annotationName.contains("/") || annotationName.contains("&")) {
                    throw new InvalidNameException("The annotation name cannnot contain characters / or &.");
                }
                annotation = this.createNewAnnotation(sess, annotationName, codeVisibility, idGenomeVersion, (idAnnotationGrouping == -99) ? null : idAnnotationGrouping, (idUserGroup == -99) ? null : idUserGroup);
                sess.flush();
            }
            if (annotation != null) {
                if (!this.genoPubSecurity.canWrite(annotation)) {
                    throw new InsufficientPermissionException("Bypassing upload of annotation " + annotation.getName() + " due to insufficient permissions.");
                }
                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                if (!new File(this.genometry_genopub_dir).exists()) {
                    final boolean success = new File(this.genometry_genopub_dir).mkdir();
                    if (!success) {
                        throw new IOException("Unable to create directory " + this.genometry_genopub_dir);
                    }
                }
                final String annotationFileDir = annotation.getDirectory(this.genometry_genopub_dir);
                if (!new File(annotationFileDir).exists()) {
                    final boolean success2 = new File(annotationFileDir).mkdir();
                    if (!success2) {
                        throw new IOException("Unable to create directory " + annotationFileDir);
                    }
                }
                while ((part = mp.readNextPart()) != null) {
                    if (part.isFile()) {
                        final FilePart filePart = (FilePart)part;
                        fileName = filePart.getFileName();
                        if (fileName.endsWith("bulkUpload")) {
                            tempBulkUploadFile = new File(this.genometry_genopub_dir, "TempFileDeleteMe_" + USeqArchive.createRandowWord(6));
                            filePart.writeTo(tempBulkUploadFile);
                            final AnnotationGrouping ag = this.getDefaultAnnotationGrouping(annotation, sess, idAnnotationGrouping);
                            this.uploadBulkAnnotations(sess, tempBulkUploadFile, annotation, ag, res);
                            if (tempBulkUploadFile.exists()) {
                                if (!tempBulkUploadFile.delete()) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + tempBulkUploadFile.getName() + " during bulk upload.");
                                    break;
                                }
                                break;
                            }
                        }
                        if (!Util.isValidAnnotationFileType(fileName)) {
                            final String message = "Bypassing upload of annotation file  " + fileName + " for annotation " + annotation.getName() + ".  Unsupported file extension.";
                            throw new UnsupportedFileTypeException(message);
                        }
                        if (fileName.toUpperCase().endsWith(".BAR")) {
                            final GenomeVersion genomeVersion = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)annotation.getIdGenomeVersion()));
                            if (!Util.fileHasSegmentName(fileName, genomeVersion)) {
                                final String message2 = "Bypassing upload of annotation file  " + fileName + " for annotation " + annotation.getName() + ".  File name is invalid because it does not start with a valid segment name.";
                                throw new IncorrectFileNameException(message2);
                            }
                        }
                        if (fileName == null) {
                            continue;
                        }
                        final File file = new File(annotationFileDir, fileName);
                        final long size = filePart.writeTo(file);
                        if (Util.tooManyLines(file)) {
                            if (!file.delete()) {
                                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + file.getName() + ".");
                            }
                            throw new FileTooBigException("Aborting upload, text formatted annotation file '" + annotation.getName() + " exceeds the maximum allowed size (" + 10000 + " lines). Convert to xxx.useq (see http://useq.sourceforge.net/useqArchiveFormat.html) or other binary form (xxx.bar).");
                        }
                        if (!fileName.toUpperCase().endsWith(".BAM")) {
                            continue;
                        }
                        final String error = checkBamFile(file);
                        if (error != null) {
                            if (!file.delete()) {
                                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + file.getName() + ".");
                            }
                            throw new MalformedBamFileException("Errors found with bam file -> " + fileName + ". Aborting upload. " + error);
                        }
                        continue;
                    }
                }
                sess.flush();
            }
            tx.commit();
            this.reportSuccess(res, "idAnnotation", annotation.getIdAnnotation());
        }
        catch (InvalidNameException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e.getMessage(), 907);
        }
        catch (MalformedBamFileException e2) {
            Logger.getLogger(this.getClass().getName()).warning(e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e2.getMessage(), 906);
        }
        catch (InsufficientPermissionException e3) {
            Logger.getLogger(this.getClass().getName()).warning(e3.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e3.getMessage(), 904);
        }
        catch (IncorrectFileNameException e4) {
            Logger.getLogger(this.getClass().getName()).warning(e4.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e4.getMessage(), 903);
        }
        catch (FileTooBigException e5) {
            Logger.getLogger(this.getClass().getName()).warning(e5.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e5.getMessage(), 905);
        }
        catch (UnsupportedFileTypeException e6) {
            Logger.getLogger(this.getClass().getName()).warning(e6.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e6.getMessage(), 902);
        }
        catch (IOException e7) {
            Logger.getLogger(this.getClass().getName()).warning(e7.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            this.reportError(res, e7.getMessage(), 908);
        }
        catch (Exception e8) {
            Logger.getLogger(this.getClass().getName()).warning(e8.toString());
            if (tx != null) {
                tx.rollback();
            }
            e8.printStackTrace();
            this.reportError(res, e8.toString(), 901);
        }
        finally {
            if (tempBulkUploadFile != null && tempBulkUploadFile.exists()) {
                tempBulkUploadFile.delete();
            }
            if (sess != null) {
                sess.close();
            }
        }
    }

    public static String checkBamFile(final File bamFile) {
        String message = null;
        SAMFileReader reader = null;
        final Pattern oneTwoDigit = Pattern.compile("\\w{1,2}");
        try {
            reader = new SAMFileReader(bamFile);
            final SAMFileHeader h = reader.getFileHeader();
            if (h.getSortOrder().compareTo(SAMFileHeader.SortOrder.coordinate) != 0) {
                throw new Exception("Your bam file doesn't appear to be sorted by coordinate.");
            }
            final List<SAMSequenceRecord> chroms = (List<SAMSequenceRecord>)h.getSequenceDictionary().getSequences();
            final StringBuilder badChroms = new StringBuilder();
            boolean badMito = false;
            for (final SAMSequenceRecord r : chroms) {
                if (oneTwoDigit.matcher(r.getSequenceName()).matches()) {
                    badChroms.append(r.getSequenceName() + " ");
                }
                if (r.getSequenceName().equals("chrMT")) {
                    badMito = true;
                }
            }
            if (badChroms.length() != 0) {
                throw new Exception("\nYour bam file contains chromosomes that are 1-2 letters/ numbers long. For DAS compatibility they should start with 'chr' for chromosomes and something longish for contigs/ unassembled segments, see -> " + (Object)badChroms + "\n");
            }
            if (badMito) {
                throw new Exception("\nYour bam file contains a chrMT chromosome. For DAS compatibility convert it to chrM.");
            }
            final SAMRecordIterator it = reader.iterator();
            if (it.hasNext()) {
                it.next();
            }
            reader.close();
        }
        catch (Exception e) {
            message = e.getMessage();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
        return message;
    }

    private String validateBulkUploadFile(final File spreadSheet) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(spreadSheet));
            final StringBuilder errors = new StringBuilder();
            final HashSet<String> bamBaiFiles = new HashSet<String>();
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0 && !line.startsWith("#")) {
                    if (line.startsWith("Name")) {
                        continue;
                    }
                    final Matcher mat = GenoPubServlet.BULK_UPLOAD_LINE_SPLITTER.matcher(line);
                    if (!mat.matches()) {
                        errors.append("Malformed data line -> " + line + " . \n");
                    }
                    else {
                        final String name = mat.group(1).trim();
                        if (name.length() == 0) {
                            errors.append("Missing name -> " + line + " . \n");
                        }
                        final File dataFile = new File(mat.group(2).trim());
                        if (!dataFile.canRead() || !dataFile.canWrite()) {
                            errors.append("Cannot find/modify file -> " + line + " . \n");
                        }
                        else {
                            final String fileName = dataFile.toString();
                            if (!Util.isValidAnnotationFileType(fileName)) {
                                errors.append("Unsupported file type ->  " + line + " . \n");
                            }
                            else {
                                if (Util.tooManyLines(dataFile)) {
                                    errors.append("Too many lines in file ->  " + line + " . Convert to xxx.useq (see http://useq.sourceforge.net/useqArchiveFormat.html).\n");
                                }
                                if (fileName.endsWith(".bam") || fileName.endsWith(".bai")) {
                                    bamBaiFiles.add(name + "__" + fileName);
                                }
                                if (!fileName.endsWith(".bam")) {
                                    continue;
                                }
                                final String log = checkBamFile(dataFile);
                                if (log == null) {
                                    continue;
                                }
                                errors.append("Problems were found with this bam file ->  " + line + " . " + log);
                            }
                        }
                    }
                }
            }
            for (final String f : bamBaiFiles) {
                if (f.endsWith(".bam")) {
                    final String bai1 = f.substring(0, f.length() - 4) + ".bai";
                    final String bai2 = f + ".bai";
                    if (bamBaiFiles.contains(bai1) || bamBaiFiles.contains(bai2)) {
                        continue;
                    }
                    errors.append("Missing xxx.bai index file for ->  " + f + " . \n");
                }
                else {
                    String bam = f.substring(0, f.length() - 4);
                    if (!bam.endsWith(".bam")) {
                        bam += ".bam";
                    }
                    if (bamBaiFiles.contains(bam)) {
                        continue;
                    }
                    errors.append("Missing xxx.bam alignment file for ->  " + f + " . \n");
                }
            }
            if (errors.length() != 0) {
                errors.append("Aborting bulk uploading. \n");
                return errors.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return null;
    }

    private void uploadBulkAnnotations(final Session sess, final File spreadSheet, final Annotation sourceAnnotation, final AnnotationGrouping defaultAnnotationGrouping, final HttpServletResponse res) throws Exception {
        final String errors = this.validateBulkUploadFile(spreadSheet);
        if (errors != null) {
            throw new BulkFileUploadException(errors);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(spreadSheet));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0 && !line.startsWith("#")) {
                    if (line.startsWith("Name")) {
                        continue;
                    }
                    final Matcher mat = GenoPubServlet.BULK_UPLOAD_LINE_SPLITTER.matcher(line);
                    mat.matches();
                    String name = mat.group(1).trim();
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    final File dataFile = new File(mat.group(2).trim());
                    final String summary = mat.group(3).trim();
                    final String description = mat.group(4).trim();
                    String annotationName = "";
                    AnnotationGrouping ag = null;
                    if (name.lastIndexOf("/") >= 0) {
                        annotationName = name.substring(name.lastIndexOf("/") + 1);
                        ag = this.getSpecifiedAnnotationGrouping(sess, defaultAnnotationGrouping, name.substring(0, name.lastIndexOf("/")));
                    }
                    else {
                        annotationName = name;
                        ag = defaultAnnotationGrouping;
                    }
                    final File dir = this.fetchAnnotationDirectory(ag, annotationName);
                    if (dir != null) {
                        final File moved = new File(dir, dataFile.getName());
                        if (!dataFile.renameTo(moved)) {
                            throw new BulkFileUploadException("Failed to move the dataFile '" + dataFile + "' to its archive location  '" + moved + "' . Aborting bulk uploading.");
                        }
                        continue;
                    }
                    else {
                        this.addNewClonedAnnotation(sess, sourceAnnotation, annotationName, summary, description, dataFile, ag, res);
                    }
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e2) {
                    throw e2;
                }
            }
        }
    }

    private File fetchAnnotationDirectory(final AnnotationGrouping ag, final String annotationName) {
        for (final Annotation a : (Set<Annotation>) ag.getAnnotations()) {
            if (a.getName().equals(annotationName)) {
                return new File(a.getDirectory(this.genometry_genopub_dir));
            }
        }
        return null;
    }

    private AnnotationGrouping getDefaultAnnotationGrouping(final Annotation sourceAnnot, final Session sess, final Integer idAnnotationGrouping) throws Exception {
        AnnotationGrouping ag = null;
        if (idAnnotationGrouping == null || idAnnotationGrouping == -99) {
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)sourceAnnot.getIdGenomeVersion()));
            ag = gv.getRootAnnotationGrouping();
            if (ag == null) {
                throw new Exception("Cannot find root annotation grouping for " + gv.getName());
            }
        }
        else {
            ag = AnnotationGrouping.class.cast(sess.load((Class)AnnotationGrouping.class, (Serializable)idAnnotationGrouping));
        }
        return ag;
    }

    private AnnotationGrouping getSpecifiedAnnotationGrouping(final Session sess, final AnnotationGrouping annotationGroupingBase, final String name) {
        AnnotationGrouping agNext = annotationGroupingBase;
        final String[] tokens = name.split("/");
        AnnotationGrouping agCurrent = annotationGroupingBase;
        for (int x = 0; x < tokens.length; ++x) {
            final String agName = tokens[x];
            agNext = null;
            final Iterator<?> i = agCurrent.getAnnotationGroupings().iterator();
            while (i.hasNext()) {
                final AnnotationGrouping ag = AnnotationGrouping.class.cast(i.next());
                if (ag.getName().equalsIgnoreCase(agName)) {
                    agNext = ag;
                    break;
                }
            }
            if (agNext == null) {
                agNext = new AnnotationGrouping();
                agNext.setName(agName);
                agNext.setIdParentAnnotationGrouping(agCurrent.getIdAnnotationGrouping());
                agNext.setIdGenomeVersion(agCurrent.getIdGenomeVersion());
                agNext.setIdUserGroup(agCurrent.getIdUserGroup());
                sess.save((Object)agNext);
                sess.flush();
                sess.refresh((Object)agNext);
                sess.refresh((Object)agCurrent);
            }
            agCurrent = agNext;
        }
        return agNext;
    }

    private void addNewClonedAnnotation(final Session sess, final Annotation sourceAnnot, final String name, final String summary, final String description, final File dataFile, final AnnotationGrouping ag, final HttpServletResponse res) throws BulkFileUploadException, InsufficientPermissionException {
        if (!this.genoPubSecurity.canWrite(sourceAnnot)) {
            throw new InsufficientPermissionException("Insufficient permision to write annotation.");
        }
        final Annotation dup = new Annotation();
        dup.setName(name);
        if (description.length() != 0) {
            dup.setDescription(description);
        }
        else {
            dup.setDescription(sourceAnnot.getDescription());
        }
        if (summary.length() != 0) {
            dup.setSummary(summary);
        }
        else {
            dup.setSummary(sourceAnnot.getSummary());
        }
        dup.setCodeVisibility(sourceAnnot.getCodeVisibility());
        dup.setIdUserGroup(sourceAnnot.getIdUserGroup());
        dup.setIdUser(sourceAnnot.getIdUser());
        dup.setIdGenomeVersion(sourceAnnot.getIdGenomeVersion());
        dup.setIsLoaded("N");
        dup.setCreateDate(new Date(System.currentTimeMillis()));
        dup.setCreatedBy(this.genoPubSecurity.getUserName());
        dup.setDataPath(this.genometry_genopub_dir);
        sess.save((Object)dup);
        final Set<AnnotationProperty> clonedAPSet = new HashSet<AnnotationProperty>();
        for (final AnnotationProperty sourceAP : (Set<AnnotationProperty>) sourceAnnot.getAnnotationProperties()) {
            final AnnotationProperty clonedAP = new AnnotationProperty();
            clonedAP.setIdProperty(sourceAP.getIdProperty());
            clonedAP.setName(sourceAP.getName());
            clonedAP.setValue(sourceAP.getValue());
            clonedAP.setIdAnnotation(dup.getIdAnnotation());
            sess.save((Object)clonedAP);
            sess.flush();
            clonedAPSet.add(clonedAP);
            final Set<AnnotationPropertyValue> clonedAPV = new HashSet<AnnotationPropertyValue>();
            for (final AnnotationPropertyValue sourceAV : (Set<AnnotationPropertyValue>)sourceAP.getValues()) {
                final AnnotationPropertyValue clonedAV = new AnnotationPropertyValue();
                clonedAV.setIdAnnotationProperty(clonedAP.getIdAnnotationProperty());
                clonedAV.setValue(sourceAV.getValue());
                sess.save((Object)clonedAV);
                clonedAPV.add(clonedAV);
            }
            clonedAP.setValues(clonedAPV);
            final TreeSet<PropertyOption> clonedOptions = new TreeSet<PropertyOption>(new PropertyOptionComparator());
            for (final PropertyOption sourceOption : (Set<PropertyOption>)sourceAP.getOptions()) {
                clonedOptions.add(sourceOption);
            }
            clonedAP.setOptions(clonedOptions);
        }
        dup.setAnnotationProperties(clonedAPSet);
        final TreeSet<User> collaborators = new TreeSet<User>(new UserComparator());
        final Iterator<?> cIt = sourceAnnot.getCollaborators().iterator();
        while (cIt.hasNext()) {
            collaborators.add((User)cIt.next());
        }
        dup.setCollaborators(collaborators);
        sess.save((Object)dup);
        sess.flush();
        final Set<Annotation> newAnnotations = new TreeSet<Annotation>(new AnnotationComparator());
        final Iterator<?> j = ag.getAnnotations().iterator();
        while (j.hasNext()) {
            final Annotation a = Annotation.class.cast(j.next());
            newAnnotations.add(a);
        }
        newAnnotations.add(dup);
        ag.setAnnotations(newAnnotations);
        sess.flush();
        dup.setFileName("A" + dup.getIdAnnotation());
        final File dir = new File(this.genometry_genopub_dir, dup.getFileName());
        if (!dir.mkdir()) {
            throw new BulkFileUploadException("Failed to move the dataFile '" + dataFile + "' to its archive location.  Rename failed . Aborting bulk uploading.");
        }
        final File moved = new File(dir, dataFile.getName());
        if (!dataFile.renameTo(moved)) {
            throw new BulkFileUploadException("Failed to move the dataFile '" + dataFile + "' to its archive location  '" + moved + "' . Aborting bulk uploading.");
        }
    }

    private void handleAnnotationEstimateDownloadSizeRequest(final HttpServletRequest req, final HttpServletResponse res) {
        Session sess = null;
        final String keys = req.getParameter("keys");
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            long estimatedDownloadSize = 0L;
            long uncompressedDownloadSize = 0L;
            final String[] keyTokens = keys.split(":");
            for (int x = 0; x < keyTokens.length; ++x) {
                final String key = keyTokens[x];
                final String[] idTokens = key.split(",");
                if (idTokens.length != 2) {
                    throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idAnnotation and idAnnotationGrouping");
                }
                final Integer idAnnotation = new Integer(idTokens[0]);
                final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
                for (final File file : annotation.getFiles(this.genometry_genopub_dir)) {
                    double compressionRatio = 1.0;
                    if (file.getName().toUpperCase().endsWith("BAR")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BED")) {
                        compressionRatio = 2.5;
                    }
                    else if (file.getName().toUpperCase().endsWith("GFF")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BRS")) {
                        compressionRatio = 4.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BGN")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BGR")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BP1")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BP2")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("CYT")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("GTF")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("PSL")) {
                        compressionRatio = 3.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("USEQ")) {
                        compressionRatio = 1.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("BNIB")) {
                        compressionRatio = 2.0;
                    }
                    else if (file.getName().toUpperCase().endsWith("FASTA")) {
                        compressionRatio = 2.0;
                    }
                    estimatedDownloadSize += new BigDecimal(file.length() / compressionRatio).longValue();
                    uncompressedDownloadSize += file.length();
                }
            }
            req.getSession().setAttribute("genopubDownloadKeys", (Object)keys);
            this.reportSuccess(res, "size", Long.valueOf(estimatedDownloadSize).toString(), "uncompressedSize", uncompressedDownloadSize);
        }
        catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).warning(e.toString());
            e.printStackTrace();
            this.reportError(res, e.toString(), 901);
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationDownloadRequest(final HttpServletRequest req, final HttpServletResponse res) {
        Session sess = null;
        final String keys = (String)req.getSession().getAttribute("genopubDownloadKeys");
        req.getSession().setAttribute("genopubDownloadKeys", (Object)"");
        final ArchiveHelper archiveHelper = new ArchiveHelper();
        while (true) {
            if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
                archiveHelper.setMode(req.getParameter("mode"));
                try {
                    if (keys == null || keys.equals("")) {
                        throw new Exception("Cannot perform download due to empty keys parameter.");
                    }
                    sess = (Session)HibernateUtil.getSessionFactory().openSession();
                    res.setContentType("application/x-download");
                    res.setHeader("Content-Disposition", "attachment;filename=genopub_annotations.zip");
                    res.setHeader("Cache-Control", "max-age=0, must-revalidate");
                    archiveHelper.setTempDir("./");
                    TarArchiveOutputStream tarOut = null;
                    ZipOutputStream zipOut = null;
                    if (archiveHelper.isZipMode()) {
                        zipOut = new ZipOutputStream((OutputStream)res.getOutputStream());
                    }
                    else {
                        tarOut = new TarArchiveOutputStream((OutputStream)res.getOutputStream());
                    }
                    long totalArchiveSize = 0L;
                    final String[] keyTokens = keys.split(":");
                    for (int x = 0; x < keyTokens.length; ++x) {
                        final String key = keyTokens[x];
                        final String[] idTokens = key.split(",");
                        if (idTokens.length != 2) {
                            throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idAnnotation and idAnnotationGrouping");
                        }
                        final Integer idAnnotation = new Integer(idTokens[0]);
                        final Integer idAnnotationGrouping = new Integer(idTokens[1]);
                        final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
                        if (!this.genoPubSecurity.canRead(annotation)) {
                            throw new InsufficientPermissionException("Insufficient permission to read/download annotation.");
                        }
                        AnnotationGrouping annotationGrouping = null;
                        if (idAnnotationGrouping == -99) {
                            final DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                            final GenomeVersion gv = dh.getGenomeVersion(annotation.getIdGenomeVersion());
                            annotationGrouping = gv.getRootAnnotationGrouping();
                        }
                        else {
                            final Iterator<?> i = annotation.getAnnotationGroupings().iterator();
                            while (i.hasNext()) {
                                final AnnotationGrouping ag = AnnotationGrouping.class.cast(i.next());
                                if (ag.getIdAnnotationGrouping().equals(idAnnotationGrouping)) {
                                    annotationGrouping = ag;
                                    break;
                                }
                            }
                        }
                        if (annotationGrouping == null) {
                            throw new Exception("Unable to find annotation grouping " + idAnnotationGrouping);
                        }
                        final String path = annotationGrouping.getQualifiedName() + "/" + annotation.getName() + "/";
                        for (final File file : annotation.getFiles(this.genometry_genopub_dir)) {
                            final String zipEntryName = path + file.getName();
                            archiveHelper.setArchiveEntryName(zipEntryName);
                            final InputStream in = archiveHelper.getInputStreamToArchive(file.getAbsolutePath(), zipEntryName);
                            ZipEntry zipEntry = null;
                            if (archiveHelper.isZipMode()) {
                                zipEntry = new ZipEntry(archiveHelper.getArchiveEntryName());
                                zipOut.putNextEntry(zipEntry);
                            }
                            else {
                                final TarArchiveEntry entry = new TarArchiveEntry(archiveHelper.getArchiveEntryName());
                                entry.setSize(archiveHelper.getArchiveFileSize());
                                tarOut.putArchiveEntry((ArchiveEntry)entry);
                            }
                            OutputStream out = null;
                            if (archiveHelper.isZipMode()) {
                                out = zipOut;
                            }
                            else {
                                out = (OutputStream)tarOut;
                            }
                            final int size = archiveHelper.transferBytes(in, out);
                            totalArchiveSize += size;
                            if (archiveHelper.isZipMode()) {
                                zipOut.closeEntry();
                                totalArchiveSize += zipEntry.getCompressedSize();
                            }
                            else {
                                tarOut.closeArchiveEntry();
                                totalArchiveSize += archiveHelper.getArchiveFileSize();
                            }
                            archiveHelper.removeTemporaryFile();
                        }
                    }
                    if (archiveHelper.isZipMode()) {
                        zipOut.finish();
                        zipOut.flush();
                    }
                    else {
                        tarOut.close();
                        tarOut.flush();
                    }
                }
                catch (InsufficientPermissionException e) {
                    Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
                    this.reportError(res, e.getMessage(), 904);
                }
                catch (Exception e2) {
                    Logger.getLogger(this.getClass().getName()).warning(e2.toString());
                    e2.printStackTrace();
                    this.reportError(res, e2.toString(), 901);
                }
                finally {
                    if (sess != null) {
                        sess.close();
                    }
                }
                return;
            }
            continue;
        }
    }

    private void handleAnnotationFDTDownloadRequest(final HttpServletRequest req, final HttpServletResponse res) {
        Session sess = null;
        final String keys = (String)req.getSession().getAttribute("genopubDownloadKeys");
        req.getSession().setAttribute("genopubDownloadKeys", (Object)"");
        try {
            if (keys == null || keys.equals("")) {
                throw new Exception("Cannot perform download due to empty keys parameter.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final UUID uuid = UUID.randomUUID();
            final String uuidStr = uuid.toString();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final String download_base = "genopub_download_" + dateFormat.format(Calendar.getInstance().getTime());
            final String fdt_base_dir_genopub = this.getFDTDirForGenoPub() + uuidStr;
            final File fdtBaseDir = new File(fdt_base_dir_genopub);
            if (!fdtBaseDir.exists() && !fdtBaseDir.mkdir()) {
                throw new Exception("unable to create fdt directory " + fdtBaseDir);
            }
            final String fdt_dir_genopub = this.getFDTDirForGenoPub() + uuidStr + "/" + download_base;
            final String fdt_dir = this.getFDTDir() + uuidStr + "/" + download_base;
            final String[] keyTokens = keys.split(":");
            for (int x = 0; x < keyTokens.length; ++x) {
                final String key = keyTokens[x];
                final String[] idTokens = key.split(",");
                if (idTokens.length != 2) {
                    throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idAnnotation and idAnnotationGrouping");
                }
                final Integer idAnnotation = new Integer(idTokens[0]);
                final Integer idAnnotationGrouping = new Integer(idTokens[1]);
                final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)idAnnotation));
                if (!this.genoPubSecurity.canRead(annotation)) {
                    throw new InsufficientPermissionException("Insufficient permission to read/download annotation.");
                }
                AnnotationGrouping annotationGrouping = null;
                if (idAnnotationGrouping == -99) {
                    final DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                    final GenomeVersion gv = dh.getGenomeVersion(annotation.getIdGenomeVersion());
                    annotationGrouping = gv.getRootAnnotationGrouping();
                }
                else {
                    final Iterator<?> i = annotation.getAnnotationGroupings().iterator();
                    while (i.hasNext()) {
                        final AnnotationGrouping ag = AnnotationGrouping.class.cast(i.next());
                        if (ag.getIdAnnotationGrouping().equals(idAnnotationGrouping)) {
                            annotationGrouping = ag;
                            break;
                        }
                    }
                }
                if (annotationGrouping == null) {
                    throw new Exception("Unable to find annotation grouping " + idAnnotationGrouping);
                }
                final String sourcePath = annotationGrouping.getQualifiedName() + "/" + annotation.getName() + "/";
                for (final File file : annotation.getFiles(this.genometry_genopub_dir)) {
                    final String fdtFullDirName = fdt_dir_genopub + "/" + sourcePath;
                    final File fdtFullDir = new File(fdtFullDirName);
                    if (!fdtFullDir.exists() && !fdtFullDir.mkdirs()) {
                        throw new Exception("unable to create fdt directory " + fdtFullDir);
                    }
                    final String fdtLinkName = fdtFullDirName + file.getName();
                    final Process process = Runtime.getRuntime().exec(new String[] { "ln", "-s", file.getAbsolutePath(), fdtLinkName });
                    process.waitFor();
                    process.destroy();
                }
            }
            res.setHeader("Content-Disposition", "attachment;filename=\"genopub_fdt_download.jnlp\"");
            res.setContentType("application/jnlp");
            res.setHeader("Cache-Control", "max-age=0, must-revalidate");
            final ServletOutputStream out = res.getOutputStream();
            final String title = "FDT Download of GenoPub Files";
            out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            out.println("<jnlp spec=\"1.0\"");
            out.println("codebase=\"" + this.getFDTClientCodebase() + "\">");
            out.println("<information>");
            out.println("<title>title</title>");
            out.println("<vendor>Sun Microsystems, Inc.</vendor>");
            out.println("<offline-allowed/>");
            out.println("</information>");
            out.println("<security> ");
            out.println("<all-permissions/> ");
            out.println("</security>");
            out.println("<resources>");
            out.println("<jar href=\"fdtClient.jar\"/>");
            out.println("<j2se version=\"1.6+\"/>");
            out.println("</resources>");
            out.println("<application-desc main-class=\"gui.FdtMain\">");
            out.println("<argument>" + this.getFDTServerName() + "</argument>");
            out.println("<argument>download</argument>");
            out.println("<argument>" + fdt_dir + "</argument>");
            out.println("</application-desc>");
            out.println("</jnlp>");
            out.close();
            out.flush();
        }
        catch (InsufficientPermissionException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            this.reportError(res, e.getMessage(), 904);
        }
        catch (Exception e2) {
            Logger.getLogger(this.getClass().getName()).warning(e2.toString());
            e2.printStackTrace();
            this.reportError(res, e2.toString(), 901);
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleAnnotationFDTUploadRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)Util.getIntegerParameter(request, "idAnnotation")));
            if (!this.genoPubSecurity.canWrite(annotation)) {
                throw new InsufficientPermissionException("Insufficient permision to write annotation.");
            }
            final String targetDir = annotation.getDirectory(this.genometry_genopub_dir);
            final UUID uuid = UUID.randomUUID();
            final String uuidStr = uuid.toString();
            final String fdt_task_dir = this.getFDTTaskDir();
            final String fdt_dir_genopub = this.getFDTDirForGenoPub() + uuidStr;
            final String fdt_dir = this.getFDTDir() + uuidStr;
            final File dir = new File(fdt_dir_genopub);
            final boolean isDirCreated = dir.mkdir();
            if (!isDirCreated) {
                throw new Exception("Unable to create " + fdt_dir_genopub + " directory.");
            }
            if (!new File(targetDir).exists()) {
                final boolean success = new File(targetDir).mkdir();
                if (!success) {
                    throw new Exception("Unable to create directory " + targetDir);
                }
            }
            final Process process = Runtime.getRuntime().exec(new String[] { "chmod", "777", fdt_dir_genopub });
            process.waitFor();
            process.destroy();
            addFDTDaemonTask(fdt_task_dir, fdt_dir_genopub, targetDir);
            res.setHeader("Content-Disposition", "attachment;filename=\"genopub_fdt_upload.jnlp\"");
            res.setContentType("application/jnlp");
            res.setHeader("Cache-Control", "max-age=0, must-revalidate");
            final ServletOutputStream out = res.getOutputStream();
            final String title = "GenoPub FDT - Upload " + annotation.getNumber() + " Annotation files";
            out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            out.println("<jnlp spec=\"1.0\"");
            out.println("codebase=\"" + this.getFDTClientCodebase() + "\">");
            out.println("<information>");
            out.println("<title>" + title + "</title>");
            out.println("<vendor>Sun Microsystems, Inc.</vendor>");
            out.println("<offline-allowed/>");
            out.println("</information>");
            out.println("<security> ");
            out.println("<all-permissions/> ");
            out.println("</security>");
            out.println("<resources>");
            out.println("<jar href=\"fdtClient.jar\"/>");
            out.println("<j2se version=\"1.6+\"/>");
            out.println("</resources>");
            out.println("<application-desc main-class=\"gui.FdtMain\">");
            out.println("<argument>" + this.getFDTServerName() + "</argument>");
            out.println("<argument>upload</argument>");
            out.println("<argument>" + fdt_dir + "</argument>");
            out.println("</application-desc>");
            out.println("</jnlp>");
            out.close();
            out.flush();
        }
        catch (InsufficientPermissionException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            this.reportError(res, e.getMessage(), 904);
        }
        catch (Exception e2) {
            Logger.getLogger(this.getClass().getName()).warning(e2.toString());
            e2.printStackTrace();
            this.reportError(res, e2.toString(), 901);
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

	private static void addFDTDaemonTask(String taskFileDir, String sourceDir, String targetDir) throws Exception {

		if (!new File(taskFileDir).exists()) {
			File dir = new File(taskFileDir);
			boolean success = dir.mkdir();
			if (!success) {
				throw new Exception("FDT Upload Error: unable to create task file directory.");
			}
		}

		File taskFile;
		int numTries = 10;
		while(true) {
			String taskFileName = taskFileDir + Long.toString(System.currentTimeMillis())+".txt";
			taskFile = new File(taskFileName);
			if(!taskFile.exists()) {
				boolean success;
				try {
					success = taskFile.createNewFile();
					if (!success) {
						throw new Exception("FDT Upload Error: unable to create task file.");
					}
					break;
				} catch (IOException e) {
					throw new Exception("FDT Error: unable to create task file.");
				}
			}
			// If the file already exists then try again but don't try forever
			numTries--;
			if(numTries == 0) {
				throw new Exception("FDT Upload `Error: Unable to create task file: " + taskFileName);
			}
		}

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(taskFile));
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pw.println("Started: " + f.format(new Date(System.currentTimeMillis())));
			pw.println("LastActivity: 0");
			pw.println("SourceDirectory: " + sourceDir);
			pw.println("TargetDirectory: " + targetDir);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			throw new Exception("FDT Upload IOException: " + e.getMessage());
		}
	}

    private void handleUsersAndGroupsRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("UsersAndGroups");
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            StringBuffer query = new StringBuffer();
            query.append("SELECT      gr, ");
            query.append("            mem   ");
            query.append("FROM        UserGroup as gr   ");
            query.append("LEFT JOIN   gr.members as mem ");
            query.append("ORDER BY    gr.name, mem.lastName, mem.firstName ");
            List<?> rows = (List<?>)sess.createQuery(query.toString()).list();
            String groupNamePrev = "";
            Element groupNode = null;
            Element membersNode = null;
            Element collabsNode = null;
            Element managersNode = null;
            Element userNode = null;
            Element institutesNode = null;
            Element instituteNode = null;
            final HashMap<Integer, Element> groupNodeMap = new HashMap<Integer, Element>();
            Iterator<?> i = rows.iterator();
            while (i.hasNext()) {
                final Object[] row = Object[].class.cast(i.next());
                final UserGroup group = (UserGroup)row[0];
                final User user = (User)row[1];
                if (!this.genoPubSecurity.isManager(group)) {
                    continue;
                }
                if (!group.getName().equals(groupNamePrev)) {
                    groupNode = root.addElement("UserGroup");
                    groupNode.addAttribute("label", group.getName());
                    groupNode.addAttribute("name", group.getName());
                    groupNode.addAttribute("contact", (group.getContact() != null) ? group.getContact() : "");
                    groupNode.addAttribute("email", (group.getEmail() != null) ? group.getEmail() : "");
                    groupNode.addAttribute("idUserGroup", group.getIdUserGroup().toString());
                    groupNode.addAttribute("canWrite", this.genoPubSecurity.canWrite(group) ? "Y" : "N");
                    groupNodeMap.put(group.getIdUserGroup(), groupNode);
                    membersNode = null;
                }
                if (user != null) {
                    if (membersNode == null) {
                        membersNode = groupNode.addElement("members");
                    }
                    userNode = membersNode.addElement("User");
                    userNode.addAttribute("label", user.getLastName() + ", " + user.getFirstName());
                    userNode.addAttribute("name", user.getLastName() + ", " + user.getFirstName());
                    userNode.addAttribute("idUser", user.getIdUser().toString());
                    userNode.addAttribute("type", "Member");
                }
                groupNamePrev = group.getName();
            }
            query = new StringBuffer();
            query.append("SELECT      gr, ");
            query.append("            col   ");
            query.append("FROM        UserGroup as gr   ");
            query.append("JOIN   gr.collaborators as col ");
            query.append("ORDER BY    gr.name, col.lastName, col.firstName ");
            rows = (List<?>)sess.createQuery(query.toString()).list();
            i = rows.iterator();
            while (i.hasNext()) {
                final Object[] row = Object[].class.cast(i.next());
                final UserGroup group = (UserGroup)row[0];
                final User user = (User)row[1];
                if (!this.genoPubSecurity.isManager(group)) {
                    continue;
                }
                groupNode = groupNodeMap.get(group.getIdUserGroup());
                collabsNode = groupNode.element("collaborators");
                if (collabsNode == null) {
                    collabsNode = groupNode.addElement("collaborators");
                }
                userNode = collabsNode.addElement("User");
                userNode.addAttribute("label", user.getLastName() + ", " + user.getFirstName());
                userNode.addAttribute("name", user.getLastName() + ", " + user.getFirstName());
                userNode.addAttribute("idUser", user.getIdUser().toString());
                userNode.addAttribute("type", "Collaborator");
            }
            query = new StringBuffer();
            query.append("SELECT      gr, ");
            query.append("            mgr   ");
            query.append("FROM        UserGroup as gr   ");
            query.append("JOIN   gr.managers as mgr ");
            query.append("ORDER BY    gr.name, mgr.lastName, mgr.firstName ");
            rows = (List<?>)sess.createQuery(query.toString()).list();
            i = rows.iterator();
            while (i.hasNext()) {
                final Object[] row = Object[].class.cast(i.next());
                final UserGroup group = (UserGroup)row[0];
                final User user = (User)row[1];
                groupNode = groupNodeMap.get(group.getIdUserGroup());
                if (!this.genoPubSecurity.isManager(group)) {
                    continue;
                }
                managersNode = groupNode.element("managers");
                if (managersNode == null) {
                    managersNode = groupNode.addElement("managers");
                }
                userNode = managersNode.addElement("User");
                userNode.addAttribute("label", user.getName());
                userNode.addAttribute("name", user.getName());
                userNode.addAttribute("idUser", user.getIdUser().toString());
                userNode.addAttribute("type", "Manager");
            }
            query = new StringBuffer();
            query.append("SELECT      gr, ");
            query.append("            inst   ");
            query.append("FROM        UserGroup as gr   ");
            query.append("JOIN   gr.institutes as inst ");
            query.append("ORDER BY    inst.name ");
            rows = (List<?>)sess.createQuery(query.toString()).list();
            i = rows.iterator();
            while (i.hasNext()) {
                final Object[] row = Object[].class.cast(i.next());
                final UserGroup group = (UserGroup)row[0];
                final Institute institute = (Institute)row[1];
                groupNode = groupNodeMap.get(group.getIdUserGroup());
                if (!this.genoPubSecurity.isManager(group)) {
                    continue;
                }
                institutesNode = groupNode.element("institutes");
                if (institutesNode == null) {
                    institutesNode = groupNode.addElement("institutes");
                }
                instituteNode = institutesNode.addElement("Institute");
                instituteNode.addAttribute("label", institute.getName());
                instituteNode.addAttribute("name", institute.getName());
                instituteNode.addAttribute("idInstitute", institute.getIdInstitute().toString());
            }
            query = new StringBuffer();
            query.append("SELECT      user ");
            query.append("FROM        User as user   ");
            query.append("ORDER BY    user.lastName, user.firstName ");
            final List<?> users = (List<?>)sess.createQuery(query.toString()).list();
            final Iterator<?> j = users.iterator();
            while (j.hasNext()) {
                final User user2 = User.class.cast(j.next());
                userNode = root.addElement("User");
                userNode.addAttribute("label", user2.getName());
                userNode.addAttribute("name", user2.getName());
                userNode.addAttribute("idUser", user2.getIdUser().toString());
                userNode.addAttribute("firstName", (user2.getFirstName() != null) ? user2.getFirstName() : "");
                userNode.addAttribute("lastName", (user2.getLastName() != null) ? user2.getLastName() : "");
                userNode.addAttribute("middleName", (user2.getMiddleName() != null) ? user2.getMiddleName() : "");
                userNode.addAttribute("email", (user2.getEmail() != null) ? user2.getEmail() : "");
                userNode.addAttribute("institute", (user2.getInstitute() != null) ? user2.getInstitute() : "");
                userNode.addAttribute("ucscUrl", (user2.getUcscUrl() != null) ? user2.getUcscUrl() : "http://genome.ucsc.edu");
                userNode.addAttribute("userName", (user2.getUserName() != null) ? user2.getUserName() : "");
                userNode.addAttribute("canWrite", this.genoPubSecurity.canWrite(user2) ? "Y" : "N");
                if (this.genoPubSecurity.canWrite(user2)) {
                    userNode.addAttribute("passwordDisplay", (user2.getPasswordDisplay() != null) ? user2.getPasswordDisplay() : "");
                    final Iterator<?> i2 = user2.getRoles().iterator();
                    while (i2.hasNext()) {
                        final UserRole role = UserRole.class.cast(i2.next());
                        userNode.addAttribute("role", role.getRoleName());
                    }
                    final StringBuffer memberGroups = new StringBuffer();
                    final Iterator<?> i3 = user2.getMemberUserGroups().iterator();
                    while (i3.hasNext()) {
                        final UserGroup memberGroup = UserGroup.class.cast(i3.next());
                        if (memberGroups.length() > 0) {
                            memberGroups.append(", ");
                        }
                        memberGroups.append(memberGroup.getName());
                    }
                    userNode.addAttribute("memberGroups", (memberGroups.length() > 0) ? memberGroups.toString() : "(none)");
                    final StringBuffer collaboratorGroups = new StringBuffer();
                    final Iterator<?> i4 = user2.getCollaboratingUserGroups().iterator();
                    while (i4.hasNext()) {
                        final UserGroup colGroup = UserGroup.class.cast(i4.next());
                        if (collaboratorGroups.length() > 0) {
                            collaboratorGroups.append(", ");
                        }
                        collaboratorGroups.append(colGroup.getName());
                    }
                    userNode.addAttribute("collaboratorGroups", (collaboratorGroups.length() > 0) ? collaboratorGroups.toString() : "(none)");
                    final StringBuffer managerGroups = new StringBuffer();
                    final Iterator<?> i5 = user2.getManagingUserGroups().iterator();
                    while (i5.hasNext()) {
                        final UserGroup mgrGroup = UserGroup.class.cast(i5.next());
                        if (managerGroups.length() > 0) {
                            managerGroups.append(", ");
                        }
                        managerGroups.append(mgrGroup.getName());
                    }
                    userNode.addAttribute("managerGroups", (managerGroups.length() > 0) ? managerGroups.toString() : "(none)");
                }
            }
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleUserAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (!this.genoPubSecurity.isAdminRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to add users.");
            }
            if ((request.getParameter("firstName") == null || request.getParameter("firstName").equals("")) && (request.getParameter("lastName") == null || request.getParameter("lastName").equals(""))) {
                throw new Exception("Please enter first or last name.");
            }
            if (request.getParameter("userName") == null || request.getParameter("userName").equals("")) {
                throw new Exception("Please enter the user name.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final List<?> users = (List<?>)sess.createQuery("SELECT u.userName from User u where u.userName = '" + request.getParameter("userName") + "'").list();
            if (users.size() > 0) {
                throw new Exception("The user name " + request.getParameter("userName") + " is already taken.  Please enter a unique user name.");
            }
            final User user = new User();
            user.setFirstName(request.getParameter("firstName"));
            user.setMiddleName(request.getParameter("middleName"));
            user.setLastName(request.getParameter("lastName"));
            user.setUserName(request.getParameter("userName"));
            sess.save((Object)user);
            sess.flush();
            final UserRole role = new UserRole();
            role.setRoleName("user");
            role.setUserName(user.getUserName());
            role.setIdUser(user.getIdUser());
            sess.save((Object)role);
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "idUser", user.getIdUser());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleUserDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final User user = User.class.cast(sess.load((Class)User.class, (Serializable)Util.getIntegerParameter(request, "idUser")));
            if (!this.genoPubSecurity.canWrite(user)) {
                throw new InsufficientPermissionException("Insufficient permissions to delete user.");
            }
            final Iterator<?> i = user.getRoles().iterator();
            while (i.hasNext()) {
                final UserRole role = UserRole.class.cast(i.next());
                sess.delete((Object)role);
            }
            sess.flush();
            sess.refresh((Object)user);
            sess.delete((Object)user);
            sess.flush();
            tx.commit();
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleUserUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final User user = User.class.cast(sess.load((Class)User.class, (Serializable)Util.getIntegerParameter(request, "idUser")));
            if (!this.genoPubSecurity.canWrite(user)) {
                throw new InsufficientPermissionException("Insufficient permissions to write user.");
            }
            if ((request.getParameter("firstName") == null || request.getParameter("firstName").equals("")) && (request.getParameter("lastName") == null || request.getParameter("lastName").equals(""))) {
                throw new Exception("Please enter first or last name.");
            }
            if (request.getParameter("userName") == null || request.getParameter("userName").equals("")) {
                throw new Exception("Please enter the user name.");
            }
            if (request.getParameter("role") == null || request.getParameter("role").equals("")) {
                throw new Exception("Please select a role (admin, user, guest).");
            }
            boolean userNameChanged = false;
            if (!user.getUserName().equals(request.getParameter("userName"))) {
                userNameChanged = true;
            }
            if (userNameChanged) {
                final List<?> users = (List<?>)sess.createQuery("SELECT u.userName from User u where u.userName = '" + request.getParameter("userName") + "'").list();
                if (users.size() > 0) {
                    throw new Exception("The user name " + request.getParameter("userName") + " is already taken.  Please enter a unique user name.");
                }
                final Iterator<?> i = user.getRoles().iterator();
                while (i.hasNext()) {
                    final UserRole role = UserRole.class.cast(i.next());
                    sess.delete((Object)role);
                    sess.flush();
                }
            }
            user.setFirstName(request.getParameter("firstName"));
            user.setMiddleName(request.getParameter("middleName"));
            user.setLastName(request.getParameter("lastName"));
            user.setUserName(request.getParameter("userName"));
            user.setEmail(request.getParameter("email"));
            user.setInstitute(request.getParameter("institute"));
            user.setUcscUrl(request.getParameter("ucscUrl"));
            if (!request.getParameter("password").equals("XXXX")) {
                final String pw = user.getUserName() + ":" + "Das2" + ":" + request.getParameter("password");
                try {
                    final String digestedPassword = this.getDigestedPassword(pw);
                    user.setPassword(digestedPassword);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Logger.getLogger(this.getClass().getName()).severe("Unabled to get digested password " + e.toString());
                }
            }
            sess.flush();
            if (user.getRoles() != null && !userNameChanged) {
                final Iterator<?> j = user.getRoles().iterator();
                while (j.hasNext()) {
                    final UserRole role2 = UserRole.class.cast(j.next());
                    role2.setRoleName(request.getParameter("role"));
                    role2.setUserName(user.getUserName());
                }
            }
            else {
                final UserRole role3 = new UserRole();
                role3.setRoleName(request.getParameter("role"));
                role3.setUserName(user.getUserName());
                role3.setIdUser(user.getIdUser());
                sess.save((Object)role3);
            }
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "idUser", user.getIdUser());
        }
        catch (InsufficientPermissionException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleUserPasswordRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (this.genoPubSecurity.isGuestRole()) {
                throw new InsufficientPermissionException("Cannot change guest account password");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final User user = User.class.cast(sess.load((Class)User.class, (Serializable)this.genoPubSecurity.getIdUser()));
            if (!request.getParameter("password").equals("XXXX") && !request.getParameter("password").equals("")) {
                final String pw = user.getUserName() + ":" + "Das2" + ":" + request.getParameter("password");
                try {
                    final String digestedPassword = this.getDigestedPassword(pw);
                    user.setPassword(digestedPassword);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Logger.getLogger(this.getClass().getName()).severe("Unabled to get digested password " + e.toString());
                }
            }
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "idUser", user.getIdUser());
        }
        catch (InsufficientPermissionException e2) {
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            this.reportError(res, e3.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGroupAddRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            if (!this.genoPubSecurity.isAdminRole()) {
                throw new InsufficientPermissionException("Insufficient permissions to add groups.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter the group name.");
            }
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final UserGroup group = new UserGroup();
            group.setName(request.getParameter("name"));
            sess.save((Object)group);
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "idUserGroup", group.getIdUserGroup());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGroupDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final UserGroup group = UserGroup.class.cast(sess.load((Class)UserGroup.class, (Serializable)Util.getIntegerParameter(request, "idUserGroup")));
            if (!this.genoPubSecurity.canWrite(group)) {
                throw new InsufficientPermissionException("Insufficient permissions to delete group.");
            }
            sess.delete((Object)group);
            sess.flush();
            tx.commit();
            this.reportSuccess(res);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleGroupUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final UserGroup group = UserGroup.class.cast(sess.load((Class)UserGroup.class, (Serializable)Util.getIntegerParameter(request, "idUserGroup")));
            if (!this.genoPubSecurity.canWrite(group)) {
                throw new InsufficientPermissionException("Insufficient permissions to write group.");
            }
            if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
                throw new Exception("Please enter the group name.");
            }
            group.setName(request.getParameter("name"));
            group.setContact(request.getParameter("contact"));
            group.setEmail(request.getParameter("email"));
            StringReader reader = new StringReader(request.getParameter("membersXML"));
            SAXReader sax = new SAXReader();
            final Document membersDoc = sax.read((Reader)reader);
            final TreeSet<User> members = new TreeSet<User>(new UserComparator());
            final Iterator<?> i = (Iterator<?>)membersDoc.getRootElement().elementIterator();
            while (i.hasNext()) {
                final Element memberNode = (Element)i.next();
                final Integer idUser = new Integer(memberNode.attributeValue("idUser"));
                final User member = User.class.cast(sess.get((Class)User.class, (Serializable)idUser));
                members.add(member);
            }
            group.setMembers(members);
            reader = new StringReader(request.getParameter("collaboratorsXML"));
            sax = new SAXReader();
            final Document collabsDoc = sax.read((Reader)reader);
            final TreeSet<User> collaborators = new TreeSet<User>(new UserComparator());
            final Iterator<?> j = (Iterator<?>)collabsDoc.getRootElement().elementIterator();
            while (j.hasNext()) {
                final Element collabNode = (Element)j.next();
                final Integer idUser2 = new Integer(collabNode.attributeValue("idUser"));
                final User collab = User.class.cast(sess.get((Class)User.class, (Serializable)idUser2));
                collaborators.add(collab);
            }
            group.setCollaborators(collaborators);
            reader = new StringReader(request.getParameter("managersXML"));
            sax = new SAXReader();
            final Document managersDoc = sax.read((Reader)reader);
            final TreeSet<User> managers = new TreeSet<User>(new UserComparator());
            final Iterator<?> k = (Iterator<?>)managersDoc.getRootElement().elementIterator();
            while (k.hasNext()) {
                final Element mgrNode = (Element)k.next();
                final Integer idUser3 = new Integer(mgrNode.attributeValue("idUser"));
                final User mgr = User.class.cast(sess.get((Class)User.class, (Serializable)idUser3));
                managers.add(mgr);
            }
            group.setManagers(managers);
            reader = new StringReader(request.getParameter("institutesXML"));
            sax = new SAXReader();
            final Document institutesDoc = sax.read((Reader)reader);
            final TreeSet<Institute> institutes = new TreeSet<Institute>(new InstituteComparator());
            final Iterator<?> l = (Iterator<?>)institutesDoc.getRootElement().elementIterator();
            while (l.hasNext()) {
                final Element instituteNode = (Element)l.next();
                final Integer idInstitute = new Integer(instituteNode.attributeValue("idInstitute"));
                final Institute institute = Institute.class.cast(sess.get((Class)Institute.class, (Serializable)idInstitute));
                institutes.add(institute);
            }
            group.setInstitutes(institutes);
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "idUserGroup", group.getIdUserGroup());
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleDictionaryAddRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final String dictionaryName = request.getParameter("dictionaryName");
            Integer id = null;
            if (dictionaryName.equals("Property")) {
                final Property prop = new Property();
                prop.setName(request.getParameter("name"));
                prop.setCodePropertyType("TEXT");
                prop.setIsActive(Util.getFlagParameter(request, "isActive"));
                prop.setSortOrder(Util.getIntegerParameter(request, "sortOrder"));
                prop.setIdUser(this.genoPubSecurity.isAdminRole() ? null : this.genoPubSecurity.getIdUser());
                sess.save((Object)prop);
                id = prop.getIdProperty();
            }
            sess.flush();
            tx.commit();
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            root.addAttribute("id", (id != null) ? id.toString() : "");
            root.addAttribute("dictionaryName", dictionaryName);
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleDictionaryDeleteRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final String dictionaryName = request.getParameter("dictionaryName");
            final Integer id = Util.getIntegerParameter(request, "id");
            if (dictionaryName.equals("Property")) {
                final Property prop = Property.class.cast(sess.load((Class)Property.class, (Serializable)id));
                if (!this.genoPubSecurity.canWrite(prop)) {
                    throw new Exception("Insufficient permissions to delete property.");
                }
                final Iterator<?> i = prop.getOptions().iterator();
                while (i.hasNext()) {
                    final PropertyOption option = PropertyOption.class.cast(i.next());
                    sess.delete((Object)option);
                }
                sess.flush();
                sess.delete((Object)prop);
            }
            sess.flush();
            tx.commit();
            this.reportSuccess(res);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleDictionaryUpdateRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            final String dictionaryName = request.getParameter("dictionaryName");
            final Integer id = Util.getIntegerParameter(request, "id");
            if (dictionaryName.equals("Property")) {
                final Property property = Property.class.cast(sess.load((Class)Property.class, (Serializable)id));
                if (!this.genoPubSecurity.canWrite(property)) {
                    throw new InsufficientPermissionException("Insufficient permissions to write property.");
                }
                property.setName(request.getParameter("name"));
                property.setIsActive(Util.getFlagParameter(request, "isActive"));
                if (this.genoPubSecurity.isAdminRole()) {
                    property.setIdUser(Util.getIntegerParameter(request, "idUser"));
                }
                property.setSortOrder(Util.getIntegerParameter(request, "sortOrder"));
                property.setCodePropertyType(request.getParameter("codePropertyType"));
                final StringReader reader = new StringReader(request.getParameter("propertyOptionsXML"));
                final SAXReader sax = new SAXReader();
                final Document optionsDoc = sax.read((Reader)reader);
                Iterator<?> i = property.getOptions().iterator();
                while (i.hasNext()) {
                    final PropertyOption option = PropertyOption.class.cast(i.next());
                    boolean found = false;
                    final Iterator<?> i2 = (Iterator<?>)optionsDoc.getRootElement().elementIterator();
                    while (i2.hasNext()) {
                        final Element optionNode = (Element)i2.next();
                        final String idPropertyOption = optionNode.attributeValue("idPropertyOption");
                        if (idPropertyOption != null && !idPropertyOption.equals("") && option.getIdPropertyOption().equals(new Integer(idPropertyOption))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        sess.delete((Object)option);
                    }
                }
                sess.flush();
                i = (Iterator<?>)optionsDoc.getRootElement().elementIterator();
                while (i.hasNext()) {
                    final Element node = (Element)i.next();
                    final String idPropertyOption2 = node.attributeValue("idPropertyOption");
                    PropertyOption propertyOption = null;
                    if (idPropertyOption2.startsWith("PropertyOption")) {
                        propertyOption = new PropertyOption();
                    }
                    else {
                        propertyOption = PropertyOption.class.cast(sess.get((Class)PropertyOption.class, (Serializable)Integer.valueOf(idPropertyOption2)));
                    }
                    propertyOption.setName(node.attributeValue("name"));
                    propertyOption.setIsActive(node.attributeValue("isActive"));
                    propertyOption.setSortOrder(Integer.valueOf(node.attributeValue("sortOrder")));
                    propertyOption.setIdProperty(property.getIdProperty());
                    if (idPropertyOption2.startsWith("PropertyOption")) {
                        sess.save((Object)propertyOption);
                        sess.flush();
                    }
                }
            }
            sess.flush();
            tx.commit();
            this.reportSuccess(res, "id", id);
        }
        catch (InsufficientPermissionException e) {
            this.reportError(res, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.reportError(res, e2.toString());
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleInstitutesRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Institutes");
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final StringBuffer query = new StringBuffer();
            query.append("SELECT      i ");
            query.append("FROM        Institute as i   ");
            query.append("ORDER BY    i.name ");
            final List<?> rows = (List<?>)sess.createQuery(query.toString()).list();
            final Iterator<?> i = rows.iterator();
            while (i.hasNext()) {
                final Institute in = Institute.class.cast(i.next());
                final Element node = root.addElement("Institute");
                node.addAttribute("idInstitute", in.getIdInstitute().toString());
                node.addAttribute("label", in.getName());
                node.addAttribute("name", in.getName());
                node.addAttribute("description", (in.getDescription() != null) ? in.getDescription() : "");
                node.addAttribute("isActive", (in.getIsActive() != null) ? in.getIsActive() : "Y");
                node.addAttribute("canWrite", this.genoPubSecurity.canWrite(in) ? "Y" : "N");
            }
            final XMLWriter writer = new XMLWriter((OutputStream)res.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleInstitutesSaveRequest(final HttpServletRequest request, final HttpServletResponse res) {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            tx = sess.beginTransaction();
            StringReader reader = new StringReader(request.getParameter("institutesXML"));
            SAXReader sax = new SAXReader();
            final Document institutesDoc = sax.read((Reader)reader);
            final Iterator<?> i = (Iterator<?>)institutesDoc.getRootElement().elementIterator();
            while (i.hasNext()) {
                final Element node = (Element)i.next();
                final String idInstitute = node.attributeValue("idInstitute");
                Institute institute = null;
                if (idInstitute.startsWith("Institute")) {
                    institute = new Institute();
                }
                else {
                    institute = Institute.class.cast(sess.load((Class)Institute.class, (Serializable)Integer.parseInt(idInstitute)));
                }
                if (!this.genoPubSecurity.canWrite(institute)) {
                    throw new InsufficientPermissionException("Insufficient permissions to write institute.");
                }
                institute.setName(node.attributeValue("name"));
                institute.setDescription(node.attributeValue("description"));
                institute.setIsActive(node.attributeValue("isActive"));
                sess.save((Object)institute);
            }
            reader = new StringReader(request.getParameter("institutesToRemoveXML"));
            sax = new SAXReader();
            final Document institutesToRemoveDoc = sax.read((Reader)reader);
            final Iterator<?> j = (Iterator<?>)institutesToRemoveDoc.getRootElement().elementIterator();
            while (j.hasNext()) {
                final Element node2 = (Element)j.next();
                final String idInstitute2 = node2.attributeValue("idInstitute");
                final Institute institute2 = Institute.class.cast(sess.load((Class)Institute.class, (Serializable)Integer.parseInt(idInstitute2)));
                sess.delete((Object)institute2);
            }
            sess.flush();
            tx.commit();
            this.reportSuccess(res);
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private void handleMakeUCSCLinkRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        try {
            final ArrayList<String> urlsToLoad = this.makeUCSCLink(request, res);
            final String url1 = urlsToLoad.get(0);
            String url2 = "";
            if (urlsToLoad.size() == 2) {
                url2 = urlsToLoad.get(1);
            }
            this.reportSuccess(res, "ucscURL1", url1, "ucscURL2", url2);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
        }
    }

    private void handleMakeURLLinksRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        try {
            final ArrayList<String> urlsToLink = this.makeURLLinks(request, res);
            final StringBuilder sb = new StringBuilder(urlsToLink.get(0));
            for (int i = 1; i < urlsToLink.size(); ++i) {
                sb.append("\n\n");
                sb.append(urlsToLink.get(i));
            }
            this.reportSuccess(res, "urlsToLink", sb.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.getMessage());
        }
    }

    private void handleVerifyReloadRequest(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        final StringBuffer invalidGenomeVersions = new StringBuffer();
        final StringBuffer emptyAnnotations = new StringBuffer();
        int loadCount = 0;
        int unloadCount = 0;
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final AnnotationQuery annotationQuery = new AnnotationQuery();
            annotationQuery.runAnnotationQuery(sess, this.genoPubSecurity, true);
            for (final Organism organism : annotationQuery.getOrganisms()) {
                for (final String genomeVersionName : annotationQuery.getVersionNames(organism)) {
                    final GenomeVersion gv = annotationQuery.getGenomeVersion(genomeVersionName);
                    final List<Segment> segments = annotationQuery.getSegments(organism, genomeVersionName);
                    if ((annotationQuery.getQualifiedAnnotations(organism, genomeVersionName).size() > 0 || gv.hasSequence(this.genometry_genopub_dir)) && (segments == null || segments.size() == 0)) {
                        if (invalidGenomeVersions.length() > 0) {
                            invalidGenomeVersions.append(", ");
                        }
                        invalidGenomeVersions.append(genomeVersionName);
                    }
                    for (final QualifiedAnnotation qa : annotationQuery.getQualifiedAnnotations(organism, genomeVersionName)) {
                        if (qa.getAnnotation().getFileCount(this.genometry_genopub_dir) == 0) {
                            if (emptyAnnotations.length() > 0) {
                                emptyAnnotations.append("\n");
                            }
                            emptyAnnotations.append(gv.getName() + ":  ");
                            break;
                        }
                    }
                    boolean firstAnnot = true;
                    for (final QualifiedAnnotation qa2 : annotationQuery.getQualifiedAnnotations(organism, genomeVersionName)) {
                        if (qa2.getAnnotation().getFileCount(this.genometry_genopub_dir) == 0) {
                            if (firstAnnot) {
                                firstAnnot = false;
                            }
                            else if (emptyAnnotations.length() > 0) {
                                emptyAnnotations.append(", ");
                            }
                            emptyAnnotations.append(qa2.getAnnotation().getName());
                        }
                        else {
                            ++loadCount;
                        }
                    }
                    final List<UnloadAnnotation> unloadAnnotations = AnnotationQuery.getUnloadedAnnotations(sess, this.genoPubSecurity, gv);
                    unloadCount += unloadAnnotations.size();
                }
            }
            final StringBuffer confirmMessage = new StringBuffer();
            if (loadCount > 0 || unloadCount > 0) {
                if (loadCount > 0) {
                    confirmMessage.append(loadCount + " annotation(s) and ready to load to DAS/2.\n\n");
                }
                if (unloadCount > 0) {
                    confirmMessage.append(unloadCount + " annotation(s) ready to unload from DAS/2.\n\n");
                }
                confirmMessage.append("Do you wish to continue?\n\n");
            }
            else {
                confirmMessage.append("No annotations are queued for reload.  Do you wish to continue?\n\n");
            }
            final StringBuffer message = new StringBuffer();
            if (invalidGenomeVersions.length() > 0 || emptyAnnotations.length() > 0) {
                if (invalidGenomeVersions.length() > 0) {
                    message.append("Annotations and sequence for the following genome versions will be bypassed due to missing segment information:\n" + invalidGenomeVersions.toString() + ".\n\n");
                }
                if (emptyAnnotations.length() > 0) {
                    message.append("The following empty annotations will be bypassed:\n" + emptyAnnotations.toString() + ".\n\n");
                }
                message.append(confirmMessage.toString());
                this.reportError(res, message.toString());
            }
            else {
                this.reportSuccess(res, confirmMessage.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.reportError(res, e.toString());
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    private String getFlexHTMLWrapper(final HttpServletRequest request) {
        final StringBuffer buf = new StringBuffer();
        BufferedReader input = null;
        try {
            String fileName = this.getServletContext().getRealPath("/");
            fileName = fileName + "/" + "GenoPub.html";
            final FileReader fileReader = new FileReader(fileName);
            input = new BufferedReader(fileReader);
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        if (input != null) {
            try {
                String line = null;
                final String flashVarsLine = null;
                while ((line = input.readLine()) != null) {
                    if (line.contains("var flashvars = {}") && request.getParameter("idAnnotation") != null) {
                        line = line + "    flashvars.idAnnotation = \"" + request.getParameter("idAnnotation") + "\";";
                    }
                    buf.append(line);
                    buf.append(System.getProperty("line.separator"));
                }
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
            finally {
                try {
                    input.close();
                }
                catch (IOException ex3) {}
            }
        }
        return buf.toString();
    }

    private final boolean getGenoPubDir() {
        final ServletContext context = this.getServletContext();
        this.genometry_genopub_dir = context.getInitParameter("genometry_server_dir_genopub");
        if (this.genometry_genopub_dir == null || this.genometry_genopub_dir.equals("")) {
            Logger.getLogger(this.getClass().getName()).severe("Unable to find parameter genometry_server_dir_genopub");
            return false;
        }
        if (!new File(this.genometry_genopub_dir).exists()) {
            final boolean success = new File(this.genometry_genopub_dir).mkdir();
            if (!success) {
                Logger.getLogger(this.getClass().getName()).severe("Unable to create directory " + this.genometry_genopub_dir);
                return false;
            }
        }
        if (this.genometry_genopub_dir != null && !this.genometry_genopub_dir.endsWith("/")) {
            this.genometry_genopub_dir += "/";
        }
        this.genoPubWebAppDir = new File(context.getRealPath("/"));
        Logger.getLogger(this.getClass().getName()).fine("genometry_genopub_dir = " + this.genometry_genopub_dir);
        return true;
    }

    private boolean isFDTSupported() {
        return this.getFDTDir() != null && this.getFDTClientCodebase() != null && this.getFDTDirForGenoPub() != null && this.getFDTServerName() != null;
    }

    private final String getFDTDir() {
        if (this.fdt_dir == null) {
            final ServletContext context = this.getServletContext();
            this.fdt_dir = context.getInitParameter("fdt_dir");
            if (this.fdt_dir != null && !this.fdt_dir.endsWith("/")) {
                this.fdt_dir += "/";
            }
        }
        return this.fdt_dir;
    }

    private final String getFDTTaskDir() {
        if (this.fdt_task_dir == null) {
            final ServletContext context = this.getServletContext();
            this.fdt_task_dir = context.getInitParameter("fdt_task_dir");
            if (this.fdt_task_dir != null && !this.fdt_task_dir.endsWith("/")) {
                this.fdt_task_dir += "/";
            }
        }
        return this.fdt_task_dir;
    }

    private final String getFDTDirForGenoPub() {
        if (this.fdt_dir_genopub == null) {
            final ServletContext context = this.getServletContext();
            this.fdt_dir_genopub = context.getInitParameter("fdt_dir_genopub");
            if (this.fdt_dir_genopub != null && !this.fdt_dir_genopub.endsWith("/")) {
                this.fdt_dir_genopub += "/";
            }
        }
        return this.fdt_dir_genopub;
    }

    private final String getFDTClientCodebase() {
        if (this.fdt_client_codebase == null) {
            final ServletContext context = this.getServletContext();
            this.fdt_client_codebase = context.getInitParameter("fdt_client_codebase");
        }
        return this.fdt_client_codebase;
    }

    private final String getFDTServerName() {
        if (this.fdt_server_name == null) {
            final ServletContext context = this.getServletContext();
            this.fdt_server_name = context.getInitParameter("fdt_server_name");
        }
        return this.fdt_server_name;
    }

    private static final HashMap<String, String> loadFileIntoHashMap(final File file) {
        BufferedReader in = null;
        HashMap<String, String> names = null;
        try {
            names = new HashMap<String, String>();
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final String[] keyValue = line.split("\\s+");
                    if (keyValue.length < 2) {
                        continue;
                    }
                    names.put(keyValue[0], keyValue[1]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose((Closeable)in);
        }
        return names;
    }

    private String getDigestedPassword(final String password) throws NoSuchAlgorithmException {
        final byte[] defaultBytes = password.getBytes();
        final MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(defaultBytes);
        final byte[] messageDigest = algorithm.digest();
        final StringBuffer hexString = new StringBuffer(messageDigest.length * 2);
        for (int i = 0; i < messageDigest.length; ++i) {
            int value1 = messageDigest[i] >> 4;
            value1 &= 0xF;
            if (value1 >= 10) {
                hexString.append((char)(value1 - 10 + 97));
            }
            else {
                hexString.append((char)(value1 + 48));
            }
            int value2 = messageDigest[i] & 0xF;
            value2 &= 0xF;
            if (value2 >= 10) {
                hexString.append((char)(value2 - 10 + 97));
            }
            else {
                hexString.append((char)(value2 + 48));
            }
        }
        final String digestedPassword = hexString.toString();
        return digestedPassword;
    }

    private void reportError(final HttpServletResponse response, final String message) {
        this.reportError(response, message, null);
    }

    private void reportError(final HttpServletResponse response, final String message, final Integer statusCode) {
        try {
            if (statusCode != null) {
                response.setStatus((int)statusCode);
                response.addHeader("message", message);
            }
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("Error");
            root.addAttribute("message", message);
            final XMLWriter writer = new XMLWriter((OutputStream)response.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportSuccess(final HttpServletResponse response) {
        this.reportSuccess(response, null, null);
    }

    private void reportSuccess(final HttpServletResponse response, final String message) {
        try {
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            if (message != null) {
                root.addAttribute("message", message);
            }
            final XMLWriter writer = new XMLWriter((OutputStream)response.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportSuccess(final HttpServletResponse response, final String attributeName, final Object id) {
        try {
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            if (id != null && attributeName != null) {
                root.addAttribute(attributeName, id.toString());
            }
            final XMLWriter writer = new XMLWriter((OutputStream)response.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportSuccess(final HttpServletResponse response, final String attributeName1, final Object id1, final String attributeName2, final Object id2) {
        try {
            final Document doc = DocumentHelper.createDocument();
            final Element root = doc.addElement("SUCCESS");
            if (id1 != null && attributeName1 != null) {
                root.addAttribute(attributeName1, id1.toString());
            }
            if (id2 != null && attributeName2 != null) {
                root.addAttribute(attributeName2, id2.toString());
            }
            final XMLWriter writer = new XMLWriter((OutputStream)response.getOutputStream(), OutputFormat.createCompactFormat());
            writer.write(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File checkUCSCLinkDirectory(final String xml_base) throws Exception {
        final File urlLinkDir = new File(this.genoPubWebAppDir, "URLLinks");
        urlLinkDir.mkdirs();
        if (!urlLinkDir.exists()) {
            throw new Exception("\nFailed to find and or make a directory to contain url softlinks for UCSC data distribution.\n");
        }
        final File redirect = new File(urlLinkDir, "index.html");
        if (!redirect.exists()) {
            final String toWrite = "<html> <head> <META HTTP-EQUIV=\"Refresh\" Content=\"0; URL=" + xml_base + "genopub\"> </head> <body>Access denied.</body>";
            final PrintWriter out = new PrintWriter(new FileWriter(redirect));
            out.println(toWrite);
            out.close();
        }
        Util.deleteNonIndexFiles(urlLinkDir, 7L);
        return urlLinkDir;
    }

    private UCSCLinkFiles fetchURLLinkFiles(final List<File> files) throws Exception {
        if (this.urlLinkFileExtensions == null) {
            this.urlLinkFileExtensions = new HashSet<String>();
            for (final String ext : Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS) {
                this.urlLinkFileExtensions.add(ext);
            }
        }
        File useq = null;
        File bigFile = null;
        final ArrayList<File> filesAL = new ArrayList<File>();
        for (final File f : files) {
            final int index = f.getName().lastIndexOf(".");
            if (index > 0) {
                final String ext2 = f.getName().substring(index);
                if (ext2.equals(".useq")) {
                    useq = f;
                }
                else if (ext2.equals(".bw") || ext2.equals(".bb")) {
                    bigFile = f;
                }
                filesAL.add(f);
            }
        }
        ArrayList<File> convertedUSeqFiles = null;
        if (bigFile == null && useq != null && GenoPubServlet.autoConvertUSeqArchives) {
            final USeq2UCSCBig c = new USeq2UCSCBig(this.ucscWig2BigWigExe, this.ucscBed2BigBedExe, useq);
            convertedUSeqFiles = (ArrayList<File>)c.fetchConvertedFileNames();
            c.convert();
        }
        if (filesAL.size() != 0) {
            boolean stranded = false;
            if (convertedUSeqFiles != null) {
                filesAL.addAll(convertedUSeqFiles);
                if (convertedUSeqFiles.size() == 2) {
                    final String name = convertedUSeqFiles.get(0).getName();
                    if (name.endsWith("_Plus.bw") || name.endsWith("_Minus.bw")) {
                        stranded = true;
                    }
                }
            }
            final File[] toReturn = new File[filesAL.size()];
            filesAL.toArray(toReturn);
            return new UCSCLinkFiles(toReturn, false, stranded);
        }
        return null;
    }

    public static String fetchUCSCDataType(final File[] filesToLink) {
        for (final File f : filesToLink) {
            final String name = f.getName();
            if (name.endsWith(".bw")) {
                return "bigWig";
            }
            if (name.endsWith(".bb")) {
                return "bigBed";
            }
            if (name.endsWith(".bam")) {
                return "bam";
            }
        }
        return null;
    }

    private UCSCLinkFiles fetchUCSCLinkFiles(final List<File> files) throws Exception {
        if (this.urlLinkFileExtensions == null) {
            this.urlLinkFileExtensions = new HashSet<String>();
            for (final String ext : Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS) {
                this.urlLinkFileExtensions.add(ext);
            }
        }
        File useq = null;
        final boolean converting = false;
        ArrayList<File> filesAL = new ArrayList<File>();
        for (final File f : files) {
            final int index = f.getName().lastIndexOf(".");
            if (index > 0) {
                final String ext2 = f.getName().substring(index);
                if (ext2.equals(".useq")) {
                    useq = f;
                }
                else {
                    if (!this.urlLinkFileExtensions.contains(ext2)) {
                        continue;
                    }
                    filesAL.add(f);
                }
            }
        }
        if (filesAL.size() == 0 && useq != null && GenoPubServlet.autoConvertUSeqArchives) {
            final USeq2UCSCBig c = new USeq2UCSCBig(this.ucscWig2BigWigExe, this.ucscBed2BigBedExe, useq);
            filesAL = (ArrayList<File>)c.fetchConvertedFileNames();
            c.convert();
        }
        if (filesAL.size() != 0) {
            final File[] toReturn = new File[filesAL.size()];
            filesAL.toArray(toReturn);
            boolean stranded = false;
            if (toReturn.length == 2) {
                final String name = toReturn[0].getName();
                if (name.endsWith("_Plus.bw") || name.endsWith("_Minus.bw")) {
                    stranded = true;
                }
            }
            return new UCSCLinkFiles(toReturn, converting, stranded);
        }
        return null;
    }

    private boolean fetchUCSCExecutableFiles() {
        final File ucscExtDir = new File(this.genoPubWebAppDir, "UCSCExecutables");
        this.ucscWig2BigWigExe = new File(ucscExtDir, "wigToBigWig");
        this.ucscBed2BigBedExe = new File(ucscExtDir, "bedToBigBed");
        if (this.ucscWig2BigWigExe.exists()) {
            this.ucscWig2BigWigExe.setExecutable(true);
        }
        if (this.ucscBed2BigBedExe.exists()) {
            this.ucscBed2BigBedExe.setExecutable(true);
        }
        if (!this.ucscWig2BigWigExe.canExecute() || !this.ucscBed2BigBedExe.canExecute()) {
            System.err.println("\nError: can't execute or find " + this.ucscWig2BigWigExe + " or " + this.ucscBed2BigBedExe);
            return false;
        }
        return true;
    }

    private ArrayList<String> makeURLLinks(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        final ArrayList<String> urlsToLoad = new ArrayList<String>();
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)Util.getIntegerParameter(request, "idAnnotation")));
            final String annotationName = Util.stripBadURLChars(annotation.getName(), "_") + "_" + annotation.getFileName() + "_";
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)annotation.getIdGenomeVersion()));
            final String ucscGenomeVersionName = gv.getUcscName();
            final UCSCLinkFiles link = this.fetchURLLinkFiles(annotation.getFiles(this.genometry_genopub_dir));
            final File[] filesToLink = link.getFilesToLink();
            if (filesToLink == null) {
                throw new Exception("No files to link?!");
            }
            final String xml_base = this.getServletContext().getInitParameter("xml_base").replace("/genome", "/");
            final File urlLinkDir = this.checkUCSCLinkDirectory(xml_base);
            String randomWord = UUID.randomUUID().toString();
            if (randomWord.length() > 6) {
                randomWord = randomWord.substring(0, 6) + "_" + gv.getName();
            }
            if (ucscGenomeVersionName != null && ucscGenomeVersionName.length() != 0) {
                randomWord = randomWord + "_" + ucscGenomeVersionName;
            }
            final File dir = new File(urlLinkDir, randomWord);
            dir.mkdir();
            for (final File f : filesToLink) {
                final File annoFile = new File(dir, annotationName + Util.stripBadURLChars(f.getName(), "_"));
                final String annoString = annoFile.toString();
                Util.makeSoftLinkViaUNIXCommandLine(f, annoFile);
                if (!annoString.endsWith(".bai")) {
                    final int index = annoString.indexOf("URLLinks");
                    final String annoPartialPath = annoString.substring(index);
                    urlsToLoad.add(xml_base + annoPartialPath);
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
        return urlsToLoad;
    }

    private ArrayList<String> makeUCSCLink(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        Session sess = null;
        final ArrayList<String> urlsToLoad = new ArrayList<String>();
        try {
            sess = (Session)HibernateUtil.getSessionFactory().openSession();
            final String ucscUrl = this.fetchUCSCUrl(request.getParameter("userName"), sess);
            final Annotation annotation = Annotation.class.cast(sess.load((Class)Annotation.class, (Serializable)Util.getIntegerParameter(request, "idAnnotation")));
            final GenomeVersion gv = GenomeVersion.class.cast(sess.load((Class)GenomeVersion.class, (Serializable)annotation.getIdGenomeVersion()));
            final String ucscGenomeVersionName = gv.getUcscName();
            if (ucscGenomeVersionName == null || ucscGenomeVersionName.length() == 0) {
                throw new Exception("Missing UCSC Genome Version name, update, and resubmit.");
            }
            final UCSCLinkFiles link = this.fetchUCSCLinkFiles(annotation.getFiles(this.genometry_genopub_dir));
            final File[] filesToLink = link.getFilesToLink();
            if (filesToLink == null) {
                throw new Exception("No files to link?!");
            }
            final String xml_base = this.getServletContext().getInitParameter("xml_base").replace("/genome", "/");
            final File urlLinkDir = this.checkUCSCLinkDirectory(xml_base);
            final String type = "type=" + fetchUCSCDataType(filesToLink);
            String summary = annotation.getSummary();
            if (summary != null && summary.trim().length() != 0) {
                summary = GenoPubServlet.HTML_BRACKETS.matcher(summary).replaceAll("");
                summary = "description=\"" + summary + "\"";
            }
            else {
                summary = "";
            }
            final String randomWord = UUID.randomUUID().toString();
            final File dir = new File(urlLinkDir, randomWord);
            dir.mkdir();
            String customHttpLink = null;
            String toEncode = null;
            for (final File f : filesToLink) {
                final File annoFile = new File(dir, Util.stripBadURLChars(f.getName(), "_"));
                final String annoString = annoFile.toString();
                Util.makeSoftLinkViaUNIXCommandLine(f, annoFile);
                if (!annoString.endsWith(".bai")) {
                    String strand = "";
                    if (link.isStranded()) {
                        if (annoString.endsWith("_Plus.bw")) {
                            strand = " + ";
                        }
                        else {
                            if (!annoString.endsWith("_Minus.bw")) {
                                throw new Exception("\nCan't determine strand of bw file? " + annoString);
                            }
                            strand = " - ";
                        }
                    }
                    final String datasetName = "name=\"" + annotation.getName() + strand + " " + annotation.getFileName() + "\"";
                    final int index = annoString.indexOf("URLLinks");
                    final String annoPartialPath = annoString.substring(index);
                    final String bigDataUrl = "bigDataUrl=" + xml_base + annoPartialPath;
                    customHttpLink = ucscUrl + "/cgi-bin/hgTracks?db=" + ucscGenomeVersionName + "&hgct_customText=track+visibility=full+";
                    toEncode = type + " " + datasetName + " " + summary + " " + bigDataUrl;
                    urlsToLoad.add(customHttpLink + GeneralUtils.URLEncode(toEncode));
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (sess != null) {
                sess.close();
            }
        }
        return urlsToLoad;
    }

    private String fetchUCSCUrl(final String userName, final Session sess) throws Exception {
        final String query = "SELECT ucscUrl FROM User WHERE userName = '" + userName + "'";
        final List<?> rows = (List<?>)sess.createQuery(query).list();
        if (rows.size() != 1) {
            System.out.println("\nWarning: '" + userName + "' has no associated ucscURL.\n");
            return "http://genome.ucsc.edu";
        }
        return rows.get(0).toString();
    }

    static {
        HTML_BRACKETS = Pattern.compile("<[^>]+>");
        GenoPubServlet.autoConvertUSeqArchives = true;
        BULK_UPLOAD_LINE_SPLITTER = Pattern.compile("([^\\t]+)\\t([^\\t]+)\\t([^\\t]+)\\t(.+)", 32);
    }
}
