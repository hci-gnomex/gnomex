
package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.ChromatParserRequestFilter;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.apache.log4j.Logger;
public class ChromatogramParser extends DetailObject implements Serializable
{
  private static Logger LOG = Logger.getLogger(ChromatogramParser.class);
  
  private Document doc;
  
  private SecurityAdvisor secAdvisor = null;
  private Integer idReleaser;
  
  private Map redoRequestsMap = new HashMap();
  private Map instrumentRunsMap = new HashMap();
  
  public ChromatogramParser(Document doc) {
    this.doc = doc;
  }
  
  public ChromatogramParser(){
  }


  public void parse(Session sess, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) throws Exception {
   
    Chromatogram ch = new Chromatogram();
    Element root = this.doc.getRootElement();

    this.secAdvisor = secAdvisor;
    this.idReleaser = this.secAdvisor.getIdAppUser();
    
    // Loop through each chromatogram
    for (Iterator i = root.getChildren("Chromatogram").iterator(); i.hasNext();) {
      Element node = (Element) i.next();

      String idChromatogram = node.getAttributeValue("idChromatogram");
      
      // Get the chromatogram from the DB or create a new one
      if (idChromatogram.equals(null) || idChromatogram.equals("0")) {
        ch = new Chromatogram();
        sess.save( ch );
        idChromatogram = ch.getIdChromatogram().toString();
      } else {
        ch = (Chromatogram) sess.get(Chromatogram.class,
            Integer.parseInt( idChromatogram ));
      }
      
      this.initializeChromat(sess, node, ch);
      
      sess.flush();
      
      // Get the instrument run for the chromat
      PlateWell pw = null;
      Plate p = null;
      InstrumentRun ir=null;
      if ( ch.getIdPlateWell() != null ) {
        pw = (PlateWell) sess.get(PlateWell.class, ch.getIdPlateWell());
        if ( pw != null && pw.getIdPlate() != null ) {
          p = (Plate) sess.get(Plate.class, pw.getIdPlate());
          if ( p != null && p.getIdInstrumentRun() != null ) {
            ir = (InstrumentRun) sess.get(InstrumentRun.class, p.getIdInstrumentRun());
            if ( ir!=null && !instrumentRunsMap.containsKey( ir.getIdInstrumentRun() ) ) {
              instrumentRunsMap.put( ir.getIdInstrumentRun(), ir );
            }
          }
        }
      }
      
      sess.flush();
    }
    
    // Update instrument run and request statuses
    for(Iterator i = instrumentRunsMap.keySet().iterator(); i.hasNext();) {
      int idInstrumentRun = (Integer)i.next();
      InstrumentRun ir = (InstrumentRun)instrumentRunsMap.get(idInstrumentRun);
      if (InstrumentRun.areAllChromatogramsReleased(sess, ir)) {
        ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
      }
      this.changeRequestsToComplete(sess, ir, secAdvisor, launchAppURL, appURL, serverName);
    }

    // Send redo emails for any requests with samples being requeued
    for(Iterator i = redoRequestsMap.keySet().iterator(); i.hasNext();) {
      int idRequest = (Integer)i.next();
      Request req = (Request)redoRequestsMap.get(idRequest);
      
        try {
          EmailHelper.sendRedoEmail(sess, req, secAdvisor, launchAppURL, appURL, serverName);          
        } catch (Exception e) {
          LOG.error("Error in ChromatogramParser", e);
          LOG.warn("Cannot send confirmation email for request " + req.getNumber());
        }
    }
  }

