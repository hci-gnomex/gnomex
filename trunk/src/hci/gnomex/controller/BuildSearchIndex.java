package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.hibernate.Session;

public class BuildSearchIndex extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BuildSearchIndex.class);
  
  private Map projectRequestMap;
  private Map projectAnnotationMap;
  private Map sampleAnnotationMap;
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
      IndexWriter writer = new IndexWriter("c:/orion/luceneIndexGnomEx/", new StandardAnalyzer(), true);
      
      // Get dictionaries
      DictionaryHelper dh = new DictionaryHelper();
      dh.getDictionaries(sess);
      
      // Get basic project/request data
      getProjectRequestData(sess);
      
      // Get project annotations (experiment design and factors)
      getProjectAnnotations(sess);
      
      // Get sample annotations (sample characteristics)
      getSampleAnnotations(sess);

      //
      // For each project / request
      //
      for(Iterator i = projectRequestMap.keySet().iterator(); i.hasNext();) {
        String key = (String)i.next();
        
        Object[] keyTokens = key.split("-");
        Integer idProject = new Integer((String)keyTokens[0]);
        Integer idRequest = keyTokens.length == 2 && keyTokens[1] != null ? new Integer((String)keyTokens[1]) : null;
        
        
        Document doc = new Document();

        //
        // Obtain basic project and request text fields
        //
        List rows = (List)projectRequestMap.get(key);

        String  requestNumber = null;
        String  projectName = null;
        String  projectDescription = null;

        
        StringBuffer hybNotes = new StringBuffer();
        StringBuffer sampleNames = new StringBuffer();
        StringBuffer sampleDescriptions = new StringBuffer();
        StringBuffer sampleOrganisms = new StringBuffer();
        StringBuffer sampleSources = new StringBuffer();
        Integer      idSlideProduct = null;
        String       slideProduct = null;
        Integer      idOrganismSlideProduct = null;
        String       slideProductOrganism = null;
        String       codeRequestCategory = null;
        String       requestCategory = null;
        Integer      idLabProject = null;
        String       labProject = null;
        Integer      idLabRequest = null;
        String       labRequest = null;
        String       codeMicroarrayCategory = null;
        String       microarrayCategory = null;
        String       requestOwnerFirstName = null;
        String       requestOwnerLastName = null;
        String       projectCodeVisibility = null;
        String       projectPublicNote = null;
        String       requestCodeVisibility = null;
        String       requestPublicNote = null;
        Date         requestCreateDate = null;
        StringBuffer requestDisplayName = null;
        
        
        for(Iterator i1 = rows.iterator(); i1.hasNext();) {
          Object[] row = (Object[])i1.next();
          
          idProject           = (Integer)row[0];
          idRequest           = (Integer)row[1];
          requestNumber       = (String) row[2];
          projectName         = (String) row[3];
          projectDescription  = (String) row[4];
          
          String hybNote = (String) row[5];
          hybNotes.append(hybNote          != null ? hybNote + " " : "");

          // sample 1
          String  sampleName      = (String) row[6];
          String  sampleDesc      = (String) row[7];
          Integer idOrganism      = (Integer)row[8];
          Integer idSlideSource   = (Integer)row[9];
          
          sampleNames.append       (sampleName    != null ? sampleName + " " : "");
          sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
          sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
          sampleSources.append(     idSlideSource != null ? dh.getSampleSource(idSlideSource) + " " : "");

          // sample 2
          sampleName      = (String) row[10];
          sampleDesc      = (String) row[11];
          idOrganism      = (Integer)row[12];
          idSlideSource   = (Integer)row[13];
          
          sampleNames.append       (sampleName    != null ? sampleName + " " : "");
          sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
          sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
          sampleSources.append(     idSlideSource != null ? dh.getSampleSource(idSlideSource) + " " : "");
          
          // more request data
          idSlideProduct           = (Integer)row[14];
          idOrganismSlideProduct   = (Integer)row[15];
          codeRequestCategory      = (String) row[16];
          idLabProject             = (Integer)row[17];
          labProject               = (String) row[18];
          idLabRequest             = (Integer)row[19];
          labRequest               = (String) row[20];
          codeMicroarrayCategory   = (String) row[21];
          requestOwnerFirstName    = (String) row[22];
          requestOwnerLastName     = (String) row[23];
          projectCodeVisibility    = (String) row[24];
          requestCodeVisibility    = (String) row[25];
          requestCreateDate        = (java.sql.Date) row[26];
          
          slideProduct             = idSlideProduct != null ? dh.getSlideProductName(idSlideProduct) : null;
          slideProductOrganism     = idOrganismSlideProduct != null ? dh.getOrganism(idOrganismSlideProduct) : null;
          requestCategory          = dh.getRequestCategory(codeRequestCategory);
          microarrayCategory       = dh.getMicroarrayCategory(codeMicroarrayCategory);
          if (projectCodeVisibility != null && projectCodeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
            projectPublicNote = "(Public) ";
          } 
          if (requestCodeVisibility != null && requestCodeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
            requestPublicNote = "(Public) ";
          }
          
          requestDisplayName = new StringBuffer();
          if (codeRequestCategory != null && codeRequestCategory.equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
            requestDisplayName.append(requestNumber);
            requestDisplayName.append(" - ");
            requestDisplayName.append(requestOwnerFirstName);
            requestDisplayName.append(" ");
            requestDisplayName.append(requestOwnerLastName);
            requestDisplayName.append(" ");
            requestDisplayName.append(this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL));      
          } else {
            requestDisplayName.append(requestNumber);
            requestDisplayName.append(" - ");
            requestDisplayName.append(slideProduct);      
            requestDisplayName.append(" - ");
            requestDisplayName.append(requestOwnerFirstName);
            requestDisplayName.append(" ");
            requestDisplayName.append(requestOwnerLastName);
            requestDisplayName.append(" ");
            requestDisplayName.append(this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL));      
          }
          
        }
        
        
        //
        // Obtain experiment design entries and experiment factor entries
        // on project
        //
        StringBuffer projectAnnotations = new StringBuffer();
        List projectAnnotationRows = (List)projectAnnotationMap.get(idProject);
        if (projectAnnotationRows != null) {
          for(Iterator i1 = projectAnnotationRows.iterator(); i1.hasNext();) {
            Object[] row = (Object[])i1.next();
            projectAnnotations.append((String)row[1] != null && !((String)row[1]).trim().equals("") ? (String)row[1] + " " : "");
            projectAnnotations.append((String)row[2] != null && !((String)row[2]).trim().equals("") ? (String)row[2] + " " : "");
          }          
        }
        

        //
        // Obtain sample annoations on samples of request
        //
        StringBuffer sampleAnnotations = new StringBuffer();
        if (idRequest != null) {
          List sampleAnnotationRows = (List)sampleAnnotationMap.get(idRequest);
          if (sampleAnnotationRows != null) {
            for(Iterator i1 = sampleAnnotationRows.iterator(); i1.hasNext();) {
              Object[] row = (Object[])i1.next();
              sampleAnnotations.append((String)row[1] != null && !((String)row[1]).trim().equals("") ? (String)row[1] + " " : "");
              sampleAnnotations.append((String)row[2] != null && !((String)row[2]).trim().equals("") ? (String)row[2] + " " : "");
              sampleAnnotations.append((String)row[3] != null && !((String)row[3]).trim().equals("") ? (String)row[3] + " " : "");
            }          
          }
          
        }
        
        //
        // Add non-indexed fields
        //
        addNonIndexedField(doc, "idProject",          idProject.toString());  
        if (idRequest != null) {
          addNonIndexedField(doc, "idRequest",        idRequest.toString());               
        }
        addNonIndexedField(doc, "requestNumber",          requestNumber);  
        addNonIndexedField(doc, "requestDisplayName",     requestDisplayName.toString());  
        addNonIndexedField(doc, "requestOwnerFirstName",  requestOwnerFirstName);  
        addNonIndexedField(doc, "requestOwnerLastName",   requestOwnerLastName);  
        addNonIndexedField(doc, "requestCreateDate",      this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL));  
        addNonIndexedField(doc, "microarrayCategory",     microarrayCategory);  
        addNonIndexedField(doc, "projectPublicNote",      projectPublicNote);  
        addNonIndexedField(doc, "requestPublicNote",      requestPublicNote);  
        
        
        //
        // Add indexed fields
        //
        addIndexedField(doc, "projectName",               projectName);     
        addIndexedField(doc, "projectDescription",        projectDescription);     
        addIndexedField(doc, "hybNotes",                  hybNotes.toString());     
        addIndexedField(doc, "sampleNames",               sampleNames.toString());             
        addIndexedField(doc, "sampleDescriptions",        sampleDescriptions.toString());    
        addIndexedField(doc, "sampleOrganisms",           sampleOrganisms.toString());    
        addIndexedField(doc, "sampleSources",             sampleSources.toString());    
        addIndexedField(doc, "requestCategory",           requestCategory);    
        addIndexedField(doc, "codeRequestCategory",       codeRequestCategory);    
        addIndexedField(doc, "codeMicroarrayCategory",    codeMicroarrayCategory);    
        addIndexedField(doc, "idSlideProduct",            idSlideProduct);    
        addIndexedField(doc, "slideProduct",              slideProduct);    
        addIndexedField(doc, "slideProductOrganism",      slideProductOrganism);    
        addIndexedField(doc, "requestCategory",           requestCategory);    
        addIndexedField(doc, "projectIdLab",              idLabProject);    
        addIndexedField(doc, "projectLab",                labProject);    
        addIndexedField(doc, "requestIdLab",              idLabRequest);    
        addIndexedField(doc, "requestLab",                labRequest);    
        addIndexedField(doc, "projectCodeVisibility",     projectCodeVisibility);  
        addIndexedField(doc, "requestCodeVisibility",     requestCodeVisibility);  
        addIndexedField(doc, "projectAnnotations",        projectAnnotations.toString());               
        addIndexedField(doc, "sampleAnnotations",         sampleAnnotations.toString());               

        // Combine all text into one search field
        StringBuffer text = new StringBuffer();
        text.append(projectName);
        text.append(" ");
        text.append(projectDescription);
        text.append(" ");
        text.append(hybNotes.toString());
        text.append(" ");
        text.append(sampleNames.toString());
        text.append(" ");
        text.append(sampleDescriptions.toString());
        text.append(" ");
        text.append(projectAnnotations.toString());
        text.append(" ");
        text.append(sampleAnnotations.toString());
        text.append(" ");        
        text.append(sampleOrganisms.toString());
        text.append(" ");        
        text.append(sampleSources.toString());
        text.append(" ");        
        text.append(slideProduct);
        text.append(" ");        
        text.append(slideProductOrganism);
        text.append(" ");        
        text.append(labProject);
        text.append(" ");        
        text.append(labRequest);
        text.append(" ");        
        addIndexedField(doc, "text",                      text.toString());     

        
        
        writer.addDocument(doc);
      }
      

      writer.optimize();
      writer.close();

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetLab ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
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
  
  private void getProjectRequestData(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT proj.id, ");
    buf.append("       req.id, ");
    buf.append("       req.number, ");
    buf.append("       proj.name, ");
    buf.append("       proj.description, ");
    buf.append("       hyb.notes, ");
    buf.append("       s1.name, ");
    buf.append("       s1.description, ");
    buf.append("       s1.idOrganism, ");
    buf.append("       s1.idSampleSource, ");
    buf.append("       s2.name, ");
    buf.append("       s2.description,  ");
    buf.append("       s2.idOrganism,  ");
    buf.append("       s2.idSampleSource, ");
    buf.append("       req.idSlideProduct,  ");
    buf.append("       slideProd.idOrganism,  ");
    buf.append("       req.codeRequestCategory,  ");
    buf.append("       proj.idLab,  ");
    buf.append("       labProj.name,  ");
    buf.append("       req.idLab,  ");
    buf.append("       labReq.name,  ");
    buf.append("       req.codeMicroarrayCategory, ");
    buf.append("       reqOwner.firstName, ");
    buf.append("       reqOwner.lastName, ");
    buf.append("       proj.codeVisibility, ");
    buf.append("       req.codeVisibility, ");
    buf.append("       req.createDate ");
    buf.append("FROM        Project as proj ");
    buf.append("LEFT JOIN   proj.lab as labProj ");
    buf.append("LEFT JOIN   proj.requests as req ");
    buf.append("LEFT JOIN   req.lab as labReq ");
    buf.append("LEFT JOIN   req.slideProduct as slideProd ");
    buf.append("LEFT JOIN   req.appUser as reqOwner ");
    buf.append("LEFT JOIN   req.hybridizations as hyb ");
    buf.append("LEFT JOIN   hyb.labeledSampleChannel1 as ls1 ");
    buf.append("LEFT JOIN   ls1.sample as s1 ");
    buf.append("LEFT JOIN   hyb.labeledSampleChannel1 as ls2 ");
    buf.append("LEFT JOIN   ls2.sample as s2 ");
    buf.append("ORDER BY proj.idProject, req.idRequest ");
    
    List results = sess.createQuery(buf.toString()).list();
    projectRequestMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      Integer idRequest = (Integer)row[1];
      String key = idProject + "-" + (idRequest != null ? idRequest.toString() : "");
      
      List rows = (List)projectRequestMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        projectRequestMap.put(key, rows);
      }
      rows.add(row);
    }    
  }
  
  private void getProjectAnnotations(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT ede.idProject, ");
    buf.append("       ed.experimentDesign, ");
    buf.append("       ede.otherLabel  ");
    buf.append("FROM   ExperimentDesignEntry as ede, ExperimentDesign ed ");
    buf.append("WHERE  ede.value = 'Y' ");
    buf.append("AND    ede.codeExperimentDesign = ed.codeExperimentDesign ");
    buf.append("ORDER BY ede.idProject ");
    
    List results = sess.createQuery(buf.toString()).list();
    projectAnnotationMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      
      List rows = (List)projectAnnotationMap.get(idProject);
      if (rows == null) {
        rows = new ArrayList();
        projectAnnotationMap.put(idProject, rows);
      }
      rows.add(row);
    }   
    
    buf = new StringBuffer();
    buf.append("SELECT efe.idProject, ");
    buf.append("       ef.experimentFactor, ");
    buf.append("       efe.otherLabel  ");
    buf.append("FROM   ExperimentFactorEntry as efe, ExperimentFactor ef ");
    buf.append("WHERE  efe.value = 'Y' ");
    buf.append("AND    efe.codeExperimentFactor = ef.codeExperimentFactor ");
    buf.append("ORDER BY efe.idProject ");
    
    results = sess.createQuery(buf.toString()).list();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      
      List rows = (List)projectAnnotationMap.get(idProject);
      if (rows == null) {
        rows = new ArrayList();
        projectAnnotationMap.put(idProject, rows);
      }
      rows.add(row);
    }    
  }
  
  
  private void getSampleAnnotations(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT s.idRequest, ");
    buf.append("       sc.sampleCharacteristic, ");
    buf.append("       sce.value,  ");
    buf.append("       sce.otherLabel  ");
    buf.append("FROM   Sample s, SampleCharacteristicEntry as sce, SampleCharacteristic sc ");
    buf.append("WHERE  sce.value is not NULL ");
    buf.append("AND    s.idSample = sce.idSample ");
    buf.append("AND    sce.codeSampleCharacteristic = sc.codeSampleCharacteristic ");
    buf.append("ORDER BY s.idRequest ");
    
    List results = sess.createQuery(buf.toString()).list();
    sampleAnnotationMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idRequest = (Integer)row[0];
      
      List rows = (List)sampleAnnotationMap.get(idRequest);
      if (rows == null) {
        rows = new ArrayList();
        sampleAnnotationMap.put(idRequest, rows);
      }
      rows.add(row);
    }   
  }
  
  private void addIndexedField(Document doc, String name, String value) {
    if (value != null && !value.trim().equals("")) {
      doc.add( new Field(name, value, Field.Store.YES, Field.Index.TOKENIZED));          
    }
  }

  private void addIndexedField(Document doc, String name, Integer value) {
    if (value != null) {
      doc.add( new Field(name, value.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));          
    }
  }

  private void addNonIndexedField(Document doc, String name, String value) {
    if (value != null && !value.trim().equals("")) {
      doc.add( new Field(name, value, Field.Store.YES, Field.Index.NO));          
    }
  }

}