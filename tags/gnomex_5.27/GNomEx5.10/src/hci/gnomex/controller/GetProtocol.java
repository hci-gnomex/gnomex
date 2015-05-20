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
import hci.gnomex.model.SeqLibProtocol;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProtocol extends GNomExCommand implements Serializable {

  private static Logger log = Logger.getLogger(GetProtocol.class);
  private Integer idProtocol;
  private String protocolClassName;
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      String id = null;
      String protocolName = null;
      String codeRequestCategory = null;
      String description = null;
      String url = null;
      String isActive = null;
      Integer idAnalysisType = null;
      Integer idAppUser = null;
      String canRead = "N";
      String canUpdate = "N";
      String canDelete = "N";
      
      if (this.idProtocol != null || this.idProtocol.intValue() != 0) {
        if (this.protocolClassName.equals(FeatureExtractionProtocol.class.getName())) {
          FeatureExtractionProtocol fep = (FeatureExtractionProtocol) sess.load(FeatureExtractionProtocol.class, this.idProtocol);
          id = fep.getIdFeatureExtractionProtocol().toString();
          protocolName = fep.getFeatureExtractionProtocol();
          codeRequestCategory = fep.getCodeRequestCategory();
          description = fep.getDescription();
          url = fep.getUrl();
          isActive = fep.getIsActive();
          setPermissions(fep);
          canRead   = fep.canRead() ? "Y" : "N";
          canUpdate = fep.canUpdate() ? "Y" : "N";
          canDelete = fep.canDelete() ? "Y" : "N";
          
        } else if (this.protocolClassName.equals(HybProtocol.class.getName())) {
          HybProtocol hp = (HybProtocol) sess.load(HybProtocol.class, this.idProtocol);
          id = hp.getIdHybProtocol().toString();
          protocolName = hp.getHybProtocol();
          codeRequestCategory = hp.getCodeRequestCategory();
          description = hp.getDescription();
          url = hp.getUrl();
          isActive = hp.getIsActive();
          setPermissions(hp);
          canRead   = hp.canRead() ? "Y" : "N";
          canUpdate = hp.canUpdate() ? "Y" : "N";
          canDelete = hp.canDelete() ? "Y" : "N";
          
        } else if (this.protocolClassName.equals(LabelingProtocol.class.getName())) {
          LabelingProtocol lp = (LabelingProtocol) sess.load(LabelingProtocol.class,this.idProtocol);
          id = lp.getIdLabelingProtocol().toString();
          protocolName = lp.getLabelingProtocol();
          codeRequestCategory = lp.getCodeRequestCategory();
          description = lp.getDescription();
          url = lp.getUrl();
          isActive = lp.getIsActive();
          setPermissions(lp);
          canRead   = lp.canRead() ? "Y" : "N";
          canUpdate = lp.canUpdate() ? "Y" : "N";
          canDelete = lp.canDelete() ? "Y" : "N";
          
        } else if (this.protocolClassName.equals(ScanProtocol.class.getName())) {
          ScanProtocol sp = (ScanProtocol) sess.load(ScanProtocol.class,this.idProtocol);
          id = sp.getIdScanProtocol().toString();
          protocolName = sp.getScanProtocol();
          codeRequestCategory = sp.getCodeRequestCategory();
          description = sp.getDescription();
          url = sp.getUrl();
          isActive = sp.getIsActive();
          setPermissions(sp);
          canRead   = sp.canRead() ? "Y" : "N";
          canUpdate = sp.canUpdate() ? "Y" : "N";
          canDelete = sp.canDelete() ? "Y" : "N";
        } else if (this.protocolClassName.equals(SeqLibProtocol.class.getName())) {
          SeqLibProtocol sp = (SeqLibProtocol) sess.load(SeqLibProtocol.class,this.idProtocol);
          id = sp.getIdSeqLibProtocol().toString();
          protocolName = sp.getSeqLibProtocol();
          description = sp.getDescription();
          url = sp.getUrl();
          isActive = sp.getIsActive();
          setPermissions(sp);
          canRead   = sp.canRead() ? "Y" : "N";
          canUpdate = sp.canUpdate() ? "Y" : "N";
          canDelete = sp.canDelete() ? "Y" : "N";
        } else if (this.protocolClassName.equals(AnalysisProtocol.class.getName())) {
          AnalysisProtocol ap = (AnalysisProtocol) sess.load(AnalysisProtocol.class,this.idProtocol);
          id = ap.getIdAnalysisProtocol().toString();
          protocolName = ap.getAnalysisProtocol();
          description = ap.getDescription();
          url = ap.getUrl();
          isActive = ap.getIsActive();
          idAnalysisType = ap.getIdAnalysisType();
          idAppUser = ap.getIdAppUser();
          setPermissions(ap);
          canRead   = ap.canRead() ? "Y" : "N";
          canUpdate = ap.canUpdate() ? "Y" : "N";
          canDelete = ap.canDelete() ? "Y" : "N";
        }
        
        Element root = new Element("Protocol");
        Document doc = new Document(root);
        root.addContent(new Element("id").addContent(id));
        root.addContent(new Element("name").addContent(protocolName));
        root.addContent(new Element("description").addContent(description));
        root.addContent(new Element("url").addContent(url));
        root.addContent(new Element("idAppUser").addContent(idAppUser != null ? idAppUser.toString() : ""));
        root.addContent(new Element("canRead").addContent(canRead));
        root.addContent(new Element("canUpdate").addContent(canUpdate));
        root.addContent(new Element("canDelete").addContent(canDelete));
        
        if (this.protocolClassName.equals(AnalysisProtocol.class.getName())) {
          root.addContent(new Element("idAnalysisType").addContent(idAnalysisType != null ? idAnalysisType.toString() : ""));
        } else {
          root.addContent(new Element("codeRequestCategory").addContent(codeRequestCategory));          
        }
        
        root.addContent(new Element("isActive").addContent(isActive));
        root.addContent(new Element("protocolClassName").addContent(this.protocolClassName));
        
        XMLOutputter out = new XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
      } else {
        this.addInvalidField("Unknown Protocol", "Unknown Protocol");
      }
      
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
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("id") != null && request.getParameter("id") != "") {
      this.idProtocol = new Integer(request.getParameter("id"));
    } else {
      this.addInvalidField("Protocol Id", "Protocol ID is required.");
    }
    
    if (request.getParameter("protocolClassName") != null && request.getParameter("protocolClassName") != "") {
      this.protocolClassName = request.getParameter("protocolClassName");
    } else {
      this.addInvalidField("Protocol Class Name", "Protocol Class Name is required");
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

}
