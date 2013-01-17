package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UCSCLinkFiles;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**Edited 1/15/2013 Mosbruger


/**Used for making html url links formatted for IGV and softlinked to GNomEx files.*/
public class MakeDataTrackIGVLink extends GNomExCommand implements Serializable {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackIGVLink.class);
	private String dataTrackFileServerWebContext;
	private String baseURL;
	private String baseDir;
	private String analysisBaseDir;
	private String dataTrackFileServerURL;
	private String serverName;
	private static final String IGV_LINK_PATH = "IGVLinks";
	
	public static final Pattern TO_STRIP = Pattern.compile("\\n");

	public void validate() {}

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		serverName = request.getServerName();
	}
	
	private String linkContents(String path,DataTrackFolder folder,int depth, Session sess) throws Exception {
		//Create prefix for indentation
		StringBuilder prefix = new StringBuilder("");
		for (int i=0;i<depth;i++) {
			prefix.append("\t");
		}
		
		path += "/" + folder.getName();
		
		//Create StringBuilder
		StringBuilder xmlResult = new StringBuilder("");
		
		boolean toWrite = false;
		
		//Print out data within the folder
		StringBuilder dataTrackResult = new StringBuilder("");		
		
		ArrayList<DataTrack> dts = new ArrayList<DataTrack>(folder.getDataTracks());
		for (DataTrack dt: dts) {
			//If one of the datatracks is readable by the user, create folder and links
			
			if (getSecAdvisor().canRead(dt)) {
				File dir = new File(path);
				dir.mkdirs();
				
				String trackResults = makeIGVLink(sess,dt,path,prefix);
				if (!trackResults.equals("")) {
					dataTrackResult.append(trackResults);
					toWrite = true;
				}
			} 
		}
		
		//Recursively call printContents in subfolders
		ArrayList<DataTrackFolder> dtfs = new ArrayList<DataTrackFolder>(folder.getFolders());
		StringBuilder dataFolderResult = new StringBuilder("");
		for (DataTrackFolder dtf: dtfs) {
			String result = linkContents(path,dtf,depth+1, sess);
			if (!result.equals("")) {
				dataFolderResult.append(result);
				toWrite = true;
			}
		}
		
		if (toWrite) {
			xmlResult.append(prefix + "<Category name=\"" + folder.getName() + "\">\n");
			xmlResult.append(dataTrackResult);
			xmlResult.append(dataFolderResult);
			xmlResult.append(prefix + "</Category>\n");
		}
		
		return xmlResult.toString();
		
	}

	public Command execute() throws RollBackCommandException {
		try {
			Session sess = getSecAdvisor().getHibernateSession(getUsername());
			baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);
			analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
			dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);      
			dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);
	
			// We have to serve files from Tomcat, so use das2 base url
			baseURL =  dataTrackFileServerURL + "/";
			
			//If the user already has a directory, get the existing name and destroy everything beneath it.  This way, 
			//existing path still work.  This will be nice if we set up a cron job that automatically populates these directories
			//If the user doesn't have a directory, a new random string will be created.
			File linkDir = checkIGVLinkDirectory(baseDir,dataTrackFileServerWebContext);
			String linkPath = this.checkForIGVUserFolderExistence(linkDir, getUsername());
	
			if (linkPath == null) {
				linkPath = UUID.randomUUID().toString() + getUsername();
			}
			
			//Create the users' data directory
			File dir = new File(linkDir.getAbsoluteFile(),linkPath);
			dir.mkdir();
			String rootPath = dir.getAbsolutePath(); //Path via server
			String htmlPath = dataTrackFileServerURL + IGV_LINK_PATH + "/" + linkPath + "/"; //Path wia web
			
			
			//Create Duplicate IGV repository
			Pattern broadPattern = Pattern.compile("\"((.+?)_dataServerRegistry\\.txt)\"");
			URL broadAnns = new URL("http://www.broadinstitute.org/igvdata");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(broadAnns.openStream()));
				String line;
				while ((line=br.readLine()) != null) {
					Matcher broadMatch = broadPattern.matcher(line);
					if (broadMatch.find()) {
						File broadFile = new File(dir,"igv_registry_" + broadMatch.group(2) + ".txt");
						StringBuilder broadAnnData = new StringBuilder("");
						URL broad = new URL("http://www.broadinstitute.org/igvdata/"+broadMatch.group(1));
						try {
							BufferedReader br2 = new BufferedReader(new InputStreamReader(broad.openStream()));
							String line2;
							while((line2=br2.readLine()) != null) {
								broadAnnData.append(line2 + "\n");
							}
							br2.close();
							
							BufferedWriter bw2 = new BufferedWriter(new FileWriter(broadFile));
							bw2.write(broadAnnData.toString());
							bw2.close();
						} catch (IOException ex) {
							addInvalidField("IOError","Could not read from the Broad repository file: " + broadMatch.group(1));
						}
						
					}
				}
				br.close();
			} catch (IOException ioex) {
				addInvalidField("IOError","Could not read from the Broad repository");
			}
			
			/*****************************************************************
			 * Grab the list of available genomes.  Check if the user has data for the genome
			 * and if the genome is supported by IGV.  If so, create a repository for the local 
			 * data and add to Broad repository
			 */
	
			//Grab the list of available GNomEx genomes
			boolean permissionForAny = false; //If the user own something, report IGV Link
			List<Integer> genomeIndexList = sess.createQuery("SELECT idGenomeBuild from GenomeBuild").list();
			
			for(Iterator<Integer> i = genomeIndexList.iterator(); i.hasNext();) {
				//Grab genome build, check if data exists and if IGV supports it.  If no data or no IGV support, skip to next genome
		        Integer gnIdx = i.next();
		        GenomeBuild gb = GenomeBuild.class.cast(sess.load(GenomeBuild.class,gnIdx));
		        String igvGenomeBuildName = gb.getIgvName();
		        ArrayList<DataTrackFolder> dataTrackFolders = new ArrayList<DataTrackFolder>(gb.getDataTrackFolders());
		        if (dataTrackFolders.size() == 0 || igvGenomeBuildName == null) {
		        	continue;
		        }
		        
		        //Find the root folder of Genome.  Should be the one with the UCSC identifier.
		        DataTrackFolder rootFolder=null;
	        	boolean found = false;
		        for (DataTrackFolder folder: dataTrackFolders) {
		        	if (folder.getIdParentDataTrackFolder() == null)  {
		        		if (found) {
		        			addInvalidField("IOError","Found two parental folders???? "  + gb.getGenomeBuildName());
		        		} else {
		        			rootFolder = folder;
		        			found = true;
			        	}
		        	}
		        }
		        if (!found) {
		        	addInvalidField("IOError","Found no parental folders???? "  + gb.getGenomeBuildName());
		        } else {		
					
					//Create data repository
					String result = this.linkContents(rootPath,rootFolder,1, sess);
					
					//If there was a result, create the repository file and add to registry.
					if (!result.equals("")) {
						//Write registry file
						File registry = new File(dir,"igv_registry_" + igvGenomeBuildName + ".txt");
						BufferedWriter br = new BufferedWriter(new FileWriter(registry,true)); //append to registry file in case
						br.write(htmlPath + igvGenomeBuildName + "_dataset.xml");
						br.close();
						
						//Create repository
						StringBuilder dataSetContents = new StringBuilder("");
						dataSetContents.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
						dataSetContents.append("<Global name=\"" + getUsername() + "\" version=\"1\">\n");
						dataSetContents.append(result);
						dataSetContents.append("</Global>\n");
						File xmlFile = new File(dir,igvGenomeBuildName + "_dataset.xml");
						br = new BufferedWriter(new FileWriter(xmlFile));
						br.write(dataSetContents.toString());
						br.close();
						
						//signal results
						permissionForAny = true;
					} //end if results
		        } //end if_else data tracks found
		    } //end for each genome
						
			sess.close();
			
			
			//If the user has permission for any data track, give the the repository link
			if (permissionForAny) {
				StringBuilder sbo = new StringBuilder(htmlPath + "igv_registry_$$.txt");
		     
				xmlResult = "<SUCCESS igvURL=\"" +  sbo.toString() +"\"/>";
				setResponsePage(SUCCESS_JSP);

			} else {
				addInvalidField("insufficient permission", "No data tracks associated with this user!");
			}

		}catch (Exception e){
			log.error("An exception has occurred in MakeDataTrackIGVLinks ", e);
			e.printStackTrace(System.out);
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				getSecAdvisor().closeHibernateSession();        
			} catch(Exception e) {}
		}
		return this;
	}
	
	private String checkForIGVUserFolderExistence(File igvLinkDir, String username) throws Exception{
		File[] directoryList = igvLinkDir.listFiles();
		
		String desiredDirectory = null;
		
		for (File directory: directoryList) {
			if (directory.getName().endsWith(username)) {
				desiredDirectory = directory.getName();
				delete(directory);
			}
		}
		
		return desiredDirectory;
		
	}
	
	private void delete(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
	}
	
	private File checkIGVLinkDirectory(String baseURL, String webContextPath) throws Exception{
	    File igvLinkDir = new File (webContextPath,IGV_LINK_PATH);
	    igvLinkDir.mkdirs();
	    if (igvLinkDir.exists() == false) throw new Exception("\nFailed to find and or make a directory to contain url softlinks for IGV data distribution.\n");

	    //add redirect index.html if not present, send them to genopub
	    File redirect = new File (igvLinkDir, "index.html");
	    if (redirect.exists() == false){
	      String toWrite = "<html> <head> <META HTTP-EQUIV=\"Refresh\" Content=\"0; URL="+baseURL+"genopub\"> </head> <body>Access denied.</body>";
	      PrintWriter out = new PrintWriter (new FileWriter (redirect));
	      out.println(toWrite);
	      out.close();
	    }


	    return igvLinkDir;
	 }

	private String makeIGVLink(Session sess, DataTrack dataTrack, String directory, StringBuilder prefix) throws Exception {
		StringBuilder sb = new StringBuilder("");
		try {
			//load dataTrack
			//DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));      
			sess = getSecAdvisor().getHibernateSession(getUsername());
			
			
			List<File> dataTrackFiles = dataTrack.getFiles(baseDir, analysisBaseDir);
			
		
			//If the user is an admin or is a member of datatrack lab, allow autoconvert
			UCSCLinkFiles link;
			if (getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) || getSecAdvisor().isOwner(dataTrack.getIdAppUser()) || 
					getSecAdvisor().isGroupIAmMemberOf(dataTrack.getIdLab()) || getSecAdvisor().isGroupICollaborateWith(dataTrack.getIdLab())) {
				//check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.useq (will be converted if autoConvert is true))
				link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath(),true);
			} else {
				link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath(),false);
			}
			
			
			//'link' will be null if the user can read the track, doesn't own the track and the bw has not yet been created.
			if (link != null) {
				//check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.useq (will be converted if autoConvert is true))
				//UCSCLinkFiles link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath());
				File[] filesToLink = link.getFilesToLink();
				if (filesToLink== null)  throw new Exception ("No files to link?!");

				// When new .bw/.bb files are created, add analysis files and then link via data
				// track file to the data track.
				MakeDataTrackUCSCLinks.registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);


				//for each file, there might be two for xxx.bam and xxx.bai files, possibly two for converted useq files, plus/minus strands, otherwise just one.
				ArrayList<String> names = new ArrayList<String>();
				ArrayList<String> fileURLs = new ArrayList<String>();
				for (File f: filesToLink){
					
					File annoFile = new File(directory, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
					String annoString = annoFile.toString();

					//make soft link
					DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

					//is it a bam index xxx.bai? If so then skip AFTER making soft link.
					if (annoString.endsWith(".bai")) continue;
					
					//stranded?
					String strand = "";
					if (link.isStranded()){
						if (annoString.endsWith("_Plus.bw")) strand = " + ";
						else if (annoString.endsWith("_Minus.bw")) strand = " - ";
						else throw new Exception ("\nCan't determine strand of bw file? "+annoString);
					}
					

					//dataset name
					String datasetName = dataTrack.getName()+ strand +" "+dataTrack.getFileName();
					names.add(datasetName);
					
					//make bigData URL e.g. bigDataUrl=http://genome.ucsc.edu/goldenPath/help/examples/bigBedExample.bb
					int index = annoString.indexOf(IGV_LINK_PATH);
					String annoPartialPath = annoString.substring(index);
					String bigDataUrl = baseURL + annoPartialPath;
					fileURLs.add(bigDataUrl);
					
					sb.append(prefix + "\t<Resource name=\"" + dataTrack.getName() + "\" path=\"" + bigDataUrl + "\"/>\n");
				}
			} 
			
		} catch (Exception e) {
			throw e;      
		}
		
		return sb.toString();

	}
	
	private static String stripBadNameCharacters(String name){
		name = name.trim();
		name.replaceAll(",", "_");
		return name;
	}

	
}