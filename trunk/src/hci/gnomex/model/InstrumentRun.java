package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Date;
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
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT  ch ");
    queryBuf.append("FROM    Chromatogram as c ");
    queryBuf.append("JOIN    c.plateWell as pw ");
    queryBuf.append("JOIN    pw.plate as plate ");
    queryBuf.append("JOIN    plate.instrumentRun as run ");
    queryBuf.append("WHERE   run.idInstrumentRun = " + run.getIdInstrumentRun());
    
    int releaseCount = 0;
    List<Chromatogram> chromatograms = (List<Chromatogram>)sess.createQuery(queryBuf.toString()).list();
    for (Chromatogram ch : chromatograms) {
      if (ch.getReleaseDate() != null) {
        releaseCount++;
      }
    }
    
    return releaseCount == chromatograms.size();
  }

    
}