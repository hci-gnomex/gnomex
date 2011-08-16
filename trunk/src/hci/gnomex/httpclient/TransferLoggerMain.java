/**
 * 
 */
package hci.gnomex.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author Tony Di Sera
 * This is a java main that will be called by a script to 
 * log info about a file upload or download.
 *
 */
public class TransferLoggerMain {
  
  private String      server;
  private String      type;
  private String      method;
  private String      fileName;
  private String      startDateTimeStr;
  private String      endDateTimeStr;
  private BigDecimal  fileSize;

  
  private String userName;
  private String password;
  
  private String propertiesFileName = "/properties/gnomex_httpclient.properties";

  
  

  /**
   * @param args
   */
  public static void main(String[] args) {

    TransferLoggerMain transferLogger = new TransferLoggerMain(args);
    try {
        transferLogger.callServlet();
    } catch (Exception e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
    }
  }
  
  private TransferLoggerMain(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-h")) {
        printUsage();
        return;
      } else if (args[i].equals("-server")) {
        server = args[++i];
      } else if (args[i].equals("-type")) {
        type = args[++i];
      } else if (args[i].equals("-method")) {
        method = args[++i];
      } else if (args[i].equals("-fileName")) {
        fileName = args[++i];
      } else if (args[i].equals("-fileSize")) {
        fileSize = new BigDecimal(args[++i]);
      } else if (args[i].equals("-startDateTime")) {
        startDateTimeStr = args[++i];
      } else if (args[i].equals("-endDateTime")) {
        endDateTimeStr = args[++i];
      }else if (args[i].equals("-properties")) {
        propertiesFileName = args[++i];
      } 
    }
  }
    
  
  private void printUsage() {
    System.out.println("java hci.gnomex.utility.TransferLogger " + "\n" +
        "-server application server name" + "\n" +
        "-method http|ftp" + "\n" +
        "-type upload|download" + "\n" + 
        "-fileName fileName" + "\n" +
        "[-fileSize fileSize]" + "\n" +
        "[-startDateTime yyyy-MM-dd hh:mm:ss.SSS]" + "\n" +
        "[-endDateTime yyyy-MM-dd hh:mm:ss.SSS]" + "\n" +
        "[-properties properties]");
  }
  
  
  private String getCurrentTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    return dateFormat.format(new java.util.Date(System.currentTimeMillis()));
  }
  

  private void callServlet() {
    BufferedReader in = null;
    String inputLine;    
    StringBuffer outputXML = new StringBuffer();
    boolean success = false;

    try {
      loadProperties();

      // Make sure mandatory arguments were passed in
      if (server == null || server.equals("") ||
          fileName == null || fileName.equals("") || 
          type == null || type.equals("") ||
          method == null || method.equals("")) {
        this.printUsage();
        throw new Exception("Please specify all mandatory arguments.  See command line usage.");
      }

      trustCerts(); 

      //
      // Login using forms based authentication
      //
      URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/loginsucceeded.jsp?j_username=" + userName + "&j_password=" + password);
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


      //
      // Make http request to insert transfer log
      //

      // Construct request parameters
      String parms = URLEncoder.encode("fileName", "UTF-8") + "=" + URLEncoder.encode(fileName, "UTF-8");
      parms += "&" + URLEncoder.encode("transferType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
      parms += "&" + URLEncoder.encode("transferMethod", "UTF-8") + "=" + URLEncoder.encode(method.toString(), "UTF-8");
      if (startDateTimeStr != null) {
        parms += "&" + URLEncoder.encode("startDateTime", "UTF-8") + "=" + URLEncoder.encode(startDateTimeStr.toString(), "UTF-8");        
      }
      if (endDateTimeStr != null) {
        parms += "&" + URLEncoder.encode("endDateTime", "UTF-8") + "=" + URLEncoder.encode(endDateTimeStr.toString(), "UTF-8");
      }
      if (fileSize != null) {
        parms += "&" + URLEncoder.encode("fileSize", "UTF-8") + "=" + URLEncoder.encode(fileSize.toString(), "UTF-8");
      }
      

      success = false;
      outputXML = new StringBuffer();
      url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/SaveTransferLog.gx");
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
        System.out.print(inputLine);
        if (inputLine.indexOf("<SUCCESS") >= 0) {
          success = true;
        } 
      }
      System.out.println();
      if (!success) {
        throw new Exception("Unable to insert transfer log");
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
  private void loadProperties() throws FileNotFoundException, IOException {
    File file = new File(propertiesFileName);
    FileInputStream fis = new FileInputStream(file);
    Properties p = new Properties();
    p.load(fis);
    userName = (String)p.get("userName");
    password = (String)p.get("password");
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
