package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.Segment;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
public class ImportSegments extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImportSegments.class);
  
  private String             chromosomeInfo;
  private Integer            idGenomeBuild;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("chromosomeInfo") != null && !request.getParameter("chromosomeInfo").equals("")) {
      chromosomeInfo = request.getParameter("chromosomeInfo");
    } 
    if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
      idGenomeBuild = Integer.valueOf(request.getParameter("idGenomeBuild"));
    } else {
      this.addInvalidField("idGenomeBuild", "idGenomeBuild is required");
    }

    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      GenomeBuild genomeBuild = (GenomeBuild)sess.load(GenomeBuild.class, idGenomeBuild);
      
      
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_DICTIONARIES)) {

        String line;
        int count = 1;
        if (chromosomeInfo != null && !chromosomeInfo.equals("")) {

          //work around, need to test on PC with IE and Firefox!
          Pattern pat = Pattern.compile("(\\w+)\\s+(\\d+)");
          Pattern ret = Pattern.compile("\\r");
          chromosomeInfo = ret.matcher(chromosomeInfo).replaceAll(""); //just to be safe
          Matcher mat = pat.matcher(chromosomeInfo);
          while (mat.find()){
            Segment s = new Segment();
            s.setName(mat.group(1));
            s.setLength(new Integer (mat.group(2)));
            s.setSortOrder(Integer.valueOf(count));
            s.setIdGenomeBuild(genomeBuild.getIdGenomeBuild());
            sess.save(s);
            count++;
          }

          //upload the data
          sess.flush();
        }

        this.xmlResult = "<SUCCESS idGenomeBuild=\"" + idGenomeBuild + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save data track folder.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in ImportSegments ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
}