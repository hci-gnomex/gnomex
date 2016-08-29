/**
 * 
 */
package hci.gnomex.httpclient;

import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

/**
 * @author Tony Di Sera This is a java main that will be called by a batch script to look for chromatogram files in the dropoff directory. Read each files from
 *         the directory, calling the http service SaveChromatogramFromFile.
 *
 */
public class SaveChromatogramsFromFiles extends HttpClientBase {

	private static int sleepInterval = 300000; // Number of milliseconds to sleep between running. defaults to 300 seconds (5 mins).

	private boolean debug = false;
	private String dropFilePath;
	private String archiveFilePath;
	private Integer idPlateWell;
	private File currentFile;
	private String filePath;
	private String currentFD;
	private StringBuffer errors;
	private int numerrors;
	private boolean noemail = false;
	private String toAddress = "gnomex.support@hci.utah.edu"; // "LabTech_Group@genetics.utah.edu";
	private String fromAddress = "GNomEx.Support@hci.utah.edu";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SaveChromatogramsFromFiles saveChromatograms = new SaveChromatogramsFromFiles(args);

		while (true) {
			try {

				saveChromatograms.run();

				Thread.sleep(sleepInterval);

			} catch (Exception e) {
				System.out.println(getTimeStamp() + "Exception: " + e.getMessage());
				try {
					Thread.sleep(sleepInterval);
				} catch (Exception e1) {
				}
			}
		}
	}

	private SaveChromatogramsFromFiles(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-sleepSeconds")) {
				sleepInterval = Integer.valueOf(args[++i]) * 1000;
			} else if (args[i].equals("-h")) {
				printUsage();
				return;
			} else if (args[i].equals("-debug")) {
				debug = true;
			} else if (args[i].equals("-properties")) {
				propertiesFileName = args[++i];
			} else if (args[i].equals("-dropFilePath")) {
				dropFilePath = args[++i];
			} else if (args[i].equals("-archiveFilePath")) {
				archiveFilePath = args[++i];
			} else if (args[i].equals("-server")) {
				server = args[++i];
			} else if (args[i].equals("-noemail")) {
				noemail = true;
			} else if (args[i].equals("-idPlateWell")) {
				idPlateWell = Integer.valueOf(args[++i]);
			}
		}

	}

	protected String getServletName() {
		return "SaveChromatogramFromFileServlet";
	}

	protected boolean checkParms() {
		if (dropFilePath == null || dropFilePath.equals("") || archiveFilePath == null || archiveFilePath.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	protected String getParms() throws UnsupportedEncodingException {
		String parms = URLEncoder.encode("fileName", "UTF-8") + "=" + URLEncoder.encode(currentFile.getName(), "UTF-8");
		parms += "&" + URLEncoder.encode("filePath", "UTF-8") + "=" + URLEncoder.encode(filePath, "UTF-8");
		if (idPlateWell != null) {
			parms += "&idPlateWell=" + idPlateWell.toString();
		}
		return parms;
	}

	protected void printUsage() {
		System.out.println("java hci.gnomex.utility.SaveChromatogramsFromFiles " + "\n" + "[-debug] " + "\n"
				+ "[-sleepInterval <number of seconds to sleep between run>] " + "\n" + "[-properties <propertiesFileName>] " + "\n" + "-server <server> "
				+ "\n" + "-idPlateWell <first idPlateWell of reaction plate for chromatogram>" + "\n" + "-archiveFilePath <archiveFilePath>" + "\n"
				+ "-dropFilePath <dropFilePath>" + "\n");
	}

	private void run() throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("-------------------------------------------------------------------");
		System.out.println(dateFormat.format(new Date(System.currentTimeMillis())) + " SaveChromatogramsFromFiles started");

		numerrors = 0;
		errors = new StringBuffer(32768);

		errors.append("The following Chromatogram files did not parse and were deleted:\n\n");
		errors.append("File\tDirectory\tAddition Information\n");

		try {
			init();

			//
			// For each file, save chromatogram from file
			//
			File dropFile = new File(dropFilePath);
			if (!dropFile.exists()) {
				System.out.println("Drop file path " + dropFilePath + " does not exist.");
				System.exit(-1);
			}
			if (!dropFile.isDirectory()) {
				System.out.println("Drop file path " + dropFilePath + " is not a directory.");
				System.exit(-1);
			}

			TreeSet<File> theFiles = new TreeSet<File>();
			hashFiles(dropFile, theFiles);

			int theIdPlateWell = idPlateWell != null ? idPlateWell.intValue() : -1;
			for (File f : theFiles) {
				currentFile = f;
				int pos = f.getCanonicalPath().lastIndexOf(f.getName());
				filePath = f.getCanonicalPath().substring(0, pos - 1);
				if (debug) {
					System.out.println(f.getName() + " " + filePath);
				}

				// remember which file and directory we are processing in case of error
				currentFD = f.getName() + "\t" + filePath;

				// Note this calls checkSuccess when input is read.
				callServletImpl();

				if (idPlateWell != null) {
					idPlateWell = new Integer(idPlateWell.intValue() + 1);
				}

			}

			// did we have any errors?
			if (numerrors > 0) {
				System.out.println("*********************** ERRORS ************************");
				System.out.println(errors.toString());
				System.out.println("*********************** ERRORS ************************");

				// send an email?
				if (!noemail) {
					sendErrorReport(toAddress, fromAddress);
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new Exception(e.toString());
		}
	}

	// No longer throws an exception. An email is sent with the file information
	// and the file is deleted.
	protected Boolean checkSuccess(BufferedReader in) {
		String inputLine;
		Boolean success = false;
		String reason = "";
		try {
			while ((inputLine = in.readLine()) != null) {
				if (debug) {
					System.out.println(inputLine);
				}
				if (inputLine.indexOf("<SUCCESS") >= 0) {
					success = true;
					int start = inputLine.indexOf("destDir=\"");
					if (start == -1) {
						reason = "destDir attribute not returned in XML result";
						success = false;
						break;
					}
					start = start + 9;
					int end = inputLine.indexOf("\"", start);
					if (end == -1) {
						reason = "destDir attribute could not be parsed from XML result";
						success = false;
						break;
					}
					String destDirName = inputLine.substring(start, end);
					File destDir = new File(destDirName);
					if (!destDir.exists()) {
						destDir.mkdirs();
					}

					reason = moveFile(currentFile, archiveFilePath, destDirName);
					if (!reason.equals("")) {
						success = false;
						break;
					}
				}
			}
			System.out.println();
			if (!success) {
				System.out.println(inputLine);
				System.out.println("Unable to save chromatogram file");
				System.out.println(currentFD + "\t" + reason);

				numerrors++;
				errors.append(currentFD + "\t" + reason + "\n");
				success = true;

				// delete it
				currentFile.delete();
			}
		} catch (IOException ioe) {
		}

		return success;
	}

	private void hashFiles(File file, TreeSet<File> theFiles) {
		File[] childFiles = file.listFiles();
		for (int x = 0; x < childFiles.length; x++) {
			File childFile = childFiles[x];
			if (childFile.isDirectory()) {
				if (childFile.listFiles().length == 0) {
					System.out.println("encountered empty directory " + childFile.getName());
					// If the folder is empty and older than 12 hours, delete it.
					long diff = new Date().getTime() - childFile.lastModified();
					if (diff > 12 * 60 * 60 * 1000) {
						String folderName = childFile.getName();
						if (childFile.delete()) {
							System.out.println("deleting empty directory " + folderName);
						} else {
							System.out.println("unable to delete empty directory " + folderName);
						}
					}
				} else {
					hashFiles(childFile, theFiles);
				}
			} else {
				if (childFile.getName().toLowerCase().endsWith("ab1") || childFile.getName().toLowerCase().endsWith("abi")) {
					theFiles.add(childFile);
				}
			}
		}
	}

	// returns "" if successful otherwise the reason for the failure
	private String moveFile(File sourceFile, String archiveFilePath, String destDirName) {

		String reason = "";

		// First let's backup the file to an archive dna seq drop folder
		String archiveDir = archiveFilePath + File.separator + sourceFile.getParentFile().getName();
		File ad = new File(archiveDir);
		if (!ad.exists()) {
			ad.mkdirs();
			if (!ad.exists()) {
				reason = "Unable to create archive directory " + archiveDir;
				return reason;
			}
		}
		String archiveFileName = archiveDir + File.separator + sourceFile.getName();
		boolean success = this.copyFile(sourceFile, archiveFileName);
		if (!success) {
			reason = "Unable to copy file " + sourceFile.getPath() + " to " + archiveFileName;
			return reason;
		}

		String destFileName = destDirName + File.separator + sourceFile.getName();

		// Try to move to file. If we can't move it, copy then delete it.
		String operation = "java file rename";
		success = sourceFile.renameTo(new File(destFileName));
		if (!success) {
			// Java rename didn't work, possibly because we are trying to
			// move file across file systems. Try "mv" command instead.
			try {
				operation = "move";
				Process process = Runtime.getRuntime().exec(new String[] { "mv", sourceFile.getCanonicalPath(), destFileName });
				process.waitFor();
				process.destroy();

				// Find out if the move worked
				if (!new File(destFileName).exists()) {
					success = false;
				} else {
					success = true;
				}
			} catch (Exception e) {
				success = false;
			}

			// Java rename and "mv" commend didn't work. Now we just
			// throw up out hands and do a copy.
			if (!success) {
				operation = "copy";
				// If move doesn't work then do a copy/delete
				FileChannel in = null;
				FileChannel out = null;

				try {
					in = new FileInputStream(sourceFile).getChannel();
					File outFile = new File(destFileName);
					out = new FileOutputStream(outFile).getChannel();
					in.transferTo(0, in.size(), out);
					in.close();
					in = null;
					out.close();
					out = null;
					sourceFile.delete();
					success = true;
					operation = "move (file system)";
				} catch (Exception e) {
					success = false;
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (Exception e) {
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (Exception e) {
						}
					}

				}

			}
		}

		if (success) {
			System.out.println(getTimeStamp() + operation + " " + sourceFile.getName() + " to " + destDirName);
		} else {
			reason = "ERROR - " + operation + " " + sourceFile.getName() + " to " + destDirName + " failed.";
		}

		return reason;
	}

	private static String getTimeStamp() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(new Date()) + " ";
	}

	private boolean copyFile(File f, String targetFileName) {
		FileChannel in = null;
		FileChannel out = null;
		boolean success = false;
		try {
			in = new FileInputStream(f).getChannel();
			File outFile = new File(targetFileName);
			out = new FileOutputStream(outFile).getChannel();
			in.transferTo(0, in.size(), out);
			in.close();
			in = null;
			out.close();
			out = null;
			success = true;
		} catch (Exception e) {
			success = false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

		}
		return success;
	}

	private void sendErrorReport(String toAddress, String fromAddress) {
		try {
			java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
			Properties mailProps = new BatchMailer().getMailProperties();

			MailUtilHelper helper = new MailUtilHelper(mailProps, toAddress, null, null, fromAddress, "Register Files Error [Server: "
					+ localMachine.getHostName() + "]", errors.toString(), null, false, null, server); // DictionaryHelper.getInstance(sess), serverName);
			MailUtil.validateAndSendEmail(helper);

		} catch (Exception e) {
			System.err.println("SaveChromatographFiles unable to send error report.   " + e.toString());
		}
	}

}
