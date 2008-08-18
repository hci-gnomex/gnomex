package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.AnalysisProtocol;
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
  private String protocolClassName;
  
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      List l = null;
      
      Element root = new Element("ProtocolList");
      Document doc = new Document(root);
      
      // get the Feature Extraction Protocols
      if (protocolClassName == null || protocolClassName.equals("hci.gnomex.model.FeatureExtractionProtocol")) {
        Element featureExtractionProtocols = new Element("Protocols");
        featureExtractionProtocols.setAttribute("label", "Feature Extraction Protocol");
        featureExtractionProtocols.setAttribute("protocolClassName", FeatureExtractionProtocol.class.getName());
        root.addContent(featureExtractionProtocols);
        l = sess.createQuery("select fe from FeatureExtractionProtocol fe order by fe.featureExtractionProtocol ").list();
        if (!l.isEmpty()) {
          Iterator iter = l.iterator();
          while (iter.hasNext()) {
            FeatureExtractionProtocol fep = (FeatureExtractionProtocol) iter.next();
            addFeatureExtractProtocolNode(fep, featureExtractionProtocols);
          }
        }
        
      }
      
      // get the HybProtocols
      if (protocolClassName == null || protocolClassName.equals("hci.gnomex.model.HybProtocol")) {
      Element hybProtocols = new Element("Protocols");
        hybProtocols.setAttribute("label", "Hyb Protocol");
        hybProtocols.setAttribute("protocolClassName", HybProtocol.class.getName());
        root.addContent(hybProtocols);
        l = sess.createQuery("select h from HybProtocol h order by h.hybProtocol ").list();
        if (!l.isEmpty()) {
          Iterator iter = l.iterator();
          while (iter.hasNext()) {
            HybProtocol hp = (HybProtocol) iter.next();
            addHybProtocolNode(hp, hybProtocols);
          }
        }
      }
      
      // get the LabelingProtocols
      if (protocolClassName == null || protocolClassName.equals("hci.gnomex.model.LabelingProtocol")) {
        Element labelingProtocols = new Element("Protocols");
        labelingProtocols.setAttribute("label", "Labeling Protocol");
        labelingProtocols.setAttribute("protocolClassName", LabelingProtocol.class.getName());
        root.addContent(labelingProtocols);
        l = sess.createQuery("select l from LabelingProtocol l order by l.labelingProtocol").list();
        if (!l.isEmpty()) {
          Iterator iter = l.iterator();
          while (iter.hasNext()) {
            LabelingProtocol lp = (LabelingProtocol) iter.next();
            addLabelingProtocolNode(lp, labelingProtocols);
          }
        }        
      }
      
      // get the Scan Protocols
      if (protocolClassName == null || protocolClassName.equals("hci.gnomex.model.ScanProtocol")) {
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
      }

      // get the Analysis Protocols
      if (protocolClassName == null || protocolClassName.equals("hci.gnomex.model.AnalysisProtocol")) {
        Element analysisProtocols = new Element("Protocols");
        analysisProtocols.setAttribute("label", "Analysis Protocol");
        analysisProtocols.setAttribute("protocolClassName", AnalysisProtocol.class.getName());
        root.addContent(analysisProtocols);
        l = sess.createQuery("select a from AnalysisProtocol a order by a.analysisProtocol").list();
        if (!l.isEmpty()) {
          Iterator iter = l.iterator();
          while (iter.hasNext()) {
            AnalysisProtocol ap = (AnalysisProtocol) iter.next();
            addAnalysisProtocolNode(ap, analysisProtocols);
          }
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
    if (request.getParameter("protocolClassName") != null && !request.getParameter("protocolClassName").equals("")) {
      this.protocolClassName = request.getParameter("protocolClassName");
    }
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

    setPermissions(fep);
    e.setAttribute("canRead",   fep.canRead()   ? "Y" : "N");
    e.setAttribute("canDelete", fep.canDelete() ? "Y" : "N");
    e.setAttribute("canUpdate", fep.canUpdate() ? "Y" : "N");    
    
    parent.addContent(e);
  }
  
  public void addHybProtocolNode(HybProtocol hp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(hp.getIdHybProtocol()));
    e.setAttribute("label", this.getNonNullString(hp.getHybProtocol()));
    e.setAttribute("isActive", this.getNonNullString(hp.getIsActive()));
    e.setAttribute("protocolClassName", hp.getClass().getName());

    setPermissions(hp);
    e.setAttribute("canRead",   hp.canRead()   ? "Y" : "N");
    e.setAttribute("canDelete", hp.canDelete() ? "Y" : "N");
    e.setAttribute("canUpdate", hp.canUpdate() ? "Y" : "N");    
    
    parent.addContent(e);
  }
  
  public void addLabelingProtocolNode(LabelingProtocol lp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(lp.getIdLabelingProtocol()));
    e.setAttribute("label", this.getNonNullString(lp.getLabelingProtocol()));
    e.setAttribute("isActive", this.getNonNullString(lp.getIsActive()));
    e.setAttribute("protocolClassName", lp.getClass().getName());

    setPermissions(lp);
    e.setAttribute("canRead",   lp.canRead()   ? "Y" : "N");
    e.setAttribute("canDelete", lp.canDelete() ? "Y" : "N");
    e.setAttribute("canUpdate", lp.canUpdate() ? "Y" : "N");
    
    parent.addContent(e);
  }
  
  public void addScanProtocolNode(ScanProtocol sp, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(sp.getIdScanProtocol()));
    e.setAttribute("label", this.getNonNullString(sp.getScanProtocol()));
    e.setAttribute("isActive", this.getNonNullString(sp.getIsActive()));
    e.setAttribute("protocolClassName", sp.getClass().getName());

    setPermissions(sp);
    e.setAttribute("canRead",   sp.canRead()   ? "Y" : "N");
    e.setAttribute("canDelete", sp.canDelete() ? "Y" : "N");
    e.setAttribute("canUpdate", sp.canUpdate() ? "Y" : "N");
        
    parent.addContent(e);
  }
  
  public void addAnalysisProtocolNode(AnalysisProtocol ap, Element parent) {
    Element e = new Element("Protocol");
    e.setAttribute("id", this.getNonNullString(ap.getIdAnalysisProtocol()));
    e.setAttribute("label", this.getNonNullString(ap.getAnalysisProtocol()));
    e.setAttribute("isActive", this.getNonNullString(ap.getIsActive()));
    e.setAttribute("protocolClassName", ap.getClass().getName());
    e.setAttribute("idAppUser", this.getNonNullString(ap.getIdAppUser()));
    
    setPermissions(ap);
    e.setAttribute("canRead",   ap.canRead()   ? "Y" : "N");
    e.setAttribute("canDelete", ap.canDelete() ? "Y" : "N");
    e.setAttribute("canUpdate", ap.canUpdate() ? "Y" : "N");
    
    parent.addContent(e);
  }  
  
  public void setPermissions(DetailObject o) {
    try {
      o.canRead(this.getSecAdvisor().canRead(o));
    } catch (UnknownPermissionException e) {
      o.canRead(false);
    }

    try {
      o.canUpdate(this.getSecAdvisor().canUpdate(o));
    } catch (UnknownPermissionException e) {
      o.canUpdate(false);
    }

    try {
      o.canDelete(this.getSecAdvisor().canDelete(o));
    } catch (UnknownPermissionException e) {
      o.canDelete(false);
    }


  }
}
