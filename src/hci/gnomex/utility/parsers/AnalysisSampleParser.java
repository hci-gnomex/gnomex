package hci.gnomex.utility.parsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

import hci.framework.model.DetailObject;

@SuppressWarnings("serial")
public class AnalysisSampleParser extends DetailObject implements Serializable {
	
	protected Document								doc;
	protected List<Integer>							idSampleList = new ArrayList<Integer>();
	protected HashMap<Integer, Integer>     		idRequestMap = new HashMap<Integer, Integer>();
	
	public AnalysisSampleParser(Document doc) {
		this.doc = doc;
	}
	
	@SuppressWarnings("rawtypes")
	public void parse(Session sess) throws Exception {
		Element root = this.doc.getRootElement();
		
		for (Iterator iter = root.getChildren("Sample").iterator(); iter.hasNext();) {
			Element node = (Element) iter.next();
			
			String idSampleString = node.getAttributeValue("idSample");
			Integer idSample = new Integer(idSampleString);
			
			String idRequestString = node.getAttributeValue("idRequest");
			Integer idRequest = new Integer(idRequestString);
			
			idSampleList.add(idSample);
			idRequestMap.put(idSample, idRequest);
		}
	}
	
	public List<Integer> getIdSamples() {
		return idSampleList;
	}
	
	public Integer getIdRequest(Integer idSample) {
		return idRequestMap.get(idSample);
	}

}
