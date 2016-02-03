package hci.gnomex.daemon;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CreateDataTracks {
	private static CreateDataTracks app = null;
	
	//Command line
	private String analysisName = null;
	private String directory = null;
	private String fileName = null;
	private String login = null;	
	
	//For use in datatrack creation
	private Integer idLab;
	private Integer idGenomeBuild;
	private Integer idUser;

	
	//hibernate stuff
	private SecurityAdvisor secAdvisor;	
	private BatchDataSource dataSource;
    private Session sess;
	private Transaction tx;
	private String serverName = "localhost";
	
	//Gnomex Stuff
	private String baseDirDataTrack = null;
	private String baseDirAnalysis = null;
	//private String orionPath = "../../";
	//private String schemaPath = ".";
	
	
	/********************************
	* Constructor 
	********************************/
	public CreateDataTracks(String[] args) {
		parseArgs(args);
	}
	
	/********************************
	* Pull in command line arguments 
	* and make sure they are valid
	********************************/
	private void parseArgs(String[] args) {
		Pattern pat = Pattern.compile("-[a-z]");
		for (int i = 0; i<args.length; i++){
			String lcArg = args[i].toLowerCase();
			Matcher mat = pat.matcher(lcArg);
			if (mat.matches()){
				char test = args[i].charAt(1);
				try{
					switch (test){
					case 'a': analysisName = args[++i]; break;
					case 'f': fileName = args[++i]; break;
					case 'd': directory  = args[++i]; break;
					case 's': serverName = args[++i]; break;
					case 'l': login = args[++i]; break;					
					case 'h': printDocs(); System.exit(0);
					default: System.out.println("\nProblem, unknown option! " + mat.group()); System.exit(0);
					}
				}
				catch (Exception e){
					System.out.println("\nSorry, something doesn't look right with this parameter request: -"+test);
				}
			}
		}
		
		if (analysisName == null) {
			System.out.println("Please specify an analysis ID");
			System.exit(1);
		}
		
		if (fileName == null) {
			System.out.println("Please specify a fileName");
			System.exit(1);
		}
		
		if (login == null) {
			System.out.println("Please specify a username");
			System.exit(1);
		}
	 	
		if (directory == null) {
			System.out.println("Inferring directory structure from analysis directory");
		}
		
		
	}
	
	/********************************
	* Help menu
	********************************/
	private void printDocs() {
		System.out.println("\n" +
				"**************************************************************************************\n" +
				"**                                   CreateDataTracks: Feb 2013                              **\n" +
				"**************************************************************************************\n" +
				"This application creates DataTracks via the command line.  This script was intended to be \n" +
				"run with Darren's autoaligner script.  This allows the autoaligner to register DataTracks \n" +
				"once the aligner is finished.\n" +
				"\n"+
				
				"Required:\n"+
				"-a Analysis ID \n"+
				"-n Analysis Name \n" +
				"\n" +
				
				"Optional \n" +
				"-d DataTrackDirectory.  Path to datatrack (starts from GenomeBuild) \n "+
				"-s Servername. Name of the server\n " +
				"\n" +
				
				"Example: java -Xmx2G -jar path/to/CreateDataTracks -a A1380 -n 8143R_mouse.bam -d /I/love/mice\n" +

				"**************************************************************************************\n");
	}


	public static void main(String[] args) {
		app = new CreateDataTracks(args);
		app.run();
	}
	
	
	/********************************
	* Driver
	********************************/
	private void run(){
	    try {
	      /**************************************
		  * Connect to GNomEx and Pull analysis Record
		  **************************************/
	      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
	      log.setLevel(Level.ERROR);
	     
	      System.out.println("Attempting to connect");
	    	
	      //dataSource = new BatchDataSource(orionPath,schemaPath);
	      dataSource = new BatchDataSource();
	      app.connect();

	      // Create a security advisor
	      secAdvisor = SecurityAdvisor.create(sess, login);
	      	      
	      System.out.println("Trying to grab analysis");
	      
	      //Grab information from the analysis record.
	      StringBuffer analysisQueryString = new StringBuffer();
	      analysisQueryString.append("SELECT idAnalysis ");
	      analysisQueryString.append("FROM Analysis ");
	      analysisQueryString.append("WHERE number='");
	      analysisQueryString.append(analysisName);
	      analysisQueryString.append("'");
	      
	      //Grab the analysis record
	      List analysisQueryResults = sess.createQuery(analysisQueryString.toString()).list();
	      
	      
	      //If there no records return 
	      if (analysisQueryResults.size() == 0) {
	    	  System.out.println("Analysis does not exist: " + analysisName);
	    	  System.exit(1);
	      } else if (analysisQueryResults.size() > 1) {
	    	  System.out.println("Analysis isn't unique: " + analysisName);
	    	  System.exit(1);
	      }
	      
	      System.out.println("Extracting data");
	      
	      /**************************************
	       * Extract a bunch of data from database
	       **************************************/
	      //analysis
    	  Integer row = (Integer)analysisQueryResults.get(0);
    	  Analysis analysis = (Analysis)sess.load(Analysis.class, row);
    	  
    	  //lab
    	  idLab = analysis.getIdLab();
    	  Lab lab = analysis.getLab();
    	  String labName = lab.getName();
    	  
    	  //genome build
    	  Set<GenomeBuild> gbs = analysis.getGenomeBuilds();
    	  GenomeBuild gb = gbs.iterator().next();
    	  idGenomeBuild = gb.getIdGenomeBuild(); //Just pull the first one, should only be one.
    	  
    	  //fileList
    	  ArrayList<AnalysisFile> fileList = new ArrayList<AnalysisFile>(analysis.getFiles());
    	  ArrayList<AnalysisFile> bamFiles = new ArrayList<AnalysisFile>();
    	  ArrayList<AnalysisFile> covFiles = new ArrayList<AnalysisFile>();
    	  ArrayList<AnalysisFile> vcfFiles = new ArrayList<AnalysisFile>();
    	  
    	  //folderNames
    	  for (AnalysisFile af: fileList) {
    		  String afBaseFileName = fetchBaseName(af.getFileName(), Constants.DATATRACK_FILE_EXTENSIONS);
    		  if (fileName.toUpperCase().equals(afBaseFileName.toUpperCase())) {
    			  String afFileNameUpperCase = af.getFileName().toUpperCase();
  				  if (afFileNameUpperCase.endsWith(".BAM")) {
  						bamFiles.add(af);
  				  } else if (afFileNameUpperCase.endsWith(".USEQ") || afFileNameUpperCase.endsWith(".USEQ")) {
  						covFiles.add(af);
  				  } else if (afFileNameUpperCase.endsWith(".VCF.GZ")) {
  						vcfFiles.add(af);
  				  }
  			  }
    	  }
    	  
    	  //bail if no matching files are found.
    	  if (bamFiles.size() == 0 && covFiles.size() == 0 && vcfFiles.size() == 0) {
    		  System.out.println("Could not find any files matching datatrack criteria.  Check your command line input and "
    		  		+ "analysis directory");
    		  System.exit(1);
    	  }
	       
    	  /*************************************
    	   * Create Subfolder structure
    	   **************************************/
    	  System.out.println("Locating the root directory");
    	  // find the root folder
    	  DataTrackFolder rootFolder = gb.getRootDataTrackFolder();
    	  
    	  
    
		  if (rootFolder == null) {
			  System.out.println("Could not find the root data track folder for genome, exiting: " + gb.getGenomeBuildName());
			  System.exit(1);
		  }
		  
		  ArrayList<DataTrackFolder> existingFolders = new ArrayList<DataTrackFolder>(rootFolder.getFolders());
		  
		 
		  //Determine directory structure
		  ArrayList<String> toCreate = new ArrayList<String>();
		  
		  System.out.println("Making Directory List");
    	  if (directory == null) { //unknown directory structure
    		 toCreate.add(labName);
    		 toCreate.add(analysisName);
    	  } else { //user-specified directory structure
    		  directory = directory.replaceAll("/+","/");
    		  if (directory.charAt(0) == '/') {
    			  directory = directory.substring(1);
    		  }
    		  
    		  
    		  if (directory.equals("") || directory.equals("/")) {
    			  System.out.println("Can't create datatracks in Datatrack root.  Specify a proper directory path or let the applications use default");
    			  System.exit(1);
    		  }
    		  
    		  String[] dirs = directory.split("/");
    		  for (String dir: dirs) {
    			  toCreate.add(dir);
    		  }  
    	  }
    	  
    	  
    	  System.out.println("Creating directories");
    	  //Create Directories if they don't already exist
    	  Integer parentId = rootFolder.getIdDataTrackFolder();
    	  boolean isNew = false;
    	  for (String dir: toCreate) {
    		  if (!isNew) { //If we might find an existing folder
    			  //System.out.println("\tTesting if new");
    			  boolean exists = false;
    			  for (DataTrackFolder dtf: existingFolders) {
        			  if (dtf.getName().equals(dir)) {
        				  //System.out.println("\tNope, this directory exists " + dir);
        				  exists = true;
        				  existingFolders = new ArrayList<DataTrackFolder>(dtf.getFolders());
        				  parentId = dtf.getIdDataTrackFolder();
        				  break;
        			  }
        		  }
        		 
        		  if (!exists) {
        			  //System.out.println("\tDirectory didn't exist, lets create it " + dir);
        			  parentId = this.createDataTrackFolders(dir, parentId);
        			  isNew = true;
        		  }
    		  } else { //We are in new folde territory.
    			  //System.out.println("\tNew territory, creating " + dir);
    			  parentId = this.createDataTrackFolders(dir, parentId);
    		  }
    		  
    		  
    	  }
    	  
    	  /*************************************
    	   * Create DataTracks
    	   **************************************/
    	  
    	  
    	  //Create directory and datatracks for each type
    	  System.out.println("Creating bam");
    	  this.createDataTrackDriver("alignment", parentId, bamFiles,existingFolders);
    	  System.out.println("Creating useq");
    	  this.createDataTrackDriver("coverage",parentId,covFiles,existingFolders);
    	  System.out.println("Creating vcf");
    	  this.createDataTrackDriver("variation",parentId,vcfFiles,existingFolders);
    	  

	    } catch (Exception e) {
	      printError("Error creating data tracks: " + e.getMessage());
	    } finally {
	      if (sess != null) {
	        try {
	          app.disconnect();
	        } catch(Exception e) {
	          printError( "CreateDataTracks was unable to disconnect from hibernate session.   " + e.toString() );
	        }
	      }
	    }
	    System.out.println(new Date() + " processing complete");
	    System.exit(0);
	  }
	
	/******************************
	 * Create Datatracks
	 ****************************/
	private void createDataTrackDriver(String folderName, Integer parentId, ArrayList<AnalysisFile> filesToLink, ArrayList<DataTrackFolder> existing) {
		 //Create directory and datatracks for each type
  	  if (filesToLink.size() != 0) {
  		  Integer subId = null;
  		  boolean exists = false;
  		  for (DataTrackFolder dtf: existing) {
  			if (dtf.getName().equals(folderName)) {
  				exists = true;
  				subId = dtf.getIdDataTrackFolder();
  				break;
  			}
 		  }
  		  if (!exists) {
  			 subId = this.createDataTrackFolders(folderName,parentId);
  			 
  			 sess.disconnect();
  			 try {
				sess = dataSource.connect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			 
  			 
  		  }
  		 
  		  for (AnalysisFile af: filesToLink) {
  			  this.createDataTracks(af.getIdAnalysisFile(),subId);
  		  }
  	  }
	}
	
	/*******************************
	 * Stolen from linkDataTrackFiles. 
	 *********************************/
	private void createDataTracks(Integer idAnalysisFile, Integer idDataTrackFolder) {
		DataTrack dataTrack = null;
		AnalysisFile analysisFile = null;
        
		tx = sess.beginTransaction();
		try {
			PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
			baseDirDataTrack = propertyHelper.getDataTrackDirectory(serverName);
			baseDirAnalysis = propertyHelper.getAnalysisDirectory(serverName);

			analysisFile = (AnalysisFile)sess.load(AnalysisFile.class, idAnalysisFile);
			Analysis analysis = (Analysis)sess.load(Analysis.class, analysisFile.getIdAnalysis());

			dataTrack = new DataTrack();
		

			dataTrack.setName(analysisFile.getAnalysis().getNumber() + "_" + analysisFile.getFileName());
			dataTrack.setIdLab(idLab);
			dataTrack.setIdGenomeBuild(idGenomeBuild);
			

			String defaultVisibility = propertyHelper.getProperty(PropertyDictionary.DEFAULT_VISIBILITY_DATATRACK);
			if (defaultVisibility != null && defaultVisibility.length() > 0) {
				dataTrack.setCodeVisibility(defaultVisibility);
				if(defaultVisibility.compareTo(hci.gnomex.model.Visibility.VISIBLE_TO_INSTITUTION_MEMBERS) == 0) {
					if (dataTrack.getIdLab() != null) {
						Lab lab = (Lab)sess.load(Lab.class, dataTrack.getIdLab());
						Hibernate.initialize(lab.getInstitutions());
						Iterator it = lab.getInstitutions().iterator();
						while(it.hasNext()) {
							Institution thisInst = (Institution) it.next();
							if(thisInst.getIsDefault().compareTo("Y") == 0) {
								dataTrack.setIdInstitution(thisInst.getIdInstitution());            
							}
						}
					}
				}
			} else {
				dataTrack.setCodeVisibility(hci.gnomex.model.Visibility.VISIBLE_TO_GROUP_MEMBERS);
			}                   

			dataTrack.setIdAppUser(analysis.getIdAppUser());
			
			dataTrack.setDataPath(baseDirDataTrack);
			dataTrack.setCreatedBy("tomato");
			dataTrack.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
			dataTrack.setIsLoaded("N");

			sess.save(dataTrack);
			dataTrack.setFileName("DT" + dataTrack.getIdDataTrack());
			sess.flush();

			// Validate the the bam file
			if (analysisFile.getFileName().endsWith(".bam") || analysisFile.getFileName().endsWith(".BAM")) {
				File file = analysisFile.getFile(baseDirAnalysis);
				String error = DataTrackUtil.checkBamFile(file);
				if (error != null) {
					System.out.println("Invalid BAM file: " + error + ". Please correct errors before distributing the data track");
				}
			}


			// If we are linking a .bw/.bb, .bai/.bam, or .vcf.gz/.vcf.gz.tbi see if we have linked to its pair.
			// If not, fill in idAnalysisFileOther, so that the pair is linked as well.
			Integer idAnalysisFileOther = null;
			
			boolean lookForBam = false;
			boolean lookForBai = false;
			boolean lookForBigWig = false;
			boolean lookForUSeq = false;
			boolean lookForVCF = false;
			boolean lookForVCFTBI = false;

			String baseFileName = fetchBaseName(analysisFile.getFileName(), Constants.DATATRACK_FILE_EXTENSIONS);			

			String fileName = analysisFile.getFileName().toUpperCase();
			if (fileName.endsWith(".BAI")) lookForBam = true;
			else if (fileName.endsWith(".BAM")) lookForBai = true;
			else if (fileName.endsWith(".USEQ")) lookForBigWig = true;
			else if (fileName.endsWith(".BW") || fileName.endsWith(".BB")  ) lookForUSeq = true;
			else if (fileName.endsWith(".VCF.GZ")) lookForVCFTBI = true;
			else if (fileName.endsWith(".VCF.GZ.TBI")) lookForVCF = true;	

			for (Iterator i = analysisFile.getAnalysis().getFiles().iterator(); i.hasNext();) {
				AnalysisFile af = (AnalysisFile)i.next();
				String afBaseFileName = fetchBaseName(af.getFileName(), Constants.DATATRACK_FILE_EXTENSIONS);

				//do the baseNames match?
				String afFileNameUpperCase = af.getFileName().toUpperCase();
				if (baseFileName.toUpperCase().equals(afBaseFileName.toUpperCase())) {						
					if (lookForBai && afFileNameUpperCase.endsWith(".BAI")) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					} else if (lookForBam && afFileNameUpperCase.endsWith(".BAM")) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					} else if (lookForBigWig && (afFileNameUpperCase.endsWith(".BW") || afFileNameUpperCase.endsWith(".BB"))) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					} else if (lookForUSeq && (afFileNameUpperCase.endsWith(".USEQ") || afFileNameUpperCase.endsWith(".USEQ"))) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					} else if (lookForVCFTBI && afFileNameUpperCase.endsWith(".VCF.GZ.TBI")) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					} else if (lookForVCF && afFileNameUpperCase.endsWith(".VCF.GZ")) {
						idAnalysisFileOther = af.getIdAnalysisFile();
					}
				}
			}

			//is it a paired file set? then must have other
			String afFileNameUpper = analysisFile.getFileName().toUpperCase(); 
			boolean saveDataTrack = true;
			if (afFileNameUpper.endsWith(".BAM") || afFileNameUpper.endsWith(".BAI") || afFileNameUpper.endsWith(".VCF.GZ") || afFileNameUpper.endsWith(".VCF.GZ.TBI")){
				if (idAnalysisFileOther == null){
					//not sure if this makes this invalid so using boolean
					System.out.println("Missing indexed file or file index?!  Please add either a matching xxx.bam or xxx.bai; or add a xxx.vcf.gz or xxx.vcf.gz.tbi.");
					saveDataTrack = false;
				}
			}

			if (saveDataTrack){
				//Create datatrack
				DataTrackFile dtFile = new DataTrackFile();
				dtFile.setIdAnalysisFile(idAnalysisFile);
				dtFile.setIdDataTrack(dataTrack.getIdDataTrack());
				sess.save(dtFile);

				// If this is a file pair, add the other analysis file
				if (idAnalysisFileOther != null) {			
					DataTrackFile dtFileOther = new DataTrackFile();
					dtFileOther.setIdAnalysisFile(idAnalysisFileOther);
					dtFileOther.setIdDataTrack(dataTrack.getIdDataTrack());
					sess.save(dtFileOther);
				}
				sess.flush();
			}

			// If this is a new data track, add it to the folder
			GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

			DataTrackFolder folderNew = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
	
			Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
			ArrayList<DataTrack> dtl = new ArrayList<DataTrack>(folderNew.getDataTracks());
			Iterator<DataTrack> it = dtl.iterator();
			while(it.hasNext()) {
				newDataTracks.add(it.next());
			}
			newDataTracks.add(dataTrack);
			folderNew.setDataTracks(newDataTracks);
			sess.flush();
			tx.commit();
			


		} catch (Exception e){
			tx.rollback();
			System.out.println("An exception has occurred in LinkDataTrackFile: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	
	
	
	
	
	/**********************************************
	 * Stolen from SaveDataTrackFolder 
	 *********************************************/
	
	private Integer createDataTrackFolders(String folderName, Integer idParentDataTrackFolder) {
	    DataTrackFolder dataTrackFolder = new DataTrackFolder();
		
	    dataTrackFolder.setCreatedBy("tomato");
	    dataTrackFolder.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
	    dataTrackFolder.setName(RequestParser.unEscape(folderName));
	    dataTrackFolder.setIdLab(idLab);
	    dataTrackFolder.setIdGenomeBuild(idGenomeBuild);
	    dataTrackFolder.setIdParentDataTrackFolder(idParentDataTrackFolder);
	    
	    DataTrackFolder parentDataTrackFolder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class,idParentDataTrackFolder));


	    // If parent data track folder is owned by a user group, this
	    // child data track  folder must be as well.
	    if (parentDataTrackFolder.getIdLab() != null) {
	    	if ( idLab == null || !parentDataTrackFolder.getIdLab().equals(idLab)) {
	    		System.out.println("Folder '" + folderName + "' must belong to lab '" + 
	    				DictionaryHelper.getInstance(sess).getLabObject(parentDataTrackFolder.getIdLab()).getName() + "'");
	    		System.exit(1);

	    	}
	    } 
	    
	  tx = sess.beginTransaction();
	  try {
		  sess.save(dataTrackFolder);
		  sess.flush();
		  tx.commit();
	  }	  catch (Exception ex) {
		  tx.rollback();
		  System.out.println("Could not save datatrackfolder: " + ex.getMessage());
		  ex.getStackTrace();
		  System.exit(1);
	  }
	  
      return dataTrackFolder.getIdDataTrackFolder();
	}  
	
	
	
	
	/**Removes the extension and its period.  Thus alta.is.great.bam.bai -> alta.is.great.bam .*/
	private String fetchBaseName(String fileName, String[] lowerCaseExtensions){
		int extLength = 0;
		String lower = fileName.toLowerCase();
		for (String ext: lowerCaseExtensions){
			if (lower.endsWith(ext)){
				extLength = ext.length();
				break;
			}
		}
		return fileName.substring(0, fileName.length() - extLength);
	}
	
	/********************************
	* Connect to GNomEx
	********************************/
	private void connect() throws Exception {
		sess = dataSource.connect();
	}
			 
	/********************************
	* Disconnect from GNomEx
	********************************/
	private void disconnect() throws Exception {
		sess.close();
	}
			  
	/********************************
	* Errors!
	********************************/
	private void printError(String message) {
		String msg = "\n" + new Date() + " " + message;
		System.err.println(msg);
	}
	
}

