package hci.gnomex.controller;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;
import hci.gnomex.model.MasterBillingItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingTemplateParser;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.Order;
import hci.gnomex.utility.ParserException;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SaveBillingTemplate extends GNomExCommand implements Serializable {

	private static Logger 	LOG = Logger.getLogger(SaveBillingTemplate.class);

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
				LOG.error("Cannot parse billingTemplateXMLString", e);
				this.addInvalidField("billingTemplateXMLString", "Cannot parse billingTemplateXMLString");
			}
		} else {
			this.addInvalidField("billingTemplateXMLString", "billingTemplateXMLString must be provided");
		}

		if (!getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS) && !getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && !getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
			LOG.error("Insufficient permissions to save billing template for " + this.getSecAdvisor().getUserFirstName() + " " + this.getSecAdvisor().getUserLastName());
			this.addInvalidField("PermissionError", "Insufficient permissions to save billing template");
		}
	}

	@Override
	public Command execute() throws RollBackCommandException {
		Session sess = null;

		try {

			if (this.isValid()) {
				sess = HibernateSession.currentSession(this.getUsername());

				BillingTemplateParser btParser = new BillingTemplateParser(billingTemplateDoc.getRootElement());
				btParser.parse(sess);
				BillingTemplate billingTemplate = btParser.getBillingTemplate();
				
				// If the template has any approved billing items, prevent modification
				Set<BillingItem> oldBillingItems = billingTemplate.getBillingItems(sess);
				for (BillingItem billingItem : oldBillingItems) {
                    if (!billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING) && !billingItem.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
                        throw new GNomExRollbackException("Cannot delete approved billing items", true, "Approved billing items cannot be reassigned.");
                    }
                }
				
				sess.save(billingTemplate);
				sess.flush();
				
				Map<Integer, List<Object>> infoForRecreatingBillingItems = BillingTemplate.retrieveInfoForRecreatingBillingItems(billingTemplate.getAcceptingBalanceItem(), oldBillingItems);

				// Delete old billing template items if any
				Set<BillingTemplateItem> oldBtiSet = new TreeSet<BillingTemplateItem>();
				oldBtiSet.addAll(billingTemplate.getItems());
				for (BillingTemplateItem billingTemplateItemToDelete : oldBtiSet) {
					BillingTemplateItem persistentBTI = sess.load(BillingTemplateItem.class, billingTemplateItemToDelete.getIdBillingTemplateItem());
					sess.delete(persistentBTI);
				}
				sess.flush();
				billingTemplate.getItems().clear();

				// Get new template items from parser and save to billing template
				TreeSet<BillingTemplateItem> btiSet = btParser.getBillingTemplateItems();
				for (BillingTemplateItem newlyCreatedItem : btiSet) {
					newlyCreatedItem.setIdBillingTemplate(billingTemplate.getIdBillingTemplate());
					billingTemplate.getItems().add(newlyCreatedItem);
					sess.save( newlyCreatedItem );
				}
				sess.flush();

				// Delete existing billing items
				for (BillingItem billingItemToDelete : oldBillingItems) {
					sess.delete(billingItemToDelete);
				}
				sess.flush();

				// Save new billing items
				Set<BillingItem> newBillingItems = billingTemplate.recreateBillingItems(sess, infoForRecreatingBillingItems);
				for (BillingItem newlyCreatedBillingItem : newBillingItems) {
					sess.save(newlyCreatedBillingItem);
				}
				sess.flush();
				
				// Update idBillingAccount for order
				Order order = billingTemplate.retrieveOrder(sess);
				if (order != null) {
				    order.setIdBillingAccount(billingTemplate.getAcceptingBalanceItem().getIdBillingAccount());
				    sess.save(order);
				    sess.flush();
				}

				this.xmlResult = "<SUCCESS/>";
			}

			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (GNomExRollbackException e) {
		    LOG.error("An exception has occurred in SaveBillingTemplate ", e);

            throw e;
		} catch (Exception e) {
			LOG.error("An exception has occurred in SaveBillingTemplate ", e);

			throw new GNomExRollbackException(e.getMessage() != null ? e.getMessage() : ERROR_MESSAGE, true, e instanceof ParserException ? ERROR_MESSAGE + ": " + e.getMessage() : ERROR_MESSAGE);
		} finally {
			try {
				if (sess != null) {
					//closeHibernateSession;
				}
			} catch (Exception e) {
				LOG.error("An exception has occurred in SaveBillingTemplate ", e);
			}
		}

		return this;
	}

	public static Set<BillingItem> createBillingItemsForMaster(Session sess, MasterBillingItem masterBillingItem, BillingTemplate template, Map<Integer, List<Object>> additionalInfo) {
		ArrayList<BillingTemplateItem> sortedTemplateItems = new ArrayList<BillingTemplateItem>();
		sortedTemplateItems.addAll(template.getItems());
		Collections.sort(sortedTemplateItems);

		Set<BillingItem> createdBillingItems = new HashSet<BillingItem>();

		BigDecimal amountAccountedFor = new BigDecimal(0);
		BigDecimal percentAccountedFor = new BigDecimal(0);

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
			if (!templateItem.isAcceptingBalance()) {
				billingItem.setPercentagePrice(templateItem.getPercentSplit()!=null?templateItem.getPercentSplit():BigDecimal.valueOf(1));
			}
			BillingAccount billingAccount = sess.load(BillingAccount.class, templateItem.getIdBillingAccount());
			if (billingAccount != null) {
				billingItem.setIdLab(billingAccount.getLab().getIdLab());
			}
			if ( template.getTargetClassName().indexOf( "Request" ) >= 0 ) {
				billingItem.setIdRequest( template.getTargetClassIdentifier() );
			} else if ( template.getTargetClassName().indexOf( "ProductOrder" ) >= 0 ) {
				billingItem.setIdProductOrder( template.getTargetClassIdentifier() );
			}
			if (additionalInfo != null && additionalInfo.containsKey(masterBillingItem.getIdMasterBillingItem())) {
			    List<Object> info = additionalInfo.get(masterBillingItem.getIdMasterBillingItem());
			    billingItem.setCodeBillingStatus((String) info.get(0));
			    billingItem.setCurrentCodeBillingStatus((String) info.get(1));
			    billingItem.setNotes((String) info.get(2));
			    billingItem.setCompleteDate((Date) info.get(3));
			} else {
			    billingItem.setCodeBillingStatus(BillingStatus.PENDING);
			}
	        // TODO - Fields not set
	        /*
	        private Integer idInvoice;
	        */

			BigDecimal percentSplit = templateItem.getPercentSplit();
			BigDecimal dollarAmountBalance = templateItem.getDollarAmountBalance();

			if (templateItem.isAcceptingBalance()) {
				billingItem.setInvoicePrice(masterBillingItem.getTotalPrice().subtract(amountAccountedFor));
				billingItem.setPercentagePrice(BigDecimal.valueOf(1).subtract(percentAccountedFor));
			} else if (percentSplit != null) {
				billingItem.setInvoicePrice(percentSplit.multiply(masterBillingItem.getTotalPrice()));
				percentAccountedFor = percentAccountedFor.add( percentSplit );
			} else {
				if (dollarAmountBalance.compareTo(masterBillingItem.getTotalPrice().subtract(amountAccountedFor)) >= 0) {
					billingItem.setInvoicePrice(masterBillingItem.getTotalPrice().subtract(amountAccountedFor));
					templateItem.setDollarAmountBalance(dollarAmountBalance.subtract(billingItem.getInvoicePrice()));
				} else {
					billingItem.setInvoicePrice(dollarAmountBalance);
					templateItem.setDollarAmountBalance(new BigDecimal(0));
				}
			}

			masterBillingItem.getBillingItems().add(billingItem);

			amountAccountedFor = amountAccountedFor.add(billingItem.getInvoicePrice());

			createdBillingItems.add(billingItem);
		}

		return createdBillingItems;
	}

	@Override
	public void validate() {
	}

}
