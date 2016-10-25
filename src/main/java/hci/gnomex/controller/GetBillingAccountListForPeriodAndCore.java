package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Invoice;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;
public class GetBillingAccountListForPeriodAndCore extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(GetBillingAccountListForPeriodAndCore.class);
  
  private Integer idBillingPeriod;
  private Integer idCoreFacility;
  private Integer idLab;
  
  @Override
  public void validate() {
  }
  
  @Override
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idBillingPeriod") != null && request.getParameter("idBillingPeriod").length() > 0) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }
    if (request.getParameter("idCoreFacility") != null && request.getParameter("idBillingPeriod").length() > 0) {
      idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
    } else {
      this.addInvalidField("idCoreFacility", "idCoreFacility is required");
    }
    if (request.getParameter("idLab") != null && request.getParameter("idLab").length() > 0) {
      idLab = new Integer(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "idLab is required");
    }

  }

  @Override
  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      ArrayList<String> statuses = new ArrayList<String>();
      statuses.add(BillingStatus.COMPLETED);
      statuses.add(BillingStatus.APPROVED);
      statuses.add(BillingStatus.APPROVED_CC);
      statuses.add(BillingStatus.APPROVED_PO);
      String queryString = "select distinct ba from BillingItem bi join bi.billingAccount ba where bi.idCoreFacility=:idCoreFacility and bi.idBillingPeriod=:idBillingPeriod and bi.idLab=:idLab and bi.codeBillingStatus in (:statuses)";
      Query query = sess.createQuery(queryString);
      query.setParameter("idCoreFacility", idCoreFacility);
      query.setParameter("idBillingPeriod", idBillingPeriod);
      query.setParameter("idLab", idLab);
      query.setParameterList("statuses", statuses);
      List<BillingAccount> accts = (List<BillingAccount>)query.list();
      Document doc = new Document(new Element("BillingAccountList"));
      for(BillingAccount acct : accts) {
        Element baNode = acct.toXMLDocument(null, GNomExCommand.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(baNode);
      }
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      LOG.error("An exception has occurred in GetBillingAccountListForPeriodAndCore ", e);

      throw new RollBackCommandException(e.getMessage());        
    }catch (SQLException e) {
      LOG.error("An exception has occurred in GetBillingAccountListForPeriodAndCore ", e);

      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetBillingAccountListForPeriodAndCore ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in GetBillingAccountListForPeriodAndCore ", e);
      }
    }
      
    return this;
  }
}
