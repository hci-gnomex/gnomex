package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.util.Iterator;

public class PropertyOption extends DictionaryEntry {

	private Integer idPropertyOption;
	private Integer idProperty;
	private String option;
	private Integer sortOrder;
	private String isActive;

	public String getDisplay() {
		return option != null ? option + getInactiveDisplay() : "";
	}

	public String getInactiveDisplay() {
		if (isActive != null && isActive.equals("N")) {
			return " (inactive)";
		} else {
			return "";
		}
	}

	public String getValue() {
		return idPropertyOption.toString();
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getIdPropertyOption() {
		return idPropertyOption;
	}

	public void setIdPropertyOption(Integer idPropertyOption) {
		this.idPropertyOption = idPropertyOption;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(Integer idProperty) {
		this.idProperty = idProperty;
	}

	public static Price getPriceForPropertyOption(PropertyOption po, PriceCategory pc) {
		Price price = null;
		if (po == null || po.getOption() == null || pc == null || pc.getPrices() == null) {
			return price;
		}
		for (Iterator i = pc.getPrices().iterator(); i.hasNext();) {
			price = (Price) i.next();
			if (price.getName().equalsIgnoreCase(po.getOption())) {
				break;
			}
		}
		return price;
	}

}