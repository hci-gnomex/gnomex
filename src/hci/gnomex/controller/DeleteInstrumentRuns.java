package hci.gnomex.controller;


import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ChromatogramParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class DeleteInstrumentRuns extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteInstrumentRuns.class);

  private String runsToDeleteXMLString;
  private Document runsToDeleteDoc;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("runsToDeleteXMLString") != null && !request.getParameter("runsToDeleteXMLString").equals("")) {
      runsToDeleteXMLString = request.getParameter("runsToDeleteXMLString");
      StringReader reader = new StringReader(runsToDeleteXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        runsToDeleteDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse runsToDeleteXMLString", je );
        this.addInvalidField( "runsToDeleteXMLString", "Invalid runsToDeleteXMLString");
      }
    } 


  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      //Change this conditional to check for this.getSecurityAdvisor().hasPermission(SecurityAdvisor.canDelete())
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
        for(Iterator i = this.runsToDeleteDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
          Element node = (Element)i.next();
          Integer idInstrumentRun = Integer.parseInt(node.getText());
          InstrumentRun ir = (InstrumentRun) sess.load(InstrumentRun.class, idInstrumentRun);
          changeStatusDeletePlates(sess, ir, RequestStatus.PROCESSING);
          sess.delete(ir); //delete the instrument run after all associations to it have been removed(ie:plate, plate wells)

        }       
        sess.flush();
        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to edit dictionareis.");
        setResponsePage(this.ERROR_JSP);
      }


    }catch (Exception e){
      LOG.error("An exception has occurred in DeleteInstrumentRuns ", e);

      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        //closeHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in DeleteInstrumentRuns ", e);
      }
    }

    return this;
  }

  //This function reverts status of plate back to what it was before being put on the run.
  private void changeStatusDeletePlates( Session sess, InstrumentRun ir, String status ) {

    // Get any requests on that run
    Map requests = new HashMap();
    ChromatogramParser cp = new ChromatogramParser();
    List plates = sess.createQuery( "SELECT p from Plate as p " +
        " where p.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();

    for(Iterator i1 = plates.iterator(); i1.hasNext();) {
      Plate plate = (Plate)i1.next();
      plate.setIdInstrumentRun(null);
      plate.setQuadrant(-1);
      sess.update(plate);
    }

    sess.flush();
  }



}