package hci.gnomex.utility;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateAppListener implements ServletContextListener	{

	/* Application Startup Event */
	public void	contextInitialized(ServletContextEvent ce) {
	  

	      try  {
          Class.forName("hci.gnomex.utility.HibernateGuestUtil").newInstance();
          Logger.getLogger(this.getClass().getName()).info("HibernateGuestUtil created.");

          Class.forName("hci.gnomex.utility.HibernateUtil").newInstance();
	        Logger.getLogger(this.getClass().getName()).info("HibernateUtil created.");
	      }
	      catch (Exception e)  {
	        Logger.getLogger(this.getClass().getName()).severe("FAILED HibernateAppListener.contextInitialize()");
	      }
	}

	/* Application Shutdown	Event */
	public void	contextDestroyed(ServletContextEvent ce) {}
}