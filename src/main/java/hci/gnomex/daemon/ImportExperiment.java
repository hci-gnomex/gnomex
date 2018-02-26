package hci.gnomex.daemon;

import hci.gnomex.controller.SaveRequest;
import hci.gnomex.model.*;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.*;

import java.io.File;
import java.io.StringReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import static hci.gnomex.controller.SaveRequest.initSample;

public class ImportExperiment {

	private BatchDataSource dataSource;
	private Session sess;

	private Transaction tx;

	private static ImportExperiment app = null;

	private String errorMessagePrefixString = "Error in ImportExperiment";

	private RequestParser requestParser;
	private SecurityAdvisor secAdvisor;

	private String login = null;
	private String fileName = "";
	private String annotationFileName = "";
	private String isExternal = "Y"; // By default, experiment imported will be external

	private Document document;
	private Element requestNode;

	private Request request;
	private Integer nextSampleNumber;
	private Map idSampleMap = new HashMap();
	private Map<String, String> oldIdSampleMap = new HashMap<String, String>();
	private TreeSet samples = new TreeSet(new SampleNumberComparator());
	private TreeSet sequenceLanes = new TreeSet(new SequenceLaneNumberComparator());

	private TreeSet samplesAdded = new TreeSet(new SampleNumberComparator());
	private TreeSet sequenceLanesAdded = new TreeSet(new SequenceLaneNumberComparator());

	private AppUser appuser;
	private Lab lab;
	private String labLastName;
	private String labFirstName;
	private Integer idAppUser;
	private String submitterFirstName;
	private String submitterLastName;
	private Project project;
	private Organism organism;
	private boolean updateMode;
	private Application application;
	private HashMap<String, String> sourcePropertyOptionMap = new HashMap<String, String>(); // key is idProperty "-" idPropertyOption, value is option name
	private HashMap<Integer, Property> sourceToTargetPropertyMap = new HashMap<Integer, Property>();
	private HashMap<String, Property> targetPropertyMap = new HashMap<String, Property>();
	private HashMap<Integer, Property> targetIdToPropertyMap = new HashMap<Integer, Property>();
	private HashMap<String, RequestCategory> requestCategoryMap = new HashMap<String, RequestCategory>();
	private HashMap<String, Organism> organismMap = new HashMap<String, Organism>();
	private HashMap<String, SampleType> sampleTypeMap = new HashMap<String, SampleType>();
	private HashMap<String, GenomeBuild> genomeBuildMap = new HashMap<String, GenomeBuild>();

	private HashMap<Integer, Integer> idPropertyMap = new HashMap<Integer, Integer>();

	private Set<AppUser> members;

	public ImportExperiment(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-file")) {
				fileName = args[++i];
			} else if (args[i].equals("-annotationFile")) {
				annotationFileName = args[++i];
			} else if (args[i].equals("-isExternal")) {
				isExternal = args[++i];
			} else if (args[i].equals("-login")) {
				login = args[++i];
			} else if (args[i].equals("-help")) {
				printUsage();
				System.exit(0);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		app = new ImportExperiment(args);

		app.run();
	}

