package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Lab;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;
public class GetAllLabs extends GNomExCommand implements Serializable {
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetLabList.class);

  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {

  }

  @Override
  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      Document doc = new Document(new Element("LabList"));

      List rows = sess.createQuery("Select l.idLab, l.firstName, l.lastName from Lab as l order by l.lastName ").list();

      for (Iterator i = rows.iterator(); i.hasNext();) {
        Element labNode = new Element("Lab");
        Object[] row = (Object[]) i.next();

        Integer idLab = (Integer) row[0];
        String firstName = row[1] != null ? (String) row[1] : "";
        String lastName = row[2] != null ? (String) row[2] : "";

        labNode.setAttribute("idLab", idLab.toString());

        if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(idLab)) {
          labNode.setAttribute("isMyLab", "Y");
        } else {
          labNode.setAttribute("isMyLab", "N");
        }

        labNode.setAttribute("name", Lab.formatLabName(lastName, firstName));

        doc.getRootElement().addContent(labNode);
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      setResponsePage(this.SUCCESS_JSP);

    } catch (Exception e) {
      e.printStackTrace();

    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {

      }
    }
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
