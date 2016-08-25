package hci.gnomex.controller;
import org.apache.log4j.Logger;
public class SaveChromatogramFromFileServlet extends HttpClientServletBase {
  private static Logger LOG = Logger.getLogger(SaveChromatogramFromFileServlet.class);

  protected String getNameOfServlet() {
    return "SaveChromatogramFromFileServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveChromatogramFromFile();
  }
  
  protected void logError(String msg, Exception ex) {
    LOG.error(msg, ex);
  }
}
