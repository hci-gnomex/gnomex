package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryType;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SubmissionInstruction;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.LabeledSampleNumberComparator;
import hci.gnomex.utility.RequestHTMLFormatter;
import hci.gnomex.utility.RequestParser;

import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowRequestForm.class);

  public String SUCCESS_JSP = "/getHTML.jsp";

  private Integer          idRequest;
  private Request          request;

  private String           amendState = "";

  private AppUser          appUser;
  private BillingAccount   billingAccount;

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

          RequestHTMLFormatter formatter = new RequestHTMLFormatter(this.getSecAdvisor(), request, appUser, billingAccount, dictionaryHelper);

          if (this.getSecAdvisor().canRead(request)) {

            Element root = new Element("HTML");
            Document doc = new Document(root);

            Element head = new Element("HEAD");
            root.addContent(head);

            Element link = new Element("link");
            link.setAttribute("rel", "stylesheet");
            link.setAttribute("type", "text/css");
            link.setAttribute("href", Constants.REQUEST_FORM_CSS);
            link.setAttribute("title", "standard");
            head.addContent(link);


            Element linkPrint = new Element("link");
            linkPrint.setAttribute("rel", "stylesheet");
            linkPrint.setAttribute("type", "text/css");
            linkPrint.setAttribute("href", Constants.REQUEST_FORM_PRINT_CSS);
            linkPrint.setAttribute("title", "print");
            head.addContent(linkPrint);


            Element linkPrintInstr = new Element("link");
            linkPrintInstr.setAttribute("rel", "stylesheet");
            linkPrintInstr.setAttribute("type", "text/css");
            linkPrintInstr.setAttribute("href", Constants.REQUEST_FORM_PRINT_INSTRUCTIONS_CSS);
            linkPrintInstr.setAttribute("title", "printAll");
            head.addContent(linkPrintInstr);            

            // We need the </script> ending element and standard
            // XML won't do this.  So we work around the problem
            // by just injecting the <SCRIPT> tag as string
            // the document produces a string.
            head.addContent("JAVASCRIPT_GOES_HERE");


            Element title = new Element("TITLE");
            title.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber());
            head.addContent(title);

            Element body = new Element("BODY");
            root.addContent(body);

            Element outerDiv = new Element("DIV");
            outerDiv.setAttribute("id", "container");
            body.addContent(outerDiv);

            Element maindiv = new Element("DIV");
            maindiv.setAttribute("id", "containerForm");
            outerDiv.addContent(maindiv);


            // Print links
            Element printColRight = new Element("DIV");
            printColRight.setAttribute("id", "printLinkColRight");
            maindiv.addContent(printColRight);

            Element printLink = new Element("A");
            printLink.setAttribute("HREF", "javascript:setPrintStyleSheet('print');window.print()");
            printLink.addContent("Print");
            printColRight.addContent(printLink);


            Element printColLeft = new Element("DIV");
            printColLeft.setAttribute("id", "printLinkColLeft");
            maindiv.addContent(printColLeft);
            Element printWithInstructionsLink = new Element("A");
            printWithInstructionsLink.setAttribute("HREF", "javascript:setPrintStyleSheet('printAll');window.print()");
            printWithInstructionsLink.addContent("Print all (with instructions)");
            printColLeft.addContent(printWithInstructionsLink);



            Element ftr = new Element("DIV");
            ftr.setAttribute("id", "footer");            
            maindiv.addContent(ftr);



            Element h2 = new Element("H2");
            h2.addContent(formatter.makeRequestCategoryImage(null));
            h2.addContent(request.getNumber() + "&nbsp;&nbsp;&nbsp;");
            h2.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + (request.getIsExternal() != null && request.getIsExternal().equals("Y") ? "" :  " Request"));
            maindiv.addContent(h2);

            if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
              Element he = new Element("H4");
              he.addContent("External Experiment");
              maindiv.addContent(he);              
            }
            
            // FORM HEADER
            // Application
            if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
                Element hApp = new Element("H4");
                hApp.addContent(dictionaryHelper.getApplication(request.getCodeApplication()));
                if ( request.getCodeApplication().equals("OTHER") && request.getApplicationNotes() != null && !request.getApplicationNotes().equals( "" ) ) {
                  hApp.addContent( " - " + request.getApplicationNotes() );
                }
                maindiv.addContent(hApp);
            } else if ( request.getCodeDNAPrepType() != null && !request.getCodeDNAPrepType().equals("")) {
              RequestCategory rc = dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory());
              RequestCategoryType rct = rc.getCategoryType();
              if ( rct.getCodeRequestCategoryType().equals( RequestCategoryType.TYPE_ISOLATION )) {
              Element hApp = new Element("H4");
              hApp.addContent(dictionaryHelper.getDNAPrepType(request.getCodeDNAPrepType()));
              maindiv.addContent(hApp);
              }
          } else if ( request.getCodeRNAPrepType() != null && !request.getCodeRNAPrepType().equals("")) {
            RequestCategory rc = dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory());
            RequestCategoryType rct = rc.getCategoryType();
            if ( rct.getCodeRequestCategoryType().equals( RequestCategoryType.TYPE_ISOLATION )) {
            Element hApp = new Element("H4");
            hApp.addContent(dictionaryHelper.getRNAPrepType(request.getCodeRNAPrepType()));
            maindiv.addContent(hApp);
            }
        }
            
            // Number of seq cycles and seq run type
            if (request.getSequenceLanes().iterator().hasNext() && (request.getIsExternal() != null && !request.getIsExternal().equals("Y"))) {
	            Element seqType = new Element("H4");
	            SequenceLane lane = (SequenceLane) request.getSequenceLanes().iterator().next();
	            seqType.addContent(lane.getIdNumberSequencingCycles()!= null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;" + "&nbsp;&nbsp;&nbsp;");
	            seqType.addContent(lane.getIdSeqRunType() != null ? "&nbsp;" + dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
	            maindiv.addContent(seqType);
            }
            // Sample Type
            if (request.getSamples().size() > 0) {
            	Sample s = (Sample)request.getSamples().iterator().next();
            	if (s.getIdSampleType() != null) {
            		String sampleTypeDisplay = dictionaryHelper.getSampleType(s.getIdSampleType());
                Element sampleTypeNode = new Element("H4");
                sampleTypeNode.addContent(sampleTypeDisplay);
                maindiv.addContent(sampleTypeNode);
            	}
            }
            
            if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
	            boolean corePrepLib = true;
             /* String steps = request.getApplication().getCoreSteps();
              String sampleType = "";
              String samplePrepMethod = "";*/
	            if (request.getSamples().iterator().hasNext()) {
                Sample smp = (Sample) request.getSamples().iterator().next(); 
                if (smp.getSeqPrepByCore() != null && smp.getSeqPrepByCore().equals("N")) {
                  corePrepLib = false;
                }
                if (!corePrepLib) {
                	Element preppedByUserNode = new Element("H4");
                	preppedByUserNode.setAttribute("class", "special");
                	if (request.getHasPrePooledLibraries() != null && request.getHasPrePooledLibraries().equals("Y") && request.getNumPrePooledTubes() != null) {
                    preppedByUserNode.addContent("Library Prepared By Client, " + request.getNumPrePooledTubes().toString() + " Pre-Pooled Tubes");
                	} else {
                    preppedByUserNode.addContent("Library Prepared By Client");
                	}
                	maindiv.addContent(preppedByUserNode);
                }
	            }
            }
            
            if (request.getSamples().size() > 0) {
              Sample s = (Sample)request.getSamples().iterator().next();
              if (s.getOtherSamplePrepMethod() != null && s.getOtherSamplePrepMethod().length() > 0) {
                Element othSamplePrepMethod = new Element("H4");
                othSamplePrepMethod.addContent("Sample Nucl. Extraction Method: " + s.getOtherSamplePrepMethod());
                maindiv.addContent(othSamplePrepMethod);
              }
            }
            
            
            if (request.getCaptureLibDesignId() != null && !request.getCaptureLibDesignId().equals("")){
              Element capLibDesign = new Element("H5");
              capLibDesign.addContent("Custom Design Id: " + request.getCaptureLibDesignId());
              capLibDesign.setAttribute("style", "text-align:center; color:black");
              maindiv.addContent(capLibDesign);
            }

            maindiv.addContent(new Element("BR"));
            maindiv.addContent(formatter.makeRequestTable()); //Header details

            maindiv.addContent(new Element ("BR"));

            // Show description for external experiments
            if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
              Element experimentNote = new Element("H5");
              experimentNote.addContent("Experiment Description");
              maindiv.addContent(experimentNote);

              Element description = new Element("H6");
              String desc = request.getDescription();
              if (desc == null || desc.trim().equals("")) {
                desc = "&nbsp";
              }
              description.addContent(desc);
              maindiv.addContent(description);              
            } else if (!RequestCategory.isMolecularDiagnoticsRequestCategory(request.getCodeRequestCategory())) {
              // Show core facility notes for internal experiments
              Element sequenceNote = new Element("H5");
              sequenceNote.addContent("Notes for Core facility");
              maindiv.addContent(sequenceNote);

              Element coreInstruction = new Element("H6");
              String corePrepInstructions = request.getCorePrepInstructions();
              if (corePrepInstructions == null || corePrepInstructions.trim().equals("")) {
            	  corePrepInstructions = "&nbsp;";
              }
              coreInstruction.addContent(corePrepInstructions);
              maindiv.addContent(coreInstruction);
            }

            if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
            	formatter.addIlluminaSampleTable(maindiv,  request.getSamples()); // Samples(#) table
            	formatter.addCovarisSampleTable(maindiv, request.getSamples()); // Covaris Information table
            } else {
                formatter.addSampleTable(maindiv, request.getSamples());
            }

            Set laneSamples = new TreeSet();
            
            if (!request.getHybridizations().isEmpty()) {
              laneSamples = request.getLabeledSamples();	
              formatter.makePageBreak(maindiv);

              TreeSet labeledSamples = new TreeSet(new LabeledSampleNumberComparator());
              labeledSamples.addAll(request.getLabeledSamples());
              formatter.makeLabeledSampleTable(maindiv, labeledSamples);

              formatter.makePageBreak(maindiv);

              maindiv.addContent(formatter.makeHybTable(request.getHybridizations()));          
            }

            if (request.getSequenceLanes().iterator().hasNext() && (request.getIsExternal() != null && !request.getIsExternal().equals("Y"))) {
              formatter.makePageBreak(maindiv);

              formatter.addSequenceLaneTable(maindiv, request.getSequenceLanes(), amendState);   
            }
            
            // Append the submission instructions to the printable form
            // for non-guest users.
            String instructions = "";
            if (request.getIsExternal() == null || request.getIsExternal().equals("N")) {
              if (!this.getSecAdvisor().isGuest()) {
                instructions = this.getInstructions();
                if (instructions != null) {
                  Element instructDiv = new Element("DIV");
                  instructDiv.setAttribute("id", "containerInstruction");
                  outerDiv.addContent(instructDiv);

                  instructDiv.addContent(new Element("BR"));
                  formatter.makePageBreak(instructDiv);

                  instructDiv.addContent("SUBMISSION_INSTRUCTIONS_GO_HERE");
                  // Convert degree and micro symbols to html escape codes
                  instructions = instructions.replaceAll("\\xB0", "&#176;");
                  instructions = instructions.replaceAll("\\xB5", "&#181;");              
                }              
              }              
            }
            
 
            if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
	            boolean corePrepLib = true;
              String steps = request.getApplication().getCoreSteps();
              String sampleType = "";
              String samplePrepMethod = "";
	            if (request.getSamples().iterator().hasNext()) {
                Sample smp = (Sample) request.getSamples().iterator().next(); 
                if (smp.getSeqPrepByCore() != null && smp.getSeqPrepByCore().equals("N")) {
                  corePrepLib = false;
                }
//                if (!corePrepLib) {
//                	
//                  steps = request.getApplication().getCoreStepsNoLibPrep(); // is it possible to have core steps when no lib prep is requested? steps is being set to null in no lib prep case. JFK
//                  // The sample will not be prepared by the core. Add a line at end of printable form to show the requester will prepare sample.  8/12/13 Jared Kubly
//                  Element preppedByUser = new Element("H7");
//    	            preppedByUser.addContent("<P ALIGN=\"LEFT\">Library Prepared By: _____" + appUser.getFirstLastDisplayName() + "_____</P>");
//    	            maindiv.addContent(preppedByUser);
//    	                            
//                  
//                }
                sampleType = dictionaryHelper.getSampleType(smp);
                samplePrepMethod = getSamplePrepMethod(smp);
	            }

	            
	            if (steps != null && steps.length() > 0) {
	              // New Page
                maindiv.addContent(new Element("BR"));
                maindiv.addContent(new Element("BR"));
                maindiv.addContent(new Element("BR"));
                maindiv.addContent(new Element("HR"));
	              formatter.makePageBreak(maindiv);
	              
	              maindiv.addContent(new Element("BR"));
	              maindiv.addContent(formatter.makeRequestTable());
	              maindiv.addContent(new Element ("BR"));
	  
	              Element reqNum = new Element("H2");
	              reqNum.addContent(formatter.makeRequestCategoryImage(null));
	              reqNum.addContent(request.getNumber() + "&nbsp;&nbsp;&nbsp;");
	              maindiv.addContent(reqNum);
	              
	              Element reqCat = new Element("H4");
	              reqCat.addContent(dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + (request.getIsExternal() != null && request.getIsExternal().equals("Y") ? "" :  " Request"));
	              maindiv.addContent(reqCat);
	              
	              if (request.getSequenceLanes().iterator().hasNext()) {
	                Element seqType = new Element("H4");
	                SequenceLane lane = (SequenceLane) request.getSequenceLanes().iterator().next();
	                seqType.addContent(lane.getIdNumberSequencingCycles()!= null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;" + "&nbsp;&nbsp;&nbsp;");
	                seqType.addContent(lane.getIdSeqRunType() != null ? "&nbsp;" + dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
	                maindiv.addContent(seqType);
	                
	              }

	              if (request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
	                Element hApp = new Element("H4");
	                hApp.addContent(dictionaryHelper.getApplication(request.getCodeApplication()));
	                maindiv.addContent(hApp);
	              }
	              
	              maindiv.addContent(new Element("BR"));
                Element stepsNote = new Element("H5");
                stepsNote.addContent("Steps");
                maindiv.addContent(stepsNote);

                String coreStepsString = "";
                coreStepsString += "<P ALIGN=\"LEFT\">Sample Type:  " + (sampleType == null || sampleType.length() == 0 || sampleType.equals("&nbsp;") ? "_____________________________" : "<u>" + sampleType + "</u>") + "</P>";
                coreStepsString += "<P ALIGN=\"LEFT\">Nucleic Acid Extraction Method:  " + (samplePrepMethod == null || samplePrepMethod.length() == 0 || samplePrepMethod.equals("&nbsp;") ? "___________________________________" : "<u>" + samplePrepMethod + "</u>") + "</P>";
                coreStepsString += "<P ALIGN=\"LEFT\">Received Date:  ___________________</P>";
                //TODO: show prepped by if core lab has been requested to prepare the sample
                if(corePrepLib)
                {
                	coreStepsString += "<P ALIGN=\"LEFT\">Library Prepared By: ______________________ (Core Lab Employee)</P>";
                }
                else                	
                {
                	//coreStepsString += "<P ALIGN=\"LEFT\">Library Prepared By: _____" + appUser.getFirstLastDisplayName() + "_____</P>"; // code not being called. 8/12/13 Jared Kubly
                }
                if (corePrepLib) {
                  coreStepsString += request.getApplication().getCoreSteps();
                } else {
                  coreStepsString += request.getApplication().getCoreStepsNoLibPrep();
                }
  	
  	            Element coreStepsDescription = new Element("H7");
  	            coreStepsDescription.addContent(coreStepsString);
  	            maindiv.addContent(coreStepsDescription);              
	            }
	            
            }  

            XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setOmitEncoding(true);
            this.xmlResult = out.outputString(doc);
            this.xmlResult = RequestParser.unEscapeBasic(this.xmlResult);
            this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");
            // Add the submission instructions to the end of the request
            // form.
            if (request.getIsExternal() == null || request.getIsExternal().equals("N")) {
              this.xmlResult = this.xmlResult.replaceAll("SUBMISSION_INSTRUCTIONS_GO_HERE", instructions);              
            }

            // Injust the <script> for java script handling of alternate style sheets
            this.xmlResult = this.xmlResult.replaceAll("JAVASCRIPT_GOES_HERE", "<script type=\"text/javascript\" src=\"switchPrintStyleSheet.js\"></script>");

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

  private String getSamplePrepMethod(Sample sample) {
	   
	    String spm = null;
	    spm = sample.getOtherSamplePrepMethod();
	    return spm != null && !spm.trim().equals("") ? spm : "&nbsp;";
  }

  private Element makeRow(String header1, String value1) {
	    Element row = new Element("TR");
	    Element cell = new Element("TD");
	    cell.setAttribute("CLASS", "label");
	    cell.setAttribute("ALIGN", "LEFT");
	    cell.addContent(header1);
	    row.addContent(cell);
	    
	    cell = new Element("TD");
	    cell.setAttribute("CLASS", "value");
	    cell.setAttribute("ALIGN", "LEFT");
	    cell.addContent(value1);
	    row.addContent(cell);
	    
	    return row;
  }

  private Element makeRow(String header1, String value1, String header2, String value2) {
	    Element row = new Element("TR");
	    Element cell = new Element("TD");
	    cell.setAttribute("CLASS", "label");
	    cell.setAttribute("ALIGN", "RIGHT");
	    cell.addContent(header1);
	    row.addContent(cell);
	    
	    cell = new Element("TD");
	    cell.setAttribute("CLASS", "value");
	    cell.setAttribute("ALIGN", "LEFT");
	    cell.addContent(value1);
	    row.addContent(cell);
	    
	    cell = new Element("TD");
	    //cell.setAttribute("WIDTH", "80");
	    row.addContent(cell);

	    cell = new Element("TD");
	    cell.setAttribute("CLASS", "label");
	    cell.setAttribute("ALIGN", "RIGHT");
	    cell.addContent(header2);
	    row.addContent(cell);
	    
	    cell = new Element("TD");
	    cell.setAttribute("CLASS", "value");
	    cell.setAttribute("ALIGN", "LEFT");
	    cell.addContent(value2);
	    row.addContent(cell);

	    return row;
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
      instructionDoc = parser.build(new FileInputStream(GNomExFrontController.getWebContextPath() + instructionURL));
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
      } 
        log.warn("Cannot find exact matching Submission Instructions for request " + request.getNumber());
        return null;
           
    } 
      log.warn("No matching Submission Instructions for request " + request.getNumber());
      return null;
   

  }
}