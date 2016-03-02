package hci.gnomex.model;

import java.math.BigDecimal;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;

@SuppressWarnings("serial")
public class MasterBillingItem extends HibernateDetailObject {
	
	private Integer 			idMasterBillingItem;
	private Integer 			idBillingTemplate;
	private String 				codeBillingChargeKind;
	private String 				category;
	private String 				description;
	private Integer 			qty;
	private BigDecimal 			unitPrice;
	private BigDecimal 			totalPrice;
	private Integer 			idBillingPeriod;
	private Integer 			idPrice;
	private Integer 			idPriceCategory;
	private Integer 			idCoreFacility;
	private Set<BillingItem>	billingItems;
	private BillingTemplate		billingTemplate;
	private BillingChargeKind	billingChargeKind;
	private BillingPeriod		billingPeriod;
	private Price				price;
	private PriceCategory		priceCategory;
	private CoreFacility		coreFacility;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.idMasterBillingItem == null) ? super.hashCode() : this.idMasterBillingItem.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MasterBillingItem))
			return false;
		MasterBillingItem other = (MasterBillingItem) obj;
		if (this.idMasterBillingItem == null) {
			if (other.idMasterBillingItem != null)
				return false;
		} else if (!this.idMasterBillingItem.equals(other.idMasterBillingItem))
			return false;
		return true;
	}
	public Integer getIdMasterBillingItem() {
		return this.idMasterBillingItem;
	}
	public void setIdMasterBillingItem(Integer idMasterBillingItem) {
		this.idMasterBillingItem = idMasterBillingItem;
	}
	public Integer getIdBillingTemplate() {
		return this.idBillingTemplate;
	}
	public void setIdBillingTemplate(Integer idBillingTemplate) {
		this.idBillingTemplate = idBillingTemplate;
	}
	public String getCodeBillingChargeKind() {
		return this.codeBillingChargeKind;
	}
	public void setCodeBillingChargeKind(String codeBillingChargeKind) {
		this.codeBillingChargeKind = codeBillingChargeKind;
	}
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQty() {
		return this.qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getIdBillingPeriod() {
		return this.idBillingPeriod;
	}
	public void setIdBillingPeriod(Integer idBillingPeriod) {
		this.idBillingPeriod = idBillingPeriod;
	}
	public Integer getIdPrice() {
		return this.idPrice;
	}
	public void setIdPrice(Integer idPrice) {
		this.idPrice = idPrice;
	}
	public Integer getIdPriceCategory() {
		return this.idPriceCategory;
	}
	public void setIdPriceCategory(Integer idPriceCategory) {
		this.idPriceCategory = idPriceCategory;
	}
	public Integer getIdCoreFacility() {
		return this.idCoreFacility;
	}
	public void setIdCoreFacility(Integer idCoreFacility) {
		this.idCoreFacility = idCoreFacility;
	}
	public Set<BillingItem> getBillingItems() {
		return this.billingItems;
	}
	public void setBillingItems(Set<BillingItem> billingItems) {
		this.billingItems = billingItems;
	}
	public BillingTemplate getBillingTemplate() {
		return this.billingTemplate;
	}
	public void setBillingTemplate(BillingTemplate billingTemplate) {
		this.billingTemplate = billingTemplate;
	}
	public BillingChargeKind getBillingChargeKind() {
		return this.billingChargeKind;
	}
	public void setBillingChargeKind(BillingChargeKind billingChargeKind) {
		this.billingChargeKind = billingChargeKind;
	}
	public BillingPeriod getBillingPeriod() {
		return this.billingPeriod;
	}
	public void setBillingPeriod(BillingPeriod billingPeriod) {
		this.billingPeriod = billingPeriod;
	}
	public Price getPrice() {
		return this.price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	public PriceCategory getPriceCategory() {
		return this.priceCategory;
	}
	public void setPriceCategory(PriceCategory priceCategory) {
		this.priceCategory = priceCategory;
	}
	public CoreFacility getCoreFacility() {
		return this.coreFacility;
	}
	public void setCoreFacility(CoreFacility coreFacility) {
		this.coreFacility = coreFacility;
	}
	
}
