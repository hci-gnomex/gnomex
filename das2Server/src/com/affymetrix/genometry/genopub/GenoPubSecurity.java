//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.logging.Logger;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import com.affymetrix.genometryImpl.AnnotSecurity;

public class GenoPubSecurity implements AnnotSecurity, Serializable
{
    public static final String SESSION_KEY = "GenoPubSecurity";
    public static final String ADMIN_ROLE = "admin";
    public static final String USER_ROLE = "user";
    public static final String GUEST_ROLE = "guest";
    public static final String USER_SCOPE_LEVEL = "USER";
    public static final String GROUP_SCOPE_LEVEL = "GROUP";
    public static final String ALL_SCOPE_LEVEL = "ALL";
    private boolean scrutinizeAccess;
    private User user;
    private boolean isAdminRole;
    private boolean isGuestRole;
    private boolean isFDTSupported;
    private final HashMap<Integer, UserGroup> groupsMemCollabVisibility;
    private final HashMap<Integer, UserGroup> groupsMemVisibility;
    private final HashMap<Integer, Institute> institutesVisibility;
    private final HashMap<String, HashMap<Integer, QualifiedAnnotation>> versionToAuthorizedAnnotationMap;
    private Map<String, GenomeVersion> versionNameToVersionMap;
    private String baseURL;

    public void setBaseURL(final String fullURL, final String servletPath, final String pathInfo) {
        this.baseURL = "";
        final String extraPath = servletPath + pathInfo;
        final int pos = fullURL.lastIndexOf(extraPath);
        if (pos > 0) {
            this.baseURL = fullURL.substring(0, pos);
        }
    }

    public String getBaseURL() {
        return this.baseURL;
    }

    public GenoPubSecurity(final Session sess, final String userName, final boolean scrutinizeAccess, final boolean isAdminRole, final boolean isGuestRole, final boolean isFDTSupported) throws Exception {
        this.scrutinizeAccess = false;
        this.isAdminRole = false;
        this.isGuestRole = true;
        this.isFDTSupported = false;
        this.groupsMemCollabVisibility = new HashMap<Integer, UserGroup>();
        this.groupsMemVisibility = new HashMap<Integer, UserGroup>();
        this.institutesVisibility = new HashMap<Integer, Institute>();
        this.versionToAuthorizedAnnotationMap = new HashMap<String, HashMap<Integer, QualifiedAnnotation>>();
        this.versionNameToVersionMap = new HashMap<String, GenomeVersion>();
        this.isFDTSupported = isFDTSupported;
        this.scrutinizeAccess = scrutinizeAccess;
        if (this.scrutinizeAccess) {
            final List<User> users = (List<User>)sess.createQuery("SELECT u from User as u where u.userName = '" + userName + "'").list();
            if (users == null || users.size() == 0) {
                throw new Exception("Cannot find user " + userName);
            }
            this.user = users.get(0);
            this.isAdminRole = isAdminRole;
            this.isGuestRole = isGuestRole;
            for (final UserGroup sc : (Set<UserGroup>) this.user.getMemberUserGroups()) {
                this.groupsMemCollabVisibility.put(sc.getIdUserGroup(), sc);
                this.groupsMemVisibility.put(sc.getIdUserGroup(), sc);
                for (final Institute i : (Set<Institute>) sc.getInstitutes()) {
                    this.institutesVisibility.put(i.getIdInstitute(), i);
                }
            }
            for (final UserGroup sc : (Set<UserGroup>) this.user.getManagingUserGroups()) {
                this.groupsMemCollabVisibility.put(sc.getIdUserGroup(), sc);
                this.groupsMemVisibility.put(sc.getIdUserGroup(), sc);
                for (final Institute i : (Set<Institute>) sc.getInstitutes()) {
                    this.institutesVisibility.put(i.getIdInstitute(), i);
                }
            }
            for (final UserGroup sc : (Set<UserGroup>) this.user.getCollaboratingUserGroups()) {
                this.groupsMemCollabVisibility.put(sc.getIdUserGroup(), sc);
            }
            this.loadAuthorizedResources(sess);
        }
    }

