package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.EmailHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveChromatogram extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogram.class);
  
  private int                   idChromatogram;
  private boolean               isNew = true;
  
  private String                serverName = null;
  private String                launchAppURL;
  private String                appURL;
  
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
    
    serverName = request.getServerName();
    
    try {
      launchAppURL = this.getLaunchAppURL(request);      
      appURL = this.getAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveChromatogram", e);
    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      Chromatogram ch;

      if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

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
          ch.setIdReleaser(this.getSecAdvisor().getIdAppUser());
          if (ir != null) {
            // The instrument run status should change to COMPLETE if all chromatograms
            // for instrument run have been released.
            if (InstrumentRun.areAllChromatogramsReleased(sess, ir)) {
              ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
            }
            // Loop through each experiment for an instrument run.  If all of the chromatograms
            // on the experiment have been released and none of the source wells are flagged
            // for redo, mark the experiment run status as COMPLETE.
            changeRequestsToComplete( sess, ir, this.getSecAdvisor(), launchAppURL, appURL, serverName );
          }
        }
        if (releaseDateStr != null) {
          java.util.Date releaseDate = this.parseDate(releaseDateStr);
          ch.setReleaseDate(releaseDate);
          if ( ir!=null ) {
            ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
            changeRequestsToComplete( sess, ir, this.getSecAdvisor(), launchAppURL, appURL, serverName );
          }
        }

        if ( idPlateWell != 0 )           {ch.setIdPlateWell( idPlateWell );}
        if ( idRequest != 0 )             {ch.setIdRequest( idRequest );}
        if ( qualifiedFilePath != null )  {ch.setQualifiedFilePath( qualifiedFilePath );}
        if ( displayName != null )        {ch.setDisplayName( displayName );}
        if ( readLength != 0 )            {ch.setReadLength( readLength );}
        if ( trimmedLength != 0 )         {ch.setTrimmedLength( trimmedLength );}
        if ( q20 != 0 )                   {ch.setQ20( q20 );}
        if ( q40 != 0 )                   {ch.setQ40( q40 );}
        if ( aSignalStrength != 0 )       {ch.setaSignalStrength( aSignalStrength );}
        if ( cSignalStrength != 0 )       {ch.setcSignalStrength( cSignalStrength );}
        if ( gSignalStrength != 0 )       {ch.setgSignalStrength( gSignalStrength );}
        if ( tSignalStrength != 0 )       {ch.settSignalStrength( tSignalStrength );}

        sess.flush();

        this.xmlResult = "<SUCCESS idChromatogram=\"" + ch.getIdChromatogram() + "\"/>";

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save chromatogram.");
        setResponsePage(this.ERROR_JSP);
      }
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
  
  
  public static void changeRequestsToComplete(Session sess, InstrumentRun ir, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) {
 
    // Get any requests on that run
    Map requests = new HashMap();
    List wells = sess.createQuery( "SELECT pw from PlateWell as pw " +
        " join pw.plate as plate where plate.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
    for(Iterator i1 = wells.iterator(); i1.hasNext();) {
      PlateWell well = (PlateWell)i1.next();
      // If the plate well points to a request (not the control plate well),
      // has the request.  We will iterate through this hash to see
      // which requests should be completed.
      if (well.getIdRequest() != null) {
        if ( !requests.containsKey( well.getIdRequest() ) ) {
          Request req = (Request) sess.get(Request.class, well.getIdRequest());
          requests.put( req.getIdRequest(), req );
        }
        
      }
    }
    
    // Change request Status to completed if all plate wells have released
    // chromatograms and there are no pending redos.
    for ( Iterator i = requests.keySet().iterator(); i.hasNext();) {
      int idReq = (Integer) i.next();
      Request req = (Request) sess.get(Request.class, idReq );
      if ( req.getCompletedDate() == null ) {
        
        // Don't complete if there is a redo well
        boolean hasRedo = false;
        for (Sample s : (Set<Sample>)req.getSamples()) {
          for (PlateWell well : (Set<PlateWell>)s.getWells()) {
            // Only check source wells for redo.  The reaction well will be set to redo and not toggle back.
            if (well.getPlate() == null || well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
              if (well.getRedoFlag() != null && well.getRedoFlag().equals("Y")) {
                hasRedo = true;
                break;
              }
            }
          }
        }
        
        if (hasRedo) {
          continue;
        }
        
        int releaseCount = 0;
        for (Chromatogram ch : (Set<Chromatogram>)req.getChromatograms()) {
          if (ch.getReleaseDate() != null) {
            releaseCount++;
          }
        }
        // If all of the chromatograms for the experiments
        // have been released, we can complete the experiment;
        // otherwise, we have to assume the experiment is not
        // yet complete.
        if (releaseCount < req.getChromatograms().size()) {
          continue;
        }
        
        req.setCodeRequestStatus( RequestStatus.COMPLETED );

        // We need to email the submitter that the experiment results
        // are ready to download
        try {
          EmailHelper.sendConfirmationEmail(sess, req, secAdvisor, launchAppURL, appURL, serverName);          
        } catch (Exception e) {
          log.warn("Cannot send confirmation email for request " + req.getNumber());
        }
      }
      req.setCompletedDate( new java.sql.Date( System.currentTimeMillis() ) );
    }
    sess.flush();
  }
}