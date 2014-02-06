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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
public class TransferLoggerMain extends HttpClientBase {
  
  private String      type;
  private String      method;
  private String      fileName;
  private String      startDateTimeStr;
  private String      endDateTimeStr;
  private BigDecimal  fileSize;

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
      } else if (args[i].equals("-serverURL")) {
        serverURL = args[++i];
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
    
  protected void printUsage() {
    System.out.println("java hci.gnomex.utility.TransferLogger " + "\n" +
        "-server | -serverURL server name or server url (e.g. http://server.somewhere.edu:8008)" + "\n" +
        "-method http|fdt" + "\n" +
        "-type upload|download" + "\n" + 
        "-fileName fileName" + "\n" +
        "[-fileSize fileSize]" + "\n" +
        "[-startDateTime yyyy-MM-dd hh:mm:ss.SSS]" + "\n" +
        "[-endDateTime yyyy-MM-dd hh:mm:ss.SSS]" + "\n" +
        "[-properties properties]");
   }
 
   protected boolean checkParms() {
     if (fileName == null || fileName.equals("") || type == null || type.equals("") || method == null || method.equals("")) {
       return false;
     } else {
       return true;
     }
   }
  
   protected String getServletName() {
     return "SaveTransferLogServlet";
   }
   
   protected String getParms() throws UnsupportedEncodingException {
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
     return parms;
   }
}
