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
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
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

import org.hibernate.Session;


public class DownloadSampleSheet extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadSampleSheet.class);
  
  
  private SecurityAdvisor               secAdvisor;
  private Integer                       idRequest;
  private HashMap                       columnNames;
  private int                           columnCount = 1;
  
  private String                        today;
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = Integer.valueOf(request.getParameter("idRequest"));
    } 
    
    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }
    
    today = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
  }

  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report.jsp";
    this.SUCCESS_JSP_CSV = "/report_csv.jsp";
    this.SUCCESS_JSP_PDF = "/report_pdf.jsp";
    this.SUCCESS_JSP_XLS = "/report_xls.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    
    try {
         
      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      columnNames = generateColumnNames();
      List results = (List)sess.createQuery(generateQuery().toString()).list();
      createReportTray((Object [])results.get(0), sess);
      
//      List properties = (List)sess.createQuery("SELECT DISTINCT prop.name from Sample as samp LEFT JOIN samp.propertyEntry as propEntry LEFT JOIN propEntry.property as prop where samp.idRequest= " + idRequest);
//      for(Iterator j = properties.iterator(); j.hasNext();){
//        Object[] propRow = (Object[])j.next();
//        createReportTrayColumns(propRow);
//      }

      if (this.isValid()) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        
        for(Iterator i = results.iterator(); i.hasNext();) {
          Object[] row = (Object[])i.next();
          ReportRow reportRow = makeReportRow(row, dateFormat, (Object [])results.get(0), sess);
          tray.addRow(reportRow);
        }
      }
      
      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowAnnotationReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowAnnotationReport ", e);
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
  
  private HashMap<Integer, String> generateColumnNames(){
    HashMap<Integer, String> columnNames = new HashMap<Integer, String>();
    columnNames.put(0, "Sample Number");
    columnNames.put(1, "Sample Name");
    columnNames.put(2, "Concentration");
    columnNames.put(3, "Concentration Unit");
    columnNames.put(4, "Sample Type");
    columnNames.put(5, "Organism");
    columnNames.put(6, "Sample Source");
    columnNames.put(7, "Code Bioanalyzer Chip Type");
    columnNames.put(8, "Qual Failed");
    columnNames.put(9, "Qual ByPassed");
    columnNames.put(10, "Qual 260nm to 230nm Ratio");
    columnNames.put(11, "Qual 260nm to 280nm Ratio");
    columnNames.put(12, "Qual Calc Concentration");
    columnNames.put(13, "Qual 28s to 18s Ribosomal Ratio");
    columnNames.put(14, "Qual RIN number");
    columnNames.put(15, "Fragment size from");
    columnNames.put(16, "Fragment size to");
    columnNames.put(17, "Seq prep by core");
    columnNames.put(18, "Seq prep failed");
    columnNames.put(19, "Seq prep bypassed");
    columnNames.put(20, "Seq lib concentration");
    columnNames.put(21, "Prep Instructions");
    columnNames.put(22, "Barcode Sequence");
    columnNames.put(23, "Multiplex group number");
    columnNames.put(24, "Other organism");
    columnNames.put(25, "Other sample prep method");
    
    return columnNames;
    
  }
  
  private StringBuffer generateQuery(){
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT     samp.number, ");
    queryBuf.append("           samp.name, ");
    queryBuf.append("           samp.concentration, ");
    queryBuf.append("           samp.codeConcentrationUnit, ");
    queryBuf.append("           samp.idSampleType, ");
    queryBuf.append("           samp.idOrganism, ");
    queryBuf.append("           samp.idSampleSource, ");
    queryBuf.append("           samp.codeBioanalyzerChipType, ");
    queryBuf.append("           samp.qualFailed, ");
    queryBuf.append("           samp.qualBypassed, ");
    queryBuf.append("           samp.qual260nmTo230nmRatio, ");
    queryBuf.append("           samp.qual260nmTo280nmRatio, ");
    queryBuf.append("           samp.qualCalcConcentration, ");
    queryBuf.append("           samp.qual28sTo18sRibosomalRatio, ");
    queryBuf.append("           samp.qualRINNumber, ");
    queryBuf.append("           samp.fragmentSizeFrom, ");
    queryBuf.append("           samp.fragmentSizeTo, ");
    queryBuf.append("           samp.seqPrepByCore, ");
    queryBuf.append("           samp.seqPrepFailed, ");
    queryBuf.append("           samp.seqPrepBypassed, ");
    queryBuf.append("           samp.seqPrepLibConcentration, ");
    queryBuf.append("           samp.prepInstructions, ");
    queryBuf.append("           samp.barcodeSequence, ");
    queryBuf.append("           samp.multiplexGroupNumber, ");
    queryBuf.append("           samp.otherOrganism, ");
    queryBuf.append("           samp.otherSamplePrepMethod, ");
    queryBuf.append("           samp.idSample ");
    
    queryBuf.append("FROM       Sample as samp ");
    
    queryBuf.append("WHERE      samp.idRequest = " + idRequest);
    
    return queryBuf;
    
  }
  
  private void createReportTray(Object [] row, Session sess) {
    String title = "GNomEx Sample Sheet";
    String fileName = "gnomex_sampleSheet_" + today;
    
    // set up the ReportTray
    tray = new ReportTray();
    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
    tray.setReportTitle(title);
    tray.setReportDescription(title);
    tray.setFileName(fileName);
    tray.setFormat(ReportFormats.CSV);
    
    Set columns = new TreeSet();
    
    for(int i = 0; i < row.length; i++){
      if(row[i] != null && !row[i].equals("") && i < columnNames.size()){
        columns.add(makeReportColumn((String)columnNames.get(i), columnCount));
        columnCount++;
      } else if (i == columnNames.size()){
        Sample samp = (Sample)sess.load(Sample.class, (Integer)row[i]);
        for(Object pe : samp.getPropertyEntries()){
          PropertyEntry propEntry = (PropertyEntry) pe;
          columns.add(makeReportColumn(propEntry.getProperty().getName(), columnCount));
          columnCount++;
        }
        
      }
    }
    tray.setColumns(columns);
  }
  
