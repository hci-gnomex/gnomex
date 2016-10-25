package hci.gnomex.controller;

import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadPurchaseOrder extends HttpServlet {
private Integer idBillingAccount;
private String fileName;
private File file;
private String directoryName;

private static final int ERROR_MISSING_TEMP_DIRECTORY_PROPERTY = 900;
private static final int ERROR_INVALID_TEMP_DIRECTORY = 901;
private static Logger LOG = Logger.getLogger(UploadPurchaseOrder.class);

protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

}

protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	try {
		Session sess = HibernateSession.currentSession(req.getUserPrincipal().getName());

		res.setContentType("text/html");
		MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
		FileInputStream fileStream;
		byte[] blob = new byte[1024];
		Part part;
		String fileType = null;

		directoryName = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(
				PropertyDictionary.TEMP_DIRECTORY, req.getServerName());
		if (directoryName == null || directoryName.equals("")) {
			res.setStatus(this.ERROR_MISSING_TEMP_DIRECTORY_PROPERTY);
			throw new ServletException(
					"Unable to upload sample sheet. Missing GNomEx property for temp_directory.  Please add using 'Manage Dictionaries'.");
		}
		if (!directoryName.endsWith("/") && !directoryName.endsWith("\\")) {
			directoryName += File.separator;
		}

		File dir = new File(directoryName);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
				throw new ServletException("Unable to upload sample sheet.  Cannot create temp directory "
						+ directoryName);
			}
		}
		if (!dir.canRead()) {
			res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
			throw new ServletException("Unable to upload sample sheet.  Cannot read temp directory " + directoryName);
		}
		if (!dir.canWrite()) {
			res.setStatus(this.ERROR_INVALID_TEMP_DIRECTORY);
			throw new ServletException("Unable to upload sample sheet.  Cannot write to temp directory "
					+ directoryName);
		}

		while ((part = mp.readNextPart()) != null) {
			String name = part.getName();
			if (part.isParam()) {
				// it's a parameter part
				ParamPart paramPart = (ParamPart) part;
				String value = paramPart.getStringValue();
				if (name.equals("idBillingAccount")) {
					idBillingAccount = new Integer((String) value);
				}
			} else if (part.isFile()) {
				FilePart filePart = (FilePart) part;
				fileName = filePart.getFileName();
				fileType = fileName.substring(fileName.indexOf("."));

				if (fileName != null) {
					file = new File(directoryName + fileName);
					filePart.writeTo(file);
					fileStream = new FileInputStream(file);
					blob = new byte[(int) file.length()];
					fileStream.read(blob);
					fileStream.close();
				}
			}
		}
		BillingAccount ba = (BillingAccount) sess.load(BillingAccount.class, idBillingAccount);
		ba.setPurchaseOrderForm(blob);
		ba.setOrderFormFileType(fileType.toLowerCase());
		ba.setOrderFormFileSize(new Long(file.length()));
		sess.update(ba);
		sess.flush();
		// Delete the file now that we are finished
		file.delete();

	} catch (Exception e) {
		HibernateSession.rollback();
		LOG.error("Unexpected error in UploadPurchaseOrder", e);
		throw new ServletException("Unable to upload purchase order file.  Please contact gnomex support.");
	} finally {
		try {
			HibernateSession.closeSession();
		} catch (Exception e) {
			LOG.error("Unable to close hibernate session in UploadPurcahseOrder", e);
		}
	}
}

}
