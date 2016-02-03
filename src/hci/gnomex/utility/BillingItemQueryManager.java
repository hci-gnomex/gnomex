package hci.gnomex.utility;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

public class BillingItemQueryManager extends QueryManager {
	
	private String 		requestNumber;
	private String		invoiceLookupNumber;
	private Integer 	idLab;
	private Integer		idBillingPeriod;
	private Integer		idBillingAccount;
	private Integer		idCoreFacility;
	
	private Boolean 	excludeNewRequests;
	private Boolean 	showOtherBillingItems;

	public BillingItemQueryManager(SecurityAdvisor secAdvisor) {
		super(secAdvisor);
	}

	@Override
	public boolean hasSufficientCriteria() {
		return QueryManager.atLeastOneNonNullCriteria(requestNumber, invoiceLookupNumber, idLab, idBillingPeriod, idBillingAccount, idCoreFacility);
	}
	
	@SuppressWarnings("unchecked")
	public static Set<BillingItem> getBillingItemsForBillingTemplate(Session sess, Integer idBillingTemplate) {
		StringBuffer queryBuffer = new StringBuffer();
		boolean addWhere = true;
		
		queryBuffer.append(" SELECT b FROM BillingItem AS b ");
		queryBuffer.append(" JOIN b.masterBillingItem AS m ");
		queryBuffer.append(" JOIN m.billingTemplate AS bt ");
		addWhere = QueryManager.addWhereOrAnd(addWhere, queryBuffer);
		queryBuffer.append(" bt.idBillingTemplate = :idBillingTemplate ");
		
		Query query = sess.createQuery(queryBuffer.toString());
		query.setParameter("idBillingTemplate", idBillingTemplate);
		
		return new TreeSet<BillingItem>((List<BillingItem>) query.list());
	}
	
	private void addRequestCriteria() {
		if (idLab != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" bi.idLab = ? ");
			this.addCriteria(idLab, Hibernate.INTEGER);
		}
		
		if (idCoreFacility != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.idCoreFacility = ? ");
			this.addCriteria(idCoreFacility, Hibernate.INTEGER);
		}
		
		if (idBillingAccount != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.idBillingAccount = ? ");
			this.addCriteria(idBillingAccount, Hibernate.INTEGER);
		}
		
		if (requestNumber != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" (req.number LIKE ? OR req.number = ? OR req.number LIKE ? OR req.number = ?) ");
			String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
			this.addCriteria(requestNumberBase + "[0-9]", Hibernate.STRING);
			this.addCriteria(requestNumberBase, Hibernate.STRING);
			this.addCriteria(requestNumberBase + "R[0-9]", Hibernate.STRING);
			this.addCriteria(requestNumberBase + "R", Hibernate.STRING);
		}
		
		if (invoiceLookupNumber != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" (inv.invoiceNumber LIKE ?) ");
			this.addCriteria("%" + invoiceLookupNumber + "%", Hibernate.STRING);
		}
		
		if (excludeNewRequests != null && excludeNewRequests == true) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.codeRequestStatus != ? ");
			this.addCriteria("NEW", Hibernate.STRING);
		}
	}

}
