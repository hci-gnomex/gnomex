package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Request;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class DeleteOldExperimentAndAnalysisFilesEmailFormatter  extends DetailObject {
  
  private Lab                    lab;
  private Map<Integer, Analysis> analysisMap;
  private List<AnalysisFile>     analysisFiles;
  private Map<Integer, Request>  requestMap;
  private List<ExperimentFile>   experimentFiles;
  private BillingPeriod          billingPeriod;
  private String                 contactNameCoreFacility;
  private String                 contactPhoneCoreFacility;
  private String                 baseURL;
  private boolean                forWarning;

  CoreFacility           facility;
  
  public DeleteOldExperimentAndAnalysisFilesEmailFormatter(Lab lab, Map<Integer, 
      Analysis> analysisMap, List<AnalysisFile> analysisFiles, 
      Map<Integer, Request> requestMap, List<ExperimentFile> experimentFiles, 
      BillingPeriod billingPeriod, String contactNameCoreFacility, String contactPhoneCoreFacility, String baseURL,
      boolean forWarning) {
    this.lab                        = lab;
    this.analysisMap                = analysisMap;
    this.analysisFiles              = analysisFiles;
    this.requestMap                 = requestMap;
    this.experimentFiles            = experimentFiles;
    this.billingPeriod              = billingPeriod;
    this.contactNameCoreFacility    = contactNameCoreFacility;
    this.contactPhoneCoreFacility   = contactPhoneCoreFacility;
    this.baseURL                    = baseURL;  
    this.forWarning                 = forWarning;
  }
 
  public String format() {
    Element root = new Element("HTML");
    Document doc = new Document(root);

    Element center1 = formatHeader(root);
    center1.addContent(new Element("BR"));
    
    if (analysisFiles != null && analysisFiles.size() > 0) {
      center1.addContent(new Element("BR"));
      center1.addContent(formatAnalysisFiles());
    }
    
    if (experimentFiles != null && experimentFiles.size() > 0) {
      center1.addContent(new Element("BR"));
      center1.addContent(formatExperimentFiles());
    }
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    String buf = out.outputString(doc);
    buf = buf.replaceAll("&amp;",    "&");
    buf = buf.replaceAll("�",        "&micro");
    buf = buf.replaceAll("&gt;",     ">");
    buf = buf.replaceAll("&lt;",     "<");
    
    return buf;
  }
 
  public Element formatHeader(Element root) {
    Element head = new Element("HEAD");
    root.addContent(head);

    Element title = new Element("TITLE");
    title.addContent(getSubject());
    head.addContent(title);

    Element style = new Element("style");
    style.setAttribute("type", "text/css");
    style.addContent(this.getCascadingStyleSheet());
    head.addContent(style);
    
    Element body = new Element("BODY");
    root.addContent(body);

    body.addContent(makeIntroNote());
    
    return body;
  }

  public String getSubject() {
    return lab.getFormattedLabName() + " GNomEx File Deletion Warning";
  }
  
  private String getCascadingStyleSheet() {
    StringBuffer buf = new StringBuffer();
    for(String line: Constants.DELETE_OLD_EXPERIMENT_AND_ANALYSIS_FILES_CSS) {
      buf.append(line);
      buf.append(System.getProperty("line.separator"));
    }

    return buf.toString();
  }
  
  private  Element makeIntroNote() {
    String line1 = "This report provides itemized list of analysis and experiment files created by your lab that are scheduled for deletion by end of " + billingPeriod.getBillingPeriod() + ".";
    String line2 = "&nbsp;&nbsp;&nbsp; - ";
    String line3 = "";
    if (lab.isExternalLab()) {
      line2 += "Please make sure you have downloaded your data prior to this date.";
    } else {
      line2 += "To avoid deletion a work authorization must be submitted and approved prior to this date.  (" + getWorkAuthLink() + ")";
      line3 = "&nbsp;&nbsp;&nbsp; - Estimated charges for disk space, if any, will be sent in a separate email.";
    }
    if (!forWarning) {
      Calendar cal = Calendar.getInstance();
      line1 = "The following analysis and experiment files created by your lab have been deleted on " + new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());
      line2 = "";
      line3 = "";
    }
    String line4 = "&nbsp;";
    String line5 = "If you have any questions, please contact " + contactNameCoreFacility + " (" + contactPhoneCoreFacility + ").";
     
    Element table = new Element("TABLE");   
    table.setAttribute("CELLPADDING", "0");
    table.addContent(makeNoteRow(line1));
    if (line2.length() > 0) {
      table.addContent(makeNoteRow(line2));
    }
    if (line3.length() > 0) {
      table.addContent(makeNoteRow(line3));
    }
    table.addContent(makeNoteRow(line4));
    table.addContent(makeNoteRow(line5));
    
    return table;
  }

  private String getWorkAuthLink() {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"").append(baseURL).append("gnomexFlex.jsp?launchWindow=WorkAuthForm").append("\">Work Authorization</a>");
    return buf.toString();
  }
  
  private Element makeNoteRow(String header1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "note");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);
    

    return row;
  }
  
  private Element formatAnalysisFiles() {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Analysis ID");
    this.addHeaderCell(rowh, "File");
    
    Integer prevIdAnalysis = -1;
    for(AnalysisFile file : analysisFiles) {
      if (!prevIdAnalysis.equals(file.getIdAnalysis())) {
        Analysis analysis = analysisMap.get(file.getIdAnalysis());
        table.addContent(getAnalysisRow(analysis));
        prevIdAnalysis = file.getIdAnalysis();
      }
      
      table.addContent(getAnalysisFileRow(file));
    }
    
    return table;
  }

  private Element getAnalysisRow(Analysis analysis) {
    Element row = new Element("TR");
    addCell(row, analysis.getNumber());
    addEmptyCell(row);
    
    return row;
  }
  
  private Element getAnalysisFileRow(AnalysisFile file) {
    Element row = new Element("TR");
    addEmptyCell(row);
    addCell(row, file.getFileName());
    
    return row;
  }
  
  private Element formatExperimentFiles() {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Request ID");
    this.addHeaderCell(rowh, "File");
    
    Integer prevIdReq = -1;
    for(ExperimentFile file : experimentFiles) {
      if (!prevIdReq.equals(file.getIdRequest())) {
        Request req = requestMap.get(file.getIdRequest());
        table.addContent(getRequestRow(req));
        prevIdReq = file.getIdRequest();
      }
      
      table.addContent(getExperimentFileRow(file));
    }
    
    return table;
  }

  private Element getRequestRow(Request req) {
    Element row = new Element("TR");
    addCell(row, req.getNumber());
    addEmptyCell(row);
    
    return row;
  }
  
  private Element getExperimentFileRow(ExperimentFile file) {
    Element row = new Element("TR");
    addEmptyCell(row);
    addCell(row, file.getFileName());
    
    return row;
  }
  
  private void addHeaderCell(Element row, String header) {
    addHeaderCell(row, header, "normal");
  }
  
  private void addHeaderCell(Element row, String header, String clazzName) {

    addHeaderCell(row, header, clazzName, null);
  }
  
  
  private void addHeaderCell(Element row, String header, String clazzName, Integer width) {
    Element cell = new Element("TH");  
    
    // for consistent rendering in different email apps
    if (clazzName.equals("right")) {
      cell.setAttribute("ALIGN", "RIGHT");      
    } else {
      cell.setAttribute("ALIGN", "LEFT");
    }
    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    cell.addContent(header);
    row.addContent(cell);
  }

  private void addCell(Element row, String value) {
      addCell(row, value, "grid", null);
  }
  private void addCell(Element row, String value, String cssClass, Integer colSpan) {
    addCell(row, value, cssClass, colSpan, null);
  }
  private void addCell(Element row, String value, Integer colSpan) {
    addCell(row, value, "grid", colSpan, null);
  }
  private void addCell(Element row, String value, String cssClass, Integer colSpan, String align) {
      Element cell = new Element("TD");
      cell.setAttribute("CLASS", cssClass);      
      cell.addContent(value);
      row.addContent(cell);
      if (colSpan != null) {
        cell.setAttribute("COLSPAN", colSpan.toString());
      }
      if (align != null) {
        cell.setAttribute("ALIGN", align);
      }
  }
  
  private void addEmptyCell(Element row, Integer colSpan) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
    row.addContent(cell);
    if (colSpan != null) {
      cell.setAttribute("COLSPAN", colSpan.toString());      
    }
    
  }
  private void addEmptyCell(Element row) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
    row.addContent(cell);
  }

  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", null);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, Integer width) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", width);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName) {
    addHeaderCell(row, header, rowSpan, colSpan, clazzName, null);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName, Integer width) {
    Element cell = new Element("TH");    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    cell.addContent(header);
    if (colSpan != null) {    
      cell.setAttribute("COLSPAN", colSpan.toString());
    }
    if (rowSpan != null) {
      cell.setAttribute("ROWSPAN", rowSpan.toString());      
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    row.addContent(cell);
  }
}
