package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.util.IteratorIterable;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;



public class XMLParser {
	
	private Map<String, TreeMap<String, List<PersonEntry>>> avEntriesMap;
	private String fileName;
	private String initXML;
	private String annotationFileName;
	private String importScript;
	private String outFileName;
	private List<List<PersonEntry>> flaggedAvatarEntries;
	private String importMode;
	private List<String> analysisIDList;
	private static final String FOUNDATION_FOLDER="Patients - Foundation - NO PHI";
	private static final String AVATAR_FOLDER="Patients - Avatar - NO PHI";
	
	
	
	
	
	public XMLParser(String[] args) {
		this.flaggedAvatarEntries = new ArrayList< List<PersonEntry>>();
		this.avEntriesMap = new TreeMap<String, TreeMap<String, List<PersonEntry>>>();
		this.analysisIDList =  new ArrayList<String>();
		
		for (int i = 0; i < args.length; i++) {
			args[i] =  args[i].toLowerCase();

			if (args[i].equals("-file")) {
				fileName = args[++i];
			} else if (args[i].equals("-annotationxml")) {
				annotationFileName = args[++i];
			} else if (args[i].equals("-importscript")) {
				importScript = args[++i];
			}else if(args[i].equals("-outfile")){
				this.outFileName = args[++i];
			}
			else if(args[i].equals("-importmode")){
				this.importMode = args[++i];
			}else if(args[i].equals("-initxml")){
				this.initXML = args[++i];
			}
			else if (args[i].equals("-help")) {
				//printUsage();
				System.exit(0);
			}
		}
	}
	
	
	
