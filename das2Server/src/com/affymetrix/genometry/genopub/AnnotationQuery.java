// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.hibernate.Query;
import java.util.logging.Logger;
import org.dom4j.Document;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;

public class AnnotationQuery
{
    private String scopeLevel;
    private Integer idUserGroup;
    private Integer idOrganism;
    private Integer idGenomeVersion;
    private String isVisibilityPublic;
    private String isVisibilityOwner;
    private String isVisibilityMembers;
    private String isVisibilityInstitute;
    private String isServerRefreshMode;
    private StringBuffer queryBuf;
    private boolean addWhere;
    private static String KEY_DELIM;
    private static final int ANNOTATION_GROUPING_LEVEL = 1;
    private static final int ANNOTATION_LEVEL = 2;
    private TreeMap<String, TreeMap<GenomeVersion, ?>> organismToVersion;
    private HashMap<String, TreeMap<String, ?>> versionToRootGroupings;
    private HashMap<String, TreeMap<String, ?>> groupingToGroupings;
    private HashMap<String, TreeMap<String, ?>> groupingToAnnotations;
    private HashMap<String, List<Segment>> versionToSegments;
    private HashMap<String, Organism> organismMap;
    private HashMap<String, GenomeVersion> genomeVersionNameMap;
    private HashMap<Integer, Annotation> annotationMap;
    private HashMap<Integer, AnnotationGrouping> annotationGroupingMap;
    
    public static List<UnloadAnnotation> getUnloadedAnnotations(final Session sess, final GenoPubSecurity genoPubSecurity, final GenomeVersion genomeVersion) throws Exception {
        final StringBuffer queryBuf = new StringBuffer();
        queryBuf.append(" SELECT     u  ");
        queryBuf.append(" FROM       UnloadAnnotation as u  ");
        queryBuf.append(" WHERE      u.idGenomeVersion = " + genomeVersion.getIdGenomeVersion());
        if (genoPubSecurity != null && !genoPubSecurity.isAdminRole()) {
            queryBuf.append(" AND u.idUser = " + genoPubSecurity.getIdUser());
        }
        queryBuf.append(" ORDER BY   u.idUnloadAnnotation");
        final List<UnloadAnnotation> results = (List<UnloadAnnotation>)sess.createQuery(queryBuf.toString()).list();
        return results;
    }
    
    public AnnotationQuery() {
        this.scopeLevel = "";
        this.isVisibilityPublic = "Y";
        this.isVisibilityOwner = "Y";
        this.isVisibilityMembers = "Y";
        this.isVisibilityInstitute = "Y";
        this.isServerRefreshMode = "N";
        this.addWhere = true;
        if (this.scopeLevel == null || this.scopeLevel.equals("")) {
            this.scopeLevel = "ALL";
        }
    }
    
    public AnnotationQuery(final HttpServletRequest req) {
        this.scopeLevel = "";
        this.isVisibilityPublic = "Y";
        this.isVisibilityOwner = "Y";
        this.isVisibilityMembers = "Y";
        this.isVisibilityInstitute = "Y";
        this.isServerRefreshMode = "N";
        this.addWhere = true;
        this.scopeLevel = req.getParameter("scopeLevel");
        this.idUserGroup = Util.getIntegerParameter(req, "idUserGroup");
        this.idOrganism = Util.getIntegerParameter(req, "idOrganism");
        this.idGenomeVersion = Util.getIntegerParameter(req, "idGenomeVersion");
        this.isVisibilityOwner = Util.getFlagParameter(req, "isVisibilityOwner");
        this.isVisibilityMembers = Util.getFlagParameter(req, "isVisibilityMembers");
        this.isVisibilityInstitute = Util.getFlagParameter(req, "isVisibilityInstitute");
        this.isVisibilityPublic = Util.getFlagParameter(req, "isVisibilityPublic");
        if (this.scopeLevel == null || this.scopeLevel.equals("")) {
            this.scopeLevel = "ALL";
        }
    }
    
