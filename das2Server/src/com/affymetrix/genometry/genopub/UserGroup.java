// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Set;
import java.io.Serializable;

public class UserGroup implements Serializable
{
    private Integer idUserGroup;
    private String name;
    private String contact;
    private String email;
    private Set members;
    private Set collaborators;
    private Set managers;
    private Set institutes;
    
    public Integer getIdUserGroup() {
        return this.idUserGroup;
    }
    
    public void setIdUserGroup(final Integer isUserGroup) {
        this.idUserGroup = isUserGroup;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Set getMembers() {
        return this.members;
    }
    
    public void setMembers(final Set members) {
        this.members = members;
    }
    
    public Set getCollaborators() {
        return this.collaborators;
    }
    
    public void setCollaborators(final Set collaborators) {
        this.collaborators = collaborators;
    }
    
    public Set getManagers() {
        return this.managers;
    }
    
    public void setManagers(final Set managers) {
        this.managers = managers;
    }
    
    public Set getInstitutes() {
        return this.institutes;
    }
    
    public void setInstitutes(final Set institutes) {
        this.institutes = institutes;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getContact() {
        return this.contact;
    }
    
    public void setContact(final String contact) {
        this.contact = contact;
    }
}
