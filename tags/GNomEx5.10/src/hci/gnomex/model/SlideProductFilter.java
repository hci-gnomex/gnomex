package hci.gnomex.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;

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

 public Iterable getSlideProductList(Session sess, SecurityAdvisor secAdvisor) throws UnknownPermissionException {
   List slideProducts = null;
   TreeMap slideProductSortedMap = new TreeMap();
   StringBuffer buf = new StringBuffer();
   
   if (secAdvisor.hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
     buf = this.getQuery(secAdvisor);
     slideProducts = (List)sess.createQuery(buf.toString()).list();
   } else {
     slideProducts = new ArrayList();
   }
   for(Iterator i = slideProducts.iterator(); i.hasNext();) {
     SlideProduct sp = (SlideProduct)i.next();
     slideProductSortedMap.put(sp.getName() + sp.getIdSlideProduct(), sp);
   }
   
   // Figure out the slides that have public experiments on them.  All users
   // can see these slides
   buf = new StringBuffer();
   buf.append("SELECT distinct sp ");
   buf.append("FROM   Request r ");
   buf.append("JOIN   r.slideProduct as sp ");
   buf.append("WHERE  r.codeVisibility = '" + Visibility.VISIBLE_TO_PUBLIC + "'");

   List publicSlideProducts = (List)sess.createQuery(buf.toString()).list();

   // Indicate that this slides have public experiments on them
   for(Iterator i = publicSlideProducts.iterator(); i.hasNext();) {
     SlideProduct sp = (SlideProduct)i.next();
     
     sp.hasPublicExperiments(true);
     slideProductSortedMap.put(sp.getName() + sp.getIdSlideProduct(), sp);
   }

   
   ArrayList finalList = new ArrayList();
   for(Iterator i = slideProductSortedMap.keySet().iterator(); i.hasNext();) {
     String key = (String)i.next();
     
     SlideProduct sp = (SlideProduct)slideProductSortedMap.get(key);
     Hibernate.initialize(sp.getSlideDesigns());
     Hibernate.initialize(sp.getApplications());

     // Don't show any custom slide products that user doesn't have read permission on.
     if (!secAdvisor.canRead(sp)) {
       continue;
     }

     finalList.add(sp);

   }      
   
   return finalList;
 }
  


  
  
}
