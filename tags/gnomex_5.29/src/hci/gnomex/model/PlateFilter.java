package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Date;

public class PlateFilter extends DetailObject {


  // Criteria
  private Integer               idPlate;
  private Integer               idInstrumentRun;
  
  private String                plateName;
  
  private String                getAll = "N";
  private String                notAddedToARun = "N";

  private String                status;
  private String                plateType;
  private String                codeReactionType;

  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  private Date					createDateFrom;
  private Date					createDateTo;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT p.idPlate, ");
    queryBuf.append("        p.idInstrumentRun, ");
    queryBuf.append("        p.quadrant, ");
    queryBuf.append("        p.createDate, ");
    queryBuf.append("        p.comments, ");
    queryBuf.append("        p.label, ");
    queryBuf.append("        p.codeReactionType, ");
    queryBuf.append("        p.creator, ");
    queryBuf.append("        p.codeSealType, ");
    queryBuf.append("        p.codePlateType ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idPlate != null ||
        idInstrumentRun != null ||
        (plateName != null && !plateName.equals("")) ||
        (status != null && !status.equals("")) ||
        (plateType != null && !plateType.equals("")) ||
        (codeReactionType != null && !codeReactionType.equals("")) ||
        (getAll != null && getAll.equals("Y")) ||
        createDateFrom != null ||
        createDateTo != null ||
        (notAddedToARun != null && notAddedToARun.equals("Y"))) {
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

    queryBuf.append(" FROM                Plate as p ");
    
    if (!getAll.equals("Y")) {
      addCriteria();
    }
    
    queryBuf.append(" order by p.createDate");

  }

  private void addCriteria() {

    if (idPlate != null){
      this.addWhereOrAnd();
      queryBuf.append(" p.idPlate =");
      queryBuf.append(idPlate);
    }
    
    if (plateName != null && !plateName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" p.label like '");
      queryBuf.append(plateName);
      queryBuf.append("%'");
    }

    if (idInstrumentRun != null){
      this.addWhereOrAnd();
      queryBuf.append(" p.idInstrumentRun =");
      queryBuf.append(idInstrumentRun);
    } 

    // Search by plate type
    if (plateType != null && !plateType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.codePlateType like '%" + plateType + "%'");
      queryBuf.append(")");
    }
    
    // Search by reaction type
    if (codeReactionType != null && !codeReactionType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.codeReactionType like '%" + codeReactionType + "%'");
      queryBuf.append(")");
    }
    
    // Search by not added to run
    if (notAddedToARun.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.idInstrumentRun is null");
      queryBuf.append(")");
    }
    
    // Search by create date
    if (createDateFrom != null) {
        this.addWhereOrAnd();
        queryBuf.append(" p.createDate >= '");
        queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");    	
    }
    if (createDateTo != null) {
    	createDateTo.setTime(createDateTo.getTime() + 24*60*60*1000);
        this.addWhereOrAnd();
        queryBuf.append(" p.createDate <= '");
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


  public Integer getIdPlate() {
    return idPlate;
  }


  public void setIdPlate(Integer idPlate) {
    this.idPlate = idPlate;
  }

  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }


  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }


  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
  }

  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }

  
  public String getPlateType() {
    return plateType;
  }

  
  public void setPlateType( String plateType ) {
    this.plateType = plateType;
  }

  
  public String getCodeReactionType() {
    return codeReactionType;
  }

  
  public void setCodeReactionType( String codeReactionType ) {
    this.codeReactionType = codeReactionType;
  }

  public String getNotAddedToARun()
  {
    return notAddedToARun;
  }

  public void setNotAddedToARun(String notAddedToARun)
  {
    this.notAddedToARun = notAddedToARun;
  }
  
  public String getCodePlateType() {
    return plateType;
  }

  public void setCodePlateType(String codePlateType) {
    this.plateType = codePlateType;
  }

  public String getPlateName() {
    return plateName;
  }

  public void setPlateName(String plateName) {
    this.plateName = plateName;
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

}
