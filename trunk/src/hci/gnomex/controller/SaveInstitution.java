package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Organism;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
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

    if (request.getParameter("institutionsXMLString") != null
        && !request.getParameter("institutionsXMLString").equals("")) {
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
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      StringBuffer query = new StringBuffer("SELECT i from Institution i");
      List institutions = sess.createQuery(query.toString()).list();

      for (int i = 0; i < institutions.size(); i++) {
        boolean isFound = false;
        Institution dbInstitution = (Institution) institutions.get(i);
        for (Iterator j = this.institutionsDoc.getRootElement().getChildren()
            .iterator(); j.hasNext();) {
          Element node = (Element) j.next();
          Integer id = Integer
          .parseInt(node.getAttributeValue("idInstitution"));

          if (dbInstitution.getIdInstitution().equals(id)) {
            isFound = true;
            break;
          }
        }
        if (!isFound) {
          sess.delete(dbInstitution);
        }

      }

      if (institutionsDoc != null) {

        for (Iterator i = this.institutionsDoc.getRootElement().getChildren()
            .iterator(); i.hasNext();) {
          Element node = (Element) i.next();

          String idInstitution = node.getAttributeValue("idInstitution");
          if (idInstitution == null) {
            inst1 = new Institution();
          } else {
            inst1 = (Institution) sess.load(Institution.class,
                Integer.valueOf(idInstitution));
          }

          inst1.setInstitution(node.getAttributeValue("institution"));
          inst1.setDescription(node.getAttributeValue("description"));
          inst1.setIsActive(node.getAttributeValue("isActive"));
          if (inst1.getIsActive().equals("false"))
            inst1.setIsActive("N");

          if (inst1.getIsActive().equals("true"))
            inst1.setIsActive("Y");

          sess.save(inst1);

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
    + "\"/>";
    setResponsePage(this.SUCCESS_JSP);

    return this;
  }

}
