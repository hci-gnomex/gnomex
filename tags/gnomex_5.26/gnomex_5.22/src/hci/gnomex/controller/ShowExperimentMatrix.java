package hci.gnomex.controller; 

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.AnalysisMatrixLinkInfo;
import hci.gnomex.utility.ExperimentMatrixLinkInfo;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.MatrixLinkInfoBase;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Date;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

/*
 * This class will generate an web page, showing an experiment matrix.
 * The page will be organized into tabs, a tab for each organism.
 * In each tab will be a table show a matrix with each cell having an experiment count (or blank).
 * The columns are the experiment platforms (RequestCategry + Application)
 * The rows are the various annotation values for Stage, Cell Line, Cell Type, Tissue, and Antibody.
 * 
 *  This command takes one argument called scope.  If the scope = PUBLIC (the default), only
 *  counts for public experiments are shown.  If the scope = ALL, all counts on all
 *  experiment (regardless of visibility) are shown.
 *  
 *  NOTE:  This command is meant to be shown from a public non-authenticated website.
 *  
 *  Each cell contains the experiment count and the related analysis count (if any).
 *  Clicking on the link will bring up a window showing the experiment/analysis links in a list.
 *  Clicking on a list from this popup window will launch GNomEx, showing the particular
 *  experiment or analysis of interest.
 *  
 *  We need this kind of organization to create the matrix
 *  
 *  Organism -> Property -> Distinct Annotation Values -> Application -> Experiment
 *  Organism -> Application (to get the distinct platforms of experiments for this organism)
 *  
 *  This will accomplished with the following maps:
 *  
 *  OrgMap:        key = organism name  value = PropertyMap
 *  PropertyMap:   key = property name  value = AnnotMap
 *  AnnotMap:      key = annot value    value = PlatformMap
 *  PlatformMap:   key = code app       value = ExpMap
 *  ExpMap:        key = requestNumber  value = ExperimentMatrixLinkInfo
 *  
 *  OrgColMap      key = organism name  value = PlatformColMap
 *  PlatformColMap key = code app       value = application name
 *   
 *  
 */
public class ShowExperimentMatrix extends ReportCommand implements Serializable {
  
  private static org.apache.log4j.Logger    log = org.apache.log4j.Logger.getLogger(ShowExperimentMatrix.class);
  private static final String               DELIM = "$";

  private String                            scope = "PUBLIC";
  private String                            siteName;
  
  public TreeMap<String, Map>                      orgMap         = new TreeMap<String, Map>();
  public TreeMap<String, Map>                      propertyMap    = new TreeMap<String, Map>();
  public TreeMap<String, Map>                      annotMap       = new TreeMap<String, Map>();
  public TreeMap<String, Map>                      platformMap    = new TreeMap<String, Map>();
  public TreeMap<String, MatrixLinkInfoBase>       expMap         = new TreeMap<String, MatrixLinkInfoBase>();
  
