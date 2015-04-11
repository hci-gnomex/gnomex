package hci.gnomex.controller;

public class FastDataTransferUploadStartServlet extends HttpClientServletBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadStartServlet.class);

  @Override
  protected String getNameOfServlet() {
    return "FastDataTransferUploadStartServlet";
  }
  
  @Override
  protected GNomExCommand getCommand() {
    return new FastDataTransferUploadStart();
  }

  @Override
  protected void logError(String msg, Exception ex) {
    log.error(msg, ex);
  }
  
}
