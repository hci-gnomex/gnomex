package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLedger;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderStatus;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ChangeProductOrderStatus extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProductOrder.class);

  private String selectedOrdersXMLString;
  private String selectedlineItemsXMLString;
  private String codeProductOrderStatus;
  private Document orderDoc;
  private Document lineItemDoc;
  private String resultMessage = "";

  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    if(request.getParameter("selectedOrders") != null && !request.getParameter("selectedOrders").equals("") ) {
      selectedOrdersXMLString = request.getParameter("selectedOrders");

      StringReader reader = new StringReader(selectedOrdersXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        orderDoc = sax.build(reader);
      } catch (JDOMException je ) {
        log.error( "Cannot parse selectedOrdersXMLString", je );
        this.addInvalidField( "selectedOrdersXMLString", "Invalid selectedOrders xml");
      }

    } else if(request.getParameter("selectedLineItems") != null && !request.getParameter("selectedLineItems").equals("") ) {
      selectedlineItemsXMLString = request.getParameter("selectedLineItems");
      StringReader reader = new StringReader(selectedlineItemsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        lineItemDoc = sax.build(reader);
      } catch (JDOMException je ) {
        log.error( "Cannot parse selectedlineItemsXMLString", je );
        this.addInvalidField( "selectedlineItemsXMLString", "Invalid selectedlineItems xml");
      }
    } else {
      this.addInvalidField("lineItemsXMLString", "Missing line item list");
    }

    if(request.getParameter("codeProductOrderStatus") != null && !request.getParameter("codeProductOrderStatus").equals("") ) {
      codeProductOrderStatus = request.getParameter("codeProductOrderStatus");
    } else {
      this.addInvalidField("codeProductOrderStatus", "Missing codeProductOrderStatus");
    }

  }

  public Command execute() throws RollBackCommandException {
    try {
      if(this.isValid()) {
        Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

        if ( orderDoc != null ) {
          for(Iterator i = orderDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element n = (Element)i.next();
            Integer idProductOrder = Integer.valueOf(n.getAttributeValue("idProductOrder"));
            ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);
            for (ProductLineItem li : (Set<ProductLineItem>)po.getProductLineItems()) {
              String oldStatus = li.getCodeProductOrderStatus();
              if ( updateLedger(li, po, oldStatus, codeProductOrderStatus, sess) ) {
                li.setCodeProductOrderStatus(codeProductOrderStatus);
                sess.update(li);
              } 
            }
          }
        }
        if ( lineItemDoc != null ) {
          for(Iterator i = lineItemDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element n = (Element)i.next();
            Integer idProductLineItem = Integer.valueOf(n.getAttributeValue("idProductLineItem"));
            ProductLineItem pli = (ProductLineItem)sess.load(ProductLineItem.class, idProductLineItem);
            ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, pli.getIdProductOrder());
            String oldStatus = pli.getCodeProductOrderStatus();
            if ( updateLedger(pli, po, oldStatus, codeProductOrderStatus, sess) ) {
              pli.setCodeProductOrderStatus(codeProductOrderStatus);
              sess.update(pli);
            } 
          }
        }
        sess.flush();
        this.xmlResult = "<SUCCESS message=\"" + resultMessage + "\"/>";
        this.setResponsePage(this.SUCCESS_JSP);
      } else {
        this.setResponsePage(this.ERROR_JSP);
      }
    }catch(Exception e) {
      log.error("An exception has occurred in ChangeProductOrderStatus ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage()); 
    }finally {
      try {
        this.getSecAdvisor().closeHibernateSession();
      } catch(Exception e) {
      }
    }
    return this;
  }

  public boolean updateLedger(ProductLineItem pli, ProductOrder po, String oldCodePOStatus, String newPOStatus, Session sess) {

    int orderQty = 1; 
    if ( pli.getProduct().getOrderQty() != null && pli.getProduct().getOrderQty() > 0 ) {
      orderQty = pli.getQty();
    }

    // Check for an old status of something other than completed and new status is completed.  
    // If so, add items to ledger
    if ( (oldCodePOStatus == null || !oldCodePOStatus.equals( ProductOrderStatus.COMPLETED )) && newPOStatus.equals( ProductOrderStatus.COMPLETED ) ) {
      ProductLedger ledger = new ProductLedger();
      ledger.setIdLab( po.getIdLab() );
      ledger.setIdProduct( pli.getIdProduct() );
      ledger.setQty( orderQty * pli.getQty() );
      ledger.setTimeStamp( new Timestamp( System.currentTimeMillis() ) );
      ledger.setIdProductOrder( po.getIdProductOrder() );
      ledger.setComment( po.getDisplay() + " changed to completed status." );
      sess.save( ledger );
      resultMessage += "Status changed for " + po.getDisplay() + ", " + pli.getDisplay() + ".\nLedger entry created.\r\n";
    }
    // Check for old status is completed and new status is not.  
    // If so, remove items in ledger
    else if ( (oldCodePOStatus != null && oldCodePOStatus.equals( ProductOrderStatus.COMPLETED )) && !newPOStatus.equals( ProductOrderStatus.COMPLETED ) ) {
      if ( pli.getIdProductOrder() != null ) {

        Integer labTotal = (Integer) sess.createQuery( "select SUM(qty) from ProductLedger where idLab = " + po.getIdLab() + " and idProduct = " + pli.getIdProduct() ).uniqueResult();
        Integer poTotal = (Integer) sess.createQuery( "select SUM(qty) from ProductLedger where idProductOrder = " + pli.getIdProductOrder() ).uniqueResult();

        if ( labTotal - poTotal >= 0 ) {
          String query = "SELECT pl from ProductLedger pl where pl.idProductOrder = " + pli.getIdProductOrder();
          List ledgerEntries = sess.createQuery(query).list();

          for(Iterator i = ledgerEntries.iterator(); i.hasNext();) {
            ProductLedger ledger = (ProductLedger)i.next();
            sess.delete( ledger );
            resultMessage += "Status changed for " + po.getDisplay() + ", " + pli.getDisplay() + ".\nLedger entries deleted.\r\n";
          }
          sess.flush();
        } else {
          resultMessage += "Cannot revert status for " + po.getDisplay() + ", " + pli.getDisplay() + "; removing ledger entries will result in a negative balance.\r\n";
          return false;
        }
      }
    }
    else {
      resultMessage += "Status changed for " + po.getDisplay() + ", " + pli.getDisplay() + ".\r\n";
    }
    return true;
  }

  public void validate() {

  }

}
