package hci.gnomex.model;


import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

import hci.gnomex.constants.Constants;
import hci.hibernate3utils.HibernateDetailObject;



public class Hybridization extends HibernateDetailObject {
  
  private Integer         idHybridization;
  private String          number;
  private String          notes;
  private Date            hybDate;
  private String          extractionFailed;
  private String          extractionBypassed;
  private Date            extractionDate;
  private String          codeSlideSource;
  private Integer         idSlideDesign;
  private Integer         idLabeledSampleChannel1;
  private Integer         idLabeledSampleChannel2;
  private LabeledSample   labeledSampleChannel1;
  private LabeledSample   labeledSampleChannel2;
  private Integer         idHybProtocol;
  private Slide           slide;
  private Integer         idSlide;
  private ArrayCoordinate arrayCoordinate;
  private Integer         idArrayCoordinate;
  private String          hybFailed;
  private String          hybBypassed;
  private Integer         idScanProtocol;
  private Integer         idFeatureExtractionProtocol;
  private String          hasResults;
  private java.util.Date  createDate;
  

  public Date getExtractionDate() {
    return extractionDate;
  }
  
  public void setExtractionDate(Date extractionDate) {
    this.extractionDate = extractionDate;
  }
  
  public Date getHybDate() {
    return hybDate;
  }
  
  public void setHybDate(Date hybDate) {
    this.hybDate = hybDate;
  }
  
  public Integer getIdArrayCoordinate() {
    return idArrayCoordinate;
  }
  
  public void setIdArrayCoordinate(Integer idArrayCoordinate) {
    this.idArrayCoordinate = idArrayCoordinate;
  }
  
  public Integer getIdHybProtocol() {
    return idHybProtocol;
  }
  
  public void setIdHybProtocol(Integer idHybProtocol) {
    this.idHybProtocol = idHybProtocol;
  }
  
  public Integer getIdHybridization() {
    return idHybridization;
  }
  
  public void setIdHybridization(Integer idHybridization) {
    this.idHybridization = idHybridization;
  }
  
  public Integer getIdLabeledSampleChannel1() {
    return idLabeledSampleChannel1;
  }
  
  public void setIdLabeledSampleChannel1(Integer idLabeledSampleChannel1) {
    this.idLabeledSampleChannel1 = idLabeledSampleChannel1;
  }
  
  public Integer getIdLabeledSampleChannel2() {
    return idLabeledSampleChannel2;
  }
  
  public void setIdLabeledSampleChannel2(Integer idLabeledSampleChannel2) {
    this.idLabeledSampleChannel2 = idLabeledSampleChannel2;
  }
  
  public Integer getIdSlide() {
    return idSlide;
  }
  
  public void setIdSlide(Integer idSlide) {
    this.idSlide = idSlide;
  }
  
  public Integer getIdSlideDesign() {
    return idSlideDesign;
  }
  
  public void setIdSlideDesign(Integer idSlideDesign) {
    this.idSlideDesign = idSlideDesign;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }

  
  public String getHybFailed() {
    return hybFailed;
  }

  
  public void setHybFailed(String hybFailed) {
    this.hybFailed = hybFailed;
  }

  
  
  public void setLabeledSampleChannel1(LabeledSample labeledSampleChannel1) {
    this.labeledSampleChannel1 = labeledSampleChannel1;
  }

  
  public void setLabeledSampleChannel2(LabeledSample labeledSampleChannel2) {
    this.labeledSampleChannel2 = labeledSampleChannel2;
  }

  
  public LabeledSample getLabeledSampleChannel1() {
    return labeledSampleChannel1;
  }

  
  public LabeledSample getLabeledSampleChannel2() {
    return labeledSampleChannel2;
  }
  
  public Integer getRow() {
    return this.getIdHybridization();
  }
  
  public Integer getIdSampleChannel1() {
    if (getLabeledSampleChannel1() != null) {
      return getLabeledSampleChannel1().getIdSample();
    } else {
      return null;
    }    
  }

  public Integer getIdSampleChannel2() {
    if (getLabeledSampleChannel2() != null) {
      return getLabeledSampleChannel2().getIdSample();
    } else {
      return null;
    }    
  }
  
  public String getSlideNumber() {
    return "";
  }

  
  public Slide getSlide() {
    return slide;
  }

  
  public void setSlide(Slide slide) {
    this.slide = slide;
  }

  
  public String getHasResults() {
    return hasResults;
  }

  
  public void setHasResults(String hasResults) {
    this.hasResults = hasResults;
  }

 
  
  public String getSlideBarcode() {
    if (slide != null) {
      return slide.getBarcode();
    } else {
      return "";
    }
  }
  
  public String getHybCompleted() {
    if (hybDate != null) {
      return "Y";
    } else {
      return "N";
    }
    
  }
  public String getExtractionCompleted() {
    if (extractionDate != null) {
      return "Y";
    } else {
      return "N";
    }
    
  }

  
  public ArrayCoordinate getArrayCoordinate() {
    return arrayCoordinate;
  }

  
  public void setArrayCoordinate(ArrayCoordinate arrayCoordinate) {
    this.arrayCoordinate = arrayCoordinate;
  }
  

