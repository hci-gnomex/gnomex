package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class FastDataTransferUploadStart extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(FastDataTransferUploadStart.class);

  private String serverName;

  private Integer idAnalysis;
  private Integer idRequest;
  private Integer idDataTrack;
  private Integer idProductOrder;

  private String analysisNumber;
  private String requestNumber;

  private String targetDir;

  private String emailAddress;

  private String remoteIPAddress;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    serverName = request.getServerName();

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = Integer.valueOf(request.getParameter("idRequest"));
    }
    if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
      idAnalysis = Integer.valueOf(request.getParameter("idAnalysis"));
    }
    if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = Integer.valueOf(request.getParameter("idDataTrack"));
    }
    if (request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = Integer.valueOf(request.getParameter("idProductOrder"));
    }
    if (request.getParameter("requestNumber") != null && !request.getParameter("requestNumber").equals("")) {
      requestNumber = request.getParameter("requestNumber");
    }
    if (request.getParameter("analysisNumber") != null && !request.getParameter("analysisNumber").equals("")) {
      analysisNumber = request.getParameter("analysisNumber");
    }

    if (idAnalysis == null && idRequest == null && idDataTrack == null && analysisNumber == null && requestNumber == null && idProductOrder == null) {
      this.addInvalidField("missing id", "idRequest/requestNumber or idAnalysis/analysisNumber or idDataTrack or idProductOrder must be provided");
    }

    emailAddress = "";
    if (request.getParameter("emailAddress") != null && !request.getParameter("emailAddress").equals("")) {
      emailAddress = request.getParameter("emailAddress");
    }

    remoteIPAddress = GNomExCommand.getRemoteIP(request);

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      SecurityAdvisor secAdvisor = this.getSecAdvisor();
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
      String createYear = "";
      String targetNumber = "";

      String theIdRequest = "";
      String theIdLab = "";
      String theIdAnalysis = "";

      String fdtSupported = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_SUPPORTED);
      if (fdtSupported == null || !fdtSupported.equals("Y")) {
        this.addInvalidField("fdtNotSupport", "GNomEx is not configured to support FDT.  Please contact GNomEx support to set appropriate property");
      }

      if (idAnalysis != null || analysisNumber != null) {
        Analysis analysis = null;
        if (idAnalysis != null) {
          theIdAnalysis = "" + idAnalysis;
          analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
        } else if (analysisNumber != null) {
          analysis = (Analysis)sess.createQuery("from Analysis a where a.number ='" + analysisNumber + "'").uniqueResult();
          if (analysis == null) {
            throw new RuntimeException("Cannot find analysis " + analysisNumber);
          }

          theIdLab = "" + analysis.getIdLab();
        }
        if (!secAdvisor.canUploadData(analysis)) {
          this.addInvalidField("insufficient permissions", "insufficient permissions to upload analysis files");
        }
        if (this.isValid()) {
          createYear = yearFormatter.format(analysis.getCreateDate());
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
          String use_altstr = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USE_ALT_REPOSITORY);
          if (use_altstr != null && use_altstr.equalsIgnoreCase("yes")) {
            baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                    PropertyDictionaryHelper.ANALYSIS_DIRECTORY_ALT,this.getUsername());
          }
          targetDir = baseDir + createYear + Constants.FILE_SEPARATOR + analysis.getNumber() + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR;
          targetNumber = analysis.getNumber();


        }
      } else if (idRequest != null || requestNumber != null) {
        Request experiment = null;
        if (idRequest != null) {
          theIdRequest = "" + idRequest;
          experiment = (Request)sess.get(Request.class, idRequest);
          theIdLab = "" + experiment.getIdLab();

        } else if (requestNumber != null) {
          experiment = (Request)sess.createQuery("from Request r where r.number ='" + requestNumber + "'").uniqueResult();
          if (experiment == null) {
            throw new RuntimeException("Cannot find experiment " + requestNumber);
          }
          theIdRequest = "" + experiment.getIdRequest();
          theIdLab = "" + experiment.getIdLab();
        }
        if (experiment.getRequestCategory().getIsClinicalResearch() != null && experiment.getRequestCategory().getIsClinicalResearch().equals("Y")) {
          this.addInvalidField("Clinical Research", "Clinical research experiments cannot upload using FDT.");
        }
        if (!secAdvisor.canUploadData(experiment)) {
          this.addInvalidField("insufficient permissions", "insufficient permissions to upload experiment files");
        }
        if (this.isValid()) {
          createYear = yearFormatter.format(experiment.getCreateDate());
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, experiment.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
          targetDir = baseDir + createYear + Constants.FILE_SEPARATOR + Request.getBaseRequestNumber(experiment.getNumber()) + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR;
          targetNumber = Request.getBaseRequestNumber(experiment.getNumber());
        }
      } else if (idDataTrack != null) {
        DataTrack dataTrack = null;
        dataTrack = (DataTrack)sess.get(DataTrack.class, idDataTrack);
        if (!secAdvisor.canUploadData(dataTrack)) {
          this.addInvalidField("insufficient permissions", "insufficient permissions to upload data track files");
        }
        if (this.isValid()) {
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
          targetDir = baseDir + Constants.FILE_SEPARATOR + dataTrack.getFileName() + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR;
          targetNumber = dataTrack.getNumber();
        }
      } else if (idProductOrder != null) {
        ProductOrder po = null;
        po = (ProductOrder)sess.get(ProductOrder.class, idProductOrder);
        if (this.isValid()) {
          createYear = yearFormatter.format(po.getSubmitDate());
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, po.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_PRODUCT_ORDER_DIRECTORY);
          targetDir = baseDir + createYear + Constants.FILE_SEPARATOR + po.getProductOrderNumber() + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR;
          targetNumber = po.getProductOrderNumber();
        }
      }
      if (this.isValid()) {
        if (!new File(targetDir).exists()) {
          File dir = new File(targetDir);
          boolean success = dir.mkdirs();
          if (!success) {
            this.addInvalidField("mkdir", "Unable to make target dir");
          }
        }
      }

      if (this.isValid()) {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();

        // The softlinks_dir_gnomex is the place where the files will be uploaded to.  This may be a mount
        // point to a different server, so this is why we get the property known to where gnomex is running.
        // Directories must be created one level at a time so that permissions will be set properly in Linux
        String softlinks_dir_gnomex = PropertyDictionaryHelper.getInstance(sess).getFDTDirectoryForGNomEx(serverName) + uuidStr;
        makeDirectory(softlinks_dir_gnomex);
        changeOwnershipAndPermissions(sess, softlinks_dir_gnomex);

        // Add on either request or analysis number to softlinks_dir
        softlinks_dir_gnomex  += Constants.FILE_SEPARATOR + targetNumber;
        makeDirectory(softlinks_dir_gnomex);
        changeOwnershipAndPermissions(sess, softlinks_dir_gnomex);

        // The softlinks_dir is the place where the files will be uploaded to.  This is the directory referenced
        // by the FDT server which may be running on a different server than gnomex.
        String softlinks_dir = PropertyDictionaryHelper.getInstance(sess).GetFDTDirectory(serverName) + uuidStr;
        String softlinks_dir1  = softlinks_dir + File.separator + targetNumber;

        // Create "fdtUploadTransferLog" file used by FDT postProcessing routine
        String theFileTransferLogFile = "fdtUploadTransferLog_" + uuid.toString();
        addTask(softlinks_dir, softlinks_dir1, targetDir, secAdvisor, remoteIPAddress, emailAddress,  theIdRequest, theIdLab,  theIdAnalysis, theFileTransferLogFile);

        this.xmlResult = "<FDTUploadUuid uuid='" + uuidStr + Constants.FILE_SEPARATOR + targetNumber + "'/>";

      }



      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in FastDataTransferUploadStart", e);

      throw new RollBackCommandException(e.getMessage());
    }

    return this;

  }


  private boolean makeDirectory(String directoryName) {
    File dir = new File(directoryName);
    boolean isDirCreated = dir.mkdir();
    if (!isDirCreated) {
      this.addInvalidField("Error.", "Unable to create " + directoryName + " directory.");
    }
    return isDirCreated;
  }

  public static void changeOwnershipAndPermissions(Session sess, String dir) throws Exception {
    //Kludge for testing
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows")) {
      return;
    }

    // linux ownership code
    String fdtUser = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_USER);
    if (fdtUser == null || fdtUser.equals("")) {
      fdtUser = "fdt";
    }
    String fdtGroup = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_GROUP);
    if (fdtGroup == null || fdtGroup.equals("")) {
      fdtGroup = "fdtsecurity";
    }
    Process process = Runtime.getRuntime().exec( new String[] { "chown", "-R", fdtUser + ":" + fdtGroup, dir } );
    process.waitFor();
    process.destroy();

    // only fdt user and group have permissions on this directory
    process = Runtime.getRuntime().exec( new String[] { "chmod", "770", dir } );
    process.waitFor();
    process.destroy();
  }

  private static void addTask(String taskFileDir, String sourceDir, String targetDir, SecurityAdvisor secAdvisor, String remoteIPAddress, String emailAddress, String theIdRequest, String theIdLab, String theIdAnalysis, String theFDTUploadTransferFile ) {
    String taskFileName = taskFileDir + "/" + "fdtUploadInfoFile";
    String uploadtransferlog = taskFileDir + "/" + theFDTUploadTransferFile;
    File taskFile;
    int numTries = 10;
    while (true) {
      taskFile = new File(taskFileName);
      if (!taskFile.exists()) {
        boolean success;
        try {
          success = taskFile.createNewFile();
          if (!success) {
            System.out.println("[FastDataTransferUploadStart] Error: unable to create task file. " + taskFileName);
            return;
          }
          break;
        } catch (IOException e) {
          System.out.println("[FastDataTransferUploadStart] Error: unable to create task file. " + taskFileName);
          return;
        }
      }
      // If the file already exists then try again but don't try forever
      numTries--;
      if (numTries == 0) {
        System.out.println("[FastDataTransferUploadStart] Error: Unable to create task file: " + taskFileName);
        return;
      }
    }

    try {
      PrintWriter pw = new PrintWriter(new FileWriter(taskFile));
      SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//      pw.println("Started: " + f.format(new Date()));
//      pw.println("LastActivity: 0");
      pw.println("SourceDirectory: " + sourceDir);
      pw.println("TargetDirectory: " + targetDir);
      pw.println("EmailAddress: " + emailAddress);
      pw.println("Remote ipAddress: " + remoteIPAddress);
      pw.println("idAppUser: " + secAdvisor.getIdAppUser().toString());
      pw.println("idRequest: " + theIdRequest);
      pw.println("idLab: " + theIdLab);
      pw.println("idAnalysis: " + theIdAnalysis);
      pw.println("transferlog: " + uploadtransferlog);

      pw.flush();
      pw.close();
    } catch (IOException e) {
      System.out.println("[FastDataTransferUploadStart] IOException: file " + taskFileName + " " + e.getMessage());
      return;
    }


    try {
      PrintWriter pw = new PrintWriter(new FileWriter(theFDTUploadTransferFile));
      SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


      pw.flush();
      pw.close();
    } catch (IOException e) {
      System.out.println("[FastDataTransferUploadStart] IOException: file " + theFDTUploadTransferFile + " " + e.getMessage());
      return;
    }
  }
}

