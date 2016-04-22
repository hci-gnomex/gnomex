// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.gnomex;

import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateAppListener implements ServletContextListener
{
    public void contextInitialized(final ServletContextEvent ce) {
        if (ce.getServletContext().getInitParameter("genometry_mode") != null && ce.getServletContext().getInitParameter("genometry_mode").equalsIgnoreCase("gnomex")) {
            try {
                Class.forName("com.affymetrix.genometry.gnomex.HibernateUtil").newInstance();
                Logger.getLogger(this.getClass().getName()).info("GNomEx Hibernate session factory created.");
            }
            catch (Exception e) {
                Logger.getLogger(this.getClass().getName()).severe("FAILED GNomEx HibernateAppListener.contextInitialize()");
            }
        }
        else if (ce.getServletContext().getInitParameter("genometry_mode").equalsIgnoreCase("classic")) {
            Logger.getLogger(this.getClass().getName()).info("Hibernate GNomEx session factory NOT created because data tracks will be loaded directly from file system.");
        }
        else if (ce.getServletContext().getInitParameter("genometry_mode").equalsIgnoreCase("genopub")) {
            Logger.getLogger(this.getClass().getName()).info("Hibernate GNomEx session factory NOT created because data tracks will be loaded from GenoPub database");
        }
    }
    
    public void contextDestroyed(final ServletContextEvent ce) {
    }
}
