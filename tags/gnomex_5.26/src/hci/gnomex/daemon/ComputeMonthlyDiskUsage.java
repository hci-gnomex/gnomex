package hci.gnomex.daemon;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.InternalAccountFieldsConfiguration;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

public class ComputeMonthlyDiskUsage {

  private BatchDataSource                 dataSource;
  private Session                         sess;

  private Transaction                     tx;

  private Properties                      mailProps;
  private Boolean                         sendMail = true;
  private Boolean                         testEmail = false;

  private static String                   serverName = "";
  private static ComputeMonthlyDiskUsage  app = null;
  private static BigDecimal               gigabyte = new BigDecimal(1000000000);

  private String                          errorMessagePrefixString = "Error in ComputeMonthlyDiskUsage";

  private Boolean                         computeForPreviousMonth = false;
  private Boolean                         overrideAutoPreviousMonth = false;
  private Boolean                         storeBillingItems = true;
  private Boolean                         sendReports = false;
  private String                          orionPath = "";
  private String                          testEmailAddress = "";
  private String                          baseURL = null;

  private Boolean                         diskUsageForExperiments = false;
  private Boolean                         diskUsageForAnalysis = false;
  private BigDecimal                      diskUsageIncrement;
  private String                          diskUsageIncrementString = "";
  private BigDecimal                      diskUsageFreePerIncrement;
  private BigDecimal                      diskUsageFreeBeforeCharge;
  private Integer                         analysisGracePeriod;
  private Integer                         experimentGracePeriod;
  private PriceCategory                   priceCategory;
  private Price                           price;
  private String                          gnomexSupportEmail;
  private String                          fromEmailAddress;
  private CoreFacility                    billingCoreFacility;

  private Date                            runDate;
  private BillingPeriod                   billingPeriod;

  private String                          processingStatement;

