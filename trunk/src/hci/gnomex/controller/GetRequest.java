package hci.gnomex.controller;

import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SampleCharacteristicEntry;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SequenceLane;


public class GetRequest extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);
  
  private Integer idRequest;
  private String  requestNumber;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } 
    if (request.getParameter("requestNumber") != null && !request.getParameter("requestNumber").equals("")) {
      requestNumber = request.getParameter("requestNumber");
    } 
    
    
    if (idRequest == null && requestNumber == null) {
      this.addInvalidField("idRequest or requestNumber", "Either idRequest or requestNumber must be provided");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
 
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
        requestNumber = requestNumber.replaceAll("#", "");
        StringBuffer buf = new StringBuffer("SELECT req from Request as req where req.number = '" + requestNumber.toUpperCase() + "'");
        List requests = (List)sess.createQuery(buf.toString()).list();
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
          
          
          if (!newRequest) {
            this.getSecAdvisor().flagPermissions(request);            
          }
          
          StringBuffer queryBuf = new StringBuffer();
          queryBuf.append("SELECT sc from SampleCharacteristic as sc ");
          List sampleCharacteristics = sess.createQuery(queryBuf.toString()).list();


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

          
          // Show list of sample characteristic entries
          Element scParentNode = new Element("SampleCharacteristicEntries");
          requestNode.addContent(scParentNode);
          for(Iterator i = sampleCharacteristics.iterator(); i.hasNext();) {
            SampleCharacteristic sc = (SampleCharacteristic)i.next();

            Element scNode = new Element("SampleCharacteristicEntry");
            SampleCharacteristicEntry entry = null;
            for(Iterator i1 = request.getSamples().iterator(); i1.hasNext();) {
              Sample sample = (Sample)i1.next();
              for(Iterator i2 = sample.getSampleCharacteristicEntries().iterator(); i2.hasNext();) {
                SampleCharacteristicEntry scEntry = (SampleCharacteristicEntry)i2.next();
                if (scEntry.getCodeSampleCharacteristic().equals(sc.getCodeSampleCharacteristic())) {
                  entry = scEntry;
                  break;
                }
              }
            }
            scNode.setAttribute("codeSampleCharacteristic", sc.getCodeSampleCharacteristic());
            scNode.setAttribute("sampleCharacteristic", sc.getSampleCharacteristic());
            scNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
            scNode.setAttribute("isSelected", entry != null ? "true" : "false");
                
            scParentNode.addContent(scNode);
            
          }
          
          // Show list of seq lib treatments
          Element stParentNode = new Element("SeqLibTreatmentEntries");
          requestNode.addContent(stParentNode);
          for(Iterator i1 = dh.getSeqLibTreatments().iterator(); i1.hasNext();) {
            SeqLibTreatment st = (SeqLibTreatment)i1.next();
            
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