    public Document getAnnotationDocument(final Session sess, final GenoPubSecurity genoPubSecurity) throws Exception {
        StringBuffer queryBuf = this.getAnnotationGroupingQuery(genoPubSecurity);
        Logger.getLogger(this.getClass().getName()).fine("Annotation grouping query: " + queryBuf.toString());
        Query query = sess.createQuery(queryBuf.toString());
        final List<Object[]> annotationGroupingRows = (List<Object[]>)query.list();
        queryBuf = this.getAnnotationQuery(genoPubSecurity);
        Logger.getLogger(this.getClass().getName()).fine("Annotation query: " + queryBuf.toString());
        query = sess.createQuery(queryBuf.toString());
        final List<Object[]> annotationRows = (List<Object[]>)query.list();
        queryBuf = this.getSegmentQuery();
        query = sess.createQuery(queryBuf.toString());
        final List<Segment> segmentRows = (List<Segment>)query.list();
        final Document doc = this.getAnnotationDocument(annotationGroupingRows, annotationRows, segmentRows, DictionaryHelper.getInstance(sess), genoPubSecurity);
        return doc;
    }
    
    public void runAnnotationQuery(final Session sess, final GenoPubSecurity genoPubSecurity, final boolean isServerRefreshMode) throws Exception {
        this.isServerRefreshMode = (isServerRefreshMode ? "Y" : "N");
        StringBuffer queryBuf = this.getAnnotationGroupingQuery(genoPubSecurity);
        Logger.getLogger(this.getClass().getName()).fine("Annotation grouping query: " + queryBuf.toString());
        Query query = sess.createQuery(queryBuf.toString());
        final List<Object[]> annotationGroupingRows = (List<Object[]>)query.list();
        queryBuf = this.getAnnotationQuery(genoPubSecurity);
        Logger.getLogger(this.getClass().getName()).fine("Annotation query: " + queryBuf.toString());
        query = sess.createQuery(queryBuf.toString());
        final List<Object[]> annotationRows = (List<Object[]>)query.list();
        queryBuf = this.getSegmentQuery();
        query = sess.createQuery(queryBuf.toString());
        final List<Segment> segmentRows = (List<Segment>)query.list();
        this.hashAnnotations(annotationGroupingRows, annotationRows, segmentRows, DictionaryHelper.getInstance(sess));
    }
    
    private StringBuffer getAnnotationGroupingQuery(final GenoPubSecurity genoPubSecurity) throws Exception {
        this.addWhere = true;
        (this.queryBuf = new StringBuffer()).append(" SELECT     org, ");
        this.queryBuf.append("            ver, ");
        this.queryBuf.append("            ag, ");
        this.queryBuf.append("            pag ");
        this.queryBuf.append(" FROM       Organism as org ");
        this.queryBuf.append(" JOIN       org.genomeVersions as ver ");
        this.queryBuf.append(" JOIN       ver.annotationGroupings as ag ");
        this.queryBuf.append(" LEFT JOIN  ag.parentAnnotationGrouping as pag ");
        this.addWhere = true;
        this.addCriteria(1);
        this.queryBuf.append(" ORDER BY org.name asc, ver.buildDate desc, ag.name asc ");
        return this.queryBuf;
    }
    
    private StringBuffer getAnnotationQuery(final GenoPubSecurity genoPubSecurity) throws Exception {
        this.addWhere = true;
        (this.queryBuf = new StringBuffer()).append(" SELECT     org, ");
        this.queryBuf.append("            ver, ");
        this.queryBuf.append("            ag, ");
        this.queryBuf.append("            pag, ");
        this.queryBuf.append("            a  ");
        this.queryBuf.append(" FROM       Organism as org ");
        this.queryBuf.append(" JOIN       org.genomeVersions as ver ");
        this.queryBuf.append(" JOIN       ver.annotationGroupings as ag ");
        this.queryBuf.append(" LEFT JOIN  ag.parentAnnotationGrouping as pag ");
        this.queryBuf.append(" LEFT JOIN  ag.annotations as a ");
        this.queryBuf.append(" LEFT JOIN  a.collaborators as collab ");
        this.addWhere = true;
        this.addCriteria(2);
        if (genoPubSecurity != null) {
            this.addWhere = genoPubSecurity.appendAnnotationHQLSecurity(this.scopeLevel, this.queryBuf, "a", "ag", "collab", this.addWhere);
        }
        if (this.isServerRefreshMode.equals("Y")) {
            this.AND();
            this.queryBuf.append("(");
            this.queryBuf.append(" a.isLoaded = 'N' ");
            if (!genoPubSecurity.isAdminRole()) {
                this.AND();
                this.queryBuf.append(" a.idUser = " + genoPubSecurity.getIdUser());
            }
            this.queryBuf.append(")");
        }
        this.queryBuf.append(" ORDER BY org.name asc, ver.buildDate desc, ag.name asc, a.name asc ");
        return this.queryBuf;
    }
    
