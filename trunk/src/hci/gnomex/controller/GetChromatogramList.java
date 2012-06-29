package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.ChromatogramFilter;
import hci.gnomex.model.Request;

import java.io.File;
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

        boolean alt = false;
        String runNumberPrev = "";
        Integer idPlatePrev = new Integer(-1);
        AppUser releaser = null;
        for(Iterator i = chromats.iterator(); i.hasNext();) {

          Object[] row = (Object[])i.next();

          Integer idChromatogram = row[0] == null ? new Integer(0) : (Integer)row[0];
          Integer idPlateWell = row[1] == null ? new Integer(0) : (Integer)row[1];
          Integer idRequest = row[2] == null ? new Integer(0) : (Integer)row[2];
          String  qualifiedFilePath    = row[3] == null ? "" : (String)row[3];
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
          String wellRow = row[14] != null ? (String)row[14] : "";
          String wellCol = row[15] != null ? ((Integer)row[15]).toString() : "";
          String requestNumber = row[16] != null ? (String)row[16] : "";
          String sampleName = row[17] != null ? (String)row[17] : "";
          String submitterFirstName = row[18] != null ? (String)row[18] : "";
          String submitterLastName = row[19] != null ? (String)row[19] : "";
          String runNumber = row[20] != null ? ((Integer)row[20]).toString() : "";
          String runName = row[21] != null ? (String)row[21] : "";
          Integer idPlate = row[22] == null ? new Integer(0) : (Integer)row[22];
          String plateLabel = row[23] != null ? (String)row[23] : "";
          String redoFlag = row[24] != null ? (String)row[24] : "";
          Integer idReleaser = row[25] != null ? (Integer)row[25] : new Integer(0);
          
          if (!runNumber.equals(runNumberPrev)) {
            alt = !alt;
          } else if (!idPlate.equals(idPlatePrev)) {
            alt = !alt;
          }
          
          abiFileName = qualifiedFilePath + File.separator + displayName;
          File abiFile = new File(qualifiedFilePath, displayName);

          
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
          
          releaser = (AppUser)sess.get(AppUser.class, idReleaser);
          
          String submitter = AppUser.formatAppUserName(submitterLastName, submitterFirstName);

          
          Element cNode = new Element("Chromatogram");
          cNode.setAttribute("idChromatogram", idChromatogram.toString());
          cNode.setAttribute("idPlateWell", idPlateWell.toString());
          cNode.setAttribute("idRequest", idRequest.toString());
          cNode.setAttribute("qualifiedFilePath", qualifiedFilePath);
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
          cNode.setAttribute("viewURL", getViewURL(idChromatogram));
          cNode.setAttribute( "releaseDate", releaseDate );
          cNode.setAttribute( "submitter", submitter );
          cNode.setAttribute( "wellRow", wellRow );
          cNode.setAttribute( "wellCol", wellCol );
          cNode.setAttribute( "requestNumber", requestNumber );
          cNode.setAttribute( "sampleName", sampleName );
          cNode.setAttribute( "runNumber", runNumber );
          cNode.setAttribute( "runName", runName );
          cNode.setAttribute( "fileSize", Long.valueOf(abiFile.length()).toString() );
          cNode.setAttribute("altColor",  new Boolean(alt).toString());
          cNode.setAttribute("plateLabel",  plateLabel != null && !plateLabel.equals("") ? plateLabel : idPlate.toString());
          cNode.setAttribute("redoFlag", redoFlag);
          cNode.setAttribute("releaser", releaser != null ? releaser.getDisplayName() : "");
          
          doc.getRootElement().addContent(cNode);

          runNumberPrev = runNumber;
          idPlatePrev = idPlate;
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
  
  public String getViewURL(Integer idChromatogram) {
    String viewURL = Constants.DOWNLOAD_CHROMATOGRAM_FILE_SERVLET + "?idChromatogram=" + idChromatogram;    
    return viewURL;
  }

}