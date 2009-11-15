package hci.gnomex.security;

import hci.gnomex.constants.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.orionsupport.security.SimpleUserManager;


public class SecurityManagerGNomEx extends SimpleUserManager  {
  private String dbUsername = null;
  private String dbDriver = null;
  private String dbPassword = null;
  private String dbURL = null;
  

  public SecurityManagerGNomEx() {
    super();
  }
  


  protected boolean checkPassword(String uNID, String password) {
    boolean result = false;


    if (uNID != null && password != null && !password.equals("")) {
      result = this.checkCredentials(uNID, password);
    }
    return result;
  }
  

  
  private boolean isAuthenticatedGNomExUser(String uid, String password) {
    
    boolean result = false;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = this.getConnection();

      stmt = con.prepareStatement("SELECT isActive, userNameExternal, passwordExternal FROM AppUser WHERE userNameExternal = ?");

      stmt.setString(1, uid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String isActive = rs.getString("isActive");
        String gnomexPasswordEncrypted = rs.getString("passwordExternal");
        
        if (isActive != null && isActive.equalsIgnoreCase("Y")) {
          String thePasswordEncrypted = EncrypterService.getInstance().encrypt(password);
          if (thePasswordEncrypted.equals(gnomexPasswordEncrypted)) {
            result = true;
          }
        }
      }

    } catch (ClassNotFoundException cnfe) {
      System.err.println("FATAL: The JDBC driver was not found on the classpath \n" + cnfe.getMessage());
      return false;
    } catch (SQLException ex) {
      System.err.println("FATAL: Unable to initialize hci.gnomex.security.SecurityManagerLocal");
      System.err.println(ex.toString());
      return false;
    } finally {
      this.closeConnection(con);
    }

    return result;
  }

  private boolean checkCredentials(String uid, String password) {

    try {
      loadProperties();      
    } catch (Exception e) {
      System.err.println("hci.gnomex.security.SecurityManagerGNomEx ERROR - Cannot load connection properties. " + e.toString());
      return false;
    }
    
    if (this.isAuthenticatedGNomExUser(uid, password)) {
      return true;
    } else {
      return false;
    }
  }
  
  
  private void loadProperties()  throws Exception {
      File dataSourcesFile = new File(Constants.DATA_SOURCES);
      if(dataSourcesFile.exists()) {
        SAXBuilder builder = new SAXBuilder();
          org.jdom.Document doc = builder.build(dataSourcesFile);
          this.getGNomExConnectionProperties(doc);
        
      } else {
        throw new Exception("SecurityManagerGNomEx loadProperties failed.  Cannot find data sources file called " + Constants.DATA_SOURCES);
      }
  }


  private void getGNomExConnectionProperties(org.jdom.Document doc) {
    Element root = doc.getRootElement();
    if (root.getChildren("data-source") != null) {
      Iterator i = root.getChildren("data-source").iterator();
      while (i.hasNext()) {
        Element e = (Element) i.next();
        if (e.getAttributeValue("name") != null && e.getAttributeValue("name").equals("GNOMEX_GUEST")) {
          this.dbDriver = e.getAttributeValue("connection-driver");
          this.dbURL = e.getAttributeValue("url");
          this.dbUsername = e.getAttributeValue("username");
          this.dbPassword = e.getAttributeValue("password");
        } 
      }
    }
  }


  /* (non-Javadoc)
   * @see com.orionsupport.security.SimpleUserManager#inGroup(java.lang.String, java.lang.String)
   */
  protected boolean inGroup(String username, String groupname) {
    // TODO Auto-generated method stub
    return true;
  }

  /* (non-Javadoc)
   * @see com.orionsupport.security.SimpleUserManager#userExists(java.lang.String)
   */
  protected boolean userExists(String username) {
    // TODO Auto-generated method stub
    return true;
  }
  
  protected Connection getConnection() throws SQLException, ClassNotFoundException {
    Connection con = null;

    Class.forName(dbDriver);
    con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

    return con;
  }

  protected void closeConnection(Connection con) {
    try {
      if (con != null && !con.isClosed()) {
         con.close();
      }
    } catch (SQLException ex) {
      System.err.println("FATAL: Unable to initialize Security Manager");
    }
  }
  
}