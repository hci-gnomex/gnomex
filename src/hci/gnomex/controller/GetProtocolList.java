package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.FeatureExtractionProtocol;
import hci.gnomex.model.HybProtocol;
import hci.gnomex.model.LabelingProtocol;
import hci.gnomex.model.ScanProtocol;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProtocolList extends GNomExCommand implements Serializable {

  private static Logger log = Logger.getLogger(GetProtocolList.class);
  
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      List l = null;
      
      Element root = new Element("ProtocolList");
      Document doc = new Document(root);
      
      // TODO: get the Feature Extraction Protocols
      Element featureExtractionProtocols = new Element("Protocols");
      featureExtractionProtocols.setAttribute("label", "Feature Extraction Protocol");
      featureExtractionProtocols.setAttribute("protocolClassName", FeatureExtractionProtocol.class.getName());
      root.addContent(featureExtractionProtocols);
      l = sess.createQuery("from FeatureExtractionProtocol").list();
      if (!l.isEmpty()) {
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
          FeatureExtractionProtocol fep = (FeatureExtractionProtocol) iter.next();
          addFeatureExtractProtocolNode(fep, featureExtractionProtocols);
        }
      }
      
      // TODO: get the HybProtocols
      Element hybProtocols = new Element("Protocols");
      hybProtocols.setAttribute("label", "Hyb Protocol");
      hybProtocols.setAttribute("protocolClassName", HybProtocol.class.getName());
      root.addContent(hybProtocols);
      l = sess.createQuery("from HybProtocol").list();
      if (!l.isEmpty()) {
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
          HybProtocol hp = (HybProtocol) iter.next();
          addHybProtocolNode(hp, hybProtocols);
        }
      }
      
      // TODO: get the LabelingProtocols
      Element labelingProtocols = new Element("Protocols");
      labelingProtocols.setAttribute("label", "Labeling Protocol");
      labelingProtocols.setAttribute("protocolClassName", LabelingProtocol.class.getName());
      root.addContent(labelingProtocols);
      l = sess.createQuery("from LabelingProtocol").list();
      if (!l.isEmpty()) {
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
          LabelingProtocol lp = (LabelingProtocol) iter.next();
          addLabelingProtocolNode(lp, labelingProtocols);
        }
      }
      
      // TODO: get the Scan Protocols
      Element scanProtocols = new Element("Protocols");
      scanProtocols.setAttribute("label", "Scan Protocol");
      scanProtocols.setAttribute("protocolClassName", ScanProtocol.class.getName());
      root.addContent(scanProtocols);
      l = sess.createQuery("from ScanProtocol").list();
      if (!l.isEmpty()) {
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
          ScanProtocol sp = (ScanProtocol) iter.next();
          addScanProtocolNode(sp, scanProtocols);
        }
      }
      XMLOutputter out = new XMLOutputter();
      xmlResult = out.outputString(doc);
      
      this.validate();
      
    } catch (HibernateException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (Exception e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    }
    finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      }
    }
    
    return this;
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.validate();
  }

  public void validate() {
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
  }

  public void addFeatureExtractProtocolNode(FeatureExtractionProtocol fep, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(fep.getIdFeatureExtractionProtocol()));
    e.setAttribute("label", this.getNonNullString(fep.getFeatureExtractionProtocol()));
    e.setAttribute("isActive", this.getNonNullString(fep.getIsActive()));
    e.setAttribute("protocolClassName", fep.getClass().getName());
    parent.addContent(e);
  }
  
  public void addHybProtocolNode(HybProtocol hp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(hp.getIdHybProtocol()));
    e.setAttribute("label", this.getNonNullString(hp.getHybProtocol()));
    e.setAttribute("isActive", this.getNonNullString(hp.getIsActive()));
    e.setAttribute("protocolClassName", hp.getClass().getName());
    parent.addContent(e);
  }
  
  public void addLabelingProtocolNode(LabelingProtocol lp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(lp.getIdLabelingProtocol()));
    e.setAttribute("label", this.getNonNullString(lp.getLabelingProtocol()));
    e.setAttribute("isActive", this.getNonNullString(lp.getIsActive()));
    e.setAttribute("protocolClassName", lp.getClass().getName());
    parent.addContent(e);
  }
  
  public void addScanProtocolNode(ScanProtocol sp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(sp.getIdScanProtocol()));
    e.setAttribute("label", this.getNonNullString(sp.getScanProtocol()));
    e.setAttribute("isActive", this.getNonNullString(sp.getIsActive()));
    e.setAttribute("protocolClassName", sp.getClass().getName());
    parent.addContent(e);
  }
}
