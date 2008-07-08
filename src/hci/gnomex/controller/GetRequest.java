package hci.gnomex.controller;

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
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SampleCharacteristicEntry;


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
          
          if (!newRequest) {
            this.getSecAdvisor().flagPermissions(request);            
          }
          
          StringBuffer queryBuf = new StringBuffer();
          queryBuf.append("SELECT sc from SampleCharacteristic as sc ");
          List sampleCharacteristics = sess.createQuery(queryBuf.toString()).list();


        
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