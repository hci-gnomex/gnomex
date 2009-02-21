package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestHTMLFormatter;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class ShowRequestForm extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idRequest;
  private Request          request;
  
  private AppUser          appUser;
  private BillingAccount   billingAccount;
  private Project          project;
  private Lab              lab;
  
  private DictionaryHelper dictionaryHelper;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
     
      dictionaryHelper = DictionaryHelper.getInstance(sess);
    
      request = (Request)sess.get(Request.class, idRequest);
      if (request == null) {
        this.addInvalidField("no request", "Request not found");
      }
      

      if (this.isValid()) {
        if (this.getSecAdvisor().canRead(request)) { 
          if (request.getIdAppUser() != null) {
            appUser = (AppUser)sess.get(AppUser.class, request.getIdAppUser());        
          }
          if (request.getIdBillingAccount() != null) {
            billingAccount = (BillingAccount)sess.get(BillingAccount.class, request.getIdBillingAccount());
          }
          
          RequestHTMLFormatter formatter = new RequestHTMLFormatter(request, appUser, billingAccount, dictionaryHelper);
          
          if (this.getSecAdvisor().canRead(request)) {
            
            Element root = new Element("HTML");
            Document doc = new Document(root);
            
            Element head = new Element("HEAD");
            root.addContent(head);
            
            Element link = new Element("link");
            link.setAttribute("rel", "stylesheet");
            link.setAttribute("type", "text/css");
            link.setAttribute("href", "requestForm.css");
            head.addContent(link);
            
            Element title = new Element("TITLE");
            title.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber());
            head.addContent(title);
            
            Element body = new Element("BODY");
            root.addContent(body);

            Element center = new Element("CENTER");
            body.addContent(center);
            
            
            // 'Print this page' link
            Element printLink = new Element("A");
            printLink.setAttribute("HREF", "javascript:window.print()");
            printLink.addContent("Print page");
            Element printTable = new Element("TABLE");   
            Element row = new Element("TR");
            Element cell = new Element("TD");
            cell.setAttribute("ALIGN", "RIGHT");
            printTable.addContent(row);
            row.addContent(cell);
            cell.addContent(printLink);
            center.addContent(printTable);
            
            Element h3 = new Element("H3");
            h3.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request");
            center.addContent(h3);
            
            Element center1 = new Element("CENTER");
            body.addContent(center1);
            
            
            Element h2 = new Element("H2");
            h2.addContent(this.formatDate(request.getCreateDate()));
            center1.addContent(h2);
            
            
            
            
            center1.addContent(formatter.makeRequestTable());

            center1.addContent(formatter.makeSampleTable(request.getSamples()));
            
            center1.addContent(new Element("BR"));

            if (!request.getHybridizations().isEmpty()) {
              center1.addContent(formatter.makeHybTable(request.getHybridizations()));          
            }

            if (!request.getSequenceLanes().isEmpty()) {
              center1.addContent(formatter.makeSequenceLaneTable(request.getSequenceLanes()));          
            }

            
          
            XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setOmitEncoding(true);
            this.xmlResult = out.outputString(doc);
            this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
            this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");
          } else {
            this.addInvalidField("Insufficient Permission", "Insufficient permission to access this request");      
          }
          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to read request.");
        }
        
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowRequestForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowRequestForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowRequestForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowRequestForm ", e);
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
  

  /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    return response;
  } 
 

  
}