package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.text.NumberFormatter;

import org.hibernate.Session;
import org.jdom.Element;


public class BillingInvoiceHTMLFormatter  extends DetailObject {
  
  private BillingPeriod  billingPeriod;
  private Lab            lab;
  private BillingAccount billingAccount;
  private Map            billingItemMap; 
  private Map            requestMap; 
  private NumberFormat   currencyFormat = NumberFormat.getCurrencyInstance();
  private String         coreFacilityName;
  private String         contactNameCoreFacility;
  private String         contactPhoneCoreFacility;
  
 public BillingInvoiceHTMLFormatter(String coreFacilityName, String contactNameCoreFacility, String contactPhoneCoreFacility, BillingPeriod billingPeriod, Lab lab, BillingAccount billingAccount, Map billingItemMap, Map requestMap) {
   this.billingPeriod  = billingPeriod;
   this.lab            = lab;
   this.billingAccount = billingAccount;
   this.billingItemMap = billingItemMap;
   this.requestMap     = requestMap;
   this.coreFacilityName = coreFacilityName;
   this.contactNameCoreFacility = contactNameCoreFacility;
   this.contactPhoneCoreFacility = contactPhoneCoreFacility;
   
 }
 
 public Element makeIntroNote() {
   
   
   String line1 = "This report provides itemized documentation of services that were completed for your lab by the " + coreFacilityName + " during the month of " + billingPeriod.getBillingPeriod() + ".";
   String line2 = "&nbsp;&nbsp;&nbsp; - University of Utah accounts listed on this document will be electronically billed."; 
   String line3 = "&nbsp;&nbsp;&nbsp; - External accounts listed on this document will receive an invoice from the " + coreFacilityName + "."; 
   String line4 = "&nbsp;";
   String line5 = "If you have any questions, please contact " + contactNameCoreFacility + " (" + contactPhoneCoreFacility + ").";
       
    
   Element table = new Element("TABLE");   
   table.setAttribute("CELLPADDING", "0");
   table.addContent(makeNoteRow(line1));
   table.addContent(makeNoteRow(line2));
   table.addContent(makeNoteRow(line3));
   table.addContent(makeNoteRow(line4));
   table.addContent(makeNoteRow(line5));
   
   return table;
 }
 

 public Element makeHeader() {
    
    
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "0");
    table.addContent(makeRow(lab.getName()));
    table.addContent(makeRow("Account " + (billingAccount.getAccountNumber() != null && !billingAccount.getAccountNumber().equals("" ) ? billingAccount.getAccountNumber() + " " : "") + 
                                          (billingAccount.getAccountName() != null && !billingAccount.getAccountName().equals("") ? "(" + billingAccount.getAccountName() + ")": "")));
    table.addContent(makeRow(billingPeriod.getBillingPeriod() + " " + coreFacilityName + " Chargeback")); 
    
    return table;
 }


  
  public Element makeDetail() throws Exception {
    
    int columnCount = 9;
    // Find out if any billing items have % other than 100%.  If
    // so, show % column.
    boolean showPercentCol = false;
    for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
      String requestNumber = (String)i.next();      
      Request request = (Request)requestMap.get(requestNumber);      
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
    
    BigDecimal grandTotal = new BigDecimal(0);
    
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "5");
    table.setAttribute("CELLSPACING", "5");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Date", "left");
    this.addHeaderCell(rowh, "Req ID");
    this.addHeaderCell(rowh, "Client"    );
    this.addHeaderCell(rowh, "Service");
    this.addHeaderCell(rowh, "Description"    );
    this.addHeaderCell(rowh, "Notes"    );
    if (showPercentCol) {
      this.addHeaderCell(rowh, "Percent", "right");
    }
    this.addHeaderCell(rowh, "Qty", "right");
    this.addHeaderCell(rowh, "Unit Price", "right");
    this.addHeaderCell(rowh, "Total Price", "right");

    
    for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
      String requestNumber = (String)i.next();
      Request request = (Request)requestMap.get(requestNumber);      
      List billingItems = (List)billingItemMap.get(requestNumber);
      
      
      BigDecimal totalPriceForRequest = new BigDecimal(0);
      for(Iterator i1 = billingItems.iterator(); i1.hasNext();) {
        BillingItem bi = (BillingItem)i1.next();
        
       
        String client = request.getAppUser() != null ? request.getAppUser().getDisplayName() : "&nbsp;";        
        
        
        Element row = new Element("TR");
        table.addContent(row);
        this.addCell(row, this.formatDate(request.getCreateDate(), this.DATE_OUTPUT_SLASH));
        this.addCell(row, request.getNumber());
        this.addCell(row, client);
        this.addCell(row, this.getHTMLString(bi.getCategory() != null ? bi.getCategory() : "&nbsp;"));
        this.addCell(row, this.getHTMLString(bi.getDescription() != null ? bi.getDescription() : "&nbsp;"));
        this.addCell(row, this.getHTMLString(bi.getNotes() != null && !bi.getNotes().equals("") ? bi.getNotes() : "&nbsp;"));
        if (showPercentCol) {
          this.addRightAlignCell(row, this.getHTMLString(bi.getPercentageDisplay()));          
        }
        this.addRightAlignCell(row, this.getHTMLString(bi.getQty()));
        this.addRightAlignCell(row, bi.getUnitPrice() != null ? currencyFormat.format(bi.getUnitPrice()) : "&nbsp;");
        this.addRightAlignCell(row, bi.getTotalPrice() != null ? currencyFormat.format(bi.getTotalPrice()) : "&nbsp;");
        
        if (bi.getTotalPrice() != null) {
          totalPriceForRequest = totalPriceForRequest.add(bi.getTotalPrice());          
          grandTotal = grandTotal.add(bi.getTotalPrice());          
        }
        
      }

      
      Element rowt2 = new Element("TR");
      table.addContent(rowt2);
      this.addEmptyCell(rowt2, new Integer(columnCount - 1));
      this.addTotalCell(rowt2, totalPriceForRequest != null ? currencyFormat.format(totalPriceForRequest) : "&nbsp;");

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
    cell.setAttribute("CLASS", "gridright");      
    cell.addContent(value);
    row.addContent(cell);
  }
  
  private void addTotalCell(Element row, String value) {
    Element cell = new Element("TD");
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
      Element cell = new Element("TD");
      cell.setAttribute("CLASS", "grid");      
      cell.addContent(value);
      row.addContent(cell);
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