    private StringBuffer getSegmentQuery() throws Exception {
        this.addWhere = true;
        (this.queryBuf = new StringBuffer()).append(" SELECT     seg  ");
        this.queryBuf.append(" FROM       Segment as seg  ");
        this.queryBuf.append(" ORDER BY   seg.sortOrder");
        return this.queryBuf;
    }
    
    private Document getAnnotationDocument(final List<Object[]> annotationGroupingRows, final List<Object[]> annotationRows, final List<Segment> segmentRows, final DictionaryHelper dictionaryHelper, final GenoPubSecurity genoPubSecurity) throws Exception {
        this.hashAnnotations(annotationGroupingRows, annotationRows, segmentRows, dictionaryHelper);
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Annotations");
        this.fillOrganismNodes(root, dictionaryHelper, genoPubSecurity, true);
        this.fillOrganismNodes(root, dictionaryHelper, genoPubSecurity, false);
        return doc;
    }
    
    private void fillOrganismNodes(final Element root, final DictionaryHelper dictionaryHelper, final GenoPubSecurity genoPubSecurity, final boolean fillPopulatedOrganisms) throws Exception {
        Element organismNode = null;
        Element versionNode = null;
        for (final String organismBinomialName : this.organismToVersion.keySet()) {
            final TreeMap<GenomeVersion, ?> versionMap = this.organismToVersion.get(organismBinomialName);
            final Organism organism = this.organismMap.get(organismBinomialName);
            boolean keep = false;
            final boolean isPopulated = this.hasPopulatedGenomes(organism);
            if (fillPopulatedOrganisms) {
                keep = isPopulated;
            }
            else {
                keep = !isPopulated;
            }
            if (!keep) {
                continue;
            }
            organismNode = organism.getXML(genoPubSecurity).getRootElement();
            organismNode.addAttribute("isPopulated", isPopulated ? "Y" : "N");
            root.add(organismNode);
            if (versionMap != null) {
                for (final GenomeVersion genomeVersion : versionMap.keySet()) {
                    versionNode = genomeVersion.getXML(genoPubSecurity, null).getRootElement();
                    final List<Segment> segments = this.getSegments(organism, genomeVersion.getName());
                    versionNode.addAttribute("hasSegments", (segments != null && segments.size() > 0) ? "Y" : "N");
                    organismNode.add(versionNode);
                    final TreeMap<String, ?> rootGroupings = this.versionToRootGroupings.get(genomeVersion.getName());
                    this.fillGroupingNode(genomeVersion, versionNode, rootGroupings, genoPubSecurity, dictionaryHelper, false);
                    if (this.hasAnnotationCriteria() && !versionNode.hasContent()) {
                        organismNode.remove(versionNode);
                    }
                }
            }
            if (!this.hasAnnotationCriteria() || organismNode.hasContent()) {
                continue;
            }
            root.remove(organismNode);
        }
    }
    
