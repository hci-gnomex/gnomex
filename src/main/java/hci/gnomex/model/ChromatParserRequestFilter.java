package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class ChromatParserRequestFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idInstrumentRun;
  
  private StringBuffer          queryBuf;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" chromat.idChromatogram, ");
    queryBuf.append(" chromat.releaseDate, ");
    queryBuf.append(" well.idPlateWell, ");
    queryBuf.append(" well.redoFlag, ");
    queryBuf.append(" plate.idPlate, ");
    queryBuf.append(" plate.codePlateType, ");
    queryBuf.append(" run.idInstrumentRun ");
    
    getQueryBody(queryBuf);
    
    queryBuf.append(" order by req.idRequest, well.idPlateWell ");

    return queryBuf;
    
  }
  
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.plateWells as well ");
    queryBuf.append(" LEFT JOIN   well.chromatograms as chromat ");
    queryBuf.append(" LEFT JOIN   well.plate as plate ");
    queryBuf.append(" LEFT JOIN   plate.instrumentRun as run ");
    
    appendRequestCriteria();
    addSecurityCriteria();
    
  }


  private void appendRequestCriteria() {
    queryBuf.append(" WHERE req.idRequest in ( ");
    addRequestQueryBody();
    queryBuf.append(" ) ");
  }
  
  private void addRequestQueryBody() {
    queryBuf.append(" SELECT DISTINCT");
    queryBuf.append(" req.idRequest ");
    queryBuf.append(" FROM        Plate as plate ");
    queryBuf.append(" LEFT JOIN   plate.plateWells as well ");
    queryBuf.append(" LEFT JOIN   well.request as req ");
    queryBuf.append(" WHERE plate.idInstrumentRun = ");
    queryBuf.append(idInstrumentRun);
    queryBuf.append(" AND req.idRequest is not null ");
  }
  
  
  private void addSecurityCriteria() {
    secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", false, false, true);
  }
    
    
  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }

  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }


  
  
}
