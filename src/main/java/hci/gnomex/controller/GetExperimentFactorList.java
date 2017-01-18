package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.Annotations;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.ExperimentFactor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetExperimentFactorList extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetExperimentFactorList.class);


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
  }

  public Command execute() throws RollBackCommandException {

    try {


    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());



    // Get codes that are used
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT distinct ef.codeExperimentFactor from ExperimentFactorEntry as ef ");
    List usedCodes = (List)sess.createQuery(queryBuf.toString()).list();


    //  Now get all used experiment factors
    List usedFactors = new ArrayList<ExperimentFactor>();
    if (usedCodes.size() > 0) {
      queryBuf = new StringBuffer();
      queryBuf.append("SELECT ef from ExperimentFactor as ef ");
      if (usedCodes.size() > 0) {
        queryBuf.append(" where ef.codeExperimentFactor in (");
        for(Iterator i = usedCodes.iterator(); i.hasNext();) {
          String code= (String)i.next();
          queryBuf.append("'" + code + "'");
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }
        queryBuf.append(")");
      }
      usedFactors = sess.createQuery(queryBuf.toString()).list();
    }


    // Now get all other experiment factors
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT ef from ExperimentFactor as ef ");
    if (usedCodes.size() > 0) {
      queryBuf.append(" where ef.codeExperimentFactor not in (");
      for(Iterator i = usedCodes.iterator(); i.hasNext();) {
        String code= (String)i.next();
        queryBuf.append("'" + code + "'");
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(")");
    }

    List notUsedFactors = (List)sess.createQuery(queryBuf.toString()).list();


    // Generate XML for each experiment factor.
    Document doc = new Document(new Element("ExperimentFactorList"));
    generateXML(doc, usedFactors,    "Y");
    generateXML(doc, notUsedFactors, "N");


    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);

    setResponsePage(this.SUCCESS_JSP);
    }catch (Exception e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetExperimentFactorList ", e);
      throw new RollBackCommandException(e.getMessage());
    }

    return this;
  }

  private void generateXML(Document doc, List factors, String isUsed) throws XMLReflectException {
    for(Iterator i = factors.iterator(); i.hasNext();) {
      ExperimentFactor ef = (ExperimentFactor)i.next();
      Element node = ef.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL, null, Annotations.IGNORE).getRootElement();
      node.setAttribute("isUsed", isUsed);
      node.setAttribute("isSelected", "false");
      doc.getRootElement().addContent(node);
    }
  }

}