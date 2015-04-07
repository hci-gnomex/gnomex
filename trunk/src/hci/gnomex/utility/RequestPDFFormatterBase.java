package hci.gnomex.utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyPlatformApplication;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;

public abstract class RequestPDFFormatterBase {
	
	protected SecurityAdvisor  	secAdvisor;
	protected Request          	request;
	protected AppUser          	appUser;
	protected BillingAccount   	billingAccount;
	protected DictionaryHelper 	dictionaryHelper;
	protected Session			sess;
	protected boolean			dnaSamples;
	
	private static final float REQUEST_CATEGORY_IMAGE_WIDTH = 10;
	private static final float REQUEST_CATEGORY_IMAGE_HEIGHT = 10;
	private static final float PROPERTY_DESCRIPTION_INDENT = 30; 
	
	protected RequestPDFFormatterBase(SecurityAdvisor secAdvisor, Request request, AppUser appUser, BillingAccount billingAccount, DictionaryHelper dictionaryHelper, Session sess) {
		this.secAdvisor = secAdvisor;
		this.request = request;
		this.appUser = appUser;
		this.billingAccount = billingAccount;
		this.dictionaryHelper = dictionaryHelper;
		this.sess = sess;
		
		dnaSamples = determineIfAllDNASamples(request.getSamples());
	}
	
	public Element makeTitleHeader() {
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_CENTER);
		title.setFont(RequestPDFFormatter.FONT_TITLE);
		
		// Request Category Icon
		Image image = PDFFormatterUtil.makeRequestCategoryImage(dictionaryHelper, request, REQUEST_CATEGORY_IMAGE_WIDTH, REQUEST_CATEGORY_IMAGE_HEIGHT);
		if (image != null) {
			title.add(new Chunk(image, 0, 0, false));
			title.add(new Chunk("  "));
		}		
		
		title.add(request.getNumber() + "   ");
		title.add(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + (request.getIsExternal() != null && request.getIsExternal().equals("Y") ? "" :  " Request"));
		
