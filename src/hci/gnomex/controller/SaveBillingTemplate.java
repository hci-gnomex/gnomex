package hci.gnomex.controller;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;
import hci.gnomex.model.MasterBillingItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingTemplateParser;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.ParserException;

@SuppressWarnings("serial")
public class SaveBillingTemplate extends GNomExCommand implements Serializable {
	
	private static org.apache.log4j.Logger 	log = org.apache.log4j.Logger.getLogger(SaveBillingTemplate.class);
	
	private static final String 			ERROR_MESSAGE = "An error occurred while saving the billing template";
	
	private Document						billingTemplateDoc;

	@Override
	public void loadCommand(HttpServletRequest request, HttpSession sess) {
		if (request.getParameter("billingTemplateXMLString") != null && !request.getParameter("billingTemplateXMLString").trim().equals("")) {
			String billingTemplateXMLString = request.getParameter("billingTemplateXMLString");
			StringReader reader = new StringReader(billingTemplateXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				billingTemplateDoc = sax.build(reader);
			} catch (Exception e) {
				log.error("Cannot parse billingTemplateXMLString", e);
				this.addInvalidField("billingTemplateXMLString", "Cannot parse billingTemplateXMLString");
			}
		} else {
			this.addInvalidField("billingTemplateXMLString", "billingTemplateXMLString must be provided");
		}
		
		if (!getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS) && !getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && !getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
			log.error("Insufficient permissions to save billing template for " + this.getSecAdvisor().getUserFirstName() + " " + this.getSecAdvisor().getUserLastName());
			this.addInvalidField("PermissionError", "Insufficient permissions to save billing template");
		}
	}
	
	@Override
	public Command execute() throws RollBackCommandException {
		Session sess = null;
		
		try {
			
			if (this.isValid()) {
				sess = HibernateSession.currentSession(this.getUsername());
				
				BillingTemplate newBillingTemplate = BillingTemplateParser.parse(billingTemplateDoc, sess);
				
				// Delete existing billing items
				for (BillingItem billingItemToDelete : newBillingTemplate.getBillingItems(sess)) {
					sess.delete(billingItemToDelete);
				}
				
				for (BillingItem newlyCreatedBillingItem : newBillingTemplate.recreateBillingItems(sess)) {
					sess.save(newlyCreatedBillingItem);
				}
				
				sess.flush();
				
				this.xmlResult = "<SUCCESS/>";
			}
			
			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}
			
		} catch (Exception e) {
			log.error("An exception has occurred in SaveBillingTemplate ", e);
			e.printStackTrace();
			throw new GNomExRollbackException(e.getMessage() != null ? e.getMessage() : ERROR_MESSAGE, true, e instanceof ParserException ? ERROR_MESSAGE + ": " + e.getMessage() : ERROR_MESSAGE);
		} finally {
			try {
				if (sess != null) {
					HibernateSession.closeSession();
				}
			} catch (Exception e) {
				
			}
		}
		
		return this;
	}
	
	public static Set<BillingItem> createBillingItemsForMaster(Session sess, MasterBillingItem masterBillingItem, BillingTemplate template) {
		ArrayList<BillingTemplateItem> sortedTemplateItems = new ArrayList<BillingTemplateItem>();
		sortedTemplateItems.addAll(template.getItems());
		Collections.sort(sortedTemplateItems);
		
		Set<BillingItem> createdBillingItems = new HashSet<BillingItem>();
		
		BigDecimal amountAccountedFor = new BigDecimal(0);
		
		for (BillingTemplateItem templateItem : sortedTemplateItems) {
			// If all the costs of this master billing item are already covered, break
			if (amountAccountedFor.compareTo(masterBillingItem.getTotalPrice()) == 0) {
				break;
			}
			
			// If account has already used up all its balance, don't make a billing item for $0
			if (templateItem.getDollarAmount() != null && templateItem.getDollarAmountBalance().compareTo(BigDecimal.valueOf(0)) == 0) {
				continue;
			}
			
			BillingItem billingItem = new BillingItem();
			billingItem.setIdMasterBillingItem(masterBillingItem.getIdMasterBillingItem());
			billingItem.setCodeBillingChargeKind(masterBillingItem.getCodeBillingChargeKind());
			billingItem.setCategory(masterBillingItem.getCategory());
			billingItem.setDescription(masterBillingItem.getDescription());
			billingItem.setQty(masterBillingItem.getQty());
			billingItem.setUnitPrice(masterBillingItem.getUnitPrice());
			billingItem.setIdBillingPeriod(masterBillingItem.getIdBillingPeriod());
			billingItem.setIdPrice(masterBillingItem.getIdPrice());
			billingItem.setIdPriceCategory(masterBillingItem.getIdPriceCategory());
			billingItem.setIdCoreFacility(masterBillingItem.getIdCoreFacility());
			billingItem.setIdBillingAccount(templateItem.getIdBillingAccount());
			billingItem.setPercentagePrice(BigDecimal.valueOf(1));
			BillingAccount billingAccount = (BillingAccount) sess.load(BillingAccount.class, templateItem.getIdBillingAccount());
			if (billingAccount != null) {
				billingItem.setIdLab(billingAccount.getLab().getIdLab());
			}
			
			BigDecimal percentSplit = templateItem.getPercentSplit();
			BigDecimal dollarAmountBalance = templateItem.getDollarAmountBalance();
			
			if (templateItem.isAcceptingBalance()) {
				billingItem.setInvoicePrice(masterBillingItem.getTotalPrice().subtract(amountAccountedFor));
			} else if (percentSplit != null) {
				billingItem.setInvoicePrice(percentSplit.multiply(masterBillingItem.getTotalPrice()));
			} else {
				if (dollarAmountBalance.compareTo(masterBillingItem.getTotalPrice().subtract(amountAccountedFor)) >= 0) {
					billingItem.setInvoicePrice(masterBillingItem.getTotalPrice().subtract(amountAccountedFor));
					templateItem.setDollarAmountBalance(dollarAmountBalance.subtract(billingItem.getInvoicePrice()));
				} else {
					billingItem.setInvoicePrice(dollarAmountBalance);
					templateItem.setDollarAmountBalance(new BigDecimal(0));
				}
			}

			sess.save(billingItem);

			masterBillingItem.getBillingItems().add(billingItem);
			
			amountAccountedFor = amountAccountedFor.add(billingItem.getInvoicePrice());
			
			// TODO - Fields not set
			/*
			private String         	codeBillingStatus;
			private String         	currentCodeBillingStatus;
			private String         	notes;
			private Date           	completeDate;
			private Integer        	idInvoice;
			*/
			
			createdBillingItems.add(billingItem);
		}
		
		return createdBillingItems;
	}

	@Override
	public void validate() {
	}

}
