package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AppUserComparator;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.DataTrackFileComparator;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.PropertyOptionComparator;
import hci.gnomex.utility.RequestParser;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Session;


public class SaveDataTrack extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveDataTrack.class);
  
  private DataTrack    load;
  private DataTrack    dataTrack;
  private boolean      isNewDataTrack = false;
  private Integer      idDataTrackFolder;
  private String       collaboratorsXML;
  private String       filesToRemoveXML;
  private String       propertiesXML;
  private String       serverName;
  private String       baseDir;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    load = new DataTrack();
    HashMap errors = this.loadDetailObject(request, load);
    this.addInvalidFields(errors);

    if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
      idDataTrackFolder = Integer.valueOf(request.getParameter("idDataTrackFolder"));
    }

    // Make sure that name doesn't have forward slashes (/).
    if (load.getName().contains("/") || load.getName().contains("&")) {
      this.addInvalidField("namechar", "The name cannnot contain any characters / or &.");
    }
    
    if (request.getParameter("collaboratorsXML") != null && !request.getParameter("collaboratorsXML").equals("")) {
      collaboratorsXML = request.getParameter("collaboratorsXML");    
    } 
    
    if (request.getParameter("filesToRemoveXML") != null && !request.getParameter("filesToRemoveXML").equals("")) {
      filesToRemoveXML = request.getParameter("filesToRemoveXML");    
    } 
    
    if (request.getParameter("propertiesXML") != null && !request.getParameter("propertiesXML").equals("")) {
      propertiesXML = request.getParameter("propertiesXML");    
    }
    
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackReadDirectory(serverName);
      
      this.initializeDataTrack(sess);      
      
      if (this.getSecAdvisor().canUpdate(dataTrack)) {

        // Set collaborators
        if (collaboratorsXML != null && !collaboratorsXML.equals("")) {
          StringReader reader = new StringReader(collaboratorsXML);
          SAXReader sax = new SAXReader();
          Document collaboratorsDoc = sax.read(reader);
          TreeSet<AppUser> collaborators = new TreeSet<AppUser>(new AppUserComparator());
          for(Iterator<?> i = collaboratorsDoc.getRootElement().elementIterator(); i.hasNext();) {
            Element userNode = (Element)i.next();
            Integer idAppUser = Integer.parseInt(userNode.attributeValue("idAppUser"));
            AppUser user = AppUser.class.cast(sess.load(AppUser.class, idAppUser));
            collaborators.add(user);
          }
          dataTrack.setCollaborators(collaborators);

          sess.flush();
        }

        // Remove dataTrack files that are stored directly in the data track folder.
        // Unlink dataTrack files that are associated with analysis file(s).
        HashMap<Integer, Integer> dataTrackFilesToRemove = new HashMap<Integer, Integer>();
        if (filesToRemoveXML != null && !filesToRemoveXML.equals("")) {
          StringReader reader = new StringReader(filesToRemoveXML);
          SAXReader sax = new SAXReader();
          Document filesDoc = sax.read(reader);
          for(Iterator<?> i = filesDoc.getRootElement().elementIterator(); i.hasNext();) {
            Element fileNode = (Element)i.next();
            if (fileNode.attributeValue("idDataTrackFile").equals("")) {
              File file = new File(fileNode.attributeValue("url"));
              if (!file.delete()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable remove dataTrack file " + file.getName() + " for dataTrack " + dataTrack.getName());
              }
            } else {
              dataTrackFilesToRemove.put(Integer.valueOf(fileNode.attributeValue("idDataTrackFile")), Integer.valueOf(fileNode.attributeValue("idDataTrackFile")));
            }
          }       
        }
        
        // Delete dataTrackFile objects, unlinking from analysis file
        if (!isNewDataTrack) {
          TreeSet dataTrackFiles = new TreeSet<DataTrackFile>(new DataTrackFileComparator());
          for (DataTrackFile dataTrackFile : (Set<DataTrackFile>)dataTrack.getDataTrackFiles()) {
            if (dataTrackFilesToRemove.containsKey(dataTrackFile.getIdDataTrackFile())) {
            }
            dataTrackFiles.add(dataTrackFile);
          }
          dataTrack.setDataTrackFiles(dataTrackFiles);
          sess.flush();
          for (Iterator<Integer> i1 = dataTrackFilesToRemove.keySet().iterator(); i1.hasNext();) {
            Integer idDataTrackFile = (Integer)i1.next();
            DataTrackFile dtf = (DataTrackFile)sess.load(DataTrackFile.class, idDataTrackFile);
            sess.delete(dtf);
          }
          sess.flush();
        }
        

        // Delete dataTrack properties  
        if (propertiesXML != null && !propertiesXML.equals("")) {
          StringReader reader = new StringReader(propertiesXML);
          SAXReader sax = new SAXReader();
          Document propsDoc = sax.read(reader);
          for(Iterator<?> i = dataTrack.getPropertyEntries().iterator(); i.hasNext();) {
            PropertyEntry pe = PropertyEntry.class.cast(i.next());
            boolean found = false;
            for(Iterator<?> i1 = propsDoc.getRootElement().elementIterator(); i1.hasNext();) {
              Element propNode = (Element)i1.next();
              String idPropertyEntry = propNode.attributeValue("idPropertyEntry");
              if (idPropertyEntry != null && !idPropertyEntry.equals("")) {
                if (pe.getIdPropertyEntry().equals(new Integer(idPropertyEntry))) {
                  found = true;
                  break;
                }
              }                   
            }
            if (!found) {
              // delete dataTrack property values
              for(Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
                PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
                sess.delete(av);
              }  
              sess.flush();
              // delete dataTrack property
              sess.delete(pe);
            }
          } 
          sess.flush();
          // Add dataTrack properties
          for(Iterator<?> i = propsDoc.getRootElement().elementIterator(); i.hasNext();) {
            Element node = (Element)i.next();
            //Adding dataTracks
            String idPropertyEntry = node.attributeValue("idPropertyEntry");

            PropertyEntry pe = null;
            if (idPropertyEntry == null || idPropertyEntry.equals("")) {
              pe = new PropertyEntry();
              pe.setIdProperty(Integer.valueOf(node.attributeValue("idProperty")));
            } else {
              pe  = PropertyEntry.class.cast(sess.get(PropertyEntry.class, Integer.valueOf(idPropertyEntry))); 
            }
            pe.setValue(node.attributeValue("value"));
            pe.setIdDataTrack(dataTrack.getIdDataTrack());

            if (idPropertyEntry == null || idPropertyEntry.equals("")) {
              sess.save(pe);
              sess.flush();
            }

            // Remove PropertyEntryValues
            if (pe.getValues() != null) {
              for(Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
                PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
                boolean found = false;
                for(Iterator<?> i2 = node.elementIterator(); i2.hasNext();) {
                  Element n = (Element)i2.next();
                  if (n.getName().equals("PropertyEntryValue")) {
                    String idPropertyEntryValue = n.attributeValue("idPropertyEntryValue");
                    if (idPropertyEntryValue != null && !idPropertyEntryValue.equals("")) {
                      if (av.getIdPropertyEntryValue().equals(new Integer(idPropertyEntryValue))) {
                        found = true;
                        break;
                      }
                    }                   
                  }
                }
                if (!found) {
                  sess.delete(av);
                }
              }
              sess.flush();
            }

            // Add and update PropertyEntryValues
            for(Iterator<?> i1 = node.elementIterator(); i1.hasNext();) {
              Element n = (Element)i1.next();
              if (n.getName().equals("PropertyEntryValue")) {
                String idPropertyEntryValue = n.attributeValue("idPropertyEntryValue");
                String value = n.attributeValue("value");
                PropertyEntryValue av = null;
                // Ignore 'blank' url value
                if (value == null || value.equals("") || value.equals("Enter URL here...")) {
                  continue;
                }
                if (idPropertyEntryValue == null || idPropertyEntryValue.equals("")) {
                  av = new PropertyEntryValue();
                  av.setIdPropertyEntry(pe.getIdPropertyEntry());
                } else {
                  av = PropertyEntryValue.class.cast(sess.load(PropertyEntryValue.class, Integer.valueOf(idPropertyEntryValue)));              
                }
                av.setValue(n.attributeValue("value"));

                if (idPropertyEntryValue == null || idPropertyEntryValue.equals("")) {
                  sess.save(av);
                }
              }
            }
            sess.flush();


            String optionValue = "";
            TreeSet<PropertyOption> options = new TreeSet<PropertyOption>(new PropertyOptionComparator());
            for(Iterator<?> i1 = node.elementIterator(); i1.hasNext();) {
              Element n = (Element)i1.next();
              if (n.getName().equals("PropertyOption")) {
                Integer idPropertyOption = Integer.parseInt(n.attributeValue("idPropertyOption"));
                String selected = n.attributeValue("selected");
                if (selected != null && selected.equals("Y")) {
                  PropertyOption option = PropertyOption.class.cast(sess.load(PropertyOption.class, idPropertyOption));
                  options.add(option);
                  if (optionValue.length() > 0) {
                    optionValue += ",";
                  }
                  optionValue += option.getOption();
                }
              }
            }
            pe.setOptions(options);
            if (options.size() > 0) {
              pe.setValue(optionValue);
            }
            sess.flush();
          }
        }
        this.xmlResult = "<SUCCESS idDataTrack=\"" + dataTrack.getIdDataTrack() + "\"" +  " idDataTrackFolder=\"" + 
        (idDataTrackFolder != null ? idDataTrackFolder.toString() : "")
        + "\"/>";
        
        setResponsePage(this.SUCCESS_JSP);
        


      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save data track.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveDataTrackFolder ", e);
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
  
  private void initializeDataTrack(Session sess) throws Exception {
    
    if (load.getIdDataTrack() == null || load.getIdDataTrack().intValue() == 0) {
      createNewDataTrack(sess, load, idDataTrackFolder);
      isNewDataTrack = true;
    } else {
      dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, load.getIdDataTrack()));
      dataTrack.setName(load.getName());
      dataTrack.setDescription(load.getDescription());
      dataTrack.setSummary(load.getSummary());
      dataTrack.setCodeVisibility(load.getCodeVisibility());
      if (dataTrack.getCodeVisibility() != null && dataTrack.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
        dataTrack.setIdInstitution(load.getIdInstitution());
      } else {
        dataTrack.setIdInstitution(null);
      }
      dataTrack.setIdLab(load.getIdLab());
      dataTrack.setIdAppUser(load.getIdAppUser());
      sess.flush();
    }
  }  
  private DataTrack createNewDataTrack(Session sess, DataTrack load, Integer idDataTrackFolder) throws Exception {
    dataTrack = load;
    dataTrack.setCreatedBy(this.getSecAdvisor().getUID());
    dataTrack.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
    dataTrack.setIsLoaded("N");

    // TODO:  GenoPub - Need base directory property
    dataTrack.setDataPath(baseDir);

    // Only set ownership if this is not an admin
    if (!getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      dataTrack.setIdAppUser(getSecAdvisor().getIdAppUser());        
    }

    sess.save(dataTrack);
    sess.flush();

    // Get the dataTrack grouping this dataTrack is in.
    DataTrackFolder folder = null;
    if (idDataTrackFolder == null) {
      // If this is a root dataTrack, find the default root dataTrack
      // folder for the genome build.
      GenomeBuild gb = GenomeBuild.class.cast(sess.load(GenomeBuild.class, load.getIdGenomeBuild()));
      folder = gb.getRootDataTrackFolder();
      if (folder == null) {
        throw new Exception("Cannot find root dataTrack folder for " + gb.getGenomeBuildName());
      }
    } else {
      // Otherwise, find the dataTrack grouping passed in as a request parameter.
      folder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
    }

    // Add the dataTrack to the dataTrack grouping
    Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
    for(Iterator<?>i = folder.getDataTracks().iterator(); i.hasNext();) {
      DataTrack a = DataTrack.class.cast(i.next());
      newDataTracks.add(a);
    }
    newDataTracks.add(dataTrack);
    folder.setDataTracks(newDataTracks);


    // Assign a file directory name
    dataTrack.setFileName("DT" + dataTrack.getIdDataTrack());
    sess.flush();

    return dataTrack;

  }

 
  
  

}