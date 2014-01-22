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
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author Tony Di Sera
 * This is a java main that will be called by a batch script to automatically
 * create the FDT staging directory to upload files to an experiment
 * or an analysis in GNomEx.
 *
 */
public class FastDataTransferUploadStart extends HttpClientBase {
  
  private boolean debug = false;
  private String analysisNumber;
  private String requestNumber;

  /**
   * @param args
   */
  public static void main(String[] args) {

    FastDataTransferUploadStart upload = new FastDataTransferUploadStart(args);
    upload.callServlet();
  }
  
  private FastDataTransferUploadStart(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-h")) {
        printUsage();
        return;
      } else if (args[i].equals("-debug")) {
        debug = true;
      }  else if (args[i].equals("-properties")) {
        propertiesFileName = args[++i];
      } else if (args[i].equals("-server")) {
        server = args[++i];
      } else if (args[i].equals("-experimentNumber")) {
        requestNumber = args[++i];
      } else if (args[i].equals("-analysisNumber")) {
        analysisNumber = args[++i];
      }
    }
    
   
  }
  
  @Override
  protected String getServletName() {
    return "FastDataTransferUploadStartServlet";
  }
  
  @Override
  protected boolean checkParms() {
    if ( (requestNumber == null || requestNumber.equals("")) && (analysisNumber == null || analysisNumber.equals(""))) {
      return false;
    } else {
      return true;
    }
  }
  
  @Override
  protected String getParms() throws UnsupportedEncodingException {
    String parms = "";
    if (requestNumber != null) {
      parms = URLEncoder.encode("requestNumber", "UTF-8") + "=" + URLEncoder.encode(requestNumber, "UTF-8");        
    }else if (analysisNumber != null) {
      parms =  URLEncoder.encode("analysisNumber", "UTF-8") + "=" + URLEncoder.encode(analysisNumber, "UTF-8");        
    }
    return parms;
  }

  @Override
  protected Boolean checkSuccess(BufferedReader in) throws Exception {
    Boolean success = false;
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      System.out.print(inputLine);
      if (inputLine.indexOf("<FDTUploadUuid") >= 0) {
        success = true;
      } 
    }
    return success;
  }

  protected void printUsage() {
    System.out.println("java hci.gnomex.utility.FastDataTransferUploadStart " + "\n" +
        "[-debug] " + "\n" +
        "-properties <propertiesFileName> " + "\n" +
        "-server <serverName>" + "\n" +
        "-experimentNumber <experimentNumber>" + "\n" +
        "-analysisNumber <analysisNumber>" + "\n" );
  }

}
