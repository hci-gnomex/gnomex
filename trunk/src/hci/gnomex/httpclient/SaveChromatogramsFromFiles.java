/**
 * 
 */
package hci.gnomex.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author Tony Di Sera
 * This is a java main that will be called by a batch script to look
 * for chromatogram files in the dropoff directory.  Read
 * each files from the directory, calling the http service
 * SaveChromatogramFromFile.
 *
 */
public class SaveChromatogramsFromFiles {
  
  private String userName;
  private String password;
  
  private String propertiesFileName = "/properties/gnomex_httpclient.properties";
  private boolean debug = false;
  private String server;
  private String dropFilePath;
  private String experimentFilePath;

  /**
   * @param args
   */
  public static void main(String[] args) {

    SaveChromatogramsFromFiles createAnalysis = new SaveChromatogramsFromFiles(args);
    createAnalysis.callServlet();
  }
  
  private SaveChromatogramsFromFiles(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-h")) {
        printUsage();
        return;
      } else if (args[i].equals("-debug")) {
        debug = true;
      }  else if (args[i].equals("-properties")) {
        propertiesFileName = args[++i];
      } else if (args[i].equals("-dropFilePath")) {
        dropFilePath = args[++i];
      } else if (args[i].equals("-experimentFilePath")) {
        experimentFilePath = args[++i];
      } else if (args[i].equals("-server")) {
        server = args[++i];
      } 
    }
    
   
  }
    
  private void loadProperties() throws FileNotFoundException, IOException {
    File file = new File(propertiesFileName);
    FileInputStream fis = new FileInputStream(file);
    Properties p = new Properties();
    p.load(fis);
    userName = (String)p.get("userName");
    password = (String)p.get("password");
  }


  
  private void printUsage() {
    System.out.println("java hci.gnomex.utility.SaveChromatogramsFromFiles " + "\n" +
        "[-debug] " + "\n" +
        "-properties <propertiesFileName> " + "\n" +
        "-server <server> " + "\n" +
        "-dropFilePath <dropFilePath>" + "\n" +
        "-experimentFilePath <experimentFilePath>" + "\n" );
  }
  
  private void callServlet() {

    
    BufferedReader in = null;
    String inputLine;    
    StringBuffer outputXML = new StringBuffer();
    boolean success = false;
   

    try {
      loadProperties();
      
      // Make sure mandatory arguments were passed in
      if ( dropFilePath == null || dropFilePath.equals("")) {
        this.printUsage();
        throw new Exception("Please specify all mandatory arguments.  See command line usage.");
      }
      if (server == null || server.equals("")) {
        this.printUsage();
        throw new Exception("Please specify all mandatory arguments.  See command line usage.");
      }
      
      trustCerts(); 
      
      //
      // Login using forms based authentication
      //
      URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/login_verify.jsp?j_username=" + userName + "&j_password=" + password);
      URLConnection conn = url.openConnection();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      success = false;
      while ((inputLine = in.readLine()) != null) {
        System.out.println(inputLine);
        if (inputLine.indexOf("<SUCCESS") >= 0) {
          success = true;
          break;
        }
      }
      if (!success) {
        System.err.print(outputXML.toString());
        throw new Exception("Unable to login");
      }
      // Capture session id from cookie
      List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
      
      //
      // Create a security advisor
      //
      url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/CreateSecurityAdvisor.gx");
      conn = url.openConnection();
      for (String cookie : cookies) {
        conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
      }
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      success = false;
      outputXML = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        outputXML.append(inputLine);
        if (inputLine.indexOf("<SecurityAdvisor") >= 0) {
          success = true;
          break;
        }
      }
      if (!success) {
        System.err.print(outputXML.toString());
        throw new Exception("Unable to create security advisor");
      }
      
      in.close();
      
      //
      // For each file, save chromatogram from file
      //
      File dropFile = new File(dropFilePath);
      if (!dropFile.exists()) {
        throw new Exception("Drop file path " + dropFilePath + " does not exist.");
      }
      if (!dropFile.isDirectory()) {
        throw new Exception("Drop file path " + dropFilePath + " is not a directory.");
      }
      
      TreeSet<File> theFiles = new TreeSet<File>();
      hashFiles(dropFile, theFiles);
     
      for (File f : theFiles) {
        int pos = f.getCanonicalPath().lastIndexOf(f.getName());
        String filePath = f.getCanonicalPath().substring(0, pos - 1);
        System.out.println(f.getName() + " " + filePath);
        
        // Construct request parameters
        String parms = URLEncoder.encode("fileName", "UTF-8") + "=" + URLEncoder.encode(f.getName(), "UTF-8");        
        parms += "&" + URLEncoder.encode("filePath", "UTF-8") + "=" + URLEncoder.encode(filePath, "UTF-8");
        
        success = false;
        outputXML = new StringBuffer();
        url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/SaveChromatogramFromFile.gx");
        conn = url.openConnection();
        conn.setDoOutput(true);
        for (String cookie : cookies) {
          conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
        }
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(parms);
        wr.flush();

        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
          if (inputLine.indexOf("<SUCCESS") >= 0) {
            success = true;
            int start = inputLine.indexOf("destDir=\"");
            if (start == -1) {
              throw new Exception("destDir attribute not returned in XML result " + inputLine);
            }
            start = start + 11;
            int end = inputLine.indexOf("\"", start);
            if (end == -1) {
              throw new Exception("destDir attribute could not be parsed from XML result " + inputLine);
            }
            String destDirName = inputLine.substring(start, end);
            File destDir = new File(destDirName);
            if (!destDir.exists()) {
              destDir.mkdirs();
            }
                          
            moveFile(f, destDirName);
          } 
        }
        System.out.println();
        if (!success) {
          throw new Exception("Unable to save chromatogram file file");
        }

      }
      
      

    } catch (MalformedURLException e) {
      printUsage();
      e.printStackTrace();
      System.err.println(e.toString());
    } catch (IOException e) {
      printUsage();
      e.printStackTrace();
      System.err.println(e.toString());
    } catch (Exception e) {
      printUsage();
      e.printStackTrace();
      System.err.println(e.toString());
    } finally {
      if (in != null) {
        try {
          in.close();          
        } catch (IOException e) {
          
        }
      }
    }
  }
  
  private void hashFiles(File file, TreeSet<File> theFiles) {
    File[] childFiles = file.listFiles();
    for (int x = 0; x < childFiles.length; x++) {
      File childFile = childFiles[x];
      if (childFile.isDirectory()) {
        hashFiles(childFile, theFiles);
      }else {
        if (childFile.getName().toLowerCase().endsWith("ab1") || childFile.getName().toLowerCase().endsWith("abi")) {
          theFiles.add(childFile);
        }
      }
    }
  }

  private void moveFile(File sourceFile, String destDirName) throws Exception {
    String destFileName = destDirName + File.separator + sourceFile.getName();
    
    // Try to move to file.  If we can't move it, copy then delete it.
    String operation = "java file rename";
    boolean success = sourceFile.renameTo(new File(destFileName));
    if (!success) {
      // Java rename didn't work, possibly because we are trying to
      // move file across file systems.  Try "mv" command instead.      
      try {
        operation = "move";
        Process process = Runtime.getRuntime().exec( new String[] { "mv", sourceFile.getCanonicalPath(), destFileName } );          
        process.waitFor();
        process.destroy();
        
        // Find out if the move worked
        if (!new File(destFileName).exists()) {
          success = false;
        } else {
          success = true;
        }
      } catch (Exception e) {
        success = false;
      }
      
      // Java rename and "mv" commend didn't work.  Now we just
      // throw up out hands and do a copy.
      if (!success) {
        operation = "copy";
        // If move doesn't work then do a copy/delete
        FileChannel in = null;  
        FileChannel out = null; 

        try {  
          in = new FileInputStream(sourceFile).getChannel();  
          File outFile = new File(destFileName);  
          out = new FileOutputStream(outFile).getChannel(); 
          in.transferTo(0, in.size(), out);
          in.close();
          in = null;
          out.close();
          out = null; 
          sourceFile.delete();
          success  = true;
          operation = "move (file system)";
        } catch (Exception e) {
          success = false;  
        } finally {
          if (in != null) {
            try {
              in.close();                
            } catch (Exception e){
            }
          }
          if (out != null) {  
            try {
              out.close();                
            } catch (Exception e) {
            }
          }

        }   
        
      }
    }

    if (success) {
      System.out.println(getTimeStamp() + operation + " " + sourceFile.getName() +  " to " + destDirName); 
    } else {
      throw new Exception("ERROR - " + operation + " " + sourceFile.getName() + " to " + destDirName + " failed.");
    }
  }

  private static String getTimeStamp() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(new Date()) + " "; 
  }
 
    private void trustCerts() {
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
          return null;  
        }  

        public void checkClientTrusted(  
            java.security.cert.X509Certificate[] certs, String authType) {  
        }  

        public void checkServerTrusted(  
          java.security.cert.X509Certificate[] certs, String authType) {  
          if (debug) {
            System.out.println("authType is " + authType);  
            System.out.println("cert issuers");              
          }
          for (int i = 0; i < certs.length; i++) {
            if (debug) {
              System.out.println("\t" + certs[i].getIssuerX500Principal().getName());  
              System.out.println("\t" + certs[i].getIssuerDN().getName());                
            }
          }  
        }  
      } };  

      try {  
        SSLContext sc = SSLContext.getInstance("SSL");  
        sc.init(null, trustAllCerts, new java.security.SecureRandom());  
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
      } catch (Exception e) {  
        e.printStackTrace();  
        System.exit(1);  
      }  

    }
    


  private static class MyAuthenticator extends Authenticator {
    private String userName;
    private String password;
    
    private MyAuthenticator(String userName, String password) {
      this.userName = userName;
      this.password = password;
    }

    // This method is called when a password-protected URL is accessed
    protected PasswordAuthentication getPasswordAuthentication() {
        // Get information about the request
        String promptString = getRequestingPrompt();
        String hostname = getRequestingHost();
        InetAddress ipaddr = getRequestingSite();
        int port = getRequestingPort();

        // Return the information
        return new PasswordAuthentication(userName, password.toCharArray());
    }
}

}
