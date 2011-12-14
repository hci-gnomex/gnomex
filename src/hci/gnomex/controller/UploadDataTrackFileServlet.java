package hci.gnomex.controller;

import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AppUserComparator;
import hci.gnomex.utility.BulkFileUploadException;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.PropertyOptionComparator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadDataTrackFileServlet extends HttpServlet {
  


  private String fileName = null;
  private  StringBuffer bypassedFiles = new StringBuffer();
  private File tempBulkUploadFile = null;

  private SecurityAdvisor secAdvisor = null;

  private String serverName;
  private String baseDir;
  
  //fields for bulkUploading
  private static final Pattern BULK_UPLOAD_LINE_SPLITTER = Pattern.compile("([^\\t]+)\\t([^\\t]+)\\t([^\\t]+)\\t(.+)", Pattern.DOTALL);


  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
  }

  /*
   * SPECIAL NOTE -  This servlet must be run on non-secure socket layer (http) in order to
   *                 keep track of previously created session. (see note below concerning
   *                 flex upload bug on Safari and FireFox).  Otherwise, session is 
   *                 not maintained.  Although the code tries to work around this
   *                 problem by creating a new security advisor if one is not found,
   *                 the Safari browser cannot handle authenicating the user (this second time).
   *                 So for now, this servlet must be run non-secure. 
   */
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    Session sess = null;
    Transaction tx = null;
    
    serverName = req.getServerName();
    
    Integer idDataTrack = null;
    DataTrack dataTrack = null;
    String dataTrackName = null;
    String codeVisibility = null;
    Integer idGenomeBuild = null;
    Integer idDataTrackFolder = null;
    Integer idLab = null;
    
    try {
      sess = HibernateSession.currentSession(req.getUserPrincipal().getName());
      tx = sess.beginTransaction();
      tx.begin();
      
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackWriteDirectory(serverName);
     
      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Get security advisor
      secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadDataTrackFileServlet:  Warning - unable to find existing session. Creating security advisor.");
        secAdvisor = SecurityAdvisor.create(sess, req.getUserPrincipal().getName());
      }
      

      
      //
      // To work around flex upload problem with FireFox and Safari, create security advisor since
      // we loose session and thus don't have security advisor in session attribute.
      //
      // Note from Flex developer forum (http://www.kahunaburger.com/2007/10/31/flex-uploads-via-httphttps/):
      // Firefox uses two different processes to upload the file.
      // The first one is the one that hosts your Flex (Flash) application and communicates with the server on one channel.
      // The second one is the actual file-upload process that pipes multipart-mime data to the server. 
      // And, unfortunately, those two processes do not share cookies. So any sessionid-cookie that was established in the first channel 
      // is not being transported to the server in the second channel. This means that the server upload code cannot associate the posted 
      // data with an active session and rejects the data, thus failing the upload.
      //
      if (secAdvisor == null) {
        System.out.println("UploadDataTrackFileServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload analysis file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }
      
      if (secAdvisor.getIsGuest().equals("Y")) {
        throw new Exception("Insufficient permissions to upload data.");
      }

      res.setDateHeader("Expires", -1);
      res.setDateHeader("Last-Modified", System.currentTimeMillis());
      res.setHeader("Pragma", "");
      res.setHeader("Cache-Control", "");

      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
      Part part;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();     
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("idDataTrack")) {
            idDataTrack = new Integer(String.class.cast(value));
          } else if (name.equals("name")) {
            dataTrackName = value;
          } else if (name.equals("codeVisibility")) {
            codeVisibility = value;
          } else if (name.equals("idGenomeBuild")) {
            idGenomeBuild = new Integer(value);
          } else if (name.equals("idDataTrackFolder")) {
            if (value != null && !value.equals("")) {
              idDataTrackFolder = new Integer(value);
            }
          } else if (name.equals("idLab")) {
            if (value != null && !value.equals("")) {
              idLab = new Integer(value);
            }
          }
        }

        if (idDataTrack != null) {
          break;
        } else if (dataTrackName != null && codeVisibility != null && idGenomeBuild != null && idDataTrackFolder != null && idLab != null) {
          break;
        }

      }


      if (idDataTrack != null) {       
        dataTrack = (DataTrack)sess.get(DataTrack.class, idDataTrack);
      } else {
        // If idDataTrack wasn't sent in as parameter, we are adding
        // dataTrack as part of the upload

        // Make sure that name doesn't have forward slashes (/) or &.
        if (dataTrackName.contains("/") || dataTrackName.contains("&")) {
          throw new Exception("The dataTrack name cannnot contain characters / or &.");
        }
        dataTrack = createNewDataTrack(sess, dataTrackName, 
            codeVisibility, 
            idGenomeBuild, 
            idDataTrackFolder != null && idDataTrackFolder.intValue() == -99 ? null : idDataTrackFolder, 
            idLab != null && idLab.intValue() == -99 ? null : idLab);
        sess.flush();       
      }
      
      if (dataTrack == null) {
        throw new Exception("Cannot find data track in db");
      }
      
      if (!secAdvisor.canUpdate(dataTrack)) {
        throw new Exception("Insufficient permissions to write to data track");
      }

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

      // Make sure that the genometry server dir exists
      if (!new File(baseDir).exists()) {
        boolean success = (new File(baseDir)).mkdir();
        if (!success) {
          throw new IOException("Unable to create directory " + baseDir);      
        }
      }

      String dataTrackFileDir = dataTrack.getDirectory(baseDir);

      // Create dataTrack directory if it doesn't exist
      if (!new File(dataTrackFileDir).exists()) {
        boolean success = (new File(dataTrackFileDir)).mkdir();
        if (!success) {
          throw new IOException("Unable to create directory " + dataTrackFileDir);      
        }      
      }
      while ((part = mp.readNextPart()) != null) {        
        if (part.isFile()) {
          // it's a file part
          FilePart filePart = (FilePart) part;
          fileName = filePart.getFileName();              
          //is it a bulk upload? 
          if (fileName.endsWith("bulkUpload")) {
            //write temp file
            tempBulkUploadFile = new File (baseDir, "TempFileDeleteMe_"+DataTrackUtil.createRandowWord(6));
            filePart.writeTo(tempBulkUploadFile); 
            //make new dataTracks based on current dataTrack with modifications from the 1.ablk text file
            DataTrackFolder ag = getDefaultDataTrackFolder(dataTrack, sess, idDataTrackFolder);             
            uploadBulkDataTracks(sess, tempBulkUploadFile, dataTrack, ag, res);
            if (tempBulkUploadFile.exists()) { 
              if (!tempBulkUploadFile.delete()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + tempBulkUploadFile.getName() + " during bulk upload.");
              }
              break;
            }
          }

          // Is this a valid file extension?
          if (!DataTrackUtil.isValidDataTrackFileType(fileName)) {
            String message = "Bypassing upload of dataTrack file  " + fileName + " for dataTrack " + dataTrack.getName() + ".  Unsupported file extension.";        
            throw new Exception(message);
          }

          // If this is a bar file, does the file name match a known segment name?
          if (fileName.toUpperCase().endsWith(".BAR")) {
            GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
            if (!DataTrackUtil.fileHasSegmentName(fileName, genomeBuild)) {
              String message = "Bypassing upload of dataTrack file  " + fileName + " for dataTrack " + dataTrack.getName() + ".  File name is invalid because it does not start with a valid segment name.";                   
              throw new Exception(message);
            }
          }

          if (fileName != null) {
            // the part actually contained a file
            File file = new File (dataTrackFileDir, fileName);
            long size = filePart.writeTo(file);
            //check size of text files
            if (DataTrackUtil.tooManyLines(file)){
              if (!file.delete()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + file.getName() + ".");
              }
              throw new Exception("Aborting upload, text formatted dataTrack file '" + dataTrack.getName() + " exceeds the maximum allowed size ("+
                  Constants.MAXIMUM_NUMBER_TEXT_FILE_LINES+" lines). Convert to xxx.useq (see http://useq.sourceforge.net/useqArchiveFormat.html) or other binary form (xxx.bar).");
            }
            // bam file? check if it is sorted and can be read
            if (fileName.toUpperCase().endsWith(".BAM")) {
              try {
                String error = DataTrackUtil.checkBamFile(file);
                if (error != null ) {
                  if (!file.delete()) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unable to delete file " + file.getName() + ".");
                  }
                  throw new Exception("Errors found with bam file -> "+fileName+". Aborting upload. "+error);
                }
              } catch (Exception e) {
                throw new Exception("Bypassing upload of BAM file " + file.getName() + ". Unexpected error encountered " + e.toString());
              }
            }
          }

        }
      }
      sess.flush();
      tx.commit();
      
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("SUCCESS");
      root.addAttribute("idDataTrack", dataTrack.getIdDataTrack().toString());
      root.addAttribute("idDataTrackFolder", idDataTrackFolder.toString());
      root.addAttribute("idGenomeBuild", idGenomeBuild.toString());
      XMLWriter writer = new XMLWriter(res.getOutputStream(), OutputFormat.createCompactFormat());
      writer.write(doc);

    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
      e.printStackTrace();
      // TODO: Figure out auto-commit setting that prevents rollback
      // not sure we want to rollback anyways....
      //tx.rollback();
      sess.flush();
      tx.commit();
      res.addHeader("message", e.getMessage());
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("ERROR");
      root.addAttribute("message", e.getMessage());
      XMLWriter writer = new XMLWriter(res.getOutputStream(), OutputFormat.createCompactFormat());
      writer.write(doc);  
      
      
    } finally {
      if (tempBulkUploadFile != null && tempBulkUploadFile.exists()) tempBulkUploadFile.delete();
      if (sess != null) {
        try {
          HibernateSession.closeSession();          
        } catch (Exception e) {
        }
      }
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

    }
  }
  private DataTrack createNewDataTrack(Session sess, String name, String codeVisibility, Integer idGenomeBuild, Integer idDataTrackFolder,  Integer idLab) throws Exception {
    DataTrack dataTrack = new DataTrack();


    dataTrack.setName(name);
    dataTrack.setIdGenomeBuild(idGenomeBuild);
    dataTrack.setCodeVisibility(codeVisibility);
    dataTrack.setIdLab(idLab);
    dataTrack.setIsLoaded("N");
    dataTrack.setDataPath(baseDir);

    // Only set ownership if this is not an admin
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      dataTrack.setIdAppUser(secAdvisor.getIdAppUser());        
    }

    dataTrack.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
    dataTrack.setCreatedBy(this.secAdvisor.getUID());

    sess.save(dataTrack);
    sess.flush();

    // Get the dataTrack folder this dataTrack is in.
    DataTrackFolder folder = null;
    if (idDataTrackFolder == null) {
      // If this is a root dataTrack, find the default root dataTrack
      // folder for the genome version.
      GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));
      folder = gv.getRootDataTrackFolder();
      if (folder == null) {
        throw new Exception("Cannot find root dataTrack folder for " + gv.getDas2Name());
      }
    } else {
      // Otherwise, find the dataTrack folder passed in as a request parameter.
      folder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
    }

    // Add the dataTrack to the dataTrack folder
    Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
    for(Iterator<?>i = folder.getDataTracks().iterator(); i.hasNext();) {
      DataTrack a = DataTrack.class.cast(i.next());
      newDataTracks.add(a);
    }
    newDataTracks.add(dataTrack);
    folder.setDataTracks(newDataTracks);


    // Assign a file directory name
    dataTrack.setFileName("DT" + dataTrack.getIdDataTrack());

    return dataTrack;

  }
  private DataTrackFolder getDefaultDataTrackFolder(DataTrack sourceDataTrack, Session sess, Integer idDataTrackFolder) throws Exception{   
    // Get the folder this dataTrack is in.
    DataTrackFolder folder = null;
    if (idDataTrackFolder == null || idDataTrackFolder.intValue() == -99) {
      // If this is a root dataTrack, find the default root dataTrack
      // folder for the genome version.
      GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, sourceDataTrack.getIdGenomeBuild()));
      folder = gv.getRootDataTrackFolder();
      if (folder == null) {
        throw new Exception("Cannot find root folder for " + gv.getDas2Name());
      }
    } else {
      // Otherwise, find the folder passed in as a request parameter.      
      folder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
    }
    return folder;
  }

  /**Reads in a tab delimited file (name, fullPathFileName, summary, description) describing new DataTrackFolders to be created using a sourceDataTrackFolder as a template.
   * @author davidnix*/
  private void uploadBulkDataTracks(Session sess, File spreadSheet, DataTrack sourceDataTrack, DataTrackFolder defaultDataTrackFolder, HttpServletResponse res) 
  throws Exception  {

    //validate upload file
    String errors = validateBulkUploadFile(spreadSheet);
    if (errors != null) {
      throw new BulkFileUploadException (errors);
    }

    //all OK so make dataTracks
    BufferedReader in = null;
    try {
      in = new BufferedReader (new FileReader(spreadSheet));
      String line;

      //for each line create a new dataTrack if it doesn't exist
      while ((line = in.readLine()) != null){
        line = line.trim();
        if (line.length() == 0 || line.startsWith("#") || line.startsWith("Name")) continue;

        //parse name, fileName, summary, description
        Matcher mat = BULK_UPLOAD_LINE_SPLITTER.matcher(line);
        mat.matches();
        String name = mat.group(1).trim();

        //remove any preceeding /s
        if (name.startsWith("/")) name = name.substring(1);
        File dataFile = new File (mat.group(2).trim());
        String summary = mat.group(3).trim();
        String description = mat.group(4).trim();

        // If the dataTrack name is preceded by a directory structure, parse
        // out actual name and create/find the dataTrack folders represented
        // the the directory structure embedded in the name;
        String dataTrackName = "";
        DataTrackFolder ag = null;
        if (name.lastIndexOf("/") >= 0) {
          dataTrackName = name.substring(name.lastIndexOf("/") + 1);
          ag = getSpecifiedDataTrackFolder(sess, defaultDataTrackFolder, name.substring(0, name.lastIndexOf("/")));
        } else {
          dataTrackName = name;
          ag = defaultDataTrackFolder;
        }

        //does the dataTrack currently exist?  if so then add files to it, needed for bar and bam files
        File dir = fetchDataTrackDirectory(ag, dataTrackName);
        if (dir != null) {
          File moved = new File (dir, dataFile.getName());
          if (dataFile.renameTo(moved) == false) {
            //System.out.println("Moved "+moved.canWrite());
            //System.out.println("Dir "+dir.canWrite()+" | "+dir.exists());
            //System.out.println("DataFile "+dataFile.canWrite()+" | "+dataFile.exists());
            throw new BulkFileUploadException("Failed to move the dataFile '" +dataFile + "' to its archive location  '" + moved +"' . Aborting bulk uploading.");

          }
        }
        //make new dataTrack cloning current dataTrack
        else addNewClonedDataTrack(sess, sourceDataTrack, dataTrackName, summary, description, dataFile, ag, res);

      }

    } catch (Exception e) {
      throw e;
    } finally {
      if (in != null)
        try {
          in.close();
        } catch (IOException e) {
          throw e;
        }
    }

  }

  /**Checks the bulk upload file before making any dataTracks. Returns null if all OK or error messages.
   * @author davidnix*/
  private String validateBulkUploadFile(File spreadSheet)   {
    //for each line check params are OK
    BufferedReader in = null;
    try {
      in = new BufferedReader (new FileReader(spreadSheet));
      String line;
      StringBuilder errors = new StringBuilder();
      HashSet<String> bamBaiFiles = new HashSet<String>();
      while ((line = in.readLine()) != null){
        line = line.trim();
        if (line.length() == 0 || line.startsWith("#") || line.startsWith("Name")) continue;

        //parse name, fileName, summary, description
        Matcher mat = BULK_UPLOAD_LINE_SPLITTER.matcher(line);
        if (mat.matches() == false) { 
          errors.append("Malformed data line -> " + line+" . \n");
          continue;
        }
        //name is required
        String name = mat.group(1).trim();
        if (name.length()==0) {
          errors.append("Missing name -> " + line+" . \n");
        }
        //check file
        File dataFile = new File (mat.group(2).trim());
        if (dataFile.canRead() == false || dataFile.canWrite() == false) {
          errors.append("Cannot find/modify file -> " + line+" . \n");
        }
        //check live file
        else {
          //check file extension
          String fileName = dataFile.toString();
          if (DataTrackUtil.isValidDataTrackFileType(fileName) == false) {
            errors.append("Unsupported file type ->  " + line+" . \n");
          }
          else {
            //too many lines in txt file?
            if (DataTrackUtil.tooManyLines(dataFile)) errors.append("Too many lines in file ->  " + line+" . Convert to xxx.useq (see http://useq.sourceforge.net/useqArchiveFormat.html).\n");
            //bam or bai?
            if (fileName.endsWith(".bam") || fileName.endsWith(".bai")) bamBaiFiles.add(name+"__"+fileName);
            //check bam file
            if (fileName.endsWith(".bam")) {
              String log = DataTrackUtil.checkBamFile(dataFile);
              if (log != null) errors.append("Problems were found with this bam file ->  " + line+" . "+log);
            }
          }
        }
      }

      //check bam and bai files, must be paired
      for (String f: bamBaiFiles){
        if (f.endsWith(".bam")) {
          String bai1 = f.substring(0, f.length()-4)+".bai";
          String bai2 = f+".bai";
          if (bamBaiFiles.contains(bai1) == false && bamBaiFiles.contains(bai2) == false) errors.append("Missing xxx.bai index file for ->  " + f+" . \n");
        }
        else {
          //else bai, might be .bam.bai
          String bam = f.substring(0, f.length()-4);
          if (bam.endsWith(".bam") == false) bam += ".bam";
          if (bamBaiFiles.contains(bam) == false) errors.append("Missing xxx.bam alignment file for ->  " + f+" . \n");
        }
      }
      if (errors.length() != 0) {
        errors.append("Aborting bulk uploading. \n");
        return errors.toString();
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null)
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return null;
  }
  

  /*
   * Get the annotation grouping (off of the base annotation grouping) specified as a directory structure 
   * in the annotation name.  If annotation groupings do not exist, create them.  
   */
  private DataTrackFolder getSpecifiedDataTrackFolder(Session sess, DataTrackFolder annotationFolderBase, String name){
    DataTrackFolder agNext = annotationFolderBase;

    String[] tokens = name.split("/");
    DataTrackFolder agCurrent = annotationFolderBase;
    for (int x = 0; x < tokens.length; x++) {
      String agName = tokens[x];
      agNext = null;
      for (Iterator<?> i = agCurrent.getFolders().iterator(); i.hasNext();) {
        DataTrackFolder ag = DataTrackFolder.class.cast(i.next());
        if (ag.getName().equalsIgnoreCase(agName)) {
          agNext = ag;
          break;
        }
      }
      if (agNext == null) {
        agNext = new DataTrackFolder();
        agNext.setName(agName);
        agNext.setIdParentDataTrackFolder(agCurrent.getIdDataTrackFolder());
        agNext.setIdGenomeBuild(agCurrent.getIdGenomeBuild());
        agNext.setIdLab(agCurrent.getIdLab());
        sess.save(agNext);
        sess.flush(); 
        sess.refresh(agNext);
        sess.refresh(agCurrent);
      }
      agCurrent = agNext;
    }
    return agNext;    
  }


  /**Adds a new DataTrack cloning in part the source annotation. For bulk uploading.
   * @author davidnix*/
  private void addNewClonedDataTrack(Session sess, DataTrack sourceDataTrack, String name, String summary, String description, File dataFile, DataTrackFolder ag, HttpServletResponse res) 
  throws BulkFileUploadException, UnknownPermissionException, Exception {   


    // Make sure the user can write this annotation 
    if (!this.secAdvisor.canUpdate(sourceDataTrack)) {
      throw new Exception("Insufficient permision to write annotation.");
    }

    DataTrack dup = new DataTrack();

    //name
    dup.setName(name);

    //description
    if (description.length()!=0) dup.setDescription(description);
    else dup.setDescription(sourceDataTrack.getDescription());

    //summary
    if (summary.length()!=0) dup.setSummary(summary);
    else dup.setSummary(sourceDataTrack.getSummary());

    dup.setCodeVisibility(sourceDataTrack.getCodeVisibility());
    dup.setIdLab(sourceDataTrack.getIdLab());
    dup.setIdAppUser(sourceDataTrack.getIdAppUser());
    dup.setIdGenomeBuild(sourceDataTrack.getIdGenomeBuild());
    dup.setIsLoaded("N");
    dup.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
    dup.setCreatedBy(this.secAdvisor.getUID());
    dup.setDataPath(baseDir);

    //save DataTrack so that it's assigned an ID
    sess.save(dup);

    //add annotation properties   
    Set<PropertyEntry> clonedAPSet = new HashSet<PropertyEntry>(); 

    //for each PropertyEntry in the source DataTrack
    for(Iterator<?> i = sourceDataTrack.getPropertyEntries().iterator(); i.hasNext();) {

      //get the PropertyEntry
      PropertyEntry sourceAP = (PropertyEntry)i.next();

      //make clone and copy over params from source
      PropertyEntry clonedAP = new PropertyEntry();
      clonedAP.setIdProperty( sourceAP.getIdProperty());
      clonedAP.setValue( sourceAP.getValue());
      clonedAP.setIdDataTrack(dup.getIdDataTrack());

      //save it and flush it to assign the DB id
      sess.save(clonedAP);
      sess.flush();

      //add to set
      clonedAPSet.add(clonedAP);

      Set<PropertyEntryValue> clonedAPV = new HashSet<PropertyEntryValue>();

      //for each PropertyEntryValue in the sourceAP
      for (Iterator<?> iX = sourceAP.getValues().iterator(); iX.hasNext();) {

        PropertyEntryValue sourceAV = (PropertyEntryValue)iX.next();

        //make clone and copy over params from source
        PropertyEntryValue clonedAV = new PropertyEntryValue();
        clonedAV.setIdPropertyEntry(clonedAP.getIdPropertyEntry());
        clonedAV.setValue(sourceAV.getValue());

        //save it to DB
        sess.save(clonedAV);

        //add to set
        clonedAPV.add(clonedAV);
      }

      //add set to AP
      clonedAP.setValues(clonedAPV);

      TreeSet<PropertyOption> clonedOptions = new TreeSet<PropertyOption>(new PropertyOptionComparator());

      //for each PropertyOption in the sourceAP
      //don't understand how this will work!
      for (Iterator<?> iY = sourceAP.getOptions().iterator(); iY.hasNext();) {
        PropertyOption sourceOption = (PropertyOption)iY.next();
        clonedOptions.add(sourceOption);    
      }

      //add set to AP
      clonedAP.setOptions(clonedOptions);
    }

    //add Set of DataTrackPropery to cloned DataTrack
    dup.setPropertyEntries(clonedAPSet);

    //collaborators?
    TreeSet<AppUser> collaborators = new TreeSet<AppUser>(new AppUserComparator());
    Iterator<?> cIt = sourceDataTrack.getCollaborators().iterator();
    while (cIt.hasNext()) collaborators.add((AppUser)cIt.next());
    dup.setCollaborators(collaborators);

    //is this needed?
    sess.save(dup);
    sess.flush();

    // Add the annotation to the annotation grouping
    Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
    for(Iterator<?> i = ag.getDataTracks().iterator(); i.hasNext();) {
      DataTrack a = DataTrack.class.cast(i.next());
      newDataTracks.add(a);
    }
    newDataTracks.add(dup);
    ag.setDataTracks(newDataTracks);

    sess.flush();

    // Create a file directory and move in the data file
    dup.setFileName("DT" + dup.getIdDataTrack());
    File dir = new File (baseDir, dup.getFileName());
    if (!dir.mkdir()) {
      throw new BulkFileUploadException("Failed to move the dataFile '" + dataFile + "' to its archive location.  Rename failed . Aborting bulk uploading.");
    }
    File moved = new File (dir, dataFile.getName());
    if (dataFile.renameTo(moved) == false) {
      throw new BulkFileUploadException("Failed to move the dataFile '" +dataFile + "' to its archive location  '" + moved +"' . Aborting bulk uploading.");
    }
  }
  

  /**Looks for an DataTrack in the provided DataTrackGrouping with the given name.  Returns null if not found or its directory.
   * Used for adding multiple files to the same DataTrack to suport bar and bam file formats.
   * @author davidnix*/
  private File fetchDataTrackDirectory (DataTrackFolder folder, String dataTrackName){
    Iterator it = folder.getDataTracks().iterator();
    while (it.hasNext()){
      DataTrack a = (DataTrack)it.next();
      if (a.getName().equals(dataTrackName)) return new File (a.getDirectory(baseDir));
    }
    return null;
  }


}
