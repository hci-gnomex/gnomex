package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;
public class GetProductLedgerList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetProductLedgerList.class);

  private Integer idLab;
  private Integer idProduct;

  @Override
  public void loadCommand(HttpServletRequest req, HttpSession sess) {

    if(req.getParameter("idLab") != null && !req.getParameter("idLab").equals("")) {
      idLab = Integer.parseInt(req.getParameter("idLab"));
    }

    if(req.getParameter("idProduct") != null && !req.getParameter("idProduct").equals("")) {
      idProduct = Integer.parseInt(req.getParameter("idProduct"));
    }

  }


  @Override
  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.username);

      Set coreList = this.getSecAdvisor().getCoreFacilitiesIManage(); //if core facility manager get all labs for that core

      Set userLabList = this.getSecAdvisor().getAllMyGroups(); //if regular user get all labs they are a part of

      List ledger = new ArrayList();
      List pendingLedger = new ArrayList();
      StringBuffer buf = new StringBuffer();
      //If they are super admin show everything
      if(this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        buf = generateSuperAdminLedgerQuery();
        ledger = sess.createQuery(buf.toString()).list();
        
        buf = generateSuperAdminPendingQuery();
        pendingLedger = sess.createQuery(buf.toString()).list();
      } else if(coreList.size() > 0) { //Get labs in cores they manage if not super admin
        buf = generateLedgerQuery(true, coreList);
        ledger = sess.createQuery(buf.toString()).list();
        
        buf = generatePendingQuery(true, coreList);
        pendingLedger = sess.createQuery(buf.toString()).list();
      } else if(userLabList.size() > 0) { //If no admin capabilities then add all labs that user is a part of
        buf = generateLedgerQuery(false, userLabList);
        ledger = sess.createQuery(buf.toString()).list();
        
        buf = generatePendingQuery(false, userLabList);
        pendingLedger = sess.createQuery(buf.toString()).list();
      }

      Document doc = new Document(new Element("LedgerList"));

      HashMap<String, List> nodes = new HashMap();
      for(Iterator i = ledger.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        String firstName = row[0] != null ? (String)row[0] : "";
        String lastName = row[1] != null ? (String)row[1] : "";
        String qty = row[2] != null ? ((Long)row[2]).toString() : "";
        String idProduct = row[3] != null ? ((Integer)row[3]).toString() : "";
        String name = row[4] != null ? (String)row[4] : "";
        String idLab = row[5] != null ? ((Integer)row[5]).toString() : "";
        
        Object[] pendingRow = getPendingRow(idLab, idProduct, pendingLedger);
        String pendingQty = "";
        if (pendingRow != null) {
        	pendingQty = pendingRow[2] != null ? ((Long)pendingRow[2]).toString() : "";
        }

        Element product = new Element("product");
        if (!qty.equals("") && !pendingQty.equals("")) {
        	product.setAttribute("display", name + " (" + qty + ", Pending " + pendingQty + ")");
        } else if (!qty.equals("")) {
        	product.setAttribute("display", name + " (" + qty + ")");
        } else {
        	product.setAttribute("display", name);
        }
        product.setAttribute("idProduct", idProduct);
        product.setAttribute("idLab", idLab);
        product.setAttribute("qty", qty);
        product.setAttribute("pendingQty", pendingQty);

        String labDisplay = Lab.formatLabNameFirstLast(firstName, lastName);
        if(nodes.containsKey(labDisplay)) {
          List elements = nodes.get(labDisplay);
          elements.add(product);
        } else {
          List list = new ArrayList();
          list.add(product);
          nodes.put(labDisplay, list);
        }

      }
      
      // Add products that only have pending balances
      for (Iterator remainingPendingIter = pendingLedger.iterator(); remainingPendingIter.hasNext();) {
          Object[] pendingRow = (Object[]) remainingPendingIter.next();
          String firstName = pendingRow[3] != null ? (String)pendingRow[3] : "";
          String lastName = pendingRow[4] != null ? (String)pendingRow[4] : "";
          String qty = "0";
          String idProduct = pendingRow[1] != null ? ((Integer)pendingRow[1]).toString() : "";
          String name = pendingRow[5] != null ? (String)pendingRow[5] : "";
          String idLab = pendingRow[0] != null ? ((Integer)pendingRow[0]).toString() : "";
          String pendingQty = pendingRow[2] != null ? ((Long)pendingRow[2]).toString() : "";

          Element product = new Element("product");
          if (!pendingQty.equals("")) {
          	product.setAttribute("display", name + " (" + qty + ", Pending " + pendingQty + ")");
          } else {
          	product.setAttribute("display", name);
          }
          product.setAttribute("idProduct", idProduct);
          product.setAttribute("idLab", idLab);
          product.setAttribute("qty", qty);
          product.setAttribute("pendingQty", pendingQty);

          String labDisplay = Lab.formatLabNameFirstLast(firstName, lastName);
          if(nodes.containsKey(labDisplay)) {
            List elements = nodes.get(labDisplay);
            elements.add(product);
          } else {
            List list = new ArrayList();
            list.add(product);
            nodes.put(labDisplay, list);
          }

      }

      for(Iterator i = nodes.keySet().iterator(); i.hasNext();) {
        String labName = (String)i.next();
        Element lab = new Element("Lab");
        lab.setAttribute("display", labName);

        List <Element>productNodes = nodes.get(labName);
        lab.setAttribute("idLab", productNodes.get(0).getAttributeValue("idLab"));

        for(Element prod : productNodes) {
          lab.addContent(prod);
        }

        doc.getRootElement().addContent(lab);

      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

    }catch(Exception e) {
      LOG.error("An exception has occurred in GetLabList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }



    return null;
  }

  @Override
  public void validate() {
    
  }
  
  private Object[] getPendingRow(String idLab, String idProduct, List pendingLedger) {
	  Object[] pendingRow = null;
	  
	  if (!idLab.equals("") && !idProduct.equals("")) {
		  for (int index = 0; index < pendingLedger.size(); index++) {
			  Object[] candidate = (Object[]) pendingLedger.get(index);
			  String pendingIdLab = candidate[0] != null ? ((Integer)candidate[0]).toString() : "";
			  String pendingIdProduct = candidate[1] != null ? ((Integer)candidate[1]).toString() : "";
			  if (idLab.equals(pendingIdLab) && idProduct.equals(pendingIdProduct)) {
				  pendingRow = (Object[]) pendingLedger.remove(index);
				  break;
			  }
		  }
	  }
	  
	  return pendingRow;
  }
  
  private StringBuffer generateSuperAdminLedgerQuery() {
	  StringBuffer buf = new StringBuffer();
      buf.append("SELECT lab.firstName, lab.lastName, SUM(pl.qty), pl.idProduct, prod.name, lab.idLab ");
      buf.append(" FROM ProductLedger as pl ");
      buf.append(" JOIN pl.lab as lab ");
      buf.append(" JOIN pl.product as prod ");

      boolean addWhereOrAnd = false;
      
      if(idLab != null) {
        buf.append(" WHERE lab.idLab = " + idLab);
        addWhereOrAnd = true;
      }

      if(idProduct != null) {
        if(addWhereOrAnd) {
          buf.append(" AND pl.idProduct = " + idProduct); 
        } else {
          buf.append(" WHERE pl.idProduct = " + idProduct);
        }
      }
      buf.append(" GROUP BY lab.lastName, lab.firstName, lab.idLab, pl.idProduct, prod.name ");
      buf.append(" ORDER BY lab.lastName, SUM(pl.qty) ");
      
      return buf;
  }
  
  private StringBuffer generateSuperAdminPendingQuery() {
	  StringBuffer buf = new StringBuffer();
	  
      buf.append(" SELECT lab.idLab, pli.idProduct, SUM(pli.qty), lab.firstName, lab.lastName, prod.name ");
      buf.append(" FROM ProductLineItem as pli ");
      buf.append(" JOIN pli.productOrder as po ");
      buf.append(" JOIN pli.product as prod ");
      buf.append(" JOIN po.lab as lab ");
      
      buf.append(" WHERE pli.codeProductOrderStatus != \'COMPLETE\' ");
      if (idLab != null) {
        buf.append(" AND po.idLab = " + idLab);
      }
      if (idProduct != null) {
    	  buf.append(" AND pli.idProduct = " + idProduct);
      }
      
      buf.append(" GROUP BY lab.idLab, pli.idProduct, lab.firstName, lab.lastName, prod.name ");
      
      return buf;
  }

  private StringBuffer generateLedgerQuery(Boolean forCore, Set criteria) {
    StringBuffer buf = new StringBuffer();
    if(forCore) {
      buf.append("SELECT lab.firstName, lab.lastName, SUM(pl.qty), pl.idProduct, prod.name, lab.idLab ");
      buf.append(" FROM ProductLedger as pl ");
      buf.append(" JOIN pl.lab as lab ");
      buf.append(" JOIN pl.product as prod ");
      buf.append(" JOIN prod.productType as prodType ");
      buf.append(" LEFT JOIN lab.coreFacilities as coreFacility ");
      
      // Lab core facility
      buf.append(" WHERE coreFacility.idCoreFacility in ( ");

      for(Iterator i = criteria.iterator(); i.hasNext();) {
        CoreFacility cf = (CoreFacility)i.next();
        buf.append(cf.getIdCoreFacility());

        if(i.hasNext()) {
          buf.append(" , ");
        }
      }

      buf.append(" ) ");
      
      // Product type core facility
      buf.append(" AND prodType.idCoreFacility in ( ");

      for(Iterator i = criteria.iterator(); i.hasNext();) {
        CoreFacility cf = (CoreFacility)i.next();
        buf.append(cf.getIdCoreFacility());

        if(i.hasNext()) {
          buf.append(" , ");
        }
      }

      buf.append(" ) ");
      
      // Other Criteria
      if(idLab != null) {
        buf.append(" AND lab.idLab = " + idLab);
      }

      if(idProduct != null) {
        buf.append(" AND pl.idProduct = " + idProduct);
      }


      buf.append(" GROUP BY lab.lastName, lab.firstName, lab.idLab, pl.idProduct, prod.name ");
      buf.append(" ORDER BY lab.lastName, SUM(pl.qty) ");

    } else {
      buf.append("SELECT lab.firstName, lab.lastName, SUM(pl.qty), pl.idProduct, prod.name, lab.idLab ");
      buf.append(" FROM ProductLedger as pl ");
      buf.append(" JOIN pl.lab as lab ");
      buf.append(" JOIN pl.product as prod ");
      buf.append(" WHERE lab.idLab in ( ");

      for(Iterator i = criteria.iterator(); i.hasNext();) {
        Lab l = (Lab)i.next();
        buf.append(l.getIdLab());

        if(i.hasNext()) {
          buf.append(" , ");
        }
      }

      buf.append(" ) ");

      if(idLab != null) {
        buf.append(" AND lab.idLab = " + idLab);
      }

      if(idProduct != null) {
        buf.append(" AND pl.idProduct = " + idProduct);
      }

      buf.append(" GROUP BY lab.lastName, lab.firstName, lab.idLab, pl.idProduct, prod.name ");
      buf.append(" ORDER BY lab.lastName, SUM(pl.qty) ");

    }

    return buf;
  }
  
  private StringBuffer generatePendingQuery(Boolean forCore, Set criteria) {
      StringBuffer buf = new StringBuffer();
      
      if (forCore) {
        buf.append(" SELECT lab.idLab, pli.idProduct, SUM(pli.qty), lab.firstName, lab.lastName, prod.name ");
        buf.append(" FROM ProductLineItem as pli ");
        buf.append(" JOIN pli.productOrder as po ");
        buf.append(" JOIN po.lab as lab ");
        buf.append(" JOIN pli.product as prod ");
        buf.append(" JOIN prod.productType as prodType ");
        buf.append(" LEFT JOIN lab.coreFacilities as coreFacility ");
        
        buf.append(" WHERE pli.codeProductOrderStatus != \'COMPLETE\' ");
        
        // Lab core facility
        buf.append(" AND coreFacility.idCoreFacility in ( ");

        for (Iterator i = criteria.iterator(); i.hasNext();) {
          CoreFacility cf = (CoreFacility)i.next();
          buf.append(cf.getIdCoreFacility());

          if (i.hasNext()) {
            buf.append(" , ");
          }
        }

        buf.append(" ) ");
        
        // Product type core facility
        buf.append(" AND prodType.idCoreFacility in ( ");

        for (Iterator i = criteria.iterator(); i.hasNext();) {
          CoreFacility cf = (CoreFacility)i.next();
          buf.append(cf.getIdCoreFacility());

          if (i.hasNext()) {
            buf.append(" , ");
          }
        }

        buf.append(" ) ");
        
        // Other Criteria
        if (idLab != null) {
          buf.append(" AND lab.idLab = " + idLab);
        }

        if (idProduct != null) {
          buf.append(" AND pli.idProduct = " + idProduct);
        }

        buf.append(" GROUP BY lab.idLab, pli.idProduct, lab.firstName, lab.lastName, prod.name ");

      } else {
        buf.append(" SELECT lab.idLab, pli.idProduct, SUM(pli.qty), lab.firstName, lab.lastName, prod.name ");
        buf.append(" FROM ProductLineItem as pli ");
        buf.append(" JOIN pli.productOrder as po ");
        buf.append(" JOIN pli.product as prod ");
        buf.append(" JOIN po.lab as lab ");
        
        buf.append(" WHERE pli.codeProductOrderStatus != \'COMPLETE\' ");
        
        buf.append(" AND lab.idLab in ( ");

        for (Iterator i = criteria.iterator(); i.hasNext();) {
          Lab l = (Lab)i.next();
          buf.append(l.getIdLab());

          if (i.hasNext()) {
            buf.append(" , ");
          }
        }

        buf.append(" ) ");

        if (idLab != null) {
          buf.append(" AND lab.idLab = " + idLab);
        }

        if (idProduct != null) {
          buf.append(" AND pli.idProduct = " + idProduct);
        }

        buf.append(" GROUP BY lab.idLab, pli.idProduct, lab.firstName, lab.lastName, prod.name ");

      }

      return buf;
  }

}
