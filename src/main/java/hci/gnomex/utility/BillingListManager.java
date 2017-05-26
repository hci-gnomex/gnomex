package hci.gnomex.utility;

import hci.framework.control.Command;
import hci.framework.model.FieldFormatter;
import hci.gnomex.model.*;
import hci.gnomex.security.SecurityAdvisor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jdom2.Element;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class BillingListManager {
    private Session             sess;
    private SecurityAdvisor     securityAdvisor;
    private DictionaryHelper    dh;
    private FieldFormatter      formatter;
    private int                 dateOutputStyle;
    private int                 mode;

    private Integer     idBillingPeriod;
    private Integer     idLab;
    private Integer     idBillingAccount;
    private Integer     idCoreFacility;
    private Boolean     excludeNewRequests;
    private Boolean     excludeInactiveBillingTemplates;
    private String      requestNumber;
    private String      invoiceLookupNumber;
    private Boolean     showOtherBillingItems;
    private Boolean     sortResults;
    private Boolean     deepSortResults;

    private static final int    MODE_NOT_SPECIFIED = -1;
    private static final int    MODE_BILLING_REQUEST_LIST = 1;
    private static final int    MODE_BILLING_ITEM_LIST = 2;

    private static final int    RESULT_SET_TYPE_REQUEST = 10;
    private static final int    RESULT_SET_TYPE_PRODUCT_ORDER = 11;
    private static final int    RESULT_SET_TYPE_DISK_USAGE = 12;

    public BillingListManager(Session session, SecurityAdvisor securityAdvisor, HttpServletRequest request, Command command, int dateOutputStyle) {
        this.sess = session;
        this.securityAdvisor = securityAdvisor;
        this.dh = DictionaryHelper.getInstance(this.sess);
        this.formatter = new FieldFormatter();
        this.dateOutputStyle = dateOutputStyle;
        this.mode = MODE_NOT_SPECIFIED;

        this.parseRequestParameters(request, command);
    }

    public void setSession(Session session) {
        this.sess = session;
    }

    private void parseRequestParameters(HttpServletRequest request, Command command) {
        idBillingPeriod = Util.retrieveRequestIntegerParameter(request, "idBillingPeriod", command);
        idLab = Util.retrieveRequestIntegerParameter(request, "idLab", command);
        idBillingAccount = Util.retrieveRequestIntegerParameter(request, "idBillingAccount", command);
        idCoreFacility = Util.retrieveRequestIntegerParameter(request, "idCoreFacility", command);
        excludeNewRequests = Util.retrieveRequestBooleanParameter(request, "excludeNewRequests", command, false);
        excludeInactiveBillingTemplates = Util.retrieveRequestBooleanParameter(request, "excludeInactiveBillingTemplates", command, true);
        requestNumber = Util.retrieveRequestStringParameter(request, "requestNumber");
        invoiceLookupNumber = Util.retrieveRequestStringParameter(request, "invoiceLookupNumber");
        showOtherBillingItems = Util.retrieveRequestBooleanParameter(request, "showOtherBillingItems", command, false);
        sortResults = Util.retrieveRequestBooleanParameter(request, "sortResults", command, true);
        deepSortResults = Util.retrieveRequestBooleanParameter(request, "deepSortResults", command, false);
        if (deepSortResults) {
            sortResults = true;
        }
    }

    public boolean hasSufficientCriteria() {
        return !(requestNumber == null && invoiceLookupNumber == null && (idCoreFacility == null || idBillingPeriod == null));
    }

    public String getInsufficientCriteriaMessage() {
        return "Please select a core facility and a billing period or provide an experiment or invoice number";
    }

    public Element buildBillingItemList() {
        this.mode = MODE_BILLING_ITEM_LIST;
        return this.buildList();
    }

    public Element buildBillingRequestList() {
        this.mode = MODE_BILLING_REQUEST_LIST;
        return this.buildList();
    }

    private Element buildList() {
        Map<String, Element> statusMap = new HashMap<String, Element>();
        Map<String, Element> labMap = new HashMap<String, Element>();
        Element root = new Element(mode == MODE_BILLING_REQUEST_LIST ? "BillingRequestList" : "BillingItemList");

        if (requestNumber != null) {
            this.buildXML(root, statusMap, labMap, this.getRequestNumberQueryResult(), RESULT_SET_TYPE_REQUEST);
        } else if (invoiceLookupNumber != null) {
            // Search requests
            this.buildXML(root, statusMap, labMap, this.getInvoiceNumberQueryResult("Request", "req", "idRequest"), RESULT_SET_TYPE_REQUEST);

            // Search product orders
            this.buildXML(root, statusMap, labMap, this.getInvoiceNumberQueryResult("ProductOrder", "po", "idProductOrder"), RESULT_SET_TYPE_PRODUCT_ORDER);

            // Search disk usage
            this.buildXML(root, statusMap, labMap, this.getInvoiceNumberQueryResult("DiskUsageByMonth", "dsk", "idDiskUsageByMonth"), RESULT_SET_TYPE_DISK_USAGE);
        } else {
            // Search requests
            this.buildXML(root, statusMap, labMap, this.getMainQueryResult("Request", "req", "idRequest"), RESULT_SET_TYPE_REQUEST);

            // Search product orders
            this.buildXML(root, statusMap, labMap, this.getMainQueryResult("ProductOrder", "po", "idProductOrder"), RESULT_SET_TYPE_PRODUCT_ORDER);

            // Search disk usage
            this.buildXML(root, statusMap, labMap, this.getMainQueryResult("DiskUsageByMonth", "dsk", "idDiskUsageByMonth"), RESULT_SET_TYPE_DISK_USAGE);
        }

        if (sortResults) {
            this.sortXML(root);
        }

        return root;
    }

    private void sortXML(Element root) {
        if (mode == MODE_BILLING_REQUEST_LIST) {
            // Sort status nodes
            root.sortChildren(this.statusNodeComparator);
            // Sort lab nodes (and order nodes directly under pending status node)
            for (Element statusNode : root.getChildren()) {
                if (statusNode.getAttributeValue("status").equalsIgnoreCase(BillingStatus.PENDING)) {
                    statusNode.sortChildren(this.orderNodeComparator);
                    if (deepSortResults) {
                        for (Element orderNode : statusNode.getChildren()) {
                            orderNode.sortChildren(this.billingItemNodeComparator);
                        }
                    }
                } else {
                    statusNode.sortChildren(this.labNodeComparator);
                    for (Element labNode : statusNode.getChildren()) {
                        labNode.sortChildren(this.orderNodeComparator);
                        if (deepSortResults) {
                            for (Element orderNode : labNode.getChildren()) {
                                orderNode.sortChildren(this.billingItemNodeComparator);
                            }
                        }
                    }
                }
            }
        } else {
            // Sort order nodes
            root.sortChildren(this.orderNodeComparator);
            // If deep sorting, sort billing items under order nodes
            if (deepSortResults) {
                for (Element orderNode : root.getChildren()) {
                    orderNode.sortChildren(this.billingItemNodeComparator);
                }
            }
        }
    }

    private void buildXML(Element root, Map<String, Element> statusMap, Map<String, Element> labMap, Set<Integer> resultSet, int resultSetType) {
        for (Integer idOrder : resultSet) {
            for (QueryRowResult rowResult : this.makeOrderNodesQuery(idOrder, resultSetType)) {
                Element node = this.createOrderNode(rowResult, resultSetType);

                if (mode == MODE_BILLING_REQUEST_LIST) {
                    // Make status node
                    String codeBillingStatus = node.getAttributeValue("codeBillingStatus");
                    String codeBillingStatusForSorting = codeBillingStatus.equals(BillingStatus.NEW) ? BillingStatus.PENDING : codeBillingStatus;
                    Element statusNode;
                    if (statusMap.containsKey(codeBillingStatusForSorting)) {
                        statusNode = statusMap.get(codeBillingStatusForSorting);
                    } else {
                        statusNode = this.createStatusNode(codeBillingStatusForSorting);
                        statusMap.put(codeBillingStatusForSorting, statusNode);
                        root.addContent(statusNode);
                    }

                    // Make lab node if necessary, otherwise add directly to status node
                    if (codeBillingStatusForSorting.equals(BillingStatus.PENDING)) {
                        statusNode.addContent(node);
                    } else {
                        StringBuilder labKeyBuilder = new StringBuilder();
                        labKeyBuilder.append(node.getAttributeValue("labName"));
                        labKeyBuilder.append(node.getAttribute("idBillingAccount"));
                        labKeyBuilder.append(codeBillingStatusForSorting);
                        String labKey = labKeyBuilder.toString();
                        Element labNode;
                        if (labMap.containsKey(labKey)) {
                            labNode = labMap.get(labKey);
                        } else {
                            labNode = this.createLabNode(node, rowResult);
                            labMap.put(labKey, labNode);
                            statusNode.addContent(labNode);
                        }
                        labNode.addContent(node);
                    }
                } else if (mode == MODE_BILLING_ITEM_LIST) {
                    this.appendBillingItemNodes(node, rowResult, resultSetType);
                    root.addContent(node);
                }
            }
        }
    }

    private void appendBillingItemNodes(Element node, QueryRowResult queryResult, int resultSetType) {
        String idColumnName = null;
        Integer id = null;
        if (resultSetType == RESULT_SET_TYPE_REQUEST) {
            idColumnName = "idRequest";
            id = queryResult.getFieldValueInteger("req.idRequest");
        } else if (resultSetType == RESULT_SET_TYPE_PRODUCT_ORDER) {
            idColumnName = "idProductOrder";
            id = queryResult.getFieldValueInteger("po.idProductOrder");
        } else if (resultSetType == RESULT_SET_TYPE_DISK_USAGE) {
            idColumnName = "idDiskUsageByMonth";
            id = queryResult.getFieldValueInteger("dsk.idDiskUsageByMonth");
        }
        Integer idBillingAccount = Integer.parseInt(node.getAttributeValue("idBillingAccount"));

        Set<Integer> idBillingItemsMain = this.getIdBillingItems(idColumnName, id, idBillingAccount, false);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        BigDecimal invoicePrice = new BigDecimal(0);
        invoicePrice = invoicePrice.setScale(2, BigDecimal.ROUND_UP);
        BigDecimal totalPrice = new BigDecimal(0);
        totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_UP);
        for (Integer idBillingItem : idBillingItemsMain) {
            BillingItem item = sess.get(BillingItem.class, idBillingItem);
            Element billingItemNode = this.createBillingItemNode(item, false, nf);
            node.addContent(billingItemNode);
            invoicePrice = invoicePrice.add(item.getInvoicePrice() != null ? item.getInvoicePrice() : new BigDecimal(0));
            totalPrice = totalPrice.add(item.getTotalPrice() != null ? item.getTotalPrice() : new BigDecimal(0));
        }
        node.setAttribute("invoicePrice", nf.format(invoicePrice.doubleValue()));
        node.setAttribute("totalPrice", nf.format(totalPrice.doubleValue()));

        if (showOtherBillingItems) {
            for (Integer idBillingItem : this.getIdBillingItems(idColumnName, id, idBillingAccount, true)) {
                BillingItem item = sess.get(BillingItem.class, idBillingItem);
                Element billingItemNode = this.createBillingItemNode(item, true, nf);
                node.addContent(billingItemNode);
            }
        }
    }

    private Element createBillingItemNode(BillingItem bi, boolean isOther, NumberFormat nf) {
        Element billingItemNode = new Element("BillingItem");
        billingItemNode.setAttribute("description", bi.getDescription());
        billingItemNode.setAttribute("idBillingAccount", bi.getIdBillingAccount().toString());
        billingItemNode.setAttribute("idCoreFacility", this.getNonNullString(bi.getIdCoreFacility()));
        billingItemNode.setAttribute("idProductOrder", this.getNonNullString(bi.getIdProductOrder()));
        billingItemNode.setAttribute("labName", bi.getLabName());
        billingItemNode.setAttribute("notes", this.getNonNullString(bi.getNotes()));
        billingItemNode.setAttribute("accountName", bi.getAccountName());
        billingItemNode.setAttribute("idLab", bi.getIdLab().toString());
        billingItemNode.setAttribute("idRequest", this.getNonNullString(bi.getIdRequest()));
        billingItemNode.setAttribute("category", bi.getCategory());
        billingItemNode.setAttribute("codeBillingStatus", bi.getCodeBillingStatus());
        billingItemNode.setAttribute("accountNameDisplay", bi.getAccountNameDisplay());
        billingItemNode.setAttribute("idBillingItem", bi.getIdBillingItem().toString());
        billingItemNode.setAttribute("qty", this.getNonNullString(bi.getQty()));
        billingItemNode.setAttribute("unitPrice", this.getNonNullString(bi.getUnitPrice(), nf));
        billingItemNode.setAttribute("totalPrice", this.getNonNullString(bi.getTotalPrice(), nf));
        billingItemNode.setAttribute("completeDate", bi.getCompleteDate() != null ? formatter.formatDate(bi.getCompleteDate(), FieldFormatter.DATE_OUTPUT_SQL) : "");
        billingItemNode.setAttribute("splitType", this.getNonNullString(bi.getSplitType()));
        billingItemNode.setAttribute("idPrice", bi.getIdPrice().toString());
        billingItemNode.setAttribute("idInvoice", this.getNonNullString(bi.getIdInvoice()));
        billingItemNode.setAttribute("idPriceCategory", bi.getIdPriceCategory().toString());
        billingItemNode.setAttribute("codeBillingChargeKind", bi.getCodeBillingChargeKind());
        billingItemNode.setAttribute("totalPriceDisplay", this.getNonNullString(bi.getTotalPriceDisplay()));
        billingItemNode.setAttribute("invoicePriceDisplay", this.getNonNullString(bi.getInvoicePriceDisplay()));
        billingItemNode.setAttribute("idBillingPeriod", bi.getIdBillingPeriod().toString());
        billingItemNode.setAttribute("percentagePrice", bi.getPercentagePrice().toString());
        billingItemNode.setAttribute("completeDateOther", this.getNonNullString(bi.getCompleteDateOther()));
        billingItemNode.setAttribute("accountNameAndNumber", bi.getAccountNameAndNumber());
        billingItemNode.setAttribute("accountNameDisplay", bi.getAccountNameDisplay());
        billingItemNode.setAttribute("percentageDisplay", bi.getPercentageDisplay());
        billingItemNode.setAttribute("idDiskUsageMonth", this.getNonNullString(bi.getIdDiskUsageByMonth()));
        billingItemNode.setAttribute("idMasterBillingItem", this.getNonNullString(bi.getIdMasterBillingItem()));
        billingItemNode.setAttribute("other", isOther ? "Y" : "N");
        billingItemNode.setAttribute("isDirty", "N");
        billingItemNode.setAttribute("currentCodeBillingStatus", bi.getCodeBillingStatus());
        if (bi.getInvoicePrice() != null) {
            billingItemNode.setAttribute("invoicePrice", this.getNonNullString(bi.getInvoicePrice(), nf));
        }
        return billingItemNode;
    }

    private Element createOrderNode(QueryRowResult queryResult, int resultSetType) {
        Element node = new Element("Request");
        StringBuilder toolTip = new StringBuilder();
        Date createDate = null;
        java.sql.Date completedDate = null;
        Integer idBillingAccount = null;
        if (resultSetType == RESULT_SET_TYPE_REQUEST) {
            Integer idRequest = queryResult.getFieldValueInteger("req.idRequest");
            String requestNumber = queryResult.getFieldValueString("req.number");
            String codeRequestCategory = queryResult.getFieldValueString("req.codeRequestCategory");
            idBillingAccount = queryResult.getFieldValueInteger("bti.idBillingAccount");
            String idBillingAccountString = queryResult.getFieldValueIntegerAsString("bti.idBillingAccount");
            createDate = (Date) queryResult.getFieldValue("req.createDate");
            try {
                completedDate = (java.sql.Date) queryResult.getFieldValue("req.completedDate");
            } catch (ClassCastException e) {
                completedDate = new java.sql.Date(((java.sql.Timestamp) queryResult.getFieldValue("req.completedDate")).getTime());
            }
            Integer idSubmitter = queryResult.getFieldValueInteger("req.idSubmitter");

            RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
            String requestCategoryIcon = (requestCategory != null && requestCategory.getIcon() != null) ? requestCategory.getIcon() : "";
            String requestCategoryType = (requestCategory != null && requestCategory.getType() != null) ? requestCategory.getType() : "";
            String createDateString = createDate != null ? formatter.formatDate(createDate, FieldFormatter.DATE_OUTPUT_DASH) : "";
            String completedDateString = completedDate != null ? formatter.formatDate(completedDate, FieldFormatter.DATE_OUTPUT_DASH) : "";
            String codeBillingStatus = "";
            String idInvoice = "";
            if (!idBillingAccountString.equals("")) {
                Integer parsedIdBillingAccount = Integer.parseInt(idBillingAccountString);
                if (mode == MODE_BILLING_REQUEST_LIST) {
                    codeBillingStatus = this.determineStatusForOrder("idRequest", idRequest, parsedIdBillingAccount);
                }
                idInvoice = this.determineIdInvoiceForOrder("idRequest", idRequest, parsedIdBillingAccount);
            }
            String hasBillingItems = "";
            if (!codeBillingStatus.equals("")) {
                hasBillingItems = codeBillingStatus.equals(BillingStatus.NEW) ? "N" : "Y";
            }
            AppUser submitter = sess.load(AppUser.class, idSubmitter);
            toolTip.append(requestNumber);

            node.setAttribute("idRequest", idRequest.toString());
            node.setAttribute("requestNumber", requestNumber);
            node.setAttribute("label", requestNumber);
            node.setAttribute("codeRequestCategory", codeRequestCategory);
            node.setAttribute("icon", requestCategoryIcon);
            node.setAttribute("type", requestCategoryType);
            node.setAttribute("idBillingAccount", idBillingAccountString);
            node.setAttribute("createDate", createDateString);
            node.setAttribute("completedDate", completedDateString);
            node.setAttribute("submitter", submitter.getDisplayName());
            if (mode == MODE_BILLING_REQUEST_LIST) {
                node.setAttribute("codeBillingStatus", codeBillingStatus);
                node.setAttribute("hasBillingItems", hasBillingItems);
            }
            if (!idInvoice.equals("")) {
                node.setAttribute("idInvoice", idInvoice);
            }
        }
        if (resultSetType == RESULT_SET_TYPE_PRODUCT_ORDER) {
            Integer idProductOrder = queryResult.getFieldValueInteger("po.idProductOrder");
            String idProductOrderString = idProductOrder.toString();
            String idCoreFacility = queryResult.getFieldValueIntegerAsString("po.idCoreFacility");
            createDate = (Date) queryResult.getFieldValue("po.submitDate");
            idBillingAccount = queryResult.getFieldValueInteger("bti.idBillingAccount");
            String idBillingAccountString = queryResult.getFieldValueIntegerAsString("bti.idBillingAccount");
            String productOrderNumber = queryResult.getFieldValueString("po.productOrderNumber");

            toolTip.append("Product Order Charge for ");
            String hasBillingItems = "Y";
            String codeBillingStatus = "";
            String idInvoice = "";
            if (!idBillingAccountString.equals("")) {
                Integer parsedIdBillingAccount = Integer.parseInt(idBillingAccountString);
                if (mode == MODE_BILLING_REQUEST_LIST) {
                    codeBillingStatus = this.determineStatusForOrder("idProductOrder", idProductOrder, parsedIdBillingAccount);
                }
                idInvoice = this.determineIdInvoiceForOrder("idProductOrder", idProductOrder, parsedIdBillingAccount);
            }
            String label = "Product Order " + (productOrderNumber.equals("") ? idProductOrder.toString() : productOrderNumber);

            node.setAttribute("idRequest", idProductOrderString);
            node.setAttribute("idProductOrder", idProductOrderString);
            node.setAttribute("idCoreFacility", idCoreFacility);
            node.setAttribute("label", label);
            node.setAttribute("requestNumber", productOrderNumber.equals("") ? idProductOrderString : productOrderNumber);
            node.setAttribute("codeRequestCategory", ProductOrder.PRODUCT_ORDER_REQUEST_CATEGORY);
            node.setAttribute("icon", ProductOrder.PRODUCT_ORDER_ICON);
            node.setAttribute("type", "Product Order");
            node.setAttribute("submitter", "");
            node.setAttribute("idBillingAccount", idBillingAccountString);
            if (mode == MODE_BILLING_REQUEST_LIST) {
                node.setAttribute("hasBillingItems", hasBillingItems);
                node.setAttribute("codeBillingStatus", codeBillingStatus);
            }
            if (!idInvoice.equals("")) {
                node.setAttribute("idInvoice", idInvoice);
            }
        }
        if (resultSetType == RESULT_SET_TYPE_DISK_USAGE) {
            Integer idDiskUsageByMonth = queryResult.getFieldValueInteger("dsk.idDiskUsageByMonth");
            String idDiskUsageByMonthString = idDiskUsageByMonth.toString();
            createDate = (Date) queryResult.getFieldValue("dsk.asOfDate");
            idBillingAccount = queryResult.getFieldValueInteger("bti.idBillingAccount");
            String idBillingAccountString = queryResult.getFieldValueIntegerAsString("bti.idBillingAccount");

            toolTip.append("Disk Usage Charge for ");
            String hasBillingItems = "Y";
            String codeBillingStatus = "";
            String idInvoice = "";
            if (!idBillingAccountString.equals("")) {
                Integer parsedIdBillingAccount = Integer.parseInt(idBillingAccountString);
                if (mode == MODE_BILLING_REQUEST_LIST) {
                    codeBillingStatus = this.determineStatusForOrder("idDiskUsageByMonth", idDiskUsageByMonth, parsedIdBillingAccount);
                }
                idInvoice = this.determineIdInvoiceForOrder("idDiskUsageByMonth", idDiskUsageByMonth, parsedIdBillingAccount);
            }
            String label = "Disk Usage";

            node.setAttribute("idRequest", idDiskUsageByMonthString);
            node.setAttribute("label", label);
            node.setAttribute("requestNumber", label);
            node.setAttribute("codeRequestCategory", DiskUsageByMonth.DISK_USAGE_REQUEST_CATEGORY);
            node.setAttribute("icon", DiskUsageByMonth.DISK_USAGE_ICON);
            node.setAttribute("type", label);
            node.setAttribute("submitter", "");
            node.setAttribute("idBillingAccount", idBillingAccountString);
            if (mode == MODE_BILLING_REQUEST_LIST) {
                node.setAttribute("hasBillingItems", hasBillingItems);
                node.setAttribute("codeBillingStatus", codeBillingStatus);
            }
            if (!idInvoice.equals("")) {
                node.setAttribute("idInvoice", idInvoice);
            }
        }

        String labLastName = queryResult.getFieldValueString("lab.lastName");
        String labFirstName = queryResult.getFieldValueString("lab.firstName");
        String labIsExternalPricing = queryResult.getFieldValueString("lab.isExternalPricing", null);
        String labIsExternalPricingCommercial = queryResult.getFieldValueString("lab.isExternalPricingCommercial", null);
        String idLab = queryResult.getFieldValueIntegerAsString("lab.idLab");

        String labName = Lab.formatLabName(labLastName, labFirstName);
        toolTip.append(" ");
        toolTip.append(labName);
        StringBuilder labBillingName = new StringBuilder();
        labBillingName.append(labName);
        labBillingName.append(" (");
        String billingAccountName = "";
        if (idBillingAccount != null) {
            BillingAccount billingAccount = sess.load(BillingAccount.class, idBillingAccount);
            String billingAccountNameAndNumber = billingAccount.getAccountNameAndNumber();
            labBillingName.append(billingAccountNameAndNumber);
            billingAccountName = billingAccountNameAndNumber;
        }
        labBillingName.append(")");

        node.setAttribute("idLab", idLab);
        node.setAttribute("labName", labName);
        node.setAttribute("labBillingName", labBillingName.toString());
        node.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
        node.setAttribute("isExternalPricingCommercial", labIsExternalPricingCommercial != null ? labIsExternalPricingCommercial : "N");
        node.setAttribute("billingAccountName", billingAccountName);

        if (createDate != null) {
            toolTip.append("\nsubmitted ");
            toolTip.append(formatter.formatDate(createDate, FieldFormatter.DATE_OUTPUT_DASH_SHORT));
        }
        if (completedDate != null) {
            toolTip.append(", completed ");
            toolTip.append(formatter.formatDate(completedDate, FieldFormatter.DATE_OUTPUT_DASH_SHORT));
        }
        node.setAttribute("toolTip", toolTip.toString());
        if (mode == MODE_BILLING_ITEM_LIST) {
            node.setAttribute("isDirty", "N");
        }
        return node;
    }

    private Element createLabNode(Element orderNode, QueryRowResult queryResult) {
        Integer idBillingAccount = Integer.parseInt(orderNode.getAttributeValue("idBillingAccount"));
        BillingAccount billingAccount = sess.load(BillingAccount.class, idBillingAccount);
        String labName = orderNode.getAttributeValue("labName");
        String billingAccountNameAndNumber = billingAccount.getAccountNameAndNumber();
        StringBuilder labBillingNameBuilder = new StringBuilder();
        labBillingNameBuilder.append(labName);
        labBillingNameBuilder.append(" (");
        labBillingNameBuilder.append(billingAccountNameAndNumber);
        labBillingNameBuilder.append(")");
        String labBillingName = labBillingNameBuilder.toString();
        Integer idLab = queryResult.getFieldValueInteger("lab.idLab");
        String contactEmail = queryResult.getFieldValueString("lab.contactEmail");
        String billingContactEmail = queryResult.getFieldValueString("lab.billingContactEmail");
        String billingNotificationEmail = Lab.formatBillingNotificationEmail(contactEmail, billingContactEmail);

        Element labNode = new Element("Lab");
        labNode.setAttribute("label", labBillingName);
        labNode.setAttribute("dataTip", labBillingName);
        labNode.setAttribute("idLab", idLab.toString());
        labNode.setAttribute("name", labName);
        labNode.setAttribute("idBillingAccount", idBillingAccount.toString());
        labNode.setAttribute("billingAccountName", billingAccountNameAndNumber);
        labNode.setAttribute("contactEmail", contactEmail);
        labNode.setAttribute("billingNotificationEmail", billingNotificationEmail);
        return labNode;
    }

    private Element createStatusNode(String codeBillingStatus) {
        Element statusNode = new Element("Status");
        statusNode.setAttribute("label", dh.getBillingStatus(codeBillingStatus));
        statusNode.setAttribute("status", codeBillingStatus);
        return statusNode;
    }

    private String determineStatusForOrder(String idColumnName, Integer id, Integer idBillingAccount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT codeBillingStatus FROM BillingItem WHERE ");
        queryBuilder.append(idColumnName);
        queryBuilder.append(" = ");
        queryBuilder.append(id);
        queryBuilder.append(" AND idBillingAccount = ");
        queryBuilder.append(idBillingAccount);
        queryBuilder.append(" AND idBillingPeriod = ");
        queryBuilder.append(idBillingPeriod);
        queryBuilder.append(" ORDER BY codeBillingStatus DESC");

        List result = sess.createQuery(queryBuilder.toString()).list();
        if (result.size() < 1) {
            return BillingStatus.NEW;
        } else {
            String currentCode;
            String finalCode = null;
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                currentCode = (String) iter.next();
                if (currentCode.equals(BillingStatus.PENDING)) {
                    return BillingStatus.PENDING;
                }
                if (finalCode == null) {
                    finalCode = currentCode;
                    continue;
                }
                if (finalCode.equals(BillingStatus.COMPLETED)) {
                    continue;
                }
                if (currentCode.equals(BillingStatus.COMPLETED)) {
                    finalCode = BillingStatus.COMPLETED;
                    continue;
                }
                if (finalCode.equals(BillingStatus.APPROVED)) {
                    continue;
                }
                if (currentCode.equals(BillingStatus.APPROVED)) {
                    finalCode = BillingStatus.APPROVED;
                    continue;
                }
                if (finalCode.equals(BillingStatus.APPROVED_PO)) {
                    continue;
                }
                if (currentCode.equals(BillingStatus.APPROVED_PO)) {
                    finalCode = BillingStatus.APPROVED_PO;
                    continue;
                }
                if (finalCode.equals(BillingStatus.APPROVED_CC)) {
                    continue;
                }
                if (currentCode.equals(BillingStatus.APPROVED_CC)) {
                    finalCode = BillingStatus.APPROVED_CC;
                }
            }
            return finalCode;
        }
    }

    private String determineIdInvoiceForOrder(String idColumnName, Integer id, Integer idBillingAccount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT idInvoice FROM BillingItem WHERE ");
        queryBuilder.append(idColumnName);
        queryBuilder.append(" = ");
        queryBuilder.append(id);
        queryBuilder.append(" AND idBillingAccount = ");
        queryBuilder.append(idBillingAccount);

        Integer idInvoice = (Integer) sess.createQuery(queryBuilder.toString()).uniqueResult();
        if (idInvoice != null) {
            return idInvoice.toString();
        } else {
            return "";
        }
    }

    private Set<Integer> getMainQueryResult(String className, String classShortName, String idColumnString) {
        boolean isRequest = classShortName.equals("req");
        boolean isDiskUsage = classShortName.equals("dsk");
        boolean joinWithLabNeeded = idLab != null;
        boolean joinWithBillingAccountNeeded = idBillingAccount != null || joinWithLabNeeded;

        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT DISTINCT ");
        queryBuffer.append(classShortName);
        queryBuffer.append(".");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" FROM BillingTemplate bt JOIN ");
        queryBuffer.append(className);
        queryBuffer.append(" ");
        queryBuffer.append(classShortName);
        queryBuffer.append(" ON bt.targetClassIdentifier = ");
        queryBuffer.append(classShortName);
        queryBuffer.append(".");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" LEFT JOIN BillingItem bi ON bi.");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" = ");
        queryBuffer.append(classShortName);
        queryBuffer.append(".");
        queryBuffer.append(idColumnString);

        if (joinWithLabNeeded || joinWithBillingAccountNeeded) {
            queryBuffer.append(" JOIN BillingTemplateItem bti ON bt.idBillingTemplate = bti.idBillingTemplate JOIN BillingAccount ba ON bti.idBillingAccount = ba.idBillingAccount ");
        }
        if (joinWithLabNeeded) {
            queryBuffer.append(" JOIN Lab lab ON ba.idLab = lab.idLab ");
        }

        queryBuffer.append(" WHERE bt.targetClassName LIKE '%");
        queryBuffer.append(className);
        queryBuffer.append("%' ");
        if (idBillingPeriod != null) {
            queryBuffer.append(" AND (bi.idBillingPeriod = ");
            queryBuffer.append(idBillingPeriod);

            BillingPeriod billingPeriod = sess.load(BillingPeriod.class, idBillingPeriod);
            if (isRequest && billingPeriod != null) {
                queryBuffer.append(" OR (((req.createDate >= '");
                queryBuffer.append(formatter.formatDateTime(billingPeriod.getStartDate(),  dateOutputStyle));
                queryBuffer.append("' AND req.createDate <= '");
                queryBuffer.append(formatter.formatDate(billingPeriod.getEndDate(), FieldFormatter.DATE_OUTPUT_SQL));
                queryBuffer.append(" 23:59:59') OR (req.completedDate >= '");
                queryBuffer.append(formatter.formatDate(billingPeriod.getStartDate(), FieldFormatter.DATE_OUTPUT_SQL));
                queryBuffer.append("' AND req.completedDate <= '");
                queryBuffer.append(formatter.formatDate(billingPeriod.getEndDate(), FieldFormatter.DATE_OUTPUT_SQL));
                queryBuffer.append(" 23:59:59')) AND bi.idBillingItem IS NULL)");
            }
            queryBuffer.append(") ");
        }
        if (idLab != null) {
            queryBuffer.append(" AND lab.idLab = ");
            queryBuffer.append(idLab);
            queryBuffer.append(" ");
        }
        if (idBillingAccount != null) {
            queryBuffer.append(" AND ba.idBillingAccount = ");
            queryBuffer.append(idBillingAccount);
            queryBuffer.append(" ");
        }
        if (idCoreFacility != null) {
            queryBuffer.append(" AND ");
            if (isDiskUsage) {
                queryBuffer.append("bi.idCoreFacility = ");
                queryBuffer.append(idCoreFacility);
            } else {
                queryBuffer.append(classShortName);
                queryBuffer.append(".idCoreFacility = ");
                queryBuffer.append(idCoreFacility);
            }
            queryBuffer.append(" ");
        }
        if (excludeNewRequests != null && excludeNewRequests && isRequest) {
            queryBuffer.append(" AND req.codeRequestStatus != '");
            queryBuffer.append(BillingStatus.NEW);
            queryBuffer.append("' ");
        }
        if (isRequest) {
            queryBuffer.append(" AND (req.archived IS NULL OR req.archived = 'N') ");
        }
        if (excludeInactiveBillingTemplates != null && excludeInactiveBillingTemplates) {
            queryBuffer.append(" AND (bt.isActive = 'Y' OR bt.isActive != 'N') ");
        }
        this.addSecurityCriteria(queryBuffer, false, classShortName);

        List result = sess.createNativeQuery(queryBuffer.toString()).list();
        Set<Integer> resultSet = new HashSet<Integer>(result.size());
        if (result.size() > 0) {
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                Integer id = (Integer) iter.next();
                resultSet.add(id);
            }
        }

        return resultSet;
    }

    private Set<Integer> getIdBillingItems(String idColumnName, Integer id, Integer idBillingAccount, boolean isOtherMode) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT idBillingItem FROM BillingItem WHERE ");
        query.append(idColumnName);
        query.append(" = ");
        query.append(id);
        if (isOtherMode) {
            query.append(" AND (idBillingAccount != ");
            query.append(idBillingAccount);
            query.append(" OR idBillingPeriod != ");
            query.append(idBillingPeriod);
            query.append(")");
        } else {
            query.append(" AND idBillingAccount = ");
            query.append(idBillingAccount);
            query.append(" AND idBillingPeriod = ");
            query.append(idBillingPeriod);
        }

        List result = sess.createQuery(query.toString()).list();
        Set<Integer> resultSet = new HashSet<Integer>(result.size());
        if (result.size() > 0) {
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                Integer idBillingItem = (Integer) iter.next();
                resultSet.add(idBillingItem);
            }
        }

        return resultSet;
    }

    private Set<Integer> getRequestNumberQueryResult() {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT DISTINCT idRequest FROM Request AS req WHERE (number LIKE :requestNumberVariant1 OR number = :requestNumberVariant2 OR number LIKE :requestNumberVariant3 OR number = :requestNumberVariant4) ");
        this.addSecurityCriteria(queryBuffer, false, "req");

        Query query = sess.createQuery(queryBuffer.toString());
        String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
        query.setParameter("requestNumberVariant1", requestNumberBase + "[0-9]");
        query.setParameter("requestNumberVariant2", requestNumberBase);
        query.setParameter("requestNumberVariant3", requestNumberBase + "R[0-9]");
        query.setParameter("requestNumberVariant4", requestNumberBase + "R");

        List result = query.list();
        Set<Integer> resultSet = new HashSet<Integer>(result.size());
        if (result.size() > 0) {
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                Integer id = (Integer) iter.next();
                resultSet.add(id);
            }
        }

        return resultSet;
    }

    private Set<Integer> getInvoiceNumberQueryResult(String className, String classShortName, String idColumnString) {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT DISTINCT ");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" FROM ");
        queryBuffer.append(className);
        queryBuffer.append(" AS ");
        queryBuffer.append(classShortName);
        queryBuffer.append(" WHERE ");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" IN (SELECT DISTINCT bi.");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" FROM BillingItem AS bi JOIN bi.invoice AS inv WHERE bi.");
        queryBuffer.append(idColumnString);
        queryBuffer.append(" IS NOT NULL AND inv.invoiceNumber LIKE :invoiceNumber) ");
        this.addSecurityCriteria(queryBuffer, false, classShortName);

        Query query = sess.createQuery(queryBuffer.toString());
        query.setParameter("invoiceNumber", invoiceLookupNumber);

        List result = query.list();
        Set<Integer> resultSet = new HashSet<Integer>(result.size());
        if (result.size() > 0) {
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                Integer id = (Integer) iter.next();
                resultSet.add(id);
            }
        }

        return resultSet;
    }

    private Set<QueryRowResult> makeOrderNodesQuery(Integer idOrder, int orderType) {
        StringBuilder query = new StringBuilder();

        // Specify columns
        query.append("SELECT DISTINCT ");
        query.append("lab.lastName, lab.firstName, lab.isExternalPricing, lab.isExternalPricingCommercial, lab.idLab, lab.contactEmail, lab.billingContactEmail, bti.idBillingAccount, ");
        if (orderType == RESULT_SET_TYPE_REQUEST) {
            query.append("req.idRequest, req.number, req.codeRequestCategory, req.createDate, req.completedDate, req.idSubmitter");
        } else if (orderType == RESULT_SET_TYPE_PRODUCT_ORDER) {
            query.append("po.idProductOrder, po.productOrderNumber, po.idCoreFacility, po.submitDate");
        } else if (orderType == RESULT_SET_TYPE_DISK_USAGE) {
            query.append("dsk.idDiskUsageByMonth, dsk.asOfDate");
        }

        query.append(" FROM BillingTemplate bt");
        query.append(" JOIN BillingTemplateItem bti ON bt.idBillingTemplate = bti.idBillingTemplate");
        query.append(" JOIN BillingAccount ba ON bti.idBillingAccount = ba.idBillingAccount");
        query.append(" JOIN Lab lab ON ba.idLab = lab.idLab");

        // Join appropriate order table
        if (orderType == RESULT_SET_TYPE_REQUEST) {
            query.append(" JOIN Request req ON req.idRequest = ");
            query.append(idOrder);
        } else if (orderType == RESULT_SET_TYPE_PRODUCT_ORDER) {
            query.append(" JOIN ProductOrder po ON po.idProductOrder = ");
            query.append(idOrder);
        } else if (orderType == RESULT_SET_TYPE_DISK_USAGE) {
            query.append(" JOIN DiskUsageByMonth dsk ON dsk.idDiskUsageByMonth = ");
            query.append(idOrder);
        }

        query.append(" WHERE bt.targetClassIdentifier = ");
        query.append(idOrder);
        query.append(" AND targetClassName LIKE '%");
        if (orderType == RESULT_SET_TYPE_REQUEST) {
            query.append("Request");
        } else if (orderType == RESULT_SET_TYPE_PRODUCT_ORDER) {
            query.append("ProductOrder");
        } else if (orderType == RESULT_SET_TYPE_DISK_USAGE) {
            query.append("DiskUsageByMonth");
        }
        query.append("%'");
        if (excludeInactiveBillingTemplates != null && excludeInactiveBillingTemplates) {
            query.append(" AND (bt.isActive = 'Y' OR bt.isActive != 'N')");
        }
        String queryString = query.toString();

        List queryResult = sess.createNativeQuery(queryString).list();
        Set<QueryRowResult> results = new HashSet<QueryRowResult>(queryResult.size());
        for (Iterator iter = queryResult.iterator(); iter.hasNext();) {
            Object[] rowFields = (Object[]) iter.next();
            results.add(new QueryRowResult(queryString, rowFields));
        }
        return results;
    }

    private void addSecurityCriteria(StringBuffer query, boolean addWhere, String classShortName) {
        if (!securityAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
            if (addWhere) {
                query.append(" WHERE ");
            } else {
                query.append(" AND ");
            }
            securityAdvisor.appendCoreFacilityCriteria(query, classShortName);
        }
    }

    private String getNonNullString(String string) {
        return string != null ? string : "";
    }

    private String getNonNullString(Integer integer) {
        return (integer != null) ? this.getNonNullString(integer.toString()) : "";
    }

    private String getNonNullString(BigDecimal bigDecimal, NumberFormat nf) {
        return bigDecimal != null ? nf.format(bigDecimal.doubleValue()) : "";
    }

    private Comparator<Element> statusNodeComparator = new Comparator<Element>() {
        @Override
        public int compare(Element e1, Element e2) {
            int status1Value = this.getStatusValue(e1.getAttributeValue("status"));
            int status2Value = this.getStatusValue(e2.getAttributeValue("status"));
            return status1Value - status2Value;
        }
        private int getStatusValue(String status) {
            if (status == null) {
                return 0;
            } else if (status.equalsIgnoreCase(BillingStatus.NEW)) {
                return 1;
            } else if (status.equalsIgnoreCase(BillingStatus.PENDING)) {
                return 2;
            } else if (status.equalsIgnoreCase(BillingStatus.COMPLETED)) {
                return 3;
            } else if (status.equalsIgnoreCase(BillingStatus.APPROVED)) {
                return 6;
            } else if (status.equalsIgnoreCase(BillingStatus.APPROVED_PO)) {
                return 5;
            } else if (status.equalsIgnoreCase(BillingStatus.APPROVED_CC)) {
                return 4;
            } else {
                return 0;
            }
        }
    };

    private Comparator<Element> orderNodeComparator = new Comparator<Element>() {
        @Override
        public int compare(Element e1, Element e2) {
            boolean e1IsProductOrder = e1.getAttributeValue("idProductOrder") == null;
            boolean e2IsProductOrder = e2.getAttributeValue("idProductOrder") == null;
            if ((!e1IsProductOrder && !e2IsProductOrder) || (e1IsProductOrder && e2IsProductOrder)) {
                return e1.getAttributeValue("idRequest").compareTo(e2.getAttributeValue("idRequest"));
            } else if (e1IsProductOrder && !e2IsProductOrder) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    private Comparator<Element> labNodeComparator = new Comparator<Element>() {
        @Override
        public int compare(Element e1, Element e2) {
            return e1.getAttributeValue("label").compareTo(e2.getAttributeValue("label"));
        }
    };

    private Comparator<Element> billingItemNodeComparator = new Comparator<Element>() {
        @Override
        public int compare(Element e1, Element e2) {
            return e1.getAttributeValue("idBillingItem").compareTo(e2.getAttributeValue("idBillingItem"));
        }
    };
}