    private boolean hasPopulatedGenomes(final Organism organism) {
        boolean isPopulated = false;
        final TreeMap<GenomeVersion, ?> versionMap = this.organismToVersion.get(organism.getBinomialName());
        if (versionMap != null) {
            for (final GenomeVersion genomeVersion : versionMap.keySet()) {
                if (isPopulated) {
                    break;
                }
                final List<Segment> segments = this.getSegments(organism, genomeVersion.getName());
                if (segments != null && segments.size() > 0) {
                    isPopulated = true;
                    break;
                }
                final TreeMap<String, ?> rootGroupings = this.versionToRootGroupings.get(genomeVersion.getName());
                if (rootGroupings == null || rootGroupings.size() <= 0) {
                    continue;
                }
                for (final String groupingKey : rootGroupings.keySet()) {
                    final TreeMap<String, ?> annotNameMap = this.groupingToAnnotations.get(groupingKey);
                    if (annotNameMap != null && annotNameMap.size() > 0) {
                        isPopulated = true;
                    }
                    final TreeMap<String, ?> childGroupings = this.groupingToGroupings.get(groupingKey);
                    if (childGroupings != null && childGroupings.size() > 0) {
                        isPopulated = true;
                    }
                }
            }
        }
        return isPopulated;
    }
    
