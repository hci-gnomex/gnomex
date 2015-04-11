package hci.gnomex.security;

import hci.gnomex.utility.Util;
import hci.utility.server.LDAPURLEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.orionsupport.security.SimpleUserManager;
import com.sun.tools.javac.code.Attribute.Array;


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
  private String ldap_domain = null;
  private String ldap_user_attributes  = null;
  private HashMap<String, String> ldap_user_attribute_map = new HashMap<String, String>();
  
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
  
  private boolean isInactiveGNomExUniversityUser(String uid) {
    
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
          if (isActive != null && isActive.equalsIgnoreCase("Y")) {
              result = false;
          } else {
            result = true;
          }
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
    EncryptionUtility passwordEncrypter = new EncryptionUtility();

    try {
      con = this.getConnection();

      stmt = con.prepareStatement("SELECT isActive, userNameExternal, passwordExternal, salt FROM AppUser WHERE userNameExternal = ?");

      stmt.setString(1, uid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String isActive = rs.getString("isActive");
        String gnomexPasswordEncrypted = rs.getString("passwordExternal");
        String salt = rs.getString("salt");
        
        if (isActive != null && isActive.equalsIgnoreCase("Y")) {
          String thePasswordEncrypted = passwordEncrypter.createPassword(password, salt);
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
    } else if (this.isInactiveGNomExUniversityUser(uid)) {
      return false;  // if user has account that has been marked inactive do not allow login.
    } else {
      // Otherwise, if this is not a GNomEx user, check the credentials
      // against the Univ of Utah LDAP
      return checkUniversityCredentials(uid, password);
    } 
  }
  
  private boolean checkUniversityCredentials(String uid, String password) {
    boolean isAuthenticated = false;
    if (ldap_sec_principal.contains("<")) {
      ldap_sec_principal = ldap_sec_principal.replace("<uid>", uid);      
    } else if(ldap_sec_principal.contains("[")) {
      // Need brackets if provided in a property because <> messes up parsing of context (xml) file
      ldap_sec_principal = ldap_sec_principal.replace("[uid]", uid);            
    }
    try {
      ActiveDirectory ad = new ActiveDirectory(uid, 
          password, 
          ldap_init_context_factory,
          ldap_provider_url, 
          ldap_sec_protocol, 
          ldap_sec_auth, 
          ldap_sec_principal);
      
      if (ldap_domain != null && ldap_user_attribute_map != null && !ldap_user_attribute_map.isEmpty()) {
        NamingEnumeration<SearchResult> answer = ad.searchUser(uid, ldap_domain, Util.keysToArray(ldap_user_attribute_map));
        isAuthenticated = ad.doesMatchUserAttribute(answer, ldap_user_attribute_map);        
      } else {
        isAuthenticated = true;
      }
      
      
    } catch (Exception e) {
      isAuthenticated = false;
    } 
    return isAuthenticated;
    
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
      if (p.getProperty("ldap_domain") != null) {
        ldap_domain = p.getProperty("ldap_domain");
      }
      if (p.getProperty("ldap_user_attributes") != null) {
        ldap_user_attributes = p.getProperty("ldap_user_attributes");
      }
      // Populate user attributes and values into a may (key=attribute, value=attribute value)
      if (ldap_user_attributes != null) {
        String attributeTokens[] = ldap_user_attributes.split(",");
        for (int x = 0; x < attributeTokens.length; x++) {
          String tokens[] = attributeTokens[x].split(":");
          String attribute = tokens.length > 0 ? tokens[0] : null;
          String value = tokens.length > 1 ? tokens[1] : null;
          if (attribute != null && value != null) {
            ldap_user_attribute_map.put(attribute, value);            
          } else {
            System.err.println("Unexpected token for ldap_user_attributes: " + " attribute entry=" + attributeTokens[x] + " tokens=" + tokens);
          }
        }
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