package hci.gnomex.utility;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;

public class BillingTemplateParser {
	
	public static BillingTemplate parse(Element billingTemplateNode, Session sess) throws Exception {
		BillingTemplate parsedBillingTemplate;
		
		Integer idBillingTemplate = Integer.parseInt(billingTemplateNode.getAttributeValue("idBillingTemplate"));
		if (idBillingTemplate.intValue() == 0) {
			parsedBillingTemplate = new BillingTemplate();
		} else {
			parsedBillingTemplate = (BillingTemplate) sess.load(BillingTemplate.class, idBillingTemplate);
		}
		
		String fullTargetClassName = QueryManager.convertToFullTargetClassName(billingTemplateNode.getAttributeValue("targetClassName"));
		if (!QueryManager.isValidTargetClass(billingTemplateNode.getAttributeValue("targetClassIdentifier"), fullTargetClassName, sess)) {
			throw new ParserException("The specified target class identifier and class name are not valid");
		}
		parsedBillingTemplate.setTargetClassIdentifier(Integer.parseInt(billingTemplateNode.getAttributeValue("targetClassIdentifier")));
		parsedBillingTemplate.setTargetClassName(fullTargetClassName);
		
		boolean usingPercentSplit = billingTemplateNode.getAttributeValue("usingPercentSplit").trim().equalsIgnoreCase("true");
		BigDecimal percentTotal = BigDecimal.valueOf(0);
		boolean hasItemAcceptingBalance = false;
		
		parsedBillingTemplate.setItems(new TreeSet<BillingTemplateItem>());
		for (Object child : billingTemplateNode.getChildren("BillingTemplateItem")) {
			Element billingTemplateItemNode = (Element) child;
			
			BillingTemplateItem parsedBillingTemplateItem = new BillingTemplateItem(parsedBillingTemplate);
			parsedBillingTemplateItem.setIdBillingTemplate(parsedBillingTemplate.getIdBillingTemplate());
			parsedBillingTemplateItem.setIdBillingAccount(Integer.parseInt(billingTemplateItemNode.getAttributeValue("idBillingAccount")));
			parsedBillingTemplateItem.setSortOrder(Integer.parseInt(billingTemplateItemNode.getAttributeValue("sortOrder")));
			
			boolean acceptingBalance = billingTemplateItemNode.getAttributeValue("acceptBalance") != null && billingTemplateItemNode.getAttributeValue("acceptBalance").trim().equalsIgnoreCase("true");
			
			if (acceptingBalance) {
				if (hasItemAcceptingBalance) {
					throw new ParserException("Only one account on a billing template may accept remaining balance.");
				}
				
				if (usingPercentSplit) {
					parsedBillingTemplateItem.setPercentSplit(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
				} else {
					parsedBillingTemplateItem.setDollarAmount(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
					parsedBillingTemplateItem.setDollarAmountBalance(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
				}
				
				hasItemAcceptingBalance = true;
			} else {
				if (usingPercentSplit) {
					BigDecimal percentSplit = new BigDecimal(billingTemplateItemNode.getAttributeValue("percentSplit"));
					if (percentSplit.compareTo(BigDecimal.valueOf(0)) <= 0) {
						throw new ParserException("All billing accounts must accept a percentage greater than 0%.");
					}
					percentTotal = percentTotal.add(percentSplit);
					if (percentTotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
						throw new ParserException("Total percentage of all billing accounts cannot exceed 100%.");
					}
					parsedBillingTemplateItem.setPercentSplit(percentSplit);
				} else {
					BigDecimal dollarAmount = new BigDecimal(billingTemplateItemNode.getAttributeValue("dollarAmount"));
					if (dollarAmount.compareTo(BigDecimal.valueOf(0)) <= 0) {
						throw new ParserException("All billing accounts must accept a dollar amount greater than $0.");
					}
					parsedBillingTemplateItem.setDollarAmount(dollarAmount);
					parsedBillingTemplateItem.setDollarAmountBalance(dollarAmount);
				}
			}
			
			parsedBillingTemplate.getItems().add(parsedBillingTemplateItem);
		}
		
		if (!hasItemAcceptingBalance) {
			throw new ParserException("The billing template must have an account accepting the remaining balance.");
		}
		
		return parsedBillingTemplate;
	}
	
	public static BillingTemplate parse(Document bilingTemplateDoc, Session sess) throws Exception {
		return BillingTemplateParser.parse(bilingTemplateDoc.getRootElement(), sess);
	}
	
	public static BillingTemplate parseExistingBillingTemplate(Element billingTemplateNode, Session sess) {
		try {
			return (BillingTemplate) sess.load(BillingTemplate.class, Integer.parseInt(billingTemplateNode.getAttributeValue("idBillingTemplate")));
		} catch (Exception e) {
			return null;
		}
	}

}