    private void hashAnnotations(final List<Object[]> annotationGroupingRows, final List<Object[]> annotationRows, final List<Segment> segmentRows, final DictionaryHelper dictionaryHelper) {
        this.organismToVersion = new TreeMap<String, TreeMap<GenomeVersion, ?>>();
        this.versionToRootGroupings = new HashMap<String, TreeMap<String, ?>>();
        this.groupingToGroupings = new HashMap<String, TreeMap<String, ?>>();
        this.groupingToAnnotations = new HashMap<String, TreeMap<String, ?>>();
        this.versionToSegments = new HashMap<String, List<Segment>>();
        this.organismMap = new HashMap<String, Organism>();
        this.genomeVersionNameMap = new HashMap<String, GenomeVersion>();
        this.annotationGroupingMap = new HashMap<Integer, AnnotationGrouping>();
        this.annotationMap = new HashMap<Integer, Annotation>();
        for (final Organism o : dictionaryHelper.getOrganisms()) {
            this.organismMap.put(o.getBinomialName(), o);
            if (this.idOrganism != null && !this.idOrganism.equals(o.getIdOrganism())) {
                continue;
            }
            final TreeMap<GenomeVersion, ?> versionMap = new TreeMap<GenomeVersion, Object>(new GenomeVersionComparator());
            this.organismToVersion.put(o.getBinomialName(), versionMap);
            if (dictionaryHelper.getGenomeVersions(o.getIdOrganism()) == null) {
                continue;
            }
            for (final GenomeVersion v : dictionaryHelper.getGenomeVersions(o.getIdOrganism())) {
                this.genomeVersionNameMap.put(v.getName(), v);
                if (this.idGenomeVersion != null && !this.idGenomeVersion.equals(v.getIdGenomeVersion())) {
                    continue;
                }
                versionMap.put(v, null);
                final AnnotationGrouping rootGrouping = v.getRootAnnotationGrouping();
                if (rootGrouping == null) {
                    continue;
                }
                final String groupingKey = rootGrouping.getName() + AnnotationQuery.KEY_DELIM + rootGrouping.getIdAnnotationGrouping();
                final TreeMap<String, String> groupings = new TreeMap<String, String>();
                groupings.put(groupingKey, null);
                this.versionToRootGroupings.put(v.getName(), groupings);
            }
        }
        if (segmentRows != null) {
            for (final Segment segment : segmentRows) {
                if (segment == null) {
                    continue;
                }
                final GenomeVersion genomeVersion = dictionaryHelper.getGenomeVersion(segment.getIdGenomeVersion());
                if (genomeVersion == null) {
                    System.out.println("Warning - Segment " + segment.getIdSegment() + " does not belong to a valid Genome Version");
                }
                else {
                    List<Segment> segments = this.versionToSegments.get(genomeVersion.getName());
                    if (segments == null) {
                        segments = new ArrayList<Segment>();
                        this.versionToSegments.put(genomeVersion.getName(), segments);
                    }
                    segments.add(segment);
                }
            }
        }
        for (final Object[] row : annotationGroupingRows) {
            final Organism organism = (Organism)row[0];
            final GenomeVersion genomeVersion2 = (GenomeVersion)row[1];
            final AnnotationGrouping annotGrouping = (AnnotationGrouping)row[2];
            final AnnotationGrouping parentAnnotGrouping = (AnnotationGrouping)row[3];
            TreeMap<GenomeVersion, ?> versionMap2 = this.organismToVersion.get(organism.getBinomialName());
            if (versionMap2 == null) {
                versionMap2 = new TreeMap<GenomeVersion, Object>(new GenomeVersionComparator());
                this.organismToVersion.put(organism.getBinomialName(), versionMap2);
                this.organismMap.put(organism.getBinomialName(), organism);
            }
            if (genomeVersion2 != null) {
                versionMap2.put(genomeVersion2, null);
                this.genomeVersionNameMap.put(genomeVersion2.getName(), genomeVersion2);
            }
            final String groupingKey2 = annotGrouping.getName() + AnnotationQuery.KEY_DELIM + annotGrouping.getIdAnnotationGrouping();
            if (parentAnnotGrouping == null) {
                TreeMap<String, ?> groupingNameMap = this.versionToRootGroupings.get(genomeVersion2.getName());
                if (groupingNameMap == null) {
                    groupingNameMap = new TreeMap<String, Object>();
                    this.versionToRootGroupings.put(genomeVersion2.getName(), groupingNameMap);
                }
                groupingNameMap.put(groupingKey2, null);
            }
            else {
                final String parentGroupingKey = parentAnnotGrouping.getName() + AnnotationQuery.KEY_DELIM + parentAnnotGrouping.getIdAnnotationGrouping();
                TreeMap<String, ?> childGroupingNameMap = this.groupingToGroupings.get(parentGroupingKey);
                if (childGroupingNameMap == null) {
                    childGroupingNameMap = new TreeMap<String, Object>();
                    this.groupingToGroupings.put(parentGroupingKey, childGroupingNameMap);
                }
                childGroupingNameMap.put(groupingKey2, null);
            }
            this.annotationGroupingMap.put(annotGrouping.getIdAnnotationGrouping(), annotGrouping);
        }
        for (final Object[] row : annotationRows) {
            final Organism organism = (Organism)row[0];
            final GenomeVersion genomeVersion2 = (GenomeVersion)row[1];
            final AnnotationGrouping annotGrouping = (AnnotationGrouping)row[2];
            final AnnotationGrouping parentAnnotGrouping = (AnnotationGrouping)row[3];
            final Annotation annot = (Annotation)row[4];
            if (annot != null) {
                annot.loadProps(dictionaryHelper);
            }
            TreeMap<GenomeVersion, ?> versionMap3 = this.organismToVersion.get(organism.getBinomialName());
            if (versionMap3 == null) {
                versionMap3 = new TreeMap<GenomeVersion, Object>(new GenomeVersionComparator());
                this.organismToVersion.put(organism.getBinomialName(), versionMap3);
            }
            if (genomeVersion2 != null) {
                versionMap3.put(genomeVersion2, null);
            }
            if (annotGrouping != null) {
                final String groupingKey3 = annotGrouping.getName() + AnnotationQuery.KEY_DELIM + annotGrouping.getIdAnnotationGrouping();
                if (parentAnnotGrouping == null) {
                    TreeMap<String, ?> groupingNameMap2 = this.versionToRootGroupings.get(genomeVersion2.getName());
                    if (groupingNameMap2 == null) {
                        groupingNameMap2 = new TreeMap<String, Object>();
                        this.versionToRootGroupings.put(genomeVersion2.getName(), groupingNameMap2);
                    }
                    groupingNameMap2.put(groupingKey3, null);
                }
                else {
                    final String parentGroupingKey2 = parentAnnotGrouping.getName() + AnnotationQuery.KEY_DELIM + parentAnnotGrouping.getIdAnnotationGrouping();
                    TreeMap<String, ?> childGroupingNameMap2 = this.groupingToGroupings.get(parentGroupingKey2);
                    if (childGroupingNameMap2 == null) {
                        childGroupingNameMap2 = new TreeMap<String, Object>();
                        this.groupingToGroupings.put(parentGroupingKey2, childGroupingNameMap2);
                    }
                    childGroupingNameMap2.put(groupingKey3, null);
                }
                this.annotationGroupingMap.put(annotGrouping.getIdAnnotationGrouping(), annotGrouping);
                if (annot == null) {
                    continue;
                }
                TreeMap<String, ?> annotNameMap = this.groupingToAnnotations.get(groupingKey3);
                if (annotNameMap == null) {
                    annotNameMap = new TreeMap<String, Object>();
                    this.groupingToAnnotations.put(groupingKey3, annotNameMap);
                }
                final String annotKey = annot.getName() + AnnotationQuery.KEY_DELIM + annot.getIdAnnotation();
                annotNameMap.put(annotKey, null);
                this.annotationMap.put(annot.getIdAnnotation(), annot);
            }
        }
    }
    
