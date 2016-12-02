package hci.gnomex.utility;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import hci.framework.model.DetailObject;
import hci.gnomex.controller.ShowBillingInvoiceForm;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;

@SuppressWarnings("serial")
public class BillingPDFFormatter extends DetailObject {
	
	private Session				sess;
	private BillingPeriod 		billingPeriod;
	private CoreFacility 		coreFacility;
	private String 				contactAddressCoreFacility;
	private String				contactRemitAddressCoreFacility;
	private String				contactNameCoreFacility;
	private String				contactPhoneCoreFacility;
	private Map[]				billingItemMap;
	private Map[]				relatedBillingItemMap;
	private Map[]				requestMap;
	
	private boolean 			multipleLabs;
	
	private Lab 				lab;
	private Lab[] 				labs;
	private BillingAccount 		billingAccount;
	private BillingAccount[] 	billingAccounts;
	
	private boolean 			isDNASeqCore;
	private NumberFormat   		currencyFormat;
	private BigDecimal			grandTotal;
	
	private String				useInvoiceNumbering;
	
	// Font library
	private static final Font FONT_HEADER = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	private static final Font FONT_TABLE_HEADER = new Font(FontFamily.HELVETICA, 7.2f, Font.BOLD, BaseColor.BLACK);
	private static final Font FONT_TABLE_VALUE_NORMAL = new Font(FontFamily.HELVETICA, 7.2f, Font.NORMAL, BaseColor.BLACK);
	private static final Font FONT_TABLE_VALUE_NORMAL_BOLD = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
	private static final Font FONT_TABLE_VALUE_RELATED_ITEMS = new Font(FontFamily.HELVETICA, 6, Font.ITALIC, BaseColor.BLACK);
	private static final Font FONT_REMITTANCE_HEADER = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	private static final Font FONT_REMITTANCE_NORMAL = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	private static final Font FONT_REMITTANCE_BOLD = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	
	public BillingPDFFormatter(Session sess, BillingPeriod billingPeriod, CoreFacility coreFacility, boolean multipleLabs, Lab lab, Lab[] labs, BillingAccount billingAccount, BillingAccount[] billingAccounts, 
								String contactAddressCoreFacility, String contactRemitAddressCoreFacility, String contactNameCoreFacility, String contactPhoneCoreFacility, 
								Map[] billingItemMap, Map[] relatedBillingItemMap, Map[] requestMap) {
		this.sess = sess;
		this.billingPeriod = billingPeriod;
		this.coreFacility = coreFacility;
		this.multipleLabs = multipleLabs;
		this.lab = lab;
		this.labs = labs;
		this.billingAccount = billingAccount;
		this.billingAccounts = billingAccounts;
		this.contactAddressCoreFacility = contactAddressCoreFacility;
		this.contactRemitAddressCoreFacility = contactRemitAddressCoreFacility;
		this.contactNameCoreFacility = contactNameCoreFacility;
		this.contactPhoneCoreFacility = contactPhoneCoreFacility;
		this.billingItemMap = billingItemMap;
		this.relatedBillingItemMap = relatedBillingItemMap;
		this.requestMap = requestMap;
		
		if (this.billingAccount != null) {
			isDNASeqCore = this.billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue();
		}
		currencyFormat = NumberFormat.getCurrencyInstance();
		grandTotal = new BigDecimal(0);
		
		useInvoiceNumbering = PropertyDictionaryHelper.getInstance(this.sess).getCoreFacilityProperty(this.coreFacility.getIdCoreFacility(), PropertyDictionary.USE_INVOICE_NUMBERING);
	}
	
	public ArrayList<Element> makeContent() {
		ArrayList<Element> content = new ArrayList<Element>();
		
		if (multipleLabs && labs.length > 0) {
			content.addAll(makeContentForLab(labs[0], billingAccounts[0], billingItemMap[0], relatedBillingItemMap[0], requestMap[0]));
			for (int i = 1; i < labs.length; i++) {
				content.add(Chunk.NEXTPAGE);
				content.addAll(makeContentForLab(labs[i], billingAccounts[i], billingItemMap[i], relatedBillingItemMap[i], requestMap[i]));
			}
		}
		else {
			content.addAll(makeContentForLab(lab, billingAccount, billingItemMap[0], relatedBillingItemMap[0], requestMap[0]));
		}
		
		return content;
	}
	
