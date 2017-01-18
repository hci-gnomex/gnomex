package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetRequestList extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(GetRequestList.class);

private RequestFilter requestFilter;
private HashMap<Integer, List<Object[]>> reactionPlateMap = new HashMap<Integer, List<Object[]>>();
private HashMap<Integer, List<Object[]>> sourcePlateMap = new HashMap<Integer, List<Object[]>>();

private Boolean includePlateInfo = true;
private Boolean linkToAnalysisExpsOnly = false;
private Boolean ignoreMaxRequestLimit = false;

private String message = "";
private static final int DEFAULT_MAX_REQUESTS_COUNT = 100;

public void validate() {
}

public void loadCommand(HttpServletRequest request, HttpSession session) {

	requestFilter = new RequestFilter();
	HashMap errors = this.loadDetailObject(request, requestFilter);
	this.addInvalidFields(errors);

	if (request.getParameter("includePlateInfo") != null && !request.getParameter("includePlateInfo").equals("")) {
		includePlateInfo = request.getParameter("includePlateInfo").equals("Y") ? true : false;
	}

	if (request.getParameter("linkToAnalysisExpsOnly") != null
			&& !request.getParameter("linkToAnalysisExpsOnly").equals("")) {
		linkToAnalysisExpsOnly = request.getParameter("linkToAnalysisExpsOnly").equals("Y") ? true : false;
	}
	if (request.getParameter("ignoreMaxRequestLimit") != null
			&& !request.getParameter("ignoreMaxRequestLimit").equals("")) {
		ignoreMaxRequestLimit = request.getParameter("ignoreMaxRequestLimit").equals("Y") ? true : false;
	}
}

