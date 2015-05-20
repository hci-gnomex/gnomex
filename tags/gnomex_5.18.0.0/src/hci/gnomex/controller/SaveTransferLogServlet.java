package hci.gnomex.controller;

public class SaveTransferLogServlet extends HttpClientServletBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveTransferLogServlet.class);
  
  protected String getNameOfServlet() {
    return "SaveTransferLogServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveTransferLog();
  }

  protected void logError(String msg, Exception ex) {
    log.error(msg, ex);
  }
}
