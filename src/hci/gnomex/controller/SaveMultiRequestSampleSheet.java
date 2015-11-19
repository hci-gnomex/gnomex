package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.parsers.MultiRequestSampleSheetXMLParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class SaveMultiRequestSampleSheet extends GNomExCommand implements Serializable {
   
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveMultiRequestSampleSheet.class);
  
  private Document hdrDoc = null;
  private Document rowDoc = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("sampleSheetHeaderXMLString") != null && !request.getParameter("sampleSheetHeaderXMLString").equals("")) {
      String sampleSheetHeaderXMLString = request.getParameter("sampleSheetHeaderXMLString");
      StringReader reader = new StringReader(sampleSheetHeaderXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        hdrDoc = sax.build(reader);
      } catch (JDOMException je ) {
        log.error( "SaveMultiRequestSampleSheet: Cannot parse headers", je );
        this.addInvalidField( "Headers", "Invalid headers xml");
      }
    } else {
      log.error("SaveMultiRequestSampleSheet: sampleSheetHeaderXMLString not specified.");
      this.addInvalidField("Headers", "Headers not specifed.");
    }

    if (request.getParameter("sampleSheetRowXMLString") != null && !request.getParameter("sampleSheetRowXMLString").equals("")) {
      String sampleSheetRowXMLString = request.getParameter("sampleSheetRowXMLString");
      StringReader reader = new StringReader(sampleSheetRowXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        rowDoc = sax.build(reader);
      } catch (JDOMException je ) {
        log.error( "SaveMultiRequestSampleSheet: Cannot parse rows", je );
        this.addInvalidField( "Rows", "Invalid rows xml");
      }
    } else {
      log.error("SaveMultiRequestSampleSheet: sampleSheetRowXMLString not specified.");
      this.addInvalidField("Rows", "Rows not specifed.");
    }
  }
  
  public Command execute() throws RollBackCommandException {
    Session sess = null;
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      MultiRequestSampleSheetXMLParser parser = new MultiRequestSampleSheetXMLParser(hdrDoc, rowDoc, this.getSecAdvisor());
      
      parser.parse(sess);

      Map<String, Request> requestMap = parser.getRequestMap();
      Map<String, Map> annotationMap = parser.getAnnotationMap();
      Map<String, Map> annotationsToDeleteMap = parser.getAnnotationsToDeleteMap();
      for(String requestNumber : requestMap.keySet()) {
        Request request = requestMap.get(requestNumber);
        Integer nextSampleNumber = getStartingNextSampleNumber(request);
        for(Sample sample : parser.getModifiedSamplesForRequest(requestNumber)) {
          // idSampleString only set on new or modified samples
          if (sample != null && sample.getIdSampleString() != null) {
            Boolean isNewSample = false;
            if (sample.getIdSample() == null) {
              isNewSample = true;
            }
            nextSampleNumber = SaveRequest.initSample(sess, request, sample, isNewSample, nextSampleNumber);
            Map sampleAnnotations = annotationMap.get(sample.getIdSampleString());
            Map sampleAnnotationsToDelete = annotationsToDeleteMap.get(sample.getIdSampleString());
            if (sampleAnnotations != null) {
              SaveRequest.setSampleProperties(sess, request, sample, isNewSample, sampleAnnotations, 
                  null, sampleAnnotationsToDelete, dh.getPropertyMap());
            }
            sess.flush();
          }
        }
      }
      
      // Set sample numbers on errors for any samples created.
      parser.setErrorSampleNumbersAfterCreate();
      
      Document doc = parser.toXMLDocument();
      XMLOutputter xmlOut = new XMLOutputter();
      this.xmlResult = xmlOut.outputString(doc);   

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
      
    } catch (Exception e){
      log.error("An exception has occurred while saving a Multi-Request sample sheet. ", e);
      throw new RollBackCommandException(e.toString());      
    } finally {
      try {
       
        if (sess != null) {
          HibernateSession.closeSession();
        }
      } catch(Exception e) {
        
      }
    }

    return this;
  }
  
  private Integer getStartingNextSampleNumber(Request request) {
    Integer nextSampleNumber = 0;
    for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      // Don't look at new samples
      if (sample.getIdSample() != null) {
        String numberAsString = sample.getNumber();
        if (numberAsString != null && numberAsString.length() != 0 && numberAsString.indexOf("X") > 0) {
          numberAsString = numberAsString.substring(numberAsString.indexOf("X") + 1);
          try {
            Integer number = Integer.parseInt(numberAsString);
            if (number > nextSampleNumber) {
              nextSampleNumber = number;
            }
          } catch(Exception ex) {}
        }
      }
    }
    nextSampleNumber++;
    
    return nextSampleNumber;
  }
}
