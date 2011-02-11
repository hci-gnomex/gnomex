/**
 * 
 */
package hci.gnomex.utility;

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

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

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
    File file = new File("/properties/gnomex_batch.properties");
    FileInputStream fis = new FileInputStream(file);
    Properties p = new Properties();
    p.load(fis);
    userName = (String)p.get("userName");
    password = (String)p.get("password");
  }


  
  private void printUsage() {
    System.out.println("java hci.gnomex.utility.CreateAnalysisMain " + "\n" +
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
    
   

    try {
      loadProperties();
      
      // Install the custom authenticator
      Authenticator.setDefault(new MyAuthenticator(userName, password));
      
      //
      // Create a security advisor
      //
      URL url = new URL("http://localhost/gnomex/CreateSecurityAdvisor.gx");
      URLConnection conn = url.openConnection();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      Document doc  = new SAXBuilder().build(in);
      if (!doc.getRootElement().getName().equals("SecurityAdvisor")) {
        new XMLOutputter().output(doc.getRootElement(), System.out);
        throw new Exception("Unable to create security advisor");
      }
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

      url = new URL("http://localhost/gnomex/SaveAnalysis.gx");
      conn = url.openConnection();
      conn.setDoOutput(true);
      for (String cookie : cookies) {
        conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
      }
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(parms);
      wr.flush();

      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      doc  = new SAXBuilder().build(in);
      if (!doc.getRootElement().getName().equals("SUCCESS")) {
        new XMLOutputter().output(doc.getRootElement(), System.out);
        throw new Exception("Unable to create analysis");        
      } else {
        new XMLOutputter().output(doc.getRootElement(), System.out);
      }

    } catch (JDOMException e) {
      printUsage();
      e.printStackTrace();
      System.err.println(e.toString());
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
