package hci.gnomex.utility;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SequencingControl;
import hci.gnomex.model.SlideSource;
import hci.framework.model.DetailObject;

import java.util.Iterator;
import java.util.Set;

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
   cell.addContent(note);
   row.addContent(cell);
   table.addContent(row);
   
   return table;
 }

 public Element makeRequestTable() {
    
    
    String userAndLab = "";
    String phone = "";
    String email = "";
    if (appUser != null) {
      userAndLab = appUser.getFirstName() + " " + appUser.getLastName();
      phone    = appUser.getPhone();
      email    = appUser.getEmail();
    }
    String accountName = "";
    if (billingAccount != null) {
      accountName = billingAccount.getAccountName();
    }
    String labName = "";
    if (request.getLab() != null) {
      userAndLab += " (" + request.getLab().getName() + ")";
    }
    
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "5");
    table.addContent(makeRow("Request #:",    request.getNumber(), 
                              "Requested by:", userAndLab));
    if (request.getCodeRequestCategory() != null && 
        !request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY) && 
        !request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      table.addContent(makeRow("Category:",     dictionaryHelper.getMicroarrayCategory(request.getCodeMicroarrayCategory()), 
                               "Phone:",        phone));      
    } else {
      table.addContent(makeRow("", "",
                               "Phone:",        phone));
    }
    table.addContent(makeRow("", "",
                              "Email:",        email));
    table.addContent(makeRow("", "",
                             "Account :",      accountName));
    
    
    
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
    
    
    this.addHeaderCell(rowh, "Sample #", rowSpan, new Integer(1), "left");
    this.addHeaderCell(rowh, "Sample Name", rowSpan, new Integer(1));
    this.addHeaderCell(rowh, "Sample Type", rowSpan, new Integer(1), new Integer(200));
    this.addHeaderCell(rowh, "Conc. ng/uL", rowSpan, new Integer(1));
    if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      this.addHeaderCell(rowh, "Frag size", rowSpan, new Integer(1));
      this.addHeaderCell(rowh, "Core to perform library prep?", rowSpan, new Integer(1));
    } 
    this.addHeaderCell(rowh, "Sample Prep Method", rowSpan, new Integer(1), new Integer(300));
    if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      this.addHeaderCell(rowh, "Unit", rowSpan, new Integer(1));
      this.addHeaderCell(rowh, "Chip Type",rowSpan, new Integer(1));
    }
    if (includeMicroarrayCoreNotes ) {
      this.addHeaderCell(rowh, "Quality", new Integer(1), new Integer(4),
         "colgroup");
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        this.addHeaderCell(rowh, "Lib Prep", new Integer(1), new Integer(2), "colgroup");        
      }
      
      rowh = new Element("TR");
      table.addContent(rowh);
      this.addHeaderCell(rowh, "Conc. ng/uL");
      this.addHeaderCell(rowh, "260/230");
      this.addHeaderCell(rowh, "Bioanal method");
      if (dnaSamples) {
        this.addHeaderCell(rowh, "Frag size");
      } else {
        this.addHeaderCell(rowh, "RIN#");                
      }
      
      
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        this.addHeaderCell(rowh, "Starting template quantity");
        this.addHeaderCell(rowh, "Gel size range");
      }
        

    }

    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      
      String fragmentSizeRange = "";
      if (sample.getFragmentSizeFrom() != null) {
        fragmentSizeRange = sample.getFragmentSizeFrom().toString() + " - ";
      } else {
        fragmentSizeRange = "? - ";
      }
      if (sample.getFragmentSizeTo() != null) {
        fragmentSizeRange += sample.getFragmentSizeTo().toString();
      } else {
        fragmentSizeRange += "?";
      }
      

      this.addLeftCell(row, sample.getNumber());
      this.addCell(row, sample.getName());
      this.addCell(row, sample.getIdSampleType() == null ? "&nbsp;"       : dictionaryHelper.getSampleType(sample));
      this.addCell(row, sample.getConcentration() == null ? "&nbsp;"      : (new Integer(sample.getConcentration().intValue())).toString() );
      if (request.getCodeRequestCategory() != null &&  request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        this.addCell(row, fragmentSizeRange);
        this.addCell(row, sample.getSeqPrepByCore() != null ? sample.getSeqPrepByCore() : "&nbsp;");
      }
      this.addCell(row, sample.getIdSamplePrepMethod() == null ? "&nbsp;" : dictionaryHelper.getSamplePrepMethod(sample));
      if (request.getCodeRequestCategory() != null &&  request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
        this.addCell(row, sample.getCodeConcentrationUnit() == null ? "&nbsp;"      : sample.getCodeConcentrationUnit());
        this.addCell(row, dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()) == null || dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()).equals("") ? "&nbsp;" : 
                           dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()));
      }
      if (includeMicroarrayCoreNotes) {
          this.addSmallEmptyCell(row);
          this.addSmallEmptyCell(row);              
          this.addSmallEmptyCell(row);              
          this.addSmallEmptyCell(row);              
      }
      if (includeMicroarrayCoreNotes ) {
        if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
          this.addSmallEmptyCell(row);              
          this.addSmallEmptyCell(row);                        
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
      this.addHeaderCell(rowh, "Unit", new Integer(2), new Integer(1));;
      this.addHeaderCell(rowh, "Chip Type", new Integer(2), new Integer(1));;
    }
    this.addHeaderCell(rowh, "Quality", new Integer(1), new Integer(4), "colgroup");
      
    rowh = new Element("TR");
    table.addContent(rowh);    
    this.addHeaderCell(rowh, "Conc. ng/uL");
    this.addHeaderCell(rowh, "260/230");
    this.addHeaderCell(rowh, "Bioanalyzer method");
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
      

      this.addLeftCell(row, sample.getNumber());
      this.addCell(row, sample.getName());
      this.addCell(row, sample.getIdSampleType() == null ? "&nbsp;"       : dictionaryHelper.getSampleType(sample));
      this.addCell(row, sample.getIdSamplePrepMethod() == null ? "&nbsp;" : dictionaryHelper.getSamplePrepMethod(sample));
      this.addCell(row, sample.getConcentration() == null ? "&nbsp;"      : (new Integer(sample.getConcentration().intValue())).toString() );
      if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
        this.addCell(row, sample.getCodeConcentrationUnit() == null ? "&nbsp;"      : sample.getCodeConcentrationUnit());
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
    this.addHeaderCell(rowh, "Slide");
    this.addHeaderCell(rowh, "Array ID");
    this.addHeaderCell(rowh, "Slide Source");
    if (includeMicroarrayCoreNotes) {
      this.addHeaderCell(rowh, "Barcode");
      this.addHeaderCell(rowh, "Notes");      
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
        this.addSmallEmptyCell(row);
        this.addEmptyCell(row);        
      }
      
    }
    
    return table;
  }
  
  
  public Element makeSequenceLaneTable(Set lanes) {
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
    this.addHeaderCell(rowh, "# Sequencing Cycles");
    this.addHeaderCell(rowh, "Genome Build (align to)");
    this.addHeaderCell(rowh, "Analysis instructions");      

 
    
    
    
    
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      


      this.addLeftCell(row, lane.getNumber());
      this.addCell(row, lane.getSample() != null ? lane.getSample().getName() : "&nbsp;");
      this.addCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
      this.addCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
      this.addCell(row, lane.getIdGenomeBuildAlignTo() != null  ? dictionaryHelper.getGenomeBuild(lane.getIdGenomeBuildAlignTo()) : "&nbsp;");
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
    this.addHeaderCell(rowh, "# Sequencing Cycles");
    this.addHeaderCell(rowh, "Genome Build (align to)");
    this.addHeaderCell(rowh, "Analysis instructions");      

 
    
    
    
    
    for(Iterator i = flowCellChannels.iterator(); i.hasNext();) {
      FlowCellChannel channel = (FlowCellChannel)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      this.addLeftCell(row, channel.getNumber().toString());

      if (channel.getSequenceLane() != null) {
        SequenceLane lane = channel.getSequenceLane();

        this.addCell(row, lane.getSample() != null ? lane.getSample().getName() : "&nbsp;");
        this.addCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
        this.addCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
        this.addCell(row, lane.getIdGenomeBuildAlignTo() != null  ? dictionaryHelper.getGenomeBuild(lane.getIdGenomeBuildAlignTo()) : "&nbsp;");
        this.addCell(row, lane.getAnalysisInstructions() != null && !lane.getAnalysisInstructions().equals("") ? lane.getAnalysisInstructions() : "&nbsp;");
        
      } else if (channel.getSequencingControl() != null) {
        SequencingControl control = channel.getSequencingControl();
        this.addCell(row, control.getDisplay());        
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
      } else {
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
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



}
