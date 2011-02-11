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
  private Integer idLab;
  private Integer idOrganism;
  private Integer idGenomeBuild;
  private Integer idAnalysisType;
  private String  name;
  private String description;
  private String analysisGroupName;
  private String analysisGroupDescription;
  private Integer idRequest;
  private List<String> idSequenceLanes = new ArrayList<String>();
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
      } else if (args[i].equals("-idLab")) {
        idLab = Integer.parseInt(args[++i]);
      } else if (args[i].equals("-idGenomeBuild")) {
        idGenomeBuild = Integer.parseInt(args[++i]);
      } else if (args[i].equals("-idOrganism")) {
        idOrganism = Integer.parseInt(args[++i]);
      } else if (args[i].equals("-idAnalysisType")) {
        idAnalysisType = Integer.parseInt(args[++i]);
      } else if (args[i].equals("-name")) {
        name = args[++i];
      } else if (args[i].equals("-description")) {
        description = args[++i];
      } else if (args[i].equals("-analysisGroupName")) {
        analysisGroupName = args[++i];
      } else if (args[i].equals("-analysisGroupDescription")) {
        analysisGroupDescription = args[++i];
      } else if (args[i].equals("-idSequenceLane")) {
        String idSequenceLane = args[++i];
        idSequenceLanes.add(idSequenceLane);
      } 
    }
    
    if (!idSequenceLanes.isEmpty()) {
        lanesXMLString = "<lanes>";
        for (Iterator iter = idSequenceLanes.iterator(); iter.hasNext();) {
          String idSequenceLane = (String)iter.next();
          lanesXMLString += "<SequenceLane idSequenceLane=\"" + idSequenceLane + "\"/>";                  
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
        "[-description <analysisDescription>]" + "\n" +
        "-idLab <idLab>" + "\n" +
        "-analysisGroupName <analysisGroupName>" + "\n" + 
        "[-analysisGroupDescription <analysisGroupDescription>]" + "\n" +
        "[-idAnalysisType <idAnalysisType>]" +  "\n" +
        "[-idOrganism <idOrganism>" +  "\n" +
        "[-idGenomeBuild <idGenomeBuild>]" + "\n" +
        "[-idSequenceLane <idSequenceLane> [...]]");
  }
  
  private void callServlet() {

    
    BufferedReader in = null;
    String inputLine;    
    StringBuffer outputXML = new StringBuffer();
    boolean success = false;
   

    try {
      loadProperties();
      trustCerts(); 
      
      // Install the custom authenticator
      Authenticator.setDefault(new MyAuthenticator(userName, password));
      
      //
      // Create a security advisor
      //
      URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/CreateSecurityAdvisor.gx");
      URLConnection conn = url.openConnection();
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
      
      // Capture session id from cookie
      List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
      in.close();
      
      //
      // Create analysis
      //
      
      // Construct request parameters
      String parms = URLEncoder.encode("idLab", "UTF-8") + "=" + URLEncoder.encode(idLab.toString(), "UTF-8");
      parms += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
      parms += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
      parms += "&" + URLEncoder.encode("newAnalysisGroupName", "UTF-8") + "=" + URLEncoder.encode(analysisGroupName, "UTF-8");
      parms += "&" + URLEncoder.encode("newAnalysisGroupDescription", "UTF-8") + "=" + URLEncoder.encode(analysisGroupDescription, "UTF-8");
      parms += "&" + URLEncoder.encode("idOrganism", "UTF-8") + "=" + URLEncoder.encode(idOrganism.toString(), "UTF-8");
      parms += "&" + URLEncoder.encode("idGenomeBuild", "UTF-8") + "=" + URLEncoder.encode(idGenomeBuild.toString(), "UTF-8");
      parms += "&" + URLEncoder.encode("idAnalysisType", "UTF-8") + "=" + URLEncoder.encode(idAnalysisType.toString(), "UTF-8");
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
      if (!success) {
        throw new Exception("Unable to create analysis");
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
