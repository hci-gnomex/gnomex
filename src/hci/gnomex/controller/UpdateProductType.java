package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("serial")
public class UpdateProductType extends GNomExCommand implements Serializable {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateProductType.class);
	
	private String				codeProductType;
	private Integer				idPriceCategory;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		
		if (request.getParameter("codeProductType") != null && !request.getParameter("codeProductType").trim().equals("")) {
			codeProductType = request.getParameter("codeProductType");
		}
		
		if (request.getParameter("idPriceCategory") != null && !request.getParameter("idPriceCategory").trim().equals("")) {
			idPriceCategory = new Integer(request.getParameter("idPriceCategory"));
		}
		
	}

	public Command execute() throws RollBackCommandException {

		try {

			Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

			if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
				this.addInvalidField("permissionerror", "Insufficient permissions to modify product type price category.");
			}

			if (isValid()) {
				
				this.updatePriceCategory(sess);

				this.xmlResult = "<SUCCESS/>";
			}

			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (Exception e) {
			log.error("An exception has occurred in UpdateProductType ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();
			} catch (Exception e) {

			}
		}

		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void updatePriceCategory(Session sess) {
		if (codeProductType == null) {
			return;
		}
		
		// Update price category for all prices associated with products of the product type
		
		Query query = sess.createQuery(" SELECT DISTINCT p FROM Product AS p WHERE p.codeProductType =:codeProductType ");
		query.setParameter("codeProductType", codeProductType);
		List<Product> queryResult = (List<Product>) query.list();
		
		for (Product p : queryResult) {
			if (p.getIdPrice() != null) {
				Price price = (Price) sess.load(Price.class, p.getIdPrice());
				if (price != null) {
					price.setIdPriceCategory(idPriceCategory);
					sess.save(price);
				}
			}
		}
	}

}
