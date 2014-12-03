package hci.gnomex.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrderFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProductOrderLineItemList extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrderLineItemList.class);

  private Integer idProductOrder;
  private ProductOrderFilter filter;


  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    if(request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = Integer.valueOf(request.getParameter("idProductOrder"));
    } else {
      this.addInvalidField("idProductOrder", "Missing idProductOrder");
    }

  }

  @Override
  public Command execute() throws RollBackCommandException {
    try {
      if(this.isValid()) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.username);

        StringBuffer queryBuf = new StringBuffer();
        queryBuf.append("SELECT pli from ProductLineItem pli where pli.idProductOrder = " + idProductOrder);
        List results = sess.createQuery(queryBuf.toString()).list();

        Document doc = new Document(new Element("LineItems"));
        for(Iterator i = results.iterator(); i.hasNext();) {
          ProductLineItem pli = (ProductLineItem)i.next();
          Element n = new Element("LineItem");
          n.setAttribute("name", pli.getProduct().getName());
          n.setAttribute("totalPrice", (pli.getUnitPrice().multiply(new BigDecimal(pli.getQty()))).toString());
          n.setAttribute("unitPrice", pli.getUnitPrice().toString());
          n.setAttribute("qty", pli.getQty().toString());
          doc.getRootElement().addContent(n);
        }

        new BigDecimal(new Integer(5));

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
