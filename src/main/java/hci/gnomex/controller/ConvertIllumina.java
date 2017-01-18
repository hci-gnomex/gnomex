package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class ConvertIllumina extends GNomExCommand implements Serializable {
	// the static field for logging in Log4J
	private static Logger LOG = Logger.getLogger(ConvertIllumina.class);

	private Integer idRequest;
	private Integer idNumberSequencingCyclesAllowed;
	private Integer idNumberSequencingCycles;
	private String codeApplication;
	private Integer idSeqLibProtocol;

	public void loadCommand(HttpServletRequest request, HttpSession sess) {
		if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
			this.idRequest = Integer.parseInt(request.getParameter("idRequest"));
		} else {
			this.addInvalidField("idRequest", "Missing idRequest");
		}

		if (request.getParameter("idNumberSequencingCyclesAllowed") != null && !request.getParameter("idNumberSequencingCyclesAllowed").equals("")) {
			this.idNumberSequencingCyclesAllowed = Integer.parseInt(request.getParameter("idNumberSequencingCyclesAllowed"));

		} else {
			this.addInvalidField("idNumberSequencingCyclesAllowed", "Missing idNumberSequencingCyclesAllowed");
		}

		if (request.getParameter("idSeqLibProtocol") != null && !request.getParameter("idSeqLibProtocol").equals("")) {
			this.idSeqLibProtocol = Integer.parseInt(request.getParameter("idSeqLibProtocol"));

		} else {
			this.addInvalidField("idSeqLibProtocol", "Missing idSeqLibProtocol");
		}

		if (request.getParameter("idNumberSequencingCycles") != null && !request.getParameter("idNumberSequencingCycles").equals("")) {
			this.idNumberSequencingCycles = Integer.parseInt(request.getParameter("idNumberSequencingCycles"));

		} else {
			this.addInvalidField("idNumberSequencingCycles", "Missing idNumberSequencingCycles");
		}

		if (request.getParameter("codeApplication") != null && !request.getParameter("codeApplication").equals("")) {
			this.codeApplication = request.getParameter("codeApplication");

		} else {
			this.addInvalidField("codeApplication", "Missing codeApplication");
		}

	}

	public Command execute() throws RollBackCommandException {

		try {
			Session sess = HibernateSession.currentSession(this.getUsername());

			Request r = sess.load(Request.class, idRequest);

			// first check the work items. If there are any past the QC or PREP phase then we can't convert
			for (Iterator i = r.getWorkItems().iterator(); i.hasNext();) {
				WorkItem wi = (WorkItem) i.next();
				if (!wi.getCodeStepNext().equals(Step.HISEQ_QC) && !wi.getCodeStepNext().equals(Step.HISEQ_PREP) && !wi.getCodeStepNext().equals(Step.MISEQ_QC)
						&& !wi.getCodeStepNext().equals(Step.MISEQ_PREP)) {
					this.addInvalidField("Cannot convert Experiment",
							"This request contains work items that have passed the library prep phase.  Therefore conversion is not possible.");
					break;
				}
			}

			if (this.isValid()) {
				boolean originallyHISEQ = false;

				// update the code requeset category
				if (r.getCodeRequestCategory().equals(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY)) {
					r.setCodeRequestCategory(RequestCategory.ILLUMINA_MISEQ_REQUEST_CATEGORY);
					originallyHISEQ = true;
				} else if (r.getCodeRequestCategory().equals(RequestCategory.ILLUMINA_MISEQ_REQUEST_CATEGORY)) {
					r.setCodeRequestCategory(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY);
				}

				// update the code application
				r.setCodeApplication(this.codeApplication);

				// update the sequence lanes of the request to have the new num seq cycles
				for (Iterator i = r.getSequenceLanes().iterator(); i.hasNext();) {
					SequenceLane sl = (SequenceLane) i.next();
					sl.setIdNumberSequencingCycles(this.idNumberSequencingCycles);
					sl.setIdNumberSequencingCyclesAllowed(this.idNumberSequencingCyclesAllowed);
					sess.save(sl);
				}

				// update the samples to have the seq lib protocol that is tied with the new application
				for (Iterator i = r.getSamples().iterator(); i.hasNext();) {
					Sample s = (Sample) i.next();
					s.setIdSeqLibProtocol(this.idSeqLibProtocol);
					sess.save(s);
				}

				// update the work items to have the correct work flow
				// They can only be QC or Prep items
				for (Iterator i = r.getWorkItems().iterator(); i.hasNext();) {
					WorkItem wi = (WorkItem) i.next();
					if (originallyHISEQ) {
						if (wi.getCodeStepNext().equals(Step.HISEQ_QC)) {
							wi.setCodeStepNext(Step.MISEQ_QC);
						} else {
							wi.setCodeStepNext(Step.MISEQ_PREP);
						}
					} else {
						if (wi.getCodeStepNext().equals(Step.MISEQ_QC)) {
							wi.setCodeStepNext(Step.HISEQ_QC);
						} else {
							wi.setCodeStepNext(Step.HISEQ_PREP);
						}
					}
					sess.save(wi);
				}

				sess.save(r);
				sess.flush();

				this.setResponsePage(this.SUCCESS_JSP);

			} else {
				this.setResponsePage(this.ERROR_JSP);
			}

		} catch (Exception e) {
			this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in ConvertIllumina ", e);

			throw new RollBackCommandException(e.getMessage());

		}
		return this;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
