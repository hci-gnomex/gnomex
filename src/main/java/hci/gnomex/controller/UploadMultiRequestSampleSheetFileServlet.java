package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MultiRequestSampleSheetFileParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadMultiRequestSampleSheetFileServlet extends HttpServlet {

/**
   * 
   */
private static final long serialVersionUID = 1L;

// the static field for logging in Log4J
private static Logger LOG = Logger.getLogger(UploadMultiRequestSampleSheetFileServlet.class);


private static final int ERROR_MISSING_TEMP_DIRECTORY_PROPERTY = 900;
private static final int ERROR_INVALID_TEMP_DIRECTORY = 901;
private static final int ERROR_SECURITY_EXCEPTION = 902;
private static final int ERROR_UPLOAD_MISC = 903;

protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
}

/*
 * SPECIAL NOTE - This servlet must be run on non-secure socket layer (http) in order to keep track of previously created session. (see note below concerning
 * flex upload bug on Safari and FireFox). Otherwise, session is not maintained. Although the code tries to work around this problem by creating a new security
 * advisor if one is not found, the Safari browser cannot handle authenicating the user (this second time). So for now, this servlet must be run non-secure.
 */
protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	String directoryName = "";
	String fileName = null;

	try {
		Session sess = HibernateSession.currentReadOnlySession((req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest"));

		// Get the dictionary helper
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);

		// Get security advisor
		SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(
				SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
		if (secAdvisor == null) {
			System.out
					.println("UploadSampleSheetFileServlet:  Warning - unable to find existing session. Creating security advisor.");
			secAdvisor = SecurityAdvisor.create(sess, (req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest"));
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
			System.out
					.println("UploadMultiRequestSampleSheetFileServlet: Error - Unable to find or create security advisor.");
			res.setStatus(ERROR_SECURITY_EXCEPTION);
			LOG.error("UploadMultiRequestSampleSheetFileServlet: Unable to upload sample sheet file.  Servlet unable to obtain security information. Please contact GNomEx support.");
			throw new ServletException(
					"Unable to upload sample sheet file.  Servlet unable to obtain security information. Please contact GNomEx support.");
		}

		// Only admins can import multiple requests
		if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
			LOG.error("UploadMultiRequestSampleSheetFileServlet: Only admins can import multi-request spread sheets.");
			throw new ServletException("Only admins can import multi-request spread sheets");
		}

		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control", "max-age=0, must-revalidate");

		MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
		Part part;

		directoryName = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(
				PropertyDictionary.TEMP_DIRECTORY, req.getServerName());
		if (directoryName == null || directoryName.equals("")) {
			res.setStatus(this.ERROR_MISSING_TEMP_DIRECTORY_PROPERTY);
			LOG.error("UploadMultiRequestSampleSheetFileServlet: Unable to upload sample sheet. Missing GNomEx property for temp_directory.  Please add using 'Manage Dictionaries'.");
			throw new ServletException(
					"Unable to upload sample sheet. Missing GNomEx property for temp_directory.  Please add using 'Manage Dictionaries'.");
		}
		if (!directoryName.endsWith(Constants.FILE_SEPARATOR) && !directoryName.endsWith("\\")) {
			directoryName += Constants.FILE_SEPARATOR;
		}

		File dir = new File(directoryName);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
				LOG.error("UploadMultiRequestSampleSheetFileServlet:Unable to upload sample sheet.  Cannot create temp directory "
						+ directoryName);
				throw new ServletException("Unable to upload sample sheet.  Cannot create temp directory "
						+ directoryName);
			}
		}
		if (!dir.canRead()) {
			res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
			LOG.error("UploadMultiRequestSampleSheetFileServlet:Unable to upload sample sheet.  Cannot read temp directory "
					+ directoryName);
			throw new ServletException("Unable to upload sample sheet.  Cannot read temp directory " + directoryName);
		}
		if (!dir.canWrite()) {
			res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
			LOG.error("UploadMultiRequestSampleSheetFileServlet:Unable to upload sample sheet.  Cannot write to temp directory "
					+ directoryName);
			throw new ServletException("Unable to upload sample sheet.  Cannot write to temp directory "
					+ directoryName);
		}

		boolean fileWasWritten = false;
		boolean hasColumnNames = false;

		while ((part = mp.readNextPart()) != null) {
			String name = part.getName();
			if (part.isParam()) {
				// it's a parameter part
				ParamPart paramPart = (ParamPart) part;
				String value = paramPart.getStringValue();
				if (name.equals("hasColumnNames")) {
					String hasColumnNamesValue = (String) value;
					if (hasColumnNamesValue != null && hasColumnNamesValue.compareTo("1") == 0) {
						hasColumnNames = true;
					}

				}
			}
			if (part.isFile()) {
				// it's a file part
				FilePart filePart = (FilePart) part;
				fileName = filePart.getFileName();
				if (fileName != null) {
					// the part actually contained a file
					filePart.writeTo(new File(directoryName));
					fileWasWritten = true;
				} else {
				}
				out.flush();
			}
		}

		Document doc = new Document(new Element("FileNotWritten"));
		if (fileWasWritten) {
			MultiRequestSampleSheetFileParser parser = new MultiRequestSampleSheetFileParser(directoryName + fileName,
					secAdvisor);
			parser.parse(sess);
			doc = parser.toXMLDocument();
		}

		PrintWriter responseOut = res.getWriter();
		res.setHeader("Cache-Control", "cache, must-revalidate, proxy-revalidate, s-maxage=0, max-age=0");
		res.setHeader("Pragma", "public");
		res.setDateHeader("Expires", 0);
		res.setContentType("application/xml");

		XMLOutputter xmlOut = new XMLOutputter();
		responseOut.println(xmlOut.outputString(doc));

	} catch (ServletException e) {
		unexpectedError(e, res);
		LOG.error("Error in UploadMultiRequestSampleSheetFileServlet: ", e);
	} catch (org.jdom.IllegalDataException e) {
		unexpectedError(e, res);
	} catch (Exception e) {
		res.setStatus(ERROR_UPLOAD_MISC);
		unexpectedError(e, res);
		LOG.error("Error in UploadMultiRequestSampleSheetFileServlet: ", e);
		throw new ServletException("Unable to upload file " + fileName + " due to a server error.\n\n" + e.toString()
				+ "\n\nPlease contact GNomEx support.");
	} finally {
		try {
			HibernateSession.closeSession();
		} catch (Exception e1) {
			LOG.error("UploadSampleSheetFileServlet warning - cannot close hibernate session", e1);
		}

		// Delete the file when finished
		File f = new File(directoryName + fileName);
		f.delete();
	}
}

private void unexpectedError(Exception e, HttpServletResponse res) {
	try {
		LOG.error("Error in UploadMultiRequestSampleSheetFileServlet: ", e);
		PrintWriter responseOut = res.getWriter();
		res.setHeader("Cache-Control", "cache, must-revalidate, proxy-revalidate, s-maxage=0, max-age=0");
		res.setHeader("Pragma", "public");
		res.setDateHeader("Expires", 0);
		res.setContentType("application/xml");
		responseOut.println("<ERROR message=\"Illegal data\"/>");
	} catch (IOException ioe) {
		LOG.error("UploadMultiRequestSampleSheetParser unable to build response:", ioe);
	}
}
}