	private ArrayList<Element> makeContentForLab(Lab lab, BillingAccount billingAccount, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
		isDNASeqCore = billingAccount.getIdCoreFacility().intValue() == CoreFacility.CORE_FACILITY_DNA_SEQ_ID.intValue();
		grandTotal = new BigDecimal(0);
		
		Invoice invoice = retrieveInvoice(billingPeriod, coreFacility, billingAccount);
		
		ArrayList<Element> content = new ArrayList<Element>();
		
		// Header
		ArrayList<Element> headerElements = makeHeader(lab, billingAccount, invoice);
		for (Element e : headerElements) {
			content.add(e);
		}
		
		content.add(Chunk.NEWLINE);
		
		// Body table
		ArrayList<Element> bodyElements = makeBody(invoice, lab, billingAccount, billingItemMap, relatedBillingItemMap, requestMap);
		for (Element e : bodyElements) {
			content.add(e);
		}
		
		return content;
	}
	
	private ArrayList<Element> makeHeader(Lab lab, BillingAccount billingAccount, Invoice invoice) {
		ArrayList<Element> headerElements = new ArrayList<Element>();
		
		if (!isDNASeqCore) {
			headerElements.addAll(makeLabInfoHeader(lab, billingAccount));
			headerElements.add(Chunk.NEWLINE);
		}
		
		if (isDNASeqCore && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")) {
			PDFFormatterUtil.addPhrase(headerElements, coreFacility.getFacilityName(), FONT_HEADER);
			headerElements.add(Chunk.NEWLINE);
			headerElements.addAll(makeAddressHeader(contactAddressCoreFacility));
			headerElements.add(Chunk.NEWLINE);			
		}
		
		if (!headerElements.isEmpty()) headerElements.add(Chunk.NEWLINE);
		headerElements.addAll(makePersonalAddressHeader(lab, billingAccount));
		headerElements.add(Chunk.NEWLINE);
		
		if (isDNASeqCore && billingAccount.getIsPO().equals("N") && billingAccount.getIsCreditCard().equals("N")) {
			PDFFormatterUtil.addPhrase(headerElements, coreFacility.getFacilityName(), FONT_HEADER);
			headerElements.add(Chunk.NEWLINE);
			headerElements.addAll(makeAddressHeader(contactAddressCoreFacility));
			headerElements.add(Chunk.NEWLINE);			
		}
		
		Paragraph headerNote = makeInvoiceHeaderNote(billingAccount);
		if (headerNote.getContent() != null && !headerNote.getContent().trim().equals("")) {
			headerElements.add(Chunk.NEWLINE);
			headerElements.add(headerNote);
		}
		
		if (isDNASeqCore) {
			PDFFormatterUtil.addPhrase(headerElements, billingPeriod.getBillingPeriod(), FONT_HEADER);
			if (billingAccount.getIsPO().equals("N") && billingAccount.getIsCreditCard().equals("N")) {
				PDFFormatterUtil.addPhrase(headerElements, billingAccount.getAccountNumber(), FONT_HEADER);
			} else {
				PDFFormatterUtil.addPhrase(headerElements, billingAccount.getAccountName(), FONT_HEADER);
			}
		}
		else {
			PDFFormatterUtil.addPhrase(headerElements, billingPeriod.getBillingPeriod() + " " + coreFacility.getFacilityName() + " Billing", FONT_HEADER);
			if (invoice != null && (useInvoiceNumbering == null || !useInvoiceNumbering.equals("N"))) {
				PDFFormatterUtil.addPhrase(headerElements, "Invoice # " + invoice.getInvoiceNumber(), FONT_HEADER);
			}
		}
		
		return headerElements;
	}
	
