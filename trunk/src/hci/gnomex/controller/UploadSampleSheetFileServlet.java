package hci.gnomex.controller;

import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.*;

import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import java.io.BufferedReader;
import java.io.FileReader;

public class UploadSampleSheetFileServlet extends HttpServlet {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String  directoryName = "";
  
  private String   fileName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
  }

  /*
   * SPECIAL NOTE -  This servlet must be run on non-secure socket layer (http) in order to
   *                 keep track of previously created session. (see note below concerning
   *                 flex upload bug on Safari and FireFox).  Otherwise, session is 
   *                 not maintained.  Although the code tries to work around this
   *                 problem by creating a new security advisor if one is not found,
   *                 the Safari browser cannot handle authenicating the user (this second time).
   *                 So for now, this servlet must be run non-secure. 
   */
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      Session sess = HibernateSession.currentSession(req.getUserPrincipal().getName());

      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadSampleSheetFileServlet:  Warning - unable to find existing session. Creating security advisor.");
        secAdvisor = SecurityAdvisor.create(sess, req.getUserPrincipal().getName());
      }

      //
      // To work around flex upload problem with FireFox and Safari, create security advisor since
      // we loose session and thus don't have security advisor in session attribute.
      //
      // Note from Flex developer forum (http://www.kahunaburger.com/2007/10/31/flex-uploads-via-httphttps/):
      // Firefox uses two different processes to upload the file.
      // The first one is the one that hosts your Flex (Flash) application and communicates with the server on one channel.
      // The second one is the actual file-upload process that pipes multipart-mime data to the server. 
      // And, unfortunately, those two processes do not share cookies. So any sessionid-cookie that was established in the first channel 
      // is not being transported to the server in the second channel. This means that the server upload code cannot associate the posted 
      // data with an active session and rejects the data, thus failing the upload.
      //
      if (secAdvisor == null) {
        System.out.println("UploadSampleSheetFileServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload sample sheet file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }

      String className = "SampleSheet";
      Document doc = new Document(new Element(className));
      
      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");
   
      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
      Part part;
      
      directoryName = dh.getProperty(Property.TEMP_DIRECTORY);
      
      boolean fileWasWritten = false;
      boolean hasColumnNames = false;
      
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("hasColumnNames")) {
            String hasColumnNamesValue = (String)value;
            if(hasColumnNamesValue != null && hasColumnNamesValue.compareTo("1")== 0) {
              hasColumnNames = true;
            }

          }
        }
        if (part.isFile()) {
          // it's a file part
          FilePart filePart = (FilePart) part;
          fileName = filePart.getFileName();
          if (fileName != null) {
            // the part actually contained a file
            filePart.writeTo(new File(directoryName));
            fileWasWritten = true;
          }
          else { 
          }
          out.flush();
        }        
      }

      if(fileWasWritten) {
        Element columnSelector = new Element("ColumnSelector");
        // Add a blank column selector
        Element columnSelectorItem = new Element("ColumnSelectorItem");
        
        // Add a blank entry for the default
        columnSelectorItem.setAttribute("label", " ");
        columnSelectorItem.setAttribute("data", "0");
        columnSelector.addContent(columnSelectorItem);
        
        Element sampleSheetList = new Element("SampleSheetData");
        Element currentRow;
        int rowNum = 1;
        
        BufferedReader readbuffer = new BufferedReader(new FileReader(directoryName+fileName));
        String strRead;
        while ((strRead=readbuffer.readLine())!=null) {
          currentRow = new Element("Row");
          currentRow.setAttribute("Name", ""+rowNum);
          sampleSheetList.addContent(currentRow);
          String splitarray[] = strRead.split("\t");
          for(int i = 0; i < splitarray.length; i++) {
            String thisEntry = splitarray[i]; 
            int colNum = i+1;
            if(rowNum == 1) {
              columnSelectorItem = new Element("ColumnSelectorItem");
              // If on first row build the column selector
              if(hasColumnNames) {
                columnSelectorItem.setAttribute("label", thisEntry);
              } else {
                columnSelectorItem.setAttribute("label", "Column " + colNum);
              }
              columnSelectorItem.setAttribute("data", "" + colNum);
              columnSelector.addContent(columnSelectorItem);
            }
            Element currentCol = new Element("Column");
            currentCol.setAttribute("Name", "" + colNum);            
            currentCol.setAttribute("Value", thisEntry);            
            currentRow.addContent(currentCol);            
          }
          rowNum++;
        }
        readbuffer.close();
        doc.getRootElement().addContent(columnSelector);
        doc.getRootElement().addContent(sampleSheetList);
      }
      
      // Delete the file when finished
      File f = new File(directoryName+fileName);
      f.delete();
      
      PrintWriter responseOut = res.getWriter();
      res.setHeader("Cache-Control", "cache, must-revalidate, proxy-revalidate, s-maxage=0, max-age=0");
      res.setHeader("Pragma", "public");
      res.setDateHeader("Expires", 0);
      res.setContentType("application/xml");
      
      XMLOutputter xmlOut = new XMLOutputter();
      responseOut.println(xmlOut.outputString(doc));   
    
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
    }  finally {
      try {
        HibernateSession.closeSession();        
      } catch (Exception e1) {
        System.out.println("UploadSampleSheetFileServlet warning - cannot close hibernate session");
      }
    } 

  }
}
