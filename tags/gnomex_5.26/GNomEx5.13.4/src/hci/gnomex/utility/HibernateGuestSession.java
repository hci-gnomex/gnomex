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

public class HibernateGuestSession {

  public static final ThreadLocal guestSession = new ThreadLocal();

  public static final String GUEST_SESSION_FACTORY_JNDI_NAME = "sessions/GNOMEX_GUEST_FACTORY";


    
  public static Session currentGuestSession(String username) throws NamingException, HibernateException, SQLException {
    Session s = (Session) guestSession.get();
    if (s == null) {
      
      if (GNomExFrontController.isTomcat()) {
        s = HibernateGuestUtil.getSessionFactory().openSession();
      } else {
        SessionFactory sf = CachedGuestSessionFactory.getCachedGuestSessionFactory().getFactory(GUEST_SESSION_FACTORY_JNDI_NAME);
        s = sf.openSession();
      }
      guestSession.set(s);
    }

    HibernateSession.setAppName(s, username);
    

    return s;
  }

  public static Session currentGuestSession() throws NamingException, HibernateException, SQLException {
    Session s = (Session) guestSession.get();
    if (s == null) {
      throw new HibernateException("This method can only be invoked if a session already exists in the thread of execution");
    }
    return s;
  }


  public static void closeGuestSession() throws HibernateException, SQLException {
    Session s = (Session) guestSession.get();

    CallableStatement stmt;
    try {
      HibernateSession.setAppName(s, null);

    }
    finally {
      guestSession.set(null);
      if (s!=null) s.close();
    }
  }
  
  public static boolean hasCurrentGuestSession () {
    return (guestSession.get() != null);
  }
}