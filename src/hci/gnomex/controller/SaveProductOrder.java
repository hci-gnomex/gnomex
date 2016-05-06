package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.billing.ProductPlugin;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.MasterBillingItem;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.model.ProductOrderStatus;
import hci.gnomex.model.ProductType;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BillingTemplateParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequisitionFormUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class SaveProductOrder extends GNomExCommand implements Serializable {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProductOrder.class);

	private String 			productListXMLString;
	private Integer 		idBillingAccount;
	private Integer 		idAppUser;
	private Integer 		idLab;
	private BillingPeriod 	billingPeriod;
	private Integer 		idCoreFacility;
	private Document 		productDoc;
	private String 			codeProductOrderStatus;
	private String 			billingTemplateXMLString;
	private Document 		billingTemplateDoc;

	private ProductPlugin 	productPlugin = new ProductPlugin();

	private String 			appURL;
	private String 			serverName;

	public void loadCommand(HttpServletRequest request, HttpSession sess) {

		try {
			appURL = this.getAppURL(request);
		} catch (Exception e) {
			log.warn("Cannot get launch app URL in SaveProductOrder", e);
		}

		serverName = request.getServerName();
		
		if (request.getParameter("idBillingAccount") != null && !request.getParameter("idBillingAccount").equals("")) {
			idBillingAccount = Integer.parseInt(request.getParameter("idBillingAccount"));
		} else if (request.getParameter("billingTemplate") != null && !request.getParameter("billingTemplate").equals("")) {
			billingTemplateXMLString = request.getParameter("billingTemplate");
			StringReader reader = new StringReader(billingTemplateXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				billingTemplateDoc = sax.build(reader);
			} catch (JDOMException je) {
				log.error("Cannot parse billingTemplateXMLString", je);
				this.addInvalidField("billingTemplateXMLString", "Invalid billingTemplate xml");
			}
		}
		
		if (idBillingAccount == null && billingTemplateXMLString == null) {
			this.addInvalidField("Billing Information", "Missing either idBillingAccount or billingTemplate");
		}

		if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
			idAppUser = Integer.parseInt(request.getParameter("idAppUser"));
		} else {
			this.addInvalidField("idAppUser", "Missing idAppUser");
		}

		if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
			idCoreFacility = Integer.parseInt(request.getParameter("idCoreFacility"));
		} else {
			this.addInvalidField("idCoreFacility", "Missing idCoreFacility");
		}

		if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
			idLab = Integer.parseInt(request.getParameter("idLab"));
		} else {
			this.addInvalidField("idLab", "Missing idLab");
		}

		if (request.getParameter("codeProductOrderStatus") != null && !request.getParameter("codeProductOrderStatus").equals("")) {
			codeProductOrderStatus = request.getParameter("codeProductOrderStatus");
		} else {
			this.addInvalidField("codeProductOrderStatus", "Missing codeProductOrderStatus");
		}

		if (request.getParameter("productListXMLString") != null && !request.getParameter("productListXMLString").equals("")) {
			productListXMLString = request.getParameter("productListXMLString");
		} else {
			this.addInvalidField("productListXMLString", "Missing productListXMLString");
		}

		StringReader reader = new StringReader(productListXMLString);
		try {
			SAXBuilder sax = new SAXBuilder();
			productDoc = sax.build(reader);
		} catch (JDOMException je) {
			log.error("Cannot parse producListXMLString", je);
			this.addInvalidField("productListXMLString", "Invalid producList xml");
		}

	}

	public Command execute() throws RollBackCommandException {
		Session sess = null;
		try {
			if (this.isValid()) {
				sess = HibernateSession.currentSession(this.getUsername());
				DictionaryHelper dh = DictionaryHelper.getInstance(sess);

				Element root = new Element("SUCCESS");
				Document outputDoc = new Document(root);

				billingPeriod = DictionaryHelper.getInstance(sess).getCurrentBillingPeriod();
				Lab lab = DictionaryHelper.getInstance(sess).getLabObject(idLab);
				HashMap<Integer, ArrayList<Element>> productTypes = new HashMap<Integer, ArrayList<Element>>();

				for (Iterator i = productDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
					Element n = (Element) i.next();
					if (n.getAttribute("quantity") == null || n.getAttributeValue("quantity").equals("") || n.getAttributeValue("quantity").equals("0")) {
						continue;
					}
					if (!productTypes.containsKey(Integer.parseInt(n.getAttributeValue("idProductType")))) {
						ArrayList<Element> products = new ArrayList<Element>();
						products.add(n);
						productTypes.put(Integer.parseInt(n.getAttributeValue("idProductType")), products);
					} else {
						ArrayList<Element> existingList = productTypes.get(Integer.parseInt(n.getAttributeValue("idProductType")));
						existingList.add(n);
						productTypes.put(Integer.parseInt(n.getAttributeValue("idProductType")), existingList);
					}
				}

				for (Iterator i = productTypes.keySet().iterator(); i.hasNext();) {
					Integer idProductTypeKey = (Integer) i.next();
					ProductType productType = sess.load(ProductType.class, idProductTypeKey);
					PriceCategory priceCategory = sess.load(PriceCategory.class, productType.getIdPriceCategory());
					ArrayList<Element> products = productTypes.get(idProductTypeKey);
					Set<ProductLineItem> productLineItems = new TreeSet<ProductLineItem>(new ProductLineItemComparator());

					if (products.size() > 0) {
						// Set up product order
						ProductOrder po = new ProductOrder();
						initializeProductOrder(po, idProductTypeKey);
						sess.save(po);
						po.setProductOrderNumber(getNextPONumber(po, sess));
						sess.save(po);
						
						// Set up billing template
						BillingTemplate billingTemplate;
						if (idBillingAccount != null) {
							billingTemplate = new BillingTemplate();
							billingTemplate.setOrder(po);
							billingTemplate.updateSingleBillingAccount(idBillingAccount);
							sess.save(billingTemplate);
							for (BillingTemplateItem item : billingTemplate.getItems()) {
								item.setIdBillingTemplate(billingTemplate.getIdBillingTemplate());
								sess.save(item);
							}
							sess.flush();
						} else {
							BillingTemplateParser btParser = new BillingTemplateParser(billingTemplateDoc.getRootElement());
							btParser.parse(sess);
							billingTemplate = btParser.getBillingTemplate();
							billingTemplate.setOrder(po);
							sess.save(billingTemplate);
							sess.flush();

							// Get new template items from parser and save to billing template
							TreeSet<BillingTemplateItem> btiSet = btParser.getBillingTemplateItems();
							for (BillingTemplateItem newlyCreatedItem : btiSet) {
								newlyCreatedItem.setIdBillingTemplate(billingTemplate.getIdBillingTemplate());
								billingTemplate.getItems().add(newlyCreatedItem);
								sess.save(newlyCreatedItem);
							}
							sess.flush();
						}
						
						po.setIdBillingAccount(billingTemplate.getAcceptingBalanceItem().getIdBillingAccount());
						sess.save(po);

						for (Element n : products) {
							if (n.getAttributeValue("isSelected").equals("Y") && n.getAttributeValue("quantity") != null
									&& !n.getAttributeValue("quantity").equals("") && !n.getAttributeValue("quantity").equals("0")) {
								ProductLineItem pi = new ProductLineItem();
								Price p = sess.load(Price.class, Integer.parseInt(n.getAttributeValue("idPrice")));
								initializeProductLineItem(pi, po.getIdProductOrder(), n, p.getEffectiveUnitPrice(lab));
								productLineItems.add(pi);
								sess.save(pi);
							}
						}
						po.setProductLineItems(productLineItems);
						sess.save(po);

						sess.flush();
						sess.refresh(po);

						Element poNode = new Element("ProductOrder");

						String submitter = dh.getAppUserObject(po.getIdAppUser()).getDisplayName();
						String orderStatus = po.getStatus();

						poNode.setAttribute("display", po.getDisplay());
						poNode.setAttribute("submitter", submitter);
						poNode.setAttribute("submitDate", po.getSubmitDate().toString());
						poNode.setAttribute("status", orderStatus);
						poNode.setAttribute("idLab", idLab == null ? "" : idLab.toString());
						poNode.setAttribute("idProductOrder", po.getIdProductOrder() != null ? po.getIdProductOrder().toString() : "");
						poNode.setAttribute("productOrderNumber", po.getProductOrderNumber() != null ? po.getProductOrderNumber() : "");
						outputDoc.getRootElement().addContent(poNode);

						List<BillingItem> billingItems = productPlugin.constructBillingItems(sess, billingPeriod, priceCategory, po, productLineItems, billingTemplate);

						for (MasterBillingItem masterBillingItem : billingTemplate.getMasterBillingItems()) {
							sess.save(masterBillingItem);
							for (BillingItem billingItem : masterBillingItem.getBillingItems()) {
								billingItem.setIdMasterBillingItem(masterBillingItem.getIdMasterBillingItem());
							}
						}

						for (Iterator<BillingItem> j = billingItems.iterator(); j.hasNext();) {
							BillingItem bi = j.next();
							sess.save(bi);
						}

						sendConfirmationEmail(sess, po, ProductOrderStatus.NEW, serverName);

						boolean isHCI = lab.getContactEmail().indexOf("@hci.utah.edu") > 0;
						// Check that the product type is set up to use the purchasing system
						if (productType.getUtilizePurchasingSystem() != null && productType.getUtilizePurchasingSystem().equals("Y") && !isHCI
								&& !lab.isExternalLab()) {
							// REQUISITION FORM
							try {
								// Download and fill out requisition form
								File reqFile = RequisitionFormUtil.saveReqFileFromURL(po, sess, serverName);
								reqFile = RequisitionFormUtil.populateRequisitionForm(po, reqFile, sess);

								if (reqFile == null) {
									String msg = "Unable to download requisition form for ProductOrder " + po.getIdProductOrder() + ".";
									System.out.println(msg);
								} else {
									// Save requisition form as ProductOrderFile and send vendor email
									String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, po.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_PRODUCT_ORDER_DIRECTORY)
											+ po.getCreateYear() + "/" + po.getIdProductOrder();
									String qualDir = "/" + Constants.REQUISITION_DIR;
									ProductOrderFile poFile = new ProductOrderFile();
									poFile.setIdProductOrder(po.getIdProductOrder());
									poFile.setCreateDate(new Date(System.currentTimeMillis()));
									poFile.setFileName(reqFile.getName());
									poFile.setFileSize(new BigDecimal(reqFile.length()));
									poFile.setBaseFilePath(baseDir);
									poFile.setQualifiedFilePath(qualDir);
									sess.save(poFile);

									sendVendorEmail(sess, po, productType);
								}

							} catch (Exception e) {
								String msg = "Unable to download requisition form for ProductOrder " + po.getIdProductOrder() + ".  " + e.toString();
								System.out.println(msg);
								e.printStackTrace();
							}
						}
					}
				}

				sess.flush();

				XMLOutputter out = new org.jdom.output.XMLOutputter();
				out.setOmitEncoding(true);
				this.xmlResult = out.outputString(outputDoc);
				this.setResponsePage(SUCCESS_JSP);

			} else {
				this.addInvalidField("Insufficient permissions", "Insufficient permission to create product orders.");
				setResponsePage(this.ERROR_JSP);
			}

		} catch (Exception e) {
			log.error("An exception has occurred while emailing in SaveRequest ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.toString());
		} finally {
			try {
				HibernateSession.closeSession();
			} catch (Exception e) {

			}
		}

		return this;
	}

	public static void sendConfirmationEmail(Session sess, ProductOrder po, String orderStatus, String serverName) throws NamingException, MessagingException,
			IOException {

		DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		CoreFacility cf = sess.load(CoreFacility.class, po.getIdCoreFacility());

		String subject = "";
		if (orderStatus.equals(ProductOrderStatus.NEW)) {
			subject = "Product Order " + po.getProductOrderNumber() + " has been submitted.";
		} else if (orderStatus.equals(ProductOrderStatus.COMPLETED)) {
			subject = "Product Order " + po.getProductOrderNumber() + " has been completed.";
		}
		String contactEmailCoreFacility = cf.getContactEmail() != null ? cf.getContactEmail() : "";
		String contactEmailAppUser = po.getSubmitter().getEmail() != null ? po.getSubmitter().getEmail() : "";
		String fromAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		String noAppUserEmailMsg = "";

		String toAddress = contactEmailCoreFacility + "," + contactEmailAppUser;

		BillingAccount ba = sess.load(BillingAccount.class, po.getAcceptingBalanceAccountId(sess));
		ProductType pt = sess.load(ProductType.class, po.getIdProductType());

		if (!MailUtil.isValidEmail(contactEmailAppUser)) {
			noAppUserEmailMsg = "The user who submitted this product order did not receive a copy of this confirmation because they do not have a valid email on file.\n";
		}

		// If no valid to address then send to gnomex support team
		if (!MailUtil.isValidEmail(toAddress)) {
			toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
		}

		StringBuffer body = new StringBuffer();
        body.append("  <STYLE TYPE=\"text/css\">" +
                "TD{font-family: Arial; font-size: 9pt;}" +
                "</STYLE><FONT face=\"arial\" size=\"9pt\">");
		if (orderStatus.equals(ProductOrderStatus.NEW)) {
			body.append("Product Order " + po.getProductOrderNumber() + " has been submitted to the " + cf.getFacilityName() + ".<br>");
		} else if (orderStatus.equals(ProductOrderStatus.COMPLETED)) {
			body.append("Product Order " + po.getProductOrderNumber() + " has been completed and the products are ready for your use.<br>");
		}

        body.append("<br><table border='0' width='400'>");
		body.append("<tr><td>Submit Date:</td><td>" + po.getSubmitDate() + "</td></tr>");
		body.append("<tr><td>Submitted By:</td><td>" + po.getSubmitter().getDisplayName() + "</td></tr>");
		body.append("<tr><td>Lab:</td><td>" + po.getLab().getName(false, true) + "</td></tr>");
		body.append("<tr><td>Billing Acct:</td><td>" + ba.getAccountNameAndNumber() + "</td></tr>");
		body.append("<tr><td>Product Type:</td><td>" + pt.getDisplay() + "</td></tr>");
		body.append("</table><br>Products Ordered:<br>");

        body.append(getProductLineItemTable(po, sess));

		body.append("<br><br><FONT COLOR=\"#ff0000\">" + noAppUserEmailMsg + "</FONT></FONT>");

		MailUtilHelper mailHelper = new MailUtilHelper(toAddress, fromAddress, subject, body.toString(), null, true, dictionaryHelper, serverName);

		MailUtil.validateAndSendEmail(mailHelper);

	}

    private static StringBuffer getProductLineItemTable(ProductOrder po, Session sess) {
        StringBuffer productTableString = new StringBuffer();
        productTableString.append("<table border='0' width = '300'>");
        productTableString.append("<tr><th>Name</th><th>Qty</th><th>Cost</th></tr>");

        BigDecimal grandTotal = new BigDecimal(BigInteger.ZERO, 2);
        for (Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
            ProductLineItem pli = (ProductLineItem) i.next();
            Product p = sess.load(Product.class, pli.getIdProduct());
            BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
            estimatedCost = pli.getUnitPrice().multiply(new BigDecimal(pli.getQty()));
            grandTotal = grandTotal.add(estimatedCost);

            productTableString.append("<tr><td>" + p.getDisplay() + "</td><td align=\"center\">" + pli.getQty() + "</td><td align=\"right\">$" + estimatedCost + "</td></tr>");
        }


        productTableString.append("</table>");
        productTableString.append("<br>Grand Total:  $" + grandTotal);

        return productTableString;
    }

	public static String getNextPONumber(ProductOrder po, Session sess) throws SQLException {
		String poNumber = "";
		String procedure = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(po.getIdCoreFacility(),
				PropertyDictionary.GET_PO_NUMBER_PROCEDURE);
		if (procedure != null && procedure.length() > 0) {
			SessionImpl sessionImpl = (SessionImpl) sess;
			Connection con = sessionImpl.connection();
			String queryString = "";
			if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
				queryString = "exec " + procedure;
			} else {
				queryString = "call " + procedure;
			}
			SQLQuery query = sess.createSQLQuery(queryString);
			List l = query.list();
			if (l.size() != 0) {
				Object o = l.get(0);
				if (o.getClass().equals(String.class)) {
					poNumber = (String) o;
					poNumber = poNumber.toUpperCase();
				}
			}
		}
		if (poNumber==null || poNumber.length() == 0) {
			poNumber = po.getIdProductOrder().toString();
		}

		return poNumber;
	}

	private void initializeProductOrder(ProductOrder po, Integer idProductType) {
		po.setSubmitDate(new Date(System.currentTimeMillis()));
		po.setIdProductType(idProductType);
		po.setQuoteNumber("");
		po.setUuid(UUID.randomUUID().toString()); // Probably only need to set this if going to use purchasing system...
		po.setIdAppUser(idAppUser);
		po.setIdCoreFacility(idCoreFacility);
		po.setIdLab(idLab);
	}

	private void initializeProductLineItem(ProductLineItem pi, Integer idProductOrder, Element n, BigDecimal unitPrice) {
		pi.setIdProductOrder(idProductOrder);
		pi.setIdProduct(Integer.parseInt(n.getAttributeValue("idProduct")));
		pi.setQty(Integer.parseInt(n.getAttributeValue("quantity")));
		pi.setUnitPrice(unitPrice);
		pi.setCodeProductOrderStatus(codeProductOrderStatus);
	}

	private boolean sendVendorEmail(Session sess, ProductOrder po, ProductType productType) throws NamingException, MessagingException {

		DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

		CoreFacility cf = sess.load(CoreFacility.class, idCoreFacility);

		StringBuffer emailBody = new StringBuffer();

		String uuidStr = po.getUuid();
		String uploadQuoteURL = appURL + "/" + Constants.UPLOAD_QUOTE_JSP + "?orderUuid=" + uuidStr;

		String productTypeString = productType.getDisplay();

		emailBody.append("A request for " + productTypeString + " has been submitted from the " + cf.getFacilityName() + " core.");

		emailBody.append("<br><br><table border='0' width = '600'>");
		for (Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
			ProductLineItem pi = (ProductLineItem) i.next();
			Product p = sess.load(Product.class, pi.getIdProduct());

			emailBody.append("<tr><td>Product Name:</td><td>" + p.getName() + "</td></tr>");
			emailBody.append("<tr><td>Catalog Number:</td><td>" + p.getCatalogNumber() + "</td></tr>");

			if (i.hasNext()) {
				emailBody.append("</table><br><table border='0' width = '600'>");
			}

		}

		emailBody.append("</table><br><br>To enter a quote number and upload a file, click <a href=\"" + uploadQuoteURL + "\">" + Constants.APP_NAME
				+ " - Upload Quote Info</a>.");

		String subject = "Request for Quote Number for " + productTypeString;

		String contactEmailCoreFacility = cf.getContactEmail();
		// TODO Should look up email address for vendor instead of property for Illumina rep.
		String contactEmailIlluminaRep = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility,
				PropertyDictionary.CONTACT_EMAIL_ILLUMINA_REP);

		String senderEmail = contactEmailCoreFacility;
		String contactEmail = contactEmailIlluminaRep;
		String ccEmail = null;

		if (!MailUtil.isValidEmail(contactEmail)) {
			log.error("Invalid email address: " + contactEmail);
		}

		if (!MailUtil.isValidEmail(senderEmail)) {
			senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		}

		boolean sent = false;
		try {
			MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBody.toString(), null, true, dictionaryHelper,
					serverName);
			sent = MailUtil.validateAndSendEmail(helper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sent;
	}

	public class ProductLineItemComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			ProductLineItem li1 = (ProductLineItem) o1;
			ProductLineItem li2 = (ProductLineItem) o2;
			return li1.getIdProduct().compareTo(li2.getIdProduct());

		}
	}

	public void validate() {
	}

}
