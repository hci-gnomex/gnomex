package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import hci.gnomex.model.ProductLedger;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.Request;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class GetProductLedgerEntries extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductLedgerEntries.class);

  private Integer idLab;
  private Integer idProduct;

  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {
    if(request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.parseInt(request.getParameter("idLab"));
    }

    if(request.getParameter("idProduct") != null && !request.getParameter("idProduct").equals("")) {
      idProduct = Integer.parseInt(request.getParameter("idProduct"));
    }


  }

  @Override
  public Command execute() throws RollBackCommandException {
    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      StringBuffer buf = new StringBuffer();
      buf.append("SELECT pl from ProductLedger as pl ");
      buf.append(" WHERE pl.idLab = " + idLab);
      buf.append(" AND pl.idProduct = " + idProduct);
      buf.append(" order by pl.timeStamp DESC" );

      List<ProductLedger> entries = sess.createQuery(buf.toString()).list();

      Document doc = new Document(new Element("ledgerEntries"));
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      for(Iterator i = entries.iterator(); i.hasNext();) {
        ProductLedger pl = (ProductLedger)i.next();
        Element e = new Element("entry");

        Integer idProductOrder = pl.getIdProductOrder();
        Integer idRequest = pl.getIdRequest();
        String comment = pl.getComment() != null ? pl.getComment() : "";
        String date = pl.getTimeStamp() != null ? sdf.format(pl.getTimeStamp()) : "";
        String productOrderNumber = "";
        String requestNumber = "";
        if(idRequest != null) {
          Request req = (Request)sess.load(Request.class, idRequest);
          requestNumber = req.getNumber();
        }
        if(idProductOrder != null) {
          ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);
          productOrderNumber = po.getProductOrderNumber();
        }

        e.setAttribute("productOrderNumber", productOrderNumber != null ? productOrderNumber : "" );
        e.setAttribute("requestNumber", requestNumber );
        e.setAttribute("idProductOrder", idProductOrder != null ? idProductOrder.toString() : "");
        e.setAttribute("idRequest", idRequest != null ? idRequest.toString() : "");
        e.setAttribute("comment", comment);
        e.setAttribute("date", date);
        e.setAttribute("qty", pl.getQty().toString());

        doc.getRootElement().addContent(e);
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

    }catch(Exception e) {
      log.error("An exception has occurred in GetLabLedgerEntries ", e);
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
