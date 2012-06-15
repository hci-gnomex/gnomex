
package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.controller.SaveChromatogram;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class ChromatogramParser extends DetailObject implements Serializable
{

  private Document doc;
  private Map      chMap = new HashMap();
  private List<Chromatogram>       chromatList = new ArrayList<Chromatogram>();
  
  private SecurityAdvisor secAdvisor = null;
  private String launchAppURL = null;
  private String appURL = null;
  private String serverName = null;
  
  public ChromatogramParser(Document doc) {
    this.doc = doc;
  }
  
  public ChromatogramParser(){
    
  }

  public void init() {
    setChMap( new HashMap() );
  }

  public void parse(Session sess, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) throws Exception {
    Chromatogram ch = new Chromatogram();
    Element root = this.doc.getRootElement();

    this.secAdvisor = secAdvisor;
    this.launchAppURL = launchAppURL;
    this.appURL = appURL;
    this.serverName = serverName;
    
    for (Iterator i = root.getChildren("Chromatogram").iterator(); i.hasNext();) {
      Element node = (Element) i.next();

      String idChromatogram = node.getAttributeValue("idChromatogram");

      if (idChromatogram.equals(null) || idChromatogram.equals("0")) {
        
        ch = new Chromatogram();
        sess.save( ch );
        idChromatogram = ch.getIdChromatogram().toString();
        
      } else {
        
        ch = (Chromatogram) sess.get(Chromatogram.class,
            Integer.parseInt(idChromatogram));
      }

      this.initializeChromat(sess, node, ch);
      
      sess.flush();
      
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
      if ( ch.getReleaseDate() != null ) {
        if ( ir!=null ) {
          ir.setCodeInstrumentRunStatus( InstrumentRunStatus.COMPLETE );
          SaveChromatogram.changeRequestsToComplete(sess, ir, secAdvisor, launchAppURL, appURL, serverName);
        }
      }
      
      sess.flush();
      
      chMap.put( idChromatogram, ch );
      chromatList.add( ch );
    }
  }

  protected void initializeChromat(Session sess, Element n,
      Chromatogram ch) throws Exception {
    
    int                   idPlateWell = 0;
    int                   idRequest = 0;
    String                released = "N";
    String                releaseDateStr = null;
    String                requeue = "N";
    String                fileName = null;
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
    
    if (n.getAttributeValue("fileName") != null && !n.getAttributeValue("fileName").equals("")) {
      fileName = n.getAttributeValue("fileName");
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
    
    // Set releaseDate if released
    if ( released.equals( "Y" )) {
      ch.setReleaseDate(new java.util.Date(System.currentTimeMillis()));
    }
    if (releaseDateStr != null) {
      java.util.Date releaseDate = this.parseDate(releaseDateStr);
      ch.setReleaseDate(releaseDate);
    }
    
    if ( idPlateWell != 0 ) {ch.setIdPlateWell( idPlateWell );}
    if ( idRequest != 0 ) {ch.setIdRequest( idRequest );}
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
    
    if ( requeue.equals( "Y" )) {
      requeueSourceWells( idPlateWell, sess );
    }
  }

  public void setChMap( Map chMap ) {
    this.chMap = chMap;
  }

  public Map getChMap() {
    return chMap;
  }

  
  public List<Chromatogram> getChromatList() {
    return chromatList;
  }

  
  public void setChromatList( List<Chromatogram> chromatList ) {
    this.chromatList = chromatList;
  }

  public void requeueSourceWells( int idReactionWell, Session sess ) {
      PlateWell reactionWell = (PlateWell) sess.get( PlateWell.class, idReactionWell );
      StringBuffer buf = getRedoQuery( reactionWell, false );
      Query query = sess.createQuery(buf.toString());
      List redoWells = query.list();
      
      // If source redo wells are found, mark the reaction well as a redo, and
      // remove redo flag from the source wells.
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
  
}
