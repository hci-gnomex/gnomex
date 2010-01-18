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
import java.sql.SQLException;
import java.util.Iterator;
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
            maindiv.addContent(printTable);

            Element center1 = new Element("CENTER");
            maindiv.addContent(center1);

            Element h2 = new Element("H2");
            h2.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request");
            center1.addContent(h2);
            
            Element h3 = new Element("H4");
            h3.addContent(this.formatDate(request.getCreateDate()));
            center1.addContent(h3);
            
            
            
            
            maindiv.addContent(formatter.makeRequestTable());

            maindiv.addContent(formatter.makeSampleTable(request.getSamples()));
            
            maindiv.addContent(new Element("BR"));

            if (!request.getHybridizations().isEmpty()) {
              TreeSet labeledSamples = new TreeSet(new LabeledSampleNumberComparator());
              labeledSamples.addAll(request.getLabeledSamples());
              maindiv.addContent(formatter.makeLabeledSampleTable(labeledSamples));
              
              maindiv.addContent(new Element("BR"));
              maindiv.addContent(formatter.makeHybTable(request.getHybridizations()));          
            }

            if (!request.getSequenceLanes().isEmpty()) {
              maindiv.addContent(formatter.makeSequenceLaneTable(request.getSequenceLanes()));          
            }
            
            maindiv.addContent(new Element("BR"));
            maindiv.addContent(new Element("BR"));
            maindiv.addContent(new Element("HR"));
                       
            Element pb = new Element("P");
            pb.setAttribute("CLASS", "break");
            maindiv.addContent(pb);
            maindiv.addContent("SUBMISSION_INSTRUCTIONS_GO_HERE");
            String instructions = this.getInstructions();
            // Convert degree and micro symbols to html escape codes
            instructions = instructions.replaceAll("\\xB0", "&#176;");
            instructions = instructions.replaceAll("\\xB5", "&#181;");

            
            
          
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
    Document inputDoc = parser.build(new FileInputStream(Constants.WEBCONTEXT_DIR + "doc/" + getSubmissionInstructionsFileName()));
    
    Document tempDoc = new Document(new Element("div"));
    for(Iterator i = inputDoc.getRootElement().getChildren("body").iterator(); i.hasNext();) {
      Element body = (Element)i.next();
      for(Iterator i1 = body.getChildren().iterator(); i1.hasNext();) {
          Element node = (Element)i1.next();
          tempDoc.getRootElement().addContent((Element)node.clone());
      }
    }
    
    return out.outputString(tempDoc.getRootElement());
  }

  private String getSubmissionInstructionsFileName() {
    String fileName = "";
    if (this.request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      if (request.getIdSampleTypeDefault() != null) {
        if (dictionaryHelper.getSampleType(this.request.getIdSampleTypeDefault()).toUpperCase().indexOf("CHIP") >= 0 ||
            dictionaryHelper.getSampleType(this.request.getIdSampleTypeDefault()).toUpperCase().indexOf("CHROMATIN IP") >= 0) {
          fileName = "submission_instructions_illumina_chipseq.html";
        } else if (dictionaryHelper.getSampleType(this.request.getIdSampleTypeDefault()).toUpperCase().indexOf("DNA") >= 0) {
          fileName = "submission_instructions_illumina_genomic_dna.html";
        } else if (dictionaryHelper.getSampleType(this.request.getIdSampleTypeDefault()).toUpperCase().indexOf("SMALL RNA") >= 0) {
          fileName = "submission_instructions_illumina_smallrna_seq.html";
        } else if (dictionaryHelper.getSampleType(this.request.getIdSampleTypeDefault()).toUpperCase().indexOf("RNA") >= 0) {
          fileName = "submission_instructions_illumina_mrna_seq.html";
        }
      } 
    } else if (this.request.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      if (request.getCodeBioanalyzerChipType() != null && !request.getCodeBioanalyzerChipType().equals("")) {
        if (request.getCodeBioanalyzerChipType().equals(BioanalyzerChipType.DNA1000)) {
          fileName = "submission_instructions_agilent_bioanalyzer_dna_1000_chip.html";
        } else if (request.getCodeBioanalyzerChipType().equals(BioanalyzerChipType.RNA_NANO)) {
          fileName = "submission_instructions_agilent_bioanalyzer_rna_nano_chip.html";
        } else if (request.getCodeBioanalyzerChipType().equals(BioanalyzerChipType.RNA_PICO)) {
          fileName = "submission_instructions_agilent_bioanalyzer_rna_pico_chip.html";
        } 
      } 
    } else if (this.request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
      if (request.getCodeApplication() != null) {
        if (request.getCodeApplication().equals(Application.EXPRESSION_MICROARRAY_CATEGORY)) {
          fileName = "submission_instructions_affy_ge_microarray.html";
        } else if (request.getCodeApplication().equals(Application.SNP_MICROARRAY_CATEGORY)) {
          if (request.getIdSlideProduct() != null) {
              if (dictionaryHelper.getSlideProductName(request.getIdSlideProduct()).toUpperCase().indexOf("5.0") >= 0) {
                fileName = "submission_instructions_affy_5.0_6.0_snp.html";
              } else if (dictionaryHelper.getSlideProductName(request.getIdSlideProduct()).toUpperCase().indexOf("5.0") >= 0) {
                fileName = "submission_instructions_affy_5.0_6.0_snp.html";
              } else {
                fileName = "submission_instructions_affy_250k_snp.html";
              }
          }
        } 
      }
    } else if (this.request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY) ||
                this.request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY)) {
      if (request.getCodeApplication() != null) {
        if (request.getCodeApplication().equals(Application.EXPRESSION_MICROARRAY_CATEGORY)) {
          fileName = "submission_instructions_agilent_ge_microarray.html";
        } else if (request.getCodeApplication().equals(Application.CGH_MICROARRAY_CATEGORY)) {
          fileName = "submission_instructions_agilent_cgh_microarray.html";
        } else if (request.getCodeApplication().equals(Application.CHIP_ON_CHIP_MICROARRAY_CATEGORY)) {
          fileName = "submission_instructions_agilent_chip_on_chip_microarray.html";
        } else if (request.getCodeApplication().equals(Application.MIRNA_MICROARRAY_CATEGORY)) {
          fileName = "submission_instructions_agilent_mirna_microarray.html";
        } 
      }
    }
    return fileName;
  }
}