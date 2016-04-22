//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.Iterator;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

public class DictionaryHelper
{
    private static DictionaryHelper theDictionaryHelper;
    private boolean isLoaded;
    private final HashMap<Integer, Property> propertyMap;
    private final List<Property> propertyList;
    private final HashMap<Integer, Organism> organismMap;
    private final List<Organism> organismList;
    private final HashMap<Integer, GenomeVersion> genomeVersionMap;
    private final List<GenomeVersion> genomeVersionList;
    private final HashMap<Integer, List<GenomeVersion>> organismToGenomeVersionMap;
    private final HashMap<Integer, UserGroup> groupMap;
    private final List<UserGroup> groupList;
    private final HashMap<Integer, User> userMap;
    private final List<User> userList;
    private final List<Visibility> visibilityList;
    private final HashMap<Integer, Institute> instituteMap;
    private final List<Institute> instituteList;

    public DictionaryHelper() {
        this.isLoaded = false;
        this.propertyMap = new HashMap<Integer, Property>();
        this.propertyList = new ArrayList<Property>();
        this.organismMap = new HashMap<Integer, Organism>();
        this.organismList = new ArrayList<Organism>();
        this.genomeVersionMap = new HashMap<Integer, GenomeVersion>();
        this.genomeVersionList = new ArrayList<GenomeVersion>();
        this.organismToGenomeVersionMap = new HashMap<Integer, List<GenomeVersion>>();
        this.groupMap = new HashMap<Integer, UserGroup>();
        this.groupList = new ArrayList<UserGroup>();
        this.userMap = new HashMap<Integer, User>();
        this.userList = new ArrayList<User>();
        this.visibilityList = new ArrayList<Visibility>();
        this.instituteMap = new HashMap<Integer, Institute>();
        this.instituteList = new ArrayList<Institute>();
    }

    public static DictionaryHelper getInstance(final Session sess) {
        if (!DictionaryHelper.theDictionaryHelper.isLoaded) {
            DictionaryHelper.theDictionaryHelper.load(sess);
        }
        return DictionaryHelper.theDictionaryHelper;
    }

    public static DictionaryHelper reload(final Session sess) {
        DictionaryHelper.theDictionaryHelper.propertyMap.clear();
        DictionaryHelper.theDictionaryHelper.propertyList.clear();
        DictionaryHelper.theDictionaryHelper.organismMap.clear();
        DictionaryHelper.theDictionaryHelper.organismList.clear();
        DictionaryHelper.theDictionaryHelper.genomeVersionMap.clear();
        DictionaryHelper.theDictionaryHelper.genomeVersionList.clear();
        DictionaryHelper.theDictionaryHelper.organismToGenomeVersionMap.clear();
        DictionaryHelper.theDictionaryHelper.groupMap.clear();
        DictionaryHelper.theDictionaryHelper.groupList.clear();
        DictionaryHelper.theDictionaryHelper.userMap.clear();
        DictionaryHelper.theDictionaryHelper.userList.clear();
        DictionaryHelper.theDictionaryHelper.visibilityList.clear();
        DictionaryHelper.theDictionaryHelper.instituteMap.clear();
        DictionaryHelper.theDictionaryHelper.instituteList.clear();
        DictionaryHelper.theDictionaryHelper.load(sess);
        return DictionaryHelper.theDictionaryHelper;
    }