	private ArrayList<Element> makeBody(Invoice invoice, Lab lab, BillingAccount billingAccount, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
		ArrayList<Element> elements = new ArrayList<Element>();

		// Table
		PdfPTable table;
		boolean showPercentColumn = determineShowPercentColumn(billingItemMap, relatedBillingItemMap, requestMap);
		int columnCount;
		if (showPercentColumn) {
			columnCount = 12;
		} else {
			columnCount = 11;
		}
		table = new PdfPTable(columnCount);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		// Table header
		table.setHeaderRows(2); // Number of header rows + footer rows
		PDFFormatterUtil.addToTableHeader(table, "Req Date", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Req ID", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Client", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Service", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Date", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Description", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Notes", FONT_TABLE_HEADER);
		if (showPercentColumn) {
			PDFFormatterUtil.addToTableHeader(table, "Percent", FONT_TABLE_HEADER);
		}
		PDFFormatterUtil.addToTableHeader(table, "Qty", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Unit Price", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Total Price", FONT_TABLE_HEADER);
		PDFFormatterUtil.addToTableHeader(table, "Invoice Price", FONT_TABLE_HEADER);
		
		// Footer note
		String note = makeInvoiceFooterNote();
		if (note != null && !note.trim().equals("")) {
			table.setFooterRows(1);
			note = PDFFormatterUtil.stripEndOfLineCharacters(note);
			PDFFormatterUtil.addToTable(table, note, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, columnCount, 1);
		}
		
		// Populate table
		for (Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
			String number = (String) i.next();
			BigDecimal totalPriceForRequest = new BigDecimal(0);
			
			populateTableRequestHeader(table, number, showPercentColumn, columnCount, billingItemMap, relatedBillingItemMap, requestMap);
			List billingItems = (List) billingItemMap.get(number);
			for (Iterator biIter = billingItems.iterator(); biIter.hasNext();) {
				BillingItem bi = (BillingItem) biIter.next();
				populateTableBillingItem(table, bi, showPercentColumn);
				if (bi.getInvoicePrice() != null) {
					totalPriceForRequest = totalPriceForRequest.add(bi.getInvoicePrice());
					grandTotal = grandTotal.add(bi.getInvoicePrice());
				}
			}
			PDFFormatterUtil.addToTablePaddingCell(table, columnCount - 1, 1);
			PDFFormatterUtil.addToTable(table, currencyFormat.format(totalPriceForRequest), FONT_TABLE_VALUE_NORMAL_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, true, false, false, false, BaseColor.LIGHT_GRAY, 1, 1);
			
			billingItems = (List) relatedBillingItemMap.get(number);
			if (billingItems != null) {
				Integer idBillingAccount = -1;
				for (Iterator biIter = billingItems.iterator(); biIter.hasNext();) {
					BillingItem bi = (BillingItem) biIter.next();
					if (!idBillingAccount.equals(bi.getIdBillingAccount())) {
						idBillingAccount = bi.getIdBillingAccount();
						totalPriceForRequest = new BigDecimal(0);
						populateTableRelatedBillingItemAccountHeader(table, bi, columnCount);
					}
					populateTableRelatedBillingItem(table, bi, showPercentColumn);
					if (bi.getInvoicePrice() != null) {
						totalPriceForRequest = totalPriceForRequest.add(bi.getInvoicePrice());
					}
				}
				PDFFormatterUtil.addToTablePaddingCell(table, columnCount - 1, 1);
				PDFFormatterUtil.addToTable(table, currencyFormat.format(totalPriceForRequest), FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.LIGHT_GRAY, 1, 1);
			}
		}
		
		// Total row
		PDFFormatterUtil.addToTableBlankRow(table);
		PDFFormatterUtil.addToTableBlankRow(table);
		PDFFormatterUtil.addToTable(table, "Total", FONT_TABLE_VALUE_NORMAL, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, columnCount - 1, 1);
		PDFFormatterUtil.addToTable(table, currencyFormat.format(grandTotal), FONT_TABLE_VALUE_NORMAL_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, true, false, false, false, BaseColor.BLACK, 1, 1);
		
		elements.add(table);
		
		// Remittance
		if (isDNASeqCore && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")) {
			elements.add(Chunk.NEWLINE);
			PdfPTable remitTable = new PdfPTable(2);
			remitTable.setWidthPercentage(100);
			remitTable.setHorizontalAlignment(Element.ALIGN_CENTER);
			remitTable.setKeepTogether(true);
			
			String remittanceHeader = "To ensure proper credit, please return this portion with your payment to University of Utah";
			PDFFormatterUtil.addToTable(remitTable, remittanceHeader, FONT_REMITTANCE_HEADER, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, true, false, false, false, BaseColor.GRAY, 2, 1);
			PDFFormatterUtil.addToTableBlankRow(remitTable);
			PDFFormatterUtil.addToTable(remitTable, new PdfPCell(PDFFormatterUtil.combineTables(makeRemitTable1(), makeRemitTable2(invoice))), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 2, 1);
			
			elements.add(remitTable);
		}
		
		return elements;
	}
	
