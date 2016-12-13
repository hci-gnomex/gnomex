package hci.gnomex.utility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

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
		
		return new HashSet<BillingItem>((List<BillingItem>) query.list());
	}
	
	private void addRequestCriteria() {
		if (idLab != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" bi.idLab = ? ");
			this.addCriteria(idLab, IntegerType.INSTANCE);
		}
		
		if (idCoreFacility != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.idCoreFacility = ? ");
			this.addCriteria(idCoreFacility, IntegerType.INSTANCE);
		}
		
		if (idBillingAccount != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.idBillingAccount = ? ");
			this.addCriteria(idBillingAccount, IntegerType.INSTANCE);
		}
		
		if (requestNumber != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" (req.number LIKE ? OR req.number = ? OR req.number LIKE ? OR req.number = ?) ");
			String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
			this.addCriteria(requestNumberBase + "[0-9]", StringType.INSTANCE);
			this.addCriteria(requestNumberBase, StringType.INSTANCE);
			this.addCriteria(requestNumberBase + "R[0-9]", StringType.INSTANCE);
			this.addCriteria(requestNumberBase + "R", StringType.INSTANCE);
		}
		
		if (invoiceLookupNumber != null) {
			this.addWhereOrAnd();
			queryBuffer.append(" (inv.invoiceNumber LIKE ?) ");
			this.addCriteria("%" + invoiceLookupNumber + "%", StringType.INSTANCE);
		}
		
		if (excludeNewRequests != null && excludeNewRequests == true) {
			this.addWhereOrAnd();
			queryBuffer.append(" req.codeRequestStatus != ? ");
			this.addCriteria("NEW", StringType.INSTANCE);
		}
	}

}
