// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class HibernateUtil
{
    private static final SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        return HibernateUtil.sessionFactory;
    }
    
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("Hibernate session factory created.");
        }
        catch (Throwable ex) {
            System.out.println("HibernateUtil SessionFactory created failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
