package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLedger;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;

@SuppressWarnings("serial")
public class SaveProductLedgerEntry extends GNomExCommand implements Serializable {
private static Logger LOG = Logger.getLogger(SaveProductLedgerEntry.class);

private Integer idLab;
private Integer idProduct;
private Integer qty;
private String comment;
private String notes;

@Override
public void loadCommand(HttpServletRequest request, HttpSession sess) {

	if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
		idLab = Integer.parseInt(request.getParameter("idLab"));
	} else {
		this.addInvalidField("missing idLab", "missing idLab");
	}

	if (request.getParameter("idProduct") != null && !request.getParameter("idProduct").equals("")) {
		idProduct = Integer.parseInt(request.getParameter("idProduct"));
	} else {
		this.addInvalidField("missing idProduct", "missing idProduct");
	}

	if (request.getParameter("qty") != null && !request.getParameter("qty").equals("")) {
		qty = Integer.parseInt(request.getParameter("qty"));
	} else {
		this.addInvalidField("missing qty", "missing qty");
	}

	if (request.getParameter("comment") != null && !request.getParameter("comment").equals("")) {
		comment = request.getParameter("comment");
	} else {
		this.addInvalidField("missing comment", "missing comment");
	}

	if (request.getParameter("notes") != null) {
		notes = request.getParameter("notes");
	} else {
		this.addInvalidField("missing notes", "missing notes");
	}

}

@Override
public Command execute() throws RollBackCommandException {

	try {

		if (this.isValid()) {

			Session sess = this.getSecAdvisor().getHibernateSession(this.username);

			ProductLedger pl = new ProductLedger();

			pl.setIdLab(idLab);
			pl.setQty(qty);
			pl.setComment(comment);
			pl.setIdProduct(idProduct);
			pl.setNotes(notes);

			pl.setTimeStamp(new Timestamp(System.currentTimeMillis()));

			sess.save(pl);
			this.setResponsePage(this.SUCCESS_JSP);

		} else {
			this.setResponsePage(this.ERROR_JSP);
		}

	} catch (Exception e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in SaveProductLedgerEntry ", e);
		throw new RollBackCommandException(e.getMessage());

	}
	return this;
}

@Override
public void validate() {
	// TODO Auto-generated method stub

}

}
