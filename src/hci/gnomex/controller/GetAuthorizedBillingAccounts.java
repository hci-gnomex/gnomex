package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.FieldFormatter;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.GNomExRollbackException;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

@SuppressWarnings("serial")
public class GetAuthorizedBillingAccounts extends GNomExCommand implements Serializable {
	
	// The static field for logging in Log4J
	private static org.apache.log4j.Logger 	log = org.apache.log4j.Logger.getLogger(GetAuthorizedBillingAccounts.class);
	
	private Integer 						idAppUser;
	private Integer							idCoreFacility;
	
	private Boolean							includeOnlyApprovedAccounts = true;
	private Boolean							includeOnlyActiveLabs = true;
	private Boolean							includeOnlyUnexpiredAccounts = true;
	private Boolean							includeOnlyStartedAccounts = true;
	
	@Override
	public void validate() {
	}
	
	@Override
	public void loadCommand(HttpServletRequest request, HttpSession session) {
		if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").trim().equals("")) {
			try {
				idAppUser = new Integer(request.getParameter("idAppUser").trim());
			} catch (NumberFormatException e) {
				this.addInvalidField("idAppUser", "idAppUser must be parsable as an Integer.");
			}
		}
		
		if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").trim().equals("")) {
			try {
				idCoreFacility = new Integer(request.getParameter("idCoreFacility").trim());
			} catch (NumberFormatException e) {
				this.addInvalidField("idCoreFacility", "idCoreFacility must be parsable as an Integer.");
			}
		}
		
		if (request.getParameter("includeOnlyApprovedAccounts") != null && !request.getParameter("includeOnlyApprovedAccounts").trim().equals("")) {
			includeOnlyApprovedAccounts = new Boolean(request.getParameter("includeOnlyApprovedAccounts").trim());
		}
		if (request.getParameter("includeOnlyActiveLabs") != null && !request.getParameter("includeOnlyActiveLabs").trim().equals("")) {
			includeOnlyActiveLabs = new Boolean(request.getParameter("includeOnlyActiveLabs").trim());
		}
		if (request.getParameter("includeOnlyUnexpiredAccounts") != null && !request.getParameter("includeOnlyUnexpiredAccounts").trim().equals("")) {
			includeOnlyUnexpiredAccounts = new Boolean(request.getParameter("includeOnlyUnexpiredAccounts").trim());
		}
		if (request.getParameter("includeOnlyStartedAccounts") != null && !request.getParameter("includeOnlyStartedAccounts").trim().equals("")) {
			includeOnlyStartedAccounts = new Boolean(request.getParameter("includeOnlyStartedAccounts").trim());
		}
		
		if (this.isValid()) {
			setResponsePage(this.SUCCESS_JSP);
		} else {
			setResponsePage(this.ERROR_JSP);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Command execute() throws RollBackCommandException {
		try {
			
			Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
			
			if (idAppUser == null) {
				idAppUser = this.getSecAdvisor().getAppUser().getIdAppUser();
			}
			
			ArrayList<BillingAccount> allAuthorizedBillingAccounts = new ArrayList<BillingAccount>();
			
			// Super admins
			if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
				
				// Add all billing accounts
				List<Integer> billingAccounts = (List<Integer>) sess.createQuery(generateQueryForAllBillingAccounts(null).toString()).list();
				for (Iterator<Integer> iter = billingAccounts.iterator(); iter.hasNext();) {
					allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
				}
				
			}
			// Admins
			else if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
				
				Set<Integer> myCoreFacilities = new HashSet<Integer>();
				for (Iterator iter = this.getSecAdvisor().getCoreFacilitiesIManage().iterator(); iter.hasNext();) {
					CoreFacility coreFacility = (CoreFacility) iter.next();
					myCoreFacilities.add(coreFacility.getIdCoreFacility());
				}
				
				// Add all billing accounts from the appropriate cores
				List<Integer> billingAccounts = (List<Integer>) sess.createQuery(generateQueryForAllBillingAccounts(myCoreFacilities).toString()).list();
				for (Iterator<Integer> iter = billingAccounts.iterator(); iter.hasNext();) {
					allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
				}
				
			}
			// Non-admins
			else {
				
				// Add all billing accounts with no specified "users" for all labs the user is a member of
				List<Integer> billingAccountsForUsersLabs = (List<Integer>) sess.createQuery(generateQueryForLabBillingAccountsWithNoUsers().toString()).list();
				for (Iterator<Integer> iter = billingAccountsForUsersLabs.iterator(); iter.hasNext();) {
					allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
				}
				
				// Add all billing accounts for which the user is listed as a "user" on
				List<Integer> billingAccountsUserIsAuthorizedFor = (List<Integer>) sess.createQuery(generateQueryForBillingAccountsWithUsers().toString()).list();
				for (Iterator<Integer> iter = billingAccountsUserIsAuthorizedFor.iterator(); iter.hasNext();) {
					allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
				}
				
			}
			
			HashMap<Integer, ArrayList<BillingAccount>> billingAccountsByLab = organizeAccountsByLab(allAuthorizedBillingAccounts);
			
			Document doc = generateXMLDocument(billingAccountsByLab, sess);
			
			XMLOutputter output = new org.jdom.output.XMLOutputter();
			this.xmlResult = output.outputString(doc);
			
			setResponsePage(this.SUCCESS_JSP);
			
		} catch (Exception e) {
			
			log.error("An exception has occurred in GetAuthorizedBillingAccounts ", e);
			e.printStackTrace();
			throw new GNomExRollbackException(e.getMessage(), false, "An error occurred retrieving your authorized billing accounts");
			
		} finally {
			
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();
			} catch (Exception e) {
				log.error("An exception has occurred in GetAuthorizedBillingAccounts while closing the session ", e);
			}
			
		}
		
		return this;
	}
	
	private Document generateXMLDocument(HashMap<Integer, ArrayList<BillingAccount>> billingAccountsByLab, Session sess) throws XMLReflectException {
		Element root = new Element("AuthorizedBillingAccounts");
		root.setAttribute("idAppUser", idAppUser.toString());
		root.setAttribute("hasAuthorizedAccounts", billingAccountsByLab.isEmpty() ? "N" : "Y");
		
		Document doc = new Document(root);
		
		for (Integer idLab : billingAccountsByLab.keySet()) {
			Lab lab = (Lab) sess.load(Lab.class, idLab);
			ArrayList<BillingAccount> accounts = billingAccountsByLab.get(idLab);
			
			Element labNode = new Element("Lab");
			labNode.setAttribute("name", lab.getFormattedLabName(false));
			labNode.setAttribute("idLab", lab.getIdLab().toString());
			labNode.setAttribute("isActive", lab.getIsActive());
			
			for (BillingAccount acct : accounts) {
				Element accountNode = acct.toXMLDocument(null, GNomExCommand.DATE_OUTPUT_SQL).getRootElement();
				
				labNode.addContent(accountNode);
			}
			
			doc.getRootElement().addContent(labNode);
		}
		
		return doc;
	}
	
	private HashMap<Integer, ArrayList<BillingAccount>> organizeAccountsByLab(List<BillingAccount> allAccounts) {
		HashMap<Integer, ArrayList<BillingAccount>> labToAccountsMap = new HashMap<Integer, ArrayList<BillingAccount>>();
		
		for (BillingAccount account : allAccounts) {
			Integer idLab = account.getIdLab();
			
			if (idLab == null) {
				continue;
			}
			
			if (labToAccountsMap.containsKey(idLab)) {
				labToAccountsMap.get(idLab).add(account);
			} else {
				ArrayList<BillingAccount> accountsForThisLab = new ArrayList<BillingAccount>();
				accountsForThisLab.add(account);
				labToAccountsMap.put(idLab, accountsForThisLab);
			}
		}
		
		return labToAccountsMap;
	}
	
	/**
	 * Returns the query for selecting all billing accounts with no specified users for all
	 * labs the selected user is a member of.
	 */
	private StringBuffer generateQueryForLabBillingAccountsWithNoUsers() {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.lab AS l ");
		queryBuff.append(" JOIN l.members AS m ");
		
		// Criteria
		queryBuff.append(" WHERE m.idAppUser = " + idAppUser.toString() + " ");
		queryBuff.append(queryForCommonBillingAccountCriteria(false, false));
		if (includeOnlyActiveLabs) {
			queryBuff.append(" AND l.isActive = \'Y\' ");
		}
		queryBuff.append(" AND ba.idBillingAccount NOT IN (" + generateSubQueryForAllIdBillingAccountsWithUsers().toString() + ") ");
		
		return queryBuff;
	}
	
	/**
	 * Returns the query for selecting all billing accounts the selected user is authorized
	 * as a "user" on.
	 */
	private StringBuffer generateQueryForBillingAccountsWithUsers() {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.users AS u ");
		
		// Criteria
		queryBuff.append(" WHERE u.idAppUser = " + idAppUser.toString() + " ");
		queryBuff.append(queryForCommonBillingAccountCriteria(false, false));
		
		return queryBuff;
	}
	
	private StringBuffer queryForRequiredBillingAccountColumns() {
		return new StringBuffer(" SELECT ba.idBillingAccount ");
	}
	
	private BillingAccount parseBillingAccountQueryRow(Integer idBillingAccount, Session sess) {
		return (BillingAccount) sess.load(BillingAccount.class, idBillingAccount);
	}
	
	private StringBuffer queryForCommonBillingAccountCriteria(boolean ignoreIdCoreFacility, boolean addWhere) {
		StringBuffer queryBuff = new StringBuffer();
		boolean useWhere = addWhere;
		
		if (!ignoreIdCoreFacility && idCoreFacility != null) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" ba.idCoreFacility = " + idCoreFacility.toString() + " ");
		}
		
		if (includeOnlyApprovedAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" ba.isApproved = \'Y\' ");
		}
		
		String today = this.formatDate(new Date(System.currentTimeMillis()), FieldFormatter.DATE_OUTPUT_SQL);
		
		if (includeOnlyStartedAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" (ba.startDate IS NULL OR ba.startDate <= \'" + today + "\') ");
		}
		
		if (includeOnlyUnexpiredAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" (ba. expirationDate IS NULL OR ba.expirationDate > \'" + today + "\') ");
		}
		
		return queryBuff;
	}
	
	private boolean addWhereOrAnd(StringBuffer queryBuff, boolean addWhere) {
		if (addWhere) {
			queryBuff.append(" WHERE ");
		} else {
			queryBuff.append(" AND ");
		}
		return false;
	}
	
	/**
	 * Returns the query for selecting the id's of all billing accounts that have at least one authorized
	 * "user" associated with them.
	 */
	private StringBuffer generateSubQueryForAllIdBillingAccountsWithUsers() {
		return new StringBuffer(" SELECT DISTINCT ba.idBillingAccount FROM BillingAccount AS ba JOIN ba.users AS u ");
	}
	
	private StringBuffer generateQueryForAllBillingAccounts(Set<Integer> idCoreFacilities) {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		
		// Criteria
		boolean addWhere = true;
		if (idCoreFacilities != null && idCoreFacilities.size() > 0) {
			queryBuff.append(" WHERE ba.idCoreFacility IN (");
			boolean firstParameter = true;
			for (Integer idCoreFacility : idCoreFacilities) {
				if (firstParameter) {
					queryBuff.append(idCoreFacility.toString());
					firstParameter = false;
				} else {
					queryBuff.append(", " + idCoreFacility.toString());
				}
			}
			queryBuff.append(") ");
			addWhere = false;
		}
		
		queryBuff.append(queryForCommonBillingAccountCriteria(true, addWhere));
		
		return queryBuff;
	}

}
