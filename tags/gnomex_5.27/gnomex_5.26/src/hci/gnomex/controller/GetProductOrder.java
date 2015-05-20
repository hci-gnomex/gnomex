package hci.gnomex.controller;

import java.io.Serializable;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.ProductOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProductOrder extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrder.class);

  private Integer idProductOrder;

  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {
    if(request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = Integer.valueOf(request.getParameter("idProductOrder"));
    } else {
      this.addInvalidField("idProductOrder", "Please provide an idProductOrder");
    }

  }
  @Override
  public Command execute() throws RollBackCommandException {
    try {
      if(this.isValid()) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.username);

        ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);

        Element root = new Element("ProductOrder");
        if(po != null) {
          String billingAccountName = "";

          if(po.getIdBillingAccount() != null) {
            BillingAccount ba = (BillingAccount)sess.load(BillingAccount.class, po.getIdBillingAccount());
            billingAccountName = ba.getAccountNameDisplay();
          }
          root.setAttribute("idProductOrder", po.getIdProductOrder().toString());
          root.setAttribute("submitter", po.getSubmitter().getFirstLastDisplayName());
          root.setAttribute("labName", po.getLab().getFormattedLabName(true));
          root.setAttribute("submitDate", po.getSubmitDate() != null ? po.getSubmitDate().toString() : "");
          root.setAttribute("orderStatus", po.getStatus());
          root.setAttribute("quoteNumber", po.getQuoteNumber() != null ? po.getQuoteNumber() : "");
          root.setAttribute("quoteReceivedDate", po.getQuoteReceivedDate() != null ? po.getQuoteReceivedDate().toString() : "");
          root.setAttribute("billingAccount", billingAccountName);
          root.setAttribute("productOrderNumber", po.getProductOrderNumber() != null ? po.getProductOrderNumber() : po.getIdProductOrder().toString());
        }

        Document doc = new Document(root);
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);

      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch(Exception e) {
      log.error("An exception has occurred in GetProductOrder ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());  
    }finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }

    }
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