  public String getIdLabelingProtocolChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getIdLabelingProtocol() != null) {
      return labeledSampleChannel1.getIdLabelingProtocol().toString();
    } else {
      return "";
    }
  }
  
  public String getIdLabelingProtocolChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getIdLabelingProtocol() != null) {
      return labeledSampleChannel2.getIdLabelingProtocol().toString();
    } else {
      return "";
    }
  }
  
  public String getLabelingYieldChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getLabelingYield() != null) {
      return labeledSampleChannel1.getLabelingYield().toString();
    } else {
      return "";
    }
  }
  public String getNumberOfReactionsChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getNumberOfReactions() != null) {
      return labeledSampleChannel1.getNumberOfReactions().toString();
    } else {
      return "";
    }
  }
  public String getCodeLabelingReactionSizeChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getCodeLabelingReactionSize() != null) {
      return labeledSampleChannel1.getCodeLabelingReactionSize().toString();
    } else {
      return "";
    }
  }
  
  public String getLabelingYieldChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getLabelingYield() != null) {
      return labeledSampleChannel2.getLabelingYield().toString();
    } else {
      return "";
    }
  }
  public String getNumberOfReactionsChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getNumberOfReactions() != null) {
      return labeledSampleChannel2.getNumberOfReactions().toString();
    } else {
      return "";
    }
  }
  public String getCodeLabelingReactionSizeChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getCodeLabelingReactionSize() != null) {
      return labeledSampleChannel2.getCodeLabelingReactionSize().toString();
    } else {
      return "";
    }
  }
  
  public String getLabelingFailedChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getLabelingFailed() != null) {
      return labeledSampleChannel1.getLabelingFailed();
    } else {
      return "N";
    }
  }
  public String getLabelingFailedChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getLabelingFailed() != null) {
      return labeledSampleChannel2.getLabelingFailed();
    } else {
      return "N";
    }
  }
  public String getLabelingCompleteChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getLabelingDate() != null) {
      return "Y";
    } else {
      return "N";
    }
  }
  public String getLabelingCompleteChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getLabelingDate() != null) {
      return "Y";
    } else {
      return "N";
    }
  }
  
  public String getLabelingStatusChannel1() {
    if (labeledSampleChannel1 != null && labeledSampleChannel1.getLabelingStatus() != null) {
      return labeledSampleChannel1.getLabelingStatus();
    } else {
      return "";
    }
  }
  public String getLabelingStatusChannel2() {
    if (labeledSampleChannel2 != null && labeledSampleChannel2.getLabelingStatus() != null) {
      return labeledSampleChannel2.getLabelingStatus();
    } else {
      return "";
    }
  }
  
  public String getCodeSlideSource() {
    return codeSlideSource;
  }

  
  public void setCodeSlideSource(String codeSlideSource) {
    this.codeSlideSource = codeSlideSource;
  }
  
  public String getCanChangeSampleDesignations() {
    if (labeledSampleChannel1 != null && 
        (labeledSampleChannel1.getLabelingDate() != null || 
        (labeledSampleChannel1.getLabelingFailed() != null && labeledSampleChannel1.getLabelingFailed().equals("Y"))) ) {
      return "N";
    } else  if (labeledSampleChannel2 != null && 
        (labeledSampleChannel2.getLabelingDate() != null || 
        (labeledSampleChannel2.getLabelingFailed() != null && labeledSampleChannel2.getLabelingFailed().equals("Y"))) ) {
      return "N";
    } else {
      GregorianCalendar gracePeriod = new GregorianCalendar();
      gracePeriod.setTime(createDate);
      gracePeriod.add(Calendar.HOUR, 24);
      GregorianCalendar now = new GregorianCalendar();
      if (now.after(gracePeriod)) {
        return "N";
      } else {
        return "Y";        
      }
    }
  }
  
  public String getCanChangeSlideDesign() {
    return getCanChangeSampleDesignations();
  }
  
  public String getCanChangeSlideSource() {
    return getCanChangeSampleDesignations();
  }
  
  public String getArrayCoordinateName() {
    if (arrayCoordinate != null) {
      return arrayCoordinate.getName();
    } else {
      return "";
    }
  }

  
  public java.util.Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }

  
  public Integer getIdScanProtocol() {
    return idScanProtocol;
  }

  
  public void setIdScanProtocol(Integer idScanProtocol) {
    this.idScanProtocol = idScanProtocol;
  }

  
  public Integer getIdFeatureExtractionProtocol() {
    return idFeatureExtractionProtocol;
  }

  
  public void setIdFeatureExtractionProtocol(Integer idFeatureExtractionProtocol) {
    this.idFeatureExtractionProtocol = idFeatureExtractionProtocol;
  }

  
  public String getHybBypassed() {
    return hybBypassed;
  }

  
  public void setHybBypassed(String hybBypassed) {
    this.hybBypassed = hybBypassed;
  }

  
  public String getExtractionBypassed() {
    return extractionBypassed;
  }

  
  public void setExtractionBypassed(String extractionBypassed) {
    this.extractionBypassed = extractionBypassed;
  }

  
  public String getExtractionFailed() {
    return extractionFailed;
  }

  
  public void setExtractionFailed(String extractionFailed) {
    this.extractionFailed = extractionFailed;
  }
  
  public String getHybStatus() {
    if (hybDate != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getHybFailed() != null && this.getHybFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else if (this.getHybBypassed() != null && this.getHybBypassed().equals("Y")) {
      return Constants.STATUS_BYPASSED;
    } else {
      return "";
    }
  }

  public String getExtractionStatus() {
    if (extractionDate != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getExtractionFailed() != null && this.getExtractionFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else if (this.getExtractionBypassed() != null && this.getExtractionBypassed().equals("Y")) {
      return Constants.STATUS_BYPASSED;
    } else {
      return "";
    }
  }

}