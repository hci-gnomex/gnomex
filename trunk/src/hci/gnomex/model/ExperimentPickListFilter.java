package hci.gnomex.model;


import java.util.Iterator;
import java.util.List;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

public class ExperimentPickListFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  private DictionaryHelper      dictionaryHelper;
  
  
  
  public StringBuffer getMicroarrayQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT project.name, ");
    queryBuf.append("        req.idRequest, ");
    queryBuf.append("        req.createDate, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, ");
    queryBuf.append("        slideProduct.name, ");
    queryBuf.append("        slideProduct.isSlideSet, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        hyb.number, ");
    queryBuf.append("        hyb.idSlideDesign, ");
    queryBuf.append("        -1,  ");
    queryBuf.append("        -1,  ");
    queryBuf.append("        s1.number, s1.name, ");
    queryBuf.append("        s2.number, s2.name,  ");
    queryBuf.append("        -1, ");
    queryBuf.append("        -1, ");
    queryBuf.append("        '', ");
    queryBuf.append("        -1, ");
    queryBuf.append("        '', ");
    queryBuf.append("        hyb.idHybridization, ");
    queryBuf.append("        slideDesign.name, ");
    queryBuf.append("        req.name ");
    getMicroarrayQueryBody(queryBuf);
    
    queryBuf.append(" ORDER BY project.name, req.number, hyb.number ");
    
    return queryBuf;
    
  }
  
  public void getMicroarrayQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Project as project ");
    queryBuf.append(" JOIN           project.requests as req ");
    queryBuf.append(" JOIN           req.slideProduct as slideProduct ");
    queryBuf.append(" JOIN           req.hybridizations as hyb ");
    queryBuf.append(" JOIN           hyb.labeledSampleChannel1 as ls1 ");
    queryBuf.append(" JOIN           ls1.sample as s1 ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");
    queryBuf.append(" LEFT JOIN      hyb.labeledSampleChannel2 as ls2 ");
    queryBuf.append(" LEFT JOIN      ls2.sample as s2 ");
    queryBuf.append(" LEFT JOIN      hyb.slideDesign as slideDesign ");


    addRequestCriteria();
    addHybCriteria();
    addSecurityCriteria();
  
  }
  

  public StringBuffer getNextGenSeqQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT project.name, ");
    queryBuf.append("        req.idRequest, ");
    queryBuf.append("        req.createDate, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        req.codeRequestCategory, "); 
    queryBuf.append("        '', ");
    queryBuf.append("        '', ");
    queryBuf.append("        '', ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        lane.number, ");
    queryBuf.append("        -1, ");
    queryBuf.append("        lane.idNumberSequencingCycles,  ");
    queryBuf.append("        lane.idSeqRunType,  ");
    queryBuf.append("        s.number, s.name, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        s.idSampleType, ");
    queryBuf.append("        lane.idGenomeBuildAlignTo, ");
    queryBuf.append("        lane.analysisInstructions, ");
    queryBuf.append("        ch.number, ");
    queryBuf.append("        fc.number, ");
    queryBuf.append("        lane.idSequenceLane, ");
    queryBuf.append("        '', ");
    queryBuf.append("        req.name ");

    getNextGenSeqQueryBody(queryBuf);
    
    queryBuf.append(" ORDER BY project.name, req.number, lane.number ");

    return queryBuf;
    
  }
  public void getNextGenSeqQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Project as project ");
    queryBuf.append(" JOIN           project.requests as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as lane ");
    queryBuf.append(" JOIN           lane.sample as s ");
    queryBuf.append(" LEFT JOIN      lane.flowCellChannel as ch ");
    queryBuf.append(" LEFT JOIN      ch.flowCell as fc ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");


    addRequestCriteria();
    
  
    addLaneCriteria();
    addSecurityCriteria();
  }
  
  
  
  public boolean hasCriteria() {
    if (idLab != null) {
      return true;
    } else {
      return false;
    }
  }
  
  
  

  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    
   
    
  }
  

  
  private void addHybCriteria() {
    
  }

  private void addLaneCriteria() {
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory in (");
    
    List requestCategories = dictionaryHelper.getRequestCategoryList();
    int count = 0;
    for (Iterator i = requestCategories.iterator(); i.hasNext();) {
      RequestCategory requestCategory = (RequestCategory)i.next();
      if (requestCategory.isNextGenSeqRequestCategory()) {
        if (count > 0) {
          queryBuf.append(", ");            
        }
        
        queryBuf.append("'");
        queryBuf.append(requestCategory.getCodeRequestCategory());
        queryBuf.append("'");    
        count++;
      }
      
    }
    
    queryBuf.append(") ");

    //TODO - need to filter by lane complete date
  }

 
  
  private void addSecurityCriteria() {
    secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, false);
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

  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  
  

  
 

  
  
}
