package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Date;

public class ChromatogramFilter extends DetailObject {


  // Criteria
  private Integer               idChromatogram;
  private Integer               idRequest;

  private String                requestNumber;
  private Integer               idInstrumentRun;
  private String                runName;
  private String                plateName;

  private String                getAll = "N";
  
  private Date					createDateFrom;
  private Date					createDateTo;

  private String                released = "";

  private String                capSeq = "";
  private String                mitSeq = "";

  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT c.idChromatogram, ");
    queryBuf.append("        c.idPlateWell, ");
    queryBuf.append("        c.idRequest, ");
    queryBuf.append("        c.qualifiedFilePath, ");
    queryBuf.append("        c.displayName, ");
    queryBuf.append("        c.readLength, ");
    queryBuf.append("        c.trimmedLength, ");
    queryBuf.append("        c.q20, ");
    queryBuf.append("        c.q40, ");
    queryBuf.append("        c.aSignalStrength, ");
    queryBuf.append("        c.cSignalStrength, ");
    queryBuf.append("        c.gSignalStrength, ");
    queryBuf.append("        c.tSignalStrength, ");
    queryBuf.append("        c.releaseDate, ");
    queryBuf.append("        pw.row, ");
    queryBuf.append("        pw.col, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        sample.name, ");
    queryBuf.append("        submitter.firstName, ");
    queryBuf.append("        submitter.lastName, ");
    queryBuf.append("        run.idInstrumentRun, ");
    queryBuf.append("        run.label, ");
    queryBuf.append("        plate.idPlate, ");
    queryBuf.append("        plate.label, ");
    queryBuf.append("        pw.redoFlag, ");
    queryBuf.append("        c.idReleaser, ");
    queryBuf.append("        c.lane, ");
    queryBuf.append("        pw.position, ");
    queryBuf.append("        plate.quadrant, ");
    queryBuf.append("        sample.idSampleType ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if ((released.equals("Y") || released.equals("")) &&
    	(createDateFrom == null && createDateTo == null) &&
        (plateName == null || plateName.equals("")) &&
        (runName == null || runName.equals("")) &&
        (requestNumber == null || requestNumber.equals("")) &&
        (idRequest == null)) {
      // If we are showing both released and unreleased, make sure that we have another
      // filter to limit the results
      hasLimitingCriteria = false;
    } else if (idChromatogram != null ||
        idRequest != null ||
        requestNumber != null ||
        idInstrumentRun != null ||
        (runName != null && !runName.equals("")) ||
        (plateName != null && !plateName.equals("")) ||
        //        (capSeq != null && capSeq.equals("Y")) ||
        //        (mitSeq != null && mitSeq.equals("Y")) ||
        createDateFrom != null ||
        createDateTo != null ||
        (released != null && !released.equals(""))  ||
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

    queryBuf.append(" FROM                Chromatogram as c ");

    queryBuf.append("LEFT JOIN    c.plateWell as pw ");
    queryBuf.append("LEFT JOIN    pw.plate as plate ");
    queryBuf.append("LEFT JOIN    plate.instrumentRun as run ");
    queryBuf.append("LEFT JOIN    pw.sample as sample ");
    queryBuf.append("LEFT JOIN    c.request as req ");
    queryBuf.append("LEFT JOIN    req.appUser as submitter ");

    addCriteria();

    queryBuf.append(" order by run.idInstrumentRun, plate.quadrant, pw.position, c.idRequest");
  }

  private void addCriteria() {
	  
	// Search by create date
	if (createDateFrom != null) {
		this.addWhereOrAnd();
	    queryBuf.append(" pw.createDate >= '");
	    queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
	    queryBuf.append("'");
	}
	if (createDateTo != null) {
		createDateTo.setTime(createDateTo.getTime() + 24*60*60*1000);
		this.addWhereOrAnd();
	    queryBuf.append(" pw.createDate <= '");
	    queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL));
	    queryBuf.append("'");		
	}    

    if (requestNumber != null) {
      this.addWhereOrAnd();

      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "' OR req.number like '" + requestNumberBase + "R[0-9]' OR req.number = '" + requestNumberBase + "R') ");
    }

    if (idChromatogram != null){
      this.addWhereOrAnd();
      queryBuf.append(" c.idChromatogram = ");
      queryBuf.append(idChromatogram);
    } 

    if (idInstrumentRun != null){
      this.addWhereOrAnd();
      queryBuf.append(" run.idInstrumentRun = ");
      queryBuf.append(idInstrumentRun);
    }


    if (runName != null){
      this.addWhereOrAnd();
      queryBuf.append(" run.label like '");
      queryBuf.append(runName);
      queryBuf.append("%' ");
    }

    if (plateName != null){
      this.addWhereOrAnd();
      queryBuf.append(" plate.label like '");
      queryBuf.append(plateName);
      queryBuf.append("%' ");
    }

    // Cap seq
    if (capSeq.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" run.codeReactionType = '");
      queryBuf.append(ReactionType.SEQUENCING_REACTION_TYPE);
      queryBuf.append("' ");
    }
    // Mit seq
    if (mitSeq.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" run.codeReactionType = '");
      queryBuf.append(ReactionType.MITO_DLOOP_REACTION_TYPE);
      queryBuf.append("' ");
    }

    if (idRequest != null){
      this.addWhereOrAnd();
      queryBuf.append(" c.idRequest = ");
      queryBuf.append(idRequest);
    } 

    if (released != null && released.equals("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" c.releaseDate is not null ");
    } 

    if (released != null && released.equals("N")){
      this.addWhereOrAnd();
      queryBuf.append(" c.releaseDate is null ");
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


  public Integer getIdChromatogram() {
    return idChromatogram;
  }


  public void setIdChromatogram(Integer idChromatogram) {
    this.idChromatogram = idChromatogram;
  }

  public Integer getIdRequest() {
    return idRequest;
  }


  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }


  public String getRequestNumber()
  {
    return requestNumber;
  }

  public void setRequestNumber(String requestNumber)
  {
    this.requestNumber = requestNumber;
  }

  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
  }

  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }

  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }

  public String getReleased()
  {
    return released;
  }

  public String getCapSeq() {
    return capSeq;
  }

  public void setCapSeq(String capSeq) {
    this.capSeq = capSeq;
  }

  public String getMitSeq() {
    return mitSeq;
  }

  public void setMitSeq(String mitSeq) {
    this.mitSeq = mitSeq;
  }

  public void setReleased(String released)
  {
    this.released = released;
  }

  public String getRunName() {
    return runName;
  }

  public void setRunName(String runName) {
    this.runName = runName;
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
