package hci.gnomex.security.tomcat;

import hci.gnomex.security.EncrypterService;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import javax.sql.DataSource;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;

public class GNomExRealm extends RealmBase {


  private String username;
  private String password;
  
  @Override
  public Principal authenticate(String username, byte[] credentials) {
    return this.authenticate(username, credentials);
  }

  @Override
  public void init() {
    super.init();

  }

  @Override
  public void start() throws LifecycleException {
    super.start();
  }

  @Override
  public void stop() throws LifecycleException {
    super.stop();
  }

  public GNomExRealm() {
    super();
  }

  @Override
  public Principal authenticate(String username, String credentials) {
    this.username = username;
    this.password = credentials;

    if (isAuthenticated()) {
      return getPrincipal(username);
    } else {
      return null;
    }
  }

  @Override
  protected Principal getPrincipal(String username) {

    List<String> roles = new ArrayList<String>();
    roles.add("GNomExUser");
    return new GenericPrincipal(this, username, password, roles);
  }

  @Override
  protected String getPassword(String string) {
    return password;
  }

  @Override
  protected String getName() {
    return username;
  }

  private boolean isAuthenticated() {
    boolean isAuthenticated = false;
    
    if (this.isAuthenticatedGNomExUser()) {
      // If this is a GNomEx external user, check credentials 
      // against the GNomEx encrypted password
      isAuthenticated =  true;
    } 
    return isAuthenticated;
  }



  private boolean isAuthenticatedGNomExUser() {
    
    boolean isAuthenticated = false;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = this.getConnection();
      stmt = con.prepareStatement("SELECT isActive, userNameExternal, passwordExternal FROM AppUser WHERE userNameExternal = ?");
      stmt.setString(1, username);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String isActive = rs.getString("isActive");
        String gnomexPasswordEncrypted = rs.getString("passwordExternal");
        
        if (isActive != null && isActive.equalsIgnoreCase("Y")) {
          String thePasswordEncrypted = EncrypterService.getInstance().encrypt(password);
          if (thePasswordEncrypted.equals(gnomexPasswordEncrypted)) {
            isAuthenticated = true;
          }
        }
      }

    } catch (NamingException ne) {
      System.err.println("FATAL: Naming exception while trying to get connection \n" + ne.getMessage());
      return false;
    } catch (ClassNotFoundException cnfe) {
      System.err.println("FATAL: The JDBC driver was not found on the classpath \n" + cnfe.getMessage());
      return false;
    } catch (SQLException ex) {
      System.err.println("FATAL: Unable to run query on AppUser in hci.gnomex.security.tomcat.GNomExRealm");
      System.err.println(ex.toString());
      return false;
    } finally {
      this.closeConnection(con);
    }

    return isAuthenticated;
  }
  


  protected Connection getConnection() throws SQLException, ClassNotFoundException, NamingException {
    Context initCtx = new InitialContext();
    DataSource ds = (DataSource)initCtx.lookup("java:openejb/Resource/jdbc/gnomexGuest");
    
    return ds.getConnection();
  }

  protected void closeConnection(Connection con) {
    try {
      if (con != null && !con.isClosed()) {
         con.close();
      }
    } catch (SQLException ex) {
      System.err.println("FATAL: Unable to close db connection in hci.gnomex.security.tomcat.GNomExRealm");
    }
  }
}





