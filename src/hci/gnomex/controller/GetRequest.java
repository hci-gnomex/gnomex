package hci.gnomex.controller;

import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.*;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetRequest extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);

  private Integer idRequest;
  private String requestNumber;
  private String showUploads = "N";
  private String serverName;
  private String baseDir;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    }
    if (request.getParameter("requestNumber") != null && !request.getParameter("requestNumber").equals("")) {
      requestNumber = request.getParameter("requestNumber");
    }
    if (request.getParameter("showUploads") != null && !request.getParameter("showUploads").equals("")) {
      showUploads = request.getParameter("showUploads");
    }

    serverName = request.getServerName();

    if (idRequest == null && requestNumber == null) {
      this.addInvalidField("idRequest or requestNumber", "Either idRequest or requestNumber must be provided");
    }
  }

  public Command execute() throws RollBackCommandException {

    long startTime = System.currentTimeMillis();
    String reqNumber = "";

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);

      // Find request
      boolean newRequest = false;

      Request request = null;
      if (idRequest != null && idRequest.intValue() == 0) {
        newRequest = true;
        request = new Request();
        request.setIdRequest(idRequest);
        request.canRead(true);
        request.canDelete(true);
        request.canUpdate(true);
        request.setCanDeleteSample(true);
        request.setCanUpdateSamples(true);

        // Set the default visibility if there is a property
        String defaultVisibility = propertyHelper.getProperty(PropertyDictionary.DEFAULT_VISIBILITY_EXPERIMENT);
        if (defaultVisibility != null && defaultVisibility.length() > 0) {
          request.setCodeVisibility(defaultVisibility);
        }

        // We can't set to institution level visibility if the default institution was not provided.
        if (request.getCodeVisibility() != null && request.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
          String idInstitutionString = propertyHelper.getProperty(PropertyDictionary.ID_DEFAULT_INSTITUTION);
          if (idInstitutionString == null || idInstitutionString.equals("")) {
            request.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
          } else {
            request.setIdInstitution(Integer.valueOf(idInstitutionString));
          }
        }

        // Set the default privacy expiration date
        String expirationInMonths = propertyHelper.getProperty(PropertyDictionary.DATASET_PRIVACY_EXPIRATION);
        if (expirationInMonths != null && !expirationInMonths.equals("") && !expirationInMonths.equals("0")) {
          Integer expireMonths = Integer.valueOf(expirationInMonths);
          GregorianCalendar cal = new GregorianCalendar();
          cal.add(GregorianCalendar.MONTH, expireMonths);
          request.setPrivacyExpirationDate(new java.sql.Date(cal.getTimeInMillis()));
        }

      } else if (idRequest != null) {
        request = sess.get(Request.class, idRequest);
      } else {
        request = GetRequest.getRequestFromRequestNumber(sess, requestNumber);
      }
      if (request != null) {
        // Make sure user has permission to view request
        if (!newRequest) {
          if (!this.getSecAdvisor().canRead(request)) {
            this.addInvalidField("perm", "Insufficient permission to access this request");
          } else if(request.getArchived() != null && request.getArchived().equals("Y")){
            this.addInvalidField("Request is archived", "This request has been archived.  If you need access to this request please contact GNomEx support.");
          }

        }

        if (this.isValid()) {

          reqNumber = request.getNumber();
          baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());

          Hibernate.initialize(request.getSamples());
          Hibernate.initialize(request.getHybridizations());
          Hibernate.initialize(request.getAnalysisExperimentItems());
          Hibernate.initialize(request.getSeqLibTreatments());
          Hibernate.initialize(request.getTopics());

          if (request.getTopics() != null) {
            Iterator<?> it = request.getTopics().iterator();
            while (it.hasNext()) {
              Topic t = (Topic) it.next();
              t.excludeMethodFromXML("getTopics");
              t.excludeMethodFromXML("getRequests");
              t.excludeMethodFromXML("getAnalyses");
              t.excludeMethodFromXML("getDataTracks");
              t.excludeMethodFromXML("getAppUser");
              t.excludeMethodFromXML("getLab");
            }

          }

          request.excludeMethodFromXML("getBillingItemList");
          request.excludeMethodFromXML("getTopics");

          // If user can write the request, show collaborators.
          if (this.getSecAdvisor().canUpdate(request)) {
            Hibernate.initialize(request.getCollaborators());
          } else {
            request.excludeMethodFromXML("getCollaborators");
          }

          if (!newRequest) {
            this.getSecAdvisor().flagPermissions(request);
          }

          // Set number of seq lanes per sample
          for (Iterator i5 = request.getSamples().iterator(); i5.hasNext();) {
            Sample s = (Sample) i5.next();
            int seqLaneCount = 0;
            for (Iterator i6 = request.getSequenceLanes().iterator(); i6.hasNext();) {
              SequenceLane seqLane = (SequenceLane) i6.next();
              if (seqLane.getIdSample().equals(s.getIdSample())) {
                seqLaneCount++;
              }
            }
            s.setSequenceLaneCount(seqLaneCount);
          }

          // Generate xml
          Document doc = new Document(new Element("OpenRequestList"));

          Element requestNode = request.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          requestNode.setAttribute("canUpdateVisibility", this.getSecAdvisor().canUpdateVisibility(request.getIdLab(), request.getIdAppUser()) ? "Y" : "N");

          BillingTemplate billingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
          if (billingTemplate != null) {
        	  requestNode.setAttribute("idBillingTemplate", billingTemplate.getIdBillingTemplate().toString());
          }

          if (request.isDNASeqExperiment().equals("Y") || request.isSequenomPlate()) {
            flagPlateInfo(newRequest, request, requestNode);
          }

          AppUser user = null;
          if (request.getIdAppUser() != null && request.getIdAppUser() != 0) {
            user = sess.load(AppUser.class, request.getIdAppUser());
          }

          String requestStatus = request.getCodeRequestStatus() != null ? DictionaryManager.getDisplay("hci.gnomex.model.RequestStatus", request.getCodeRequestStatus()) : "";
          requestNode.setAttribute("requestStatus", requestStatus);

          if (request.getProject() != null && request.getProject().getDescription() != null && !request.getProject().getDescription().equals("")) {
            requestNode.setAttribute("projectDescription", request.getProject().getDescription());
          } else {
            requestNode.setAttribute("projectDescription", "");
          }

          String accountNumberDisplay = "";
          if (request.getBillingAccount() != null) {
            accountNumberDisplay = request.getBillingAccount().getAccountNumberDisplay();
          } else if (billingTemplate != null) {
        	  Hibernate.initialize(billingTemplate.getItems());
        	  boolean firstAccount = true;
        	  for (BillingTemplateItem item : billingTemplate.getItems()) {
        		  BillingAccount account = sess.load(BillingAccount.class, item.getIdBillingAccount());
        		  if (firstAccount) {
        			  accountNumberDisplay = account.getAccountNumberDisplay();
        			  firstAccount = false;
        		  } else {
        			  accountNumberDisplay += ", " + account.getAccountNumberDisplay();
        		  }
        	  }
          }
          requestNode.setAttribute("accountNumberDisplay", accountNumberDisplay);

          if (user != null) {
            requestNode.setAttribute("email", user.getEmail() != null ? user.getEmail() : "");
            requestNode.setAttribute("phone", user.getPhone() != null ? user.getPhone() : "");
          }

          // Initialize attributes from request category
          RequestCategory requestCategory = null;
          if (request.getCodeRequestCategory() != null && !request.getCodeRequestCategory().equals("")) {
            requestCategory = dh.getRequestCategoryObject(request.getCodeRequestCategory());
            requestNode.setAttribute("icon", requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
            requestNode.setAttribute("type", requestCategory.getType() != null ? requestCategory.getType() : "");
            requestNode.setAttribute("requestCategory", requestCategory.getRequestCategory());
          }

          // Show sequence lanes, organized by multiplex group or flow cell channel
          if (request.getSequenceLanes().size() > 0) {
            Element multiplexLanesNode = new Element("multiplexSequenceLanes");
            for (Iterator i = request.getSequenceLanes().iterator(); i.hasNext();) {
              SequenceLane sl = (SequenceLane) i.next();
              if (sl.getIdGenomeBuildAlignTo() != null) {
                multiplexLanesNode.setAttribute("idGenomeBuildAlignTo", sl.getIdGenomeBuildAlignTo().toString());
                break;
              }
            }
            requestNode.addContent(multiplexLanesNode);
            SequenceLane.addMultiplexLaneNodes(multiplexLanesNode, request.getSequenceLanes(), request.getCreateDate());
          }

          // add organism at experiment level
          requestNode.setAttribute("idOrganism", "");
          requestNode.setAttribute("organismName", "");
          requestNode.setAttribute("otherOrganism", "");
          TreeSet<String> organismNameSet = new TreeSet<String>();
          if (request.getSamples().size() > 0) {
            String organismName = "";
            Integer idOrganism = null;
            String otherOrganism = "";
            for (Iterator i = request.getSamples().iterator(); i.hasNext();) {
              Sample s = (Sample) i.next();
              idOrganism = s.getIdOrganism();
              otherOrganism = s.getOtherOrganism();

              if (otherOrganism == null) {
                otherOrganism = "";
              }
              if (idOrganism != null) {
                organismName = dh.getOrganism(idOrganism);
                if (organismName == null) {
                  organismName = "";
                }
                if (organismName.equals("Other")) {
                  organismName += " (" + otherOrganism + ")";
                }
              }
              organismNameSet.add(organismName);
            }
            String organismNames = "";
            for (String o : organismNameSet) {
              if (organismNames.length() > 0) {
                organismNames += ",";
              }
              organismNames += o;
            }
            requestNode.setAttribute("idOrganism", idOrganism != null ? idOrganism.toString() : "");
            requestNode.setAttribute("organismName", organismNames);
            requestNode.setAttribute("otherOrganism", otherOrganism);
          }

          // Show list of property entries
          Element scParentNode = new Element("PropertyEntries");
          requestNode.addContent(scParentNode);
          boolean hasCCNumber = false;
          boolean hasSampleDescription = false;
          for (Iterator i = dh.getPropertyList().iterator(); i.hasNext();) {
            Property prop = (Property) i.next();

            if (prop.getForSample() == null || !prop.getForSample().equals("Y")) {
              continue;
            }

            Element peNode = new Element("PropertyEntry");
            PropertyEntry entry = null;

            for (Iterator i1 = request.getSamples().iterator(); i1.hasNext();) {
              Sample sample = (Sample) i1.next();
              if (sample.getCcNumber() != null && sample.getCcNumber().length() > 0) {
                hasCCNumber = true;
              }
              if (sample.getDescription() != null && sample.getDescription().length() > 0) {
                hasSampleDescription = true;
              }
              for (Iterator i2 = sample.getPropertyEntries().iterator(); i2.hasNext();) {
                PropertyEntry propEntry = (PropertyEntry) i2.next();
                if (propEntry.getIdProperty().equals(prop.getIdProperty())) {
                  entry = propEntry;
                  break;
                }
              }
            }

            // Skip if property has no values for samples and is not active.
            if (entry == null && prop.getIsActive().equals("N")) {
              continue;
            }

            // Note that requestCategory is null for new experiments as this is called before they select the request category.
            // for sequenom and iscan types we only include properties that explicitly apply to the request category.
            boolean autoSelect = false;
            boolean include = true;
            if (requestCategory != null && (requestCategory.getType().equals(RequestCategoryType.TYPE_ISCAN))) {
              include = false;
            }
            if (prop.getPlatformApplications() != null && prop.getPlatformApplications().size() > 0 && requestCategory != null) {
              include = false;
              for (Iterator i1 = prop.getPlatformApplications().iterator(); i1.hasNext();) {
                PropertyPlatformApplication pa = (PropertyPlatformApplication) i1.next();
                if (pa.getCodeRequestCategory().equals(request.getCodeRequestCategory()) && (pa.getApplication() == null || pa.getApplication().getCodeApplication().equals(request.getCodeApplication()))) {
                  include = true;
                  if (requestCategory.getType().equals(RequestCategoryType.TYPE_ISCAN)) {
                    autoSelect = true;
                  } else if (requestCategory.getType().equals(RequestCategoryType.TYPE_ISOLATION) || requestCategory.getType().equals(RequestCategoryType.TYPE_SEQUENOM) || requestCategory.getType().equals(RequestCategoryType.TYPE_CLINICAL_SEQUENOM)) {
                    autoSelect = !newRequest;
                  }
                  break;
                }
              }

            }
            if (requestCategory != null && prop.getIdCoreFacility() != null && !requestCategory.getIdCoreFacility().equals(prop.getIdCoreFacility())) {
              include = false;
            }
            if (!include) {
              continue;
            }

            peNode.setAttribute("idProperty", prop.getIdProperty().toString());
            peNode.setAttribute("name", prop.getName());
            peNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
            peNode.setAttribute("isSelected", (prop.getIsRequired() != null && prop.getIsRequired().equals("Y")) || entry != null || autoSelect ? "true" : "false");
            peNode.setAttribute("isRequired", (prop.getIsRequired() != null && prop.getIsRequired().equals("Y")) ? "true" : "false");
            peNode.setAttribute("sortOrder", prop.getSortOrder() != null ? prop.getSortOrder().toString() : "999999");
            peNode.setAttribute("isActive", prop.getIsActive() != null ? prop.getIsActive() : "Y");
            peNode.setAttribute("idCoreFacility", prop.getIdCoreFacility() != null ? prop.getIdCoreFacility().toString() : "");
            peNode.setAttribute("description", prop.getDescription() != null ? prop.getDescription() : "");

            scParentNode.addContent(peNode);

          }
          requestNode.setAttribute("hasCCNumber", hasCCNumber ? "Y" : "N");
          requestNode.setAttribute("hasSampleDescription", hasSampleDescription ? "Y" : "N");

          // Show list of request properties
          Element rpParentNode = new Element("RequestProperties");
          requestNode.addContent(rpParentNode);
          for (Iterator i = dh.getPropertyList().iterator(); i.hasNext();) {
            Property prop = (Property) i.next();

            if (prop.getForRequest() == null || !prop.getForRequest().equals("Y")) {
              continue;
            }

            Element peNode = new Element("PropertyEntry");
            PropertyEntry entry = null;

            if (request.getPropertyEntries() != null) {
              for (Iterator i2 = request.getPropertyEntries().iterator(); i2.hasNext();) {
                PropertyEntry propEntry = (PropertyEntry) i2.next();
                if (propEntry.getIdProperty().equals(prop.getIdProperty())) {
                  entry = propEntry;
                  break;
                }
              }
            }

            if (entry == null && prop.getIsActive().equals("N")) {
              continue;
            }

            // Note that requestCategory is null for new experiments as this is called before they select the request category.
            boolean include = true;
            if (requestCategory != null && prop.getIdCoreFacility() != null && !requestCategory.getIdCoreFacility().equals(prop.getIdCoreFacility())) {
              include = false;
            }
            if (include && prop.getPlatformApplications() != null && prop.getPlatformApplications().size() > 0 && requestCategory != null) {
              include = false;
              for (Iterator i1 = prop.getPlatformApplications().iterator(); i1.hasNext();) {
                PropertyPlatformApplication pa = (PropertyPlatformApplication) i1.next();
                if (pa.getCodeRequestCategory().equals(request.getCodeRequestCategory()) && (pa.getApplication() == null || pa.getApplication().getCodeApplication().equals(request.getCodeApplication()))) {
                  include = true;
                  break;
                }
              }

            }
            if (!include) {
              continue;
            }

            peNode.setAttribute("idProperty", prop.getIdProperty().toString());
            peNode.setAttribute("idPropertyEntry", entry != null ? entry.getIdPropertyEntry().toString() : "");
            peNode.setAttribute("name", prop.getName());
            peNode.setAttribute("description", prop.getDescription() != null ? prop.getDescription() : "");
            peNode.setAttribute("value", entry != null && entry.getValue() != null ? entry.getValue() : "");
            peNode.setAttribute("codePropertyType", prop.getCodePropertyType());
            peNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
            peNode.setAttribute("isRequired", prop.getIsRequired() != null ? prop.getIsRequired() : "N");
            peNode.setAttribute("isSelected", "true");
            peNode.setAttribute("sortOrder", prop.getSortOrder() != null ? prop.getSortOrder().toString() : "999999");
            peNode.setAttribute("isActive", prop.getIsActive() != null ? prop.getIsActive() : "Y");
            peNode.setAttribute("idCoreFacility", prop.getIdCoreFacility() != null ? prop.getIdCoreFacility().toString() : "");

            Property.appendEntryContentXML(prop,entry,peNode);


            rpParentNode.addContent(peNode);

          }

          // Show list of seq lib treatments
          Element stParentNode = new Element("SeqLibTreatmentEntries");
          requestNode.addContent(stParentNode);
          for (Iterator i1 = dh.getSeqLibTreatments().iterator(); i1.hasNext();) {
            Object de = i1.next();
            if (de instanceof NullDictionaryEntry) {
              continue;
            }
            SeqLibTreatment st = (SeqLibTreatment) de;

            if (st.getIsActive() != null && st.getIsActive().equalsIgnoreCase("N")) {
              continue;
            }

            Element stNode = (Element) st.toXMLDocument(null).getRootElement().clone();
            stParentNode.addContent(stNode);

            boolean isSelected = false;
            for (Iterator i2 = request.getSeqLibTreatments().iterator(); i2.hasNext();) {
              SeqLibTreatment theSeqLibTreatment = (SeqLibTreatment) i2.next();
              if (theSeqLibTreatment.getIdSeqLibTreatment().equals(st.getIdSeqLibTreatment())) {
                isSelected = true;
                break;
              }
            }
            stNode.setAttribute("isSelected", isSelected ? "true" : "false");
          }

          // Show list of protocols used on this experiment
          Element protocolsNode = new Element("protocols");
          requestNode.addContent(protocolsNode);
          Boolean isFirst = true;
          for (Iterator i0 = request.getLabeledSamples().iterator(); i0.hasNext();) {
            LabeledSample ls = (LabeledSample) i0.next();
            if (ls.getIdLabelingProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", ls.getIdLabelingProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.LabelingProtocol");
              protocolNode.setAttribute("name", dh.getLabelingProtocol(ls.getIdLabelingProtocol()));
              protocolNode.setAttribute("label", isFirst ? "Label Protocols" : "");
              isFirst = false;
              break;
            }
          }
          isFirst = true;
          for (Iterator i1 = request.getHybridizations().iterator(); i1.hasNext();) {
            Hybridization hyb = (Hybridization) i1.next();
            if (hyb.getIdHybProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdHybProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.HybProtocol");
              protocolNode.setAttribute("name", dh.getHybProtocol(hyb.getIdHybProtocol()));
              protocolNode.setAttribute("label", isFirst ? "Hyb. Protocols" : "");
              isFirst = false;
              break;
            }
          }
          isFirst = true;
          for (Iterator i2 = request.getHybridizations().iterator(); i2.hasNext();) {
            Hybridization hyb = (Hybridization) i2.next();
            if (hyb.getIdScanProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdScanProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.ScanProtocol");
              protocolNode.setAttribute("name", dh.getScanProtocol(hyb.getIdScanProtocol()));
              protocolNode.setAttribute("label", isFirst ? "Scan Protocols" : "");
              isFirst = false;
              break;
            }
          }
          isFirst = true;
          for (Iterator i3 = request.getHybridizations().iterator(); i3.hasNext();) {
            Hybridization hyb = (Hybridization) i3.next();
            if (hyb.getIdFeatureExtractionProtocol() != null) {
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", hyb.getIdFeatureExtractionProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.FeatureExtractionProtocol");
              protocolNode.setAttribute("name", dh.getFeatureExtractionProtocol(hyb.getIdFeatureExtractionProtocol()));
              protocolNode.setAttribute("label", isFirst ? "Extract. Protocols" : "");
              isFirst = false;
              break;
            }
          }
          TreeMap<String, String> map2 = new TreeMap<String, String>();
          isFirst = true;
          for (Iterator i4 = request.getSamples().iterator(); i4.hasNext();) {
            Sample s = (Sample) i4.next();
            if (s.getCodeBioanalyzerChipType() != null && !(map2.containsKey(s.getCodeBioanalyzerChipType()))) {
              map2.put(s.getCodeBioanalyzerChipType(), "1");
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", s.getCodeBioanalyzerChipType());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.BioanalyzerChipType");
              protocolNode.setAttribute("name", dh.getBioanalyzerChipType(s.getCodeBioanalyzerChipType()));
              protocolNode.setAttribute("label", isFirst ? "QC Protocols" : "");
              isFirst = false;
            }
          }
          TreeMap<Integer, Sample> map = new TreeMap<Integer, Sample>();
          isFirst = true;
          for (Iterator i4 = request.getSamples().iterator(); i4.hasNext();) {
            Sample s = (Sample) i4.next();
            if (s.getIdSeqLibProtocol() != null && !(map.containsKey(s.getIdSeqLibProtocol()))) {
              map.put(s.getIdSeqLibProtocol(), s);
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", s.getIdSeqLibProtocol().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.SeqLibProtocol");

              String protocolName = dh.getSeqLibProtocol(s.getIdSeqLibProtocol());
              if (protocolName == null) {
                SeqLibProtocol slp = sess.load(SeqLibProtocol.class, s.getIdSeqLibProtocol());
                protocolName = slp.getDisplay();
              }
              protocolNode.setAttribute("name", protocolName != null ? protocolName : "");
              protocolNode.setAttribute("label", isFirst ? "Lib. Prep. Protocols" : "");

              String fivePrime = dh.getSeqLibProtocolObject(s.getIdSeqLibProtocol()).getAdapterSequenceFivePrime();
              protocolNode.setAttribute("Adapter5Prime", fivePrime != null ? fivePrime : "");

              String threePrime = dh.getSeqLibProtocolObject(s.getIdSeqLibProtocol()).getAdapterSequenceThreePrime();
              protocolNode.setAttribute("Adapter3Prime", threePrime != null ? threePrime : "");
              isFirst = false;
            }
          }

          TreeMap<Integer, Integer> map1 = new TreeMap<Integer, Integer>();
          isFirst = true;
          for (Iterator i5 = request.getSequenceLanes().iterator(); i5.hasNext();) {
            SequenceLane seq = (SequenceLane) i5.next();
            if (seq.getIdNumberSequencingCyclesAllowed() != null && !map1.containsKey(seq.getIdNumberSequencingCyclesAllowed())) {
              map1.put(seq.getIdNumberSequencingCyclesAllowed(), 1);
              Element protocolNode = new Element("Protocol");
              protocolsNode.addContent(protocolNode);
              protocolNode.setAttribute("idProtocol", seq.getIdNumberSequencingCyclesAllowed().toString());
              protocolNode.setAttribute("protocolClassName", "hci.gnomex.model.NumberSequencingCyclesAllowed");
              protocolNode.setAttribute("name", dh.getIlluminaSequencingProtocol(seq.getIdNumberSequencingCyclesAllowed()));
              protocolNode.setAttribute("label", isFirst ? "Seq. Protocols" : "");
              isFirst = false;
            }
          }

          // If user has write permission, show billing items for this request
          // Organize split accounts by lab
          if (this.getSecAdvisor().canUpdate(request)) {
            Element billingItemsNode = new Element("billingItems");
            requestNode.addContent(billingItemsNode);
            TreeMap billingItemMap = new TreeMap();
            for (Iterator i = request.getBillingItemList(sess).iterator(); i.hasNext();) {
              BillingItem bi = (BillingItem) i.next();
              String key = bi.getLabName() + "_%%%_" + bi.getAccountNameAndNumber();
              List billingItems = (List) billingItemMap.get(key);
              if (billingItems == null) {
                billingItems = new ArrayList();
                billingItemMap.put(key, billingItems);
              }
              billingItems.add(bi);
            }
            for (Iterator i = billingItemMap.keySet().iterator(); i.hasNext();) {
              String key = (String) i.next();
              String keyTokens[] = key.split("_%%%_");
              List billingItems = (List) billingItemMap.get(key);
              Element billingLabNode = new Element("BillingLab");
              billingItemsNode.addContent(billingLabNode);
              billingLabNode.setAttribute("labName", keyTokens[0]);
              billingLabNode.setAttribute("accountName", keyTokens[1]);

              BigDecimal labInvoicePrice = new BigDecimal(0);
              for (Iterator i2 = billingItems.iterator(); i2.hasNext();) {
                BillingItem theBillingItem = (BillingItem) i2.next();
                theBillingItem.excludeMethodFromXML("getLabName");
                theBillingItem.excludeMethodFromXML("getAccountName");
                billingLabNode.addContent(theBillingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement());
                labInvoicePrice = labInvoicePrice.add(theBillingItem.getInvoicePrice() != null ? theBillingItem.getInvoicePrice() : new BigDecimal(0));
              }
              billingLabNode.setAttribute("invoicePrice", NumberFormat.getCurrencyInstance().format(labInvoicePrice.doubleValue()));
            }

          }

          // Show files uploads that are in the staging area.
          // Only show these files if user has write permissions.
          if (showUploads.equals("Y") && this.getSecAdvisor().canUploadData(request)) {
            Element requestUploadNode = new Element("RequestUpload");
            requestNode.addContent(requestUploadNode);
            requestUploadNode.setAttribute("canDelete", "Y");
            requestUploadNode.setAttribute("canRename", "Y");
            requestNode.setAttribute("canDelete", "N");
            requestNode.setAttribute("canRename", "N");
            String key = request.getKey(Constants.UPLOAD_STAGING_DIR);
            GetRequestDownloadList.addExpandedFileNodes(sess, serverName, null, requestNode, requestUploadNode, request.getNumber(), key, request.getCodeRequestCategory(), dh, false);
          }

          // Default to not breaking out samples by plates.
          requestNode.setAttribute("hasPlates", "N");

          // get list of sample ids for the request. Used in querying PlateWell.
          ArrayList<Integer> sampleIds = new ArrayList<Integer>();
          for (Iterator i1 = request.getSamples().iterator(); i1.hasNext();) {
            Sample sample = (Sample) i1.next();
            sampleIds.add(sample.getIdSample());
          }

          if (request.getCodeRequestCategory() != null && (request.getCodeRequestCategory().equals(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY) || requestCategory.getType().equals(RequestCategoryType.TYPE_SEQUENOM)) && request.getSamples().size() > 0) {
            String plateQueryString = "SELECT pw from PlateWell pw left join pw.plate p where p.codePlateType='SOURCE' and pw.idSample in (:ids) Order By pw.idSample";
            Query plateQuery = sess.createQuery(plateQueryString);
            plateQuery.setParameterList("ids", sampleIds);
            List wells = plateQuery.list();
            if (wells.size() > 0) {
              // has plates, so it's not tubes.
              requestNode.setAttribute("containerType", "PLATE");

              // augment samples for plates.
              List samples = requestNode.getChild("samples").getChildren("Sample");
              for (Iterator i1 = samples.iterator(); i1.hasNext();) {
                Element sampleNode = (Element) i1.next();
                for (Iterator i2 = wells.iterator(); i2.hasNext();) {
                  PlateWell pw = (PlateWell) i2.next();
                  if (pw.getIdSample().toString().equals(sampleNode.getAttributeValue("idSample"))) {
                    sampleNode.setAttribute("wellName", pw.getWellName());
                    sampleNode.setAttribute("idPlateWell", pw.getIdPlateWell().toString());
                    sampleNode.setAttribute("idPlate", pw.getIdPlate().toString());
                    sampleNode.setAttribute("plateName", pw.getPlate().getLabel());
                  }
                }
              }
              // Sort samples by plate and well
              ArrayList sampleArray = new ArrayList(samples);
              requestNode.getChild("samples").setContent(null);
              Collections.sort(sampleArray, new PlateAndWellComparator());
              requestNode.getChild("samples").setContent(sampleArray);
            } else {
              requestNode.setAttribute("containerType", "TUBE");
            }
          }
          if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY)) {
            // biuld the primer list and well names for the samples
            HashMap<String, Primer> primerList = new HashMap<String, Primer>();
            HashMap<Integer, String> sampleWellMap = new HashMap<Integer, String>();
            TreeMap<Integer, String> primerNumberMap = new TreeMap<Integer, String>();
            // order by is important to preserve order of primers
            String primerQueryString = "SELECT pw from PlateWell pw join pw.plate pl where pw.idSample in (:ids) and pl.codePlateType='SOURCE' order by pw.idPlateWell";
            Query primerQuery = sess.createQuery(primerQueryString);
            primerQuery.setParameterList("ids", sampleIds);
            List primers = primerQuery.list();
            String plateName = "";
            Integer nextPrimerNumber = 0;
            for (Iterator i = primers.iterator(); i.hasNext();) {
              PlateWell pw = (PlateWell) i.next();
              if (pw.getPrimer() != null && !primerList.containsKey(pw.getPrimer().getName())) {
                primerList.put(pw.getPrimer().getName(), pw.getPrimer());
                nextPrimerNumber++;
                primerNumberMap.put(nextPrimerNumber, pw.getPrimer().getName());
              }
              sampleWellMap.put(pw.getIdSample(), pw.getWellName());
              if (plateName.length() == 0 && pw.getPlate().getLabel() != null) {
                plateName = pw.getPlate().getLabel();
              }
            }
            // This shouldn't happen now, but just in case.
            if (plateName.length() == 0) {
              plateName = "Plate 1";
            }

            String primerListString = "";
            for (Integer pNumber : primerNumberMap.keySet()) {
              String pName = primerNumberMap.get(pNumber);
              if (primerListString.length() > 0) {
                primerListString += ", ";
              }
              primerListString += pName;
            }
            requestNode.setAttribute("primerList", primerListString);

            // Add primers to request
            Element primersNode = new Element("primers");
            for (Integer pNumber : primerNumberMap.keySet()) {
              String primerKey = primerNumberMap.get(pNumber);
              Primer primer = primerList.get(primerKey);
              Element primerNode = new Element("Primer");
              primerNode.setAttribute("name", primer.getName());
              primerNode.setAttribute("id", primer.getIdPrimer().toString());
              primerNode.setAttribute("number", pNumber.toString());
              primersNode.addContent(primerNode);
            }
            requestNode.addContent(primersNode);

            // add well names to samples
            List samples = requestNode.getChild("samples").getChildren("Sample");
            for (Iterator i1 = samples.iterator(); i1.hasNext();) {
              Element sampleNode = (Element) i1.next();
              sampleNode.setAttribute("wellName", sampleWellMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample"))));
              sampleNode.setAttribute("plateName", plateName);
            }
          }

          if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)) {
            // biuld the assay list and well names for the samples
            TreeMap<String, Assay> assayList = new TreeMap<String, Assay>();
            HashMap<Integer, String> sampleWellMap = new HashMap<Integer, String>();
            String assayQueryString = "SELECT pw from PlateWell pw join pw.plate pl where pw.idSample in (:ids) and pl.codePlateType='SOURCE'";
            Query assayQuery = sess.createQuery(assayQueryString);
            assayQuery.setParameterList("ids", sampleIds);
            List assays = assayQuery.list();
            HashMap<Integer, ArrayList<String>> sampleAssayMap = new HashMap<Integer, ArrayList<String>>();
            String plateName = "";
            for (Iterator i = assays.iterator(); i.hasNext();) {
              PlateWell pw = (PlateWell) i.next();
              if (pw.getAssay() != null) {
                assayList.put(pw.getAssay().getName(), pw.getAssay());
              }
              sampleWellMap.put(pw.getIdSample(), pw.getWellName());
              ArrayList<String> sampleAssays = sampleAssayMap.get(pw.getIdSample());
              if (sampleAssays == null) {
                sampleAssays = new ArrayList<String>();
              }
              if (pw.getAssay() != null) {
                sampleAssays.add(pw.getAssay().getName());
              }
              sampleAssayMap.put(pw.getIdSample(), sampleAssays);
              if (plateName.length() == 0 && pw.getPlate().getLabel() != null) {
                plateName = pw.getPlate().getLabel();
              }
            }

            // Shouldn't happen -- but just in case.
            if (plateName.length() == 0) {
              plateName = "Plate 1";
            }

            String assayListString = "";
            for (String aName : assayList.keySet()) {
              if (assayListString.length() > 0) {
                assayListString += ", ";
              }
              assayListString += aName;
            }
            requestNode.setAttribute("assayList", assayListString);

            // Add selected assays to request
            Element selectedAssaysNode = new Element("assays");
            for (String assayName : assayList.keySet()) {
              Assay assay = assayList.get(assayName);
              Element assayNode = new Element("Assay");
              assayNode.setAttribute("name", assayName);
              assayNode.setAttribute("id", assay.getIdAssay().toString());
              selectedAssaysNode.addContent(assayNode);
            }
            requestNode.addContent(selectedAssaysNode);

            // add well names and assays to samples
            List samples = requestNode.getChild("samples").getChildren("Sample");
            for (Iterator i1 = samples.iterator(); i1.hasNext();) {
              Element sampleNode = (Element) i1.next();
              sampleNode.setAttribute("wellName", this.getNonNullString(sampleWellMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample")))));
              sampleNode.setAttribute("plateName", plateName);
              ArrayList<String> sampleAssays = sampleAssayMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample")));
              for (Iterator assayIter = assayList.keySet().iterator(); assayIter.hasNext();) {
                String assay = (String) assayIter.next();
                String assayValue = "";
                for (Iterator saIter = sampleAssays.iterator(); saIter.hasNext();) {
                  String sampAssay = (String) saIter.next();
                  if (sampAssay.equals(assay)) {
                    assayValue = "Y";
                    break;
                  }
                }
                sampleNode.setAttribute("hasAssay" + assay, assayValue);
              }
            }
          }

          if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY)) {
            // build source well and plate names for the samples
            HashMap<Integer, String> sampleSourceWellMap = new HashMap<Integer, String>();
            HashMap<Integer, String> sampleSourcePlateMap = new HashMap<Integer, String>();
            TreeMap<String, String> destinationPlates = new TreeMap<String, String>();

            String sourceQueryString = "SELECT pw from PlateWell pw join pw.plate pl where pw.idSample in (:ids) and pl.codePlateType='SOURCE'";
            Query sourceQuery = sess.createQuery(sourceQueryString);
            sourceQuery.setParameterList("ids", sampleIds);
            List sourceWells = sourceQuery.list();
            for (Iterator srcIter = sourceWells.iterator(); srcIter.hasNext();) {
              PlateWell pw = (PlateWell) srcIter.next();
              sampleSourceWellMap.put(pw.getIdSample(), pw.getWellName());
              sampleSourcePlateMap.put(pw.getIdSample(), pw.getPlate().getLabel());
              destinationPlates.put(pw.getPlate().getLabel(), pw.getPlate().getLabel());
            }

            // build destination well names for the samples
            HashMap<Integer, String> sampleDestinationWellMap = new HashMap<Integer, String>();
            String destinationQueryString = "SELECT pw from PlateWell pw join pw.plate pl where pw.idSample in (:ids) and pl.codePlateType='REACTION' and pw.redoFlag='N'";
            Query destinationQuery = sess.createQuery(destinationQueryString);
            destinationQuery.setParameterList("ids", sampleIds);
            List destinationWells = destinationQuery.list();
            Integer numDestinationWells = 96;
            for (Iterator destIter = destinationWells.iterator(); destIter.hasNext();) {
              PlateWell pw = (PlateWell) destIter.next();
              sampleDestinationWellMap.put(pw.getIdSample(), pw.getWellName());
              if (pw.getRow().compareTo("H") > 0 || pw.getCol() > 12) {
                numDestinationWells = 384;
              }
            }
            requestNode.setAttribute("numDestinationWells", numDestinationWells.toString());

            // add well plate names to samples
            List samples = requestNode.getChild("samples").getChildren("Sample");
            for (Iterator i1 = samples.iterator(); i1.hasNext();) {
              Element sampleNode = (Element) i1.next();
              sampleNode.setAttribute("sourceWell", sampleSourceWellMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample"))));
              sampleNode.setAttribute("sourcePlate", sampleSourcePlateMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample"))));
              sampleNode.setAttribute("destinationWell", sampleDestinationWellMap.get(Integer.parseInt(sampleNode.getAttributeValue("idSample"))));
            }

            // Add plate list
            Element platesNode = new Element("cherryPlateList");
            for (String plateName : destinationPlates.keySet()) {
              Element plateNode = new Element("Plate");
              plateNode.setAttribute("name", plateName);
              platesNode.addContent(plateNode);
            }
            requestNode.addContent(platesNode);

          }

          if (request.getCodeRequestCategory() != null && request.getCodeRequestCategory().equals(RequestCategory.ISCAN_REQUEST_CATEGORY) && request.getSamples().size() > 0) {

            TreeMap<String, String> sourcePlates = new TreeMap<String, String>();

            String plateQueryString = "SELECT pw from PlateWell pw left join pw.plate p where p.codePlateType='SOURCE' and pw.idSample in (:ids) Order By pw.idSample";
            Query plateQuery = sess.createQuery(plateQueryString);
            plateQuery.setParameterList("ids", sampleIds);

            List wells = plateQuery.list();
            if (wells.size() > 0) {
              // has plates, so it's not tubes.
              requestNode.setAttribute("containerType", "PLATE");

              // augment samples for plates.
              List samples = requestNode.getChild("samples").getChildren("Sample");
              for (Iterator i1 = samples.iterator(); i1.hasNext();) {
                Element sampleNode = (Element) i1.next();
                for (Iterator i2 = wells.iterator(); i2.hasNext();) {
                  PlateWell pw = (PlateWell) i2.next();
                  if (pw.getIdSample().toString().equals(sampleNode.getAttributeValue("idSample"))) {
                    sampleNode.setAttribute("wellName", pw.getWellName());
                    sampleNode.setAttribute("idPlateWell", pw.getIdPlateWell().toString());
                    sampleNode.setAttribute("idPlate", pw.getIdPlate().toString());
                    sampleNode.setAttribute("plateName", pw.getPlate().getLabel());
                    sourcePlates.put(pw.getPlate().getLabel(), pw.getPlate().getLabel());
                  }
                }
              }
              // Sort samples by plate and well
              ArrayList sampleArray = new ArrayList(samples);
              requestNode.getChild("samples").setContent(null);
              Collections.sort(sampleArray, new PlateAndWellComparator());
              requestNode.getChild("samples").setContent(sampleArray);

              Element platesNode = new Element("iScanPlateList");
              for (String plateName : sourcePlates.keySet()) {
                Element plateNode = new Element("Plate");
                plateNode.setAttribute("name", plateName);
                platesNode.addContent(plateNode);
              }
              requestNode.addContent(platesNode);

            } else {
              requestNode.setAttribute("containerType", "TUBE");
            }
          }

          // Augment sample with sample type name so that imports can lookup idSampeType based
          // on the name. Do the same for organism. Also add in codeApplication from the request
          // so it can be displayed for experiments with application but no seq lib protocol.
          for (Iterator i1 = requestNode.getChild("samples").getChildren("Sample").iterator(); i1.hasNext();) {
            Element sampleNode = (Element) i1.next();
            String idSampleTypeString = sampleNode.getAttributeValue("idSampleType");
            if (idSampleTypeString != null && !idSampleTypeString.equals("")) {
              Integer idSampleType = Integer.parseInt(idSampleTypeString);
              String sampleTypeName = dh.getSampleType(idSampleType);
              if (sampleTypeName != null) {
                sampleNode.setAttribute("sampleType", sampleTypeName);
              }
            }
            String idOrganismString = sampleNode.getAttributeValue("idOrganism");
            if (idOrganismString != null && !idOrganismString.equals("")) {
              Integer idOrganism = Integer.parseInt(idOrganismString);
              String organismName = dh.getOrganism(idOrganism);
              if (organismName != null) {
                sampleNode.setAttribute("organism", organismName);
              }
            }
            sampleNode.setAttribute("codeApplication", request.getCodeApplication() == null ? "" : request.getCodeApplication());
          }

          // Augment sequence lane node with organism and genome build names.
          if (requestNode.getChild("sequenceLanes") != null) {
            for (Iterator i1 = requestNode.getChild("sequenceLanes").getChildren("SequenceLane").iterator(); i1.hasNext();) {
              Element sequenceLaneNode = (Element) i1.next();

              String idOrganismString = sequenceLaneNode.getAttributeValue("idOrganism");
              if (idOrganismString != null && !idOrganismString.equals("")) {
                Integer idOrganism = Integer.parseInt(idOrganismString);
                String organismName = dh.getOrganism(idOrganism);
                sequenceLaneNode.setAttribute("organism", organismName);
              }
              String idGenomeBuildString = sequenceLaneNode.getAttributeValue("idGenomeBuildAlignTo");
              if (idGenomeBuildString != null && !idGenomeBuildString.equals("")) {
                Integer idGenomeBuild = Integer.parseInt(idGenomeBuildString);
                String genomeBuildName = dh.getGenomeBuild(idGenomeBuild);
                sequenceLaneNode.setAttribute("genomeBuild", genomeBuildName);
              }
            }
          }

          // Append related analysis and data tracks and topics
          if (!newRequest) {
            appendRelatedNodes(this.getSecAdvisor(), sess, request, requestNode);
          }

          // Append workflow progress info
          if (!newRequest) {
            appendWorkflowStatusNodes(request, requestNode);
          }

          // Append valid submitters from other cores.
          if (!newRequest) {
            appendSubmittersFromOtherCores(sess, request, requestNode);
          }

          doc.getRootElement().addContent(requestNode);

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (UnknownPermissionException e) {
      log.error("An exception has occurred in GetRequest ", e);
      throw new RollBackCommandException(e.getMessage());
    } catch (NamingException e) {
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (SQLException e) {
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e) {
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {

      }
    }

    String dinfo = "GetRequest (" + this.getUsername() + " - " + reqNumber + "), ";
    Util.showTime(startTime, dinfo);

    return this;
  }

  public static Request getRequestFromRequestNumber(Session sess, String requestNumber) {
    // Make sure dictionary is loaded.
    PropertyDictionaryHelper.getInstance(sess);

    Request request = null;
    String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
    StringBuffer buf = new StringBuffer("SELECT req from Request as req where req.number like '" + requestNumberBase + "%' OR req.number = '" + requestNumberBase + "'");
    List requests = sess.createQuery(buf.toString()).list();

    if (requests.size() == 0 && requestNumberBase.indexOf("R") == -1) {
      // If nothing returned then try with "R" appended
      requestNumberBase = requestNumberBase + "R";
      buf = new StringBuffer("SELECT req from Request as req where req.number like '" + requestNumberBase + "%' OR req.number = '" + requestNumberBase + "'");
      requests = sess.createQuery(buf.toString()).list();
    }
    if (requests.size() > 0) {
      request = (Request) requests.get(0);
    }
    return request;
  }

  @SuppressWarnings("unchecked")
  private static void appendWorkflowStatusNodes(Request request, Element requestNode) throws Exception {
    if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
      return;
    }
    Element statusNode = new Element("workflowStatus");
    requestNode.addContent(statusNode);
    if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory())) {
      if (request.isLibPrepByCore()) {
        appendStepNode(statusNode, "Submitted");
        appendStepNode(statusNode, "Sample QC");
        appendStepNode(statusNode, "Library Prep");
        appendStepNode(statusNode, "Ready to Sequence");
        appendStepNode(statusNode, "Sequence in Progress");
        appendStepNode(statusNode, "Data Available");
        statusNode.setAttribute("numberOfSteps", "6");
      } else {
        appendStepNode(statusNode, "Submitted");
        appendStepNode(statusNode, "Library QC");
        appendStepNode(statusNode, "Ready to Sequence");
        appendStepNode(statusNode, "Sequence in Progress");
        appendStepNode(statusNode, "Data Available");
        statusNode.setAttribute("numberOfSteps", "5");
      }
    } else if (RequestCategory.isMicroarrayRequestCategory(request.getCodeRequestCategory())) {
      appendStepNode(statusNode, "Submitted");
      appendStepNode(statusNode, "Sample QC");
      appendStepNode(statusNode, "Labeling");
      appendStepNode(statusNode, "Hybridization");
      appendStepNode(statusNode, "Extraction");
      statusNode.setAttribute("numberOfSteps", "5");
    } else if (request.getRequestCategory().getType().equals(RequestCategoryType.TYPE_QC)) {
      appendStepNode(statusNode, "Submitted");
      appendStepNode(statusNode, "Sample QC");
      statusNode.setAttribute("numberOfSteps", "2");
    } else {
      return;
    }

    TreeMap<String, Integer> workflowStepHash = new TreeMap<String, Integer>();
    for (Sample sample : (Set<Sample>) request.getSamples()) {
      String stepNumber = sample.getWorkflowStep();
      Integer count = workflowStepHash.get(stepNumber);
      if (count == null) {
        count = new Integer(1);
      } else {
        count = new Integer(count.intValue() + 1);
      }
      workflowStepHash.put(stepNumber, count);
    }

    for (String workflowStep : workflowStepHash.keySet()) {
      Element stepNode = new Element("Progress");
      Integer count = workflowStepHash.get(workflowStep);

      boolean partial = false;
      if (workflowStep.contains(",")) {
        partial = true;
        String tokens[] = workflowStep.split(",");
        workflowStep = tokens[0];
      }
      stepNode.setAttribute("stepNumber", workflowStep);
      stepNode.setAttribute("title", count.toString() + (count.intValue() > 1 ? " Samples" : " Sample") + (partial ? " (partial progress)" : ""));
      stepNode.setAttribute("partial", partial ? "Y" : "N");
      statusNode.addContent(stepNode);
    }

  }

  private static void appendStepNode(Element statusNode, String name) {
    Element node1 = new Element("Step");
    node1.setAttribute("name", name);
    statusNode.addContent(node1);
  }

  /*
   * Append related experiments, data tracks, and topics.
   */
  @SuppressWarnings("unchecked")
  private static void appendRelatedNodes(SecurityAdvisor secAdvisor, Session sess, Request request, Element node) throws Exception {
    Element relatedNode = new Element("relatedObjects");
    relatedNode.setAttribute("label", "Related Items");
    node.addContent(relatedNode);
    HashMap<Integer, Integer> analysisHash = new HashMap<Integer, Integer>();
    for (AnalysisExperimentItem x : (Set<AnalysisExperimentItem>) request.getAnalysisExperimentItems()) {
      if (!analysisHash.containsKey(x.getAnalysis().getIdAnalysis())) {
        Element analysisNode = x.getAnalysis().appendBasicXML(secAdvisor, relatedNode);

        if (x.getAnalysis().getFiles().size() > 0) {
          GetAnalysis.appendDataTrackNodes(secAdvisor, sess, x.getAnalysis(), analysisNode);
        }

        analysisHash.put(x.getAnalysis().getIdAnalysis(), null);
      }
    }

    // Append the parent topics (and the contents of the topic) XML
    Element relatedTopicNode = new Element("relatedTopics");
    relatedTopicNode.setAttribute("label", "Related Topics");
    node.addContent(relatedTopicNode);
    Topic.appendParentTopicsXML(secAdvisor, relatedTopicNode, request.getTopics());

  }

  private void appendSubmittersFromOtherCores(Session sess, Request request, Element node) throws Exception {
    Element subNode = new Element("submitterFromOtherCores");
    node.addContent(subNode);

    String str = "SELECT DISTINCT au FROM AppUser au JOIN au.coreFacilitiesICanSubmitTo core where core.idCoreFacility=:idCoreFacility";
    Query query = sess.createQuery(str);
    query.setParameter("idCoreFacility", request.getIdCoreFacility());
    List<AppUser> l = query.list();
    for (AppUser au : l) {
      Element auNode = au.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      if (au.getIdAppUser() == null) {
        auNode.setAttribute("value", "-1");
      } else {
        auNode.setAttribute("value", au.getIdAppUser().toString());
      }
      auNode.setAttribute("display", au.getDisplayName());
      subNode.addContent(auNode);
    }
  }

  private void flagPlateInfo(boolean isNewRequest, Request request, Element requestNode) {

    boolean onReactionPlate = false;
    boolean hasPendingRedo = false;

    if (!isNewRequest) {
      // Find out if the samples are on a reaction plate. If they
      // are, flag the request so that appropriate warnings
      // can be displayed if the data is changed.

      Element sNode = new Element("Sample");
      StringBuffer redoSamples = new StringBuffer();

      for (Sample s : (Set<Sample>) request.getSamples()) {

        // Find the sample node
        List samples = requestNode.getChild("samples").getChildren("Sample");
        for (Iterator i1 = samples.iterator(); i1.hasNext();) {
          Element sampleNode = (Element) i1.next();
          if (s.getIdSample().toString().equals(sampleNode.getAttributeValue("idSample"))) {
            sNode = sampleNode;
            break;
          }
        }

        boolean sRedoFlag = false;
        TreeMap<Integer, Plate> rxnPlates = new TreeMap<Integer, Plate>();

        for (PlateWell well : (Set<PlateWell>) s.getWells()) {

          if (well.getRedoFlag() != null && well.getRedoFlag().equals("Y")) {
            sRedoFlag = true;
          }

          // Only check source wells for redo. The reaction well will be set to redo and not toggle back.
          if (well.getPlate() == null || well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
            if (well.getRedoFlag() != null && well.getRedoFlag().equals("Y")) {
              hasPendingRedo = true;
              if (redoSamples.length() > 0) {
                redoSamples.append(", ");
              }
              redoSamples.append(s.getName());
            }

          } else if (well.getPlate() != null && well.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
            onReactionPlate = true;
            rxnPlates.put(well.getIdPlate(), well.getPlate());
          }

        }

        String rxnPlateNames = "";
        for (Integer idPlate : rxnPlates.keySet()) {
          Plate plate = rxnPlates.get(idPlate);
          if (rxnPlateNames.length() > 0) {
            rxnPlateNames += ", ";
          }
          rxnPlateNames += plate.getLabel();
        }

        sNode.setAttribute("reactionPlateNames", rxnPlateNames);
        sNode.setAttribute("redoFlag", sRedoFlag ? "Y" : "N");
      }

      requestNode.setAttribute("redoSampleNames", hasPendingRedo ? redoSamples.toString() : "");
    }

    requestNode.setAttribute("hasPendingRedo", hasPendingRedo ? "Y" : "N");
    requestNode.setAttribute("onReactionPlate", onReactionPlate ? "Y" : "N");

  }

  private static class PlateAndWellComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      String p1 = ((Element) o1).getAttributeValue("plateName");
      String p2 = ((Element) o2).getAttributeValue("plateName");
      String w1 = ((Element) o1).getAttributeValue("wellName");
      String w2 = ((Element) o2).getAttributeValue("wellName");

      if (p1.equals(p2)) {
        // Sort column first numerically, then row
        Integer w1Int = new Integer(w1.substring(1));
        Integer w2Int = new Integer(w2.substring(1));
        if (w1Int.equals(w2Int)) {
          return w1.compareTo(w2);
        }
        return w1Int.compareTo(w2Int);
      }
      return p1.compareTo(p2);

    }
  }

}