package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;
@SuppressWarnings("serial")
public class GetPriceCategories extends GNomExCommand implements Serializable {

	private static Logger LOG = Logger.getLogger(GetPriceCategories.class);
	
	private boolean		addWhere;
	
	private Integer 	idPriceSheet;
	private String		priceSheetName;
	
	private boolean		requireIsActive; 

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {

		if (request.getParameter("idPriceSheet") != null) {
			idPriceSheet = new Integer(request.getParameter("idPriceSheet"));
		}
		
		if (request.getParameter("priceSheetName") != null) {
			priceSheetName = request.getParameter("priceSheetName");
		}
		
		requireIsActive = true;
		if (request.getParameter("requireIsActive") != null && request.getParameter("requireIsActive").equalsIgnoreCase("Y")) {
			requireIsActive = true;
		} else if (request.getParameter("requireIsActive") != null && request.getParameter("requireIsActive").equalsIgnoreCase("N")) {
			requireIsActive = false;
		}
		
		if (idPriceSheet == null && priceSheetName == null) {
			this.addInvalidField("idPriceSheet", "idPriceSheet is required");
			this.addInvalidField("priceSheetName", "priceSheetName is required");
		}
	}

	@SuppressWarnings("unchecked")
	public Command execute() throws RollBackCommandException {

		try {

			Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

			if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
				this.addInvalidField("permissionerror", "Insufficient permissions to access these price categories.");
			}

			if (isValid()) {
				StringBuffer queryBuf = new StringBuffer();
				addWhere = true;
				
				queryBuf.append(" SELECT DISTINCT ps FROM PriceSheet AS ps ");
				
				if (idPriceSheet != null) {
					this.addWhereOrAnd(queryBuf);
					queryBuf.append(" ps.idPriceSheet =:idPriceSheet ");
				}
				if (priceSheetName != null) {
					this.addWhereOrAnd(queryBuf);
					queryBuf.append(" ps.name =:priceSheetName ");
				}
				
				LOG.info("GetPriceCategories Query (parameters not set): " + queryBuf.toString());
				Query query = sess.createQuery(queryBuf.toString());
				
				if (idPriceSheet != null) {
					query.setParameter("idPriceSheet", idPriceSheet);
				}
				if (priceSheetName != null) {
					query.setParameter("priceSheetName", priceSheetName);
				}
				
				List<PriceSheet> queryResult = (List<PriceSheet>) query.list();
				HashSet<PriceCategory> priceCategories = new HashSet<PriceCategory>();
				for (PriceSheet ps : queryResult) {
					for(Iterator<PriceSheetPriceCategory> i = ps.getPriceCategories().iterator(); i.hasNext();) {
			            PriceSheetPriceCategory pspc  = i.next();
			            PriceCategory cat = pspc.getPriceCategory();
			            if (cat != null) {
			            	priceCategories.add(cat);
			            }
					}
				}
				
				Document doc = new Document(new Element("PriceCategoryList"));
				for (PriceCategory pc : priceCategories) {
					if (requireIsActive && (pc.getIsActive() == null || !pc.getIsActive().equalsIgnoreCase("Y"))) {
						continue;
					}
					pc.excludeMethodFromXML("getPrices");
					pc.excludeMethodFromXML("getSteps");
					Element priceCategoryNode = pc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
					doc.getRootElement().addContent(priceCategoryNode);
				}

				XMLOutputter out = new org.jdom.output.XMLOutputter();
				this.xmlResult = out.outputString(doc);
			}

			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (UnknownPermissionException e) {
			LOG.error("An exception has occurred in GetPriceCategories ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (NamingException e) {
			LOG.error("An exception has occurred in GetPriceCategories ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (SQLException e) {
			LOG.error("An exception has occurred in GetPriceCategories ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (XMLReflectException e) {
			LOG.error("An exception has occurred in GetPriceCategories ", e);

			throw new RollBackCommandException(e.getMessage());
		} catch (Exception e) {
			LOG.error("An exception has occurred in GetPriceCategories ", e);

			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();
			} catch (Exception e) {

			}
		}

		return this;
	}
	
	private boolean addWhereOrAnd(StringBuffer queryBuf) {
	    if (addWhere) {
	      queryBuf.append(" WHERE ");
	      addWhere = false;
	    } else {
	      queryBuf.append(" AND ");
	    }
	    return addWhere;
	  }

}
