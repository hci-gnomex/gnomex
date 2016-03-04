package hci.gnomex.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import org.hibernate.Session;
import org.jdom.Element;

import hci.gnomex.utility.DetailObject;
import hci.gnomex.utility.XMLTools;
import hci.hibernate3utils.HibernateDetailObject;

@SuppressWarnings("serial")
public class BillingTemplateItem extends HibernateDetailObject implements Comparable<BillingTemplateItem>, DetailObject {
	
	public static final BigDecimal	WILL_TAKE_REMAINING_BALANCE = BigDecimal.valueOf(-1.0);
	
	private Integer					idBillingTemplateItem;
	private Integer 				idBillingTemplate;
	private Integer 				idBillingAccount;
	private BigDecimal 				percentSplit;
	private BigDecimal 				dollarAmount;
	private BigDecimal				dollarAmountBalance;
	private Integer					sortOrder;
	
	public BillingTemplateItem() {
		super();
	}
	
	public BillingTemplateItem(BillingTemplate template) {
		this();
		
		this.setIdBillingTemplate(template.getIdBillingTemplate());
	}
	
	public boolean isAcceptingBalance() {
		return (percentSplit != null && percentSplit.compareTo(WILL_TAKE_REMAINING_BALANCE) == 0) || (dollarAmount != null && dollarAmount.compareTo(WILL_TAKE_REMAINING_BALANCE) == 0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.idBillingTemplateItem == null) ? super.hashCode() : this.idBillingTemplateItem.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BillingTemplateItem))
			return false;
		BillingTemplateItem other = (BillingTemplateItem) obj;
		if (this.idBillingTemplateItem == null) {
			if (other.idBillingTemplateItem != null)
				return false;
		} else if (!this.idBillingTemplateItem.equals(other.idBillingTemplateItem))
			return false;
		return true;
	}
	public Integer getIdBillingTemplateItem() {
		return this.idBillingTemplateItem;
	}
	public void setIdBillingTemplateItem(Integer idBillingTemplateItem) {
		this.idBillingTemplateItem = idBillingTemplateItem;
	}
	public Integer getIdBillingTemplate() {
		return this.idBillingTemplate;
	}
	public void setIdBillingTemplate(Integer idBillingTemplate) {
		this.idBillingTemplate = idBillingTemplate;
	}
	public Integer getIdBillingAccount() {
		return this.idBillingAccount;
	}
	public void setIdBillingAccount(Integer idBillingAccount) {
		this.idBillingAccount = idBillingAccount;
	}
	public BigDecimal getPercentSplit() {
		return this.percentSplit;
	}
	public void setPercentSplit(BigDecimal percentSplit) {
		if (percentSplit != null) {
			this.percentSplit = percentSplit.setScale(1, RoundingMode.HALF_EVEN);
			this.setDollarAmount(null);
			this.setDollarAmountBalance(null);
		} else {
			this.percentSplit = percentSplit;
		}
	}
	public BigDecimal getDollarAmount() {
		return this.dollarAmount;
	}
	public void setDollarAmount(BigDecimal dollarAmount) {
		if (dollarAmount != null) {
			this.dollarAmount = dollarAmount.setScale(2, RoundingMode.HALF_EVEN);
			this.setPercentSplit(null);
		} else {
			this.dollarAmount = dollarAmount;
		}
	}
	public BigDecimal getDollarAmountBalance() {
		return this.dollarAmountBalance;
	}
	public void setDollarAmountBalance(BigDecimal dollarAmountBalance) {
		if (dollarAmountBalance != null) {
			this.dollarAmountBalance = dollarAmountBalance.setScale(2, RoundingMode.HALF_EVEN);
		} else {
			this.dollarAmountBalance = dollarAmountBalance;
		}
	}
	public Integer getSortOrder() {
		return this.sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@Override
	public int compareTo(BillingTemplateItem o) {
		return this.sortOrder - o.sortOrder;
	}

	@Override
	public Element toXML(Session sess, Set<String> detailParameters) {
		Element billingTemplateItemNode = new Element("BillingTemplateItem");
		
		billingTemplateItemNode.setAttribute("idBillingTemplateItem", XMLTools.safeXMLValue(this.getIdBillingTemplateItem()));
		billingTemplateItemNode.setAttribute("idBillingTemplate", XMLTools.safeXMLValue(this.getIdBillingTemplate()));
		billingTemplateItemNode.setAttribute("idBillingAccount", XMLTools.safeXMLValue(this.getIdBillingAccount()));
		billingTemplateItemNode.setAttribute("percentSplit", XMLTools.safeXMLValue(this.getPercentSplit()));
		billingTemplateItemNode.setAttribute("dollarAmount", XMLTools.safeXMLValue(this.getDollarAmount()));
		billingTemplateItemNode.setAttribute("dollarAmountBalance", XMLTools.safeXMLValue(this.getDollarAmountBalance()));
		billingTemplateItemNode.setAttribute("sortOrder", XMLTools.safeXMLValue(this.getSortOrder()));
		billingTemplateItemNode.setAttribute("acceptBalance", this.isAcceptingBalance() ? "true" : "false");
		
		BillingAccount billingAccount = (BillingAccount) sess.load(BillingAccount.class, this.getIdBillingAccount());
		if (billingAccount != null) {
			billingTemplateItemNode.setAttribute("accountName", XMLTools.safeXMLValue(billingAccount.getAccountName()));
			billingTemplateItemNode.setAttribute("accountNumber", XMLTools.safeXMLValue(billingAccount.getAccountNumber()));
			billingTemplateItemNode.setAttribute("accountNumberDisplay", XMLTools.safeXMLValue(billingAccount.getAccountNumberDisplay()));
			Lab lab = billingAccount.getLab();
			if (lab != null) {
				billingTemplateItemNode.setAttribute("idLab", XMLTools.safeXMLValue(lab.getIdLab()));
				billingTemplateItemNode.setAttribute("labName", XMLTools.safeXMLValue(lab.getName()));
			}
		}
		
		return billingTemplateItemNode;
	}

}