    private void load(final Session sess) {
        final List<Property> properties = (List<Property>)sess.createQuery("SELECT p from Property p order by p.sortOrder, p.name").list();
        for (final Property p : properties) {
            Hibernate.initialize((Object)p.getOptions());
            this.propertyMap.put(p.getIdProperty(), p);
            this.propertyList.add(p);
        }
        final List<Visibility> visibilities = (List<Visibility>)sess.createQuery("SELECT d from Visibility d order by d.name").list();
        for (final Visibility d : visibilities) {
            this.visibilityList.add(d);
        }
        final List<Organism> organisms = (List<Organism>)sess.createQuery("SELECT d from Organism d order by d.binomialName").list();
        for (final Organism d2 : organisms) {
            this.organismMap.put(d2.getIdOrganism(), d2);
            this.organismList.add(d2);
        }
        final List<GenomeVersion> genomeVersions = (List<GenomeVersion>)sess.createQuery("SELECT d from GenomeVersion d order by d.buildDate desc, d.name asc").list();
        for (final GenomeVersion d3 : genomeVersions) {
            this.genomeVersionMap.put(d3.getIdGenomeVersion(), d3);
            this.genomeVersionList.add(d3);
            List<GenomeVersion> versions = this.organismToGenomeVersionMap.get(d3.getIdOrganism());
            if (versions == null) {
                versions = new ArrayList<GenomeVersion>();
                this.organismToGenomeVersionMap.put(d3.getIdOrganism(), versions);
            }
            versions.add(d3);
        }
        final List<Institute> institutes = (List<Institute>)sess.createQuery("SELECT i from Institute i order by i.name").list();
        for (final Institute i : institutes) {
            this.instituteMap.put(i.getIdInstitute(), i);
            this.instituteList.add(i);
        }
        final List<UserGroup> groups = (List<UserGroup>)sess.createQuery("SELECT d from UserGroup d order by d.name").list();
        for (final UserGroup d4 : groups) {
            this.groupMap.put(d4.getIdUserGroup(), d4);
            this.groupList.add(d4);
        }
        final List<User> users = (List<User>)sess.createQuery("SELECT d from User d order by d.lastName, d.firstName, d.middleName").list();
        for (final User d5 : users) {
            this.userMap.put(d5.getIdUser(), d5);
            this.userList.add(d5);
        }
        this.isLoaded = true;
    }

