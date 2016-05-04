package hci.gnomex.utility;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Lab;

import java.util.Set;

import org.hibernate.Session;

public interface Order {
	
	public Integer getTargetClassIdentifier();
	
	public String getTargetClassName();
	
	public Integer getIdProduct();
	
	public String getCodeApplication();
	
	public String getCodeRequestCategory();
	
	public Integer getIdRequest();
	
	public Integer getIdLab();
	
	public Integer getIdBillingAccount();
	
	public Set<BillingItem> getBillingItemList(Session sess);
	
	public Integer getIdCoreFacility();
	
	public Lab getLab();
	
	public String getCodeBioanalyzerChipType();
	
	public BillingTemplate getBillingTemplate(Session sess);
	
	public Integer getIdProductOrder();
	
	public Integer getAcceptingBalanceAccountId(Session sess);
	
	public void setIdBillingAccount(Integer idBillingAccount);

}
