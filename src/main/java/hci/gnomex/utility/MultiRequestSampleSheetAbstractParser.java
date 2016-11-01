package hci.gnomex.utility;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.apache.log4j.Logger;
public abstract class MultiRequestSampleSheetAbstractParser implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(MultiRequestSampleSheetAbstractParser.class);

  private static final String HEADER_REQUEST_NUMBER = "Request #";
  private static final String HEADER_REQUEST_NUMBER2 = "Request Number";
  private static final String HEADER_SAMPLE_NUMBER = "Sample #";
  private static final String HEADER_SAMPLE_NUMBER2 = "Sample Number";
  private static final String HEADER_SAMPLE_NAME = "Sample Name";
  private static final String HEADER_CONC = "Conc.";
  private static final String HEADER_UNIT = "Unit";
  private static final String HEADER_DESCRIPTION = "Description";
  private static final String HEADER_DESCRIPTION1 = "Sample Description";
  private static final String HEADER_SAMPLE_TYPE = "Sample Type";
  private static final String HEADER_ORGANISM = "Organism";
  private static final String HEADER_CC_NUMBER = "CC Number";

  protected List<Error> errors = null;
  protected Map<Integer, ColumnInfo> columnMap;
  private Integer requestNumberOrdinal;
  private Integer sampleNumberOrdinal;
  private Integer sampleNameOrdinal;
  private Map<String, Request>requestMap;
  private Map<String, Map> annotationMap;
  private Map<String, Map> annotationsToDeleteMap;
  private SecurityAdvisor secAdvisor;
  private Map<String, List<Sample>> requestSampleMap;

  public MultiRequestSampleSheetAbstractParser(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
  }

  public void parse(Session sess) {
    errors = new ArrayList<Error>();
    readFile();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    if (!fatalError()) {
      createColumnMap(dh);
      if (requestNumberOrdinal == null || sampleNumberOrdinal == null) {
        errors.add(new Error(Error.FATAL, "Both Request # and Sample # columns must be included in the spread sheet."));
      }
    }

    if (!fatalError()) {
      requestMap = new HashMap<String, Request>();
      annotationMap = new HashMap<String, Map>();
      annotationsToDeleteMap = new HashMap<String, Map>();
      requestSampleMap = new HashMap<String, List<Sample>>();
      parseRows(sess, dh);
    }
  }

  public Map<String, Request> getRequestMap() {
    return requestMap;
  }

  public Map<String, Map> getAnnotationMap() {
    return annotationMap;
  }

  public Map<String, Map> getAnnotationsToDeleteMap() {
    return annotationsToDeleteMap;
  }

  public List<Sample> getModifiedSamplesForRequest(String requestNumber) {
    return requestSampleMap.get(requestNumber);
  }

  protected abstract void readFile();

  protected abstract void parseRows(Session sess, DictionaryHelper dh);

  public Boolean fatalError() {
    Boolean fatal = false;
    for(Error error : errors) {
      if (error.getStatus().equals(Error.FATAL)) {
        fatal = true;
        break;
      }
    }

    return fatal;
  }

  private void createColumnMap(DictionaryHelper dh) {
    columnMap = new HashMap<Integer, ColumnInfo>();
    String[] headers = getHeaderStrings();
    this.requestNumberOrdinal = null;
    this.sampleNumberOrdinal = null;
    this.sampleNameOrdinal = null;
    int ordinal = 0;
    for(String header : headers) {
      ColumnInfo info = getColumnColumnInfo(ordinal, header);
      if (info == null) {
        info = getAnnotationColumnInfo(dh, ordinal, header);
      }
      if (info == null) {
        errors.add(new Error(Error.COLUMN_ERROR, "Column with header '" + header + "' does not map to a column or annotation.", null, ordinal));
        info = getErrorColumnInfo(ordinal, header);
      }

      columnMap.put(info.getOrdinal(), info);
      ordinal++;
    }
  }

  protected abstract String[] getHeaderStrings();

  private ColumnInfo getColumnColumnInfo(Integer ordinal, String header) {
    ColumnInfo info = null;
    if (header.toLowerCase().equals(HEADER_REQUEST_NUMBER.toLowerCase()) || header.toLowerCase().equals(HEADER_REQUEST_NUMBER2.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_REQUEST_NUMBER, null, PropertyType.TEXT, null);
      requestNumberOrdinal = ordinal;
    } else if (header.toLowerCase().equals(HEADER_SAMPLE_NUMBER.toLowerCase()) || header.toLowerCase().equals(HEADER_SAMPLE_NUMBER2.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_SAMPLE_NUMBER, "number", PropertyType.TEXT, null);
      sampleNumberOrdinal = ordinal;
    } else if (header.toLowerCase().equals(HEADER_SAMPLE_NAME.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_SAMPLE_NAME, "name", PropertyType.TEXT, null);
      sampleNameOrdinal = ordinal;
    } else if (header.toLowerCase().equals(HEADER_CONC.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_CONC, "concentration", ColumnInfo.NUMERIC, null);
    } else if (header.toLowerCase().equals(HEADER_UNIT.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_CONC, "codeConcentrationUnit", PropertyType.OPTION, "hci.gnomex.model.ConcentrationUnit");
    } else if (header.toLowerCase().equals(HEADER_DESCRIPTION.toLowerCase()) || header.toLowerCase().equals(HEADER_DESCRIPTION1.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_DESCRIPTION, "description", PropertyType.TEXT, null);
    } else if (header.toLowerCase().equals(HEADER_SAMPLE_TYPE.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_SAMPLE_TYPE, "idSampleType", PropertyType.OPTION, "hci.gnomex.model.SampleType");
    } else if (header.toLowerCase().equals(HEADER_ORGANISM.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_ORGANISM, "idOrganism", PropertyType.OPTION, "hci.gnomex.model.OrganismLite");
    } else if (header.toLowerCase().equals(HEADER_CC_NUMBER.toLowerCase())) {
      info = new ColumnInfo(ordinal, HEADER_CC_NUMBER, "ccNumber", PropertyType.TEXT, null);
    }
    return info;
  }

  private ColumnInfo getAnnotationColumnInfo(DictionaryHelper dh, Integer ordinal, String header) {
    ColumnInfo info = null;
    // Hardwire to core 1 for now.
    // Likely will have to be addressed later for more flexibility.
    Property p = dh.getPropertyByNameAndCore(header, 1);
    if (p != null && p.getIsActive().equals("Y") && p.getForSample().equals("Y")) {
      info = new ColumnInfo(ordinal, p);
    }
    return info;
  }

  private ColumnInfo getErrorColumnInfo(Integer ordinal, String header) {
    ColumnInfo info = new ColumnInfo(ordinal, header, null, PropertyType.TEXT, null);
    return info;
  }

  protected void parseRow(Session sess, DictionaryHelper dh, String[] values, Integer rowOrdinal) {
    String requestNumber = "";
    if (values.length > requestNumberOrdinal) {
      requestNumber = values[requestNumberOrdinal];
    }
    String sampleNumber = "";
    if (values.length > sampleNumberOrdinal) {
      sampleNumber = values[sampleNumberOrdinal];
    }
    String sampleName = "";
    if (sampleNameOrdinal != null && values.length > sampleNameOrdinal) {
      sampleName = values[sampleNameOrdinal];
    }

    Boolean rowError = false;
    Request request = null;
    Sample sample = null;
    request = getRequest(sess, requestNumber);
    if (request == null) {
      errors.add(new Error(Error.REQUEST_ERROR, "Request does not exist.", rowOrdinal, null, requestNumber, sampleNumber));
      rowError = true;
    } else {
      sample = getSample(sess, request, sampleNumber, rowOrdinal);
      try {
        if (!this.secAdvisor.canUpdate(request)) {
          errors.add(new Error(Error.REQUEST_ERROR, "Insufficient privileges to update request.", rowOrdinal, null, requestNumber, sampleNumber));
          rowError = true;
        }
      } catch(UnknownPermissionException ex) {
        errors.add(new Error(Error.REQUEST_ERROR, "Unknown permission exception on request.", rowOrdinal, null, requestNumber, sampleNumber));
        LOG.error("MultiRequestSampleSheetFileParser: Unknown permission exception", ex);
        rowError = true;
      }
      if (sample != null && sample.getIdSample() == null && sampleName.length() == 0) {
        errors.add(new Error(Error.ROW_ERROR, "Neither Sample Number nor Sample Name specified for row.", rowOrdinal, null, requestNumber, sampleNumber));
        rowError = true;
      }
      if (!rowError && sample == null && sampleNumber.length() > 0) {
        errors.add(new Error(Error.ROW_ERROR, "Sample not found for request", rowOrdinal, null, requestNumber, sampleNumber));
        rowError = true;
      }
    }

    for(Integer colOrdinal : columnMap.keySet()) {
      ColumnInfo info = columnMap.get(colOrdinal);
      String value = values.length > colOrdinal ? values[colOrdinal] : null;
      if (info.getType().equals(PropertyType.URL) || info.getType().equals(PropertyType.MULTI_OPTION)) {
        if (value != null && value.length() > 2 && value.startsWith("\"") && value.endsWith("\"")) {
          value = value.substring(1, value.length() - 1);
        }
      }
      info.addValue(rowOrdinal, value);

      if (!rowError && isValid(info, value, errors, rowOrdinal, colOrdinal, requestNumber, sampleNumber)) {
        Boolean doSet = true;
        if (colOrdinal.equals(this.requestNumberOrdinal) || colOrdinal.equals(this.sampleNumberOrdinal)) {
          doSet = false;
        }
        if (colOrdinal.equals(this.sampleNameOrdinal) && (value == null || value.length() == 0)) {
          doSet = false;
        }
        if (doSet) {
          setValue(info, sample, value);
        }
      }
    }
  }

  private Boolean hasRequestError(String requestNumber) {
    for(Error error : this.errors) {
      if (error.getRequestNumber() != null && error.getRequestNumber().equals(requestNumber) && error.getStatus().equals(error.REQUEST_ERROR)) {
        return true;
      }
    }

    return false;
  }

  private Request getRequest(Session sess, String requestNumber) {
    Request request = null;
    if (requestMap.containsKey(requestNumber)) {
      request = requestMap.get(requestNumber);
    } else {
      String queryString = "from Request where number = :number";
      Query query = sess.createQuery(queryString);
      query.setParameter("number", requestNumber);
      List l = query.list();
      if (l.size() == 1) {
        request = (Request)l.get(0);
      }
      requestMap.put(requestNumber, request);
    }

    return request;
  }

  private Sample getSample(Session sess, Request request, String sampleNumber, Integer rowOrdinal) {
    Sample sample = null;
    if (sampleNumber.length() > 0) {
      for(Iterator i = request.getSamples().iterator(); i.hasNext(); ) {
        Sample s = (Sample)i.next();
        if (s.getNumber().equals(sampleNumber)) {
          sample = s;
          break;
        }
      }
    } else {
      sample = new Sample();
      Integer plus = rowOrdinal + 10000;
      String number = request.getRequestNumberNoR(request.getNumber()) + "X" + plus.toString();
      sample.setNumber(number);
    }

    if (sample != null) {
      Integer plus = rowOrdinal + 100000;
      sample.setIdSampleString("Sample" + plus.toString().substring(1));
    }

    List<Sample> sampleList = requestSampleMap.get(request.getNumber());
    if (sampleList == null) {
      sampleList = new ArrayList<Sample>();
    }
    sampleList.add(sample);
    requestSampleMap.put(request.getNumber(), sampleList);

    return sample;
  }

  private Boolean isValid(ColumnInfo info, String value, List<Error> errors, Integer rowOrdinal, Integer columnOrdinal, String requestNumber, String sampleNumber) {
    Boolean valid = true;
    String message = "";
    if (value != null && value.length() > 0) {
      if (columnOrdinal.equals(this.sampleNameOrdinal) && value.length() > 30) {
        message = "Sample name too long, name truncated to 30 characters.";
      }
      if (info.getType().equals(ColumnInfo.NUMERIC)) {
        try {
          new BigDecimal(value);
        } catch(NumberFormatException ex) {
          message = "Invalid numeric value";
          valid = false;
        }
      } else if (info.getType().equals(PropertyType.CHECKBOX)) {
        if (!value.equals("Y") && !value.equals("N")) {
          message = "Checkbox must have value or 'Y' or 'N'";
          valid = false;
        }
      } else if (info.getType().equals(PropertyType.MULTI_OPTION)) {
        String[] values = value.split(",");
        for(String v : values) {
          if (!isValidOption(info, v)) {
            message = "Value does not match any choice for dropdown.";
            valid = false;
            break;
          }
        }
      } else if (info.getType().equals(PropertyType.OPTION)) {
        valid = isValidOption(info, value);
        if (!valid) {
          message = "Value does not match any choice for dropdown.";
        }
      } else if (info.getType().equals(PropertyType.URL)) {
        String[] values = value.split("\\|");
        for(String v : values) {
          String[] pair = v.split(",");
          if (pair.length > 2) {
            valid = false;
            message = "Invalid URL format.  Must be <link>,<url>|<link>,<url>...";
            break;
          }
        }
      }
    }

    if (message.length() > 0) {
      errors.add(new Error(Error.CELL_ERROR, message, rowOrdinal, columnOrdinal, requestNumber, sampleNumber));
    }
    return valid;
  }

  private Boolean isValidOption(ColumnInfo info, String value) {
    Boolean valid = false;
    value = value.trim();
    if (value.length() == 0) {
      valid = true;
    } else {
      if (info.getProperty() != null) {
        for(Iterator i = info.getProperty().getOptions().iterator(); i.hasNext(); ) {
          PropertyOption po = (PropertyOption)i.next();
          if (po.getDisplay().equals(value)) {
            valid = true;
            break;
          }
        }
      } else if (info.getDictionaryName() != null) {
        for (Iterator i = DictionaryManager.getDictionaryEntries(info.getDictionaryName()).iterator(); i.hasNext();) {
          DictionaryEntry de = (DictionaryEntry)i.next();
          if (de instanceof NullDictionaryEntry) {
            continue;
          }
          if (de.getDisplay().equals(value)) {
            valid = true;
            break;
          }
        }
      }
    }

    return valid;
  }

  private void setValue(ColumnInfo info, Sample sample, String value) {
    try {
      if (info.getOrdinal().equals(this.sampleNameOrdinal) && value.length() > 30) {
        value = value.substring(0, 30);
      }
      if (info.getProperty() == null) {
        setSampleValue(info, sample, value);
      } else {
        setAnnotationValue(info, sample, value);
      }
    } catch (Exception e) {
      LOG.error("MultiRequestSampleSheetFileParser -- error setting value", e);
    }
  }

  private void setSampleValue(ColumnInfo info, Sample sample, String value) throws Exception {
    Object v = value;
    if (info.getDictionaryName() != null) {
      for (Iterator i = DictionaryManager.getDictionaryEntries(info.getDictionaryName()).iterator(); i.hasNext();) {
        DictionaryEntry de = (DictionaryEntry)i.next();
        if (de instanceof NullDictionaryEntry) {
          continue;
        }
        if (de.getDisplay().equals(value)) {
          v = de.getValue();
          break;
        }
      }
    }
    if (v == null) {
      v = "";
    }
    for (Method method : Sample.class.getMethods()) {
      if (method.getName().equals(info.getSetterName())) {
        Class<?>[] pType  = method.getParameterTypes();
        if (pType.length == 1) {
          if (pType[0].equals(Integer.class)) {
            if (v.toString().length() == 0) {
              v = null;
            } else {
              v = Integer.parseInt(v.toString());
            }
          } else if (pType[0].equals(BigDecimal.class)) {
            if (v.toString().length() == 0) {
              v = null;
            } else {
              v = new BigDecimal(v.toString());
            }
          } // else assume string.
          method.invoke(sample,  v);
          break;
        }
      }
    }

  }

  private void setAnnotationValue(ColumnInfo info, Sample sample, String value) {
    HashMap annotations = (HashMap)annotationMap.get(sample.getIdSampleString());
    if (annotations == null) {
      annotations = new HashMap();
    }
    HashMap annotationsToDelete = (HashMap)annotationsToDeleteMap.get(sample.getIdSampleString());
    if (annotationsToDelete == null) {
      annotationsToDelete = new HashMap();
    }
    if (info.getType().equals(PropertyType.MULTI_OPTION)) {
      Property p = info.getProperty();
      String[] values = value.split(",");
      String newValue = "";
      for(String v : values) {
        v = v.trim();
        for(Iterator i = p.getOptions().iterator(); i.hasNext(); ) {
          PropertyOption o = (PropertyOption)i.next();
          if (v.equals(o.getDisplay())) {
            if (newValue.length() != 0) {
              newValue += ",";
            }
            newValue += o.getIdPropertyOption().toString();
            break;
          }
        }
        value = newValue;
      }
    } else if(info.getType().equals(PropertyType.OPTION)) {
      Property p = info.getProperty();
      String newValue = "";
      for(Iterator i = p.getOptions().iterator(); i.hasNext(); ) {
        PropertyOption o = (PropertyOption)i.next();
        if (value.equals(o.getDisplay())) {
          newValue += o.getIdPropertyOption().toString();
          break;
        }
      }
      value = newValue;
    }
    // only store annotation if non blank value.
    if (value != null && !value.equals("")) {
      annotations.put(info.getProperty().getIdProperty(), value);
    }
    annotationMap.put(sample.getIdSampleString(), annotations);

    // delete old value of annotation even if new value is blank.
    annotationsToDelete.put(info.getProperty().getIdProperty(), value);
    annotationsToDeleteMap.put(sample.getIdSampleString(), annotationsToDelete);
  }

  public Document toXMLDocument() {
    Element sampleSheetNode = new Element("SampleSheet");

    if (!this.fatalError()) {
      sampleSheetNode.addContent(getHeadersNode());
      sampleSheetNode.addContent(getRowsNode());
      sampleSheetNode.addContent(getRequestsNode());
    }

    sampleSheetNode.addContent(getErrorsNode());

    return new Document(sampleSheetNode);
  }

  private Element getHeadersNode() {
    Element headersNode = new Element ("Headers");
    for(Integer columnOrdinal : columnMap.keySet()) {
      ColumnInfo info = columnMap.get(columnOrdinal);
      Element headerNode = new Element("Header");
      headersNode.addContent(headerNode);
      headerNode.setAttribute("columnOrdinal", columnOrdinal.toString());
      headerNode.setAttribute("header", info.getHeaderName());
    }
    return headersNode;
  }

  private Element getRowsNode() {
    Element rowsNode = new Element("Rows");
    for (Integer rowOrdinal = 1; rowOrdinal <= getNumRows(); rowOrdinal++) {
      Element rowNode = new Element("Row");
      rowsNode.addContent(rowNode);
      rowNode.setAttribute("rowOrdinal", rowOrdinal.toString());
      for(Integer columnOrdinal = 0; columnOrdinal < columnMap.keySet().size(); columnOrdinal++) {
        ColumnInfo info = columnMap.get(columnOrdinal);
        String value = info.getValue(rowOrdinal);
        if (value == null) value = "";
        rowNode.setAttribute("n" + columnOrdinal.toString(), value);
        if (columnOrdinal.equals(this.requestNumberOrdinal)) {
          rowNode.setAttribute("requestNumber", value);
        }
        if (columnOrdinal.equals(this.sampleNumberOrdinal)) {
          rowNode.setAttribute("sampleNumber", value);
        }
      }
    }

    return rowsNode;
  }

  protected abstract Integer getNumRows();

  private Element getRequestsNode() {
    Element requestsNode = new Element("Requests");
    for(String requestNumber : requestMap.keySet()) {
      Request request = requestMap.get(requestNumber);
      String reqValid = this.hasRequestError(requestNumber) ? "N" : "Y";
      Element requestNode = new Element("Request");
      requestsNode.addContent(requestNode);
      requestNode.setAttribute("includeFlag", reqValid);
      requestNode.setAttribute("idRequest", request == null ? "" : request.getIdRequest().toString());
      requestNode.setAttribute("requestNumber", requestNumber);
      requestNode.setAttribute("codeRequestCategory", request == null ? "" : request.getCodeRequestCategory());
      requestNode.setAttribute("name", request == null ? "" : request.getName());
      requestNode.setAttribute("description", request == null ? "" :  request.getDescription());
      requestNode.setAttribute("codeApplication", request == null || request.getCodeApplication() == null ? "" : request.getCodeApplication());
      requestNode.setAttribute("numUnmodifiedSamples", this.numberSamplesUnchanged(request).toString());
      requestNode.setAttribute("numUpdatedSamples", this.numberSamplesUpdated(request).toString());
      requestNode.setAttribute("numCreatedSamples", this.numberSamplesCreated(request).toString());
      requestNode.setAttribute("numErrors", this.numberErrors(requestNumber).toString());
      requestNode.setAttribute("enableCheckBox", reqValid);
    }

    return requestsNode;
  }

  private Element getErrorsNode() {
    Element errorsNode = new Element("Errors");
    for(Error error : this.errors) {
      String colName = "";
      if (error.getColumnOrdinal() != null) {
        ColumnInfo info = columnMap.get(error.getColumnOrdinal());
        if (info != null) {
          colName = info.getHeaderName();
        }
      }
      Element errorNode = new Element("Error");
      errorsNode.addContent(errorNode);
      errorNode.setAttribute("rowOrdinal", error.getRowOrdinal() == null ? "" : error.getRowOrdinal().toString());
      errorNode.setAttribute("columnOrdinal", error.getColumnOrdinal() == null ? "" : error.getColumnOrdinal().toString());
      errorNode.setAttribute("status", error.getStatus());
      errorNode.setAttribute("message", error.getMessage());
      errorNode.setAttribute("requestNumber", error.getRequestNumber() == null ? "" : error.getRequestNumber());
      errorNode.setAttribute("sampleNumber", error.getSampleNumber() == null ? "" : error.getSampleNumber());
      errorNode.setAttribute("header", colName);
    }

    return errorsNode;
  }

  private Integer numberSamplesUnchanged(Request request) {
    Integer num = 0;
    if (request != null) {
      for(Iterator i = request.getSamples().iterator(); i.hasNext(); ) {
        Sample sample = (Sample)i.next();
        if (sample.getIdSampleString() == null) {
          num++;
        }
      }
    }

    return num;
  }

  private Integer numberSamplesUpdated(Request request) {
    Integer num = 0;
    if (request != null) {
      for(Iterator i = request.getSamples().iterator(); i.hasNext(); ) {
        Sample sample = (Sample)i.next();
        if (sample.getIdSampleString() != null && sample.getIdSample() != null) {
          num++;
        }
      }
    }

    return num;
  }


  private Integer numberSamplesCreated(Request request) {
    Integer num = 0;
    if (request != null) {
      for(Sample sample : requestSampleMap.get(request.getNumber())) { 
        if (sample != null && sample.getIdSampleString() != null && sample.getIdSample() == null) {
          num++;
        }
      }
    }

    return num;
  }

  public Integer numberErrors(String requestNumber) {
    Integer num = 0;
    for(Error error : this.errors) {
      if (error.getRequestNumber() != null && error.getRequestNumber().equals(requestNumber)) {
        num++;
      }
    }

    return num;
  }

  public void setErrorSampleNumbersAfterCreate() {
    for(String requestNumber : this.requestSampleMap.keySet()) {
      List<Sample> samples = requestSampleMap.get(requestNumber);
      for(Sample sample: samples) {
        if (sample != null && sample.getIdSampleString() != null && sample.getIdSampleString().startsWith("Sample") && sample.getIdSample() != null) {
          // for new samples, the idSampleString is SAMPLE<rowOrdinal>
          Integer rowOrdinal = Integer.parseInt(sample.getIdSampleString().substring(6));
          // This is a new sample that was stored and now has a correct sample number
          for(Error error : errors) {
            if (error.getRowOrdinal() != null && error.getRowOrdinal().equals(rowOrdinal)) {
              error.setSampleNumber(sample.getNumber());
            }
          }
        }
      }
    }
  }

  protected class Error implements Serializable {
    public static final String FATAL = "Fatal";
    public static final String REQUEST_ERROR = "Request Error";
    public static final String ROW_ERROR = "Row Error";
    public static final String COLUMN_ERROR = "Column Warning";
    public static final String CELL_ERROR = "Cell Warning";

    private String status;
    private String message;
    private Integer rowOrdinal;
    private Integer columnOrdinal;
    private String requestNumber;
    private String sampleNumber;

    public Error(String status, String message) {
      this.status = status;
      this.message = message;
      this.rowOrdinal = null;
      this.columnOrdinal = null;
      this.requestNumber = null;
      this.sampleNumber = null;
    }

    public Error(String status, String message, Integer rowOrdinal, Integer columnOrdinal) {
      this.status = status;
      this.message = message;
      this.rowOrdinal = rowOrdinal;
      this.columnOrdinal = columnOrdinal;
      this.requestNumber = null;
      this.sampleNumber = null;
    }

    public Error(String status, String message, Integer rowOrdinal, Integer columnOrdinal, String requestNumber, String sampleNumber) {
      this.status = status;
      this.message = message;
      this.rowOrdinal = rowOrdinal;
      this.columnOrdinal = columnOrdinal;
      this.requestNumber = requestNumber;
      this.sampleNumber = sampleNumber;
    }

    public String getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }

    public Integer getRowOrdinal() {
      return rowOrdinal;
    }

    public Integer getColumnOrdinal() {
      return columnOrdinal;
    }

    public String getRequestNumber() {
      return requestNumber;
    }

    public String getSampleNumber() {
      return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
      this.sampleNumber = sampleNumber;
    }
  }

  protected class ColumnInfo implements Serializable {
    public static final String NUMERIC = "Numeric";

    private Integer ordinal;
    private String headerName;
    private String name;
    private Property property;
    private String type;
    private String dictionaryName;
    private Map<Integer, String> valueMap;

    public ColumnInfo(Integer ordinal, String headerName, String name, String type, String dictionaryName) {
      init(ordinal, headerName, name, type, null, dictionaryName);
    }

    public ColumnInfo(Integer ordinal, Property property) {
      init(ordinal, property.getName(), null, property.getCodePropertyType(), property, null);
    }

    private void init(Integer ordinal, String headerName, String name, String type, Property property, String dictionaryName) {
      this.ordinal = ordinal;
      this.headerName = headerName;
      this.name = name;
      this.type = type;
      this.property = property;
      this.dictionaryName = dictionaryName;
      this.valueMap = new HashMap<Integer, String>();
    }

    public Integer getOrdinal() {
      return ordinal;
    }

    public String getHeaderName() {
      return headerName;
    }

    public String getName() {
      return name;
    }

    public String getSetterName() {
      String setter = "";
      if (name != null && name.length() > 0) {
        setter = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
      }
      return setter;
    }

    public String getType() {
      return type;
    }

    public Property getProperty() {
      return property;
    }

    public String getDictionaryName() {
      return dictionaryName;
    }

    public void addValue(Integer rowOrdinal, String value) {
      valueMap.put(rowOrdinal, value);
    }

    public String getValue(Integer rowOrdinal) {
      return valueMap.get(rowOrdinal);
    }
  }
}
