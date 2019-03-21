package hci.gnomex.daemon.auto_import;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
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
	private String pathOnly;
	private String appendedPathOnly;

	private static final String FOUNDATION_FOLDER="Patients - Foundation - NO PHI";
	private static final String AVATAR_FOLDER="Patients - Avatar - NO PHI";
	private static final String IMPORT_EXPERIMENT_ERROR = "import_experiment_error.log";
	private static final String IMPORT_ANALYSIS_ERROR = "import_analysis_error.log";
	private static final String LINK_EXP_ANAL_ERROR = "link_exp_analysis_error.log";





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

		if(this.fileName != null && !this.fileName.equals("") ){
			pathOnly = XMLParser.getPathWithoutName(this.initXML); // initXML will always be in the same directory, the root
			appendedPathOnly = XMLParser.appendToPathWithoutName(this.fileName, "log");
		}else{
			System.out.println("You need to have the file path for all temp files and cred files");
			System.exit(1);
		}




	}



	public void parseXML() throws Exception {


		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("current relative path is: " + s);
		System.out.println("[XMLParser]->parseXML() intXML: " +  this.initXML);
		System.out.println("[XMLParser]->parseXML() fileName: " + this.fileName );

		File inputFile = new File(this.initXML);
		FileReader reader = null;

		SAXBuilder saxBuilder = new SAXBuilder();
		try {

			reader = new FileReader(inputFile);
			readFile(this.fileName);


			findFlaggedPersonEntries();
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
			System.exit(1);
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}


	private void writeXML(Document doc) throws IOException {
		PrintWriter writer = new PrintWriter(this.outFileName);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.output(doc, writer);
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

	public void	findFlaggedPersonEntries() throws Exception{
		List<List<String>> flaggedKeys = new ArrayList<List<String>>();
		StringBuilder strBuildBody = new StringBuilder();

		for(Entry<String, TreeMap<String, List<PersonEntry>>> entry : this.avEntriesMap.entrySet()) {
			String key = entry.getKey();
			TreeMap<String,List<PersonEntry>> slMap = this.avEntriesMap.get(key);
			for(Entry<String, List<PersonEntry>> e : slMap.entrySet()) {
				List<PersonEntry> personList = e.getValue();
				if(personList.size() > 1) {
					flaggedKeys.add(Arrays.asList(key,e.getKey()));
					this.flaggedAvatarEntries.add(personList);
					//slMap.remove(e.getKey());
				}else if(personList.size() == 1) {
					if(personList.get(0).getMrn().equals("")
							|| personList.get(0).getPersonId().equals("")
							|| personList.get(0).getFullName().equals("")
							|| personList.get(0).getGender().equals("")
					) {
						flaggedKeys.add(Arrays.asList(key,e.getKey()));
						this.flaggedAvatarEntries.add(personList);
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
			}else { // if you need to remove both patient id and sample id
				TreeMap<String, List<PersonEntry>> slMap = this.avEntriesMap.get(flag.get(0));
				slMap.remove(flag.get(1));
				if(slMap.size() == 0) {
					this.avEntriesMap.remove(flag.get(0));
				}
			}
		}
		// prepare flagged entries for email and file
		strBuildBody.append("The following sample records have be flagged. They need to be verified and reimported\n\n");
		for( List<PersonEntry>sampRecord :flaggedAvatarEntries){
			for(PersonEntry person : sampRecord){
				strBuildBody.append(person.toString(importMode));
			}
		}



		System.out.println("File path used for majority of input and output files : " + pathOnly);

		//flaggedIDs.out file is used for the FileMover to determine which samples need to be moved to the flagged folder
		outFile(pathOnly , "flaggedIDs.out" , flaggedKeys );

		System.out.println("File path used for flagged log file  : " + appendedPathOnly);
		saveflaggedLog(appendedPathOnly,strBuildBody);
		sendFlaggedIDEmail(strBuildBody);


	}

	private void sendFlaggedIDEmail(StringBuilder strBuildBody){

		String to = "erik.rasmussen@hci.utah.edu, dalton.wilson@hci.utah.edu";
		String from = "erik.rasmussen@hci.utah.edu";
		String subject = "Flagged Sample ID Report PHI";

		try {
			if(flaggedAvatarEntries.size() > 0  ) {
				DirectoryBuilder.sendImportedIDReport(from, to, subject, strBuildBody.toString(), "");
			}else{
				System.out.println("There are no flagged files. Email will not be sent.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
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

				if(importMode.toLowerCase().equals("avatar")) { // avatar
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
					entry.setSampleSubtype(cleanData(aEntries[7]));
					entry.setTissueType(cleanData(aEntries[8]));
					entry.setSubmittedDiagnosis(cleanData(aEntries[9]));
					entry.setCcNumber("");


				}



				if(this.avEntriesMap.get(entry.getPersonId()) != null) {
					TreeMap<String,List<PersonEntry>> existingSLMap = this.avEntriesMap.get(entry.getPersonId());
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
					this.avEntriesMap.put(entry.getPersonId(), slMap);

				}


			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			scan.close();
			System.exit(1); //
		}
		finally {
			scan.close();
		}

	}






	private void callXMLImporter(String folderName,String name) throws Exception {


		List<String> importRequestCommands = new ArrayList<String>();
		List<String> importAnalysisCommands = new ArrayList<String>();
		List<String> linkExperimentToAnalysis = new ArrayList<String>();
		List<String> cmdCommands = new ArrayList<String>();

		Query q = new Query(pathOnly + "gnomex-creds.properties");

		importRequestCommands.add("bash " + importScript + " -login adminBatch -file " + outFileName +
				" -annotationFile " + this.annotationFileName + " -isExternal Y"+ " -requestIDList " + pathOnly + "tempRequestList.out" );

		System.out.println(importRequestCommands.get(0));

		String osName = System.getProperty("os.name");
		if(osName.equals("Windows 7")) { //osName.equals("Windows 7")
			//executeCMDCommands(cmdCommands);
		}else {
			executeCommands(importRequestCommands, appendedPathOnly + IMPORT_EXPERIMENT_ERROR);
			String requestID = readInLastEntry(pathOnly + "tempRequestList.out" );
			System.out.print("This is the request Id I retrieved from the file " + requestID);


			Integer analysisID = q.getAnalysisID(name,folderName);
			if(analysisID == -1) { // new Analysis
				String experimentNumber = getCurrentRequestId(pathOnly + "tempRequestList.out") + "R";
				importAnalysisCommands.add("bash httpclient_create_analysis.sh " + "-lab Bioinformatics "+  "-name " + name +  " -organism human -genomeBuild hg19 -analysisType Alignment -isBatchMode Y "
						+ "-folderName " + "\""+ folderName +"\" " + "-experiment " + experimentNumber + " -server localhost -linkBySample -analysisIDFile " + pathOnly + "tempAnalysisList.out" );

				System.out.println(importAnalysisCommands.get(0));
				executeCommands(importAnalysisCommands,appendedPathOnly  + IMPORT_ANALYSIS_ERROR);
				analysisID = new Integer(readInLastEntry(pathOnly + "tempAnalysisList.out" ));

			}else{ // existing analysis
				if(analysisID != null && !requestID.equals("")){

					if(!q.hasLinkAnalysisExperiment( analysisID, new Integer(requestID))){
						linkExperimentToAnalysis.add("bash LinkExpToAnal.sh -request " + requestID + " -analysis " + analysisID + " -add");
						System.out.println(linkExperimentToAnalysis.get(0));
						executeCommands(linkExperimentToAnalysis,appendedPathOnly  + LINK_EXP_ANAL_ERROR);

					}

				}

				saveAnalysisID(pathOnly + "tempAnalysisList.out",analysisID);
			}

			/*CollaboratorPermission cp = new CollaboratorPermission(this.avEntriesMap.get(name),analysisID,q);
			List<String> irbAssocationList = cp.getIRAAssociation();
			cp.assignAnalysisPermissionToCollabs(irbAssocationList);*/

		}

		q.closeConnection();

	}

	private String readInLastEntry(String fileName) {
		BufferedReader bf = null;
		String id = "";

		try {
			bf = new BufferedReader(new FileReader(fileName));
			String line = "";

			while((line= bf.readLine()) != null){
				String[] idList  = line.split(" ");
				if(idList.length > 0){
					id = idList[idList.length - 1];
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try { bf.close(); } catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(1); //
		}catch (IOException e){
			e.printStackTrace();
			try { bf.close(); } catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		finally {
			try { bf.close(); } catch (IOException e) {
				e.printStackTrace();
			}
		}

		return id;

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


	private void executeCommands(List<String> commands,String outError) throws Exception {

		File tempScript = null;

		try {
			System.out.println("started executing command");
			tempScript = createTempScript(commands);
			ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
			pb.inheritIO();
			Process process;
			File errorFile = new File(outError);
			pb.redirectError(errorFile);


			process = pb.start();
			process.waitFor();

			if(hasSubProccessErrors(errorFile)){
				System.out.println("Error detected exiting script");
				System.exit(1);
			}
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

	private boolean hasSubProccessErrors(File errorFile) {
		Scanner scan = null;
		boolean hasError = false;
		try{
			scan = new Scanner(errorFile);
			System.out.println("************************************************************");
			System.out.println("Errors will appear below if found, in this file, " + errorFile.getName());
			while(scan.hasNext()){
				String line = scan.nextLine();
				if(line != null && !line.equals("")){
					hasError = true;
					System.out.println(line);
				}

			}
			System.out.println("************************************************************");
		}catch(FileNotFoundException e){
			if(scan != null){ scan.close(); }
			hasError = false;
		}finally {
			scan.close();
		}
		return hasError;
	}

	private void executeCMDCommands(List<String> commands) {

		File tempScript = null;

		try {
			System.out.println("started executing command");
			tempScript = createTempScript(commands);
			ProcessBuilder pb = new ProcessBuilder("CMD", "/C");
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

	private void saveflaggedLog(String path,  StringBuilder report){ // This file is so the person entries can easily be modified and used to reimport flagged data.
		boolean madeDir = new File(path).mkdir();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String time = timestamp.toLocalDateTime().toString().replace(" ", "-");
		PrintWriter pw = null;
		try {
			String flaggedEnteriesFile = "flaggedEntries_" + time + ".log";
			pw = new PrintWriter(new FileWriter(path + flaggedEnteriesFile ));
			pw.write(report.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			System.exit(1);
		}finally {
			pw.close();
		}




	}

	private void outFile(String path,String fileName, List<List<String>> flaggedIDs) {
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(new FileWriter(path + fileName));

			for(int i = 0; i < flaggedIDs.size(); i++) {
				List<String> slIDs = flaggedIDs.get(i);


				if(i < flaggedIDs.size() - 1) {
					pw.write(slIDs.get(1) + "\n");
				}else {
					pw.write(slIDs.get(1) );
				}
			}
			pw.close();
			// This the sl or trf list after flagged id's have been removed out
			String name = "";
			if(this.importMode.equals("avatar")){
				name = "importedSLList.out";
			}else{
				name = "importedTRFList.out";
			}
			pw = new PrintWriter(new FileWriter(path + name));


			for( String experimentID : this.avEntriesMap.keySet()){
				Map<String, List<PersonEntry>> personInfo = this.avEntriesMap.get(experimentID);
				for(String sampleName : personInfo.keySet()){
					pw.write(sampleName + "\n");
				}
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			System.exit(1);
		}finally {
			pw.close();
		}
	}

	public static String getPathWithoutName(String fullPathWithFile) {


		String[] splitPath = fullPathWithFile.split("/");
		String filePath = String.join("/", Arrays.copyOfRange(splitPath, 0 , splitPath.length - 1));
		return filePath + "/";

	}
	public static String appendToPathWithoutName(String fullPathWithFileName, String appendedPath) {

		String[] splitPath = fullPathWithFileName.split("/");
		String filePath = "";
		int index = -1;
		for(int i = 0; i < splitPath.length; i++){
			if(splitPath[i].equals(appendedPath)){
				index = i;
				break;
			}
		}
		if(index != -1){ // if found don't need to append Path
			filePath = String.join("/", Arrays.copyOfRange(splitPath, 0 , index + 1));
		}else{// if not found add another subdirectory to path
			List<String> paths = Arrays.asList( Arrays.copyOfRange(splitPath, 0,splitPath.length - 1));
			ArrayList<String> p =  new ArrayList<String>(paths);
			p.add(appendedPath);
			filePath = String.join("/", p);
		}

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


	private void saveAnalysisID(String fileName,Integer existingAnalysisID ){

		PrintWriter pw = null;
		try {

			pw = new PrintWriter(new FileOutputStream(new File(fileName), true));
			pw.write(" " + existingAnalysisID + " ");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.close();
		}

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




