package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PlateWell;
import hci.gnomex.utility.ChromatReadUtil;
import hci.gnomex.utility.ChromatTrimUtil;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class SaveChromatogramFromFile extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogramFromFile.class);

  private String  fileName;
  private String  baseDir;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    // fileName parameter should not contain the path... 
    // Will look in some default path for the file with this name.
    if (request.getParameter("fileName") != null) {
      fileName = request.getParameter("fileName");
    } 
    
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // This should be in a dictionary?  
      baseDir = "C:/temp/";

      File abiFile = new File(baseDir, fileName);
      ChromatReadUtil chromatReader = new ChromatReadUtil(abiFile);
      
      // CreateDB  chromatogram object
      hci.gnomex.model.Chromatogram chromatogram = null;
      
      StringBuffer buf = new StringBuffer("SELECT c from Chromatogram as c where c.displayName = '" + fileName + "'");
      
      List chromats = sess.createQuery(buf.toString()).list();
      if (chromats.size() > 0) {
        chromatogram = (hci.gnomex.model.Chromatogram)chromats.get(0);
      } 
      if (chromatogram == null) {
        chromatogram =  new hci.gnomex.model.Chromatogram();
        sess.save(chromatogram);
      }
      
      // Biojava CHROMATOGRAM
//      Chromatogram chrom = ChromatogramFactory.create(abiFile);
      
      int idPlateWell = 0;
      // Get PlateWell id from the db or file comments:
      if ( chromatogram.getIdPlateWell() != null ) {
        idPlateWell = chromatogram.getIdPlateWell();
      } else {
        String comments = chromatReader.getComments();
        int ind1 = comments.indexOf("<");
        int ind2 = comments.indexOf(">");
        String idString = comments.substring(ind1+4, ind2);
        idPlateWell = !idString.equals(null) ? Integer.parseInt(idString):0;
      }
      
      
      PlateWell well;
      if (idPlateWell==0) {
        well = new PlateWell();
        sess.save(well);
      } else {
        well = (PlateWell) sess.get(PlateWell.class,
            idPlateWell);
        if (well==null) {
          well = new PlateWell();
          sess.save(well);
        }
      }
      idPlateWell = well.getIdPlateWell();
      
      // Signal Strengths:
      int aSignalStrength = chromatReader.getSignalStrengths()[1];
      int cSignalStrength = chromatReader.getSignalStrengths()[3];
      int gSignalStrength = chromatReader.getSignalStrengths()[0];
      int tSignalStrength = chromatReader.getSignalStrengths()[2];
      
      // Q20, Q40
      int q20 = chromatReader.getQ(20);
      int q40 = chromatReader.getQ(40);
      
      
      ChromatTrimUtil trimUtil = new ChromatTrimUtil(abiFile);
      int trimLength = trimUtil.getTrimInterval()[1]-trimUtil.getTrimInterval()[0] + 1;
      
      
      // Set chromatogram variables
      chromatogram.setIdPlateWell(idPlateWell);
      chromatogram.setPlateWell(well);
      // idRequest could be parsed from ABI file itself too?
      chromatogram.setIdRequest(well.getIdRequest());
      chromatogram.setFileName(abiFile.getCanonicalPath());
      chromatogram.setDisplayName(abiFile.getName());
      chromatogram.setReadLength(chromatReader.getChrom().getSequenceLength());
      chromatogram.setTrimmedLength(trimLength);
      chromatogram.setQ20(q20);
      chromatogram.setQ40(q40);
      chromatogram.setaSignalStrength(aSignalStrength);
      chromatogram.setcSignalStrength(cSignalStrength);
      chromatogram.setgSignalStrength(gSignalStrength);
      chromatogram.settSignalStrength(tSignalStrength);
      
      sess.flush();
      
      
      if (isValid())  {
        this.xmlResult = "<SUCCESS idChromatogram=\"" + chromatogram.getIdChromatogram() + "\" />";
        setResponsePage(this.SUCCESS_JSP);
      }
      setResponsePage(this.SUCCESS_JSP);
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
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