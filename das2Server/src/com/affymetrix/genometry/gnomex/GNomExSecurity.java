// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.gnomex;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.logging.Logger;
import java.util.Iterator;
import hci.gnomex.model.Organism;
import hci.gnomex.utility.DataTrackQuery;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import hci.gnomex.utility.PropertyDictionaryHelper;
import org.hibernate.Session;
import hci.gnomex.model.GenomeBuild;
import java.util.Map;
import hci.gnomex.utility.QualifiedDataTrack;
import java.util.HashMap;
import hci.gnomex.security.SecurityAdvisor;
import java.io.Serializable;
import com.affymetrix.genometryImpl.AnnotSecurity;

public class GNomExSecurity implements AnnotSecurity, Serializable
{
    public static final String SESSION_KEY = "GNomExSecurity";
    private boolean scrutinizeAccess;
    private SecurityAdvisor secAdvisor;
    private final HashMap<String, HashMap<Integer, QualifiedDataTrack>> buildToAuthorizedDataTrackMap;
    private Map<String, GenomeBuild> buildNameToVersionMap;
    private String dataTrackInfoURL;
    private String analysis_root_dir;
    
    public void setDataTrackInfoURL(final String serverName, String portNumber) {
        if (portNumber == null) {
            portNumber = "";
        }
        else if (!portNumber.equals("")) {
            portNumber = ":" + portNumber;
        }
        final String gnomexFlexApp = this.secAdvisor.isGuest() ? "gnomexGuestFlex.jsp" : "gnomexFlex.jsp";
        this.dataTrackInfoURL = "https://" + serverName + portNumber + "/gnomex/" + gnomexFlexApp;
    }
    
    public GNomExSecurity(final Session sess, final String serverName, final SecurityAdvisor secAdvisor, final boolean scrutinizeAccess) throws Exception {
        this.scrutinizeAccess = false;
        this.buildToAuthorizedDataTrackMap = new HashMap<String, HashMap<Integer, QualifiedDataTrack>>();
        this.buildNameToVersionMap = new HashMap<String, GenomeBuild>();
        this.secAdvisor = secAdvisor;
        this.scrutinizeAccess = scrutinizeAccess;
        if (this.scrutinizeAccess) {
            this.loadAuthorizedResources(sess);
        }
        this.analysis_root_dir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
    }
    
    public Document getXML() {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("GenoPubSecurity");
        root.addAttribute("userName", (this.secAdvisor.getUID() != null) ? this.secAdvisor.getUID() : "");
        root.addAttribute("userDisplayName", (this.secAdvisor.getAppUser() != null) ? this.secAdvisor.getAppUser().getDisplayName() : "");
        root.addAttribute("name", (this.secAdvisor.getAppUser() != null) ? this.secAdvisor.getAppUser().getDisplayName() : "");
        root.addAttribute("isAdmin", this.secAdvisor.hasPermission("canAdministerAllCoreFacilities") ? "Y" : "N");
        root.addAttribute("isGuest", this.secAdvisor.getIsGuest());
        root.addAttribute("canManageUsers", (this.secAdvisor.hasPermission("canAdministerAllCoreFacilities") || (this.secAdvisor.getAppUser() != null && this.secAdvisor.getGroupsIManage().size() > 0)) ? "Y" : "N");
        return doc;
    }
    
    public void loadAuthorizedResources(final Session sess) throws Exception {
        if (!this.scrutinizeAccess) {
            return;
        }
        if (!this.buildToAuthorizedDataTrackMap.isEmpty()) {
            this.buildToAuthorizedDataTrackMap.clear();
        }
        final DataTrackQuery dataTrackQuery = new DataTrackQuery();
        dataTrackQuery.runDataTrackQuery(sess, this.secAdvisor, false);
        this.buildNameToVersionMap = (Map<String, GenomeBuild>)dataTrackQuery.getGenomeBuildNameMap();
        for (final Organism organism : dataTrackQuery.getOrganisms()) {
            for (final String genomeVersionName : dataTrackQuery.getGenomeBuildNames(organism)) {
                HashMap<Integer, QualifiedDataTrack> dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
                if (dataTrackMap == null) {
                    dataTrackMap = new HashMap<Integer, QualifiedDataTrack>();
                    this.buildToAuthorizedDataTrackMap.put(genomeVersionName, dataTrackMap);
                }
                for (final QualifiedDataTrack qa : dataTrackQuery.getQualifiedDataTracks(organism, genomeVersionName)) {
                    dataTrackMap.put(qa.getDataTrack().getIdDataTrack(), qa);
                }
            }
        }
    }
    
