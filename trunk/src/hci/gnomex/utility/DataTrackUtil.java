package hci.gnomex.utility;

import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Segment;
import hci.gnomex.model.UCSCLinkFiles;

import javax.servlet.http.HttpServletRequest;

import hci.gnomex.useq.USeq2UCSCBig;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;
import net.sf.samtools.*;


public class DataTrackUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final double    KB = Math.pow(2, 10);
	public static String[] nonAmbiguousLetters = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","T","U","V","W","X","Y","3","4","6","7","8","9"};   

	private static HashSet<String> urlLinkFileExtensions = null;
  private static boolean autoConvertUSeqArchives = true;
  private static File ucscWig2BigWigExe;
  private static File ucscBed2BigBedExe;
  
	public static final Pattern toStripURL = Pattern.compile("[^a-zA-Z_0-9\\./]");
	/**Strip out non [a-zA-Z_0-9] chars with the replaceWith.*/
	public static String stripBadURLChars(String name, String replaceWith){
		return toStripURL.matcher(name).replaceAll(replaceWith);
	}
	

	/** Fast & simple file copy. From GForman http://www.experts-exchange.com/M_500026.html*/
	public static boolean copy(File source, File dest){
		FileChannel in = null, out = null;
		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();
			long size = in.size();
			MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
			out.write(buf);
			if (in != null) in.close();
			if (out != null) out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**Recursively deletes the files in a directory that are older than the number of days. Will skip a file called "index.html"*/
	public static void deleteNonIndexFiles(File directory, long days){		
		long cutoff = days * 24L* 60L * 1000L *60L;
		long current = System.currentTimeMillis();
		cutoff = current - cutoff;
		File[] files = directory.listFiles();
		for (File f: files){
			if (f.getName().equals("index.html")) continue;
			if (f.lastModified() < cutoff) {
				System.out.println("\nDeleting "+f);
				System.out.println("\tCurr\t"+ System.currentTimeMillis());
				System.out.println("\tCut \t"+(days * 24L* 60L * 1000L *60L));
				System.out.println("\tDiff\t"+cutoff);
				System.out.println("\tLast\t"+f.lastModified());
				deleteDirectory(f);
			}
		}
	}
	
	/**Attempts to delete a directory and it's contents.
	 * Returns false if all the file cannot be deleted or the directory is null.
	 * Files contained within scheduled for deletion upon close will cause the return to be false.*/
	public static void deleteDirectory(File dir){
		if (dir == null || dir.exists() == false) return;
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i=0; i<children.length; i++) {
				deleteDirectory(children[i]);
			}
			dir.delete();
		}
		dir.delete();
	}
	
	/**Makes a soft link between the realFile and the linked File using the linux 'ln -s' command.*/
	public static boolean makeSoftLinkViaUNIXCommandLine(File realFile, File link){
		try {
			String[] cmd = {"ln", "-s", realFile.getCanonicalPath(), link.toString()};
			Runtime.getRuntime().exec(cmd);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Integer getIntegerParameter(HttpServletRequest req, String parameterName) {
		if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
			return new Integer(req.getParameter(parameterName));
		} else{
			return null;
		}
	}
	
	public static Date getDateParameter(HttpServletRequest req, String parameterName) {
		if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
			try {
				return parseDate(req.getParameter(parameterName));				
			} catch (ParseException e) {
				return null;
			}
		} else{
			return null;
		}
	}
	
	public static String getFlagParameter(HttpServletRequest req, String parameterName) {
		if (req.getParameter(parameterName) != null && !req.getParameter(parameterName).equals("")) {
			return req.getParameter(parameterName);
		} else{
			return "Y";
		}
	}
	
	public static boolean fileHasSegmentName(String fileName, GenomeBuild genomeBuild) {
		// For now, just skip this check if segments haven't beens specified
		// for this genome version
		if (genomeBuild.getSegments() == null || genomeBuild.getSegments().size() == 0) {
			return true;
		}
		
		boolean isValid = false;
		for (Iterator i = genomeBuild.getSegments().iterator(); i.hasNext();) {
			Segment segment = (Segment)i.next();
			String fileParts[] = fileName.split("\\.");
			if (fileParts.length == 2) {
				if (fileParts[0].equalsIgnoreCase(segment.getName())) {
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}
	
	public static HashSet<String> getChromosmeSegmentNames(GenomeBuild genomeBuild){
		HashSet<String> chroms = new HashSet<String>();
		for (Iterator<?> i = genomeBuild.getSegments().iterator(); i.hasNext();) {
			Segment segment = (Segment)i.next();
			chroms.add(segment.getName());
		}
		return chroms;
	}
	
	public static boolean isValidDataTrackFileType(String fileName) {		
		boolean isValid = false;
		for (int x=0; x < Constants.DATATRACK_FILE_EXTENSIONS.length; x++) {
			if (fileName.toUpperCase().endsWith(Constants.DATATRACK_FILE_EXTENSIONS[x].toUpperCase())) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	public static boolean isValidSequenceFileType(String fileName) {		
		boolean isValid = false;
		for (int x=0; x < Constants.SEQUENCE_FILE_EXTENSIONS.length; x++) {
			if (fileName.toUpperCase().endsWith(Constants.SEQUENCE_FILE_EXTENSIONS[x].toUpperCase())) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	public static Date parseDate(String date) throws ParseException {
		return new Date(dateFormat.parse(date).getTime());
	}
    
	public static long getKilobytes(long bytes) {
		long kb =  Math.round(bytes / KB);
		if (kb == 0) {
			kb = 1;
		}
		return kb;
	}
	
	public static String removeHTMLTags(String buf) {
		if (buf  != null) {
			buf = buf.replaceAll("<(.|\n)+?>", " ");
			buf = DataTrackUtil.escapeHTML(buf);
		}
		return buf;
	}
	
	public static String escapeHTML(String buf) {
		if (buf != null) {
			buf = buf.replaceAll("&", "&amp;");
			buf = buf.replaceAll("<", "&lt;");
			buf = buf.replaceAll(">", "&gt;");			
			buf = buf.replaceAll("\"", "'");			
		}
		
		return buf;
	}

	public static boolean tooManyLines(File file) throws IOException{
		String lcName = file.getName().toLowerCase();
		// is it a text file to check
		for (int i=0; i< Constants.FILE_EXTENSIONS_TO_CHECK_SIZE_BEFORE_UPLOADING.length; i++){
			if (lcName.endsWith(Constants.FILE_EXTENSIONS_TO_CHECK_SIZE_BEFORE_UPLOADING[i])){
				int counter = 0;
				BufferedReader in = new BufferedReader (new FileReader(file));
				while (in.readLine() != null){
					if (counter > Constants.MAXIMUM_NUMBER_TEXT_FILE_LINES) {
						in.close();
						return true;
					}
					else counter++;
				}
				in.close();
				return false;
			}
		}
		return false;
	}
	

  /**Does some minimal error checking on a bam alignment file.
   * @return null if no problems, otherwise an error.*/
  public static String checkBamFile(File bamFile) {
    String message = null;
    SAMFileReader reader = null;
    Pattern oneTwoDigit = Pattern.compile("\\w{1,2}");
    try {
      reader = new SAMFileReader(bamFile);
      //check sort order
      SAMFileHeader h = reader.getFileHeader();
      if (h.getSortOrder().compareTo(SAMFileHeader.SortOrder.coordinate) !=0) throw new Exception("Your bam file doesn't appear to be sorted by coordinate."); 
      //check that their chromosomes aren't 1,2,3, should be chr1, chr2, chr3
      List<SAMSequenceRecord> chroms = h.getSequenceDictionary().getSequences();
      boolean badChroms = false;
      boolean badMito = false;
      for (SAMSequenceRecord r: chroms){
        if (oneTwoDigit.matcher(r.getSequenceName()).matches()) badChroms = true;
        if (r.getSequenceName().equals("chrMT")) badMito = true;
      }
      if (badChroms) throw new Exception("Your bam file contains chromosomes that are 1-2 letters/ numbers long. For DAS compatibility they need to start with 'chr'.");
      if (badMito) throw new Exception("Your bam file contains a chrMT chromosome. For DAS compatibility convert it to chrM.");
      //read an alignment
      SAMRecordIterator it = reader.iterator();
      if (it.hasNext()) it.next();
      //clean up
      reader.close();
    } catch (Exception e){
      message = e.getMessage();
    } finally {
      if (reader != null) reader.close();
    }
    return message;
  }
  
  /**Creates pseudorandom Strings derived from an alphabet of String[] using the
   * java.util.Random class.  Indicate how long you want a particular word and
   * the number of words.*/
  public static String[] createRandomWords(String[] alphabet,int lengthOfWord,int numberOfWords) {
    ArrayList<String> words = new ArrayList<String>();
    Random r = new Random();
    int len = alphabet.length;
    for (int i = 0; i < numberOfWords; i++) {
      StringBuffer w = new StringBuffer();
      for (int j = 0; j < lengthOfWord; j++) {
        w.append(alphabet[r.nextInt(len)]);
      }
      words.add(w.toString());
    }
    String[] w = new String[words.size()];
    words.toArray(w);
    return w;
  }

  /**Returns a random word using nonambiguous alphabet.  Don't use this method for creating more than one word!*/
  public static String createRandowWord(int lengthOfWord){
    return createRandomWords(nonAmbiguousLetters, lengthOfWord,1)[0];
  }
  
  /**Returns null if no appropriate file is found for http linking or a UCSCLinkFiles object that will let you know if on the fly useq conversion is going on.
   * For bw and bb, only one file will be returned for useq files converted to bw, might have two, one for each strand, for bam will have two, bam and its index bai.*/
  public static UCSCLinkFiles fetchUCSCLinkFiles(List<File> files, String webContextPath) throws Exception{

    if (urlLinkFileExtensions == null){
      urlLinkFileExtensions = new HashSet<String>();
      for (String ext: Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS) urlLinkFileExtensions.add(ext);
    }
    
    boolean canFetchExecutables = fetchUCSCExecutableFiles(webContextPath);
    
    //attempt to find the UCSC executables for converting bed and wig files to bigBed, bigWig formats
    if (autoConvertUSeqArchives && canFetchExecutables == false){
      autoConvertUSeqArchives = false;
      Logger.getLogger(DataTrackUtil.class.getName()).warning("FAILED to find the UCSC big file executables, turning off useq auto conversion.");
    }   

    File useq = null;
    boolean converting = false;
    ArrayList<File> filesAL = new ArrayList<File>();
    for (File f: files){
      int index = f.getName().lastIndexOf(".");
      if (index > 0) {
        String ext = f.getName().substring(index);
        //System.out.println("\nFile Extension "+ ext+" "+f.getName());
        if (ext.equals(USeqUtilities.USEQ_EXTENSION_WITH_PERIOD)) useq = f;
        else if (urlLinkFileExtensions.contains(ext))  filesAL.add(f);
      }
    }

    //convert useq archive?  If a xxx.useq file is found and autoConvertUSeqArchives == true, then the file is converted using a separate thread.
    if (filesAL.size()==0 && useq !=null && autoConvertUSeqArchives){
      //this can consume alot of resources and take 1-10min
      USeq2UCSCBig c = new USeq2UCSCBig(ucscWig2BigWigExe, ucscBed2BigBedExe, useq);
      filesAL = c.fetchConvertedFileNames();
      //converting = true;
      c.convert(); //same thread!
      //c.start(); //separate thread!
    }

    if (filesAL.size() !=0){
      File[] toReturn = new File[filesAL.size()];
      filesAL.toArray(toReturn);
      //stranded?
      boolean stranded = false;
      if (toReturn.length == 2){
        String name = toReturn[0].getName();
        if (name.endsWith("_Plus.bw") || name.endsWith("_Minus.bw")) stranded = true;
      }
      return new UCSCLinkFiles (toReturn, converting, stranded);
    }

    //something bad happened.
    return null;
  }
  
  /**Returns all files and if needed converts useq files to bw and bb. Returns null if something bad happened.*/
  public static UCSCLinkFiles fetchURLLinkFiles(List<File> files, String webContextPath) throws Exception{
    //fetch hashSet
    if (urlLinkFileExtensions == null){
      urlLinkFileExtensions = new HashSet<String>();
      for (String ext: Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS) urlLinkFileExtensions.add(ext);
    }
    
    boolean canFetchExecutables = fetchUCSCExecutableFiles(webContextPath);
    
    //attempt to find the UCSC executables for converting bed and wig files to bigBed, bigWig formats
    if (autoConvertUSeqArchives && canFetchExecutables == false){
      autoConvertUSeqArchives = false;
      Logger.getLogger(DataTrackUtil.class.getName()).warning("FAILED to find the UCSC big file executables, turning off useq auto conversion.");
    } 
    
    File useq = null;
    File bigFile = null;
    
    ArrayList<File> filesAL = new ArrayList<File>();
    for (File f: files){
      int index = f.getName().lastIndexOf(".");
      if (index > 0) {
        String ext = f.getName().substring(index);      
        if (ext.equals(USeqUtilities.USEQ_EXTENSION_WITH_PERIOD)) useq = f;
        else if (ext.equals(".bw") || ext.equals(".bb")) bigFile = f; 
        filesAL.add(f);
      }
    }

    //convert useq archive?  If a xxx.useq file is found and autoConvertUSeqArchives == true, then the file is converted using a separate thread.
    ArrayList<File> convertedUSeqFiles = null;
    if (bigFile == null && useq !=null && autoConvertUSeqArchives){
      //this can consume alot of resources and take 1-10min
      USeq2UCSCBig c = new USeq2UCSCBig(ucscWig2BigWigExe, ucscBed2BigBedExe, useq);
      convertedUSeqFiles = c.fetchConvertedFileNames();
      //converting = true;
    
      c.convert(); //same thread!
      //c.start(); //separate thread!
    }

    if (filesAL.size() !=0){
      //stranded?
      boolean stranded = false;
      if (convertedUSeqFiles != null) {
        filesAL.addAll(convertedUSeqFiles);
        if (convertedUSeqFiles.size() == 2){
          String name = convertedUSeqFiles.get(0).getName();
          if (name.endsWith("_Plus.bw") || name.endsWith("_Minus.bw")) stranded = true;
        }
      }
      File[] toReturn = new File[filesAL.size()];
      filesAL.toArray(toReturn);
      
      return new UCSCLinkFiles (toReturn, false, stranded);
    }

    //something bad happened.
    return null;
  }

  
  public static File checkUCSCLinkDirectory(String baseURL, String webContextPath) throws Exception{
    File urlLinkDir = new File (webContextPath, Constants.URL_LINK_DIR_NAME);
    urlLinkDir.mkdirs();
    if (urlLinkDir.exists() == false) throw new Exception("\nFailed to find and or make a directory to contain url softlinks for UCSC data distribution.\n");

    //add redirect index.html if not present, send them to genopub
    File redirect = new File (urlLinkDir, "index.html");
    if (redirect.exists() == false){
      String toWrite = "<html> <head> <META HTTP-EQUIV=\"Refresh\" Content=\"0; URL="+baseURL+"genopub\"> </head> <body>Access denied.</body>";
      PrintWriter out = new PrintWriter (new FileWriter (redirect));
      out.println(toWrite);
      out.close();
    }

    //delete old softlinks within
    DataTrackUtil.deleteNonIndexFiles(urlLinkDir, Constants.DAYS_TO_KEEP_URL_LINKS);

    return urlLinkDir;
  }


  /**Returns 'bigWig' , 'bigBed', 'bam', or null for xxx.bw, xxx.bb, xxx.bam*/
  public static String fetchUCSCDataType(File[] filesToLink) {
    for (File f: filesToLink){
      String name = f.getName();
      if (name.endsWith(".bw")) return "bigWig";
      if (name.endsWith(".bb")) return "bigBed";
      if (name.endsWith(".bam")) return "bam";
    }
    return null;
  }
  
  private static boolean fetchUCSCExecutableFiles(String webContextPath){
    File ucscExtDir = new File (webContextPath, Constants.UCSC_EXECUTABLE_DIR_NAME);
    ucscWig2BigWigExe = new File (ucscExtDir, Constants.UCSC_WIG_TO_BIG_WIG_NAME);
    ucscBed2BigBedExe = new File (ucscExtDir, Constants.UCSC_BED_TO_BIG_BED_NAME);
    //make executable
    if (ucscWig2BigWigExe.exists()) ucscWig2BigWigExe.setExecutable(true);
    if (ucscBed2BigBedExe.exists()) ucscBed2BigBedExe.setExecutable(true);
    //check files
    if (ucscWig2BigWigExe.canExecute() == false || ucscBed2BigBedExe.canExecute() == false) {
      System.err.println("\nError: can't execute or find "+ucscWig2BigWigExe+" or "+ucscBed2BigBedExe);
      return false;
    }
    return true;
  }
  
  

}
