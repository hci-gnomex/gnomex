package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountSplitParser;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SplitBillingAccounts extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SplitBillingAccounts.class);

  private Integer                      idBillingPeriod; 
  private String                       accountXMLString;
  private Document                     accountDoc;
  private BillingAccountSplitParser    parser;
  private String                       totalPriceString;
  private String                       splitType;
  private String                       serverName;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }
    if (request.getParameter("totalPrice") != null && !request.getParameter("totalPrice").equals("")) {
      totalPriceString = request.getParameter("totalPrice");
      totalPriceString = totalPriceString.replaceAll("\\$","").replaceAll(",","");
    } else {
      this.addInvalidField("totalPrice", "totalPrice is required");
    }
    if (request.getParameter("splitType") != null && !request.getParameter("splitType").equals("")) {
      splitType = request.getParameter("splitType");
    } else {
      this.addInvalidField("splitType", "splitType is required");
    }

    if (request.getParameter("accountXMLString") != null && !request.getParameter("accountXMLString").equals("")) {
      accountXMLString = request.getParameter("accountXMLString");

      StringReader reader = new StringReader(accountXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        accountDoc = sax.build(reader);
        parser = new BillingAccountSplitParser(accountDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse accountXMLString", je );
        this.addInvalidField( "accountXMLString", "Invalid xml");
      }
    }

    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {

    if (accountXMLString != null) {
      try {
        BigDecimal totalPrice = new BigDecimal(totalPriceString);
        Session sess = HibernateSession.currentSession(this.getUsername());

        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
          parser.parse(sess);

          // Set up hashmap to keep track of which billing items are still around.
          HashMap<Integer, Boolean> allBi = new HashMap<Integer, Boolean>();
          for(Iterator i1 = parser.getRequest().getBillingItemList(sess).iterator(); i1.hasNext();) {
            BillingItem bi = (BillingItem)i1.next();
            // Only update percentages for billing items for the given billing period.
            if (!bi.getIdBillingPeriod().equals(idBillingPeriod)) {
              continue;
            }

            allBi.put(bi.getIdBillingItem(), false);
          }

          for(Iterator i = parser.getBillingAccounts().iterator(); i.hasNext();) {
            BillingAccount ba = (BillingAccount)i.next();
            if (!ba.getIdCoreFacility().equals(parser.getRequest().getIdCoreFacility())) {
              throw new Exception("Cannot split billing item -- Billing Account " + ba.getIdBillingAccount().toString() + " has different core facility than request " + parser.getRequest().getIdRequest().toString());
            }
            BigDecimal percentage = parser.getPercentage(ba.getIdBillingAccount());
            BigDecimal invoicePrice = parser.getInvoicePrice(ba.getIdBillingAccount());
            BigDecimal summedInvoicePrice = new BigDecimal(0);
            BillingItem itemToAdjust = null;

            boolean found = false;
            // For billing account, find all matching billing items for the request and
            // change the percentage.
            for(Iterator i1 = parser.getRequest().getBillingItemList(sess).iterator(); i1.hasNext();) {
              BillingItem bi = (BillingItem)i1.next();
              // Only update percentages for billing items for the given billing period.
              if (!bi.getIdBillingPeriod().equals(idBillingPeriod)) {
                continue;
              }

              if (bi.getIdBillingAccount().equals(ba.getIdBillingAccount())) {
                allBi.put(bi.getIdBillingItem(), true);
                bi.setPercentagePrice(percentage);
                bi.setSplitType(splitType);
                if (bi.getQty() == null) {
                  throw new Exception("Cannot split billing item " + bi.getDescription() + " because qty is blank.");
                }
                if (bi.getUnitPrice() == null) {
                  throw new Exception("Cannot split billing item " + bi.getDescription() + " because unit price is blank.");
                }
                if (bi.getQty().intValue() > 0 && bi.getUnitPrice() != null) {
                  bi.setInvoicePrice(getComputedInvoicePrice(bi, percentage, invoicePrice, totalPrice));
                }
                summedInvoicePrice = summedInvoicePrice.add(bi.getInvoicePrice());
                itemToAdjust = bi;
                found = true;
              }

            }
            // If we didn't find any billing items for this account, clone the billing
            // items and assign to the billing account.
            if (!found) {
              HashSet<String> alreadyClonedTags = new HashSet<String>();
              
              for(Iterator i1 = parser.getRequest().getBillingItemList(sess).iterator(); i1.hasNext();) {
                BillingItem bi = (BillingItem)i1.next();

                // Only clone billing items for the given billing period.
                if (!bi.getIdBillingPeriod().equals(idBillingPeriod)) {
                  continue;
                }

                BillingItem billingItem = new BillingItem();
                billingItem.setIdBillingAccount(ba.getIdBillingAccount());
                billingItem.setIdLab(ba.getIdLab());
                
                // Only clone non-duplicate billing items
                if (bi.getTag() != null) {
                	if (alreadyClonedTags.contains(bi.getTag())) {
                		continue;
                	}
                	
                	billingItem.setTag(bi.getTag());
                	alreadyClonedTags.add(bi.getTag());
                } else {
                	String newTag = getUniqueBillingItemTag(sess, alreadyClonedTags);
                	bi.setTag(newTag);
                	billingItem.setTag(newTag);
                	alreadyClonedTags.add(newTag);
                	
                	sess.save(bi);
                }

                billingItem.setCodeBillingChargeKind(bi.getCodeBillingChargeKind());
                billingItem.setIdBillingPeriod(bi.getIdBillingPeriod());
                billingItem.setDescription(bi.getDescription());
                billingItem.setQty(bi.getQty());
                billingItem.setIdCoreFacility(bi.getIdCoreFacility());
                billingItem.setUnitPrice(bi.getUnitPrice());
                billingItem.setPercentagePrice(percentage);
                billingItem.setNotes(bi.getNotes());
                if (bi.getQty().intValue() > 0 && bi.getUnitPrice() != null) {
                  billingItem.setInvoicePrice(getComputedInvoicePrice(billingItem, percentage, invoicePrice, totalPrice));
                  summedInvoicePrice = summedInvoicePrice.add(billingItem.getInvoicePrice());
                }
                billingItem.setCodeBillingStatus(bi.getCodeBillingStatus());
                if (bi.getCompleteDate() != null) {
                  billingItem.setCompleteDate(bi.getCompleteDate());
                }
                billingItem.setIdRequest(parser.getRequest().getIdRequest());
                billingItem.setIdPrice(bi.getIdPrice());
                billingItem.setIdPriceCategory(bi.getIdPriceCategory());
                billingItem.setCategory(bi.getCategory());
                billingItem.setTotalPrice(bi.getUnitPrice().multiply(new BigDecimal(bi.getQty().intValue())));
                billingItem.setSplitType(splitType);

                sess.save(billingItem);
                itemToAdjust = billingItem;
                allBi.put(billingItem.getIdBillingItem(), true);
              }
            }

            if (itemToAdjust != null && !summedInvoicePrice.equals(invoicePrice) && itemToAdjust.getInvoicePrice() != null) {
              BigDecimal ip = itemToAdjust.getInvoicePrice();
              ip = ip.add(invoicePrice.subtract(summedInvoicePrice));
              itemToAdjust.setInvoicePrice(ip);
            }
          }

          // Remove any billing items no longer in the list
          for (Integer key:allBi.keySet()) {
            Boolean inList = allBi.get(key);
            if (!inList) {
              BillingItem toDelete = (BillingItem)sess.load(BillingItem.class, key);
              sess.delete(toDelete);
            }
          }

          //send invoice email to labs
          for(Iterator i = parser.getBillingAccounts().iterator(); i.hasNext();) {
            BillingAccount ba = (BillingAccount)i.next();
            Lab lab = (Lab)sess.load(Lab.class, ba.getIdLab());
            CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, ba.getIdCoreFacility());
            BillingPeriod bp = (BillingPeriod)sess.load(BillingPeriod.class, idBillingPeriod);
            TreeMap billingItemMap = new TreeMap();
            TreeMap relatedBillingItemMap = new TreeMap();
            TreeMap requestMap = new TreeMap();
            Boolean allItemsApproved = true;
            ShowBillingInvoiceForm.cacheBillingItemMaps(sess, this.getSecAdvisor(), idBillingPeriod, lab.getIdLab(), ba.getIdBillingAccount(), ba.getIdCoreFacility(), billingItemMap, relatedBillingItemMap, requestMap);
            for(Iterator j = parser.getRequest().getBillingItemList(sess).iterator(); j.hasNext();){
              BillingItem bi = (BillingItem)j.next();
              if(!bi.getCodeBillingStatus().equals(BillingStatus.APPROVED) && !bi.getCodeBillingStatus().equals(BillingStatus.APPROVED_PO) && !bi.getCodeBillingStatus().equals(BillingStatus.APPROVED_CC))
                allItemsApproved = false;
              break;
            }

            if(allItemsApproved){
              this.sendInvoiceEmail(sess, lab.getBillingNotificationEmail(), cf, bp, lab, ba, billingItemMap, relatedBillingItemMap, requestMap);
            }
          }



          sess.flush();


          this.xmlResult = "<SUCCESS/>";

          setResponsePage(this.SUCCESS_JSP);          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage billing");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        log.error("An exception has occurred in SplitBillingAccounts ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());

      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {

        }
      }

    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }

    return this;
  }
  
  private String getUniqueBillingItemTag(Session sess, HashSet<String> alreadyClonedTags) {
	  String candidate = null;
	  candidateLoop : for (int count = 1; count <= 999999999; count++) {
		  candidate = "" + count;
		  
		  for (Iterator allBiIter = parser.getRequest().getBillingItemList(sess).iterator(); allBiIter.hasNext();) {
			  BillingItem bi = (BillingItem) allBiIter.next();
			  if (bi.getTag() != null) {
				  if (candidate.equalsIgnoreCase(bi.getTag())) {
					  continue candidateLoop;
				  }
			  }
		  }
		  
		  if (alreadyClonedTags.contains(candidate)) {
			  continue candidateLoop;
		  }
		  
		  break;
	  }
	  
	  return candidate;
  }

  private void sendInvoiceEmail(Session sess, String contactEmail, CoreFacility coreFacility,
      BillingPeriod billingPeriod, Lab lab,
      BillingAccount billingAccount, Map billingItemMap, Map relatedBillingItemMap,
      Map requestMap) throws Exception {

    DictionaryHelper dh = DictionaryHelper.getInstance(sess);

    String queryString="from Invoice where idCoreFacility=:idCoreFacility and idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount";
    Query query = sess.createQuery(queryString);
    query.setParameter("idCoreFacility", coreFacility.getIdCoreFacility());
    query.setParameter("idBillingPeriod", idBillingPeriod);
    query.setParameter("idBillingAccount", billingAccount.getIdBillingAccount());
    Invoice invoice = (Invoice)query.uniqueResult();
    BillingInvoiceEmailFormatter emailFormatter = new BillingInvoiceEmailFormatter(sess, coreFacility,
        billingPeriod, lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap);
    String subject = emailFormatter.getSubject();
    String body = emailFormatter.format();

    String note = "";
    String emailRecipients = contactEmail;
    String ccList = emailFormatter.getCCList(sess);
    String fromAddress = coreFacility.getContactEmail();
    if(!MailUtil.isValidEmail(emailRecipients)){
      log.error("Invalid email " + emailRecipients);
    }
    boolean send = false;
    if (contactEmail != null && !contactEmail.equals("")) {
      send = true;    
    } else {
      note = "Unable to email billing invoice. Billing contact email is blank for " + lab.getName(false, true);
    }

    if (send) {
      if(!MailUtil.isValidEmail(fromAddress)){
        fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      try {
    	MailUtilHelper helper = new MailUtilHelper(	
    			emailRecipients,
    			ccList,
    			null,
    			fromAddress,
    			subject,
    			body,
    			null,
				true, 
				dh,
				serverName 		);
    	MailUtil.validateAndSendEmail(helper);

        note = "Billing invoice emailed to " + contactEmail + ".";

        // Set last email date
        if (invoice != null) {
          invoice.setLastEmailDate(new java.sql.Date(System.currentTimeMillis()));
          sess.save(invoice);
          sess.flush();
        }
      } catch( Exception e) {
        log.error("Unable to send invoice email to " + contactEmail, e);
        note = "Unable to email invoice to " + contactEmail + " due to the following error: " + e.toString();        
      } 
    }
  }  


  private BigDecimal getComputedInvoicePrice(BillingItem bi, BigDecimal percentage, BigDecimal invoicePrice, BigDecimal totalPrice) {
    BigDecimal newInvoicePrice = bi.getInvoicePrice();
    if (splitType.equals(Constants.BILLING_SPLIT_TYPE_PERCENT_CODE)) {
      newInvoicePrice = bi.getUnitPrice().multiply(percentage.multiply(new BigDecimal(bi.getQty().intValue())));
    } else if (!invoicePrice.equals(totalPrice)) {
      double ip = (bi.getUnitPrice().doubleValue() * bi.getQty().doubleValue()) * (invoicePrice.doubleValue() / totalPrice.doubleValue());
      long ipLong = Math.round(ip * 100.0);
      newInvoicePrice = new BigDecimal(ipLong);
      newInvoicePrice = newInvoicePrice.movePointLeft(2);
    }
    return newInvoicePrice;
  }
}