package hci.gnomex.utility;

import java.util.HashMap;
import java.util.Map;

public class XMLTools {
	
	private static final HashMap<String, String> escapeCharsXML;
	static {
		// Generates Map with XML special chars and their escaped representations
		escapeCharsXML = new HashMap<String, String>();
		escapeCharsXML.put("&", "&amp;");
		escapeCharsXML.put("<", "&lt;");
		escapeCharsXML.put(">", "&gt;");
		escapeCharsXML.put("\'", "&apos;");
		escapeCharsXML.put("\"", "&quot;");
	}
	
	public static String escapeXMLChars(String s) {
		  // Iterate over Map and replace any instances of XML special chars in String with escaped representations
		  for (Map.Entry<String, String> entry : escapeCharsXML.entrySet()) {
			  s = s.replaceAll(entry.getKey(), entry.getValue());
		  }
		  
		  return s;
	  }

}
