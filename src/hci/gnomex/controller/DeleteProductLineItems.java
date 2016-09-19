package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.ProductLineItemComparator;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;



public class DeleteProductLineItems extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteProductLineItems.class);

  private String productOrdersToDeleteXMLString;
  private Document productOrdersToDeleteDoc;
  private String productLineItemsToDeleteXMLString;
  private Document productLineItemsToDeleteDoc;
  private String resultMessage = "";

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("productOrdersToDeleteXMLString") != null && !request.getParameter("productOrdersToDeleteXMLString").equals("")) {
      productOrdersToDeleteXMLString = request.getParameter("productOrdersToDeleteXMLString");
      StringReader reader = new StringReader(productOrdersToDeleteXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        productOrdersToDeleteDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse productOrdersToDeleteXMLString", je );
        this.addInvalidField( "productOrdersToDeleteXMLString", "Invalid productOrdersToDeleteXMLString");
      }
    } else if (request.getParameter("productLineItemsToDeleteXMLString") != null && !request.getParameter("productLineItemsToDeleteXMLString").equals("")) {
      productLineItemsToDeleteXMLString = request.getParameter("productLineItemsToDeleteXMLString");
      StringReader reader = new StringReader(productLineItemsToDeleteXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        productLineItemsToDeleteDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse productLineItemsToDeleteXMLString", je );
        this.addInvalidField( "productLineItemsToDeleteXMLString", "Invalid productLineItemsToDeleteXMLString");
      }
    } else {
      this.addInvalidField("xmlString", "Missing line items to delete XML String");
    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      if ( productOrdersToDeleteDoc != null ) {
        for(Iterator i = this.productOrdersToDeleteDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
          Element node = (Element)i.next();
          Integer idProductOrder = Integer.parseInt(node.getAttributeValue("idProductOrder") );
          ProductOrder po = (ProductOrder) sess.load( ProductOrder.class, idProductOrder );

          if (po != null && po.getIdCoreFacility() != null && (this.getSecAdvisor().isCoreFacilityIManage(po.getIdCoreFacility()) 
              || this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) ){

            if ( po.getProductLineItems() == null || po.getProductLineItems().size() == 0 ) {
              resultMessage += po.getDisplay() + " deleted.\r\n";
              sess.delete(po);
            } else {
              Set <ProductLineItem> plis = po.getProductLineItems();
              // Remove all line items from product order
              po.setProductLineItems( new TreeSet(new ProductLineItemComparator()) );
              for (ProductLineItem li : plis) {
                po.getProductLineItems().remove( li );
                if ( !this.deleteProductLineItem( li, sess ) ) {
                  // Add line item back to product order if it can't be deleted.
                  po.getProductLineItems().add( li );
                }
              }
              // Delete product order if all the line items have been deleted.
              if ( po != null && po.getProductLineItems().size()==0 ) {
                resultMessage += po.getDisplay() + " deleted.\r\n";
                sess.delete(po);
              }
            }
          } else {
            this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete product order id:" + po.getIdProductOrder() + ".");
            setResponsePage(this.ERROR_JSP);
          } 
        }
      }
      if ( productLineItemsToDeleteDoc != null ) {
        for(Iterator i = this.productLineItemsToDeleteDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
          Element node = (Element)i.next();
          Integer idProductLineItem = Integer.parseInt(node.getAttributeValue("idProductLineItem") );
          ProductLineItem pli = (ProductLineItem)sess.load(ProductLineItem.class, idProductLineItem);
          ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, pli.getIdProductOrder());
          if (po != null && po.getIdCoreFacility() != null && (this.getSecAdvisor().isCoreFacilityIManage(po.getIdCoreFacility()) 
              || this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) ){

            this.deleteProductLineItem( pli, sess );

            // Delete product order if all the line items have been deleted.
            if ( po != null && po.getProductLineItems().size()==0 ) {
              resultMessage += po.getDisplay() + " deleted.\r\n";
              sess.delete(po);
            }
          } else {
            this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete product order id:" + po.getIdProductOrder() + ".");
            setResponsePage(this.ERROR_JSP);
          } 
        }
      }
      
      

      this.xmlResult = "<SUCCESS message=\"" + resultMessage + "\"/>";
      sess.flush();
      setResponsePage(this.SUCCESS_JSP);


    }catch (Exception e){
      LOG.error("An exception has occurred in DeleteProductOrders ", e);

      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        //closeHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in DeleteProductOrders ", e);
      }
    }

    return this;
  }

  private boolean deleteProductLineItem(ProductLineItem pli, Session sess) {

    if ( pli.getCodeProductOrderStatus() != null &&  pli.getCodeProductOrderStatus().equals( ProductOrderStatus.COMPLETED )) {
      resultMessage += "Cannot delete completed product line item: " + pli.getDisplay() + ".\r\n";
      this.addInvalidField("Cannot delete line item", "Cannot delete completed product line item: " + pli.getDisplay() + ".");
      return false;
    } 

    sess.delete(pli);
    resultMessage += "Product line item: " + pli.getDisplay() + " deleted.\r\n";

    sess.flush();
    return true;
  }

}