	private PdfPTable makeRemitTable1() {
		PdfPTable remitTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(remitTable, "REMITTANCE ADVICE", FONT_REMITTANCE_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		PDFFormatterUtil.addToTableBlankRow(remitTable);
		PDFFormatterUtil.addToTable(remitTable, "Your payment is due upon receipt", FONT_REMITTANCE_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		PDFFormatterUtil.addToTableBlankRow(remitTable);
		PDFFormatterUtil.addToTable(remitTable, "REMIT TO ADDRESS:", FONT_REMITTANCE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		for (Element e : makeAddressElements(contactRemitAddressCoreFacility, FONT_REMITTANCE_NORMAL)) {
			PdfPCell cell = new PdfPCell();
			cell.addElement(e);
			PDFFormatterUtil.addToTable(remitTable, cell, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		}
		return remitTable;
	}
	
	private PdfPTable makeRemitTable2(Invoice invoice) {
		PdfPTable remitTable = new PdfPTable(1);
		if (invoice != null && (useInvoiceNumbering == null || !useInvoiceNumbering.equals("N"))) {
			PDFFormatterUtil.addToTable(remitTable, "Invoice Number: " + invoice.getInvoiceNumber(), FONT_REMITTANCE_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
			PDFFormatterUtil.addToTableBlankRow(remitTable);
		}
		PDFFormatterUtil.addToTable(remitTable, "Amount Due: " + currencyFormat.format(grandTotal), FONT_REMITTANCE_BOLD, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		PDFFormatterUtil.addToTableBlankRow(remitTable);
		PDFFormatterUtil.addToTable(remitTable, "LAB ADDRESS:", FONT_REMITTANCE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		for (Element e : makeAddressElements(contactAddressCoreFacility, FONT_REMITTANCE_NORMAL)) {
			PdfPCell cell = new PdfPCell();
			cell.addElement(e);
			PDFFormatterUtil.addToTable(remitTable, cell, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.GRAY, 1, 1);
		}
		return remitTable;
	}
	
	private void populateTableRequestHeader(PdfPTable table, String number, boolean showPercentColumn, int columnCount, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
		Request request = null;
		DiskUsageByMonth dsk = null;
		ProductOrder po = null;
		if (number.startsWith(ShowBillingInvoiceForm.DISK_USAGE_NUMBER_PREFIX)) {
			dsk = (DiskUsageByMonth) requestMap.get(number);
		} else if (number.startsWith(ShowBillingInvoiceForm.PRODUCT_ORDER_NUMBER_PREFIX)) {
			po = (ProductOrder) requestMap.get(number);
		} else {
			request = (Request) requestMap.get(number);
		}
		
		String client = "";
		if (request != null && request.getAppUser() != null) {
			client = request.getAppUser().getDisplayName();
		} else if (dsk != null) {
			client = "Disk Usage";
		} else {
			client = "Product Order";
		}
		
		if (request != null) {
			PDFFormatterUtil.addToTable(table, formatDate(request.getCreateDate(), DATE_OUTPUT_SLASH), FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(table, request.getNumber(), FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(table, client, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		} 
		else if (dsk != null) {
			PDFFormatterUtil.addToTable(table, formatDate(dsk.getAsOfDate()), FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(table, client, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
		} 
		else {
			PDFFormatterUtil.addToTable(table, formatDate(po.getSubmitDate()), FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(table, po.getProductOrderNumber(), FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(table, client, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		}
		
		PDFFormatterUtil.addToTablePaddingCell(table, columnCount - 3, 1);
	}
	
	private void populateTableBillingItem(PdfPTable table, BillingItem bi, boolean showPercentColumn) {
		String service = "";
		if (bi.getCategory() != null) {
			service = bi.getCategory();
		}
		String date = formatDate(bi.getCompleteDate(), DATE_OUTPUT_SLASH);
		String description = "";
		if (bi.getDescription() != null) {
			description = bi.getDescription();
		}
		String notes = "";
		if (bi.getNotes() != null) {
			notes = bi.getNotes();
		}
		String percentage = "";
		if (showPercentColumn) {
			percentage = bi.getPercentageDisplay();
		}
		String qty = "";
		if (bi.getQty() != null) {
			qty = bi.getQty().toString();
		}
		String unitPrice = "";
		if (bi.getUnitPrice() != null) {
			unitPrice = currencyFormat.format(bi.getUnitPrice());
		}
		String totalPrice = "";
		if (bi.getTotalPrice() != null) {
			totalPrice = currencyFormat.format(bi.getTotalPrice());
		}
		String invoicePrice = "";
		if (bi.getInvoicePrice() != null) {
			invoicePrice = currencyFormat.format(bi.getInvoicePrice());
		}
		
		PDFFormatterUtil.addToTablePaddingCell(table, 3, 1);
		PDFFormatterUtil.addToTable(table, service, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, date, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, description, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, notes, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		if (showPercentColumn) {
			PDFFormatterUtil.addToTable(table, percentage, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		}
		PDFFormatterUtil.addToTable(table, qty, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, unitPrice, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, totalPrice, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, invoicePrice, FONT_TABLE_VALUE_NORMAL, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	}
	
	private void populateTableRelatedBillingItemAccountHeader(PdfPTable table, BillingItem bi, int columnCount) {
		PDFFormatterUtil.addToTablePaddingCell(table, 2, 1);
		PDFFormatterUtil.addToTable(table, bi.getLabName(), FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 4, 1);
		PDFFormatterUtil.addToTablePaddingCell(table, columnCount - 6, 1);
		
		PDFFormatterUtil.addToTablePaddingCell(table, 2, 1);
		PDFFormatterUtil.addToTable(table, formatAccountNumber(bi.getAccountNumberDisplay(), bi.getAccountName()), FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 4, 1);
		PDFFormatterUtil.addToTablePaddingCell(table, columnCount - 6, 1);
	}
	
	private void populateTableRelatedBillingItem(PdfPTable table, BillingItem bi, boolean showPercentColumn) {
		String service = "";
		if (bi.getCategory() != null) {
			service = bi.getCategory();
		}
		String date = formatDate(bi.getCompleteDate(), DATE_OUTPUT_SLASH);
		String description = "";
		if (bi.getDescription() != null) {
			description = bi.getDescription();
		}
		String notes = "";
		if (bi.getNotes() != null) {
			notes = bi.getNotes();
		}
		String percentage = "";
		if (showPercentColumn) {
			percentage = bi.getPercentageDisplay();
		}
		String qty = "";
		if (bi.getQty() != null) {
			qty = bi.getQty().toString();
		}
		String unitPrice = "";
		if (bi.getUnitPrice() != null) {
			unitPrice = currencyFormat.format(bi.getUnitPrice());
		}
		String totalPrice = "";
		if (bi.getTotalPrice() != null) {
			totalPrice = currencyFormat.format(bi.getTotalPrice());
		}
		String invoicePrice = "";
		if (bi.getInvoicePrice() != null) {
			invoicePrice = currencyFormat.format(bi.getInvoicePrice());
		}
		
		PDFFormatterUtil.addToTablePaddingCell(table, 3, 1);
		PDFFormatterUtil.addToTable(table, service, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, date, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, description, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, notes, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		if (showPercentColumn) {
			PDFFormatterUtil.addToTable(table, percentage, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		}
		PDFFormatterUtil.addToTable(table, qty, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, unitPrice, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, totalPrice, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTable(table, invoicePrice, FONT_TABLE_VALUE_RELATED_ITEMS, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	}
	
	private ArrayList<Element> makeLabInfoHeader(Lab lab, BillingAccount billingAccount) {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		PDFFormatterUtil.addPhrase(elements, lab.getName(false, true), FONT_HEADER);
		PDFFormatterUtil.addPhrase(elements, formatAccountNumber(billingAccount.getAccountNumber(), billingAccount.getAccountName()), FONT_HEADER);		
		
		return elements;
	}
	
	private ArrayList<Element> makePersonalAddressHeader(Lab lab, BillingAccount billingAccount) {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		if (isDNASeqCore && billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")) {
			PDFFormatterUtil.addPhrase(elements, lab.getName(false, true), FONT_HEADER);
		}
		if (lab.getContactName() != null && !lab.getContactName().equals("")) {
			PDFFormatterUtil.addPhrase(elements, "ATTN: " + lab.getContactName(), FONT_HEADER);
		}
		String contactEmail = lab.getBillingNotificationEmail();
	    if (contactEmail != null && !contactEmail.trim().equals("")) {
	    	PDFFormatterUtil.addPhrase(elements, contactEmail, FONT_HEADER);
	    }
		if (lab.getContactAddress2() != null && !lab.getContactAddress2().equals("")) {
			PDFFormatterUtil.addPhrase(elements, lab.getContactAddress2(), FONT_HEADER);
		}
		if (lab.getContactAddress() != null && !lab.getContactAddress().equals("")) {
			PDFFormatterUtil.addPhrase(elements, lab.getContactAddress(), FONT_HEADER);
		}
		if (lab.getContactCity() != null && lab.getContactZip() != null && lab.getContactCountry() != null) {
			// No state => non-U.S. address
			if (lab.getContactCodeState() == null) {
				PDFFormatterUtil.addPhrase(elements, lab.getContactCity() + " " + lab.getContactZip() + " " + lab.getContactCountry(), FONT_HEADER);
			} else {
				PDFFormatterUtil.addPhrase(elements, lab.getContactCity() + ", " + lab.getContactCodeState() + " " + lab.getContactZip() + ", " + lab.getContactCountry(), FONT_HEADER);
			}
		}		
		
		return elements;
	}
	
	private ArrayList<Element> makeAddressHeader(String address) {
		return makeAddressHeader(address, FONT_HEADER);
	}
	
	private ArrayList<Element> makeAddressHeader(String address, Font font) {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		String addressArray[] = address.split("\r");
		for (String s : addressArray) {
			PDFFormatterUtil.addPhrase(elements, s, font);
		}
		
		return elements;
	}
	
	private ArrayList<Element> makeAddressElements(String address, Font font) {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		String addressArray[] = address.split("\r");
		for (String s : addressArray) {
			elements.add(new Phrase(s, font));
		}
		
		return elements;		
	}
	
	private Paragraph makeInvoiceHeaderNote(BillingAccount billingAccount) {
		Paragraph note = new Paragraph();
		note.setAlignment(Element.ALIGN_LEFT);
		note.setFont(FONT_HEADER);
		
		String text;
		if (billingAccount.getIsPO().equals("Y") && billingAccount.getIsCreditCard().equals("N")) {
			text = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(coreFacility.getIdCoreFacility(), null, PropertyDictionary.INVOICE_HEADER_NOTE_PO);
		} else {
			text = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(coreFacility.getIdCoreFacility(), null, PropertyDictionary.INVOICE_HEADER_NOTE);
		}
		
		if (text != null) {
			note.add(text);
		} else {
			note.add("");
		}
		
		return note;
	}
	
	private String makeInvoiceFooterNote() {
		return PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(coreFacility.getIdCoreFacility(), null, PropertyDictionary.INVOICE_FOOTER_NOTE);
	}
	
	private String formatAccountNumber(String accountNumber, String accountName) {
		StringBuilder result = new StringBuilder("Account");
		if (accountNumber != null && !accountNumber.equals("")) {
			result.append(" " + accountNumber);
		}
		if (accountName != null && !accountName.equals("")) {
			result.append(" (" + accountName + ")");
		}
		return result.toString();
	}
	
	private Invoice retrieveInvoice(BillingPeriod billingPeriod, CoreFacility coreFacility, BillingAccount billingAccount) {
		String queryString = "from Invoice where idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount and idCoreFacility=:idCoreFacility";
		Query query = sess.createQuery(queryString);
		query.setParameter("idBillingPeriod", billingPeriod.getIdBillingPeriod());
		query.setParameter("idBillingAccount", billingAccount.getIdBillingAccount());
		query.setParameter("idCoreFacility", coreFacility.getIdCoreFacility());
		return (Invoice) query.uniqueResult();
	}
	
	private boolean determineShowPercentColumn(Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
		for (Iterator reqIter = requestMap.keySet().iterator(); reqIter.hasNext();) {
			String requestNumber = (String) reqIter.next();
			List billingItems = (List) billingItemMap.get(requestNumber);
			BigDecimal totalPriceForRequest = new BigDecimal(0);
			for (Iterator biIter = billingItems.iterator(); biIter.hasNext();) {
				BillingItem bi = (BillingItem) biIter.next();
				if (!bi.getPercentageDisplay().equals("100.0%")) {
					return true;
				}
			}
		}
		
		return false;
	}

}
