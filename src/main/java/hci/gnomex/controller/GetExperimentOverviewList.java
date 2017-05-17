package hci.gnomex.controller;

import hci.gnomex.model.ExperimentOverviewFilter;
import hci.gnomex.model.Step;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetExperimentOverviewList extends GNomExCommand implements Serializable {


private static Logger LOG = Logger.getLogger(GetExperimentOverviewList.class);

  private ExperimentOverviewFilter filter;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new ExperimentOverviewFilter();

    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {

    try {

//      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        /// Process filter calling here
        TreeMap clusterGenMap = new TreeMap();
        Document doc = new Document(new Element("ExperimentOverviewList"));

        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
        List rows = (List) sess.createQuery(queryBuf.toString()).list();

        for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
            Object[] row = (Object[]) i1.next();

            Element n = new Element("ExperimentOverview");

            n.setAttribute("sampleId",              	 row[0] == null ? "" :  ((Integer)row[0]).toString());
            n.setAttribute("sampleName",              	 row[1] == null ? "" :  (String)row[1]);
            n.setAttribute("sampleBarcode",              row[2] == null ? "" :  (String)row[2]);
            n.setAttribute("samplePrepDate",             row[3] == null ? "" :  this.formatDate((java.util.Date)row[3]));
            n.setAttribute("expCodeApp",	             row[4] == null ? "" :  (String)row[4]);
            n.setAttribute("experimentId", 	             row[5] == null ? "" :  (String)row[5]);
            n.setAttribute("expCreateDate",              row[6] == null ? "" :  this.formatDate((java.util.Date)row[6]));
            n.setAttribute("expStatus",  	             row[7] == null ? "" :  (String)row[7]);
            n.setAttribute("userFirstName",          	 row[8] == null ? "" :  (String)row[8]);
            n.setAttribute("userLastName",               row[9] == null ? "" :  (String)row[9]);
            n.setAttribute("labId",              	 	 row[10] == null ? "" :  ((Integer)row[10]).toString());
            n.setAttribute("labFirstName",		         row[11] == null ? "" :  (String)row[11]);
            n.setAttribute("expReadApp",	             row[12] == null ? "" :  (String)row[12]);
            n.setAttribute("expSeqRunType", 			 row[13] == null ? "" : (String)row[13]);
            n.setAttribute("expNumSeqCycles",			 row[14] == null ? "" : ((Integer)row[14]).toString());
            n.setAttribute("expInstrument", 			 row[15] == null ? "" : (String)row[15]);
            n.setAttribute("labLastName",		         row[16] == null ? "" :  (String)row[16]);
            n.setAttribute("sampleCodeStepNext",		 row[17] == null ? "" : (String)row[17]);
            n.setAttribute("idCoreFacility",	 		 row[18] == null ? "" : ((Integer)row[18]).toString());
            n.setAttribute("sampleNumber",	 		 	 row[19] == null ? "" : (String)row[19]);

            String labFullName = "";
            if (row[16] != null) {
              labFullName = (String)row[16];
            }
            if (row[11] != null) {
              if (labFullName.length() > 0) {
                labFullName += ", ";
              }
              labFullName += (String)row[11];
            }

            n.setAttribute("labFullName", labFullName);

            String userFullName = "";
            if (row[9] != null) {
              userFullName = (String)row[9];
            }
            if (row[8] != null) {
              if (userFullName.length() > 0) {
                userFullName += ", ";
              }
              userFullName += (String)row[8];
            }

            n.setAttribute("userFullName", userFullName);

            // Add node content to rootElement XML output.
            doc.getRootElement().addContent(n);
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        // Send redirect with response SUCCESS or ERROR page.
        setResponsePage(this.SUCCESS_JSP);
/*
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }*/

    } catch (Exception e) {
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetExperimentOverviewList ", e);
      throw new RollBackCommandException(e.getMessage());
    }

    return this;
  }


}