package hci.gnomex.controller;


import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import hci.framework.security.UnknownPermissionException;

import hci.gnomex.security.EncrypterService;
import hci.gnomex.utility.HibernateSession;


public class GetFlowCellChannel extends GNomExCommand implements Serializable {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetFlowCellChannel.class);

	// Parameter:
	private Integer idFlowCellChannel;

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		if (request.getParameter("id") != null) {
			idFlowCellChannel = new Integer(request.getParameter("id"));
		} else {
			this.addInvalidField("idFlowCellChannel", "idFlowCellChannel is required");
		}
		this.validate();
	}

	public Command execute() throws RollBackCommandException {
		try {

			if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
			  
				Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
				FlowCellChannel fcc = null;
				
				if (idFlowCellChannel == null || idFlowCellChannel.intValue() == 0) {
					fcc = new FlowCellChannel();
				} else {
					fcc = (FlowCellChannel)sess.get(FlowCellChannel.class, idFlowCellChannel);
				}

				Hibernate.initialize(fcc.getSequenceLanes());
				
				Document doc = new Document(new Element("FlowCellChannel"));

				Element fccNode = fcc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
				doc.getRootElement().addContent(fccNode);

				XMLOutputter out = new org.jdom.output.XMLOutputter();
				this.xmlResult = out.outputString(doc);
				
			} else {
				this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
				setResponsePage(this.ERROR_JSP);
				}
			
		}catch (UnknownPermissionException e){
			log.error("An exception has occurred in GetFlowCellChannel ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		}catch (NamingException e){
			log.error("An exception has occurred in GetFlowCellChannel ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		}catch (SQLException e) {
			log.error("An exception has occurred in GetFlowCellChannel ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		} catch (XMLReflectException e){
			log.error("An exception has occurred in GetFlowCellChannel ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		} catch (Exception e){
			log.error("An exception has occurred in GetFlowCellChannel ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();        
			} catch(Exception e) {
			}
		} 

		return this;
	}

	public void validate() {
		if (isValid()) {
			setResponsePage(this.SUCCESS_JSP);
		} else {
			setResponsePage(this.ERROR_JSP);
		}
	}
}