    public Document getXML(final GenoPubSecurity genoPubSecurity) {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Dictionaries");
        final Element dictEdit = root.addElement("Dictionary");
        dictEdit.addAttribute("dictionaryName", "Property");
        dictEdit.addAttribute("dictionaryDisplayName", "Property");
        dictEdit.addAttribute("label", "Properites");
        Element dict = root.addElement("Properties");
        this.makeBlankNode(dict, "Property", "name", "New property...");
        for (final Property p : this.propertyList) {
            final Element dictEntry = dictEdit.addElement("DictionaryEntry");
            dictEntry.addAttribute("dictionaryName", "Property");
            dictEntry.addAttribute("dictionaryDisplayName", "Property");
            dictEntry.addAttribute("id", p.getIdProperty().toString());
            dictEntry.addAttribute("name", p.getName());
            dictEntry.addAttribute("label", p.getName());
            dictEntry.addAttribute("isActive", p.getIsActive());
            dictEntry.addAttribute("sortOrder", (p.getSortOrder() != null) ? p.getSortOrder().toString() : "");
            dictEntry.addAttribute("codePropertyType", p.getCodePropertyType());
            dictEntry.addAttribute("type", "DictionaryEntry");
            dictEntry.addAttribute("canWrite", genoPubSecurity.canWrite(p) ? "Y" : "N");
            dictEntry.addAttribute("idUser", (p.getIdUser() != null) ? p.getIdUser().toString() : "");
            dictEntry.addAttribute("owner", this.getUserFullName(p.getIdUser()));
            if (p.getOptions() != null && p.getOptions().size() > 0) {
                for (final PropertyOption o : (Set<PropertyOption>) p.getOptions()) {
                    final Element option = dictEntry.addElement("PropertyOption");
                    option.addAttribute("idPropertyOption", o.getIdPropertyOption().toString());
                    option.addAttribute("name", o.getName());
                    option.addAttribute("label", o.getName());
                    option.addAttribute("isActive", o.getIsActive());
                    option.addAttribute("sortOrder", (o.getSortOrder() != null) ? o.getSortOrder().toString() : "");
                }
            }
            final Element de = (Element)dictEntry.clone();
            de.setName("Property");
            de.addAttribute("idProperty", p.getIdProperty().toString());
            dict.add(de);
        }
        dict = root.addElement("Visibilities");
        this.makeBlankNode(dict, "Visibility");
        for (final Visibility d : this.visibilityList) {
            final Element dictEntry = dict.addElement("Visibility");
            dictEntry.addAttribute("id", d.getCodeVisibility());
            dictEntry.addAttribute("name", d.getName());
        }
        dict = root.addElement("GenomeVersions");
        this.makeBlankNode(dict, "GenomeVersion", "name", "Genome version...");
        for (final GenomeVersion d2 : this.genomeVersionList) {
            final Element dictEntry = dict.addElement("GenomeVersion");
            dictEntry.addAttribute("id", d2.getIdGenomeVersion().toString());
            dictEntry.addAttribute("name", d2.getName());
            dictEntry.addAttribute("idOrganism", d2.getIdOrganism().toString());
        }
        dict = root.addElement("Organisms");
        this.makeBlankNode(dict, "Organism", "binomialName", "Species...");
        for (final Organism d3 : this.organismList) {
            final Element dictEntry = dict.addElement("Organism");
            dictEntry.addAttribute("id", d3.getIdOrganism().toString());
            dictEntry.addAttribute("name", d3.getName());
            dictEntry.addAttribute("binomialName", d3.getBinomialName());
            dictEntry.addAttribute("commonName", d3.getCommonName());
            this.makeBlankNode(dictEntry, "GenomeVersion", "name", "Genome version...");
            if (this.getGenomeVersions(d3.getIdOrganism()) != null) {
                for (final GenomeVersion gv : this.getGenomeVersions(d3.getIdOrganism())) {
                    final Element de2 = dictEntry.addElement("GenomeVersion");
                    de2.addAttribute("id", gv.getIdGenomeVersion().toString());
                    de2.addAttribute("name", gv.getName());
                    de2.addAttribute("idOrganism", gv.getIdOrganism().toString());
                }
            }
        }
        dict = root.addElement("UserGroups");
        final Element blank = this.makeBlankNode(dict, "UserGroup", "promptedName", "User group...");
        blank.addAttribute("isPartOf", "N");
        for (final UserGroup d4 : this.groupList) {
            final Element dictEntry2 = dict.addElement("UserGroup");
            dictEntry2.addAttribute("id", d4.getIdUserGroup().toString());
            dictEntry2.addAttribute("name", d4.getName());
            dictEntry2.addAttribute("promptedName", d4.getName());
            dictEntry2.addAttribute("isPartOf", (genoPubSecurity.isAdminRole() || genoPubSecurity.belongsToGroup(d4)) ? "Y" : "N");
            dictEntry2.addAttribute("isMemberOf", (genoPubSecurity.isAdminRole() || genoPubSecurity.isMember(d4)) ? "Y" : "N");
            dictEntry2.addAttribute("isManagerOf", (genoPubSecurity.isAdminRole() || genoPubSecurity.isManager(d4)) ? "Y" : "N");
            dictEntry2.addAttribute("isCollaboratorOf", (genoPubSecurity.isAdminRole() || genoPubSecurity.isCollaborator(d4)) ? "Y" : "N");
            final Element membersNode = dictEntry2.addElement("Members");
            this.makeBlankNode(membersNode, "User");
            for (final User member : (Set<User>) d4.getMembers()) {
                final Element memberNode = membersNode.addElement("User");
                memberNode.addAttribute("id", member.getIdUser().toString());
                memberNode.addAttribute("name", member.getName());
            }
            final Element collaboratorsNode = dictEntry2.addElement("Collaborators");
            this.makeBlankNode(collaboratorsNode, "User");
            for (final User member2 : (Set<User>)d4.getCollaborators()) {
                final Element memberNode2 = collaboratorsNode.addElement("User");
                memberNode2.addAttribute("id", member2.getIdUser().toString());
                memberNode2.addAttribute("name", member2.getName());
            }
            final Element managersNode = dictEntry2.addElement("Managers");
            this.makeBlankNode(managersNode, "User");
            for (final User member3 : (Set<User>)d4.getManagers()) {
                final Element memberNode3 = managersNode.addElement("User");
                memberNode3.addAttribute("id", member3.getIdUser().toString());
                memberNode3.addAttribute("name", member3.getName());
            }
        }
        dict = root.addElement("Users");
        this.makeBlankNode(dict, "User");
        for (final User d5 : (Set<User>)this.userList) {
            final Element dictEntry2 = dict.addElement("User");
            dictEntry2.addAttribute("id", d5.getIdUser().toString());
            dictEntry2.addAttribute("name", d5.getName());
        }
        return doc;
    }

