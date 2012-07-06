package hci.gnomex.controller;

import hci.gnomex.model.BillingAccount;
import hci.gnomex.utility.HibernateSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class GetPurchaseOrderForm extends HttpServlet {
  private Integer idBillingAccount;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    doPost(req, res);
  }

  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      Session sess = HibernateSession.currentSession(req.getUserPrincipal().getName());

      byte[] output = new byte[8000];
      String fileType = "";

      if(req.getParameter("idBillingAccount") != null){
        idBillingAccount = Integer.parseInt(req.getParameter("idBillingAccount"));
      }
      
      res.setHeader("Content-Disposition", "inline; filename=BillingAccount" + idBillingAccount);

      BillingAccount ba = (BillingAccount)sess.load(BillingAccount.class, idBillingAccount);

      ByteArrayInputStream in = new ByteArrayInputStream(ba.getPurchaseOrderForm());
      ServletOutputStream out = res.getOutputStream();
      fileType = ba.getOrderFormFileType();

      if(fileType.equals(".pdf")){
        res.setHeader("Content-Type", "application/pdf");
        res.setHeader("Content-Disposition", "inline; filename=BillingAccount" + idBillingAccount + ".pdf");
      }
      else if(fileType.equals(".doc")){
        res.setHeader("Content-Type", "application/msword");
        res.setHeader("Content-Disposition", "inline; filename=BillingAccount" + idBillingAccount + ".doc");
      }
      else if(fileType.equals(".txt")){
        res.setHeader("Content-Type", "text/plain");
      }
      else if(fileType.equals(".html")){
        res.setHeader("Content-Type", "text/html");
      }
      else if(fileType.equals(".zip")){
        res.setHeader("Content-Type", "application/zip");
        res.setHeader("Content-Disposition", "inline; filename=BillingAccount" + idBillingAccount + ".zip");
      }
      else if(fileType.equals(".xls")){
        res.setHeader("Content-Type", "application/msexcel");
        res.setHeader("Content-Disposition", "inline; filename=BillingAccount" + idBillingAccount + ".xls");
      }

      while(in.read(output , 0, 1024) != -1){
        out.write(output, 0, 1024);
      }
      res.setContentLength((int)output.length);   
      out.flush(); 
      out.close();

      return;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally{
      try {
        HibernateSession.closeSession();
      } catch (Exception e) {
        e.printStackTrace();
      }
      }
  }
}
