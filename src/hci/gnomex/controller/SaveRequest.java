package hci.gnomex.controller;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.billing.BillingPlugin;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.BillingTemplateItem;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.ExperimentCollaborator;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Label;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.LabelingReactionSize;
import hci.gnomex.model.MasterBillingItem;
import hci.gnomex.model.Notification;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyPlatformApplication;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.ReactionType;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryType;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Slide;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.Step;
import hci.gnomex.model.TransferLog;
import hci.gnomex.model.TreatmentEntry;
import hci.gnomex.model.Visibility;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptorUploadParser;
import hci.gnomex.utility.FreeMarkerConfiguration;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.HybNumberComparator;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.ProductException;
import hci.gnomex.utility.ProductUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.PropertyEntryComparator;
import hci.gnomex.utility.PropertyOptionComparator;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.RequestParser;
import hci.gnomex.utility.RequestParser.HybInfo;
import hci.gnomex.utility.SampleAssaysParser;
import hci.gnomex.utility.SampleNumberComparator;
import hci.gnomex.utility.SamplePrimersParser;
import hci.gnomex.utility.SequenceLaneNumberComparator;
import hci.gnomex.utility.WorkItemHybParser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class SaveRequest extends GNomExCommand implements Serializable {

	// the static field for logging in Log4J
	private static Logger LOG = Logger.getLogger(SaveRequest.class);

	private String requestXMLString;
	private String description;
	private Document requestDoc;
	private RequestParser requestParser;

	private String filesToRemoveXMLString;
	private Document filesToRemoveDoc;
	private FileDescriptorUploadParser filesToRemoveParser;

	private String propertiesXML;

	private BillingPeriod billingPeriod;

	private String launchAppURL;
	private String appURL;
	private String serverName;

	private String originalRequestNumber;
	private Integer nextSampleNumber;
	private boolean hasNewSample;

	private Map labelMap = new HashMap();
	private Map idSampleMap = new HashMap();
	private TreeSet hybs = new TreeSet(new HybNumberComparator());
	private TreeSet samples = new TreeSet(new SampleNumberComparator());
	private TreeSet sequenceLanes = new TreeSet(new SequenceLaneNumberComparator());

	private TreeSet hybsAdded = new TreeSet(new HybNumberComparator());
	private TreeSet samplesAdded = new TreeSet(new SampleNumberComparator());
	private TreeSet labeledSamplesAdded = new TreeSet(new LabeledSampleComparator());
	private TreeSet sequenceLanesAdded = new TreeSet(new SequenceLaneNumberComparator());

	private TreeSet samplesDeleted = new TreeSet(new SampleNumberComparator());
	private TreeSet sequenceLanesDeleted = new TreeSet(new SequenceLaneNumberComparator());
	private TreeSet hybsDeleted = new TreeSet(new HybNumberComparator());

	private Map channel1SampleMap = new HashMap();
	private Map channel2SampleMap = new HashMap();

	private Integer idLabelingProtocolDefault;
	private Integer idHybProtocolDefault;
	private Integer idScanProtocolDefault;
	private Integer idFeatureExtractionProtocolDefault;

	private String invoicePrice;

	private Map<String, Plate> storePlateMap = new HashMap<String, Plate>();

	private SampleAssaysParser assaysParser;
	private SamplePrimersParser primersParser;

	private Plate assayPlate;
	private Plate primerPlate;
	private Map<String, Plate> cherrySourcePlateMap = new HashMap<String, Plate>();
	private Plate cherryPickDestinationPlate;

	private Integer sampleCountOnPlate;
	private Integer previousCapSeqPlateId = null;
	private Integer previousIScanPlateId = null;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {

		if (request.getParameter("requestXMLString") != null && !request.getParameter("requestXMLString").equals("")) {
			requestXMLString = request.getParameter("requestXMLString");
		}

		if (request.getParameter("description") != null) {
			description = request.getParameter("description");
		}

		if (request.getParameter("filesToRemoveXMLString") != null && !request.getParameter("filesToRemoveXMLString").equals("")) {
			filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString") + "</FilesToRemove>";

			StringReader reader = new StringReader(filesToRemoveXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				filesToRemoveDoc = sax.build(reader);
				filesToRemoveParser = new FileDescriptorUploadParser(filesToRemoveDoc);
			} catch (JDOMException je) {
				LOG.error("Cannot parse filesToRemoveXMLString", je);
				this.addInvalidField("FilesToRemoveXMLString", "Invalid filesToRemove xml");
			}
		}

		if (request.getParameter("propertiesXML") != null && !request.getParameter("propertiesXML").equals("")) {
			propertiesXML = request.getParameter("propertiesXML");
		}

		invoicePrice = "";
		if (request.getParameter("invoicePrice") != null && request.getParameter("invoicePrice").length() > 0) {
			// If total price present it means price exceeded $500.00 so we want to
			// send an advisory email
			invoicePrice = request.getParameter("invoicePrice");
		}

		StringReader reader = new StringReader(requestXMLString);
		try {
			SAXBuilder sax = new SAXBuilder();
			requestDoc = sax.build(reader);
			requestParser = new RequestParser(requestDoc, this.getSecAdvisor());
		} catch (JDOMException je) {
			LOG.error("Cannot parse requestXMLString", je);
			this.addInvalidField("RequestXMLString", "Invalid request xml");
		}

		if (request.getParameter("idProject") != null && !request.getParameter("idProject").equals("")) {
			new Integer(request.getParameter("idProject"));
		}

		try {
			launchAppURL = this.getLaunchAppURL(request);
			this.getShowRequestFormURL(request);
			appURL = this.getAppURL(request);
		} catch (Exception e) {
			LOG.warn("Cannot get launch app URL in SaveRequest", e);
		}

		serverName = request.getServerName();

		if (request.getParameter("assaysXMLString") != null && !request.getParameter("assaysXMLString").equals("")) {
			String assaysXMLString = "<assays>" + request.getParameter("assaysXMLString") + "</assays>";
			reader = new StringReader(assaysXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				Document assaysDoc = sax.build(reader);
				assaysParser = new SampleAssaysParser(assaysDoc);
			} catch (JDOMException je) {
				LOG.error("Cannot parse assays", je);
				this.addInvalidField("Assays", "Invalid assays xml");
			}
		}

		if (request.getParameter("primersXMLString") != null && !request.getParameter("primersXMLString").equals("")) {
			String assaysXMLString = "<primers>" + request.getParameter("primersXMLString") + "</primers>";
			reader = new StringReader(assaysXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				Document primersDoc = sax.build(reader);
				primersParser = new SamplePrimersParser(primersDoc);
			} catch (JDOMException je) {
				LOG.error("Cannot parse primers", je);
				this.addInvalidField("Primers", "Invalid primers xml");
			}
		}

		if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)
				&& !this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES)) {
			LOG.error("Insufficient permissions to submit requests for " + this.getSecAdvisor().getUserFirstName() + " "
					+ this.getSecAdvisor().getUserLastName());
			this.addInvalidField("PermissionError", "Insufficient permissions to submit request");
		}

	}

	public Command execute() throws GNomExRollbackException {

		Session sess = null;
		String billingAccountMessage = "";

		try {
			sess = HibernateSession.currentSession(this.getUsername());
			DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
			PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

			requestParser.parse(sess);

			if (this.isValid()) {
				// Get the current billing period
				billingPeriod = dictionaryHelper.getCurrentBillingPeriod();
				if (billingPeriod == null && requestXMLString.contains("isExternal=\"N\"")) {
					throw new Exception("Cannot find current billing period to create billing items");
				}

				Lab lab = sess.load(Lab.class, requestParser.getRequest().getIdLab());
				if (!lab.validateVisibilityInLab(requestParser.getRequest())) {
					this.addInvalidField("Institution", "You must choose an institution when visibility is set to Institute");
				}

				// The following code makes sure any ccNumbers that have been entered
				// actually exist
				PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
				if (propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED) != null
						&& propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED).equals("Y")) {
					validateCCNumbers();
				}

				if (requestParser.isNewRequest()) {
					Lab l = sess.load(Lab.class, requestParser.getRequest().getIdLab());
					if (!this.getSecAdvisor().isGroupIAmMemberOrManagerOf(requestParser.getRequest().getIdLab()) && !this.getSecAdvisor().isLabICanSubmitTo(l)
							&& !this.getSecAdvisor().isGroupICollaborateWith(l.getIdLab())) {
						this.addInvalidField("PermissionLab", "Insufficient permissions to submit the request for this lab.");
					}
				} else {
					if (!this.getSecAdvisor().canUpdate(requestParser.getRequest())) {
						this.addInvalidField("PermissionAddRequest", "Insufficient permissions to edit the request.");
					}
				}

				// If the default visibility is Institute level, make sure that the institution set for the
				// Request is an institution the lab is associated with. If not, set the default visibility
				// to Member level.
				if (requestParser.isNewRequest()) {
					boolean foundInstitution = false;
					if (requestParser.getRequest().getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
						for (Institution inst : (Set<Institution>) lab.getInstitutions()) {
							if (requestParser.getRequest().getIdInstitution() != null
									&& requestParser.getRequest().getIdInstitution().equals(inst.getIdInstitution())) {
								foundInstitution = true;
								break;
							}
						}
						if (!foundInstitution) {
							requestParser.getRequest().setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
							requestParser.getRequest().setIdInstitution(null);
						}
					}
				}

				if (this.isValid()) {
					List labels = sess.createQuery("SELECT label from Label label").list();
					for (Iterator i = labels.iterator(); i.hasNext();) {
						Label l = (Label) i.next();
						labelMap.put(l.getLabel(), l.getIdLabel());
					}

					// save request
					originalRequestNumber = saveRequest(sess, requestParser, description);
					sendNotification(requestParser.getRequest(), sess, requestParser.isNewRequest() ? Notification.NEW_STATE : Notification.EXISTING_STATE,
							Notification.SOURCE_TYPE_ADMIN, Notification.TYPE_REQUEST);
					sendNotification(requestParser.getRequest(), sess, requestParser.isNewRequest() ? Notification.NEW_STATE : Notification.EXISTING_STATE,
							Notification.SOURCE_TYPE_USER, Notification.TYPE_REQUEST);

					// If this request uses products, create ledger entries
					if (ProductUtil.updateLedgerOnRequestStatusChange(sess, requestParser.getRequest(), requestParser.getPreviousCodeRequestStatus(),
							requestParser.getRequest().getCodeRequestStatus())) {
						sess.flush();
					}

					// Save billing template
					BillingTemplate billingTemplate = requestParser.getBillingTemplate();
					BillingTemplate oldBillingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, requestParser.getRequest());

					// If the request has any approved billing items, prevent billing template modification
					boolean allowBillingTemplateModification = true;
					if (billingTemplate != null) {
						Set<BillingItem> oldBillingItems = billingTemplate.getBillingItems(sess);
						for (BillingItem billingItem : oldBillingItems) {
							if (!billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)
									&& !billingItem.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
								billingAccountMessage = "Billing was not adjusted because approved billing items cannot be reassigned.";
								allowBillingTemplateModification = false;
								break;
							}
						}
					}
					if (oldBillingTemplate != null) {
						Set<BillingItem> oldBillingItems2 = oldBillingTemplate.getBillingItems(sess);
						for (BillingItem billingItem : oldBillingItems2) {
							if (!billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)
									&& !billingItem.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
								billingAccountMessage = "Billing was not adjusted because approved billing items cannot be reassigned.";
								allowBillingTemplateModification = false;
								break;
							}
						}
					}

					if (allowBillingTemplateModification && billingTemplate != null) {

						if (requestParser.isNewRequest()) {
							billingTemplate.setOrder(requestParser.getRequest());
						}
						sess.save(billingTemplate);
						sess.flush();

						Map<Integer, List<Object>> infoForRecreatingBillingItems;
						if (oldBillingTemplate != null && !requestParser.isNewRequest() && requestParser.isReassignBillingAccount()) {
							infoForRecreatingBillingItems = BillingTemplate.retrieveInfoForRecreatingBillingItems(oldBillingTemplate.getAcceptingBalanceItem(),
									oldBillingTemplate.getBillingItems(sess));
						} else {
							infoForRecreatingBillingItems = BillingTemplate.retrieveInfoForRecreatingBillingItems(null, null);
						}

						// Delete old billing template items if any
						Set<BillingTemplateItem> oldBtiSet = new TreeSet<BillingTemplateItem>();
						oldBtiSet.addAll(billingTemplate.getItems());
						for (BillingTemplateItem billingTemplateItemToDelete : oldBtiSet) {
							BillingTemplateItem persistentBTI = sess.load(BillingTemplateItem.class, billingTemplateItemToDelete.getIdBillingTemplateItem());
							sess.delete(persistentBTI);
						}
						sess.flush();
						billingTemplate.getItems().clear();

						if (oldBillingTemplate != null && !requestParser.isNewRequest() && requestParser.isReassignBillingAccount()) {
							// Delete old billing template items if any
							Set<BillingTemplateItem> oldBtiSet2 = new TreeSet<BillingTemplateItem>();
							oldBtiSet2.addAll(oldBillingTemplate.getItems());
							for (BillingTemplateItem billingTemplateItemToDelete : oldBtiSet2) {
								BillingTemplateItem persistentBTI = sess
										.load(BillingTemplateItem.class, billingTemplateItemToDelete.getIdBillingTemplateItem());
								sess.delete(persistentBTI);
							}
							sess.flush();
							oldBillingTemplate.getItems().clear();
						}

						// Save new billing template items
						Set<BillingTemplateItem> btiSet = requestParser.getBillingTemplateItems();
						for (BillingTemplateItem newlyCreatedItem : btiSet) {
							if (newlyCreatedItem.isAcceptingBalance()) {
								requestParser.getRequest().setIdBillingAccount(newlyCreatedItem.getIdBillingAccount());
							}
							newlyCreatedItem.setIdBillingTemplate(billingTemplate.getIdBillingTemplate());
							billingTemplate.getItems().add(newlyCreatedItem);
							sess.save(newlyCreatedItem);
						}
						sess.flush();
						if (!requestParser.isNewRequest() && requestParser.isReassignBillingAccount()) {
							// Transfer master billing items
							if (oldBillingTemplate != null && !billingTemplate.getIdBillingTemplate().equals(oldBillingTemplate.getIdBillingTemplate())) {
								Set<MasterBillingItem> masterBillingItems = new HashSet<MasterBillingItem>();
								masterBillingItems.addAll(oldBillingTemplate.getMasterBillingItems());
								for (MasterBillingItem master : masterBillingItems) {
									master.setIdBillingTemplate(billingTemplate.getIdBillingTemplate());
									billingTemplate.getMasterBillingItems().add(master);
									sess.save(master);
								}
								sess.flush();
								oldBillingTemplate.getMasterBillingItems().clear();
							}

							// Delete existing billing items
							Set<BillingItem> oldBillingItems = billingTemplate.getBillingItems(sess);
							for (BillingItem billingItemToDelete : oldBillingItems) {
								sess.delete(billingItemToDelete);
							}
							sess.flush();

							// Delete existing billing items
							if (oldBillingTemplate != null) {
								Set<BillingItem> oldBillingItems2 = oldBillingTemplate.getBillingItems(sess);
								for (BillingItem billingItemToDelete : oldBillingItems2) {
									sess.delete(billingItemToDelete);
								}
								sess.flush();
							}

							// Save new billing items
							Set<BillingItem> newBillingItems = billingTemplate.recreateBillingItems(sess, infoForRecreatingBillingItems);
							for (BillingItem newlyCreatedBillingItem : newBillingItems) {
								sess.save(newlyCreatedBillingItem);
							}
							sess.flush();

							if (oldBillingTemplate != null && !billingTemplate.getIdBillingTemplate().equals(oldBillingTemplate.getIdBillingTemplate())) {
								BillingTemplate persistentBT = sess.load(BillingTemplate.class, oldBillingTemplate.getIdBillingTemplate());
								oldBillingTemplate = null;
								sess.delete(persistentBT);
								sess.flush();
							}
						}

						requestParser.getRequest().setIdBillingAccount(billingTemplate.getAcceptingBalanceItem().getIdBillingAccount());
					}

					if (!allowBillingTemplateModification) {
						billingTemplate = oldBillingTemplate;
					}

					// Remove files from file system
					if (filesToRemoveParser != null) {
						for (Iterator i = filesToRemoveParser.parseFilesToRemove().iterator(); i.hasNext();) {
							String fileName = (String) i.next();
							File f = new File(fileName);

							// Remove references of file in TransferLog
							String queryBuf = "SELECT tl from TransferLog tl where tl.idRequest = " + requestParser.getRequest().getIdRequest()
									+ " AND tl.fileName like '%" + new File(fileName).getName() + "'";
							List transferLogs = sess.createQuery(queryBuf).list();
							// Go ahead and delete the transfer log if there is just one row.
							// If there are multiple transfer log rows for this filename, just
							// bypass deleting the transfer log since it is not possible
							// to tell which entry should be deleted.
							if (transferLogs.size() == 1) {
								TransferLog transferLog = (TransferLog) transferLogs.get(0);
								sess.delete(transferLog);
							}

							if (f.isDirectory()) {
								deleteDir(f, fileName);
							}

							if (f.exists()) {
								boolean success = f.delete();
								if (!success) {
									// File was not successfully deleted
									throw new Exception("Unable to delete file " + fileName);
								}
							}

						}
						sess.flush();
					}

					// Figure out which samples will be deleted
					if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {

						for (Iterator i = requestParser.getRequest().getSamples().iterator(); i.hasNext();) {
							Sample sample = (Sample) i.next();
							boolean found = false;
							for (Iterator i1 = requestParser.getSampleIds().iterator(); i1.hasNext();) {
								String idSampleString = (String) i1.next();
								if (idSampleString != null && !idSampleString.equals("") && !idSampleString.startsWith("Sample")) {
									if (Integer.valueOf(idSampleString).equals(sample.getIdSample())) {
										found = true;
										break;
									}
								}
							}
							if (!found) {
								this.samplesDeleted.add(sample);
							}
						}
					}

					// Save the samples
					saveSamples(sess);
					requestParser.getRequest().setSamples(samples);

					// If we are editing a request, figure out which hybs will be deleted
					if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {

						for (Iterator i = requestParser.getRequest().getHybridizations().iterator(); i.hasNext();) {
							Hybridization hyb = (Hybridization) i.next();
							boolean found = false;
							for (Iterator i1 = requestParser.getHybInfos().iterator(); i1.hasNext();) {
								HybInfo hybInfo = (HybInfo) i1.next();
								if (hybInfo.getIdHybridization() != null && !hybInfo.getIdHybridization().equals("")
										&& !hybInfo.getIdHybridization().startsWith("Hyb")) {
									if (Integer.valueOf(hybInfo.getIdHybridization()).equals(hyb.getIdHybridization())) {
										found = true;
										break;
									}
								}
							}
							if (!found) {
								this.hybsDeleted.add(hyb);
							}
						}
					}
					// Only admins should be deleting hybs
					if (this.hybsDeleted.size() > 0) {
						if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
							throw new RollBackCommandException("Insufficient permission to delete hybs.");
						}
					}

					// Initialize sample channel 1 and 1 map if we are editting a request.
					// This will allow us to keep track of brand new labeled samples
					// vs. existing labeled samples when hybs are added to a request.
					if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {

						for (Iterator i = requestParser.getRequest().getHybridizations().iterator(); i.hasNext();) {
							Hybridization hyb = (Hybridization) i.next();
							if (hyb.getIdLabeledSampleChannel1() != null) {
								this.channel1SampleMap.put(hyb.getIdSampleChannel1(), hyb.getIdLabeledSampleChannel1());
							}
							if (hyb.getIdLabeledSampleChannel2() != null) {
								this.channel2SampleMap.put(hyb.getIdSampleChannel2(), hyb.getIdLabeledSampleChannel2());
							}
						}
					}

					// save hybs
					if (!requestParser.isNewRequest()) {
						requestParser.getRequest().getHybridizations().size();
					}
					if (!requestParser.getHybInfos().isEmpty()) {
						int hybCount = 1;
						int newHybCount = 0;
						for (Iterator i = requestParser.getHybInfos().iterator(); i.hasNext();) {
							RequestParser.HybInfo hybInfo = (RequestParser.HybInfo) i.next();
							boolean isNewHyb = requestParser.isNewRequest() || hybInfo.getIdHybridization() == null
									|| hybInfo.getIdHybridization().startsWith("Hyb");
							if (isNewHyb) {
								newHybCount++;
							}
							saveHyb(hybInfo, sess, hybCount);
							hybCount++;
						}
						if (requestParser.isNewRequest()) {
							requestParser.getRequest().setHybridizations(hybs);
						} else if (newHybCount > 0) {
							requestParser.getRequest().getHybridizations().addAll(hybs);

						}
					}

					// Create Hyb work items if QC->Microarray request
					StringBuffer buf = new StringBuffer();
					if (requestParser.getAmendState().equals(Constants.AMEND_QC_TO_MICROARRAY)) {
						for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
							String idSampleString = (String) i.next();
							boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("")
									|| idSampleString.startsWith("Sample");
							Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);

							// Create work items for labeling step if experiment modified
							if (!requestParser.isExternalExperiment() && !isNewSample) {
								buf = new StringBuffer();
								buf.append("SELECT  ls ");
								buf.append(" from LabeledSample ls ");
								buf.append(" WHERE  ls.idSample =  " + sample.getIdSample());

								List labeledSamples = sess.createQuery(buf.toString()).list();
								for (Iterator i1 = labeledSamples.iterator(); i1.hasNext();) {
									LabeledSample ls = (LabeledSample) i1.next();

									WorkItem wi = new WorkItem();
									wi.setIdRequest(sample.getIdRequest());
									wi.setIdCoreFacility(sample.getRequest().getIdCoreFacility());
									wi.setCodeStepNext(Step.LABELING_STEP);
									wi.setLabeledSample(ls);
									wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));

									sess.save(wi);
								}

							}
						}
					}

					// save sequence lanes
					RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());
					Map existingLanesSaved = saveSequenceLanes(this.getSecAdvisor(), requestParser, sess, requestCategory, idSampleMap, sequenceLanes,
							sequenceLanesAdded);

					// Delete sequence lanes (edit request only)
					ArrayList samplesNotToDelete = new ArrayList();
					if (!requestParser.isAmendRequest()) {
						for (Iterator i = requestParser.getRequest().getSequenceLanes().iterator(); i.hasNext();) {
							SequenceLane lane = (SequenceLane) i.next();
							if (!existingLanesSaved.containsKey(lane.getIdSequenceLane())) {
								boolean canDeleteLane = true;

								if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT) && !requestParser.isExternalExperiment()) {
									this.addInvalidField("deleteLanePermissionError1", "Insufficient permissions to delete sequence lane\n");
									canDeleteLane = false;
								}

								buf = new StringBuffer("SELECT x.idSequenceLane from AnalysisExperimentItem x where x.idSequenceLane = "
										+ lane.getIdSequenceLane());
								List analysis = sess.createQuery(buf.toString()).list();
								if (analysis != null && analysis.size() > 0) {
									canDeleteLane = false;
									this.addInvalidField("deleteLaneError1", "Cannot delete lane " + lane.getNumber()
											+ " because it is associated with existing analysis in GNomEx.  Please sever link before attempting delete\n");

								}
								if (lane.getFlowCellChannel() != null) {
									canDeleteLane = false;
									this.addInvalidField("deleteLaneError2", "Cannot delete lane " + lane.getNumber()
											+ " because it is loaded on a flow cell.  Please delete flow cell channel before attempting delete\n");
								}
								if (lane.getFlowCellChannel() != null) {
									buf = new StringBuffer(
											"SELECT ch.idFlowCellChannel from WorkItem wi join wi.flowCellChannel ch where ch.idFlowCellChannel = "
													+ lane.getIdFlowCellChannel());
									List workItems = sess.createQuery(buf.toString()).list();
									if (workItems != null && workItems.size() > 0) {
										canDeleteLane = false;
										this.addInvalidField(
												"deleteLaneError3",
												"Cannot delete lane "
														+ lane.getNumber()
														+ " because it is loaded on a flow cell that is on the seq run worklist.  Please delete flow cell channel and work item before attempting delete\n");
									}

								}

								if (canDeleteLane) {
									sequenceLanesDeleted.add(lane);
									sess.delete(lane);
								} else {
									/*
									 * If it is a sample we can't delete because of linked data we need to add the idSample back to the list of idSamples and we
									 * need to add the sample to the sample map, this way the samples idRequest won't be set to null in the following code
									 * starting on line 558
									 */
									if (!requestParser.getSampleIds().contains(lane.getIdSample())) {
										Sample s = sess.load(Sample.class, lane.getIdSample());
										samplesNotToDelete.add(s);
										for (Iterator it = samplesDeleted.iterator(); it.hasNext();) {
											Sample sd = (Sample) it.next();
											if (sd.getIdSample() == s.getIdSample()) {
												samplesDeleted.remove(s);
												break;
											}
										}
									}
								}

							}
						}

					}

					// Add the samples we can't delete back to the sample set on the
					// request
					for (Iterator i = samplesNotToDelete.iterator(); i.hasNext();) {
						Sample s = (Sample) i.next();
						requestParser.getRequest().getSamples().add(s);
					}

					// Only admins should be deleting samples unless dna sequencing then
					// based on status.
					if (this.samplesDeleted.size() > 0) {
						if (!this.getSecAdvisor().canDeleteSample(requestParser.getRequest())) {
							this.addInvalidField("deleteSamplePermission", "Only admins can delete samples from the experiment.  Please contact "
									+ propertyHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS) + ".");
							throw new RollBackCommandException("Insufficient permission to delete samples.");
						} else {

							// delete wells for deleted samples
							deleteWellsForDeletedSamples(sess);

							for (Iterator i = samplesDeleted.iterator(); i.hasNext();) {
								Sample s = (Sample) i.next();
								sess.delete(s);
							}

						}
					}

					// Set the seq lib treatments
					Set seqLibTreatments = new TreeSet();
					for (Iterator i = requestParser.getSeqLibTreatmentMap().keySet().iterator(); i.hasNext();) {
						String key = (String) i.next();
						Integer idSeqLibTreatment = Integer.parseInt(key);
						SeqLibTreatment slt = dictionaryHelper.getSeqLibTreatment(idSeqLibTreatment);
						seqLibTreatments.add(slt);
					}
					this.requestParser.getRequest().setSeqLibTreatments(seqLibTreatments);

					//
					// Save properties
					//

					// i need a copy of all of the old values of the property entries before they are changed in the saveRequestProperties call
					// this will be null on saving a new request
					HashMap<Integer, String[]> oldPE = new HashMap<Integer, String[]>();
					if (requestParser.getRequest().getCodeRequestStatus() != null && !requestParser.getRequest().getCodeRequestStatus().equals("NEW")
							&& requestParser.getRequest().getPropertyEntries() != null) {
						for (Iterator i = requestParser.getRequest().getPropertyEntries().iterator(); i.hasNext();) {
							PropertyEntry pe = (PropertyEntry) i.next();
							oldPE.put(pe.getIdPropertyEntry(), new String[] { pe.getValue(), String.valueOf(pe.getProperty().getIdPriceCategory()) });
						}
					}

					Set propertyEntries = this.saveRequestProperties(propertiesXML, sess, requestParser);

					// if it isn't a new request and property entries have been added or removed
					String requestPropertyBillingMessage = "";
					if (requestParser.getRequest().getCodeRequestStatus() != null && !requestParser.getRequest().getCodeRequestStatus().equals("NEW")) {

						for (Iterator i = propertyEntries.iterator(); i.hasNext();) {
							PropertyEntry pe = (PropertyEntry) i.next();
							String[] oldValue = oldPE.get(pe.getIdPropertyEntry());
							// if the old value doesn't match the new value it has changed. Check if this property is associated with a price
							// if it is then warn admin that billing needs to change. If old value is null then a new property was added
							if (oldValue == null || oldValue[0] == null || (oldValue[0] != null && !oldValue[0].equals(pe.getValue()))) {
								Property p = sess.load(Property.class, pe.getIdProperty());
								if (p.getIdPriceCategory() != null && !p.getIdPriceCategory().equals("")) {
									requestPropertyBillingMessage = "The request properties have been changed you will need to update the billing for this request to reflect these changes.";
									break;
								}
							}

							oldPE.remove(pe.getIdPropertyEntry());

						}

						// stuff was deleted so check if those deleted had price categories
						if (requestPropertyBillingMessage.length() == 0 && oldPE.size() > 0) {
							for (Iterator<String[]> i = oldPE.values().iterator(); i.hasNext();) {
								String[] peValues = i.next();
								if (peValues[1] != null && !peValues[1].equals("null") && !peValues[1].equals(""))
									requestPropertyBillingMessage = "The request properties have been changed you will need to update the billing for this request to reflect these changes.";
								break;
							}
						}
					}

					sess.save(requestParser.getRequest());
					sess.flush();

					// Delete any collaborators that were removed
					for (Iterator i1 = requestParser.getRequest().getCollaborators().iterator(); i1.hasNext();) {
						ExperimentCollaborator ec = (ExperimentCollaborator) i1.next();
						if (!requestParser.getCollaboratorUploadMap().containsKey(ec.getIdAppUser())) {
							sess.delete(ec);
						}
					}

					// Add/update collaborators
					for (Iterator i = requestParser.getCollaboratorUpdateMap().keySet().iterator(); i.hasNext();) {
						String key = (String) i.next();
						Integer idAppUser = Integer.parseInt(key);
						String canUploadData = (String) requestParser.getCollaboratorUploadMap().get(key);
						String canUpdate = (String) requestParser.getCollaboratorUpdateMap().get(key);

						// TODO (performance): Would be better if app user was cached.
						ExperimentCollaborator collaborator = (ExperimentCollaborator) sess.createQuery(
								"SELECT ec from ExperimentCollaborator ec where idRequest = " + requestParser.getRequest().getIdRequest() + " and idAppUser = "
										+ idAppUser).uniqueResult();

						// If the collaborator doesn't exist, create it.
						if (collaborator == null) {
							collaborator = new ExperimentCollaborator();
							collaborator.setIdAppUser(idAppUser);
							collaborator.setIdRequest(requestParser.getRequest().getIdRequest());
							collaborator.setCanUploadData(canUploadData);
							collaborator.setCanUpdate(canUpdate);
							sess.save(collaborator);
						} else {
							// If the collaborator does exist, just update the upload permission flag.
							collaborator.setCanUploadData(canUploadData);
							collaborator.setCanUpdate(canUpdate);
						}
					}
					sess.flush();

					// Bump up the revision number on the request if services have been added
					// or services have been removed
					if (!requestParser.isNewRequest()
							&& (requestParser.isAmendRequest() || !samplesAdded.isEmpty() || !labeledSamplesAdded.isEmpty() || !hybsAdded.isEmpty()
									|| !sequenceLanesAdded.isEmpty() || !sequenceLanesDeleted.isEmpty())) {
						originalRequestNumber = requestParser.getRequest().getNumber();
						int revNumber = 1;
						// If services are being added to the request,
						// add a revision number to the end of the request
						String[] tokens = requestParser.getRequest().getNumber().split("R");
						if (tokens.length > 1) {
							if (tokens[1] != null && !tokens[1].equals("")) {
								Integer oldRevNumber = Integer.valueOf(tokens[1]);
								revNumber = oldRevNumber.intValue() + 1;
							}
							originalRequestNumber = tokens[0] + "R";
						}
						requestParser.getRequest().setNumber(originalRequestNumber + revNumber);
						sess.flush();
					}

					// We will create billing items if this is not an external experiment.
					// For new experiments, don't create billing items for DNA Seq Core experiments as these get
					// created when the status is changed to submitted.
					// For existing experiments, create billing items (for new charges) for all experiment
					// types except fragment analysis and mit seq as these are plate based and should not be altered.
					boolean createBillingItems = false;
					if (!requestParser.isExternalExperiment()) {
						if (requestParser.isNewRequest()
								&& !pdh.getCoreFacilityRequestCategoryProperty(requestParser.getRequest().getIdCoreFacility(),
										requestParser.getRequest().getCodeRequestCategory(), PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y")) {
							// if we are to create billing items during workflow we don't want to create them here...
							String prop = propertyHelper.getCoreFacilityRequestCategoryProperty(requestCategory.getIdCoreFacility(),
									requestCategory.getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW);
							if (prop == null || !prop.equals("Y")) {
								createBillingItems = true;
							}
						} else if (!requestParser.isNewRequest()
								&& !requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)
								&& !requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY)) {

							// For dna seq facility orders, warn the admin to adjust billing if samples have been added.
							// (We don't automatically adjust billing items because of tiered pricing issues.)
							if (RequestCategory.isDNASeqCoreRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
								if (requestParser.getRequest().getBillingItemList(sess) != null
										&& !requestParser.getRequest().getBillingItemList(sess).isEmpty()) {
									if (hasNewSample) {
										billingAccountMessage += billingAccountMessage.length() > 0 ? " " : "" + "Request "
												+ requestParser.getRequest().getNumber()
												+ " has been saved.\n\nSamples have been added, please adjust billing accordingly.";
									}
								}
							}
						}
					}
					if (createBillingItems || requestParser.isReassignBillingAccount()) {
						sess.refresh(requestParser.getRequest());

						// Create the billing items
						// We need to include the samples even though they were not added
						// b/c we need to perform lib prep on them.
						if (requestParser.getAmendState().equals(Constants.AMEND_QC_TO_SEQ)) {
							samplesAdded.addAll(requestParser.getRequest().getSamples());
						}

						createBillingItems(sess, requestParser.getRequest(), requestParser.getAmendState(), billingPeriod, dictionaryHelper, samplesAdded,
								labeledSamplesAdded, hybsAdded, sequenceLanesAdded, requestParser.getSampleAssays(), null, BillingStatus.PENDING,
								propertyEntries, billingTemplate, false, false);

						sess.flush();
					} else if (!requestParser.isExternalExperiment()
							&& !pdh.getCoreFacilityRequestCategoryProperty(requestParser.getRequest().getIdCoreFacility(),
									requestParser.getRequest().getCodeRequestCategory(), PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y")) {
						// if not save then submit but bill during workflow, then create the request properties
						createBillingItems(sess, requestParser.getRequest(), requestParser.getAmendState(), billingPeriod, dictionaryHelper, samplesAdded,
								labeledSamplesAdded, hybsAdded, sequenceLanesAdded, requestParser.getSampleAssays(), null, BillingStatus.PENDING,
								propertyEntries, billingTemplate, true, false);
					}

					// If the lab on the request was changed, reassign the lab on the
					// transfer logs for this request
					reassignLabForTransferLog(sess);
					sess.flush();

					// Create file server data directories for request based off of code request category
					if (!requestParser.isExternalExperiment() && RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {

						this.createResultDirectories(
								requestParser.getRequest(),
								"Sample QC",
								PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, requestParser.getRequest().getIdCoreFacility(),
										PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY));
						this.createResultDirectories(
								requestParser.getRequest(),
								"Library QC",
								PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, requestParser.getRequest().getIdCoreFacility(),
										PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY));
					} else if (!requestParser.isExternalExperiment()
							&& (RequestCategory.isMicroarrayRequestCategory(requestParser.getRequest().getCodeRequestCategory()) || requestParser.getRequest()
									.getCodeRequestCategory().equals(RequestCategoryType.TYPE_QC))) {
						this.createResultDirectories(
								requestParser.getRequest(),
								"Sample QC",
								PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, requestParser.getRequest().getIdCoreFacility(),
										PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY));
					}

					String emailErrorMessage = sendEmails(sess);

					this.xmlResult = "<SUCCESS idRequest=\"" + requestParser.getRequest().getIdRequest() + "\" requestNumber=\""
							+ requestParser.getRequest().getNumber() + "\" deleteSampleCount=\"" + this.samplesDeleted.size() + "\" deleteHybCount=\""
							+ this.hybsDeleted.size() + "\" deleteLaneCount=\"" + this.sequenceLanesDeleted.size() + "\" billingAccountMessage = \""
							+ billingAccountMessage + "\" emailErrorMessage = \"" + emailErrorMessage + "\" requestPropertyBillingMessage = \""
							+ requestPropertyBillingMessage + "\"/>";

				}

			}

			if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (GNomExRollbackException e) {
			LOG.error("An exception has occurred in SaveRequest ", e);

			throw e;
		} catch (ProductException e) {
			LOG.error("An exception has occurred in SaveRequest ", e);
			LOG.error("Unable to create ProductLedger for request. " + e.getMessage(), e);

			throw new GNomExRollbackException(e.getMessage(), true, e.getMessage());
		} catch (Exception e) {
			LOG.error("An exception has occurred in SaveRequest ", e);

			throw new GNomExRollbackException(e.getMessage(), true, "An error occurred saving the request.");
		} finally {
			try {

				if (sess != null) {
					//closeHibernateSession;
				}
			} catch (Exception e) {
				LOG.error("An exception has occurred in SaveRequest ", e);
			}
		}

		return this;
	}

	private String sendEmails(Session sess) {

		StringBuffer message = new StringBuffer();
		if (requestParser.isNewRequest() || requestParser.isAmendRequest()) {
			sess.refresh(requestParser.getRequest());
			if (!RequestCategory.isDNASeqCoreRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
				String otherRecipients = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(
						requestParser.getRequest().getIdCoreFacility(), requestParser.getRequest().getCodeRequestCategory(),
						PropertyDictionary.REQUEST_SUBMIT_CONFIRMATION_EMAIL);
				if ((requestParser.getRequest().getAppUser() != null && requestParser.getRequest().getAppUser().getEmail() != null && !requestParser
						.getRequest().getAppUser().getEmail().equals(""))
						|| (otherRecipients != null && otherRecipients.length() > 0)) {
					try {
						// confirmation email for dna seq requests is sent at submit time.
						sendConfirmationEmail(sess, otherRecipients);
					} catch (Exception e) {
						String msg = "Unable to send confirmation email notifying submitter that request " + requestParser.getRequest().getNumber()
								+ " has been submitted.  " + e.toString();
						LOG.error(msg, e);
						message.append(msg + "\n");
					}
				} else {
					String msg = ("Unable to send confirmation email notifying submitter that request " + requestParser.getRequest().getNumber() + " has been submitted.  Request submitter or request submitter email is blank.");
					LOG.error(msg);
					message.append(msg + "\n");
				}
			}
			if (this.invoicePrice.length() > 0) {
				HashSet<String> emails = new HashSet<String>();
				Lab lab = requestParser.getRequest().getLab();
				String billedAccountName = requestParser.getRequest().getBillingAccountName();
				emails.add(lab.getContactEmail());
				// String contactEmail = lab.getContactEmail();
				// String ccEmail = "";
				if (lab.getBillingContactEmail() != null && lab.getBillingContactEmail().length() > 0) {
					emails.add(lab.getBillingContactEmail());
					// ccEmail = lab.getBillingContactEmail() + ", ";
				}
				for (Iterator i1 = lab.getManagers().iterator(); i1.hasNext();) {
					AppUser manager = (AppUser) i1.next();
					if (manager.getIsActive() != null && manager.getIsActive().equalsIgnoreCase("Y")) {
						if (manager.getEmail() != null) {
							// ccEmail = ccEmail + manager.getEmail() + ", ";
							emails.add(manager.getEmail());
						}
					}
				}
				if (emails.size() > 0) {
					try {
						String contactEmail = "";
						String ccEmail = "";
						for (Iterator i = emails.iterator(); i.hasNext();) {
							String address = (String) i.next();
							contactEmail += address;
							if (i.hasNext()) {
								contactEmail += ", ";
							}
						}
						sendInvoicePriceEmail(sess, contactEmail, ccEmail, billedAccountName);
					} catch (Exception e) {
						String msg = "Unable to send estimated charges notification for request " + requestParser.getRequest().getNumber() + "  "
								+ e.toString();
						LOG.error(msg, e);
						message.append(msg + "\n");
					}
				} else {
					String msg = "Unable to send estimated charges notification for request " + requestParser.getRequest().getNumber()
							+ " has been submitted.  Contact or lab manager(s) email is blank.";
					LOG.error(msg);
					message.append(msg + "\n");
				}

			}

			// if this is an adding sequence lanes to existing request send special email to core facility director
			if (requestParser.isAmendRequest()) {
				sendPrintableReqFormToCore(sess);
			}
			// Add to BILLING Notification to table.
			sendNotification(requestParser.getRequest(), sess, Notification.NEW_STATE, Notification.SOURCE_TYPE_BILLING, Notification.TYPE_REQUEST);

		}

		return message.toString();

	}

	private void validateCCNumbers() {
		Session sessGuest = null;
		Connection con = null;

		boolean hasCCNumbers = false;
		List<String> ccNumberList = requestParser.getCcNumberList();
		StringBuffer buf = new StringBuffer("select ccNumber from BST.dbo.Sample WHERE ccNumber in (");
		Iterator<String> itStr = ccNumberList.iterator();
		boolean firstTime = true;
		while (itStr.hasNext()) {
			hasCCNumbers = true;
			String thisKey = itStr.next();
			if (!firstTime)
				buf.append(",");
			else
				firstTime = false;
			buf.append("'" + thisKey + "'");
		}
		buf.append(")");

		if (hasCCNumbers) {
			try {
				Statement stmt = null;
				ResultSet rs = null;

				// Use guest session for validating ccNumbers because it has read permissions on BST
				sessGuest = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

				SessionImpl sessionImpl = (SessionImpl) sessGuest;
				con = sessionImpl.connection();

				stmt = con.createStatement();
				rs = stmt.executeQuery(buf.toString());
				List<String> ccNumbersRetreivedList = new ArrayList<String>();
				while (rs.next()) {
					ccNumbersRetreivedList.add(rs.getString("ccNumber"));
				}
				rs.close();
				stmt.close();

				buf = new StringBuffer();
				// Now check to see if any ccNumbers weren't found
				itStr = ccNumberList.iterator();
				firstTime = true;
				while (itStr.hasNext()) {
					String thisKey = itStr.next();
					if (!ccNumbersRetreivedList.contains(thisKey)) {
						if (!firstTime)
							buf.append(", ");
						else
							firstTime = false;
						buf.append("'" + thisKey + "'");
					}
				}
				if (buf.toString().length() > 0) {
					this.addInvalidField("InvalidCCNumber", "The following CC Numbers do not exist in BST: " + buf.toString()
							+ ".\n\nPlease correct on the Samples tab.");
				}

			} catch (Exception e) {
				LOG.error("An exception has occurred in SaveRequest ", e);
			} finally {
				try {
					if (sessGuest != null) {
						// dont close the session b/c the same one is being used during save request
						// sessGuest.close();
						// //closeReadOnlyHibernateSession;
					}
				} catch (Exception e) {
					LOG.error("An exception has occurred in SaveRequest ", e);
				}
			}

		}

	}

	public static String saveRequest(Session sess, RequestParser requestParser, String description) throws Exception {
		boolean isImport = false;
		return saveRequest(sess, requestParser, description, isImport);
	}

	public static String saveRequest(Session sess, RequestParser requestParser, String description, boolean isImport) throws Exception {

		Request request = requestParser.getRequest();
		request.setDescription(description);
		sess.save(request);

		if (requestParser.isNewRequest() && !isImport) {
			request.setNumber(getNextRequestNumber(requestParser, sess));
			sess.save(request);

			if (request.getName() == null || request.getName().trim().equals("")) {
				sess.flush();
				sess.refresh(request);
				request.setName(request.getAppUser().getShortName() + "-" + request.getNumber());
				sess.save(request);
			}
		}

		sess.flush();

		return requestParser.getRequest().getNumber();
	}

	public static String getNextRequestNumber(RequestParser requestParser, Session sess) throws SQLException {
		String requestNumber = "";

		// do external experiments have their own numbering?
		String isExternal = requestParser.getRequest().getIsExternal();

		PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
		if (propertyHelper.getProperty(PropertyDictionary.USE_EXTERNAL_EXPERIMENT_NUMBERING) != null
				&& propertyHelper.getProperty(PropertyDictionary.USE_EXTERNAL_EXPERIMENT_NUMBERING).equals("Y")
				&& propertyHelper.getProperty(PropertyDictionary.GET_REQUEST_NUMBER_PROCEDURE_EXTERNAL) != null
				&& propertyHelper.getProperty(PropertyDictionary.GET_REQUEST_NUMBER_PROCEDURE_EXTERNAL).length() > 0 && isExternal != null
				&& isExternal.equals("Y")) {
			String procedure = propertyHelper.getProperty(PropertyDictionary.GET_REQUEST_NUMBER_PROCEDURE_EXTERNAL);
			Connection con = ((SessionImpl) sess).connection();

			String queryString = "";
			if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
				queryString = "exec " + procedure;
			} else {
				queryString = "select " + procedure + "();";
			}

			NativeQuery query = sess.createNativeQuery(queryString);
			List l = query.list();
			if (l.size() != 0) {
				Object o = l.get(0);
				if (o.getClass().equals(String.class)) {
					requestNumber = (String) o;
					requestNumber = requestNumber.toUpperCase();
					if (!requestNumber.endsWith("R")) {
						requestNumber = requestNumber + "R";
					}
				}
			}

		} else {
			// See if there is a stored procedure for the core
			String procedure = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(
					requestParser.getRequest().getIdCoreFacility(), requestParser.getRequest().getCodeRequestCategory(),
					PropertyDictionary.GET_REQUEST_NUMBER_PROCEDURE);
			if (procedure != null && procedure.length() > 0) {

				SessionImpl sessionImpl = (SessionImpl) sess;
				Connection con = sessionImpl.connection();

				String queryString = "";
				if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
					queryString = "exec " + procedure;
				} else {
					queryString = "select " + procedure + "();";
				}
				NativeQuery query = sess.createNativeQuery(queryString);
				List l = query.list();
				if (l.size() != 0) {
					Object o = l.get(0);
					if (o.getClass().equals(String.class)) {
						requestNumber = (String) o;
						requestNumber = requestNumber.toUpperCase();
						if (!requestNumber.endsWith("R")) {
							requestNumber = requestNumber + "R";
						}
					}
				}
			}
		}

		if (requestNumber.length() == 0) {
			requestNumber = requestParser.getRequest().getIdRequest().toString() + "R";
		}

		return requestNumber;
	}

	private void saveSamples(Session sess) throws Exception {
		nextSampleNumber = getStartingNextSampleNumber(requestParser);

		// save samples
		sampleCountOnPlate = 1;
		hasNewSample = false;
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);
		RequestCategory requestCategory = dh.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());
		for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
			String idSampleString = (String) i.next();
			boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
			hasNewSample = isNewSample || hasNewSample;
			Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);

			nextSampleNumber = saveSample(sess, requestParser, idSampleString, sample, idSampleMap, samples, samplesAdded, dh.getPropertyMap(),
					nextSampleNumber);

			// Set the barcodeSequence if idOligoBarcodeSequence is filled in
			if (sample.getIdOligoBarcode() != null) {
				sample.setBarcodeSequence(dh.getBarcodeSequence(sample.getIdOligoBarcode()));
			}

			// Set the barcodeSequenceB if idOligoBarcodeB is filled in
			if (sample.getIdOligoBarcodeB() != null) {
				sample.setBarcodeSequenceB(dh.getBarcodeSequence(sample.getIdOligoBarcodeB()));
			}

			// set the qc code application as required
			if (sample.getCodeBioanalyzerChipType() != null) {
				sample.setQcCodeApplication(dh.getBioanalyzerCodeApplication(sample.getCodeBioanalyzerChipType()));
			} else if (sample.getQcCodeApplication() == null) {
				if (requestCategory.isQCRequestCategory()) {
					sample.setQcCodeApplication(requestParser.getRequest().getCodeApplication());
				}
			}

			// handle plates and plate wells for Cap Seq And Sequenom
			updatePlates(sess, requestParser, sample, idSampleString);

			// handle plate and plate wells for fragment analysis, if applicable
			updateFragAnalPlates(sess, sample, idSampleString);

			// handle mitochondrial sequencing wells and plates
			updateMitSeqWells(sess, sample, idSampleString);

			// Cherry pick source and destination wells
			updateCherryPickWells(sess, sample, idSampleString);

			// handle plates and plate wells for iScan
			updateIScanPlates(sess, sample, idSampleString);

			// if this is a new request, create QC work items for each sample
			if (!requestParser.isExternalExperiment()
					&& (RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())
							|| RequestCategory.isQCRequestCategory(requestParser.getRequest().getCodeRequestCategory())
							|| RequestCategory.isMicroarrayRequestCategory(requestParser.getRequest().getCodeRequestCategory()) || RequestCategory
								.isNanoStringRequestCategoryType(requestParser.getRequest().getCodeRequestCategory()))) {
				if ((requestParser.isNewRequest() || isNewSample || requestParser.isQCAmendRequest())) {
					WorkItem workItem = new WorkItem();
					workItem.setIdRequest(requestParser.getRequest().getIdRequest());
					workItem.setIdCoreFacility(requestCategory.getIdCoreFacility());
					if (RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {

						if (requestParser.isQCAmendRequest() && !isNewSample) {
							String codeStepNext = "";
							if (requestCategory.getType().equals(RequestCategoryType.TYPE_HISEQ)) {
								codeStepNext = Step.HISEQ_PREP;
							} else if (requestCategory.getType().equals(RequestCategoryType.TYPE_MISEQ)) {
								codeStepNext = Step.MISEQ_PREP;
							}
							// QC->Solexa request....
							// Place samples on Seq Prep worklist.
							workItem.setCodeStepNext(codeStepNext);
							if (sample.getSeqPrepByCore() != null && sample.getSeqPrepByCore().equalsIgnoreCase("Y")) {
								sample.setQualBypassed("Y");
								sample.setQualDate(new java.sql.Date(System.currentTimeMillis()));
							}
						} else {
							// New request....
							// For Solexa samples to be prepped by core, place on Solexa QC worklist.
							// For samples NOT prepped by core, place on Solexa Seq Prep worklist (where the post Lib prep QC fields
							// will be recorded.
							if (sample.getSeqPrepByCore() != null && sample.getSeqPrepByCore().equalsIgnoreCase("Y")) {
								String codeStepNext = "";
								if (requestCategory.getType().equals(RequestCategoryType.TYPE_HISEQ)) {
									codeStepNext = Step.HISEQ_QC;
								}
								if (requestCategory.getType().equals(RequestCategoryType.TYPE_MISEQ)) {
									codeStepNext = Step.MISEQ_QC;
								}
								workItem.setCodeStepNext(codeStepNext);
							} else {
								String codeStepNext = "";
								if (requestCategory.getType().equals(RequestCategoryType.TYPE_HISEQ)) {
									codeStepNext = Step.HISEQ_PREP_QC;
								} else if (requestCategory.getType().equals(RequestCategoryType.TYPE_MISEQ)) {
									codeStepNext = Step.MISEQ_PREP_QC;
								}
								workItem.setCodeStepNext(codeStepNext);
								sample.setQualBypassed("Y");
								sample.setQualDate(new java.sql.Date(System.currentTimeMillis()));
								sample.setSeqPrepDate(new java.sql.Date(System.currentTimeMillis()));
								sample.setSeqPrepFailed("N");
								sample.setSeqPrepBypassed("N");
								sample.setIdLibPrepPerformedBy(this.getSecAdvisor().getIdAppUser()); //set the lib prep performed id to the user who is submitting the request.
							}
						}

					} else {
						if (requestParser.isNewRequest() || isNewSample) {
							// New Microarray request or new Sample Quality request...
							// Place samples on QC work list
							workItem.setCodeStepNext(Step.QUALITY_CONTROL_STEP);
						}
					}
					if (workItem.getCodeStepNext() != null) {
						workItem.setSample(sample);
						workItem.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
						sess.save(workItem);
					}
				}
			}

			sampleCountOnPlate++;
		}

	}

	public static Integer saveSample(Session sess, RequestParser requestParser, String idSampleString, Sample sample, Map idSampleMap, Set samples,
			Set samplesAdded, Map<Integer, Property> propertyMap, Integer nextSampleNumber) throws Exception {

		boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");

		nextSampleNumber = initSample(sess, requestParser.getRequest(), sample, isNewSample, nextSampleNumber);

		if (requestParser.getSampleAnnotationMap() != null && propertyMap != null) {
			setSampleProperties(sess, requestParser.getRequest(), sample, isNewSample, (Map) requestParser.getSampleAnnotationMap().get(idSampleString),
					requestParser.getOtherCharacteristicLabel(), propertyMap);
			addStandardSampleProperties(sess, requestParser, idSampleString, sample);

			// Delete the existing sample treatments
			if (!isNewSample && sample.getTreatmentEntries() != null) {
				for (Iterator i = sample.getTreatmentEntries().iterator(); i.hasNext();) {
					TreatmentEntry entry = (TreatmentEntry) i.next();
					sess.delete(entry);
				}
			}

			// Add treatment
			String treatment = (String) requestParser.getSampleTreatmentMap().get(idSampleString);
			if (requestParser.getShowTreatments() && treatment != null && !treatment.equals("")) {
				TreatmentEntry entry = new TreatmentEntry();
				entry.setIdSample(sample.getIdSample());
				entry.setTreatment(treatment);
				sess.save(entry);
			}
		}

		sess.flush();

		idSampleMap.put(idSampleString, sample.getIdSample());
		samples.add(sample);

		if (isNewSample) {
			samplesAdded.add(sample);
		}

		return nextSampleNumber;

	}

	public static Integer initSample(Session sess, Request request, Sample sample, Boolean isNewSample, Integer nextSampleNumber) {
		sample.setIdRequest(request.getIdRequest());
		sess.save(sample);

		if (isNewSample) {
			sample.setNumber(Request.getRequestNumberNoR(request.getNumber()) + "X" + nextSampleNumber);
			nextSampleNumber++;
			sess.save(sample);
		}

		return nextSampleNumber;
	}

	public static void setSampleProperties(Session sess, Request request, Sample sample, Boolean isNewSample, Map sampleAnnotations,
			String otherCharacteristicLabel, Map<Integer, Property> idToPropertyMap) {
		setSampleProperties(sess, request, sample, isNewSample, sampleAnnotations, otherCharacteristicLabel, null, idToPropertyMap);
	}

	public static void setSampleProperties(Session sess, Request request, Sample sample, Boolean isNewSample, Map sampleAnnotations,
			String otherCharacteristicLabel, Map propertiesToDelete, Map<Integer, Property> idToPropertyMap) {
		// Delete the existing sample property entries
		if (!isNewSample) {
			for (Iterator i = sample.getPropertyEntries().iterator(); i.hasNext();) {
				PropertyEntry entry = (PropertyEntry) i.next();
				if (propertiesToDelete == null || propertiesToDelete.get(entry.getIdProperty()) != null) {
					for (Iterator i1 = entry.getValues().iterator(); i1.hasNext();) {
						PropertyEntryValue v = (PropertyEntryValue) i1.next();
						sess.delete(v);
					}
					sess.flush();
					entry.setValues(null);
					entry.setOptions(null);
					sess.delete(entry);
				}
			}
		}

		// Create sample property entries and options
		for (Iterator i = sampleAnnotations.keySet().iterator(); i.hasNext();) {

			Integer idProperty = (Integer) i.next();
			String value = (String) sampleAnnotations.get(idProperty);
			if (idProperty == -1) {
				continue;
			}

			Property property = idToPropertyMap.get(idProperty);

			PropertyEntry entry = new PropertyEntry();
			entry.setIdSample(sample.getIdSample());
			if (property.getName().equals("Other")) {
				entry.setOtherLabel(otherCharacteristicLabel);
			}
			entry.setIdProperty(idProperty);
			entry.setValue(value);
			sess.save(entry);
			sess.flush();

			// If the sample property type is "url", save the options. If it is option or multi option, save the options as well
			if (value != null && !value.equals("") && property.getCodePropertyType().equals(PropertyType.URL)) {
				String[] valueTokens = value.split("\\|");
				for (int x = 0; x < valueTokens.length; x++) {
					String v = valueTokens[x];
					PropertyEntryValue urlValue = new PropertyEntryValue();
					urlValue.setValue(v);
					urlValue.setIdPropertyEntry(entry.getIdPropertyEntry());
					sess.save(urlValue);
				}
			} else if (value != null && !value.equals("")
					&& (property.getCodePropertyType().equals(PropertyType.OPTION) || property.getCodePropertyType().equals(PropertyType.MULTI_OPTION))) {
				String[] valueTokens = value.split(",");
				Set<PropertyOption> entryOptions = new TreeSet<PropertyOption>();
				for (int x = 0; x < valueTokens.length; x++) {
					String v = valueTokens[x];
					for (Iterator j = property.getOptions().iterator(); j.hasNext();) {
						PropertyOption option = (PropertyOption) j.next();
						if (option.getOption().equals(v)) {
							entryOptions.add(option);
							break;
						}
					}
				}
				entry.setOptions(entryOptions);
				sess.save(entry);
			}
			sess.flush();

		}
	}

	private void updatePlates(Session sess, RequestParser requestParser, Sample sample, String idSampleString) {
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);
		RequestCategory requestCategory = dh.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());

		if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY)
				|| RequestCategory.isSequenom(requestParser.getRequest().getCodeRequestCategory())) {

			Plate plate = requestParser.getPlate(idSampleString);
			PlateWell well = requestParser.getWell(idSampleString);
			if (plate != null && well != null) {
				// this means it was Plate container type.
				String idAsString = requestParser.getPlateIdAsString(idSampleString);
				Plate realPlate = storePlateMap.get(idAsString);
				if (realPlate == null) {
					realPlate = plate;
					if (plate.getIdPlate() != null) {
						realPlate = sess.load(Plate.class, plate.getIdPlate());
					} else {
						realPlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					}
					realPlate.setLabel(plate.getLabel());
					realPlate.setCodePlateType(PlateType.SOURCE_PLATE_TYPE);
					sess.save(realPlate);
					sess.flush();
					if (this.previousCapSeqPlateId == null || !this.previousCapSeqPlateId.equals(realPlate.getIdPlate())) {
						this.sampleCountOnPlate = 1;
					}
					this.previousCapSeqPlateId = realPlate.getIdPlate();
					this.storePlateMap.put(idAsString, realPlate);
				}
				PlateWell realWell = well;
				if (well.getIdPlateWell() != null) {
					realWell = sess.load(PlateWell.class, well.getIdPlateWell());
				} else {
					realWell.setSample(sample);
					realWell.setIdSample(sample.getIdSample());
					realWell.setPlate(realPlate);
					realWell.setIdPlate(realPlate.getIdPlate());
					realWell.setIdRequest(requestParser.getRequest().getIdRequest());
				}
				realWell.setRow(well.getRow());
				realWell.setCol(well.getCol());
				// We will assume that the samples are being saved
				// in the order they are listed, so set the well position based on
				// to sample count which is incremented as we iterate through the
				// list of samples.
				realWell.setPosition(new Integer(sampleCountOnPlate));
				sess.save(realWell);
				sess.flush();
			} else {
				well = null;
				if (sample.getWells() != null && sample.getWells().size() > 0) {
					// this loop should be unnecessary since there should only be the 1 well with no plate (source well)
					for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
						PlateWell w = (PlateWell) i.next();
						if (w.getIdPlate() == null) {
							well = w;
							break;
						}
					}
				}
				if (well == null) {
					well = new PlateWell();
					well.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					well.setIdSample(sample.getIdSample());
					well.setSample(sample);
					well.setIdRequest(requestParser.getRequest().getIdRequest());
				}
				well.setPosition(new Integer(sampleCountOnPlate));
				sess.save(well);
			}

			sess.flush();
		}
	}

	private void updateFragAnalPlates(Session sess, Sample sample, String idSampleString) throws Exception {
		if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)) {
			if (assaysParser != null) {
				assaysParser.parse(sess);

				if (assayPlate == null) {
					if (requestParser.isNewRequest()) {
						assayPlate = new Plate();
						assayPlate.setCodePlateType(PlateType.SOURCE_PLATE_TYPE);
						assayPlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
						assayPlate.setLabel(requestParser.getPlate(idSampleString).getLabel());
						sess.save(assayPlate);
						sess.flush();
					} else {
						String query = "select p from Plate p where p.codePlateType='" + PlateType.SOURCE_PLATE_TYPE
								+ "' and p.idPlate in (select idPlate from PlateWell where idRequest = " + requestParser.getRequest().getIdRequest() + ")";
						assayPlate = (Plate) sess.createQuery(query).uniqueResult();
					}
				}
				PlateWell parsedWell = requestParser.getWell(idSampleString);
				if (sample.getWells() == null) {
					for (String assayName : requestParser.getAssays(idSampleString)) {
						PlateWell assayWell = new PlateWell();
						assayWell.setCreateDate(new java.util.Date(System.currentTimeMillis()));
						assayWell.setIdAssay(assaysParser.getID(assayName));
						assayWell.setIdPlate(assayPlate.getIdPlate());
						assayWell.setIdSample(sample.getIdSample());
						assayWell.setPlate(assayPlate);
						assayWell.setPosition(new Integer(sampleCountOnPlate));
						assayWell.setCol(parsedWell.getCol());
						assayWell.setRow(parsedWell.getRow());
						assayWell.setSample(sample);
						assayWell.setIdRequest(requestParser.getRequest().getIdRequest());
						sess.save(assayWell);
					}
					sess.flush();
				} else {
					// update any wells for assays that are still around and delete ones that aren't
					ArrayList<PlateWell> wellsFound = new ArrayList<PlateWell>();
					for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
						PlateWell well = (PlateWell) i.next();
						if (well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
							Boolean found = false;
							for (String assayName : requestParser.getAssays(idSampleString)) {
								if (well.getAssay() != null && assayName.equals(well.getAssay().getName())) {
									wellsFound.add(well);
									well.setCol(parsedWell.getCol());
									well.setPosition(new Integer(sampleCountOnPlate));
									well.setRow(parsedWell.getRow());
									sess.save(well);
									found = true;
								}
							}
							if (!found) {
								sess.delete(well);
							}
						}
					}

					// add wells for any new assays for the sample.
					for (String assayName : requestParser.getAssays(idSampleString)) {
						Boolean found = false;
						for (PlateWell well : wellsFound) {
							if (well.getAssay().getName().equals(assayName)) {
								found = true;
							}
						}
						if (!found) {
							PlateWell assayWell = new PlateWell();
							assayWell.setCreateDate(new java.util.Date(System.currentTimeMillis()));
							assayWell.setIdAssay(assaysParser.getID(assayName));
							assayWell.setIdPlate(assayPlate.getIdPlate());
							assayWell.setIdSample(sample.getIdSample());
							assayWell.setPlate(assayPlate);
							assayWell.setPosition(new Integer(sampleCountOnPlate));
							assayWell.setCol(parsedWell.getCol());
							assayWell.setRow(parsedWell.getRow());
							assayWell.setSample(sample);
							assayWell.setIdRequest(requestParser.getRequest().getIdRequest());
							sess.save(assayWell);
						}
					}
					sess.flush();
				}
			}
		}
	}

	private void updateMitSeqWells(Session sess, Sample sample, String idSampleString) throws Exception {
		// create/update plate and plate wells for mitochondrial sequencing, if applicable
		if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY)) {
			primersParser.parse(sess);

			if (primerPlate == null) {
				if (requestParser.isNewRequest()) {
					primerPlate = new Plate();
					primerPlate.setCodePlateType(PlateType.SOURCE_PLATE_TYPE);
					primerPlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					primerPlate.setLabel(requestParser.getPlate(idSampleString).getLabel());
					sess.save(primerPlate);
					sess.flush();
				} else {
					String query = "select p from Plate p where p.codePlateType='" + PlateType.SOURCE_PLATE_TYPE
							+ "' and p.idPlate in (select idPlate from PlateWell where idRequest = " + requestParser.getRequest().getIdRequest() + ")";
					primerPlate = (Plate) sess.createQuery(query).uniqueResult();
				}
			}

			PlateWell parsedWell = requestParser.getWell(idSampleString);
			if (sample.getWells() == null) {
				for (Integer primerNumber = 1; primerNumber < 7; primerNumber++) {
					PlateWell primerWell = new PlateWell();
					primerWell.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					primerWell.setIdPrimer(primersParser.getID(primerNumber));
					primerWell.setIdPlate(primerPlate.getIdPlate());
					primerWell.setIdSample(sample.getIdSample());
					primerWell.setPlate(primerPlate);
					primerWell.setPosition(new Integer(sampleCountOnPlate));
					primerWell.setCol(parsedWell.getCol());
					primerWell.setRow(parsedWell.getRow());
					primerWell.setSample(sample);
					primerWell.setIdRequest(requestParser.getRequest().getIdRequest());
					sess.save(primerWell);
				}
			} else {
				for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
					PlateWell well = (PlateWell) i.next();
					if (well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
						if (!well.getCol().equals(parsedWell.getCol()) || !well.getPosition().equals(new Integer(sampleCountOnPlate))
								|| !well.getRow().equals(parsedWell.getRow())) {
							well.setCol(parsedWell.getCol());
							well.setPosition(new Integer(sampleCountOnPlate));
							well.setRow(parsedWell.getRow());
							sess.save(well);
						}
					}
				}
			}
			sess.flush();
		}
	}

	private void updateCherryPickWells(Session sess, Sample sample, String idSampleString) throws Exception {
		if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY)) {
			String cherryPickSourceWell = requestParser.getCherryPickSourceWell(idSampleString);
			if (cherryPickSourceWell != null && cherryPickSourceWell.length() > 0) {
				String sourcePlateName = requestParser.getCherryPickSourcePlate(idSampleString);
				Plate cherrySourcePlate = cherrySourcePlateMap.get(sourcePlateName);
				if (cherrySourcePlate == null) {
					if (requestParser.isNewRequest()) {
						cherrySourcePlate = new Plate();
						cherrySourcePlate.setCodePlateType(PlateType.SOURCE_PLATE_TYPE);
						cherrySourcePlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
						cherrySourcePlate.setLabel(sourcePlateName);
						sess.save(cherrySourcePlate);
						sess.flush();
					} else {
						String query = "select p from Plate p where p.idPlate in (select idPlate from PlateWell where p.codePlateType='"
								+ PlateType.SOURCE_PLATE_TYPE + "' and p.label = '" + sourcePlateName + "' and idRequest = "
								+ requestParser.getRequest().getIdRequest() + ")";
						cherrySourcePlate = (Plate) sess.createQuery(query).uniqueResult();
					}
					cherrySourcePlateMap.put(sourcePlateName, cherrySourcePlate);
				}
				if (sample.getWells() == null) {
					PlateWell sourceWell = new PlateWell();
					sourceWell.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					sourceWell.setCol(Integer.parseInt(cherryPickSourceWell.substring(1)));
					sourceWell.setRow(cherryPickSourceWell.substring(0, 1));
					sourceWell.setPosition(new Integer(sampleCountOnPlate));
					sourceWell.setIdPlate(cherrySourcePlate.getIdPlate());
					sourceWell.setPlate(cherrySourcePlate);
					sourceWell.setIdSample(sample.getIdSample());
					sourceWell.setSample(sample);
					sourceWell.setIdRequest(requestParser.getRequest().getIdRequest());
					sess.save(sourceWell);
				} else {
					for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
						PlateWell well = (PlateWell) i.next();
						if (well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
							well.setCol(Integer.parseInt(cherryPickSourceWell.substring(1)));
							well.setRow(cherryPickSourceWell.substring(0, 1));
							well.setPosition(new Integer(sampleCountOnPlate));
							well.setIdPlate(cherrySourcePlate.getIdPlate());
							well.setPlate(cherrySourcePlate);
							sess.save(well);
							break;
						}
					}
				}
			}
			String cherryPickDestinationWell = requestParser.getCherryPickDestinationWell(idSampleString);
			if (cherryPickDestinationWell != null && cherryPickDestinationWell.length() > 0) {
				if (cherryPickDestinationPlate == null) {
					if (requestParser.isNewRequest()) {
						cherryPickDestinationPlate = new Plate();
						cherryPickDestinationPlate.setCodePlateType(PlateType.REACTION_PLATE_TYPE);
						cherryPickDestinationPlate.setCodeReactionType(ReactionType.CHERRY_PICKING_REACTION_TYPE);
						cherryPickDestinationPlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
						cherryPickDestinationPlate.setLabel("Cherry Reaction Plate");
						sess.save(cherryPickDestinationPlate);
						sess.flush();
					} else {
						String query = "select p from Plate p where p.idPlate in (select idPlate from PlateWell where p.codePlateType='"
								+ PlateType.REACTION_PLATE_TYPE + "' and idRequest = " + requestParser.getRequest().getIdRequest() + ")";
						cherryPickDestinationPlate = (Plate) sess.createQuery(query).uniqueResult();
					}
				}
				if (sample.getWells() == null) {
					PlateWell destinationWell = new PlateWell();
					destinationWell.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					destinationWell.setCol(Integer.parseInt(cherryPickDestinationWell.substring(1)));
					destinationWell.setRow(cherryPickDestinationWell.substring(0, 1));
					destinationWell.setPosition(new Integer(sampleCountOnPlate));
					destinationWell.setCodeReactionType(ReactionType.CHERRY_PICKING_REACTION_TYPE);
					destinationWell.setIdPlate(cherryPickDestinationPlate.getIdPlate());
					destinationWell.setPlate(cherryPickDestinationPlate);
					destinationWell.setIdSample(sample.getIdSample());
					destinationWell.setSample(sample);
					destinationWell.setIdRequest(requestParser.getRequest().getIdRequest());
					sess.save(destinationWell);
				} else {
					for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
						PlateWell well = (PlateWell) i.next();
						if (well.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
							well.setCol(Integer.parseInt(cherryPickDestinationWell.substring(1)));
							well.setRow(cherryPickDestinationWell.substring(0, 1));
							well.setPosition(new Integer(sampleCountOnPlate));
							sess.save(well);
							break;
						}
					}
				}
			}
		}
	}

	private void updateIScanPlates(Session sess, Sample sample, String idSampleString) {
		if (requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.ISCAN_REQUEST_CATEGORY)) {
			Plate plate = requestParser.getPlate(idSampleString);
			PlateWell well = requestParser.getWell(idSampleString);
			if (plate != null && well != null) {
				// this means it was Plate container type.
				String idAsString = requestParser.getPlateIdAsString(idSampleString);
				Plate realPlate = storePlateMap.get(idAsString);
				if (realPlate == null) {
					realPlate = plate;
					if (plate.getIdPlate() != null) {
						realPlate = sess.load(Plate.class, plate.getIdPlate());
					} else {
						realPlate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					}
					realPlate.setLabel(plate.getLabel());
					realPlate.setCodePlateType(PlateType.SOURCE_PLATE_TYPE);
					sess.save(realPlate);
					sess.flush();
					if (previousIScanPlateId == null || !previousIScanPlateId.equals(realPlate.getIdPlate())) {
						sampleCountOnPlate = 1;
					}
					previousIScanPlateId = realPlate.getIdPlate();
					storePlateMap.put(idAsString, realPlate);
				}
				PlateWell realWell = well;
				if (well.getIdPlateWell() != null) {
					realWell = sess.load(PlateWell.class, well.getIdPlateWell());
				} else {
					realWell.setSample(sample);
					realWell.setIdSample(sample.getIdSample());
					realWell.setPlate(realPlate);
					realWell.setIdPlate(realPlate.getIdPlate());
					realWell.setIdRequest(requestParser.getRequest().getIdRequest());
				}
				realWell.setRow(well.getRow());
				realWell.setCol(well.getCol());
				// We will assume that the samples are being saved
				// in the order they are listed, so set the well position based on
				// to sample count which is incremented as we iterate through the
				// list of samples.
				realWell.setPosition(new Integer(sampleCountOnPlate));
				sess.save(realWell);
				sess.flush();
			} else {
				well = null;
				if (sample.getWells() != null && sample.getWells().size() > 0) {
					// this loop should be unnecessary since there should only be the 1 well with no plate (source well)
					for (Iterator i = sample.getWells().iterator(); i.hasNext();) {
						PlateWell w = (PlateWell) i.next();
						if (w.getIdPlate() == null) {
							well = w;
							break;
						}
					}
				}
				if (well == null) {
					well = new PlateWell();
					well.setCreateDate(new java.util.Date(System.currentTimeMillis()));
					well.setIdSample(sample.getIdSample());
					well.setSample(sample);
					well.setIdRequest(requestParser.getRequest().getIdRequest());
				}
				well.setPosition(new Integer(sampleCountOnPlate));
				sess.save(well);
			}

			sess.flush();
		}
	}

	private void deleteWellsForDeletedSamples(Session sess) {
		if (this.samplesDeleted.size() > 0) {
			// get wells to delete
			ArrayList<Integer> sampleIds = new ArrayList<Integer>();
			for (Iterator i = this.samplesDeleted.iterator(); i.hasNext();) {
				Sample sample = (Sample) i.next();
				sampleIds.add(sample.getIdSample());
			}
			// instrument run check there just to be paranoid. Should never happen because of edit security around statuses.
			String queryString = "SELECT pw from PlateWell pw left join pw.plate p where p.idInstrumentRun is null AND pw.idSample in (:ids) Order By pw.idSample";
			Query query = sess.createQuery(queryString);
			query.setParameterList("ids", sampleIds);
			List wells = query.list();

			// Delete the wells. Save list of plate ids in case we orphan one or more.
			HashMap<Integer, Integer> plateIds = new HashMap<Integer, Integer>();
			for (Iterator i = wells.iterator(); i.hasNext();) {
				PlateWell well = (PlateWell) i.next();
				if (well.getIdPlate() != null) {
					plateIds.put(well.getIdPlate(), well.getIdPlate());
				}
				sess.delete(well);
			}
			sess.flush();

			// delete any orphaned plates
			if (plateIds.keySet().size() > 0) {
				queryString = "select p from Plate p where p.idPlate in (:ids) and p.idPlate not in (select idPlate from PlateWell where idPlate is not null)";
				Query plateQuery = sess.createQuery(queryString);
				plateQuery.setParameterList("ids", plateIds.keySet());
				List plates = plateQuery.list();
				for (Iterator i = plates.iterator(); i.hasNext();) {
					Plate plate = (Plate) i.next();
					sess.delete(plate);
				}
			}
		}
	}

	private void saveHyb(RequestParser.HybInfo hybInfo, Session sess, int hybCount) throws Exception {

		// Figure out the default protocol for the given request category and microarray application.
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT x.idLabelingProtocolDefault, x.idHybProtocolDefault, x.idScanProtocolDefault, x.idFeatureExtractionProtocolDefault ");
		buf.append(" FROM  RequestCategoryApplication x ");
		buf.append(" WHERE x.codeRequestCategory = '" + requestParser.getRequest().getCodeRequestCategory() + "'");
		buf.append(" AND   x.codeApplication = '" + requestParser.getRequest().getCodeApplication() + "'");
		List defaultProtocolIds = sess.createQuery(buf.toString()).list();
		if (defaultProtocolIds.size() > 0) {
			Object[] row = (Object[]) defaultProtocolIds.get(0);
			idLabelingProtocolDefault = (Integer) row[0];
			idHybProtocolDefault = (Integer) row[1];
			idScanProtocolDefault = (Integer) row[2];
			idFeatureExtractionProtocolDefault = (Integer) row[3];
		}

		Hybridization hyb = null;
		boolean isNewHyb = requestParser.isNewRequest() || hybInfo.getIdHybridization() == null || hybInfo.getIdHybridization().startsWith("Hyb");

		if (isNewHyb) {
			hyb = new Hybridization();
			hyb.setCreateDate(new Date(System.currentTimeMillis()));
			hyb.setIdHybProtocol(idHybProtocolDefault);
			hyb.setIdScanProtocol(idScanProtocolDefault);
			hyb.setIdFeatureExtractionProtocol(idFeatureExtractionProtocolDefault);
			isNewHyb = true;
		} else {
			hyb = sess.load(Hybridization.class, new Integer(hybInfo.getIdHybridization()));
		}

		Integer idSampleChannel1Real = null;
		if (hybInfo.getIdSampleChannel1String() != null && !hybInfo.getIdSampleChannel1String().equals("")) {
			idSampleChannel1Real = (Integer) idSampleMap.get(hybInfo.getIdSampleChannel1String());
		}
		Integer idSampleChannel2Real = null;
		if (hybInfo.getIdSampleChannel2String() != null && !hybInfo.getIdSampleChannel2String().equals("")) {
			idSampleChannel2Real = (Integer) idSampleMap.get(hybInfo.getIdSampleChannel2String());
		}

		LabeledSample labeledSampleChannel1 = null;
		LabeledSample labeledSampleChannel2 = null;
		if (isNewHyb) {
			Integer idLabeledSampleChannel1 = (Integer) channel1SampleMap.get(idSampleChannel1Real);

			if (!channel1SampleMap.containsKey(idSampleChannel1Real)) {
				labeledSampleChannel1 = new LabeledSample();
				labeledSampleChannel1.setIdSample(idSampleChannel1Real);
				labeledSampleChannel1.setIdLabel((Integer) labelMap.get("Cy3"));
				labeledSampleChannel1.setIdRequest(requestParser.getRequest().getIdRequest());
				labeledSampleChannel1.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
				labeledSampleChannel1.setNumberOfReactions(new Integer(1));
				labeledSampleChannel1.setIdLabelingProtocol(idLabelingProtocolDefault);
				sess.save(labeledSampleChannel1);

				idLabeledSampleChannel1 = labeledSampleChannel1.getIdLabeledSample();

				channel1SampleMap.put(idSampleChannel1Real, idLabeledSampleChannel1);

				labeledSamplesAdded.add(labeledSampleChannel1);
			}
			hyb.setIdLabeledSampleChannel1(idLabeledSampleChannel1);

			if (idSampleChannel2Real != null) {

				Integer idLabeledSampleChannel2 = (Integer) channel2SampleMap.get(idSampleChannel2Real);

				if (!channel2SampleMap.containsKey(idSampleChannel2Real)) {
					labeledSampleChannel2 = new LabeledSample();
					labeledSampleChannel2.setIdSample(idSampleChannel2Real);
					labeledSampleChannel2.setIdLabel((Integer) labelMap.get("Cy5"));
					labeledSampleChannel2.setIdRequest(requestParser.getRequest().getIdRequest());
					labeledSampleChannel2.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
					labeledSampleChannel2.setNumberOfReactions(new Integer(1));
					labeledSampleChannel2.setIdLabelingProtocol(idLabelingProtocolDefault);

					sess.save(labeledSampleChannel2);

					idLabeledSampleChannel2 = labeledSampleChannel2.getIdLabeledSample();

					channel2SampleMap.put(idSampleChannel2Real, idLabeledSampleChannel2);

					labeledSamplesAdded.add(labeledSampleChannel2);
				}
				hyb.setIdLabeledSampleChannel2(idLabeledSampleChannel2);
			}
		} else {
			boolean changedChannelSample = false;

			// If the sample has changed, for an existing hyb, create a new labeled sample and
			// delete the old one
			if ((hyb.getLabeledSampleChannel1() == null && idSampleChannel1Real != null)
					|| (hyb.getLabeledSampleChannel1() != null && idSampleChannel1Real == null)
					|| (hyb.getLabeledSampleChannel1() != null && idSampleChannel1Real != null && !hyb.getLabeledSampleChannel1().getIdSample()
							.equals(idSampleChannel1Real))) {

				LabeledSample labeledSampleObsoleted = null;
				if (hyb.getIdLabeledSampleChannel1() != null) {
					labeledSampleObsoleted = hyb.getLabeledSampleChannel1();
				}

				// If the Cy3 Sample has been is filled in
				if (idSampleChannel1Real != null) {
					Integer idLabeledSampleChannel1 = null;
					if (channel1SampleMap.containsKey(idSampleChannel1Real)) {
						idLabeledSampleChannel1 = (Integer) channel1SampleMap.get(idSampleChannel1Real);
					} else {
						labeledSampleChannel1 = new LabeledSample();
						labeledSampleChannel1.setIdSample(idSampleChannel1Real);
						labeledSampleChannel1.setIdLabel((Integer) labelMap.get("Cy3"));
						labeledSampleChannel1.setIdRequest(requestParser.getRequest().getIdRequest());
						labeledSampleChannel1.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
						labeledSampleChannel1.setNumberOfReactions(new Integer(1));
						labeledSampleChannel1.setIdLabelingProtocol(idLabelingProtocolDefault);

						sess.save(labeledSampleChannel1);
						idLabeledSampleChannel1 = labeledSampleChannel1.getIdLabeledSample();
						channel1SampleMap.put(idSampleChannel1Real, idLabeledSampleChannel1);
					}

					hyb.setIdLabeledSampleChannel1(idLabeledSampleChannel1);
				}
				// If the Cy3 Sample has been blanked out
				else {
					hyb.setIdLabeledSampleChannel1(null);
				}
				sess.flush();

				if (labeledSampleObsoleted != null) {
					// Replace the labeled sample on the labeling worklist (if present).
					List referencingWorkItems = sess.createQuery(
							"SELECT wi from WorkItem wi join wi.labeledSample as ls where ls.idLabeledSample = " + labeledSampleObsoleted.getIdLabeledSample())
							.list();
					if (referencingWorkItems.size() > 0) {
						for (Iterator i1 = referencingWorkItems.iterator(); i1.hasNext();) {
							WorkItem wi = (WorkItem) i1.next();
							if (labeledSampleChannel1 != null) {
								wi.setLabeledSample(labeledSampleChannel1);
							} else {
								sess.delete(wi);
							}
						}
					}

					// Get rid of the labeled sample that was replaced
					List referencingHybs = sess.createQuery(
							"SELECT h from Hybridization h where h.idLabeledSampleChannel1 = " + labeledSampleObsoleted.getIdLabeledSample()).list();
					if (referencingHybs.size() == 0) {
						sess.delete(labeledSampleObsoleted);
					}

				}

				changedChannelSample = true;

			}

			if ((hyb.getLabeledSampleChannel2() == null && idSampleChannel2Real != null)
					|| (hyb.getLabeledSampleChannel2() != null && idSampleChannel2Real == null)
					|| (hyb.getLabeledSampleChannel2() != null && idSampleChannel2Real != null && !hyb.getLabeledSampleChannel2().getIdSample()
							.equals(idSampleChannel2Real))) {

				LabeledSample labeledSampleObsoleted = null;
				if (hyb.getIdLabeledSampleChannel1() != null) {
					labeledSampleObsoleted = hyb.getLabeledSampleChannel2();
				}
				// If the Cy5 Sample has been filled in
				if (idSampleChannel2Real != null) {
					Integer idLabeledSampleChannel2 = null;
					if (channel2SampleMap.containsKey(idSampleChannel2Real)) {
						idLabeledSampleChannel2 = (Integer) channel2SampleMap.get(idSampleChannel2Real);
					} else {
						labeledSampleChannel2 = new LabeledSample();
						labeledSampleChannel2.setIdSample(idSampleChannel2Real);
						labeledSampleChannel2.setIdLabel((Integer) labelMap.get("Cy5"));
						labeledSampleChannel2.setIdRequest(requestParser.getRequest().getIdRequest());
						labeledSampleChannel2.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
						labeledSampleChannel2.setNumberOfReactions(new Integer(1));
						labeledSampleChannel2.setIdLabelingProtocol(idLabelingProtocolDefault);

						sess.save(labeledSampleChannel2);
						idLabeledSampleChannel2 = labeledSampleChannel2.getIdLabeledSample();
						channel2SampleMap.put(idSampleChannel2Real, idLabeledSampleChannel2);
					}

					hyb.setIdLabeledSampleChannel2(idLabeledSampleChannel2);
				}
				// If the Cy5 Sample has been blanked out
				else {

					hyb.setIdLabeledSampleChannel2(null);
				}
				sess.flush();

				if (labeledSampleObsoleted != null) {
					// Replace the labeled sample on the labeling worklist (if present).
					List referencingWorkItems = sess.createQuery(
							"SELECT wi from WorkItem wi join wi.labeledSample as ls where ls.idLabeledSample = " + labeledSampleObsoleted.getIdLabeledSample())
							.list();
					if (referencingWorkItems.size() > 0) {
						for (Iterator i1 = referencingWorkItems.iterator(); i1.hasNext();) {
							WorkItem wi = (WorkItem) i1.next();
							if (labeledSampleChannel2 != null) {
								wi.setLabeledSample(labeledSampleChannel2);
							} else {
								sess.delete(wi);
							}
						}
					}

					// Get rid of the labeled sample that was replaced
					List referencingHybs = sess.createQuery(
							"SELECT h from Hybridization h where h.idLabeledSampleChannel2 = " + labeledSampleObsoleted.getIdLabeledSample()).list();
					if (referencingHybs.size() == 0) {
						sess.delete(labeledSampleObsoleted);
					}
				}

				changedChannelSample = true;

			}

			// If the user has not changed the sample designations and the user can manage workflow,
			// save any changes made to workflow fields.
			if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {

				// Labeling reaction for channel1 labeled sample
				if (!changedChannelSample) {
					if (hyb.getLabeledSampleChannel1() != null) {
						if (hybInfo.getLabelingCompletedChannel1().equals("Y") && hyb.getLabeledSampleChannel1().getLabelingDate() == null) {
							hyb.getLabeledSampleChannel1().setLabelingDate(new java.sql.Date(System.currentTimeMillis()));
						}
						hyb.getLabeledSampleChannel1().setLabelingBypassed(hybInfo.getLabelingBypassedChannel1());
						hyb.getLabeledSampleChannel1().setLabelingFailed(hybInfo.getLabelingFailedChannel1());
						hyb.getLabeledSampleChannel1().setIdLabelingProtocol(hybInfo.getIdLabelingProtocolChannel1());
						hyb.getLabeledSampleChannel1().setLabelingYield(hybInfo.getLabelingYieldChannel1());
						hyb.getLabeledSampleChannel1().setNumberOfReactions(hybInfo.getNumberOfReactionsChannel1());
						hyb.getLabeledSampleChannel1().setCodeLabelingReactionSize(hybInfo.getCodeLabelingReactionSizeChannel1());
					}

					// Labeling reaction for channel2 labeled sample
					if (hyb.getLabeledSampleChannel2() != null) {
						if (hybInfo.getLabelingCompletedChannel2().equals("Y") && hyb.getLabeledSampleChannel2().getLabelingDate() == null) {
							hyb.getLabeledSampleChannel2().setLabelingDate(new java.sql.Date(System.currentTimeMillis()));
						}
						hyb.getLabeledSampleChannel2().setLabelingBypassed(hybInfo.getLabelingBypassedChannel2());
						hyb.getLabeledSampleChannel2().setLabelingFailed(hybInfo.getLabelingFailedChannel2());
						hyb.getLabeledSampleChannel2().setIdLabelingProtocol(hybInfo.getIdLabelingProtocolChannel2());
						hyb.getLabeledSampleChannel2().setLabelingYield(hybInfo.getLabelingYieldChannel2());
						hyb.getLabeledSampleChannel2().setNumberOfReactions(hybInfo.getNumberOfReactionsChannel2());
						hyb.getLabeledSampleChannel2().setCodeLabelingReactionSize(hybInfo.getCodeLabelingReactionSizeChannel2());
					}
				}

				//
				// Hyb workflow
				//
				hyb.setIdHybProtocol(hybInfo.getIdHybProtocol());
				hyb.setIdScanProtocol(hybInfo.getIdScanProtocol());
				hyb.setIdFeatureExtractionProtocol(hybInfo.getIdFeatureExtractionProtocol());

				if (hybInfo.getHybCompleted().equals("Y") && hyb.getHybDate() == null) {
					hyb.setHybDate(new java.sql.Date(System.currentTimeMillis()));
				}
				hyb.setHybFailed(hybInfo.getHybFailed());
				hyb.setHybBypassed(hybInfo.getHybBypassed());

				if (hybInfo.getExtractionCompleted().equals("Y") && hyb.getExtractionDate() == null) {
					hyb.setExtractionDate(new java.sql.Date(System.currentTimeMillis()));
				}
				hyb.setExtractionFailed(hybInfo.getExtractionFailed());
				hyb.setExtractionBypassed(hybInfo.getExtractionBypassed());

				// Save the slide
				Slide slide = hyb.getSlide();
				if (hybInfo.getSlideBarcode() != null) {
					slide = WorkItemHybParser.getSlideForHyb(sess, hyb, hyb.getIdSlideDesign(), hybInfo.getSlideBarcode(), requestParser.getRequest()
							.getIdRequest());

					// Create a new slide if one doesn't already exist
					if (slide == null) {
						slide = new Slide();
						sess.save(slide);

						// If we are switching out the old slide, we need to delete the old one if there are not any references to it.
						if (hyb.getSlide() != null) {
							WorkItemHybParser.deleteOrphanSlide(sess, hyb, requestParser.getRequest().getIdRequest());
						}

					}

					// Assign the slide to the hyb
					hyb.setIdSlide(slide.getIdSlide());

					// Set the slideDesign
					slide.setIdSlideDesign(hyb.getIdSlideDesign());

					// Set the barcode
					slide.setBarcode(hybInfo.getSlideBarcode());

					// Set the array coordinate
					WorkItemHybParser.setArrayCoordinate(sess, hyb, slide, hybInfo.getArrayCoordinateName(), requestParser.getRequest().getIdRequest());
				}
			}

		}

		String codeSlideSource = hybInfo.getCodeSlideSource();
		hyb.setCodeSlideSource(codeSlideSource);

		if (hybInfo.getIdSlideDesign() != null) {
			hyb.setIdSlideDesign(hybInfo.getIdSlideDesign());
		} else {
			List slideDesigns = sess.createQuery("select sd from SlideDesign sd where sd.idSlideProduct = " + requestParser.getRequest().getIdSlideProduct())
					.list();
			if (slideDesigns.size() > 1) {
				throw new Exception("Cannot set slide design because multiple slide designs exist for slide product "
						+ requestParser.getRequest().getIdSlideProduct());
			} else if (slideDesigns.size() == 0) {
				throw new Exception("Cannot set slide design because no slide designs exist for slide product "
						+ requestParser.getRequest().getIdSlideProduct());
			}
			SlideDesign sd = (SlideDesign) slideDesigns.get(0);
			hyb.setIdSlideDesign(sd.getIdSlideDesign());
		}

		hyb.setNotes(hybInfo.getNotes());

		sess.save(hyb);

		if (isNewHyb) {
			hyb.setNumber(Request.getRequestNumberNoR(requestParser.getRequest().getNumber()) + "E" + hybCount);
			sess.save(hyb);
			sess.flush();

			sess.refresh(hyb);
			if (hyb.getLabeledSampleChannel1() != null) {
				sess.refresh(hyb.getLabeledSampleChannel1());
			}
			if (hyb.getLabeledSampleChannel2() != null) {
				sess.refresh(hyb.getLabeledSampleChannel2());
			}
			hybs.add(hyb);

			hybsAdded.add(hyb);

		}

		sess.flush();
	}

	public static Map saveSequenceLanes(SecurityAdvisor secAdvisor, RequestParser requestParser, Session sess, RequestCategory requestCategory,
			Map idSampleMap, Set sequenceLanes, Set sequenceLanesAdded) throws Exception {
		return saveSequenceLanes(secAdvisor, requestParser, sess, requestCategory, idSampleMap, sequenceLanes, sequenceLanesAdded, false);
	}

	public static Map saveSequenceLanes(SecurityAdvisor secAdvisor, RequestParser requestParser, Session sess, RequestCategory requestCategory,
			Map idSampleMap, Set sequenceLanes, Set sequenceLanesAdded, boolean isImport) throws Exception {

		HashMap sampleToLaneMap = new HashMap();
		HashMap existingLanesSaved = new HashMap();
		if (!requestParser.getSequenceLaneInfos().isEmpty()) {

			// Hash lanes by sample id
			for (Iterator i = requestParser.getSequenceLaneInfos().iterator(); i.hasNext();) {
				RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i.next();

				List lanes = (List) sampleToLaneMap.get(laneInfo.getIdSampleString());
				if (lanes == null) {
					lanes = new ArrayList();
					sampleToLaneMap.put(laneInfo.getIdSampleString(), lanes);
				}
				lanes.add(laneInfo);
			}

			Date timestamp = new Date(System.currentTimeMillis()); // save the current time here so that the timestamp is the same on every sequence lane in
			// this batch
			for (Iterator i = sampleToLaneMap.keySet().iterator(); i.hasNext();) {
				String idSampleString = (String) i.next();
				List lanes = (List) sampleToLaneMap.get(idSampleString);

				Integer idSample = (Integer) idSampleMap.get(idSampleString);
				Sample s = null;
				if (idSample != null) {
					s = sess.load(Sample.class, idSample);
				}

				int lastSampleSeqCount = 0;

				// Figure out next number to assign for a
				for (Iterator i1 = lanes.iterator(); i1.hasNext();) {
					RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i1.next();
					boolean isNewLane = requestParser.isNewRequest() || laneInfo.getIdSequenceLane() == null
							|| laneInfo.getIdSequenceLane().startsWith("SequenceLane");
					if (!isNewLane) {

						SequenceLane sl = sess.load(SequenceLane.class, new Integer(laneInfo.getIdSequenceLane()));
						boolean seqLaneReassignment = isSeqReassignment(sess, laneInfo, idSampleMap);

						if (!seqLaneReassignment) {
							lastSampleSeqCount++;
						}

					}
				}

				for (Iterator i1 = lanes.iterator(); i1.hasNext();) {
					RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i1.next();
					if (idSampleMap.get(laneInfo.getIdSampleString()) == null) {
						// Looks like sample for this lane is deleted. This will cause lane to get deleted as well.
						continue;
					}
					boolean isNewLane = requestParser.isNewRequest() || laneInfo.getIdSequenceLane() == null
							|| laneInfo.getIdSequenceLane().startsWith("SequenceLane");
					// If the sample id's don't match up then we had a drag and drop reassignment from one sample to another. We need to adjust the number to
					// match new sample
					boolean seqLaneReassignment = false;
					Integer idSampleReal = null;
					if (!isNewLane) {
						SequenceLane sl = sess.load(SequenceLane.class, new Integer(laneInfo.getIdSequenceLane()));
						if (laneInfo.getIdSampleString() != null && !laneInfo.getIdSampleString().equals("") && !laneInfo.getIdSampleString().equals("0")) {
							idSampleReal = (Integer) idSampleMap.get(laneInfo.getIdSampleString());
						}

						if (idSampleReal != null && sl.getIdSample() != null && !idSampleReal.equals(sl.getIdSample())) {
							seqLaneReassignment = true;
						}
					}

					SequenceLane lane = saveSequenceLane(secAdvisor, requestParser, laneInfo, sess, lastSampleSeqCount, timestamp, idSampleMap, sequenceLanes,
							sequenceLanesAdded, isImport);

					if (!isNewLane) {
						existingLanesSaved.put(lane.getIdSequenceLane(), lane);
					}

					// if this is a not a new request, but these is a new sequence lane,
					// create a work item for the Cluster Gen (Assemble) worklist.
					// Also ignore this if this is a QC Amend as seqPrep work items were
					// created above.
					if ((!requestParser.isExternalExperiment() && !requestParser.isNewRequest() && !requestParser.isQCAmendRequest() && isNewLane && s != null
							&& s.getWorkItems() != null && s.getWorkItems().size() == 0)) {
						WorkItem workItem = new WorkItem();
						workItem.setIdRequest(requestParser.getRequest().getIdRequest());
						workItem.setIdCoreFacility(requestParser.getRequest().getIdCoreFacility());
						workItem.setSequenceLane(lane);
						String codeStepNext = "";
						if (requestCategory.getType().equals(RequestCategoryType.TYPE_HISEQ)) {
							codeStepNext = Step.HISEQ_CLUSTER_GEN;
						} else if (requestCategory.getType().equals(RequestCategoryType.TYPE_MISEQ)) {
							codeStepNext = Step.MISEQ_CLUSTER_GEN;
						}
						workItem.setCodeStepNext(codeStepNext);
						workItem.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
						sess.save(workItem);
					}

					if (isNewLane || seqLaneReassignment) {
						lastSampleSeqCount++;
					}
				}
			}

		}
		return existingLanesSaved;

	}

	/*
	 * Helper function to determine if an existing sequence lane had a new sample reassigned to it
	 */
	private static boolean isSeqReassignment(Session sess, RequestParser.SequenceLaneInfo laneInfo, Map idSampleMap) {
		SequenceLane sl = sess.load(SequenceLane.class, new Integer(laneInfo.getIdSequenceLane()));
		Integer idSampleReal = null;
		if (laneInfo.getIdSampleString() != null && !laneInfo.getIdSampleString().equals("") && !laneInfo.getIdSampleString().equals("0")) {
			idSampleReal = (Integer) idSampleMap.get(laneInfo.getIdSampleString());
		}

		if (idSampleReal != null && sl.getIdSample() != null && !idSampleReal.equals(sl.getIdSample())) {
			return true;
		} else {
			return false;
		}

	}

	private static SequenceLane saveSequenceLane(SecurityAdvisor secAdvisor, RequestParser requestParser, RequestParser.SequenceLaneInfo sequenceLaneInfo,
			Session sess, int lastSampleSeqCount, Date theTime, Map idSampleMap, Set sequenceLanes, Set sequenceLanesAdded, boolean isImport) throws Exception {

		SequenceLane sequenceLane = null;
		boolean seqLaneReassignment = false;
		boolean isNewSequenceLane = requestParser.isNewRequest() || sequenceLaneInfo.getIdSequenceLane() == null
				|| sequenceLaneInfo.getIdSequenceLane().startsWith("SequenceLane");

		if (isNewSequenceLane) {
			sequenceLane = new SequenceLane();
			sequenceLane.setIdRequest(requestParser.getRequest().getIdRequest());
			sequenceLane.setCreateDate(theTime);
			isNewSequenceLane = true;
		} else {
			sequenceLane = sess.load(SequenceLane.class, new Integer(sequenceLaneInfo.getIdSequenceLane()));
		}

		Integer idSampleReal = null;
		if (sequenceLaneInfo.getIdSampleString() != null && !sequenceLaneInfo.getIdSampleString().equals("")
				&& !sequenceLaneInfo.getIdSampleString().equals("0")) {
			idSampleReal = (Integer) idSampleMap.get(sequenceLaneInfo.getIdSampleString());
		}

		// If the samples don't line up then we had a drag and drop reassignment. We need to adjust the number to match new sample
		if (idSampleReal != null && sequenceLane.getIdSample() != null && !idSampleReal.equals(sequenceLane.getIdSample())) {
			seqLaneReassignment = true;
		}
		sequenceLane.setIdSample(idSampleReal);

		sequenceLane.setIdSeqRunType(sequenceLaneInfo.getIdSeqRunType());
		sequenceLane.setIdNumberSequencingCycles(sequenceLaneInfo.getIdNumberSequencingCycles());
		sequenceLane.setIdNumberSequencingCyclesAllowed(sequenceLaneInfo.getIdNumberSequencingCyclesAllowed());
		sequenceLane.setIdGenomeBuildAlignTo(sequenceLaneInfo.getIdGenomeBuildAlignTo());
		sequenceLane.setAnalysisInstructions(sequenceLaneInfo.getAnalysisInstructions());

		Sample theSample = sess.get(Sample.class, idSampleReal);
		String seqLaneLetter = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.SEQ_LANE_LETTER);
		String flowCellNumber = theSample.getNumber().toString().replaceFirst("X", seqLaneLetter);

		if (isNewSequenceLane) {
			if (isImport) {
				// Use the sequence lane ID in the XML
				sequenceLane.setNumber(sequenceLaneInfo.getNumber());
			} else {
				// Generate a new sequence lane number
				sequenceLane.setNumber(flowCellNumber + PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.SEQ_LANE_NUMBER_SEPARATOR)
						+ (lastSampleSeqCount + 1));
			}
			sequenceLanes.add(sequenceLane);
			sequenceLanesAdded.add(sequenceLane); // used in createBillingItems
		}

		if (seqLaneReassignment) {
			sequenceLane.setNumber(flowCellNumber + PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.SEQ_LANE_NUMBER_SEPARATOR)
					+ (lastSampleSeqCount + 1));
		}

		sess.save(sequenceLane);

		// Update workflow fields (for flow cell channel)
		if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
			if (sequenceLane.getFlowCellChannel() != null) {
				FlowCellChannel channel = sequenceLane.getFlowCellChannel();
				channel.setClustersPerTile(sequenceLaneInfo.getClustersPerTile());
				channel.setNumberSequencingCyclesActual(sequenceLaneInfo.getNumberSequencingCyclesActual());
				channel.setFileName(sequenceLaneInfo.getFileName());

				if (sequenceLaneInfo.getSeqRunFirstCycleCompleted().equals("Y") && channel.getFirstCycleDate() == null) {
					channel.setFirstCycleDate(new java.sql.Date(System.currentTimeMillis()));
				}
				channel.setFirstCycleFailed(sequenceLaneInfo.getSeqRunFirstCycleFailed());

				if (sequenceLaneInfo.getSeqRunLastCycleCompleted().equals("Y") && channel.getLastCycleDate() == null) {
					channel.setLastCycleDate(new java.sql.Date(System.currentTimeMillis()));
				}
				channel.setLastCycleFailed(sequenceLaneInfo.getSeqRunLastCycleFailed());

				if (sequenceLaneInfo.getSeqRunPipelineCompleted().equals("Y") && channel.getPipelineDate() == null) {
					channel.setPipelineDate(new java.sql.Date(System.currentTimeMillis()));
				}
				channel.setPipelineFailed(sequenceLaneInfo.getSeqRunPipelineFailed());

			}
		}

		if (sequenceLane.getFlowCellChannel() != null) {
			FlowCellChannel channel = sequenceLane.getFlowCellChannel();
			channel.setSampleConcentrationpM(sequenceLaneInfo.getFlowCellChannelSampleConcentrationpM());
		}

		sess.flush();
		sess.refresh(sequenceLane);
		return sequenceLane;
	}

	public static void createBillingItems(Session sess, Request request, String amendState, BillingPeriod billingPeriod, DictionaryHelper dh,
			Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes,
			Map<String, ArrayList<String>> sampleToAssaysMap, String codeStepNext, String billingStatus, BillingTemplate billingTemplate,
			Boolean requestPropertiesOnly, Boolean comingFromWorkflow) throws Exception {
		createBillingItems(sess, request, amendState, billingPeriod, dh, samples, labeledSamples, hybs, lanes, sampleToAssaysMap, codeStepNext, billingStatus,
				null, billingTemplate, requestPropertiesOnly, comingFromWorkflow);
	}

	public static void createBillingItems(Session sess, Request request, String amendState, BillingPeriod billingPeriod, DictionaryHelper dh,
			Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes,
			Map<String, ArrayList<String>> sampleToAssaysMap, BillingTemplate billingTemplate, Boolean requestPropertiesOnly, Boolean comingFromWorkflow)
			throws Exception {
		createBillingItems(sess, request, amendState, billingPeriod, dh, samples, labeledSamples, hybs, lanes, sampleToAssaysMap, null, BillingStatus.PENDING,
				null, billingTemplate, requestPropertiesOnly, comingFromWorkflow);
	}

	public static void createBillingItems(Session sess, Request request, String amendState, BillingPeriod billingPeriod, DictionaryHelper dh,
			Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes,
			Map<String, ArrayList<String>> sampleToAssaysMap, String codeStepNext, String billingStatus, Set<PropertyEntry> propertyEntries,
			BillingTemplate billingTemplate, Boolean requestPropertiesOnly, Boolean comingFromWorkflow) throws Exception {

		// if someone is trying to edit a very old request (i.e, 2014) we could get here with no billing template
		// if so, just leave
		if (billingTemplate == null) {
			return;
		}

		List billingItems = new ArrayList<BillingItem>();
		List discountBillingItems = new ArrayList<BillingItem>();

		// Find the appropriate price sheet
		PriceSheet priceSheet = null;
		List priceSheets = sess.createQuery("SELECT ps from PriceSheet as ps").list();
		for (Iterator i = priceSheets.iterator(); i.hasNext();) {
			PriceSheet ps = (PriceSheet) i.next();
			for (Iterator i1 = ps.getRequestCategories().iterator(); i1.hasNext();) {
				RequestCategory requestCategory = (RequestCategory) i1.next();
				if (requestCategory.getCodeRequestCategory().equals(request.getCodeRequestCategory())) {
					priceSheet = ps;
					break;
				}

			}
		}

		// if (priceSheet == null) {
		// throw new Exception("Cannot find price sheet to create billing items for added services");
		// }

		if (priceSheet != null) {
			for (Iterator i1 = priceSheet.getPriceCategories().iterator(); i1.hasNext();) {
				PriceSheetPriceCategory priceCategoryX = (PriceSheetPriceCategory) i1.next();
				PriceCategory priceCategory = priceCategoryX.getPriceCategory();

				// we want to only create request property billing items AND/OR unit charge billing items for products
				// at submit when we have a bill during workflow
				if (requestPropertiesOnly
						&& (priceCategory.getPluginClassName() != null
								&& !priceCategory.getPluginClassName().equals("hci.gnomex.billing.PropertyPricingPlugin")
								&& !priceCategory.getPluginClassName().equals("hci.gnomex.billing.PropertyPricingNotBySamplePlugin") && !priceCategory
								.getPluginClassName().equals("hci.gnomex.billing.UnitChargeAppFilterPlugin"))) {
					continue;
				}

				// if this is being called from a workflow servlet then skip the creation of request property billing items
				// because we already created them at submit time.
				if (comingFromWorkflow
						&& (priceCategory.getPluginClassName() != null && (priceCategory.getPluginClassName()
								.equals("hci.gnomex.billing.PropertyPricingPlugin") || priceCategory.getPluginClassName().equals(
								"hci.gnomex.billing.PropertyPricingNotBySamplePlugin")))) {
					continue;
				}

				// Ignore inactive price categories
				if (priceCategory.getIsActive() != null && priceCategory.getIsActive().equals("N")) {
					continue;
				}

				// If a step is provided, ignore price categories that are not available for that step
				if (codeStepNext != null) {
					Boolean found = false;
					for (Step s : (Set<Step>) priceCategory.getSteps()) {
						if (s.getCodeStep().equals(codeStepNext)) {
							found = true;
							break;
						}
					}
					if (!found) {
						continue;
					}
				}

				// Instantiate plugin for billing category
				BillingPlugin plugin = null;
				Boolean isDiscount = false;
				if (priceCategory.getPluginClassName() != null) {
					try {
						plugin = (BillingPlugin) Class.forName(priceCategory.getPluginClassName()).newInstance();
						if (priceCategory.getPluginClassName().toLowerCase().indexOf("discount") != -1) {
							isDiscount = true;
						}
					} catch (Exception e) {
						LOG.error("Unable to instantiate billing plugin " + priceCategory.getPluginClassName(), e);
					}

				}

				// Get the billing items
				if (plugin != null) {
					List billingItemsForCategory = plugin.constructBillingItems(sess, amendState, billingPeriod, priceCategory, request, samples,
							labeledSamples, hybs, lanes, sampleToAssaysMap, billingStatus, propertyEntries, billingTemplate);
					if (isDiscount) {
						discountBillingItems.addAll(billingItemsForCategory);
					} else {
						billingItems.addAll(billingItemsForCategory);
					}
				}
			}

			for (MasterBillingItem masterBillingItem : billingTemplate.getMasterBillingItems()) {
				sess.save(masterBillingItem);
				for (BillingItem billingItem : masterBillingItem.getBillingItems()) {
					billingItem.setIdMasterBillingItem(masterBillingItem.getIdMasterBillingItem());
				}
			}

			BigDecimal grandInvoicePrice = new BigDecimal(0);
			for (Iterator i = billingItems.iterator(); i.hasNext();) {
				BillingItem bi = (BillingItem) i.next();
				grandInvoicePrice = grandInvoicePrice.add(bi.getInvoicePrice());
				sess.save(bi);
			}
			for (Iterator i = discountBillingItems.iterator(); i.hasNext();) {
				BillingItem bi = (BillingItem) i.next();
				if (bi.getUnitPrice() != null) {
					BigDecimal invoicePrice = bi.getUnitPrice().multiply(grandInvoicePrice);
					bi.setUnitPrice(invoicePrice);
					bi.setInvoicePrice(invoicePrice);
					bi.resetInvoiceForBillingItem(sess);
					sess.save(bi);
				}
			}

		}
	}

	private void sendPrintableReqFormToCore(Session sess) {

		CoreFacility cf = sess.load(CoreFacility.class, requestParser.getRequest().getIdCoreFacility());
		// get core facility email
		String toAddress = cf.getContactEmail();

		String fromAddress = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);

		// new property for additional people who should get this email
		String ccAddress = "";
		// construct printable request form url

		String reqFormURL = launchAppURL + "?idRequest=" + requestParser.getRequest().getIdRequest() + "&amendState=" + Constants.AMEND_ADD_SEQ_LANES
				+ "&comingFromEmail=Y" + "&launchWindow=showRequestForm";

		String subject = "New sequence lanes added to request " + requestParser.getRequest().getNumber();

		StringBuffer emailBody = new StringBuffer();

		emailBody.append("A request to add services to existing experiment " + originalRequestNumber + " has been submitted to the " + cf.getFacilityName()
				+ " core.");
		emailBody.append("<br><br><table border='0' width = '400'><tr><td>Request Type:</td><td>"
				+ requestParser.getRequest().getRequestCategory().getDisplay());
		emailBody.append("</td></tr><tr><td>Request #:</td><td>" + requestParser.getRequest().getNumber());
		emailBody.append("</td></tr><tr><td>Submitter:</td><td>" + requestParser.getRequest().getSubmitterName());

		emailBody
				.append("</td></tr></table><br><br>To view the printable request form and notify the other people listed on this email as well as the submitter of the additional lanes, click <a href=\""
						+ reqFormURL + "\">here</a>.");

		try {
			MailUtilHelper helper = new MailUtilHelper(toAddress, ccAddress, null, fromAddress, subject, emailBody.toString(), null, true,
					DictionaryHelper.getInstance(sess), serverName);
			MailUtil.validateAndSendEmail(helper);
		} catch (Exception e) {
			LOG.error("An exception has occurred in SaveRequest ", e);
		}

	}

	private void sendConfirmationEmail(Session sess, String otherRecipients) throws NamingException, MessagingException, IOException {

		DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		CoreFacility cf = sess.load(CoreFacility.class, this.requestParser.getRequest().getIdCoreFacility());

		// Refresh request and samples to make sure everything is populated.
		sess.refresh(requestParser.getRequest());
		for (Iterator i = requestParser.getRequest().getSamples().iterator(); i.hasNext();) {
			Sample s = (Sample) i.next();
			sess.refresh(s);
			if (requestParser.getRequest().getRequestCategory() != null
					&& requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)) {
				for (Iterator j = s.getSourceWells().iterator(); j.hasNext();) {
					sess.refresh(j.next());
				}
			}
		}

		String body = getTemplatedConfirmationEmailBody(cf, dictionaryHelper, pdh);
		if (body == null || body.length() == 0) {
			body = getDefaultConfirmationEmailBody(cf, sess, dictionaryHelper);
		}

		String subject = dictionaryHelper.getRequestCategory(requestParser.getRequest().getCodeRequestCategory())
				+ (requestParser.isExternalExperiment() ? " Experiment " : " Experiment Request ") + requestParser.getRequest().getNumber()
				+ (requestParser.isExternalExperiment() ? " registered" : " submitted");

		String contactEmailCoreFacility = cf.getContactEmail();
		String contactEmailSoftwareBugs = pdh.getCoreFacilityProperty(requestParser.getRequest().getIdCoreFacility(),
				PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
		String emailRecipients = "";
		if (requestParser.getRequest().getAppUser() != null && requestParser.getRequest().getAppUser().getEmail() != null) {
			emailRecipients = requestParser.getRequest().getAppUser().getEmail();
		}
		if (!emailRecipients.equals("") && !MailUtil.isValidEmail(emailRecipients)) {
			LOG.error("Invalid email address " + emailRecipients);
		}
		if (otherRecipients != null && otherRecipients.length() > 0) {
			if (emailRecipients.length() > 0) {
				emailRecipients += ",";
			}
			emailRecipients += otherRecipients;
		}

		if (emailRecipients.contains(",")) {
			for (String e : emailRecipients.split(",")) {
				if (!MailUtil.isValidEmail(e.trim())) {
					LOG.error("Invalid email address: " + e);
				}
			}
		}

		String fromAddress = requestParser.isExternalExperiment() ? contactEmailSoftwareBugs : contactEmailCoreFacility;

		if (!MailUtil.isValidEmail(fromAddress)) {
			fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		}

		MailUtilHelper helper = new MailUtilHelper(emailRecipients, fromAddress, subject, body, null, true, dictionaryHelper, serverName);
		helper.setRecipientAppUser(requestParser.getRequest().getAppUser());
		MailUtil.validateAndSendEmail(helper);

	}

	private String getDefaultConfirmationEmailBody(CoreFacility cf, Session sess, DictionaryHelper dictionaryHelper) {
		StringBuffer introNote = new StringBuffer();
		String trackRequestURL = launchAppURL + "?requestNumber=" + requestParser.getRequest().getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
		if (requestParser.isExternalExperiment()) {
			if (requestParser.isNewRequest()) {
				introNote.append("Experiment " + requestParser.getRequest().getNumber() + " has been registered in the GNomEx repository.");
			} else {
				introNote.append("Additional services have been added to experiment " + originalRequestNumber + ".");

			}
			introNote.append("<br><br>To view the experiment details, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - "
					+ Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");

		} else {
			if (requestParser.isNewRequest()) {
				introNote.append("Experiment request " + requestParser.getRequest().getNumber() + " has been submitted to the " + cf.getFacilityName()
						+ " core.  You will receive email notification when the experiment is complete.");
			} else {
				introNote
						.append("Request " + requestParser.getRequest().getNumber() + " to add services to existing experiment " + originalRequestNumber
								+ " has been submitted to the " + cf.getFacilityName()
								+ " core.  You will receive email notification when the experiment is complete.");

			}
			introNote.append("<br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - "
					+ requestParser.getRequest().getNumber() + "</a>.");

		}

		RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper,
				requestParser.getRequest(), requestParser.getAmendState(), requestParser.getRequest().getSamples(), hybs, sequenceLanes, introNote.toString());

		return emailFormatter.format();
	}

	private String getTemplatedConfirmationEmailBody(CoreFacility cf, DictionaryHelper dictionaryHelper, PropertyDictionaryHelper pdh) {
		String emailBody = "";
		RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());
		String numberSequencingCyclesAllowed = "";
		String genomeBuildToAlignTo = "";
		if (requestParser.getRequest().getSequenceLanes().iterator().hasNext()) {
			SequenceLane lane = (SequenceLane) requestParser.getRequest().getSequenceLanes().iterator().next();
			numberSequencingCyclesAllowed = lane.getIdNumberSequencingCyclesAllowed() != null ? dictionaryHelper.getNumberSequencingCyclesAllowed(lane
					.getIdNumberSequencingCyclesAllowed()) : "";
			genomeBuildToAlignTo = lane.getIdGenomeBuildAlignTo() != null ? dictionaryHelper.getGenomeBuild(lane.getIdGenomeBuildAlignTo()) : "";
		}

		String templateString = pdh.getCoreFacilityRequestCategoryProperty(requestParser.getRequest().getIdCoreFacility(), requestParser.getRequest()
				.getCodeRequestCategory(), PropertyDictionary.EXPERIMENT_CONFIRMATION_EMAIL_TEMPLATE);

		if (templateString != null && templateString.length() > 0) {
			Map root = new HashMap();
			root.put("request", requestParser.getRequest());
			root.put("facility", cf);
			root.put("originalRequestNumber", originalRequestNumber != null ? originalRequestNumber : "");
			root.put("requestCategory", requestCategory);
			root.put("isNewRequest", requestParser.isNewRequest());
			root.put("addedSequenceLanes", sequenceLanes);
			root.put("dictionaryHelper", dictionaryHelper);
			root.put("gnomexURL", launchAppURL);
			root.put("assetURL", appURL + "/assets/");
			root.put("imageURL", appURL + "/images/");

			try {
				Template template = new Template("root", new StringReader(templateString), FreeMarkerConfiguration.instance().getConfiguration());

				Writer out = new StringWriter();
				template.process(root, out);
				emailBody = out.toString();
			} catch (IOException ex) {
				LOG.error("Unable to read template for invoice email for " + requestParser.getRequest().getNumber(), ex);
			} catch (TemplateException ex) {
				LOG.error("Error processing template for invoice email for " + requestParser.getRequest().getNumber(), ex);
			}
		}

		return emailBody;
	}

	private void sendInvoicePriceEmail(Session sess, String contactEmail, String ccEmail, String billedAccountName) throws NamingException, MessagingException,
			IOException {

		DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		CoreFacility cf = sess.load(CoreFacility.class, requestParser.getRequest().getIdCoreFacility());

		// If it isn't microarray or illumina, don't send email
		if (!RequestCategory.isMicroarrayRequestCategory(requestParser.getRequest().getCodeRequestCategory())
				&& !RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
			return;
		}

		String emailBody = getTemplatedInvoiceEmailBody(cf, dictionaryHelper, pdh);
		if (emailBody == null || emailBody.length() == 0) {
			emailBody = getDefaultInvoiceEmailBody(billedAccountName, dictionaryHelper, cf);
		}

		String subject = "Estimated " + cf.getFacilityName() + " charges for request " + requestParser.getRequest().getNumber();

		String contactEmailCoreFacility = cf.getContactEmail();
		String contactEmailSoftwareBugs = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(requestParser.getRequest().getIdCoreFacility(),
				PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);

		String senderEmail = requestParser.isExternalExperiment() ? contactEmailSoftwareBugs : contactEmailCoreFacility;

		if (contactEmail == null || contactEmail.length() == 0) {
			contactEmail = ccEmail;
			ccEmail = null;
			if (contactEmail == null) {
				// If neither email present then just cend to the lab
				contactEmail = senderEmail;
			}
		} else if (ccEmail != null && ccEmail.length() == 0) {
			ccEmail = null;
		}

		if (!MailUtil.isValidEmail(contactEmail)) {
			LOG.error("Invalid email address: " + contactEmail);
		}

		if (!MailUtil.isValidEmail(senderEmail)) {
			senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		}

		MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBody, null, true, dictionaryHelper, serverName);
		MailUtil.validateAndSendEmail(helper);

	}

	private String getDefaultInvoiceEmailBody(String billedAccountName, DictionaryHelper dictionaryHelper, CoreFacility cf) {
		String requestType = dictionaryHelper.getRequestCategory(requestParser.getRequest().getCodeRequestCategory());
		String requestNumber = requestParser.getRequest().getNumber();
		String submitterName = requestParser.getRequest().getSubmitterName();
		String billedAccountNumber = requestParser.getRequest().getBillingAccountNumber();
		StringBuffer emailBody = new StringBuffer();
		String trackRequestURL = launchAppURL + "?requestNumber=" + requestNumber + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;

		if (requestParser.isNewRequest()) {
			emailBody.append("An experiment request has been submitted to the " + cf.getFacilityName() + " core.");
		} else {
			emailBody.append("A request to add services to existing experiment (" + originalRequestNumber + ") has been submitted to the "
					+ cf.getFacilityName() + " core.");
		}
		// emailBody.append(" You are receiving this email notification because estimated charges are over $500.00 and the account to be billed belongs to your lab or group.");

		emailBody.append("<br><br><table border='0' width = '400'><tr><td>Request Type:</td><td>" + requestType);
		emailBody.append("</td></tr><tr><td>Request #:</td><td>" + requestNumber);
		emailBody.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + this.invoicePrice);
		emailBody.append("</td></tr><tr><td>Submitter:</td><td>" + submitterName);
		emailBody.append("</td></tr><tr><td>Billing Account Name:</td><td>" + billedAccountName);
		emailBody.append("</td></tr><tr><td>Billing Account Number:</td><td>" + billedAccountNumber);

		emailBody.append("</td></tr></table><br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">"
				+ Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");

		return emailBody.toString();
	}

	private String getTemplatedInvoiceEmailBody(CoreFacility cf, DictionaryHelper dictionaryHelper, PropertyDictionaryHelper pdh) {
		String emailBody = "";
		RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());
		String templateString = pdh.getCoreFacilityRequestCategoryProperty(requestParser.getRequest().getIdCoreFacility(), requestParser.getRequest()
				.getCodeRequestCategory(), PropertyDictionary.EXPERIMENT_INVOICE_EMAIL_TEMPLATE);

		if (templateString != null && templateString.length() > 0) {
			Map root = new HashMap();
			root.put("request", requestParser.getRequest());
			root.put("facility", cf);
			root.put("invoicePrice", invoicePrice);
			root.put("requestCategory", requestCategory);
			root.put("gnomexURL", launchAppURL);
			root.put("assetURL", appURL + "/assets/");

			try {
				Template template = new Template("root", new StringReader(templateString), FreeMarkerConfiguration.instance().getConfiguration());

				Writer out = new StringWriter();
				template.process(root, out);
				emailBody = out.toString();
			} catch (IOException ex) {
				LOG.error("Unable to read template for invoice email for " + requestParser.getRequest().getNumber(), ex);
			} catch (TemplateException ex) {
				LOG.error("Error processing template for invoice email for " + requestParser.getRequest().getNumber(), ex);
			}
		}

		return emailBody;
	}

	private void reassignLabForTransferLog(Session sess) {
		if (!requestParser.isNewRequest() && !requestParser.getOriginalIdLab().equals(requestParser.getRequest().getIdLab())) {
			// If an existing request has been assigned to a different lab, change
			// the idLab on the TransferLogs.
			String buf = "SELECT tl from TransferLog tl where idRequest = " + requestParser.getRequest().getIdRequest();
			List transferLogs = sess.createQuery(buf).list();
			for (Iterator i = transferLogs.iterator(); i.hasNext();) {
				TransferLog tl = (TransferLog) i.next();
				tl.setIdLab(requestParser.getRequest().getIdLab());
			}
		}
	}

	private void createResultDirectories(Request req, String qcDirectory, String microarrayDir) {

		String createYear = this.formatDate(req.getCreateDate(), this.DATE_OUTPUT_ALTIO).substring(0, 4);
		String rootDir = microarrayDir + Constants.FILE_SEPARATOR + createYear;

		boolean success = false;
		if (!new File(rootDir).exists()) {
			success = (new File(rootDir)).mkdir();
			if (!success) {
				LOG.error("Unable to create directory " + rootDir);
			}
		}

		String baseRequestNumber = Request.getBaseRequestNumber(req.getNumber());
		String directoryName = rootDir + Constants.FILE_SEPARATOR + baseRequestNumber;

		if (!new File(directoryName).exists()) {
			success = (new File(directoryName)).mkdir();
			if (!success) {
				LOG.error("Unable to create directory " + directoryName);
			}
		}

		String qcDirectoryName = directoryName + Constants.FILE_SEPARATOR + qcDirectory;

		if (!new File(qcDirectoryName).exists()) {
			success = (new File(qcDirectoryName)).mkdir();
			if (!success) {
				LOG.error("Unable to create directory " + qcDirectoryName);
			}
		}

		if (req.getHybridizations() != null) {
			for (Iterator i = req.getHybridizations().iterator(); i.hasNext();) {
				Hybridization hyb = (Hybridization) i.next();
				String hybDirectoryName = directoryName + Constants.FILE_SEPARATOR + hyb.getNumber();
				if (!new File(hybDirectoryName).exists()) {
					success = (new File(hybDirectoryName)).mkdir();
					if (!success) {
						LOG.error("Unable to create directory " + hybDirectoryName);
					}
				}
			}
		}

	}

	public static Integer getStartingNextSampleNumber(RequestParser requestParser) {
		Integer nextSampleNumber = 0;
		for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
			String idSampleString = (String) i.next();
			Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);
			String numberAsString = sample.getNumber();
			if (numberAsString != null && numberAsString.length() != 0 && numberAsString.indexOf("X") > 0) {
				numberAsString = numberAsString.substring(numberAsString.indexOf("X") + 1);
				try {
					Integer number = Integer.parseInt(numberAsString);
					if (number.intValue() > nextSampleNumber.intValue()) {
						nextSampleNumber = number;
					}
				} catch (Exception ex) {
				}
			}
		}
		return ++nextSampleNumber;
	}

	public class LabeledSampleComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			LabeledSample ls1 = (LabeledSample) o1;
			LabeledSample ls2 = (LabeledSample) o2;

			return ls1.getIdLabeledSample().compareTo(ls2.getIdLabeledSample());

		}
	}

	public void deleteDir(File f, String fileName) throws Exception {
		for (String file : f.list()) {
			File child = new File(fileName + Constants.FILE_SEPARATOR + file);
			if (child.isDirectory()) {
				deleteDir(child, child.getCanonicalPath().replace("\\", Constants.FILE_SEPARATOR));
			} else if (!(new File(fileName + Constants.FILE_SEPARATOR + file).delete())) {
				throw new Exception("Unable to delete file " + fileName + Constants.FILE_SEPARATOR + file);
			} else {
				filesToRemoveParser.parseFilesToRemove().remove(fileName + Constants.FILE_SEPARATOR + file);
			}

		}
		if (f.list().length == 0) {
			if (!f.delete()) {
				throw new Exception("Unable to delete file " + f.getCanonicalPath().replace("\\", Constants.FILE_SEPARATOR));
			}
			return;
		}

	}

	private Set saveRequestProperties(String propertiesXML, Session sess, RequestParser requestParser) throws org.jdom.JDOMException {
		return saveRequestProperties(propertiesXML, sess, requestParser, true);
	}

	public static Set saveRequestProperties(String propertiesXML, Session sess, RequestParser requestParser, boolean saveToDB) throws org.jdom.JDOMException {
		Set<PropertyEntry> propertyEntries = new TreeSet<PropertyEntry>(new PropertyEntryComparator());
		// Delete properties
		if (propertiesXML != null && !propertiesXML.equals("")) {
			StringReader reader = new StringReader(propertiesXML);
			SAXBuilder sax = new SAXBuilder();
			Document propsDoc = sax.build(reader);
			if (requestParser.getRequest().getPropertyEntries() != null && saveToDB) {
				for (Iterator<?> i = requestParser.getRequest().getPropertyEntries().iterator(); i.hasNext();) {
					PropertyEntry pe = PropertyEntry.class.cast(i.next());
					boolean found = false;
					for (Iterator<?> i1 = propsDoc.getRootElement().getChildren().iterator(); i1.hasNext();) {
						Element propNode = (Element) i1.next();
						String idPropertyEntry = propNode.getAttributeValue("idPropertyEntry");
						if (idPropertyEntry != null && !idPropertyEntry.equals("")) {
							if (pe.getIdPropertyEntry().equals(new Integer(idPropertyEntry))) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						// delete property values
						for (Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
							PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
							sess.delete(av);
						}
						sess.flush();
						pe.setValues(null);
						sess.save(pe);
						sess.flush();
						// delete property
						sess.delete(pe);
					}
				}
				sess.flush();
			}

			// Add properties
			for (Iterator<?> i = propsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
				Element node = (Element) i.next();
				// Adding dataTracks
				String idPropertyEntry = node.getAttributeValue("idPropertyEntry");

				PropertyEntry pe = null;
				if (idPropertyEntry == null || idPropertyEntry.equals("")) {
					pe = new PropertyEntry();
				} else {
					pe = PropertyEntry.class.cast(sess.get(PropertyEntry.class, Integer.valueOf(idPropertyEntry)));
				}
				pe.setIdProperty(Integer.valueOf(node.getAttributeValue("idProperty")));
				pe.setValue(node.getAttributeValue("value"));
				pe.setIdRequest(requestParser.getRequest().getIdRequest());

				if ((idPropertyEntry == null || idPropertyEntry.equals("")) && saveToDB) {
					sess.save(pe);
					sess.flush();
				}

				// Remove PropertyEntryValues
				if (pe.getValues() != null) {
					for (Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
						PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
						boolean found = false;
						for (Iterator<?> i2 = node.getChildren().iterator(); i2.hasNext();) {
							Element n = (Element) i2.next();
							if (n.getName().equals("PropertyEntryValue")) {
								String idPropertyEntryValue = n.getAttributeValue("idPropertyEntryValue");
								if (idPropertyEntryValue != null && !idPropertyEntryValue.equals("")) {
									if (av.getIdPropertyEntryValue().equals(new Integer(idPropertyEntryValue))) {
										found = true;
										break;
									}
								}
							}
						}
						if (!found && saveToDB) {
							sess.delete(av);
						}
					}
					if (saveToDB) {
						sess.flush();
					}
				}

				// Add and update PropertyEntryValues
				for (Iterator<?> i1 = node.getChildren().iterator(); i1.hasNext();) {
					Element n = (Element) i1.next();
					if (n.getName().equals("PropertyEntryValue")) {
						String idPropertyEntryValue = n.getAttributeValue("idPropertyEntryValue");
						String value = n.getAttributeValue("value");
						PropertyEntryValue av = null;
						// Ignore 'blank' url value
						if (value == null || value.equals("") || value.equals("Enter URL here...")) {
							continue;
						}
						if (idPropertyEntryValue == null || idPropertyEntryValue.equals("")) {
							av = new PropertyEntryValue();
							av.setIdPropertyEntry(pe.getIdPropertyEntry());
						} else {
							av = PropertyEntryValue.class.cast(sess.load(PropertyEntryValue.class, Integer.valueOf(idPropertyEntryValue)));
						}
						av.setValue(n.getAttributeValue("value"));

						if ((idPropertyEntryValue == null || idPropertyEntryValue.equals("")) && saveToDB) {
							sess.save(av);
						}
					}
				}
				if (saveToDB) {
					sess.flush();
				}

				String optionValue = "";
				TreeSet<PropertyOption> options = new TreeSet<PropertyOption>(new PropertyOptionComparator());
				for (Iterator<?> i1 = node.getChildren().iterator(); i1.hasNext();) {
					Element n = (Element) i1.next();
					if (n.getName().equals("PropertyOption")) {
						Integer idPropertyOption = Integer.parseInt(n.getAttributeValue("idPropertyOption"));
						String selected = n.getAttributeValue("selected");
						if (selected != null && selected.equals("Y")) {
							PropertyOption option = PropertyOption.class.cast(sess.load(PropertyOption.class, idPropertyOption));
							if (!saveToDB) {
								// Need to explicitly save if not using hibernate to save to db
								option.setIdPropertyOption(idPropertyOption);
							}
							options.add(option);
							if (optionValue.length() > 0) {
								optionValue += ",";
							}
							optionValue += option.getOption();
						}
					}
				}
				pe.setOptions(options);
				if (options.size() > 0) {
					pe.setValue(optionValue);
				}
				if (saveToDB) {
					sess.flush();
				}

				propertyEntries.add(pe);
			}
		}
		return propertyEntries;
	}

	// Sequenom experiments add a default annotation that doesn't show up in submit but then
	// shows up in view and edit.
	private static void addStandardSampleProperties(Session sess, RequestParser requestParser, String idSampleString, Sample sample) {
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);
		RequestCategory requestCategory = dh.getRequestCategoryObject(requestParser.getRequest().getCodeRequestCategory());
		Boolean addedProperty = false;
		if (requestCategory.getType().equals(RequestCategoryType.TYPE_SEQUENOM) || requestCategory.getType().equals(RequestCategoryType.TYPE_CLINICAL_SEQUENOM)) {
			Map sampleAnnotations = (Map) requestParser.getSampleAnnotationMap().get(idSampleString);
			for (Property prop : dh.getPropertyList()) {
				if (prop != null && prop.getPlatformApplications() != null && !sampleAnnotations.containsKey(prop.getIdProperty())) {
					for (Iterator i1 = prop.getPlatformApplications().iterator(); i1.hasNext();) {
						PropertyPlatformApplication pa = (PropertyPlatformApplication) i1.next();
						if (pa.getCodeRequestCategory() != null && pa.getCodeRequestCategory().equals(requestParser.getRequest().getCodeRequestCategory())
								&& (pa.getCodeApplication() == null || pa.getCodeApplication().equals(requestParser.getRequest().getCodeApplication()))) {
							PropertyEntry entry = new PropertyEntry();
							entry.setIdSample(sample.getIdSample());
							entry.setIdProperty(prop.getIdProperty());
							entry.setValue("");
							sess.save(entry);
							addedProperty = true;
							break;
						}
					}
				}
			}
		}

		if (addedProperty) {
			sess.flush();
		}
	}
}