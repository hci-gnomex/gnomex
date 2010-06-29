package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Lab;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
import hci.gnomex.model.SlideSource;
import hci.framework.model.DetailObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class RequestHTMLFormatter {
  
  private Request          request;
  private AppUser          appUser;
  private BillingAccount   billingAccount;
  private DictionaryHelper dictionaryHelper;
  private boolean         includeMicroarrayCoreNotes = true;
  private boolean         dnaSamples = false;
  
 public RequestHTMLFormatter(Request request, AppUser appUser, BillingAccount billingAccount, DictionaryHelper dictionaryHelper) {
   this.request = request;
   this.appUser = appUser;
   this.billingAccount = billingAccount;
   this.dictionaryHelper = dictionaryHelper;
   
   // Figure out if all samples are DNA samples.  This will affect what
   // sample quality columns show
   dnaSamples = false;
   int dnaSampleCount = 0;
   for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
     Sample sample = (Sample)i.next();
     if (this.isDNASampleType(sample)) {
       dnaSampleCount++;
     }
   }
   if (dnaSampleCount == request.getSamples().size()) {
     dnaSamples = true;
   }

   
 }
 
 public Element makeIntroNote(String note) {
   Element table = new Element("TABLE");   
   Element row = new Element("TR");
   Element cell = new Element("TD");
   cell.setAttribute("CLASS", "noborder");
   cell.addContent(note);
   row.addContent(cell);
   table.addContent(row);
   
   return table;
 }
 
 public Element makeRequestCategoryImage(String appURL) {
   Element img = new Element("img");
   
   String imageName = "";
   if (this.request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY)) {
     imageName = "microarray_small.png";
   } else if (this.request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY)) {
     imageName = "microarray_small_single_color.png";
   } else if (this.request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
     imageName = "microarray_chip.png";
   } else if (this.request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
     imageName = "chart_line.png";
   } else if (this.request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
     imageName = "DNA_diag.png";
   } else if (this.request.getCodeRequestCategory().equals(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY)) {
     imageName = "DNA_diag_lightening.png";
   } else {
     imageName = "flask.png";
   }
   
   img.setAttribute("src", (appURL != null ? appURL + "/images/" : "images/") + imageName);
   
   return img;
 }

 public Element makeRequestTable() {
    
    
    String userName = "";
    String phone = "";
    String email = "";
    if (appUser != null) {
      userName = appUser.getFirstName() + " " + appUser.getLastName();
      phone    = appUser.getPhone();
      email    = appUser.getEmail();
    }
    String accountName = "";
    if (billingAccount != null) {
      accountName = billingAccount.getAccountName();
    }
    String accountNumber = "";
    if (billingAccount != null) {
      accountNumber = billingAccount.getAccountNumber();
    }
    String labName = "";
    if (request.getLab() != null) {
      labName = request.getLab().getName();
    }
    
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "5");
    table.addContent(makeRow("Requester",   userName,
                             "Lab",         labName));
    
    table.addContent(makeRow("Date", request.formatDate(request.getCreateDate()),
                             "Phone",        phone));

    table.addContent(makeRow("Account",     accountName, 
                    "Email",        email));      

    table.addContent(makeRow("", accountNumber,
                     (request.getLastModifyDate() != null ? "Modified" : "&nbsp;"),  (request.getLastModifyDate() != null ? request.formatDate(request.getLastModifyDate()): "&nbsp;")));

    
    
    
    return table;
  }
  
  public Element makeSampleTable(Set samples) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS", "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Samples");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    
    Integer rowSpan = new Integer(1);
    if (includeMicroarrayCoreNotes) {
      rowSpan = new Integer(2);
    }
    
    boolean showSeqLibProtocol = false;
    boolean showBarcodeTag = false;
    for(Iterator i = samples.iterator(); i.hasNext();) {
    	Sample s = (Sample)i.next();
    	if (s.getSeqPrepByCore() != null && s.getSeqPrepByCore().equalsIgnoreCase("N")) {
    		showSeqLibProtocol = true;
    	}
    	if (s.getIdOligoBarcode() != null) {
    	  showBarcodeTag = true;
    	}
    }
    
    
    this.addHeaderCell(rowh, "Sample #", rowSpan, new Integer(1), "left");
    this.addHeaderCell(rowh, "Sample Name", rowSpan, new Integer(1));
    this.addHeaderCell(rowh, "Sample Type", rowSpan, new Integer(1), new Integer(200));
    this.addHeaderCell(rowh, "Conc.", rowSpan, new Integer(1));
    this.addHeaderCell(rowh, "Sample Prep Method", rowSpan, new Integer(1), new Integer(300));
    if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      if (showBarcodeTag) {
        this.addHeaderCell(rowh, "Barcode Tag", rowSpan, new Integer(1));        
      }
      if (showSeqLibProtocol) {
          this.addHeaderCell(rowh, "Seq Lib Protocol", rowSpan, new Integer(1));    	  
      }
    } 
    if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      
      this.addHeaderCell(rowh, "Chip Type",rowSpan, new Integer(1));
    }
    if (includeMicroarrayCoreNotes ) {
      this.addHeaderCell(rowh, "----------Quality-----------", new Integer(1), new Integer(4),
         "colgroup");
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        this.addHeaderCell(rowh, "---Lib Prep---", new Integer(1), new Integer(2), "colgroup");        
      }
      
      rowh = new Element("TR");
      table.addContent(rowh);
      this.addHeaderCell(rowh, "Conc. ng/uL");
      this.addHeaderCell(rowh, "260/ 230");
      this.addHeaderCell(rowh, "QC meth");
      if (dnaSamples) {
        this.addHeaderCell(rowh, "Frag size");
      } else {
        this.addHeaderCell(rowh, "RIN#");                
      }
      
      
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        this.addHeaderCell(rowh, "Start templ. qty");
        this.addHeaderCell(rowh, "Gel size rng");
      }
        

    }

    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      

      
      String concentration = "";
      if (sample.getConcentration() != null) {
        concentration = new Integer(sample.getConcentration().intValue()).toString();
        if (sample.getCodeConcentrationUnit() != null && !sample.getCodeConcentrationUnit().equals("")) {
          concentration += " " + sample.getCodeConcentrationUnit();
        }
      }
 
      
      String qualFragmentSizeRange = "&nbsp;";
      if (sample.getQualFragmentSizeFrom() != null && !sample.getQualFragmentSizeFrom().equals("")) {
        qualFragmentSizeRange = sample.getQualFragmentSizeFrom() + "-";
      }
      if (sample.getQualFragmentSizeTo() != null && !sample.getQualFragmentSizeTo().equals("")) {
        qualFragmentSizeRange += sample.getQualFragmentSizeTo();
      } else {
        qualFragmentSizeRange += "&nbsp;";
      }
      
      this.addLeftCell(row, sample.getNumber());
      this.addCell(row, sample.getName());
      this.addCell(row, sample.getIdSampleType() == null ? "&nbsp;"       : dictionaryHelper.getSampleType(sample));
      this.addCell(row, sample.getConcentration() == null ? "&nbsp;"      : concentration);
      this.addCell(row, sample.getIdSamplePrepMethod() == null ? "&nbsp;" : dictionaryHelper.getSamplePrepMethod(sample));
      if (request.getCodeRequestCategory() != null &&  request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        if (showBarcodeTag) {
          this.addCell(row, sample.getIdOligoBarcode() != null ? dictionaryHelper.getBarcodeSequence(sample.getIdOligoBarcode()) : "&nbsp;");          
        }
        if (showSeqLibProtocol) {
        	this.addCell(row, sample.getIdSeqLibProtocol() != null ? dictionaryHelper.getSeqLibProtocol(sample.getIdSeqLibProtocol()) : "&nbsp;");
        }
      }
      if (request.getCodeRequestCategory() != null &&  request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {        
        this.addCell(row, dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()) == null || dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()).equals("") ? "&nbsp;" : 
                           dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()));
      }
      if (includeMicroarrayCoreNotes) {
          this.addCell(row, sample.getQualCalcConcentration() == null ? "&nbsp;"      : sample.getQualCalcConcentration().toString());
          this.addCell(row, sample.getQual260nmTo230nmRatio() == null ? "&nbsp;"      : sample.getQual260nmTo230nmRatio().toString());
                
          this.addEmptyCell(row);
          
          if (dnaSamples) {
            this.addCell(row, qualFragmentSizeRange);        
            
          } else {
            this.addCell(row, sample.getQualRINNumber() == null ? "&nbsp;"              : sample.getQualRINNumber().toString());        
            
          }
                      
      }
      if (includeMicroarrayCoreNotes ) {
        if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
          if (sample.getSeqPrepByCore() == null || sample.getSeqPrepByCore().equals("N")) {
            this.addCell(row, "prepped by lab");              
            this.addCell(row, "prepped by lab");                          
          } else {
            this.addSmallEmptyCell(row);
            this.addSmallEmptyCell(row);
          }
        }
      }
    }
    
    return table;
  }
  
  public Element makeSampleQualityTable(Set samples) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS", "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Samples");
    caption.setAttribute("ALIGN", "LEFT");
    table.addContent(caption);
    

    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Sample #", new Integer(2), new Integer(1), "left");
    this.addHeaderCell(rowh, "Sample Name", new Integer(2), new Integer(1));;
    this.addHeaderCell(rowh, "Sample Type", new Integer(2), new Integer(1));;
    this.addHeaderCell(rowh, "Sample Prep Method", new Integer(2), new Integer(1));;
    this.addHeaderCell(rowh, "Conc.", new Integer(2), new Integer(1));;
    if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      this.addHeaderCell(rowh, "Chip Type", new Integer(2), new Integer(1));;
    }
    this.addHeaderCell(rowh, "Quality", new Integer(1), new Integer(4), "colgroup");
      
    rowh = new Element("TR");
    table.addContent(rowh);    
    this.addHeaderCell(rowh, "Conc. ng/uL");
    this.addHeaderCell(rowh, "260/230");
    this.addHeaderCell(rowh, "QC meth");
    if (this.dnaSamples) {
      this.addHeaderCell(rowh, "Size range");      
    } else {
      this.addHeaderCell(rowh, "RIN#");            
    }
    
    
    
    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      
      
      String qualFragmentSizeRange = "";
      if (sample.getQualFragmentSizeFrom() != null && !sample.getQualFragmentSizeFrom().equals("")) {
        qualFragmentSizeRange = sample.getQualFragmentSizeFrom() + "-";
      }
      if (sample.getQualFragmentSizeTo() != null && !sample.getQualFragmentSizeTo().equals("")) {
        qualFragmentSizeRange += sample.getQualFragmentSizeTo();
      } else {
        qualFragmentSizeRange += "?";
      }
      
      String concentration = "";
      if (sample.getConcentration() != null) {
        concentration = new Integer(sample.getConcentration().intValue()).toString();
        if (sample.getCodeConcentrationUnit() != null && !sample.getCodeConcentrationUnit().equals("")) {
          concentration += " " + sample.getCodeConcentrationUnit();
        }
      }

      this.addLeftCell(row, sample.getNumber());
      this.addCell(row, sample.getName());
      this.addCell(row, sample.getIdSampleType() == null ? "&nbsp;"       : dictionaryHelper.getSampleType(sample));
      this.addCell(row, sample.getIdSamplePrepMethod() == null ? "&nbsp;" : dictionaryHelper.getSamplePrepMethod(sample));
      this.addCell(row, sample.getConcentration() == null ? "&nbsp;"      : concentration);
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
        this.addCell(row, dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()) == null || dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()).equals("") ? "&nbsp;" : 
                           dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()));
      }
      
      this.addCell(row, sample.getQualCalcConcentration() == null ? "&nbsp;"      : sample.getQualCalcConcentration().toString());
      this.addCell(row, sample.getQual260nmTo230nmRatio() == null ? "&nbsp;"      : sample.getQual260nmTo230nmRatio().toString());
      this.addCell(row, dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()) == null || dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()).equals("") ? "&nbsp;" : 
        dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()));
      if (this.dnaSamples) {
        this.addCell(row, qualFragmentSizeRange);        
      } else {
        this.addCell(row, sample.getQualRINNumber() == null ? "&nbsp;"              : sample.getQualRINNumber().toString());        
      }
        
    }
    
    return table;
  }
  
  public Element makeLabeledSampleTable(Element container, Set labeledSamples) {


    Element col1 = new Element("DIV");
    col1.setAttribute("id", "col1");
    container.addContent(col1);

    col1.addContent(makeLabelSampleTable(labeledSamples, "Cy3"));

    
    // Do we have some Cy5 samples?
    boolean hasCy5Samples = false;
    for(Iterator i = labeledSamples.iterator(); i.hasNext();) {
      LabeledSample labeledSample = (LabeledSample)i.next();
      
      if (this.dictionaryHelper.getLabel(labeledSample.getIdLabel()).equals("Cy5")) {
        hasCy5Samples = true;
        break;
      }
    }
    
    // Only show Cy5 labeled sample table if we have some.
    if (hasCy5Samples) {
      Element col2 = new Element("DIV");
      col2.setAttribute("id", "col2");
      container.addContent(col2);

      col2.addContent(makeLabelSampleTable(labeledSamples, "Cy5"));
    }
    
    Element ftr = new Element("DIV");
    ftr.setAttribute("id", "footer");
    container.addContent(ftr);
    
    return container;
  }
  
  public Element makeLabelSampleTable(Set labeledSamples, String label) {
    
    
    Element table = new Element("TABLE");
    table.setAttribute("CLASS", "gridHalf");
    
    Element caption = new Element("CAPTION");
    caption.addContent(label + " Samples");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    
    Integer rowSpan = new Integer(1);
    
    
    this.addHeaderCell(rowh, "Sample #", rowSpan, new Integer(1), "left");
    this.addHeaderCell(rowh, "Conc.", rowSpan, new Integer(1));
    this.addHeaderCell(rowh, "Volume", rowSpan, new Integer(1));

    for(Iterator i = labeledSamples.iterator(); i.hasNext();) {
      LabeledSample labeledSample = (LabeledSample)i.next();
      
      if (this.dictionaryHelper.getLabel(labeledSample.getIdLabel()).equals(label)) {
        Sample sample = labeledSample.getSample();
        
        Element row = new Element("TR");
        row.setAttribute("CLASS", "forcedRowHeight");
        table.addContent(row);

        
        this.addLeftCell(row, sample.getNumber());
        this.addEmptyCell(row);
        this.addEmptyCell(row);
        
      }
      
    }
    
    return table;
  }

  
  public Element makeHybTable(Set hybridizations) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Hybridizations");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Hyb #", "left");
    this.addHeaderCell(rowh, "Cy3 Sample #"    );
    if (request.getCodeRequestCategory() == null ||
        (dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels() != null &&
         dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels().intValue() == 2)) {
      this.addHeaderCell(rowh, "Cy5 Sample #"    );
    }
    this.addHeaderCell(rowh, "Slide", "normal", new Integer(200));
    this.addHeaderCell(rowh, "Array ID");
    this.addHeaderCell(rowh, "Slide Source");
    if (includeMicroarrayCoreNotes) {
      this.addHeaderCell(rowh, "Barcode", "normal", new Integer(130));
      this.addHeaderCell(rowh, "Row-col", "normal", new Integer(40));      
    }

 
    
    
    
    
    for(Iterator i = hybridizations.iterator(); i.hasNext();) {
      Hybridization hyb = (Hybridization)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      
      
      String slideSource = null;
      if (hyb.getCodeSlideSource() != null && !hyb.getCodeSlideSource().equals("")) {
        slideSource = dictionaryHelper.getSlideSource(hyb.getCodeSlideSource());        
      }

      
      String slideDesignProtocolName = dictionaryHelper.getSlideDesignProtocolName(hyb.getIdSlideDesign());
        

      this.addLeftCell(row, hyb.getNumber());
      this.addCell(row, hyb.getLabeledSampleChannel1() != null ? hyb.getLabeledSampleChannel1().getSample().getNumber() : "&nbsp;");
      if (request.getCodeRequestCategory() == null ||
          (dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels() != null &&
           dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory()).getNumberOfChannels().intValue() == 2)) {
        this.addCell(row, hyb.getLabeledSampleChannel2() != null ? hyb.getLabeledSampleChannel2().getSample().getNumber() : "&nbsp;");
      }
      this.addCell(row, hyb.getIdSlideDesign() != null         ? dictionaryHelper.getSlideDesignName(hyb.getIdSlideDesign()) : "&nbsp;");
      this.addCell(row, slideDesignProtocolName != null        ? slideDesignProtocolName                                : "&nbsp;");
      if (hyb.getCodeSlideSource() != null && hyb.getCodeSlideSource().equals(SlideSource.STRIPPED)) {
        this.addHighlightedCell(row, slideSource != null ? slideSource : "&nbsp;");              
        
      } else {
        this.addCell(row, slideSource != null ? slideSource : "&nbsp;");              
      }
      if (includeMicroarrayCoreNotes) {
        this.addEmptyCell(row);
        this.addSmallEmptyCell(row);        
      }
      
    }
    
    return table;
  }
  
  
  public void addSequenceLaneTable(Element parentNode, Set lanes, String amendState) {

    // Group lanes by create Date
    TreeMap<Date, List<SequenceLane>> laneDateMap = new TreeMap<Date, List<SequenceLane>>(new DescendingDateComparator());
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();
      List<SequenceLane> theLanes = laneDateMap.get(lane.getCreateDate());
      if (theLanes == null) {
        theLanes = new ArrayList<SequenceLane>();
        laneDateMap.put(lane.getCreateDate(), theLanes);
      }
      theLanes.add(lane);
    }
    
    //Now show a lane table for each create date, most recent date first
    int sectionCount = 0;
    for(Iterator i = laneDateMap.keySet().iterator(); i.hasNext();) {
      Date createDate = (Date)i.next();
      List<SequenceLane> theLanes = laneDateMap.get(createDate);

      String caption = "Sequence Lanes";
      if (laneDateMap.size() > 1 || 
          (amendState != null && amendState.equals(Constants.AMEND_ADD_SEQ_LANES))) {
        if (sectionCount == 0) {
          caption = "Most recent Sequence Lanes added on " + request.formatDate(createDate);
        } else {
          caption = "Sequence Lanes added on " + request.formatDate(createDate);
        }
      }

      
      parentNode.addContent(makeSequenceLaneTable(caption, theLanes));
      
      // If the user just added lanes from the 'Add services' window,
      // just show the lanes just added, not all of the existing lanes.
      if (amendState != null && amendState.equals(Constants.AMEND_ADD_SEQ_LANES)) {
        break;
      }
      
      if (i.hasNext()) {
        makePageBreak(parentNode);        
      }
      sectionCount++;
    }
    
    
  }
    
    
  private Element makeSequenceLaneTable(String caption, List lanes) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");

    Element cap = new Element("CAPTION");
    cap.addContent(caption);
    table.addContent(cap);


    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "#", "left");
    this.addHeaderCell(rowh, "Sample name"    );
    this.addHeaderCell(rowh, "Status"    );
    this.addHeaderCell(rowh, "Seq Run Type");
    this.addHeaderCell(rowh, "# Seq Cycles");
    this.addHeaderCell(rowh, "Genome Build (align to)");
    this.addHeaderCell(rowh, "Analysis instructions");      



    // How show all remaining lanes
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();

      Element row = new Element("TR");
      table.addContent(row);



      this.addLeftCell(row, lane.getNumber());
      this.addCell(row, lane.getSample() != null ? lane.getSample().getName() : "&nbsp;");
      this.addCell(row, lane.getWorkflowStatusAbbreviated().equals("") ? "&nbsp;" : lane.getWorkflowStatusAbbreviated());
      this.addSmallCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
      this.addSmallCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
      this.addSmallCell(row, lane.getIdGenomeBuildAlignTo() != null  ? dictionaryHelper.getGenomeBuild(lane.getIdGenomeBuildAlignTo()) : "&nbsp;");
      this.addCell(row, lane.getAnalysisInstructions() != null && !lane.getAnalysisInstructions().equals("") ? lane.getAnalysisInstructions() : "&nbsp;");
    }

    return table;
  }



  public Element makeChannelTable(Set flowCellChannels) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");

    Element caption = new Element("CAPTION");
    caption.addContent("Sequence Lanes");
    table.addContent(caption);


    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "#", "left");
    this.addHeaderCell(rowh, "Sample name"    );
    this.addHeaderCell(rowh, "Seq Run Type");
    this.addHeaderCell(rowh, "# Seq Cycles");
    this.addHeaderCell(rowh, "Genome Build (align to)");
    this.addHeaderCell(rowh, "Analysis instructions");      

 
    
    
    
    
    for(Iterator i = flowCellChannels.iterator(); i.hasNext();) {
      FlowCellChannel channel = (FlowCellChannel)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      this.addLeftCell(row, channel.getNumber().toString());

      if (channel.getSequenceLanes() != null) {
        for (Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext();) {
          SequenceLane lane = (SequenceLane)i1.next();

          this.addCell(row, lane.getSample() != null ? lane.getSample().getName() : "&nbsp;");
          this.addSmallCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
          this.addSmallCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
          this.addCell(row, lane.getIdGenomeBuildAlignTo() != null  ? dictionaryHelper.getGenomeBuild(lane.getIdGenomeBuildAlignTo()) : "&nbsp;");
          this.addCell(row, lane.getAnalysisInstructions() != null && !lane.getAnalysisInstructions().equals("") ? lane.getAnalysisInstructions() : "&nbsp;");
          
        }
        
      } else if (channel.getSequencingControl() != null) {
        SequencingControl control = channel.getSequencingControl();
        this.addCell(row, control.getDisplay());        
        this.addSmallCell(row, "&nbsp;");
        this.addSmallCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
      } else {
        this.addCell(row, "&nbsp;");
        this.addSmallCell(row, "&nbsp;");
        this.addSmallCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
      }
      
    }
    
    return table;
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

  private void addLeftCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridleft");      
    cell.addContent(value);
    row.addContent(cell);
}

  private void addCell(Element row, String value) {
      Element cell = new Element("TD");
      cell.setAttribute("CLASS", "grid");      
      cell.addContent(value);
      row.addContent(cell);
  }
  
  
  private void addSmallCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridSmall");      
    cell.addContent(value);
    row.addContent(cell);
}

  
  private void addHighlightedCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridreverse");      
    cell.addContent(value);
    row.addContent(cell);
}
  
  private void addEmptyCell(Element row) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
    row.addContent(cell);
  }
  
  private void addSmallEmptyCell(Element row) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridemptysmall");
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

  
  public boolean isIncludeMicroarrayCoreNotes() {
    return includeMicroarrayCoreNotes;
  }

  
  public void setIncludeMicroarrayCoreNotes(boolean includeMicroarrayCoreNotes) {
    this.includeMicroarrayCoreNotes = includeMicroarrayCoreNotes;
  }
  
  
  private boolean isDNASampleType(Sample sample) {
    String sampleType = this.dictionaryHelper.getSampleType(sample.getIdSampleType());
    if (sampleType != null && sampleType.matches(".*DNA.*")) {
      return true;
    } else {
      return false;
    }
  }

  public static void makePageBreak(Element maindiv) {
    Element pb = new Element("P");
    pb.setAttribute("CLASS", "break");
    maindiv.addContent(pb);
    maindiv.addContent(new Element("BR"));
  }


}