  protected void initializeChromat(Session sess, Element n,
      Chromatogram ch) throws Exception {
    
    int                   idPlateWell = 0;
    int                   idRequest = 0;
    String                released = "N";
    String                releaseDateStr = null;
    String                requeue = "N";
    String                qualifiedFilePath = null;
    String                displayName = null;
    int                   readLength = 0;
    int                   trimmedLength = 0;
    int                   q20 = 0;
    int                   q40 = 0;
    int                   aSignalStrength = 0;
    int                   cSignalStrength = 0;
    int                   gSignalStrength = 0;
    int                   tSignalStrength = 0;
    int                   lane = 0;
    
    
    if (n.getAttributeValue("releaseDate") != null && !n.getAttributeValue("releaseDate").equals("")) {
      releaseDateStr = n.getAttributeValue("releaseDate");
    }
    if (n.getAttributeValue("released") != null && n.getAttributeValue("released").equals("Y")) {
      released = "Y";
    }
    if (n.getAttributeValue("requeue") != null && n.getAttributeValue("requeue").equals("Y")) {
      requeue = "Y";
    }
    if (n.getAttributeValue("idPlateWell") != null && !n.getAttributeValue("idPlateWell").equals("")) {
      idPlateWell = Integer.parseInt(n.getAttributeValue("idPlateWell"));
    }
    if (n.getAttributeValue("idRequest") != null && !n.getAttributeValue("idRequest").equals("")) {
      idRequest = Integer.parseInt(n.getAttributeValue("idRequest"));
    }
    if (n.getAttributeValue("qualifiedFilePath") != null && !n.getAttributeValue("qualifiedFilePath").equals("")) {
      qualifiedFilePath = n.getAttributeValue("qualifiedFilePath");
    } 
    if (n.getAttributeValue("displayName") != null && !n.getAttributeValue("displayName").equals("")) {
      displayName = n.getAttributeValue("displayName");
    }
    if (n.getAttributeValue("readLength") != null && !n.getAttributeValue("readLength").equals("")) {
      readLength = Integer.parseInt(n.getAttributeValue("readLength"));
    }
    if (n.getAttributeValue("trimmedLength") != null && !n.getAttributeValue("trimmedLength").equals("")) {
      trimmedLength = Integer.parseInt(n.getAttributeValue("trimmedLength"));
    }
    if (n.getAttributeValue("q20") != null && !n.getAttributeValue("q20").equals("")) {
      q20 = Integer.parseInt(n.getAttributeValue("q20"));
    }
    if (n.getAttributeValue("q40") != null && !n.getAttributeValue("q40").equals("")) {
      q40 = Integer.parseInt(n.getAttributeValue("q40"));
    }
    if (n.getAttributeValue("aSignalStrength") != null && !n.getAttributeValue("aSignalStrength").equals("")) {
      aSignalStrength = Integer.parseInt(n.getAttributeValue("aSignalStrength"));
    }
    if (n.getAttributeValue("cSignalStrength") != null && !n.getAttributeValue("cSignalStrength").equals("")) {
      cSignalStrength = Integer.parseInt(n.getAttributeValue("cSignalStrength"));
    }
    if (n.getAttributeValue("tSignalStrength") != null && !n.getAttributeValue("aSignalStrength").equals("")) {
      tSignalStrength = Integer.parseInt(n.getAttributeValue("tSignalStrength"));
    }
    if (n.getAttributeValue("gSignalStrength") != null && !n.getAttributeValue("gSignalStrength").equals("")) {
      gSignalStrength = Integer.parseInt(n.getAttributeValue("gSignalStrength"));
    }
    if (n.getAttributeValue("lane") != null && !n.getAttributeValue("lane").equals("")) {
      lane = Integer.parseInt(n.getAttributeValue("lane"));
    }
    
    // Set releaseDate if released
    if ( released.equals( "Y" )) {
      ch.setReleaseDate(new java.util.Date(System.currentTimeMillis()));
      ch.setIdReleaser(this.idReleaser);
    }
    if (releaseDateStr != null) {
      java.util.Date releaseDate = this.parseDate(releaseDateStr);
      ch.setReleaseDate(releaseDate);
    }
    
    // Get the request
    Request req = null;
    if ( idRequest == 0 && idPlateWell != 0 ) {
      PlateWell pw = (PlateWell) sess.get( PlateWell.class, idPlateWell );
      if ( pw != null && pw.getIdRequest() != null ) {
        if ( pw.getIsControl() != null && !pw.getIsControl().equals("Y") ) { 
          idRequest = pw.getIdRequest();
        }
      }
    }
    if ( idRequest != 0 ) {
      req = (Request) sess.get(Request.class, idRequest);
    }
    
    if ( idPlateWell != 0 ) {ch.setIdPlateWell( idPlateWell );}
    if ( req != null ) {ch.setIdRequest( req.getIdRequest() );}
    if ( qualifiedFilePath != null )  {ch.setQualifiedFilePath( qualifiedFilePath );}
    if ( displayName != null ) {ch.setFileName( displayName );}
    if ( readLength != 0 ) {ch.setReadLength( readLength );}
    if ( trimmedLength != 0 ) {ch.setTrimmedLength( trimmedLength );}
    if ( q20 != 0 )  {ch.setQ20( q20 );}
    if ( q40 != 0 ) {ch.setQ40( q40 );}
    if ( aSignalStrength != 0 ) {ch.setaSignalStrength( aSignalStrength );}
    if ( cSignalStrength != 0 )  {ch.setcSignalStrength( cSignalStrength );}
    if ( gSignalStrength != 0 ) {ch.setgSignalStrength( gSignalStrength );}
    if ( tSignalStrength != 0 ) {ch.settSignalStrength( tSignalStrength );}
    if ( lane != 0 ) {ch.setLane( lane );}
    
    if ( requeue.equals( "Y" )) {
      requeueSourceWells( idPlateWell, sess );
      if ( req != null ) {
        redoRequestsMap.put( req.getIdRequest(), req );
      }
    }
  }

