package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.PropertyDictionary;
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
import java.text.SimpleDateFormat;
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
import javax.swing.text.DateFormatter;

import org.hibernate.Session;



public class ShowBillingGLInterface extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowBillingGLInterface.class);
  
  
  private Integer                  idBillingPeriod;
  private BigDecimal               expectedGrandTotalPrice;
  private Integer                  revisionNumber = new Integer(1);
  private SecurityAdvisor          secAdvisor;
  
  
  private NumberFormat             currencyFormat = NumberFormat.getCurrencyInstance();
  
  private BigDecimal               totalPriceForLabAccount = new BigDecimal(0);
  private BigDecimal               totalPrice = new BigDecimal(0);
  
  private String                   accountDescription = "";
  
  private static final String    JOURNAL_ID = "SE090";
  private static final String    JOURNAL_LINE_REF = "MICROARRAY";
  private String                   journalEntry;
  
  
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idBillingPeriod") != null) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }

    if (request.getParameter("grandTotalPrice") != null) {
      String grandTotalPrice = request.getParameter("grandTotalPrice");
      grandTotalPrice = grandTotalPrice.replaceAll("\\$", "");
      grandTotalPrice = grandTotalPrice.replaceAll(",", "");
      expectedGrandTotalPrice = new BigDecimal(grandTotalPrice);
    } else {
      this.addInvalidField("grandTotalPrice", "grandTotalPrice is required");
    }
    
    if (request.getParameter("revisionNumber") != null && !request.getParameter("revisionNumber").equals("")) {
      revisionNumber = new Integer(request.getParameter("revisionNumber"));
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
    this.SUCCESS_JSP_XLS = "/report_xls_gl_interface.jsp";
    this.ERROR_JSP = "/messageHTML.jsp";
    
    SimpleDateFormat headerDateFormat = new SimpleDateFormat("MMM yy");
    SimpleDateFormat periodFormat = new SimpleDateFormat("MMMyy");
    SimpleDateFormat dateFormat  = new SimpleDateFormat("MMddyyyy");
    SimpleDateFormat journalDateFormat  = new SimpleDateFormat("MMyy");
    
   
    try {
      
      
   
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      
     
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);
      journalEntry = this.JOURNAL_ID + journalDateFormat.format(billingPeriod.getStartDate()) + revisionNumber.toString();

      if (this.isValid()) { 
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 
          

          StringBuffer buf = new StringBuffer();
          buf.append("SELECT req, bi ");
          buf.append("FROM   Request req ");
          buf.append("JOIN   req.billingItems bi ");
          buf.append("JOIN   bi.lab as lab ");
          buf.append("JOIN   bi.billingAccount as ba ");
          buf.append("WHERE  bi.codeBillingStatus = '" + BillingStatus.APPROVED + "' ");
          buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
          if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
            buf.append(" AND ");
            secAdvisor.appendCoreFacilityCriteria(buf, "bi");
            buf.append(" ");
          }
          buf.append("ORDER BY lab.lastName, lab.firstName, ba.accountName, req.number, bi.idBillingItem ");
          
          List results = sess.createQuery(buf.toString()).list();
          TreeMap requestMap = new TreeMap();
          TreeMap billingItemMap = new TreeMap();
          
          for(Iterator i = results.iterator(); i.hasNext();) {
            Object[] row = (Object[])i.next();
            Request req    =  (Request)row[0];
            BillingItem bi =  (BillingItem)row[1];
            
            // Bypass PO billing accounts
            if (bi.getBillingAccount().getIsPO() != null && bi.getBillingAccount().getIsPO().equals("Y")) {
              continue;
            }
            
            
            // Exclude any requests that have billing items with status
            // other than status provided in parameter.
            boolean mixedStatus = false;
            for (Iterator i1 = req.getBillingItems().iterator(); i1.hasNext();) {
              BillingItem item = (BillingItem)i1.next();
              if (item.getIdBillingPeriod().equals(idBillingPeriod) &&
                  item.getIdBillingAccount().equals(bi.getIdBillingAccount())) {
                if (!item.getCodeBillingStatus().equals(BillingStatus.APPROVED)) {
                  mixedStatus = true;
                }
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
          
          BillingAccount prevBillingAccount = null;
          String prevLabName = null;
       
          if (isValid()) {
            
            // set up the ReportTray
            tray = new ReportTray();
            tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
            tray.setReportTitle(billingPeriod.getBillingPeriod() + " Microarray Chargeback - GL Interface");
            tray.setReportDescription(billingPeriod.getBillingPeriod() + " Microarray Chargeback - GL Interface");
            tray.setFileName("umerge_microarray_" + periodFormat.format(billingPeriod.getStartDate()));
            tray.setFormat(ReportFormats.XLS);
            
            Set columns = new TreeSet();
            columns.add(makeReportColumn("", 1));
            
            tray.setColumns(columns);

            
            String prevLabBillingName = "XXX";
           
            
            // Example of header:
            // H01   SE0900109101312009ACTUALS   N        HCI        Jan 09 HCI Microarray Billing USD
            //
            String header = "H";
            header += getString("01", 5, true);
            header += getString(journalEntry, 10, true);
            header += dateFormat.format(billingPeriod.getEndDate());
            header += getString("ACTUALS", 10, true);
            header += "N";
            header += getEmptyString(8, true);
            header += "HCI";
            header += getEmptyString(8, true);
            header += getString(headerDateFormat.format(billingPeriod.getStartDate()) + " HCI Microarray Billing", 30, true);
            header += "USD";
            header += getEmptyString(5, true);
            header += getEmptyString(8, true);
            header += getEmptyString(16, true);
            
            String headerX = "H";
            headerX += getString("01", 5, false);
            headerX += getString(journalEntry, 10, false);
            headerX += dateFormat.format(billingPeriod.getEndDate());
            headerX += getString("ACTUALS", 10, false);
            headerX += "N";
            headerX += getEmptyString(8, false);
            headerX += "HCI";
            headerX += getEmptyString(8, false);
            headerX += getString(headerDateFormat.format(billingPeriod.getStartDate()) + " HCI Microarray Billing", 30, false);
            headerX += "USD";
            headerX += getEmptyString(5, false);
            headerX += getEmptyString(8, false);
            headerX += getEmptyString(16, false);
            
            ReportRow reportRow = new ReportRow();
            List values  = new ArrayList();
            
            ArrayList valueInfo = new ArrayList();
            valueInfo.add(header);
            valueInfo.add(headerX);
            valueInfo.add(new Integer(headerX.length()));
            valueInfo.add("left");
            values.add(valueInfo.toArray());
            
            reportRow.setValues(values);
            tray.addRow(reportRow);
            
            
            
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
                String labName = bi.getLabName();
                
                String labBillingName = bi.getLabName() + acctNum;

                if (!firstTime && !labBillingName.equals(prevLabBillingName)) {
                  addAccountTotalRows(prevLabName, prevBillingAccount, accountDescription);
                }

              
   

                if (bi.getInvoicePrice() != null) {
                  totalPriceForLabAccount = totalPriceForLabAccount.add(bi.getInvoicePrice());          
                  totalPrice              = totalPrice.add(bi.getInvoicePrice());          
                }
                
                firstTime = false;
                prevBillingAccount = bi.getBillingAccount();
                prevLabName = labName;
                prevLabBillingName = labBillingName;
              }

              if (accountDescription.length() > 0) {
                accountDescription += ",";
              }
              accountDescription += request.getNumber();

            
            }

          }

          if (requestMap.size() > 0) {
            addAccountTotalRows(prevLabName, prevBillingAccount, accountDescription);
            
            // Verify that grand total matches expected grand total
            if (!this.totalPrice.equals(this.expectedGrandTotalPrice)) {
              this.addInvalidField("UnexpectedTotal", "The GNomEx GL interface for " + 
                  billingPeriod.getBillingPeriod() + 
                  " could not be generated.  The total price $" + 
                  totalPrice + 
                  " does not match the expected total price of $" + 
                  expectedGrandTotalPrice + ".");
            }
            
            // Show the microarray credit for the total billing (internal customers)
            this.addMicroarrayTotal(billingPeriod, dh, PropertyDictionary.BILLING_CORE_FACILITY_ACCOUNT, this.totalPrice, true);    
            
            // Get the total price for all external PO billing items
            buf = new StringBuffer();
            buf.append("SELECT sum(bi.invoicePrice) ");
            buf.append("FROM   Request req ");
            buf.append("JOIN   req.billingItems bi ");
            buf.append("JOIN   bi.lab as lab ");
            buf.append("JOIN   bi.billingAccount as ba ");
            buf.append("WHERE  bi.codeBillingStatus = '" + BillingStatus.APPROVED_PO + "' ");
            buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
            results = sess.createQuery(buf.toString()).list();
            if (results.size() > 0) {
              BigDecimal totalPriceExternalPO = (BigDecimal)results.get(0);
              if (totalPriceExternalPO != null) {
                
                // Show the microarray debit for the total billing (customers billed from POs)
                this.addMicroarrayTotal(billingPeriod, dh, PropertyDictionary.BILLING_PO_ACCOUNT, totalPriceExternalPO, false);            
                
                // Show the microarray credit for the total billing (customers billed from POs)
                this.addMicroarrayTotal(billingPeriod, dh, PropertyDictionary.BILLING_CORE_FACILITY_PO_ACCOUNT, totalPriceExternalPO, true);            
              }

              

            }
 

          }
          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to generate GL Interface.");
        }
        
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowBillingGLInterface ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowBillingGLInterface ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowBillingGLInterface ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowBillingGLInterface ", e);
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
  

  
  private void addAccountTotalRows(String labName, BillingAccount billingAccount, String description) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();

    
    String amt = this.currencyFormat.format(this.totalPriceForLabAccount);
    amt = amt.replaceAll("\\.", "");
    amt = amt.replaceAll(",", "");
    amt = amt.replaceAll("\\$", "");
   
    
    values.add(getFixedWidthValue("L", 1)); // record type
    values.add(getFixedWidthValue(billingAccount.getAccountNumberBus(), 5));  // business unit
    values.add(getFixedWidthEmptyValue(6)); // journal line number (blank)
    values.add(getFixedWidthValue(billingAccount.getAccountNumberAccount(), 6)); // account
    values.add(getFixedWidthValue(billingAccount.getAccountNumberFund(), 5)); // fund
    values.add(getFixedWidthValue(billingAccount.getAccountNumberOrg(), 10)); // dept id
    values.add(getFixedWidthValue(billingAccount.getAccountNumberActivity(), 5)); //activity
    
    if (billingAccount.getAccountNumberActivity() != null && !billingAccount.getAccountNumberActivity().equals("")) {
      values.add(getFixedWidthValue("1", 5));  // au = "1" when project is being charged      
    } else {
      values.add(getFixedWidthEmptyValue(5));  // au blank when project is charged
    }
    values.add(getFixedWidthEmptyValue(4)); // budget year (blank)
    values.add(getFixedWidthValue(billingAccount.getAccountNumberProject(), 15)); // project id
    values.add(getFixedWidthEmptyValue(3));     // statistics code
    values.add(getFixedWidthEmptyValue(5));  // affiliate
    values.add(getFixedWidthValue("USD", 3)); // transaction currency code
    values.add(getFixedWidthValueRightJustify(amt, 16)); // transaction monetary amount
    values.add(getFixedWidthEmptyValue(16)); //statistics amount (blank)
    values.add(getFixedWidthValue(this.JOURNAL_LINE_REF, 10)); //journal line ref
    values.add(getFixedWidthValue(description, 30)); //journal line description
    values.add(getFixedWidthEmptyValue(5)); // foreign currency rate type (blank)
    values.add(getFixedWidthEmptyValue(16)); // foreign currency exchange rate (blank)
    values.add(getFixedWidthEmptyValue(16)); // base currency amount (blank)
    
    reportRow.setValues(values);
    tray.addRow(reportRow);
    
    totalPriceForLabAccount = new BigDecimal(0);
    accountDescription = ""; 
    
  }
  
  private void addMicroarrayTotal(BillingPeriod billingPeriod, DictionaryHelper dh, String account, BigDecimal totalAmt, boolean isCredit) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();

    
    String amt = this.currencyFormat.format(totalAmt);
    amt = amt.replaceAll("\\.", "");
    amt = amt.replaceAll(",", "");
    amt = amt.replaceAll("\\$", "");
    if (isCredit) {
      amt = "-" + amt;      
    }
   
    
    values.add(getFixedWidthValue("L", 1)); // record type
    values.add(getFixedWidthValue(dh.getPropertyDictionary(PropertyDictionary.BILLING_CORE_FACILITY_BUSINESS_UNIT), 5));  // business unit
    values.add(getFixedWidthEmptyValue(6)); // journal line number (blank)
    values.add(getFixedWidthValue(dh.getPropertyDictionary(account), 6)); // account
    values.add(getFixedWidthValue(dh.getPropertyDictionary(PropertyDictionary.BILLING_CORE_FACILITY_FUND), 5)); // fund
    values.add(getFixedWidthValue(dh.getPropertyDictionary(PropertyDictionary.BILLING_CORE_FACILITY_ORG), 10)); // dept id
    values.add(getFixedWidthValue(dh.getPropertyDictionary(PropertyDictionary.BILLING_CORE_FACILITY_ACTIVITY), 5)); //activity
    values.add(getFixedWidthEmptyValue(5));  // au (blank for credits)
    values.add(getFixedWidthEmptyValue(4)); // budget year (blank)
    values.add(getFixedWidthEmptyValue(15)); // project id
    values.add(getFixedWidthEmptyValue(3));     // statistics code
    values.add(getFixedWidthEmptyValue(5));  // affiliate
    values.add(getFixedWidthValue("USD", 3)); // transaction currency code
    values.add(getFixedWidthValueRightJustify(amt, 16)); // transaction monetary amount
    values.add(getFixedWidthEmptyValue(16)); //statistics amount (blank)
    values.add(getFixedWidthValue(JOURNAL_LINE_REF, 10)); //journal line ref
    values.add(getFixedWidthValue("HCI Microarray " + billingPeriod.getBillingPeriod(), 30)); //journal line description
    values.add(getFixedWidthEmptyValue(5)); // foreign currency rate type (blank)
    values.add(getFixedWidthEmptyValue(16)); // foreign currency exchange rate (blank)
    values.add(getFixedWidthEmptyValue(16)); // base currency amount (blank)
    
    reportRow.setValues(values);
    tray.addRow(reportRow);
    

    
  }
  private Object[] getFixedWidthValue(String buf, int len) {
    ArrayList valueInfo = new ArrayList();
    valueInfo.add(getString(buf, len, true));
    valueInfo.add(getString(buf, len, false));
    valueInfo.add(new Integer(len));
    valueInfo.add("left");
    return valueInfo.toArray();
  }
  
  private Object[] getFixedWidthValueRightJustify(String buf, int len) {
    ArrayList valueInfo = new ArrayList();
    valueInfo.add(getStringRightJustify(buf, len, true));
    valueInfo.add(getStringRightJustify(buf, len, false));
    valueInfo.add(new Integer(len));
    valueInfo.add("right");
    return valueInfo.toArray();
  }
  
  private Object[] getFixedWidthEmptyValue(int len) {
    ArrayList valueInfo = new ArrayList();
    valueInfo.add(getEmptyString(len, true));
    valueInfo.add(getEmptyString(len, false));
    valueInfo.add(new Integer(len));
    valueInfo.add("left");
    return valueInfo.toArray();
  }
  
  private String getString(String buf, int len, boolean showSpan) {
    if (buf == null) {
      return getEmptyString(len, showSpan);
    } else if (buf.length() == len) {
      return buf;
    } else if (buf.length() > len) {
      return buf.substring(0, len);      
    } else {
      int stringLen = buf.length();
      if (showSpan) {
        buf += "<span style='mso-spacerun:yes'>";        
      }
      for(int x = stringLen; x < len; x++) {
        buf += " ";
      }
      if (showSpan) {
        buf += "</span>";
      }
      return buf;
    }
  }
  


  private String getStringRightJustify(String buf, int len, boolean showSpan) {
    if (buf == null) {
      return getEmptyString(len, showSpan);
    } else if (buf.length() == len) {
      return buf;
    } else if (buf.length() > len) {
      return buf.substring(0, len);      
    } else {
      int stringLen = buf.length();
      if (showSpan) {
        buf += "<span style='mso-spacerun:yes'>";        
      }
      for(int x = stringLen; x < len; x++) {
        buf = " " + buf;
      }
      if (showSpan) {
        buf += "</span>";
      }
      return buf;
    }
  }
  
  private String getEmptyString(int len, boolean showSpan) {
    String buf = new String();
    if (showSpan) {
      buf += "<span style='mso-spacerun:yes'>";      
    }
  
    for(int x = 0; x < len; x++) {
      buf += " ";
    }
    
    if (showSpan) {
      buf += "</span>";      
    }
    return buf;
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