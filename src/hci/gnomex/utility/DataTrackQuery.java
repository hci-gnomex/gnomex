package hci.gnomex.utility;

import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Segment;
import hci.gnomex.model.UnloadDataTrack;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Query;
import org.hibernate.Session;


public class DataTrackQuery implements Serializable {

  
	// Criteria
	private String             scopeLevel = "";
	private Integer            idLab;
	private Integer            idOrganism;
	private Integer            idGenomeBuild;
	private String             isVisibilityPublic = "Y";
  private String             isVisibilityOwner = "Y";
	private String             isVisibilityMembers = "Y";
	private String             isVisibilityInstitute = "Y";
	private String             isServerRefreshMode = "N";

    
	private StringBuffer       queryBuf;
	private boolean            addWhere = true;
	
	private static String      KEY_DELIM = "!!!!";
	
	private static final int                         FOLDER_LEVEL = 1;
	private static final int                         DATATRACK_LEVEL = 2;
	
	
  private TreeMap<String, TreeMap<GenomeBuild, ?>> organismToVersion;
  private HashMap<String, TreeMap<String, ?>>      versionToRootFolders;
  private HashMap<String, TreeMap<String, ?>>      folderToFolders;
  private HashMap<String, TreeMap<String, ?>>      folderToDataTracks;

  private HashMap<String, List<Segment>>           versionToSegments;

  private HashMap<String, Organism>                organismMap;
  private HashMap<String, GenomeBuild>             genomeBuildNameMap;
  private HashMap<Integer, DataTrack>              dataTrackMap;
  private HashMap<Integer, DataTrackFolder>        dataTrackFolderMap;

	@SuppressWarnings("unchecked")
	public static List<UnloadDataTrack> getUnloadedDataTracks(Session sess, SecurityAdvisor secAdvisor, GenomeBuild genomeBuild) throws Exception {
		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append(" SELECT     u  ");
		queryBuf.append(" FROM       UnloadDataTrack as u  ");
		queryBuf.append(" WHERE      u.idGenomeBuild = " + genomeBuild.getIdGenomeBuild());
		
		if (secAdvisor != null && !secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
			queryBuf.append(" AND u.idAppUser = " + secAdvisor.getIdAppUser());		
		}

		queryBuf.append(" ORDER BY   u.idUnloadDataTrack");
		
		List<UnloadDataTrack> results = (List<UnloadDataTrack>)sess.createQuery(queryBuf.toString()).list();
		
		return results;
	}
	
	public DataTrackQuery() {
		if (scopeLevel == null || scopeLevel.equals("")) {
			scopeLevel = SecurityAdvisor.ALL_SCOPE_LEVEL;
		}		
	}
	
	public DataTrackQuery(HttpServletRequest req) {
		scopeLevel         = req.getParameter("scopeLevel");
		idLab        = DataTrackUtil.getIntegerParameter(req, "idLab");
		idOrganism         = DataTrackUtil.getIntegerParameter(req, "idOrganism");
		idGenomeBuild    = DataTrackUtil.getIntegerParameter(req, "idGenomeBuild");
    this.isVisibilityOwner = DataTrackUtil.getFlagParameter(req, "isVisibilityOwner");
		this.isVisibilityMembers = DataTrackUtil.getFlagParameter(req, "isVisibilityMembers");
		this.isVisibilityInstitute = DataTrackUtil.getFlagParameter(req, "isVisibilityInstitute");
		this.isVisibilityPublic = DataTrackUtil.getFlagParameter(req, "isVisibilityPublic");
		
		if (scopeLevel == null || scopeLevel.equals("")) {
			scopeLevel = SecurityAdvisor.ALL_SCOPE_LEVEL;
		}		
	}

