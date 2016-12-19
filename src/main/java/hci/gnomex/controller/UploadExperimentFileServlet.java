package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.GnomexFile;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hci.hibernate5utils.HibernateDetailObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadExperimentFileServlet extends UploadFileServletBase {

private static final Logger LOG = Logger.getLogger(UploadExperimentFileServlet.class);

@Override
protected void setParentObjectName(UploadFileServletData data) {
	data.parentObjectName = "experiment";
}

@Override
protected void setIdFieldName(UploadFileServletData data) {
	data.idFieldName = "idRequest";
}

@Override
protected void setNumberFieldName(UploadFileServletData data) {
	data.numberFieldName = "requestNumber";
}

@Override
protected void handleIdParameter(UploadFileServletData data, String value) {
	data.parentObject = data.sess.get(Request.class, new Integer(value));
}

@Override
protected void handleNumberParameter(UploadFileServletData data, String value) {
	data.generateOutput = true;
	List objects = data.sess.createQuery("SELECT r from Request r WHERE r.number = '" + value + "'").list();
	if (objects.size() == 1) {
		data.parentObject = (HibernateDetailObject)objects.get(0);
	} else {
		data.parentObject = null;
	}
}

@Override
protected void setBaseDirectory(UploadFileServletData data) {
	Request request = (Request) data.parentObject;
	String baseDir = PropertyDictionaryHelper.getInstance(data.sess).getDirectory(data.req.getServerName(), request.getIdCoreFacility(),
			PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
	String createYear = new SimpleDateFormat("yyyy").format(request.getCreateDate());
	data.baseDirectory = FileStringUtil.appendDirectory(baseDir, createYear);
}

@Override
protected void setUploadDirectory(UploadFileServletData data) {
	Request request = (Request) data.parentObject;
	data.uploadDirectory = data.baseDirectory;
	data.uploadDirectory = FileStringUtil.appendDirectory(data.uploadDirectory, Request.getBaseRequestNumber(request.getNumber()));
	data.uploadDirectory = FileStringUtil.appendDirectory(data.uploadDirectory, Constants.UPLOAD_STAGING_DIR);
}

@Override
protected Set<GnomexFile> getExistingFiles(UploadFileServletData data) {
	// we do not update existing ExperimentFile objects, so we don't need to load them
	return null;
}

@Override
protected void updateTransferLogFromParentObject(UploadFileServletData data, TransferLog transferLog) {
	Request request = (Request) data.parentObject;
	transferLog.setIdRequest(request.getIdRequest());
	transferLog.setIdLab(request.getIdLab());
	transferLog.setFileName(FileStringUtil.appendFile(request.getNumber(), data.fileName));
}

@Override
protected void updateExistingFile(UploadFileServletData data, GnomexFile existingFile) {
	// do not update ExperimentFile object
}

@Override
protected void createNewFile(UploadFileServletData data) {
	// do not create new ExperimentFile object
}

@Override
protected void updateParentObject(UploadFileServletData data) {
	// If we have uploaded file(s) then update the last modify date on the request but don't increment
	// the revision number. This is used for Project/Experiment reporting purposes
	Request request = (Request) data.parentObject;
	request.setLastModifyDate(new java.sql.Date(System.currentTimeMillis()));
	data.sess.save(request);
}

}
