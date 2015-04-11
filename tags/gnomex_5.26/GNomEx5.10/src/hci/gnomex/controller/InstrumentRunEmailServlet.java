package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.hibernate.Session;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class InstrumentRunEmailServlet extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteInstrumentRuns.class);
  private String subject      = "GNomEx Instrument Run Announcement";
  private StringBuffer body   = null;
  private String format       = "text";
  private Document selectedRunsDoc;
  private String runsSelectedXMLString;

  public void loadCommand(HttpServletRequest req, HttpSession res) {
    if (req.getParameter("body") != null && !req.getParameter("body").equals("")) { 
      body = new StringBuffer(req.getParameter("body"));
    }

    if (req.getParameter("subject") != null && !req.getParameter("subject").equals("")) {
      subject = req.getParameter("subject");
    }
    if (req.getParameter("format") != null && !req.getParameter("format").equals("")) {
      format = req.getParameter("format");
    }

    if (req.getParameter("runsSelectedXMLString") != null && !req.getParameter("runsSelectedXMLString").equals("")) {
      runsSelectedXMLString = req.getParameter("runsSelectedXMLString");
      StringReader reader = new StringReader(runsSelectedXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        selectedRunsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse runsSelectedXMLString", je );
        this.addInvalidField( "runsSelectedXMLString", "Invalid runsSelectedXMLString");

      }
    } 

  }

  public void validate() {

  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
     // DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      Integer idCoreFacility = DictionaryHelper.getIdCoreFacilityDNASeq();
      String senderAddress = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

      if(senderAddress == null){
        this.addInvalidField("Invalid email address", "Please  check the email address you are sending from");
        setResponsePage(this.ERROR_JSP);
      }
      else if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
        //Create where clause of hql statement for submitter's emails.
        String queryPart = " where ir.idInstrumentRun= ";

        for(Iterator i = this.selectedRunsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
          Element node = (Element)i.next();
          queryPart += node.getText();
          if(i.hasNext()){
            queryPart += " or ir.idInstrumentRun= ";
          }
        } 

        // Get a list of all active users with email accounts
        List idRequests = sess.createQuery("SELECT distinct pws.idRequest from Plate as p Left Join p.plateWells as pws Left Join p.instrumentRun as ir " + queryPart).list();
        idRequests.remove(null);
        
        List<String> appUserEmail = new ArrayList<String>();

        if (body != null && body.length() > 0) {

          for (Iterator i = idRequests.iterator(); i.hasNext();) {
            Request request = (Request)sess.load(Request.class, (Integer)i.next());
            AppUser appUser = request.getAppUser();
            String recipientAddress = appUser.getEmail();
            if(recipientAddress == null || recipientAddress.equals("") || appUserEmail.contains(recipientAddress)){
              continue;
            }
            else{
              appUserEmail.add(recipientAddress);
            }

            String theSubject = subject;

            // Email app user //CHANGE BACK TO recipientAddress after testing!!!!!!!!!!!!!!
            MailUtil.send(recipientAddress, 
                null,
                senderAddress,
                theSubject,
                body.toString(),
                format.equalsIgnoreCase("HTML") ? true : false); 
          }
        }
        setResponsePage(this.SUCCESS_JSP);
      }
      else{
        this.addInvalidField("Insufficient permissions", "Insufficient permission to edit dictionaries.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }  finally {
      try {
        HibernateSession.closeSession();        
      } catch (Exception e1) {
        System.out.println("InstrumentRunEmailServlet warning - cannot close hibernate session");
      }
    }
    return this;
  }

}
