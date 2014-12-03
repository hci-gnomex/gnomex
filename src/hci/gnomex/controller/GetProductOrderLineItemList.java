package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.model.ProductOrderFilter;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class GetProductOrderLineItemList extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrderLineItemList.class);

  private ProductOrderFilter productOrderFilter;
  private HashMap<Integer, List<Object[]>> lineItemMap = new HashMap<Integer, List<Object[]>>();
  private HashMap<Integer, List<Object[]>> billingItemMap = new HashMap<Integer, List<Object[]>>();
  private HashMap<Integer, List<Object[]>> filesMap = new HashMap<Integer, List<Object[]>>();
  
  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    productOrderFilter = new ProductOrderFilter(this.getSecAdvisor());
    HashMap errors = this.loadDetailObject(request, productOrderFilter);
    this.addInvalidFields(errors);

    //TODO: Different permissions
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
      this.addInvalidField("permission", "Insufficient permission to manage billing items");
    }
  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      Document doc = new Document(new Element("ProductOrderList"));

      StringBuffer buf = productOrderFilter.getProductOrderQuery();
      log.info("Query for GetProductOrderLineItemList: " + buf.toString());
      
      List productOrders = sess.createQuery(buf.toString()).list();

      // hash product line items
      hashLineItems( sess );
      
      // hash billing items
      hashBillingItems( sess );
      
      // hash files
      hashFiles( sess );
      
      for ( Iterator i = productOrders.iterator(); i.hasNext(); ) {
        Object[] row = (Object[])i.next();
        
        // Get all attributes according to query rows returned
        Integer idProductOrder        = (Integer) row[0];
        Integer idAppUser             = (Integer) row[1];
        AppUser submitter             = (AppUser) row[2];
        Integer idLab                 = (Integer) row[3];
        String labFirstName           = row[4] != null ? (String) row[4] : "";
        String labLastName            = row[5] != null ? (String) row[5] : "";
        Integer idCoreFacility        = (Integer) row[6];
        String submitDate             = row[7] != null ? this.formatDateTime((java.util.Date)row[7], this.DATE_OUTPUT_DASH) : "";
        String codeProductType        = row[8] != null ? (String) row[8] : "";
        String codeProductOrderStatus = row[9] != null ? (String) row[9] : "";
        String quoteNumber            = row[10] != null ? (String) row[10] : "";
        String quoteReceivedDate      = row[11] != null ? this.formatDateTime((java.util.Date)row[11], this.DATE_OUTPUT_DASH) : "";
        String uuid                   = row[12] != null ? (String) row[12] : "";

        String labName = Lab.formatLabName(labLastName, labFirstName);
        
        Element poNode = new Element("ProductOrder");
        
        // Set all attributes obtained above
        poNode.setAttribute("idProductOrder", toString(idProductOrder));
        poNode.setAttribute("nodeDisplay", "Order " + toString(idProductOrder));
        poNode.setAttribute("idAppUser", toString(idAppUser));
        poNode.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        poNode.setAttribute("idLab", toString(idLab));
        poNode.setAttribute("labName", labName);
        poNode.setAttribute("idCoreFacility", toString(idCoreFacility));
        poNode.setAttribute("submitDate", submitDate);
        poNode.setAttribute("codeProductType", codeProductType);
        poNode.setAttribute("codeProductOrderStatus", codeProductOrderStatus);
        poNode.setAttribute("quoteNumber", quoteNumber);
        poNode.setAttribute("quoteReceivedDate", quoteReceivedDate);
        poNode.setAttribute("uuid", uuid);
        
        // Add line items
        List<Object[]> lineItemRows = lineItemMap.get(idProductOrder);
        appendLineItemInfo(poNode, lineItemRows);
        
        // Add files
        List<Object[]> fileRows = filesMap.get(idProductOrder);
        appendFileInfo(poNode, fileRows);
        
        doc.getRootElement().addContent(poNode);
      }
      

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetProductOrderLineItemList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetProductOrderLineItemList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetProductOrderLineItemList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }


  private void hashLineItems(Session sess) {
    lineItemMap = new HashMap<Integer, List<Object[]>>();
    
    StringBuffer queryBuf = productOrderFilter.getLineItemQuery(this.getSecAdvisor());
    List<Object[]> lineItemRows = sess.createQuery(queryBuf.toString()).list();
    
    if (lineItemRows != null) {
      for (Object[] lineItemRow : lineItemRows) {
        Integer idProductOrder        = (Integer)lineItemRow[0];
        List<Object[]> theLineItemRows = lineItemMap.get(idProductOrder);
        if (theLineItemRows == null) {
          theLineItemRows = new ArrayList<Object[]>();
          lineItemMap.put(idProductOrder, theLineItemRows);
        }
        theLineItemRows.add(lineItemRow);
      }
    }    
  }
  
  private void appendLineItemInfo(Element node, List<Object[]> lineItemRows) throws XMLReflectException {
    
    if (node != null) {
      if (lineItemRows != null) {
        for(Iterator i1 = lineItemRows.iterator(); i1.hasNext();) {
          Object[] lineItemRow = (Object[])i1.next();
          Integer idProductOrder        = (Integer)lineItemRow[0];
          Integer qty                   = (Integer)lineItemRow[1];
          BigDecimal unitPrice          = (BigDecimal)lineItemRow[2];
          Product product               = (Product)lineItemRow[3];
          Element productNode = product.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
          
          // Add line item details
          productNode.setAttribute( "nodeDisplay", "Line Item" );
          productNode.setAttribute( "qty", qty != null ? qty.toString() : "" );
          productNode.setAttribute( "unitPrice", unitPrice != null ? "$" + unitPrice.toString() : "" );
          // Calculate total price for that product
          BigDecimal totalPrice = unitPrice != null ? unitPrice.multiply(new BigDecimal(qty)) : new BigDecimal(0);
          totalPrice.setScale( 2 );
          productNode.setAttribute( "totalPriceDisplay", "$" + totalPrice.toString() );
          // Add product node to product order
          node.addContent(productNode);
          // Add billing items for that product
          List<Object[]> billingItemRows = billingItemMap.get(idProductOrder);
          appendBillingItemInfo(productNode, billingItemRows);
        }
      }
    }
    
  }
  
  private void hashBillingItems(Session sess) {
    billingItemMap = new HashMap<Integer, List<Object[]>>();
    
    StringBuffer queryBuf = productOrderFilter.getBillingItemQuery(this.getSecAdvisor());
    List<Object[]> billingItemRows = sess.createQuery(queryBuf.toString()).list();
    
    if (billingItemRows != null) {
      for (Object[] billingItemRow : billingItemRows) {
        Integer idProductOrder        = (Integer)billingItemRow[0];
        List<Object[]> theBillingItemRows = billingItemMap.get(idProductOrder);
        if (theBillingItemRows == null) {
          theBillingItemRows = new ArrayList<Object[]>();
          billingItemMap.put(idProductOrder, theBillingItemRows);
        }
        theBillingItemRows.add(billingItemRow);
      }
    }    
  }
  
  private void appendBillingItemInfo(Element node, List<Object[]> billingItemRows) throws XMLReflectException {
    
    if (node != null) {
      if (billingItemRows != null) {
        for(Iterator i1 = billingItemRows.iterator(); i1.hasNext();) {
          Object[] billingItemRow = (Object[])i1.next();
//          Integer idProductOrder        = (Integer)billingItemRow[0];
          BillingItem bi                = (BillingItem)billingItemRow[1];
          BillingAccount ba             = (BillingAccount)billingItemRow[2];
          // Match the billing items to the particular product
          if ( bi.getIdPrice().toString().equals( node.getAttributeValue( "idPrice" ) ) ) {
            Element billingItemNode = bi.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
            billingItemNode.setAttribute("billingAccountName", ba.getAccountNameDisplay() );
            billingItemNode.setAttribute( "nodeDisplay", "Billing Item" );
            // Add the billing item node to the product node
            node.addContent(billingItemNode);
          }
        }
      }
    }
    
  }
  
  private void hashFiles(Session sess) {
    filesMap = new HashMap<Integer, List<Object[]>>();
    
    StringBuffer queryBuf = productOrderFilter.getFilesQuery(this.getSecAdvisor());
    List<Object[]> fileRows = sess.createQuery(queryBuf.toString()).list();
    
    if (fileRows != null) {
      for (Object[] fileRow : fileRows) {
        Integer idProductOrder        = (Integer)fileRow[0];
        List<Object[]> theFileRows = filesMap.get(idProductOrder);
        if (theFileRows == null) {
          theFileRows = new ArrayList<Object[]>();
          filesMap.put(idProductOrder, theFileRows);
        }
        theFileRows.add(fileRow);
      }
    }    
  }
  
  private void appendFileInfo(Element node, List<Object[]> lineItemRows) throws XMLReflectException {
    
    if (node != null) {
      if (lineItemRows != null) {
        for(Iterator i1 = lineItemRows.iterator(); i1.hasNext();) {
          Object[] lineItemRow = (Object[])i1.next();
          Integer idProductOrder        = (Integer)lineItemRow[0];
          ProductOrderFile file         = (ProductOrderFile)lineItemRow[1];
          Element fileNode = file.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
          fileNode.setAttribute( "nodeDisplay", "File");
          fileNode.setAttribute( "display", file.getFileName() != null ? file.getFileName() : "" );
          // Add file node to product order
          node.addContent(fileNode);
         
        }
      }
    }
    
  }
  
  public ProductOrderFilter getProductOrderFilter() {
    return productOrderFilter;
  }


  public void setProductOrderFilter(ProductOrderFilter billingItemFilter) {
    this.productOrderFilter = billingItemFilter;
  }

  private String toString(Object theValue) {
    if (theValue != null) {
      return theValue.toString();
    }
    return "";
  }
  
}