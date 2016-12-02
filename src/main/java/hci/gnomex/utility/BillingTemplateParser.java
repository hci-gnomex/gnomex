package hci.gnomex.utility;

import java.math.BigDecimal;
import java.util.TreeSet;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;

public class BillingTemplateParser {
	
  private BillingTemplate billingTemplate;
  private Element billingTemplateNode;
  
  public BillingTemplateParser(Document doc) {
    this(doc.getRootElement());
  }
  
  public BillingTemplateParser(Element node) {
    this.billingTemplateNode = node;
  }
  
  public BillingTemplate getBillingTemplate() {
    return billingTemplate;
  }
  
	public void parse(Session sess) throws Exception {
		// Get billing template or create new one
		Integer idBillingTemplate = Integer.parseInt(billingTemplateNode.getAttributeValue("idBillingTemplate"));
		if (idBillingTemplate.intValue() == 0) {
			billingTemplate = new BillingTemplate();
		} else {
			billingTemplate = sess.load(BillingTemplate.class, idBillingTemplate);
			
			// Populate fields
			String fullTargetClassName = QueryManager.convertToFullTargetClassName(billingTemplateNode.getAttributeValue("targetClassName"));
			if (!QueryManager.isValidTargetClass(billingTemplateNode.getAttributeValue("targetClassIdentifier"), fullTargetClassName, sess)) {
				throw new ParserException("The specified target class identifier and class name are not valid");
			}
			billingTemplate.setTargetClassIdentifier(Integer.parseInt(billingTemplateNode.getAttributeValue("targetClassIdentifier")));
			billingTemplate.setTargetClassName(fullTargetClassName);
		}
	}
	

	public TreeSet<BillingTemplateItem> getBillingTemplateItems() throws Exception {

		TreeSet<BillingTemplateItem> billingTemplateItems = new TreeSet<BillingTemplateItem>();

		boolean hasItemAcceptingBalance = false;
		boolean usingPercentSplit = billingTemplateNode.getAttributeValue("usingPercentSplit").trim().equalsIgnoreCase("true");
		BigDecimal percentTotal = BigDecimal.valueOf(0);

		for (Object child : billingTemplateNode.getChildren("BillingTemplateItem")) {
			Element billingTemplateItemNode = (Element) child;

			BillingTemplateItem parsedBillingTemplateItem = new BillingTemplateItem();
			parsedBillingTemplateItem.setIdBillingAccount(Integer.parseInt(billingTemplateItemNode.getAttributeValue("idBillingAccount")));

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
					BigDecimal percentSplit = new BigDecimal(billingTemplateItemNode.getAttributeValue("percentSplit").replace("%",""));
					if (percentSplit.compareTo(BigDecimal.valueOf(0)) <= 0) {
						throw new ParserException("All billing accounts must accept a percentage greater than 0%.");
					}
					percentTotal = percentTotal.add(percentSplit);
					if (percentTotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
						throw new ParserException("Total percentage of all billing accounts cannot exceed 100%.");
					}
					parsedBillingTemplateItem.setPercentSplit( percentSplit.divide( new BigDecimal(100) ));
				} else {
					BigDecimal dollarAmount = new BigDecimal(billingTemplateItemNode.getAttributeValue("dollarAmount").replace("$","").replace(",",""));
					if (dollarAmount.compareTo(BigDecimal.valueOf(0)) <= 0) {
						throw new ParserException("All billing accounts must accept a dollar amount greater than $0.");
					}
					parsedBillingTemplateItem.setDollarAmount(dollarAmount);
					parsedBillingTemplateItem.setDollarAmountBalance(dollarAmount);
				}
			}
			billingTemplateItems.add( parsedBillingTemplateItem );
		}

		if (!hasItemAcceptingBalance) {
			throw new ParserException("The billing template must have an account accepting the remaining balance.");
		}

		return billingTemplateItems;
	}
	

}