    public GenomeVersion getGenomeVersion(final String genomeVersionName) {
        return this.genomeVersionNameMap.get(genomeVersionName);
    }
    
    public HashMap<String, GenomeVersion> getGenomeVersionNameMap() {
        return this.genomeVersionNameMap;
    }
    
    public Set<Organism> getOrganisms() {
        final TreeSet<Organism> organisms = new TreeSet<Organism>(new OrganismComparator());
        for (final String organismBinomialName : this.organismToVersion.keySet()) {
            final Organism organism = this.organismMap.get(organismBinomialName);
            organisms.add(organism);
        }
        return organisms;
    }
    
    public Set<String> getVersionNames(final Organism organism) {
        final Set<String> versionNames = new TreeSet<String>();
        final TreeMap<GenomeVersion, ?> versionMap = this.organismToVersion.get(organism.getBinomialName());
        if (versionMap != null) {
            for (final GenomeVersion version : versionMap.keySet()) {
                versionNames.add(version.getName());
            }
        }
        return versionNames;
    }
    
    public List<Segment> getSegments(final Organism organism, final String genomeVersionName) {
        List<Segment> segments = null;
        final TreeMap<GenomeVersion, ?> versionMap = this.organismToVersion.get(organism.getBinomialName());
        if (versionMap != null) {
            for (final GenomeVersion genomeVersion : versionMap.keySet()) {
                if (genomeVersion.getName().equals(genomeVersionName)) {
                    segments = this.versionToSegments.get(genomeVersion.getName());
                    break;
                }
            }
        }
        return segments;
    }
    
    public List<QualifiedAnnotation> getQualifiedAnnotations(final Organism organism, final String genomeVersionName) {
        final List<QualifiedAnnotation> qualifiedAnnotations = new ArrayList<QualifiedAnnotation>();
        final TreeMap<GenomeVersion, ?> versionMap = this.organismToVersion.get(organism.getBinomialName());
        if (versionMap != null) {
            for (final GenomeVersion genomeVersion : versionMap.keySet()) {
                if (genomeVersion.getName().equals(genomeVersionName)) {
                    final TreeMap<String, ?> rootGroupingNameMap = this.versionToRootGroupings.get(genomeVersion.getName());
                    final String qualifiedName = "";
                    this.getQualifiedAnnotation(rootGroupingNameMap, qualifiedAnnotations, qualifiedName, false);
                }
            }
        }
        return qualifiedAnnotations;
    }
    
