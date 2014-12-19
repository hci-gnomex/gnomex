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

public class GetProductLedgerList extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductLedgerList.class);

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
      StringBuffer buf = new StringBuffer();
      //If they are super admin show everything
      if(this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        buf = new StringBuffer();
        buf.append("SELECT lab.firstName, lab.lastName, SUM(pl.qty), pl.idProduct, prod.name, lab.idLab ");
        buf.append(" FROM ProductLedger as pl ");
        buf.append(" JOIN pl.lab as lab ");
        buf.append(" JOIN pl.product as prod ");
        buf.append(" GROUP BY lab.lastName, lab.firstName, lab.idLab, pl.idProduct, prod.name ");
        buf.append(" ORDER BY lab.lastName, SUM(pl.qty) ");
        ledger = sess.createQuery(buf.toString()).list();
      } else if(coreList.size() > 0) { //Get labs in cores they manage if not super admin
        buf = generateQuery(true, coreList);
        ledger = sess.createQuery(buf.toString()).list();

      } else if(userLabList.size() > 0) { //If no admin capabilities then add all labs that user is a part of
        buf = generateQuery(false, userLabList);
        ledger = sess.createQuery(buf.toString()).list();
      }

      Document doc = new Document(new Element("LedgerList"));

      HashMap<String, List> nodes = new HashMap();
      for(Iterator i = ledger.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        String firstName = row[0] != null ? (String)row[0] : "";
        String lastName = row[1] != null ? (String)row[1] : "";
        String qty = row[2] != null ? ((Integer)row[2]).toString() : "";
        String idProduct = row[3] != null ? ((Integer)row[3]).toString() : "";
        String name = row[4] != null ? (String)row[4] : "";
        String idLab = row[5] != null ? ((Integer)row[5]).toString() : "";

        Element product = new Element("product");
        product.setAttribute("display", name + " (" + qty + ")");
        product.setAttribute("idProduct", idProduct);
        product.setAttribute("idLab", idLab);
        product.setAttribute("qty", qty);

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
      log.error("An exception has occurred in GetLabList ", e);
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
    // TODO Auto-generated method stub

  }

  private StringBuffer generateQuery(Boolean forCore, Set criteria) {
    StringBuffer buf = new StringBuffer();
    if(forCore) {
      buf.append("SELECT lab.firstName, lab.lastName, SUM(pl.qty), pl.idProduct, prod.name, lab.idLab ");
      buf.append(" FROM ProductLedger as pl ");
      buf.append(" JOIN pl.lab as lab ");
      buf.append(" JOIN pl.product as prod ");
      buf.append(" LEFT JOIN lab.coreFacilities as coreFacility ");
      buf.append(" WHERE coreFacility.idCoreFacility in ( ");

      for(Iterator i = criteria.iterator(); i.hasNext();) {
        CoreFacility cf = (CoreFacility)i.next();
        buf.append(cf.getIdCoreFacility());

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

}
