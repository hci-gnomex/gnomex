package hci.gnomex.utility;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

	/*
	 * Indicates if file is a link file on unix.
	 */
	public static boolean isSymlink(File file) {
		try {
			if (file == null) {
				return false;
			}
			File canon;
			if (file.getParent() == null) {
				canon = file;
			} else {
				File canonDir = file.getParentFile().getCanonicalFile();
				canon = new File(canonDir, file.getName());
			}

			return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
		} catch (IOException ex) {
			return false;
		}
	}

	public static boolean renameTo (File sourceFile, File destFile) {
		boolean success = false;
		try {
			Path sourcePath = sourceFile.toPath();
			Path targetPath = destFile.toPath();
			Files.move(sourcePath,targetPath);
			success = true;
		}
		catch (Exception rex) {
			System.out.println ("[Util.renameTo] move error: " + rex.toString());
			success = false;
		}

		return success;
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

	public static String listIntToString(List<Integer> list) {
		if (list == null || list.size() == 0)
			return "";

		boolean firstTime = true;
		String stringList = "";
		for (Integer id : list) {
			if (!firstTime)
				stringList += ",";
			else
				firstTime = false;
			stringList += id;
		}
		return stringList;
	}

	public static String listStrToString(List<String> list) {
		if (list == null || list.size() == 0)
			return "";

		boolean firstTime = true;
		String stringList = "";
		for (String str : list) {
			if (!firstTime)
				stringList += ",";
			else
				firstTime = false;
			stringList += "'" + str + "'";
		}
		return stringList;
	}

	public static void showTime(long start, String info) {
		long endTime = System.currentTimeMillis();
		long numMillis = endTime - start;

		double numsec = numMillis / 1000.0;

		Date d = new Date(System.currentTimeMillis());

		System.out.println(d.toString() + info + numsec + " seconds elapsed time.");

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
			headerInfo.append (key + ": ");
			headerInfo.append (value + "\n");
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
		while(headerNames.hasMoreElements()) {
			warning = "";
			String headerName = (String)headerNames.nextElement();
			String theHeader = httpRequest.getHeader(headerName);
			System.out.println(headerName + " = " + theHeader);
			if (theHeader.length() > MAXSIZE) {
				warning = "\nWARNING: header truncated to " + MAXSIZE + " characters\n";
				theHeader = theHeader.substring(0,MAXSIZE);
			}
			request.append (warning + headerName + " = " + theHeader + "\n");
		}

		String parameters = "\n\n *** Parameters ***\n";
		System.out.print(parameters);
		request.append(parameters);

		Enumeration params = httpRequest.getParameterNames();
		while(params.hasMoreElements()){
			warning = "";
			String paramName = (String)params.nextElement();
			String theParameter = httpRequest.getParameter(paramName);
			System.out.println(paramName + " = " + theParameter);
			if (theParameter.length() > MAXSIZE) {
				warning = "\nWARNING: parameter truncated to " + MAXSIZE + " characters\n";
				theParameter = theParameter.substring(0,MAXSIZE);
			}
			request.append (warning + paramName + " = " + theParameter + "\n");
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

		LOG.error (whathappend, e);

		return theInfo;
	}

	public static String addProblemFile (String status, String filename, int [] numlines) {
		if (numlines[0] == 0) {
			status = "Warning: Unable to move some files:\n";
			numlines[0]++;
		}
		if (numlines[0] <= 5) {
			status += filename + "\n";
			numlines[0]++;
		}
		return status;
	}
}