    private void getQualifiedAnnotation(final TreeMap<String, ?> theGroupings, final List<QualifiedAnnotation> qualifiedAnnotations, final String typePrefix, final boolean showGroupingLevel) {
        if (theGroupings != null) {
            for (final String groupingKey : theGroupings.keySet()) {
                final String[] tokens = groupingKey.split(AnnotationQuery.KEY_DELIM);
                final String groupingName = tokens[0];
                final TreeMap<String, ?> annotNameMap = this.groupingToAnnotations.get(groupingKey);
                if (annotNameMap != null) {
                    for (final String annotNameKey : annotNameMap.keySet()) {
                        final String[] tokens2 = annotNameKey.split(AnnotationQuery.KEY_DELIM);
                        final Integer idAnnotation = new Integer(tokens2[1]);
                        final Annotation annot = this.annotationMap.get(idAnnotation);
                        String fullTypePrefix = this.concatenateTypePrefix(typePrefix, groupingName, showGroupingLevel);
                        if (fullTypePrefix != null && fullTypePrefix.length() > 0) {
                            fullTypePrefix += "/";
                        }
                        qualifiedAnnotations.add(new QualifiedAnnotation(annot, fullTypePrefix + annot.getName(), fullTypePrefix + annot.getName()));
                    }
                }
                final TreeMap<String, ?> childGroupings = this.groupingToGroupings.get(groupingKey);
                if (childGroupings != null) {
                    this.getQualifiedAnnotation(childGroupings, qualifiedAnnotations, this.concatenateTypePrefix(typePrefix, groupingName, showGroupingLevel), true);
                }
            }
        }
    }
    
    private String concatenateTypePrefix(final String typePrefix, final String groupingName, final boolean showGroupingLevel) {
        if (!showGroupingLevel) {
            return (typePrefix != null) ? typePrefix : "";
        }
        if (typePrefix == null || typePrefix.equals("")) {
            return groupingName;
        }
        return typePrefix + "/" + groupingName;
    }
    
    private void fillGroupingNode(final GenomeVersion genomeVersion, final Element parentNode, final TreeMap<String, ?> theGroupings, final GenoPubSecurity genoPubSecurity, final DictionaryHelper dictionaryHelper, final boolean showGroupingLevel) throws Exception {
        if (theGroupings != null) {
            for (final String groupingKey : theGroupings.keySet()) {
                final String[] tokens = groupingKey.split(AnnotationQuery.KEY_DELIM);
                final String groupingName = tokens[0];
                final Integer idAnnotationGrouping = new Integer(tokens[1]);
                Element groupingNode = null;
                AnnotationGrouping annotGrouping = null;
                if (showGroupingLevel) {
                    annotGrouping = this.annotationGroupingMap.get(idAnnotationGrouping);
                    groupingNode = annotGrouping.getXML(genoPubSecurity, dictionaryHelper).getRootElement();
                    parentNode.add(groupingNode);
                }
                else {
                    groupingNode = parentNode;
                }
                final TreeMap<String, ?> annotNameMap = this.groupingToAnnotations.get(groupingKey);
                if (annotNameMap != null && annotNameMap.size() > 0) {
                    groupingNode.addAttribute("annotationCount", String.valueOf(annotNameMap.size()));
                    for (final String annotNameKey : annotNameMap.keySet()) {
                        final String[] tokens2 = annotNameKey.split(AnnotationQuery.KEY_DELIM);
                        final Integer idAnnotation = Integer.valueOf(tokens2[1]);
                        final Annotation annot = this.annotationMap.get(idAnnotation);
                        final Element annotNode = annot.getXML(genoPubSecurity, dictionaryHelper, null).getRootElement();
                        annotNode.addAttribute("idAnnotationGrouping", (annotGrouping != null) ? annotGrouping.getIdAnnotationGrouping().toString() : "");
                        groupingNode.add(annotNode);
                    }
                }
                final TreeMap<String, ?> childGroupings = this.groupingToGroupings.get(groupingKey);
                this.fillGroupingNode(genomeVersion, groupingNode, childGroupings, genoPubSecurity, dictionaryHelper, true);
                if (!groupingNode.hasContent()) {
                    final String folderIdUserGroup = groupingNode.attributeValue("idUserGroup");
                    boolean prune = true;
                    if (groupingNode.getName().equals("AnnotationGrouping") && (folderIdUserGroup == null || folderIdUserGroup.equals(""))) {
                        prune = (this.idUserGroup != null);
                    }
                    else {
                        prune = (!groupingNode.getName().equals("AnnotationGrouping") || folderIdUserGroup == null || folderIdUserGroup.equals("") || !genoPubSecurity.belongsToGroup(new Integer(folderIdUserGroup)) || (this.idUserGroup != null && !this.idUserGroup.equals(new Integer(folderIdUserGroup))));
                    }
                    if (!prune) {
                        continue;
                    }
                    parentNode.remove(groupingNode);
                }
            }
        }
    }
    
