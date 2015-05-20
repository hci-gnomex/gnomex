package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AlignmentProfile;
import hci.gnomex.model.GenomeIndex;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.GenomeIndexComparator;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SaveAlignmentProfile extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAlignmentProfile.class);

  private String                         genomeIndexListXMLString;
  private Document                       genomeIndexListDoc;

  private AlignmentProfile               alignmentProfileScreen;
  private boolean                        isNewAlignmentProfile = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    alignmentProfileScreen = new AlignmentProfile();
    HashMap errors = this.loadDetailObject(request, alignmentProfileScreen);
    this.addInvalidFields(errors);
    if (alignmentProfileScreen.getIdAlignmentProfile() == null || alignmentProfileScreen.getIdAlignmentProfile().intValue() == 0) {
      isNewAlignmentProfile = true;
    }
    
    
    if (request.getParameter("genomeIndexListXMLString") != null && !request.getParameter("genomeIndexListXMLString").equals("")) {
      genomeIndexListXMLString = request.getParameter("genomeIndexListXMLString");
      StringReader reader = new StringReader(genomeIndexListXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        genomeIndexListDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse genomeIndexListXMLString", je );
        this.addInvalidField( "genomeIndexListXMLString", "Invalid genomeIndexListXMLString");
      }
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        AlignmentProfile ap = null;
              
        if (isNewAlignmentProfile) {
          ap = alignmentProfileScreen;
          
          sess.save(ap);
        } else {
          
          ap = (AlignmentProfile)sess.load(AlignmentProfile.class, alignmentProfileScreen.getIdAlignmentProfile());
          
          Hibernate.initialize(ap.getGenomeIndexList());
          
          initializeAlignmentProfile(ap);
        }

        //
        // Save genomeIndexes
        //
        TreeSet genomeIndexList = new TreeSet(new GenomeIndexComparator());
        if (genomeIndexListDoc != null) {
          for(Iterator i = this.genomeIndexListDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element genomeIndexNode = (Element)i.next();
            GenomeIndex genomeIndex = (GenomeIndex)sess.load(GenomeIndex.class, Integer.valueOf(genomeIndexNode.getAttributeValue("idGenomeIndex")));
            genomeIndexList.add(genomeIndex);
          }
        }
        ap.setGenomeIndexList(genomeIndexList);  
        
        sess.flush();
        
        
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idAlignmentProfile=\"" + ap.getIdAlignmentProfile() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save alignment profile.");
        setResponsePage(this.ERROR_JSP);
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in SaveAlignmentProfile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private void initializeAlignmentProfile(AlignmentProfile ap) {
    ap.setAlignmentProfileName(alignmentProfileScreen.getAlignmentProfileName());
    ap.setDescription(alignmentProfileScreen.getDescription());
    ap.setParameters(alignmentProfileScreen.getParameters());
    ap.setIsActive(alignmentProfileScreen.getIsActive()); 
    ap.setIdAlignmentPlatform(alignmentProfileScreen.getIdAlignmentPlatform());
    ap.setIdSeqRunType(alignmentProfileScreen.getIdSeqRunType());
  }
}