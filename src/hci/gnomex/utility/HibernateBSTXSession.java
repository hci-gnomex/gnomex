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


import hci.gnomex.controller.GNomExFrontController;

import javax.naming.*;

import java.sql.*;

public class HibernateBSTXSession {

  public static final ThreadLocal bstxSession = new ThreadLocal();

  public static final String SESSION_BSTX_FACTORY_JNDI_NAME = "sessions/BSTX_FACTORY";
    
  public static Session currentBSTXSession(String username) throws NamingException, HibernateException, SQLException {
    Session s = (Session) bstxSession.get();
    if (s == null) {
      
      if (GNomExFrontController.isTomcat()) {
        s = HibernateBSTXUtil.getSessionFactory().openSession();
      } else {
        SessionFactory sf = CachedBSTXSessionFactory.getCachedBSTXSessionFactory().getFactory(SESSION_BSTX_FACTORY_JNDI_NAME);
        s = sf.openSession();
      }
      bstxSession.set(s);
    }

    HibernateSession.setAppName(s, username);
    

    return s;
  }

  public static Session currentBSTXSession() throws NamingException, HibernateException, SQLException {
    Session s = (Session) bstxSession.get();
    if (s == null) {
      throw new HibernateException("This method can only be invoked if a session already exists in the thread of execution");
    }
    return s;
  }


  public static void closeBSTXSession() throws HibernateException, SQLException {
    Session s = (Session) bstxSession.get();

    CallableStatement stmt;
    try {
      HibernateSession.setAppName(s, null);

    }
    finally {
      bstxSession.set(null);
      if (s!=null) s.close();
    }
  }
  
  public static boolean hasCurrentBSTXSession () {
    return (bstxSession.get() != null);
  }
}