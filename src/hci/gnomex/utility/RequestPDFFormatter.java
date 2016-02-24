package hci.gnomex.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.Session;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;

import hci.dictionary.utility.DictionaryManager;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryType;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.security.SecurityAdvisor;

public class RequestPDFFormatter extends RequestPDFFormatterBase {
	
	private String amendState;
	
	// Constants for common header titles
	protected static final String HEADER_SAMPLE_ID = "Sample ID";
	protected static final String HEADER_SAMPLE_NAME = "Sample Name";
	protected static final String HEADER_SAMPLE_TYPE = "Sample Type";
	protected static final String HEADER_PLATE = "Plate";
	protected static final String HEADER_WELL = "Well";
	protected static final String HEADER_CONCENTRATION = "Conc.";
	protected static final String HEADER_NUCLEIC_EXTRACTION_METHOD = "Nucl. Acid Extraction Method";
	protected static final String HEADER_CC_NUMBER = "CC Number";
	protected static final String HEADER_QUALITY_CONCENTRATION = "Conc. ng/uL";
	protected static final String HEADER_QUALITY_RATIO = "260/ 230";
	protected static final String HEADER_QUALITY_METHOD = "QC meth";
	protected static final String HEADER_QUALITY_FRAGMENT_ANALYSIS = "Frag size";
	protected static final String HEADER_QUALITY_RIN_NUMBER = "RIN #";
	
	protected static final BaseColor LABEL_BLUE = new BaseColor( 52, 102, 153 );
	protected static final float     TABLE_PERCENT_WIDTH_100 = 100;
	protected static final float     TABLE_PERCENT_WIDTH_80 = 80;
  
