package hci.gnomex.controller;

import hci.gnomex.model.BillingAccount;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadPurchaseOrder extends HttpServlet {
  private Integer idBillingAccount;
  private String fileName;
  private File file;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {

  }

  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      Session sess = HibernateSession.currentSession(req.getUserPrincipal().getName());

      res.setContentType("text/html");
      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
      FileInputStream fileStream;
      byte[] blob = new byte[1024];
      Part part;
      String fileType = null;


      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("idBillingAccount")) {
            idBillingAccount = new Integer((String)value);
          }
        }
        else if(part.isFile()){
          FilePart filePart = (FilePart) part;
          fileName = filePart.getFileName();
          fileType = fileName.substring(fileName.indexOf("."));

          if(fileName != null){
            file = new File(fileName);
            filePart.writeTo(file);
            fileStream = new FileInputStream(file);
            blob = new byte[(int)file.length()];
            fileStream.read(blob);
            
          }
        }
      }
      BillingAccount ba = (BillingAccount)sess.load(BillingAccount.class, idBillingAccount);
      ba.setPurchaseOrderForm(blob);
      ba.setOrderFormFileType(fileType.toLowerCase());
      ba.setOrderFormFileSize(new Long(file.length()));
      sess.update(ba);
      sess.flush();

    } 
    catch (Exception e) {
      HibernateSession.rollback();
      e.printStackTrace();
    }
    finally{
      try {
        HibernateSession.closeSession();
        HibernateSession.closeTomcatSession();
      } catch (Exception e) {
        e.printStackTrace();
      }
      }
    }



  }

