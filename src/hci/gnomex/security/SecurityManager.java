package hci.gnomex.security;

import hci.utility.server.LDAPURLEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.orionsupport.security.SimpleUserManager;


public class SecurityManager extends SimpleUserManager  {
  private String dbUsername = null;
  private String dbDriver = null;
  private String dbPassword = null;
  private String dbURL = null;
  
  private String ldap_init_context_factory = null;
  private String ldap_provider_url = null;
  private String ldap_sec_protocol = null;
  private String ldap_sec_auth = null;
  private String ldap_sec_principal = null;
  
  public SecurityManager() {
    super();
  }
  


  protected boolean checkPassword(String uNID, String password) {
    boolean result = false;

    // we need to make sure this user is in the Associate table and active
    if (uNID != null && password != null && !password.equals("")) {
      result = this.checkCredentials(uNID, password);
    }
    return result;
  }
  
  private boolean isGNomExUniversityUser(String uid) {
    
      boolean result = false;

      Connection con = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;

      try {
        con = this.getConnection();

        stmt = con.prepareStatement("SELECT isActive, uNID FROM AppUser WHERE uNID = ?");

        stmt.setString(1, uid);

        rs = stmt.executeQuery();

        while (rs.next()) {
          String isActive = rs.getString("isActive");
          if (isActive != null && isActive.equalsIgnoreCase("Y"))
              result = true;
        }

      } catch (ClassNotFoundException cnfe) {
        System.err.println("FATAL: The JDBC driver was not found on the classpath \n" + cnfe.getMessage());
        return false;
      } catch (SQLException ex) {
        System.err.println("FATAL: Unable to initialize hci.gnomex.security.SecurityManager");
        ex.printStackTrace(System.err);
        return false;
      } finally {
        this.closeConnection(con);
      }

      return result;
  }
  
  
  private boolean isAuthenticatedGNomExExternalUser(String uid, String password) {
    
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
      System.err.println("FATAL: Unable to initialize hci.gnomex.security.SecurityManager");
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
      System.err.println("hci.gnomex.security.SecurityManager ERROR - Cannot load gnomex_config.properties. " + e.toString());
      return false;
    }
    
    if (this.isGNomExUniversityUser(uid)) {
      // If this is a GNomEx user with a uNID, check the credentials 
      // against the Univ of Utah LDAP
      return checkUniversityCredentials(uid, password);
    } else if (this.isAuthenticatedGNomExExternalUser(uid, password)) {
      // If this is a GNomEx external user, check credentials 
      // against the GNomEx encrypted password
      return true;
    } else {
      // Otherwise, if this is not a GNomEx user, check the credentials
      // against the Univ of Utah LDAP
      return checkUniversityCredentials(uid, password);
    } 
  }
  
  private boolean checkUniversityCredentials(String uid, String password) {
        
    
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,ldap_init_context_factory);
    env.put(Context.PROVIDER_URL, ldap_provider_url);
    env.put(Context.SECURITY_PROTOCOL, ldap_sec_protocol);
    env.put(Context.SECURITY_AUTHENTICATION, ldap_sec_auth);
    // Authenticate with the end user's uid and password (collected from a servlet form)
    ldap_sec_principal = ldap_sec_principal.replace("<uid>", uid);
    env.put(Context.SECURITY_PRINCIPAL, ldap_sec_principal);
    env.put(Context.SECURITY_CREDENTIALS, password);

    try {
      
      DirContext ctx = new InitialDirContext(env);
      
      ctx.close();

      return true;
    } catch (AuthenticationException ae) {
      // Auth failed so return false
      return false;
    } catch (Exception e) {
      System.err.println("hci.gnomex.security.SecurityManager ERROR - Cannot connect to UofU LDAP server " + e.toString());
      return false;
    }
  }
  
  
  private void loadProperties()  throws Exception {
    try {
      File file = new File("/properties/gnomex_ldap_connection.properties");
      FileInputStream fis = new FileInputStream(file);
      Properties p = new Properties();
      p.load(fis);
      
      // look for both with uofu_ and without for backwards compatability 
      if (p.getProperty("ldap_init_context_factory") != null) {
        ldap_init_context_factory = p.getProperty("ldap_init_context_factory");
      } else if (p.getProperty("uofu_ldap_init_context_factory") != null) {
        ldap_init_context_factory = p.getProperty("uofu_ldap_init_context_factory");
      }
      if (p.getProperty("ldap_provider_url") != null) {
        ldap_provider_url = LDAPURLEncoder.encode(p.getProperty("ldap_provider_url"));
      } else if (p.getProperty("uofu_ldap_provider_url") != null) {
        ldap_provider_url = LDAPURLEncoder.encode(p.getProperty("uofu_ldap_provider_url"));
      }
      if (p.getProperty("ldap_sec_protocol") != null) {
        ldap_sec_protocol = p.getProperty("ldap_sec_protocol");
      } else if (p.getProperty("uofu_ldap_sec_protocol") != null) {
        ldap_sec_protocol = p.getProperty("uofu_ldap_sec_protocol");
      }
      if (p.getProperty("ldap_sec_auth") != null) {
        ldap_sec_auth = p.getProperty("ldap_sec_auth");
      } else if (p.getProperty("uofu_ldap_sec_auth") != null) {
        ldap_sec_auth = p.getProperty("uofu_ldap_sec_auth");
      }
      ldap_sec_principal = "uid=<uid>,ou=People,dc=utah,dc=edu"; // default to uofu values for backwards compatability
      if (p.getProperty("ldap_sec_principal") != null) {
        ldap_sec_principal = p.getProperty("ldap_sec_principal");
      } else if (p.getProperty("uofu_ldap_sec_principal") != null) {
        ldap_sec_principal = p.getProperty("uofu_ldap_sec_principal");
      }
      if (p.getProperty("dbUsername") != null) {
        dbUsername = p.getProperty("dbUsername");
      }
      if (p.getProperty("dbPassword") != null) {
        dbPassword = p.getProperty("dbPassword");
      }
      if (p.getProperty("dbDriver") != null) {
        dbDriver = p.getProperty("dbDriver");
      }
      if (p.getProperty("dbURL") != null) {
        dbURL = p.getProperty("dbURL");
      }
      
    } catch (Exception e) {
      e.printStackTrace();      
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