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



import javax.naming.*;
import javax.servlet.ServletContext;

import java.sql.*;

public class HibernateSession {

  public static final ThreadLocal session      = new ThreadLocal();
  
  public static final String SESSION_FACTORY_JNDI_NAME       = "sessions/GNOMEX_FACTORY";
  


  public static Session currentSession(String username) throws NamingException, HibernateException, SQLException {
    Session s = (Session) session.get();
    if (s == null) {
      SessionFactory sf = CachedSessionFactory.getCachedSessionFactory().getFactory(SESSION_FACTORY_JNDI_NAME);     
      s = sf.openSession();
      session.set(s);
    }
    
    
    /*
    CallableStatement stmt;
    Connection con = s.connection();    

    stmt = con.prepareCall("{ call master.dbo.setAppUser(?) }");

    stmt.setString(1, username);

    stmt.executeUpdate();
    */

    return s;
  }

  public static Session currentSession() throws NamingException, HibernateException, SQLException {
    Session s = (Session) session.get();
    if (s == null) {
      throw new HibernateException("This method can only be invoked if a session already exists in the thread of execution");
    }
    return s;
  }


  public static void closeSession() throws HibernateException, SQLException {
    Session s = (Session) session.get();

    CallableStatement stmt;
    try {
      /*
      Connection con = s.connection();
      
      stmt = con.prepareCall("{ call master.dbo.setAppUser(?) }");
      
      stmt.setString(1, null);
      
      stmt.executeUpdate();
      */
    }
    finally {
      session.set(null);
      if (s!=null) s.close();
    }
  }
  
  public static boolean hasCurrentSession () {
    return (session.get() != null);
  }
  
  
}