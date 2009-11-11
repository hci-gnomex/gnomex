package hci.gnomex.utility;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Kirt Henrie
 * @version 2.0
 *
 * Modification:
 * 6/10/03    K. Henrie        Change for hibernate 2.0
 */

import java.io.File;
import java.util.*;
import org.hibernate.*;
import org.hibernate.cfg.*;

import javax.naming.*;

public class CachedGuestSessionFactory {

  private HashMap factories;
  private static CachedGuestSessionFactory factory;

  private CachedGuestSessionFactory() {
    this.factories = new HashMap();
  }

  public static CachedGuestSessionFactory getCachedGuestSessionFactory() {
    if (factory == null) {
      factory = new CachedGuestSessionFactory();
    }
    return factory;
  }

  public SessionFactory getFactory (String jndiName) throws HibernateException, NamingException {
    if (this.factories.containsKey(jndiName)) {
      return (SessionFactory) factories.get(jndiName);
    } else {
      Configuration config = new Configuration();
      //config.setProperty("hibernate.connection.provider_class", "com.opensourceconnections.msjdbcproxy.HibernateProvider");	    
      config.configure( new File("applications/gnomex/lib/hibernateGuest.cfg.xml") ).buildSessionFactory();
      SessionFactory factory = (SessionFactory) new InitialContext().lookup(jndiName);
      factories.put(jndiName, factory);
      return factory;     
    }
  }

}