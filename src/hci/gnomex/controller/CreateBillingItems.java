package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.billing.BillingPlugin;
import hci.gnomex.controller.SaveRequest.LabeledSampleComparator;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Label;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.LabelingReactionSize;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HybNumberComparator;
import hci.gnomex.utility.RequestParser;
import hci.gnomex.utility.SampleNumberComparator;
import hci.gnomex.utility.SequenceLaneNumberComparator;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class CreateBillingItems extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateBillingItems.class);
  
  private Integer          idRequest;
  private Integer          idBillingPeriod;
  private String           requestXMLString;
  private Document         requestDoc;
  private RequestParser    requestParser;
  

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    }
    
    if (request.getParameter("requestXMLString") != null && !request.getParameter("requestXMLString").equals("")) {
      requestXMLString = request.getParameter("requestXMLString");
      this.requestXMLString = this.requestXMLString.replaceAll("&", "&amp;");
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
    
    
    if (request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    }
    
    if (idRequest == null && requestParser == null) {
      this.addInvalidField("idRequest", "idRequest or RequestXMLString is required.");
    }
    
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
          
          List billingItems = new ArrayList<BillingItem>();
 
          DictionaryHelper dh = DictionaryHelper.getInstance(sess);
          NumberFormat nf = NumberFormat.getCurrencyInstance();
          HashMap labelMap = new HashMap();
          
          // Get microarray labels
          List labels = sess.createQuery("SELECT label from Label label").list();
          for(Iterator i = labels.iterator(); i.hasNext();) {
            Label l = (Label)i.next();
            labelMap.put(l.getLabel(), l.getIdLabel());
          }


          // Get the current billing period
          BillingPeriod billingPeriod = null;
          if (idBillingPeriod == null) {
            billingPeriod = dh.getCurrentBillingPeriod();
          } else {
            billingPeriod = dh.getBillingPeriod(idBillingPeriod);
          }
          if (billingPeriod == null) {
            throw new RollBackCommandException("Cannot find current billing period in dictionary");
          }

          // Read the experiment
          Request request = null;
          Set hybs = null;
          Set samples = null;
          Set lanes = null;
          Set labeledSamples = null;
          int x = 0;

          Map labeledSampleChannel1Map = new HashMap();
          Map labeledSampleChannel2Map = new HashMap();
          if (idRequest != null) {
            request = (Request)sess.get(Request.class, idRequest);
            
            samples = request.getSamples();
            hybs = request.getHybridizations();
            lanes = request.getSequenceLanes();
          } else {
            requestParser.parse(sess);
            request = requestParser.getRequest();
            request.setIdRequest(new Integer(0));
            request.setNumber("");

            
            // Plugin assumes slide product initialized on request
            if (request.getIdSlideProduct() != null) {
              SlideProduct slideProduct = (SlideProduct)sess.load(SlideProduct.class, request.getIdSlideProduct());
              request.setSlideProduct(slideProduct);
            }

            
            hybs = new TreeSet(new HybComparator());
            samples = new TreeSet(new SampleComparator());
            lanes = new TreeSet(new LaneComparator());
            labeledSamples = new TreeSet(new LabeledSampleComparator());

            
            // Parse the samples
            x = 0;
            for(Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
              String idSampleString = (String)i.next();
              Sample sample = (Sample)requestParser.getSampleMap().get(idSampleString);
              sample.setIdSample(new Integer(x++));
              samples.add(sample);
            }
            
            // Parse the hybs. Plugin just need a thinly initialized Hyb for count purposes in the
            x = 0;
            for(Iterator i = requestParser.getHybInfos().iterator(); i.hasNext();) {
              RequestParser.HybInfo hybInfo = (RequestParser.HybInfo)i.next();
              Hybridization hyb = new Hybridization();
              hyb.setIdHybridization(new Integer(x++));
              if (hybInfo.getIdSampleChannel1String() != null && !hybInfo.getIdSampleChannel1String().equals("") && !hybInfo.getIdSampleChannel1String().equals("0")) {
                labeledSampleChannel1Map.put(hybInfo.getIdSampleChannel1String(), null);                
              }
              if (hybInfo.getIdSampleChannel2String() != null && !hybInfo.getIdSampleChannel2String().equals("") && !hybInfo.getIdSampleChannel2String().equals("0") ) {
                labeledSampleChannel2Map.put(hybInfo.getIdSampleChannel2String(), null);       
              }
              hybs.add(hyb);
            }
            
            // Use the hybs to initalize the set if labeled samples.
            // Plugin just need number of labeled samples
            x = 0;
            for(Iterator i = labeledSampleChannel1Map.keySet().iterator(); i.hasNext();) {
              Object key = i.next();
              LabeledSample ls = new LabeledSample();
              ls.setIdLabeledSample(new Integer(x++));
              ls.setIdLabel((Integer)labelMap.get("Cy3"));
              ls.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
              ls.setNumberOfReactions(new Integer(1));
              labeledSamples.add(ls);
            }
            for(Iterator i = labeledSampleChannel2Map.keySet().iterator(); i.hasNext();) {
              Object key = i.next();
              LabeledSample ls = new LabeledSample();
              ls.setIdLabeledSample(new Integer(x++));
              ls.setIdLabel((Integer)labelMap.get("Cy5"));
              ls.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
              ls.setNumberOfReactions(new Integer(1));
              labeledSamples.add(ls);
            }
            
            // Parse the sequence lanes
            x = 0;
            for(Iterator i = requestParser.getSequenceLaneInfos().iterator(); i.hasNext();) {
              RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo)i.next();
              SequenceLane lane = new SequenceLane();
              lane.setIdSequenceLane(new Integer(x++));
              lane.setIdNumberSequencingCycles(laneInfo.getIdNumberSequencingCycles());
              lane.setIdSeqRunType(laneInfo.getIdSeqRunType());              
              lanes.add(lane);
            }
              
            
          }
          
          
          // Find the appropriate price sheet
          PriceSheet priceSheet = null;
          List priceSheets = sess.createQuery("SELECT ps from PriceSheet as ps").list();
          for(Iterator i = priceSheets.iterator(); i.hasNext();) {
            PriceSheet ps = (PriceSheet)i.next();
            for(Iterator i1 = ps.getRequestCategories().iterator(); i1.hasNext();) {
              RequestCategory requestCategory = (RequestCategory)i1.next();
              if(requestCategory.getCodeRequestCategory().equals(request.getCodeRequestCategory())) {
                priceSheet = ps;
                break;
              }
              
            }
          }
          
          if (priceSheet != null) {
            
            
            for(Iterator i1 = priceSheet.getPriceCategories().iterator(); i1.hasNext();) {
              PriceSheetPriceCategory priceCategoryX = (PriceSheetPriceCategory)i1.next();
              PriceCategory priceCategory = priceCategoryX.getPriceCategory();
              
              // Ignore inactive price categories
              if (priceCategory.getIsActive() != null && priceCategory.getIsActive().equals("N")) {
                continue;
              }

              
              // Instantiate plugin for billing category
              BillingPlugin plugin = null;
              if (priceCategory.getPluginClassName() != null) {
                try {
                  plugin = (BillingPlugin)Class.forName(priceCategory.getPluginClassName()).newInstance();
                } catch(Exception e) {
                  log.error("Unable to instantiate billing plugin " + priceCategory.getPluginClassName());
                }
                
              }
              
              // Get the billing items
              if (plugin != null) {
                List billingItemsForCategory = plugin.constructBillingItems(sess, null, billingPeriod, priceCategory, request, samples, labeledSamples, hybs, lanes);
                billingItems.addAll(billingItemsForCategory);                
              }
            }
            
          }
          
            
          Document doc = new Document(new Element("NewBilling"));
          
          
          Element requestNode = new Element("Request");
          requestNode.setAttribute("idRequest", request.getIdRequest().toString());
          requestNode.setAttribute("idBillingAccount", request.getIdBillingAccount().toString());
          requestNode.setAttribute("requestNumber", request.getNumber());
          requestNode.setAttribute("idLab", request.getIdLab().toString());
          requestNode.setAttribute("label", request.getNumber());
          requestNode.setAttribute("submitter", request.getAppUser() != null ? request.getAppUser().getDisplayName() : "");
          requestNode.setAttribute("codeRequestCategory", request.getCodeRequestCategory());        
          requestNode.setAttribute("billingLabName", request.getLabName());        
          requestNode.setAttribute("billingAccountName", request.getBillingAccount() != null ? request.getBillingAccount().getAccountName() : "");        
          requestNode.setAttribute("status", BillingStatus.NEW);
          requestNode.setAttribute("isDirty", "Y");
          doc.getRootElement().addContent(requestNode);          

          
          BigDecimal grandTotalPrice = new BigDecimal(0);
          for(Iterator i = billingItems.iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            Element billingItemNode = bi.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
            if (bi.getTotalPrice() != null) {
              grandTotalPrice = grandTotalPrice.add(bi.getTotalPrice());
              billingItemNode.setAttribute("totalPrice", nf.format(bi.getTotalPrice().doubleValue()));        
            }            
            requestNode.addContent(billingItemNode);
          }

          requestNode.setAttribute("totalPrice", nf.format(grandTotalPrice.doubleValue()));
      
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
      
          setResponsePage(this.SUCCESS_JSP);
          
          // We don't want to save anything;
          sess.clear();
          
      
        } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to create billing items");
      }
    }catch (NamingException e){
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }
  
  public class SampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Sample s1 = (Sample)o1;
      Sample s2 = (Sample)o2;
      return s1.getIdSample().compareTo(s2.getIdSample());
      
    }
  }
  public class LabeledSampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      LabeledSample ls1 = (LabeledSample)o1;
      LabeledSample ls2 = (LabeledSample)o2;
      return ls1.getIdLabeledSample().compareTo(ls2.getIdLabeledSample());
      
    }
  }
  public class HybComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Hybridization h1 = (Hybridization)o1;
      Hybridization h2 = (Hybridization)o2;
      return h1.getIdHybridization().compareTo(h2.getIdHybridization());
      
    }
  }
  public class LaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      SequenceLane l1 = (SequenceLane)o1;
      SequenceLane l2 = (SequenceLane)o2;
      return l1.getIdSequenceLane().compareTo(l2.getIdSequenceLane());
      
    }
  }
}