package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductType;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.apache.log4j.Logger;
public class GetProductList extends GNomExCommand implements Serializable {

	// the static field for logging in Log4J
	private static Logger LOG = Logger.getLogger(GetProductList.class);

	private boolean isAdmin = false;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {

		if (isValid()) {
			setResponsePage(this.SUCCESS_JSP);
		} else {
			setResponsePage(this.ERROR_JSP);
		}

	}

	public Command execute() throws RollBackCommandException {

		try {

			Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
			DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

			Document doc = new Document(new Element("ProductList"));

			List idCoreFacility = new ArrayList();
			if (this.getSecAdvisor().getAppUser() != null) {
				// If we are super admin add all active core ids. Otherwise get the core facilities that a normal admin manages.
				if (this.getSecAdvisor().getAppUser().getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
					for (Iterator i = CoreFacility.getActiveCoreFacilities(sess).iterator(); i.hasNext();) {
						CoreFacility cf = (CoreFacility) i.next();
						idCoreFacility.add(cf.getIdCoreFacility());
					}
					isAdmin = true;

				} else if (this.getSecAdvisor().getCoreFacilitiesIManage().size() > 0) {
					for (Iterator i = this.getSecAdvisor().getCoreFacilitiesIManage().iterator(); i.hasNext();) {
						CoreFacility cf = (CoreFacility) i.next();
						idCoreFacility.add(cf.getIdCoreFacility());
					}

					isAdmin = true;

				}
			}

			// if they are admin then filter this list by core facility so that admins don't see other admins products in the prodcut dropdowns in the ledger
			// and config windows.
			// if they are normal users then bring back all products b/c they may be able to submit to all cores. This list that this servlet returns is used in
			// both locations.
			StringBuffer buf = new StringBuffer();
			if (isAdmin) {
				buf.append(" SELECT p from Product p ");
				buf.append(" JOIN p.productType as pt ");
				buf.append(" WHERE pt.idCoreFacility IN (:ids) ");
				buf.append(" order by p.name ");
			} else {
				buf.append(" SELECT p from Product p ");
			}

			Query q = sess.createQuery(buf.toString());

			if (isAdmin) {
				q.setParameterList("ids", idCoreFacility);
			}

			List products = q.list();

			for (Iterator i = products.iterator(); i.hasNext();) {
				Product product = (Product) i.next();
				this.getSecAdvisor().flagPermissions(product);
				Element node = product.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

				ProductType pt = dictionaryHelper.getProductTypeObject(product.getIdProductType());
				Price price = getProductPrice(sess, product, pt);

				node.setAttribute("unitPriceInternal", price != null ? price.getUnitPrice().toString() : "");
				node.setAttribute("unitPriceExternalAcademic", price != null ? price.getUnitPriceExternalAcademic().toString() : "");
				node.setAttribute("unitPriceExternalCommercial", price != null ? price.getUnitPriceExternalCommercial().toString() : "");

				doc.getRootElement().addContent(node);
			}

			org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
			this.xmlResult = out.outputString(doc);

			setResponsePage(this.SUCCESS_JSP);
		} catch (NamingException e) {
			this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProductList ", e);

			throw new RollBackCommandException(e.getMessage());

		} catch (SQLException e) {
			this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProductList ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (XMLReflectException e) {
			this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProductList ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (Exception e) {
			this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProductList ", e);

			throw new RollBackCommandException(e.getMessage());
		}

		if (isValid()) {
			setResponsePage(this.SUCCESS_JSP);
		} else {
			setResponsePage(this.ERROR_JSP);
		}

		return this;
	}

	public static Price getProductPrice(Session sess, Product product, ProductType pt) {

		if (product == null || pt == null) {
			return null;
		}

		String priceQuery;

		if (product.getIdPrice() != null) {
			priceQuery = "SELECT p from Price as p where p.idPrice=" + product.getIdPrice();
		} else {
			priceQuery = "SELECT p from Price as p where p.idPriceCategory=" + pt.getIdPriceCategory() + "       AND p.name='" + product.getName() + "'";
		}
		Price price = (Price) sess.createQuery(priceQuery).uniqueResult();

		return price;
	}

}