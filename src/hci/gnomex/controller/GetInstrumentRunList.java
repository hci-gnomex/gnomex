package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.InstrumentRunFilter;
import hci.gnomex.model.Plate;

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


public class GetInstrumentRunList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetInstrumentRunList.class);

  private InstrumentRunFilter  runFilter;
  private String               listKind = "RunList";
  private Element              rootNode = null;
  private String               message = "";

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    runFilter = new InstrumentRunFilter();
    HashMap errors = this.loadDetailObject(request, runFilter);
    this.addInvalidFields(errors);

    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");

    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Document doc = new Document(new Element(listKind));

      if (!runFilter.hasSufficientCriteria(this.getSecAdvisor())) {
        message = "Please select a filter";
        rootNode.setAttribute("message", message);
      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

        StringBuffer buf = runFilter.getQuery(this.getSecAdvisor());
        log.info("Query for GetInstrumentRunList: " + buf.toString());
        List runs = sess.createQuery(buf.toString()).list();

        for(Iterator i = runs.iterator(); i.hasNext();) {

          Object[] row = (Object[])i.next();

          Integer idInstrumentRun = row[0] == null ? new Integer(-2) : (Integer)row[0];
          String runDate    = this.formatDate((java.sql.Timestamp)row[1]);
          //        String  status    = row[2] == null ? "" : (String)row[2];

          Element iNode = new Element("InstrumentRun");
          iNode.setAttribute("idInstrumentRun", idInstrumentRun.toString());
          iNode.setAttribute("runDate", runDate);

          List plates = sess.createQuery("SELECT p from Plate as p where p.idInstrumentRun=" + idInstrumentRun).list();

          for(Iterator i2 = plates.iterator(); i2.hasNext();) {
            Plate plate = (Plate)i2.next();

            Element node = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

            iNode.addContent(node);
          }

          doc.getRootElement().addContent(iNode);

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