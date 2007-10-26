package hci.gnomex.model;



import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.jdom.Document;

import hci.gnomex.constants.Constants;
import hci.framework.utilities.XMLReflectException;
import hci.hibernate3utils.HibernateDetailObject;



public class LabeledSample extends HibernateDetailObject {
  
  private Integer     idLabeledSample;
  private Integer     idSample;
  private Date        labelingDate;
  private BigDecimal  labelingYield;
  private String      labelingFailed;
  private String      labelingBypassed;
  private Integer     idLabel;
  private Integer     idLabelingProtocol;
  private String      codeLabelingReactionSize;
  private Integer     numberOfReactions;
  private Sample      sample;
  private Integer     idRequest;
  
  public Integer getIdLabel() {
    return idLabel;
  }
  
  public void setIdLabel(Integer idLabel) {
    this.idLabel = idLabel;
  }
  
  public Integer getIdLabeledSample() {
    return idLabeledSample;
  }
  
  public void setIdLabeledSample(Integer idLabeledSample) {
    this.idLabeledSample = idLabeledSample;
  }
  
  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }
  
  public Date getLabelingDate() {
    return labelingDate;
  }
  
  public void setLabelingDate(Date labelingDate) {
    this.labelingDate = labelingDate;
  }
  
  public String getLabelingFailed() {
    return labelingFailed;
  }
  
  public void setLabelingFailed(String labelingFailed) {
    this.labelingFailed = labelingFailed;
  }
  
  public BigDecimal getLabelingYield() {
    return labelingYield;
  }
  
  public void setLabelingYield(BigDecimal labelingYield) {
    this.labelingYield = labelingYield;
  }

  
  public Integer getIdLabelingProtocol() {
    return idLabelingProtocol;
  }

  
  public void setIdLabelingProtocol(Integer idLabelingProtocol) {
    this.idLabelingProtocol = idLabelingProtocol;
  }

  
  public Sample getSample() {
    return sample;
  }

  
  public void setSample(Sample sample) {
    this.sample = sample;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }


  
  public Integer getNumberOfReactions() {
    return numberOfReactions;
  }

  
  public void setNumberOfReactions(Integer numberOfReactions) {
    this.numberOfReactions = numberOfReactions;
  }

  
  public String getCodeLabelingReactionSize() {
    return codeLabelingReactionSize;
  }

  
  public void setCodeLabelingReactionSize(String codeLabelingReactionSize) {
    this.codeLabelingReactionSize = codeLabelingReactionSize;
  }
  
  public String getLabelingStatus() {
    if (labelingDate != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getLabelingFailed() != null && this.getLabelingFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else if (this.getLabelingBypassed() != null && this.getLabelingBypassed().equals("Y")) {
      return Constants.STATUS_BYPASSED;
    } else {
      return "";
    }
  }

  
  public String getLabelingBypassed() {
    return labelingBypassed;
  }

  
  public void setLabelingBypassed(String labelingBypassed) {
    this.labelingBypassed = labelingBypassed;
  }
  
}

