package hci.gnomex.utility;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: HCI - Informatics</p>
 * @author Kirt Henrie
 * @version 2.0
 *
 * Modification:
 * 6/10/03    K. Henrie        Change for hibernate 2.0
 */

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;



import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;

import javax.naming.*;
import javax.servlet.ServletContext;

import java.sql.*;

public class HibernateSession {

  private static final ThreadLocal session      = new ThreadLocal();
  private static final ThreadLocal transaction = new ThreadLocal();
  
  public static final String SESSION_FACTORY_JNDI_NAME       = "sessions/GNOMEX_FACTORY";
  


  public static Session currentSession(String username) throws NamingException, HibernateException, SQLException {
    Session s = (Session) session.get();
    if (s == null) {
      
      if (GNomExFrontController.isTomcat()) {
        s = HibernateUtil.getSessionFactory().openSession();
        if (GNomExFrontController.isTomcat()) {
          // User hibernate transactions (not EJB) if tomcat.
          Transaction tx = s.beginTransaction();
          transaction.set(tx);
        }
      } else {
        SessionFactory sf = CachedSessionFactory.getCachedSessionFactory().getFactory(SESSION_FACTORY_JNDI_NAME);     
        s = sf.openSession();
      }
      session.set(s);
    }
    
    
    setAppName(s, username);

    return s;
  }
  


  public static Session currentSession() throws NamingException, HibernateException, SQLException {
    Session s = (Session) session.get();
    if (s == null) {
      throw new HibernateException("This method can only be invoked if a session already exists in the thread of execution");
    }
    return s;
  }

  public static void rollback() {
    // tx will be null unless Tomcat server.
    Transaction tx = (Transaction) transaction.get();
    if (tx != null) {
      transaction.set(null);
      tx.rollback();
    }
  }
  
  public static void closeSession() throws HibernateException, SQLException {
    // tx will be null if not tomcat.
    Session s = (Session) session.get();
    Transaction tx = (Transaction) transaction.get();
    CallableStatement stmt;
    try {
      setAppName(s, null);
    }
    finally {
      session.set(null);
      transaction.set(null);
      if (tx != null) tx.commit();
      if (s!=null) s.close();
    }
  }
  
  public static boolean hasCurrentSession () {
    return (session.get() != null);
  }
  
  public static void setAppName(Session s, String username) throws SQLException {
    Connection con = s.connection();    
    if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
      CallableStatement stmt;
      stmt = con.prepareCall("{ call master.dbo.setAppUser(?) }");
      stmt.setString(1, username);
      stmt.executeUpdate();
    }
    
  }
}