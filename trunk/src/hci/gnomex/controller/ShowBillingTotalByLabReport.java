package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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


public class ShowBillingTotalByLabReport extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowBillingMonthendReport.class);
  
  
  private java.sql.Date    startDate;
  private java.sql.Date    endDate;
  private SecurityAdvisor  secAdvisor;
  
  
  
  private NumberFormat   currencyFormat = NumberFormat.getCurrencyInstance();
  private NumberFormat   percentFormat = NumberFormat.getPercentInstance();
  
  private BigDecimal     zero = new BigDecimal(0);
  
  private BigDecimal grandTotalIllumina = new BigDecimal("0");
  private BigDecimal grandTotalMicroarray = new BigDecimal("0");
  private BigDecimal grandTotalQC= new BigDecimal("0");
  private BigDecimal grandTotal = new BigDecimal("0");

  
 
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
      
     
 
      

      if (this.isValid()) {
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 
          
          TreeMap labMap = new TreeMap();

          TreeMap solexaMap = new TreeMap();
          getBillingItems(sess, RequestCategory.SOLEXA_REQUEST_CATEGORY, labMap, solexaMap);
          
          TreeMap qcMap = new TreeMap();
          getBillingItems(sess, RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY, labMap, qcMap);
          
          TreeMap agilent2ColorMap = new TreeMap();
          getBillingItems(sess, RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY, labMap, agilent2ColorMap);

          TreeMap agilent1ColorMap = new TreeMap();
          getBillingItems(sess, RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY, labMap, agilent1ColorMap);

          TreeMap affyMap = new TreeMap();
          getBillingItems(sess, RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY, labMap, affyMap);

       
          if (isValid()) {
            // set up the ReportTray
            tray = new ReportTray();
            tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
            tray.setReportTitle("Total Billing By Lab Report");
            tray.setReportDescription("Total Billing By Lab Report" + " " + this.formatDate(startDate) + " - " + this.formatDate(endDate));
            tray.setFileName("Microarry Total Billing By Lab");
            tray.setFormat(ReportFormats.XLS);
            
            Set columns = new TreeSet();
            columns.add(makeReportColumn("Lab", 1));
            columns.add(makeReportColumn("Illumina", 2));
            columns.add(makeReportColumn("%", 3));
            columns.add(makeReportColumn("", 4));
            columns.add(makeReportColumn("Microarray", 5));
            columns.add(makeReportColumn("%", 6));
            columns.add(makeReportColumn("", 7));
            columns.add(makeReportColumn("Sample Quality", 8));
            columns.add(makeReportColumn("%", 9));
            columns.add(makeReportColumn("", 10));
            columns.add(makeReportColumn("Total", 11));
            columns.add(makeReportColumn("%", 12));
            
            tray.setColumns(columns);
            
            for(Iterator i = labMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();
              
              BigDecimal totalPriceIllumina        = (BigDecimal)solexaMap.get(key);      
              BigDecimal totalPriceAgilent         = (BigDecimal)agilent2ColorMap.get(key);      
              BigDecimal totalPriceAgilent1Color   = (BigDecimal)agilent1ColorMap.get(key);      
              BigDecimal totalPriceAffy            = (BigDecimal)affyMap.get(key);      
              BigDecimal totalPriceQC              = (BigDecimal)qcMap.get(key);     
              
              BigDecimal totalMicroarray = new BigDecimal(0);
              totalMicroarray = totalMicroarray.add(totalPriceAgilent != null ? totalPriceAgilent : new BigDecimal(0));
              totalMicroarray = totalMicroarray.add(totalPriceAgilent1Color != null ? totalPriceAgilent1Color : new BigDecimal(0));
              totalMicroarray = totalMicroarray.add(totalPriceAffy != null ? totalPriceAffy : new BigDecimal(0));

              ReportRow reportRow = new ReportRow();
              List values  = new ArrayList();
              
              BigDecimal total = new BigDecimal(0);
              total = total.add(totalPriceIllumina != null ? totalPriceIllumina : new BigDecimal(0));
              total = total.add(totalMicroarray != null ? totalMicroarray : new BigDecimal(0));
              total = total.add(totalPriceQC != null ? totalPriceQC : new BigDecimal(0));

              BigDecimal illuminaPercent = totalPriceIllumina != null ? totalPriceIllumina.divide(grandTotalIllumina, 3, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
              BigDecimal microarrayPercent = totalMicroarray != null ? totalMicroarray.divide(grandTotalMicroarray, 3, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
              BigDecimal qcPercent = totalPriceQC != null ? totalPriceQC.divide(grandTotalQC, 3, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
              BigDecimal totalPercent = total != null ? total.divide(grandTotal, 3, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);


              values.add(key);
              values.add(totalPriceIllumina != null ? currencyFormat.format(totalPriceIllumina) : "");
              values.add(illuminaPercent.compareTo(zero) > 0 ? percentFormat.format(illuminaPercent) : "");
              values.add("");
              values.add(totalMicroarray.intValue() > 0 ? currencyFormat.format(totalMicroarray) : "");
              values.add(microarrayPercent.compareTo(zero) > 0 ? percentFormat.format(microarrayPercent) : "");
              values.add("");
              values.add(totalPriceQC != null ? currencyFormat.format(totalPriceQC) : "");
              values.add(qcPercent.compareTo(zero) > 0 ? percentFormat.format(qcPercent) : "");
              values.add("");
              values.add(total != null ? currencyFormat.format(total) : "");
              values.add(totalPercent.compareTo(zero) > 0 ? percentFormat.format(totalPercent) : "< 1%");
              
              reportRow.setValues(values);
              tray.addRow(reportRow);

            }
            ReportRow reportRow = new ReportRow();
            List values  = new ArrayList();

            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            values.add("");

            reportRow.setValues(values);
            tray.addRow(reportRow);

            reportRow = new ReportRow();
            values  = new ArrayList();

            values.add("Grand Total");
            values.add(grandTotalIllumina != null ? currencyFormat.format(grandTotalIllumina) : "");
            values.add("");
            values.add("");
            values.add(grandTotalMicroarray != null ? currencyFormat.format(grandTotalMicroarray) : "");
            values.add("");
            values.add("");
            values.add(grandTotalQC != null ? currencyFormat.format(grandTotalQC) : "");
            values.add("");
            values.add("");
            values.add(grandTotal != null ? currencyFormat.format(grandTotal) : "");
            values.add("");

            reportRow.setValues(values);
            tray.addRow(reportRow);

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
      log.error("An exception has occurred in ShowBillingTotalByLabReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowBillingTotalByLabReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowBillingTotalByLabReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowBillingTotalByLabReport ", e);
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
  
  private void getBillingItems(Session sess, String codeRequestCategory, Map labMap, Map map) throws Exception {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT lab.lastName, lab.firstName, req.codeRequestCategory, bi.totalPrice ");
    buf.append("FROM   Request req ");
    buf.append("JOIN   req.billingItems bi ");
    buf.append("JOIN   bi.lab as lab ");
    buf.append("JOIN   bi.billingPeriod as bp ");
    buf.append("WHERE  bp.startDate >= '" + this.formatDate(startDate) + "' ");
    buf.append("AND    bp.endDate <= '" + this.formatDate(endDate) + "' ");
    buf.append("AND    req.codeRequestCategory = '" + codeRequestCategory + "'");
    buf.append("ORDER BY lab.lastName, lab.firstName, req.codeRequestCategory ");
    
    List results = sess.createQuery(buf.toString()).list();
    
    fillMap(labMap, map, results);
    
  }
  
  
  private void fillMap(Map labMap, Map map, List results) {
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String labLastName         = (String)row[0];
      String labFirstName        = row[1] != null ? (String)row[1] : "";
      String codeRequestCategory = (String)row[2];
      BigDecimal totalPrice      = row[3] != null ? (BigDecimal)row[3] : new BigDecimal(0);
      
      String key = labLastName;
      if (!labFirstName.equals("")) {
        key += ", " + labFirstName;
      }

      BigDecimal total = (BigDecimal)map.get(key);
      if (total == null) {
        total = totalPrice;
      } else {
        total = total.add(totalPrice);
      }
      map.put(key, total);
      
      labMap.put(key, key);
      
      if (totalPrice != null) {
        grandTotal = grandTotal.add(totalPrice);
        
        if (codeRequestCategory.equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
          grandTotalIllumina = grandTotalIllumina.add(totalPrice);
        }
        
        if (codeRequestCategory.equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
          grandTotalQC = grandTotalQC.add(totalPrice);
        }
        
        if (RequestCategory.isMicroarrayRequestCategory(codeRequestCategory)) {
          grandTotalMicroarray = grandTotalMicroarray.add(totalPrice);
        }
        
      }
      
      
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