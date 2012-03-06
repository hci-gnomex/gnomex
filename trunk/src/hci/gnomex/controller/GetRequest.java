package hci.gnomex.controller;

import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.dictionary.model.NullDictionaryEntry;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.ExperimentCollaborator;
import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SequenceLane;


public class GetRequest extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);
  
  private Integer idRequest;
  private String  requestNumber;
  private String  showUploads = "N";
  private String  serverName;
  private String  baseDir;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } 
    if (request.getParameter("requestNumber") != null && !request.getParameter("requestNumber").equals("")) {
      requestNumber = request.getParameter("requestNumber");
    } 
    if (request.getParameter("showUploads") != null && !request.getParameter("showUploads").equals("")) {
      showUploads = request.getParameter("showUploads");
    } 
    
    serverName = request.getServerName();
    
    
    if (idRequest == null && requestNumber == null) {
      this.addInvalidField("idRequest or requestNumber", "Either idRequest or requestNumber must be provided");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = dh.getMicroarrayDirectoryForWriting(serverName);
 
      // Find request
      boolean newRequest = false;
      
      Request request = null;
      if (idRequest != null && idRequest.intValue() == 0) {
        newRequest = true;
        request = new Request();
        request.setIdRequest(idRequest);
        request.canRead(true);
        request.canDelete(true);
        request.canUpdate(true);
      } else if (idRequest != null) {
        request = (Request)sess.get(Request.class, idRequest);
      } else {
        String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
        StringBuffer buf = new StringBuffer("SELECT req from Request as req where req.number like '" + requestNumberBase + "%' OR req.number = '" + requestNumberBase + "'");
        List requests = (List)sess.createQuery(buf.toString()).list();
        if (requests.size() == 0 && requestNumberBase.indexOf("R") == -1) {
        	// If nothing returned then try with "R" appended
        	requestNumberBase = requestNumberBase + "R";
            buf = new StringBuffer("SELECT req from Request as req where req.number like '" + requestNumberBase + "%' OR req.number = '" + requestNumberBase + "'");
            requests = (List)sess.createQuery(buf.toString()).list();
        }
        if (requests.size() > 0) {
          request = (Request)requests.get(0);
        }
      }      
      if (request != null) {
        // Make sure user has permission to view request
        if (!newRequest) {
          if (!this.getSecAdvisor().canRead(request)) {
            this.addInvalidField("perm", "Insufficient permission to access this request");
          }          
        }
          
        if (this.isValid()) {
          
          Hibernate.initialize(request.getSamples());
          Hibernate.initialize(request.getHybridizations());
          Hibernate.initialize(request.getAnalysisExperimentItems());
          Hibernate.initialize(request.getSeqLibTreatments());
         
          request.excludeMethodFromXML("getBillingItems");
         
          
          // If user can write the request, show collaborators. 
          if (this.getSecAdvisor().canUpdate(request)) {
            Hibernate.initialize(request.getCollaborators());
          } else {
            request.excludeMethodFromXML("getCollaborators");
          }
          
          if (!newRequest) {
            this.getSecAdvisor().flagPermissions(request);            
          }


          // Set number of seq lanes per sample
          for(Iterator i5 = request.getSamples().iterator(); i5.hasNext();) {
            Sample s = (Sample)i5.next();
            int seqLaneCount = 0;
            for(Iterator i6 = request.getSequenceLanes().iterator(); i6.hasNext();) {
              SequenceLane seqLane = (SequenceLane)i6.next();
              if (seqLane.getIdSample().equals(s.getIdSample())) {
                seqLaneCount++;
              }
            }
            s.setSequenceLaneCount(seqLaneCount);
          }
          
          // Generate xml
          Document doc = new Document(new Element("OpenRequestList"));
          Element requestNode = request.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          
          String accountNumberDisplay = "";
          
          if(request.getBillingAccount() != null) {
            accountNumberDisplay = request.getBillingAccount().getAccountNumberDisplay();
          }
          requestNode.setAttribute("accountNumberDisplay", accountNumberDisplay);

          // Initialize attributes from request category
          if (request.getCodeRequestCategory() != null && !request.getCodeRequestCategory().equals("")) {
            RequestCategory requestCategory = dh.getRequestCategoryObject(request.getCodeRequestCategory());
            requestNode.setAttribute("icon", requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
            requestNode.setAttribute("type", requestCategory.getType() != null ? requestCategory.getType() : "");            
          }
          
          // Show prepInstructions and analysisInstructions.  Although
          // these instructions are stored at the detailed sample and 
          // sequence lane level, the ability to edit the instructions
          // at this level has been disabled.  So just grab the first
          // instruction from the detail to show on the experiment.
          String prepInstructions = "";
          if (request.getSamples() != null && request.getSamples().size() > 0) {
            for (Iterator i = request.getSamples().iterator(); i.hasNext();) {
              Sample s = (Sample)i.next();
              if (s.getPrepInstructions() != null && !s.getPrepInstructions().equals("")) {
                prepInstructions = s.getPrepInstructions();
                break;                
              }
            }
          }
          requestNode.setAttribute("corePrepInstructions", prepInstructions);

          String analysisInstructions = "";
          if (request.getSequenceLanes() != null && request.getSequenceLanes().size() > 0) {
            for (Iterator i = request.getSequenceLanes().iterator(); i.hasNext();) {
              SequenceLane l = (SequenceLane)i.next();
              if (l.getAnalysisInstructions() != null && !l.getAnalysisInstructions().equals("")) {
                analysisInstructions = l.getAnalysisInstructions();
                break;                
              }
            }
          }
          //requestNode.setAttribute("analysisInstructions", analysisInstructions);

          
          // Show sequence lanes, organized by multiplex group or flow cell channel
          if (request.getSequenceLanes().size() > 0) {
            Element multiplexLanesNode = new Element("multiplexSequenceLanes");
            requestNode.addContent(multiplexLanesNode);
            SequenceLane.addMultiplexLaneNodes(multiplexLanesNode, request.getSequenceLanes(), request.getCreateDate());
          }
          
          // Show list of property entries
          Element scParentNode = new Element("PropertyEntries");
          requestNode.addContent(scParentNode);
          boolean hasCCNumber = false;
          boolean hasSampleDescription = false;
          for(Iterator i = dh.getPropertyList().iterator(); i.hasNext();) {
            Property prop = (Property)i.next();
            
            if (prop.getForSample() == null || !prop.getForSample().equals("Y")) {
              continue;
            }

            Element peNode = new Element("PropertyEntry");
            PropertyEntry entry = null;
            
            for(Iterator i1 = request.getSamples().iterator(); i1.hasNext();) {
              Sample sample = (Sample)i1.next();
              if(sample.getCcNumber()!=null && sample.getCcNumber().length() > 0) {
            	  hasCCNumber = true;
              }
              if(sample.getDescription()!=null && sample.getDescription().length() > 0) {
                hasSampleDescription = true;
              }
              for(Iterator i2 = sample.getPropertyEntries().iterator(); i2.hasNext();) {
                PropertyEntry propEntry = (PropertyEntry)i2.next();
                if (propEntry.getIdProperty().equals(prop.getIdProperty())) {
                  entry = propEntry;
                  break;
                }
              }
            }
            
            // Skip if property has no values for samples and is not active.
            if (entry == null && prop.getIsActive().equals("N")) {
              continue;
            }
            
            peNode.setAttribute("idProperty", prop.getIdProperty().toString());
            peNode.setAttribute("name", prop.getName());
            peNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
            peNode.setAttribute("isSelected", (prop.getIsRequired() != null && prop.getIsRequired().equals("Y")) || entry != null ? "true" : "false");
            peNode.setAttribute("isActive", prop.getIsActive() != null ? prop.getIsActive() : "Y");
                
            scParentNode.addContent(peNode);
            
          }
          requestNode.setAttribute("hasCCNumber", hasCCNumber ? "Y":"N");
          requestNode.setAttribute("hasSampleDescription", hasSampleDescription ? "Y":"N");
          
          // Show list of seq lib treatments
          Element stParentNode = new Element("SeqLibTreatmentEntries");
          requestNode.addContent(stParentNode);
          for(Iterator i1 = dh.getSeqLibTreatments().iterator(); i1.hasNext();) {
            Object de = i1.next();
            if (de instanceof NullDictionaryEntry) {
              continue;
            }
            SeqLibTreatment st = (SeqLibTreatment)de;
            
            if (st.getIsActive() != null && st.getIsActive().equalsIgnoreCase("N")) {
              continue;
            }

            Element stNode = (Element)st.toXMLDocument(null).getRootElement().clone();
            stParentNode.addContent(stNode);
            
            boolean isSelected = false;
            for(Iterator i2 = request.getSeqLibTreatments().iterator(); i2.hasNext();) {
              SeqLibTreatment theSeqLibTreatment = (SeqLibTreatment)i2.next();
              if (theSeqLibTreatment.getIdSeqLibTreatment().equals(st.getIdSeqLibTreatment())) {
                isSelected = true;
                break;
              } 
            }
            stNode.setAttribute("isSelected", isSelected ? "true" : "false");
          }          
          
          // Show list of protocols used on this experiment
          Element protocolsNode = new Element("protocols");
          requestNode.addContent(protocolsNode);
          for(Iterator i0 = request.getLabeledSamples().iterator(); i0.hasNext();) {
            LabeledSample ls = (LabeledSample)i0.next();
            if (ls.getIdLabelingProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", ls.getIdLabelingProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.LabelingProtocol");
              protocolNode.setAttribute("label", dh.getLabelingProtocol(ls.getIdLabelingProtocol()));
              break;
            }
          }
          for(Iterator i1 = request.getHybridizations().iterator(); i1.hasNext();) {
            Hybridization hyb = (Hybridization)i1.next();
            if (hyb.getIdHybProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdHybProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.HybProtocol");
              protocolNode.setAttribute("label", dh.getHybProtocol(hyb.getIdHybProtocol()));
              break;
            }
          }
          for(Iterator i2 = request.getHybridizations().iterator(); i2.hasNext();) {
            Hybridization hyb = (Hybridization)i2.next();
            if (hyb.getIdScanProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdScanProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.ScanProtocol");
              protocolNode.setAttribute("label", dh.getScanProtocol(hyb.getIdScanProtocol()));
              break;
            }
          }
          for(Iterator i3 = request.getHybridizations().iterator(); i3.hasNext();) {
            Hybridization hyb = (Hybridization)i3.next();
            if (hyb.getIdFeatureExtractionProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdFeatureExtractionProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.FeatureExtractionProtocol");
              protocolNode.setAttribute("label", dh.getFeatureExtractionProtocol(hyb.getIdFeatureExtractionProtocol()));
              break;
            }
          }          
          for(Iterator i4 = request.getSamples().iterator(); i4.hasNext();) {
            Sample s = (Sample)i4.next();
            if (s.getIdSeqLibProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", s.getIdSeqLibProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.SeqLibProtocol");
              protocolNode.setAttribute("label", dh.getSeqLibProtocol(s.getIdSeqLibProtocol()));
              break;
            }
          }
          
          // If user has write permission, show billing items for this request
          // Organize split accounts by lab
          if (this.getSecAdvisor().canUpdate(request)) {
            Element billingItemsNode = new Element("billingItems");
            requestNode.addContent(billingItemsNode);
            TreeMap billingItemMap = new TreeMap();
            for(Iterator i = request.getBillingItems().iterator(); i.hasNext();) {
              BillingItem bi = (BillingItem)i.next();
              String key = bi.getLabName() + "_%%%_" + bi.getAccountName();
              List billingItems = (List)billingItemMap.get(key);
              if (billingItems == null) {
                billingItems = new ArrayList();
                billingItemMap.put(key, billingItems);
              }
              billingItems.add(bi);
            }
            for(Iterator i = billingItemMap.keySet().iterator(); i.hasNext();) {
              String key = (String)i.next();
              String keyTokens[] = key.split("_%%%_");
              List billingItems = (List)billingItemMap.get(key);
              Element billingLabNode = new Element("BillingLab");
              billingItemsNode.addContent(billingLabNode);
              billingLabNode.setAttribute("labName", keyTokens[0]);
              billingLabNode.setAttribute("accountName", keyTokens[1]);
              
              
              BigDecimal labInvoicePrice = new BigDecimal(0);
              for(Iterator i2 = billingItems.iterator(); i2.hasNext();) {
                BillingItem theBillingItem = (BillingItem)i2.next();
                theBillingItem.excludeMethodFromXML("getLabName");
                theBillingItem.excludeMethodFromXML("getAccountName");
                billingLabNode.addContent(theBillingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement());
                labInvoicePrice = labInvoicePrice.add(theBillingItem.getInvoicePrice() != null ? theBillingItem.getInvoicePrice() : new BigDecimal(0));
              }
              billingLabNode.setAttribute("invoicePrice", NumberFormat.getCurrencyInstance().format(labInvoicePrice.doubleValue()));
            }
            
          }
          
          // Show files uploads that are in the staging area.
          // Only show these files if user has write permissions.
          if (showUploads.equals("Y") && this.getSecAdvisor().canUploadData(request)) {
            Element requestUploadNode = new Element("RequestUpload");
            requestNode.addContent(requestUploadNode);
            String key = request.getKey(Constants.UPLOAD_STAGING_DIR);
            GetRequestDownloadList.addExpandedFileNodes(baseDir, null, requestNode, requestUploadNode, request.getNumber(), key, request.getCodeRequestCategory(), dh);
          }

          doc.getRootElement().addContent(requestNode);
        
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
        } 
        
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetRequest ", e);
      throw new RollBackCommandException(e.getMessage());        
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetRequest ", e);
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

}