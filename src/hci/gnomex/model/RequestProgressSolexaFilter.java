package hci.gnomex.model;


import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class RequestProgressSolexaFilter extends RequestProgressFilter {
  
  

  
  
  public StringBuffer getSolexaQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
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

  
  public void getSolexaQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.samples as s ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");

    
    addRequestCriteria();
    addSecurityCriteria();

    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.SOLEXA_REQUEST_CATEGORY);
    queryBuf.append("', ");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");
    

    queryBuf.append(" order by req.createDate desc, req.number desc , s.number asc");
  
  }

  
  public StringBuffer getSolexaLaneStatusQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        count(s.number) ");
    getSolexaLaneStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  public void getSolexaLaneStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.SOLEXA_REQUEST_CATEGORY);
    queryBuf.append("', ");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");

    queryBuf.append("        group by s.number ");
    
  } 


  
  public StringBuffer getSolexaLaneSeqStatusQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
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
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        ch.pipelineDate, ");
    queryBuf.append("        count(s.number) ");
    getSolexaLanePipelineStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }  

  
  public void getSolexaLaneSeqStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");
    queryBuf.append(" LEFT JOIN      l.flowCellChannel as ch ");
    queryBuf.append(" LEFT JOIN      ch.flowCell as fc ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.SOLEXA_REQUEST_CATEGORY);
    queryBuf.append("', ");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");

    queryBuf.append("        group by s.number, ch.lastCycleDate ");
    
    queryBuf.append("        having ch.lastCycleDate != null");      

  } 
  
  


  
  public void getSolexaLanePipelineStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");
    queryBuf.append(" LEFT JOIN      l.flowCellChannel as ch ");
    queryBuf.append(" LEFT JOIN      ch.flowCell as fc ");

    addRequestCriteria();
    addSecurityCriteria();
    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.SOLEXA_REQUEST_CATEGORY);
    queryBuf.append("', ");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");

    queryBuf.append("        group by s.number, ch.pipelineDate ");
    
    queryBuf.append("        having ch.pipelineDate != null");      

  } 
  
  
  
}