	@SuppressWarnings("unchecked")
	public Document getDataTrackDocument(Session sess, SecurityAdvisor secAdvisor) throws Exception {
	  // Run query to get dataTrack folders, organized under
	  // organism and genome version
	  StringBuffer queryBuf = this.getDataTrackFolderQuery(secAdvisor);    	
	  Logger.getLogger(this.getClass().getName()).fine("DataTrack folder query: " + queryBuf.toString());
	  Query query = sess.createQuery(queryBuf.toString());
	  List<Object[]> dataTrackFolderRows = (List<Object[]>)query.list();

	  // Run query to get dataTrack folder and dataTracks, organized under
	  // organism and genome version
	  queryBuf = this.getDataTrackQuery(secAdvisor);
	  Logger.getLogger(this.getClass().getName()).fine("DataTrack query: " + queryBuf.toString());
	  query = sess.createQuery(queryBuf.toString());
	  List<Object[]> dataTrackRows = (List<Object[]>)query.list();

	  // Now run query to get the genome version segments
	  queryBuf = this.getSegmentQuery();
	  query = sess.createQuery(queryBuf.toString());
	  List<Segment> segmentRows = (List<Segment>) query.list();

	  // Create an XML document
	  Document doc = this.getDataTrackDocument(dataTrackFolderRows, dataTrackRows, segmentRows, DictionaryHelper.getInstance(sess), secAdvisor);
	  return doc;
		
	}
	

	@SuppressWarnings("unchecked")
	public void runDataTrackQuery(Session sess, SecurityAdvisor secAdvisor, boolean isServerRefreshMode) throws Exception {
		this.isServerRefreshMode = isServerRefreshMode ? "Y" : "N";
		
		// Run query to get dataTrack folders, organized under
		// organism and genome version
		StringBuffer queryBuf = this.getDataTrackFolderQuery(secAdvisor);    	
		Logger.getLogger(this.getClass().getName()).fine("DataTrack folder query: " + queryBuf.toString());
    	Query query = sess.createQuery(queryBuf.toString());
		List<Object[]> dataTrackFolderRows = (List<Object[]>)query.list();
		
		// Run query to get dataTracks, organized under dataTrack folder,
		// organism, and genome version
		queryBuf = this.getDataTrackQuery(secAdvisor);    	
		Logger.getLogger(this.getClass().getName()).fine("DataTrack query: " + queryBuf.toString());
    	query = sess.createQuery(queryBuf.toString());
		List<Object[]> dataTrackRows = (List<Object[]>)query.list();
		
		// Now run query to get the genome version segments
		queryBuf = this.getSegmentQuery();			
    	query = sess.createQuery(queryBuf.toString());
		List<Segment> segmentRows = (List<Segment>)query.list();
			
		this.hashDataTracks(dataTrackFolderRows, dataTrackRows, segmentRows, DictionaryHelper.getInstance(sess));
		
	}
	
	private StringBuffer getDataTrackFolderQuery(SecurityAdvisor secAdvisor) throws Exception {
		
		addWhere = true;
		queryBuf = new StringBuffer();

		queryBuf.append(" SELECT     org, ");
		queryBuf.append("            gb, ");
		queryBuf.append("            folder, ");
		queryBuf.append("            parentFolder ");
		queryBuf.append(" FROM       Organism as org ");
		queryBuf.append(" JOIN       org.genomeBuilds as gb ");
		queryBuf.append(" JOIN       gb.dataTrackFolders as folder ");
		queryBuf.append(" LEFT JOIN  folder.parentFolder as parentFolder ");
		

		addWhere = true;

		addCriteria(FOLDER_LEVEL);
		
		queryBuf.append(" ORDER BY org.das2Name asc, gb.buildDate desc, folder.name asc ");

		return queryBuf;

	}

	private StringBuffer getDataTrackQuery(SecurityAdvisor secAdvisor) throws Exception {
		
		addWhere = true;
		queryBuf = new StringBuffer();

		queryBuf.append(" SELECT     org, ");
		queryBuf.append("            gb, ");
		queryBuf.append("            folder, ");
		queryBuf.append("            parentFolder, ");
		queryBuf.append("            dataTrack  ");
		queryBuf.append(" FROM       Organism as org ");
		queryBuf.append(" JOIN       org.genomeBuilds as gb ");
		queryBuf.append(" JOIN       gb.dataTrackFolders as folder ");
		queryBuf.append(" LEFT JOIN  folder.parentFolder as parentFolder ");
		queryBuf.append(" LEFT JOIN  folder.dataTracks as dataTrack ");
		queryBuf.append(" LEFT JOIN  dataTrack.collaborators as collab ");
		

		addWhere = true;

		addCriteria(DATATRACK_LEVEL);
		
		if (secAdvisor != null) {
		  addWhere = secAdvisor.buildSecurityCriteria(queryBuf, "dataTrack", "collab", addWhere, false);
		}
		
		
		// If this is a server reload, get dataTracks not yet loaded
		if (this.isServerRefreshMode.equals("Y")) {
			this.AND();
			queryBuf.append("(");
			queryBuf.append(" dataTrack.isLoaded = 'N' ");

			if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
				this.AND();
				queryBuf.append(" dataTrack.idAppUser = " + secAdvisor.getIdAppUser());		
			}

			queryBuf.append(")");
		}

