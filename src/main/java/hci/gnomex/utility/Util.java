package hci.gnomex.utility;

import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.io.*;

public class Util {

    // Parses a comma delimited string where commas are ignored if between quotes.
    public static String[] parseCommaDelimited(String s) {
        if (s == null) {
            return new String[0];
        } else {
            String otherThanQuote = " [^\"] ";
            String quotedString = String.format(" \" %s* \" ", otherThanQuote);
            String regex = String.format("(?x) " + // enable comments, ignore white spaces
                            ",                         " + // match a comma
                            "(?=                       " + // start positive look ahead
                            "  (                       " + // start group 1
                            "    %s*                   " + // match 'otherThanQuote' zero or more times
                            "    %s                    " + // match 'quotedString'
                            "  )*                      " + // end group 1 and repeat it zero or more times
                            "  %s*                     " + // match 'otherThanQuote'
                            "  $                       " + // match the end of the string
                            ")                         ", // stop positive look ahead
                    otherThanQuote, quotedString, otherThanQuote);

            String[] tokens = s.split(regex);

            return tokens;
        }
    }

    public static int compareRequestNumbers(String reqNumber1, String reqNumber2) {
        int comp = 0;

        String firstChar1 = getReqFirstChar(reqNumber1);
        String firstChar2 = getReqFirstChar(reqNumber2);

        Integer num1 = getReqNumber(reqNumber1);
        Integer num2 = getReqNumber(reqNumber2);

        if (firstChar1.equals(firstChar2)) {
            comp = num1.compareTo(num2);
        } else {
            comp = firstChar1.compareTo(firstChar2);
        }

        return comp;
    }

    private static String getReqFirstChar(String reqNumber) {
        String c = "0";
        if ("0123456789".indexOf(reqNumber.substring(0, 1)) < 0) {
            c = reqNumber.substring(0, 1);
        }

        return c;
    }

    private static Integer getReqNumber(String reqNumber) {
        String intStr = reqNumber;
        if ("0123456789".indexOf(intStr.substring(0, 1)) < 0) {
            intStr = intStr.substring(1);
        }
        if (intStr.indexOf("R") >= 0) {
            intStr = intStr.substring(0, intStr.indexOf("R"));
        }

        Integer num = Integer.parseInt(intStr);

        return num;
    }

    /*
     * return the key set of the Map as an array of Strings.
     */
    public static String[] keysToArray(Map<String, ?> map) {
        String[] keys = new String[map.size()];

        int index = 0;
        for (String key : map.keySet()) {
            keys[index] = key;
            index++;
        }
        return keys;
    }

    public static String addURLParameter(String url, String parameter) {
        if (parameter.startsWith("&") || parameter.startsWith("?")) {
            parameter = parameter.substring(1);
        }
        if (url.contains("?")) {
            url += "&";
        } else {
            url += "?";
        }
        url += parameter;
        return url;
    }

    /**
     * Converts a list of objects to a comma-delimited string
     *
     * @param list the list of objects to be converted
     * @return a String listing the provided objects
     */
    public static String listToString(List list) {
        return listToString(list, null, null, null, 0);
    }

