package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveChromatogram extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogram.class);
  
  private int                   idChromatogram;
  private boolean               isNew = true;
  
  private int                   idPlateWell = 0;
  private int                   idRequest = 0;
  private String                released = "N";
  private String                releaseDateStr = null;
  private String                fileName = null;
  private String                qualifiedFilePath = null;
  private String                displayName = null;
  private int                   readLength = 0;
  private int                   trimmedLength = 0;
  private int                   q20 = 0;
  private int                   q40 = 0;
  private int                   aSignalStrength = 0;
  private int                   cSignalStrength = 0;
  private int                   gSignalStrength = 0;
  private int                   tSignalStrength = 0;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idChromatogram") != null && !request.getParameter("idChromatogram").equals("")) {
      idChromatogram = Integer.parseInt(request.getParameter("idChromatogram"));
      isNew = false;
    }
    if (request.getParameter("releaseDate") != null && !request.getParameter("releaseDate").equals("")) {
      releaseDateStr = request.getParameter("releaseDate");
    }
    if (request.getParameter("released") != null && request.getParameter("released").equals("Y")) {
      released = "Y";
    }
    
    if (request.getParameter("idPlateWell") != null && !request.getParameter("idPlateWell").equals("")) {
      idPlateWell = Integer.parseInt(request.getParameter("idPlateWell"));
    }
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = Integer.parseInt(request.getParameter("idRequest"));
    }
    
    if (request.getParameter("fileName") != null && !request.getParameter("fileName").equals("")) {
      fileName = request.getParameter("fileName");
    } 
    if (request.getParameter("qualifiedFilePath") != null && !request.getParameter("qualifiedFilePath").equals("")) {
      qualifiedFilePath = request.getParameter("qualifiedFilePath");
    } 
    if (request.getParameter("displayName") != null && !request.getParameter("displayName").equals("")) {
      displayName = request.getParameter("displayName");
    }
    
    if (request.getParameter("readLength") != null && !request.getParameter("readLength").equals("")) {
      readLength = Integer.parseInt(request.getParameter("readLength"));
    }
    if (request.getParameter("trimmedLength") != null && !request.getParameter("trimmedLength").equals("")) {
      trimmedLength = Integer.parseInt(request.getParameter("trimmedLength"));
    }
    if (request.getParameter("q20") != null && !request.getParameter("q20").equals("")) {
      q20 = Integer.parseInt(request.getParameter("q20"));
    }
    if (request.getParameter("q40") != null && !request.getParameter("q40").equals("")) {
      q40 = Integer.parseInt(request.getParameter("q40"));
    }
    if (request.getParameter("aSignalStrength") != null && !request.getParameter("aSignalStrength").equals("")) {
      aSignalStrength = Integer.parseInt(request.getParameter("aSignalStrength"));
    }
    if (request.getParameter("cSignalStrength") != null && !request.getParameter("cSignalStrength").equals("")) {
      cSignalStrength = Integer.parseInt(request.getParameter("cSignalStrength"));
    }
    if (request.getParameter("tSignalStrength") != null && !request.getParameter("aSignalStrength").equals("")) {
      tSignalStrength = Integer.parseInt(request.getParameter("tSignalStrength"));
    }
    if (request.getParameter("gSignalStrength") != null && !request.getParameter("gSignalStrength").equals("")) {
      gSignalStrength = Integer.parseInt(request.getParameter("gSignalStrength"));
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      Chromatogram ch;
      
      if(isNew) {
        if ( fileName!=null && !fileName.equals( "" )) {
          ch = new Chromatogram();
          sess.save( ch );
          // SAVE FROM FILE, GET BACK ID, CONTINUE
          idChromatogram = ch.getIdChromatogram(); 
        }
      } 
      
      ch = (Chromatogram) sess.get(Chromatogram.class, idChromatogram);
      
      PlateWell pw = null;
      Plate p = null;
      InstrumentRun ir=null;
      
      if ( ch.getIdPlateWell() != null ) {
        pw = (PlateWell) sess.get(PlateWell.class, ch.getIdPlateWell());
      }
      if ( pw != null && pw.getIdPlate() != null ) {
        p = (Plate) sess.get(Plate.class, pw.getIdPlate());
      }
      if ( p != null && p.getIdInstrumentRun() != null ) {
        ir = (InstrumentRun) sess.get(InstrumentRun.class, p.getIdInstrumentRun());
      }
      
      // Set releaseDate if released
      if ( released.equals( "Y" )) {
        ch.setReleaseDate(new java.util.Date(System.currentTimeMillis()));
        if ( ir!=null ) {
          ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
        }
      }
      if (releaseDateStr != null) {
        java.util.Date releaseDate = this.parseDate(releaseDateStr);
        ch.setReleaseDate(releaseDate);
        if ( ir!=null ) {
          ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
        }
      }
      
      if ( idPlateWell != 0 ) {ch.setIdPlateWell( idPlateWell );}
      if ( idRequest != 0 ) {ch.setIdRequest( idRequest );}
      if ( fileName != null ) {ch.setFileName( fileName );}
      if ( qualifiedFilePath != null )  {ch.setQualifiedFilePath( qualifiedFilePath );}
      if ( displayName != null ) {ch.setDisplayName( displayName );}
      if ( readLength != 0 ) {ch.setReadLength( readLength );}
      if ( trimmedLength != 0 ) {ch.setTrimmedLength( trimmedLength );}
      if ( q20 != 0 )  {ch.setQ20( q20 );}
      if ( q40 != 0 ) {ch.setQ40( q40 );}
      if ( aSignalStrength != 0 ) {ch.setaSignalStrength( aSignalStrength );}
      if ( cSignalStrength != 0 )  {ch.setcSignalStrength( cSignalStrength );}
      if ( gSignalStrength != 0 ) {ch.setgSignalStrength( gSignalStrength );}
      if ( tSignalStrength != 0 ) {ch.settSignalStrength( tSignalStrength );}
        
      sess.flush();
        
      this.xmlResult = "<SUCCESS idChromatogram=\"" + ch.getIdChromatogram() + "\"/>";
      
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveChromatogram ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  
}