package hci.gnomex.utility;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class HibernateBSTXUtil { 

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure("hibernateBSTX.tomcat.cfg.xml").buildSessionFactory();
            
            System.out.println("Hibernate bstx session factory created.");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.out.println("HibernateBSTXUtil SessionFactory created failed." + ex);
            ex.printStackTrace(System.out);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
