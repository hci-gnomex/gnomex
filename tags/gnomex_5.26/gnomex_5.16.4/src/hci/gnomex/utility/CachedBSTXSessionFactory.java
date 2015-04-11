package hci.gnomex.utility;

import java.io.File;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CachedBSTXSessionFactory 
{
  private HashMap factories;
  private static CachedBSTXSessionFactory factory;

  private CachedBSTXSessionFactory() 
  {
    this.factories = new HashMap();
  }

  public static CachedBSTXSessionFactory getCachedBSTXSessionFactory() 
  {
    if (factory == null) 
      factory = new CachedBSTXSessionFactory();
    return factory;
  }

  public SessionFactory getFactory (String jndiName) throws HibernateException, NamingException 
  {
    synchronized(this.factories) 
    {
      if (this.factories.containsKey(jndiName)) 
        return (SessionFactory) factories.get(jndiName);
      else 
      {
        Configuration config = new Configuration();
        config.setProperty("hibernate.connection.provider_class", "com.opensourceconnections.msjdbcproxy.HibernateProvider");   
        config.configure( new File("applications/gnomex/lib/hibernateBSTX.cfg.xml") ).buildSessionFactory();
        SessionFactory factory = (SessionFactory) new InitialContext().lookup(jndiName);
        factories.put(jndiName, factory);
        return factory;     
      }
    }
  }
}
