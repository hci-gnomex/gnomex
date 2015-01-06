package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.controller.ShowBillingInvoiceForm;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Request;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;


public class BillingInvoiceHTMLFormatter  extends DetailObject {

  private BillingPeriod  billingPeriod;
  private Lab            lab;
  private BillingAccount billingAccount;
  private Invoice        invoice;
  private Map            billingItemMap; 
  private Map            relatedBillingItemMap;
  private Map            requestMap; 
  private NumberFormat   currencyFormat = NumberFormat.getCurrencyInstance();
  private String         coreFacilityName;
  private String         contactNameCoreFacility;
  private String         contactPhoneCoreFacility;
  private String         invoiceNote1;
  private String         invoiceNote2;
  private String         contactAddressCoreFacility;
  private String         contactRemitAddressCoreFacility;
  BigDecimal             grandTotal = new BigDecimal(0);

  public BillingInvoiceHTMLFormatter(String coreFacilityName, String contactNameCoreFacility, String contactPhoneCoreFacility, String invoiceNote1, String invoiceNote2, BillingPeriod billingPeriod, Lab lab, BillingAccount billingAccount, Invoice invoice, Map billingItemMap, Map relatedBillingItemMap, Map requestMap, String contactAddressCoreFacility, String contactRemitAddressCoreFacility) {
    this.billingPeriod  = billingPeriod;
    this.lab            = lab;
    this.billingAccount = billingAccount;
    this.invoice        = invoice;
    this.billingItemMap = billingItemMap;
    this.relatedBillingItemMap = relatedBillingItemMap;
    this.requestMap     = requestMap;
    this.coreFacilityName = coreFacilityName;
    this.contactNameCoreFacility = contactNameCoreFacility;
    this.contactPhoneCoreFacility = contactPhoneCoreFacility;
    this.invoiceNote1 = invoiceNote1;
    this.invoiceNote2 = invoiceNote2;
    this.contactAddressCoreFacility = contactAddressCoreFacility;
    this.contactRemitAddressCoreFacility = contactRemitAddressCoreFacility;

  }

  public Element makeIntroNote() {


    String line1 = "This report provides itemized documentation of services that were completed for your lab by the " + coreFacilityName + " during the month of " + billingPeriod.getBillingPeriod() + ".";
    String line2 = "&nbsp;&nbsp;&nbsp; - University of Utah accounts listed on this document will be electronically billed."; 
    String line3 = "&nbsp;&nbsp;&nbsp; - External accounts listed on this document will receive an invoice from the " + coreFacilityName + "."; 
    if ((this.invoiceNote1 != null && this.invoiceNote1.length() > 0) || this.invoiceNote2 != null && this.invoiceNote2.length() > 0) {
      line2 = "";
      if (this.invoiceNote1 != null) {
        line2 = this.invoiceNote1;
      }
      line3 = "";
      if (this.invoiceNote2 != null) {
        line3 = this.invoiceNote2;
      }
    }
    String line4 = "&nbsp;";
    String line5 = "If you have any questions, please contact " + contactNameCoreFacility + " (" + contactPhoneCoreFacility + ").";

    Element table = new Element("TABLE");   
    table.setAttribute("CELLPADDING", "0");
    table.addContent(makeNoteRow(line1));
    if (line2.length() > 0) {
      table.addContent(makeNoteRow(line2));
    }
    if (line3.length() > 0) {
      table.addContent(makeNoteRow(line3));
    }
    table.addContent(makeNoteRow(line4));
    table.addContent(makeNoteRow(line5));

    return table;
  }


