package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.utility.ChromatReadUtil;
import hci.gnomex.utility.ChromatTrimUtil;
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
  private Integer idCoreFacility;


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

    serverName = request.getServerName();

    idCoreFacility = DictionaryHelper.getIdCoreFacilityDNASeq();
    if (this.idCoreFacility == null) {
      this.addInvalidField("idCoreFacility", "Unable to find Core Facility for DNA Sequencing");
    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // Get the abi file and create a chromatogram read utility object
      File abiFile = new File(filePath, fileName);
      ChromatReadUtil chromatReader = new ChromatReadUtil(abiFile);
      
      // Create new DB chromatogram object
      Chromatogram chromatogram = new Chromatogram();
      sess.save(chromatogram);
      
      // Extract idPlateWell from comments
      String comments = chromatReader.getComments();
      int ind1 = comments.indexOf("<ID:");
      int ind2 = comments.indexOf(">");
      String idString = comments.substring(ind1+4, ind2);
      int idPlateWell = !idString.equals(null) ? Integer.parseInt(idString):0;
      
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
        baseDir = PropertyDictionaryHelper.getInstance(sess).getInstrumentRunDirectory(serverName, this.idCoreFacility);
        destDir = baseDir + "/" + well.getPlate().getInstrumentRun().getCreateYear() + "/" + well.getPlate().getInstrumentRun().getIdInstrumentRun();
      } else {
        // Non-controls / Chromat belongs to a request
        Request request = (Request)sess.load(Request.class, well.getIdRequest());
        if ( request == null ) {
          throw new Exception("The request associated with the plate well does not exist.");
        }
        baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());
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
      int aSignalStrength = chromatReader.getSignalStrengths()[1];
      int cSignalStrength = chromatReader.getSignalStrengths()[3];
      int gSignalStrength = chromatReader.getSignalStrengths()[0];
      int tSignalStrength = chromatReader.getSignalStrengths()[2];
      // Lane
      int lane = Integer.parseInt(chromatReader.getLane());
      // Quality scores
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
      chromatogram.setDisplayName(abiFile.getName());
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