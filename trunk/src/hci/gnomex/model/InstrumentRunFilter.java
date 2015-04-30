package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Date;

public class InstrumentRunFilter extends DetailObject {


  // Criteria
  private Integer               idInstrumentRun;
  private String                runName;

  private String                getAll = "N";

  private String                status;
  private String                codeReactionType;
  
  private Date					runDateFrom;
  private Date					runDateTo;
  
  private Date					createDateFrom;
  private Date					createDateTo;

  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT ir.idInstrumentRun, ");
    queryBuf.append("        ir.runDate, ");
    queryBuf.append("        ir.createDate, ");
    queryBuf.append("        ir.codeInstrumentRunStatus, ");
    queryBuf.append("        ir.comments, ");
    queryBuf.append("        ir.label, ");
    queryBuf.append("        ir.codeReactionType, ");
    queryBuf.append("        ir.creator, ");
    queryBuf.append("        ir.codeSealType ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idInstrumentRun != null ||
        (runName != null && !runName.equals("")) ||
        (status != null && !status.equals("")) ||
        (codeReactionType != null && !codeReactionType.equals("")) ||
        createDateFrom != null ||
        createDateTo != null ||
        runDateFrom != null ||
        runDateTo != null ||
        (getAll != null && getAll.equals("Y"))) {
      hasLimitingCriteria = true;
    } else {
      hasLimitingCriteria = false;
    }

    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      return hasLimitingCriteria;      
    } 
    return true;


  }

  public void getQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM                InstrumentRun as ir ");
    
    if (! getAll.equals("Y")) {
      addRunCriteria();
    }

    queryBuf.append(" order by ir.createDate");

  }

  private void addRunCriteria() {
    // Search by id 
    if (idInstrumentRun != null){
      this.addWhereOrAnd();
      queryBuf.append(" ir.idInstrumentRun =");
      queryBuf.append(idInstrumentRun);
    } 
    
    // Search by run name 
    if (runName != null){
      this.addWhereOrAnd();
      queryBuf.append(" ir.label like '");
      queryBuf.append(runName);
      queryBuf.append("%' ");
    } 


    // Search by status
    if (status != null && !status.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" ir.codeInstrumentRunStatus like '%" + status + "%'");
      queryBuf.append(")");
    }
    
    if (codeReactionType != null && !codeReactionType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" ir.codeReactionType like '%" + codeReactionType + "%'");
      queryBuf.append(")");
    }

    // --------------------------
    // Search by run date
    //---------------------------
    if (runDateFrom != null) {
        this.addWhereOrAnd();
        queryBuf.append(" ir.runDate >= '");
        queryBuf.append(this.formatDate(runDateFrom, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");    	
    }
    if (runDateTo != null) {
        this.addWhereOrAnd();
        queryBuf.append(" ir.runDate < '");
        queryBuf.append(this.formatDate(runDateTo, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");    	
    }    

    // --------------------------
    // Search by create date
    //---------------------------
    if (createDateFrom != null) {
        this.addWhereOrAnd();
        queryBuf.append(" ir.createDate >= '");
        queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");
    }
    if (createDateTo != null) {
        this.addWhereOrAnd();
        queryBuf.append(" ir.createDate < '");
        queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");    	
    }
  }


  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  protected boolean addWhereOrOr() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" OR ");
    }
    return addWhere;
  }


  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }


  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }



  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }


  
  public String getCodeReactionType() {
    return codeReactionType;
  }

  
  public void setCodeReactionType( String codeReactionType ) {
    this.codeReactionType = codeReactionType;
  }

  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
  }

  public String getRunName() {
    return runName;
  }

  public void setRunName(String runName) {
    this.runName = runName;
  }
  
  public Date getCreateDateFrom() {
	return createDateFrom;
  }

  public void setCreateDateFrom(Date createDateFrom) {
    this.createDateFrom = createDateFrom;
  }
  
  public Date getCreateDateTo() {
	return createDateTo;
  }

  public void setCreateDateTo(Date createDateTo) {
    this.createDateTo = createDateTo;
  }
  
  public Date getRunDateFrom() {
	return runDateFrom;
  }

  public void setRunDateFrom(Date runDateFrom) {
    this.runDateFrom = runDateFrom;
  }
  
  public Date getRunDateTo() {
	return runDateTo;
  }

  public void setRunDateTo(Date runDateTo) {
    this.runDateTo = runDateTo;
  }  

}