		return title;
	}
	
	public Element makeApplicationHeader() {
		Paragraph header = new Paragraph();
		header.setAlignment(Element.ALIGN_CENTER);
		header.setFont(RequestPDFFormatter.FONT_APPLICATION);
		
		if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
			header.add("External Experiment");
		}
		
		if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
			if (!header.isEmpty()) header.add(Chunk.NEWLINE);
			header.add(dictionaryHelper.getApplication(request.getCodeApplication()));
			if (request.getCodeApplication().equals("OTHER") && request.getApplicationNotes() != null && !request.getApplicationNotes().equals("")) {
				header.add(" - " + request.getApplicationNotes());
	        }
		}
		
		return header;
	}
	
	public Element makeApplicationSubHeader() {
		Paragraph header = new Paragraph();
		header.setAlignment(Element.ALIGN_CENTER);
		header.setFont(RequestPDFFormatter.FONT_APPLICATION_SUB);
		
		// Sample type
		if (request.getSamples().size() > 0) {
			Sample s = (Sample) request.getSamples().iterator().next();
        	if (s.getIdSampleType() != null) {
        		header.add(dictionaryHelper.getSampleType(s.getIdSampleType()));
        	}
		}		
		
		return header;
	}
	
	protected Element makeRequestTable() {
		PdfPTable table = new PdfPTable(6);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		String userName = "";
	    String phone = "";
	    String email = "";
	    if (appUser != null) {
	    	userName = (appUser.getFirstName() != null ? appUser.getFirstName() : "") + " " + (appUser.getLastName() != null ? appUser.getLastName() : "");
	    	phone    = appUser.getPhone();
	    	email    = appUser.getEmail();
	    }
	    
	    String accountName = "";
	    String accountNumber = "";
	    if (billingAccount != null) {
	    	accountName = billingAccount.getAccountName();
	    	if (!this.secAdvisor.isGuest() && (request.getIsExternal() == null || request.getIsExternal().equals("N"))) {
	    		// Don't show the account number if the user is logged in as guest or request is external
	    		accountNumber = billingAccount.getAccountNumber();
	    	}
	    }
	    
	    String date = "";
	    if (request.getCreateDate() != null) {
	    	date = request.formatDate(request.getCreateDate());
	    }
	    
	    String lastModifyDate = "";
	    if (request.getLastModifyDate() != null) {
	    	lastModifyDate = request.formatDate(request.getLastModifyDate());
	    }
	    
	    String labName = "";
	    if (request.getLab() != null) {
	    	labName = request.getLab().getName(false, false);
	    }
	    
	    // Populate table
	    PDFFormatterUtil.addToTable(table, "Requester", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, userName, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Lab", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, labName, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Phone", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, phone, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);	    
	    
	    PDFFormatterUtil.addToTable(table, "Account", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, accountName, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Email", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, email, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Account Number", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, accountNumber, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Date", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, date, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
	    PDFFormatterUtil.addToTable(table, "Last Modified", RequestPDFFormatter.FONT_REQUEST_TABLE_FIELD, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 1, 1);
	    PDFFormatterUtil.addToTable(table, lastModifyDate, RequestPDFFormatter.FONT_REQUEST_TABLE_VALUE, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 2, 1);
	    
		return table;
	}
	
	protected Element makeTableDefault() {
		Set samples = request.getSamples();
		
		PdfPTable table = new PdfPTable(2);
		Font tableHeaderFont = RequestPDFFormatter.FONT_TABLE_HEADERS_SMALL;
		Font tableValueFont = RequestPDFFormatter.FONT_TABLE_VALUES_SMALL;
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, RequestPDFFormatter.HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, RequestPDFFormatter.HEADER_SAMPLE_NAME, tableHeaderFont);
		
		// Populate table
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			
			// Get data
			String sampleID = "";
			if (s.getNumber() != null) {
				sampleID = s.getNumber();
			}
			String sampleName = "";
			if (s.getName() != null) {
				sampleName = s.getName();
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
		}
		
		return table;	
	}
	
	protected ArrayList<Element> makeExperimentProperties() {	
		TreeSet<PropertyEntry> properties = new TreeSet<PropertyEntry>(new PropertyEntryComparator());
		for (Iterator<PropertyEntry> propsIter = getExperimentPropertyEntries().iterator(); propsIter.hasNext();) {
			properties.add(propsIter.next());
		}
		
		ArrayList<Element> propertiesDisplay = new ArrayList<Element>();
		boolean firstProperty = true;
		for (Iterator sortedPropsIter = properties.iterator(); sortedPropsIter.hasNext();) {
			PropertyEntry entry = (PropertyEntry) sortedPropsIter.next();
			
			if (!firstProperty) propertiesDisplay.add(Chunk.NEWLINE);
			String type = entry.getProperty().getCodePropertyType();
			
			// Property description
			Property property = entry.getProperty();
			if (property.getDescription() != null && !property.getDescription().trim().equals("")) {
				Paragraph description = new Paragraph();
				description.setIndentationLeft(PROPERTY_DESCRIPTION_INDENT);
				description.add(new Phrase(property.getDescription(), RequestPDFFormatter.FONT_PROPERTY_TEXT_VALUE));
				propertiesDisplay.add(description);
			}
			
			if (type.equals(PropertyType.URL)) {
				for (Element e : makeSamplePropertyURL(entry)) {
					propertiesDisplay.add(e);
				}
			}
			else if (type.equals(PropertyType.TEXT)) {
				for (Element e : makeSamplePropertyTEXT(entry)) {
					propertiesDisplay.add(e);
				}
			}
			else if (type.equals(PropertyType.CHECKBOX)) {
				for (Element e : makeSamplePropertyCHECKBOX(entry)) {
					propertiesDisplay.add(e);
				}
			}
			else if (type.equals(PropertyType.OPTION)) {
				for (Element e : makeSamplePropertyOPTION(entry)) {
					propertiesDisplay.add(e);
				}
			}
			else if (type.equals(PropertyType.MULTI_OPTION)) {
				for (Element e : makeSamplePropertyMULTIOPTION(entry)) {
					propertiesDisplay.add(e);
				}
			}
			firstProperty = false;
		}
		
		return propertiesDisplay;
	}
	
	protected class PropertyEntryComparator implements Comparator<PropertyEntry> {

		@Override
		public int compare(PropertyEntry pe1, PropertyEntry pe2) {
			if (pe1 == null && pe2 == null) {
				return 0;
			} else if(pe1 == null) {
				return 1;
			} else if (pe2 == null) {
				return -1;
			} else {
				Property p1 = pe1.getProperty();
				Property p2 = pe2.getProperty();
				Integer so1 = (p1.getSortOrder() != null && !p1.getSortOrder().toString().equals("")) ? p1.getSortOrder() : new Integer(999999);
				Integer so2 = (p2.getSortOrder() != null && !p2.getSortOrder().toString().equals("")) ? p2.getSortOrder() : new Integer(999999);
				String n1 = p1.getName();
				String n2 = p2.getName();
				
				if (so1.intValue() < so2.intValue()) {
					return -1;
				} else if (so1.intValue() > so2.intValue()) {
					return 1;
				} else {
					if (n1.equals("Other")) {
						return 1;
					} else if (n2.equals("Other")) {
						return -1;
					} else {
						if (n1.toLowerCase().compareTo(n2.toLowerCase()) < 0) {
							return -1;
						} else if (n1.toLowerCase().compareTo(n2.toLowerCase()) > 0) {
							return 1;
						} else {
							return 0;
						}
					}
				}
			}	
		}
		
	}
	
	protected ArrayList<Element> makeSamplePropertyURL(PropertyEntry entry) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Property property = entry.getProperty();
		
		PdfPTable titleTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(titleTable, property.getDisplay() + ":", RequestPDFFormatter.FONT_PROPERTY_URL_FIELD, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
		
		PdfPTable urlTable = new PdfPTable(1);
		for (Iterator valueIter = entry.getValues().iterator(); valueIter.hasNext();) {
			PropertyEntryValue value = (PropertyEntryValue) valueIter.next();
			if (!value.getUrl().trim().equals("")) {
				Chunk aliasUrl = new Chunk(value.getUrlDisplay() + " (", RequestPDFFormatter.FONT_PROPERTY_URL_VALUE_ALIAS_URL);
				Anchor realUrlAnchor = new Anchor(value.getUrl(), RequestPDFFormatter.FONT_PROPERTY_URL_VALUE_REAL_URL);
				realUrlAnchor.setReference(value.getUrl());
				Chunk closing = new Chunk(")", RequestPDFFormatter.FONT_PROPERTY_URL_VALUE_ALIAS_URL);
				Phrase combinedPhrase = new Phrase();
				combinedPhrase.add(aliasUrl);
				combinedPhrase.add(realUrlAnchor);
				combinedPhrase.add(closing);
				PDFFormatterUtil.addToTable(urlTable, new PdfPCell(combinedPhrase), Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
			}
		}
		
		elements.add(PDFFormatterUtil.combineTables(titleTable, urlTable)); 
		
		return elements;
	}
	
	protected ArrayList<Element> makeSamplePropertyTEXT(PropertyEntry entry) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Property property = entry.getProperty();
		
		PdfPTable nameTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(nameTable, property.getDisplay() + ":", RequestPDFFormatter.FONT_PROPERTY_TEXT_FIELD, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
		
		PdfPTable textTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(textTable, entry.getValueForDisplay(), RequestPDFFormatter.FONT_PROPERTY_TEXT_VALUE, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
				
		elements.add(PDFFormatterUtil.combineTables(nameTable, textTable, Element.ALIGN_TOP, Element.ALIGN_TOP));
		
		return elements;
	}
	
	protected ArrayList<Element> makeSamplePropertyCHECKBOX(PropertyEntry entry) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Property property = entry.getProperty();
		
		PdfPTable nameTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(nameTable, property.getDisplay(), RequestPDFFormatter.FONT_PROPERTY_CHECKBOX_FIELD, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
		
		PdfPTable checkboxTable = new PdfPTable(5);
		PDFFormatterUtil.addToTable(checkboxTable, entry.getValueForDisplay().equals("Y") ? "X" : "", RequestPDFFormatter.FONT_PROPERTY_CHECKBOX_VALUE, Element.ALIGN_CENTER, Element.ALIGN_TOP, true, true, true, true, BaseColor.BLACK, 1, 1);
		PDFFormatterUtil.addToTablePaddingCell(checkboxTable, 4, 1);
			
		elements.add(PDFFormatterUtil.combineTables(nameTable, checkboxTable));
		
		return elements;
	}
	
	protected ArrayList<Element> makeSamplePropertyOPTION(PropertyEntry entry) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Property property = entry.getProperty();
		
		PdfPTable nameTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(nameTable, property.getDisplay(), RequestPDFFormatter.FONT_PROPERTY_OPTION_FIELD, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
		
		PdfPTable optionTable = new PdfPTable(5);
		PDFFormatterUtil.addToTable(optionTable, entry.getValueForDisplay(), RequestPDFFormatter.FONT_PROPERTY_OPTION_VALUE, Element.ALIGN_LEFT, Element.ALIGN_TOP, true, true, true, true, BaseColor.BLACK, 5, 1);
			
		elements.add(PDFFormatterUtil.combineTables(nameTable, optionTable));
		
		return elements;
	}
	
	protected ArrayList<Element> makeSamplePropertyMULTIOPTION(PropertyEntry entry) {
		ArrayList<Element> elements = new ArrayList<Element>();
		Property property = entry.getProperty();
		
		PdfPTable nameTable = new PdfPTable(1);
		PDFFormatterUtil.addToTable(nameTable, property.getDisplay(), RequestPDFFormatter.FONT_PROPERTY_OPTION_FIELD, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 1, 1);
		
		PdfPTable optionsTable = new PdfPTable(5);
		for (Iterator optionsIter = property.getOptions().iterator(); optionsIter.hasNext();) {
			PropertyOption option = (PropertyOption) optionsIter.next();
			boolean checked = false;
			if (entry.getValueForDisplay().indexOf(option.getDisplay()) != -1) {
				checked = true;
			}
			PDFFormatterUtil.addToTable(optionsTable, checked ? "X" : "", RequestPDFFormatter.FONT_PROPERTY_OPTION_VALUE, Element.ALIGN_CENTER, Element.ALIGN_TOP, true, true, true, true, BaseColor.BLACK, 1, 1);
			PDFFormatterUtil.addToTable(optionsTable, option.getDisplay(), RequestPDFFormatter.FONT_PROPERTY_OPTION_VALUE, Element.ALIGN_LEFT, Element.ALIGN_TOP, false, false, false, false, BaseColor.BLACK, 4, 1);
		}
		
		elements.add(PDFFormatterUtil.combineTables(nameTable, optionsTable));
		
		return elements;
	}
	
	protected boolean determineIfAllDNASamples(Set samples) {
		boolean dnaSamples = false;
		// Figure out if all samples are DNA samples
		int dnaSampleCount = 0;
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			String sampleType = dictionaryHelper.getSampleType(s.getIdSampleType());
			if (sampleType != null && sampleType.matches(".*DNA.*")) {
				dnaSampleCount++;
			}
		}
		if (dnaSampleCount == request.getSamples().size()) {
			dnaSamples = true;
		}
		return dnaSamples;
	}
	
	private ArrayList<PropertyEntry> getExperimentPropertyEntries() {
		ArrayList<PropertyEntry> props = new ArrayList<PropertyEntry>();
		
		for (Iterator iter = dictionaryHelper.getPropertyList().iterator(); iter.hasNext();) {
			Property prop = (Property) iter.next();
			
			if (prop.getForRequest() == null || !prop.getForRequest().equals("Y")) {
				continue;
			}
			
			PropertyEntry entry = null;
			
			if (request.getPropertyEntries() != null) {
				for (Iterator reqProps = request.getPropertyEntries().iterator(); reqProps.hasNext();) {
					PropertyEntry reqPropEntry = (PropertyEntry) reqProps.next();
					if (reqPropEntry.getIdProperty().equals(prop.getIdProperty())) {
						entry = reqPropEntry;
						break;
					}
				}
			}
			
			// Skip if property has no values for samples and is not active
			if (entry == null && prop.getIsActive().equals("N")) {
				continue;
			}
			
            boolean include = false;
            if (prop.getPlatformApplications() != null && prop.getPlatformApplications().size() > 0 && request.getRequestCategory() != null) {
            	for (Iterator paIter = prop.getPlatformApplications().iterator(); paIter.hasNext();) {
            		PropertyPlatformApplication pa = (PropertyPlatformApplication) paIter.next();
            		if (pa.getCodeRequestCategory().equals(request.getCodeRequestCategory()) &&
                       (pa.getApplication() == null || pa.getApplication().getCodeApplication().equals(request.getCodeApplication()))) {
            			include = true;
            			break;
            		}   
            	}
            }
            if (request.getRequestCategory() != null && prop.getIdCoreFacility() != null && !request.getRequestCategory().getIdCoreFacility().equals(prop.getIdCoreFacility())) {
            	include = false;
            }
            if (!include) {
            	continue;
            }
            
            if (entry == null) {
                entry = new PropertyEntry();
                entry.setProperty(prop);
                entry.setValue("");
                if (prop.getCodePropertyType().equals(PropertyType.URL)) {
                	PropertyEntryValue value = new PropertyEntryValue();
                	value.setValue("");
                	HashSet<PropertyEntryValue> values = new HashSet<PropertyEntryValue>(1);
                	values.add(value);
                	entry.setValues(values);
                }
            }
			
			props.add(entry);
		}
		
		return props;
	}

}
