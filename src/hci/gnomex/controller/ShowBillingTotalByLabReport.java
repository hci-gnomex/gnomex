package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Lab;
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
  private BigDecimal grandTotalCapSeq = new BigDecimal("0");
  private BigDecimal grandTotalMitSeq = new BigDecimal("0");
  private BigDecimal grandTotalFragAnal = new BigDecimal("0");
  private BigDecimal grandTotalCherryPick = new BigDecimal("0");
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
    percentFormat.setMinimumFractionDigits(2);
    percentFormat.setMaximumFractionDigits(2);
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

          TreeMap illuminaMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_ILLUMINA, labMap, illuminaMap);
          
          TreeMap qcMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_QC, labMap, qcMap);
          
          TreeMap microarrayMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_MICROARRAY, labMap, microarrayMap);
          
          TreeMap capSeqMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_CAP_SEQ, labMap, capSeqMap);
          
          TreeMap mitSeqMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_MITOCHONDRIAL_DLOOP, labMap, mitSeqMap);
          
          TreeMap fragAnalMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_FRAGMENT_ANALYSIS, labMap, fragAnalMap);
          
          TreeMap cherryPickMap = new TreeMap();
          getBillingItems(sess, RequestCategory.TYPE_CHERRY_PICKING, labMap, cherryPickMap);

       
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
            columns.add(makeReportColumn("CCSG Member", 2));
            
            if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
              columns.add(makeReportColumn("Illumina", 3));
              columns.add(makeReportColumn("%", 4));
              columns.add(makeReportColumn("", 5));
              columns.add(makeReportColumn("Microarray", 6));
              columns.add(makeReportColumn("%", 7));
              columns.add(makeReportColumn("", 8));
              columns.add(makeReportColumn("Sample Quality", 9));
              columns.add(makeReportColumn("%", 10));
              columns.add(makeReportColumn("", 11));
              columns.add(makeReportColumn("Capillary Sequencing", 12));
              columns.add(makeReportColumn("%", 13));
              columns.add(makeReportColumn("", 14));
              columns.add(makeReportColumn("Mitochondrial Sequencing", 15));
              columns.add(makeReportColumn("%", 16));
              columns.add(makeReportColumn("", 17));
              columns.add(makeReportColumn("Fragment Analysis", 18));
              columns.add(makeReportColumn("%", 19));
              columns.add(makeReportColumn("", 20));
              columns.add(makeReportColumn("Cherry Picking", 21));
              columns.add(makeReportColumn("%", 22));
              columns.add(makeReportColumn("", 23));
              columns.add(makeReportColumn("Total", 24));
              columns.add(makeReportColumn("%", 25));
              
            } else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
              columns.add(makeReportColumn("Capillary Sequencing", 3));
              columns.add(makeReportColumn("%", 4));
              columns.add(makeReportColumn("", 5));
              columns.add(makeReportColumn("Mitochondrial Sequencing", 6));
              columns.add(makeReportColumn("%", 7));
              columns.add(makeReportColumn("", 8));
              columns.add(makeReportColumn("Fragment Analysis", 9));
              columns.add(makeReportColumn("%", 10));
              columns.add(makeReportColumn("", 11));
              columns.add(makeReportColumn("Cherry Picking", 12));
              columns.add(makeReportColumn("%", 13));
              columns.add(makeReportColumn("", 14));
              columns.add(makeReportColumn("Total", 15));
              columns.add(makeReportColumn("%", 16));
              
            } else {
              columns.add(makeReportColumn("Illumina", 3));
              columns.add(makeReportColumn("%", 4));
              columns.add(makeReportColumn("", 5));
              columns.add(makeReportColumn("Microarray", 6));
              columns.add(makeReportColumn("%", 7));
              columns.add(makeReportColumn("", 8));
              columns.add(makeReportColumn("Sample Quality", 9));
              columns.add(makeReportColumn("%", 10));
              columns.add(makeReportColumn("", 11));
              columns.add(makeReportColumn("Total", 12));
              columns.add(makeReportColumn("%", 13));
              
            }
            
            tray.setColumns(columns);
            
            for(Iterator i = labMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();
              Lab lab = (Lab)labMap.get(key);
              
              BigDecimal totalPriceIllumina        = illuminaMap.containsKey(key) ? (BigDecimal)illuminaMap.get(key) : new BigDecimal(0);      
              BigDecimal totalPriceQC              = qcMap.containsKey(key) ? (BigDecimal)qcMap.get(key) : new BigDecimal(0);     
              BigDecimal totalPriceMicroarray      = microarrayMap.containsKey(key) ? (BigDecimal)microarrayMap.get(key) : new BigDecimal(0);      
              BigDecimal totalPriceCapSeq          = capSeqMap.containsKey(key) ? (BigDecimal)capSeqMap.get(key) : new BigDecimal(0);      
              BigDecimal totalPriceMitSeq          = mitSeqMap.containsKey(key) ? (BigDecimal)mitSeqMap.get(key) : new BigDecimal(0);      
              BigDecimal totalPriceFragAnal        = fragAnalMap.containsKey(key) ? (BigDecimal)fragAnalMap.get(key) : new BigDecimal(0);      
              BigDecimal totalPriceCherryPick      = cherryPickMap.containsKey(key) ? (BigDecimal)cherryPickMap.get(key): new BigDecimal(0);      
              

              ReportRow reportRow = new ReportRow();
              List<String> values  = new ArrayList();
              
              values.add(key);
              values.add(lab.getIsCcsgMember() != null && lab.getIsCcsgMember().equals("Y") ? "X" : "");

              
              BigDecimal total = new BigDecimal(0);
              if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
                total = total.add(totalPriceIllumina != null ? totalPriceIllumina : new BigDecimal(0));
                total = total.add(totalPriceMicroarray != null ? totalPriceMicroarray : new BigDecimal(0));
                total = total.add(totalPriceQC != null ? totalPriceQC : new BigDecimal(0));
                total = total.add(totalPriceCapSeq != null ? totalPriceCapSeq : new BigDecimal(0));
                total = total.add(totalPriceMitSeq != null ? totalPriceMitSeq : new BigDecimal(0));
                total = total.add(totalPriceFragAnal != null ? totalPriceFragAnal : new BigDecimal(0));
                total = total.add(totalPriceCherryPick != null ? totalPriceCherryPick : new BigDecimal(0));

                BigDecimal illuminaPercent = totalPriceIllumina != null && totalPriceIllumina.intValue() > 0 ? totalPriceIllumina.divide(grandTotalIllumina, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal microarrayPercent = totalPriceMicroarray != null && totalPriceMicroarray.intValue() > 0 ? totalPriceMicroarray.divide(grandTotalMicroarray, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal qcPercent = totalPriceQC != null && totalPriceQC.intValue() > 0 ? totalPriceQC.divide(grandTotalQC, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal capSeqPercent = totalPriceQC != null && totalPriceCapSeq.intValue() > 0 ? totalPriceCapSeq.divide(grandTotalCapSeq, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal mitSeqPercent = totalPriceQC != null && totalPriceMitSeq.intValue() > 0 ? totalPriceMitSeq.divide(grandTotalMitSeq, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal fragAnalPercent = totalPriceFragAnal != null && totalPriceFragAnal.intValue() > 0 ? totalPriceFragAnal.divide(grandTotalFragAnal, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal cherryPickPercent = totalPriceCherryPick != null && totalPriceCherryPick.intValue() > 0 ? totalPriceCherryPick.divide(grandTotalCherryPick, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal totalPercent = total != null && total.intValue() > 0 ? total.divide(grandTotal, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
               
                values.add(totalPriceIllumina != null ? currencyFormat.format(totalPriceIllumina) : "");
                values.add(illuminaPercent.compareTo(zero) > 0 ? percentFormat.format(illuminaPercent) : "");
                values.add("");
                values.add(totalPriceMicroarray.intValue() > 0 ? currencyFormat.format(totalPriceMicroarray) : "");
                values.add(microarrayPercent.compareTo(zero) > 0 ? percentFormat.format(microarrayPercent) : "");
                values.add("");
                values.add(totalPriceQC != null ? currencyFormat.format(totalPriceQC) : "");
                values.add(qcPercent.compareTo(zero) > 0 ? percentFormat.format(qcPercent) : "");
                values.add("");
                values.add(totalPriceCapSeq != null ? currencyFormat.format(totalPriceCapSeq) : "");
                values.add(capSeqPercent.compareTo(zero) > 0 ? percentFormat.format(capSeqPercent) : "");
                values.add("");
                values.add(totalPriceMitSeq != null ? currencyFormat.format(totalPriceMitSeq) : "");
                values.add(mitSeqPercent.compareTo(zero) > 0 ? percentFormat.format(mitSeqPercent) : "");
                values.add("");
                values.add(totalPriceFragAnal != null ? currencyFormat.format(totalPriceFragAnal) : "");
                values.add(fragAnalPercent.compareTo(zero) > 0 ? percentFormat.format(fragAnalPercent) : "");
                values.add("");
                values.add(totalPriceCherryPick != null ? currencyFormat.format(totalPriceCherryPick) : "");
                values.add(cherryPickPercent.compareTo(zero) > 0 ? percentFormat.format(cherryPickPercent) : "");
                values.add("");
                values.add(total != null ? currencyFormat.format(total) : "");
                values.add(totalPercent.compareTo(zero) > 0 ? percentFormat.format(totalPercent) : "< 1%");

              } else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
                total = total.add(totalPriceCapSeq != null ? totalPriceCapSeq : new BigDecimal(0));
                total = total.add(totalPriceMitSeq != null ? totalPriceMitSeq : new BigDecimal(0));
                total = total.add(totalPriceFragAnal != null ? totalPriceFragAnal : new BigDecimal(0));
                total = total.add(totalPriceCherryPick != null ? totalPriceCherryPick : new BigDecimal(0));

                
                BigDecimal capSeqPercent = totalPriceQC != null && totalPriceCapSeq.intValue() > 0 ? totalPriceCapSeq.divide(grandTotalCapSeq, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal mitSeqPercent = totalPriceQC != null && totalPriceMitSeq.intValue() > 0 ? totalPriceMitSeq.divide(grandTotalMitSeq, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal fragAnalPercent = totalPriceFragAnal != null && totalPriceFragAnal.intValue() > 0 ? totalPriceFragAnal.divide(grandTotalFragAnal, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal cherryPickPercent = totalPriceCherryPick != null && totalPriceCherryPick.intValue() > 0 ? totalPriceCherryPick.divide(grandTotalCherryPick, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal totalPercent = total != null && total.intValue() > 0 ? total.divide(grandTotal, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);

                values.add(totalPriceCapSeq != null ? currencyFormat.format(totalPriceCapSeq) : "");
                values.add(capSeqPercent.compareTo(zero) > 0 ? percentFormat.format(capSeqPercent) : "");
                values.add("");
                values.add(totalPriceMitSeq != null ? currencyFormat.format(totalPriceMitSeq) : "");
                values.add(mitSeqPercent.compareTo(zero) > 0 ? percentFormat.format(mitSeqPercent) : "");
                values.add("");
                values.add(totalPriceFragAnal != null ? currencyFormat.format(totalPriceFragAnal) : "");
                values.add(fragAnalPercent.compareTo(zero) > 0 ? percentFormat.format(fragAnalPercent) : "");
                values.add("");
                values.add(totalPriceCherryPick != null ? currencyFormat.format(totalPriceCherryPick) : "");
                values.add(cherryPickPercent.compareTo(zero) > 0 ? percentFormat.format(cherryPickPercent) : "");
                values.add("");
                values.add(total != null ? currencyFormat.format(total) : "");
                values.add(totalPercent.compareTo(zero) > 0 ? percentFormat.format(totalPercent) : "< 1%");

              } else {
                total = total.add(totalPriceIllumina != null ? totalPriceIllumina : new BigDecimal(0));
                total = total.add(totalPriceMicroarray != null ? totalPriceMicroarray : new BigDecimal(0));
                total = total.add(totalPriceQC != null ? totalPriceQC : new BigDecimal(0));

                BigDecimal illuminaPercent = totalPriceIllumina != null && totalPriceIllumina.intValue() > 0 ? totalPriceIllumina.divide(grandTotalIllumina, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal microarrayPercent = totalPriceMicroarray != null && totalPriceMicroarray.intValue() > 0 ? totalPriceMicroarray.divide(grandTotalMicroarray, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal qcPercent = totalPriceQC != null && totalPriceQC.intValue() > 0 ? totalPriceQC.divide(grandTotalQC, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                BigDecimal totalPercent = total != null && total.intValue() > 0 ? total.divide(grandTotal, 4, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
                
                values.add(totalPriceIllumina != null ? currencyFormat.format(totalPriceIllumina) : "");
                values.add(illuminaPercent.compareTo(zero) > 0 ? percentFormat.format(illuminaPercent) : "");
                values.add("");
                values.add(totalPriceMicroarray.intValue() > 0 ? currencyFormat.format(totalPriceMicroarray) : "");
                values.add(microarrayPercent.compareTo(zero) > 0 ? percentFormat.format(microarrayPercent) : "");
                values.add("");
                values.add(totalPriceQC != null ? currencyFormat.format(totalPriceQC) : "");
                values.add(qcPercent.compareTo(zero) > 0 ? percentFormat.format(qcPercent) : "");
                values.add("");
                values.add(total != null ? currencyFormat.format(total) : "");
                values.add(totalPercent.compareTo(zero) > 0 ? percentFormat.format(totalPercent) : "< 1%");

              }

              reportRow.setValues(values);
              tray.addRow(reportRow);

            }
            ReportRow reportRow = new ReportRow();
            List values  = new ArrayList();

            if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
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
              values.add("");
              
            }else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
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
              values.add("");
              values.add("");
              
              values.add("");
              values.add("");
              
            }else {
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
              values.add("");
              
            }

            reportRow.setValues(values);
            tray.addRow(reportRow);

            reportRow = new ReportRow();
            values  = new ArrayList();

            
            if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
              values.add("Grand Total");
              values.add("");
              values.add(grandTotalIllumina != null ? currencyFormat.format(grandTotalIllumina) : "");
              values.add("");
              values.add("");
              values.add(grandTotalMicroarray != null ? currencyFormat.format(grandTotalMicroarray) : "");
              values.add("");
              values.add("");
              values.add(grandTotalQC != null ? currencyFormat.format(grandTotalQC) : "");
              values.add("");
              values.add("");
              values.add(grandTotalCapSeq != null ? currencyFormat.format(grandTotalCapSeq) : "");
              values.add("");
              values.add("");
              values.add(grandTotalMitSeq != null ? currencyFormat.format(grandTotalMitSeq) : "");
              values.add("");
              values.add("");
              values.add(grandTotalFragAnal != null ? currencyFormat.format(grandTotalFragAnal) : "");
              values.add("");
              values.add("");
              values.add(grandTotalCherryPick != null ? currencyFormat.format(grandTotalCherryPick) : "");
              values.add("");
              values.add("");
              values.add(grandTotal != null ? currencyFormat.format(grandTotal) : "");
              values.add("");
              
            }else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
              values.add("Grand Total");
              values.add("");
              values.add(grandTotalCapSeq != null ? currencyFormat.format(grandTotalCapSeq) : "");
              values.add("");
              values.add("");
              values.add(grandTotalMitSeq != null ? currencyFormat.format(grandTotalMitSeq) : "");
              values.add("");
              values.add("");
              values.add(grandTotalFragAnal != null ? currencyFormat.format(grandTotalFragAnal) : "");
              values.add("");
              values.add("");
              values.add(grandTotalCherryPick != null ? currencyFormat.format(grandTotalCherryPick) : "");
              values.add("");
              values.add("");
              values.add(grandTotal != null ? currencyFormat.format(grandTotal) : "");
              values.add("");

            }else {
              values.add("Grand Total");
              values.add("");
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
              
            }


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
  
  private void getBillingItems(Session sess, String requestCategoryType, Map labMap, Map map) throws Exception {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT lab, rc, bi.invoicePrice ");
    buf.append("FROM   Request req ");
    buf.append("JOIN   req.requestCategory as rc ");
    buf.append("JOIN   req.billingItems bi ");
    buf.append("JOIN   bi.lab as lab ");
    buf.append("JOIN   bi.billingPeriod as bp ");
    buf.append("WHERE  bp.startDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    buf.append("AND    bp.endDate <= '" + this.formatDate(endDate, this.DATE_OUTPUT_SQL) + "' ");
    buf.append("AND    rc.type = '" + requestCategoryType + "'");
  
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }
    
    buf.append("ORDER BY lab.lastName, lab.firstName ");
    
    List results = sess.createQuery(buf.toString()).list();
    
    fillMap(labMap, map, results);
    
  }
  
  
  
  private void fillMap(Map labMap, Map map, List results) {
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Lab    lab                 = (Lab)row[0];
      String labLastName         = lab.getLastName() != null ? lab.getLastName() : "";
      String labFirstName        = lab.getFirstName() != null ? lab.getFirstName() : "";
      RequestCategory requestCategory = (RequestCategory)row[1];
      BigDecimal totalPrice      = row[2] != null ? (BigDecimal)row[2] : new BigDecimal(0);
      
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
      
      labMap.put(key, lab);
      
      if (totalPrice != null) {
        grandTotal = grandTotal.add(totalPrice);
        
        if (requestCategory.getType().equals(RequestCategory.TYPE_ILLUMINA)) {
          grandTotalIllumina = grandTotalIllumina.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_QC)) {
          grandTotalQC = grandTotalQC.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_MICROARRAY)) {
          grandTotalMicroarray = grandTotalMicroarray.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_CAP_SEQ)) {
          grandTotalCapSeq = grandTotalCapSeq.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_MITOCHONDRIAL_DLOOP)) {
          grandTotalMitSeq = grandTotalMitSeq.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_FRAGMENT_ANALYSIS)) {
          grandTotalFragAnal = grandTotalFragAnal.add(totalPrice);
        }else if (requestCategory.getType().equals(RequestCategory.TYPE_CHERRY_PICKING)) {
          grandTotalCherryPick = grandTotalCherryPick.add(totalPrice);
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