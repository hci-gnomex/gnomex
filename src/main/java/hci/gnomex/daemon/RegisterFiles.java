package hci.gnomex.daemon;

// 08/18/2015	tim		Added the ability to specify a particular Analysis or Request

import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GetExpandedAnalysisFileList;
import hci.gnomex.controller.GetRequestDownloadList;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.SampleExperimentFile;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UploadDownloadHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RegisterFiles extends TimerTask {

	private static long fONCE_PER_DAY = 1000 * 60 * 60 * 24; // A day in
																// milliseconds
	private static int fONE_DAY = 1;
	private static int wakeupHour = 2; // Default wakupHour is 2 am
	private static int fZERO_MINUTES = 0;

	private BatchDataSource dataSource;
	private Session sess;

	private static boolean all = false;
	private static Integer daysSince = null;
	private static String serverName = "";
	private static RegisterFiles app = null;

	private Properties mailProps;

	private boolean runAsDaemon = false;

	private String baseExperimentDir;
	private String baseFlowCellDir;
	private String baseAnalysisDir;
	private String flowCellDirFlag;
	private Calendar asOfDate;
	private Calendar runDate; // Date program is being run.

	private Transaction tx;

	private String orionPath = "";

	private Boolean sendMail = true;
	private Boolean testingSendMail = false;
	private Boolean debug = false;
	private Boolean testConnection = false;

	private String errorMessageString = "Error in RegisterFiles";

	private String gnomexSupportEmail = "GNomEx.Support@hci.utah.edu";
	private String fromEmailAddress = "DoNotReply@hci.utah.edu";

	private String currentEntityString;

	private Boolean justOne = false;
	private String analysisId = null;
	private String requestId = null;
	private Boolean removeLinks = false;
	private Boolean analysisWarnings = false;
	private Boolean experimentWarnings = false;

	private String dataTrackFileServerWebContext;

	Map<String, Map<String, String>> emailAnalysisMap = new HashMap<String, Map<String, String>>();
	Map<String, List<AnalysisFileInfo>> analysisFileMap = new HashMap<String, List<AnalysisFileInfo>>();

	public RegisterFiles(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-wakeupHour")) {
				wakeupHour = Integer.valueOf(args[++i]);
			} else if (args[i].equals("-runAsDaemon")) {
				runAsDaemon = true;
			} else if (args[i].equals("-all")) {
				all = true;
			} else if (args[i].equals("-daysSince")) {
				daysSince = Integer.valueOf(args[++i]);
			} else if (args[i].equals("-server")) {
				serverName = args[++i];
			} else if (args[i].equals("-orionPath")) {
				orionPath = args[++i];
			} else if (args[i].equals("-doNotSendMail")) {
				sendMail = false;
			} else if (args[i].equals("-analysisWarnings")) {
				analysisWarnings = true;
			} else if (args[i].equals("-experimentWarnings")) {
				experimentWarnings = true;
			} else if (args[i].equals("-testConnection")) {
				testConnection = true;
			} else if (args[i].equals("-analysis")) {
				justOne = true;
				analysisId = args[++i];
			} else if (args[i].equals("-experiment")) {
				justOne = true;
				requestId = args[++i];
			} else if (args[i].equals("-removeLinks")) {
				removeLinks = true;
			} else if (args[i].equals("-debug")) {
				debug = true;
				testingSendMail = sendMail;
				sendMail = false;
			}
		}

		try {
			if (sendMail || testingSendMail) {
				mailProps = new BatchMailer(orionPath).getMailProperties();
				System.out.println(getCurrentDateString() + ":" + "WARNING: Emails are enabled.");
			} else {
				System.out.println(getCurrentDateString() + ":" + "Emails are disabled.");
			}
		} catch (Exception e) {
			String msg = "Register files cannot initialize mail properties.   " + e.toString() + "\n\t";

			StackTraceElement[] stack = e.getStackTrace();
			for (StackTraceElement s : stack) {
				msg = msg + s.toString() + "\n\t\t";
			}
			;

			System.out.println(msg);

			if (!errorMessageString.equals("")) {
				errorMessageString += "\n";
			}
			errorMessageString += msg;

			this.sendErrorReport(this.gnomexSupportEmail, this.fromEmailAddress);

			System.err.println(errorMessageString);

			System.exit(0);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		app = new RegisterFiles(args);

		// Can either be run as daemon or run once (for scheduled execution - e.g. crontab)
		if (app.runAsDaemon) {
			// Perform the task once a day at <wakeupHour>., starting tomorrow morning
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(app, getWakeupTime(), fONCE_PER_DAY);
		} else {
			app.run();
		}
	}

	@Override
	public void run() {
		runDate = Calendar.getInstance();
		errorMessageString += " on " + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(runDate.getTime()) + "\n";

		try {
			Logger LOG = Logger.getLogger("org.hibernate");
			LOG.setLevel(Level.ERROR);

			dataSource = new BatchDataSource();
			app.connect();

			app.initialize();

			if (removeLinks) {
				app.destroyLinks();
			} else if (justOne && analysisId != null) {
				app.registerAnalysisFiles();
			} else if (justOne && requestId != null) {
				app.registerExperimentFiles();
			} else {
				app.registerExperimentFiles();
				app.registerAnalysisFiles();
			}

			app.disconnect();
			System.out.println("Exiting...");
			System.exit(0);

		} catch (Exception e) {

			String msg = "Could not remove links or register experiment or analysis files (error at " + this.currentEntityString
					+ "). Transaction rolled back:   " + e.toString() + "\n\t";
			System.out.println(msg);

			StackTraceElement[] stack = e.getStackTrace();
			for (StackTraceElement s : stack) {
				msg = msg + s.toString() + "\n\t\t";
			}

			System.out.println(msg);

			try {
				if (tx != null) {
					tx.rollback();
				}
			} catch (TransactionException te) {
				msg += "\nTransactionException: " + te.getMessage() + "\n\t";
				stack = te.getStackTrace();
				for (StackTraceElement s : stack) {
					msg = msg + s.toString() + "\n\t\t";
				}
			}

			if (!errorMessageString.equals("")) {
				errorMessageString += "\n";
			}
			errorMessageString += msg;

			this.sendErrorReport(this.gnomexSupportEmail, this.fromEmailAddress);

			System.err.println(errorMessageString);

		}

		System.out.println("Exiting(2)...");
		System.exit(0);

	}

	private void initialize() throws Exception {
		PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);
		baseFlowCellDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_FLOWCELL_DIRECTORY);
		baseAnalysisDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);

		flowCellDirFlag = ph.getProperty(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG);

		gnomexSupportEmail = ph.getQualifiedProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL, serverName);
		if (gnomexSupportEmail == null) {
			gnomexSupportEmail = ph.getQualifiedProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER, serverName);
		}
		this.fromEmailAddress = ph.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);

		// Figure out how far back we should look at experiments

		if (all) {
			// Don't filter on date criteria of all experiments and analysis to be registered
		} else {
			asOfDate = GregorianCalendar.getInstance();

			// Back up today's date to correct interval
			if (daysSince != null) {
				// Argument was provided that tells use how far to look back
				asOfDate.add(Calendar.DATE, daysSince.intValue() * -1);
			} else {
				// No argument provided. Just go back a month.
				asOfDate.add(Calendar.MONTH, -1);
			}
		}
	}

	private String getCurrentDateString() {
		runDate = Calendar.getInstance();
		return new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(runDate.getTime());

	}

	private void destroyLinks() throws Exception {
		dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

		File ucscLinkDir = new File(dataTrackFileServerWebContext, Constants.URL_LINK_DIR_NAME);

		destroyFolder(ucscLinkDir);
	}

	private void registerExperimentFiles() throws Exception {
		// Hash experiment files
		StringBuffer buf = new StringBuffer();

		// Get all of the experiments created on or after the as-of-date
		buf = new StringBuffer("SELECT r ");
		buf.append(" FROM Request r");

		if (!justOne && asOfDate != null) {
			buf.append(" WHERE r.createDate >= '" + new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()) + "'");
		}

		if (justOne) {
			buf.append(" WHERE r.number = '" + requestId + "'");
		}

		System.out.println(getCurrentDateString() + ":" + buf.toString());
		List results = sess.createQuery(buf.toString()).list();
		if (results.isEmpty()) {
			System.out.println("WARNING: No experiments to process.");
			System.exit(3);
		}

		// For each experiment
		for (Iterator i = results.iterator(); i.hasNext();) {
			tx = sess.beginTransaction();

			Request request = (Request) i.next();

			this.currentEntityString = request.getNumber();

			String baseRequestNumber = Request.getBaseRequestNumber(request.getNumber());

			System.out.println("\n" + getCurrentDateString() + ":" + baseRequestNumber);

			// Get all of the files from the file system
			Map fileMap = hashFiles(sess, baseRequestNumber, request.getCreateDate(), request.getCodeRequestCategory(), request.getIdCoreFacility());
			for (Iterator i1 = fileMap.keySet().iterator(); i1.hasNext();) {
				String fileName = (String) i1.next();
				FileDescriptor fd = (FileDescriptor) fileMap.get(fileName);
				if (experimentWarnings) {
					System.out.println(getCurrentDateString() + ":" + fileName + " " + fd.getFileSizeText());
				}
			}

			// Now compare to the experiment files already registered in the db
			TreeSet newExperimentFiles = new TreeSet(new ExperimentFileComparator());
			if (request.getFiles() != null) {
				String baseExperimentDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, request.getIdCoreFacility(),
						PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
				String directoryName = baseExperimentDir + Request.getCreateYear(request.getCreateDate()) + "/"
						+ Request.getBaseRequestNumber(request.getNumber());
				directoryName.replace("\\", "/");

				for (Iterator i2 = request.getFiles().iterator(); i2.hasNext();) {
					LinkedList<String> dirs = new LinkedList<String>();
					ExperimentFile ef = (ExperimentFile) i2.next();
					FileDescriptor fd = (FileDescriptor) fileMap.get(ef.getFileName().replace("\\", "/"));

					String efFileName = ef.getFileName().substring(ef.getFileName().lastIndexOf("/") + 1);
					printDebugStatement("EXPERIMENT FILE NAME::::::::::::: " + efFileName);
					printDebugStatement("EXPERIMENT FILE SIZE::::::::::::: " + ef.getFileSize());
					dirs.add(directoryName);
					String newFilePath = null;

					// If fd is initially null then the file may have just been
					// moved so look for it in other directories
					if (fd == null) {
						printDebugStatement("name: " + efFileName + "     size: " + ef.getFileSize() + "        sizeOfLinkedList: " + dirs.size());
						newFilePath = recurseDirectoriesForFile(efFileName, ef.getFileSize(), request, dirs);
					}

					// Create a new file descriptor to reference the experiment
					// file if we found it.
					if (newFilePath != null) {
						newFilePath = newFilePath.replace("\\", "/");
						printDebugStatement("NEW FILE PATH, WE FOUND OLD FILE::::::::::::: " + newFilePath);
						fd = new FileDescriptor(request.getNumber(), newFilePath.substring(newFilePath.lastIndexOf("/")), new File(newFilePath),
								newFilePath.substring(newFilePath.indexOf(baseRequestNumber)));
					}

					// If we don't find the file on the file system, delete it from the db
					if (fd == null && newFilePath == null) {
						if (experimentWarnings) {
							System.out.println(getCurrentDateString() + ":" + "WARNING - experiment file " + ef.getFileName() + " not found for "
									+ ef.getRequest().getNumber());
						}
						printDebugStatement("ABOUT TO REMOVE EF FROM REQUEST FILE SET!!");
						i2.remove();// Remove the experiment file so we can
									// delete the hibernate object

						printDebugStatement("SUCCESSFUL REMOVAL FROM REQUEST FILE SET");
						deleteExpFileAndNotify(sess, ef);
					} else {
						// Mark that the file system file has been found
						fd.isFound(true);
						printDebugStatement("FD IS FOUND = TRUE!!!!    " + fd.isFound() + fd.getFileName());
						Boolean changed = false;

						// If the file size is different on the file system, update the ExperimentFile
						if (ef.getFileSize() == null || !ef.getFileSize().equals(BigDecimal.valueOf(fd.getFileSize()))) {
							ef.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
							changed = true;
						}
						if (ef.getCreateDate() == null) {
							ef.setCreateDate(getEffectiveRequestFileCreateDate(fd, request.getCreateDate()));
							changed = true;
						}
						if (newFilePath != null) {
							fileMap.remove(ef.getFileName());
							fileMap.put(newFilePath.substring(newFilePath.indexOf(baseRequestNumber)), fd);
							ef.setFileName(newFilePath.substring(newFilePath.indexOf(baseRequestNumber)));
							changed = true;
						}
						if (changed) {
							sess.saveOrUpdate(ef);
						}
						newExperimentFiles.add(ef);
					}
				}
			}

			// Now add ExperimentFiles to the db for any files on the file system not found in the db.
			boolean updateModifyDate = true;
			for (Iterator i3 = fileMap.keySet().iterator(); i3.hasNext();) {
				String fileName = (String) i3.next();
				printDebugStatement("LINE 354 FILENAME::::::::: " + fileName);
				FileDescriptor fd = (FileDescriptor) fileMap.get(fileName);
				printDebugStatement(fd.getFileName() + "      " + fd.isFound());
				if (!fd.isFound()) {
					ExperimentFile ef = new ExperimentFile();
					ef.setIdRequest(request.getIdRequest());
					ef.setFileName(fileName.replace("\\", "/"));
					ef.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
					ef.setCreateDate(getEffectiveRequestFileCreateDate(fd, request.getCreateDate()));
					sess.save(ef);
					newExperimentFiles.add(ef);
					if (updateModifyDate == true) {
						request.setLastModifyDate(new java.sql.Date(System.currentTimeMillis()));
						// sess.save(request); // ***************************************************
						updateModifyDate = false;
					}
				}
			}

			request.getFiles().clear();

			if (debug) {
				for (Iterator debug = newExperimentFiles.iterator(); debug.hasNext();) {
					System.out.println(debug.next());
				}
			}

			if (newExperimentFiles != null && newExperimentFiles.size() > 0) {
				request.getFiles().addAll(newExperimentFiles);
			}

			sess.saveOrUpdate(request);
			sess.flush();
			tx.commit();

			sess.clear();
			this.currentEntityString = "";
		}
	}

	private java.sql.Date getEffectiveRequestFileCreateDate(FileDescriptor fd, java.util.Date requestCreateDate) {

		java.sql.Date createDate = new java.sql.Date(runDate.getTime().getTime());

		return createDate;
	}

	private void registerAnalysisFiles() throws Exception {

		// Get all of the analysis files created on or after the as-of-date
		StringBuffer buf = new StringBuffer("SELECT a ");
		buf.append(" FROM Analysis a");
		if (!justOne && asOfDate != null) {
			buf.append(" WHERE a.createDate >= '" + new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()) + "'");
		}

		if (justOne) {
			buf.append(" WHERE a.number = '" + analysisId + "'");
		}
		List results = sess.createQuery(buf.toString()).list();
		if (results.isEmpty()) {
			System.out.println("WARNING: No analyses to process.");
			System.exit(3);
		}

		// For each analysis
		for (Iterator i = results.iterator(); i.hasNext();) {
			tx = sess.beginTransaction();
			Analysis analysis = (Analysis) i.next();
			this.currentEntityString = analysis.getNumber();

			System.out.println("\n" + getCurrentDateString() + ":" + analysis.getNumber());

			// Get all of the files from the file system
			Map fileMap = hashFiles(analysis);
			for (Iterator i1 = fileMap.keySet().iterator(); i1.hasNext();) {
				String fileName = (String) i1.next();
				FileDescriptor fd = (FileDescriptor) fileMap.get(fileName);
				if (analysisWarnings) {
					System.out.println(getCurrentDateString() + ":" + fileName + " " + fd.getFileSizeText());
				}
			}

			// Now compare to the analysis files already registered in the db
			TreeSet newAnalysisFiles = new TreeSet(new AnalysisFileComparator());
			for (Iterator i2 = analysis.getFiles().iterator(); i2.hasNext();) {

				AnalysisFile af = (AnalysisFile) i2.next();
				String directoryName = analysis.getNumber() + "/";
				if (af.getQualifiedFilePath() != null && !af.getQualifiedFilePath().trim().equals("")) {
					directoryName += af.getQualifiedFilePath() + "/";
				}
				String qualifiedFileName = directoryName + af.getFileName();
				FileDescriptor fd = (FileDescriptor) fileMap.get(qualifiedFileName);

				// If we don't find the file on the file system, delete it from the db.
				if (fd == null) {

					if (analysisWarnings) {
						System.out.println("\n" + getCurrentDateString() + ":" + "WARNING - analysis file " + qualifiedFileName + " not found for "
								+ af.getAnalysis().getNumber());
					}

					// DataTrack and DataTrackFile query
					StringBuffer queryBuf = new StringBuffer();
					queryBuf.append("SELECT dtf, dt FROM DataTrack dt ");
					queryBuf.append("JOIN dt.dataTrackFiles dtf ");
					queryBuf.append("WHERE dtf.idAnalysisFile = ");
					queryBuf.append(af.getIdAnalysisFile());

					// Run the Query
					List dataTrackFiles = sess.createQuery(queryBuf.toString()).list();

					// Data tracks were found, so delete them and warn the owners
					if (dataTrackFiles.size() > 0) {

						// Delete the DataTrackFiles and DataTracks
						for (Iterator i4 = dataTrackFiles.iterator(); i4.hasNext();) {
							Object[] row = (Object[]) i4.next();
							DataTrackFile dtf = (DataTrackFile) row[0];
							DataTrack dt = (DataTrack) row[1];

							// if we aren't sending email, don't do the work
							if (sendMail || testingSendMail) {
								// Get an email address for the file
								String emailAddress = "";
								if (dt.getAppUser() != null && dt.getAppUser().getEmail() != null) {
									emailAddress = dt.getAppUser().getEmail();
								}
								if (emailAddress == null || emailAddress.equals("")) {
									if (dt.getLab() != null && dt.getLab().getContactEmail() != null) {
										emailAddress = dt.getLab().getContactEmail();
									}
								}
								if (emailAddress == null || emailAddress.equals("")) {
									emailAddress = this.gnomexSupportEmail;
								}

								hashForNotification(emailAddress, af, dt);
							}

							sess.delete(dtf);
							sess.flush();
						}
					}

					// if we aren't sending email, don't do the work
					if (sendMail || testingSendMail) {
						// If the analysis has any comments, warn the app user that it was deleted.
						if (af.getComments() != null && !af.getComments().trim().equals("")) {
							// Get an email address for the file
							String emailAddress = "";
							if (af.getAnalysis().getAppUser() != null && af.getAnalysis().getAppUser().getEmail() != null) {
								emailAddress = af.getAnalysis().getAppUser().getEmail();
							}
							if (emailAddress == null || emailAddress.equals("")) {
								if (af.getAnalysis().getLab() != null && af.getAnalysis().getLab().getContactEmail() != null) {
									emailAddress = af.getAnalysis().getLab().getContactEmail();
								}
							}
							if (emailAddress == null || emailAddress.equals("")) {
								emailAddress = this.gnomexSupportEmail;
							}

							hashForNotification(emailAddress, af, null);

						}
					}

					i2.remove();
					sess.delete(af);

				} else {
					// Mark that the file system file has been found
					fd.isFound(true);
					newAnalysisFiles.add(af);

					// If the file size is different on the file system, update the AnalysisFile
					if (af.getFileSize() == null || !af.getFileSize().equals(BigDecimal.valueOf(fd.getFileSize()))) {
						af.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
					}

					// If create date not set, then set it
					if (af.getCreateDate() == null) {
						af.setCreateDate(this.getEffectiveAnalysisFileCreateDate(fd, analysis.getCreateDate()));
					}
				}

			}

			// Now add AnalysisFiles to the db for any files on the file system not found in the db.
			for (Iterator i3 = fileMap.keySet().iterator(); i3.hasNext();) {
				String fileName = (String) i3.next();
				FileDescriptor fd = (FileDescriptor) fileMap.get(fileName);
				if (!fd.isFound()) {
					AnalysisFile af = new AnalysisFile();
					af.setIdAnalysis(analysis.getIdAnalysis());
					af.setFileName(fd.getDisplayName());
					af.setQualifiedFilePath(fd.getQualifiedFilePath());
					af.setBaseFilePath(fd.getBaseFilePath());
					af.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
					af.setBaseFilePath(baseAnalysisDir + analysis.getCreateYear() + File.separatorChar + analysis.getNumber());
					af.setCreateDate(this.getEffectiveAnalysisFileCreateDate(fd, analysis.getCreateDate()));
					newAnalysisFiles.add(af);
				}
			}

			if (newAnalysisFiles == null || newAnalysisFiles.isEmpty()) {
				analysis.getFiles().clear();
			} else {
				analysis.getFiles().clear();
				analysis.getFiles().addAll(newAnalysisFiles);

			}

			sess.flush();
			tx.commit();

			sess.clear();
			this.currentEntityString = "";
		}

		try {
			sendNotifyEmails(this.gnomexSupportEmail);
		} catch (Exception e) {

			// Notify software people that the emails didn't go through?
			String msg = "Unable to send warning email notifying user that analysis files have been deleted:  " + e.toString() + "\n\t";

			StackTraceElement[] stack = e.getStackTrace();
			for (StackTraceElement s : stack) {
				msg = msg + s.toString() + "\n\t\t";
			}
			;

			if (!errorMessageString.equals("")) {
				errorMessageString += "\n";
			}
			errorMessageString += msg;

			this.sendErrorReport(this.gnomexSupportEmail, this.fromEmailAddress);

			System.err.println(errorMessageString);
		}
	}

	private java.sql.Date getEffectiveAnalysisFileCreateDate(FileDescriptor fd, java.util.Date analysisCreateDate) {
		java.sql.Date createDate = new java.sql.Date(runDate.getTime().getTime());

		return createDate;
	}

	private void hashForNotification(String emailAddress, AnalysisFile af, DataTrack dt) {
		Map<String, String> analysisNumbers = emailAnalysisMap.get(emailAddress);
		if (analysisNumbers == null) {
			analysisNumbers = new TreeMap<String, String>();
			emailAnalysisMap.put(emailAddress, analysisNumbers);
		}
		analysisNumbers.put(af.getAnalysis().getNumber(), null);
		emailAnalysisMap.put(emailAddress, analysisNumbers);

		List fileInfos = analysisFileMap.get(af.getAnalysis().getNumber());
		if (fileInfos == null) {
			fileInfos = new ArrayList<AnalysisFileInfo>();
			analysisFileMap.put(af.getAnalysis().getNumber(), fileInfos);
		}
		fileInfos.add(new AnalysisFileInfo(af.getAnalysis().getNumber(), af.getQualifiedFileName() + "/" + af.getFileName(), dt, af.getComments()));
	}

	private void sendNotifyEmails(String fromAddress) throws NamingException, MessagingException, IOException {
		if (this.emailAnalysisMap.size() == 0) {
			return;
		}

		for (Iterator i = this.emailAnalysisMap.keySet().iterator(); i.hasNext();) {
			StringBuffer body = new StringBuffer();
			body.append("The following analysis files no longer exist on the file system and have been removed from the database.  Please remove the associated data tracks.");
			String emailAddress = (String) i.next();
			Map<String, String> analysisNumbers = emailAnalysisMap.get(emailAddress);
			for (String analysisNumber : analysisNumbers.keySet()) {
				body.append("\n\nAnalysis " + analysisNumber);
				List<AnalysisFileInfo> fileInfos = this.analysisFileMap.get(analysisNumber);
				for (AnalysisFileInfo info : fileInfos) {
					body.append("\n\t" + info.analysisFileName + "\t" + info.comment + "\t" + info.dataTrackNumber);
				}
			}

			if (sendMail) {
				MailUtilHelper helper = new MailUtilHelper(mailProps, emailAddress, fromAddress, null, fromAddress,
						"GNomEx Analysis file(s) missing from file system", body.toString(), null, false, DictionaryHelper.getInstance(sess), serverName);
				MailUtil.validateAndSendEmail(helper);

			}
		}
	}

	private void sendErrorReport(String softwareTestEmail, String fromAddress) {
		try {
			java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
			if (debug) {
				printDebugStatement(localMachine.getHostName());
			}
			if (sendMail) {
				MailUtilHelper helper = new MailUtilHelper(mailProps, softwareTestEmail, null, null, fromAddress, "Register Files Error [Server: "
						+ localMachine.getHostName() + "]", errorMessageString, null, false, DictionaryHelper.getInstance(sess), serverName);
				MailUtil.validateAndSendEmail(helper);

			}
		} catch (Exception e) {
			System.err.println("Register files unable to send error report.   " + e.toString());
		}
	}

	private Map hashFiles(Session sess, String requestNumber, java.util.Date createDate, String codeRequestCategory, Integer idCoreFacility) throws Exception {
		HashMap fileMap = new HashMap(5000);
		String baseRequestNumber = Request.getBaseRequestNumber(requestNumber);

		String baseExperimentDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, idCoreFacility,
				PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
		String directoryName = baseExperimentDir + Request.getCreateYear(createDate) + "/" + baseRequestNumber;
		directoryName.replace("\\", "/");

		String[] fileList = new File(directoryName).list();
		// Get all files directly underneath the experiment number directory
		if (fileList != null) {
			for (int x = 0; x < fileList.length; x++) {
				String fileName = directoryName + "/" + fileList[x];
				File f1 = new File(fileName);
				if (f1.isFile()) { // && !Util.isSymlink(f1)) {
					FileDescriptor fd = new FileDescriptor(requestNumber, fileList[x], f1, baseRequestNumber + "/" + fileList[x]);
					fileMap.put(fd.getZipEntryName().replace("\\", "/"), fd);
				}

			}
		}

		// Get all of the folders in the experiment directory
		Set folders = GetRequestDownloadList.getRequestDownloadFolders(baseExperimentDir, baseRequestNumber, Request.getCreateYear(createDate),
				codeRequestCategory);
		for (Iterator i1 = folders.iterator(); i1.hasNext();) {
			String folderName = (String) i1.next();

			// For each folder under experiment directory, get the files (recursive)
			Map requestMap = new TreeMap();
			Map directoryMap = new TreeMap();
			List requestNumbers = new ArrayList<String>();
			UploadDownloadHelper.getFileNamesToDownload(sess, serverName, null, Request.getKey(requestNumber, createDate, folderName, idCoreFacility),
					requestNumbers, requestMap, directoryMap, flowCellDirFlag);
			List directoryKeys = (List) requestMap.get(baseRequestNumber);
			if (directoryKeys != null) {
				for (Iterator i2 = directoryKeys.iterator(); i2.hasNext();) {
					String directoryKey = (String) i2.next();

					List theFiles = (List) directoryMap.get(directoryKey);

					// Hash all of the files for this experiment
					for (Iterator i3 = theFiles.iterator(); i3.hasNext();) {
						FileDescriptor fd = (FileDescriptor) i3.next();
						recurseHashFiles(fd, fileMap);
					}
				}
			}
		}
		return fileMap;
	}

	public static void recurseHashFiles(FileDescriptor fd, Map fileMap) throws XMLReflectException {
		if (new File(fd.getFileName()).isDirectory()) {
			for (Iterator i = fd.getChildren().iterator(); i.hasNext();) {
				FileDescriptor childFd = (FileDescriptor) i.next();
				recurseHashFiles(childFd, fileMap);
			}

		} else {
			fileMap.put(fd.getZipEntryName().replaceAll("\\\\", "/"), fd);
		}

	}

	private HashMap hashFiles(Analysis analysis) throws Exception {
		HashMap fileMap = new HashMap(5000);

		Map analysisMap = new TreeMap();
		Map directoryMap = new TreeMap();
		List analysisNumbers = new ArrayList<String>();
		GetExpandedAnalysisFileList.getFileNamesToDownload(baseAnalysisDir, analysis.getKey(), analysisNumbers, analysisMap, directoryMap, false);

		for (Iterator i = analysisNumbers.iterator(); i.hasNext();) {
			String analysisNumber = (String) i.next();
			List directoryKeys = (List) analysisMap.get(analysisNumber);

			// For each directory of analysis
			boolean firstDirForAnalysis = true;
			int unregisteredFileCount = 0;
			for (Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {

				String directoryKey = (String) i1.next();
				List theFiles = (List) directoryMap.get(directoryKey);

				// Hash all of the files for this experiment
				for (Iterator i3 = theFiles.iterator(); i3.hasNext();) {
					FileDescriptor fd = (FileDescriptor) i3.next();
					recurseHashFiles(fd, fileMap);
				}
			}
		}
		return fileMap;
	}

	private String recurseDirectoriesForFile(String fileToLookFor, BigDecimal sizeOfFile, Request r, LinkedList<String> dirs) {
		if (dirs.isEmpty()) {
			return null;
		}
		String dir = dirs.removeFirst();
		printDebugStatement("DIRECTORY WE ARE CURRENTLY LOOKING THROUGH:   " + dir);
		File f = new File(dir);

		if (f.exists()) {
			String[] contents = f.list();
			for (int i = 0; i < contents.length; i++) {
				File f1 = new File(dir + "/" + contents[i]);
				if (f1.isFile() && f1.getName().equals(fileToLookFor) && (BigDecimal.valueOf(f1.length()).equals(sizeOfFile))) {
					// we found the file so return its new path
					printDebugStatement("WE FOUND THE FILE::::::::: " + f1.getAbsolutePath());
					return f1.getAbsolutePath();
				} else if (f1.isDirectory()) {
					dirs.add(f1.getAbsolutePath());
				}
			}
		}

		return recurseDirectoriesForFile(fileToLookFor, sizeOfFile, r, dirs);

	}

	private void deleteExpFileAndNotify(Session sess, ExperimentFile ef) {
		List sampleExperimentFiles = sess.createQuery(
				"Select sef from SampleExperimentFile sef where idExpFileRead1 = " + ef.getIdExperimentFile() + " OR idExpFileRead2 = "
						+ ef.getIdExperimentFile()).list();
		ArrayList<Integer> listOfSampleIds = new ArrayList<Integer>();
		String listOfSampleNames = "";
		String listOfSampleIdsString = "";
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

		String subject = "Experiment File " + ef.getFileName() + " has been deleted";

		printDebugStatement("SIZE OF SAMPLE_EXPERIMENT_FILES_LIST::::::::: " + sampleExperimentFiles.size());

		for (Iterator i = sampleExperimentFiles.iterator(); i.hasNext();) {
			SampleExperimentFile sef = (SampleExperimentFile) i.next();
			if (sef.getIdExpFileRead1() != null && ef.getIdExperimentFile().equals(sef.getIdExpFileRead1())) {
				sef.setIdExpFileRead1(null);
			} else if (sef.getIdExpFileRead2() != null && ef.getIdExperimentFile().equals(sef.getIdExpFileRead2())) {
				sef.setIdExpFileRead2(null);
			}

			if (sef.getIdExpFileRead1() == null && sef.getIdExpFileRead2() == null) {
				listOfSampleIds.add(sef.getIdSample());
				sess.delete(sef);
				printDebugStatement("Deleted SEF::::: " + sef.getIdSample());
			}
		}

		if (listOfSampleIds.size() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("Select s.name from Sample s where idSample = ");
			for (Iterator j = listOfSampleIds.iterator(); j.hasNext();) {
				Integer i = (Integer) j.next();
				listOfSampleIdsString += i.toString();
				buf.append(i.toString());
				if (j.hasNext()) {
					buf.append(" OR idSample = ");
					listOfSampleIdsString += ", ";
				}
			}

			printDebugStatement(buf.toString());
			List sampleNames = sess.createQuery(buf.toString()).list();
			for (Iterator k = sampleNames.iterator(); k.hasNext();) {
				String sampleName = (String) k.next();
				printDebugStatement(sampleName);
				listOfSampleNames += sampleName;
				if (k.hasNext()) {
					listOfSampleNames += " ,";
				}
			}
		}

		sess.delete(ef);
		sess.flush();

		printDebugStatement("SAMPLE NAMES: " + listOfSampleNames);
		if (!listOfSampleNames.equals("") && sendMail) {
			String toAddress = pdh.getProperty(PropertyDictionary.CONTACT_EMAIL_MANAGE_SAMPLE_FILE_LINK);
			if (!MailUtil.isValidEmail(toAddress)) {
				System.err.println("Unable to notify of unlinking of sample experiment files due to invalid email address:  " + toAddress);
				return;
			}
			String fromAddress = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
			String body = "The following Sample Names were unlinked: " + listOfSampleNames + "\n\n";
			body += "(Corresponding Sample Id's: " + listOfSampleIdsString + ")\n\n";
			body += "Please review your sample linkage.";

			printDebugStatement("TO ADDRESS: " + toAddress + "       FROM ADDRESS: " + fromAddress);
			try {
				MailUtilHelper helper = new MailUtilHelper(mailProps, toAddress, null, null, fromAddress, subject, body, null, false,
						DictionaryHelper.getInstance(sess), serverName);
				MailUtil.validateAndSendEmail(helper);

			} catch (Exception e) {
				System.err.println("WARNING: Unable to send email notifying of deletion of Sample Experiment Files. Trying to send to: " + toAddress
						+ e.toString());
			}
		}
	}

	private void printDebugStatement(String message) {
		if (debug) {
			System.out.println(message);
		}
	}

	private static Date getWakeupTime() {
		Calendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DATE, fONE_DAY);
		Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE), wakeupHour,
				fZERO_MINUTES);
		return result.getTime();
	}

	private void connect() throws Exception {
		sess = dataSource.connect();
		if (sess == null) {
			System.out.println("[RegisterFiles] ERROR: Unable to acquire session. Exiting...");
			System.exit(1);
		}
	}

	private void disconnect() throws Exception {
		if (sess == null) {
			return;
		}

		sess.close();
	}

	// Bypassed dtd validation when reading data sources.
	public class DummyEntityRes implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			return new InputSource(new StringReader(" "));
		}

	}

	public static class ExperimentFileComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			ExperimentFile ef1 = (ExperimentFile) o1;
			ExperimentFile ef2 = (ExperimentFile) o2;

			if (ef1.getIdExperimentFile() == null || ef2.getIdExperimentFile() == null) {
				return ef1.getFileName().compareTo(ef2.getFileName());
			} else {
				return ef1.getIdExperimentFile().compareTo(ef2.getIdExperimentFile());
			}
		}
	}

	public static class AnalysisFileComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			AnalysisFile ef1 = (AnalysisFile) o1;
			AnalysisFile ef2 = (AnalysisFile) o2;

			if (ef1.getIdAnalysisFile() == null || ef2.getIdAnalysisFile() == null) {
				return ef1.getQualifiedFileName().compareTo(ef2.getQualifiedFileName());
			} else {
				return ef1.getIdAnalysisFile().compareTo(ef2.getIdAnalysisFile());
			}
		}
	}

	public static class AnalysisFileInfo {
		public String analysisNumber;
		public String analysisFileName;
		public String dataTrackNumber;
		public String comment;

		public AnalysisFileInfo(String analysisNumber, String analysisFileName, DataTrack dataTrack, String comment) {
			super();
			this.analysisNumber = analysisNumber;
			this.analysisFileName = analysisFileName;
			this.dataTrackNumber = dataTrack != null ? dataTrack.getNumber() : "";
			this.comment = comment;
		}

	}

	private static void destroyFolder(File igvLinkDir) throws Exception {
		File[] directoryList = igvLinkDir.listFiles();

		for (File directory : directoryList) {
			delete(directory);
		}
	}

	private static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

}
