package hci.gnomex.utility;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

public abstract class QueryManager {
	
	private static final HashMap<String, String> targetClassNameMap;
	static {
		targetClassNameMap = new HashMap<String, String>();
		
		targetClassNameMap.put(Request.class.getSimpleName(), Request.class.getName());
		targetClassNameMap.put(ProductOrder.class.getSimpleName(), ProductOrder.class.getName());
		targetClassNameMap.put("ProductOrder", ProductOrder.class.getName());
	}
	
	protected SecurityAdvisor 	secAdvisor;
	
	protected Query				query;
	protected StringBuffer 		queryBuffer;
	protected ArrayList<Object>	valueList;
	protected ArrayList<Type> 	typeList;
	protected boolean 			addWhere;
	
	public QueryManager(SecurityAdvisor secAdvisor) {
		this.secAdvisor = secAdvisor;
		
		this.prepareForNewQuery();
	}
	
	public HashMap<String, String> load(HttpServletRequest request) {
		HashMap<String, String> errors = new HashMap<String, String>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			String parameter = request.getParameter(fieldName);
			
			if (parameter != null && !parameter.trim().equals("")) {
				try {
					if (field.getType().isAssignableFrom(Integer.class)) {
						field.set(this, new Integer(Integer.parseInt(parameter)));
					} else if (field.getType().isAssignableFrom(Boolean.class)) {
						if (parameter.trim().equalsIgnoreCase("Y") || parameter.trim().equalsIgnoreCase("true")) {
							field.set(this, new Boolean(true));
						} else {
							field.set(this, new Boolean(false));
						}
					} else if (field.getType().isAssignableFrom(String.class)) {
						field.set(this, parameter);
					} else if (field.getType().isAssignableFrom(Date.class)) {
						field.set(this, DateFormat.getDateInstance().parse(parameter));
					}
				} catch (NumberFormatException e) {
					errors.put(fieldName, "Cannot parse parameter.");
				} catch (IllegalAccessException e) {
					errors.put(fieldName, "Field is inaccessible.");
				} catch (IllegalArgumentException e) {
					errors.put(fieldName, "Field is unavailable or not compatible with parameter.");
				} catch (ParseException e) {
					errors.put(fieldName, "Cannot parse parameter.");
				}
			}
		}
		
		return errors;
	}
	
	abstract public boolean hasSufficientCriteria();
	
	protected void prepareForNewQuery() {
		query = null;
		queryBuffer = new StringBuffer();
		addWhere = true;
		valueList = new ArrayList<Object>();
		typeList = new ArrayList<Type>();
	}
	
	protected void addWhereOrAnd() {
		if (addWhere) {
			queryBuffer.append(" WHERE ");
			addWhere = false;
		} else {
			queryBuffer.append(" AND ");
		}
	}
	
	public static boolean addWhereOrAnd(boolean addWhere, StringBuffer queryBuffer) {
		if (addWhere) {
			queryBuffer.append(" WHERE ");
		} else {
			queryBuffer.append(" AND ");
		}
		return false;
	}
	
	protected static boolean atLeastOneNonNullCriteria(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter != null) {
				return true;
			}
		}
		return false;
	}
	
	protected static boolean allNonNullCriteria(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) {
				return false;
			}
		}
		return true;
	}
	
	protected void addCriteria(Object value, Type type, int index) {
		this.valueList.add(index, value);
		this.typeList.add(index, type);
	}
	
	protected void addCriteria(Object value, Type type) {
		this.valueList.add(value);
		this.typeList.add(type);
	}
	
	protected void applyCriteria() {
		Type[] typeArray = typeList.toArray(new Type[typeList.size()]);
		query.setParameters(this.valueList.toArray(), typeArray);
	}
	
	public static Order retrieveTargetClass(Order order, Session sess) {
		if (order == null) {
			return null;
		}
		
		return QueryManager.retrieveTargetClass(order.getTargetClassIdentifier(), order.getTargetClassName(), sess);
	}
	
	public static Order retrieveTargetClass(Integer targetClassIdentifier, String targetClassName, Session sess) {
		if (QueryManager.isValidTargetClass(targetClassIdentifier, targetClassName, sess)) {
			return (Order) sess.load(targetClassName, targetClassIdentifier);
		} else {
			return null;
		}
	}
	
	public static boolean isValidTargetClass(Integer targetClassIdentifier, String targetClassName, Session sess) {
		if (targetClassIdentifier == null || targetClassName == null) {
			return false;
		}
		
		try {
			return sess.get(targetClassName, targetClassIdentifier) != null;
		} catch (HibernateException e) {
			return false;
		}
	}
	
	public static boolean isValidTargetClass(String targetClassIdentifier, String targetClassName, Session sess) {
		Integer parsedTargetClassIdentifier;
		try {
			parsedTargetClassIdentifier = Integer.parseInt(targetClassIdentifier);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return QueryManager.isValidTargetClass(parsedTargetClassIdentifier, targetClassName, sess);
	}
	
	public static String convertToFullTargetClassName(String simpleClassName) {
		if (simpleClassName == null) {
			return null;
		}
		
		if (targetClassNameMap.containsKey(simpleClassName)) {
			return targetClassNameMap.get(simpleClassName);
		} else {
			for (Map.Entry<String, String> entry : targetClassNameMap.entrySet()) {
				if (entry.getValue().equals(simpleClassName)) {
					return simpleClassName;
				}
			}
			
			return null;
		}
	}
	
	public static String convertToSimpleTargetClassName(String fullClassName) {
		if (fullClassName == null) {
			return null;
		}
		
		for (Map.Entry<String, String> entry : targetClassNameMap.entrySet()) {
			if (entry.getValue().equals(fullClassName)) {
				return entry.getKey();
			}
		}
		
		if (targetClassNameMap.containsKey(fullClassName)) {
			return fullClassName;
		}
		
		return null;
	}

}
