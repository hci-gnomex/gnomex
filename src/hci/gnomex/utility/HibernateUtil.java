package hci.gnomex.utility;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionImpl;
import org.hibernate.service.ServiceRegistry;



public class HibernateUtil { 

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	sessionFactory = HibernateUtil.configureSessionFactory("hibernate.tomcat.cfg.xml");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.out.println("\nHibernateUtil SessionFactory create  failed. " + ex + "\n");
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Connection getConnection(Session sess) {
    	
    	SessionImpl sessionImpl = (SessionImpl) sess;
    	Connection conn = sessionImpl.connection();
    	return conn;
    }
        

    public synchronized  static SessionFactory configureSessionFactory(String configpath) {
    	SessionFactory sf = null;

        	System.out.println ("[configureSessionFactory] -->Creating Session Factory for " + configpath);
        	
        	final ServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure(configpath) // configures settings from hibernate.cfg.xml
            .build();
        	
    try {
          sf = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
          
    }
    catch (Exception e) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
    	System.out.println ("\n[configureSessionFactory] ERROR: " + e + "\n");
        StandardServiceRegistryBuilder.destroy( registry );
    }

    return sf;
    
    }

}
