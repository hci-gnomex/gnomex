package hci.gnomex.utility;

import hci.dictionary.model.NullDictionaryEntry;
import hci.framework.model.DetailObject;
import hci.gnomex.model.AnalysisCollaborator;
import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class UsageRowDescriptor extends DetailObject implements Serializable, Comparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Integer   idLab;
  private String    labLastName;
  private String    labFirstName;
  private Date      createDate;
  private String    number;
  private String    fileName;
  public Integer getIdLab() {
    return idLab;
  }
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  public String getLabLastName() {
    return labLastName;
  }
  public void setLabLastName(String labLastName) {
    this.labLastName = labLastName;
  }
  public String getLabFirstName() {
    return labFirstName;
  }
  public void setLabFirstName(String labFirstName) {
    this.labFirstName = labFirstName;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  public String getNumber() {
    return number;
  }
  public void setNumber(String number) {
    this.number = number;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }  
  public void setUsageRowDescriptorAsCounter(UsageRowDescriptor ud) {
    setIdLab(ud.getIdLab());
    setLabLastName(ud.getLabLastName());
    setLabFirstName(ud.getLabFirstName());
    setCreateDate(ud.getCreateDate());
    setNumber(ud.getNumber());
    setFileName("");
  }
  @Override
  public boolean equals(Object obj) {
    UsageRowDescriptor compareTo = (UsageRowDescriptor) obj;
    if(this.getIdLab().equals(compareTo.getIdLab()) &&
        this.getCreateDate().equals(compareTo.getCreateDate()) &&
        this.getNumber().equals(compareTo.getNumber()) &&
        this.getFileName().equals(compareTo.getFileName())) {
      return true;
    }
    return false;

  }
  
  public int compareTo(Object o) {
    UsageRowDescriptor ur2 = (UsageRowDescriptor)o;
    String combinedName1 = this.getLabLastName() + this.getLabFirstName() + this.getNumber() + this.getFileName();
    String combinedName2 = ur2.getLabLastName() + ur2.getLabFirstName() + ur2.getNumber() + ur2.getFileName();
    
    int retValue = combinedName1.compareTo(combinedName2);
    if(retValue == 0) {
      // if string portion identical then compare against date
      retValue = this.getCreateDate().compareTo(ur2.getCreateDate());
    }
    return retValue;
   
  }
  
  public static Date stripTime(Date dateToStrip) {
    Calendar cal = Calendar.getInstance();  
    cal.setTime(dateToStrip);  
      
    // Set time fields to zero  
    cal.set(Calendar.HOUR_OF_DAY, 0);  
    cal.set(Calendar.MINUTE, 0);  
    cal.set(Calendar.SECOND, 0);  
    cal.set(Calendar.MILLISECOND, 0);  
      
    // Put it back in the Date object  
    return cal.getTime(); 
  }
   
}
