package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Request;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class ShowBillingMonthendReport extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowBillingMonthendReport.class);
  
  
  private Integer          idBillingPeriod;
  private SecurityAdvisor  secAdvisor;
  
  private NumberFormat   currencyFormat = NumberFormat.getCurrencyInstance();
  
  private BigDecimal totalPriceForRequest = new BigDecimal("0");
  private BigDecimal totalPriceForLabAccount = new BigDecimal("0");
  private BigDecimal grandTotalPrice = new BigDecimal("0");

  
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idBillingPeriod") != null) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
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
          
          BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);

          StringBuffer buf = new StringBuffer();
          buf.append("SELECT req, bi ");
          buf.append("FROM   Request req ");
          buf.append("JOIN   req.billingItems bi ");
          buf.append("JOIN   bi.lab as lab ");
          buf.append("JOIN   bi.billingAccount as ba ");
          buf.append("WHERE  bi.codeBillingStatus = '" + BillingStatus.APPROVED + "' ");
          buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
          buf.append("ORDER BY lab.name, ba.accountName, req.number, bi.idBillingItem ");
          
          List results = sess.createQuery(buf.toString()).list();
          TreeMap requestMap = new TreeMap();
          TreeMap billingItemMap = new TreeMap();
          
          for(Iterator i = results.iterator(); i.hasNext();) {
            Object[] row = (Object[])i.next();
            Request req    =  (Request)row[0];
            BillingItem bi =  (BillingItem)row[1];
            
            // Exclude any requests that have billing items with status
            // other than status provided in parameter.
            boolean mixedStatus = false;
            for (Iterator i1 = req.getBillingItems().iterator(); i1.hasNext();) {
              BillingItem item = (BillingItem)i1.next();
              if (!item.getCodeBillingStatus().equals(BillingStatus.APPROVED)) {
                mixedStatus = true;
              }
            }
            if (mixedStatus) {
              continue;
            }
            
            String key = bi.getLabName() + "_" +
                         bi.getBillingAccount().getIdBillingAccount() +  "_" +
                         req.getNumber();
            
            requestMap.put(key, req);
            
            List billingItems = (List)billingItemMap.get(key);
            if (billingItems == null) {
              billingItems = new ArrayList();
              billingItemMap.put(key, billingItems);
            }
            billingItems.add(bi);
          }
          

       
          if (isValid()) {
            // set up the ReportTray
            tray = new ReportTray();
            tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
            tray.setReportTitle(billingPeriod.getBillingPeriod() + " Microarray Chargeback");
            tray.setReportDescription(billingPeriod.getBillingPeriod() + " Microarray Chargeback");
            tray.setFileName("MicroarryBillingSummary_" + billingPeriod.getBillingPeriod());
            tray.setFormat(ReportFormats.XLS);
            
            Set columns = new TreeSet();
            columns.add(makeReportColumn("Lab", 1));
            columns.add(makeReportColumn("Account Number", 2));
            columns.add(makeReportColumn("Account Name", 3));
            columns.add(makeReportColumn("Date", 4));
            columns.add(makeReportColumn("Req ID", 5));
            columns.add(makeReportColumn("Client", 6));
            columns.add(makeReportColumn("Service", 7));
            columns.add(makeReportColumn("Product", 8));
            columns.add(makeReportColumn("Category", 9));
            columns.add(makeReportColumn("Description", 10));
            columns.add(makeReportColumn("Notes", 11));
            columns.add(makeReportColumn("Percent", 12));
            columns.add(makeReportColumn("Qty", 13));
            columns.add(makeReportColumn("Unit Price", 14));
            columns.add(makeReportColumn("Total Price", 15));
            
            tray.setColumns(columns);
            
            String prevRequestNumber = "XXX";
            String prevLabBillingName = "XXX";
            

            
            boolean firstTime = true;
            
            for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();
              Request request = (Request)requestMap.get(key);      

              String[] tokens = key.split("_");
              String requestNumber = tokens[2];
              List billingItems = (List)billingItemMap.get(key);
              
              for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
                BillingItem bi = (BillingItem)i1.next();

                String acctNum = bi.getBillingAccount().getAccountNumber();
                String acctName = bi.getBillingAccount().getAccountName();
                
                String labBillingName = bi.getLabName() + acctNum;

                
                if (!firstTime && !requestNumber.equals(prevRequestNumber)) {
                  addRequestTotalRows();
                } 
                if (!firstTime && !labBillingName.equals(prevLabBillingName)) {
                  addAccountTotalRows();
                }

                
                ReportRow reportRow = new ReportRow();
                List values  = new ArrayList();
            
               
                values.add(bi.getLabName());
                values.add(acctNum);
                values.add(acctName);
                values.add(this.formatDate(request.getCreateDate(), this.DATE_OUTPUT_SLASH));
                values.add(request.getNumber());
                values.add(request.getAppUser().getFirstName() + " " + request.getAppUser().getLastName());
                values.add(bi.getCodeBillingChargeKind().equals(BillingChargeKind.SERVICE) ? "X" : "");
                values.add(bi.getCodeBillingChargeKind().equals(BillingChargeKind.PRODUCT) ? "X" : "");
                values.add(bi.getCategory());
                values.add(bi.getDescription());
                values.add(bi.getNotes() != null? bi.getNotes() : "");
                values.add(bi.getPercentageDisplay());
                values.add(bi.getQty() != null ? bi.getQty().toString() : "");
                values.add(bi.getUnitPrice() != null ? currencyFormat.format(bi.getUnitPrice()) : "");
                values.add(bi.getTotalPrice() != null ? currencyFormat.format(bi.getTotalPrice()) : "");
               
                reportRow.setValues(values);
                tray.addRow(reportRow);
                

                if (bi.getTotalPrice() != null) {
                  totalPriceForRequest = totalPriceForRequest.add(bi.getTotalPrice());          
                  totalPriceForLabAccount = totalPriceForLabAccount.add(bi.getTotalPrice());          
                  grandTotalPrice = grandTotalPrice.add(bi.getTotalPrice());          
                }
                
                firstTime = false;
                prevRequestNumber = requestNumber;
                prevLabBillingName = labBillingName;
              }
            }

          }
          addRequestTotalRows();
          addAccountTotalRows();
          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show flow cell report.");
        }
        
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowBillingMonthendReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowBillingMonthendReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowBillingMonthendReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowBillingMonthendReport ", e);
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
  
  private void addRequestTotalRows() {
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
    values.add("");
    values.add("");
    values.add(currencyFormat.format(totalPriceForRequest));
    
    reportRow.setValues(values);
    tray.addRow(makeTotalBlankRow());
    tray.addRow(reportRow);
    tray.addRow(makeBlankRow());
    totalPriceForRequest = new BigDecimal(0);
    
  }
  
  private void addAccountTotalRows() {
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
    values.add("Account Total");
    values.add("");
    values.add(currencyFormat.format(totalPriceForLabAccount));
    
    reportRow.setValues(values);
    tray.addRow(makeTotalBlankRow());
    tray.addRow(reportRow);
    tray.addRow(makeBlankRow());
    tray.addRow(makeBlankRow());
    tray.addRow(makeBlankRow());
    tray.addRow(makeBlankRow());
    totalPriceForLabAccount = new BigDecimal(0);
    
  }
  
  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }
  
  private ReportRow makeBlankRow() {
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
    values.add("");
    values.add("");
    values.add("");

    reportRow.setValues(values);
    return reportRow;
  }

  private ReportRow makeTotalBlankRow() {
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
    values.add("");
    values.add("");
    values.add("=========");

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