package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Institution;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SaveInstitution extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger
  .getLogger(SaveInstitution.class);

  private String                         institutionsXMLString;
  private Document                       institutionsDoc;
  private Institution                    inst;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("institutionsXMLString") != null && !request.getParameter("institutionsXMLString").equals("")) {
      institutionsXMLString = request.getParameter("institutionsXMLString");
      StringReader reader = new StringReader(institutionsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        institutionsDoc = sax.build(reader);
      } catch (JDOMException je) {
        log.error("Cannot parse institutionsXMLString", je);
        this.addInvalidField("institutionsXMLString",
        "Invalid institutionsXMLString");
      }
    }
    if (institutionsDoc == null) {
      this.addInvalidField("institutionsXMLString",
      "institutionsXMLString is required");
    }

  }

  public Command execute() throws RollBackCommandException {
    Institution inst1 = null;
    List institutesToDelete = new ArrayList();
    String unRemovableInst = "";

    try {
    if(this.isValid()){
        Session sess = HibernateSession.currentSession(this.getUsername());

        StringBuffer query = new StringBuffer("SELECT i from Institution i");
        List institutions = sess.createQuery(query.toString()).list();

        for (int i = 0; i < institutions.size(); i++) {
          boolean isFound = false;
          Institution dbInstitution = (Institution) institutions.get(i);
          for (Iterator j = this.institutionsDoc.getRootElement().getChildren().iterator(); j.hasNext();) {
            Element node = (Element) j.next();
            //If it isn't a new institution and the inst. from the doc is in the database then we don't delete it
            if (!node.getAttributeValue("idInstitution").equals("") && 
                dbInstitution.getIdInstitution().equals(Integer.parseInt(node.getAttributeValue("idInstitution")))) {
              isFound = true;
              break;
            }
          }
          //if we can't find it, delete it
          if (!isFound) {
            institutesToDelete.add(dbInstitution);
          }
        }

        //save new institutions and update old ones with any changed information
        if (institutionsDoc != null) {
          for (Iterator i = this.institutionsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element) i.next();

            String idInstitution = node.getAttributeValue("idInstitution");
            if (idInstitution == null || idInstitution.equals("")) {
              inst1 = new Institution();
            } 
            else {
              inst1 = (Institution) sess.load(Institution.class, Integer.valueOf(idInstitution));
            }

            inst1.setInstitution(node.getAttributeValue("institution"));
            inst1.setDescription(node.getAttributeValue("description"));
            inst1.setIsActive(node.getAttributeValue("isActive"));
            sess.save(inst1);
          }
          sess.flush();
        }


        //Before deleting institutions we need to check if they are assoicated with data.  If they are don't delete and inform user.
        for (Iterator j = institutesToDelete.iterator(); j.hasNext();){
          Institution inst = (Institution)j.next();
          List results = sess.createQuery("Select req.idRequest from Request req where req.idInstitution =  " + inst.getIdInstitution()).list();
          if(results.size() > 0){
            unRemovableInst += inst.getInstitution() + ", ";
            continue;
          }
          
          results = sess.createQuery("Select a.idAnalysis from Analysis a where a.idInstitution =  " + inst.getIdInstitution()).list();
          if(results.size() > 0){
            unRemovableInst += inst.getInstitution() + ", ";
            continue;
          }
          
          results = sess.createQuery("Select dt.idDataTrack from DataTrack dt where dt.idInstitution =  " + inst.getIdInstitution()).list();
          if(results.size() > 0){
            unRemovableInst += inst.getInstitution() + ", ";
            continue;
          }
          
          results = sess.createQuery("Select t.idTopic from Topic t where t.idInstitution =  " + inst.getIdInstitution()).list();
          if(results.size() > 0){
            unRemovableInst += inst.getInstitution() + ", ";
            continue;
          }
          
          sess.delete(inst);
        }
        
        if(!unRemovableInst.equals("")){
          unRemovableInst = unRemovableInst.substring(0, unRemovableInst.lastIndexOf(","));
        }
        sess.flush();
        
      }

      else {
        this.addInvalidField("Insufficient permissions",
        "Insufficient permission to save new Institutions.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e) {
      log.error("An exception has occurred in SaveInstitution ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();
      } catch (Exception e) {
      }
    }

    this.xmlResult = "<SUCCESS institution=\"" + inst1.getIdInstitution()
    + "\" unRemovableInst = \"" + unRemovableInst + "\"/>";
    setResponsePage(this.SUCCESS_JSP);

    return this;
  }

}