  public void requeueSourceWells( int idReactionWell, Session sess ) {
      PlateWell reactionWell = (PlateWell) sess.get( PlateWell.class, idReactionWell );
      if ( reactionWell.getIsControl() != null && reactionWell.getIsControl().equals("Y") ) {
        return;
      }
      StringBuffer buf = getRedoQuery( reactionWell, false );
      Query query = sess.createQuery(buf.toString());
      List redoWells = query.list();
      
      // Now we mark the source well as a redo.  This will result
      // in the source well on the pending sample list for Fill Plate.
      // Once the source well is placed on a plate, the redo flag
      // is turned off.  We will have a record on the original
      // reaction well indicating that it was redone.
      for ( Iterator i2 = redoWells.iterator(); i2.hasNext();) {
        PlateWell redoWell = (PlateWell) i2.next();
        redoWell.setRedoFlag( "Y" );
      }
  }
  
  public static StringBuffer getRedoQuery( PlateWell reactionWell, boolean toToggleBack ) {
    StringBuffer    queryBuf = new StringBuffer();
    queryBuf.append(" SELECT     well FROM PlateWell as well ");
    queryBuf.append(" LEFT JOIN  well.plate plate ");
    
    queryBuf.append(" WHERE   (well.idPlate is NULL or plate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "') "); 
    // Filter to get source wells with redo flags
    if (!toToggleBack) {
      queryBuf.append(" AND well.redoFlag = 'N' ");
    } else {
      queryBuf.append( " AND well.redoFlag = 'Y' " );
    }
    // with sample, request, assay, and primer matching the reaction well
    if ( reactionWell.getIdSample() != null ) {
      queryBuf.append(" AND   (well.idSample = '" + reactionWell.getIdSample() + "') ");
    }
    if ( reactionWell.getIdRequest() != null ) {
      queryBuf.append(" AND   (well.idRequest = '" + reactionWell.getIdRequest() + "') ");
    }
    if ( reactionWell.getIdAssay() != null ) {
      queryBuf.append(" AND   (well.idAssay = '" + reactionWell.getIdAssay() + "') "); 
    }
    if ( reactionWell.getIdPrimer() != null ) {
      queryBuf.append(" AND   (well.idPrimer = '" + reactionWell.getIdPrimer() + "') ");
    }
    

    return queryBuf;
    
  }
  
  public static void changeRequestsToComplete(Session sess, InstrumentRun ir, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) throws ProductException {
    
    // Get requests and hash plateWells by idRequest
    HashMap<Integer, List<Object[]>> requestPlateWellMap = queryAndHashPlateWells(sess, ir, secAdvisor);
    
    // Loop through requests
    for ( Iterator i = requestPlateWellMap.keySet().iterator(); i.hasNext();) {
      int idReq = (Integer) i.next();
      
      boolean hasRedo = false;
      boolean hasMissingChromat = false;
      
      // Get the plate well rows
      List<Object[]> plateWellRows = requestPlateWellMap.get(idReq);      
      
      // Loop through plate wells
      if ( plateWellRows != null ) {
        for (Object[] wellRow : plateWellRows) {
          Integer idRequest          = (Integer)wellRow[0];
          Integer idChromatogram     = (Integer)wellRow[1];
          java.util.Date releaseDate = (java.util.Date)wellRow[2];
          Integer idPlateWell        = (Integer)wellRow[3];
          String redoFlag            = (String)wellRow[4];
          Integer idPlate            = (Integer)wellRow[5];
          String codePlateType       = (String)wellRow[6];
          Integer idInstrumentRun    = (Integer)wellRow[7];
          
          // Check for redo status on any source plate wells
          if ( codePlateType == null || codePlateType.equals( PlateType.SOURCE_PLATE_TYPE )) {
            if ( redoFlag.equals( "Y" ) ) {
              hasRedo = true;
              break;
            }
          } else if ( codePlateType != null && codePlateType.equals( PlateType.REACTION_PLATE_TYPE ) ) {
            // Check for reaction plate wells with no chromatogram -- no longer care if the chromats are unreleased or not 
            if ( idChromatogram == null ) {
              hasMissingChromat = true;
              break;
            }
          }  
        }
      }
      
      // If we found a redo flag or a missing or unreleased chromat, request is not complete, skip it
      if ( hasRedo || hasMissingChromat ) {
        continue;
      }
      
      // If request is complete, get it and change request status and complete date
      Request req = (Request) sess.get(Request.class, idReq );
      
      // If the request was already complete or failed, skip it.
      String oldRequestStatus = req.getCodeRequestStatus();
      if ( oldRequestStatus != null && ( oldRequestStatus.equals( RequestStatus.COMPLETED ) || oldRequestStatus.equals( RequestStatus.FAILED )) ) {
        continue;
      }
      
      ProductUtil.updateLedgerOnRequestStatusChange(sess, req, req.getCodeRequestStatus(), RequestStatus.COMPLETED);
      
      req.setCodeRequestStatus( RequestStatus.COMPLETED );
      req.setCompletedDate( new java.sql.Date( System.currentTimeMillis() ) );
      
      // Now change the billing items for the request from PENDING to COMPLETE
      for (BillingItem billingItem : (Set<BillingItem>)req.getBillingItemList(sess)) {
        if(billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)){
          billingItem.setCodeBillingStatus(BillingStatus.COMPLETED);
        }
      }

      // We need to email the submitter that the experiment results
      // are ready to download
      try {
        EmailHelper.sendConfirmationEmail(sess, req, secAdvisor, launchAppURL, appURL, serverName);          
      } catch (Exception e) {
        LOG.error("Error in ChromatogramParser", e);
        LOG.warn("Cannot send confirmation email for request " + req.getNumber());
      }
    
    }
    sess.flush();
  }
  