    private Element makeBlankNode(final Element parentNode, final String name) {
        final Element node = parentNode.addElement(name);
        node.addAttribute("id", "");
        node.addAttribute("name", "");
        return node;
    }

    private Element makeBlankNode(final Element parentNode, final String name, final String displayAttributeName, final String display) {
        final Element node = parentNode.addElement(name);
        node.addAttribute("id", "");
        node.addAttribute(displayAttributeName, display);
        if (!displayAttributeName.equals("name")) {
            node.addAttribute("name", "");
        }
        return node;
    }

    public List<Organism> getOrganisms() {
        return this.organismList;
    }

    public List<GenomeVersion> getGenomeVersions(final Integer idOrganism) {
        return this.organismToGenomeVersionMap.get(idOrganism);
    }

    public String getUserFullName(final Integer idUser) {
        final User user = this.userMap.get(idUser);
        if (user != null) {
            return user.getName();
        }
        return "";
    }

    public String getUserEmail(final Integer idUser) {
        final User user = this.userMap.get(idUser);
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    public String getUserInstitute(final Integer idUser) {
        final User user = this.userMap.get(idUser);
        if (user != null) {
            return user.getInstitute();
        }
        return null;
    }

    public String getOrganismName(final Integer idOrganism) {
        final Organism organism = this.organismMap.get(idOrganism);
        if (organism != null) {
            return organism.getName();
        }
        return "";
    }

    public String getOrganismName(final GenomeVersion genomeVersion) {
        if (genomeVersion == null || genomeVersion.getIdOrganism() == null) {
            return "";
        }
        final Organism organism = this.organismMap.get(genomeVersion.getIdOrganism());
        if (organism != null) {
            return organism.getName();
        }
        return "";
    }

    public String getOrganismBinomialName(final Integer idOrganism) {
        final Organism organism = this.organismMap.get(idOrganism);
        if (organism != null) {
            return organism.getBinomialName();
        }
        return "";
    }

    public String getOrganismBinomialName(final GenomeVersion genomeVersion) {
        if (genomeVersion == null || genomeVersion.getIdOrganism() == null) {
            return "";
        }
        final Organism organism = this.organismMap.get(genomeVersion.getIdOrganism());
        if (organism != null) {
            return organism.getBinomialName();
        }
        return "";
    }

    public String getGenomeVersionName(final Integer idGenomeVersion) {
        final GenomeVersion genomeVersion = this.genomeVersionMap.get(idGenomeVersion);
        if (genomeVersion != null) {
            return genomeVersion.getName();
        }
        return "";
    }

    public GenomeVersion getGenomeVersion(final Integer idGenomeVersion) {
        final GenomeVersion genomeVersion = this.genomeVersionMap.get(idGenomeVersion);
        return genomeVersion;
    }

    public String getInstituteName(final Integer idInstitute) {
        final Institute institute = this.instituteMap.get(idInstitute);
        if (institute != null) {
            return institute.getName();
        }
        return "";
    }

    public String getUserGroupName(final Integer idUserGroup) {
        final UserGroup group = this.groupMap.get(idUserGroup);
        if (group != null) {
            return group.getName();
        }
        return "";
    }

    public String getUserGroupContact(final Integer idUserGroup) {
        final UserGroup group = this.groupMap.get(idUserGroup);
        if (group != null) {
            return group.getContact();
        }
        return null;
    }

    public String getUserGroupEmail(final Integer idUserGroup) {
        final UserGroup group = this.groupMap.get(idUserGroup);
        if (group != null) {
            return group.getEmail();
        }
        return null;
    }

    public List<Property> getPropertyList() {
        return this.propertyList;
    }

    static {
        DictionaryHelper.theDictionaryHelper = new DictionaryHelper();
    }
}
