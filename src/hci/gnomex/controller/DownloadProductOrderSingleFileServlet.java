package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Session;


public class DownloadProductOrderSingleFileServlet extends HttpServlet {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadProductOrderSingleFileServlet.class);

    private String                          baseDir = null;
    private Integer                         idProductOrder = null;
    private String                          fileName = null;
    private String                          dir = null;
    private String                          view = "N";
    private String                          emailAddress = "";

    private boolean                         needToPreprocess = false;
    private String						  productOrderDir = null;
    private StringBuilder                   htmlText = new StringBuilder(1024000);


    public void init() {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        baseDir = null;
        idProductOrder = null;
        fileName = null;
        dir = "";
        emailAddress = "";

        view = "N";
        needToPreprocess = false;
        htmlText = new StringBuilder(1024000);
        productOrderDir = null;

        // restrict commands to local host if request is not secure
        if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
            if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
                    || req.getRemoteAddr().equals("127.0.0.1")
                    || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
                log.debug("Requested from local host");
            }
            else {
                log.error("Accessing secure command over non-secure line from remote host is not allowed");

                response.setContentType("text/html");
                response.getOutputStream().println(
                        "<html><head><title>Error</title></head>");
                response.getOutputStream().println("<body><b>");
                response.getOutputStream().println(
                        "Secure connection is required. Prefix your request with 'https: "
                                + "<br>");
                response.getOutputStream().println("</body>");
                response.getOutputStream().println("</html>");
                return;
            }
        }

        // Get the idProductOrder parameter
        if (req.getParameter("idProductOrder") != null && !req.getParameter("idProductOrder").equals("")) {
            idProductOrder = new Integer(req.getParameter("idProductOrder"));
        }
        // Get the fileName parameter
        if (req.getParameter("fileName") != null && !req.getParameter("fileName").equals("")) {
            fileName = req.getParameter("fileName");
        }
        // Get the email address parameter
        if (req.getParameter("emailAddress") != null && !req.getParameter("emailAddress").equals("")) {
            emailAddress = req.getParameter("emailAddress");
        }

        // Get the dir parameter
        if (req.getParameter("dir") != null && !req.getParameter("dir").equals("")) {
            dir = req.getParameter("dir");
        }
        // Get the view flag
        if (req.getParameter("view") != null && !req.getParameter("view").equals("")) {
            view = req.getParameter("view");
        }
        if (idProductOrder == null || fileName == null) {
            log.error("idProductOrder and fileName required");

            response.setContentType("text/html");
            response.getOutputStream().println(
                    "<html><head><title>Error</title></head>");
            response.getOutputStream().println("<body><b>");
            response.getOutputStream().println(
                    "Missing parameters:  idProductOrder and fileName required"
                            + "<br>");
            response.getOutputStream().println("</body>");
            response.getOutputStream().println("</html>");
            return;

        }

        InputStream in = null;
        SecurityAdvisor secAdvisor = null;
        try {


            // Get security advisor
            secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

            if (secAdvisor != null) {
                String mimeType = req.getSession().getServletContext().getMimeType(fileName);
                if (view.equals("Y") && mimeType != null) {
                    response.setContentType(mimeType);
                    response.setHeader("Content-Disposition", "filename=" + "\"" + fileName + "\"");
                    response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                } else {
                    response.setContentType("application/x-download");
                    response.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fileName + "\"");
                    response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                }

                needToPreprocess = false;
                if (view.equals("Y")) {
                    needToPreprocess = true;
                    if (!(fileName.toLowerCase().endsWith("html") || fileName.toLowerCase().endsWith("htm"))) {
                        needToPreprocess = false;
                    }
                }



                Session sess = secAdvisor.getWritableHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");


                ProductOrder productOrder = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);

                baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(req.getServerName(), productOrder.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_PRODUCT_ORDER_DIRECTORY);


                // If we can't find the productOrder in the database, just bypass it.
                if (productOrder == null) {
                    throw new Exception("Cannot find productOrder " + idProductOrder);
                }

                // Check permissions - bypass this productOrder if the user
                // does not have  permission to read it.
                if (!secAdvisor.canRead(productOrder)) {
                    throw new Exception("Insufficient permissions to read request " + productOrder.getProductOrderNumber() + ".  Bypassing download.");
                }

                // Now get the files that exist on the file server for this productOrder
                Map productOrderMap = new TreeMap();
                Map directoryMap = new TreeMap();
                Map fileMap = new HashMap();
                List productOrderNumbers = new ArrayList<String>();
                GetProductOrderDownloadList.getFileNamesToDownload(baseDir, productOrder.getKey(), productOrderNumbers, productOrderMap, directoryMap, false);


                // Find the file matching the fileName passed in as a parameter
                FileDescriptor productOrderFd = null;
                List directoryKeys   = (List)productOrderMap.get(productOrder.getProductOrderNumber());
                for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
                    String directoryKey = (String)i1.next();
                    String dirTokens[] = directoryKey.split("-");

                    String theDirectory = "";
                    if (dirTokens.length > 1) {
                        theDirectory = dirTokens[1];
                    }

                    List   theFiles     = (List)directoryMap.get(directoryKey);
                    for(Iterator i2 = theFiles.iterator(); i2.hasNext();) {
                        FileDescriptor fd = (FileDescriptor) i2.next();
                        fd.setQualifiedFilePath(theDirectory);
                        FileDescriptor matchingFd = recurseGetMatchingFileDescriptor(fd, fileName, theDirectory);
                        if (matchingFd != null) {
                            productOrderFd = matchingFd;
                            break;
                        }
                    }
                    if (productOrderFd != null) {
                        break;
                    }

                }

                // If we found the productOrder, download it
                if (productOrderFd != null) {

                    // Insert a transfer log entry
                    TransferLog xferLog = new TransferLog();

                    xferLog.setFileName(productOrderFd.getDisplayName());

                    xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
                    xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
                    xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
                    xferLog.setPerformCompression("Y");
                    xferLog.setIdProductOrder(productOrder.getIdProductOrder());
                    xferLog.setIdLab(productOrder.getIdLab());
                    xferLog.setEmailAddress(emailAddress);
                    xferLog.setIpAddress(GNomExCommand.getRemoteIP(req));
                    xferLog.setIdAppUser(secAdvisor.getIdAppUser());

                    //String productOrderfdFileName = productOrderFd.getFileName().replaceAll("\\\\", "/");
                    productOrderDir = productOrderFd.getFileName().substring(0,productOrderFd.getFileName().lastIndexOf("/")+1);

                    in = new FileInputStream(productOrderFd.getFileName());
                    OutputStream out = response.getOutputStream();


                    byte b[] = new byte[102400];
                    int numRead = 0;
                    int size = 0;
                    while (numRead != -1) {
                        numRead = in.read(b);
                        if (numRead != -1) {
                            if (!needToPreprocess) {
                                out.write(b, 0, numRead);
                            }
                            else {
                                // we are going to preprocess this, save it for now
                                byte b1[] = new byte[numRead];
                                System.arraycopy(b, 0, b1, 0, numRead);
                                htmlText.append(new String (b1,"UTF-8"));
                            }
                            size += numRead;
                        }
                    }

                    // Save transfer log
                    xferLog.setFileSize(new BigDecimal(size));
                    xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
                    sess.save(xferLog);

                    in.close();

                    if (needToPreprocess) {
                        // remember htmlText is global
                        preProcessIMGTags (out);
                    }

                    out.flush();
                    out.close();

                    in = null;
                    out = null;

                }

                sess.flush();


            } else {
                response.setContentType("text/html");
                response.getOutputStream().println(
                        "<html><head><title>Error</title></head>");
                response.getOutputStream().println("<body><b>");
                response.getOutputStream().println(
                        "DownloadAnalyisSingleFileServlet: You must have a SecurityAdvisor in order to run this command."
                                + "<br>");
                response.getOutputStream().println("</body>");
                response.getOutputStream().println("</html>");
                System.out.println( "DownloadAnalyisSingleFileServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            HibernateSession.rollback();
            response.setContentType("text/html");
            response.getOutputStream().println(
                    "<html><head><title>Error</title></head>");
            response.getOutputStream().println("<body><b>");
            response.getOutputStream().println(
                    "DownloadAnalyisSingleFileServlet: An exception occurred " + e.toString()
                            + "<br>");
            response.getOutputStream().println("</body>");
            response.getOutputStream().println("</html>");
            System.out.println( "DownloadAnalyisSingleFileServlet: An exception occurred " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                secAdvisor.closeHibernateSession();
            } catch (Exception e) {

            }

            if (in != null) {
                in.close();
            }
        }

    }


    private FileDescriptor recurseGetMatchingFileDescriptor(FileDescriptor fd, String fileName, String theDirectory) {
        // Change all backslash to forward slash for comparison
        //String fdFileName = fd.getFileName().replaceAll("\\\\", "/");
//        if ( dir != null ) {
//            dir = dir.replace("\\", "/");
//        }
        // theDirectory = theDirectory.replace("\\", "/");

        if (fd.getFileName().endsWith(fileName) && dir.equals(theDirectory)) {
            return fd;
        } else if (fd.getChildren() != null && fd.getChildren().size() > 0) {
            for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
                FileDescriptor childFd = (FileDescriptor)i.next();

                childFd.setQualifiedFilePath(!fd.getQualifiedFilePath().equals("") ? fd.getQualifiedFilePath() + "/" + fd.getDisplayName() : fd.getDisplayName());

                FileDescriptor matchingFd = recurseGetMatchingFileDescriptor(childFd, fileName, childFd.getQualifiedFilePath());
                if (matchingFd != null) {
                    return matchingFd;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private void preProcessIMGTags (OutputStream out) {
        int 					ipos = -1; 			// start of <img tag
        int						epos = -1; 			// > end of tag
        int						nxtpos = 0;			// next position in htmlText to search
        int						lenhtmlText = -1;	// size of htmlText

        lenhtmlText = htmlText.length();

        while (nxtpos < lenhtmlText) {
            // find the start of the <img tag
            ipos = htmlText.indexOf("<img",nxtpos);

            if (ipos == -1) {
                // we are done, put the rest out
                epos = lenhtmlText - 1;
                outString (htmlText,nxtpos,epos,out);
                break;
            }

            epos = htmlText.indexOf(">",ipos+4);
            if (epos == -1) {
                // assume it was the last characer
                epos = lenhtmlText - 1;
            }

            // put out everything up to the image tag
            outString (htmlText,nxtpos,ipos,out);

            // get the line
            String imgline = htmlText.substring (ipos, epos+1);

            // process it
            if (!processIMG (imgline,out)) {
                // not the kind of img we are interested in, output the original text here
                outString (htmlText,ipos,epos+1,out);
            }

            nxtpos = epos + 1;

        } // end of while

    }

    private void outString (StringBuilder theText, int startpos, int endpos, OutputStream out) {

        String theBytes = theText.substring(startpos,endpos);
        byte[] asBytes = null;

        try {
            asBytes = theBytes.getBytes("UTF-8");
            out.write(asBytes);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean processIMG (String imgline, OutputStream out) {
        boolean processed = false;

        // if already an inline base64 image, just return
        int jpos = imgline.indexOf("src=\"data:image/");
        if (jpos != -1) {
            return processed;
        }

        // there are two types of syntax, image tags containing DownloadproductOrderSingleFileServlet
        // and those with src="local image name" without a &dir specified
        int syntaxType = 1;								// assume DownloadproductOrderSingleFileServlet
        int ipos = imgline.indexOf("DownloadproductOrderSingleFileServlet");
        if (ipos == -1) {
            syntaxType = 2;
        }

        String localdir = null;
        String fileName = null;
        String imageType = "png";
        int startA = 0;							// <
        int endA = 0;							// everything upto src=
        int startC = 0;							// everything after close " on src filename

        if (syntaxType == 1) {
            // get the image filename, here's an example of what the html looks like
            // <img src="https://b2b.hci.utah.edu/gnomex/DownloadSingleFileServlet.gx?requestNumber=103R&fileName=per_base_quality.png&view=Y&dir=Images">
            ipos = imgline.indexOf("fileName=");
            if (ipos == -1) {
                // not a format we can deal with
                return processed;
            }

            int epos = imgline.indexOf("&",ipos+9);
            if (epos == -1) {
                return processed;
            }

            fileName = imgline.substring(ipos+9,epos);

            // figure out what type of image it is
            imageType = "png";
            ipos = fileName.lastIndexOf('.');
            if (ipos != -1 && (ipos+1 < fileName.length()) ) {
                imageType = fileName.substring(ipos+1);
            }

            // get the directory
            ipos = imgline.indexOf("&dir=");
            if (ipos == -1) {
                return processed;
            }

            epos = imgline.indexOf('"',ipos+5);
            if (epos == -1) {
                return processed;
            }

            localdir = imgline.substring(ipos+5,epos) + "/";
        }
        else if (syntaxType == 2) {
            // <img src="UGP07_Trio_Rec_Pnt_Splice_Indel_10e4.png" alt="Run '.....' " style="width: 100%">
            ipos = imgline.indexOf("src=");
            if (ipos == -1) {
                // not a format we can deal with
                return processed;
            }

            endA = ipos;			// end of everything upto src=
            ipos = imgline.indexOf('"',ipos+4);
            if (ipos == -1) {
                // not a format we can deal with
                return processed;
            }

            int epos = imgline.indexOf('"',ipos+1);
            if (epos == -1) {
                return processed;
            }

            startC = epos+1;		// start up everything after the image name
            fileName = imgline.substring(ipos+1,epos);
            if (fileName.toLowerCase().startsWith("http")) {
                // we only deal with local filenames
                return processed;
            }

            // figure out what type of image it is
            imageType = "png";
            ipos = fileName.lastIndexOf('.');
            if (ipos != -1 && (ipos+1 < fileName.length()) ) {
                imageType = fileName.substring(ipos+1);
            }


            localdir = dir + "/";
            if (productOrderDir.endsWith(localdir))
            {
                localdir = "";
            }

        }

        // get the file
        String pathname = productOrderDir + localdir + fileName;

        File imageFd = new File(pathname);

        // read it in
        long filesize = imageFd.length();

        byte thefile[] = new byte[(int)filesize];

        FileInputStream inf;
        boolean readImageOK = true;
        try {
            inf = new FileInputStream(pathname);
            int numRead = 0;
            numRead = inf.read(thefile);
            inf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            readImageOK = false;
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            readImageOK = false;
            e.printStackTrace();
        }

        if (!readImageOK) {
            return processed;
        }

        // convert it to base64
        byte[] encodedBytes = Base64.encodeBase64(thefile);

        // now build the <img tag
        StringBuilder imgtag = new StringBuilder (1024000);
        //imgtag.append("<img src=\"data:image/png;base64,");
        if (syntaxType == 1) {
            imgtag.append("<img src=\"data:image/");
            imgtag.append(imageType);
            imgtag.append(";base64,");

            imgtag.append(new String(encodedBytes));
            imgtag.append("\" />");
        }
        else if (syntaxType == 2) {
            String partA = imgline.substring(startA,endA);
            imgtag.append(partA);

            imgtag.append("src=\"data:image/");
            imgtag.append(imageType);
            imgtag.append(";base64,");

            imgtag.append(new String(encodedBytes));
            imgtag.append("\"");

            String partC = imgline.substring(startC);
            imgtag.append(partC);

        }

        // write it out
        outString (imgtag,0,imgtag.length(),out);
        processed = true;

        return processed;
    }

}