package hci.gnomex.model;


import java.sql.Date;
import java.util.Iterator;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class SlideProductFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idSlideProduct;
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT sp");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        SlideProduct as sp ");
    
    addCriteria();
    
    queryBuf.append(" order by sp.name ");
  
  }
  
  

  private void addCriteria() {
    //  Search by idSlideProduct 
    if (idSlideProduct != null){
      this.addWhereOrAnd();
      queryBuf.append(" sp.idSlideProduct =");
      queryBuf.append(idSlideProduct);
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

  
  public Integer getIdSlideProduct() {
    return idSlideProduct;
  }

  
  public void setIdSlideProduct(Integer idSlideProduct) {
    this.idSlideProduct = idSlideProduct;
  }

 
  


  
  
}
