package hci.gnomex.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.jdom.Element;

import hci.gnomex.controller.SaveBillingTemplate;
import hci.gnomex.utility.BillingItemQueryManager;
import hci.gnomex.utility.DetailObject;
import hci.gnomex.utility.Order;
import hci.gnomex.utility.XMLTools;
import hci.hibernate3utils.HibernateDetailObject;

@SuppressWarnings("serial")
public class BillingTemplate extends HibernateDetailObject implements DetailObject {
	
	private Integer 					idBillingTemplate;
	private Integer						targetClassIdentifier;
	private String						targetClassName;
	private Set<BillingTemplateItem> 	items;
	private Set<MasterBillingItem>		masterBillingItems;
	
	public BillingTemplate() {
		super();
		
		this.setItems(new TreeSet<BillingTemplateItem>());
		this.setMasterBillingItems(new HashSet<MasterBillingItem>());
	}
	
	public BillingTemplate(Order order) {
		this();
		
		this.setOrder(order);
	}
	
	public void updateSingleBillingAccount(Integer idBillingAccount) {
		this.getItems().clear();
		BillingTemplateItem item = new BillingTemplateItem(this);
		item.setIdBillingAccount(idBillingAccount);
		item.setPercentSplit(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
		item.setSortOrder(1);
		this.getItems().add(item);
	}
	
	public Set<BillingItem> getBillingItems(Session sess) {
		return BillingItemQueryManager.getBillingItemsForBillingTemplate(sess, this.idBillingTemplate);
	}
	
	public Set<BillingItem> recreateBillingItems(Session sess) {
		Set<BillingItem> createdBillingItems = new HashSet<BillingItem>();
		// Apply new template to all master billing items
		for (MasterBillingItem masterBillingItem : this.getMasterBillingItems()) {
			createdBillingItems.addAll(SaveBillingTemplate.createBillingItemsForMaster(sess, masterBillingItem, this));
		}
		return createdBillingItems;
	}
	
	public void setOrder(Order order) {
		this.setTargetClassIdentifier(order.getTargetClassIdentifier());
		this.setTargetClassName(order.getTargetClassName());
	}
	
	public List<BillingAccount> getBillingAccounts(Session sess) {
		Set<BillingTemplateItem> items = this.getItems();
		ArrayList<BillingAccount> accounts = new ArrayList<BillingAccount>();
		
		if (items != null) {
			for (BillingTemplateItem item : items) {
				accounts.add((BillingAccount) sess.load(BillingAccount.class, item.getIdBillingAccount()));
			}
		}
		
		return accounts;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getIdBillingTemplate() == null) ? super.hashCode() : this.getIdBillingTemplate().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BillingTemplate))
			return false;
		BillingTemplate other = (BillingTemplate) obj;
		if (this.getIdBillingTemplate() == null) {
			if (other.getIdBillingTemplate() != null)
				return false;
		} else if (!this.getIdBillingTemplate().equals(other.getIdBillingTemplate()))
			return false;
		return true;
	}
	public Integer getIdBillingTemplate() {
		return this.idBillingTemplate;
	}
	public void setIdBillingTemplate(Integer idBillingTemplate) {
		this.idBillingTemplate = idBillingTemplate;
	}
	public Integer getTargetClassIdentifier() {
		return this.targetClassIdentifier;
	}
	public void setTargetClassIdentifier(Integer targetClassIdentifier) {
		this.targetClassIdentifier = targetClassIdentifier;
	}
	public String getTargetClassName() {
		return this.targetClassName;
	}
	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}
	public Set<BillingTemplateItem> getItems() {
		return this.items;
	}
	public void setItems(Set<BillingTemplateItem> items) {
		this.items = items;
	}
	public Set<MasterBillingItem> getMasterBillingItems() {
		return this.masterBillingItems;
	}
	public void setMasterBillingItems(Set<MasterBillingItem> masterBillingItems) {
		this.masterBillingItems = masterBillingItems;
	}

	@Override
	public Element toXML(Session sess, Set<String> detailParameters) {
		Element billingTemplateNode = new Element("BillingTemplate");
		
		billingTemplateNode.setAttribute("idBillingTemplate", XMLTools.safeXMLValue(this.getIdBillingTemplate()));
		billingTemplateNode.setAttribute("targetClassIdentifier", XMLTools.safeXMLValue(this.getTargetClassIdentifier()));
		billingTemplateNode.setAttribute("targetClassName", XMLTools.safeXMLValue(this.getTargetClassName()));
		
		boolean usingPercentSplit = false;
		for (BillingTemplateItem item : this.getItems()) {
			if (item.getPercentSplit() != null) {
				usingPercentSplit = true;
			}
			
			billingTemplateNode.addContent(item.toXML(sess, null));
		}
		
		billingTemplateNode.setAttribute("usingPercentSplit", usingPercentSplit ? "true" : "false");
		
		return billingTemplateNode;
	}
	
	public BillingTemplateItem getAcceptingBalanceItem() {
		for (BillingTemplateItem item : this.getItems()) {
			if (item.isAcceptingBalance()) {
				return item;
			}
		}
		
		return null;
	}

}
