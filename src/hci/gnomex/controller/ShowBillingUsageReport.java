package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class ShowBillingUsageReport extends ReportCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(ShowBillingUsageReport.class);
  
  
  private java.sql.Date    startDate;
  private java.sql.Date    endDate;
  private Integer		   idCoreFacility;
  private String           isExternal = "N";
  private SecurityAdvisor  secAdvisor;
  
  private BigDecimal     zero = new BigDecimal(0);
  


  
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    
    if (request.getParameter("startDate") != null) {
      startDate = this.parseDate(request.getParameter("startDate"));
    } else {
      this.addInvalidField("startDate", "startDate is required");
    }
    
    if (request.getParameter("endDate") != null) {
      endDate = this.parseDate(request.getParameter("endDate"));
    } else {
      this.addInvalidField("endDate", "endDate is required");
    }
    
    if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
        idCoreFacility = Integer.valueOf(request.getParameter("idCoreFacility"));
    }
    
    if (request.getParameter("isExternal") != null) {
      isExternal = request.getParameter("isExternal");
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
      
      if (this.isValid()) {
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 

          TreeMap labMap = new TreeMap();
          getBillingUsage(sess, labMap);


          if (isValid()) {
            // set up the ReportTray
            tray = new ReportTray();
            tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
            tray.setReportTitle("Usage Report");
            tray.setReportDescription("Usage Report" + " " + this.formatDate(startDate) + " - " + this.formatDate(endDate) + (isExternal.equals("Y") ? " (External)" : " (Internal)"));
            String coreQualifier = "";
            if (idCoreFacility != null) {
                CoreFacility core = (CoreFacility)sess.get(CoreFacility.class, idCoreFacility);
                coreQualifier += "_" + core.getDisplay();
            }
            tray.setFileName("GNomEx Usage Report" + coreQualifier);
            tray.setFormat(ReportFormats.XLS);

            Set columns = new TreeSet();
            columns.add(makeReportColumn("Lab", 1));
            columns.add(makeReportColumn("Description", 2));
            columns.add(makeReportColumn("Kind", 3));
            columns.add(makeReportColumn("Qty", 4));

            tray.setColumns(columns);

            for(Iterator i = labMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();
              List results = (List)labMap.get(key);

              for(Iterator i1 = results.iterator(); i1.hasNext();) {
                Object[] row = (Object[])i1.next();

                String description = (String)row[2];
                String chargeKind = (String)row[3];
                BigDecimal qty = row[4] != null ? (BigDecimal)row[4] : BigDecimal.valueOf(0);
                BigDecimal qtyRounded = qty.setScale(0, BigDecimal.ROUND_UP);

                ReportRow reportRow = new ReportRow();
                List values  = new ArrayList();


                values.add(key);
                values.add(description);
                values.add(dh.getBillingChargeKind(chargeKind));
                values.add(qtyRounded.toString());

                reportRow.setValues(values);
                tray.addRow(reportRow);

              }

            }
          }
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission.");
        }

      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      LOG.error("An exception has occurred in ShowBillingUsageReport ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      LOG.error("An exception has occurred in ShowBillingUsageReport ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      LOG.error("An exception has occurred in ShowBillingUsageReport ", e);

      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      LOG.error("An exception has occurred in ShowBillingUsageReport ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        secAdvisor.closeReadOnlyHibernateSession();    
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }
  
  private void getBillingUsage(Session sess, Map labMap) throws Exception {


    // Get the total qty  by lab for all Sample Quality billing items
    StringBuffer buf = new StringBuffer();
    buf.append(" select  lab.lastName, ");
    buf.append("     lab.firstName,");
    buf.append("     lab.firstName, ");  // just a place holder in the row to stick in 'Sample Quality'
    buf.append("     bi.codeBillingChargeKind,");
    buf.append("     sum(bi.qty * bi.percentagePrice)");
    buf.append(" from BillingItem bi");
    buf.append(" join bi.billingPeriod as bp");
    buf.append(" join bi.priceCategory as cat");
    buf.append(" join bi.lab as lab");
    buf.append(" where bp.startDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' and bp.endDate <= '" + this.formatDate(endDate, this.DATE_OUTPUT_SQL) + "'");
    if (idCoreFacility != null) {
    	buf.append(" AND bi.idCoreFacility = ");
    	buf.append(idCoreFacility + " ");
    }
    buf.append(" and cat.name like 'Sample Quality%' and cat.name != 'Miscellaneous'");
    buf.append(" and qty is not null");
    buf.append(" and bi.codeBillingStatus != '" + BillingStatus.PENDING + "'");

    if ( isExternal != null && isExternal.equals( "Y" )) {
      buf.append(" and (lab.isExternalPricing = 'Y' OR lab.isExternalPricingCommercial = 'Y')");
    } else {
      buf.append(" and lab.isExternalPricing != 'Y' and lab.isExternalPricingCommercial != 'Y'");
    }
    
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }
    
    
    buf.append(" group by lab.lastName, lab.firstName, bi.codeBillingChargeKind");
    List results = sess.createQuery(buf.toString()).list();
    fillSampleQualityMap(labMap, results);

    // Get the total qty by lab for all billing items that are not Miscellaneous or Sample Quality
    buf = new StringBuffer();
    buf.append(" select  lab.lastName, ");
    buf.append(" lab.firstName,");
    buf.append(" cat.name, ");
    buf.append(" bi.codeBillingChargeKind, ");
    buf.append(" sum(bi.qty * bi.percentagePrice)");
    buf.append(" from BillingItem  bi");
    buf.append(" join bi.billingPeriod as bp");
    buf.append(" join bi.priceCategory as cat");
    buf.append(" join bi.lab as lab");
    buf.append(" where bp.startDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' and bp.endDate <= '" + this.formatDate(endDate, this.DATE_OUTPUT_SQL) + "'");
    if (idCoreFacility != null) {
    	buf.append(" AND bi.idCoreFacility = ");
    	buf.append(idCoreFacility + " ");
    }
    buf.append(" and bi.qty is not NULL");
    buf.append(" and bi.codeBillingStatus != '"+ BillingStatus.PENDING + "'");
    buf.append(" and cat.name not like 'Sample Quality%' and cat.name != 'Miscellaneous'");
    
    if ( isExternal != null && isExternal.equals( "Y" )) { 
      buf.append(" and (lab.isExternalPricing = 'Y' OR lab.isExternalPricingCommercial = 'Y')");
    } else {
      buf.append(" and lab.isExternalPricing != 'Y' and lab.isExternalPricingCommercial != 'Y'");
    }
    
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }
    
    buf.append(" group by lab.lastName, lab.firstName, cat.name, bi.codeBillingChargeKind");
    results = sess.createQuery(buf.toString()).list();
    fillMap(labMap, results);
    
    // Get the total qty by lab for Miscellaneous billing items
    buf = new StringBuffer();
    buf.append(" select  lab.lastName, ");
    buf.append("     lab.firstName,");
    buf.append("     bi.description, ");
    buf.append("     bi.codeBillingChargeKind, ");
    buf.append("     sum(bi.qty * bi.percentagePrice)");
    buf.append(" from BillingItem bi");
    buf.append(" join bi.billingPeriod as bp");
    buf.append(" join bi.priceCategory as cat");
    buf.append(" join bi.lab as lab");
    buf.append(" where bp.startDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' and bp.endDate <= '" + this.formatDate(endDate, this.DATE_OUTPUT_SQL) + "'");
    if (idCoreFacility != null) {
    	buf.append(" AND bi.idCoreFacility = ");
    	buf.append(idCoreFacility + " ");
    }
    buf.append(" and cat.name = 'Miscellaneous'");   
    buf.append(" and qty is not null");
    buf.append(" and bi.codeBillingStatus != '"+ BillingStatus.PENDING + "'");
    
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }
    
    buf.append(" group by lab.lastName, lab.firstName, bi.description, bi.codeBillingChargeKind");
    results = sess.createQuery(buf.toString()).list();
    fillMap(labMap, results);

        
    
  }
  
 
  private void fillMap(Map labMap, List results) {
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String labLastName         = row[0] != null ? (String)row[0] : "";
      String labFirstName        = row[1] != null ? (String)row[1] : "";
      
      String key = labLastName;
      if (!labFirstName.equals("")) {
        key += ", " + labFirstName;
      }
      
      List rows = (List)labMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        labMap.put(key, rows);
      }
      rows.add(row);
      
      
      labMap.put(key, rows);
    }
    
  }
  
  
  /*
   * All sample quality, regardless of the category name should be
   * totaled under the description 'Sample Quality'.  We can't
   * use constants in the HQL group by, so we just inject
   * in the constant in post query processing.
   */
  private void fillSampleQualityMap(Map labMap, List results) {
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String labLastName         = row[0] != null ? (String)row[0] : "";
      String labFirstName        = row[1] != null ? (String)row[1] : "";
      
      // Force the category to the constant 'Sample Quality'
      row[2] = "Sample Quality";
      
      String key = labLastName;
      if (!labFirstName.equals("")) {
        key += ", " + labFirstName;
      }
      
      List rows = (List)labMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        labMap.put(key, rows);
      }
      rows.add(row);
      
      
      labMap.put(key, rows);
    }
    
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