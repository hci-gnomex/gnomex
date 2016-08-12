package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.Lab;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingPDFFormatter;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("serial")
public class ShowBillingInvoiceForm extends ReportCommand implements Serializable {
	
	public static final String      		DISK_USAGE_NUMBER_PREFIX 		= "ZZZZDiskUsage"; // Leading Z's to make it sort last
	public static final String     			PRODUCT_ORDER_NUMBER_PREFIX 	= "ZZZZProductOrder"; // Leading Z's to make it sort last
	
	private static Logger 	LOG = Logger.getLogger(ShowBillingInvoiceForm.class);
	
	public String 							SUCCESS_JSP = "/form_pdf.jsp";
	
	private Integer 						idLab;
	private Integer 						idBillingAccount;
	private Integer 						idBillingPeriod;
	private Integer 						idCoreFacility;
	private String 							idLabs;
	private String 							idBillingAccounts;
	
	private SecurityAdvisor 				secAdvisor;
	private DictionaryHelper				dh;
	private BillingPeriod 					billingPeriod;
	private CoreFacility 					coreFacility;
	
	private boolean 						multipleLabs;
	
	private Lab 							lab;
	private Lab[] 							labs;
	private BillingAccount 					billingAccount;
	private BillingAccount[] 				billingAccounts;
	
	@Override
	public void validate() {
	}
	
	@Override
	public void loadCommand(HttpServletRequest request, HttpSession session) {
		if (request.getParameter("idLabs") != null && !request.getParameter("idLabs").equals("")) {
			idLabs = request.getParameter("idLabs");
			
			if (request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")) {
				idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
			} else {
				this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
			}
			
			if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
				idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
			} else {
				this.addInvalidField("idCoreFacility", "idCoreFacility is required");
			}
			
			if (request.getParameter("idBillingAccounts") != null && !request.getParameter("idBillingAccounts").equals("")) {
				idBillingAccounts = request.getParameter("idBillingAccounts");
			}
		}
		
		if (idLabs == null) {
			if (request.getParameter("idLab") != null) {
				idLab = new Integer(request.getParameter("idLab"));
			} else {
				this.addInvalidField("idLab", "idLab is required");
			}
			
			if (request.getParameter("idBillingAccount") != null && request.getParameter("idBillingAccount").length() > 0) {
				idBillingAccount = new Integer(request.getParameter("idBillingAccount"));
			} else {
				this.addInvalidField("idBillingAccount", "idBillingAccount is required");
			}
			
			if (request.getParameter("idBillingPeriod") != null) {
				idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
			} else {
				this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
			}
			
			if (request.getParameter("idCoreFacility") != null) {
				idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
			} else {
				this.addInvalidField("idCoreFacility", "idCoreFacility is required");
			}
		}
		
