package hci.gnomex.utility;

import java.util.ArrayList;

import org.hibernate.FlushMode;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import hci.gnomex.model.BillingTemplate;
import hci.gnomex.security.SecurityAdvisor;

public class BillingTemplateQueryManager extends QueryManager {
	
	private Integer	targetClassIdentifier;
	private String	targetClassName;

	public BillingTemplateQueryManager(SecurityAdvisor secAdvisor) {
		super(secAdvisor);
	}
	
	public BillingTemplateQueryManager(SecurityAdvisor secAdvisor, Integer targetClassIdentifier, String targetClassName) {
		this(secAdvisor);
		this.targetClassIdentifier = targetClassIdentifier;
		this.targetClassName = targetClassName;
	}

	@Override
	public boolean hasSufficientCriteria() {
		return QueryManager.allNonNullCriteria(targetClassIdentifier, targetClassName);
	}
	
	public BillingTemplate retrieveBillingTemplate(Session sess) {
		this.prepareForNewQuery();
		
		this.queryBuffer.append(" SELECT bt FROM BillingTemplate AS bt ");
		
		this.addWhereOrAnd();
		this.queryBuffer.append(" bt.targetClassIdentifier = ? ");
		this.addCriteria(targetClassIdentifier, IntegerType.INSTANCE);
		this.addWhereOrAnd();
		this.queryBuffer.append(" bt.targetClassName = ? ");
		this.addCriteria(QueryManager.convertToFullTargetClassName(targetClassName), StringType.INSTANCE);
		
		this.query = sess.createQuery(queryBuffer.toString());
		this.applyCriteria();
		
		if (query.list() != null && query.list().size() == 1 && query.list().get(0) instanceof BillingTemplate) {
			return (BillingTemplate) query.list().get(0);
		} else {
			return null;
		}
	}
	
	public static BillingTemplate retrieveBillingTemplate(Session sess, Order order) {
		if (order == null) {
			return null;
		}
		
		return BillingTemplateQueryManager.retrieveBillingTemplate(sess, order.getTargetClassIdentifier(), order.getTargetClassName());
	}
	
	public static BillingTemplate retrieveBillingTemplate(Session sess, Integer targetClassIdentifier, String targetClassName) {
		StringBuffer queryBuffer = new StringBuffer();
		ArrayList<Object> valueList = new ArrayList<Object>();
		ArrayList<Type> typeList = new ArrayList<Type>();
		
		queryBuffer.append(" SELECT bt FROM BillingTemplate AS bt ");
		queryBuffer.append(" WHERE ");
		queryBuffer.append(" bt.targetClassIdentifier = ? ");
		valueList.add(targetClassIdentifier);
		typeList.add(IntegerType.INSTANCE);
		queryBuffer.append(" AND ");
		queryBuffer.append(" bt.targetClassName = ? ");
		valueList.add(targetClassName);
		typeList.add(StringType.INSTANCE);

		// Use manual flush mode to force the query to run without flushing first
		Query query = sess.createQuery(queryBuffer.toString()).setFlushMode(FlushMode.MANUAL);
		Type[] typeArray = typeList.toArray(new Type[typeList.size()]);
		query.setParameters(valueList.toArray(), typeArray);
		
		if (query.list() != null && query.list().size() == 1 && query.list().get(0) instanceof BillingTemplate) {
			return (BillingTemplate) query.list().get(0);
		} else {
			return null;
		}
	}

}
