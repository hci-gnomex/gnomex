package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;


public class ServletUtil {

    public static boolean checkSecureRequest(HttpServletRequest req)
            throws ServletException, IOException {

        // restrict commands to local host if request is not secure
        if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
            if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
                    || req.getRemoteAddr().equals("127.0.0.1")
                    || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static boolean checkSecureRequest(HttpServletRequest req, Logger log)
            throws ServletException, IOException {

        // If local, record in log
        if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
                || req.getRemoteAddr().equals("127.0.0.1")
                || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
            log.debug("Requested from local host");
        }

        return checkSecureRequest(req);
    }

    public static void reportServletError(HttpServletResponse response, String responseMessage, Logger log) throws
            IOException {

        reportServletError(response, responseMessage, log, responseMessage);
    }

    public static void reportServletError(HttpServletResponse response, String responseMessage)
            throws IOException {

        response.setContentType("text/html");
        response.getOutputStream().println(
                "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(responseMessage + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");

    }

    public static void reportServletError(HttpServletResponse response, String responseMessage,
                                          Logger log, String logMessage)
            throws IOException {

        log.error(logMessage);
        reportServletError(response, responseMessage);
    }
}
