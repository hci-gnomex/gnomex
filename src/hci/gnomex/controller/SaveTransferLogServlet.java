package hci.gnomex.controller;
import org.apache.log4j.Logger;
public class SaveTransferLogServlet extends HttpClientServletBase {
  private static Logger LOG = Logger.getLogger(SaveTransferLogServlet.class);
  
  protected String getNameOfServlet() {
    return "SaveTransferLogServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveTransferLog();
  }

  protected void logError(String msg, Exception ex) {
    LOG.error(msg, ex);
  }
}
