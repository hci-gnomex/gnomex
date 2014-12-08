package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLineItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProductOrderLineItemList extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrderLineItemList.class);

  private Integer idProductOrder;

  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {
    if(request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = Integer.parseInt(request.getParameter("idProductOrder"));
    } else {
      this.addInvalidField("Missing idProductOrder", "idProductOrder required");
    }

  }

  @Override
  public Command execute() throws RollBackCommandException {

    try {
      if(this.isValid()) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

        List lineItemRows = sess.createQuery("SELECT pli from ProductLineItem as pli where pli.idProductOrder = " + idProductOrder).list();

        Document doc = new Document(new Element("LineItems"));

        for(Iterator i = lineItemRows.iterator(); i.hasNext();) {
          ProductLineItem pli = (ProductLineItem)i.next();
          Element e = new Element("LineItem");

          Integer qty = pli.getQty();
          BigDecimal unitPrice = pli.getUnitPrice();
          e.setAttribute("name", pli.getProduct().getName());
          e.setAttribute("qty", qty.toString());
          e.setAttribute("unitPrice", unitPrice.toString());
          e.setAttribute("totalPrice", unitPrice.multiply(new BigDecimal(qty)).toString());

          doc.getRootElement().addContent(e);
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);


      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch(Exception e) {
      log.error("An exception has occurred in GetProductOrderLineItemList ", e);
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
