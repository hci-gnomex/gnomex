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
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author Tony Di Sera
 * This is a java main that will be called by a batch script to autmotically
 * create a GNomEx analysis entry in the db from the bioinformatics
 * automated analysis pipeline.
 *
 */
public class CreateAnalysisMain {
  
  private String userName;
  private String password;
  
  private String propertiesFileName = "/properties/gnomex_httpclient.properties";
  private boolean debug = false;
  private String server;
  private String lab;
  private String organism;
  private String genomeBuild;
  private String analysisType;
  private String  name;
  private String description;
  private String folderName;
  private String folderDescription;
  private List<String> seqLaneNumbers = new ArrayList<String>();
  private String lanesXMLString = null;

  /**
   * @param args
   */
  public static void main(String[] args) {

    CreateAnalysisMain createAnalysis = new CreateAnalysisMain(args);
    createAnalysis.callServlet();
  }
  
  private CreateAnalysisMain(String[] args) {
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
      } else if (args[i].equals("-lab")) {
        lab = args[++i];
      } else if (args[i].equals("-genomeBuild")) {
        genomeBuild = args[++i];
      } else if (args[i].equals("-organism")) {
        organism = args[++i];
      } else if (args[i].equals("-analysisType")) {
        analysisType = args[++i];
      } else if (args[i].equals("-name")) {
        name = args[++i];
      } else if (args[i].equals("-description")) {
        description = args[++i];
      } else if (args[i].equals("-folderName")) {
        folderName = args[++i];
      } else if (args[i].equals("-folderDescription")) {
        folderDescription = args[++i];
      } else if (args[i].equals("-seqLane")) {
        String seqLaneNumber = args[++i];
        seqLaneNumbers.add(seqLaneNumber);
      } 
    }
    
    if (!seqLaneNumbers.isEmpty()) {
        lanesXMLString = "<lanes>";
        for (Iterator iter = seqLaneNumbers.iterator(); iter.hasNext();) {
          String seqLaneNumber = (String)iter.next();
          lanesXMLString += "<SequenceLane number=\"" + seqLaneNumber + "\"/>";                  
        }
        lanesXMLString += "</lanes>";
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
    System.out.println("java hci.gnomex.utility.CreateAnalysisMain " + "\n" +
        "[-debug] " + "\n" +
        "-properties <propertiesFileName> " + "\n" +
        "-server <serverName>" + "\n" +
        "-name <analysisName>" + "\n" +
        "-lab <lab name>" + "\n" +
        "-folderName <name of folder>" + "\n" + 
        "-organism <organism           example: Human,E. coli, etc.>" +  "\n" +
        "-genomeBuild <genome build    example: hg18, hg19, TAIR8, etc.>" + "\n" +
        "-analysisType <analysis type  example: Alignment,SNP/INDEL,ChIP-Seq analysis,etc..>" +  "\n" +
        "[-description <analysisDescription>]" + "\n" +
        "[-folderDescription <description of folder>]" + "\n" +
        "[-seqLane <sequence lane number example: 8432F1_1> [...]]");
  }
  
  private void callServlet() {

    
    BufferedReader in = null;
    String inputLine;    
    StringBuffer outputXML = new StringBuffer();
    boolean success = false;
   

    try {
      loadProperties();
      
      // Make sure mandatory arguments were passed in
      if (lab == null || name == null || name.equals("") || organism == null || genomeBuild == null || analysisType == null || folderName == null || folderName.equals("")) {
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
      

      
      //
      // Create analysis
      //
      
      // Construct request parameters
      String parms = URLEncoder.encode("labName", "UTF-8") + "=" + URLEncoder.encode(lab, "UTF-8");
      parms += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
      parms += "&" + URLEncoder.encode("newAnalysisGroupName", "UTF-8") + "=" + URLEncoder.encode(folderName, "UTF-8");
      parms += "&" + URLEncoder.encode("organism", "UTF-8") + "=" + URLEncoder.encode(organism, "UTF-8");
      parms += "&" + URLEncoder.encode("genomeBuild", "UTF-8") + "=" + URLEncoder.encode(genomeBuild, "UTF-8");
      parms += "&" + URLEncoder.encode("analysisType", "UTF-8") + "=" + URLEncoder.encode(analysisType, "UTF-8");
      parms += "&" + URLEncoder.encode("isBatchMode", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8");
      if (description != null) {
        parms += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");        
      }
      if (folderDescription != null) {
        parms += "&" + URLEncoder.encode("newAnalysisGroupDescription", "UTF-8") + "=" + URLEncoder.encode(folderDescription, "UTF-8");        
      }
      if (lanesXMLString != null) {
        parms += "&" + "lanesXMLString" + "=" + lanesXMLString;        
      } 

      success = false;
      outputXML = new StringBuffer();
      url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/SaveAnalysis.gx");
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
        throw new Exception("Unable to create analysis");
      }

    } catch (MalformedURLException e) {
      e.printStackTrace();
      System.err.println(e.toString());
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(e.toString());
    } catch (Exception e) {
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