public Command execute() throws RollBackCommandException {

	try {
		Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername(), "GetRequest");
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

		StringBuffer buf = requestFilter.getQuery(this.getSecAdvisor());

		LOG.info("Query for GetRequestList: " + buf.toString());
		List rows = sess.createQuery(buf.toString()).list();

		if (includePlateInfo) {
			// Hash reaction plate info by idRequest
			hashReactionPlates(sess);

			// Hash source plate info by idRequest
			hashSourcePlates(sess);
		}

		Map<Integer, Integer> requestsToSkip = this.getSecAdvisor().getBSTXSecurityIdsToExclude(sess, dh, rows, 0, 6);

		Integer maxRequests = getMaxRequests(sess);
		int requestCount = 0;

		Document doc = new Document(new Element("RequestList"));
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();

			Integer idRequest = (Integer) row[0];
			if (requestsToSkip.get(idRequest) != null) {
				// BST Security failed.
				continue;
			}
			// If we only want exps that can be linked to analysis then skip the ones who don't have it.
			String codeRequestCategory = (String) row[6];
			RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
			if (linkToAnalysisExpsOnly
					&& (requestCategory.getAssociatedWithAnalysis() == null || requestCategory
							.getAssociatedWithAnalysis().equals("N"))) {
				continue;
			}

			String number = (String) row[1];
			String name = (String) row[2];
			String description = (String) row[3];
			Integer idSampleDropOffLocation = (Integer) row[4];
			String codeRequestStatus = (String) row[5];
			// String codeRequestCategory = (String) row[6];
			String createDate = row[7] != null ? this.formatDateTime((java.util.Date) row[7], this.DATE_OUTPUT_DASH)
					: "";
			String submitterFirstName = (String) row[8];
			String submitterLastName = (String) row[9];
			String labFirstName = (String) row[10];
			String labLastName = (String) row[11];
			Integer idAppUser = (Integer) row[12];
			Integer idLab = (Integer) row[13];
			Integer idCoreFacility = (Integer) row[14];
			String corePrepInstructions = (String) row[15];
			Long numberOfSamples = (Long) row[16];
			String adminNotes = (String) row[17];
			Long numberOfWorkItems = (Long) row[18];
			// Integer numberOfSeqLanes = (Integer) row[19];
			// Integer numberOfHybs = (Integer) row[20];

			String requestStatus = dh.getRequestStatus(codeRequestStatus);
			String labName = Lab.formatLabNameFirstLast(labFirstName, labLastName);
			String ownerName = AppUser.formatName(submitterLastName, submitterFirstName);
			// RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);

			String experimentName = toString(name);
			if (experimentName.length() == 0) {
				experimentName = AppUser.formatShortName(submitterLastName, submitterFirstName) + "-" + number;
			}

			Element node = new Element("Request");

			node.setAttribute("idRequest", toString(idRequest));
			node.setAttribute("name", toString(experimentName));
			node.setAttribute("number", toString(number));
			node.setAttribute("requestNumber", toString(number));
			node.setAttribute("description", toString(description));
			node.setAttribute("idSampleDropOffLocation", toString(idSampleDropOffLocation));
			node.setAttribute("codeRequestStatus", toString(codeRequestStatus));
			node.setAttribute("requestStatus", toString(requestStatus));
			node.setAttribute("codeRequestCategory", toString(codeRequestCategory));
			node.setAttribute("createDate", createDate);
			node.setAttribute("ownerName", toString(ownerName));
			node.setAttribute("labName", toString(labName));
			node.setAttribute("corePrepInstructions", toString(corePrepInstructions));
			node.setAttribute("numberOfSamples", toString(numberOfSamples));
			// node.setAttribute("numberOfSeqLanes", toString(numberOfSeqLanes));
			// node.setAttribute("numberOfHybs", toString(numberOfHybs));
			node.setAttribute("isSelected", "N");
			node.setAttribute("adminNotes", toString(adminNotes));
			node.setAttribute("hasWorkItems", numberOfWorkItems > 0 ? "Y" : "N");

			node.setAttribute("icon",
					requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");

			if (includePlateInfo) {
				List<Object[]> rxnPlateRows = reactionPlateMap.get(idRequest);
				appendReactionPlateInfo(node, rxnPlateRows);

				List<Object[]> sourcePlateRows = sourcePlateMap.get(idRequest);
				appendSourcePlateInfo(node, sourcePlateRows);
			}

			appendSecurityFlags(node, codeRequestStatus, codeRequestCategory, idLab, idAppUser, idCoreFacility, pdh);

			doc.getRootElement().addContent(node);

			requestCount++;
			if (requestCount >= maxRequests) {
				break;
			}
		}

		doc.getRootElement().setAttribute("requestCount", Integer.valueOf(requestCount).toString());
		message = requestCount == maxRequests ? "First " + maxRequests + " displayed of " + rows.size() : "";
		doc.getRootElement().setAttribute("message", message);

		XMLOutputter out = new org.jdom.output.XMLOutputter();
		this.xmlResult = out.outputString(doc);

		setResponsePage(this.SUCCESS_JSP);
	} catch (NamingException e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetRequestList ", e);

		throw new RollBackCommandException(e.getMessage());
	} catch (SQLException e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetRequestList ", e);

		throw new RollBackCommandException(e.getMessage());
	} catch (XMLReflectException e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetRequestList ", e);

		throw new RollBackCommandException(e.getMessage());
	} catch (Exception e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetRequestList ", e);

		throw new RollBackCommandException(e.getMessage());
	}

	return this;
}

private Integer getMaxRequests(Session sess) {
	Integer maxRequests = DEFAULT_MAX_REQUESTS_COUNT;

	if (!ignoreMaxRequestLimit) {
		String prop = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.EXPERIMENT_VIEW_LIMIT);
		if (prop != null && prop.length() > 0) {
			try {
				maxRequests = Integer.parseInt(prop);
			} catch (NumberFormatException e) {
			}
		}
	} else {
		maxRequests = Integer.MAX_VALUE;
	}
	return maxRequests;
}

private String toString(Object theValue) {
	if (theValue != null) {
		return theValue.toString();
	} else {
		return "";
	}
}

private void hashReactionPlates(Session sess) {
	reactionPlateMap = new HashMap<Integer, List<Object[]>>();

	StringBuffer queryBuf = requestFilter.getReactionPlateQuery(this.getSecAdvisor());
	List<Object[]> plateRows = sess.createQuery(queryBuf.toString()).list();

	if (plateRows != null) {

		for (Object[] plateRow : plateRows) {
			Integer idRequest = (Integer) plateRow[0];
			String plateLabel = (String) plateRow[1];
			Integer idInstrumentRun = (Integer) plateRow[2];
			String runLabel = (String) plateRow[3];

			List<Object[]> thePlateRows = reactionPlateMap.get(idRequest);
			if (thePlateRows == null) {
				thePlateRows = new ArrayList<Object[]>();
				reactionPlateMap.put(idRequest, thePlateRows);
			}
			thePlateRows.add(plateRow);
		}
	}
}

