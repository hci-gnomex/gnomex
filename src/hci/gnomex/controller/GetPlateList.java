package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.PlateFilter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetPlateList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPlateList.class);

  private PlateFilter          plateFilter;
  private String               listKind = "PlateList";
  private Element              rootNode = null;
  private String               message = "";

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    plateFilter = new PlateFilter();
    HashMap errors = this.loadDetailObject(request, plateFilter);
    this.addInvalidFields(errors);

    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");

    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Document doc = new Document(new Element(listKind));
      
      if (!plateFilter.hasSufficientCriteria(this.getSecAdvisor())) {
        message = "Please select a filter";
        rootNode.setAttribute("message", message);

      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        StringBuffer buf = plateFilter.getQuery(this.getSecAdvisor());
        log.info("Query for GetPlateList: " + buf.toString());
        List runs = sess.createQuery(buf.toString()).list();

        for(Iterator i = runs.iterator(); i.hasNext();) {

          Object[] row = (Object[])i.next();

          Integer idPlate = row[0] == null ? new Integer(-2) : (Integer)row[0];
          Integer idInstrumentRun = row[1] == null ? new Integer(-2) : (Integer)row[1];
          //        String  status    = row[2] == null ? "" : (String)row[2];

          Element pNode = new Element("Plate");
          pNode.setAttribute("idPlate", idPlate.toString());
          pNode.setAttribute("idInstrumentRun", idInstrumentRun.toString());


          doc.getRootElement().addContent(pNode);

        }
      }


      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetRunList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }

}