package hci.gnomex.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.security.SecurityAdvisor;
import org.apache.log4j.Logger;
@SuppressWarnings("serial")
public class UpdateProductType extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(UpdateProductType.class);

  private Integer				idProductType;
  private Integer				idPriceCategory;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProductType") != null && !request.getParameter("idProductType").trim().equals("")) {
      idProductType = new Integer(request.getParameter("idProductType"));
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
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in UpdateProductType ", e);

      throw new RollBackCommandException(e.getMessage());
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  private void updatePriceCategory(Session sess) {
    if (idProductType == null || idProductType == 0) {
      return;
    }

    // Update price category for all prices associated with products of the product type

    Query query = sess.createQuery(" SELECT DISTINCT p FROM Product AS p WHERE p.idProductType =:idProductType ");
    query.setParameter("idProductType", idProductType);
    List<Product> queryResult = query.list();

    for (Product p : queryResult) {
      if (p.getIdPrice() != null) {
        Price price = sess.load(Price.class, p.getIdPrice());
        if (price != null) {
          price.setIdPriceCategory(idPriceCategory);
          sess.save(price);
        }
      }
    }
  }

}
