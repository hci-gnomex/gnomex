package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Segment;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackFolderComparator;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Session;



public class SaveGenomeBuild extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveGenomeBuild.class);
  

  private GenomeBuild                    gbScreen;
  private boolean                        isNewGenomeBuild = false;
  private String                         segmentsXML;
  private String                         sequenceFilesToRemoveXML;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    gbScreen = new GenomeBuild();
    HashMap errors = this.loadDetailObject(request, gbScreen);
    this.addInvalidFields(errors);
    if (gbScreen.getIdGenomeBuild() == null || gbScreen.getIdGenomeBuild().intValue() == 0) {
      isNewGenomeBuild = true;
    }
    
    // Das2 name is required
    if (gbScreen.getDas2Name() == null || gbScreen.getDas2Name().equals("")) {
      this.addInvalidField("namer", "DAS2 Name is required");
    }

    // Get segments XML string
    if (request.getParameter("segmentsXML") != null && !request.getParameter("segmentsXML").equals("")) {
      segmentsXML = request.getParameter("segmentsXML");      
    }
    // Get sequenceFilesToRemove XML string
    if (request.getParameter("sequenceFilesToRemoveXML") != null && !request.getParameter("sequenceFilesToRemoveXML").equals("")) {
      sequenceFilesToRemoveXML = request.getParameter("sequenceFilesToRemoveXML");      
    }
    
    // Make sure that the DAS2 name has no spaces or special characters
    if (isValid()) {
      if (gbScreen.getDas2Name().indexOf(" ") >= 0) {
        addInvalidField("namespaces", "The genome build DAS2 name cannot have spaces.");
      }
      Pattern pattern = Pattern.compile("\\W");
      Matcher matcher = pattern.matcher(gbScreen.getDas2Name());
      if (matcher.find()) {
        this.addInvalidField("specialc", "The genome build DAS2 name cannot have special characters.");
      }      
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        GenomeBuild gb = null;
              
        if (isNewGenomeBuild) {
          gb = gbScreen;
          
          sess.save(gb);
          sess.flush();

        } else {
          
          gb = (GenomeBuild)sess.load(GenomeBuild.class, gbScreen.getIdGenomeBuild());
          
          initializeGenomeBuild(gb);
          

          // Delete segments    
          if (segmentsXML != null) {
            StringReader reader = new StringReader(segmentsXML);
            SAXReader sax = new SAXReader();
            Document segmentsDoc = sax.read(reader);
            for (Iterator<?> i = gb.getSegments().iterator(); i.hasNext();) {
              Segment segment = Segment.class.cast(i.next());
              boolean found = false;
              for(Iterator<?> i1 = segmentsDoc.getRootElement().elementIterator(); i1.hasNext();) {
                Element segmentNode = (Element)i1.next();
                String idSegment = segmentNode.attributeValue("idSegment");
                if (idSegment != null && !idSegment.equals("")) {
                  if (segment.getIdSegment().equals(new Integer(idSegment))) {
                    found = true;
                    break;
                  }
                }                   
              }
              if (!found) {
                sess.delete(segment);
              }
            } 
            sess.flush();

            // Add segments
            for(Iterator<?> i = segmentsDoc.getRootElement().elementIterator(); i.hasNext();) {
              Element segmentNode = (Element)i.next();

              String idSegment = segmentNode.attributeValue("idSegment");
              String len = segmentNode.attributeValue("length");
              len = len.replace(",", "");
              String sortOrder = segmentNode.attributeValue("sortOrder");

              Segment s = null;
              if (idSegment != null && !idSegment.equals("")) {
                s = Segment.class.cast(sess.load(Segment.class, new Integer(idSegment)));

                s.setName(segmentNode.attributeValue("name"));
                s.setLength(len != null && !len.equals("") ? new Integer(len) : null);
                s.setSortOrder(sortOrder != null && !sortOrder.equals("") ? new Integer(sortOrder) : null);
                s.setIdGenomeBuild(gb.getIdGenomeBuild());


              } else {
                s = new Segment();    

                s.setName(segmentNode.attributeValue("name"));
                s.setLength(len != null && !len.equals("") ? new Integer(len) : null);
                s.setSortOrder(sortOrder != null && !sortOrder.equals("") ? new Integer(sortOrder) : null);
                s.setIdGenomeBuild(gb.getIdGenomeBuild());

                sess.save(s);
                sess.flush();
              }
            }            
          }
          sess.flush();
        }
        
        // Remove sequence files
        if (sequenceFilesToRemoveXML != null) {
          // Remove sequence files
          StringReader reader = new StringReader(sequenceFilesToRemoveXML);
          SAXReader sax = new SAXReader();
          Document filesDoc = sax.read(reader);
          for(Iterator<?> i = filesDoc.getRootElement().elementIterator(); i.hasNext();) {
            Element fileNode = (Element)i.next();
            File file = new File(fileNode.attributeValue("url"));
            if (!file.delete()) {
              Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete sequence file " + file.getName() + " for genome build " + gb.getDas2Name());
            }
          }            
        }
        

        if (isNewGenomeBuild || gb.getRootDataTrackFolder() == null) {
          // Now add a root folder for a new genome build
          DataTrackFolder folder = new DataTrackFolder();
          folder.setName(gb.getDas2Name());
          folder.setIdGenomeBuild(gb.getIdGenomeBuild());
          folder.setIdParentDataTrackFolder(null);
          sess.save(folder);

          Set<DataTrackFolder>  foldersToKeep = new TreeSet<DataTrackFolder>(new DataTrackFolderComparator());
          foldersToKeep.add(folder);
          gb.setDataTrackFolders(foldersToKeep);            
          sess.flush();
        }
                       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idGenomeBuild=\"" + gb.getIdGenomeBuild() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save genome build.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveGenomeBuild ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private void initializeGenomeBuild(GenomeBuild gb) {
    gb.setGenomeBuildName(gbScreen.getGenomeBuildName());
    gb.setIsActive(gbScreen.getIsActive());
    gb.setIdAppUser(gbScreen.getIdAppUser());
    gb.setDas2Name(gbScreen.getDas2Name());
    gb.setBuildDate(gbScreen.getBuildDate());
    gb.setCoordAuthority(gbScreen.getCoordAuthority());
    gb.setCoordTestRange(gbScreen.getCoordTestRange());
    gb.setCoordURI(gbScreen.getCoordURI());
    gb.setCoordVersion(gbScreen.getCoordVersion());
    gb.setCoordSource(gbScreen.getCoordSource());
    gb.setUcscName(gbScreen.getUcscName());
  }
  

}