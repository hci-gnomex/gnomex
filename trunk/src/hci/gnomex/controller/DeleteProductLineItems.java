package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class DeleteProductLineItems extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteProductLineItems.class);

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
        log.error( "Cannot parse productOrdersToDeleteXMLString", je );
        this.addInvalidField( "productOrdersToDeleteXMLString", "Invalid productOrdersToDeleteXMLString");
      }
    } else if (request.getParameter("productLineItemsToDeleteXMLString") != null && !request.getParameter("productLineItemsToDeleteXMLString").equals("")) {
      productLineItemsToDeleteXMLString = request.getParameter("productLineItemsToDeleteXMLString");
      StringReader reader = new StringReader(productLineItemsToDeleteXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        productLineItemsToDeleteDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse productLineItemsToDeleteXMLString", je );
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
              for (ProductLineItem li : (Set<ProductLineItem>)po.getProductLineItems()) {
                this.deleteProductLineItem( li, sess );
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
      log.error("An exception has occurred in DeleteProductOrders ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }

  private void deleteProductLineItem(ProductLineItem pli, Session sess) {

    ProductOrder po = (ProductOrder) sess.load( ProductOrder.class, pli.getIdProductOrder() );

    if ( pli.getCodeProductOrderStatus() != null &&  pli.getCodeProductOrderStatus().equals( ProductOrderStatus.COMPLETED )) {
      resultMessage += "Cannot delete completed product line item: " + pli.getDisplay() + ".\r\n";
      this.addInvalidField("Cannot delete line item", "Cannot delete completed product line item: " + pli.getDisplay() + ".");
    } else {
      po.getProductLineItems().remove( pli );
      sess.update( po );
      sess.delete(pli);
      sess.flush();
      resultMessage += "Product line item: " + pli.getDisplay() + " deleted.\r\n";
    }

    if ( po != null && po.getProductLineItems().size()==0 ) {
      resultMessage += po.getDisplay() + " deleted.\r\n";
      sess.delete(po);
    }

    sess.flush();
  }

}