private void appendReactionPlateInfo(Element node, List<Object[]> plateRows) {

	String rxnPlateNames = "";
	String runIds = "";
	String runNames = "";
	if (plateRows != null) {
		for (Object[] plateRow : plateRows) {
			Integer idRequest = (Integer) plateRow[0];
			String plateLabel = (String) plateRow[1];
			Integer idInstrumentRun = (Integer) plateRow[2];
			String runLabel = (String) plateRow[3];

			if (rxnPlateNames.length() > 0 && toString(plateLabel).length() > 0) {
				rxnPlateNames += ", ";
			}
			if (runIds.length() > 0 && toString(idInstrumentRun).length() > 0) {
				runIds += ", ";
			}
			if (runNames.length() > 0 & toString(runLabel).length() > 0) {
				runNames += ", ";
			}
			rxnPlateNames += toString(plateLabel);
			runIds += toString(idInstrumentRun);
			runNames += toString(runLabel);
		}
	}

	node.setAttribute("plateLabel", rxnPlateNames);
	node.setAttribute("idInstrumentRun", runIds);
	node.setAttribute("runLabel", runNames);

}

private void hashSourcePlates(Session sess) {
	sourcePlateMap = new HashMap<Integer, List<Object[]>>();

	StringBuffer queryBuf = requestFilter.getSourcePlateQuery(this.getSecAdvisor());
	List<Object[]> plateRows = sess.createQuery(queryBuf.toString()).list();

	if (plateRows != null) {

		for (Object[] plateRow : plateRows) {
			Integer idRequest = (Integer) plateRow[0];
			Integer idPlate = (Integer) plateRow[1];

			List<Object[]> thePlateRows = sourcePlateMap.get(idRequest);
			if (thePlateRows == null) {
				thePlateRows = new ArrayList<Object[]>();
				sourcePlateMap.put(idRequest, thePlateRows);
			}
			thePlateRows.add(plateRow);
		}
	}
}

private void appendSourcePlateInfo(Element node, List<Object[]> plateRows) {

	node.setAttribute("container", plateRows != null && !plateRows.isEmpty() ? "Plate" : "Tubes");

}

private void appendSecurityFlags(Element node, String codeRequestStatus, String codeRequestCategory, Integer idLab,
		Integer idAppUser, Integer idCoreFacility, PropertyDictionaryHelper pdh) {
	boolean canUpdate = false;
	boolean isDNASeqExperiment = RequestCategory.isDNASeqCoreRequestCategory(codeRequestCategory);
	boolean saveThenSubmitExperiment = pdh.getCoreFacilityRequestCategoryProperty(idCoreFacility, codeRequestCategory,
			PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y") ? true : false;

	// Super Admins
	if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
		canUpdate = true;
	}
	// Admins - Can only update requests from core facility user manages
	else if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
		canUpdate = this.getSecAdvisor().isCoreFacilityIManage(idCoreFacility);
		if (isDNASeqExperiment
				&& !(codeRequestStatus.equals(RequestStatus.SUBMITTED) || codeRequestStatus.equals(RequestStatus.NEW) || (codeRequestStatus
						.equals(RequestStatus.PROCESSING) && node.getAttributeValue("plateLabel").equals("")))) {
			canUpdate = false;
		}
	}
	// University GNomEx users
	else if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {

		// Lab manager
		if (this.getSecAdvisor().isGroupIManage(idLab)) {
			canUpdate = true;
		}
		// Owner of request
		else if (this.getSecAdvisor().isGroupIAmMemberOf(idLab) && this.getSecAdvisor().isOwner(idAppUser)) {
			canUpdate = true;
		}
		if (canUpdate && saveThenSubmitExperiment && !codeRequestStatus.equals(RequestStatus.NEW)) {
			canUpdate = false;
		}
	}

	node.setAttribute("canUpdate", canUpdate ? "Y" : "N");

}

}