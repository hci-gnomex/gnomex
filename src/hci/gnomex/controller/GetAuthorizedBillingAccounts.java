package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.GNomExRollbackException;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	
	private Integer 			    idAppUser;
	private Integer					idCoreFacility;
	
	private Boolean					includeOnlyApprovedAccounts = true;
	private Boolean					includeOnlyActiveLabs = true;
	private Boolean					includeOnlyUnexpiredAccounts = true;
	private Boolean					includeOnlyStartedAccounts = true;
	
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

	@Override
	public Command execute() throws RollBackCommandException {
		try {
			
			Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
			
			if (idAppUser == null) {
				idAppUser = this.getSecAdvisor().getAppUser().getIdAppUser();
			}
			
			Set<BillingAccount> allAuthorizedBillingAccounts = retrieveAuthorizedBillingAccounts(sess, this.getSecAdvisor(), idAppUser, null, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyActiveLabs, includeOnlyUnexpiredAccounts, includeOnlyStartedAccounts);
			
			Map<Integer, Set<BillingAccount>> billingAccountsByLab = organizeAccountsByLab(allAuthorizedBillingAccounts);
			
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public static Set<BillingAccount> retrieveAuthorizedBillingAccounts(Session sess, SecurityAdvisor secAdvisor, Integer idAppUser, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyActiveLabs, boolean includeOnlyUnexpiredAccounts, boolean includeOnlyStartedAccounts) {
	    Set<BillingAccount> allAuthorizedBillingAccounts = new HashSet<BillingAccount>();
	    
	    // Admin / Super Admin
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) || secAdvisor.hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
            
            Set<Integer> myCoreFacilities = new HashSet<Integer>();
            
            if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
                List<Integer> allIdCoreFacilities = (List<Integer>) sess.createQuery(" SELECT DISTINCT cf.idCoreFacility FROM CoreFacility as cf ").list();
                myCoreFacilities.addAll(allIdCoreFacilities);
            } else {
                for (Iterator iter = secAdvisor.getCoreFacilitiesIManage().iterator(); iter.hasNext();) {
                    CoreFacility coreFacility = (CoreFacility) iter.next();
                    myCoreFacilities.add(coreFacility.getIdCoreFacility());
                }
            }
            
            // Add all billing accounts from the appropriate cores
            List<Integer> billingAccounts = (List<Integer>) sess.createQuery(generateQueryForAllBillingAccounts(myCoreFacilities, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts).toString()).list();
            for (Iterator<Integer> iter = billingAccounts.iterator(); iter.hasNext();) {
                allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
            }
        }
        
        // Add all billing accounts with no specified "users" for all labs the user is a member of
        List<Integer> billingAccountsForUsersLabs = (List<Integer>) sess.createQuery(generateQueryForLabBillingAccountsWithNoUsers(idAppUser, includeOnlyActiveLabs, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts).toString()).list();
        for (Iterator<Integer> iter = billingAccountsForUsersLabs.iterator(); iter.hasNext();) {
            allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
        }
        
        // Add all billing accounts for labs the user is a manager of
        List<Integer> billingAccountsForManagedLabs = (List<Integer>) sess.createQuery(generateQueryForManagedLabsBillingAccounts(idAppUser, includeOnlyActiveLabs, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts).toString()).list();
        for (Iterator<Integer> iter = billingAccountsForManagedLabs.iterator(); iter.hasNext();) {
            allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
        }
        
        // Add all billing accounts for which the user is listed as a "user" on
        List<Integer> billingAccountsUserIsAuthorizedFor = (List<Integer>) sess.createQuery(generateQueryForBillingAccountsWithUsers(idAppUser, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts).toString()).list();
        for (Iterator<Integer> iter = billingAccountsUserIsAuthorizedFor.iterator(); iter.hasNext();) {
            allAuthorizedBillingAccounts.add(parseBillingAccountQueryRow(iter.next(), sess));
        }
        
        return allAuthorizedBillingAccounts;
	}
	
	private Document generateXMLDocument(Map<Integer, Set<BillingAccount>> billingAccountsByLab, Session sess) throws XMLReflectException {
		Element root = new Element("AuthorizedBillingAccounts");
		root.setAttribute("idAppUser", idAppUser.toString());
		root.setAttribute("hasAuthorizedAccounts", billingAccountsByLab.isEmpty() ? "N" : "Y");
		
		Document doc = new Document(root);
		
		boolean hasAccountWithinCore = false;
		boolean hasAccountsWithinCore = false;
		
		for (Integer idLab : billingAccountsByLab.keySet()) {
			Lab lab = (Lab) sess.load(Lab.class, idLab);
			Set<BillingAccount> accounts = billingAccountsByLab.get(idLab);
			
			Element labNode = new Element("Lab");
			labNode.setAttribute("name", lab.getFormattedLabName(false));
			labNode.setAttribute("idLab", lab.getIdLab().toString());
			labNode.setAttribute("isActive", lab.getIsActive());
			
			for (BillingAccount acct : accounts) {
				Element accountNode = acct.toXMLDocument(null, GNomExCommand.DATE_OUTPUT_SQL).getRootElement();
				
				if (idCoreFacility != null && acct.getIdCoreFacility().equals(idCoreFacility)) {
				    if (hasAccountWithinCore) {
				        hasAccountsWithinCore = true;
				    } else {
				        hasAccountWithinCore = true;
				    }
				}
				
				labNode.addContent(accountNode);
			}
			
			doc.getRootElement().setAttribute("hasAccountsWithinCore", hasAccountsWithinCore ? "Y" : "N");
			
			doc.getRootElement().addContent(labNode);
		}
		
		return doc;
	}
	
	private Map<Integer, Set<BillingAccount>> organizeAccountsByLab(Set<BillingAccount> allAccounts) {
		HashMap<Integer, Set<BillingAccount>> labToAccountsMap = new HashMap<Integer, Set<BillingAccount>>();
		
		for (BillingAccount account : allAccounts) {
			Integer idLab = account.getIdLab();
			
			if (idLab == null) {
				continue;
			}
			
			if (labToAccountsMap.containsKey(idLab)) {
				labToAccountsMap.get(idLab).add(account);
			} else {
				Set<BillingAccount> accountsForThisLab = new HashSet<BillingAccount>();
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
	private static StringBuffer generateQueryForLabBillingAccountsWithNoUsers(Integer idAppUser, boolean includeOnlyActiveLabs, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyStartedAccounts, boolean includeOnlyUnexpiredAccounts) {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.lab AS l ");
		queryBuff.append(" JOIN l.members AS m ");
		queryBuff.append(" JOIN l.coreFacilities AS cf ");
		
		// Criteria
		queryBuff.append(" WHERE m.idAppUser = " + idAppUser.toString() + " ");
		queryBuff.append(queryForCommonBillingAccountCriteria(false, false, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts));
		if (includeOnlyActiveLabs) {
			queryBuff.append(" AND l.isActive = \'Y\' ");
		}
		queryBuff.append(" AND ba.idBillingAccount NOT IN (" + generateSubQueryForAllIdBillingAccountsWithUsers().toString() + ") ");
		
		return queryBuff;
	}
	
	/**
	 * Returns the query for selecting all billing accounts for all
	 * labs the selected user is a manager of.
	 */
	private static StringBuffer generateQueryForManagedLabsBillingAccounts(Integer idAppUser, boolean includeOnlyActiveLabs, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyStartedAccounts, boolean includeOnlyUnexpiredAccounts) {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.lab AS l ");
		queryBuff.append(" JOIN l.managers AS m ");
		queryBuff.append(" JOIN l.coreFacilities AS cf ");
		
		// Criteria
		queryBuff.append(" WHERE m.idAppUser = " + idAppUser.toString() + " ");
		queryBuff.append(queryForCommonBillingAccountCriteria(false, false, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts));
		if (includeOnlyActiveLabs) {
			queryBuff.append(" AND l.isActive = \'Y\' ");
		}
		
		return queryBuff;
	}
	
	/**
	 * Returns the query for selecting all billing accounts the selected user is authorized
	 * as a "user" on.
	 */
	private static StringBuffer generateQueryForBillingAccountsWithUsers(Integer idAppUser, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyStartedAccounts, boolean includeOnlyUnexpiredAccounts) {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.lab AS l ");
		queryBuff.append(" JOIN ba.users AS u ");
		queryBuff.append(" JOIN l.coreFacilities AS cf ");
		
		// Criteria
		queryBuff.append(" WHERE u.idAppUser = " + idAppUser.toString() + " ");
		queryBuff.append(queryForCommonBillingAccountCriteria(false, false, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts));
		
		return queryBuff;
	}
	
	private static StringBuffer queryForRequiredBillingAccountColumns() {
		return new StringBuffer(" SELECT DISTINCT ba.idBillingAccount ");
	}
	
	private static BillingAccount parseBillingAccountQueryRow(Integer idBillingAccount, Session sess) {
		return (BillingAccount) sess.load(BillingAccount.class, idBillingAccount);
	}
	
	private static StringBuffer queryForCommonBillingAccountCriteria(boolean ignoreIdCoreFacility, boolean addWhere, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyStartedAccounts, boolean includeOnlyUnexpiredAccounts) {
		StringBuffer queryBuff = new StringBuffer();
		boolean useWhere = addWhere;
		
		if (!ignoreIdCoreFacility && idCoreFacility != null) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" ba.idCoreFacility = " + idCoreFacility.toString() + " AND ba.idCoreFacility = cf.idCoreFacility ");
		}
		
		if (idLab != null) {
            useWhere = addWhereOrAnd(queryBuff, useWhere);
            queryBuff.append(" ba.idLab = " + idLab.toString() + " ");
        }
		
		if (includeOnlyApprovedAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" ba.isApproved = \'Y\' ");
		}
		
		String today = new Date(System.currentTimeMillis()).toString();
		
		if (includeOnlyStartedAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" (ba.startDate IS NULL OR ba.startDate <= \'" + today + "\') ");
		}
		
		if (includeOnlyUnexpiredAccounts) {
			useWhere = addWhereOrAnd(queryBuff, useWhere);
			queryBuff.append(" (ba.expirationDate IS NULL OR ba.expirationDate > \'" + today + "\') ");
		} else {
		    long yearInMilliseconds = (long) 1000 * (long) 60 * (long) 60 * (long) 24 * (long) 365;
		    String oneYearAgo = new Date(System.currentTimeMillis() - yearInMilliseconds).toString();
		    useWhere = addWhereOrAnd(queryBuff, useWhere);
            queryBuff.append(" (ba.expirationDate IS NULL OR ba.expirationDate > \'" + oneYearAgo + "\') ");
		}
		
		return queryBuff;
	}
	
	private static boolean addWhereOrAnd(StringBuffer queryBuff, boolean addWhere) {
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
	private static StringBuffer generateSubQueryForAllIdBillingAccountsWithUsers() {
		return new StringBuffer(" SELECT DISTINCT ba.idBillingAccount FROM BillingAccount AS ba JOIN ba.users AS u ");
	}
	
	private static StringBuffer generateQueryForAllBillingAccounts(Set<Integer> idCoreFacilities, Integer idLab, Integer idCoreFacility, boolean includeOnlyApprovedAccounts, boolean includeOnlyStartedAccounts, boolean includeOnlyUnexpiredAccounts) {
		StringBuffer queryBuff = new StringBuffer();
		
		// Desired columns
		queryBuff.append(queryForRequiredBillingAccountColumns());
		
		// Body
		queryBuff.append(" FROM BillingAccount AS ba ");
		queryBuff.append(" JOIN ba.lab AS l ");
		queryBuff.append(" JOIN l.coreFacilities AS cf ");
		
		// Criteria
		StringBuffer coreFacilitiesBuff = new StringBuffer("(");
        boolean firstParameter = true;
        for (Integer idCore : idCoreFacilities) {
            if (firstParameter) {
                coreFacilitiesBuff.append(idCore.toString());
                firstParameter = false;
            } else {
                coreFacilitiesBuff.append(", " + idCore.toString());
            }
        }
        coreFacilitiesBuff.append(")");
        
        queryBuff.append(" WHERE ba.idCoreFacility IN " + coreFacilitiesBuff.toString() + " AND ba.idCoreFacility = cf.idCoreFacility ");
		
		queryBuff.append(queryForCommonBillingAccountCriteria(true, false, idLab, idCoreFacility, includeOnlyApprovedAccounts, includeOnlyStartedAccounts, includeOnlyUnexpiredAccounts));
		
		return queryBuff;
	}

}
