// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.gnomex;

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
            sessionFactory = new Configuration().configure("hibernate.gnomex.cfg.xml").buildSessionFactory();
            System.out.println("Hibernate GNomEx session factory created.");
        }
        catch (Throwable ex) {
            System.out.println("HibernateUtil GNomEx SessionFactory created failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
