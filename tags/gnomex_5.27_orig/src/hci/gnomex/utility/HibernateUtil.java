package hci.gnomex.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class HibernateUtil { 

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure("hibernate.tomcat.cfg.xml").buildSessionFactory();
            System.out.println("Hibernate session factory created.");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.out.println("HibernateUtil SessionFactory created failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
