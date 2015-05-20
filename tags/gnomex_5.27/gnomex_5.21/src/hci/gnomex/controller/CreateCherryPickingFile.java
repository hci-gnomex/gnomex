package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.ReactionType;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class CreateCherryPickingFile extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateCherryPickingFile.class);
  
  
  private Integer          idRequest;
  private Integer          transferVol;
  
  SecurityAdvisor          secAdvisor;
  
  
  private static final String    DEST_ALP = "P17";
  
  private String[] sourceFxAlpLookup = {
      "P7",
      "P8",
      "P9",
      "P10",
      "P12",
      "P13",
      "P14",
      "P16",
      "P18",
      "P19",
      "P20",
      "P21",
  };

  
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    if (request.getParameter("transferVol") != null) {
      transferVol = new Integer(request.getParameter("transferVol"));
    } else {
      this.addInvalidField("transferVol", "transferVol is required");
    }
    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report.jsp";
    this.SUCCESS_JSP_CSV = "/report_csv.jsp";
    this.SUCCESS_JSP_PDF = "/report_pdf.jsp";
    this.SUCCESS_JSP_XLS = "/report_xls.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    
    try {
   
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      
     
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
      Request request = (Request)sess.load(Request.class, idRequest);

      int sourcePlateIdx = -1;
      Integer idSourcePlatePrev = new Integer(-1);
      
      if (!secAdvisor.canRead(request)) { 
        throw new RollBackCommandException("Insufficient permissions to run this command");
      }
      
      tray = new ReportTray();
      tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
      tray.setReportTitle("Cherry Picking for experiment " + request.getNumber() );
      tray.setReportDescription("Cherry Picking for experiment " + request.getNumber());
      tray.setFileName("CherryPicking_" + "Cherry Picking for experiment " + request.getNumber());
      tray.setFormat(ReportFormats.CSV);
      
      Set columns = new TreeSet();
      columns.add(makeReportColumn("Position", 1));
      columns.add(makeReportColumn("Sample ID", 2));
      columns.add(makeReportColumn("Source Plate", 3));
      columns.add(makeReportColumn("Source Well", 4));
      columns.add(makeReportColumn("Test", 5));
      columns.add(makeReportColumn("Comments", 6));
      columns.add(makeReportColumn("Desti Well", 7));
      columns.add(makeReportColumn("Destination Alp", 8));
      columns.add(makeReportColumn("Source FX Alp", 9));
      columns.add(makeReportColumn("Transfer Vol", 10));
      columns.add(makeReportColumn("REF", 11));
      
      tray.setColumns(columns);

        
      StringBuffer buf = new StringBuffer();
      buf.append("SELECT sourceWell, sourcePlate, sample, destPlate, destWell ");
      buf.append("FROM   Request req ");
      buf.append("JOIN   req.samples as sample ");
      buf.append("JOIN   sample.wells as sourceWell ");
      buf.append("JOIN   sourceWell.plate as sourcePlate ");
      buf.append("JOIN   sample.wells as destWell ");
      buf.append("JOIN   destWell.plate as destPlate ");
      buf.append("WHERE  req.idRequest = " + idRequest + " ");
      buf.append("AND    sourcePlate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "' ");
      buf.append("AND    destPlate.codeReactionType = '" + ReactionType.CHERRY_PICKING_REACTION_TYPE + "' ");
      buf.append("ORDER BY destWell.position ");
      
      List results = sess.createQuery(buf.toString()).list();
      
      for(Iterator i = results.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        PlateWell sourceWell = (PlateWell)row[0];
        Plate sourcePlate    = (Plate)row[1];
        Sample sample        = (Sample)row[2];
        Plate destPlate      = (Plate)row[3];
        PlateWell destWell   = (PlateWell)row[4];
        
        if (sourcePlate.getIdPlate() != idSourcePlatePrev) {
          sourcePlateIdx++;
        }
        
        ReportRow reportRow = new ReportRow();
        List values  = new ArrayList();
        
        values.add(destWell.getPosition() != null ? destWell.getPosition().toString() : "");
        values.add(sample != null ? sample.getName() : "");
        values.add(sourcePlate != null ? sourcePlate.getIdPlate().toString() : "");
        values.add(sourceWell != null ? sourceWell.getRow() + sourceWell.getCol() : "");
        values.add("-");
        values.add(sample.getRequest().getNumber());
        values.add(destWell != null ? destWell.getRow() + destWell.getCol() : "");
        values.add(DEST_ALP);
        values.add(sourceFxAlpLookup[sourcePlateIdx]);
        values.add(transferVol.toString());
        values.add(new Integer(sourcePlateIdx+1).toString());
       
        reportRow.setValues(values);
        tray.addRow(reportRow);
        
        idSourcePlatePrev = sourcePlate.getIdPlate();

      }

      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in CreateCherryPickingFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in CreateCherryPickingFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in CreateCherryPickingFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in CreateCherryPickingFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        secAdvisor.closeReadOnlyHibernateSession();    
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  

  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {
    request.setAttribute("tray", this.tray);
    return request;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setResponseState(javax.servlet.http.HttpServletResponse)
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setSessionState(javax.servlet.http.HttpSession)
   */
  public HttpSession setSessionState(HttpSession session) {
    // TODO Auto-generated method stub
    return session;
  }

  /* (non-Javadoc)
   * @see hci.report.utility.ReportCommand#loadContextPermissions()
   */
  public void loadContextPermissions(){
    
  }
  public void loadContextPermissions(String userName) throws SQLException {
    
  }
  
}