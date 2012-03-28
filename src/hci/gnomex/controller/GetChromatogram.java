package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.utility.ChromatTrimUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.biojava.bio.program.abi.ABIFParser;
import org.biojava.bio.program.abi.ABITrace;
import org.biojava.bio.seq.DNATools;
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

      // Get idPlateWell from the db, if db chromatogram not found, parse it from the abi comments:
      String comments = getComments(abiFile);
      int ind1 = comments.indexOf("<");
      int ind2 = comments.indexOf(">");
      String idPlateWellString = c!=null ? new Integer(c.getIdPlateWell()).toString() : comments.substring(ind1+4, ind2);
      
      // Signal Strengths:
      String aSignalStrength = new Integer(getSignalStrengths(abiFile)[1]).toString();
      String cSignalStrength = new Integer(getSignalStrengths(abiFile)[3]).toString();
      String gSignalStrength = new Integer(getSignalStrengths(abiFile)[0]).toString();
      String tSignalStrength = new Integer(getSignalStrengths(abiFile)[2]).toString();
      // Stringify all signal strengths
      String signalStrengths = "A=" + aSignalStrength + ", C=" + cSignalStrength +
                             ", G=" + gSignalStrength + ", T=" + tSignalStrength;
      
      // Run Times
      String runStart = getStartDate(abiFile) + "  " + getStartTime(abiFile);
      String runStop = getStopDate(abiFile) + "  " + getStopTime(abiFile);
      
      
      // In Finch they use requests as folders... should we have a folder variable too?
      // get the request id from the chrom
      String idRequest = c!=null && c.getIdRequest()!=null ? new Integer(c.getIdRequest()).toString() : "0";
      
      
      if (isValid())  {

        Document doc = new Document(new Element("ChromatogramList"));
        
        // Chromatogram info
        Element chromNode = new Element("Chromatogram");
        
        chromNode.setAttribute("idChromatogram", new Integer(idChromatogram).toString());
        chromNode.setAttribute("fileName", abiFile.getCanonicalPath());
        chromNode.setAttribute("label", abiFile.getName());
        chromNode.setAttribute("idRequest", idRequest);
        chromNode.setAttribute("idPlateWell", idPlateWellString);
        chromNode.setAttribute("comments", getComments(abiFile));
        chromNode.setAttribute("readLength", new Integer(getSeq(abiFile).toString().length()).toString());
        chromNode.setAttribute("signalStrengths", signalStrengths);
        chromNode.setAttribute("mobility", getMobility(abiFile));
        chromNode.setAttribute("user", getUser(abiFile));
        chromNode.setAttribute("instrModel", getInstrModel(abiFile));
        chromNode.setAttribute("instrName", getInstrName(abiFile));
        chromNode.setAttribute("lane", getLane(abiFile));
        chromNode.setAttribute("numberOfLanes", getNumberOfLanes(abiFile));
        chromNode.setAttribute("spacing", getSpacing(abiFile));
        chromNode.setAttribute("runStart", runStart);
        chromNode.setAttribute("runStop", runStop);
        
        
        // Sequence
        // This could be used to show the sequence in flex
        if (includeSeqString) {
          Element seqNode = new Element("Sequence");
          
          seqNode.setAttribute("sequenceString", getSeq(abiFile));
          
          chromNode.addContent(seqNode);
        }
        
        // Quality information
        Element qualNode = new Element("Quality");
        
        qualNode.setAttribute("Q20", "" + getQ(abiFile, 20) + " b");
        qualNode.setAttribute("Q40", "" + getQ(abiFile, 40) + " b");
        qualNode.setAttribute("Q20_len", "" + getQPercent(abiFile, 20));
        qualNode.setAttribute("Q40_len", "" + getQPercent(abiFile, 40));
        
        // This could be used to display a quality graph in flex
        if (includeQualArray) {
          // Quality to String
          int[] data = getQualVals(abiFile);
          String q = "" + data[0];
          for ( int i = 1; i < data.length; i++) {
            q += ", " + data[i];
          }
          qualNode.setAttribute("quality", q);
        }
        
        chromNode.addContent(qualNode);
        

        // TRACE
        // This information could be used to display the chromatogram in flex
        if (includeTrace) {
          ABITrace trace = new ABITrace(abiFile);
//          int[] baseCalls = trace.getBasecalls();

          int[] aTrace = trace.getTrace(DNATools.a());
          String at = "" + aTrace[0];
          for ( int i = 1; i < aTrace.length; i++) {
            at += ", " + aTrace[i];
          }

          int[] cTrace = trace.getTrace(DNATools.c());
          String ct = "" + cTrace[0];
          for ( int i = 1; i < cTrace.length; i++) {
            ct += ", " + cTrace[i];
          }
          
          int[] gTrace = trace.getTrace(DNATools.g());
          String gt = "" + gTrace[0];
          for ( int i = 1; i < gTrace.length; i++) {
            gt += ", " + gTrace[i];
          }
          
          int[] tTrace = trace.getTrace(DNATools.t());
          String tt = "" + tTrace[0];
          for ( int i = 1; i < tTrace.length; i++) {
            tt += ", " + tTrace[i];
          }
          
          Element traceNode = new Element("Trace");
          traceNode.setAttribute("aTrace", at);
          traceNode.setAttribute("cTrace", ct);
          traceNode.setAttribute("gTrace", gt);
          traceNode.setAttribute("tTrace", tt);
          
          chromNode.addContent(traceNode);
        }
        
        // TRIM
        // This information could be used to display the chromatogram in flex
        ChromatTrimUtil trimUtil = new ChromatTrimUtil(abiFile);
        trimUtil.setAcceptableQV(25);
        trimUtil.setWindowLength(10);
        trimUtil.setReqPercent(88);
        
        Element trimNode = new Element("Trim");
        trimNode.setAttribute("TrimLength", String.valueOf(trimUtil.getTrimInterval()[1]-trimUtil.getTrimInterval()[0] + 1));
        trimNode.setAttribute("TrimPos", String.valueOf(trimUtil.getTrimInterval()[0]) + "-" + String.valueOf(trimUtil.getTrimInterval()[1]));
        
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

  // Returns the signal strengths for bases G, A, T, C respectively
  private int[] getSignalStrengths(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("S/N%", 1);

      int count = (int) r1.numberOfElements;
      int[] data = new int[count];
      int max = -1;
      if (r1.elementLength == 2) {
        byte[] shortArray = r1.offsetData;
        int i = 0;
        for ( int s=0; s<shortArray.length; s+=2) {
          data[i] = ((short)((shortArray[s]<<8)|(shortArray[s+1] & 0xff))) & 0xffff;
          max = Math.max(data[i++], max);
        }
      } else if (r1.elementLength==1) {
        byte[] byteArray = r1.offsetData;
        for ( int i=0; i < byteArray.length; i++) {
          data[i] = byteArray[i] & 0xff;
          max = Math.max(data[i],max);
        }
      }
      return data;
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return new int[0];
    }
  }
  
  private String getInstrModel(File abiFile) {
    try {
      StringBuffer sb = new StringBuffer();
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("MODL", 1);
      sb.append((char) ((r1.dataRecord >>> 24) & 0xFF));
      sb.append((char) ((r1.dataRecord >>> 16) & 0xFF));
      sb.append((char) ((r1.dataRecord >>> 8) & 0xFF));
      sb.append((char) (r1.dataRecord  & 0xFF));
      return sb.toString();
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getSeq(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PBAS", 2);
      
      int count = (int) r1.numberOfElements;
      char[] data = new char[count];
      int max = -1;

      byte[] byteArray = r1.offsetData;
      for ( int i=0; i < byteArray.length; i++) {
        data[i] = (char) ( (char) (byteArray[i]) & 0xFF);
        max = Math.max(data[i],max);
      }
      
      return new String(data);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private int[] getQualVals(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PCON", 2);

      int count = (int) r1.numberOfElements;
      int[] data = new int[count];
      int max = -1;

      byte[] byteArray = r1.offsetData;
      for ( int i=0; i < byteArray.length; i++) {
        data[i] = (char) ( (byteArray[i]) & 0xFF );
        max = Math.max(data[i],max);
      }
      
      return data;
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return new int[0];
    }
  }
  
  private int getQ(File abiFile, int qVal) {
    int count = 0;
    int[] vals = getQualVals(abiFile);
    
    for ( int i=0; i<vals.length; i++ ) {
      if (vals[i]>=qVal) {
        count ++;
      }
    }
    return count;
  }
  
  private double getQPercent(File abiFile, int qVal) {
    int length = getQualVals(abiFile).length;
    int q = getQ(abiFile, qVal);
    
    return (double) q/length;
  }
  
  private String getLane(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("LANE", 1);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getNumberOfLanes(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("NLNE", 1);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getSpacing(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("SPAC", 1);
      float spac = Float.intBitsToFloat((int) r1.dataRecord ); 
      
      return   String.valueOf(spac);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getStartDate(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUND", 1);
      
      return   Long.toString((r1.dataRecord >>> 16) & 0xffff) + "/" + 
               Long.toString((r1.dataRecord >>> 8) & 0xff) + "/" +
               Long.toString((r1.dataRecord) & 0xff);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getStopDate(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUND", 2);
      
      return   Long.toString((r1.dataRecord >>> 16) & 0xffff) + "/" + 
               Long.toString((r1.dataRecord >>> 8) & 0xff) + "/" +
               Long.toString((r1.dataRecord) & 0xff);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getStartTime(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUNT", 1);
      
      return   Long.toString((r1.dataRecord >>> 24) & 0xff) + ":" + 
               Long.toString((r1.dataRecord >>> 16) & 0xff) + ":" +
               Long.toString((r1.dataRecord >>> 8) & 0xff);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getStopTime(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUNT", 2);
      
      return   Long.toString((r1.dataRecord >>> 24) & 0xff) + ":" + 
               Long.toString((r1.dataRecord >>> 16) & 0xff) + ":" +
               Long.toString((r1.dataRecord >>> 8) & 0xff);
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  // Returns the comments from the abi file
  private String getComments(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("CMNT", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getMobility(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PDMF", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getUser(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("User", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
  private String getInstrName(File abiFile) {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("MCHN", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      log.error("An exception has occurred in ReadChromatogramFile, ABI file cannot be read: ", e);
      e.printStackTrace();
      return "";
    }
  }
  
}