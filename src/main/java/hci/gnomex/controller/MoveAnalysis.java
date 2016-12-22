package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.utility.AnalysisGroupParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class MoveAnalysis extends GNomExCommand implements Serializable {

	private static Logger LOG = Logger.getLogger(SaveLab.class);

	private Integer idLab;
	// private Integer idAppUser;
	private String idAnalysisString;
	private Document analysesDoc;
	private String analysisGroupsXMLString;
	private Document analysisGroupsDoc;
	private AnalysisGroupParser analysisGroupParser;

	@Override
	public void loadCommand(HttpServletRequest request, HttpSession sess) {
		if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
			idLab = Integer.parseInt(request.getParameter("idLab"));
		} else {
			this.addInvalidField("missing idlab", "Please provide idLab");
		}

		if (request.getParameter("idAnalysisString") != null && !request.getParameter("idAnalysisString").equals("")) {
			idAnalysisString = request.getParameter("idAnalysisString");
		} else {
			this.addInvalidField("missing idAnalysisString", "Please provide idAnalysisString");
		}

		StringReader reader = null;

		if (request.getParameter("analysisGroupsXMLString") != null && !request.getParameter("analysisGroupsXMLString").equals("")) {
			analysisGroupsXMLString = request.getParameter("analysisGroupsXMLString");
			reader = new StringReader(analysisGroupsXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				analysisGroupsDoc = sax.build(reader);
				analysisGroupParser = new AnalysisGroupParser(analysisGroupsDoc);
			} catch (JDOMException je) {
				LOG.error("Cannot parse analysisGroupsXMLString", je);
				this.addInvalidField("analysisGroupsXMLString", "Invalid analysisGroupsXMLString");
			}
		}

	}

	@Override
	public Command execute() throws RollBackCommandException {
		try {
			if (this.isValid()) {
				Session sess = HibernateSession.currentSession(this.getUsername());
				String idAnalyses[] = idAnalysisString.split(",");
				List<String> invalidPermissions = new ArrayList<String>();

				for (int i = 0; i < idAnalyses.length; i++) {
					TreeSet analysisGroups = new TreeSet(new AnalysisGroupComparator());
					Integer idAnalysis = Integer.parseInt(idAnalyses[i]);
					Analysis a = (Analysis) sess.load(Analysis.class, idAnalysis);

					if (!this.getSecurityAdvisor().canUpdate(a)) {
						invalidPermissions.add(a.getNumber());
						continue;
					}

					a.setIdLab(this.idLab);

					analysisGroupParser.parse(sess);
					if (analysisGroupParser != null && analysisGroupParser.getAnalysisGroupMap().isEmpty()) {
						// If analysis group wasn't provided, create a default one
						AnalysisGroup defaultAnalysisGroup = new AnalysisGroup();
						defaultAnalysisGroup.setName(a.getName());
						defaultAnalysisGroup.setIdLab(a.getIdLab());
						defaultAnalysisGroup.setIdAppUser(this.getSecAdvisor().getIdAppUser());
						sess.save(defaultAnalysisGroup);

						// newAnalysisGroupId = defaultAnalysisGroup.getIdAnalysisGroup();

						analysisGroups.add(defaultAnalysisGroup);
					} else {
						for (Iterator j = analysisGroupParser.getAnalysisGroupMap().keySet().iterator(); j.hasNext();) {
							String idAnalysisGroupString = (String) j.next();
							AnalysisGroup ag = (AnalysisGroup) analysisGroupParser.getAnalysisGroupMap().get(idAnalysisGroupString);
							analysisGroups.add(ag);
						}
					}

					a.setAnalysisGroups(analysisGroups);
					sess.save(a);
				}

				sess.flush();
				if (invalidPermissions.size() != 0) {
					String badAnalysisString = "";
					for (Iterator i = invalidPermissions.iterator(); i.hasNext();) {
						badAnalysisString += i.next();
						if (i.hasNext()) {
							badAnalysisString += ", ";
						}
					}

					this.xmlResult = "<SUCCESS invalidPermission=\"The following analyses could not be moved due to invalid permissions " + badAnalysisString
							+ "\"/>";
				}
				setResponsePage(this.SUCCESS_JSP);
			}
		} catch (Exception e) {
			LOG.error("An exception has occurred in MoveAnalysis ", e);

			throw new RollBackCommandException(e.getMessage());

		} finally {
			try {
				//closeHibernateSession;
			} catch (Exception e) {
				LOG.error("An exception has occurred in MoveAnalysis ", e);
			}
		}
		return this;
	}

	private class AnalysisGroupComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			AnalysisGroup ag1 = (AnalysisGroup) o1;
			AnalysisGroup ag2 = (AnalysisGroup) o2;

			return ag1.getIdAnalysisGroup().compareTo(ag2.getIdAnalysisGroup());

		}
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