	public void parseXML() throws Exception {
		//Query q = new Query();
		
		
		

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("current relative path is: " + s);
		File inputFile = new File(this.initXML);
		FileReader reader = null;

		SAXBuilder saxBuilder = new SAXBuilder();
		try {
			
			reader = new FileReader(inputFile);
			readFile(this.fileName);
			findFlaggedAvatarEntries();
			Document doc = saxBuilder.build(reader);
			Element rootElement = doc.getRootElement();
			
			
			
			String query = "//samples/Sample";//[@name='TRF89342']";
			String ReqPropQuery ="//RequestProperties/PropertyEntry";
			String personIDQuery =  "//RequestProperties/PropertyEntry[@name='Person ID']";
			
			
			 for (Entry<String, TreeMap<String, List<PersonEntry>>> entry : this.avEntriesMap.entrySet())
		        {
				
		            String key = entry.getKey();
		            TreeMap<String, List<PersonEntry>> AvatarList = entry.getValue(); // all SL number for that patient
		            
		            

					//String query = "//book/author";//[@name='TRF89342']";
					List<Element> sampleList = queryXML(query,doc);
					List<Element> rPropertiesList = queryXML(ReqPropQuery,doc);
					List<Element> propEntry = queryXML(personIDQuery, doc);
					
					
					setSamples(sampleList,AvatarList);
					setRequestProperties(rPropertiesList,AvatarList);
					String personID = propEntry.get(0).getAttributeValue("value");
					
					writeXML(doc);
					
					if(importMode.toLowerCase().equals("avatar")) {
						callXMLImporter(this.AVATAR_FOLDER,personID);
					}
					else {
						callXMLImporter(this.FOUNDATION_FOLDER,personID);
					}	
					
					reader.close();
					reader = new FileReader(new File(this.initXML));
					doc = saxBuilder.build(reader);
				
		            //use key and value
		        }
			 reader.close();

			

		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void writeXML(Document doc) throws IOException {
		PrintWriter writer = new PrintWriter(this.outFileName);
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(doc, writer);
        outputter.output(doc, System.out);
        writer.close();
		
	
	}
	
	private void setRequestProperties(List<Element> rPropertiesList, Map<String, List<PersonEntry>> rANNOTMap) {
		String k = "";
		// Any avatar entry will contain request/patient level annotations
		// grabbing first key will work to get any avatarEntry of that patient
		for (String key: rANNOTMap.keySet()) { 
			k = key; 
			break;
		}
		
		for(Element prop : rPropertiesList) {
			if(prop.getAttributeValue("name").equals("MRN")) {
				String mrn = rANNOTMap.get(k).get(0).getMrn();
				prop.getAttribute("value").setValue(mrn);
			}
			else if(prop.getAttributeValue("name").equals("Full Name")) {
				String fullName = rANNOTMap.get(k).get(0).getFullName();
				prop.getAttribute("value").setValue(fullName);
			}
			else if(prop.getAttributeValue("name").equals("Gender")) {
				String gender = rANNOTMap.get(k).get(0).getGender();
				if(gender != null) {
					prop.getAttribute("value").setValue(gender);
				}
			}
			else if(prop.getAttributeValue("name").equals("Shadow ID")) {
				String shadowID = rANNOTMap.get(k).get(0).getShadowId();
				prop.getAttribute("value").setValue(shadowID);
			}
			else if(prop.getAttributeValue("name").equals("Person ID")) {
				String pID = rANNOTMap.get(k).get(0).getPersonId();
				prop.getAttribute("value").setValue(pID);
			}
		}
		
	}

	public List<Element> queryXML(String query,Document doc){
		XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
		List<Element> elementList = xpe.evaluate(doc);
		return elementList;
	}
	public void setSamples(List<Element> sampleList, Map<String, List<PersonEntry>>sampleAnnotations) {
		// You may have multiple  entries for one person 
		
		
		int i = 0;
		for(Entry<String, List<PersonEntry>> entry : sampleAnnotations.entrySet()){ 
			String key = entry.getKey(); // 
			List<PersonEntry> entries = entry.getValue();
			
			Element samples = sampleList.get(0).getParentElement();
			
				if(i == 0 ) {
					sampleList.get(i).getAttribute("ccNumber").setValue(entries.get(0).getCcNumber());
					sampleList.get(i).getAttribute("ANNOT21").setValue(entries.get(0).getAliasType());
					sampleList.get(i).getAttribute("name").setValue(entries.get(0).getSlNumber());
					sampleList.get(i).getAttribute("ANNOT27").setValue(entries.get(0).getSubmittedDiagnosis());
					sampleList.get(i).getAttribute("ANNOT66").setValue(entries.get(0).getTissueType());
					sampleList.get(i).getAttribute("ANNOT65").setValue(entries.get(0).getSampleSubtype());
					
					
				}else {
					Element newSample = sampleList.get(0).clone();
					newSample.getAttribute("ccNumber").setValue(entries.get(0).getCcNumber());
					newSample.getAttribute("ANNOT21").setValue(entries.get(0).getAliasType());
					newSample.getAttribute("name").setValue(entries.get(0).getSlNumber());
					newSample.getAttribute("ANNOT27").setValue(entries.get(0).getSubmittedDiagnosis());
					newSample.getAttribute("ANNOT66").setValue(entries.get(0).getTissueType());
					newSample.getAttribute("ANNOT65").setValue(entries.get(0).getSampleSubtype());
					
					samples.addContent(newSample);
				}

			i++;
		}
		
	}
	
public void	findFlaggedAvatarEntries() throws Exception{
		List<List<String>> flaggedKeys = new ArrayList<List<String>>();
		
		for(Entry<String, TreeMap<String, List<PersonEntry>>> entry : this.avEntriesMap.entrySet()) {
			String key = entry.getKey();
			TreeMap<String,List<PersonEntry>> slMap = this.avEntriesMap.get(key);
			for(Entry<String, List<PersonEntry>> e : slMap.entrySet()) {
				List<PersonEntry> avList = e.getValue();
				if(avList.size() > 1) {
					flaggedKeys.add(Arrays.asList(key,e.getKey()));
					this.flaggedAvatarEntries.add(avList);
					//slMap.remove(e.getKey());
				}else if(avList.size() == 1) {
					if(avList.get(0).getMrn().equals("")) {
						flaggedKeys.add(Arrays.asList(key,e.getKey()));
						this.flaggedAvatarEntries.add(avList);
					}
				}
				else { // there should always be atleast one entry in the list
					throw new Exception("Error: at least one avatar entry should be associated with it's id number");
				}
			}
			
		}
		
		for(List<String> flag : flaggedKeys) {
			if(flag.get(0).equals("")) {
				this.avEntriesMap.remove("");
			}else {
				TreeMap<String, List<PersonEntry>> slMap = this.avEntriesMap.get(flag.get(0));
				List<PersonEntry> avEntriesForSL = slMap.remove(flag.get(1));
				if(slMap.size() == 0) {
					this.avEntriesMap.remove(flag.get(0));
				}
			
				
			}
		}
		

		String filePath = XMLParser.getPathWithoutName(this.fileName);
		outFile(filePath +"flaggedIDs.out" , flaggedKeys );
		
	}
	
	
	
private String cleanData(String value){
	if(value != null) {
		String v = value.toUpperCase();
		if(v.equals("NULL")) {
			return "";
		}
	}else {
		return "";
	}
	
	return value;
}
	
public void readFile(String fileName) throws IOException{
		
		
		PeekableScanner scan = null;
	
		
		try {
			//bf = new BufferedReader(new FileReader(fileName));
			scan = new PeekableScanner(new File(fileName));
			String line = "";
		

			while(scan.hasNext() ) {//(line= bf.readLine()) != null){
				line= scan.next();
				String[] aEntries= line.split("\t");
				PersonEntry entry = new PersonEntry();
				
				if(importMode.equals("avatar")) { // avatar
					entry.setMrn(cleanData(aEntries[0]));
					entry.setPersonId(cleanData(aEntries[1]));
					entry.setFullName(cleanData(aEntries[2]));
					entry.setGender(cleanData(aEntries[3]));
					entry.setCcNumber(cleanData(aEntries[4]));
					entry.setShadowId(cleanData(aEntries[5]));
					entry.setAliasType(cleanData(aEntries[6]));
					entry.setSlNumber(cleanData(aEntries[7]));
					entry.setTissueType(cleanData(aEntries[8]));
					entry.setSampleSubtype(cleanData(aEntries[9]));
					entry.setSubmittedDiagnosis(cleanData(aEntries[10]));
					
				}else { // Foundation has less items per entry
					entry.setMrn(cleanData(aEntries[0]));
					entry.setPersonId(cleanData(aEntries[1]));
					entry.setFullName(cleanData(aEntries[2]));
					entry.setGender(cleanData(aEntries[3]));
					entry.setShadowId(cleanData(aEntries[4]));
					entry.setAliasType(cleanData(aEntries[5]));
					entry.setSlNumber(cleanData(aEntries[6]));
					entry.setTissueType(cleanData(aEntries[7]));
					entry.setSampleSubtype(cleanData(aEntries[8]));
					entry.setSubmittedDiagnosis(cleanData(aEntries[9]));
					entry.setCcNumber("");
					entry.setTissueType("");
					
					
				}
				
				
				
				if(this.avEntriesMap.get(entry.getMrn()) != null) {
					TreeMap<String,List<PersonEntry>> existingSLMap = this.avEntriesMap.get(entry.getMrn());
					if(existingSLMap.get(entry.getSlNumber()) != null) {
						existingSLMap.get(entry.getSlNumber()).add(entry);
						
					}else {
						existingSLMap.put(entry.getSlNumber(), new ArrayList<PersonEntry>(Arrays.asList(entry)));
					}
					//this.avEntriesMap.get(entry.getMrn()).add(entry);
				}else {
					List<PersonEntry> avEnteries = new ArrayList<PersonEntry>();
					avEnteries.add(entry);
					TreeMap<String,List<PersonEntry>> slMap = new TreeMap<String, List<PersonEntry>>();
					slMap.put(entry.getSlNumber(),avEnteries);
					this.avEntriesMap.put(entry.getMrn(), slMap);
					
				}
				
			
			}	
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			scan.close();
		}
		
	}
	
	
	
	
	
	
	private void callXMLImporter(String folderName,String name) throws Exception {
		
		
		List<String> importRequestCommands = new ArrayList<String>();
		List<String> importAnalysisCommands = new ArrayList<String>();
		
		
		List<String> cmdCommands = new ArrayList<String>();
		
		String path = XMLParser.getPathWithoutName(this.fileName);
		Query q = new Query(path + "hci-creds.properties");

		
		
		
		importRequestCommands.add("bash " + importScript + " -login adminBatch -file " + outFileName +
				" -annotationFile " + this.annotationFileName + " -isExternal Y"+ " -requestIDList " + path + "tempRequestList.out" );
		
		
		
		System.out.println(importRequestCommands.get(0));
		
		String osName = System.getProperty("os.name");
		if(osName.equals("Windows 7")) { //osName.equals("Windows 7")
			//executeCMDCommands(cmdCommands); 
		}else {
			executeCommands(importRequestCommands);
			
			
			boolean newAnalysis = q.isNewAnalysis(name,folderName);
			if(newAnalysis) {
				String experimentNumber = getCurrentRequestId(path + "tempRequestList.out") + "R";
				importAnalysisCommands.add("bash create_analysis.sh " + "-lab Bioinformatics "+  "-name " + name +  " -organism human -genomeBuild hg19 -analysisType Alignment -isBatchMode Y " 
						   + "-folderName " + "\""+ folderName +"\" " + "-experiment " + experimentNumber + " -server localhost -linkBySample" );
				
				System.out.println(importAnalysisCommands.get(0));
				executeCommands(importAnalysisCommands);
				
				
				
				//String analysisId = q.getIdAnalysisFromPropertyEntry(name);
				
				///System.out.println("Here is the id For the newly created Analysis: " +  analysisId);
				
//				if(analysisId != null) {
//					importAnalysisCommands.add("bash LinkExpToAnal.sh " + " -request " + requestId + " -analysis " + analysisId + " -add"  );
//					importAnalysisCommands.remove(0);
//					
//					System.out.println(importAnalysisCommands.get(0));
//					//executeCommands(importAnalysisCommands);
//					
//				}else {
//					throw new Exception("There was an issue finding new analysis to link with for experiment"); 
//				}
			}
			
			
			
			//System.out.println(importAnalysisCommands.get(0));
			
			//executeCommands(importAnalysisCommands);
		}
	
		q.closeConnection();	
		
	}
	
	
	private File createTempScript(List<String> commands) throws IOException {
		File tempScript = File.createTempFile("script", null);

		Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
		PrintWriter printWriter = new PrintWriter(streamWriter);
		
		
		for(int i =0; i < commands.size(); i++) {
			printWriter.println(commands.get(i));
		}
		
		printWriter.close();

		return tempScript;
	}
	
	
	private void executeCommands(List<String> commands) {

		File tempScript = null;
		
		try {
			System.out.println("started executing command");
			tempScript = createTempScript(commands);
			ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
			pb.inheritIO();
			//pb.redirectError(new File("/home/u0566434/Scripts/SomeFile.txt"));
			Process process;
			process = pb.start();
			process.waitFor();
			
			System.out.println("finished executing command");
		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally

		{
			tempScript.delete();
		}
	}
	private void executeCMDCommands(List<String> commands) {

		File tempScript = null;
		
		try {
			System.out.println("started executing command");
			tempScript = createTempScript(commands);
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "start");
			pb.command(tempScript.toString());
			pb.inheritIO();
			Process process;
			process = pb.start();
			process.waitFor();
			System.out.println("finished executing command");
		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally

		{
			tempScript.delete();
		}
	}
	
	
	
	private void outFile(String fileName,List<List<String>> flaggedIDs) {
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new FileWriter(fileName));
			
			for(int i = 0; i < flaggedIDs.size(); i++) {
				List<String> slIDs = flaggedIDs.get(i);


				if(i < flaggedIDs.size() - 1) {
					pw.write(slIDs.get(1) + "\n");
				}else {
					pw.write(slIDs.get(1) );
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			pw.close();
		}
	}
	
	public static String getPathWithoutName(String fullPathWithFile) {
		String[] splitPath = fullPathWithFile.split("/");
		String filePath = String.join("/", Arrays.copyOfRange(splitPath, 0 , splitPath.length - 1));
		return filePath + "/";
		
	}
	
	public static String getCurrentRequestId(String fileName){
		BufferedReader bf = null;
		String currentId = "";

		try {
			bf = new BufferedReader(new FileReader(fileName));
			String idStr = bf.readLine();
			String[] idArray = idStr.split(" ");
			currentId = idArray[idArray.length - 1];
			System.out.println("Current requestID:  " + currentId );


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return currentId;
	}
		

}









/*xpe = XPathFactory.instance().compile("//book[@type='horror']/author", Filters.element());
authList = xpe.evaluate(doc);

for (Element auth : authList) {
	System.out.println(auth.getText().toString());
}*/


// Element auth = rootElement.getChild("book").getChild("author");



/*for (Element el : rootElement.getDescendants(Filters.element())) {
	if(el.getName().equals("samples")) {
		System.out.println(el.getAttributes().toString());
		List<Element> sampleChildren = el.getChildren();
		System.out.println(sampleChildren.toString());
	}
	
}*/
//IteratorIterable<Element> descendantIter = doc.getRootElement().getDescendants(Filters.element());


/*List<Element> childrenNodes = doc.getRootElement().getChildren();
for (Element node : childrenNodes ) {
	
	System.out.println(node.toString());
	if(node.getName().equals("samples")){
		List<Element> sampleList = node.getChildren();
		
		for(Element sample : sampleList ) {
			Attribute fullName = sample.getAttribute("ANNOT26").setValue("Peter");
			System.out.println(fullName.getValue());
			
		}
		
	}
	

	//Content c = descendantIter.next();
	//if (c.getCType() == Content.CType.Element) {
		
		/*if(childNode.getName().equals("samples")) {
			ArrayList<Element> sampleNodes =  child
		}*
		//Attribute id = childNode.getAttribute("id");
		if (id != null && id.getName().equals("bk102")) {
			System.out.println(childNode.getName() + " " + id.toString());
		}
	if(false) {
		
	} else if (c.getCType() == Content.CType.Text) {
		Text t = (Text) c;
		System.out.println(t.getText());
	} else if (c.getCType() == Content.CType.EntityRef) {
		EntityRef ef = (EntityRef) c;
		System.out.println(ef.getName());
	} else if (c.getCType() == Content.CType.Comment) {
		Comment comment = (Comment) c;
		System.out.println(comment.getText());
	}

}*/




