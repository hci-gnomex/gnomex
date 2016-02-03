package hci.gnomex.controller;

import java.io.Serializable;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.DetailObject;
import hci.gnomex.utility.GNomExRollbackException;

@SuppressWarnings("serial")
public class GetBillingTemplate extends GNomExCommand implements Serializable {
	
	private static org.apache.log4j.Logger 	log = org.apache.log4j.Logger.getLogger(GetBillingTemplate.class);
	
	private static final String 			ERROR_MESSAGE = "An error occurred while retrieving the billing template";
	
	private BillingTemplateQueryManager 	queryManager;

	@Override
	public void loadCommand(HttpServletRequest request, HttpSession sess) {
		queryManager = new BillingTemplateQueryManager(this.getSecAdvisor());
		HashMap<String, String> errors = queryManager.load(request);
		this.addInvalidFields(errors);
		if (!queryManager.hasSufficientCriteria()) {
			this.addInvalidField("Insufficient Criteria", "Insufficient criteria provided.");
		}
	}
	
	@Override
	public Command execute() throws RollBackCommandException {
		try {
			
			if (this.isValid()) {
				Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
				
				BillingTemplate billingTemplate = queryManager.retrieveBillingTemplate(sess);
				
				Element billingTemplateNode = billingTemplate.toXML(null);
				
				XMLOutputter out = new org.jdom.output.XMLOutputter();
				this.xmlResult = out.outputString(new Document(billingTemplateNode));
			}
			
			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}
			
		} catch (Exception e) {
			log.error("An exception has occurred in GetBillingTemplate ", e);
			e.printStackTrace();
			throw new GNomExRollbackException(e.getMessage() != null ? e.getMessage() : ERROR_MESSAGE, false, ERROR_MESSAGE);
		} finally {
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();
			} catch (Exception e) {
				
			}
		}
		
		return this;
	}

	@Override
	public void validate() {
	}

}
