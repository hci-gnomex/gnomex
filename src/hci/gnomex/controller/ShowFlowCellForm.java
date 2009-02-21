package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FlowCellHTMLFormatter;

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


public class ShowFlowCellForm extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowFlowCellForm.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idFlowCell;
  private FlowCell          flowCell;
  
  private DictionaryHelper dictionaryHelper;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idFlowCell") != null) {
      idFlowCell = new Integer(request.getParameter("idFlowCell"));
    } else {
      this.addInvalidField("idFlowCell", "idFlowCell is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
     
      dictionaryHelper = DictionaryHelper.getInstance(sess);
    
      flowCell = (FlowCell)sess.get(FlowCell.class, idFlowCell);
      if (flowCell == null) {
        this.addInvalidField("no flow cell", "Flow Cell not found");
      }
      

      if (this.isValid()) {
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) { 
          
          FlowCellHTMLFormatter formatter = new FlowCellHTMLFormatter(flowCell, dictionaryHelper);

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
            title.addContent("Flow Cell Run Report - " + flowCell.getNumber());
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
            h3.addContent("Flow Cell Run Report - " + flowCell.getNumber());
            center.addContent(h3);

            Element center1 = new Element("CENTER");
            body.addContent(center1);
            
            
;
            
            center1.addContent(formatter.makeFlowCellTable());

            
            Element center2 = new Element("CENTER");
            body.addContent(center2);
            
            if (!flowCell.getFlowCellChannels().isEmpty()) {
              center2.addContent(formatter.makeFlowCellChannelTable(flowCell.getFlowCellChannels()));          
            }

            
          
            XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setOmitEncoding(true);
            this.xmlResult = out.outputString(doc);
            this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
            this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");
          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show flow cell report.");
        }
        
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowFlowCellForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowFlowCellForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowFlowCellForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowFlowCellForm ", e);
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