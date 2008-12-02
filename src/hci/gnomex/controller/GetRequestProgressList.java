package hci.gnomex.controller;

import hci.gnomex.model.RequestProgressFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetRequestProgressList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequestProgressList.class);
  
  private RequestProgressFilter filter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new RequestProgressFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasCriteria()) {
      this.addInvalidField("filterRequired", "Please enter at least one search criterion");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
      StringBuffer buf = filter.getMicroarrayQuery(this.getSecAdvisor());
      log.info(buf.toString());
      List rows1 = (List)sess.createQuery(buf.toString()).list();
      TreeMap rowMap = new TreeMap(new HybSampleComparator());
      for(Iterator i = rows1.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String hybNumber     = row[5] == null || row[5].equals("") ? "" : (String)row[5];
        String key = requestNumber + "," + hybNumber + ",0";
        
        rowMap.put(key, row);
      }
      
      buf = filter.getQualityControlQuery(this.getSecAdvisor());
      log.info(buf.toString());
      List rows2 = (List)sess.createQuery(buf.toString()).list();
      for(Iterator i = rows2.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String sampleNumber     = row[11] == null || row[11].equals("") ? "" : (String)row[11];
        String key = requestNumber + ",0," + sampleNumber;
        
        rowMap.put(key, row);
      }
      
      boolean alt = false;
      String prevRequestNumber = "";
      
      
    
      Document doc = new Document(new Element("RequestProgressList"));
      for(Iterator i = rowMap.keySet().iterator(); i.hasNext();) {
        String key = (String)i.next();
        Object[] row = (Object[])rowMap.get(key);
        
        String requestNumber = (String)row[1];
        if (!requestNumber.equals(prevRequestNumber)) {
          alt = !alt;
        }
        
        
        
        Element n = new Element("RequestProgress");
        n.setAttribute("key", key);
        n.setAttribute("isSelected", "N");
        n.setAttribute("altColor", new Boolean(alt).toString());
        n.setAttribute("showRequestNumber", !requestNumber.equals(prevRequestNumber) ? "Y" : "N");        
        n.setAttribute("idRequest", row[19].toString());
        n.setAttribute("createDate", this.formatDate((java.sql.Date)row[0]));
        n.setAttribute("requestNumber", (String)row[1]);
        n.setAttribute("codeRequestCategory", row[2] == null ? "" : (String)row[2]);
        n.setAttribute("codeMicroarrayCategory", row[3] == null ? "" : (String)row[3]);
        n.setAttribute("idAppUser", row[4] == null ? "" : ((Integer)row[4]).toString());
        n.setAttribute("hybNumber", row[5] == null ? "" : (String)row[5]);
        n.setAttribute("hybDate", row[6] == null || row[6].equals("") ? "" : this.formatDate((java.sql.Date)row[6]));
        n.setAttribute("extractionDate", row[7] == null || row[7].equals("") ? "" : this.formatDate((java.sql.Date)row[7]));
        n.setAttribute("hybFailed", row[8] == null ? "" : (String)row[8]);
        n.setAttribute("labelingDateSample1", row[9] == null || row[9].equals("")? "" : this.formatDate((java.sql.Date)row[9]));
        n.setAttribute("qualDateSample1", row[10] == null || row[10].equals("")? "" : this.formatDate((java.sql.Date)row[10]));
        n.setAttribute("numberSample1", row[11] == null ? "" :  (String)row[11]);
        n.setAttribute("nameSample1", row[12] == null ? "" :  (String)row[12]);
        n.setAttribute("labelingDateSample2", row[13] == null || row[13].equals("") ? "" : this.formatDate((java.sql.Date)row[13]));
        n.setAttribute("qualDateSample2", row[14] == null || row[14].equals("") ? "" : this.formatDate((java.sql.Date)row[14]));
        n.setAttribute("numberSample2", row[15] == null ? "" :  (String)row[15]);
        n.setAttribute("nameSample2", row[16] == null ? "" :  (String)row[16]);
        n.setAttribute("idLab", row[17] == null ? "" : ((Integer)row[17]).toString());
        n.setAttribute("hasResults", row[18] == null ? "" : (String)row[18]);
        n.setAttribute("ownerFirstName", row[20] == null ? "" : (String)row[20]);
        n.setAttribute("ownerLastName", row[21] == null ? "" : (String)row[21]);
        doc.getRootElement().addContent(n);
        
        prevRequestNumber = requestNumber;
        
      }
    
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestProgressList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestProgressList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequestProgressList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

  public static class  HybSampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      String reqNumber1    = tokens1[0];
      String hybNumber1    = tokens1[1];
      String itemNumber1   = tokens1[2];
      
      String reqNumber2    = tokens2[0];
      String hybNumber2    = tokens2[1];
      String itemNumber2   = tokens2[2];
      
      String number1 = null;
      
      if (hybNumber1.equals("0")) {
        String[] itemNumberTokens1 = itemNumber1.split("X");
        number1 = itemNumberTokens1[itemNumberTokens1.length - 1];        
      } else {
        String[] hybNumberTokens1 = hybNumber1.split("E");
        number1 = hybNumberTokens1[hybNumberTokens1.length - 1];        
      }
      
      
      String number2 = null;
      
      
      if (hybNumber2.equals("0")) {
        String[] itemNumberTokens2 = itemNumber2.split("X");
        number2 = itemNumberTokens2[itemNumberTokens2.length - 1];        
      } else {
        String[] hybNumberTokens2 = hybNumber2.split("E");
        number2 = hybNumberTokens2[hybNumberTokens2.length - 1];        
      }



      if (reqNumber1.equals(reqNumber2)) {
        return new Integer(number1).compareTo(new Integer(number2));        
      } else {
        return reqNumber1.compareTo(reqNumber2);
      }
      
    }
  }
  
  
}