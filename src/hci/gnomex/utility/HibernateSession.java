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

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExFrontController;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;


public class HibernateSession {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HibernateSession.class);

  private static final ThreadLocal session      = new ThreadLocal();
  private static final ThreadLocal transaction = new ThreadLocal();

  public static final String SESSION_FACTORY_JNDI_NAME       = "sessions/GNOMEX_FACTORY";



  public static Session currentSession(String username) throws NamingException, HibernateException, SQLException {
    Session s = (Session) session.get();
    if (s == null || !s.isOpen()) {
        s = HibernateUtil.getSessionFactory().openSession();

          // User hibernate transactions (not EJB) if tomcat.
          Transaction tx = s.beginTransaction();
          transaction.set(tx);
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
	  Session s = (Session) session.get();
	  if (s == null) return;
	  
    Transaction tx = s.getTransaction();
    if (tx != null) {
      tx.rollback();
      transaction.set(null);
    }
  }

  public static void closeSession() throws HibernateException, SQLException {
    // Ignore this close if tomcat.  Close (and commit) will happen from GnomExFrontController
    if (!GNomExFrontController.isTomcat()) {
      closeSessionForReal();
    }
  }

  public static void closeTomcatSession() throws HibernateException, SQLException {
    // Only does something if tomcat.  Only called from GNomExFrontController
    if (GNomExFrontController.isTomcat()) {
      closeSessionForReal();
    }
  }

  private static void closeSessionForReal() throws HibernateException, SQLException {
    // tx will be null if not tomcat or if transaction already rolled back.
    Session s = (Session) session.get();
    if (s == null) {
    	return;
    }
    
    Transaction tx = s.getTransaction();
    if (tx == null) {
        session.set(null);
        transaction.set(null); 
        
        try {
            setAppName(s, null);
          }
        catch (Exception e)
        {}
        
        s.close();
        return;
    }
    
    TransactionStatus txStat = tx.getStatus();

    try {
      setAppName(s, null);
    }
    finally {
      if (txStat != null && txStat.isNotOneOf(TransactionStatus.COMMITTED) && txStat.isNotOneOf(TransactionStatus.COMMITTING) && 
    		  txStat.isOneOf(TransactionStatus.ACTIVE) && txStat.isNotOneOf(TransactionStatus.ROLLED_BACK) && txStat.isNotOneOf(TransactionStatus.ROLLING_BACK)) {
        try {
          tx.commit();
          txStat = null;
        }catch(Exception e) {
          log.error("Failed to commit Transaction, going to try and rollback " + e);
        }
        try {
          //Maybe the commit above worked and so the transaction is no longer active therefore don't try the rollback or else we will get an inactive tx exception.
          if(txStat != null && txStat.isOneOf(TransactionStatus.ACTIVE)) {
            tx.rollback();
          }
        } catch(Exception e) {
          log.error("Failed to rollback transaction " + e);
        }
      }
      if (s != null) {
        s.close();
      }
      session.set(null);
      transaction.set(null);
    }
  }

  public static boolean hasCurrentSession () {
    return (session.get() != null);
  }

  public static void setAppName(Session s, String username) throws SQLException {
    if (s != null) {
      Connection con = HibernateUtil.getConnection(s);
      if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
        CallableStatement stmt;
        stmt = con.prepareCall("{ call master.dbo.setAppUser(?) }");
        stmt.setString(1, username);
        stmt.executeUpdate();
      } else {
    	  Statement stmt = con.createStatement();
    	  stmt.executeUpdate("set @username='" + username + "';");
    	  
/*    	  
        CallableStatement stmt;
        stmt = con.prepareCall("call setAppUser(?);");
        stmt.setString(1, username);
        stmt.executeUpdate();
*/
      }
    }    
  }
}