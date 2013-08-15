package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Organism;
import hci.gnomex.model.ProjectExperimentReportFilter;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleSource;
import hci.gnomex.model.SampleType;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingItemParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.RequestParser;
import hci.gnomex.utility.SampleSheetColumnNamesParser;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class DownloadSampleSheet extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadSampleSheet.class);
  
  
  private SecurityAdvisor               secAdvisor;
  private String                        today;
  private SampleSheetColumnNamesParser  parser = null;
  private RequestParser                 requestParser = null;
  private String                        labName = "";
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }
    
    String columnString = request.getParameter("names");
    StringReader reader = new StringReader(columnString);
    try {
      SAXBuilder sax = new SAXBuilder();
      Document doc = sax.build(reader);
      parser = new SampleSheetColumnNamesParser(doc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse names", je );
      this.addInvalidField( "names", "Invalid sample name xml");
    }

    String requestXMLString = request.getParameter("requestXMLString");
    StringReader requestReader = new StringReader(requestXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      Document doc = sax.build(requestReader);
      requestParser = new RequestParser(doc, secAdvisor);
    } catch (JDOMException je ) {
      log.error( "Cannot parse requestXMLString", je );
      this.addInvalidField( "requestXMLString", "Invalid request xml");
    }

    today = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
  }

  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report.jsp";
    this.SUCCESS_JSP_CSV = "/report_csv.jsp";
    this.SUCCESS_JSP_PDF = "/report_pdf.jsp";
    this.SUCCESS_JSP_XLS = "/report_xls.jsp";
    this.SUCCESS_JSP_TSV = "/report_tsv.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    
    try {
         
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      parser.parse(sess);
      requestParser.parse(sess);
      if(requestParser.getRequest().getIdLab() != null && !requestParser.getRequest().getIdLab().equals("")){
        Lab l = (Lab)sess.get(Lab.class, requestParser.getRequest().getIdLab());
        labName = l.getName();
      }
      createReportTray();
      
      if (this.isValid()) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        if (requestParser.getSampleIds().size() == 0) {
          ReportRow reportRow = makeReportRow(new Sample(), dateFormat, sess);
          tray.addRow(reportRow);
        } else {
          for(Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
            String idSampleString = (String)i.next();
            Sample sample = (Sample)requestParser.getSampleMap().get(idSampleString);
            ReportRow reportRow = makeReportRow(sample, dateFormat, sess);
            tray.addRow(reportRow);
          }
        }
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in DownloadSampleSheet ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in DownloadSampleSheet ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in DownloadSampleSheet ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in DownloadSampleSheet ", e);
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
  
  private void createReportTray() {
    String title = "GNomEx Sample Sheet";
    String fileName = "";
    
    if(requestParser.getRequest().getNumber() != null){
      fileName = requestParser.getRequest().getNumber() + "_" + today;
    } else{
      fileName = labName + "_new_" + today;
      fileName = fileName.replace(",", "");
    }
    
    // set up the ReportTray
    tray = new ReportTray();
    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
    tray.setReportTitle(title);
    tray.setReportDescription(title);
    tray.setFileName(fileName);
    tray.setFormat(ReportFormats.TSV);
    
    Set columns = new TreeSet();
    Integer columnCount = 0;
    
    for(String[] names : parser.getColumnList()) {
      String propertyName = names[SampleSheetColumnNamesParser.PROPERTY_NAME_IDX];
      String gridLabel = names[SampleSheetColumnNamesParser.GRID_LABEL_IDX];
      columns.add(makeReportColumn(propertyName, gridLabel, columnCount));
      columnCount++;
    }

    tray.setColumns(columns);
  }
  
  private Column makeReportColumn(String name, String caption, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(caption);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }

  private ReportRow makeReportRow(Sample sample, SimpleDateFormat dateFormat, Session sess) {
    ReportRow reportRow = new ReportRow();
    List values = new ArrayList();
    for(String[] names : parser.getColumnList()) {
      String propertyName = names[SampleSheetColumnNamesParser.PROPERTY_NAME_IDX];
      String gridLabel = names[SampleSheetColumnNamesParser.GRID_LABEL_IDX];
      String value = getSpecialValue(sample, propertyName);
      if (value == null) {
        value = getAssayValue(sample, propertyName);
      }
      if (value == null) {
        value = getPropertyValue(sample, gridLabel);
      }
      if (value == null) {
        value = getValueByReflection(sample, propertyName);
      }
      if (value == null) {
        // hmmm... hopefully won't happen.  If it does we have problems.
        value = "";
      }
      
      values.add(value);
    }
    
    reportRow.setValues(values);
    return reportRow;
  }
  
  private String getSpecialValue(Sample sample, String column) {
    String retVal = null;
    if (column.equals("idSampleType")) {
      retVal = getDictionaryValue(sample.getIdSampleType(), "hci.gnomex.model.SampleType");
    } else if (column.equals("idOrganism")) {
      retVal = getDictionaryValue(sample.getIdOrganism(), "hci.gnomex.model.OrganismLite");
    } else if (column.equals("idOligoBarcode")) {
      retVal = getDictionaryValue(sample.getIdOligoBarcode(), "hci.gnomex.model.OligoBarcode");
    } else if (column.equals("idOligoBarcodeB")) {
      retVal = getDictionaryValue(sample.getIdOligoBarcodeB(), "hci.gnomex.model.OligoBarcode");
    } else if (column.equals("idSampleSource")) {
      retVal = getDictionaryValue(sample.getIdSampleSource(), "hci.gnomex.model.SampleSource");
    } else if (column.equals("idSeqLibProtocol")) {
      retVal = getDictionaryValue(sample.getIdSeqLibProtocol(), "hci.gnomex.model.SeqLibProtocol");
    } else if (column.equals("codeBioanalyzerChipType")) {
      retVal = getDictionaryValue(sample.getCodeBioanalyzerChipType(), "hci.gnomex.model.BioanalyzerChipType");
    }
    
    return retVal;
  }
  
  private String getAssayValue(Sample sample, String propertyName) {
    String value = null;
    if (propertyName.startsWith("hasAssay")) {
      value = "N";
      String assayName = propertyName.substring(8);
      ArrayList<String> assays = requestParser.getAssays(sample.getIdSampleString());
      for(String a : assays) {
        if (a.equals(assayName)) {
          value = "Y";
          break;
        }
      }
    } 
    
    return value;
  }
  
  private String getDictionaryValue(Integer id, String cls) {
    if (id != null) {
      return DictionaryManager.getDisplay(cls, id.toString());
    } else {
      return "";
    }
  }
  
  private String getDictionaryValue(String code, String cls) {
    if (code != null) {
      return DictionaryManager.getDisplay(cls, code);
    } else {
      return "";
    }
  }
  
  private String getPropertyValue(Sample sample, String name) {
    String retVal = null;
    if (sample.getPropertyEntries() != null) {
      for(PropertyEntry pe : (Set<PropertyEntry>)sample.getPropertyEntries()) {
        if (pe.getProperty().getName().equals(name)) {
          retVal = pe.getValueForDisplay();
          break;
        }
      }
    }
    
    return retVal;
  }
  
  private String getValueByReflection(Sample sample, String column) {
    String retVal = null;
    String methodName = "get" + column.substring(0, 1).toUpperCase() + column.substring(1);
    try {
      Method m = sample.getClass().getMethod(methodName, new Class[] {});
      Object ret = m.invoke(sample, new Object[] {});
      if (ret != null) {
        retVal = ret.toString();
      }
    } catch(Exception ex) {
    }
    
    return retVal;
  }
  
  private Object surroundWithQuotes(Object value) {
    String s;
    if (value == null) {
      s = "";
    } else {
      s = value.toString();
      s = s.replace("\"", "\"\"");
    }
    return "\"" + s + "\"";
  }
  
  private String cleanText(String description) {
    Pattern pattern = Pattern.compile("\\x0d");
    description = pattern.matcher(description).replaceAll("_NEWLINE_GOES_HERE_");

    String[] tokens = description.split("_NEWLINE_GOES_HERE_");
    if (tokens.length > 0) {
      StringBuffer buf = new StringBuffer();
      for (int x = 0; x < tokens.length; x++) {
        buf.append(tokens[x]);
        buf.append("\n");
      }
      description = buf.toString();
    } 
    return description.toString();
  }
  
  private String cleanRichText(String description) {

    if (description == null) {
      return "";
    } else if (description.trim().equals("")) {
      return "";
    }
   
    Pattern paragraph = Pattern.compile("<P.*?>");
    description = paragraph.matcher(description).replaceAll("");
    
    Pattern pattern = Pattern.compile("<\\/P.*?>");
    description = pattern.matcher(description).replaceAll("_NEWLINE_GOES_HERE_");

    String[] tokens = description.split("_NEWLINE_GOES_HERE_");
    if (tokens.length > 0) {
      StringBuffer buf = new StringBuffer();
      for (int x = 0; x < tokens.length; x++) {
        buf.append(tokens[x]);
        buf.append("\n");
      }
      description = buf.toString();
    } 
    
    
    pattern = Pattern.compile("<B.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<\\/B.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<U.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<\\/U.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<LI.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<\\/LI.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<I.*?>");
    description = pattern.matcher(description).replaceAll("");
    pattern = Pattern.compile("<\\/I.*?>");
    description = pattern.matcher(description).replaceAll("");
    
    return description;
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