package hci.gnomex.utility;

import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
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
		this.addCriteria(targetClassIdentifier, Hibernate.INTEGER);
		this.addWhereOrAnd();
		this.queryBuffer.append(" bt.targetClassName = ? ");
		this.addCriteria(targetClassName, Hibernate.STRING);
		
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
		typeList.add(Hibernate.INTEGER);
		queryBuffer.append(" AND ");
		queryBuffer.append(" bt.targetClassName = ? ");
		valueList.add(targetClassName);
		typeList.add(Hibernate.STRING);
		
		Query query = sess.createQuery(queryBuffer.toString());
		Type[] typeArray = typeList.toArray(new Type[typeList.size()]);
		query.setParameters(valueList.toArray(), typeArray);
		
		if (query.list() != null && query.list().size() == 1 && query.list().get(0) instanceof BillingTemplate) {
			return (BillingTemplate) query.list().get(0);
		} else {
			return null;
		}
	}

}
