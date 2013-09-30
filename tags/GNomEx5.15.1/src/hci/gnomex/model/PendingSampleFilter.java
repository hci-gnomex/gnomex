package hci.gnomex.model;


import hci.framework.model.DetailObject;

public class PendingSampleFilter extends DetailObject {
  
  
  // Criteria
  private String               requestNumber;
  private Integer              idLab;
  private String               codeRequestCategory = RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY;
  
  
  private StringBuffer         queryBuf;
  private boolean              addWhere = true;
  
  public boolean hasRequiredCriteria() {
    if (codeRequestCategory == null || codeRequestCategory.equals("")) {
      return false;
    } else {
      return true;
    }
  
  }

  public StringBuffer getRedoQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestStatus, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            lab.idLab, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            sample.idSample, ");
    queryBuf.append("            well.row, ");
    queryBuf.append("            well.col, ");
    queryBuf.append("            well.position, ");
    queryBuf.append("            well.idAssay, ");
    queryBuf.append("            well.idPrimer, ");
    queryBuf.append("            plate.idPlate, ");
    queryBuf.append("            plate.label, ");
    queryBuf.append("            req.name, ");
    queryBuf.append("            req.submitter,  ");
    queryBuf.append("            sample.name, ");
    queryBuf.append("            well.redoFlag");
   
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN  sample.wells as well ");
    queryBuf.append(" LEFT JOIN  well.plate plate ");
    
    queryBuf.append(" WHERE well.redoFlag = 'Y' ");
    queryBuf.append(" AND   (well.idPlate is NULL or plate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "') "); 


    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY well.idAssay, well.idPrimer, req.createDate, req.idRequest, plate.idPlate, well.position ");

    return queryBuf;
    
  }
  
  public StringBuffer getPendingSamplesQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    // Get all samples that are in tubes (well with no idPlate) or plates
    queryBuf.append(" SELECT DISTINCT req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestStatus, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            lab.idLab, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            sample.idSample, ");
    queryBuf.append("            sourceWell.row, ");  // well row
    queryBuf.append("            sourceWell.col, ");  // well col
    queryBuf.append("            sourceWell.position, ");  // well position
    queryBuf.append("            sourceWell.idAssay, ");  // well idassay
    queryBuf.append("            sourceWell.idPrimer, ");  // well idprimer
    queryBuf.append("            sourcePlate.idPlate, ");
    queryBuf.append("            sourcePlate.label, ");
    queryBuf.append("            req.name,  ");
    queryBuf.append("            req.submitter,  ");
    queryBuf.append("            sample.name,  ");
    queryBuf.append("            sourceWell.redoFlag");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN       sample.wells as sourceWell ");
    queryBuf.append(" LEFT JOIN  sourceWell.plate sourcePlate");
    
    queryBuf.append(" WHERE (req.codeRequestStatus = '" + RequestStatus.PROCESSING + "') ");
    queryBuf.append(" AND   (sourcePlate.codePlateType is NULL or sourcePlate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "') "); 
    queryBuf.append(" AND   (sourceWell.redoFlag != 'Y' OR sourceWell.redoFlag is NULL) ");

    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY req.createDate, req.idRequest, sourcePlate.idPlate, sourceWell.position ");
   

    return queryBuf;
    
  }


  public StringBuffer getPendingSamplesAlreadyOnPlateQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    // Get all samples that are in tubes (well with no idPlate) or plates
    queryBuf.append(" SELECT     sample.idSample, well.idAssay, well.idPrimer ");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN       sample.wells as well ");
    queryBuf.append(" JOIN       well.plate plate ");
    
    queryBuf.append(" WHERE (req.codeRequestStatus = '" + RequestStatus.PROCESSING + "') ");
    queryBuf.append(" AND   (plate.codePlateType = '" + PlateType.REACTION_PLATE_TYPE + "') "); 

    addWhere = false;
    
    addRequestCriteria();

    return queryBuf;
    
  }
  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    
    // Search by request category 
    if (codeRequestCategory != null && 
        !codeRequestCategory.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '" + codeRequestCategory + "' ");
    }     

    
    // Search by request number 
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "' OR req.number like '" + requestNumberBase + "R[0-9]' OR req.number = '" + requestNumberBase + "R') ");
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

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }


  public Integer getIdLab() {
    return idLab;
  }
  
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  
}