    public Document getXML() {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("GenoPubSecurity");
        root.addAttribute("userName", (this.user != null) ? this.user.getUserName() : "");
        root.addAttribute("userDisplayName", (this.user != null) ? this.user.getUserDisplayName() : "");
        root.addAttribute("name", (this.user != null) ? this.user.getName() : "");
        root.addAttribute("isAdmin", this.isAdminRole ? "Y" : "N");
        root.addAttribute("isGuest", this.isGuestRole ? "Y" : "N");
        root.addAttribute("isFDTSupported", this.isFDTSupported ? "Y" : "N");
        root.addAttribute("canManageUsers", (this.isAdminRole || (this.user != null && this.user.getManagingUserGroups().size() > 0)) ? "Y" : "N");
        return doc;
    }

    public boolean belongsToGroup(final Integer idUserGroup) {
        return this.isMember(idUserGroup) || this.isCollaborator(idUserGroup) || this.isManager(idUserGroup);
    }

    public boolean belongsToGroup(final UserGroup group) {
        return this.isMember(group) || this.isCollaborator(group) || this.isManager(group);
    }

    public boolean isMember(final UserGroup group) {
        return this.isMember(group.getIdUserGroup());
    }

    public boolean belongsToInstitute(final Integer idInstitute) {
        boolean isMyInstitute = false;
        if (idInstitute != null) {
            isMyInstitute = this.institutesVisibility.containsKey(idInstitute);
        }
        return isMyInstitute;
    }

    public boolean isMember(final Integer idUserGroup) {
        if (!this.scrutinizeAccess) {
            return false;
        }
        boolean isMember = false;
        for (final UserGroup g : (Set<UserGroup>) this.user.getMemberUserGroups()) {
            if (g.getIdUserGroup().equals(idUserGroup)) {
                isMember = true;
                break;
            }
        }
        return isMember;
    }

    public boolean isCollaborator(final UserGroup group) {
        return this.isCollaborator(group.getIdUserGroup());
    }

    public boolean isCollaborator(final Integer idUserGroup) {
        if (!this.scrutinizeAccess) {
            return false;
        }
        boolean isCollaborator = false;
        for (final UserGroup g : (Set<UserGroup>) this.user.getCollaboratingUserGroups()) {
            if (g.getIdUserGroup().equals(idUserGroup)) {
                isCollaborator = true;
                break;
            }
        }
        return isCollaborator;
    }

    public boolean isManager(final UserGroup group) {
        return this.isManager(group.getIdUserGroup());
    }

    public boolean isManager(final Integer idUserGroup) {
        if (!this.scrutinizeAccess) {
            return false;
        }
        boolean isManager = false;
        if (this.isAdminRole) {
            isManager = true;
        }
        else {
            for (final UserGroup g : (Set<UserGroup>) this.user.getManagingUserGroups()) {
                if (g.getIdUserGroup().equals(idUserGroup)) {
                    isManager = true;
                    break;
                }
            }
        }
        return isManager;
    }

    public UserGroup getDefaultUserGroup() {
        if (!this.scrutinizeAccess) {
            return null;
        }
        UserGroup defaultUserGroup = null;
        if (this.user.getManagingUserGroups() != null && this.user.getManagingUserGroups().size() > 0) {
            defaultUserGroup = UserGroup.class.cast(this.user.getManagingUserGroups().iterator().next());
        }
        else if (this.user.getMemberUserGroups() != null && this.user.getMemberUserGroups().size() > 0) {
            defaultUserGroup = UserGroup.class.cast(this.user.getMemberUserGroups().iterator().next());
        }
        else if (this.user.getCollaboratingUserGroups() != null && this.user.getCollaboratingUserGroups().size() > 0) {
            defaultUserGroup = UserGroup.class.cast(this.user.getCollaboratingUserGroups().iterator().next());
        }
        return defaultUserGroup;
    }

