package hci.gnomex.utility;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateAppListener implements ServletContextListener {

	/* Application Startup Event */
	public void contextInitialized(ServletContextEvent ce) {

		try {
			Class.forName("hci.gnomex.utility.HibernateUtil").newInstance();
			Logger.getLogger(this.getClass().getName()).info("HibernateUtil created.");
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).info("FAILED HibernateAppListener.contextInitialize()");
		}
	}

	/* Application Shutdown Event */
	public void contextDestroyed(ServletContextEvent ce) {
	}
}