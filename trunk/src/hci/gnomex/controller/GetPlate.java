package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetPlate extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPlate.class);

  private Integer                   idPlate;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idPlate") != null) {
      idPlate = new Integer(request.getParameter("idPlate"));
    } else {
      this.addInvalidField("idPlate", "idPlate is required");
    }
    this.validate();

  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      Plate p = null;

      if (idPlate == null || idPlate.intValue() == 0) {
        p = new Plate();
      } else {
        p = (Plate)sess.get(Plate.class, idPlate);
      }
      
      if (p == null) {
        this.addInvalidField("missingPlate", "Cannot find plate idPlate=" + idPlate );
      }
      
      
      Document doc = new Document(new Element("PlateList"));

      p.excludeMethodFromXML("getPlateWells");
      Element pNode = p.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      
      Element pwNode = new Element("plateWells");
      
      List plateWells = sess.createQuery("SELECT pw from PlateWell as pw where pw.idPlate=" + idPlate).list();

      for(Iterator i = plateWells.iterator(); i.hasNext();) {
        PlateWell plateWell = (PlateWell)i.next();
        plateWell.excludeMethodFromXML("getPlate");
        
        Element node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        
        String idRequestString = plateWell.getIdRequest().toString();
        if ( idRequestString != null && !idRequestString.equals("")) {
          Request request = (Request) sess.createQuery("SELECT r from Request as r where r.idRequest=" + idRequestString).uniqueResult();
          if ( request != null ) {
            node.setAttribute("submitDate", request.getCreateDate().toString());
            node.setAttribute("submitter", request.getOwnerName());
            
          }
        }
      

        pwNode.addContent(node);
      }
      
      pNode.addContent(pwNode);
      doc.getRootElement().addContent(pNode);

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetPlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetPlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetPlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetPlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}