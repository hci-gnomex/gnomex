package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
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

import org.hibernate.Session;



public class ShowBillingGLInterface extends ReportCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowBillingGLInterface.class);


  private Integer                  idBillingPeriod;
  private Integer                  idCoreFacility;
  private BigDecimal               expectedGrandTotalPrice;
  private Integer                  revisionNumber = new Integer(1);
  private SecurityAdvisor          secAdvisor;


  private NumberFormat             currencyFormat = NumberFormat.getCurrencyInstance();

  private BigDecimal               totalPriceForLabAccount = new BigDecimal(0);
  private BigDecimal               totalPrice = new BigDecimal(0);

  private String                   accountDescription = "";

  private String                   journalId;
  private String                   journalLineRef;
  private String                   journalEntry;
  private String                   glHeaderFacility;        // Up to 19 characters
  private String                   glHeaderDescription;     // Up to 30 characters 



  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idBillingPeriod") != null) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }

    if (request.getParameter("idCoreFacility") != null) {
      idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
    } else {
      this.addInvalidField("idCoreFacility", "idCoreFacility is required");
    }


    if (request.getParameter("grandTotalPrice") != null ) {
      if (request.getParameter("grandTotalPrice").equals("")) {
        expectedGrandTotalPrice = new BigDecimal(0.00);
      } else {
        String grandTotalPrice = request.getParameter("grandTotalPrice");
        grandTotalPrice = grandTotalPrice.replaceAll("\\$", "");
        grandTotalPrice = grandTotalPrice.replaceAll(",", "");
        expectedGrandTotalPrice = new BigDecimal(grandTotalPrice);
      }
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

      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);

      journalId = pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_GL_JOURNAL_ID_CORE_FACILITY);
      journalLineRef = pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_GL_JOURNAL_LINE_REF_CORE_FACILITY);
      glHeaderFacility = pdh.getCoreFacilityProperty(idCoreFacility,  PropertyDictionary.BILLING_GL_HEADER_FACILITY);
      glHeaderDescription = pdh.getCoreFacilityProperty(idCoreFacility,  PropertyDictionary.BILLING_GL_HEADER_DESCRIPTION);
      String glHeaderCurrency = pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_GL_HEADER_CURRENCY);
      journalEntry = this.journalId + journalDateFormat.format(billingPeriod.getStartDate()) + revisionNumber.toString();
      String blankYearString = pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_GL_BLANK_YEAR);
      String fiscalYear;
      if (blankYearString != null && blankYearString.toUpperCase().equals("Y")) {
        fiscalYear = "";
      } else {
        fiscalYear = billingPeriod.getFiscalYear(pdh, idCoreFacility);
      }



      if (this.isValid()) { 
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 

          CoreFacility core = (CoreFacility)sess.load(CoreFacility.class, idCoreFacility);

          TreeMap billingItemMap = new TreeMap();

          addBillingItems(sess, "req", billingItemMap);  //Request
          addBillingItems(sess, "dsk", billingItemMap);  //Disk usage
          addBillingItems(sess, "po", billingItemMap);  //ProductOrder

          BillingAccount prevBillingAccount = null;
          String prevLabName = null;

          if (isValid()) {

            // set up the ReportTray
            tray = new ReportTray();
            tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
            tray.setReportTitle(billingPeriod.getBillingPeriod() + " " + core.getFacilityName() + " Chargeback - GL Interface");
            tray.setReportDescription(billingPeriod.getBillingPeriod() + " " + core.getFacilityName() + " Chargeback - GL Interface");
            tray.setFileName("umerge_" + periodFormat.format(billingPeriod.getStartDate()));
            tray.setFormat(ReportFormats.XLS);

            Set columns = new TreeSet();
            columns.add(makeReportColumn("", 1));

            tray.setColumns(columns);


            String prevLabBillingName = "XXX";


            //
            // The header row
            // 
            //   H01   jjjjjmmyy#MMDDYYYYACTUALS   NfffffffffffffffffffDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDUSDX
            //   H01   SE0900109101312009ACTUALS   N        HCI        Jan 09 HCI Microarray Billing USD
            //   H01   SE0420001005312013ACTUALS   N MC HEALTH SCIENCE RECHARGE CENTER               USDX 
            //
            //   H01        6 characters long (H01 followed by 3 spaces)
            //   jjjjj      Journal ID (filled from property BILLING_GL_JOURNAL_ID_CORE_FACILITY 
            //              (example SE090 for Microarray, SE042 for DNA SeqCore)
            //   mmyy       Starting month and year of billing period 
            //              example: 0109 is Jan 2009 this may be unused, other example shows '0001'
            //   #          Revision # (normally 0, but if GL interface has to be amended (more entries), 
            //              the revision number increments
            //   MMDDYYYY   Ending date of billing period (example: 01312009 for Jan 31, 2009)
            //   ACTUALS    10 characters long (ACTUALS followed by 3 spaces)
            //   N          1 character, always N
            //   ffff....   18 characters long, first character is space 
            //              space + facility name, filled from property BILLING_GL_HEADER_FACILITY
            //   DDDD.....  30 characters long, a title, 
            //              'MMM YY ' (7 characters) 
            //              followed by a description from property BILLING_GL_HEADER_DESCRIPTION (23 characters max)
            //   USD        3 characters, currency, always USD
            //   X          1 character, always X, probably optional
            // Example of header for DNA Seq Core:
            //
            String header = "H";
            header += getString("01", 5, true);
            header += getString(journalEntry, 10, true);
            header += dateFormat.format(billingPeriod.getEndDate());
            header += getString("ACTUALS", 10, true);
            header += "N";
            header += " " + getString(glHeaderFacility, 18, true);  // facility name, 19 chars long, first char is space
            header += getString(headerDateFormat.format(billingPeriod.getStartDate()) + " " + glHeaderDescription, 30, true);
            header += getString(glHeaderCurrency, 4, true);
            header += getEmptyString(4, true);
            header += getEmptyString(8, true);
            header += getEmptyString(16, true);

            String headerX = "H";
            headerX += getString("01", 5, false);
            headerX += getString(journalEntry, 10, false);
            headerX += dateFormat.format(billingPeriod.getEndDate());
            headerX += getString("ACTUALS", 10, false);
            headerX += "N";
            headerX += " " + getString(glHeaderFacility, 18, false);  // facility name, 19 chars long, first char is space
            headerX += getString(headerDateFormat.format(billingPeriod.getStartDate()) + " " + glHeaderDescription, 30, false);
            headerX += getString(glHeaderCurrency, 4, false);
            headerX += getEmptyString(4, false);
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

            for(Iterator i = billingItemMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();



              String[] tokens = key.split("_");
              String requestNumber = tokens[2];
              List billingItems = (List)billingItemMap.get(key);



              for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
                BillingItem bi = (BillingItem)i1.next();

                String acctNum = bi.getBillingAccount().getAccountNumber();
                String labName = bi.getLabName();

                String labBillingName = bi.getLabName() + acctNum;

                if (!firstTime && !labBillingName.equals(prevLabBillingName)) {
                  writeLabAccountDebit(prevLabName, prevBillingAccount, accountDescription, fiscalYear);
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
              accountDescription += requestNumber;


            }

          }

          if (billingItemMap.size() > 0) {
            writeLabAccountDebit(prevLabName, prevBillingAccount, accountDescription, fiscalYear);

            // Verify that grand total matches expected grand total
            if (this.totalPrice.compareTo(this.expectedGrandTotalPrice) != 0) {
              this.addInvalidField("UnexpectedTotal", "The GNomEx GL interface for " + 
                  billingPeriod.getBillingPeriod() + 
                  " could not be generated.  The total price $" + 
                  totalPrice + 
                  " does not match the expected total price of $" + 
                  expectedGrandTotalPrice + ".");
            }

            // Show the core facility credit for the total billing (internal customers)
            this.writeCoreFacilityCredit(billingPeriod, pdh, PropertyDictionary.BILLING_CORE_FACILITY_ACCOUNT, this.totalPrice, true, fiscalYear);    


            // Only show the debit and credit lines for manual billing on POs if there is a core facility property
            // for billing_core_facility_po_account.

            String propPO = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_CORE_FACILITY_PO_ACCOUNT);
            if (propPO != null && !propPO.equals("")) {
              // Get the total price for all external PO billing items
              StringBuffer buf = new StringBuffer();
              buf.append("SELECT sum(bi.invoicePrice) ");
              buf.append("FROM   BillingItem bi ");
              buf.append("JOIN   bi.lab as lab ");
              buf.append("JOIN   bi.billingAccount as ba ");
              buf.append("WHERE  (bi.codeBillingStatus = '" + BillingStatus.APPROVED_PO + "' ");
              buf.append("OR     bi.codeBillingStatus = '" + BillingStatus.APPROVED_CC + "') ");
              buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
              buf.append("AND    bi.idCoreFacility = " + idCoreFacility + " ");
              if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
                buf.append(" AND ");
                secAdvisor.appendCoreFacilityCriteria(buf, "bi");
                buf.append(" ");
              }
              List results = sess.createQuery(buf.toString()).list();
              if (results.size() > 0) {
                BigDecimal totalPriceExternalPO = (BigDecimal)results.get(0);
                if (totalPriceExternalPO != null) {

                  // Show the microarray debit for the total billing (customers billed from POs)
                  this.writeCoreFacilityCredit(billingPeriod, pdh, PropertyDictionary.BILLING_PO_ACCOUNT, totalPriceExternalPO, false, fiscalYear);            

                  // Show the microarray credit for the total billing (customers billed from POs)
                  this.writeCoreFacilityCredit(billingPeriod, pdh, PropertyDictionary.BILLING_CORE_FACILITY_PO_ACCOUNT, totalPriceExternalPO, true, fiscalYear);            
                }
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

  private void addBillingItems(Session sess, String mainObject, TreeMap billingItemMap) {
    StringBuffer buf = new StringBuffer();
    if (mainObject.equals("req")) {
      buf.append("SELECT req, bi ");
      buf.append("FROM   Request req ");
      buf.append("JOIN   req.billingItems bi ");
    } else if (mainObject.equals("dsk")) {
      buf.append("SELECT dsk, bi ");
      buf.append("FROM   DiskUsageByMonth dsk ");
      buf.append("JOIN   dsk.billingItems bi ");
    } else {
      buf.append("SELECT po, bi ");
      buf.append("FROM   ProductOrder po ");
      buf.append("JOIN   po.billingItems bi ");
    }
    buf.append("JOIN   bi.lab as lab ");
    buf.append("JOIN   bi.billingAccount as ba ");
    buf.append("WHERE  bi.codeBillingStatus = '" + BillingStatus.APPROVED + "' ");
    buf.append("AND    bi.idBillingPeriod = " + idBillingPeriod + " ");
    buf.append("AND    bi.idCoreFacility = " + idCoreFacility + " ");
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      buf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(buf, "bi");
      buf.append(" ");
    }
    if (mainObject.equals("req")) {
      buf.append("ORDER BY lab.lastName, lab.firstName, ba.accountName, req.number, bi.idBillingItem ");
    } else if (mainObject.equals("dsk")) {
      buf.append("ORDER BY lab.lastName, lab.firstName, ba.accountName, dsk.idDiskUsageByMonth, bi.idBillingItem ");
    } else {
      buf.append("ORDER BY lab.lastName, lab.firstName, ba.accountName, po.productOrderNumber, bi.idBillingItem ");
    }

    List results = sess.createQuery(buf.toString()).list();

    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      String number = "";
      Set allBillingItems = null;
      if (mainObject.equals("req")) {
        Request req    =  (Request)row[0];
        allBillingItems = req.getBillingItems();
        number = req.getNumber();
      } else if(mainObject.equals("dsk")) { 
        DiskUsageByMonth dsk = (DiskUsageByMonth)row[0];
        allBillingItems = dsk.getBillingItems();
        number = "Disk Usage";
      } else {
        ProductOrder po = (ProductOrder)row[0];
        allBillingItems = po.getBillingItems();
        number = po.getProductOrderNumber();
      }
      BillingItem bi =  (BillingItem)row[1];

      // Bypass PO billing accounts
      if (bi.getBillingAccount().getIsPO() != null && bi.getBillingAccount().getIsPO().equals("Y")) {
        continue;
      }


      // Exclude any requests that have billing items with status
      // other than status provided in parameter.
      boolean mixedStatus = false;
      for (Iterator i1 = allBillingItems.iterator(); i1.hasNext();) {
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
      number;

      List billingItems = (List)billingItemMap.get(key);
      if (billingItems == null) {
        billingItems = new ArrayList();
        billingItemMap.put(key, billingItems);
      }
      billingItems.add(bi);
    }
  }

  private void writeLabAccountDebit(String labName, BillingAccount billingAccount, String description, String fiscalYear) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();

    String amt = this.currencyFormat.format(this.totalPriceForLabAccount);
    amt = amt.replaceAll("\\.", "");
    amt = amt.replaceAll(",", "");
    amt = amt.replaceAll("\\$", "");

    // Let's put ... if description is truncated
    if (description.length() > 30) {
      int pos = description.lastIndexOf(",");
      if (pos > 26) {
        description = description.substring(0, 26);
        pos = description.lastIndexOf(",");
      }
      description = description.substring(0, pos) + "...";
    }

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
    values.add(getFixedWidthValue(fiscalYear, 4));

    values.add(getFixedWidthValue(billingAccount.getAccountNumberProject(), 15)); // project id
    values.add(getFixedWidthEmptyValue(3));     // statistics code
    values.add(getFixedWidthEmptyValue(5));  // affiliate
    values.add(getFixedWidthValue("USD", 3)); // transaction currency code
    values.add(getFixedWidthValueRightJustify(amt, 16)); // transaction monetary amount
    values.add(getFixedWidthEmptyValue(16)); //statistics amount (blank)
    values.add(getFixedWidthValue(this.journalLineRef, 10)); //journal line ref
    values.add(getFixedWidthValue(description, 30)); //journal line description
    values.add(getFixedWidthEmptyValue(5)); // foreign currency rate type (blank)
    values.add(getFixedWidthEmptyValue(16)); // foreign currency exchange rate (blank)
    values.add(getFixedWidthEmptyValue(16)); // base currency amount (blank)
    values.add(getFixedWidthEmptyValue(3)); 
    values.add(getFixedWidthEmptyValue(3));
    values.add(getFixedWidthEmptyValue(3)); 
    values.add(getFixedWidthEmptyValue(3));

    reportRow.setValues(values);
    tray.addRow(reportRow);

    totalPriceForLabAccount = new BigDecimal(0);
    accountDescription = ""; 

  }

  private void writeCoreFacilityCredit(BillingPeriod billingPeriod, PropertyDictionaryHelper pdh, String property_for_account, BigDecimal totalAmt, boolean isCredit, String fiscalYear) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();
    String year = billingPeriod.getBillingPeriod();

    String amt = this.currencyFormat.format(totalAmt);
    amt = amt.replaceAll("\\.", "");
    amt = amt.replaceAll(",", "");
    amt = amt.replaceAll("\\$", "");
    if (isCredit) {
      amt = "-" + amt;      
    }


    values.add(getFixedWidthValue("L", 1)); // record type
    values.add(getFixedWidthValue(pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_CORE_FACILITY_BUSINESS_UNIT), 5));  // business unit
    values.add(getFixedWidthEmptyValue(6)); // journal line number (blank)
    values.add(getFixedWidthValue(pdh.getCoreFacilityProperty(idCoreFacility, property_for_account), 6)); // account
    values.add(getFixedWidthValue(pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_CORE_FACILITY_FUND), 5)); // fund
    values.add(getFixedWidthValue(pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_CORE_FACILITY_ORG), 10)); // dept id
    values.add(getFixedWidthValue(pdh.getCoreFacilityProperty(idCoreFacility, PropertyDictionary.BILLING_CORE_FACILITY_ACTIVITY), 5)); //activity
    values.add(getFixedWidthEmptyValue(5));  // au (blank for credits)
    values.add(getFixedWidthValue(fiscalYear, 4));
    values.add(getFixedWidthEmptyValue(15)); // project id
    values.add(getFixedWidthEmptyValue(3));     // statistics code
    values.add(getFixedWidthEmptyValue(5));  // affiliate
    values.add(getFixedWidthValue("USD", 3)); // transaction currency code
    values.add(getFixedWidthValueRightJustify(amt, 16)); // transaction monetary amount
    values.add(getFixedWidthEmptyValue(16)); //statistics amount (blank)
    values.add(getFixedWidthValue(journalLineRef, 10)); //journal line ref
    values.add(getFixedWidthValue( journalLineRef + " " + billingPeriod.getBillingPeriod(), 30)); //journal line description
    values.add(getFixedWidthEmptyValue(5)); // foreign currency rate type (blank)
    values.add(getFixedWidthEmptyValue(16)); // foreign currency exchange rate (blank)
    values.add(getFixedWidthEmptyValue(16)); // base currency amount (blank)
    values.add(getFixedWidthEmptyValue(3));
    values.add(getFixedWidthEmptyValue(3));
    values.add(getFixedWidthEmptyValue(3));
    values.add(getFixedWidthEmptyValue(3));

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