//  private void createReportTrayColumns(Object[] row){
//    Set columns = new TreeSet();
//    
//    for(int i = 0; i < row.length; i++){
//      if(row[i] != null && !row[i].equals("")){
//        columns.add(makeReportColumn((String)row[i], columnCount));
//        columnCount++;
//      }
//    }
//    tray.setColumns(columns);
//  }
  
  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }

  private ReportRow makeReportRow(Object[] row, SimpleDateFormat dateFormat, Object[] resultRow, Session sess) {
    ReportRow reportRow = new ReportRow();
    List values  = new ArrayList();
    for(int i = 0; i < resultRow.length - 1; i++){
      if(resultRow[i] != null && !resultRow[i].equals("")){
        if(row[i] instanceof Integer && columnNames.get(i).equals("Organism")){
          Organism o = (Organism)sess.load(Organism.class, (Integer)row[i]);
          values.add(surroundWithQuotes(o.getOrganism()));
        } else if(row[i] instanceof Integer && columnNames.get(i).equals("Sample Type")){
          SampleType st = (SampleType)sess.load(SampleType.class, (Integer)row[i]);
          values.add(surroundWithQuotes(st.getSampleType()));
        } else if(row[i] instanceof Integer && columnNames.get(i).equals("Sample Source")){
          SampleSource ss = (SampleSource)sess.load(SampleSource.class, (Integer)row[i]);
          values.add(surroundWithQuotes(ss.getSampleSource()));
        } else if(row[i] instanceof BigDecimal){
          values.add(surroundWithQuotes(row[i].toString()));
        } else{
          values.add(surroundWithQuotes(row[i]));
        }
      }
      
    }
    
    Sample s = (Sample)sess.load(Sample.class, (Integer)resultRow[26]);
    for(Object pe : s.getPropertyEntries()){
      PropertyEntry propEntry = (PropertyEntry) pe;
      values.add(surroundWithQuotes(propEntry.getValue()));
      columnCount++;
    }
    
    
    
    reportRow.setValues(values);
    return reportRow;
  }
  
  private Object surroundWithQuotes(Object value) {
    if (value == null) {
      value = "";
    }
    return "\"" + value.toString() + "\"";
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