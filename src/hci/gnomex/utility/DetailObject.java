package hci.gnomex.utility;

import java.util.Set;

import org.jdom.Element;

public interface DetailObject {
	
	public static final String DETAIL_FULL 							= "full";
	public static final String DETAIL_LIGHT 						= "light";
	public static final String DETAIL_NON_SENSITIVE 				= "safe";
	public static final String DETAIL_INCLUDE_ASSOCIATED_OBJECTS 	= "include";
	
	public Element toXML(Set<String> detailParameters) throws Exception;

}