	public void run() {
		Calendar calendar = Calendar.getInstance();
		errorMessagePrefixString += " on " + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(calendar.getTime()) + "\n";

		try {
			Logger log = Logger.getLogger("org.hibernate");
			log.setLevel(Level.ERROR);

			// Connect to the database
			dataSource = new BatchDataSource();
			sess = dataSource.connect();

			// Create a security advisor
			secAdvisor = SecurityAdvisor.create(sess, login);

			Transaction tx = sess.beginTransaction();

			// Init dictionaries
			initDictionaries();

			// Read the input XML file. This is the output from GetRequest.gx
			readExperimentXMLFile();

			// Read the annotation XML File. This is the output from GetPropertyList.
			readAnnotationXMLFile();

			// Set the attributes on Request to the correct ids for lab, app user, codeApplication, project
			initRequestNode();
			initSampleNodes();
			initSequenceLaneNodes();
//			System.out.println("The request xml after init");

//            XMLOutputter out = new org.jdom.output.XMLOutputter();
//            String xmlResult = out.outputString(document);
//			System.out.println(xmlResult);


			// Set the annotation attributes so that the name contains the id of the property from the
			// target gnomex db.
			mapAnnotations();

			// Use RequestParser to parse the XML to create a request instance
			Integer updateIDRequest = requestToUpdate(sess,"Shadow ID");


			updateMode = false; // update mode made for automation proccess, won't interfere with regular import.
			//
			if((requestNode.getAttributeValue("number").equals("0")
					|| requestNode.getAttributeValue("number").equals(""))
					&& updateIDRequest != null){
				updateMode = true;
			}
			RequestCategory requestCategory = requestCategoryMap.get(requestNode.getAttributeValue("codeRequestCategory"));
			requestParser = new RequestParser(requestNode, secAdvisor);
			requestParser.parseForImport(sess, requestCategory);
			request = requestParser.getRequest();
			if (request == null) {
				System.out.println ("[ImportExperiment] request is null THIS IS BAD");
			}

			// Save the experiment
			System.out.println("updateMode: " + updateMode);
			if(!updateMode) {
				System.out.println("[ImportExperiment] before SaveRequest.saveRequest " + request.getIdRequest());
				SaveRequest.saveRequest(sess, requestParser, requestNode.getAttributeValue("description"), true);
				saveRequestProperties(requestNode, sess, requestParser, true);
				System.out.println("[ImportExperiment] after SaveRequest.saveRequest " + request.getIdRequest());
			}

			// Save the samples


			saveSamples(updateIDRequest);
			System.out.println("[ImportExperiment] after saveSamples()");

			// Save the sequence lanes
			SaveRequest.saveSequenceLanes(secAdvisor, requestParser, sess, requestCategory, idSampleMap, sequenceLanes, sequenceLanesAdded, true);
			System.out.println("[ImportExperiment] after SaveRequest.saveSequenceLanes()");

			// Commit the transaction
			sess.getTransaction().commit();

			System.out.println("[ImportExperiment] after sess.getTransaction().commit()");

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			this.sendErrorReport(e);

		} finally {
			if (sess != null) {
				try {
					sess.close();
				} catch (Exception e) {
					System.err.println("ImportExperiment unable to disconnect from hibernate session.   " + e.toString());
				}
			}
		}

		System.out.println("[ImportExperiment] at the very end");
		System.exit(0);

	}


	private Integer requestToUpdate(Session sess, String queryWithPropEntry) throws Exception {
		Integer updateReqId = null;
		List<Element> reqProps = this.requestNode.getChild("RequestProperties").getChildren();
		Element reqANNOTNode = null;
		for(int i = 0; i <  reqProps.size(); i++){
			if(reqProps.get(i).getAttributeValue("name").equals(queryWithPropEntry)){
				reqANNOTNode = reqProps.get(i);
				break;
			}
		}
		//FROM Sample samp JOIN samp.request as req
		if(reqANNOTNode != null){
			System.out.println("Found the correct annotation!!!!!!!!!!!!!!!! ");
			String reqANNOTValue = reqANNOTNode.getAttributeValue("value");
			System.out.println(reqANNOTValue);
			if(reqANNOTValue != null){ //:daysInFuture
				String queryStr = "SELECT req.idRequest, req.number" +
						"  FROM Request req" +
						"  WHERE req.idRequest IN ( SELECT pe.idRequest FROM PropertyEntry pe" +
						"  WHERE pe.value = :reqANNOTValue )";
				Query q = sess.createQuery(queryStr).setParameter("reqANNOTValue", reqANNOTValue);
				List reqSampleTable = q.list();
				if(reqSampleTable.size()  == 1 ){
					Object[] row = (Object[]) reqSampleTable.get(0);
					updateReqId = (Integer)row[0];

					System.out.println("idRequest from query of property entry: " + updateReqId);

				}else if( reqSampleTable.size() == 0 ){
					updateReqId = null;
				}else {
					throw new Exception("Multiple results for a unique entry");
				}


			}else{
				updateReqId = null;
			}
		}

		return updateReqId;
	}