		queryBuf.append(" ORDER BY org.das2Name asc, gb.buildDate desc, folder.name asc, dataTrack.name asc ");

		return queryBuf;

	}


	
	private StringBuffer getSegmentQuery() throws Exception {
		addWhere = true;
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT     seg  ");
		queryBuf.append(" FROM       Segment as seg  ");
		queryBuf.append(" ORDER BY   seg.sortOrder");
		
		return queryBuf;

	}

	private Document getDataTrackDocument(List<Object[]> dataTrackFolderRows, List<Object[]> dataTrackRows, List<Segment> segmentRows,  DictionaryHelper dictionaryHelper, SecurityAdvisor secAdvisor) throws Exception {
		
		// Organize results rows into hash tables
		hashDataTracks(dataTrackFolderRows, dataTrackRows, segmentRows, dictionaryHelper);		
		

		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("DataTracks");
		
				
		// Use hash to create XML Document.  Perform 2 passes so that organisms
		// with populated genome versions (dataTracks or sequences) appear first
		// in the list.

		fillOrganismNodes(root, dictionaryHelper, secAdvisor, true);
		fillOrganismNodes(root, dictionaryHelper, secAdvisor, false);

		return doc;
		
	}
	
	private void fillOrganismNodes(Element root, DictionaryHelper dictionaryHelper, SecurityAdvisor secAdvisor, boolean fillPopulatedOrganisms) throws Exception {
		Element organismNode = null;
		Element versionNode  = null;
		String[] tokens;
		for (String organismBinomialName : organismToVersion.keySet()) {
			TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organismBinomialName);
			Organism organism = organismMap.get(organismBinomialName);
			
			boolean keep = false;
			boolean isPopulated = hasPopulatedGenomes(organism);
			if (fillPopulatedOrganisms) {
				keep = isPopulated;
			} else {
				keep = !isPopulated;
			}
			
			if (!keep) {
				continue;
			}
			
			organismNode = organism.getXML(secAdvisor).getRootElement();
			organismNode.addAttribute("isPopulated", isPopulated ? "Y" : "N");
			root.add(organismNode);
			
			// For each version, build up hierarchy
			if (versionMap != null) {
				for (GenomeBuild genomeBuild : versionMap.keySet()) {
					
					versionNode = genomeBuild.getXML(secAdvisor, null).getRootElement();
					
					// Indicate if the genome version has segments
					List<Segment> segments = this.getSegments(organism, genomeBuild.getDas2Name());
					versionNode.addAttribute("hasSegments", (segments != null && segments.size() > 0 ? "Y" : "N"));					
					
					// Attach the genome version node
					organismNode.add(versionNode);
					
					// For each root dataTrack folder, recurse to create dataTracks
					// and folders.
					TreeMap<String, ?> rootFolders = versionToRootFolders.get(genomeBuild.getDas2Name());
					fillFolderNode(genomeBuild, versionNode, rootFolders, secAdvisor, dictionaryHelper, false);
					
					// If selection criteria was applied to query, prune out nodes that don't 
					// have any content 
					if (this.hasDataTrackCriteria()) {
						if (!versionNode.hasContent()) {
							organismNode.remove(versionNode);					
						}
					}

				}
				
			}
			
			// If selection criteria was applied to query, prune out nodes that don't 
			// have any content 
			if (this.hasDataTrackCriteria()) {
				if (!organismNode.hasContent()) {
					root.remove(organismNode);					
				}
			}
			
		}
		
	}
	
	private boolean hasPopulatedGenomes(Organism organism) {
		boolean isPopulated = false;
		
		TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
		if (versionMap != null) {
			for (GenomeBuild genomeBuild : versionMap.keySet()) {
				if (isPopulated) {
					break;
				}
				List<Segment> segments = this.getSegments(organism, genomeBuild.getDas2Name());
				if (segments != null && segments.size() > 0) {
					isPopulated = true;
					break;
				}
				TreeMap<String, ?> rootFolders = versionToRootFolders.get(genomeBuild.getDas2Name());
				if (rootFolders != null && rootFolders.size() > 0) {
					for (String folderKey : rootFolders.keySet()) {
						
						//String[] tokens     = folderKey.split(KEY_DELIM);
						//String folderName          = tokens[0];
						

						TreeMap<String, ?> dtNameMap = folderToDataTracks.get(folderKey);
						if (dtNameMap != null && dtNameMap.size() > 0) {
							isPopulated = true;
						}
						
						TreeMap<String, ?> childFolders = folderToFolders.get(folderKey);
						if (childFolders != null && childFolders.size() > 0) {
							isPopulated = true;
						}
					}
				}
				
				
			}
		}
		return isPopulated;
	}
	

	
	private void hashDataTracks(List<Object[]> dataTrackFolderRows, List<Object[]> dataTrackRows, List<Segment> segmentRows, DictionaryHelper dictionaryHelper) {

		organismToVersion        = new TreeMap<String, TreeMap<GenomeBuild, ?>>();
		versionToRootFolders   = new HashMap<String, TreeMap<String, ?>>();
		folderToFolders      = new HashMap<String, TreeMap<String, ?>>();
		folderToDataTracks    = new HashMap<String, TreeMap<String, ?>>();
		versionToSegments        = new HashMap<String, List<Segment>>();
		
		organismMap              = new HashMap<String, Organism>();
		genomeBuildNameMap     = new HashMap<String, GenomeBuild>();
		dataTrackFolderMap    = new HashMap<Integer, DataTrackFolder>();
		dataTrackMap            = new HashMap<Integer, DataTrack>();
		
		// Prefill organism, genome version, and root dataTrack folder
		// hash map with known entries
		// since those without dataTracks would otherwise not show up.
		for (Organism o : dictionaryHelper.getOrganisms()) {
		  // Skip organisms that don't have a binomial name or das2 name
		  if (o.getBinomialName() == null || o.getBinomialName().equals("") ||
		      o.getDas2Name() == null || o.getDas2Name().equals("")) {
		    continue;
		  }
		  
			organismMap.put(o.getBinomialName(), o);

			// If we are filtering by organism, only include that one
			if (this.idOrganism != null) {
				if (!this.idOrganism.equals(o.getIdOrganism())) {
					continue;
				}
			}
			TreeMap<GenomeBuild, ?> versionMap = new TreeMap<GenomeBuild, Object>(new GenomeBuildComparator());
			organismToVersion.put(o.getBinomialName(), versionMap);
			if (dictionaryHelper.getGenomeBuilds(o.getIdOrganism()) != null) {
				for(GenomeBuild v : dictionaryHelper.getGenomeBuilds(o.getIdOrganism())) {

				  // Skip genome builds that don't have a das2 name
				  if (v.getDas2Name() == null || v.getDas2Name().equals("")) {
				    continue;
				  }
					genomeBuildNameMap.put(v.getDas2Name(), v);

					// If we are filtering by genome version, only include that one
					if (this.idGenomeBuild != null) {
						if (!this.idGenomeBuild.equals(v.getIdGenomeBuild())) {
							continue;
						}
					}

					versionMap.put(v, null);

					DataTrackFolder rootFolder = v.getRootDataTrackFolder();

					if (rootFolder != null) {
						String folderKey       = rootFolder.getName()  + KEY_DELIM + rootFolder.getIdDataTrackFolder();
						TreeMap<String, String> folders = new TreeMap<String, String>();
						folders.put(folderKey, null);
						versionToRootFolders.put(v.getDas2Name(), folders);
					}

				}

			}
		}
		// Hash segments for each genome version
		if (segmentRows != null) {
			for (Segment segment : segmentRows) {
				if (segment == null) {
					continue;
				}
				GenomeBuild genomeBuild = dictionaryHelper.getGenomeBuildObject(segment.getIdGenomeBuild());
				if (genomeBuild == null) {
					System.out.println("Warning - Segment " + segment.getIdSegment() + " does not belong to a valid Genome Version");
					continue;
				}
				List<Segment> segments = versionToSegments.get(genomeBuild.getDas2Name());
				if (segments == null) {
					segments = new ArrayList<Segment>();
					versionToSegments.put(genomeBuild.getDas2Name(), segments);
				}
				segments.add(segment);
			}			
		}

		
		


		
		
		// Hash to create hierarchy:
		//   Organism
		//     Genome Version
		//       DataTrack
		//       DataTrack Folder
		//          DataTrack

		// Hash the dataTrack folder->dataTrack.  We get
		// a row for each dataTrack folder.  
		// 1. Hash the genome versions under the organism.
		// 2. Hash the root dataTrack folders under the organism.
		//    (root dataTracks are under the root dataTrack folder for
		//     the genome version.  We just hide this dataTrack folder
		//     and show the dataTracks under the genome version node instead.
		// 3. Hash the dataTrack folders under parent dataTrack folder
		//    and the dataTracks under the parent dataTrack folder.
		
		// First hash the dataTrack folders
		for (Object[] row : dataTrackFolderRows) {
			Organism organism                      = (Organism) row[0];
			GenomeBuild genomeBuild            = (GenomeBuild)  row[1];
			DataTrackFolder dtFolder       = (DataTrackFolder) row[2];
			DataTrackFolder parentAnnotFolder = (DataTrackFolder) row[3];
			
			// Hash genome versions for an organism
			TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
			if (versionMap == null) {
				versionMap = new TreeMap<GenomeBuild, Object>(new GenomeBuildComparator());
				organismToVersion.put(organism.getBinomialName(), versionMap);
				organismMap.put(organism.getBinomialName(), organism);
			}
			if (genomeBuild != null && genomeBuild.getDas2Name() != null && genomeBuild.getDas2Name().equals("")) {
				versionMap.put(genomeBuild, null);
				genomeBuildNameMap.put(genomeBuild.getDas2Name(), genomeBuild);
			}

			
			// Hash root dataTrack folders for a genome version
			String folderKey       = dtFolder.getName()  + KEY_DELIM + dtFolder.getIdDataTrackFolder();
			if (parentAnnotFolder == null) {

				TreeMap<String, ?> folderNameMap = versionToRootFolders.get(genomeBuild.getDas2Name());
				if (folderNameMap == null) {
					folderNameMap = new TreeMap<String, String>();
					versionToRootFolders.put(genomeBuild.getDas2Name(), folderNameMap);
				}
				folderNameMap.put(folderKey, null);				
			} else {
				String parentFolderKey = parentAnnotFolder.getName() + KEY_DELIM + parentAnnotFolder.getIdDataTrackFolder();

				// Hash dataTrack folder for a parent dataTrack folder
				TreeMap<String, ?> childFolderNameMap = folderToFolders.get(parentFolderKey);
				if (childFolderNameMap == null) {
					childFolderNameMap = new TreeMap<String, String>();
					folderToFolders.put(parentFolderKey, childFolderNameMap);
				}
				childFolderNameMap.put(folderKey, null);				
			}
			dataTrackFolderMap.put(dtFolder.getIdDataTrackFolder(), dtFolder);				
		}
		
		// Now hash the dataTrack rows
		for (Object[] row : dataTrackRows) {
			Organism organism                      = (Organism) row[0];
			GenomeBuild genomeBuild            = (GenomeBuild)  row[1];
			DataTrackFolder dtFolder       = (DataTrackFolder) row[2];
			DataTrackFolder parentAnnotFolder = (DataTrackFolder) row[3];
			DataTrack dt                       = (DataTrack) row[4];
			
			// Load properties for dataTracks
			if (dt != null) {
				dt.loadProps(dictionaryHelper);				
			}
			
			// Hash genome versions for an organism
			TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
			if (versionMap == null) {
				versionMap = new TreeMap<GenomeBuild, Object>(new GenomeBuildComparator());
				organismToVersion.put(organism.getBinomialName(), versionMap);
			}
			if (genomeBuild != null) {
				versionMap.put(genomeBuild, null);
			}
			
			if (dtFolder != null) {
				String folderKey       = dtFolder.getName()  + KEY_DELIM + dtFolder.getIdDataTrackFolder();
				// Hash root dataTrack folders for a genome version
				if (parentAnnotFolder == null) {

					TreeMap<String, ?> folderNameMap = versionToRootFolders.get(genomeBuild.getDas2Name());
					if (folderNameMap == null) {
						folderNameMap = new TreeMap<String, String>();
						versionToRootFolders.put(genomeBuild.getDas2Name(), folderNameMap);
					}
					folderNameMap.put(folderKey, null);				
				} else {
					String parentFolderKey = parentAnnotFolder.getName() + KEY_DELIM + parentAnnotFolder.getIdDataTrackFolder();
					
					// Hash dataTrack folder for a parent dataTrack folder
					TreeMap<String, ?> childFolderNameMap = folderToFolders.get(parentFolderKey);
					if (childFolderNameMap == null) {
						childFolderNameMap = new TreeMap<String, String>();
						folderToFolders.put(parentFolderKey, childFolderNameMap);
					}
					childFolderNameMap.put(folderKey, null);				
				}
				dataTrackFolderMap.put(dtFolder.getIdDataTrackFolder(), dtFolder);				

				// Hash dataTracks for an dataTrack folder
				if (dt != null) {
					TreeMap<String, ?> dtNameMap = folderToDataTracks.get(folderKey);
					if (dtNameMap == null) {
						dtNameMap = new TreeMap<String, String>();
						folderToDataTracks.put(folderKey, dtNameMap);
					}
					String dtKey       = dt.getName()  + KEY_DELIM + dt.getIdDataTrack();
					dtNameMap.put(dtKey, null);
					dataTrackMap.put(dt.getIdDataTrack(), dt);
				}			
			}
		}
		
	}
	
	public GenomeBuild getGenomeBuild(String genomeBuildName) {
		return genomeBuildNameMap.get(genomeBuildName);
	}
	
	public HashMap<String, GenomeBuild> getGenomeBuildNameMap() {
		return genomeBuildNameMap;
	}
	

	public Set<Organism> getOrganisms() {
		TreeSet<Organism> organisms = new TreeSet<Organism>(new OrganismComparator());
		for(String organismBinomialName: organismToVersion.keySet()) {
			Organism organism = organismMap.get(organismBinomialName);
			organisms.add(organism);
		}
		return organisms;
	}
	
	public Set<String> getVersionNames(Organism organism) {
		Set<String> versionNames = new TreeSet<String>();
		
		TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
		if (versionMap != null) {
			for(GenomeBuild version : versionMap.keySet()) {
				versionNames.add(version.getDas2Name());
			}
		}
		
		return versionNames;
	}
	
	public List<Segment> getSegments(Organism organism, String genomeBuildName) {
		List<Segment> segments = null;
		TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
			
		// For each version...
		if (versionMap != null) {
			for (GenomeBuild genomeBuild: versionMap.keySet()) {
				
				if (genomeBuild.getDas2Name().equals(genomeBuildName)) {
					segments = versionToSegments.get(genomeBuild.getDas2Name());
					break;
				}
			}
		}
		return segments;		
	}
	
	public List<QualifiedDataTrack> getQualifiedDataTracks(Organism organism, String genomeBuildName) {
		List<QualifiedDataTrack> qualifiedDataTracks = new ArrayList<QualifiedDataTrack>();
		
				
		TreeMap<GenomeBuild, ?> versionMap = organismToVersion.get(organism.getBinomialName());
			
		// For each version...
		if (versionMap != null) {
			for (GenomeBuild genomeBuild : versionMap.keySet()) {
				
				if (genomeBuild.getDas2Name().equals(genomeBuildName)) {
					// For each root dataTrack folder, recurse dataTrack folder
					// hierarchy to get leaf dataTracks.
					TreeMap<String, ?> rootFolderNameMap = versionToRootFolders.get(genomeBuild.getDas2Name());
					String qualifiedName = "";
					getQualifiedDataTrack(rootFolderNameMap, qualifiedDataTracks, qualifiedName, false);
					
				}
				
			}
			
		}
		return qualifiedDataTracks;		
	}
	
	private void getQualifiedDataTrack(TreeMap<String, ?> theFolders, List<QualifiedDataTrack> qualifiedDataTracks, String typePrefix, boolean showFolderLevel) {
		if (theFolders != null) {
			for (String folderKey : theFolders.keySet()) {
				String[] tokens     = folderKey.split(KEY_DELIM);
				String folderName          = tokens[0];
				
				// For each dataTrack....
				TreeMap<String, ?> dtNameMap = folderToDataTracks.get(folderKey);
				if (dtNameMap != null) {
					// For each dataTrack...
					for (String dtNameKey : dtNameMap.keySet()) { 
						String[] tokens1    = dtNameKey.split(KEY_DELIM);
						Integer idDataTrack = new Integer(tokens1[1]);
													
						DataTrack dt = dataTrackMap.get(idDataTrack);
						
						
						String fullTypePrefix = concatenateTypePrefix(typePrefix, folderName, showFolderLevel);
						if (fullTypePrefix != null && fullTypePrefix.length() > 0) {
							fullTypePrefix += "/";
						}
						
						qualifiedDataTracks.add(new QualifiedDataTrack(dt, 
								fullTypePrefix + dt.getName(), 
								fullTypePrefix + dt.getName() ));
					}						
				}
											
				// Recurse for each dataTrack folder (under a folder)
				TreeMap<String, ?> childFolders = folderToFolders.get(folderKey);
				if (childFolders != null) {
					getQualifiedDataTrack(childFolders, qualifiedDataTracks, concatenateTypePrefix(typePrefix, folderName, showFolderLevel), true);					
				}
			}					
		}
	}
	
	private String concatenateTypePrefix(String typePrefix, String folderName, boolean showFolderLevel) {
		if (showFolderLevel) {
			if (typePrefix == null || typePrefix.equals("")) {
				return folderName;
			} else {
				return typePrefix + "/" + folderName;
			}
		} else {
			return typePrefix != null ? typePrefix : "";
		}
	}


	
	private void fillFolderNode(GenomeBuild genomeBuild, Element parentNode, TreeMap<String, ?> theFolders, SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper, boolean showFolderLevel) throws Exception {
		if (theFolders != null) {
			for (String folderKey : theFolders.keySet()) {
				String[] tokens     = folderKey.split(KEY_DELIM);
				String folderName          = tokens[0];
				Integer idDataTrackFolder = new Integer(tokens[1]);
				
				Element folderNode = null;
				DataTrackFolder dtFolder = null;
				if (showFolderLevel) {
					dtFolder = dataTrackFolderMap.get(idDataTrackFolder);
					
				    folderNode = dtFolder.getXML(secAdvisor, dictionaryHelper).getRootElement();
				    parentNode.add(folderNode);
					

				} else {
					folderNode = parentNode;					
				}
				
				
				// For each dataTrack
				TreeMap<String, ?> dtNameMap = folderToDataTracks.get(folderKey);
				if (dtNameMap != null && dtNameMap.size() > 0) {
					folderNode.addAttribute("dataTrackCount", String.valueOf(dtNameMap.size()));
					// For each dataTrack...
					for (String dtNameKey : dtNameMap.keySet()) { 
						String[] tokens1    = dtNameKey.split(KEY_DELIM);
						Integer idDataTrack = Integer.valueOf(tokens1[1]);
						
						DataTrack dt = dataTrackMap.get(idDataTrack);
						
						Element dtNode = dt.getXML(secAdvisor, dictionaryHelper, null).getRootElement();
						dtNode.addAttribute("idDataTrackFolder", dtFolder != null ? dtFolder.getIdDataTrackFolder().toString() : "");	
						
						folderNode.add(dtNode);

					}						
				}
											
				// Recurse for each dataTrack folder (under a folder)
				TreeMap<String, ?> childFolders = folderToFolders.get(folderKey);
				fillFolderNode(genomeBuild, folderNode, childFolders, secAdvisor, dictionaryHelper, true);
				
				// Prune out other group's folders that don't have
				// any content (no authorized dataTracks in its folder
				// or its descendent's folder).
				if (!folderNode.hasContent()) {
					String folderIdUserGroup = folderNode.attributeValue("idLab");
					boolean prune = true;
					// Public folders
					if (folderNode.getName().equals("DataTrackFolder") &&
						(folderIdUserGroup == null || folderIdUserGroup.equals(""))) {
						// If we are not filtering by user group,
						// always show empty public empty folders
						if (this.idLab == null) {
							prune = false;
						} else {
							// If we are filtering by user group, prune
							// empty public folders.
							prune = true;
						}
					}
					// User group folders
					else if (folderNode.getName().equals("DataTrackFolder") &&
							    folderIdUserGroup != null && 
							    !folderIdUserGroup.equals("") &&
							  	(secAdvisor.isGroupIAmMemberOrManagerOf(new Integer(folderIdUserGroup)))) {
						if (this.idLab == null) {
							// If we are not filtering by user group, then always
							// show my group's empty folders
							prune = false;							
						} else {
							// If we are filtering by user group, prune any empty
							// folders not specifically for the group being filtered
							if (this.idLab.equals(new Integer(folderIdUserGroup))) {
								prune = false;								
							} else {
								prune = true;
							}
							
						}
					}	else {
						// Prune empty folders for other groups
						prune = true;
					}
					if (prune) {
						parentNode.remove(folderNode);					
					}
				}
			}					
		}

	}

   
	

    private boolean hasDataTrackCriteria() {
    	if (idLab != null ||
    	    hasVisibilityCriteria()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    
    private boolean hasVisibilityCriteria() {
    	if (this.isVisibilityOwner.equals("Y") && this.isVisibilityMembers.equals("Y") && this.isVisibilityInstitute.equals("Y") && this.isVisibilityPublic.equals("Y")) {
    		return false;
    	} else if (this.isVisibilityOwner.equals("N") && this.isVisibilityMembers.equals("N") && this.isVisibilityInstitute.equals("N") && this.isVisibilityPublic.equals("N")) {
    		return false;
    	} else {
    		return true;
    	}
    }
  

	private void addCriteria(int joinLevel) {
	  
	  this.AND();
	  queryBuf.append(" org.binomialName is not NULL");
	  this.AND();
	  queryBuf.append(" org.das2Name is not NULL");
    

		// Search by organism
		if (idOrganism != null) {
			this.AND();
			queryBuf.append(" org.idOrganism = ");
			queryBuf.append(idOrganism);
		}
		// Search by genome version
		if (idGenomeBuild != null) {
			this.AND();
			queryBuf.append(" ver.idGenomeBuild = ");
			queryBuf.append(idGenomeBuild);
		}
		// Search for dataTracks and dataTrack groups for a particular group
		if (idLab != null) {
			this.AND();
			queryBuf.append("(");
			if (joinLevel == DATATRACK_LEVEL) {
				queryBuf.append(" a.idLab = " + this.idLab);
			} else if (joinLevel == FOLDER_LEVEL) {
				queryBuf.append(" ag.idLab = " + this.idLab);
				queryBuf.append(" OR ");
				queryBuf.append(" ag.idLab is NULL");
			}
			queryBuf.append(")");
		}
		// Filter by dataTrack's visibility
		if (joinLevel == DATATRACK_LEVEL) {
			if (hasVisibilityCriteria()) {
				this.AND();
				int count = 0;
				queryBuf.append(" a.codeVisibility in (");
				if (this.isVisibilityOwner.equals("Y")) {
					queryBuf.append("'" + Visibility.VISIBLE_TO_OWNER + "'");
					count++;
				}
				if (this.isVisibilityMembers.equals("Y")) {
          if (count > 0) {
            queryBuf.append(", ");
          }
          queryBuf.append("'" + Visibility.VISIBLE_TO_GROUP_MEMBERS + "'");
          count++;
        }
				if (this.isVisibilityInstitute.equals("Y")) {
					if (count > 0) {
						queryBuf.append(", ");
					}
					queryBuf.append("'" + Visibility.VISIBLE_TO_INSTITUTION_MEMBERS + "'");
					count++;
				}
				if (this.isVisibilityPublic.equals("Y")) {
					if (count > 0) {
						queryBuf.append(", ");
					}
					queryBuf.append("'" + Visibility.VISIBLE_TO_PUBLIC + "'");
					count++;
				}
				queryBuf.append(")");
			}
			
		}
		
		


	}

  

  
	protected boolean AND() {
		if (addWhere) {
			queryBuf.append(" WHERE ");
			addWhere = false;
		} else {
			queryBuf.append(" AND ");
		}
		return addWhere;
	}

	public String getIsVisibilityPublic() {
    	return isVisibilityPublic;
    }

	public void setIsVisibilityPublic(String isVisibilityPublic) {
    	this.isVisibilityPublic = isVisibilityPublic;
    }

	public String getIsVisibilityMembers() {
    	return isVisibilityMembers;
    }

	public void setIsVisibilityMembers(String isVisibilityMembers) {
    	this.isVisibilityMembers = isVisibilityMembers;
    }

	public String getIsVisibilityInstitute() {
    	return isVisibilityInstitute;
    }

	public void setIsVisibilityInstitute(String isVisibilityInstitute) {
    	this.isVisibilityInstitute = isVisibilityInstitute;
   }

	public String getIsVisibilityOwner() {
    return isVisibilityOwner;
  }

	public void setIsVisibilityOwner(String isVisibilityOwner) {
    this.isVisibilityOwner = isVisibilityOwner;
  }  
}
