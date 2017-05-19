package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLedger;
import hci.gnomex.utility.GNomExRollbackException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

@SuppressWarnings("serial")
public class SaveProductLedgerEntryList extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(SaveProductLedgerEntryList.class);

Document productLedgerEntryListDoc;

@Override
public void loadCommand(HttpServletRequest request, HttpSession session) {
	String productLedgerEntryListXMLString = "";
	if (request.getParameter("productLedgerEntryList") != null
			&& !request.getParameter("productLedgerEntryList").equals("")) {
		productLedgerEntryListXMLString = request.getParameter("productLedgerEntryList");
	} else {
		this.addInvalidField("ProductLedgerEntryList", "ProductLedgerEntryList absent");
	}

	StringReader reader = new StringReader(productLedgerEntryListXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		productLedgerEntryListDoc = sax.build(reader);
	} catch (JDOMException je) {
		LOG.error("Cannot parse productLedgerEntryListXMLString", je);
		this.addInvalidField("ProductLedgerEntryListXMLString", "Invalid product ledger entry list xml");
	}
}

@SuppressWarnings("unchecked")
@Override
public Command execute() throws RollBackCommandException {

	try {
		if (this.isValid()) {
			Session sess = this.getSecAdvisor().getHibernateSession(this.username);

			Element productLedgerEntryListRoot = productLedgerEntryListDoc.getRootElement();
			List<Element> ledgerEntries = (List<Element>) productLedgerEntryListRoot.getChildren("entry");

			for (Element entry : ledgerEntries) {
				ProductLedger productLedger = sess.load(ProductLedger.class,
						Integer.parseInt(entry.getAttributeValue("idProductLedger")));

				productLedger.setNotes(entry.getAttributeValue("notes"));

				sess.save(productLedger);
			}

			this.setResponsePage(this.SUCCESS_JSP);
		} else {
			this.setResponsePage(this.ERROR_JSP);
		}
	} catch (Exception e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in SaveProductLedgerEntryList ", e);

		throw new GNomExRollbackException(e.getMessage(), true,
				"An error occurred saving the product ledger entry list");
	}

	return this;
}

@Override
public void validate() {
}

}