	// Font library
	protected static final Font FONT_TITLE = new Font(FontFamily.HELVETICA, 14, Font.BOLD, LABEL_BLUE);
	protected static final Font FONT_APPLICATION = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_APPLICATION_SUB = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_REQUEST_TABLE_FIELD = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, LABEL_BLUE);
	protected static final Font FONT_REQUEST_TABLE_VALUE = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_NOTES_TITLE = new Font(FontFamily.HELVETICA, 12, Font.BOLD, LABEL_BLUE);
	protected static final Font FONT_NOTES = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_TABLE_TITLE = new Font(FontFamily.HELVETICA, 12, Font.BOLD, LABEL_BLUE);
	protected static final Font FONT_TABLE_HEADERS_NORMAL = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
	protected static final Font FONT_TABLE_HEADERS_SMALL = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	protected static final Font FONT_TABLE_HEADERS_VERY_SMALL = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
	protected static final Font FONT_TABLE_VALUES_NORMAL = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_TABLE_VALUES_SMALL = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_TABLE_VALUES_VERY_SMALL = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_CC_NUMBER_EMBED_NORMAL = new Font(FontFamily.HELVETICA, 10, Font.UNDERLINE, BaseColor.BLUE);
	protected static final Font FONT_CC_NUMBER_EMBED_SMALL = new Font(FontFamily.HELVETICA, 8, Font.UNDERLINE, BaseColor.BLUE);
	protected static final Font FONT_CC_NUMBER_EMBED_VERY_SMALL = new Font(FontFamily.HELVETICA, 7, Font.UNDERLINE, BaseColor.BLUE);
	protected static final Font FONT_PROPERTY_URL_FIELD = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, LABEL_BLUE);
	protected static final Font FONT_PROPERTY_URL_VALUE_ALIAS_URL = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_PROPERTY_URL_VALUE_REAL_URL = new Font(FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLUE);
	protected static final Font FONT_PROPERTY_TEXT_FIELD = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, LABEL_BLUE);
	protected static final Font FONT_PROPERTY_TEXT_VALUE = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_PROPERTY_CHECKBOX_FIELD = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, LABEL_BLUE);
	protected static final Font FONT_PROPERTY_CHECKBOX_VALUE = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
	protected static final Font FONT_PROPERTY_OPTION_FIELD = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, LABEL_BLUE);
	protected static final Font FONT_PROPERTY_OPTION_VALUE = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

	public RequestPDFFormatter(SecurityAdvisor secAdvisor, Request request, AppUser appUser, BillingAccount billingAccount, DictionaryHelper dictionaryHelper, Session sess, String amendState) {
		super(secAdvisor, request, appUser, billingAccount, dictionaryHelper, sess);
		this.amendState = amendState;
	}
	
	@Override
	public Element makeApplicationHeader() {
		Paragraph header = (Paragraph) super.makeApplicationHeader();
		
		if (request.getCodeIsolationPrepType() != null && !request.getCodeIsolationPrepType().equals("")) {
			RequestCategory rc = dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory());
			RequestCategoryType rct = rc.getCategoryType();
	        if (rct.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_ISOLATION)) {
	        	header.add(" " + dictionaryHelper.getIsolationPrepType(request.getCodeIsolationPrepType()));
	        }
		}
		return header;
	}
	
	@Override
	public Element makeApplicationSubHeader() {
		Paragraph header = (Paragraph) super.makeApplicationSubHeader();
		
		// Additional info on sample type
		if (request.getSamples().size() > 0) {
			Sample s = (Sample) request.getSamples().iterator().next();
        	if (s.getOtherSamplePrepMethod() != null && s.getOtherSamplePrepMethod().length() > 0) {
        		if (!header.isEmpty()) header.add(Chunk.NEWLINE);
                header.add("Sample Nucl. Extraction Method: " + s.getOtherSamplePrepMethod());
        	}
		}		

    String lastCodeChipType = "";
		if (request.getSamples().size() > 0) {
      Sample s = (Sample) request.getSamples().iterator().next();
          if (s.getCodeBioanalyzerChipType() != null && !s.getCodeBioanalyzerChipType().equals( lastCodeChipType ) ) {
            if (!header.isEmpty()) header.add(Chunk.NEWLINE);
                header.add("Assay Type: " + dictionaryHelper.getChipTypeName(s.getCodeBioanalyzerChipType()));
                lastCodeChipType = s.getCodeBioanalyzerChipType();
          }
    }   
		
		// Number of seq cycles and seq run type
		if (request.getSequenceLanes().iterator().hasNext() && (request.getIsExternal() != null && !request.getIsExternal().equals("Y"))) {
			SequenceLane lane = (SequenceLane) request.getSequenceLanes().iterator().next();
			if (lane.getIdNumberSequencingCyclesAllowed()!= null) {
				if (!header.isEmpty()) header.add(Chunk.NEWLINE);
				header.add(dictionaryHelper.getNumberSequencingCyclesAllowed(lane.getIdNumberSequencingCyclesAllowed()));
			}
		}
		
		if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
			if (request.getSamples().iterator().hasNext()) {
				boolean corePrepLib = true;
	            Sample smp = (Sample) request.getSamples().iterator().next(); 
	            if (smp.getSeqPrepByCore() != null && smp.getSeqPrepByCore().equals("N")) {
	            	corePrepLib = false;
	            }
	            if (!corePrepLib) {
	            	if (!header.isEmpty()) header.add(Chunk.NEWLINE);
	            	if (request.getHasPrePooledLibraries() != null && request.getHasPrePooledLibraries().equals("Y") && request.getNumPrePooledTubes() != null) {
	            		header.add("Library Prepared By Client, " + request.getNumPrePooledTubes().toString() + " Pre-Pooled Tubes");
	            	} else {
	            		header.add("Library Prepared By Client");
	            	}
	            }
			}
		}
		
		if (request.getCaptureLibDesignId() != null && !request.getCaptureLibDesignId().equals("")) {
			if (!header.isEmpty()) header.add(Chunk.NEWLINE);
			header.add("Custom Design Id: " + request.getCaptureLibDesignId());
		}
		
		return header;
	}

	private Element makeDescriptionNotes() {
		Paragraph notes = null;
		
		String description = PDFFormatterUtil.stripRichText(request.getDescription());
		
		if (!description.trim().equals("")) {
			notes = new Paragraph();
			notes.add(new Phrase("Experiment Description: ", FONT_NOTES_TITLE));
	    	notes.add(new Phrase(description, FONT_NOTES));
		}
		
		return notes;
	}
	
	private Element makeCoreFacilityNotes() {
		Paragraph notes = null;
		
		String instructions = PDFFormatterUtil.stripRichText(request.getCorePrepInstructions());
    	
		if (!instructions.trim().equals("")) {
			notes = new Paragraph();
			notes.add(new Phrase("Notes for core facility: ", FONT_NOTES_TITLE));
	    	notes.add(new Phrase(instructions, FONT_NOTES));
		}
		
		return notes;
	}
	
	private ArrayList<Element> makeNotes() {
		ArrayList<Element> notes = new ArrayList<Element>();
		
		// Show description for external experiments
		if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
			Element description = makeDescriptionNotes();
			if (description != null) notes.add(description);
		}
		// Show core facility notes for internal experiments
		else {
			Element coreFacilityNotes = makeCoreFacilityNotes();
			if (coreFacilityNotes != null) notes.add(coreFacilityNotes);
		}
		
		return notes;
	}
	
	public ArrayList<Element> makeContent() {
		ArrayList<Element> tables = new ArrayList<Element>();
		
		// Header
		tables.add(makeTitleHeader());
		tables.add(Chunk.NEWLINE);
		tables.add(makeApplicationHeader());
		tables.add(Chunk.NEWLINE);
		tables.add(makeApplicationSubHeader());
		
		// Request Table
		tables.add(Chunk.NEWLINE);
		tables.add(makeRequestTable());
		
		// Experiment Properties
		ArrayList<Element> properties = makeExperimentProperties();
		if (!properties.isEmpty()) {
			tables.add(Chunk.NEWLINE);
			tables.add(PDFFormatterUtil.makeTableTitle("Properties", FONT_TITLE));
		}
		for (Element e : properties) {
			tables.add(e);
		}
		
		// General notes
		ArrayList<Element> notes = makeNotes();
		if (!notes.isEmpty()) {
			tables.add(Chunk.NEWLINE);
			for (Element e : notes) {
				tables.add(e);
			}
		}
		
		// Experiment type-specific notes and tables
		if (request.getRequestCategory().getType() != null) {
			String requestCategoryType = request.getRequestCategory().getType();
			String requestCategory = request.getCodeRequestCategory();
			
			if (requestCategoryType.equals(RequestCategoryType.TYPE_CAP_SEQ)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableCAPSEQ());
			} 
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_CLINICAL_SEQUENOM)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableCLINSEQ());
			} 
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_ISCAN)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableISCAN());
			} 
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_HISEQ) || requestCategoryType.equals(RequestCategoryType.TYPE_MISEQ)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableIllumina());
				tables.add(Chunk.NEWLINE);