		secAdvisor = (SecurityAdvisor) session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
	}	

	@Override
	public Command execute() throws RollBackCommandException { 
		try {
		    
		    if (isValid()) {
		    	
		    	if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
		    		
		    		Session sess = secAdvisor.getHibernateSession(this.getUsername());
		    		dh = DictionaryHelper.getInstance(sess);
		    		billingPeriod = dh.getBillingPeriod(idBillingPeriod);
		    		coreFacility = (CoreFacility) sess.get(CoreFacility.class, idCoreFacility);
		    		
		    		String title;
		    		String fileName;
		    		
		    		Map[] billingItemMap;
		    		Map[] relatedBillingItemMap;
		    		Map[] requestMap;
		    		
		    		if (idLabs != null) {
		    			multipleLabs = true;
		    			
		    			String[] labsAsString = idLabs.split(",");
		    			labs = new Lab[labsAsString.length];
		    			for (int i = 0; i < labsAsString.length; i++) {
		    				labs[i] = (Lab) sess.get(Lab.class, new Integer(labsAsString[i]));
		    			}
		    			
		                String[] billingAccountsAsString = idBillingAccounts.split(",");
		                billingAccounts = new BillingAccount[billingAccountsAsString.length];
		                for (int i = 0; i < billingAccountsAsString.length; i++) {
		                	billingAccounts[i] = (BillingAccount) sess.get(BillingAccount.class, new Integer(billingAccountsAsString[i]));
		                }
		                
		                lab = null;
		                billingAccount = null;
		                
		                title = "Billing Invoices";
		                fileName = "gnomex_billing_invoices";
		                
		                billingItemMap = new TreeMap[labs.length];
			    		relatedBillingItemMap = new TreeMap[labs.length];
			    		requestMap = new TreeMap[labs.length];
			    		for (int i = 0; i < labs.length; i++) {
			    			billingItemMap[i] = new TreeMap();
				    		relatedBillingItemMap[i] = new TreeMap();
				    		requestMap[i] = new TreeMap();
			    			cacheBillingItemMaps(sess, secAdvisor, idBillingPeriod, labs[i].getIdLab(), billingAccounts[i].getIdBillingAccount(), idCoreFacility, billingItemMap[i], relatedBillingItemMap[i], requestMap[i]);
			    		}
		    		} else {
		    			multipleLabs = false;
		    			
		    			lab = (Lab) sess.get(Lab.class, new Integer(idLab));
		    			billingAccount = (BillingAccount) sess.get(BillingAccount.class, idBillingAccount);
		    			
		    			labs = null;
		    			billingAccounts = null;
		    			
		    			title = "Billing Invoice - " + lab.getName(false, true) + " " + billingAccount.getAccountName();
		    			fileName = "gnomex_billing_invoice";
		    			
		    			billingItemMap = new TreeMap[1];
		    			billingItemMap[0] = new TreeMap();
			    		relatedBillingItemMap = new TreeMap[1];
			    		relatedBillingItemMap[0] = new TreeMap();
			    		requestMap = new TreeMap[1];
			    		requestMap[0] = new TreeMap();
			    		cacheBillingItemMaps(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap[0], relatedBillingItemMap[0], requestMap[0]);
		    		}
		    		
		    		// Set up the ReportTray
				    tray = new ReportTray();
				    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
				    tray.setReportTitle(title);
				    tray.setReportDescription(title);
				    tray.setFileName(fileName);
				    tray.setFormat(ReportFormats.PDF);
		    		
		    		@SuppressWarnings("rawtypes")
					java.util.List rows = new ArrayList();
		    		
		    		// Build PDF elements
		    		BillingPDFFormatter formatter = new BillingPDFFormatter(
		    				sess, 
		    				billingPeriod, 
		    				coreFacility, 
		    				multipleLabs, 
		    				lab, 
		    				labs, 
		    				billingAccount, 
		    				billingAccounts, 
		    				PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
		    				PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY),
		    				coreFacility.getContactName(),
		    				coreFacility.getContactPhone(),
		    				billingItemMap,
		    				relatedBillingItemMap,
		    				requestMap);
		    		
		    		ArrayList<Element> content = formatter.makeContent();
		    		for (Element e : content) {
		    			rows.add(e);
		    		}
		    		
		    		tray.setRows(rows);
		    		
		    	}
		    }
			
			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
		    } else {
		        setResponsePage(this.ERROR_JSP);
		    }
			
		} catch (Exception e) {
		    LOG.error("An exception has occurred in ShowBillingInvoiceForm ", e);

		    throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				secAdvisor.closeHibernateSession();    
			} catch(Exception e){
        LOG.error("Error", e);
      }
		}
		
		return this;
	}
	
	public static File makePDFBillingInvoice(Session sess, String serverName, BillingPeriod billingPeriod, CoreFacility coreFacility, boolean multipleLabs, Lab lab, Lab[] labs, 
												BillingAccount billingAccount, BillingAccount[] billingAccounts, String contactAddressCoreFacility, String contactRemitAddressCoreFacility, 
												Map[] billingItemMap, Map[] relatedBillingItemMap, Map[] requestMap) throws Exception {
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		
		String filename = "gnomex_billing_invoice.pdf";
		filename = pdh.getQualifiedProperty(PropertyDictionary.TEMP_DIRECTORY, serverName) + filename;
		File outputFile = new File(filename);
		FileOutputStream fileout = new FileOutputStream(outputFile);
		
		Document doc = new Document(PageSize.LETTER);
		PdfWriter.getInstance(doc, fileout);
		doc.open();

		doc.addCreationDate();
		doc.addTitle("Billing Invoice - " + lab.getName(false, true) + " " + billingAccount.getAccountName());
		
		BillingPDFFormatter formatter = new BillingPDFFormatter(
				sess, 
				billingPeriod, 
				coreFacility, 
				multipleLabs, 
				lab, 
				labs, 
				billingAccount, 
				billingAccounts, 
				PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(coreFacility.getIdCoreFacility(), PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
				PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(coreFacility.getIdCoreFacility(), PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY),
				coreFacility.getContactName(),
				coreFacility.getContactPhone(),
				billingItemMap,
				relatedBillingItemMap,
				requestMap);
		
		
		ArrayList<Element> content = formatter.makeContent();
		for (Element e : content) {
			doc.add(e);
		}
		if (content.isEmpty()) {
			doc.add(new Paragraph("No Results"));
		}
		
		doc.close();
		
		return outputFile;
	}
	
	public static void cacheBillingItemMaps(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) throws Exception {
		cacheRequestBillingItemMap(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
		cacheDiskUsageBillingItemMap(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
		cacheProductOrderBillingItemMap(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount, idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
	}
	
	private static void cacheRequestBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
	    StringBuffer buf = new StringBuffer();
	    buf.append("SELECT req, bi ");
	    buf.append("FROM   Request req ");
	    buf.append("JOIN   req.billingItems bi ");
	    buf.append("WHERE  bi.idLab = :idLab ");
	    buf.append("AND    bi.idBillingAccount = :idBillingAccount ");
	    buf.append("AND    bi.idBillingPeriod = :idBillingPeriod ");
	    buf.append("AND    bi.idCoreFacility = :idCoreFacility ");
	    buf.append("AND    bi.codeBillingStatus in ('" + BillingStatus.COMPLETED + "', '" + BillingStatus.APPROVED + "', '" + BillingStatus.APPROVED_CC + "', '" + BillingStatus.APPROVED_PO + "')");

	    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
	    	buf.append(" AND ");
	    	secAdvisor.appendCoreFacilityCriteria(buf, "bi");
	    	buf.append(" ");
	    }

	    buf.append("ORDER BY req.number, bi.idBillingItem ");

	    Query query1 = sess.createQuery(buf.toString());
	    query1.setParameter("idLab", idLab);
	    query1.setParameter("idBillingAccount", idBillingAccount);
	    query1.setParameter("idBillingPeriod", idBillingPeriod);
	    query1.setParameter("idCoreFacility", idCoreFacility);
	    List results = query1.list();

	    for (Iterator i = results.iterator(); i.hasNext();) {
	    	Object[] row = (Object[]) i.next();
	    	Request req    =  (Request) row[0];
	    	BillingItem bi =  (BillingItem) row[1];

	    	// Exclude any requests that have
	    	// pending billing items.
	    	if (determineContainsPendingItems(req.getBillingItemList(sess), idBillingPeriod, idBillingAccount)) {
	    		continue;
	    	}

	    	requestMap.put(req.getNumber(), req);

	    	List billingItems = (List)billingItemMap.get(req.getNumber());
	    	if (billingItems == null) {
	    		billingItems = new ArrayList();
	    		billingItemMap.put(req.getNumber(), billingItems);
	    	}
	    	billingItems.add(bi);
	    }

	    buf = new StringBuffer();
	    buf.append("SELECT req, bi ");
	    buf.append("FROM   Request req ");
	    buf.append("JOIN   req.billingItems bi ");
	    buf.append("WHERE  bi.idBillingAccount != :idBillingAccount ");
	    buf.append("AND    bi.idBillingPeriod = :idBillingPeriod ");

	    if (requestMap.keySet().iterator().hasNext()) {
	    	buf.append("AND    req.idRequest in (");
	    	Boolean first = true;
	    	for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
	    		String requestNumber = (String)i.next();      
	    		Request request = (Request)requestMap.get(requestNumber);
	    		if (!first) {
	    			buf.append(", ");
	    		}
	    		first = false;
	    		buf.append(request.getIdRequest().toString());
	      }
	      buf.append(")");
	    }

	    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
	    	buf.append(" AND ");
	    	secAdvisor.appendCoreFacilityCriteria(buf, "req");
	    	buf.append(" ");
	    }

	    buf.append(" ORDER BY req.number, bi.idBillingAccount, bi.idBillingItem ");

	    Query query2 = sess.createQuery(buf.toString());
	    query2.setParameter("idBillingAccount", idBillingAccount);
	    query2.setParameter("idBillingPeriod", idBillingPeriod);
	    results = query2.list();

	    for (Iterator i = results.iterator(); i.hasNext();) {
	    	Object[] row = (Object[]) i.next();
	    	Request req    =  (Request) row[0];
	    	BillingItem bi =  (BillingItem) row[1];

	    	List billingItems = (List)relatedBillingItemMap.get(req.getNumber());
	    	if (billingItems == null) {
	    		billingItems = new ArrayList();
	    		relatedBillingItemMap.put(req.getNumber(), billingItems);
	    	}
	    	billingItems.add(bi);
	    }		
	}
	
	private static void cacheDiskUsageBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
	    StringBuffer buf = new StringBuffer();
	    buf.append("SELECT dsk, bi ");
	    buf.append("FROM   DiskUsageByMonth dsk ");
	    buf.append("JOIN   dsk.billingItems bi ");
	    buf.append("WHERE  bi.idLab = :idLab ");
	    buf.append("AND    bi.idBillingAccount = :idBillingAccount ");
	    buf.append("AND    bi.idBillingPeriod = :idBillingPeriod ");
	    buf.append("AND    bi.idCoreFacility = :idCoreFacility ");
	    buf.append("AND    bi.codeBillingStatus in ('" + BillingStatus.COMPLETED + "', '" + BillingStatus.APPROVED + "', '" + BillingStatus.APPROVED_CC + "', '" + BillingStatus.APPROVED_PO + "')");

	    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
	    	buf.append(" AND ");
	    	secAdvisor.appendCoreFacilityCriteria(buf, "bi");
	    	buf.append(" ");
	    }

	    buf.append("ORDER BY dsk.idDiskUsageByMonth, bi.idBillingItem ");

	    Query query = sess.createQuery(buf.toString());
	    query.setParameter("idLab", idLab);
	    query.setParameter("idBillingAccount", idBillingAccount);
	    query.setParameter("idBillingPeriod", idBillingPeriod);
	    query.setParameter("idCoreFacility", idCoreFacility);
	    List results = query.list();

	    for(Iterator i = results.iterator(); i.hasNext();) {
	    	Object[] row = (Object[]) i.next();
	    	DiskUsageByMonth dsk    =  (DiskUsageByMonth) row[0];
	    	BillingItem bi          =  (BillingItem) row[1];

	    	// Exclude any disk usage that have
	    	// pending billing items.  (shouldn't be any)
	    	if (determineContainsPendingItems(dsk.getBillingItems(), idBillingPeriod, idBillingAccount)) {
	    		continue;
	    	}

	    	String diskUsageNumber = DISK_USAGE_NUMBER_PREFIX + dsk.getIdDiskUsageByMonth().toString();
	    	requestMap.put(diskUsageNumber, dsk);

	    	List billingItems = (List)billingItemMap.get(diskUsageNumber);
	      	if (billingItems == null) {
	      		billingItems = new ArrayList();
	      		billingItemMap.put(diskUsageNumber, billingItems);
	      	}
	      	billingItems.add(bi);
	    }		
	}
	
	private static void cacheProductOrderBillingItemMap(Session sess, SecurityAdvisor secAdvisor, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount, Integer idCoreFacility, Map billingItemMap, Map relatedBillingItemMap, Map requestMap) {
		StringBuffer buf = new StringBuffer();
	    buf.append("SELECT pli, po, bi ");
	    buf.append("FROM   ProductLineItem pli ");
	    buf.append("JOIN   pli.productOrder po ");
	    buf.append("JOIN   po.billingItems bi ");
	    buf.append("WHERE  bi.idLab = :idLab ");
	    buf.append("AND    bi.idBillingAccount = :idBillingAccount ");
	    buf.append("AND    bi.idBillingPeriod = :idBillingPeriod ");
	    buf.append("AND    bi.idCoreFacility = :idCoreFacility ");
	    buf.append("AND    bi.codeBillingStatus in ('" + BillingStatus.COMPLETED + "', '" + BillingStatus.APPROVED + "', '" + BillingStatus.APPROVED_CC + "', '" + BillingStatus.APPROVED_PO + "')");

	    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
	    	buf.append(" AND ");
	    	secAdvisor.appendCoreFacilityCriteria(buf, "po");
	    	buf.append(" ");
	    }

	    buf.append("ORDER BY po.productOrderNumber, bi.idBillingItem ");

	    Query query = sess.createQuery(buf.toString());
	    query.setParameter("idLab", idLab);
	    query.setParameter("idBillingAccount", idBillingAccount);
	    query.setParameter("idBillingPeriod", idBillingPeriod);
	    query.setParameter("idCoreFacility", idCoreFacility);
	    List results = query.list();

	    for (Iterator i = results.iterator(); i.hasNext();) {
	    	Object[] row = (Object[])i.next();
	    	ProductLineItem pli     = (ProductLineItem)row[0];
	    	ProductOrder po         = (ProductOrder)row[1];
	    	BillingItem bi          =  (BillingItem)row[2];

	    	// Exclude any disk usage that have
	    	// pending billing items.  (shouldn't be any)
	    	if (determineContainsPendingItems(po.getBillingItemList(sess), idBillingPeriod, idBillingAccount)) {
	    		continue;
	    	}

	    	String productOrderNumber = PRODUCT_ORDER_NUMBER_PREFIX + po.getProductOrderNumber().toString();
	    	requestMap.put(productOrderNumber, po);

	    	List billingItems = (List)billingItemMap.get(productOrderNumber);
	    	if (billingItems == null) {
	    		billingItems = new ArrayList();
	    		billingItemMap.put(productOrderNumber, billingItems);
	    	}
	    	billingItems.add(bi);
	    }
	}
	
	private static boolean determineContainsPendingItems(Set set, Integer idBillingPeriod, Integer idBillingAccount) {
		for (Iterator iter = set.iterator(); iter.hasNext();) {
    		BillingItem item = (BillingItem)iter.next();

    		if (item.getIdBillingPeriod().equals(idBillingPeriod) &&
    			item.getIdBillingAccount().equals(idBillingAccount)) {
    				if (item.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
    					return true;
    				}          
    		}
    	}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public HttpServletRequest setRequestState(HttpServletRequest request) {
		request.setAttribute("tray", this.tray);
	    return request;
	}

	/**
	  *  The callback method called after the loadCommand, and execute methods,
	  *  this method allows you to manipulate the HttpServletResponse object prior
	  *  to forwarding to the result JSP (add a cookie, etc.)
	  *
	  *@param  request  The HttpServletResponse for the command
	  *@return          The processed response
	  */
	@Override
	public HttpServletResponse setResponseState(HttpServletResponse response) {
		return response;
	}

	@Override
	public HttpSession setSessionState(HttpSession session) {
		return session;
	}
	
	/* (non-Javadoc)
	 * @see hci.report.utility.ReportCommand#loadContextPermissions()
	 */
	@Override
	public void loadContextPermissions() {
	}	

}
