package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.RequestParser;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
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
import org.apache.log4j.Logger;

public class GetMultiplexLaneList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetMultiplexLaneList.class);
  

  private String           requestXMLString;
  private Document         requestDoc;
  private RequestParser    requestParser;
  

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
  
    
    if (request.getParameter("requestXMLString") != null && !request.getParameter("requestXMLString").equals("")) {
      requestXMLString = request.getParameter("requestXMLString");
      this.requestXMLString = this.requestXMLString.replaceAll("&", "&amp;");
      StringReader reader = new StringReader(requestXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        requestDoc = sax.build(reader);
        requestParser = new RequestParser(requestDoc, this.getSecAdvisor());
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse requestXMLString", je );
        this.addInvalidField( "RequestXMLString", "Invalid request xml");
      }
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
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
     

      // Read the experiment
      Request request = null;
      Set hybs = null;
      Set samples = null;
      Set lanes = null;
      Set labeledSamples = null;
      int x = 0;

      requestParser.parse(sess);
      request = requestParser.getRequest();

      // Admins and users authorized to submit requests can view estimated
      // charges            
      if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) &&
          !this.getSecAdvisor().isGroupIAmMemberOrManagerOf(request.getIdLab()) && 
          !this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) &&
          !this.getSecAdvisor().isGroupICollaborateWith(request.getIdLab())) {
        throw new RollBackCommandException("Insufficient permission to view estimated charges");
      }

      if (request.getIdRequest() == null) {
        request.setIdRequest(new Integer(0));
        request.setNumber("");          
      }

      samples = new TreeSet(new SampleComparator());
      lanes = new TreeSet(new LaneComparator());


      // Parse the samples.   Consider samples for billing if this
      // is a new request or a qc request being converted to a microarray
      // or next gen sequencing request
      x = 0;
      if (!requestParser.isAmendRequest() || requestParser.getAmendState().equals(Constants.AMEND_QC_TO_SEQ)) {
        for(Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
          String idSampleString = (String)i.next();
          Sample sample = (Sample)requestParser.getSampleMap().get(idSampleString);
          if (sample.getIdSample() == null) {
            sample.setIdSample(new Integer(x++));
          }
          samples.add(sample);
        }          
      }

      // Parse the sequence lanes
      x = 0;
      for(Iterator i = requestParser.getSequenceLaneInfos().iterator(); i.hasNext();) {
        RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo)i.next();
        SequenceLane lane = new SequenceLane();

        boolean isNewLane = requestParser.isNewRequest() || laneInfo.getIdSequenceLane() == null || laneInfo.getIdSequenceLane().startsWith("SequenceLane");

        if (isNewLane) {
          if (lane.getIdSequenceLane() == null) {
            lane.setIdSequenceLane(new Integer(x++));
            lane.setIdNumberSequencingCycles(laneInfo.getIdNumberSequencingCycles());
            lane.setIdNumberSequencingCyclesAllowed(laneInfo.getIdNumberSequencingCyclesAllowed());
            lane.setIdGenomeBuildAlignTo(laneInfo.getIdGenomeBuildAlignTo());
          }
          lane.setIdSeqRunType(laneInfo.getIdSeqRunType());
          Sample sample = (Sample)requestParser.getSampleMap().get(laneInfo.getIdSampleString());
          lane.setSample(sample);

          lanes.add(lane);

        }
      }

      for(Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
        String idSampleString = (String)i.next();
        boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
        Sample sample = (Sample)requestParser.getSampleMap().get(idSampleString);
        if (sample.getIdOligoBarcode() != null) {
          sample.setBarcodeSequence(dh.getBarcodeSequence(sample.getIdOligoBarcode()));      
        }

        // Set the barcodeSequenceB if  idOligoBarcodeB is filled in
        if(sample.getIdOligoBarcodeB() != null){
          sample.setBarcodeSequenceB(dh.getBarcodeSequence(sample.getIdOligoBarcodeB()));
        }
      }
      Document doc = new Document(new Element("MultiplexLaneList"));
      SequenceLane.addMultiplexLaneNodes(doc.getRootElement(), lanes, null);


      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

      // We don't want to save anything;
      sess.clear();

    }catch (NamingException e){
      LOG.error("An exception has occurred in GetMultiplexLaneList ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      LOG.error("An exception has occurred in GetMultiplexLaneList ", e);

      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      LOG.error("An exception has occurred in GetMultiplexLaneList ", e);

      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetMultiplexLaneList ", e);

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