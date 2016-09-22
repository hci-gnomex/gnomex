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

import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Session;



public class SaveGenomeBuild extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveGenomeBuild.class);
  

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
    if (gbScreen.getIdGenomeBuild() == null || gbScreen.getIdGenomeBuild() == 0) {
      isNewGenomeBuild = true;
    }
    
    // Get segments XML string
    if (request.getParameter("segmentsXML") != null && !request.getParameter("segmentsXML").equals("")) {
      segmentsXML = request.getParameter("segmentsXML");      
    }
    // Get sequenceFilesToRemove XML string
    if (request.getParameter("sequenceFilesToRemoveXML") != null && !request.getParameter("sequenceFilesToRemoveXML").equals("")) {
      sequenceFilesToRemoveXML = request.getParameter("sequenceFilesToRemoveXML");      
    }

    if (isValid()) {
      validateDas2Name();
      validateSegments();
    }
    
  }

  private void validateDas2Name() {
    if (gbScreen.getDas2Name() == null || gbScreen.getDas2Name().equals("")) {
      this.addInvalidField("namer", "DAS2 Name is required");
    }
    if (gbScreen.getDas2Name().matches(".*\\W.*")) {
      // regex \W will match anything other than letters, digits, and underscore
      addInvalidField("specialc", "The genome build DAS2 name cannot have spaces or special characters.");
    }
  }

  private boolean isPositiveInteger(String value) {
    Integer intValue = IntegerValidator.getInstance().validate(value);
    return (intValue != null && intValue > 0);
  }

  private void validateSegments() {
    try {
      if (segmentsXML != null) {
        StringReader reader = new StringReader(segmentsXML);
        SAXReader sax = new SAXReader();
        Document segmentsDoc = sax.read(reader);
        int count = 0;
        for (Iterator<?> iterSegment = segmentsDoc.getRootElement().elementIterator(); iterSegment.hasNext(); ) {
          count++;
          Element segmentNode = (Element) iterSegment.next();
          String name = segmentNode.attributeValue("name");
          String length = segmentNode.attributeValue("length");
          String sortOrder = segmentNode.attributeValue("sortOrder");
          if (name.trim().isEmpty()) {
            addInvalidField("segment_" + count + "_name", "Segment name is required");
          }
          if (!isPositiveInteger(length)) {
            addInvalidField("segment_" + count + "_length", "Segment length '" + length + "' is invalid");
          }
          if (!isPositiveInteger(sortOrder)) {
            addInvalidField("segment_" + count + "_order", "Segment order '" + sortOrder + "' is invalid");
          }
        }
      }
    } catch (DocumentException e) {
      addInvalidField("segmentsXML", "Unable to process segments");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        GenomeBuild gb;
              
        if (isNewGenomeBuild) {
          gb = gbScreen;
          
          sess.save(gb);
          sess.flush();

        } else {
          
          gb = sess.load(GenomeBuild.class, gbScreen.getIdGenomeBuild());
          
          initializeGenomeBuild(gb);
          

          // Delete segments    
          if (segmentsXML != null) {
            StringReader reader = new StringReader(segmentsXML);
            SAXReader sax = new SAXReader();
            Document segmentsDoc = sax.read(reader);
            for (Object objSegment : gb.getSegments()) {
              Segment segment = Segment.class.cast(objSegment);
              boolean found = false;
              for (Object objNode : segmentsDoc.getRootElement().elements()) {
                Element segmentNode = (Element) objNode;
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
              Integer idSegment = IntegerValidator.getInstance().validate(segmentNode.attributeValue("idSegment"));
              String name = segmentNode.attributeValue("name").trim();
              Integer length = IntegerValidator.getInstance().validate(segmentNode.attributeValue("length"));
              Integer sortOrder = IntegerValidator.getInstance().validate(segmentNode.attributeValue("sortOrder"));

              Segment segment;
              if (idSegment != null) {
                segment = Segment.class.cast(sess.load(Segment.class, idSegment));

                segment.setName(name);
                segment.setLength(length);
                segment.setSortOrder(sortOrder);
                segment.setIdGenomeBuild(gb.getIdGenomeBuild());

              } else {
                segment = new Segment();

                segment.setName(name);
                segment.setLength(length);
                segment.setSortOrder(sortOrder);
                segment.setIdGenomeBuild(gb.getIdGenomeBuild());

                sess.save(segment);
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
              LOG.warn("Unable to delete sequence file " + file.getName() + " for genome build " + gb.getDas2Name());
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
      LOG.error("An exception has occurred in SaveGenomeBuild ", e);
      throw new RollBackCommandException(e.getMessage());
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
    gb.setIgvName(gbScreen.getIgvName());
  }
  

}