    public SecurityAdvisor getSecAdvisor() {
        return this.secAdvisor;
    }
    
    public boolean isAuthorized(final String genomeVersionName, final String dataTrackName, final Object dataTrackId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (dataTrackId == null) {
            Logger.getLogger(GNomExSecurity.class.getName()).warning("Unable to find dataTrack id for " + dataTrackName + ".  Blocking access.");
        }
        final Map dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
        return dataTrackMap != null && dataTrackMap.containsKey(dataTrackId);
    }
    
    public Map<String, Object> getProperties(final String genomeVersionName, final String dataTrackName, final Object dataTrackId) {
        if (!this.scrutinizeAccess) {
            return null;
        }
        if (!this.isAuthorized(genomeVersionName, dataTrackName, dataTrackId)) {
            return null;
        }
        final Map<Integer, QualifiedDataTrack> dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
        final QualifiedDataTrack qa = dataTrackMap.get(dataTrackId);
        final Map<String, Object> props = (Map<String, Object>)qa.getDataTrack().getProperties();
        if (!props.containsKey("url")) {
            final String gnomexDataTrackURL = this.dataTrackInfoURL + "?dataTrackNumber=" + qa.getDataTrack().getNumber();
            props.put("url", gnomexDataTrackURL);
        }
        return props;
    }
    
    public boolean isBarGraphData(final String data_root, final String genomeVersionName, final String dataTrackName, final Object dataTrackId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, dataTrackName, dataTrackId)) {
            return false;
        }
        final Map<Integer, QualifiedDataTrack> dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
        final QualifiedDataTrack qa = dataTrackMap.get(dataTrackId);
        try {
            return qa.getDataTrack().isBarGraphData(data_root, this.analysis_root_dir);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean isUseqGraphData(final String data_root, final String genomeVersionName, final String dataTrackName, final Object dataTrackId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, dataTrackName, dataTrackId)) {
            return false;
        }
        final Map<Integer, QualifiedDataTrack> dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
        final QualifiedDataTrack qa = dataTrackMap.get(dataTrackId);
        try {
            return qa.getDataTrack().isUseqGraphData(data_root, this.analysis_root_dir);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean isBamData(final String data_root, final String genomeVersionName, final String dataTrackName, final Object dataTrackId) {
        if (!this.scrutinizeAccess) {
            return true;
        }
        if (!this.isAuthorized(genomeVersionName, dataTrackName, dataTrackId)) {
            return false;
        }
        final Map<Integer, QualifiedDataTrack> dataTrackMap = this.buildToAuthorizedDataTrackMap.get(genomeVersionName);
        final QualifiedDataTrack qa = dataTrackMap.get(dataTrackId);
        try {
            return qa.getDataTrack().isBamData(data_root, this.analysis_root_dir);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public String getSequenceDirectory(final String data_root, final AnnotatedSeqGroup genome) throws Exception {
        if (!this.scrutinizeAccess) {
            return data_root + genome.getOrganism() + "/" + genome.getID() + "/dna/";
        }
        final GenomeBuild genomeVersion = this.buildNameToVersionMap.get(genome.getID());
        if (genomeVersion == null) {
            throw new Exception("Cannot find genome build " + genome.getID() + " in genome build map");
        }
        return data_root + "SEQ" + genomeVersion.getIdGenomeBuild().toString() + "/";
    }
    
    public boolean isGuestRole() {
        return this.secAdvisor.isGuest();
    }
}