    public boolean canRead(final Object object) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        boolean canRead = false;
        if (this.isAdminRole) {
            canRead = true;
        }
        else if (object instanceof Annotation) {
            final Annotation a = Annotation.class.cast(object);
            for (final User collaborator : (Set<User>) a.getCollaborators()) {
                if (this.user.getIdUser().equals(collaborator.getIdUser())) {
                    canRead = true;
                    break;
                }
            }
            if (!canRead) {
                if (a.getCodeVisibility().equals("PUBLIC")) {
                    canRead = true;
                }
                else if (a.getCodeVisibility().equals("MEM")) {
                    if (this.isMember(a.getIdUserGroup()) || this.isManager(a.getIdUserGroup())) {
                        canRead = true;
                    }
                }
                else if (a.getCodeVisibility().equals("MEMCOL")) {
                    if (this.belongsToGroup(a.getIdUserGroup())) {
                        canRead = true;
                    }
                }
                else if (a.getCodeVisibility().equals("INST")) {
                    if (this.belongsToInstitute(a.getIdInstitute())) {
                        canRead = true;
                    }
                }
                else if (a.getIdUser() != null && a.getIdUser().equals(this.user.getIdUser())) {
                    canRead = true;
                }
                else if (this.isManager(a.getIdUserGroup())) {
                    canRead = true;
                }
            }
        }
        else if (object instanceof AnnotationGrouping) {
            final AnnotationGrouping ag = AnnotationGrouping.class.cast(object);
            for (final Annotation a2 : (Set<Annotation>) ag.getAnnotationGroupings()) {
                for (final User collaborator2 : (Set<User>) a2.getCollaborators()) {
                    if (this.user.getIdUser().equals(collaborator2.getIdUser())) {
                        canRead = true;
                        break;
                    }
                }
            }
            if (!canRead) {
                if (ag.hasVisibility("PUBLIC")) {
                    canRead = true;
                }
                else if (ag.hasVisibility("MEM")) {
                    for (final Annotation a2 : (Set<Annotation>) ag.getAnnotationGroupings()) {
                        if (this.isMember(a2.getIdUserGroup()) || this.isManager(a2.getIdUserGroup())) {
                            canRead = true;
                            break;
                        }
                    }
                }
                else if (ag.hasVisibility("MEMCOL")) {
                    for (final Annotation a2 : (Set<Annotation>) ag.getAnnotationGroupings()) {
                        if (this.belongsToGroup(a2.getIdUserGroup())) {
                            canRead = true;
                            break;
                        }
                    }
                }
                else if (ag.hasVisibility("INST")) {
                    for (final Annotation a2 : (Set<Annotation>) ag.getAnnotationGroupings()) {
                        if (this.belongsToInstitute(a2.getIdInstitute())) {
                            canRead = true;
                            break;
                        }
                    }
                }
                else if (this.belongsToGroup(ag.getIdUserGroup())) {
                    canRead = true;
                }
            }
        }
        else {
            canRead = true;
        }
        return canRead;
    }

    public boolean canWrite(final Object object) {
        if (!this.scrutinizeAccess) {
            return false;
        }
        boolean canWrite = false;
        if (this.isAdminRole) {
            canWrite = true;
        }
        else if (object instanceof Owned) {
            final Owned o = Owned.class.cast(object);
            if (o.isOwner(this.user.getIdUser())) {
                canWrite = true;
            }
            if (!canWrite && object instanceof Annotation) {
                final Annotation a = Annotation.class.cast(object);
                if (this.isManager(a.getIdUserGroup())) {
                    canWrite = true;
                }
            }
        }
        else if (object instanceof AnnotationGrouping) {
            final AnnotationGrouping ag = AnnotationGrouping.class.cast(object);
            if (this.isMember(ag.getIdUserGroup()) || this.isManager(ag.getIdUserGroup())) {
                canWrite = true;
            }
        }
        else if (object instanceof UserGroup) {
            final UserGroup g = (UserGroup)object;
            if (this.isManager(g)) {
                canWrite = true;
            }
        }
        return canWrite;
    }

    public boolean appendAnnotationHQLSecurity(final String scopeLevel, final StringBuffer queryBuf, final String annotationAlias, final String annotationGroupingAlias, final String collaboratorAlias, boolean addWhere) throws Exception {
        if (!this.scrutinizeAccess) {
            return addWhere;
        }
        if (this.isAdminRole) {
            return addWhere;
        }
        if (this.isGuestRole) {
            addWhere = this.AND(addWhere, queryBuf);
            queryBuf.append("(");
            queryBuf.append(annotationAlias + ".codeVisibility = '" + "PUBLIC" + "'");
            queryBuf.append(")");
        }
        else if (scopeLevel.equals("USER")) {
            addWhere = this.AND(addWhere, queryBuf);
            queryBuf.append("(");
            this.appendUserOwnedHQLSecurity(queryBuf, annotationAlias, annotationGroupingAlias, addWhere);
            addWhere = this.OR(addWhere, queryBuf);
            this.appendEmptyAnnotationGroupingHQLSecurity(queryBuf, annotationAlias, annotationGroupingAlias, addWhere);
            queryBuf.append(")");
        }
        else {
            if (!scopeLevel.equals("GROUP") && !scopeLevel.equals("ALL")) {
                throw new Exception("invalid scope level " + scopeLevel);
            }
            addWhere = this.AND(addWhere, queryBuf);
            if (this.groupsMemCollabVisibility.isEmpty() && !scopeLevel.equals("ALL")) {
                this.appendUserOwnedHQLSecurity(queryBuf, annotationAlias, annotationGroupingAlias, addWhere);
            }
            else {
                boolean hasSecurityCriteria = false;
                queryBuf.append("(");
                if (!this.groupsMemVisibility.isEmpty()) {
                    queryBuf.append("(");
                    queryBuf.append(annotationAlias + ".codeVisibility in ('" + "MEM" + "', '" + "PUBLIC" + "')");
                    addWhere = this.AND(addWhere, queryBuf);
                    queryBuf.append(annotationAlias + ".idUserGroup ");
                    this.appendIdInStatement(queryBuf, this.groupsMemVisibility);
                    queryBuf.append(")");
                    hasSecurityCriteria = true;
                }
                if (!this.groupsMemCollabVisibility.isEmpty()) {
                    if (hasSecurityCriteria) {
                        addWhere = this.OR(addWhere, queryBuf);
                    }
                    queryBuf.append("(");
                    queryBuf.append(annotationAlias + ".codeVisibility = '" + "MEMCOL" + "'");
                    addWhere = this.AND(addWhere, queryBuf);
                    queryBuf.append(annotationAlias + ".idUserGroup ");
                    this.appendIdInStatement(queryBuf, this.groupsMemCollabVisibility);
                    queryBuf.append(")");
                    hasSecurityCriteria = true;
                }
                if (!this.institutesVisibility.isEmpty()) {
                    if (hasSecurityCriteria) {
                        addWhere = this.OR(addWhere, queryBuf);
                    }
                    queryBuf.append("(");
                    queryBuf.append(annotationAlias + ".codeVisibility in ('" + "INST" + "')");
                    addWhere = this.AND(addWhere, queryBuf);
                    queryBuf.append(annotationAlias + ".idInstitute ");
                    this.appendIdInStatement(queryBuf, this.institutesVisibility);
                    queryBuf.append(")");
                    hasSecurityCriteria = true;
                }
                if (annotationGroupingAlias != null && !this.groupsMemCollabVisibility.isEmpty()) {
                    if (hasSecurityCriteria) {
                        addWhere = this.OR(addWhere, queryBuf);
                    }
                    this.appendEmptyAnnotationGroupingHQLSecurity(queryBuf, annotationAlias, annotationGroupingAlias, addWhere);
                    hasSecurityCriteria = true;
                }
                if (hasSecurityCriteria) {
                    addWhere = this.OR(addWhere, queryBuf);
                }
                this.appendUserOwnedHQLSecurity(queryBuf, annotationAlias, annotationGroupingAlias, addWhere);
                hasSecurityCriteria = true;
                if (hasSecurityCriteria) {
                    addWhere = this.OR(addWhere, queryBuf);
                }
                queryBuf.append("(");
                queryBuf.append(collaboratorAlias + ".idUser = " + this.user.getIdUser());
                queryBuf.append(")");
                hasSecurityCriteria = true;
                if (scopeLevel.equals("ALL")) {
                    if (hasSecurityCriteria) {
                        addWhere = this.OR(addWhere, queryBuf);
                    }
                    queryBuf.append("(");
                    queryBuf.append(annotationAlias + ".codeVisibility = '" + "PUBLIC" + "'");
                    queryBuf.append(")");
                }
                queryBuf.append(")");
            }
        }
        return addWhere;
    }

    private void appendUserOwnedHQLSecurity(final StringBuffer queryBuf, final String annotationAlias, final String annotationGroupingAlias, boolean addWhere) throws Exception {
        queryBuf.append("(");
        queryBuf.append(annotationAlias + ".idUser = " + this.user.getIdUser());
        if (this.user.getManagingUserGroups().size() > 0) {
            addWhere = this.OR(addWhere, queryBuf);
            queryBuf.append(annotationAlias + ".idUserGroup in (");
            boolean firstTime = true;
            for (final UserGroup group : (Set<UserGroup>) this.user.getManagingUserGroups()) {
                if (!firstTime) {
                    queryBuf.append(",");
                }
                queryBuf.append(group.getIdUserGroup());
                firstTime = false;
            }
            queryBuf.append(")");
        }
        queryBuf.append(")");
    }

    private boolean appendEmptyAnnotationGroupingHQLSecurity(final StringBuffer queryBuf, final String annotationAlias, final String annotationGroupingAlias, boolean addWhere) throws Exception {
        queryBuf.append("(");
        queryBuf.append(annotationAlias + ".idAnnotation is NULL");
        addWhere = this.AND(addWhere, queryBuf);
        queryBuf.append("(");
        queryBuf.append(annotationGroupingAlias + ".idUserGroup is NULL");
        if (this.groupsMemCollabVisibility.size() > 0) {
            addWhere = this.OR(addWhere, queryBuf);
            queryBuf.append(annotationGroupingAlias + ".idUserGroup");
            this.appendIdInStatement(queryBuf, this.groupsMemCollabVisibility);
        }
        queryBuf.append(")");
        queryBuf.append(")");
        return addWhere;
    }

    private void appendIdInStatement(final StringBuffer queryBuf, final HashMap idMap) {
        queryBuf.append(" in (");
        final Iterator i = idMap.keySet().iterator();
        while (i.hasNext()) {
            final Integer id = (Integer) i.next();
            queryBuf.append(id);
            if (i.hasNext()) {
                queryBuf.append(",");
            }
        }
        queryBuf.append(")");
    }

    protected boolean AND(boolean addWhere, final StringBuffer queryBuf) {
        if (addWhere) {
            queryBuf.append(" WHERE ");
            addWhere = false;
        }
        else {
            queryBuf.append(" AND ");
        }
        return addWhere;
    }

    protected boolean OR(boolean addWhere, final StringBuffer queryBuf) {
        if (addWhere) {
            queryBuf.append(" WHERE ");
            addWhere = false;
        }
        else {
            queryBuf.append(" OR ");
        }
        return addWhere;
    }

    public boolean isAdminRole() {
        return this.isAdminRole;
    }

    public void setAdminRole(final boolean isAdminRole) {
        this.isAdminRole = isAdminRole;
    }

    public boolean isGuestRole() {
        return this.isGuestRole;
    }

    public String getUserName() {
        if (this.user != null) {
            return this.user.getUserName();
        }
        return "";
    }

    public Integer getIdUser() {
        if (this.user != null) {
            return this.user.getIdUser();
        }
        return null;
    }

    public void loadAuthorizedResources(final Session sess) throws Exception {
        if (!this.scrutinizeAccess) {
            return;
        }
        if (!this.versionToAuthorizedAnnotationMap.isEmpty()) {
            this.versionToAuthorizedAnnotationMap.clear();
        }
        final AnnotationQuery annotationQuery = new AnnotationQuery();
        annotationQuery.runAnnotationQuery(sess, this, false);
        this.versionNameToVersionMap = annotationQuery.getGenomeVersionNameMap();
        for (final Organism organism : annotationQuery.getOrganisms()) {
            for (final String genomeVersionName : annotationQuery.getVersionNames(organism)) {
                HashMap<Integer, QualifiedAnnotation> annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
                if (annotationMap == null) {
                    annotationMap = new HashMap<Integer, QualifiedAnnotation>();
                    this.versionToAuthorizedAnnotationMap.put(genomeVersionName, annotationMap);
                }
                for (final QualifiedAnnotation qa : annotationQuery.getQualifiedAnnotations(organism, genomeVersionName)) {
                    annotationMap.put(qa.getAnnotation().getIdAnnotation(), qa);
                }
            }
        }
    }

    public boolean isAuthorized(final String genomeVersionName, final String annotationName, final Object annotationId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (annotationId == null) {
            Logger.getLogger(GenoPubSecurity.class.getName()).warning("Unable to find annotation id for " + annotationName + ".  Blocking access.");
        }
        final Map annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
        return annotationMap.containsKey(annotationId);
    }

    public Map<String, Object> getProperties(final String genomeVersionName, final String annotationName, final Object annotationId) {
        if (!this.scrutinizeAccess) {
            return null;
        }
        if (!this.isAuthorized(genomeVersionName, annotationName, annotationId)) {
            return null;
        }
        final Map<Integer, QualifiedAnnotation> annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
        final QualifiedAnnotation qa = annotationMap.get(annotationId);
        final Map<String, Object> props = qa.getAnnotation().getProperties();
        if (!props.containsKey("url")) {
            props.put("url", this.baseURL + "/" + "genopub" + "?idAnnotation=" + annotationId);
        }
        return props;
    }

    public boolean isBarGraphData(final String data_root, final String genomeVersionName, final String annotationName, final Object annotationId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, annotationName, annotationId)) {
            return false;
        }
        final Map<Integer, QualifiedAnnotation> annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
        final QualifiedAnnotation qa = annotationMap.get(annotationId);
        try {
            return qa.getAnnotation().isBarGraphData(data_root);
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean isUseqGraphData(final String data_root, final String genomeVersionName, final String annotationName, final Object annotationId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, annotationName, annotationId)) {
            return false;
        }
        final Map<Integer, QualifiedAnnotation> annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
        final QualifiedAnnotation qa = annotationMap.get(annotationId);
        try {
            return qa.getAnnotation().isUseqGraphData(data_root);
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean isBamData(final String data_root, final String genomeVersionName, final String annotationName, final Object annotationId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, annotationName, annotationId)) {
            return false;
        }
        final Map<Integer, QualifiedAnnotation> annotationMap = this.versionToAuthorizedAnnotationMap.get(genomeVersionName);
        final QualifiedAnnotation qa = annotationMap.get(annotationId);
        try {
            return qa.getAnnotation().isBamData(data_root);
        }
        catch (Exception e) {
            return false;
        }
    }

    public String getSequenceDirectory(final String data_root, final AnnotatedSeqGroup genome) throws Exception {
        if (!this.scrutinizeAccess) {
            return data_root + genome.getOrganism() + "/" + genome.getID() + "/dna/";
        }
        final GenomeVersion genomeVersion = this.versionNameToVersionMap.get(genome.getID());
        if (genomeVersion == null) {
            throw new Exception("Cannot find genome version " + genome.getID() + " in genome version map");
        }
        return data_root + "SEQ" + genomeVersion.getIdGenomeVersion().toString() + "/";
    }
}
