package hci.gnomex.model;



import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;

import hci.hibernate3utils.HibernateDetailObject;



public class DiskUsageByMonth extends HibernateDetailObject {
  
  public static final String DISK_USAGE_REQUEST_CATEGORY = "DISKUSAGE";
  public static final String DISK_USAGE_ICON = "assets/disk.png";
  
  private Integer        idDiskUsageByMonth;
  private Integer        idLab;
  private Date           asOfDate;
  private Date           lastCalcDate;
  private BigDecimal     totalAnalysisDiskSpace;
  private BigDecimal     assessedAnalysisDiskSpace;
  private BigDecimal     totalExperimentDiskSpace;
  private BigDecimal     assessedExperimentDiskSpace;
  private Integer        idBillingPeriod;
  private Integer        idBillingAccount;
  private Integer        idCoreFacility;
  private Set             billingItems = new TreeSet();  

  public Integer getIdDiskUsageByMonth() {
    return idDiskUsageByMonth;
  }
  public void setIdDiskUsageByMonth(Integer idDiskUsageByMonth) {
    this.idDiskUsageByMonth = idDiskUsageByMonth;
  }
  
  public Integer getIdLab() {
    return idLab;
  }
  public void setIdLab(Integer id) {
    idLab = id;
  }
  
  public Date getAsOfDate() {
    return asOfDate;
  }
  public void setAsOfDate(Date date) {
    asOfDate = date;
  }
  
  public Date getLastCalcDate() {
    return lastCalcDate;
  }
  public void setLastCalcDate(Date date) {
    lastCalcDate = date;
  }
  
  public BigDecimal getTotalAnalysisDiskSpace() {
    return totalAnalysisDiskSpace;
  }
  public void setTotalAnalysisDiskSpace(BigDecimal space) {
    totalAnalysisDiskSpace = space;
  }
  
  public BigDecimal getAssessedAnalysisDiskSpace() {
    return assessedAnalysisDiskSpace;
  }
  public void setAssessedAnalysisDiskSpace(BigDecimal space) {
    assessedAnalysisDiskSpace = space;
  }
  
  public BigDecimal getTotalExperimentDiskSpace() {
    return totalExperimentDiskSpace;
  }
  public void setTotalExperimentDiskSpace(BigDecimal space) {
    totalExperimentDiskSpace = space;
  }
  
  public BigDecimal getAssessedExperimentDiskSpace() {
    return assessedExperimentDiskSpace;
  }
  public void setAssessedExperimentDiskSpace(BigDecimal space) {
    assessedExperimentDiskSpace = space;
  }
  
  public BigDecimal getTotalDiskSpace() {
    if (totalExperimentDiskSpace == null && totalAnalysisDiskSpace == null) {
      return BigDecimal.ZERO;
    } else if (totalExperimentDiskSpace == null) {
      return totalAnalysisDiskSpace;
    } else if (totalAnalysisDiskSpace == null) {
      return totalExperimentDiskSpace;
    } else {
      return totalExperimentDiskSpace.add(totalAnalysisDiskSpace);
    }
  }
  
  public BigDecimal getAssessedDiskSpace() {
    if (assessedExperimentDiskSpace == null && assessedAnalysisDiskSpace == null) {
      return BigDecimal.ZERO;
    } else if (assessedExperimentDiskSpace == null) {
      return assessedAnalysisDiskSpace;
    } else if (assessedAnalysisDiskSpace == null) {
      return assessedExperimentDiskSpace;
    } else {
      return assessedExperimentDiskSpace.add(assessedAnalysisDiskSpace);
    }
  }
  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }
  public void setIdBillingAccount(Integer id) {
    idBillingAccount = id;
  }
  
  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }
  public void setIdBillingPeriod(Integer id) {
    idBillingPeriod = id;
  }
  
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  public void setIdCoreFacility(Integer id) {
    idCoreFacility = id;
  }
  
  public Set getBillingItems() {
    return billingItems;
  }
  public void setBillingItems(Set billingItems) {
    this.billingItems = billingItems;
  }
}