  public Element makeHeader() {
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "0");    
    if(billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue() && billingAccount.getIsPO().equals("N") && billingAccount.getIsCreditCard().equals("N")){    
      table.addContent(new Element("TR"));
      if(lab.getContactName() != null && !lab.getContactName().equals(""))
        table.addContent(makeRow("ATTN: " + lab.getContactName()));
      if(lab.getContactAddress2() != null && !lab.getContactAddress2().equals(""))
        table.addContent(makeRow(lab.getContactAddress2()));		
      if(lab.getContactAddress() != null && !lab.getContactAddress().equals(""))
        table.addContent(makeRow(lab.getContactAddress()));
      if(lab.getContactCity() != null && lab.getContactZip() != null && lab.getContactCountry() != null)
      {
        // no state => non-U.S. address
        if(lab.getContactCodeState() == null){
          table.addContent(makeRow(lab.getContactCity() + " " + lab.getContactZip() 
              + " " + lab.getContactCountry()));      
        }
        else {
          table.addContent(makeRow(lab.getContactCity() + ", " + lab.getContactCodeState() + " " 
              + lab.getContactZip() + ", " + lab.getContactCountry()));       
        }
      }

      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow("The following billing summary (or statement) is presented for your review from the " + coreFacilityName + ". This bill will be electronically processed using the specified chartfield number without further action on your behalf. If you have any questions in regards to the billing statement, please contact Derek Warner at derek.warner@hci.utah.edu."));
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow(coreFacilityName));
      table.addContent(makeAddressRow(contactAddressCoreFacility));
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow(billingPeriod.getBillingPeriod()));
      table.addContent(makeRow(billingAccount.getAccountNumber()));
    } 
    else if(billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue() && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")){
      table.addContent(makeRow(coreFacilityName));
      table.addContent(new Element("TR"));
      table.addContent(makeAddressRow(contactAddressCoreFacility));
      table.addContent(new Element("TR"));// add line break before billing address
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow(lab.getName(false, true)));
      if(lab.getContactName() != null && !lab.getContactName().equals(""))
        table.addContent(makeRow("ATTN: " + lab.getContactName()));
      if(lab.getContactAddress2() != null && !lab.getContactAddress2().equals(""))
        table.addContent(makeRow(lab.getContactAddress2()));
      if(lab.getContactAddress() != null && !lab.getContactAddress().equals("")){
        table.addContent(makeRow(lab.getContactAddress()));
      }

      if(lab.getContactCity() != null && lab.getContactZip() != null && lab.getContactCountry() != null)
      {
        // no state => non-U.S. address
        if(lab.getContactCodeState() == null){
          table.addContent(makeRow(lab.getContactCity() + " " + lab.getContactZip() 
              + " " + lab.getContactCountry()));      
        }
        else {
          table.addContent(makeRow(lab.getContactCity() + ", " + lab.getContactCodeState() + " " 
              + lab.getContactZip() + ", " + lab.getContactCountry()));       
        }
      }
      table.addContent(new Element("TR"));// add line break after billing address
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow(billingPeriod.getBillingPeriod()));
      table.addContent(makeRow(billingAccount.getAccountName()));
    } 
    else{
      table.addContent(makeRow(lab.getName(false, true)));
      table.addContent(makeRow(formatAccountNumber(billingAccount.getAccountNumber(), billingAccount.getAccountName())));
      table.addContent(new Element("TR"));// add line break before billing address
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      if(lab.getContactName() != null && !lab.getContactName().equals(""))
        table.addContent(makeRow("ATTN: " + lab.getContactName()));
      if(lab.getContactAddress2() != null && !lab.getContactAddress2().equals(""))
        table.addContent(makeRow(lab.getContactAddress2()));		
      if(lab.getContactAddress() != null && !lab.getContactAddress().equals(""))
        table.addContent(makeRow(lab.getContactAddress()));
      if(lab.getContactCity() != null && lab.getContactZip() != null && lab.getContactCountry() != null)
      {
        // no state => non-U.S. address
        if(lab.getContactCodeState() == null){
          table.addContent(makeRow(lab.getContactCity() + " " + lab.getContactZip() 
              + " " + lab.getContactCountry()));      
        }
        else {
          table.addContent(makeRow(lab.getContactCity() + ", " + lab.getContactCodeState() + " " 
              + lab.getContactZip() + ", " + lab.getContactCountry()));       
        }
      }
      table.addContent(new Element("TR"));// add line break after billing address
      table.addContent(new Element("TR"));
      table.addContent(new Element("TR"));
      table.addContent(makeRow(billingPeriod.getBillingPeriod() + " " + coreFacilityName + " Billing"));
      if(invoice != null){
        table.addContent(makeRow("Invoice # " + invoice.getInvoiceNumber()));
      }
    }

    return table;
  }

  public Element makeRemittanceAddress(){
    String addressArray[] = contactRemitAddressCoreFacility.split("\r");
    Element p = new Element("P");
    p.addContent("REMIT TO ADDRESS:");
    p.addContent(new Element("BR"));
    for(int i = 0; i < addressArray.length; i++){
      p.addContent(addressArray[i]);
      if(i + 1 < addressArray.length){
        p.addContent(new Element("BR"));
      }
    }
    return p;
  }

  public Element makeLabAddress(){
    String addressArray[] = contactAddressCoreFacility.split("\r");
    Element p = new Element("P");
    p.addContent("LAB ADDRESS:");
    p.addContent(new Element("BR"));
    for(int i = 0; i < addressArray.length; i++){
      p.addContent(addressArray[i]);
      if(i + 1 < addressArray.length){
        p.addContent(new Element("BR"));
      }
    }
    return p;

  }

  private String formatAccountNumber(String accountNumber, String accountName) {
    return "Account " + (accountNumber != null && !accountNumber.equals("" ) ? accountNumber + " " : "") + 
    (accountName != null && !accountName.equals("") ? "(" + accountName + ")": "");
  }

  public Element makeDetail() throws Exception {

    int columnCount = 11;
    // Find out if any billing items have % other than 100%.  If
    // so, show % column.
    boolean showPercentCol = false;
    for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
      String requestNumber = (String)i.next();      
      List billingItems = (List)billingItemMap.get(requestNumber);
      BigDecimal totalPriceForRequest = new BigDecimal(0);
      for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
        BillingItem bi = (BillingItem)i1.next();
        if (!bi.getPercentageDisplay().equals("100%")) {
          showPercentCol = true;
          break;
        }
      }
    }
    if (showPercentCol) {
      columnCount++;
    }

    grandTotal = new BigDecimal(0);

    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Req Date", "left");
    this.addHeaderCell(rowh, "Req ID");
    this.addHeaderCell(rowh, "Client"    );
    this.addHeaderCell(rowh, "Service");
    this.addHeaderCell(rowh, "Date", "left");
    this.addHeaderCell(rowh, "Description"    );
    this.addHeaderCell(rowh, "Notes"    );
    if (showPercentCol) {
      this.addHeaderCell(rowh, "Percent", "right");
    }
    this.addHeaderCell(rowh, "Qty", "right");
    this.addHeaderCell(rowh, "Unit Price", "right");
    this.addHeaderCell(rowh, "Total Price", "right");
    this.addHeaderCell(rowh, "Invoice Price", "right");


    for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
      String number = (String)i.next();
      Request request = null;
      DiskUsageByMonth dsk = null;
      if (number.startsWith(ShowBillingInvoiceForm.DISK_USAGE_NUMBER_PREFIX)) {
        dsk = (DiskUsageByMonth)requestMap.get(number);
      } else {
        request = (Request)requestMap.get(number);
      }
      List billingItems = (List)billingItemMap.get(number);
      String client = "";
      if (request != null) {
        client = request.getAppUser() != null ? request.getAppUser().getDisplayName() : "&nbsp;";
      } else { 
        client = "Disk Usage";
      }


      Element rowR = new Element("TR");
      table.addContent(rowR);
      if (request != null) {
        this.addCell(rowR, this.formatDate(request.getCreateDate(), this.DATE_OUTPUT_SLASH));
        this.addCell(rowR, request.getNumber());
        this.addCell(rowR, client);
      } else {
        this.addCell(rowR, this.formatDate(dsk.getAsOfDate()));
        this.addCell(rowR, client, 2);
      }
      BigDecimal totalPriceForRequest = new BigDecimal(0);
      for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
        BillingItem bi = (BillingItem)i1.next();


        Element row = new Element("TR");
        table.addContent(row);
        this.addEmptyCell(row, new Integer(3));
        this.addCell(row, this.getHTMLString(bi.getCategory() != null ? bi.getCategory() : "&nbsp;"));
        this.addCell(row, this.formatDate(bi.getCompleteDate(), this.DATE_OUTPUT_SLASH));
        this.addCell(row, this.getHTMLString(bi.getDescription() != null ? bi.getDescription() : "&nbsp;"));
        this.addCell(row, this.getHTMLString(bi.getNotes() != null && !bi.getNotes().equals("") ? bi.getNotes() : "&nbsp;"));
        if (showPercentCol) {
          this.addRightAlignCell(row, this.getHTMLString(bi.getPercentageDisplay()));          
        }
        this.addRightAlignCell(row, this.getHTMLString(bi.getQty()));
        this.addRightAlignCell(row, bi.getUnitPrice() != null ? currencyFormat.format(bi.getUnitPrice()) : "&nbsp;");
        this.addRightAlignCell(row, bi.getTotalPrice() != null ? currencyFormat.format(bi.getTotalPrice()) : "&nbsp;");
        this.addRightAlignCell(row, bi.getInvoicePrice() != null ? currencyFormat.format(bi.getInvoicePrice()) : "&nbsp;");

        if (bi.getInvoicePrice() != null) {
          totalPriceForRequest = totalPriceForRequest.add(bi.getInvoicePrice());          
          grandTotal = grandTotal.add(bi.getInvoicePrice());          
        }

      }

      Element rowt2 = new Element("TR");
      table.addContent(rowt2);
      this.addEmptyCell(rowt2, new Integer(columnCount - 1));
      this.addTotalCell(rowt2, totalPriceForRequest != null ? currencyFormat.format(totalPriceForRequest) : "&nbsp;");

      billingItems = (List)relatedBillingItemMap.get(number);
      if (billingItems != null) {
        Integer idBillingAccount = -1;
        for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
          BillingItem bi = (BillingItem)i1.next();
          if (!idBillingAccount.equals(bi.getIdBillingAccount())) {
            idBillingAccount = bi.getIdBillingAccount();
            totalPriceForRequest = new BigDecimal(0);
            Element rowRH1 = new Element("TR");
            table.addContent(rowRH1);
            this.addEmptyCell(rowRH1, 2);
            this.addCell(rowRH1, bi.getLabName(), "gridrelated", 9);
            Element rowRH2 = new Element("TR");
            table.addContent(rowRH2);
            this.addEmptyCell(rowRH2, 2);
            this.addCell(rowRH2, formatAccountNumber(bi.getAccountNumberDisplay(), bi.getAccountName()), "gridrelated", 10);
          }

          Element row = new Element("TR");
          table.addContent(row);
          this.addEmptyCell(row, new Integer(3));
          this.addCell(row, this.getHTMLString(bi.getCategory() != null ? bi.getCategory() : "&nbsp;"), "gridrelated", null);
          this.addCell(row, this.formatDate(bi.getCompleteDate(), this.DATE_OUTPUT_SLASH), "gridrelated", null);
          this.addCell(row, this.getHTMLString(bi.getDescription() != null ? bi.getDescription() : "&nbsp;"), "gridrelated", null);
          this.addCell(row, this.getHTMLString(bi.getNotes() != null && !bi.getNotes().equals("") ? bi.getNotes() : "&nbsp;"), "gridrelated", null);
          if (showPercentCol) {
            this.addCell(row, this.getHTMLString(bi.getPercentageDisplay()), "gridrightrelated", null, "RIGHT");          
          }
          this.addCell(row, this.getHTMLString(bi.getQty()), "gridrightrelated", null, "RIGHT");
          this.addCell(row, bi.getUnitPrice() != null ? currencyFormat.format(bi.getUnitPrice()) : "&nbsp;", "gridrightrelated", null, "RIGHT");
          this.addCell(row, bi.getTotalPrice() != null ? currencyFormat.format(bi.getTotalPrice()) : "&nbsp;", "gridrightrelated", null, "RIGHT");
          this.addCell(row, bi.getInvoicePrice() != null ? currencyFormat.format(bi.getInvoicePrice()) : "&nbsp;", "gridrightrelated", null, "RIGHT");

          if (bi.getInvoicePrice() != null) {
            totalPriceForRequest = totalPriceForRequest.add(bi.getInvoicePrice());          
          }

        }

        Element rowrt2 = new Element("TR");
        table.addContent(rowrt2);
        this.addEmptyCell(rowrt2, new Integer(columnCount - 1));
        this.addCell(rowrt2, totalPriceForRequest != null ? currencyFormat.format(totalPriceForRequest) : "&nbsp;", "gridrightrelated", null, "RIGHT");
      }

      Element rowt1 = new Element("TR");
      table.addContent(rowt1);
      this.addEmptyCell(rowt1, new Integer(columnCount));
    }

    Element rowt = new Element("TR");
    table.addContent(rowt);
    this.addEmptyCell(rowt, new Integer(columnCount));

    Element rowgt = new Element("TR");
    table.addContent(rowgt);
    this.addEmptyCell(rowgt, new Integer(columnCount - 2));
    this.addRightAlignCell(rowgt, "Total");
    this.addTotalCell(rowgt, grandTotal != null ? currencyFormat.format(grandTotal) : "&nbsp;");


    return table;
  }

  public String getGrandTotal(){
    return grandTotal != null ? currencyFormat.format(grandTotal) : "";
  }

  private String getHTMLString(String value) {
    if (value == null) {
      return "&nbsp;";
    } else {
      return value;
    }
  }
  private String getHTMLString(Integer value) {
    if (value == null) {
      return "&nbsp;";
    } else {
      return value.toString();
    }
  }

  private Element makeRow(String header1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);


    return row;
  }

  private Element makeAddressRow(String header1){
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "LEFT");
    String addressArray[] = header1.split("\r");
    for(int i = 0; i < addressArray.length; i++){
      cell.addContent(addressArray[i]);
      if(i + 1 < addressArray.length){
        cell.addContent(new Element("BR"));
      }
    }
    row.addContent(cell);
    return row;
  }


  private Element makeNoteRow(String header1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "note");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);


    return row;
  }


  private void addRightAlignCell(Element row, String value) {
    Element cell = new Element("TD");

    // for consistent rendering in different email apps
    cell.setAttribute("ALIGN", "RIGHT");

    cell.setAttribute("CLASS", "gridright");      
    cell.addContent(value);
    row.addContent(cell);
  }

  private void addTotalCell(Element row, String value) {
    Element cell = new Element("TD");

    // for consistent rendering in different email apps
    cell.setAttribute("ALIGN", "RIGHT");

    cell.setAttribute("CLASS", "gridtotal");      
    cell.addContent(value);
    row.addContent(cell);
  }  

  private void addCenterAlignCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridcenter");      
    cell.addContent(value);
    row.addContent(cell);
  }

  private void addCell(Element row, String value) {
    addCell(row, value, "grid", null);
  }

  private void addCell(Element row, String value, String cssClass, Integer colSpan) {
    addCell(row, value, cssClass, colSpan, null);
  }

  private void addCell(Element row, String value, Integer colSpan) {
    addCell(row, value, "grid", colSpan, null);
  }

  private void addCell(Element row, String value, String cssClass, Integer colSpan, String align) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", cssClass);      
    cell.addContent(value);
    row.addContent(cell);
    if (colSpan != null) {
      cell.setAttribute("COLSPAN", colSpan.toString());
    }
    if (align != null) {
      cell.setAttribute("ALIGN", align);
    }
  }

  private void addEmptyCell(Element row, Integer colSpan) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
    row.addContent(cell);
    if (colSpan != null) {
      cell.setAttribute("COLSPAN", colSpan.toString());      
    }

  }



  private void addEmptyCell(Element row) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
    row.addContent(cell);
  }

  private void addHeaderCell(Element row, String header) {
    addHeaderCell(row, header, "normal");
  }

  private void addHeaderCell(Element row, String header, String clazzName) {

    addHeaderCell(row, header, clazzName, null);
  }


  private void addHeaderCell(Element row, String header, String clazzName, Integer width) {
    Element cell = new Element("TH");  

    // for consistent rendering in different email apps
    if (clazzName.equals("right")) {
      cell.setAttribute("ALIGN", "RIGHT");      
    } else {
      cell.setAttribute("ALIGN", "LEFT");
    }

    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    cell.addContent(header);
    row.addContent(cell);
  }

  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", null);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, Integer width) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", width);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName) {
    addHeaderCell(row, header, rowSpan, colSpan, clazzName, null);
  }

  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName, Integer width) {
    Element cell = new Element("TH");    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    cell.addContent(header);
    if (colSpan != null) {    
      cell.setAttribute("COLSPAN", colSpan.toString());
    }
    if (rowSpan != null) {
      cell.setAttribute("ROWSPAN", rowSpan.toString());      
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    row.addContent(cell);
  }



  private Element makeRow(String header1, String value1, String header2, String value2) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "RIGHT");
    cell.addContent(header1);
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(value1);
    row.addContent(cell);

    cell = new Element("TD");
    //cell.setAttribute("WIDTH", "80");
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "RIGHT");
    cell.addContent(header2);
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(value2);
    row.addContent(cell);

    return row;
  } 

  private Element makeRow(String header1, String value1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.setAttribute("WIDTH", "80%");
    cell.addContent(value1);
    row.addContent(cell);


    return row;
  } 

}
