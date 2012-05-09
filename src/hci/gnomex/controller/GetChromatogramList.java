package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ChromatogramFilter;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetChromatogramList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetChromatogramList.class);

  private ChromatogramFilter          chromFilter;
  private String                      listKind = "ChromatogramList";
  private String                      abiFileName;
  private Element                     rootNode = null;
  private String                      message = "";

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    chromFilter = new ChromatogramFilter();
    HashMap errors = this.loadDetailObject(request, chromFilter);
    this.addInvalidFields(errors);

    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");

    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Document doc = new Document(new Element(listKind));
      
      if (!chromFilter.hasSufficientCriteria(this.getSecAdvisor())) {
        message = "Please select a filter";
//        rootNode.setAttribute("message", message);

      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        StringBuffer buf = chromFilter.getQuery(this.getSecAdvisor());
        log.info("Query for GetChromatogramList: " + buf.toString());
        List chromats = sess.createQuery(buf.toString()).list();

        for(Iterator i = chromats.iterator(); i.hasNext();) {

          Object[] row = (Object[])i.next();

          
          Integer idChromatogram = row[0] == null ? new Integer(0) : (Integer)row[0];
          Integer idPlateWell = row[1] == null ? new Integer(0) : (Integer)row[1];
          Integer idRequest = row[2] == null ? new Integer(0) : (Integer)row[2];
          String  fileName    = row[3] == null ? "" : (String)row[3];
          abiFileName = fileName;
          String  displayName    = row[4] == null ? "" : (String)row[4];
          Integer readLength = row[5] == null ? new Integer(0) : (Integer)row[5];
          Integer trimmedLength = row[6] == null ? new Integer(0) : (Integer)row[6];
          Integer q20 = row[7] == null ? new Integer(0) : (Integer)row[7];
          Integer q40 = row[8] == null ? new Integer(0) : (Integer)row[8];
          Integer aSignalStrength = row[9] == null ? new Integer(0) : (Integer)row[9];
          Integer cSignalStrength = row[10] == null ? new Integer(0) : (Integer)row[10];
          Integer gSignalStrength = row[11] == null ? new Integer(0) : (Integer)row[11];
          Integer tSignalStrength = row[12] == null ? new Integer(0) : (Integer)row[12];
          String  releaseDate = this.formatDate( ( java.sql.Timestamp ) row[13] );
          
          double q20_len;
          double q40_len;
          
          if ( readLength!=0 ) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            q20_len = (double) q20/readLength;
            q20_len = Double.valueOf(twoDForm.format(q20_len));
            q40_len = (double) q40/readLength;
            q40_len = Double.valueOf(twoDForm.format(q40_len));
          } else {
            q20_len = new Integer(0);
            q40_len =  new Integer(0);
          }

          
          Element cNode = new Element("Chromatogram");
          cNode.setAttribute("idChromatogram", idChromatogram.toString());
          cNode.setAttribute("idPlateWell", idPlateWell.toString());
          cNode.setAttribute("idRequest", idRequest.toString());
          cNode.setAttribute("fileName", fileName);
          cNode.setAttribute("displayName", displayName);
          cNode.setAttribute("readLength", readLength.toString());
          cNode.setAttribute("trimmedLength", trimmedLength.toString());
          cNode.setAttribute("q20", q20.toString());
          cNode.setAttribute("q40", q40.toString());
          cNode.setAttribute("aSignalStrength", aSignalStrength.toString());
          cNode.setAttribute("cSignalStrength", cSignalStrength.toString());
          cNode.setAttribute("gSignalStrength", gSignalStrength.toString());
          cNode.setAttribute("tSignalStrength", tSignalStrength.toString());
          cNode.setAttribute("q20_len", "" + q20_len);
          cNode.setAttribute("q40_len", "" + q40_len);
          cNode.setAttribute("viewURL", getViewURL());
          cNode.setAttribute( "releaseDate", releaseDate );
          
          doc.getRootElement().addContent(cNode);

        }
      }


      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }
  
  public String getViewURL() {
    String viewURL = "";
    // Only allow viewing on supported browser mime types
    // TODO:  Use standard way of supported mime types instead of hardcoded list
    if (abiFileName.toLowerCase().endsWith(".abi") ||
        abiFileName.toLowerCase().endsWith(".ab1")  ) {
      // Only allow viewing for files under 50 MB
//      if (this.fileSize < Math.pow(2, 20) * 50) {
//        String dirParm = this.getQualifiedFilePath() != null  ? "&dir=" + this.getQualifiedFilePath() : "";
        viewURL = Constants.DOWNLOAD_CHROMATOGRAM_FILE_SERVLET + "?fileName=" + this.abiFileName;    
//      }
    }
    return viewURL;
  }

}