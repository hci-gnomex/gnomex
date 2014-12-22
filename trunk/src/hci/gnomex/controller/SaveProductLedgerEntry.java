package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProductLedger;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class SaveProductLedgerEntry extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProductLedgerEntry.class);

  private Integer idLab;
  private Integer idProduct;
  private Integer qty;
  private String comment;


  @Override
  public void loadCommand(HttpServletRequest request, HttpSession sess) {

    if(request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.parseInt(request.getParameter("idLab"));
    } else {
      this.addInvalidField("missing idLab", "missing idLab");
    }

    if(request.getParameter("idProduct") != null && !request.getParameter("idProduct").equals("")) {
      idProduct = Integer.parseInt(request.getParameter("idProduct"));
    } else {
      this.addInvalidField("missing idProduct", "missing idProduct");
    }

    if(request.getParameter("qty") != null && !request.getParameter("qty").equals("")) {
      qty = Integer.parseInt(request.getParameter("qty"));
    } else {
      this.addInvalidField("missing qty", "missing qty");
    }

    if(request.getParameter("comment") != null && !request.getParameter("comment").equals("")) {
      comment = request.getParameter("comment");
    } else {
      this.addInvalidField("missing comment", "missing comment");
    }



  }

  @Override
  public Command execute() throws RollBackCommandException {

    try {

      if(this.isValid()) {

        Session sess = this.getSecAdvisor().getHibernateSession(this.username);

        ProductLedger pl = new ProductLedger();

        pl.setIdLab(idLab);
        pl.setQty(qty);
        pl.setComment(comment);
        pl.setIdProduct(idProduct);

        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-mm-dd hh:mm:ss");

        pl.setTimeStamp(new Timestamp(System.currentTimeMillis()));

        sess.save(pl);
        this.setResponsePage(this.SUCCESS_JSP);

      } else {
        this.setResponsePage(this.ERROR_JSP); 
      }

    }catch(Exception e) {
      log.error("An exception has occurred in SaveProductLedgerEntry ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());  

    }finally {
      try {
        this.getSecAdvisor().closeHibernateSession();        
      } catch(Exception e) {

      }
    }
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