    /**
     * Converts a list of objects to a comma-delimited string
     *
     * @param list     the list of objects to be converted
     * @param maxItems the maximum number of items to display (additional items are ignored)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, int maxItems) {
        return listToString(list, null, null, null, maxItems);
    }

    /**
     * Converts a list of objects to a delimited string
     *
     * @param list      the list of objects to be converted
     * @param delimiter the String to use as a delimiter instead of the default comma
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String delimiter) {
        return listToString(list, delimiter, null, null, 0);
    }

    /**
     * Converts a list of objects to a delimited string
     *
     * @param list      the list of objects to be converted
     * @param delimiter the String to use as a delimiter instead of the default comma
     * @param maxItems  the maximum number of items to display (additional items are ignored)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String delimiter, int maxItems) {
        return listToString(list, delimiter, null, null, maxItems);
    }

    /**
     * Converts a list of objects to a comma-delimited string
     *
     * @param list    the list of objects to be converted
     * @param prefix  the String to insert before each item (i.e. an opening quote or brace)
     * @param postfix the String to insert after each item (i.e. a closing quote or brace)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String prefix, String postfix) {
        return listToString(list, null, prefix, postfix, 0);
    }

    /**
     * Converts a list of objects to a comma-delimited string
     *
     * @param list     the list of objects to be converted
     * @param prefix   the String to insert before each item (i.e. an opening quote or brace)
     * @param postfix  the String to insert after each item (i.e. a closing quote or brace)
     * @param maxItems the maximum number of items to display (additional items are ignored)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String prefix, String postfix, int maxItems) {
        return listToString(list, null, prefix, postfix, maxItems);
    }

    /**
     * Converts a list of objects to a delimited string
     *
     * @param list      the list of objects to be converted
     * @param delimiter the String to use as a delimiter instead of the default comma
     * @param prefix    the String to insert before each item (i.e. an opening quote or brace)
     * @param postfix   the String to insert after each item (i.e. a closing quote or brace)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String delimiter, String prefix, String postfix) {
        return listToString(list, delimiter, prefix, postfix, 0);
    }

    /**
     * Converts a list of objects to a delimited string
     *
     * @param list      the list of objects to be converted
     * @param delimiter the String to use as a delimiter instead of the default comma
     * @param prefix    the String to insert before each item (i.e. an opening quote or brace)
     * @param postfix   the String to insert after each item (i.e. a closing quote or brace)
     * @param maxItems  the maximum number of items to display (additional items are ignored)
     * @return a String listing the provided objects
     */
    public static String listToString(List list, String delimiter, String prefix, String postfix, int maxItems) {
        if (delimiter == null) {
            delimiter = ",";
        }
        if (prefix == null) {
            prefix = "";
        }
        if (postfix == null) {
            postfix = "";
        }
        int numItems = list.size();
        if (maxItems > 0 && maxItems < list.size()) {
            numItems = maxItems;
        }
        String output = "";
        for (int i = 0; i < numItems; i++) {
            if (!output.isEmpty()) {
                output += delimiter;
            }
            output += prefix + list.get(i) + postfix;
        }
        return output;
    }

    public static void showTime(long start, String info) {
        long endTime = System.currentTimeMillis();
        long numMillis = endTime - start;

        double numsec = numMillis / 1000.0;

        Date d = new Date(System.currentTimeMillis());

        System.out.println(d.toString() + info + numsec + " seconds elapsed time. Start: " + start + " End: " + endTime);

    }

    public static String encodeName(String nameIn) {
        String nameOut = nameIn;
        if (nameOut == null) {
            return nameOut;
        }

        while (nameOut.indexOf('+') >= 0) {
            nameOut = nameOut.replace("+", "%2B");
        }

        return nameOut;

    }


    //get request headers
    public static StringBuffer getRequestHeader(HttpServletRequest request) {
        StringBuffer headerInfo = new StringBuffer();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerInfo.append(key + ": ");
            headerInfo.append(value + "\n");
        }

