package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;


public class GetHiSeqRunTypePriceList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetHiSeqRunTypePriceList.class);
  
  private Integer idLab;
  private String codeRequestCategory;

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab =  new Integer(request.getParameter("idLab"));
    } else {
      this.addInvalidField("IdLab", "IdLab required");
    }

    
    if (request.getParameter("codeRequestCategory") != null && !request.getParameter("codeRequestCategory").equals("")) {
      codeRequestCategory =  request.getParameter("codeRequestCategory");
    } else {
      this.addInvalidField("codeRequestCategory", "codeRequestCategory required");
    }
    
  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      Lab lab = (Lab)sess.load(Lab.class, this.idLab);
      
      String queryString = 
          "select p, crit " +
          " from PriceSheet ps " +
          " join ps.requestCategories rc " +
          " join ps.priceCategories pc " +
          " join pc.priceCategory.prices p " +
          " join p.priceCriterias crit " +
          " where rc.codeRequestCategory = :codeRequestCategory " +
          "   and pc.priceCategory.pluginClassName='hci.gnomex.billing.illuminaseqplugin'";
      Query query = sess.createQuery(queryString);
      query.setParameter("codeRequestCategory", this.codeRequestCategory);
      
      List rows = query.list();
      
      Document doc = new Document(new Element("HiSeqRunTypePriceList"));
      for(Iterator i = rows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        Price price = (Price)row[0];
        PriceCriteria criteria = (PriceCriteria)row[1];
        Element node = new Element("Price");

        node.setAttribute("idNumberSequencingCyclesAllowed", toString(criteria.getFilter1()));
        node.setAttribute("price", toString(price.getEffectiveUnitPrice(lab)));

        doc.getRootElement().addContent(node);
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

    }catch (Exception e) {
      LOG.error("An exception has occurred in HiSeqRunTypePriceList ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;
      } catch(Exception e) {
        LOG.error("An exception has occurred in HiSeqRunTypePriceList ", e);
      }
    }

    return this;
  }

  public void validate() {
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
  }
  
  private String toString(Object theValue) {
    if (theValue != null) {
      return theValue.toString();
    } 
    return "";
  }

}