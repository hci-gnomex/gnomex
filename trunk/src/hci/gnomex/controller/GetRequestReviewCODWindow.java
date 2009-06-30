/**
 * 
 */
package hci.gnomex.controller;

import hci.altio.xml.actions.AL_Actions;
import hci.altio.xml.actions.ActNewWindow;
import hci.altio.xml.controls.AltioDetailObject;
import hci.altio.xml.controls.Label;
import hci.altio.xml.controls.Rectangle;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * @author tdisera
 *
 */
public class GetRequestReviewCODWindow extends GNomExCommand implements Serializable {
  
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequestReviewCODWindow.class);
  
  private String           requestXMLString;
  private Document         requestDoc;
  


  private RequestParser    requestParser;
  private DictionaryHelper dictionaryHelper;
  
  
  
  private int        x = 0;
  private int        y = 0;
  
  private final int              START_Y = 97;
  private final static String   REVIEW_BODY_HEADER = "REVIEW_BODY_HEADER";
  private final static String   REVIEW_BODY_COL_HEADER = "REVIEW_BODY_COL_HEADER";
  private final static String   REVIEW_BODY_SECTION_HEADER = "REVIEW_BODY_SECTION_HEADER";
  
  /* (non-Javadoc)
   * @see hci.framework.control.Command#validate()
   */
  public void validate() {
    
    
  }
  
  /* (non-Javadoc)
   * @see hci.framework.control.Command#loadCommand(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession)
   */
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("requestXMLString") != null && !request.getParameter("requestXMLString").equals("")) {
      requestXMLString = request.getParameter("requestXMLString");
      
    }
    
    StringReader reader = new StringReader(requestXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      requestDoc = sax.build(reader);
      requestParser = new RequestParser(requestDoc, this.getSecAdvisor());
    } catch (JDOMException je ) {
      log.error( "Cannot parse requestXMLString", je );
      this.addInvalidField( "RequestXMLString", "Invalid request xml");
    }
    
   
  }
  
  /* (non-Javadoc)
   * @see hci.framework.control.Command#execute()
   */
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      dictionaryHelper = DictionaryHelper.getInstance(sess);
     
      requestParser.parse();

      
      if (isValid()) {
          
          Element data = new Element("DATA");
          Document doc = new Document().setRootElement(data);
          Element al_config = new Element("AL_CONFIG");
          
          Element windows = new Element("WINDOWS");
          Document windowDoc = loadTemplate("gnomexRequestReview_template.xml");
          Element window = windowDoc.getRootElement();
          windows.addContent(window);
          
          
          
          Element panel = null;
          for( Iterator i1 = getDescendants(window, new ElementFilter("PANEL")).iterator(); i1.hasNext();) {
            panel = (Element) i1.next();
            break;
          }
          
          
          x = 5;
          y = START_Y;
          addSampleHeader(panel);
          
          int sampleCount = 0;
          for(Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
            String idSampleString = (String)i.next();
            Sample sample = (Sample)requestParser.getSampleMap().get(idSampleString);

            addSampleLabel(panel, sampleCount, idSampleString, sample);            

            sampleCount++;
          }
          
          
          int hybCount = 0;
          if (!requestParser.getHybInfos().isEmpty()) { 
            
            addHybHeader(panel);

            for(Iterator i = requestParser.getHybInfos().iterator(); i.hasNext();) {
              RequestParser.HybInfo hybInfo = (RequestParser.HybInfo)i.next();
              addHybLabel(panel, hybCount, hybInfo);
              hybCount++;
            }            
          }

          
          AL_Actions actions = new AL_Actions();
          actions.addAction(new ActNewWindow("requestReviewWindow", "/OpenRequestList/Request[@idRequest=\"0\"]"));          
          
          
          al_config.addContent(windows);
          data.addContent(al_config);
           
          
          data.addContent(actions.toXMLDocument(null,AltioDetailObject.DATE_OUTPUT_SQL).getRootElement());
          XMLOutputter out = new XMLOutputter();
          this.xmlResult = out.outputString(doc);

          
      }
      
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (HibernateException e) {
      e.printStackTrace();
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (NamingException e) {
      e.printStackTrace();
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (SQLException e) {
      e.printStackTrace();
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (XMLReflectException e) {
      e.printStackTrace();
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } 
    finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      }
      catch (Exception ex) {
        log.error("Exception trying to close the Hibernate session: "+ ex);
      }
    }
    
    return this;
  }
  
  private void addLabel(Element panel, String labelName, String caption, int height, int width, String captionFontStyle) throws Exception {
    Label label = null;
    if (captionFontStyle != null) {
      label = new Label(labelName, caption, x, y, height, width, captionFontStyle, captionFontStyle);
    } else {
      label = new Label(labelName, caption, x, y, height, width);  
    }
    panel.addContent(label.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement());
    
    
    
    if (captionFontStyle != null && captionFontStyle.equals(REVIEW_BODY_COL_HEADER)) {
      Rectangle rect = new Rectangle();
      rect.setX(x);
      rect.setY(y);
      rect.setW(width);
      rect.setH(height);
      rect.setFILLCOL("0xB0C4DE");
      rect.setCOLOR("0xB0C4DE");
      panel.addContent(rect.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement());      
    }
    
    
    x += width + 4;
  }
  
  
  private void addLabel(Element panel, String labelName, String caption, int height, int width) throws Exception {
    addLabel(panel, labelName, caption, height, width, null);    
  }
  
  private void newLine() {
    y += 18;  
    x = 5;
  }
  
  private static synchronized Document loadTemplate(String templateName) {
    Document doc = null;

    try {
      File xmlFile = new File("CODTemplates/" + templateName);
      SAXBuilder parser = new SAXBuilder();
      doc = parser.build(xmlFile);
    } catch (JDOMException e) {
      System.out.println(e.getMessage());
    }

    return doc;
  }
  
    
  
  private void addSampleHeader(Element panel) throws Exception {
    addLabel(panel, "LABEL_SAMPLES",  "Samples", 20, 100, this.REVIEW_BODY_SECTION_HEADER);
    newLine();
    
    x = x + 6;
    addLabel(panel, "HEADER_SCOUNT",  " ",                  15, 18,  REVIEW_BODY_COL_HEADER);
    addLabel(panel, "HEADER_SNAME",   "Name",               15, 100, REVIEW_BODY_COL_HEADER);
    addLabel(panel, "HEADER_CONC",     "Conc.",             15, 60,  REVIEW_BODY_COL_HEADER);
    addLabel(panel, "HEADER_CONCU",   "Unit",               15, 40,  REVIEW_BODY_COL_HEADER);      
    addLabel(panel, "HEADER_SST",     "Sample Type",        15, 120, REVIEW_BODY_COL_HEADER);
    if (!requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      addLabel(panel, "HEADER_SORG",    "Organism",           15, 120, REVIEW_BODY_COL_HEADER);
      addLabel(panel, "HEADER_SSRC",    "Source",             15, 100,  REVIEW_BODY_COL_HEADER);      
    }
    addLabel(panel, "HEADER_SSPM",    "Sample Prep Meth",   15, 120, REVIEW_BODY_COL_HEADER);
    if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      addLabel(panel, "HEADER_SCHIP", "Chip Type",          15, 120, REVIEW_BODY_COL_HEADER);      
    }
    if (requestParser.getShowTreatments() && !requestParser.getSampleTreatmentMap().isEmpty()) {
      addLabel(panel, "HEADER_STREAT", "Treatment",         15, 300, REVIEW_BODY_COL_HEADER);
    }
    
    int acount = 0;
    for(Iterator i1 = requestParser.getSampleAnnotationCodeMap().keySet().iterator(); i1.hasNext();) {
      String code = (String)i1.next();
      
      String label = (String)dictionaryHelper.getSampleCharacteristicMap().get(code);
      if (code.equals(SampleCharacteristic.OTHER) && requestParser.getOtherCharacteristicLabel() != null) {
        label = requestParser.getOtherCharacteristicLabel();
      }

      addLabel(panel, "LABEL_ANNOT" + acount, label,        15, 90, REVIEW_BODY_COL_HEADER);


      acount++;
    }

    addLabel(panel, "HEADER_SDESC",   "Description",        15, 300, REVIEW_BODY_COL_HEADER);

    newLine();
    
  }
  
  
  private void addSampleLabel(Element panel, int count, String idSampleString, Sample sample) throws Exception {
    
    x = x + 6;
    addLabel(panel, "LABEL_SCNT" + count, (count + 1) + ".",           15, 18);
    addLabel(panel, "LABEL_NAME" + count, sample.getName(),            15, 100);
    addLabel(panel, "LABEL_conc"  + count, sample.getConcentration() != null ? new Integer(sample.getConcentration().intValue()).toString() : "",   15, 60);
    addLabel(panel, "LABEL_concu" + count, sample.getCodeConcentrationUnit(),   15, 40);
    addLabel(panel, "LABEL_ST" + count,   dictionaryHelper.getSampleType(sample),       15, 120);
    if (!requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      addLabel(panel, "LABEL_SORG" + count,   dictionaryHelper.getOrganism(sample),       15, 120);
      addLabel(panel, "LABEL_SSRC" + count,  "",   15, 100);
    }
    addLabel(panel, "LABEL_SPM" + count,  dictionaryHelper.getSamplePrepMethod(sample), 15, 120);
    if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      addLabel(panel, "LABEL_SCHIP", dictionaryHelper.getChipTypeName(sample.getCodeBioanalyzerChipType()), 15, 120);      
    }
    if (requestParser.getShowTreatments() && !requestParser.getSampleTreatmentMap().isEmpty()) {
      addLabel(panel, "LABEL_STREAT", (String)requestParser.getSampleTreatmentMap().get(idSampleString), 15, 300);
    }
    
    int acount = 0;
    Map annotations = (Map)requestParser.getSampleAnnotationMap().get(idSampleString);
    for(Iterator i1 = requestParser.getSampleAnnotationCodeMap().keySet().iterator(); i1.hasNext();) {
      String code = (String)i1.next();
      String value = (String)annotations.get(code);
      if (value == null) {
        value = "";
      }

      addLabel(panel, "LABEL_ANNOT" + + count + "_" + acount, value,   15, 90);
      acount++;
    }
    
    addLabel(panel, "LABEL_DESC" + count, sample.getDescription(),     15, 300);
    
    newLine();

  }
  
  private void addHybHeader(Element panel) throws Exception {
    newLine();
    addLabel(panel, "LABEL_HYBS",            "Hybridizations", 20, 150, this.REVIEW_BODY_SECTION_HEADER);
    newLine();
     
    x = x + 6;
    addLabel(panel, "HEADER_HCOUNT",    " ",                        15, 18,  REVIEW_BODY_COL_HEADER);
    
    addLabel(panel, "HEADER_Cy3",       "Cy3 Sample",               15, 100, REVIEW_BODY_COL_HEADER);
    if (requestParser.getRequest().getCodeRequestCategory() == null ||
        (dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory()).getNumberOfChannels() != null &&
        dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory()).getNumberOfChannels().intValue() == 2)) {
      addLabel(panel, "HEADER_Cy5",       "Cy5 Sample",               15, 100, REVIEW_BODY_COL_HEADER);      
    }
    if (dictionaryHelper.getSlidesInSet(requestParser.getRequest().getIdSlideProduct()).intValue() > 1) {
      addLabel(panel, "HEADER_SlideD",  "Slide (in set)",           15, 320, REVIEW_BODY_COL_HEADER);            
    } else {
      addLabel(panel, "HEADER_SlideP",  "Slide",                    15, 320, REVIEW_BODY_COL_HEADER);            
    }
    addLabel(panel,  "HEADER_SlideSrc", "Slide Source",             15, 80,  REVIEW_BODY_COL_HEADER);
    addLabel(panel, "HEADER_HybNote",   "Notes",                    15, 300, REVIEW_BODY_COL_HEADER);
    newLine();    
  }
  
  
  
  private void addHybLabel(Element panel, int count, RequestParser.HybInfo hybInfo) throws Exception {

    Hybridization hyb = null;
    
    String sampleNameChannel1 = "";
    if (hybInfo.getSampleChannel1() != null) {
      sampleNameChannel1 = hybInfo.getSampleChannel1().getName();      
    }

    String sampleNameChannel2 = "";
    if (hybInfo.getSampleChannel2() != null) {
      sampleNameChannel2 = hybInfo.getSampleChannel2().getName();      
    }

    x = x + 6;
    addLabel(panel, "LABEL_HCNT" + count,     (count +1) + ".",                                   15, 18);
    addLabel(panel, "LABEL_Cy3" + count,      sampleNameChannel1,                                 15, 100);
    if (requestParser.getRequest().getCodeRequestCategory() == null ||
        (dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory()).getNumberOfChannels() != null &&
        dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory()).getNumberOfChannels().intValue() == 2)) {
      addLabel(panel, "LABEL_Cy5" + count,      sampleNameChannel2,                                 15, 100);
    }
    if (dictionaryHelper.getSlidesInSet(requestParser.getRequest().getIdSlideProduct()).intValue() > 1) {
      addLabel(panel, "LABEL_SlideD" + count, dictionaryHelper.getSlideDesignName(hybInfo.getIdSlideDesign()),    15, 320);      
    } else {
      addLabel(panel, "LABEL_SlideP" + count, dictionaryHelper.getSlideProductName(requestParser.getRequest().getIdSlideProduct()), 15, 320);
    }
    addLabel(panel, "LABEL_SlideUse" + count, dictionaryHelper.getSlideSource(hybInfo.getCodeSlideSource()), 15, 80);
    addLabel(panel, "LABEL_HybNote" + count,  hybInfo.getNotes(),                                 15, 300);
    newLine();
  }
  
  
  
 

  
  

  private static List getDescendants(Element e, ElementFilter filter) {
    ArrayList al = new ArrayList();

    Iterator i = e.getContent().iterator();
    while (i.hasNext()) {
      Object obj = i.next();
      if (obj instanceof Element) {
        if (filter == null || filter.matches(obj)) {
          // Add Element
          al.add(obj);
        }
        // Add its Descendants
        al.addAll(getDescendants((Element) obj, filter));
      }
    }

    return al;
  }
}