        return headerInfo;
    }

    // output request header / parameters and postrequestbody
    public static StringBuilder printRequest(HttpServletRequest httpRequest) {
        int MAXSIZE = 250000;
        String theRequest = "";
        StringBuilder request = new StringBuilder(65536);
        String headers = "\n\n *** Headers ***\n";
        request.append(headers);
        System.out.print(headers);
        String warning = "";

        Enumeration headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            warning = "";
            String headerName = (String) headerNames.nextElement();
            String theHeader = httpRequest.getHeader(headerName);
            System.out.println(headerName + " = " + theHeader);
            if (theHeader.length() > MAXSIZE) {
                warning = "\nWARNING: header truncated to " + MAXSIZE + " characters\n";
                theHeader = theHeader.substring(0, MAXSIZE);
            }
            request.append(warning + headerName + " = " + theHeader + "\n");
        }

        String parameters = "\n\n *** Parameters ***\n";
        System.out.print(parameters);
        request.append(parameters);

        Enumeration params = httpRequest.getParameterNames();
        while (params.hasMoreElements()) {
            warning = "";
            String paramName = (String) params.nextElement();
            String theParameter = httpRequest.getParameter(paramName);
            System.out.println(paramName + " = " + theParameter);
            if (theParameter.length() > MAXSIZE) {
                warning = "\nWARNING: parameter truncated to " + MAXSIZE + " characters\n";
                theParameter = theParameter.substring(0, MAXSIZE);
            }
            request.append(warning + paramName + " = " + theParameter + "\n");
        }

        return request;
    }

    public static String extractPostRequestBody(HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            Scanner s = null;
            try {
                s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s.hasNext() ? s.next() : "";
        }
        return "";
    }

    public static String GNLOG(org.apache.log4j.Logger LOG, String whathappend, Exception e) {
        String theInfo = null;

        theInfo = whathappend + "\n\n";

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        theInfo = theInfo + errors.toString() + "\n";

        LOG.error(whathappend, e);

        return theInfo;
    }

    public static boolean isParameterTrue(String requestParameter) {
        return requestParameter.equalsIgnoreCase("Y") || requestParameter.equalsIgnoreCase("true");
    }

    public static boolean isParameterFalse(String requestParameter) {
        return requestParameter.equalsIgnoreCase("N") || requestParameter.equalsIgnoreCase("false");
    }

    public static void sendErrorReport(org.hibernate.Session sess, String softwareTestEmail, String fromAddress, String userName, String errorMessage, StringBuilder requestDump) {
        try {
            String errorMessageString = "User: " + userName + "\n";
            if (errorMessage != null) {
                errorMessageString = errorMessageString + errorMessage + "\n" + requestDump.toString() + "\n";
            } else {
                errorMessageString = errorMessageString + "No traceback available" + "\n" + requestDump.toString() + "\n";
            }

            // if it wasn't really a trace back then just leave
            if (errorMessageString.equals("")) {
                return;
            }

            String toaddress = softwareTestEmail;
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            String serverName = localMachine.getHostName();

            // If the error occurred on a developer workstation, get their email address and use it instead
            String localhostEmail = getLocalhostEmail(sess, serverName);
            if (localhostEmail != null) {
                toaddress = localhostEmail;
            }

            MailUtilHelper helper = new MailUtilHelper(toaddress, null, null, fromAddress,
                    "GNomEx Runtime Error [Server: " + localMachine.getHostName() + "]", errorMessageString, null,
                    false, DictionaryHelper.getInstance(sess), serverName, false, toaddress);
            MailUtil.validateAndSendEmail(helper);

        } catch (Exception e) {
            System.err.println("GNomExFrontController unable to email error report.   " + e.toString());
        }
    }

    private static String getLocalhostEmail(Session sess, String serverName) {
        if (serverName != null && !serverName.equals("")) {

            // This property looks like 'COMPUTER1 email1@server.com,COMPUTER2 email2@server.com'
            String machinelist = PropertyDictionaryHelper.getInstance(sess).getProperty(
                    PropertyDictionaryHelper.PROPERTY_RUNTIME_ERROR_SERVER_LIST);

            if (machinelist != null) {
                for (String machineEntry : machinelist.toLowerCase().split(",")) {
                    String[] items = machineEntry.trim().split(" ", 2);
                    String machine = items[0];
                    if (items.length == 2 && machine.equals(serverName.toLowerCase())) {
                        String email = items[1];
                        return email;
                    }
                }
            }

        }
        return null;
    }

    /* getVCFHeader -- get the line with the sample id's from a vcf.gz file
     */
    public static String getVCFHeader(String filename) {
        String encoding = "US-ASCII";

        try {
            InputStream fileStream = new FileInputStream(filename);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            Reader decoder = new InputStreamReader(gzipStream, encoding);
            BufferedReader br = new BufferedReader(decoder);

            String line = null;

            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                if (line.substring(0, 6).equals("#CHROM")) {
                    // we are done
                    br.close();
                    return line;
                }
            }

        } catch (Exception e) {
            System.out.println("[Util.getVCFHeader] Exception: " + e);
            return "";
        }

        return "";
    }
}
