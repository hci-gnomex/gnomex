package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.utility.ChromatReadUtil;
import hci.gnomex.utility.ChromatTrimUtil;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetChromatogram extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetChromatogram.class);

  private String  fileName = null;
  private int     idChromatogram;
  private Boolean includeSeqString = false;
  private Boolean includeQualArray = false;
  private Boolean includeTrace = false;
  private Boolean includeRaw = false;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Search by idChromatogram or fileName.  If both are given, will search by id and 
    // get fileName from db
    if (request.getParameter("fileName") != null) {
      fileName = request.getParameter("fileName");
    } 
    
    if (request.getParameter("idChromatogram") != null) {
      idChromatogram = new Integer(request.getParameter("idChromatogram"));
    } 
    
    if (request.getParameter("includeSeqString") != null && request.getParameter("includeSeqString").equals("Y")) {
      includeSeqString = true;
    }
    
    if (request.getParameter("includeQualArray") != null && request.getParameter("includeQualArray").equals("Y")) {
      includeQualArray = true;
    }
    
    if (request.getParameter("includeTrace") != null && request.getParameter("includeTrace").equals("Y")) {
      includeTrace = true;
    }
    
    if (request.getParameter("includeRaw") != null && request.getParameter("includeRaw").equals("Y")) {
      includeRaw = true;
    }
  }

  public Command execute() throws RollBackCommandException {

    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      
      // Get the chromatogram from the db
      Chromatogram c = null;
      if ( idChromatogram!=0 ) {
        
        c = (Chromatogram) sess.get(Chromatogram.class, idChromatogram);
        
        if (c == null) {
          this.addInvalidField("missingChromatogram", "Cannot find chromatogram idChromatogram=" + idChromatogram );
        }
        
        fileName = c.getFileName();
        
      } else if ( fileName!=null ){
        
        StringBuffer buf = new StringBuffer("SELECT c from Chromatogram as c where c.displayName = '" + fileName + "'");
        
        List chromats = sess.createQuery(buf.toString()).list();
        if (chromats.size() > 0) {
          c = (Chromatogram)chromats.get(0);
        } 
        
      } else {
        this.addInvalidField("idChromatogram or fileName", "Either idChromatogram or fileName must be provided");
      }
            
      idChromatogram =  c!=null ? c.getIdChromatogram():0;


      File abiFile = new File(c.getQualifiedFilePath() + File.separator + c.getFileName());


      ChromatReadUtil chromatReader = new ChromatReadUtil(abiFile);


      
      // Get idPlateWell from the db, if db chromatogram not found, parse it from the abi comments:
      String comments = chromatReader.getComments();
      int ind1 = comments.indexOf("<");
      int ind2 = comments.indexOf(">");
      String idPlateWellString;
      if ( c != null && c.getIdPlateWell() != null ) {
        idPlateWellString = (c.getIdPlateWell()).toString();
      } else {
        idPlateWellString = comments.substring(ind1+4, ind2);
      }

      
      PlateWell plateWell = (PlateWell) sess.load( PlateWell.class, Integer.valueOf( idPlateWellString ) );
      String plateName = "";
      String instrumentRunName = "";
      String isRedo = "N";
      String sampleName = "";


      if ( plateWell != null ) {
        isRedo = plateWell.getRedoFlag();
        Plate plate = plateWell.getPlate();
        if ( plate!=null && plate.getLabel()!=null ) {
          plateName = plate.getLabel();
        } 
        InstrumentRun instrumentRun = plate.getInstrumentRun();
        if ( instrumentRun!=null && instrumentRun.getLabel()!=null ) {
          instrumentRunName = instrumentRun.getLabel();
        } 
        if ( plateWell.getSampleName() != null ) {
          sampleName = plateWell.getSampleName();
        }
      }


      // Signal Strengths:
      String aSignalStrength = Integer.toString(chromatReader.getSignalStrengths()[1]);
      String cSignalStrength = Integer.toString(chromatReader.getSignalStrengths()[3]);
      String gSignalStrength = Integer.toString(chromatReader.getSignalStrengths()[0]);
      String tSignalStrength = Integer.toString(chromatReader.getSignalStrengths()[2]);
      // Stringify all signal strengths
      String signalStrengths = "A=" + aSignalStrength + ", C=" + cSignalStrength +
                             ", G=" + gSignalStrength + ", T=" + tSignalStrength;


      // Run Times
      String runStart = chromatReader.getStartDate() + "  " + chromatReader.getStartTime();
      String runStop  = chromatReader.getStopDate()  + "  " + chromatReader.getStopTime();


      Request request = null;
      if (c.getIdRequest() != null) {
         request = (Request)sess.load(Request.class, c.getIdRequest());
      }


      String wellRowCol = "";
      if (c.getPlateWell() != null) {
        wellRowCol = c.getPlateWell().getRow() != null ? c.getPlateWell().getRow() : "";
        wellRowCol += c.getPlateWell().getCol() != null ? c.getPlateWell().getCol().toString() : "";
      }
      
      if (isValid())  {

        Document doc = new Document(new Element("ChromatogramList"));
        
        // INFO
        Element chromNode = new Element("Chromatogram");
        
        chromNode.setAttribute("idChromatogram", Integer.toString(idChromatogram));
        chromNode.setAttribute("displayName", abiFile.getName());
        chromNode.setAttribute("requestNumber", request != null ? request.getNumber() : "");
        chromNode.setAttribute("wellRowCol", wellRowCol);
        chromNode.setAttribute("idPlateWell", idPlateWellString );
        chromNode.setAttribute("comments", chromatReader.getComments());
        chromNode.setAttribute("readLength", Integer.toString(chromatReader.getSeq().toString().length()));
        chromNode.setAttribute("signalStrengths", signalStrengths);
        chromNode.setAttribute("mobility", chromatReader.getMobility());
        chromNode.setAttribute("user", request != null && request.getAppUser() != null ? request.getAppUser().getDisplayName() : "" );
        chromNode.setAttribute("instrModel", chromatReader.getInstrModel());
        chromNode.setAttribute("instrName", chromatReader.getInstrName());
        chromNode.setAttribute("lane", chromatReader.getLane());
        chromNode.setAttribute("numberOfLanes", chromatReader.getNumberOfLanes());
        chromNode.setAttribute("spacing", chromatReader.getSpacing());
        chromNode.setAttribute("runStart", runStart);
        chromNode.setAttribute("runStop", runStop);
        chromNode.setAttribute("runName", instrumentRunName);
        chromNode.setAttribute("plateName", plateName);
        chromNode.setAttribute("redoFlag", isRedo);
        chromNode.setAttribute("sampleName", sampleName);
        
        
        // SEQUENCE
        if (includeSeqString) {
          Element seqNode = new Element("Sequence");
          
          seqNode.setAttribute("sequenceString", chromatReader.getSeq());
          
          chromNode.addContent(seqNode);
        }


        // QUALITY
        Element qualNode = new Element("Quality");
        
        qualNode.setAttribute("q20", "" + chromatReader.getQ(20) + " b");
        qualNode.setAttribute("q40", "" + chromatReader.getQ(40) + " b");
        qualNode.setAttribute("q20_len", "" + chromatReader.getQPercent(20));
        qualNode.setAttribute("q40_len", "" + chromatReader.getQPercent(40));
        
        if (includeQualArray) {
          // Quality to String
          int[] data = chromatReader.getQualVals();
          String q = "" + data[0];
          for ( int i = 1; i < data.length; i++) {
            q += "," + data[i];
          }
          qualNode.setAttribute("quality", q);
        }
        
        chromNode.addContent(qualNode);


        // TRACE
        if (includeTrace) {
          
          Element traceNode = new Element("Trace");
          traceNode.setAttribute("aTrace", chromatReader.getATrace());
          traceNode.setAttribute("cTrace", chromatReader.getCTrace());
          traceNode.setAttribute("gTrace", chromatReader.getGTrace());
          traceNode.setAttribute("tTrace", chromatReader.getTTrace());
          traceNode.setAttribute("baseCalls", chromatReader.getBaseCalls());
          
          chromNode.addContent(traceNode);


        }
        
        // RawData
        if (includeRaw) {
          
          Element rawNode = new Element("RawData");
          rawNode.setAttribute("aRawData", chromatReader.getRawDataString("A"));
          rawNode.setAttribute("cRawData", chromatReader.getRawDataString("C"));
          rawNode.setAttribute("gRawData", chromatReader.getRawDataString("G"));
          rawNode.setAttribute("tRawData", chromatReader.getRawDataString("T"));
          
          chromNode.addContent(rawNode);


        }
        
        
        // TRIM
        ChromatTrimUtil trimUtil = new ChromatTrimUtil(abiFile);
        Element trimNode = new Element("Trim");
        trimNode.setAttribute("trimLength", String.valueOf(trimUtil.getTrimInterval()[1]-trimUtil.getTrimInterval()[0] + 1));
        trimNode.setAttribute("trimPos", String.valueOf(trimUtil.getTrimInterval()[0]) + "-" + String.valueOf(trimUtil.getTrimInterval()[1]));
        
        chromNode.addContent(trimNode);



        doc.getRootElement().addContent(chromNode);
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      }
      setResponsePage(this.SUCCESS_JSP);
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch (UnsupportedChromatogramFormatException e){
      log.error("An exception has occurred in ReadChromatogramFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (Exception e){
      log.error("An exception has occurred in ReadChromatogramFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
    }

    return this;
  }

  
  
}