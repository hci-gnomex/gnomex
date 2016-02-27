package hci.gnomex.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProductOrder extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrder.class);

  private Integer idProductOrder;
  private Integer productOrderNumber;
  private String serverName;

  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    if (request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = Integer.valueOf(request.getParameter("idProductOrder"));
    }

    if (request.getParameter("productOrderNumber") != null && !request.getParameter("productOrderNumber").equals("")) {
      productOrderNumber = Integer.valueOf(request.getParameter("productOrderNumber"));
    }

    if (idProductOrder == null && productOrderNumber == null) {
      this.addInvalidField("identification", "Please provide either an idProductOrder or a productOrderNumber");
    }

    serverName = request.getServerName();

  }

  @Override
  public Command execute() throws RollBackCommandException {
    try {
      if (this.isValid()) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.username);
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        ProductOrder po;
        try {
          if (idProductOrder != null) {
            po = sess.load(ProductOrder.class, idProductOrder);
          } else {
            po = (ProductOrder) sess.createQuery("FROM ProductOrder po WHERE po.productOrderNumber = \'" + productOrderNumber + "\'").uniqueResult();
          }
        } catch (Exception e) {
          log.error(e.getMessage());
          po = null;
        }

        String baseDir = PropertyDictionaryHelper.getInstance(sess).getProductOrderDirectory(serverName, po.getIdCoreFacility());

        boolean canRead = false;
        if (po != null) {
          canRead = this.getSecAdvisor().canRead(po);
        }

        Element root = new Element("ProductOrder");
        if (po != null) {
          root.setAttribute("productOrderNumber", po.getProductOrderNumber() != null ? po.getProductOrderNumber() : po.getIdProductOrder().toString());
          if (canRead) {
            String billingAccountName = "";

            if (po.getIdBillingAccount() != null) {
              BillingAccount ba = sess.load(BillingAccount.class, po.getIdBillingAccount());
              billingAccountName = ba.getAccountNameDisplay();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String submitDate = po.getSubmitDate() != null ? sdf.format(po.getSubmitDate()) : "";
            root.setAttribute("idProductOrder", po.getIdProductOrder().toString());
            root.setAttribute("submitter", po.getSubmitter().getFirstLastDisplayName());
            root.setAttribute("labName", po.getLab().getFormattedLabName(true));
            root.setAttribute("submitDate", submitDate);
            root.setAttribute("orderStatus", po.getStatus());
            root.setAttribute("quoteNumber", po.getQuoteNumber() != null ? po.getQuoteNumber() : "");
            root.setAttribute("quoteReceivedDate", po.getQuoteReceivedDate() != null ? po.getQuoteReceivedDate().toString() : "");
            root.setAttribute("billingAccount", billingAccountName);
            root.setAttribute("canRead", "Y");
            root.setAttribute("key", po.getKey());
          } else {
            root.setAttribute("canRead", "N");
          }
        }

        // Show files uploads that are in the staging area.
        // Only show these files if user has write permissions.
        // Hash the know analysis files
        Map knownProductOrderFileMap = new HashMap();
        for (Iterator i = po.getFiles().iterator(); i.hasNext();) {
          ProductOrderFile pof = (ProductOrderFile) i.next();
          knownProductOrderFileMap.put(pof.getFileName(), pof);
        }

        // Now add in the files from the upload staging area
        Element filesNode = new Element("ExpandedProductOrderFileList");
        root.addContent(filesNode);

        Map productOrderMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List ProductOrderIds = new ArrayList<Integer>();
        GetProductOrderDownloadList.getFileNamesToDownload(baseDir, po.getKey(), ProductOrderIds, productOrderMap, directoryMap, false);

        for (Iterator i = ProductOrderIds.iterator(); i.hasNext();) {
          Integer idProductOrder = (Integer) i.next();
          List directoryKeys = (List) productOrderMap.get(idProductOrder);

          // For each directory of analysis
          for (Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {

            String directoryKey = (String) i1.next();

            String[] dirTokens = directoryKey.split("-");

            String directoryName = "";
            if (dirTokens.length > 1) {
              directoryName = dirTokens[1];
            }

            // Show files uploads that are in the staging area.
            Element productOrderUploadNode = new Element("ProductOrderUpload");
            filesNode.addContent(productOrderUploadNode);
            String key = po.getKey(Constants.UPLOAD_STAGING_DIR);
            GetProductOrderDownloadList.addExpandedFileNodes(baseDir, root, productOrderUploadNode, idProductOrder, key, dh, knownProductOrderFileMap, fileMap, sess);
          }
        }

        Document doc = new Document(root);
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);

      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e) {
      log.error("An exception has occurred in GetProductOrder ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {

      }

    }
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
