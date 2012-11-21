package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.hibernate.Session;




public class GetDataTrack extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetDataTrack.class);
  
  private Integer idDataTrack;
  private String dataTrackNumber;
  private String serverName;
  private String baseDir;
  private String analysisBaseDir;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack"));   
    } else if ( request.getParameter( "dataTrackNumber" ) != null && !request.getParameter("dataTrackNumber").equals("")) {
      dataTrackNumber = request.getParameter( "dataTrackNumber" );
    } else {
      this.addInvalidField("Missing parameters", "idDataTrack or dataTrackNumber required");
    }
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);
      analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
      
      DataTrack dataTrack;
      if ( idDataTrack != null && !idDataTrack.equals( "" )) {
        dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));
      } else {
        dataTrack = this.getDataTrackFromDataTrackNumber( sess, dataTrackNumber );
      }

      // TODO: GENOPUB Need to send in analysis file data path?  
      if (this.getSecAdvisor().canRead(dataTrack)) {
        Document doc = dataTrack.getXML(this.getSecAdvisor(), DictionaryHelper.getInstance(sess), baseDir, analysisBaseDir);
        org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to access data track");
      }

    }catch (NamingException e){
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  public static DataTrack getDataTrackFromDataTrackNumber(Session sess, String  dataTrackNumber) {
    DataTrack dt = null;

    StringBuffer buf = new StringBuffer("SELECT dt from DataTrack as dt where dt.fileName = '" + dataTrackNumber.toUpperCase() + "'");
    List datatracks = sess.createQuery(buf.toString()).list();
    if (datatracks.size() > 0) {
      dt = (DataTrack)datatracks.get(0);      
    }
    return dt;
  }

}