	private void readExperimentXMLFile() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(fileName);
		document = (Document) builder.build(xmlFile);
	}

	private void readAnnotationXMLFile() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		File annotationXMLFile = new File(annotationFileName);
		Document annotationDocument = (Document) builder.build(annotationXMLFile);

		// We need a map that will allow us to lookup the option name based on the source db idPropertyOption.
		// Then we can lookup the target idPropertyOption based on this option name.
		for (Iterator i = annotationDocument.getRootElement().getChildren("Property").iterator(); i.hasNext();) {
			Element propertyNode = (Element) i.next();
			for (Iterator i1 = propertyNode.getChild("options").getChildren("PropertyOption").iterator(); i1.hasNext();) {
				Element optionNode = (Element) i1.next();
				String key = propertyNode.getAttributeValue("idProperty") + "-" + optionNode.getAttributeValue("idPropertyOption");
				sourcePropertyOptionMap.put(key, optionNode.getAttributeValue("option"));

			}
		}

	}

	private void initDictionaries() throws Exception {

		// Load properties
		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append("SELECT p from Property as p order by p.name");
		List properties = sess.createQuery(queryBuf.toString()).list();
		System.out.println("Loading annotation form the db ");
		for (Iterator i = properties.iterator(); i.hasNext();) {
			Property prop = (Property) i.next();
			try {
				Hibernate.initialize(prop.getOptions());
			} catch (HibernateException e) {
				System.out.println("warning - unable to initialize options on property " + prop.getIdProperty() + " " + e.toString());
			}
			targetPropertyMap.put(prop.getName().toUpperCase(), prop);
			targetIdToPropertyMap.put(prop.getIdProperty(), prop);
			//System.out.println("db annot: " + prop.getName() + " idProperty: " + prop.getIdProperty() );

		}
		System.out.println("finished loading annotation form the db ");

		// Load request categories
		queryBuf = new StringBuffer();
		queryBuf.append("SELECT rc from RequestCategory as rc");
		List requestCategories = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = requestCategories.iterator(); i.hasNext();) {
			RequestCategory rc = (RequestCategory) i.next();
			requestCategoryMap.put(rc.getCodeRequestCategory(), rc);
		}

		// Load organisms
		queryBuf = new StringBuffer();
		queryBuf.append("SELECT o from Organism as o");
		List organisms = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = organisms.iterator(); i.hasNext();) {
			Organism o = (Organism) i.next();
			organismMap.put(o.getOrganism(), o);
		}

		// Load genomeBuilds
		queryBuf = new StringBuffer();
		queryBuf.append("SELECT gb from GenomeBuild as gb");
		List genomeBuilds = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = genomeBuilds.iterator(); i.hasNext();) {
			GenomeBuild gb = (GenomeBuild) i.next();
			genomeBuildMap.put(gb.getGenomeBuildName(), gb);
		}

		// Load sampleTypes
		queryBuf = new StringBuffer();
		queryBuf.append("SELECT st from SampleType as st");
		List sampleTypes = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = sampleTypes.iterator(); i.hasNext();) {
			SampleType st = (SampleType) i.next();
			sampleTypeMap.put(st.getSampleType(), st);
		}
	}

	private void initRequestNode() throws Exception {
		// Zero out the idRequest so that a new request is created
		requestNode = document.getRootElement().getChild("Request");
		requestNode.setAttribute("idRequest", "0");
		requestNode.setAttribute("isExternal", isExternal);

		requestNode.setAttribute("name", requestNode.getAttributeValue("name"));

		// We only handle Illumina request types for importing at this time
		String codeRequestCategory = requestNode.getAttributeValue("codeRequestCategory");
		RequestCategory rc = requestCategoryMap.get(codeRequestCategory);
		if (rc == null) {
			throw new Exception("codeRequestCategory is not defined: " + codeRequestCategory);
		}
		// if (rc.getCategoryType() == null || rc.getCategoryType().getIsIllumina() == null || !rc.getCategoryType().getIsIllumina().equals("Y")) {
		// throw new Exception("Only Illumina HiSeq/MiSeq experiment types can be imported");
		// }

		// Fill in the idLab based on the lab name
		parseLabName();
		String theQuery = "select l from Lab l where lastName = '" + labLastName + "'" + " and firstName = '" + labFirstName + "'";
		if (labFirstName == null) {
			theQuery = "select l from Lab l where lastName = '" + labLastName + "'";
		}

		Query labQuery = sess.createQuery(theQuery);
		lab = (Lab) labQuery.uniqueResult();
		if (lab == null) {
			throw new Exception("Cannot find lab " + labLastName + ", " + labFirstName);
		}
		requestNode.setAttribute("idLab", lab.getIdLab().toString());

		// Fill in the idOwner, idAppUser based on and submitter name
		parseSubmitterName();
		idAppUser = getIdAppUser(lab);
		if (idAppUser == null) {
			throw new Exception("Cannot find idAppUser " + idAppUser);
		}

		requestNode.setAttribute("idAppUser", idAppUser.toString());
		requestNode.setAttribute("idSubmitter", idAppUser.toString());

		// Obtain the idOrganism based on the organismName
		String organismName = requestNode.getAttributeValue("organismName");
		Query organismQuery = sess.createQuery("select o from Organism o where o.organism = '" + organismName + "'");
		organism = (Organism) organismQuery.uniqueResult();
		if (organism == null) {
			throw new Exception("Cannot find organism " + organismName);
		}
		requestNode.setAttribute("idOrganism", organism.getIdOrganism().toString());
		requestNode.setAttribute("idOrganismSampleDefault", organism.getIdOrganism().toString());

		// Make sure that we can find the application based on the application name
		// String applicationName = requestNode.getChild("application").getChild("Application").getAttributeValue("application");
		// Query applicationQuery = sess.createQuery("select a from Application a where application = '" + applicationName + "'");
		// application = (Application)applicationQuery.uniqueResult();
		// if (application == null) {
		// throw new Exception("Cannot find the application '" + applicationName + "'");
		// }

		// Find the project based on the name. If it isn't found, create a new one
		String projectName = requestNode.getChild("project").getChild("Project").getAttributeValue("name");
		String projectDescription = requestNode.getChild("project").getChild("Project").getAttributeValue("description");
		Query projectQuery = sess.createQuery("select p from Project p where p.name = '" + projectName + "' and p.idLab = " + lab.getIdLab());
		project = (Project) projectQuery.uniqueResult();
		if (project == null) {
			project = new Project();
			project.setName(projectName);
			project.setDescription(projectDescription);
			project.setIdLab(lab.getIdLab());
			project.setIdAppUser(idAppUser);
			sess.save(project);
			sess.flush();
		}
		requestNode.setAttribute("idProject", project.getIdProject().toString());

		// Remove billing items
		requestNode.removeChild("billingItems");
		requestNode.setAttribute("idBillingAccount", "");

	}

	private void initSampleNodes() throws Exception {
		int x = 1;
		for (Iterator i1 = requestNode.getChild("samples").getChildren("Sample").iterator(); i1.hasNext();) {
			Element sampleNode = (Element) i1.next();

			// Set the idSample to "Sample##", incrementing the number for each sample
			String idSampleNew = "Sample" + x++;
			System.out.println("old idSample: " + sampleNode.getAttributeValue("idSample"));
			oldIdSampleMap.put(sampleNode.getAttributeValue("idSample"), idSampleNew);
			sampleNode.setAttribute("idSample", idSampleNew);
			System.out.println("new idSample: " + sampleNode.getAttribute("idSample").getValue());
			System.out.println();

			// Set the idOrganism on the sample node
			System.out.println("organism Name: " + sampleNode.getAttributeValue("organism"));
			String organismName = sampleNode.getAttributeValue("organism");
			if (organismName != null && !organismName.equals("")) {
				Organism organism = organismMap.get(organismName);
				if (organismName == null) {
					throw new Exception("Cannot find organism " + organismName + ".");
				}

				sampleNode.setAttribute("idOrganism", organism.getIdOrganism().toString());
				System.out.println(" organism id: " + sampleNode.getAttribute("idOrganism").getValue());
			} else {
				sampleNode.setAttribute("idOrganism", "");
			}
			System.out.println();
			// Set the idSampleType on the sample node
			String sampleTypeName = sampleNode.getAttributeValue("sampleType");
			System.out.println("sampleTypeName: " + sampleTypeName );
			if (sampleTypeName != null && !sampleTypeName.equals("")) {
				SampleType sampleType = sampleTypeMap.get(sampleTypeName);
				if (sampleType == null) {
					throw new Exception("Cannot find sample type " + sampleTypeName + ".");
				}
				sampleNode.setAttribute("idSampleType", sampleType.getIdSampleType().toString());
				System.out.println("idSampleType: " + sampleType.getIdSampleType().toString() );

			} else {
				sampleNode.setAttribute("idSampleType", "");
			}

			// Blank out idOligoBarcode, barcodeSequence
			sampleNode.setAttribute("idOligoBarocde", "");
			sampleNode.setAttribute("barcodeSequence", "");

		}
	}

	private void initSequenceLaneNodes() throws Exception {
		int x = 1;
		for (Iterator i1 = requestNode.getChild("sequenceLanes").getChildren("SequenceLane").iterator(); i1.hasNext();) {
			Element laneNode = (Element) i1.next();

			// Set the idSample to "Sample##", incrementing the number for each sample
			laneNode.setAttribute("idSequenceLane", "SequenceLane" + x++);

			String idSampleOld = laneNode.getAttributeValue("idSample");
			laneNode.setAttribute("idSample", oldIdSampleMap.get(idSampleOld));

			// Set the idOrganism on the sample node
			String organismName = laneNode.getAttributeValue("organism");
			if (organismName != null && !organismName.equals("")) {
				Organism organism = organismMap.get(organismName);
				if (organismName == null) {
					throw new Exception("Cannot find organism " + organismName + ".");
				}
				laneNode.setAttribute("idOrganism", organism.getIdOrganism().toString());
			} else {
				laneNode.setAttribute("idOrganism", "");
			}

			// Set the idGenomeBuild on the sample node
			String genomeBuildName = laneNode.getAttributeValue("genomeBuild");
			if (genomeBuildName != null && !genomeBuildName.equals("")) {
				GenomeBuild genomeBuild = genomeBuildMap.get(genomeBuildName);
				if (genomeBuild == null) {
					throw new Exception("Cannot find genomeBuild " + genomeBuildName + ".");
				}
				laneNode.setAttribute("idGenomeBuildAlignTo", genomeBuild.getIdGenomeBuild().toString());
			} else {
				laneNode.setAttribute("idGenomeBuildAlignTo", "");
			}

			laneNode.setAttribute("idFlowCellChannel", "");
		}

	}

	private void mapAnnotations() throws Exception {

		// Lookup the properties based on the property name. Then use the target db's idProperty in the
		// XML for PropertyEntry.
		System.out.println("Setting annotation from the xml file in property entry list");
		for (Iterator i = requestNode.getChild("PropertyEntries").getChildren("PropertyEntry").iterator(); i.hasNext();) {
			Element propertyNode = (Element) i.next();


			// Only map the attributes that are selected. In other words, only deal with the annotations that are actually
			// present on the sample.
			if (!propertyNode.getAttributeValue("isSelected").equals("true")) {
				continue;
			}

			String propertyName = propertyNode.getAttributeValue("name");

			Integer idPropertySource = Integer.parseInt(propertyNode.getAttributeValue("idProperty"));

			System.out.println("idPropertySource: " + idPropertySource + " name: " + propertyName);

			Property prop = targetPropertyMap.get(propertyName.toUpperCase());
			if (prop == null) {
				System.out.println("property doesn't exist in targetPropertyMap: " + prop.getName());


				// we don't have this annotation, make it
				prop = new Property();

				// and copy over values from source
				prop.setName(propertyName);
				prop.setIsActive("Y");
				prop.setIsRequired("N");
				prop.setForSample("Y");
				prop.setForAnalysis("N");
				prop.setForDataTrack("N");
				prop.setForRequest("N");
				prop.setCodePropertyType("TEXT");
				// Note this assumes a single core for the target system and assumes that core has 1 as id. If we use this outside of UGP this may need to be
				// generalized.
				prop.setIdCoreFacility(1);

				// save it and flush it to assign the DB id
				sess.save(prop);
				sess.flush();

				// remember it
				targetPropertyMap.put(prop.getName().toUpperCase(), prop);
				targetIdToPropertyMap.put(prop.getIdProperty(), prop);

			}

			// keep track of incoming / current idProperty's
			idPropertyMap.put(idPropertySource, prop.getIdProperty());
			System.out.println("idPropertySource: " + idPropertySource + " idProperty: " + prop.getIdProperty());



			propertyNode.setAttribute("idProperty", prop.getIdProperty().toString());

			// Map to source idProperty to the target property so that we can change the 'ANNOT' attributes
			// according to the ids in the target gnomex db.
			sourceToTargetPropertyMap.put(prop.getIdProperty(), prop);
		}

		// For each sample, change the ANNOT### attributes to use the target db's idProperty. Also, for
		// single and multi-option annotations, use the target db's idPropertyOption as the value.
		for (Iterator i1 = requestNode.getChild("samples").getChildren("Sample").iterator(); i1.hasNext();) {
			Element sampleNode = (Element) i1.next();

			Element mysampleNode = (Element) sampleNode.clone();

			List<Attribute> theattributes = mysampleNode.getAttributes();
			for (Iterator<Attribute> i2 = theattributes.iterator(); i2.hasNext();) {
				Attribute a = (Attribute) i2.next();
				String attributeName = a.getName();

				String value = a.getValue();

				// Strip off "ANNOT" from attribute name
				Integer idPropertySource = null;
				if (attributeName.startsWith("ANNOT")) {
					idPropertySource = Integer.parseInt(attributeName.substring(5));
				} else {
					continue;
				}

				Integer mappedIdProperty = idPropertyMap.get(idPropertySource);

				Property prop = sourceToTargetPropertyMap.get(mappedIdProperty);
				if (prop == null) {
					throw new Exception("Cannot find target db property for " + attributeName);
				}

				// For single or multi-choice, lookup the idPropertyOption in the target db.
				String newValue = value;
				if (prop.getCodePropertyType().equals(PropertyType.MULTI_OPTION) || prop.getCodePropertyType().equals(PropertyType.OPTION)) {
					newValue = "";
					String[] valueTokens = value.split(",");
					// For multi-option attributes, we have a comma separated list of the options selected. We will just loop
					// through once in the single-option case.
					for (int x = 0; x < valueTokens.length; x++) {
						String valueToken = valueTokens[x];

						String sourceOptionName = sourcePropertyOptionMap.get(idPropertySource + "-" + valueToken);
						if (sourceOptionName == null) {
							sourceOptionName = sourcePropertyOptionMap.get(mappedIdProperty + "-" + valueToken);
							if (sourceOptionName == null) {
								throw new Exception("Cannot find source option with the idPropertyOption = " + valueToken + " for property " + attributeName);
							}
						}
						Integer targetIdPropertyOption = null;
						for (PropertyOption option : (Set<PropertyOption>) prop.getOptions()) {
							if (option.getOption().equals(sourceOptionName)) {
								targetIdPropertyOption = option.getIdPropertyOption();
								break;
							}
						}
						if (targetIdPropertyOption == null) {
							throw new Exception("Cannot find option " + sourceOptionName + " for property " + prop.getName());
						}

						if (newValue.length() > 0) {
							newValue += ",";
						}
						newValue += targetIdPropertyOption;

					}
				}

				// Add a new one with ANNOT + prop.getIdProperty()
				sampleNode.setAttribute("ANNOT" + prop.getIdProperty(), newValue);

				// Remove the current attribute
				sampleNode.removeAttribute(a);

			}
		}

	}

	private void parseLabName() {
		String labName = requestNode.getAttributeValue("labName");
		String[] tokens = labName.split(", ");
		labLastName = tokens[0];
		if (tokens.length > 1) {
			labFirstName = tokens[1];
			// Now strip off 'Lab' from first name.
			if (labFirstName.contains(" Lab")) {
				labFirstName = labFirstName.substring(0, labFirstName.lastIndexOf(" Lab"));
			}
		}
	}

	private void parseSubmitterName() {
		String submitterName = requestNode.getAttributeValue("submitterName");
		String tokens[] = submitterName.split(" ");
		submitterFirstName = tokens[0];
		if (tokens.length > 1) {
			submitterLastName = tokens[1];
		}
	}

	private Integer getIdAppUser(Lab lab) {
		Integer idAppUser = null;
		// See if the submitter name matches one of the lab members
		for (AppUser submitter : (Set<AppUser>) lab.getMembers()) {
			if (submitter.getLastName().equals(submitterLastName)) {
				if (submitterFirstName != null) {
					if (submitter.getFirstName() != null && submitter.getFirstName().equals(submitterFirstName)) {
						idAppUser = submitter.getIdAppUser();
					}
				} else {
					if (submitter.getFirstName() == null) {
						idAppUser = submitter.getIdAppUser();
					}
				}
			}
		}
		// Otherwise, see if the submitter name matches one of the lab managers
		if (idAppUser == null) {
			for (AppUser submitter : (Set<AppUser>) lab.getManagers()) {
				if (submitter.getLastName().equals(submitterLastName)) {
					if (submitterFirstName != null) {
						if (submitter.getFirstName() != null && submitter.getFirstName().equals(submitterFirstName)) {
							idAppUser = submitter.getIdAppUser();
						}
					} else {
						if (submitter.getFirstName() == null) {
							idAppUser = submitter.getIdAppUser();
						}
					}
				}
			}
		}
		// We can't find the matching name in members or managers, so just use the first
		// manager in the list. If there isn't a manager, use the first member.
		if (idAppUser == null) {
			if (!lab.getManagers().isEmpty()) {
				idAppUser = ((AppUser) lab.getManagers().iterator().next()).getIdAppUser();
			} else if (!lab.getMembers().isEmpty()) {
				idAppUser = ((AppUser) lab.getMembers().iterator().next()).getIdAppUser();
			}
		}
		return idAppUser;

	}

	private void saveRequest() throws Exception {

		sess.save(request);

		if (requestParser.isNewRequest()) {
			sess.save(request);

			if (request.getName() == null || request.getName().trim().equals("")) {
				sess.flush();
				sess.refresh(request);
				request.setName(request.getAppUser().getShortName() + "-" + request.getNumber());
				sess.save(request);
			}
		}

		sess.flush();
	}

	private void saveSamples(Integer updateIDRequest) throws Exception {
		//getStartingNextSampleNumber();

		if(updateMode){
			Request r = requestParser.getRequest();
			r.setIdRequest(updateIDRequest);
			r.setNumber(r.getIdRequest() + "R");

			Query q = sess.createQuery("SELECT s.idSample  FROM Sample s WHERE s.idRequest = :updateIDRequest")
					.setParameter("updateIDRequest", updateIDRequest);
			int length = q.list().size();
			nextSampleNumber = length + 1;
		}else{ // intial value is one if experiment doesn't already exist
			this.nextSampleNumber = 1;
		}

		boolean hasNewSample = false;
		for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
			String idSampleString = (String) i.next();
			boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
			hasNewSample = isNewSample || hasNewSample;
			Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);
			saveSample(idSampleString, sample, sess);
		}


	}

	private void saveSample(String idSampleString, Sample sample, Session sess) throws Exception {
		System.out.println("[saveSample] idSampleString: " + idSampleString);
		//getStartingNextSampleNumber();

		boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");



		if(isNewSample && (idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample") ) ){

			isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");

			nextSampleNumber = initSample(sess, requestParser.getRequest(), sample, isNewSample, nextSampleNumber);
			System.out.println("sample number: " + sample.getNumber());
			System.out.println("Sample Ids: " + requestParser.getSampleIds().toString());
		}

		sample.setIdRequest(requestParser.getRequest().getIdRequest());
		System.out.println("Sample details: " + " idRequest: " +  sample.getIdRequest() + " Name: " + sample.getName() + " Number: " + sample.getNumber() + " idSample " + sample.getIdSample() );


		sess.save(sample);

		SaveRequest.setSampleProperties(sess, requestParser.getRequest(), sample, isNewSample,
				(Map) requestParser.getSampleAnnotationMap().get(idSampleString), requestParser.getOtherCharacteristicLabel(), targetIdToPropertyMap);

		sess.flush();

		idSampleMap.put(idSampleString, sample.getIdSample());
		samples.add(sample);

		if (isNewSample) {
			samplesAdded.add(sample);
		}

		System.out.println("[saveSample] **leaving**");
	}

	private void getStartingNextSampleNumber() {
		nextSampleNumber = 0;

		//PriceCategory priceCategory = (PriceCategory)sess.load(PriceCategory.class, idPriceCategory);
		for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext();) {
			String idSampleString = (String) i.next();
			Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);
			String numberAsString = sample.getNumber();
			if (numberAsString != null && numberAsString.length() != 0 && numberAsString.indexOf("X") > 0) {
				numberAsString = numberAsString.substring(numberAsString.indexOf("X") + 1);
				try {
					Integer number = Integer.parseInt(numberAsString);
					if (number > nextSampleNumber) {
						nextSampleNumber = number;
					}
				} catch (Exception ex) {
				}
			}
		}
		nextSampleNumber++;
	}

	private void sendErrorReport(Exception e) {

		String msg = "Could not import experiment. " + e.toString() + "\n\t";

		StackTraceElement[] stack = e.getStackTrace();
		for (StackTraceElement s : stack) {
			msg = msg + s.toString() + "\n\t\t";
		}

		String errorMessageString = errorMessagePrefixString;
		if (!errorMessageString.equals("")) {
			errorMessageString += "\n";
		}
		errorMessageString += msg;

		System.err.println(errorMessageString);

	}

	protected boolean checkParms() {
		if (login == null || fileName == null || annotationFileName == null) {
			return false;
		} else {
			return true;
		}
	}

	protected Set saveRequestProperties(Element requestNode, Session sess, RequestParser requestParser, boolean saveToDB) throws org.jdom.JDOMException {
		// This makes the assumption that you know the idProperty of the property  and that it already exists you are making the entry for.
		Set<PropertyEntry> propertyEntries = new TreeSet<PropertyEntry>(new PropertyEntryComparator());
		List<Element> reqPropEntries = requestNode.getChild("RequestProperties").getChildren();
		System.out.println("This is the requestProperties: "  + reqPropEntries.toString());
		System.out.println("Length of ReqPropertiesEntries: " + reqPropEntries.size());

		if (reqPropEntries != null) {
			if (saveToDB) {

				for (Iterator<?> i = reqPropEntries.iterator(); i.hasNext();) {
					Element node = (Element) i.next();
					// Adding dataTracks
					String idPropertyEntry = node.getAttributeValue("idPropertyEntry");

					PropertyEntry pe = null;
					if (idPropertyEntry == null || idPropertyEntry.equals("")) {
						pe = new PropertyEntry();
					}
					pe.setIdProperty(Integer.valueOf(node.getAttributeValue("idProperty")));
					pe.setValue(node.getAttributeValue("value"));
					pe.setIdRequest(requestParser.getRequest().getIdRequest());

					if ((idPropertyEntry == null || idPropertyEntry.equals("")) && saveToDB) {
						System.out.println("PropertyEntry was saved");
						sess.save(pe);
						sess.flush();
					}


					propertyEntries.add(pe);
				}



			}

		}

		return propertyEntries;
	}


	protected void printUsage() {

		System.out.println("Import an experiment in an xml file to a gnomex db.");
		System.out.println("Command line args");
		System.out.println("   -login           GNomEx uid for running import>");
		System.out.println("   -file            XML file to be imported, generated from GetRequest.gx.");
		System.out.println("   -annotationFile  XML file to be imported, generated from GetPropertyList.gx.");
		System.out.println("   -[isExternal     Y/N]");
		System.out.println("   -help - gives this message.  Note no other processing is performed if the -help switch is specified.");

	}

}
/*class MyReturnWork implements ReturningWork<ResultSet> {
	private Integer idRequest;
	private Set<String> sampleNames;
	private Map<String, List<Integer>> duplicateSamples;

	public MyReturnWork(Integer idRequest, Set<String> sampleNames) {
		this.idRequest = idRequest;
		this.sampleNames = sampleNames;
		t
	}

	@Override
	public ResultSet execute(Connection conn) throws SQLException {

		PreparedStatement preStmt = null;
		preStmt = conn.prepareStatement("SELECT idSample  FROM gnomex.sample WHERE idRequest = ? AND name = ? ");
		for(int i = 0; i <  this.sampleNames.size(); i++){
			String sampleName = sampleNames.get(i);
			preStmt.setInt(1, this.idRequest);
			preStmt.setString(2, sampleName);
			ResultSet rs = preStmt.executeQuery();
			if(rs.next()){
				 throw new SQLException("duplicate sample entry");
			}else{

			}

		}




		return preStmt.executeQuery();

	}

}*/
