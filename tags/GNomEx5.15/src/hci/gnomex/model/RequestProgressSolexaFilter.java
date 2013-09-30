package hci.gnomex.model;


import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

public class RequestProgressSolexaFilter extends RequestProgressFilter {
  
  

  
  
  public StringBuffer getSolexaQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT req.idRequest, req.createDate, req.number,  "); 
    queryBuf.append("        req.idAppUser, ");
    queryBuf.append("        s.number, s.name, s.qualDate, s.seqPrepDate, s.seqPrepByCore, ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, req.codeRequestCategory ");
    getSolexaQueryBody(queryBuf);
    
    return queryBuf;
    
  }

  
  private void getSolexaQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.requestCategory as reqCat");
    queryBuf.append(" LEFT JOIN      reqCat.categoryType as reqType");
    queryBuf.append(" LEFT JOIN      req.samples as s ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");

    
    addRequestCriteria();
    addSecurityCriteria();

    
    this.addWhereOrAnd();
    queryBuf.append(" reqType.isIllumina = 'Y' ");
    

    queryBuf.append(" order by req.createDate desc, req.number desc , s.number asc");
  
  }

  
  public StringBuffer getSolexaLaneStatusQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        count(s.number) ");
    getSolexaLaneStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  private void getSolexaLaneStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.requestCategory as reqCat");
    queryBuf.append(" LEFT JOIN      reqCat.categoryType as reqType");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" reqType.isIllumina = 'Y' ");

    queryBuf.append("        group by s.number ");
    
  } 


  
  public StringBuffer getSolexaLaneSeqStatusQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        ch.lastCycleDate, ");
    queryBuf.append("        count(s.number) ");
    getSolexaLaneSeqStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public StringBuffer getSolexaLanePipelineStatusQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        ch.pipelineDate, ");
    queryBuf.append("        count(s.number) ");
    getSolexaLanePipelineStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }  

  
  private void getSolexaLaneSeqStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.requestCategory as reqCat");
    queryBuf.append(" LEFT JOIN      reqCat.categoryType as reqType");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");
    queryBuf.append(" LEFT JOIN      l.flowCellChannel as ch ");
    queryBuf.append(" LEFT JOIN      ch.flowCell as fc ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" reqType.isIllumina = 'Y' ");

    queryBuf.append("        group by s.number, ch.lastCycleDate ");
    
    queryBuf.append("        having ch.lastCycleDate != null");      

  } 
  
  


  
  private void getSolexaLanePipelineStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.requestCategory as reqCat");
    queryBuf.append(" LEFT JOIN      reqCat.categoryType as reqType");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");
    queryBuf.append(" LEFT JOIN      l.flowCellChannel as ch ");
    queryBuf.append(" LEFT JOIN      ch.flowCell as fc ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" reqType.isIllumina = 'Y' ");

    queryBuf.append("        group by s.number, ch.pipelineDate ");
    
    queryBuf.append("        having ch.pipelineDate != null");      

  } 
  
  
  
}
