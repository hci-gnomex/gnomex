package hci.gnomex.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductType;
import hci.gnomex.utility.DictionaryHelper;


public class GetProductList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductList.class);


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

      List products = sess.createQuery("SELECT p from Product p order by p.name").list();

      for(Iterator i = products.iterator(); i.hasNext();) {
        Product product = (Product)i.next();
        this.getSecAdvisor().flagPermissions(product);
        Element node = product.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

        ProductType pt = dictionaryHelper.getProductTypeObject(product.getIdProductType());
        Price price = getProductPrice( sess, product, pt );

        node.setAttribute( "unitPriceInternal", price != null ? price.getUnitPrice().toString() : "");
        node.setAttribute( "unitPriceExternalAcademic", price != null ? price.getUnitPriceExternalAcademic().toString() : "");
        node.setAttribute( "unitPriceExternalCommercial", price != null ? price.getUnitPriceExternalCommercial().toString() : "");

        doc.getRootElement().addContent(node);
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetProductList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetProductList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetProductList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetProductList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch(Exception e) {

      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

    return this;
  }

  public static Price getProductPrice(Session sess, Product product, ProductType pt) {

    if ( product == null || pt == null ) {
      return null;
    }

    String priceQuery;

    if ( product.getIdPrice() != null ) {
      priceQuery = "SELECT p from Price as p where p.idPrice=" + product.getIdPrice();
    } else {
      priceQuery = "SELECT p from Price as p where p.idPriceCategory=" + pt.getIdPriceCategory() + "       AND p.name='" + product.getName() + "'";
    }
    Price price = (Price) sess.createQuery( priceQuery ).uniqueResult();

    return price;
  }

}