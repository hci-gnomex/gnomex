package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ChromatogramParser;
import hci.gnomex.utility.PlateWellParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;

public class SavePlateWellList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SavePlateWellList.class);

  private String                plateWellXMLString;
  private Document              plateWellDoc;
  private PlateWellParser       parser;

  private String                serverName = null;
  private String                launchAppURL;
  private String                appURL;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("plateWellXMLString") != null && !request.getParameter("plateWellXMLString").equals("")) {
      plateWellXMLString = request.getParameter("plateWellXMLString");

      StringReader reader = new StringReader(plateWellXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        plateWellDoc = sax.build(reader);
        parser = new PlateWellParser(plateWellDoc);
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse plateWellXMLString", je );
        this.addInvalidField( "PlateWellXMLString", "Invalid xml");
      }
    }

    serverName = request.getServerName();

    try {
      launchAppURL = this.getLaunchAppURL(request);      
      appURL = this.getAppURL(request);      
    } catch (Exception e) {
      LOG.warn("Cannot get launch app URL in SavePlateWellList", e);
    }

  }

  public Command execute() throws RollBackCommandException {

    if (plateWellXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());

        if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

          parser.parse(sess); //this toggles redo flag for source wells.
          sess.flush();

          //Now we grab instrument runs associated with the source wells that came in and check if all redo flags are removed for the request.  If so mark experiment complete
          //The plate wells that come through will be source wells.  Find the appropriate reaction well and get instrument run
          Map instrumentRunMap  = new HashMap();
          for(Iterator i = parser.getWellMap().keySet().iterator(); i.hasNext();) {
            String idPlateWellString = (String)i.next();
            PlateWell pw = (PlateWell)parser.getWellMap().get(idPlateWellString);
            Request r = pw.getRequest();

            for(Iterator j = r.getPlateWells().iterator(); j.hasNext();) {
              PlateWell rpw = (PlateWell) j.next();
              //code reaction type is null for source wells
              if(rpw.getCodeReactionType() != null) {
                InstrumentRun ir = rpw.getPlate().getInstrumentRun();
                instrumentRunMap.put(ir.getIdInstrumentRun(), ir);
                break;
              }
            }
          }

          //Update request status appropriately
          for(Iterator k = instrumentRunMap.keySet().iterator(); k.hasNext();) {
            Integer idInstrumentRun = (Integer)k.next();
            InstrumentRun ir = (InstrumentRun) instrumentRunMap.get(idInstrumentRun);
            ChromatogramParser.changeRequestsToComplete(sess, ir, this.getSecAdvisor(), launchAppURL, appURL, serverName);
          }


          sess.flush();

          this.xmlResult = "<SUCCESS/>";

          setResponsePage(this.SUCCESS_JSP);          

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to save plateWell list.");
          setResponsePage(this.ERROR_JSP);
        }

      }catch (Exception e){
        LOG.error("An exception has occurred in SavePlateWellList ", e);

        throw new RollBackCommandException(e.getMessage());

      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e){
        LOG.error("Error", e);
      }
      }

    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }

    return this;
  }



}