  public TreeMap<String, Map>                      orgColMap      = new TreeMap<String, Map>();
  public TreeMap<String, String>                   platformColMap = new TreeMap<String, String>();
  
 
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("scope") != null && !request.getParameter("scope").equals("")) {
      scope = request.getParameter("scope");
    } 
    

  }

  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {
    
    this.SUCCESS_JSP_HTML = "/report_experiment_matrix.jsp";
    this.ERROR_JSP = "/message.jsp";
    
    Session sess = null;
    try {
         
      sess = HibernateGuestSession.currentGuestSession("guest");
      
      // Get the site name (property driven);
      siteName = (String)sess.createQuery("SELECT p.propertyValue FROM PropertyDictionary p where p.propertyName = '" + PropertyDictionary.SITE_TITLE + "'").uniqueResult();
      if (siteName == null) {
        siteName = "";
      }
      
      // Get the property that contains a comma separated list of all of the annotations you want
      // to see down the y-axis (rows in the matrix).
      String theSampleAnnotations = "";
      List propRows = (List)sess.createQuery("SELECT p.propertyValue FROM PropertyDictionary p where p.propertyName = '" + PropertyDictionary.EXPERIMENT_MATRIX_PROPERTIES + "'").list();
      if (propRows.size() > 0) {
        theSampleAnnotations = (String)propRows.get(0);
      }
      
      // This is the query to build the experiment matrix
      StringBuffer queryBuf = new StringBuffer();
      queryBuf.append("select distinct "
          + "o.sortOrder, "
          + "o.organism, "
          + "p.name, "
          + "pe.value, "
          + "po.sortOrder, "
          + "po.option, "
          + "p.codePropertyType, "
          + "r.codeApplication, "
          + "app.application, "
          + "r.idRequest, "
          + "r.number, "
          + "r.name, "
          + "r.codeVisibility, "
          + "lab.lastName, "
          + "lab.firstName, "
          + "a.idAnalysis, "
          + "a.number as analysisNumber, "
          + "a.name as analysisName, "
          + "a.codeVisibility as analysisVisibility, "
          + "aLab.lastName as analysisLabLastName, "
          + "aLab.firstName as analysisLabFirstName "
          );
      queryBuf.append(" from Request r");
      queryBuf.append(" join r.application app");
      queryBuf.append(" join r.lab lab");
      queryBuf.append(" join r.samples s");
      queryBuf.append(" join s.organism o");
      queryBuf.append(" join s.propertyEntries pe");
      queryBuf.append(" join pe.property p");
      queryBuf.append(" left join pe.options po");
      queryBuf.append(" left join r.analysisExperimentItems aei");
      queryBuf.append(" left join aei.analysis a");
      queryBuf.append(" left join a.lab aLab");
      queryBuf.append(" where");
      queryBuf.append(" (  (p.codePropertyType = 'OPTION' and (po.option is not null and  po.option != '' and po.option != 'n/a' and  po.option != 'NA' and  po.option != ' NA'and po.option != 'none'))");
      queryBuf.append("   or"); 
      queryBuf.append("    (p.codePropertyType != 'OPTION' and (pe.value is not null and pe.value != '' and pe.value != 'n/a' and pe.value != 'NA' and pe.value != 'none'))");
      queryBuf.append(" )");
      
      if (!theSampleAnnotations.equals("")) {
        queryBuf.append(" and (");
        String[] tokens = theSampleAnnotations.split(",");
        for (int x = 0; x < tokens.length; x++) {
          if (x > 0) {
            queryBuf.append(" or ");
          }
          queryBuf.append(" p.name ");
          if (tokens[x].contains("%")) {
            queryBuf.append("like ");
          } else {
            queryBuf.append("= ");
          }
          queryBuf.append("'" + tokens[x] + "'");
        }
        queryBuf.append(" )");
      }

      if (scope.equals("PUBLIC")) {
        queryBuf.append(" and (r.codeVisibility = '" + Visibility.VISIBLE_TO_PUBLIC + "')");
      }
      
      queryBuf.append(" order by o.sortOrder, o.organism,  p.name,  po.sortOrder, po.option, pe.value,  r.codeApplication, r.number");
      
      List<Object[]> rows = (List<Object[]>)sess.createQuery(queryBuf.toString()).list();
      for (Object[] row : rows) {
        StringBuffer buf = new StringBuffer();
        
        int idx = 0;
        Integer orgSortOrder    = (Integer)row[idx++];
        String organismName     = (String)row[idx++];
        String propertyName     = (String)row[idx++];
        String value            = (String)row[idx++];
        Integer optionSortOrder = (Integer)row[idx++];
        String option           = (String)row[idx++];
        String codePropertyType = (String)row[idx++];
        String codeApplication  = (String)row[idx++];
        String application      = (String)row[idx++];
        Integer idRequest       = (Integer)row[idx++];
        String requestNumber    = (String)row[idx++];
        String requestName      = (String)row[idx++];
        String codeVisibility   = (String)row[idx++];
        String labLastName      = (String)row[idx++];
        String labFirstName     = (String)row[idx++];
        Integer idAnalysis      = (Integer)row[idx++];
        String analysisNumber   = (String)row[idx++];
        String analysisName     = (String)row[idx++];
        String analysisVisibility = (String)row[idx++];
        String analysisLabLastName = (String)row[idx++];
        String analysisLabFirstName = (String)row[idx++];
        
        String orgKey = orgSortOrder + DELIM + organismName;
        propertyMap = (TreeMap<String, Map>)orgMap.get(orgKey);
        if (propertyMap == null) {
          propertyMap = new TreeMap<String, Map>();
        }
        
        
        String propKey = propertyName;
        annotMap = (TreeMap<String, Map>)propertyMap.get(propKey);
        if (annotMap == null) {
          annotMap = new TreeMap<String, Map>();
        }
        
        // TODO: Sort by sort order on Option?
        String annotKey = value;
        //TODO:   Handle multi option
        if (codePropertyType.equals(PropertyType.OPTION)) {
          annotKey = option;
        }
        platformMap = (TreeMap<String, Map>)annotMap.get(annotKey);
        if (platformMap == null) {
          platformMap = new TreeMap<String, Map>();
        }
        
        String platformKey = codeApplication;
        expMap = (TreeMap<String, MatrixLinkInfoBase>)platformMap.get(platformKey);
        if (expMap == null) {
          expMap = new TreeMap<String, MatrixLinkInfoBase>();
        }
        if (!expMap.containsKey(requestNumber)) {
          ExperimentMatrixLinkInfo linkInfo = new ExperimentMatrixLinkInfo();
          linkInfo.number = requestNumber;
          linkInfo.name = requestName;
          linkInfo.labLastName = labLastName;
          linkInfo.labFirstName = labFirstName;
          linkInfo.codeVisibility = codeVisibility;
          
          expMap.put(requestNumber, linkInfo);
        }
        if (analysisNumber != null && !expMap.containsKey(analysisNumber)) {
          AnalysisMatrixLinkInfo linkInfo = new AnalysisMatrixLinkInfo();
          linkInfo.number = analysisNumber;
          linkInfo.name = analysisName;
          linkInfo.labLastName = analysisLabLastName;
          linkInfo.labFirstName = analysisLabFirstName;
          linkInfo.codeVisibility = analysisVisibility;
          
          expMap.put(analysisNumber, linkInfo);
        }
        platformMap.put(platformKey, expMap);
        annotMap.put(annotKey, platformMap);
        propertyMap.put(propKey, annotMap);
        orgMap.put(orgKey, propertyMap);

        platformColMap = (TreeMap<String, String>)orgColMap.get(orgKey);
        if (platformColMap == null) {
          platformColMap = new TreeMap<String, String>();
        }
        platformColMap.put(codeApplication, application);
        orgColMap.put(orgKey, platformColMap);
      }




      
      if (isValid()) {
        this.setSuccessJsp(this, "html");
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (NamingException e){
      log.error("An exception has occurred in ShowExperimentMatrix ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowExperimentMatrix ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowExperimentMatrix ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateGuestSession.closeGuestSession();
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {

    request.setAttribute("siteName", this.siteName);
    request.setAttribute("orgMap", this.orgMap);
    request.setAttribute("orgColMap", this.orgColMap);
    return request;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setResponseState(javax.servlet.http.HttpServletResponse)
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setSessionState(javax.servlet.http.HttpSession)
   */
  public HttpSession setSessionState(HttpSession session) {
    // TODO Auto-generated method stub
    return session;
  }

  /* (non-Javadoc)
   * @see hci.report.utility.ReportCommand#loadContextPermissions()
   */
  public void loadContextPermissions(){
    
  }
  public void loadContextPermissions(String userName) throws SQLException {
    
  }
}