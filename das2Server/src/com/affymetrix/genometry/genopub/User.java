// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Set;
import java.io.Serializable;

public class User implements Serializable
{
    public static final String MASKED_PASSWORD = "XXXX";
    private Integer idUser;
    private String firstName;
    private String lastName;
    private String middleName;
    private String userName;
    private String password;
    private String email;
    private String institute;
    private String ucscUrl;
    private Set memberUserGroups;
    private Set collaboratingUserGroups;
    private Set managingUserGroups;
    private Set roles;
    
    public Integer getIdUser() {
        return this.idUser;
    }
    
    public void setIdUser(final Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }
    
    public String getName() {
        final StringBuffer name = new StringBuffer();
        if (this.getLastName() != null && !this.getLastName().equals("")) {
            name.append(this.getLastName());
        }
        if (this.getFirstName() != null && !this.getFirstName().equals("")) {
            if (name.length() > 0) {
                name.append(", ");
            }
            name.append(this.getFirstName());
        }
        if (this.getMiddleName() != null && !this.getMiddleName().equals("")) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(this.getMiddleName());
        }
        return name.toString();
    }
    
    public String getUserDisplayName() {
        final StringBuffer name = new StringBuffer();
        if (this.getFirstName() != null && !this.getFirstName().equals("")) {
            name.append(this.getFirstName());
        }
        if (this.getMiddleName() != null && !this.getMiddleName().equals("")) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(this.getMiddleName());
        }
        if (this.getLastName() != null && !this.getLastName().equals("")) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(this.getLastName());
        }
        return name.toString();
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getPasswordDisplay() {
        if (this.password != null && !this.password.equals("")) {
            return "XXXX";
        }
        return "";
    }
    
    public Set getMemberUserGroups() {
        return this.memberUserGroups;
    }
    
    public void setMemberUserGroups(final Set memberUserGroups) {
        this.memberUserGroups = memberUserGroups;
    }
    
    public Set getCollaboratingUserGroups() {
        return this.collaboratingUserGroups;
    }
    
    public void setCollaboratingUserGroups(final Set collaboratingUserGroups) {
        this.collaboratingUserGroups = collaboratingUserGroups;
    }
    
    public Set getManagingUserGroups() {
        return this.managingUserGroups;
    }
    
    public void setManagingUserGroups(final Set managingUserGroups) {
        this.managingUserGroups = managingUserGroups;
    }
    
    public Set getRoles() {
        return this.roles;
    }
    
    public void setRoles(final Set roles) {
        this.roles = roles;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getInstitute() {
        return this.institute;
    }
    
    public void setInstitute(final String institute) {
        this.institute = institute;
    }
    
    public String getUcscUrl() {
        return this.ucscUrl;
    }
    
    public void setUcscUrl(final String ucscUrl) {
        this.ucscUrl = ucscUrl;
    }
}
