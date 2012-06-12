package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.InstrumentRun;
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


public class GetInstrumentRun extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetInstrumentRun.class);

  private Integer                   idInstrumentRun;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idInstrumentRun") != null) {
      idInstrumentRun = new Integer(request.getParameter("idInstrumentRun"));
    } else {
      this.addInvalidField("idInstrumentRun", "idInstrumentRun is required");
    }
    this.validate();

  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      InstrumentRun ir = null;

      if (idInstrumentRun == null || idInstrumentRun.intValue() == 0) {
        ir = new InstrumentRun();
      } else {
        ir = (InstrumentRun)sess.get(InstrumentRun.class, idInstrumentRun);
      }

      if (ir == null) {
        this.addInvalidField("missing run", "Cannot find InstrumentRun idInstrumentRun=" + idInstrumentRun );
      }


      Document doc = new Document(new Element("RunList"));

      Element iNode = ir.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

      List plates = sess.createQuery("SELECT p from Plate as p where p.idInstrumentRun=" + idInstrumentRun).list();

      for(Iterator i = plates.iterator(); i.hasNext();) {
        Plate plate = (Plate)i.next();

        plate.excludeMethodFromXML("getPlateWells");
        plate.excludeMethodFromXML("getInstrumentRun");
        Element pNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

        Element pwNode = new Element("plateWells");

        List plateWells = sess.createQuery("SELECT pw from PlateWell as pw where pw.idPlate=" + plate.getIdPlate()).list();

        for(Iterator i1 = plateWells.iterator(); i1.hasNext();) {
          PlateWell plateWell = (PlateWell)i1.next();
          plateWell.excludeMethodFromXML("getPlate");

          Element node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

          if ( plateWell.getIdRequest() != null ) {
            String idRequestString = plateWell.getIdRequest().toString();
            if ( idRequestString != null && !idRequestString.equals("")) {
              Request request = (Request) sess.createQuery("SELECT r from Request as r where r.idRequest=" + idRequestString).uniqueResult();
              if ( request != null ) {
                node.setAttribute("requestSubmitDate", request.getCreateDate().toString());
                node.setAttribute("requestSubmitter", request.getOwnerName());

              }
            }
          }
          
          pwNode.addContent(node);
        }
        pNode.addContent(pwNode);

        iNode.addContent(pNode);
      }


      doc.getRootElement().addContent(iNode);

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetInstrumentRun ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetInstrumentRun ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetInstrumentRun ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetInstrumentRun ", e);
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