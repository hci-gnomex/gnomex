package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
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
  private String  baseDir;
  private int     idChromatogram;
  private Boolean includeSeqString = false;
  private Boolean includeQualArray = false;
  private Boolean includeTrace = false;

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
  }

  public Command execute() throws RollBackCommandException {

    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      baseDir = "C:/temp/";
      
      // Get the chromatogram from the db
      Chromatogram c = null;
      if ( idChromatogram!=0 ) {
        
        c = (Chromatogram) sess.get(Chromatogram.class, idChromatogram);
        
        if (c == null) {
          this.addInvalidField("missingChromatogram", "Cannot find chromatogram idChromatogram=" + idChromatogram );
        }
        
        fileName = c.getDisplayName();
        
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
      
      File abiFile = new File(baseDir, fileName);

      ChromatReadUtil chromatReader = new ChromatReadUtil(abiFile);
      
      // Get idPlateWell from the db, if db chromatogram not found, parse it from the abi comments:
      String comments = chromatReader.getComments();
      int ind1 = comments.indexOf("<");
      int ind2 = comments.indexOf(">");
      String idPlateWellString = c!=null ? new Integer(c.getIdPlateWell()).toString() : comments.substring(ind1+4, ind2);
      
      // Signal Strengths:
      String aSignalStrength = new Integer(chromatReader.getSignalStrengths()[1]).toString();
      String cSignalStrength = new Integer(chromatReader.getSignalStrengths()[3]).toString();
      String gSignalStrength = new Integer(chromatReader.getSignalStrengths()[0]).toString();
      String tSignalStrength = new Integer(chromatReader.getSignalStrengths()[2]).toString();
      // Stringify all signal strengths
      String signalStrengths = "A=" + aSignalStrength + ", C=" + cSignalStrength +
                             ", G=" + gSignalStrength + ", T=" + tSignalStrength;
      
      // Run Times
      String runStart = chromatReader.getStartDate() + "  " + chromatReader.getStartTime();
      String runStop  = chromatReader.getStopDate()  + "  " + chromatReader.getStopTime();
      
      
      String idRequest = c!=null && c.getIdRequest()!=null ? new Integer(c.getIdRequest()).toString() : "0";
      
      
      if (isValid())  {

        Document doc = new Document(new Element("ChromatogramList"));
        
        // INFO
        Element chromNode = new Element("Chromatogram");
        
        chromNode.setAttribute("idChromatogram", new Integer(idChromatogram).toString());
        chromNode.setAttribute("fileName", abiFile.getCanonicalPath());
        chromNode.setAttribute("label", abiFile.getName());
        chromNode.setAttribute("idRequest", idRequest);
        chromNode.setAttribute("idPlateWell", idPlateWellString);
        chromNode.setAttribute("comments", chromatReader.getComments());
        chromNode.setAttribute("readLength", new Integer(chromatReader.getSeq().toString().length()).toString());
        chromNode.setAttribute("signalStrengths", signalStrengths);
        chromNode.setAttribute("mobility", chromatReader.getMobility());
        chromNode.setAttribute("user", chromatReader.getUser());
        chromNode.setAttribute("instrModel", chromatReader.getInstrModel());
        chromNode.setAttribute("instrName", chromatReader.getInstrName());
        chromNode.setAttribute("lane", chromatReader.getLane());
        chromNode.setAttribute("numberOfLanes", chromatReader.getNumberOfLanes());
        chromNode.setAttribute("spacing", chromatReader.getSpacing());
        chromNode.setAttribute("runStart", runStart);
        chromNode.setAttribute("runStop", runStop);
        
        
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