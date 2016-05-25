package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.utility.ChromatReadUtil;
import hci.gnomex.utility.ChromatTrimUtil;
import hci.gnomex.utility.ChromatogramParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class SaveChromatogramFromFile extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogramFromFile.class);

  private String  serverName;
  private String  fileName;
  private String  filePath;
  private String  baseDir;
  private Integer idPlateWell = 0;
  private Integer idCoreFacility;

  private String                launchAppURL;
  private String                appURL;
  
  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    // fileName parameter should not contain the path
    if (request.getParameter("fileName") != null && !request.getParameter("fileName").equals("")) {
      fileName = request.getParameter("fileName");
    } else {
      this.addInvalidField("fileName", "fileName is a required parameter");
    } 
    
    if (request.getParameter("filePath") != null  && !request.getParameter("filePath").equals("")) {
      filePath = request.getParameter("filePath");
    } else {
      this.addInvalidField("filePath", "filePath is a required parameter");
    }

    if (request.getParameter("idPlateWell") != null  && !request.getParameter("idPlateWell").equals("")) {
      idPlateWell = Integer.valueOf( request.getParameter("idPlateWell") );
    } 
    
    serverName = request.getServerName();

    idCoreFacility = DictionaryHelper.getIdCoreFacilityDNASeq();
    if (this.idCoreFacility == null) {
      this.addInvalidField("idCoreFacility", "Unable to find Core Facility for DNA Sequencing");
    }

    try {
      launchAppURL = this.getLaunchAppURL(request);      
      appURL = this.getAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveChromatogramFromFile", e);
    }
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // Get the abi file and create a chromatogram read utility object
      File abiFile = new File(filePath, fileName);
      ChromatReadUtil chromatReader = new ChromatReadUtil(abiFile);
      

      // Extract idPlateWell from comments if one wasn't provided
      if ( idPlateWell == 0 ) {
        String comments = chromatReader.getComments();
        int ind1 = comments.indexOf("<ID:");
        int ind2 = comments.indexOf(">");
        String idString = comments.substring(ind1+4, ind2);
        idPlateWell = idString!=null ? Integer.parseInt(idString):0;
      }
      
      // Find out if we already have a chromatogram for this plate well, named the same.
      Chromatogram chromatogram = (Chromatogram)sess.createQuery("SELECT ch from Chromatogram ch where ch.idPlateWell = " + idPlateWell + " and displayName = '" + fileName + "'").uniqueResult();
      if (chromatogram == null) {
        // This is the normal case.  We didn't find a chromatogram, so we create new DB chromatogram object
        chromatogram = new Chromatogram();
        sess.save(chromatogram);
      }
      
      
      
      // Get the plate well object from db, make sure it exists
      PlateWell well = (PlateWell) sess.get(PlateWell.class, idPlateWell);
      if (well==null) {
        throw new Exception("Chromatogram is not associated with a plate well.");
      }
      idPlateWell = well.getIdPlateWell();

      // Check that the chromat and plate well belong to a plate and a run
      if ( well.getPlate() == null ) {
        throw new Exception("Chromatogram does not belong to a plate.");
      } else if ( well.getPlate().getInstrumentRun() == null ) {
        throw new Exception("Chromatogram does not belong to an instrument run.");
      }
      
      // Figure out the experiment directory the file is to go to.
      // If this is a control, the chromatogram will be placed in an instrument
      // run directory.  Otherwise, the chromatogram will be placed in the
      // experiment directory.
      String destDir = "";
      
      if ( well.getIsControl().equals( "Y" ) || well.getIdRequest() == null) {
        // Control
        baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, this.idCoreFacility, PropertyDictionaryHelper.PROPERTY_INSTRUMENT_RUN_DIRECTORY);
        destDir = baseDir + "/" + well.getPlate().getInstrumentRun().getCreateYear() + "/" + well.getPlate().getInstrumentRun().getIdInstrumentRun();
      } else {
        // Non-controls / Chromat belongs to a request
        Request request = (Request)sess.load(Request.class, well.getIdRequest());
        if ( request == null ) {
          throw new Exception("The request associated with the plate well does not exist.");
        }
        baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, request.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
        destDir = baseDir + "/" + request.getCreateYear() + "/" + Request.getBaseRequestNumber(request.getNumber());
      }
      // Check for or create a new destination directory
      File dir = new File(destDir);
      if (!dir.exists()) {
        boolean success = dir.mkdirs();          
        if (!success) {
          throw new Exception("Unable to create destination directory: " + destDir + " for chromatogram." );      
        }
      }
      
      
      // Parse chromatogram data from the file:
      // Signal Strengths:
      if ( chromatReader.getSignalStrengths().length < 4 ) {
        throw new Exception("Signal strengths cannot be parsed from file " + fileName );  
      } 
      int aSignalStrength = chromatReader.getSignalStrengths()[1];
      int cSignalStrength = chromatReader.getSignalStrengths()[3];
      int gSignalStrength = chromatReader.getSignalStrengths()[0];
      int tSignalStrength = chromatReader.getSignalStrengths()[2];
      // Lane
      if ( chromatReader.getLane().equals( "" ) ) {
        throw new Exception("Lane cannot be parsed from file " + fileName );  
      } 
      int lane = Integer.parseInt(chromatReader.getLane());
      // Quality scores
      if ( chromatReader.getQualVals().length < 1 ) {
        throw new Exception("Quality values cannot be parsed from file " + fileName );  
      } 
      int q20 = chromatReader.getQ(20);
      int q40 = chromatReader.getQ(40);
      // Trim length
      ChromatTrimUtil trimUtil = new ChromatTrimUtil(abiFile);
      int trimLength = trimUtil.getTrimInterval()[1]-trimUtil.getTrimInterval()[0] + 1;
      
      
      // Set chromatogram object attributes
      chromatogram.setIdPlateWell(idPlateWell);
      chromatogram.setPlateWell(well);
      chromatogram.setIdRequest(well.getIdRequest());
      chromatogram.setQualifiedFilePath(destDir);
      chromatogram.setFileName(abiFile.getName());
      chromatogram.setReadLength(chromatReader.getChrom().getSequenceLength());
      chromatogram.setTrimmedLength(trimLength);
      chromatogram.setQ20(q20);
      chromatogram.setQ40(q40);
      chromatogram.setaSignalStrength(aSignalStrength);
      chromatogram.setcSignalStrength(cSignalStrength);
      chromatogram.setgSignalStrength(gSignalStrength);
      chromatogram.settSignalStrength(tSignalStrength);
      chromatogram.setLane(lane);
      
      sess.flush();
      
      // Check for complete requests.
      InstrumentRun ir = well.getPlate().getInstrumentRun();
      ChromatogramParser.changeRequestsToComplete(sess, ir, this.getSecAdvisor(), launchAppURL, appURL, serverName);
      
      if (isValid())  {
        // Success!
        this.xmlResult = "<SUCCESS idChromatogram=\"" + chromatogram.getIdChromatogram() + "\"" +
        " idRequest=\"" + chromatogram.getIdRequest() + "\"" +
        " destDir=\"" + destDir + "\"" +
        " />";
        setResponsePage(this.SUCCESS_JSP);
      } else {
        // Not successful :(
        setResponsePage(this.ERROR_JSP);
      }

    }catch (Exception e){
      log.error("An exception has occurred in SaveChromatogramFromFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
    }

    return this;
  }
  
 
}