package hci.gnomex.controller;


public class CreateRequestServlet extends HttpClientServletBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateRequestServlet.class);
  
  protected String getNameOfServlet() {
    return "CreateRequestServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveRequest();
  }

  protected void logError(String msg, Exception ex) {
    log.error(msg, ex);
  }
}
