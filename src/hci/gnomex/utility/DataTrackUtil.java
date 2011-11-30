package hci.gnomex.utility;

import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Segment;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

import hci.gnomex.constants.Constants;

public class DataTrackUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final double    KB = Math.pow(2, 10);
	
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
	
	public static boolean isValidAnnotationFileType(String fileName) {		
		boolean isValid = false;
		for (int x=0; x < Constants.ANNOTATION_FILE_EXTENSIONS.length; x++) {
			if (fileName.toUpperCase().endsWith(Constants.ANNOTATION_FILE_EXTENSIONS[x].toUpperCase())) {
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
}
