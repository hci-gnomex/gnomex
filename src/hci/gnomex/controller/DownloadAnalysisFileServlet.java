package hci.gnomex.controller;

import hci.gnomex.model.Analysis;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.AnalysisFileDescriptorParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadAnalysisFileServlet extends HttpServlet { 


  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadAnalysisFileServlet.class);
  
  private AnalysisFileDescriptorParser parser = null;
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {

    //  Get cached file descriptor parser
    parser = (AnalysisFileDescriptorParser) req.getSession().getAttribute(CacheAnalysisFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
    if (parser == null) {
      log.error("Unable to get file descriptor parser from session");
      return;
    }
    

    try {
      

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=gnomexAnalysis.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        
        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal().getName());

        parser.parse();
        
        ZipOutputStream zout = new ZipOutputStream(response.getOutputStream());
        byte b[] = new byte[102400];

        
        int totalZipSize = 0;
        // For each request
        
        for(Iterator i = parser.getAnalysisNumbers().iterator(); i.hasNext();) {
          String analysisNumber = (String)i.next();
          
          Analysis analysis = null;
          List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
          if (analysisList.size() == 1) {
            analysis = (Analysis)analysisList.get(0);
          }
          
          // If we can't find the analysis in the database, just bypass it.
          if (analysis == null) {
            log.error("Unable to find analysis " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          // Check permissions - bypass this analysis if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(analysis)) {  
            log.error("Insufficient permissions to read request " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          List fileDescriptors = parser.getFileDescriptors(analysisNumber);
          
          // For each file to be downloaded for the analysis
          for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

            AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i1.next();
            
            // Ignore file descriptors that represent directories.  We just
            // will zip up actual files.
            if (fd.getType().equals("dir")) {
              continue;
            }
            
            
            // Since we use the request number to determine if user has permission to read the data, match sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            if (!analysisNumber.equalsIgnoreCase(fd.getNumber())) {
              log.error("Analysis number does not match directory for attempted download on " + fd.getFileName() + " for user " + req.getUserPrincipal().getName() + ".  Bypassing download." );
              continue;
            }

            
            
            FileInputStream in = new FileInputStream(fd.getFileName());

            // Add ZIP entry to output stream.
            // (The file name starts after the year subdirectory)
            zout.putNextEntry(new ZipEntry("bioinformatics-analysis-" + fd.getZipEntryName()));

            // Transfer bytes from the file to the ZIP file
            int numRead = 0;
            int size = 0;

            
            while (numRead != -1) {
              numRead = in.read(b);
              if (numRead != -1) {
                zout.write(b, 0, numRead);
                size += numRead;
              }
            }
            totalZipSize += size;

            zout.closeEntry();
          }     
          
          
        }
        
        
        zout.finish();
        zout.flush();


        secAdvisor.closeReadOnlyHibernateSession();
        

      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "You must have a SecurityAdvisor in order to run this command "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "An error has occured while processing " + "<br>");
      response.getOutputStream().println(
          "Here is the exception: <br>" + e + "<br>");
      e.printStackTrace(new PrintWriter(response.getOutputStream()));
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      e.printStackTrace();
    } finally {
      // clear out session variable
      req.getSession().setAttribute(CacheAnalysisFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
      
    }

  }    
  
  
 
 
}