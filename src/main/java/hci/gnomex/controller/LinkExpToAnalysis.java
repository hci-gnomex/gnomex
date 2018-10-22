package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisExperimentItem;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
public class LinkExpToAnalysis extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetRequestList.class);

  private Integer idRequest;
  private Integer idAnalysis;

  @Override
  public void loadCommand(HttpServletRequest req, HttpSession sess) {
    if (req.getParameter("idRequest") != null && !req.getParameter("idRequest").equals("")) {
      idRequest = Integer.parseInt(req.getParameter("idRequest"));
    } else {
      this.addInvalidField("Missing idRequest", "idRequest is required");
    }

    if (req.getParameter("idAnalysis") != null && !req.getParameter("idAnalysis").equals("")) {
      idAnalysis = Integer.parseInt(req.getParameter("idAnalysis"));
    } else {
      this.addInvalidField("Missing idAnalysis", "idAnalysis is required");
    }

  }

  @Override
  public Command execute() throws GNomExRollbackException {

    try {
      Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());
      // first check to see if there is already a link between this request and analysis
      Query q = sess.createQuery("Select aei from AnalysisExperimentItem aei where aei.idRequest=? and aei.idAnalysis=?");
      q.setParameter(0, idRequest);
      q.setParameter(1, idAnalysis);
      if (q.list().size() != 0) {
        this.addInvalidField("A link already exists", "A link already exists between this request and analysis.");
      }

      if (this.isValid()) {

        Request r = sess.load(Request.class, idRequest);

        if (r.getSequenceLanes().size() > 0) {
          for (Iterator i = r.getSequenceLanes().iterator(); i.hasNext();) {
            SequenceLane sl = (SequenceLane) i.next();
            AnalysisExperimentItem aei = new AnalysisExperimentItem();
            aei.setIdRequest(idRequest);
            aei.setIdAnalysis(idAnalysis);
            aei.setIdSequenceLane(sl.getIdSequenceLane());

            sess.save(aei);
          }

        } else if (r.getHybridizations().size() > 0) {
          for (Iterator i = r.getHybridizations().iterator(); i.hasNext();) {
            Hybridization h = (Hybridization) i.next();
            AnalysisExperimentItem aei = new AnalysisExperimentItem();
            aei.setIdRequest(idRequest);
            aei.setIdAnalysis(idAnalysis);
            aei.setIdSequenceLane(h.getIdHybridization());
            sess.save(aei);
          }

        } else {
          for (Iterator i = r.getSamples().iterator(); i.hasNext();) {
            Sample s = (Sample) i.next();
            AnalysisExperimentItem aei = new AnalysisExperimentItem();
            aei.setIdRequest(idRequest);
            aei.setIdAnalysis(idAnalysis);
            aei.setIdSample(s.getIdSample());     // changed from setIdSequenceLane tim 02/27/2018
            sess.save(aei);
          }
        }

        sess.flush();
        this.setResponsePage(this.SUCCESS_JSP);

      } else {
        this.setResponsePage(this.ERROR_JSP);

      }

    } catch (Exception e) {

      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in LinkExpToAnalysis ", e);

      throw new GNomExRollbackException(e.getMessage(), true, "Unable to link analysis to experiment");
    }

    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
