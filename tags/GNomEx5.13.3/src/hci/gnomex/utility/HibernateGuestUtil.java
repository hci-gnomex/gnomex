package hci.gnomex.utility;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class HibernateGuestUtil { 

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure("hibernateGuest.tomcat.cfg.xml").buildSessionFactory();
            
            System.out.println("Hibernate guest session factory created.");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.out.println("HibernateGuestUtil SessionFactory created failed." + ex);
            ex.printStackTrace(System.out);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
