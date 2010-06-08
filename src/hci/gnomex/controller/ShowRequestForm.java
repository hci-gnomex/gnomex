package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BioanalyzerChipType;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Application;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SubmissionInstruction;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabeledSampleNumberComparator;
import hci.gnomex.utility.RequestHTMLFormatter;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class ShowRequestForm extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idRequest;
  private Request          request;
  
  private String           amendState = "";
  
  private String           appURL = "";
  
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
    if (request.getParameter("amendState") != null && !request.getParameter("amendState").equals("")) {
      amendState = request.getParameter("amendState");
    }
    try {
      this.appURL = this.getAppURL(request);
    } catch (Exception e) {
      log.warn("Unable to obtain gnomex app url", e);
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
            link.setAttribute("href", Constants.REQUEST_FORM_CSS);
            head.addContent(link);
            
            Element title = new Element("TITLE");
            title.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber());
            head.addContent(title);
            
            Element body = new Element("BODY");
            root.addContent(body);

            Element maindiv = new Element("DIV");
            maindiv.setAttribute("id", "container");
            body.addContent(maindiv);
            //Element center = new Element("CENTER");
            //body.addContent(center);
            
            Element col1 = new Element("DIV");
            col1.setAttribute("id", "printLinkCol");
            maindiv.addContent(col1);

            // 'Print this page' link
            Element printLink = new Element("A");
            printLink.setAttribute("HREF", "javascript:window.print()");
            printLink.addContent("Print page");
            col1.addContent(printLink);
            
            
            Element ftr = new Element("DIV");
            ftr.setAttribute("id", "footer");            
            maindiv.addContent(ftr);
            
           

            Element h2 = new Element("H2");
            h2.addContent(formatter.makeRequestCategoryImage(null));
            h2.addContent(request.getNumber() + "&nbsp;&nbsp;&nbsp;");
            h2.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request");
            maindiv.addContent(h2);
            
      

            if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
              Element hApp = new Element("H4");
              hApp.addContent(dictionaryHelper.getApplication(request.getCodeApplication()));
              maindiv.addContent(hApp);

              if (request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
                
                for(Iterator i = request.getSeqLibTreatments().iterator(); i.hasNext();) {
                  SeqLibTreatment t = (SeqLibTreatment)i.next();
                  Element hTreatment = new Element("H4");
                  hTreatment.addContent(t.getSeqLibTreatment());
                  maindiv.addContent(hTreatment);                  
                
                }
              }

            }
            
            
            maindiv.addContent(new Element("BR"));
            maindiv.addContent(formatter.makeRequestTable());

            maindiv.addContent(formatter.makeSampleTable(request.getSamples()));
            
            
            if (!request.getHybridizations().isEmpty()) {

              formatter.makePageBreak(maindiv);
              
              TreeSet labeledSamples = new TreeSet(new LabeledSampleNumberComparator());
              labeledSamples.addAll(request.getLabeledSamples());
              formatter.makeLabeledSampleTable(maindiv, labeledSamples);
              
              formatter.makePageBreak(maindiv);

              maindiv.addContent(formatter.makeHybTable(request.getHybridizations()));          
            }

            if (!request.getSequenceLanes().isEmpty()) {
              formatter.makePageBreak(maindiv);
              
              formatter.addSequenceLaneTable(maindiv, request.getSequenceLanes(), amendState);          
            }
            
            
                       
            String instructions = this.getInstructions();
            if (instructions != null) {
              maindiv.addContent(new Element("BR"));
              formatter.makePageBreak(maindiv);
              
              
              maindiv.addContent("SUBMISSION_INSTRUCTIONS_GO_HERE");
              // Convert degree and micro symbols to html escape codes
              instructions = instructions.replaceAll("\\xB0", "&#176;");
              instructions = instructions.replaceAll("\\xB5", "&#181;");              
            }
          
            XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setOmitEncoding(true);
            this.xmlResult = out.outputString(doc);
            this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
            this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");
            // Add the submission instructions to the end of the request
            // form.
            this.xmlResult = this.xmlResult.replaceAll("SUBMISSION_INSTRUCTIONS_GO_HERE", instructions);

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
 
  private String getInstructions() throws Exception {
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    
    SAXBuilder parser = new SAXBuilder();
    parser.setValidation(false);
    String instructionURL = this.getSubmissionInstructionsUrl();
    if (instructionURL == null) {
      return null;
    }

    Document instructionDoc = null;
    if (instructionURL.toLowerCase().startsWith("http")) {
      // We load the instructions document from a web site
      try {
        URL url = new URL(instructionURL);
        instructionDoc = parser.build(url.openStream());  
     } catch (Exception e) {
       log.warn("Unable to parse submission instructions " + instructionURL, e);
     }
    } else {
      // We load the doc directly from the app root
      instructionDoc = parser.build(new FileInputStream(Constants.WEBCONTEXT_DIR + instructionURL));
    }
    
    if (instructionDoc != null) {                  
      Document tempDoc = new Document(new Element("div"));
      for(Iterator i = instructionDoc.getRootElement().getChildren("body").iterator(); i.hasNext();) {
        Element body = (Element)i.next();
        for(Iterator i1 = body.getChildren().iterator(); i1.hasNext();) {
            Element node = (Element)i1.next();
            tempDoc.getRootElement().addContent((Element)node.clone());
        }
      }
      
      return out.outputString(tempDoc.getRootElement());
    } else {
      return null;
    }
    
  }

  @SuppressWarnings("unchecked")
  private String getSubmissionInstructionsUrl() {
    TreeMap<Integer, List<SubmissionInstruction>> matchMap = new TreeMap<Integer, List<SubmissionInstruction>>();
    
    for(Iterator i = dictionaryHelper.getSubmissionInstructionMap().keySet().iterator(); i.hasNext();) {
      int matchCount = 0;
      Integer idSubmissionInstruction = (Integer)i.next();
      SubmissionInstruction si = (SubmissionInstruction)dictionaryHelper.getSubmissionInstructionMap().get(idSubmissionInstruction);
      
      if (si.getCodeRequestCategory().equals(this.request.getCodeRequestCategory())) {
        matchCount++;
      } else {
        // Bypass submission instruction if the request category (required) 
        // doesn't match
        continue;
      }
      
      if (si.getCodeApplication() != null) {
        if (this.request.getCodeApplication() != null && 
            si.getCodeApplication().equals(this.request.getCodeApplication())) {
          matchCount++;
        } else {
          // Bypass submission instruction if the codeApplication is provided 
          // and it doesn't match the request
          continue;
        }
      }
          
      if (si.getCodeBioanalyzerChipType() != null) {
        if (this.request.getCodeBioanalyzerChipType() != null && 
            si.getCodeBioanalyzerChipType().equals(this.request.getCodeBioanalyzerChipType())) {
          matchCount++;
        } else {
          //Bypass submission instruction if the codeBioanalyzerChipType is provided 
          // and it doesn't match the request
          continue;
        }
      }

      if (si.getIdBillingSlideServiceClass() != null) {
        if (this.request.getSlideProduct() != null && 
            this.request.getSlideProduct().getIdBillingSlideServiceClass() != null && 
            si.getIdBillingSlideServiceClass().equals(this.request.getSlideProduct().getIdBillingSlideServiceClass())) {
          matchCount++;
        } else {
          //Bypass submission instruction if the idBillingSlideServiceClass is provided 
          // and it doesn't match the request
          continue;
        }
      }
      Integer key = new Integer(matchCount);
      List<SubmissionInstruction> instructions = matchMap.get(key);
      if (instructions == null) {
        instructions = new ArrayList<SubmissionInstruction>();
      }
      instructions.add(si);
      matchMap.put(key, instructions);
    }
    
    // Find the submission instructions with the highest match count.
    // If multiple submission instructions have the same match
    // count, return null because we don't have an exact match.
    if (matchMap.size() > 0) {
      Integer maxMatchCount = (Integer)matchMap.descendingKeySet().iterator().next();
      List<SubmissionInstruction> instructions = (List<SubmissionInstruction>)matchMap.get(maxMatchCount);
      if (maxMatchCount.intValue() > 0 && instructions != null && instructions.size() == 1) {
        SubmissionInstruction instruction = instructions.get(0);
        return instruction.getUrl();
      } else {
        log.warn("Cannot find exact matching Submission Instructions for request " + request.getNumber());
        return null;
      }      
    } else {
      log.warn("No matching Submission Instructions for request " + request.getNumber());
      return null;
    }
    
  }
}