  public ComputeMonthlyDiskUsage(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-computeForPreviousMonth")) {
        computeForPreviousMonth = true;
      } else if (args[i].equals ("-overrideAutoPreviousMonth")) {
        overrideAutoPreviousMonth = true;
      } else if (args[i].equals ("-doNotStoreBillingItems")) {
        storeBillingItems = false;
      } else if (args[i].equals ("-sendReports")) {
        sendReports = true;
      } else if (args[i].equals ("-server")) {
        serverName = args[++i];
      } else if (args[i].equals ("-gnomexServerURL")) {
        baseURL = args[++i];
      } else if (args[i].equals ("-orionPath")) {
        orionPath = args[++i];
      } else if (args[i].equals ("-doNotSendMail")) {
        sendMail = false;
      } else if (args[i].equals ("-testEmailAddress")) {
        testEmailAddress = args[++i];
      } else if (args[i].equals ("-help")) {
        showHelp();
        System.exit(0);
      } 
    }
  }

  private void showHelp() {
    System.out.println("Computes monthly disk charges creating both DiskUsageByMonth rows and BillingItem rows.");
    System.out.println("The 'disk_usage_for_analysis' and 'disk_usage_for_experiments' properties control if charges are computed for the core facility.");
    System.out.println("Optionally emails report of expected disk usage charges to lab contacts.");
    System.out.println("Switches:");
    System.out.println("   -computeForPreviousMonth - Causes charges to be computed for previous calendar month.");
    System.out.println("   -overrideAutoPreviousMonth - By default program computes for previous month if run in 1st 5 days of month.  This overrides.");
    System.out.println("   -doNotStoreBillingItems - Creates DiskUsageByMonth items but does not create the billing items.  Often used with -sendReports.");
    System.out.println("   -sendReports - Sends reports of current expected charges to lab managers.");
    System.out.println("   -server - server for Hibernate connection.");
    System.out.println("   -orionPath - path to Orion directory to get mail properties.");
    System.out.println("   -doNotSendEmail - used for debugging if no email server available.  Emails are created but not sent.");
    System.out.println("   -testEmailAddress - Overrides all lab emails with this email address.  Used for testing.");
    System.out.println("   -help - gives this message.  Note no other processing is performed if the -help switch is specified.");
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new ComputeMonthlyDiskUsage(args);

    app.run();
  }

  public void run(){
    Calendar calendar = Calendar.getInstance();
    errorMessagePrefixString += " on " + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(calendar.getTime()) + "\n";

    try {
      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      dataSource = new BatchDataSource();
      app.connect();

      if (sendMail) {
        mailProps = new BatchMailer(orionPath).getMailProperties();
      }

      Query coreQuery = sess.createQuery("select c from CoreFacility c");
      List coreFacilities = coreQuery.list();

      for(Iterator i = coreFacilities.iterator(); i.hasNext(); ) {
        CoreFacility f = (CoreFacility)i.next();

        // if disk usage properties are set for the core facility then compute the space.
        app.initialize(f);
        System.out.println("\n" + new Date() + ": " + this.processingStatement);
        app.computeDiskUsage(f);
      }

    } catch (Exception e) {

      this.sendErrorReport(e);

    } finally {
      if (sess != null) {
        try {
          app.disconnect();
        } catch(Exception e) {
          System.err.println( "ComputeMonthlyDiskUsage unable to disconnect from hibernate session.   " + e.toString() );
        }
      }
    }


  }

  private void initialize(CoreFacility facility) throws Exception {
    InternalAccountFieldsConfiguration.reloadConfigurations(sess);

    PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);
    String diskUsageForExperimentsString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_FOR_EXPERIMENTS, serverName, facility.getIdCoreFacility());
    String diskUsageForAnalysisString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_FOR_ANALYSIS, serverName, facility.getIdCoreFacility());
    String diskUsageIncrementString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_INCREMENT, serverName, facility.getIdCoreFacility());
    String diskUsageFreePerIncrementString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_FREE_PER_INCREMENT, serverName, facility.getIdCoreFacility());
    String diskUsageFreeBeforeChargeString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_FREE_GB, serverName, facility.getIdCoreFacility());
    String analysisGracePeriodString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_ANALYSIS_GRACE_PERIOD_IN_MONTHS, serverName, facility.getIdCoreFacility());
    String experimentGracePeriodString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_EXPERIMENT_GRACE_PERIOD_IN_MONTHS, serverName, facility.getIdCoreFacility());
    String priceCategoryNameString = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_PRICE_CATEGORY_NAME, serverName, facility.getIdCoreFacility());
    String billingCoreId = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.DISK_USAGE_BILLING_CORE, serverName, facility.getIdCoreFacility());
    gnomexSupportEmail = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL, serverName, facility.getIdCoreFacility());
    if (gnomexSupportEmail == null) {
      gnomexSupportEmail = ph.getQualifiedCoreFacilityProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER, serverName, facility.getIdCoreFacility());
    }
    this.fromEmailAddress = ph.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    testEmail = !ph.isProductionServer(serverName);
    if (!testEmail && testEmailAddress != null && testEmailAddress.length() > 0) {
      testEmail = true;
    } else if (testEmail && (testEmailAddress == null || testEmailAddress.length() == 0)) {
      testEmailAddress = ph.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }
    if (billingCoreId != null) {
      try {
        billingCoreFacility = (CoreFacility)sess.get(CoreFacility.class, Integer.parseInt(billingCoreId));
      } catch(NumberFormatException e) {
        throw new Exception("Unable to parse the billing core facility id specified in " + PropertyDictionary.DISK_USAGE_BILLING_CORE + " property for " + facility.getFacilityName() + " core.");
      }
      if (billingCoreFacility == null) {
        throw new Exception("Unable to load the billing core facility specified in " + PropertyDictionary.DISK_USAGE_BILLING_CORE + " property for " + facility.getFacilityName() + " core.");
      }
    } else {
      billingCoreFacility = facility;
    }
    if (diskUsageForExperimentsString != null && diskUsageForExperimentsString.equals("Y")) {
      diskUsageForExperiments = true;
    } else {
      diskUsageForExperiments = false;
    }
    if (diskUsageForAnalysisString != null && diskUsageForAnalysisString.equals("Y")) {
      diskUsageForAnalysis = true;
    } else {
      diskUsageForAnalysis = false;
    }
    if (diskUsageIncrementString == null) {
      diskUsageIncrementString = "100"; // default to 100 gigabytes.
    }
    diskUsageIncrement = new BigDecimal(diskUsageIncrementString.replaceAll(",", ""));
    diskUsageIncrement = diskUsageIncrement.multiply(gigabyte); // property in gigabytes.
    this.diskUsageIncrementString = diskUsageIncrementString; 
    if (diskUsageFreePerIncrementString != null) {
      diskUsageFreePerIncrement = new BigDecimal(diskUsageFreePerIncrementString.replaceAll(",", ""));
      diskUsageFreePerIncrement = diskUsageFreePerIncrement.multiply(gigabyte); // property in gigabytes
    } else {
      diskUsageFreePerIncrement = new BigDecimal("1000000000");
    }
    if (diskUsageFreeBeforeChargeString != null) {
      diskUsageFreeBeforeCharge = new BigDecimal(diskUsageFreeBeforeChargeString.replaceAll(",", ""));
      diskUsageFreeBeforeCharge = diskUsageFreeBeforeCharge.multiply(gigabyte); // property in gigabytes.
    } else {
      diskUsageFreeBeforeCharge = new BigDecimal("100000000000");
    }
    if (analysisGracePeriodString != null) {
      analysisGracePeriod = Integer.parseInt(analysisGracePeriodString);
    } else {
      analysisGracePeriod = 1;
    }
    if (experimentGracePeriodString != null) {
      experimentGracePeriod = Integer.parseInt(experimentGracePeriodString);
    } else {
      experimentGracePeriod = 3;
    }

    if (priceCategoryNameString != null) {
      Query priceCategoryQuery = sess.createQuery("select pc from PriceCategory pc where pc.name = :name");
      priceCategoryQuery.setString("name", priceCategoryNameString);
      priceCategory = (PriceCategory)priceCategoryQuery.uniqueResult();
      if (priceCategory.getPrices().size() > 0) {
        price = (Price)priceCategory.getPrices().toArray()[0];
      } else {
        price = null;
      }
    } else {
      priceCategory = null;
      price = null;
    }

    // Set string for todays date
    Calendar calendar = Calendar.getInstance();
    runDate = calendar.getTime();

    // Determine billing period we are running for.
    if (computeForPreviousMonth) {
      // Explicitly asked to compute for the previous month.
      calendar.add(Calendar.MONTH, -1);
    } else if (!overrideAutoPreviousMonth) {
      // By default if run in 1st 7 days of month it computes for the previous month.
      if (calendar.get(Calendar.DAY_OF_MONTH) <= 7) {
        calendar.add(Calendar.MONTH, -1);
      }
    }
    String computeDateString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    Query billingPeriodQuery = sess.createQuery("select b from BillingPeriod b where '" + computeDateString + "' between b.startDate and b.endDate");
    billingPeriod = (BillingPeriod)billingPeriodQuery.uniqueResult();


    if (baseURL == null) {
      baseURL = (serverName.equals("localhost")  || serverName.equals("h005973") ? "http://" : "https://") + serverName + "/gnomex/";
    }
    if (!baseURL.endsWith("/")) {
      baseURL += "/";
    }

    processingStatement = "Processing " + facility.getFacilityName();
    if (!diskUsageForExperiments && !diskUsageForAnalysis) {
      processingStatement += " to remove existing usage if any.";
    } else if (diskUsageForExperiments && diskUsageForAnalysis) {
      processingStatement += " for experiment and analysis files.";
    } else if (diskUsageForExperiments) {
      processingStatement += " for experiment files only.";
    } else if (diskUsageForAnalysis) {
      processingStatement += " for analysis files only.";
    }
  }

  private void computeDiskUsage(CoreFacility facility) {
    Map<Integer, Lab> labMap = getLabMap(facility);
    Map<Integer, DiskUsageByMonth> newUsageMap = getDiskUsage(diskUsageForExperiments, diskUsageForAnalysis, sess, 
        billingPeriod, experimentGracePeriod, analysisGracePeriod, facility);
    Map<Integer, DiskUsageByMonth> existingUsageMap = getExistingDiskUsage(facility);
    Map<Integer, BillingAccount> billingAccountMap = getBillingAccountMap(facility);
    Map<Integer, Invoice> invoiceMap = getInvoiceMap(facility);

    tx = sess.beginTransaction();

    List<DiskUsageByMonth> modifiedUsages = storeUsage(facility, newUsageMap, existingUsageMap, billingAccountMap);
    if (storeBillingItems) {
      storeBillingItems(modifiedUsages, labMap, billingAccountMap, invoiceMap);
    }
    if (sendReports) {
      sendReports(modifiedUsages, labMap, billingAccountMap, facility);
    }
    removeUsage(facility, newUsageMap, existingUsageMap);

    tx.commit();
  }

  private Map<Integer, Lab> getLabMap(CoreFacility facility) {
    Map<Integer, Lab> labMap = new HashMap<Integer, Lab>();
    String queryString = "select distinct l from Lab l";
    Query query = sess.createQuery(queryString);
    List l = query.list();
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      Lab lab = (Lab)i.next();
      labMap.put(lab.getIdLab(), lab);
    }

    return labMap;
  }

  // Public static because also used by DeleteOldExperimentAndAnalysisFiles
  public static Map<Integer, DiskUsageByMonth> getDiskUsage(Boolean diskUsageForExperiments, Boolean diskUsageForAnalysis, Session sess, 
      BillingPeriod billingPeriod, Integer experimentGracePeriod, Integer analysisGracePeriod, CoreFacility facility) {
    Map<Integer, DiskUsageByMonth> usageMap = new HashMap<Integer, DiskUsageByMonth>();

    if (diskUsageForExperiments) {
      loadExperimentUsageData(sess, billingPeriod, experimentGracePeriod, usageMap, facility);
    }

    if (diskUsageForAnalysis) {
      loadAnalysisUsageData(sess, billingPeriod, analysisGracePeriod, usageMap, facility);
    }

    return usageMap;
  }

  private static void loadAnalysisUsageData(Session sess, BillingPeriod billingPeriod,  Integer analysisGracePeriod, Map<Integer, DiskUsageByMonth> usageMap, CoreFacility facility) {
    // Figure grace period.
    Calendar assessDateCal = Calendar.getInstance();
    assessDateCal.setTime(billingPeriod.getEndDate());
    assessDateCal.add(Calendar.MONTH, analysisGracePeriod * -1);

    String queryString = "select distinct a.idLab, SUM(af.fileSize) as totalDiskUsage, "
      + " SUM(case when case when af.uploadDate is null then case when af.createDate is null then '3999-12-31' else af.createDate end else af.uploadDate end <= :assessDate then af.fileSize else 0 end) as diskUsageInPeriod "
      + " from Analysis a "
      + " join a.lab l"
      + " join l.coreFacilities c"
      + " join a.files af "
      + " where c.idCoreFacility = :idCoreFacility and case when af.uploadDate is null then case when af.createDate is null then '3999-12-31' else af.createDate end else af.uploadDate end <= :asOfDate"
      + " group by a.idLab ";
    Query query = sess.createQuery(queryString);
    query.setDate("assessDate", assessDateCal.getTime());
    query.setInteger("idCoreFacility", facility.getIdCoreFacility());
    query.setDate("asOfDate", billingPeriod.getEndDate());

    List usageList = query.list();

    for(Iterator i = usageList.iterator(); i.hasNext(); ) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      BigDecimal totalDiskUsage = (BigDecimal)row[1];
      BigDecimal assessedDiskUsage = (BigDecimal)row[2];

      DiskUsageByMonth usage = null;
      if (usageMap.containsKey(idLab)) {
        usage = usageMap.get(idLab);
      } else {
        usage = new DiskUsageByMonth();
      }
      usage.setTotalAnalysisDiskSpace(totalDiskUsage);
      usage.setAssessedAnalysisDiskSpace(assessedDiskUsage);
      usageMap.put(idLab, usage);
    }
  }

  private static void loadExperimentUsageData(Session sess, BillingPeriod billingPeriod, Integer experimentGracePeriod, Map<Integer, DiskUsageByMonth> usageMap, CoreFacility facility) {
    // Figure grace period.
    Calendar assessDateCal = Calendar.getInstance();
    assessDateCal.setTime(billingPeriod.getEndDate());
    assessDateCal.add(Calendar.MONTH, experimentGracePeriod * -1);

    String queryString = "select distinct r.idLab, SUM(ef.fileSize) as totalDiskUsage, "
      + " SUM(case when case when ef.createDate is null then '3999-12-31' else ef.createDate end <= :assessDate then ef.fileSize else 0 end) as diskUsageInPeriod "
      + " from Request r "
      + " join r.requestCategory rc"
      + " join r.files ef "
      + " where rc.idCoreFacility = :idCoreFacility and case when ef.createDate is null then '3999-12-31' else ef.createDate end <= :asOfDate"
      + " group by r.idLab ";
    Query query = sess.createQuery(queryString);
    query.setDate("assessDate", assessDateCal.getTime());
    query.setInteger("idCoreFacility", facility.getIdCoreFacility());
    query.setDate("asOfDate", billingPeriod.getEndDate());

    List usageList = query.list();

    for(Iterator i = usageList.iterator(); i.hasNext(); ) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      BigDecimal totalDiskUsage = (BigDecimal)row[1];
      BigDecimal assessedDiskUsage = (BigDecimal)row[2];

      DiskUsageByMonth usage = null;
      if (usageMap.containsKey(idLab)) {
        usage = usageMap.get(idLab);
      } else {
        usage = new DiskUsageByMonth();
      }
      usage.setTotalExperimentDiskSpace(totalDiskUsage);
      usage.setAssessedExperimentDiskSpace(assessedDiskUsage);
      usageMap.put(idLab, usage);
    }
  }

  private Map<Integer, DiskUsageByMonth> getExistingDiskUsage(CoreFacility facility) {
    String queryString = "Select d from DiskUsageByMonth d where d.idCoreFacility = :idCoreFacility and d.idBillingPeriod = :idBillingPeriod";
    Query query = sess.createQuery(queryString);
    query.setInteger("idCoreFacility", facility.getIdCoreFacility());
    query.setInteger("idBillingPeriod", billingPeriod.getIdBillingPeriod());
    List usageList = query.list();
    Map<Integer, DiskUsageByMonth> usageMap = new HashMap<Integer, DiskUsageByMonth>();
    for(Iterator i = usageList.iterator(); i.hasNext(); ) {
      DiskUsageByMonth usage = (DiskUsageByMonth)i.next();
      usageMap.put(usage.getIdLab(), usage);
    }

    return usageMap;
  }

  private Map<Integer, BillingAccount> getBillingAccountMap(CoreFacility facility) {
    // The order by clause is to make sure the selected (first) account is:
    // 1. The most recent by startdate
    // 2. If 2 have the same startdate then most recent by expiration date
    // 3. if 2 have the same expiration date then one with the largest (most recent) id
    String queryString = "select ba from BillingAccount ba where ba.idCoreFacility in (:ids) order by ba.idLab, ba.startDate desc, ba.expirationDate desc, ba.idBillingAccount desc";
    Query query = sess.createQuery(queryString);
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ids.add(facility.getIdCoreFacility());
    ids.add(billingCoreFacility.getIdCoreFacility());
    query.setParameterList("ids", ids);

    Map<Integer, BillingAccount> billingAccountMap = new HashMap<Integer, BillingAccount>();
    List billingAccountList = query.list();
    for(Iterator i = billingAccountList.iterator(); i.hasNext(); ) {
      BillingAccount ba = (BillingAccount)i.next();
      // Only use active and approved billing accounts.
      if (ba.getIsActive().equals("Y") && ba.getIsApproved().equals("Y")) {
        BillingAccount oldBA = billingAccountMap.get(ba.getIdLab());
        if (oldBA == null) {
          // first active/approved billing account encountered for the lab
          billingAccountMap.put(ba.getIdLab(), ba);
        } else if (!facility.getIdCoreFacility().equals(billingCoreFacility.getIdCoreFacility()) 
            && oldBA.getIdCoreFacility().equals(facility.getIdCoreFacility()) && ba.getIdCoreFacility().equals(billingCoreFacility.getIdCoreFacility())) {
          // Favor accounts in billing core over those in the summing core.
          billingAccountMap.put(ba.getIdLab(), ba);
        } else if ((oldBA.getIsPO() != null && oldBA.getIsPO().equals("Y")) && (ba.getIsPO() == null || ba.getIsPO().equals("N"))) {
          // Favor non-PO billing accounts over PO billing accounts
          billingAccountMap.put(ba.getIdLab(), ba);
        }
      }
    }

    return billingAccountMap;
  }

  private Map<Integer, Invoice> getInvoiceMap(CoreFacility facility) {
    String queryString = "select inv from Invoice inv where inv.idCoreFacility = :idCoreFacility and inv.idBillingPeriod = :idBillingPeriod";
    Query query = sess.createQuery(queryString);
    query.setInteger("idCoreFacility", billingCoreFacility.getIdCoreFacility());
    query.setInteger("idBillingPeriod", billingPeriod.getIdBillingPeriod());

    Map<Integer, Invoice> invoiceMap = new HashMap<Integer, Invoice>();
    List invoiceList = query.list();
    for(Iterator i = invoiceList.iterator(); i.hasNext(); ) {
      Invoice inv = (Invoice)i.next();
      invoiceMap.put(inv.getIdBillingAccount(), inv);
    }

    return invoiceMap;
  }

  private List<DiskUsageByMonth> storeUsage(CoreFacility facility, Map<Integer, DiskUsageByMonth> newUsageMap, Map<Integer, DiskUsageByMonth> existingUsageMap, Map<Integer, BillingAccount> billingAccountMap) {
    List<DiskUsageByMonth> modifiedUsages = new ArrayList<DiskUsageByMonth>();
    Date asOfDate = runDate;
    if (runDate.after(billingPeriod.getEndDate())) {
      asOfDate = billingPeriod.getEndDate();
    }
    for (Integer key: newUsageMap.keySet()) {
      DiskUsageByMonth newUsage = newUsageMap.get(key);
      DiskUsageByMonth existingUsage = existingUsageMap.get(key);
      DiskUsageByMonth usage = null;
      if (existingUsage == null) {
        usage = newUsage;
        usage.setIdLab(key);
        newUsage.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
        newUsage.setIdCoreFacility(facility.getIdCoreFacility());
      } else {
        usage = existingUsage;
      }
      usage.setAsOfDate(new java.sql.Date(asOfDate.getTime()));
      usage.setLastCalcDate(new java.sql.Date(runDate.getTime()));
      usage.setTotalAnalysisDiskSpace(newUsage.getTotalAnalysisDiskSpace() == null ? BigDecimal.ZERO : newUsage.getTotalAnalysisDiskSpace());
      usage.setAssessedAnalysisDiskSpace(newUsage.getAssessedAnalysisDiskSpace() == null ? BigDecimal.ZERO : newUsage.getAssessedAnalysisDiskSpace());
      usage.setTotalExperimentDiskSpace(newUsage.getTotalExperimentDiskSpace() == null ? BigDecimal.ZERO : newUsage.getTotalExperimentDiskSpace());
      usage.setAssessedExperimentDiskSpace(newUsage.getAssessedExperimentDiskSpace() == null ? BigDecimal.ZERO : newUsage.getAssessedExperimentDiskSpace());
      if (billingAccountMap.containsKey(usage.getIdLab())) {
        // Note at this point billing account may be from summing core instead of billing core.
        // This indicates that new billing account will be created (via copy) when billing item is created.
        usage.setIdBillingAccount(billingAccountMap.get(usage.getIdLab()).getIdBillingAccount());
      } else {
        usage.setIdBillingAccount(null);
      }
      sess.save(usage);
      modifiedUsages.add(usage);
    }

    sess.flush();
    return modifiedUsages;
  }

  private void storeBillingItems(List<DiskUsageByMonth> modifiedUsages, Map<Integer, Lab> labMap, Map<Integer, BillingAccount> billingAccountMap, Map<Integer, Invoice> invoiceMap) {
    Boolean anyModified = false;
    for(DiskUsageByMonth usage: modifiedUsages) {
      boolean modified = false;
      Lab lab = labMap.get(usage.getIdLab());
      BillingAccount ba = billingAccountMap.get(usage.getIdLab());
      if (ba != null && !ba.getIdCoreFacility().equals(billingCoreFacility.getIdCoreFacility())) {
        // Billing account is not for the billing core.  Copy it from the core it is in to allow it.
        ba = addBillingAccount(ba, lab);
        usage.setIdBillingAccount(ba.getIdBillingAccount());
      }
      BigDecimal totalIncrements = getTotalIncrements(usage);
      Invoice invoice = null;
      if (ba != null) {
        invoice = invoiceMap.get(ba.getIdBillingAccount());
      }
      if (usage.getBillingItems() == null) {
        usage.setBillingItems(new HashSet());
      }
      if (lab.isExternalLab() || totalIncrements.compareTo(BigDecimal.ZERO) <= 0 || ba == null) {
        // Don't need billing items if no charge and can't have them if no billing account
        if (usage.getBillingItems().size() > 0) {
          modified = true;
          usage.getBillingItems().clear();
        }
      } else {
        modified = true;
        if (usage.getBillingItems().size() > 0) {
          usage.getBillingItems().clear();
        }
        BillingItem bi = newBillingItem(usage, lab, totalIncrements, ba, invoice);
        usage.getBillingItems().add(bi);
      }

      if (modified) {
        anyModified = true;
      }
    }

    if (anyModified) {
      sess.flush();
    }
  }
  
  private BillingAccount addBillingAccount(BillingAccount ba, Lab lab) {
    BillingAccount newBA = ba.getCopy(billingCoreFacility.getIdCoreFacility());
    sess.save(newBA);
    addBillingCoreToLab(lab);
    sess.flush();
    return newBA;
  }
  
  @SuppressWarnings("unchecked")
  private void addBillingCoreToLab(Lab lab) {
    Boolean coreFound = false;
    for(Object o : lab.getCoreFacilities()) {
      CoreFacility core = (CoreFacility)o;
      if (core.getIdCoreFacility().equals(billingCoreFacility.getIdCoreFacility())) {
        coreFound = true;
        break;
      }
    }
    if (!coreFound) {
      lab.getCoreFacilities().add(billingCoreFacility);
      sess.save(lab);
    }
  }

  private BillingItem newBillingItem(DiskUsageByMonth usage, Lab lab, BigDecimal totalIncrements, BillingAccount ba, Invoice invoice) {
    BillingItem bi = new BillingItem();
    bi.setCategory("Disk Usage");
    bi.setDescription("Disk Usage per " + this.diskUsageIncrementString + "GB");
    bi.setQty(totalIncrements.intValue());
    bi.setUnitPrice(price.getEffectiveUnitPrice(lab));
    bi.setInvoicePrice(getTotalCharge(totalIncrements, lab));
    bi.setPercentagePrice(new BigDecimal(1));
    bi.setCodeBillingChargeKind(BillingChargeKind.SERVICE);
    bi.setCodeBillingStatus(BillingStatus.COMPLETED);
    bi.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
    bi.setIdPriceCategory(priceCategory.getIdPriceCategory());
    bi.setIdPrice(price.getIdPrice());
    bi.setIdBillingAccount(ba.getIdBillingAccount());
    bi.setIdLab(lab.getIdLab());
    bi.setCompleteDate(new java.sql.Date(runDate.getTime()));
    bi.setSplitType("%");
    bi.setIdCoreFacility(ba.getIdCoreFacility());
    if (invoice != null) {
      bi.setIdInvoice(invoice.getIdInvoice());
    } else {
      bi.resetInvoiceForBillingItem(sess);
    }
    bi.setIdDiskUsageByMonth(usage.getIdDiskUsageByMonth());
    return bi;
  }

  private BigDecimal getTotalIncrements(DiskUsageByMonth usage) {
    BigDecimal totalIncrements = BigDecimal.ZERO;

    // Handle the amount free over all
    BigDecimal realAssessedAmount = usage.getAssessedDiskSpace().subtract(this.diskUsageFreeBeforeCharge);
    // Handle the amount free per interval
    realAssessedAmount = realAssessedAmount.subtract(this.diskUsageFreePerIncrement);
    if (realAssessedAmount.compareTo(BigDecimal.ZERO) > 0 && price != null) {
      totalIncrements = realAssessedAmount.divide(this.diskUsageIncrement);
      // round increments up to total to charge
      totalIncrements = totalIncrements.setScale(0, BigDecimal.ROUND_UP);
    }

    return totalIncrements;
  }

  private BigDecimal getTotalCharge(BigDecimal totalIncrements, Lab lab) {
    BigDecimal totalCharge = BigDecimal.ZERO;
    BigDecimal thePrice = price.getEffectiveUnitPrice(lab);
    if (thePrice != null) {
      totalCharge = totalIncrements.multiply(thePrice);
    }
    // round up to nearest dollar
    totalCharge = totalCharge.setScale(0, BigDecimal.ROUND_UP);

    return totalCharge;
  }

  private void sendReports(List<DiskUsageByMonth> modifiedUsages, Map<Integer, Lab> labMap, Map<Integer, BillingAccount> billingAccountMap, CoreFacility facility) {
    for(DiskUsageByMonth usage: modifiedUsages) {
      Lab lab = labMap.get(usage.getIdLab());
      BillingAccount ba = billingAccountMap.get(usage.getIdLab());
      BigDecimal totalIncrements = getTotalIncrements(usage);

      if (totalIncrements.compareTo(BigDecimal.ZERO) > 0 && !lab.isExternalLab()) {
        addBillingCoreToLab(lab);
        sendReport(lab, ba, usage, totalIncrements, facility);
      }
    }
  }

  private void sendReport(Lab lab, BillingAccount ba, DiskUsageByMonth usage, BigDecimal totalIncrements, CoreFacility facility) {
    String emailAddress = lab.getBillingNotificationEmail();
    if (emailAddress == null || emailAddress.length() == 0) {
      emailAddress = this.gnomexSupportEmail;
    }
    BigDecimal totalCharge = getTotalCharge(totalIncrements, lab);
    BigDecimal totalSize = usage.getAssessedDiskSpace();
    totalSize = totalSize.movePointLeft(9); // Change to gigabytes
    totalSize = totalSize.setScale(0, BigDecimal.ROUND_UP);
    String chargeForDisplay = NumberFormat.getCurrencyInstance().format(totalCharge);
    String diskSizeForDisplay = NumberFormat.getNumberInstance().format(totalSize);
    String labName = lab.getName(false, true);

    StringBuffer body = new StringBuffer();
    if (ba != null) {
      String accountName = ba.getAccountNameAndNumber();
      body.append(labName).append(" is scheduled to receive a monthly charge of ").append(chargeForDisplay).append(" to account '").append(accountName)
      .append("' on disk usage of ").append(diskSizeForDisplay).append(" gigabytes of storage.");
    } else {
      body.append(labName).append(" is scheduled to receive a monthly charge of ").append(chargeForDisplay).append(" on disk usage of ")
      .append(diskSizeForDisplay).append(" gigabytes of storage.  <br><br>");
      body.append("The lab has no active billing accounts.  Please submit a new billing account for the " + billingCoreFacility.getFacilityName() + " Core Facility to avoid file deletions(")
      .append("<a href=\"").append(getLaunchWorkAuthUrl(billingCoreFacility, lab)).append("\">New Billing Account</a>).");
    }

    if (sendMail) {
      try {
        MailUtil.sendCheckTest( mailProps,
            emailAddress,
            null,
            fromEmailAddress,
            "GNomEx Disk Usage Charges",
            body.toString(),
            true,
            testEmail,
            testEmailAddress
        );
      } catch(Exception ex) {
        System.err.println("Unable to send email to lab " + labName + " with email " + emailAddress + " because of exception: " + ex.getMessage());
      }
    }
  }

  public String getLaunchWorkAuthUrl(CoreFacility facility, Lab lab) {
    return baseURL + Constants.LAUNCH_APP_JSP + "?launchWindow=WorkAuthForm&idCore=" + facility.getIdCoreFacility().toString()
          + "&idLab=" + lab.getIdLab().toString();  
  }

  private void removeUsage(CoreFacility facility, Map<Integer, DiskUsageByMonth> newUsageMap, Map<Integer, DiskUsageByMonth> existingUsageMap) {
    Boolean modified = false;
    for(Integer key: existingUsageMap.keySet()) {
      if (!newUsageMap.containsKey(key)) {
        DiskUsageByMonth toDelete = existingUsageMap.get(key);
        sess.delete(toDelete);
      }
    }

    if (modified) {
      sess.flush();
    }
  }

  private void sendErrorReport(Exception e)  {

    String msg = "Could not compute disk space usage. Transaction rolled back:   " + e.toString() + "\n\t";

    StackTraceElement[] stack = e.getStackTrace();
    for (StackTraceElement s : stack) {
      msg = msg + s.toString() + "\n\t\t";
    }

    try {
      if (tx != null) {
        tx.rollback();
      }
    }
    catch(TransactionException te) {
      msg += "\nTransactionException: " + te.getMessage() + "\n\t";
      stack = te.getStackTrace();
      for (StackTraceElement s : stack) {
        msg = msg + s.toString() + "\n\t\t";
      }
    } finally {}

    String errorMessageString = errorMessagePrefixString;
    if ( !errorMessageString.equals( "" )) {
      errorMessageString += "\n";
    }
    errorMessageString += msg;

    System.err.println(errorMessageString);

    if (sess != null) {

      String toAddress = gnomexSupportEmail;
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }
      try {
        if (sendMail) {
          MailUtil.sendCheckTest( mailProps,
              toAddress,
              null,
              fromEmailAddress,
              "ComputeMonthlyDiskUsage Error",
              errorMessageString,
              false,
              testEmail,
              testEmailAddress
          );
        }
      } catch (Exception e1) {
        System.err.println( "ComputeMonthlyDiskUsage unable to send error report.   " + e1.toString() );
      }
    }
  }

  private void connect()
  throws Exception
  {
    sess = dataSource.connect();
  }

  private void disconnect() 
  throws Exception {
    sess.close();
  }
}
