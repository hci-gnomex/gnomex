// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.List;
import java.util.Iterator;
import java.sql.Date;
import java.util.Set;
import java.io.Serializable;

public class AnnotationGrouping implements Serializable
{
    private Integer idAnnotationGrouping;
    private String name;
    private String description;
    private Integer idParentAnnotationGrouping;
    private AnnotationGrouping parentAnnotationGrouping;
    private Set annotationGroupings;
    private Set annotations;
    private Integer idUserGroup;
    private Integer idGenomeVersion;
    private String createdBy;
    private Date createDate;
    
    public Integer getIdAnnotationGrouping() {
        return this.idAnnotationGrouping;
    }
    
    public void setIdAnnotationGrouping(final Integer idAnnotationGrouping) {
        this.idAnnotationGrouping = idAnnotationGrouping;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Set getAnnotationGroupings() {
        return this.annotationGroupings;
    }
    
    public void setAnnotationGroupings(final Set annotationGroupings) {
        this.annotationGroupings = annotationGroupings;
    }
    
    public Integer getIdParentAnnotationGrouping() {
        return this.idParentAnnotationGrouping;
    }
    
    public void setIdParentAnnotationGrouping(final Integer idParentAnnotationGrouping) {
        this.idParentAnnotationGrouping = idParentAnnotationGrouping;
    }
    
    public Set getAnnotations() {
        return this.annotations;
    }
    
    public void setAnnotations(final Set annotations) {
        this.annotations = annotations;
    }
    
    public AnnotationGrouping getParentAnnotationGrouping() {
        return this.parentAnnotationGrouping;
    }
    
    public void setParentAnnotationGrouping(final AnnotationGrouping parentAnnotationGrouping) {
        this.parentAnnotationGrouping = parentAnnotationGrouping;
    }
    
    public boolean hasVisibility(final String codeVisibility) {
        boolean hasVisibility = false;
        final Iterator<?> i = this.annotations.iterator();
        while (i.hasNext()) {
            final Annotation a = Annotation.class.cast(i.next());
            if (a.getCodeVisibility().equals(codeVisibility)) {
                hasVisibility = true;
                break;
            }
        }
        return hasVisibility;
    }
    
    public Integer getIdGenomeVersion() {
        return this.idGenomeVersion;
    }
    
    public void setIdGenomeVersion(final Integer idGenomeVersion) {
        this.idGenomeVersion = idGenomeVersion;
    }
    
    public Integer getIdUserGroup() {
        return this.idUserGroup;
    }
    
    public void setIdUserGroup(final Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }
    
    public String getQualifiedTypeName() {
        if (this.getIdParentAnnotationGrouping() == null) {
            return "";
        }
        String typeName = this.getName();
        typeName = this.recurseGetParentNameExcludingRoot(typeName);
        return typeName;
    }
    
    public String getQualifiedName() {
        String qualifiedName = this.getName();
        qualifiedName = this.recurseGetParentName(qualifiedName);
        return qualifiedName;
    }
    
    private String recurseGetParentName(String qualifiedName) {
        final AnnotationGrouping parent = this.getParentAnnotationGrouping();
        if (parent != null && parent.getName() != null) {
            qualifiedName = parent.getName() + "/" + qualifiedName;
            qualifiedName = parent.recurseGetParentName(qualifiedName);
        }
        return qualifiedName;
    }
    
    private String recurseGetParentNameExcludingRoot(String typeName) {
        final AnnotationGrouping parent = this.getParentAnnotationGrouping();
        if (parent != null && parent.getName() != null && parent.getIdParentAnnotationGrouping() != null) {
            typeName = parent.getName() + "/" + typeName;
            typeName = parent.recurseGetParentNameExcludingRoot(typeName);
        }
        return typeName;
    }
    
    public void recurseGetChildren(final List<Object> descendents) {
        Iterator<?> i = this.getAnnotationGroupings().iterator();
        while (i.hasNext()) {
            final AnnotationGrouping ag = AnnotationGrouping.class.cast(i.next());
            descendents.add(ag);
            ag.recurseGetChildren(descendents);
        }
        i = this.getAnnotations().iterator();
        while (i.hasNext()) {
            final Annotation a = Annotation.class.cast(i.next());
            descendents.add(a);
        }
    }
    
    public String getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }
    
    public Document getXML(final GenoPubSecurity genoPubSecurity, final DictionaryHelper dictionaryHelper) {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("AnnotationGrouping");
        final GenomeVersion genomeVersion = dictionaryHelper.getGenomeVersion(this.getIdGenomeVersion());
        root.addAttribute("label", this.getName());
        root.addAttribute("idAnnotationGrouping", this.getIdAnnotationGrouping().toString());
        root.addAttribute("idGenomeVersion", genomeVersion.getIdGenomeVersion().toString());
        root.addAttribute("genomeVersion", genomeVersion.getName());
        root.addAttribute("name", this.getName().toString());
        root.addAttribute("description", (this.getDescription() != null) ? this.getDescription() : "");
        root.addAttribute("userGroup", dictionaryHelper.getUserGroupName(this.getIdUserGroup()));
        root.addAttribute("idUserGroup", (this.getIdUserGroup() != null) ? this.getIdUserGroup().toString() : "");
        root.addAttribute("createdBy", (this.getCreatedBy() != null) ? this.getCreatedBy() : "");
        root.addAttribute("createDate", (this.getCreateDate() != null) ? Util.formatDate(this.getCreateDate()) : "");
        root.addAttribute("canWrite", genoPubSecurity.canWrite(this) ? "Y" : "N");
        return doc;
    }
}
