package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Date;
import java.text.NumberFormat;
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
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class ShowExperimentOverviewReport extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowExperimentOverviewReport.class);
  
  private Integer           idCoreFacility;
  private String 		    workflowStatus;
  private String		    requestingUser;
  private String			experimentType;
  private String			seqType;
  private String			seqLength;
  private String			seqInstrument;
  private String 			experimentId;
  private String		   xmlCollection;
  private String		   exportFormat;
  private SecurityAdvisor  secAdvisor;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

	  if(request.getParameter("data") != null){
	    	xmlCollection = request.getParameter("data");
	    }else{
	    	this.addInvalidField("expCollection", "The generated experiment overview is missing.");
	    }
	  
    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report.jsp";
    this.SUCCESS_JSP_CSV = "/report_csv.jsp";
    this.SUCCESS_JSP_PDF = "/report_pdf.jsp";
    this.SUCCESS_JSP_XLS = "/report_xls.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    try {
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      if (this.isValid()) {
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) { 
          
         if (isValid()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
            factory.setIgnoringComments(true);
            factory.setCoalescing(true); // Convert CDATA to Text nodes
            factory.setNamespaceAware(false); // No namespaces: this is default
            factory.setValidating(false); // Don't validate DTD: also default
            DocumentBuilder builder;
             
		            try  
		            {	
		                builder = factory.newDocumentBuilder();  
		
		                // Use String reader  
		                Document document = builder.parse( new InputSource(  
		                        new StringReader( xmlCollection ) ) );

		                Element root = document.getDocumentElement();
		                // Get the root element attributes
		                idCoreFacility = new Integer(root.getAttribute("idCoreFacility"));
		                exportFormat = root.getAttribute("exportFormat");
		                workflowStatus = root.getAttribute("workflowStatus");
		                seqType = root.getAttribute("seqType");
		                seqLength = root.getAttribute("seqLength");
		                seqInstrument = root.getAttribute("seqInstrument");
		                experimentId = root.getAttribute("experimentId");
		                requestingUser = root.getAttribute("requestUser");

		                CoreFacility core = (CoreFacility)sess.load(CoreFacility.class, idCoreFacility);		                
		                
		                // set up the ReportTray
		                tray = new ReportTray();
		                tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
		                tray.setReportTitle("Experiment Overview Report for: " + core.getFacilityName());
		                tray.setReportDescription("Experiment Overview Report for: " + core.getFacilityName());
		                tray.setFileName("ExperimentOverviewSummary_" + tray.getReportDate());
		                tray.setFormat(exportFormat);
/*		                
		                makeBlankRow();
		                
		                // Filtering options header.
		                summaryRow("Core Facility: ", core.getFacilityName());
		                summaryRow("Workflow Status: ", workflowStatus);
		                summaryRow("Sequencing Type: ", seqType);
		                summaryRow("Sequence Length (Cycles) ", seqLength);
		                summaryRow("Sequencing Instrument: ", seqInstrument);
		                summaryRow("Experiment ID: ", experimentId);
		                summaryRow("Requesting User: ", requestingUser);
		                
		                makeBlankRow();
*/ 
		                Set columns = new TreeSet();
			            columns.add(makeReportColumn("Experiment ID", 1));
			            columns.add(makeReportColumn("Client", 2));
			            columns.add(makeReportColumn("Sample Number", 3));
			            columns.add(makeReportColumn("Sample Name", 4));
			            columns.add(makeReportColumn("Index", 5));
			            columns.add(makeReportColumn("Single/Paired", 6));
			            columns.add(makeReportColumn("# Cycles", 7));
			            columns.add(makeReportColumn("Experiment Type", 8));
			            columns.add(makeReportColumn("Lab", 9));
			            columns.add(makeReportColumn("Library Prep Completed", 10));
			            columns.add(makeReportColumn("Submission Date", 11));
			            columns.add(makeReportColumn("Instrument", 12));
			            
			            tray.setColumns(columns);
			            
			          //  boolean firstTime = true;
			    		NodeList entries = document.getElementsByTagName("ExperimentOverview");

			    		
			    		for (int i=0; i<entries.getLength(); i++) {
			    				Element element = (Element) entries.item(i);
			    				
			    				if(element != null){
				                    ReportRow reportRow = new ReportRow();
				                    List values  = new ArrayList();
				                
				                    values.add(element.getAttribute("experimentId") != "" ? element.getAttribute("experimentId") : "");
				                    values.add(element.getAttribute("userFullName") != "" ? element.getAttribute("userFullName") : "");
				                    values.add(element.getAttribute("sampleNumber") != "" ? element.getAttribute("sampleNumber") : "");
				                    values.add(element.getAttribute("sampleName") != "" ? element.getAttribute("sampleName") : "");
				                    values.add(element.getAttribute("sampleBarcode") != "" ? element.getAttribute("sampleBarcode") : "");
				                    values.add(element.getAttribute("expSeqRunType") != "" ? element.getAttribute("expSeqRunType") : "");
				                    values.add(element.getAttribute("expNumSeqCycles") != "" ? element.getAttribute("expNumSeqCycles") : "");
				                    values.add(element.getAttribute("expReadApp") != "" ? element.getAttribute("expReadApp") : "");
				                    values.add(element.getAttribute("labFullName") != "" ? element.getAttribute("labFullName") : "");
				                    values.add(element.getAttribute("samplePrepDate") != "" ? element.getAttribute("samplePrepDate") : "");
				                    values.add(element.getAttribute("expCreateDate") != "" ? element.getAttribute("expCreateDate") : "");
				                    values.add(element.getAttribute("expInstrument") != "" ? element.getAttribute("expInstrument") : "");
				                    reportRow.setValues(values);
				                    tray.addRow(reportRow);
			    				}
			    		}
	         		}catch (Exception e) {
					// TODO: handle exception
	         			System.out.println("Error in parsing document. " + e);
	         		}
          }
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permissions to generate Experiment Overview Report.");
        }
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowExperimentOverviewReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowExperimentOverviewReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowExperimentOverviewReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowExperimentOverviewReport ", e);
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
  

  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }
  
  private void summaryRow(String header, String value){
	  if(!value.equals("")){
		    ReportRow reportRow = new ReportRow();
		    List<String> values  = new ArrayList<String>();
		    values.add(header);
		    values.add(value);
	
		    reportRow.setValues(values);
		    tray.addRow(reportRow);
	  }
  }
  
  /* (non-Javadoc)
   * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {
    request.setAttribute("tray", this.tray);
    return request;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setResponseState(javax.servlet.http.HttpServletResponse)
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setSessionState(javax.servlet.http.HttpSession)
   */
  public HttpSession setSessionState(HttpSession session) {
    // TODO Auto-generated method stub
    return session;
  }

  /* (non-Javadoc)
   * @see hci.report.utility.ReportCommand#loadContextPermissions()
   */
  public void loadContextPermissions(){
    
  }
  public void loadContextPermissions(String userName) throws SQLException {
    
  }
  
}