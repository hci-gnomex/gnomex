package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Organism;
import hci.gnomex.model.ProjectExperimentReportFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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


public class ShowProjectExperimentReport extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowProjectExperimentReport.class);
  
  
  private SecurityAdvisor               secAdvisor;
  private Integer                       idLab;
  private ProjectExperimentReportFilter filter;
  
  private String                        today;
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.valueOf(request.getParameter("idLab"));
    } 
    
    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }
    
    today = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

    filter = new ProjectExperimentReportFilter();
    this.loadDetailObject(request, filter);
  }

  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report.jsp";
    this.SUCCESS_JSP_CSV = "/report_csv.jsp";
    this.SUCCESS_JSP_PDF = "/report_pdf.jsp";
    this.SUCCESS_JSP_XLS = "/report_xls.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    
    try {
         
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Create the report and define the columns
      createReportTray(sess, dh);
    
      // Get the results
      StringBuffer queryBuf = filter.getQuery(secAdvisor);

      if (this.isValid()) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        List results = (List)sess.createQuery(queryBuf.toString()).list();
        
        for(Iterator i = results.iterator(); i.hasNext();) {
          Object[] row = (Object[])i.next();

          ReportRow reportRow = makeReportRow(row, dateFormat, dh);
          tray.addRow(reportRow);
        }
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowAnnotationReport ", e);
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
  
  private void createReportTray(Session sess, DictionaryHelper dh) {
    // Get the lab
    String labQualifier = "";
    if (idLab != null) {
      Lab lab = (Lab)sess.get(Lab.class, idLab);
      labQualifier += "_" + lab.getLastName();
    }
    
    String title = "GNomEx Requests";
    String fileName = "gnomex_request" + labQualifier + "_" + today;
    
    // set up the ReportTray
    tray = new ReportTray();
    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
    tray.setReportTitle(title);
    tray.setReportDescription(title);
    tray.setFileName(fileName);
    tray.setFormat(ReportFormats.XLS);
    
    Set columns = new TreeSet();
    columns.add(makeReportColumn("Lab", 1));
    columns.add(makeReportColumn("Owner", 2));
    columns.add(makeReportColumn("Number", 3));
    columns.add(makeReportColumn("Category", 4));
    columns.add(makeReportColumn("Application", 5));
    columns.add(makeReportColumn("Creation", 6));
    columns.add(makeReportColumn("Last Modification", 7));
    columns.add(makeReportColumn("Visibility", 8));
    columns.add(makeReportColumn("Completed", 9));
    columns.add(makeReportColumn("Description", 10));
    columns.add(makeReportColumn("Organism", 11));
    columns.add(makeReportColumn("# Samples", 12));
    
    
    tray.setColumns(columns);
  }
  
  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }

  private ReportRow makeReportRow(Object[] row, SimpleDateFormat dateFormat, DictionaryHelper dh) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();
    
    String labLastName = (String)row[ProjectExperimentReportFilter.COL_LAB_LASTNAME];
    String labFirstName = (String)row[ProjectExperimentReportFilter.COL_LAB_FIRSTNAME];
    String ownerLastName = (String)row[ProjectExperimentReportFilter.COL_OWNER_LASTNAME];
    String ownerFirstName = (String)row[ProjectExperimentReportFilter.COL_OWNER_FIRSTNAME];
    String number = (String)row[ProjectExperimentReportFilter.COL_REQUEST_NUMBER];
    String codeRequestCategory = (String)row[ProjectExperimentReportFilter.COL_CODE_REQUEST_CATEGORY];
    String codeRequestApplication = (String)row[ProjectExperimentReportFilter.COL_CODE_REQUEST_APPLICATION];
    Date createDate = (Date)row[ProjectExperimentReportFilter.COL_CREATE_DATE];
    Date modifyDate = (Date)row[ProjectExperimentReportFilter.COL_MODIFY_DATE];
    String codeVisibility = (String)row[ProjectExperimentReportFilter.COL_CODE_VISIBILITY];
    Date completeDate = (Date)row[ProjectExperimentReportFilter.COL_COMPLETED_DATE];
    String description = (String)row[ProjectExperimentReportFilter.COL_DESCRIPTION];
    Integer idOrganism = (Integer)row[ProjectExperimentReportFilter.COL_ORGANISM];
    Integer numSamples = (Integer)row[ProjectExperimentReportFilter.COL_NUMBER_SAMPLES];

    String labName = Lab.formatLabName(labLastName, labFirstName);
    String ownerName = AppUser.formatName(ownerLastName, ownerFirstName);
    String requestCategory = dh.getRequestCategory(codeRequestCategory);
    String application = dh.getApplication(codeRequestApplication);
    String createDateString = createDate != null ? dateFormat.format(createDate) : "";
    String modifyDateString = modifyDate != null ? dateFormat.format(modifyDate) : "";
    String visibility = DictionaryManager.getDisplay("hci.gnomex.model.Visibility", codeVisibility);
    String completeDateString = completeDate != null ? dateFormat.format(completeDate) : "";
    String organism = dh.getOrganism(idOrganism);
    
    values.add(labName);
    values.add(ownerName);
    values.add(number);
    values.add(requestCategory);
    values.add(application);
    values.add(createDateString);
    values.add(modifyDateString);
    values.add(visibility);
    values.add(completeDateString);
    values.add(description);
    values.add(organism);
    values.add(numSamples.toString());
   
    reportRow.setValues(values);
    
    return reportRow;
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