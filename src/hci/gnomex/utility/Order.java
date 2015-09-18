package hci.gnomex.utility;

import hci.gnomex.model.Lab;

public interface Order {
	
	public Integer getIdProduct();
	
	public String getCodeApplication();
	
	public String getCodeRequestCategory();
	
	public Integer getIdRequest();
	
	public Integer getIdLab();
	
	public Integer getIdBillingAccount();
	
	public Integer getIdCoreFacility();
	
	public Lab getLab();

}
