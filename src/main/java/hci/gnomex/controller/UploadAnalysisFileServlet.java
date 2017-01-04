package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
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

public class UploadAnalysisFileServlet extends UploadFileServletBase {

private static final Logger LOG = Logger.getLogger(UploadAnalysisFileServlet.class);

@Override
protected void setParentObjectName(UploadFileServletData data) {
	data.parentObjectName = "analysis";
}

@Override
protected void setIdFieldName(UploadFileServletData data) {
	data.idFieldName = "idAnalysis";
}

@Override
protected void setNumberFieldName(UploadFileServletData data) {
		data.numberFieldName = "analysisNumber";
	}

@Override
protected void handleIdParameter(UploadFileServletData data, String value) {
	data.parentObject = data.sess.get(Analysis.class, new Integer(value));
}

@Override
protected void handleNumberParameter(UploadFileServletData data, String value) {
	data.generateOutput = true;
	List objects = data.sess.createQuery("SELECT a from Analysis a WHERE a.number = '" + value + "'").list();
	if (objects.size() == 1) {
		data.parentObject = (HibernateDetailObject)objects.get(0);
	} else {
		data.parentObject = null;
	}
}

@Override
protected void setBaseDirectory(UploadFileServletData data) {
	Analysis analysis = (Analysis) data.parentObject;
	String baseDir = PropertyDictionaryHelper.getInstance(data.sess).getDirectory(data.req.getServerName(), null,
			PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
	String createYear = new SimpleDateFormat("yyyy").format(analysis.getCreateDate());
	data.baseDirectory = FileStringUtil.appendDirectory(baseDir, createYear);
}

@Override
protected void setUploadDirectory(UploadFileServletData data) {
	Analysis analysis = (Analysis) data.parentObject;
	data.uploadDirectory = data.baseDirectory;
	data.uploadDirectory = FileStringUtil.appendDirectory(data.uploadDirectory, analysis.getNumber());
	data.uploadDirectory = FileStringUtil.appendDirectory(data.uploadDirectory, Constants.UPLOAD_STAGING_DIR);
}

@Override
protected Set<GnomexFile> getExistingFiles(UploadFileServletData data) {
	Analysis analysis = (Analysis) data.parentObject;
	return analysis.getFiles();
}

@Override
protected void updateTransferLogFromParentObject(UploadFileServletData data, TransferLog transferLog) {
	Analysis analysis = (Analysis) data.parentObject;
	transferLog.setIdAnalysis(analysis.getIdAnalysis());
	transferLog.setIdLab(analysis.getIdLab());
	transferLog.setFileName(FileStringUtil.appendFile(analysis.getNumber(), data.fileName));
}

@Override
protected void updateExistingFile(UploadFileServletData data, GnomexFile existingFile) {
	AnalysisFile analysisFile = (AnalysisFile) existingFile;
	analysisFile.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
	analysisFile.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
	analysisFile.setFileSize(new BigDecimal(new File(data.fileName).length()));
	data.sess.save(analysisFile);
}

@Override
protected void createNewFile(UploadFileServletData data) {
	Analysis analysis = (Analysis) data.parentObject;
	AnalysisFile af = new AnalysisFile();
	af.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
	af.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
	af.setIdAnalysis(Integer.valueOf(analysis.getIdAnalysis()));
	af.setAnalysis(analysis);
	af.setFileName(new File(data.fileName).getName());
	String analysisDirectory = FileStringUtil.appendDirectory(data.baseDirectory, analysis.getNumber());
	af.setBaseFilePath(FileStringUtil.ensureTrailingFileSeparator(analysisDirectory, false));
	af.setFileSize(new BigDecimal(data.fileSize));
	af.setQualifiedFilePath(Constants.UPLOAD_STAGING_DIR);
	data.sess.save(af);
}

@Override
protected void updateParentObject(UploadFileServletData data) {
	// We do not update the analysis object after uploading files
}

}
