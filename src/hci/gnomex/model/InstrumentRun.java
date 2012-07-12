package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;


public class InstrumentRun extends HibernateDetailObject {
  
  private Integer             idInstrumentRun;
  private Date                runDate;
  private Date                createDate;
  private String              codeInstrumentRunStatus;
  private InstrumentRunStatus instrumentRunStatus;
  private String              comments;
  private String              label;
  private String              codeReactionType;
  private String              creator;
  private String              codeSealType;
  
  
  public Integer getIdInstrumentRun()
  {
    return idInstrumentRun;
  }

  public void setIdInstrumentRun(Integer idInstrumentRun)
  {
    this.idInstrumentRun = idInstrumentRun;
  }

  public Date getRunDate()
  {
    return runDate;
  }

  public void setRunDate(Date runDate)
  {
    this.runDate = runDate;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }
  
  public String getCreateYear() {
    return InstrumentRun.getCreateYear(this.getCreateDate());
  }
  
  public static String getCreateYear(java.util.Date theCreateDate) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate  = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear  = tokens[2];
      return createYear;
    }
  }


  public String getCodeInstrumentRunStatus()
  {
    return codeInstrumentRunStatus;
  }

  public void setCodeInstrumentRunStatus(String codeInstrumentRunStatus)
  {
    this.codeInstrumentRunStatus = codeInstrumentRunStatus;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments(String comments)
  {
    this.comments = comments;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getCodeReactionType()
  {
    return codeReactionType;
  }

  public void setCodeReactionType(String codeReactionType)
  {
    this.codeReactionType = codeReactionType;
  }

  public String getCreator()
  {
    return creator;
  }

  public void setCreator(String creator)
  {
    this.creator = creator;
  }

  public void setCodeSealType(String sealType)
  {
    this.codeSealType = sealType;
  }

  public String getCodeSealType()
  {
    return codeSealType;
  }

  
  public InstrumentRunStatus getInstrumentRunStatus() {
    return instrumentRunStatus;
  }

  
  public void setInstrumentRunStatus( InstrumentRunStatus instrumentRunStatus ) {
    this.instrumentRunStatus = instrumentRunStatus;
  }
  
  public static boolean areAllChromatogramsReleased(Session sess, InstrumentRun run) {
    boolean isComplete = false;
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT  well.idPlateWell ");
    queryBuf.append("FROM    Plate plate ");
    queryBuf.append("JOIN    plate.instrumentRun as run ");
    queryBuf.append("JOIN    plate.plateWells as well ");
    queryBuf.append("WHERE   run.idInstrumentRun = " + run.getIdInstrumentRun());
    List<Integer> plateWells = (List<Integer>)sess.createQuery(queryBuf.toString()).list();
    
    
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT  ch ");
    queryBuf.append("FROM    Chromatogram as ch ");
    queryBuf.append("WHERE   ch.idPlateWell in ( ");
    boolean firstTime = true;
    for (Integer idPlateWell : plateWells) {
      if (!firstTime) {
        queryBuf.append(", ");
      }
      queryBuf.append(idPlateWell);
      firstTime = false;
    }
    queryBuf.append(")");
    List<Chromatogram> releasedChromatograms = (List<Chromatogram>)sess.createQuery(queryBuf.toString()).list();
    HashMap<Integer, Chromatogram> wellToChrom = new  HashMap<Integer, Chromatogram>();
    for (Chromatogram ch : releasedChromatograms) {
      wellToChrom.put(ch.getIdPlateWell(), ch);
    }
    
    boolean missingReleasedChromatogram = false;
    int releaseCount = 0;
    for (Integer idPlateWell : plateWells) {
      Chromatogram ch = wellToChrom.get(idPlateWell);
      if (ch != null && ch.getReleaseDate() != null) {
        releaseCount++;
      }else {
        missingReleasedChromatogram = true;
      }
    }

    
    // Return true there are no missing or unreleased chromatograms and 
    // the number of released chrom at least as large as number of reaction wells.
    return !missingReleasedChromatogram && releaseCount >= plateWells.size();
  }

    
}