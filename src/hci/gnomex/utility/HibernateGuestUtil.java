package hci.gnomex.utility;


import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;


public class HibernateGuestUtil { 

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = configureSessionFactory("hibernateGuest.tomcat.cfg.xml");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.out.println("\nHibernateGuestUtil SessionFactory create failed. " + ex + "\n");
            ex.printStackTrace(System.out);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public synchronized  static SessionFactory configureSessionFactory(String configpath) {
    	SessionFactory sf = null;

        	final ServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure(configpath) // configures settings from hibernate.cfg.xml
            .build();
        	
    try {
          sf = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
          
    }
    catch (Exception e) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
    	System.out.println ("\n[configureSessionFactory guest] ERROR: " + e + "\n");
        StandardServiceRegistryBuilder.destroy( registry );
    }
    return sf;
    }    

}
