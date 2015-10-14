package hci.gnomex.utility;

import hci.gnomex.model.Lab;

import java.util.Set;

public interface Order {
	
	public Integer getIdProduct();
	
	public String getCodeApplication();
	
	public String getCodeRequestCategory();
	
	public Integer getIdRequest();
	
	public Integer getIdLab();
	
	public Integer getIdBillingAccount();
	
	public Set getBillingItems();
	
	public Integer getIdCoreFacility();
	
	public Lab getLab();
	
	public String getCodeBioanalyzerChipType();

}
