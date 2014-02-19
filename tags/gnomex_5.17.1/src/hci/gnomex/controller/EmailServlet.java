package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class EmailServlet extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteInstrumentRuns.class);
  private String subject      = "";
  private StringBuffer body   = null;
  private String format       = "html";
  private Document selectedRunsDoc = null;
  private String runsSelectedXMLString;
  private Document selectedRequestsDoc;
  private String requestsXMLString = null;
  
  private String           serverName;

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

    if (req.getParameter("requestsXMLString") != null && !req.getParameter("requestsXMLString").equals("")) {
      requestsXMLString = req.getParameter("requestsXMLString");
      StringReader reader = new StringReader(requestsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        selectedRequestsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse requestsXMLString", je );
        this.addInvalidField( "requestsXMLString", "Invalid requestsXMLString");
      }
    } 

    if ( selectedRunsDoc == null && selectedRequestsDoc == null ) {
      this.addInvalidField( "XMLString", "Run or Request XML required");
    }
    
    serverName = req.getServerName();
  }

  public void validate() {

  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      String senderAddress = dh.getAppUserObject( this.getSecAdvisor().getIdAppUser() ).getEmail() ;

      if(senderAddress == null){
        this.addInvalidField("Invalid email address", "Please  check the email address you are sending from");
        setResponsePage(this.ERROR_JSP);
      }
      else if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {

        List idRequests = new ArrayList();
        if ( selectedRunsDoc != null  ) {
          //Create where clause of hql statement for submitter's emails.
          String queryPart = " where ir.idInstrumentRun= ";

          for(Iterator i = this.selectedRunsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element)i.next();
            queryPart += node.getText();
            if(i.hasNext()){
              queryPart += " or ir.idInstrumentRun= ";
            }
          } 
          idRequests = sess.createQuery("SELECT distinct pws.idRequest from Plate as p Left Join p.plateWells as pws Left Join p.instrumentRun as ir " + queryPart).list();

        } else if ( selectedRequestsDoc != null ) {
          for(Iterator i = this.selectedRequestsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element)i.next();
            String nextId = node.getText();
            idRequests.add( Integer.parseInt( nextId ) );
          } 
        }
        idRequests.remove( null );
        
        List<String> appUserEmail = new ArrayList<String>();

        if (body != null && body.length() > 0) {
          if(!MailUtil.isValidEmail(senderAddress)){
            senderAddress = dh.getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
          }
          for (Iterator i = idRequests.iterator(); i.hasNext();) {
            Integer nextId = (Integer) i.next();
            Request request = (Request)sess.load(Request.class, nextId );
            AppUser appUser = request.getAppUser();
            String recipientAddress = appUser.getEmail();
            
            if(recipientAddress == null || recipientAddress.equals("") || appUserEmail.contains(recipientAddress) || !MailUtil.isValidEmail(recipientAddress)){
              continue;
            }

            appUserEmail.add(recipientAddress);

            String theSubject = subject;
            
            String emailInfo = "";
            if ( !dh.isProductionServer( serverName  )) {
              theSubject = subject + "  (TEST)";
              emailInfo = "[If this were a production environment then this email would have been sent to: " + recipientAddress + "]<br><br>";
              recipientAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
             }
            
            MailUtil.send(recipientAddress, 
                null,
                senderAddress,
                theSubject,
                emailInfo + body.toString(),
                format.equalsIgnoreCase("HTML") ? true : false); 
          }
        }
        setResponsePage(this.SUCCESS_JSP);
      }
      else{
        this.addInvalidField("Insufficient permissions", "Insufficient permission to send email.");
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
        System.out.println("EmailServlet warning - cannot close hibernate session");
      }
    }
    return this;
  }

}
