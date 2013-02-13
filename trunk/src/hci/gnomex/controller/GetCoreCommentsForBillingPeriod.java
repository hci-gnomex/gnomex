package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
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



public class GetCoreCommentsForBillingPeriod extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetCoreCommentsForBillingPeriod.class);

  private BillingItemFilter billingItemFilter;
  
  private Integer                  idBillingPeriod;
  private Integer                  idCoreFacility;

  private SecurityAdvisor          secAdvisor;



  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingItemFilter = new BillingItemFilter(this.getSecAdvisor());
    HashMap errors = this.loadDetailObject(request, billingItemFilter);
    this.addInvalidFields(errors);
    
    /*if (request.getParameter("idBillingPeriod") != null) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }

    if (request.getParameter("idCoreFacility") != null) {
      idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
    } else {
      this.addInvalidField("idCoreFacility", "idCoreFacility is required");
    }

    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }*/


  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      

      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      BillingPeriod billingPeriod = dh.getBillingPeriod(idBillingPeriod);



      // Get all requests with non-blank core comments from the current billing period
//      StringBuffer queryBuf = this.getCommentQuery();
      StringBuffer queryBuf = billingItemFilter.getCoreCommentsQuery();

      List rows = sess.createQuery(queryBuf.toString()).list();
      
      Document doc = new Document(new Element("RequestList"));
      for(Iterator i = rows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();

        String number                   = (String)row[0];
        String name                     = (String)row[1];
        String corePrepInstructions    = (String)row[2];

        
        Element node = new Element("Request");

        node.setAttribute("name", toString(name));
        node.setAttribute("number", toString(number));
        node.setAttribute("corePrepInstructions", toString(corePrepInstructions));
        node.setAttribute("billingPeriod", toString(billingPeriod));

        doc.getRootElement().addContent(node);

      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetCoreCommentsForBillingPeriod ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (NamingException e){
      log.error("An exception has occurred in GetCoreCommentsForBillingPeriod ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetCoreCommentsForBillingPeriod ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } catch (Exception e) {
      log.error("An exception has occurred in GetCoreCommentsForBillingPeriod ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        secAdvisor.closeReadOnlyHibernateSession();    
      } catch(Exception e) {

      }
    }

    return this;
  }

  /*public StringBuffer getCommentQuery() {
    StringBuffer queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    //    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" req.name, ");
    //    queryBuf.append(" req.description, ");
    //    queryBuf.append(" req.idSampleDropOffLocation, ");
    //    queryBuf.append(" req.codeRequestStatus, ");
    //    queryBuf.append(" req.codeRequestCategory, ");
    //    queryBuf.append(" req.createDate, ");
    //    queryBuf.append(" submitter.firstName, ");
    //    queryBuf.append(" submitter.lastName, ");
    //    queryBuf.append(" lab.firstName, ");
    //    queryBuf.append(" lab.lastName, ");
    //    queryBuf.append(" req.idAppUser, ");
    //    queryBuf.append(" req.idLab, ");
    //    queryBuf.append(" req.idCoreFacility, ");
    queryBuf.append(" req.corePrepInstructions ");

    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" JOIN        bi.BillingPeriod as bp ");

    // Billing period
    queryBuf.append(" WHERE ");
    queryBuf.append(" bp.idBillingPeriod = ");
    queryBuf.append(idBillingPeriod);
    // Core Facility
    queryBuf.append(" AND ");
    queryBuf.append(" req.idCoreFacility = ");
    queryBuf.append(idCoreFacility);
    // Non-blank core facility notes
    queryBuf.append(" AND ");
    queryBuf.append(" req.corePrepInstructions != ''");

    queryBuf.append(" GROUP BY ");
    queryBuf.append(" req.number ");
    queryBuf.append(" order by number ");

    return queryBuf;
  }*/

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