    private boolean hasAnnotationCriteria() {
        return this.idUserGroup != null || this.hasVisibilityCriteria();
    }
    
    private boolean hasVisibilityCriteria() {
        return (!this.isVisibilityOwner.equals("Y") || !this.isVisibilityMembers.equals("Y") || !this.isVisibilityInstitute.equals("Y") || !this.isVisibilityPublic.equals("Y")) && (!this.isVisibilityOwner.equals("N") || !this.isVisibilityMembers.equals("N") || !this.isVisibilityInstitute.equals("N") || !this.isVisibilityPublic.equals("N"));
    }
    
    private void addCriteria(final int joinLevel) {
        if (this.idOrganism != null) {
            this.AND();
            this.queryBuf.append(" org.idOrganism = ");
            this.queryBuf.append(this.idOrganism);
        }
        if (this.idGenomeVersion != null) {
            this.AND();
            this.queryBuf.append(" ver.idGenomeVersion = ");
            this.queryBuf.append(this.idGenomeVersion);
        }
        if (this.idUserGroup != null) {
            this.AND();
            this.queryBuf.append("(");
            if (joinLevel == 2) {
                this.queryBuf.append(" a.idUserGroup = " + this.idUserGroup);
            }
            else if (joinLevel == 1) {
                this.queryBuf.append(" ag.idUserGroup = " + this.idUserGroup);
                this.queryBuf.append(" OR ");
                this.queryBuf.append(" ag.idUserGroup is NULL");
            }
            this.queryBuf.append(")");
        }
        if (joinLevel == 2 && this.hasVisibilityCriteria()) {
            this.AND();
            int count = 0;
            this.queryBuf.append(" a.codeVisibility in (");
            if (this.isVisibilityOwner.equals("Y")) {
                this.queryBuf.append("'OWNER'");
                ++count;
            }
            if (this.isVisibilityMembers.equals("Y")) {
                if (count > 0) {
                    this.queryBuf.append(", ");
                }
                this.queryBuf.append("'MEM'");
                ++count;
            }
            if (this.isVisibilityInstitute.equals("Y")) {
                if (count > 0) {
                    this.queryBuf.append(", ");
                }
                this.queryBuf.append("'INST'");
                ++count;
            }
            if (this.isVisibilityPublic.equals("Y")) {
                if (count > 0) {
                    this.queryBuf.append(", ");
                }
                this.queryBuf.append("'PUBLIC'");
                ++count;
            }
            this.queryBuf.append(")");
        }
    }
    
    protected boolean AND() {
        if (this.addWhere) {
            this.queryBuf.append(" WHERE ");
            this.addWhere = false;
        }
        else {
            this.queryBuf.append(" AND ");
        }
        return this.addWhere;
    }
    
    public String getIsVisibilityPublic() {
        return this.isVisibilityPublic;
    }
    
    public void setIsVisibilityPublic(final String isVisibilityPublic) {
        this.isVisibilityPublic = isVisibilityPublic;
    }
    
    public String getIsVisibilityMembers() {
        return this.isVisibilityMembers;
    }
    
    public void setIsVisibilityMembers(final String isVisibilityMembers) {
        this.isVisibilityMembers = isVisibilityMembers;
    }
    
    public String getIsVisibilityInstitute() {
        return this.isVisibilityInstitute;
    }
    
    public void setIsVisibilityInstitute(final String isVisibilityInstitute) {
        this.isVisibilityInstitute = isVisibilityInstitute;
    }
    
    public String getIsVisibilityOwner() {
        return this.isVisibilityOwner;
    }
    
    public void setIsVisibilityOwner(final String isVisibilityOwner) {
        this.isVisibilityOwner = isVisibilityOwner;
    }
    
    static {
        AnnotationQuery.KEY_DELIM = "!!!!";
    }
}
