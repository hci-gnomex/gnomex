package hci.gnomex.controller;

import java.net.URLEncoder;

import hci.framework.control.Command;
import hci.gnomex.model.*;
import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;


public class MakeDataTrackLinks extends GNomExCommand implements Serializable {

    private static Logger LOG = Logger.getLogger(MakeDataTrackLinks.class);

    private Integer idDataTrack;
    private String baseURL;
    private String baseDir;
    private String analysisBaseDir;
    private String serverName;
    private String dataTrackFileServerURL;
    private String dataTrackFileServerWebContext;
    private String bamiobioviewerURL;
    private String vcfiobioviewerURL;
    private Integer idAnalysisFile;
    private String requestType;
    private String pathName;
    private Integer idAnalysis;


    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {
        idDataTrack = -1;
        if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
            idDataTrack = new Integer(request.getParameter("idDataTrack"));
        }

        // if idAnalysisFile is a parameter we will need to figure out idDataTrack
        idAnalysisFile = null;
        if (request.getParameter("idAnalysisFile") != null && !request.getParameter("idAnalysisFile").equals("")) {
            idAnalysisFile = new Integer(request.getParameter("idAnalysisFile"));
        }

        // idAnalysis (needed for IOBIO requests)
        idAnalysis = null;
        if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
            idAnalysis = new Integer(request.getParameter("idAnalysis"));
        }


        // do we need to do special things for IOBIO?
        requestType = "NORMAL";
        if (request.getParameter("requestType") != null && !request.getParameter("requestType").equals("")) {
            requestType = request.getParameter("requestType");
        }

        // if pathName is a parameter we don't require a datatrack
        pathName = null;
        if (request.getParameter("pathName") != null && !request.getParameter("pathName").equals("")) {
            pathName = request.getParameter("pathName");
        }

        serverName = request.getServerName();
    }

    public Command execute() throws RollBackCommandException {

        try {


            Session sess = HibernateSession.currentSession(this.getSecAdvisor().getUsername());
            baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
            analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
            String use_altstr = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USE_ALT_REPOSITORY);
            if (use_altstr != null && use_altstr.equalsIgnoreCase("yes")) {
                analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                        PropertyDictionaryHelper.ANALYSIS_DIRECTORY_ALT,this.getUsername());
            }
            dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);
            bamiobioviewerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.BAM_IOBIO_VIEWER_URL);
            vcfiobioviewerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.VCF_IOBIO_VIEWER_URL);

            dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

            // do we need to figure out idDataTrack?
            if (idAnalysisFile != null) {
                idDataTrack = getidDataTrack(idAnalysisFile, sess);
            }

            String portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(PropertyDictionary.HTTP_PORT, serverName);
            if (portNumber == null) {
                portNumber = "";
            } else {
                portNumber = ":" + portNumber;
            }

            // We have to serve files from Tomcat, so use das2 base url
            baseURL = dataTrackFileServerURL;

            if (pathName != null || this.getSecAdvisor().canRead(DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack)))) {

                //make links fetching url(s)
                ArrayList<String> urlsToLink = makeURLLinks(sess);
                StringBuilder sb = new StringBuilder(urlsToLink.get(0));
                for (int i = 1; i < urlsToLink.size(); i++) {
                    sb.append("\n\n");
                    sb.append(urlsToLink.get(i));
                }


                //post results with link urls
                String theURL = sb.toString().replace("\\", Constants.FILE_SEPARATOR);

                String gBN = null;
                DataTrack dataTrack = null;

                Analysis a = null;
                if (idAnalysis != null  && idAnalysis > 0) {
                    a = (Analysis) sess.get(Analysis.class, idAnalysis);
                    if (a != null) {
                        Set<GenomeBuild> gbs = a.getGenomeBuilds();
                        if (gbs != null) {
                            GenomeBuild gb = gbs.iterator().next();
                            if (gb != null) {
                                String genomeBuildName = gb.getGenomeBuildName(); //Just pull the first one, should only be one.

                                gBN = Util.getGRCName(genomeBuildName);
                            }
                        }
                    }
                } else if (idDataTrack != null && idDataTrack > 0) {
                    dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));
                    Integer idGenomeBuild = null;
                    if (dataTrack != null) {
                        idGenomeBuild = dataTrack.getIdGenomeBuild();
                        if (idGenomeBuild != null && idGenomeBuild > 0) {
                            GenomeBuild gb = (GenomeBuild) sess.get(GenomeBuild.class, idGenomeBuild);
                            if (gb != null) {
                                String genomeBuildName = gb.getGenomeBuildName(); //Just pull the first one, should only be one.

                                gBN = Util.getGRCName(genomeBuildName);
                            }
                        }
                    }
                }

                if (gBN == null) {
                    gBN = "";
                }

                // is this an IOBIO request?
                if (requestType.equals("IOBIO")) {
                    // setup the url based on file type
                    if (theURL.toLowerCase().contains(".vcf.gz")) {
                        theURL = vcfiobioviewerURL + URLEncoder.encode(theURL, "UTF-8") + "&build=" + gBN;
                    } else {
                        theURL = bamiobioviewerURL + URLEncoder.encode(theURL, "UTF-8") + "&build=" + gBN;          // may also be .cram
                    }
                }

                System.out.println("\n[MakeDataTrackLinks] requestType: " + requestType + " urlsToLink: " + theURL + "\n");
                this.xmlResult = "<SUCCESS urlsToLink=\"" + theURL + "\"" + "/>";
                setResponsePage(this.SUCCESS_JSP);

            } else {
                this.addInvalidField("insufficient permission", "Insufficient permission to access data track");
            }

        } catch (Exception e) {
            this.errorDetails = Util.GNLOG(LOG, "An exception has occurred in MakeDataTrackUCSCLinks ", e);
            throw new RollBackCommandException(e.getMessage());
        }

        return this;
    }

    private ArrayList<String> makeURLLinks(Session sess) throws Exception {

        ArrayList<String> urlsToLoad = new ArrayList<String>();

        // if data track, process it
        File[] filesToLink = null;
        if (pathName == null) {
            //load dataTrack
            DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

            //check genome has UCSC name
            GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
            String ucscGenomeBuildName = gv.getUcscName();

            //pull all files and if needed auto convert xxx.useq to xxx.bb/.bw
            UCSCLinkFiles link = DataTrackUtil.fetchURLLinkFiles(dataTrack.getFiles(baseDir, analysisBaseDir), GNomExFrontController.getWebContextPath());
            filesToLink = link.getFilesToLink();
            if (filesToLink == null) throw new Exception("No files to link?!");

            // When new .bw/.bb files are created, add analysis files and then link via data
            // track file to the data track.
            MakeDataTrackUCSCLinks.registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);
        } else {
            // the file we want to link to
            filesToLink = new File[2];
            filesToLink[0] = new File(pathName);

            // add the correct index file
            if (pathName.endsWith(".vcf.gz")) {
                filesToLink[1] = new File(pathName + ".tbi");
            } else if (pathName.endsWith(".cram")) {
                filesToLink[1] = new File(pathName + ".crai");
            } else {
                // figure out whether the .bam.bai or the .bai file exists
                File bambai = new File(pathName + ".bai");
                if (bambai.exists()) {
                    filesToLink[1] = new File(pathName + ".bai");
                } else {
                    // we will assume the index file ends in .bai (without the .bam)
                    // we don't check or complain if it doesn't because the user can't do anything anyway
                    filesToLink[1] = new File(pathName.substring(0, pathName.length() - 4) + ".bai");
                }
            }
        }

        //look and or make directory to hold softlinks to data
        File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, dataTrackFileServerWebContext);

        String linkPath = this.checkForUserFolderExistence(urlLinkDir, username);

        if (linkPath == null) {
            linkPath = UUID.randomUUID().toString() + username;
        }

        //Create the users' data directory
        File dir = new File(urlLinkDir.getAbsoluteFile(), linkPath);
        if (!dir.exists())
            dir.mkdir();

        //for each file, there might be two for xxx.bam and xxx.bai files, two for xxx.cram and xxx.crai files, two for vcf, possibly two for converted useq files, plus/minus strands.

        for (File f : filesToLink) {
            File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));

            String dataTrackString = annoFile.toString().replace("\\", Constants.FILE_SEPARATOR);

            //make soft link
                DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);
            //is it a bam index xxx.bai? If so then skip after making soft link.
            if (dataTrackString.endsWith(".bam.bai") || dataTrackString.endsWith(".vcf.gz.tbi") || dataTrackString.endsWith(".cram.crai")) continue;

            // if it's just a .bai, make a .bam.bai link so IOBIO will work
            if (!dataTrackString.endsWith(".bam.bai") && dataTrackString.endsWith(".bai")) {
                // fix the name
                dataTrackString = dataTrackString.substring(0, dataTrackString.length() - 4) + ".bam.bai";

                // make the soft link
                DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, dataTrackString);

                continue;
            }

            //make URL to link
            int index = dataTrackString.indexOf(Constants.URL_LINK_DIR_NAME);
            String dataTrackPartialPath = dataTrackString.substring(index);

            urlsToLoad.add(baseURL + dataTrackPartialPath);
        }

        return urlsToLoad;

    }

    private String checkForUserFolderExistence(File igvLinkDir, String username) throws Exception {
        File[] directoryList = igvLinkDir.listFiles();

        String desiredDirectory = null;

        for (File directory : directoryList) {
            if (directory.getName().length() > 36) {
                String parsedUsername = directory.getName().substring(36);
                if (parsedUsername.equals(username)) {
                    desiredDirectory = directory.getName();
                }
            }
        }

        return desiredDirectory;
    }


    public static int getidDataTrack(int idAnalysisFile, Session sess) {

        int idDataTrack = -1;

        StringBuffer buf = new StringBuffer("SELECT idDataTrack from DataTrackFile where idAnalysisFile = " + idAnalysisFile);
        List results = sess.createQuery(buf.toString()).list();

        if (results.size() > 0) {
            idDataTrack = (Integer) results.get(0);
        }

        return idDataTrack;
    }

}