  private static HashMap queryAndHashPlateWells(Session sess, InstrumentRun ir, SecurityAdvisor secAdvisor) {
    HashMap<Integer, List<Object[]>> plateWellsMap = new HashMap<Integer, List<Object[]>>();
    
    // Get the request plate wells query
    ChromatParserRequestFilter requestFilter;
    requestFilter = new ChromatParserRequestFilter();
    requestFilter.setIdInstrumentRun( ir.getIdInstrumentRun() );
    
    StringBuffer queryBuf = requestFilter.getQuery(secAdvisor);
    List<Object[]> plateWells = sess.createQuery(queryBuf.toString()).list();
    
    // Loop through results, create a hash map of requests to the platewells for that request
    if (plateWells != null) {
      
      for (Object[] wellRow : plateWells) {
                
        Integer idRequest        = (Integer)wellRow[0];
        
        // Get the plateWells for the request
        List<Object[]> thePlateWellRows = plateWellsMap.get(idRequest);
        if (thePlateWellRows == null) {
          thePlateWellRows = new ArrayList<Object[]>();
          plateWellsMap.put(idRequest, thePlateWellRows);
        }
        // Add this plate well row to the plate wells for the request
        thePlateWellRows.add(wellRow);
      }
    }
    
    return plateWellsMap;
  }
  
}
