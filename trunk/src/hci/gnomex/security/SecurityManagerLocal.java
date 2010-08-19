package hci.gnomex.security;

import hci.utility.server.HCISecurityManager;
import hci.utility.server.LDAPURLEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.orionsupport.security.SimpleUserManager;


public class SecurityManagerLocal extends SimpleUserManager  {
  private String dbUsername = null;
  private String dbDriver = null;
  private String dbPassword = null;
  private String dbURL = null;
  
  
  private String local_ldap_init_context_factory = null;
  private String local_ldap_provider_url = null;
  private String local_ldap_sec_protocol = null;
  private String local_ldap_sec_auth = null;
  
  private String local_ldap_uri;
  private String local_ldap_provider;
  private String local_ldap_base;
  private String local_ldap_root_user_name;
  private String local_ldap_root_password;
  private String local_ldap_auth_method;

  private Hashtable env1 = new Hashtable();
  private DirContext ctx;
  private Attributes attr;
  
  public SecurityManagerLocal() {
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
  
  private boolean checkUniversityCredentials(String username, String password) {
    // first, get cn from AD from submitted username
    String ldapDNString = "cn=" + local_ldap_root_user_name + "," + local_ldap_base;
    env1.put(Context.PROVIDER_URL, local_ldap_uri);
    env1.put(Context.SECURITY_PRINCIPAL, ldapDNString);
    env1.put(Context.SECURITY_CREDENTIALS, local_ldap_root_password);

    // Create the initial directory context
    try {
      ctx = new InitialDirContext(env1);
      attr = new BasicAttributes(true);
    } catch (NamingException e) {
      System.err.println("Problem getting attribute: " + e);
      return false;
    }

    String cn = null;

    try {

      attr.put(new BasicAttribute("sAMAccountName", username));

      // search for username
      NamingEnumeration ne = ctx.search("", attr);

      while (ne.hasMoreElements()) {
        SearchResult sr = (SearchResult) ne.next();

        // get common name for authentication attempt
        cn = sr.getName();
      }

      // did we find our user name ? if not, return null
      if (cn == null) {
        return false;
      }
      
      

      
      
      
      
    } catch (NamingException e) {
      System.err.println("Problem getting attribute: " + e);
    }

    // now we have our common name, attempt to bind to AD with password
    String dn = cn + "," + local_ldap_base;

    env1.put(Context.SECURITY_PRINCIPAL, dn);
    env1.put(Context.SECURITY_CREDENTIALS, password);

    try {
      // Bind to the LDAP directory
      ctx = new InitialDirContext(env1);
       

      // if we are here, it worked !
      return true;
    } catch (AuthenticationException ae) {
      // wrong password
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  
  
  private void loadProperties()  throws Exception {
    try {
      File file = new File("/properties/gnomex_ldap_connection.properties");
      FileInputStream fis = new FileInputStream(file);
      Properties p = new Properties();
      p.load(fis);


      
      if (p.getProperty("local_ldap_uri") != null) {
        local_ldap_uri = LDAPURLEncoder.encode(p.getProperty("local_ldap_uri"));
      }
      if (p.getProperty("local_ldap_base_dn") != null) {
        local_ldap_base = p.getProperty("local_ldap_base_dn");
      }
      if (p.getProperty("local_ldap_provider") != null) {
        local_ldap_provider = p.getProperty("local_ldap_provider");
      }
      if (p.getProperty("local_ldap_user") != null) {
        local_ldap_root_user_name = p.getProperty("local_ldap_user");
      }
      if (p.getProperty("local_ldap_password") != null) {
        local_ldap_root_password = p.getProperty("local_ldap_password");
      }
      if (p.getProperty("local_ldap_auth_method") != null) {
        local_ldap_auth_method = p.getProperty("local_ldap_auth_method");
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
    

    // load our LDAP environment hashtable
    env1 = new Hashtable();
    env1.put(Context.INITIAL_CONTEXT_FACTORY, local_ldap_provider);
    env1.put(Context.SECURITY_AUTHENTICATION, local_ldap_auth_method);
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