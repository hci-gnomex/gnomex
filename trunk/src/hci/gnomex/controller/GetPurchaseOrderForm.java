package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hsqldb.Binary;

import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;

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
