package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class DestroyExistingLinks extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(MakeDataTrackUCSCLinks.class);

private String dataTrackFileServerWebContext;

@Override
public Command execute() throws RollBackCommandException {
	try {

		Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());
		dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(
				PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

		File igvLinkDir = new File(dataTrackFileServerWebContext, Constants.IGV_LINK_DIR_NAME);
		File ucscLinkDir = new File(dataTrackFileServerWebContext, Constants.URL_LINK_DIR_NAME);

		this.destroyFolder(igvLinkDir, username);
		this.destroyFolder(ucscLinkDir, username);

		this.xmlResult = "<SUCCESS/>";
		setResponsePage(this.SUCCESS_JSP);

	} catch (Exception e) {
		LOG.error("An exception has occurred in DestroyExistingLinks", e);
		this.xmlResult = "Failed to Delete Links: " + e.getMessage();
		throw new RollBackCommandException(e.getMessage());
	}

	return this;
}

@Override
public void loadCommand(HttpServletRequest request, HttpSession session) {
}

public void validate() {
}

private void destroyFolder(File igvLinkDir, String username) throws Exception {
	File[] directoryList = igvLinkDir.listFiles();

	for (File directory : directoryList) {
		if (directory.getName().length() > 36) {
			String parsedUsername = directory.getName().substring(36);
			if (parsedUsername.equals(username)) {
				delete(directory);
			}
		} else {
			delete(directory);
		}
	}

}

private void delete(File f) throws IOException {
	if (f.isDirectory()) {
		for (File c : f.listFiles())
			delete(c);
	}
	if (!f.delete())
		throw new FileNotFoundException("Failed to delete file: " + f);
}

}
