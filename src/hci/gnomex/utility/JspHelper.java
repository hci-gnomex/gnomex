package hci.gnomex.utility;

import hci.gnomex.model.PropertyDictionary;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.query.Query;
import org.hibernate.Session;

public class JspHelper implements Serializable {

  public static Integer getIdCoreFacility(HttpServletRequest request) {
    String idCoreAsString = (String)((request.getParameter("idCore") != null)?request.getParameter("idCore"):"");
    Integer idCoreFacility = null;
    try {
      idCoreFacility = Integer.valueOf(idCoreAsString);
    } catch(NumberFormatException ex) {
      idCoreFacility = null;
    }
    
    return idCoreFacility;
  }
}