//				tables.add(PDFFormatterUtil.makeTableTitle("Covaris", FONT_TITLE));
//				tables.add(makeTableCovaris());
//				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Sequence Lanes", FONT_TITLE));
				makeSequenceLaneSection(tables);
				
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_ISOLATION)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableISOLATION());
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_MICROARRAY)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableMICROARRAY());
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Labeled Samples", FONT_TITLE));
				tables.add(makeTableLabeledSamples());
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Hybridizations", FONT_TITLE));
				tables.add(makeTableHybridizations());
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_NANOSTRING)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableNANOSTRING());
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_QC)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableQC());				
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_SEQUENOM)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableSEQUENOM());
			}
			else if (requestCategoryType.equals(RequestCategoryType.TYPE_GENERIC)) {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableGENERIC());
			}
			// Simple table used if experiment type is not matched
			else {
				tables.add(Chunk.NEWLINE);
				tables.add(PDFFormatterUtil.makeTableTitle("Samples (" + request.getSamples().size() + ")", FONT_TITLE));
				tables.add(makeTableDefault());
			}
		}
		
		return tables;
	}
	
	private Element makeTableCAPSEQ() {
		Set samples = request.getSamples();
		boolean isCapSeqPlate = request.isCapSeqPlate();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		if (isCapSeqPlate && showCCNumber) {
			table = new PdfPTable(11);
		} else if (isCapSeqPlate) {
			table = new PdfPTable(10);
		} else if (showCCNumber) {
			table = new PdfPTable(9);
		} else {
			table = new PdfPTable(8);
		}
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
		
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Container", tableHeaderFont);
		if (isCapSeqPlate) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_PLATE, tableHeaderFont);
			PDFFormatterUtil.addToTableHeader(table, HEADER_WELL, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		}
		
		// Populate table
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			
			// Get data
			String sampleID = "";
			if (s.getNumber() != null) {
				sampleID = s.getNumber();
			}
			String container = (isCapSeqPlate) ? "PLATE" : "TUBE";
			String plate = "";
			String well = "";
			if (isCapSeqPlate) {
				if (s.getASourceWell().getPlate().getLabel() != null) {
					plate = s.getASourceWell().getPlate().getLabel();
				}
				if (s.getASourceWell().getWellName() != null) {
					well = s.getASourceWell().getWellName();
				}
			}
			String sampleName = "";
			if (s.getName() != null) {
				sampleName = s.getName();
			}
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, container, tableValueFont);
			if (isCapSeqPlate) {
				PDFFormatterUtil.addToTableValue(table, plate, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, well, tableValueFont);
			}
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableCLINSEQ() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table; 
		if (showCCNumber) {
			table = new PdfPTable(9);
		} else {
			table = new PdfPTable(8);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_NUCLEIC_EXTRACTION_METHOD, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
		// Populate table
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			
			// Get data
			String sampleID = "";
			if (s.getNumber() != null) {
				sampleID = s.getNumber();
			}
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String conc = "";
			if (s.getConcentration() != null) {
				conc = getFormattedSampleConcentration(s.getConcentration());
		        if (s.getCodeConcentrationUnit() != null && !s.getCodeConcentrationUnit().equals("")) {
		        	conc += " " + s.getCodeConcentrationUnit();
		        }
			}
			String extractMeth = "";
			if (s.getOtherSamplePrepMethod() != null) {
				extractMeth = s.getOtherSamplePrepMethod();
			}
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, extractMeth, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableISCAN() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table; 
		if (showCCNumber) {
			table = new PdfPTable(9);
		} else {
			table = new PdfPTable(8);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_PLATE, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_WELL, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
		// Populate table
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			
			// Get data
			String sampleID = "";
			if (s.getNumber() != null) {
				sampleID = s.getNumber();
			}
			String plate = "";
			if (s.getASourceWell().getPlate().getLabel() != null) {
				plate = s.getASourceWell().getPlate().getLabel();
			}
			String well = "";
			if (s.getASourceWell().getWellName() != null) {
				well = s.getASourceWell().getWellName();
			}
			String sampleName = "";
			if (s.getName() != null) {
				sampleName = s.getName();
			}
			String ccNumber = determineCCNumber(s);
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, plate, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, well, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableISOLATION() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		if (showCCNumber) {
			table = new PdfPTable(3);
		} else {
			table = new PdfPTable(2);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		
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
			String ccNumber = determineCCNumber(s);
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
		}
		
		return table;
	}
	
	private Element makeTableMICROARRAY() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		if (showCCNumber) {
			table = new PdfPTable(10);
		} else {
			table = new PdfPTable(9);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_NUCLEIC_EXTRACTION_METHOD, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
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
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String conc = "";
			if (s.getConcentration() != null) {
				conc = getFormattedSampleConcentration(s.getConcentration());
		        if (s.getCodeConcentrationUnit() != null && !s.getCodeConcentrationUnit().equals("")) {
		        	conc += " " + s.getCodeConcentrationUnit();
		        }
			}
			String extractMeth = "";
			if (s.getOtherSamplePrepMethod() != null) {
				extractMeth = s.getOtherSamplePrepMethod();
			}
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, extractMeth, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableNANOSTRING() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		if (showCCNumber) {
			table = new PdfPTable(10);
		} else {
			table = new PdfPTable(9);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_NUCLEIC_EXTRACTION_METHOD, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
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
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String conc = "";
			if (s.getConcentration() != null) {
				conc = getFormattedSampleConcentration(s.getConcentration());
		        if (s.getCodeConcentrationUnit() != null && !s.getCodeConcentrationUnit().equals("")) {
		        	conc += " " + s.getCodeConcentrationUnit();
		        }
			}
			String extractMeth = "";
			if (s.getOtherSamplePrepMethod() != null) {
				extractMeth = s.getOtherSamplePrepMethod();
			}
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, extractMeth, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;		
	}
	
	private Element makeTableQC() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		if (showCCNumber) {
			table = new PdfPTable(9);
		} else {
			table = new PdfPTable(8);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_VERY_SMALL;
		Font tableValueFont = FONT_TABLE_VALUES_VERY_SMALL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
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
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String conc = "";
			if (s.getConcentration() != null) {
				conc = getFormattedSampleConcentration(s.getConcentration());
		        if (s.getCodeConcentrationUnit() != null && !s.getCodeConcentrationUnit().equals("")) {
		        	conc += " " + s.getCodeConcentrationUnit();
		        }
			}
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;		
	}
	
	private Element makeTableSEQUENOM() {
		Set samples = request.getSamples();
		boolean isSequenomPlate = request.isSequenomPlate();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		if (isSequenomPlate && showCCNumber) {
			table = new PdfPTable(7);
		} else if (isSequenomPlate) {
			table = new PdfPTable(6);
		} else if (showCCNumber) {
			table = new PdfPTable(5);
		} else {
			table = new PdfPTable(4);
		}
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Container", tableHeaderFont);
		if (isSequenomPlate) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_PLATE, tableHeaderFont);
			PDFFormatterUtil.addToTableHeader(table, HEADER_WELL, tableHeaderFont);
		}
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		
		// Populate table
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			
			// Get data
			String sampleID = "";
			if (s.getNumber() != null) {
				sampleID = s.getNumber();
			}
			String container = (isSequenomPlate) ? "PLATE" : "TUBE";
			String plate = "";
			String well = "";
			if (isSequenomPlate) {
				if (s.getASourceWell().getPlate().getLabel() != null) {
					plate = s.getASourceWell().getPlate().getLabel();
				}
				if (s.getASourceWell().getWellName() != null) {
					well = s.getASourceWell().getWellName();
				}
			}
			String ccNumber = determineCCNumber(s);
			String sampleName = "";
			if (s.getName() != null) {
				sampleName = s.getName();
			}
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, container, tableValueFont);
			if (isSequenomPlate) {
				PDFFormatterUtil.addToTableValue(table, plate, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, well, tableValueFont);
			}
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
		}
		
		return table;
	}
	
	private Element makeTableGENERIC() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		if (showCCNumber) {
			table = new PdfPTable(10);
		} else {
			table = new PdfPTable(9);
		}
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_TYPE, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, HEADER_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_NUCLEIC_EXTRACTION_METHOD, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_CONCENTRATION, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RATIO, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_METHOD, tableHeaderFont);
		if (dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_FRAGMENT_ANALYSIS, tableHeaderFont);
		} else {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		
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
			String sampleType = "";
			if (s.getIdSample() != null) {
				sampleType = dictionaryHelper.getSampleType(s);
			}
			String ccNumber = determineCCNumber(s);
			String conc = "";
			if (s.getConcentration() != null) {
				conc = getFormattedSampleConcentration(s.getConcentration());
		        if (s.getCodeConcentrationUnit() != null && !s.getCodeConcentrationUnit().equals("")) {
		        	conc += " " + s.getCodeConcentrationUnit();
		        }
			}
			String extractMeth = "";
			if (s.getOtherSamplePrepMethod() != null) {
				extractMeth = s.getOtherSamplePrepMethod();
			}
			String concentration = "";
			if (s.getQualCalcConcentration() != null) {
				concentration = s.getQualCalcConcentration().toString();
			}
			String ratio = "";
			if (s.getQual260nmTo230nmRatio() != null) {
				ratio = s.getQual260nmTo230nmRatio().toString();
			}
			String qcMeth = "";
			String fragSize = "";
			String rinNum = "";
			if (dnaSamples) {
				if (s.getQualFragmentSizeFrom() != null && !s.getQualFragmentSizeFrom().equals("")) {
					fragSize = s.getQualFragmentSizeFrom() + "-";
				}
				if (s.getQualFragmentSizeTo() != null && !s.getQualFragmentSizeTo().equals("")) {
					fragSize += s.getQualFragmentSizeTo();
				}
			} else {
				if (s.getQualRINNumber() != null) {
					rinNum = s.getQualRINNumber();
				}
			}
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, sampleType, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, extractMeth, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, concentration, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, ratio, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, qcMeth, tableValueFont);
			if (dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, fragSize, tableValueFont);
			} else {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableIllumina() {
		Set samples = request.getSamples();
		boolean showCCNumber = determineShowCCNumber(samples);
		
		PdfPTable table;
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		if (dnaSamples && showCCNumber) {
			table = new PdfPTable(11);
		} else if (dnaSamples) {
			table = new PdfPTable(10);
		} else if (showCCNumber) {
			table = new PdfPTable(12);
		} else {
			table = new PdfPTable(11);
		}
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		if (showCCNumber) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_CC_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, "Sample Conc.", tableHeaderFont);
		if (!dnaSamples) {
			PDFFormatterUtil.addToTableHeader(table, HEADER_QUALITY_RIN_NUMBER, tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, "Multiplex Group #", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Index A", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Index B", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Lib Conc.", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Lib Size", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "# Lanes", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Seq. Date", tableHeaderFont);
		
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
			String ccNumber = determineCCNumber(s);
			String sampleConc = "";
			if (s.getConcentration() != null) {
				sampleConc = getFormattedSampleConcentration(s.getConcentration()) + " /";
			}
			String rinNum = "";
			String multiplexNum = "";
			if (s.getMultiplexGroupNumber() != null && s.getMultiplexGroupNumber().intValue() > 0) {
				multiplexNum = s.getMultiplexGroupNumber().toString();
			}
			String indexA = "";
			if (s.getIdOligoBarcode() != null) {
				indexA = DictionaryManager.getDisplay("hci.gnomex.model.OligoBarcode", s.getIdOligoBarcode().toString());
			} else if (s.getBarcodeSequence() != null) {
				indexA = s.getBarcodeSequence();
			}
			String indexB = "";
			if (s.getIdOligoBarcodeB() != null) {
				indexB = DictionaryManager.getDisplay("hci.gnomex.model.OligoBarcode", s.getIdOligoBarcodeB().toString());
			} else if (s.getBarcodeSequenceB() != null) {
				indexB = s.getBarcodeSequenceB();
			}
			String libConc = "";
			String libSize = "";
			String laneNum = "";
			if (request.getSequenceLanes() != null) {
				int numberOfLanes = 0;
				for (SequenceLane lane : (Set<SequenceLane>) request.getSequenceLanes()) {
					if (lane.getIdSample().equals(s.getIdSample())) {
						numberOfLanes++;
					}
				}
				if (numberOfLanes > 0) {
					laneNum = new Integer(numberOfLanes).toString();
				}
			}
			String seqDate = "";
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			if (showCCNumber) {
				PDFFormatterUtil.addCCNumberCell(ccNumber, table, FONT_CC_NUMBER_EMBED_SMALL, dictionaryHelper);
			}
			PDFFormatterUtil.addToTableValue(table, sampleConc, FONT_TABLE_VALUES_SMALL);
			if (!dnaSamples) {
				PDFFormatterUtil.addToTableValue(table, rinNum, tableValueFont);
			}
			PDFFormatterUtil.addToTableValue(table, multiplexNum, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, indexA, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, indexB, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, libConc, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, libSize, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, laneNum, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, seqDate, tableValueFont);
		}
		
		return table;
	}
	
	private Element makeTableCovaris() {
		Set samples = request.getSamples();
		
		PdfPTable table = new PdfPTable(4);
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Covaris Vol", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Covaris Qty", tableHeaderFont);
		
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
			String covarisVol = "                 /"; // Filled in by hand after printing
			String covarisQty = "";
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
			PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, covarisVol, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, covarisQty, tableValueFont);
		}
		
		return table;
	}
	
	@SuppressWarnings("unchecked")
	private void makeSequenceLaneSection(ArrayList<Element> tables) {
		// Group lanes by create date
		TreeMap< Date, List<SequenceLane> > laneDateMap = new TreeMap< Date, List<SequenceLane> >(new DescendingDateComparator());
		
		// For each lane, add to map's list for this lane's date if one exists or create a new list for this lane's date
		for (Iterator laneIter = request.getSequenceLanes().iterator(); laneIter.hasNext();) {
			SequenceLane lane = (SequenceLane) laneIter.next();
			List<SequenceLane> theLanes = laneDateMap.get(lane.getCreateDate());
			if (theLanes == null) {
				theLanes = new ArrayList<SequenceLane>();
				laneDateMap.put(lane.getCreateDate(), theLanes);
			}
			theLanes.add(lane);
		}
		
		// Now show a lane table for each create date, most recent date first
		for (Iterator dateIter = laneDateMap.keySet().iterator(); dateIter.hasNext();) {
			Date createDate = (Date) dateIter.next();
			List<SequenceLane> theLanes = laneDateMap.get(createDate);
			String caption = "Sequence Lanes added on " + request.formatDate(createDate);
			if (amendState != null && amendState.equals(Constants.AMEND_ADD_SEQ_LANES)) {
				tables.add(makeTableSequenceLanes(theLanes, caption));
				break; // If we are adding lanes, only add the most recent entries, otherwise print all
			}
			tables.add(makeTableSequenceLanes(theLanes, caption));
			
			if (dateIter.hasNext()) {
				tables.add(Chunk.NEWLINE);
			}
		}	
	}
	
	private Element makeTableSequenceLanes(List lanes, String caption) {
		SortedMap multiplexLaneMap = SequenceLane.getMultiplexLaneMap(lanes, null);
		
		PdfPTable table = new PdfPTable(4);
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_80 );
    
		// Headers
		table.setHeaderRows(2);
		PDFFormatterUtil.addToTable(table, caption, tableHeaderFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, table.getNumberOfColumns(), 1);
		PDFFormatterUtil.addToTableHeader(table, "Lane", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "#", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_NAME, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Status", tableHeaderFont);
		
		// Populate table
		int nonMultiplexedLaneCount = 1;
		boolean newLane;
		for (Iterator keyIter = multiplexLaneMap.keySet().iterator(); keyIter.hasNext();) {
			newLane = true;
			String key = (String) keyIter.next();
			Collection theLanes = (Collection) multiplexLaneMap.get(key);
			
			// Print a row for each sequence lane in multiplex lane
			for (Iterator laneIter = theLanes.iterator(); laneIter.hasNext();) {
				SequenceLane lane = (SequenceLane) laneIter.next();
				
				String multiplexGroupID = "";
				if (!key.equals("")) {
					multiplexGroupID = key;
				}
				
				// Get data
				String laneLabel = "";
				if (newLane) {
					if (key.equals("")) {
						laneLabel = new Integer(nonMultiplexedLaneCount++).toString();
					} else {
						laneLabel = multiplexGroupID;
					}
				}
				String num = "";
				if (lane.getNumber() != null) {
					num = lane.getNumber();
				}
				String sampleName = "";
				if (lane.getSample() != null) {
					sampleName = lane.getSample().getName();
				}
				String status = "";
				if (lane.getWorkflowStatusAbbreviated() != null) {
					status = lane.getWorkflowStatusAbbreviated();
				}
				
				// Add data to table
				PDFFormatterUtil.addToTableValue(table, laneLabel, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, num, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, sampleName, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, status, tableValueFont);
				
				newLane = false;
			}
		}
		
		return table;
	}
	
	private Element makeTableLabeledSamples() {
		TreeSet labeledSamples = new TreeSet(new LabeledSampleNumberComparator());
		labeledSamples.addAll(request.getLabeledSamples());
		boolean hasCy5Samples = false;
		for (Iterator iter = labeledSamples.iterator(); iter.hasNext();) {
			LabeledSample labeledSample = (LabeledSample) iter.next();
			if (dictionaryHelper.getLabel(labeledSample.getIdLabel()).equals("Cy5")) {
				hasCy5Samples = true;
				break;
			}
		}
		
		PdfPTable table;
		if (hasCy5Samples) {
			table = new PdfPTable(2);
		} else {
			table = new PdfPTable(1);
		}
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_80 );
    
		// Populate nested tables, only creating Cy5 table if appropriate
		PdfPTable cy3Table = makeTableLabeledSample(labeledSamples, "Cy3");
		PdfPTable cy5Table = null;
		if (hasCy5Samples) {
			cy5Table = makeTableLabeledSample(labeledSamples, "Cy5");
			
			// Add padding cell to one of the tables if necessary
			PDFFormatterUtil.padNestedTables(cy3Table, cy5Table);
		}
		
		// Populate outermost table
		table.addCell(cy3Table);
		if (hasCy5Samples) {
			table.addCell(cy5Table);
		}
		
		return table;
	}
	
	private PdfPTable makeTableLabeledSample(Set labeledSamples, String label) {
		PdfPTable table = new PdfPTable(3);
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		
		// Headers
		table.setHeaderRows(2);
		PDFFormatterUtil.addToTable(table, label, FONT_TABLE_HEADERS_NORMAL, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, 3, 1);
		PDFFormatterUtil.addToTableHeader(table, HEADER_SAMPLE_ID, tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Conc.", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Volume", tableHeaderFont);
		
		// Populate table
		for (Iterator i = labeledSamples.iterator(); i.hasNext();) {
			LabeledSample labeledS = (LabeledSample) i.next();
			
			if (labeledS.getIdLabel() != null && dictionaryHelper.getLabel(labeledS.getIdLabel()).equals(label)) {
				Sample s = labeledS.getSample();
				
				// Get data
				String sampleID = "";
				if (s.getNumber() != null) {
					sampleID = s.getNumber();
				}
				String conc = "";
				String volume = "";
				
				// Add data to table
				PDFFormatterUtil.addToTableValue(table, sampleID, FONT_TABLE_VALUES_SMALL);
				PDFFormatterUtil.addToTableValue(table, conc, tableValueFont);
				PDFFormatterUtil.addToTableValue(table, volume, tableValueFont);
			}
		}
		
		return table;
	}
	
	private Element makeTableHybridizations() {
		Set hybridizations = request.getHybridizations();
		boolean includeCy5 = false;
		if ( request.getCodeRequestCategory() == null || 
			 (dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels() != null && 
			  dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels().intValue() == 2) ) {
			includeCy5 = true;
		}
		
		PdfPTable table;
		Font tableHeaderFont = FONT_TABLE_HEADERS_NORMAL;
		Font tableValueFont = FONT_TABLE_VALUES_NORMAL;
		if (includeCy5) {
			table = new PdfPTable(8);
		} else {
			table = new PdfPTable(7);
		}
		PDFFormatterUtil.formatTable( table, TABLE_PERCENT_WIDTH_100 );
    
		// Headers
		table.setHeaderRows(1);
		PDFFormatterUtil.addToTableHeader(table, "Hyb #", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Cy3 Sample #", tableHeaderFont);
		if (includeCy5) {
			PDFFormatterUtil.addToTableHeader(table, "Cy5 Samples #", tableHeaderFont);
		}
		PDFFormatterUtil.addToTableHeader(table, "Slide", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Array ID", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Slide Source", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Barcode", tableHeaderFont);
		PDFFormatterUtil.addToTableHeader(table, "Row-col", tableHeaderFont);		
		
		// Populate table
		for (Iterator i = hybridizations.iterator(); i.hasNext();) {
			Hybridization h = (Hybridization) i.next();
			
			// Get data
			String hybNum = "";
			if (h.getNumber() != null) {
				hybNum = h.getNumber();
			}
			String cy3SampleID = "";
			if (h.getLabeledSampleChannel1() != null) {
				cy3SampleID = h.getLabeledSampleChannel1().getSample().getNumber();
			}
			String cy5SampleID = "";
			if (includeCy5 && h.getLabeledSampleChannel2() != null) {
				cy5SampleID = h.getLabeledSampleChannel2().getSample().getNumber();
			}
			String slide = "";
			if (h.getIdSlideDesign() != null) {
				slide = dictionaryHelper.getSlideDesignProtocolName(h.getIdSlideDesign());
			}
			String arrayID = "";
			if (h.getIdSlideDesign() != null) {
				arrayID = dictionaryHelper.getSlideDesignName(h.getIdSlideDesign());
			}
			String slideSource = "";
			if (h.getCodeSlideSource() != null) {
				slideSource = dictionaryHelper.getSlideSource(h.getCodeSlideSource());
			}
			String barcode = "";
			String rowCol = "";
			
			// Add data to table
			PDFFormatterUtil.addToTableValue(table, hybNum, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, cy3SampleID, FONT_TABLE_VALUES_SMALL);
			if (includeCy5) {
				PDFFormatterUtil.addToTableValue(table, cy5SampleID, FONT_TABLE_VALUES_SMALL);
			}
			PDFFormatterUtil.addToTableValue(table, slide, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, arrayID, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, slideSource, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, barcode, tableValueFont);
			PDFFormatterUtil.addToTableValue(table, rowCol, tableValueFont);
		}
		
		return table;	
	}
	
	private String getFormattedSampleConcentration(BigDecimal unformattedConcentration) {
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		
		int scale = 3;
		try {
			scale = Integer.parseInt(pdh.getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.SAMPLE_CONCENTRATION_PRECISION));
		} catch (NumberFormatException e) {
		}
		
		return unformattedConcentration.setScale(scale, RoundingMode.HALF_UP).toString();
	}
	
	private boolean determineShowCCNumber(Set samples) {
		for (Iterator i = samples.iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			if (s.getCcNumber() != null && !s.getCcNumber().equals("")) {
				return true;
			}
		}
		return false;
	}
	
	private String determineCCNumber(Sample s) {
		if (s.getCcNumber() != null) {
			return s.getCcNumber();
		} else {
			return "";
		}
	}
	
}
